package com.xsjt.learn.neo4j.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Aaron
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "InviteRelationship")
public class InviteRelationship implements Serializable {

    /** 用户id */
    @Id
    private String mid;

    /** 用户类型*/
    public int userType;

    /** 用户名称 */
    private String name;

    /** 用户邀请码 */
    private String inviteCode;

    /** 发展的下级 */
    @Relationship(type = "invite")
    private Set<InviteRelationship> fans;

    /**
     * 发展 方法
     * @param memberInvite 用户邀请信息
     */
    public void invite(InviteRelationship memberInvite) {
        if (fans == null) {
            fans = new HashSet<>();
        }
        fans.add(memberInvite);
    }
}


