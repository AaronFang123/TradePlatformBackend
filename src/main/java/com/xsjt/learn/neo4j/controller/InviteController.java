package com.xsjt.learn.neo4j.controller;

import com.xsjt.learn.neo4j.dao.InvitingActionsRepository;
import com.xsjt.learn.neo4j.model.InviteRelationship;
import com.xsjt.learn.neo4j.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 */
@RestController
@RequestMapping("/invite")
public class InviteController {
    @Autowired
    InvitingActionsRepository invitingActionsRepository;

    // 返回当前节点的信息
    @RequestMapping("/code_info")
    public Result queryValidNode(@RequestParam("invite_code") String inviteCode) {
        if (inviteCode == null) {
            return new Result(40001,"参数不能为空");
        }
        InviteRelationship parent = invitingActionsRepository.findNodeByInviteCode(inviteCode);
        if (parent == null) {
            return new Result(50001, "邀请码无效");
        }
        return new Result(200, "查询成功", parent);
    }

    @RequestMapping("/test")
    public Result test() {
        return new Result(200, "查询成功");
    }

    @RequestMapping("/invite")
    public Result inviteNewUser(@RequestParam("invite_code") String inviteCode,
                                @RequestParam("mid") String mid ,
                                @RequestParam("user_name") String userName,
                                @RequestParam("user_type") Integer userType) {
        if (inviteCode == null || mid == null || userName == null) {
            return new Result(40001,"参数不能为空");
        }

        InviteRelationship parent = invitingActionsRepository.findNodeByInviteCode(inviteCode);
        if (parent == null) {
            return new Result(50001, "邀请码无效");
        }

        InviteRelationship isExits = invitingActionsRepository.findNodeByMid(mid);
        if (isExits != null) {
            return new Result(50002, "您已经接受邀请并注册，不可重新注册");
        }

        InviteRelationship newUser = InviteRelationship.builder()
                .mid(mid)
                .inviteCode(mid.substring(0, 6))
                .name(userName)
                .userType(userType)
                .build();

        parent.invite(newUser);
        InviteRelationship save = invitingActionsRepository.save(parent);
        return new Result(200, "添加成功", save);
    }
}
