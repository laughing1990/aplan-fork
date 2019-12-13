package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.aplanmis.common.domain.AeaBusCertinst;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AgentCertinst;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电子证照实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:10</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiCertinstMapper {

    public void insertAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception;

    public void updateAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception;

    public void deleteAeaHiCertinst(@Param("id") String id) throws Exception;

    public List<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception;

    public AeaHiCertinst getAeaHiCertinstById(@Param("id") String id) throws Exception;

    List<AeaHiCertinst> getAeaHiCertinstByIds(@Param("ids") List<String> ids);

    /**
     * 查询中介机构下资格证书列表
     */
    List<AgentCertinst> listAgentCertinst(@Param("unitInfoId") String unitInfoId, @Param("unitServiceId") String unitServiceId);

    /**
     * 查询中介机构下资格证书详情
     */
    AgentCertinst getAgentCertinstDetail(@Param("certinstId") String certinstId);

    List<AeaHiCertinst> getAeaImMajorLevelAndCertinstByServiceId(@Param("serviceId") String serviceId, @Param("unitInfoId") String unitInfoId);

    public List<BscAttDetail> getFilesByRecordIds(@Param("tableName") String tableName, @Param("pkName") String pkName, @Param("keyword") String keyword, @Param("recordIds") String[] recordIds) throws Exception;

    /**
     * @param type   1-中介机构 2-执业/职业人员
     * @param typeId type=1：unitInfoId；type=2：linkmanInfoId;
     * @return
     */
    List<AeaHiCertinstBVo> listAeaHiCertinstByBusRecord(@Param("type") String type, @Param("typeId") String typeId);

    List<AeaHiCertinstBVo> getAeaHiCertinstVoByLinkmanInfoId(@Param("linkmanInfoId") String linkmanInfoId);

    List<AeaHiCertinstBVo> getAeaHiCertinstByBusCertinst(AeaBusCertinst aeabuscertinst);

    AeaHiCertinstBVo getAeaHiCertinstVoById(@Param("certinstId") String certinstId);

    /**
     * 获取事项实例下的证照实例
     *
     * @param iteminstId
     * @return
     */
    List<AeaHiCertinst> getAeaHiCertinstByIteminstId(@Param("iteminstId") String iteminstId);

    //批量删除证照实例
    int batchDeleteAeaHiCertinst(@Param("certinstIds") String[] certinstIds);

    int batchDeleteAeaHiCertinstByMatinstIds(@Param("matinstIds") String[] matinstIds);

    List<AeaHiCertinstBVo> getAeaHiCertinstByUnitServiceId(@Param("unitServiceId") String unitServiceId);

    /**
     * 获取事项实例下的证照实例列表
     *
     * @param iteminstIds
     * @return
     */
    List<AeaHiCertinst> listAeaHiCertinstByIteminstIds(@Param("iteminstIds") String[] iteminstIds);

    void batchInsertAeaHiCertinst(@Param("certinsts") List<AeaHiCertinst> certinsts);

    List<AeaHiCertinst> listAeaHiCertinstByCertinstCode(@Param("certinstCode") String certinstCode, @Param("currentOrgId") String currentOrgId);

    List<AeaHiCertinst> getCertinsts(@Param("certId") String certId, @Param("linkmanInfoId") String linkmanInfoId, @Param("projInfoId") String projInfoId, @Param("unInfoIds") String[] unInfoIds, @Param("rootOrgId") String rootOrgId);

    List<AeaHiCertinst> getCertintList(@Param("linkmanInfoId") String linkmanInfoId, @Param("unitInfoId") String unitInfoId, @Param("projInfoIds") String[] projInfoIds,@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);
}
