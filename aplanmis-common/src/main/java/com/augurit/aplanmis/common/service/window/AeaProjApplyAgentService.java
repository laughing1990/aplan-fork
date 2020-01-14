package com.augurit.aplanmis.common.service.window;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.vo.agency.AgencyDetailVo;
import com.augurit.aplanmis.common.vo.agency.SplitProjFromVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 项目代办申请表-Service服务调用接口类
 */
public interface AeaProjApplyAgentService {
    public void saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public void updateAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public void deleteAeaProjApplyAgentById(String id) throws Exception;

    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent, Page page) throws Exception;

    public AeaProjApplyAgent getAeaProjApplyAgentById(String id) throws Exception;

    public List<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgentByConditional(AeaProjApplyAgent aeaProjApplyAgent, Page page) throws Exception;

    public AgencyDetailVo getAgencyDetail(String applyAgentId) throws Exception;

    List<AeaProjApplyAgent> listAeaProjApplyAgentByProjInfoId(String projInfoId) throws Exception;

    public AeaProjApplyAgent getAgencyAgreementDetail(String applyAgentId) throws Exception;

    public boolean checkAgreementCodeUnique(String agreementCode);

    public List<AeaServiceWindowUser> getCurrAgencyWinUserList();

    public boolean deleteAgreementFile(String agreementCode) throws Exception;

    public String queryRedisData(String applyAgentId) throws Exception;

    public void saveRedisAeaProjApplyAgentInfo(AeaProjApplyAgent aeaProjApplyAgent) throws Exception;

    public SplitProjFromVo getSplitProjFromInfo(String localCode) throws Exception;

    public void saveSplitProjFromInfo(SplitProjFromVo splitProjFromVo) throws Exception;
}
