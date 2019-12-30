package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.domain.AgentLinkmanCert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 联系人表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:44:47</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaLinkmanInfoMapper {

    int insertAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    int updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    int deleteAeaLinkmanInfo(@Param("id") String id);

    int batchLinkmanInfo(@Param("ids") String[] ids);

    List<AeaLinkmanInfo> findAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    AeaLinkmanInfo getAeaLinkmanInfoById(@Param("id") String id);

    List<AeaLinkmanInfo> getProjLinkman(@Param("applyinstId") String applyinstId, @Param("projInfoId") String projInfoId);

    AeaLinkmanInfo getApplyProjLinkman(@Param("applyinstId") String applyinstId, @Param("projInfoId") String projInfoId);

    List<AeaLinkmanInfo> findAllUnitLinkman(@Param("unitInfoId") String unitInfoId);

    AeaLinkmanInfo getAeaLinkmanInfoByLoginName(@Param("loginName") String loginName, @Param("rootOrgId") String rootOrgId);

    List<AeaLinkmanInfo> findLinkmanInfoByKeyword(@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);

    List<AeaLinkmanInfo> getProjLinkmanByApplyinstId(@Param("applyinstId") String applyinstId);

    List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(@Param("unitInfoId") String unitInfoId, @Param("isBindAccount") String isBindAccount) throws Exception;

    List<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkman);

    /**
     * 查询单位人员资格证书
     */
    List<AgentLinkmanCert> listAgentLinkmanCertinst(@Param("unitInfoId") String unitInfoId, @Param("linkmanName") String linkmanName);

    /**
     * 查询中介机构业务授权人信息
     */
    List<AgentLinkmanCert> listAgentHeadLinkman(@Param("unitInfoId") String unitInfoId);

    List<AeaLinkmanInfo> getLinkmanList(@Param("unitInfoId") String unitInfoId, @Param("serviceId") String serviceId, @Param("linkmanInfoIds") List linkmanInfoIds);

    List<AeaLinkmanInfo> getAeaLinkmanInfoAndUnitInfoByLinkmanInfoId(@Param("id") String id);

    int updateAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman);

    List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoId(@Param("unitInfoId") String unitInfoId, @Param("isAll") Integer isAll);

    /**
     * 删除委托人信息
     */
    void deleteUnitLinkmanInfo(AeaUnitLinkman aeaUnitLinkman);

    List<AeaLinkmanInfo> listUnBindLinkman(AeaLinkmanInfo aeaLinkmanInfo);

    List<AeaUnitLinkman> listAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman);

    void insertUnitLinkman(AeaUnitLinkman aeaUnitLinkman);

    List<AeaLinkmanInfo> listBindLinkmanByUnitId(@Param("unitInfoId") String unitInfoId, @Param("isAdministrators") String isAdministrators, @Param("isBindAccount") String isBindAccount, @Param("keyword") String keyword);

    List<AeaLinkmanInfo> getLinkmanListByServiceId(@Param("serviceId") String serviceId, @Param("unitInfoId") String unitInfoId);

    List<AeaLinkmanInfo> listLinkmanServiceName(@Param("linkmanIds") String[] linkmanIds);

    AeaLinkmanInfo getApplyLinkman(@Param("applyinstId") String applyinstId);

    List<AeaLinkmanInfo> findLinkmanTypes(@Param("projInfoId") String projInfoId, @Param("unitInfoId") String unitInfoId);

    /**
     * 根据单位查询联系人表中的法人信息
     *
     * @param unitInfoId
     * @return
     */
    List<AeaLinkmanInfo> findCorporationByUnitInfoId(String unitInfoId);

    /**
     * 查询单位下绑定的中介超市业务联系人
     *
     * @param unitInfoId 单位ID
     * @return List<AeaLinkmanInfo>
     */
    List<AeaLinkmanInfo> findAgentBindAccountLinkman(@Param("unitInfoId") String unitInfoId);

    /**
     * 删除||启用单位下所有的联系人
     *
     * @param unitInfoId
     * @param modifier
     */
    void updateAllUnitLinkman(@Param("unitInfoId") String unitInfoId, @Param("modifier") String modifier, @Param("isDeleted") String isDeleted);

    /**
     * 根据证件号查找联系人信息
     *
     * @param linkmanCertNo 联系人证件号
     * @return List<AeaLinkmanInfo>
     */
    List<AeaLinkmanInfo> getAeaLinkmanByCertNo(@Param("linkmanCertNo") String linkmanCertNo) throws Exception;
}
