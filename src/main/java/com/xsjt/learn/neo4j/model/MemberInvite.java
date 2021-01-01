package com.xsjt.learn.neo4j.model;

import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joe
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "MemberInvit")
public class MemberInvite implements Serializable {

    /** 用户id */
    @Id
    private String mId;

    /** 用户名称 */
    private String name;

    /** 用户等级 */
    private Integer level;

    private Integer userType;

    /** 发展的下级 */
    @Relationship(type = "develop")
    private Set<MemberInvite> fans;

    /**
     * 发展 方法
     * @param memberInvite 用户邀请信息
     */
    public void develop(MemberInvite memberInvite) {
        if (fans == null) {
            fans = new HashSet<>();
        }
        fans.add(memberInvite);
    }
}