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
@NodeEntity(label = "ShareItemRelationship")
public class ShareItemRelationship implements Serializable {

    /** 分享id */
    private String shareId;
    /** 用户类型 */
    public Integer userType;
    /** 用户名称 */
    private String name;
    /** 用户分享码  sid 3 + uid 3 */
    private String uid;
    @Id
    private String inviteCode;
    /** 发展的下级 */
    @Relationship(type = "share")
    private Set<ShareItemRelationship> fans;

    /**
     * 发展 方法
     * @param relationship 用户邀请信息
     */
    public void share(ShareItemRelationship relationship) {
        if (fans == null) {
            fans = new HashSet<>();
        }
        fans.add(relationship);
    }
}

