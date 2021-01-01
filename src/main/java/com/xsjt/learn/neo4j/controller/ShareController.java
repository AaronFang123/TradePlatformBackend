package com.xsjt.learn.neo4j.controller;

import com.xsjt.learn.neo4j.dao.ShareItemActionsRespository;
import com.xsjt.learn.neo4j.model.Count;
import com.xsjt.learn.neo4j.model.Result;
import com.xsjt.learn.neo4j.model.ShareItemRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 */
@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    ShareItemActionsRespository repository;

    // 返回当前节点的信息
    @RequestMapping("/code_info")
    public Result queryValidNode(@RequestParam("invite_code") String inviteCode) {
        if (inviteCode == null) {
            return new Result(40001,"参数不能为空");
        }
        ShareItemRelationship parent = repository.findNodeByInviteCode(inviteCode);
        if (parent == null) {
            return new Result(50001, "邀请码无效");
        }
        return new Result(201, "查询成功", parent);
    }

    @RequestMapping("/share_id")
    public Result queryBySharedId(@RequestParam("share_id") String shareId) {
        if (shareId == null) {
            return new Result(40001,"参数不能为空");
        }
        List<ShareItemRelationship> shareItemRelationships = repository.queryByShareId(shareId);
        if (shareItemRelationships == null || shareItemRelationships.size() == 0) {
            return new Result(50001, "邀请码无效");
        }
        for (ShareItemRelationship relationship : shareItemRelationships) {
            if (relationship.getUserType() == 2) {
                return new Result(50002, "交易已经完成，无法进行");
            }
        }
        return new Result(202, "查询成功", shareItemRelationships);
    }

    @RequestMapping("/uid")
    public Result queryByUid(@RequestParam("uid") String uid) {
        if (uid == null) {
            return new Result(40001,"参数不能为空");
        }

        List<ShareItemRelationship> shareItemRelationships = repository.queryByUid(uid);
        if (shareItemRelationships == null || shareItemRelationships.size() == 0) {
            return new Result(50001, "uid无效或未查询到记录");
        }
        return new Result(203, "查询成功", shareItemRelationships);
    }

    @RequestMapping("/invite")
    public Result inviteNewUser(@RequestParam("invite_code") String inviteCode,
                                @RequestParam("share_id") String shareId ,
                                @RequestParam("user_name") String userName,
                                @RequestParam("user_id") String uid,
                                @RequestParam("user_type") Integer userType) {
        if (inviteCode == null || shareId == null || userName == null || uid == null) {
            return new Result(40001,"参数不能为空");
        }

        // 创建新的订单
        if (userType == 0) {
            ShareItemRelationship newNode = repository.createNewNode(shareId,
                    userType, userName,
                    inviteCode, uid);
            return new Result(200, "添加成功", newNode);
        }
        else{
            // 0. 判断是否有效
            ShareItemRelationship info = repository.findNodeByInviteCode(inviteCode);
            if (info == null) {
                return new Result(50001, "邀请码无效");
            }
            System.out.println(info.toString());
            // 条件 1. 根节点下所有的userType都不能是2，如果有2的说明交易已经完成
            // 条件 2. 除了根节点全部都是1
            // 验证阶段
            // 1. 根据shareId找到根节点
            ShareItemRelationship root = repository.getRootByShareId(shareId);
            if (root.getUserType() != 0) {
                return new Result(50001,"数据异常");
            }
            // 2. 查询所有的子节点
            List<ShareItemRelationship> childList = repository.findChildListInviteCode(root.getInviteCode());
            // 3. 对每个子节点进行判断
            for (ShareItemRelationship child : childList) {
                if (child.getUserType() == 2) {
                    return new Result(50001,"数据异常：此订单已经完成");
                }
                if (uid.equals(child.getUid())) {
                    return new Result(50001, "已经参与过此单");
                }
            }

            if (info.getUserType() == 2) {
                return new Result(50001, "邀请码无效：此交易已经完成");
            }

            // 邀请阶段
            String newInviteCode = shareId.substring(0,3) + uid.substring(2,5);
            System.out.println(newInviteCode);
            ShareItemRelationship newNode = ShareItemRelationship.builder()
                    .uid(uid)
                    .inviteCode(newInviteCode)
                    .name(userName)
                    .shareId(shareId)
                    .userType(userType)
                    .build();

            info.share(newNode);
            ShareItemRelationship save = repository.save(info);
            return new Result(200, "添加成功", save);
        }
    }

    @RequestMapping("/finish_count")
    public Result getInvokeCount(@RequestParam("uid") String uid) {
        int count = 0;
        if (uid == null) {
            return new Result(40001,"参数不能为空");
        }

        // 0. 参与过的所有Node
        List<ShareItemRelationship> shareItemRelationships = repository.queryByUid(uid);
        if (shareItemRelationships == null || shareItemRelationships.size() == 0) {
            return new Result(50001, "uid无效或未查询到记录");
        }

        // 对于所有的参与
        for (ShareItemRelationship joinNode : shareItemRelationships) {
            // 1. 找根节点
            ShareItemRelationship root = repository.getRootByShareId(joinNode.getShareId());

            // 2. 根节点的所有子节点
            List<ShareItemRelationship> childList = repository.findChildListInviteCode(root.getInviteCode());

            // 3. 判断2的个数
            for (ShareItemRelationship s : childList) {
                if (s.getUserType() == 2) {
                    count++;
                    break;
                }
            }
        }
        return new Result(210, "查询成功",count);
    }

    @RequestMapping("/total_count")
    public Result getTotalCount(@RequestParam("uid") String uid) {
        if (uid == null) {
            return new Result(40001,"参数不能为空");
        }

        List<ShareItemRelationship> shareItemRelationships = repository.queryByUid(uid);
        if (shareItemRelationships == null || shareItemRelationships.size() == 0) {
            return new Result(50001, "uid无效或未查询到记录");
        }

        int count = shareItemRelationships.size();

        return new Result(211, "查询成功",count);
    }

    // 寻找机会：状态未完结的shareId
    @RequestMapping("/chances")
    public Result getChances(@RequestParam("uid") String uid){
        if (uid == null) {
            return new Result(40001,"参数不能为空");
        }
        List<ShareItemRelationship> res = new ArrayList<>();
        List<ShareItemRelationship> allRoot = repository.findAllRoot();

        // 对于所有的根节点
        for (ShareItemRelationship relationship : allRoot) {
            // 0. 找到所有的子节点
            List<ShareItemRelationship> sons = repository.findChildListInviteCode(relationship.getInviteCode());
            boolean flag = false;
            for (ShareItemRelationship son : sons) {
                // 不要已经参与过的和已经完结的
                if (son.getUserType() == 2 || uid.equals(son.getUid())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                res.add(relationship);
            }
        }
        return new Result(220, "查询成功", res);
    }

    // 状态
    @RequestMapping("/status")
    public Result getStatus(@RequestParam("uid") String uid, @RequestParam("share_id") String shareId) {
        if (uid == null || shareId == null) {
            return new Result(40001,"参数不能为空");
        }

        ShareItemRelationship relationship = repository.getByShareIdAndUid(shareId, uid);
        if (relationship == null) {
            return new Result(40001,"错误40001");
        }

        List<ShareItemRelationship> parentList1 = repository.findParentList1(shareId, uid);
        int before  = parentList1.size();
        List<ShareItemRelationship> childList = repository.findChildList(uid, shareId);
        for (ShareItemRelationship shareItemRelationship : childList) {
            System.out.println(shareItemRelationship.toString());
        }
        int after = childList.size();

        Count count = new Count();
        count.setAfter(after);
        count.setBefore(before);

        return new Result(230, "查询成功", count);
    }


}
