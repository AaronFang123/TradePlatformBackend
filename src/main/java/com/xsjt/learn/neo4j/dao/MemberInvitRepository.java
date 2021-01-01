package com.xsjt.learn.neo4j.dao;

import com.xsjt.learn.neo4j.model.InviteRelationship;
import com.xsjt.learn.neo4j.model.MemberInvite;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * dao层
 * @author Joe
 * @date 2020-01-13
 */
@Repository
public interface MemberInvitRepository extends Neo4jRepository<MemberInvite, Long> {

    /**
     * 根据节点id查询节点信息
     * @param mId
     * @return
     */
    MemberInvite findBymId(String mId);

    /**
     * 查询某个节点的所有子节点
     * @param mId
     * @return
     */
    @Query("Match (p:MemberInvit{mId:{mId}})-[*]->(s:MemberInvit) return s ")
    List<MemberInvite> findChildList(String mId);

    /**
     * 查询某个节点的直属父节点
     * @param mId
     * @return
     */
    @Query("Match (p:MemberInvit)-[*]->(s:MemberInvit{mId:{mId}}) return p limit 1")
    MemberInvite findParent(@Param("mId") String mId);

    /**
     * 查询某个节点的所有父节点
     * @param mId
     * @return
     */
    @Query("Match (p:MemberInvit)-[*]->(s:MemberInvit{mId:$mId}) return p")
    List<MemberInvite> findParentList(@Param("mId") String mId);


    /**
     * 查询某个节点的 指定等级的 最近的父节点
     * @param mId
     * @param level
     * @return
     */
    @Query("Match (p:MemberInvit{level:{level}})-[*]->(s:MemberInvit{mId:{mId}}) return p limit 1")
    MemberInvite findParentInfoByLevel(@Param("mId") String mId, @Param("level") Integer level);

    /**
     * 查询某个节点的 指定等级的所有父节点
     * @param mId
     * @param level
     * @return
     */
    @Query("Match (p:MemberInvit{level:{level}})-[*]->(s:MemberInvit{mId:{mId}}) return p")
    List<MemberInvite> findParentByLevel(@Param("mId") String mId, @Param("level") Integer level);

    /**
     * 按属性查找节点
     */
    @Query("match (p:MemberInvit) where p.name={name} return p")
    List<MemberInvite> findNodeByName(@Param("name") String name);
}
