package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目代办申请表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaProjApplyAgentMapper {
    public void insertAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public void updateAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public void deleteAeaProjApplyAgent(@Param("id") String id) throws Exception;

    public List<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public AeaProjApplyAgent getAeaProjApplyAgentById(@Param("id") String id) throws Exception;

    public List<AeaProjApplyAgent> listAeaProjApplyAgentByConditional(AeaProjApplyAgent aeaProjApplyAgent);

    public AeaProjApplyAgent getAgencyAgreementDetail(@Param("applyAgentId") String applyAgentId);

    public AeaProjApplyAgent getAeaProjApplyAgentByAgreementCode(@Param("agreementCode") String agreementCode);

}
