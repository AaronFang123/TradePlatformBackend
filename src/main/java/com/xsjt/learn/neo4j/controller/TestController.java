package com.xsjt.learn.neo4j.controller;

import com.xsjt.learn.neo4j.dao.InvitingActionsRepository;
import com.xsjt.learn.neo4j.model.InviteRelationship;
import com.xsjt.learn.neo4j.model.Result;
import com.xsjt.learn.neo4j.util.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/register1")
public class TestController {
    @Autowired
    InvitingActionsRepository invitingActionsRepository;

    @RequestMapping("/varify")
    public Result queryValidNode(@Param("inviteCode") String inviteCode) {
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
    public Result inviteAndRegister(
            @RequestParam("inviteCode") String inviteCode,
            @RequestParam("username") String username) {
        if (username == null || inviteCode == null) {
            return new Result(40001, "参数不能为空");
        }

        Long uid = CodeUtils.getRandomID();
        String newInviteCode = CodeUtils.toSerialCode(uid);
        InviteRelationship parent = invitingActionsRepository.findNodeByInviteCode(inviteCode);
        if (parent == null) {
            return new Result(50001, "邀请码无效");
        }

        InviteRelationship son = InviteRelationship.builder()
                .mid(String.valueOf(uid))
                .name(username)
                .inviteCode(newInviteCode)
                .build();
        parent.invite(son);
        InviteRelationship save = invitingActionsRepository.save(parent);
        return new Result(200, "添加成功", save);
    }

    /**
     * 显示一个邀请码对应的所有上级
     * @param inviteCode
     * @return
     */
    @RequestMapping("/result")
    public Result queryInviteResult(@RequestParam("inviteCode") String inviteCode){
        if (inviteCode == null) {
            return new Result(40001,"参数不能为空");
        }

        InviteRelationship parent = invitingActionsRepository.findNodeByInviteCode(inviteCode);
        if (parent == null) {
            return new Result(50001, "邀请码无效");
        }

        List<InviteRelationship> parents = invitingActionsRepository.findParentList(parent.getMid());
        if (parents == null) {
            return new Result(50001, "出错了");
        }

        parents.forEach((item)->{
            item.setInviteCode("");
        });

        return new Result(201, "查找成功", parents);
    }
}
