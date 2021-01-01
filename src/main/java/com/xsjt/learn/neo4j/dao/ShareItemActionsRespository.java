package com.xsjt.learn.neo4j.dao;

import com.xsjt.learn.neo4j.model.ShareItemRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aaron
 */
@Repository
public interface ShareItemActionsRespository extends Neo4jRepository<ShareItemRelationship, String> {

    /**
     * 根据节点id查询节点信息
     * @param shareId shareId
     * @return ShareItemRelationship
     */
    ShareItemRelationship findByShareId(String shareId);

    /**
     * 查询某个节点的所有子节点
     * @param uid uid
     * @return List<ShareItemRelationship>
     */
    @Query("Match (s:ShareItemRelationship{shareId:{shareId}, uid:{uid}})-[*]->(p:ShareItemRelationship) return p")
    List<ShareItemRelationship> findChildList(@Param("uid")String uid, @Param("shareId") String shareId );

    /**
     * 查询某个节点的所有子节点
     * @param inviteCode inviteCode
     * @return List<ShareItemRelationship>
     */
    @Query("Match (p:ShareItemRelationship{inviteCode:{inviteCode}})-[*]->(s:ShareItemRelationship) return s ")
    List<ShareItemRelationship> findChildListInviteCode(String inviteCode);

    /**
     * 查询某个节点的直属父节点
     * @param shareId shareId
     * @return ShareItemRelationship
     */
    @Query("Match (p:ShareItemRelationship)-[*]->(s:ShareItemRelationship{shareId:{shareId}}) return p limit 1")
    ShareItemRelationship findParent(@Param("shareId") String shareId);

    /**
     * 查询某个节点的所有父节点
     * @param shareId
     * @return List<ShareItemRelationship>
     */
    @Query("Match (p:ShareItemRelationship)-[*]->(s:ShareItemRelationship{shareId:$mid}) return p")
    List<ShareItemRelationship> findParentList(@Param("shareId") String shareId);

    /**
     * 查询某个节点的所有父节点
     * @param shareId
     * @return List<ShareItemRelationship>
     */
    @Query("Match (p:ShareItemRelationship)-[*]->(s:ShareItemRelationship{shareId:$shareId, uid:$uid}) return p")
    List<ShareItemRelationship> findParentList1(@Param("shareId") String shareId, @Param("uid") String uid);

    // 根据uid和shareId找到某个
    @Query("MATCH (p:ShareItemRelationship {shareId:{shareId},uid:{uid}}) RETURN p Limit 1")
    ShareItemRelationship getByShareIdAndUid(@Param("shareId") String shareId, @Param("uid")  String uid);

    /**
     * 查询某个节点的 指定等级的 最近的父节点
     * @param shareId
     * @param level
     * @return ShareItemRelationship
     */
    @Query("Match (p:ShareItemRelationship{level:{level}})-[*]->(s:ShareItemRelationship{shareId:{shareId}}) return p limit 1")
    ShareItemRelationship findParentInfoByLevel(@Param("shareId") String shareId, @Param("level") Integer level);



    /**
     * 查询某个节点的 指定等级的所有父节点
     * @param shareId
     * @param level
     * @return List<ShareItemRelationship>
     */
    @Query("Match (p:ShareItemRelationship{level:{level}})-[*]->(s:ShareItemRelationship{shareId:{shareId}}) return p")
    List<ShareItemRelationship> findParentByLevel(@Param("shareId") String shareId, @Param("level") Integer level);

    /**
     * 按属性查找节点
     */
    @Query("match (p:ShareItemRelationship) where p.inviteCode={inviteCode} return p")
    ShareItemRelationship findNodeByInviteCode(@Param("inviteCode") String inviteCode);

    @Query("match (p:ShareItemRelationship) where p.shareId={shareId} return p")
    ShareItemRelationship findNodeByMid(@Param("shareId") String shareId);



    // 创建全新节点
    @Query("create(p:ShareItemRelationship {shareId:{shareId}" +
            ", userType:{userType}, name:{name}, inviteCode:{inviteCode}, uid:{uid}}) return p")
    ShareItemRelationship createNewNode(@Param("shareId") String shareId,
                                        @Param("userType") Integer userType,
                                        @Param("name") String name,
                                        @Param("inviteCode") String inviteCode,
                                        @Param("uid") String uid);

    /**
     * 根据shareId查询所有的节点
     * @param shareId  shareId
     * @return List<ShareItemRelationship>
     */
    @Query("match (r:ShareItemRelationship) where r.shareId={shareId} return r")
    List<ShareItemRelationship> queryByShareId(@Param("shareId") String shareId);

    /**
     * 根据shareId查询所有的节点
     * @param uid  shareId
     * @return List<ShareItemRelationship>
     */
    @Query("match (r:ShareItemRelationship) where r.uid={uid} return r")
    List<ShareItemRelationship> queryByUid(@Param("uid") String uid);


    @Query("match (n:ShareItemRelationship) \n" +
            "where not ()-->(n) \n" +
            "return n")
    List<ShareItemRelationship> findAllRoot();


    /**
     * 根据shareId查询根节点
     * @param shareId  shareId
     * @return List<ShareItemRelationship>
     */
    @Query("match (n:ShareItemRelationship) where not ()-->(n) and n.shareId={shareId}  return n")
    ShareItemRelationship getRootByShareId(@Param("shareId") String shareId);

}


