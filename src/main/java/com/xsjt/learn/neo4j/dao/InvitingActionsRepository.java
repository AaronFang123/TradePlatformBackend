package com.xsjt.learn.neo4j.dao;

import com.xsjt.learn.neo4j.model.InviteRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Aaron
 */
@Repository
public interface InvitingActionsRepository extends Neo4jRepository<InviteRelationship, String> {

    /**
     * 根据节点id查询节点信息
     * @param mid mid
     * @return InviteRelationship
     */
    InviteRelationship findBymid(String mid);

    /**
     * 查询某个节点的所有子节点
     * @param mid mid
     * @return List<InviteRelationship>
     */
    @Query("Match (p:InviteRelationship{mid:{mid}})-[*]->(s:InviteRelationship) return s ")
    List<InviteRelationship> findChildList(@Param("mid")String mid);

//    @Query("Sdfasd")
//    InviteRelationship xxx(Long mid);

    /**
     * 查询某个节点的直属父节点
     * @param mid
     * @return InviteRelationship
     */
    @Query("Match (p:InviteRelationship)-[*]->(s:InviteRelationship{mid:{mid}}) return p limit 1")
    InviteRelationship findParent(@Param("mid") String mid);

    /**
     * 查询某个节点的所有父节点
     * @param mid
     * @return List<InviteRelationship>
     */
    @Query("Match (p:InviteRelationship)-[*]->(s:InviteRelationship{mid:$mid}) return p")
    List<InviteRelationship> findParentList(@Param("mid") String mid);


    /**
     * 查询某个节点的 指定等级的 最近的父节点
     * @param mid
     * @param level
     * @return InviteRelationship
     */
    @Query("Match (p:InviteRelationship{level:{level}})-[*]->(s:InviteRelationship{mid:{mid}}) return p limit 1")
    InviteRelationship findParentInfoByLevel(@Param("mid") String mid, @Param("level") Integer level);

    /**
     * 查询某个节点的 指定等级的所有父节点
     * @param mid
     * @param level
     * @return List<InviteRelationship>
     */
    @Query("Match (p:InviteRelationship{level:{level}})-[*]->(s:InviteRelationship{mid:{mid}}) return p")
    List<InviteRelationship> findParentByLevel(@Param("mid") String mid, @Param("level") Integer level);

    /**
     * 按属性查找节点
     */
    @Query("match (p:InviteRelationship) where p.inviteCode={inviteCode} return p")
    InviteRelationship findNodeByInviteCode(@Param("inviteCode") String inviteCode);

    @Query("match (p:InviteRelationship) where p.mid={mid} return p")
    InviteRelationship findNodeByMid(@Param("mid") String mid);


}
