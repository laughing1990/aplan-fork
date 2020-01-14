package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.ExSJUnitFromDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单位表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:48</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaUnitInfoMapper {

    int insertAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    int updateAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    /**
     * 删除或启用单位
     *
     * @param id        单位ID
     * @param modifier  修改人
     * @param isDeleted 逻辑删除标记。0表示正常记录，1表示已删除记录。
     * @return 删除的条数
     */
    int deleteOrEnableAeaUnitInfo(@Param("id") String id, @Param("modifier") String modifier, @Param("isDeleted") String isDeleted);

    int batchDeleteUnitInfo(@Param("ids") String[] ids);

    List<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    AeaUnitInfo getAeaUnitInfoById(@Param("id") String id);

    List<AeaUnitInfo> findApplyUnitProj(@Param("applyinstId") String applyinstId, @Param("projInfoId") String projInfoId, @Param("isOwner") String isOwner);

    List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(@Param("projInfoId") String projInfoId, @Param("unitType") String unitType, @Param("rootOrgId") String rootOrgId);

    List<AeaUnitInfo> findUnitProjByProjInfoIdAndIsOwner(@Param("projInfoId") String projInfoId, @Param("isOwner") String isOwner);

    List<AeaUnitInfo> findChildUnit(@Param("unitInfoId") String unitInfoId);

    AeaUnitInfo getParentUnit(@Param("unitInfoId") String unitInfoId);

    AeaUnitInfo getAeaUnitInfoByLoginName(@Param("loginName") String loginName, @Param("rootOrgId") String rootOrgId);

    List<AeaUnitInfo> findAeaUnitInfoByKeyword(@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);

    List<AeaUnitInfo> listAeaUnitInfoByLinkIdHasBind(String userId);

    AeaUnitInfo getOneByApplyinstIdAndLinkId(String applyinstId, String linkmanInfoId);

    public List<AeaUnitInfo> listUnit(String itemVerId);

    List<AeaUnitInfo> listAgencyOrg(@Param("agencyName") String agencyName, @Param("serviceType") String serviceType,
                                    @Param("stimeOrder") String stimeOrder, @Param("countOrder") String countOrder,
                                    @Param("commentOrder") String commentOrder);

    List<AeaUnitInfo> oneAgency(@Param("agencyName") String agencyName);

    /**
     * 根据单位ID查询联系人列表
     *
     * @return
     */
    List<AeaLinkmanInfo> getLinkmanByUnitInfoId(@Param("unitInfoId") String unitInfoId);

    List<AeaUnitInfo> getAeaUnitListByLinkmanInfoId(String linkmanInfoId);

    /**
     * 查询建设单位
     *
     * @param projInfoId 项目ID
     * @param unitType   单位类型
     * @return
     */
    List<AeaUnitInfo> getAeaUintListByProjInfoIdAndUnitYype(@Param("projInfoId") String projInfoId, @Param("unitType") String unitType);

    /**
     * 模糊查询，供施工和监理单位表单使用
     *
     * @param keyword
     * @param rootOrgId
     * @return
     */
    List<ExSJUnitFromDetails> findAeaExProBuildUnitInfoByKeyword(@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);

    List<AeaUnitInfo> getRegisterUnitList(AeaUnitInfo aeaUnitInfo);

    /**
     * 根据单位ID查询单位信息，包括已删除的信息
     *
     * @param unitInfoId 单位ID
     * @return 单位信息
     */
    AeaUnitInfo getAeaUnitIncludeDeleteById(@Param("unitInfoId") String unitInfoId) throws Exception;

    List<AeaUnitInfo> getApplyJSUnitByProjInfoIdAndApplyinstId(@Param("projInfoId")String projInfoId,@Param("applyinstId")String applyinstId);

    int batchDeleteAeaUnitInfo(@Param("ids") List<String> ids) throws Exception;

    int batchInsertAeaUnitInfo(@Param("list") List<AeaUnitInfo> unitInfos) throws Exception;

    List<AeaUnitInfo> findAeaUnitInfoByProjLocalCode(@Param("localCode")String localCode,  @Param("rootOrgId") String rootOrgId);

    List<AeaUnitInfo> getApplyUnitProj(@Param("applyinstId")String applyinstId, @Param("projInfoId")String projInfoId);
}
