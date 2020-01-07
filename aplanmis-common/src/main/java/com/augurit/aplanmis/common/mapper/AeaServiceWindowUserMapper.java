package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人：yinlf</li>
 * <li>创建时间：2019-07-25 14:14:40</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaServiceWindowUserMapper {

    void insertAeaServiceWindowUser(AeaServiceWindowUser aeaServiceWindowUser);

    void updateAeaServiceWindowUser(AeaServiceWindowUser aeaServiceWindowUser);

    void deleteAeaServiceWindowUser(@Param("id") String id);

    void deleteAeaServiceWindowUserByWindowId(@Param("windowId") String windowId);

    List<AeaServiceWindowUser> listAeaServiceWindowUser(AeaServiceWindowUser aeaServiceWindowUser);

    AeaServiceWindowUser getAeaServiceWindowUserById(@Param("id") String id);

    void deleteAeaServiceWindowUserByWindowIdAndUserId(@Param("windowId") String windowId, @Param("userId") String userId);

    void enableWindowUser(@Param("windowId") String windowId, @Param("userId") String userId);

    void disableWindowUser(@Param("windowId") String windowId, @Param("userId") String userId);

    List<OpuOmUser> findWindowUserByWindowIdAndKeywordAndIsActive(@Param("windowId") String windowId, @Param("keyword") String keyword, @Param("isActive") String isActive);

    void batchDeleteWindowUserByWindowIdAndUserId(@Param("windowId") String windowId, @Param("userIds") String[] userIds);

    void batchInsertAeaServiceWindowUser(@Param("aeaServiceWindowUserList") List<AeaServiceWindowUser> aeaServiceWindowUserList);

    List<AeaServiceWindowUser> listWindowUserByWindowId(@Param("windowId")String windowId, @Param("keyword")String keyword);

    /**
     * 根据用户ID查询窗口
     * @param userId
     * @return
     */
    List<AeaServiceWindowUser> getAeaServiceWindowUserByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID查询代办中心
     * @param userId
     * @param rootOrgId
     * @return
     */
    List<AeaServiceWindowUser> getAeaServiceWindowUserByUserIdAndRootOrgId(@Param("userId") String userId,@Param("rootOrgId") String rootOrgId);

    List<OpuOmUser> findWindowUserByRegionIdAndAllItemUser(@Param("regionId")String regionId,@Param("rootOrgId") String rootOrgId);

    List<OpuOmUser> findWindowUserByRegionId(@Param("regionId") String regionId, @Param("rootOrgId") String rootOrgId);


    /**
     * 根据用户ID查询对应窗口的所有人员
     * @param userId
     * @return
     */
    List<AeaServiceWindowUser> queryAeaServiceWindowUser(@Param("userId")String userId);

    AeaServiceWindowUser getAeaServiceWindowUserByWindowIdAndUserId(@Param("windowId") String windowId, @Param("userId") String userId);

    /**
     * 查询代办中心的代办人员
     * @param windowIds
     * @param rootOrgId
     * @return
     */
    List<AeaServiceWindowUser> listAeaServiceWindowUserByWindowIdsAndRootOrgId(@Param("windowIds") String[] windowIds,@Param("rootOrgId") String rootOrgId);
}