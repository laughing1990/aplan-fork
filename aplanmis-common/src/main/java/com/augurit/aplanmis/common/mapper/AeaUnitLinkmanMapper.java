package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:49</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaUnitLinkmanMapper {

    int insertAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman) throws Exception;

    int updateAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman) throws Exception;

    int deleteAeaUnitLinkman(@Param("id") String id) throws Exception;

    List<AeaUnitLinkman> listAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman) throws Exception;

    AeaUnitLinkman getAeaUnitLinkmanById(@Param("id") String id) throws Exception;

    int batchInsertAeaUnitLinkman(@Param("aeaUnitLinkmanList") List<AeaUnitLinkman> aeaUnitLinkmanList);

    int deleteUnitLinkman(@Param("unitInfoId") String unitInfoId, @Param("linkmanInfoIds") String[] linkmanInfoIds);

    void updateAeaUnitLinkmanByUnitAndLinkman(AeaUnitLinkman aeaUnitLinkman);

    List<String> getLinkManIdByUnitInfoId(@Param("unitInfoId") String unitInfoId);

    /**
     * 查询联系人所属单位
     *
     * @param linkmanInfoId 联系人ID
     * @return List<AeaUnitLinkman>
     */
    List<AeaUnitLinkman> getLinkManIdByLinkmanInfoId(@Param("linkmanInfoId") String linkmanInfoId) throws Exception;

    /**
     * 查询单位联系人关联表
     *
     * @param unitInfoId    单位ID
     * @param linkmanInfoId 联系人ID
     * @return List<AeaUnitLinkman>
     */
    List<AeaUnitLinkman> getAeaUnitLinkmanByUnitIdOrLinkId(@Param("unitInfoId") String unitInfoId, @Param("linkmanInfoId") String linkmanInfoId);

    /**
     * 物理删除单位与联系人关联表数据
     *
     * @param unitInfoId
     * @param linkmanInfoId
     */
    int deleteAeaUnitLinkmanByUnitIdAndLinkmanId(@Param("unitInfoId") String unitInfoId, @Param("linkmanInfoId") String linkmanInfoId) throws Exception;

    /**
     * 绑定联系人到当前单位
     *
     * @param unitLinkmanIds   主键
     * @param isBindAccount    是否绑定 1 是 0 否
     * @param isAdministrators 是否管理员 1 是 0否
     */
    void bindLinkmanToUnit(@Param("unitLinkmanId") String[] unitLinkmanIds, @Param("isBindAccount") String isBindAccount, @Param("isAdministrators") String isAdministrators) throws Exception;
}
