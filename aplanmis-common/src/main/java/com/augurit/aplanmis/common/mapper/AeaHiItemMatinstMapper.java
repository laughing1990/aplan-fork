package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项材料实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:14</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemMatinstMapper {

    void insertAeaHiItemMatinst(AeaHiItemMatinst aeaHiItemMatinst) throws Exception;

    void batchInsertAeaHiItemMatinst(@Param("list") List<AeaHiItemMatinst> aeaHiItemMatinst) throws Exception;

    void updateAeaHiItemMatinst(AeaHiItemMatinst aeaHiItemMatinst) throws Exception;

    void batchUpdateAeaHiItemMatinst(@Param("list") List<AeaHiItemMatinst> aeaHiItemMatinst) throws Exception;

    void deleteAeaHiItemMatinst(@Param("id") String id) throws Exception;

    void deleteAeaHiItemMatinsts(@Param("ids") String[] ids) throws Exception;

    List<AeaHiItemMatinst> listAeaHiItemMatinst(AeaHiItemMatinst aeaHiItemMatinst) throws Exception;

    List<AeaHiItemMatinst> getMatinstListByStageinstId(@Param("stageinstId") String stageinstId, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<AeaHiItemMatinst> getOutputMatinstListByStageinstId(String stageinstId) throws Exception;

    List<AeaHiItemMatinst> getMatinstListByIteminstIds(@Param("iteminstIds") String[] iteminstIds, @Param("isInput") String isInput) throws Exception;

    List<AeaHiItemMatinst> getMatinstListByIteminstIdsAndKeyword(@Param("iteminstIds") String[] iteminstIds, @Param("isInput") String isInput, @Param("keyword") String keyword) throws Exception;


    List<AeaHiItemMatinst> getMatinstListByProjInfoIdAndMatIds(@Param("projInfoid") String projInfoid, @Param("matIds") String[] matIds) throws Exception;

    AeaHiItemMatinst getAeaHiItemMatinstById(@Param("id") String id) throws Exception;

    /**
     * 根据主键批量查询
     *
     * @param matinstIds
     * @return List<AeaHiItemMatinst>
     * @throws Exception e
     */
    List<AeaHiItemMatinst> listAeaHiItemMatinstByIds(@Param("matinstIds") String[] matinstIds) throws Exception;


    List<AeaHiItemMatinst> listUnitAttMatinst(@Param("matCode") String matCode, @Param("unitInfoIds") String[] unitInfoIds, @Param("linkmanInfoId") String linkmanInfoId);

    List<BscAttDetail> getAeaHiItemMatinstFile(@Param("tableName") String tableName, @Param("pkName") String pkName, @Param("keyword") String keyword, @Param("recordIds") String[] recordIds, @Param("excludeIds") String[] excludeIds) throws Exception;

    /**
     * 查询项目所有的材料实例
     *
     * @param matCodes    材料IDS
     * @param projInfoIds 项目ID
     * @param unitInfoIds 项目单位信息
     * @return
     */
    List<AeaHiItemMatinst> listProjAeaHiItemMatinst(@Param("matCodes") String[] matCodes, @Param("projInfoIds") String[] projInfoIds, @Param("unitInfoIds") String[] unitInfoIds);

    /**
     * 查询批文批复
     *
     * @param iteminstId 事项实例id
     */
    List<AeaHiItemMatinst> listOfficialDocsByIteminstId(@Param("iteminstId") String iteminstId);

    List<AeaHiItemMatinst> selectMatinstByIteminstIds(List<String> iteminstIds);

    /**
     * 获取阶段事项的输入材料实例列表
     *
     * @param iteminstId
     * @return
     */
    List<AeaHiItemMatinst> getMatinstListByStageIteminstId(@Param("iteminstId") String iteminstId);

    /*
     * 文件删除的相关操作
     * */
    void deleteBscAttLink(BscAttLink bscAttLink) throws Exception;


    Integer matinstbeLong2MatId(@Param("matinstId") String matinstId, @Param("matId") String matId, @Param("rootOrgId") String rootOrgId) throws Exception;

    /**
     * 查找当前登录用户的根文件
     *
     * @param tableName
     * @param pkName
     * @param recordId
     * @param orgId
     * @return
     */
    List<BscAttForm> listBscRootAttByUser(@Param("tableName") String tableName, @Param("pkName") String pkName, @Param("recordId") String recordId, @Param("orgId") String orgId);

    List<BscAttLink> getBscAttLinkByDetailId(@Param("detailId") String detailId);

    List<AeaHiItemMatinst> listAttachmentsByIteminstId(@Param("iteminstId") String iteminstId, @Param("matType") String matType, @Param("rootOrgId") String rootOrgId);

    List<AeaHiItemMatinst> getFormMatinstByProjInfoId(@Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId);

}
