package com.augurit.aplanmis.supermarket.credit.service.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import com.augurit.aplanmis.common.domain.AeaCreditSummary;
import com.augurit.aplanmis.common.domain.AeaCreditUnitInfo;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.dto.AeaCreditDetailDto;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryDto;
import com.augurit.aplanmis.common.mapper.AeaCreditDetailMapper;
import com.augurit.aplanmis.common.mapper.AeaCreditSummaryMapper;
import com.augurit.aplanmis.common.mapper.AeaCreditUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.supermarket.credit.service.SuperMarketCreditSummaryService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/***
 * @description
 * @author mohaoqi
 * @date 2019/10/30 0030
 ***/
@Service
@Transactional
public class SuperMarketCreditSummaryServiceImpl implements SuperMarketCreditSummaryService {

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Autowired
    private AeaCreditUnitInfoMapper unitInfoMapper;

    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;

    @Autowired
    private AeaCreditSummaryMapper aeaCreditSummaryMapper;

    @Autowired
    private AeaCreditDetailMapper aeaCreditDetailMapper;

    @Override
    public List<AeaCreditSummaryAllDto> listCreditSummaryDetailByByBizCode(String bizType, String bizCode) throws Exception {

        List<AeaCreditSummaryAllDto> all = new ArrayList<>();
        List<BscDicCodeItem> itemList = bscDicCodeMapper.getActiveItemsByTypeCode("AEA_CREDIT_TYPE", topOrgId);
        if(itemList!=null&&itemList.size()>0){
            for(BscDicCodeItem item:itemList){
                AeaCreditSummaryAllDto dto = new AeaCreditSummaryAllDto();
                dto.setItemName(item.getItemName());
                dto.setItemCode(item.getItemCode());
                List<AeaCreditSummary> summaries = this.listCreditSummaryByBizCode(bizType, item.getItemCode(), bizCode);
                handSummaryDetail(topOrgId, dto, summaries);
                all.add(dto);
            }
        }
        return all;
    }

    private List<AeaCreditSummary> listCreditSummaryByBizCode(String bizType, String creditType, String bizCode) {
        if(StringUtils.isBlank(bizType)){
            throw new InvalidParameterException("参数bizType为空!");
        }
        if(StringUtils.isBlank(creditType)){
            throw new InvalidParameterException("参数creditType为空!");
        }
        if(StringUtils.isBlank(bizCode)){
            throw new InvalidParameterException("参数bizCode为空!");
        }
        AeaCreditSummary summary = new AeaCreditSummary();
        summary.setRootOrgId(topOrgId);
        summary.setCreditType(creditType);
        summary.setSummaryType(bizType);
        if(bizType.equals("u")){
            AeaCreditUnitInfo unit = new AeaCreditUnitInfo();
            unit.setRootOrgId(topOrgId);
            unit.setUnifiedSocialCreditCode(bizCode);
            List<AeaCreditUnitInfo> list = unitInfoMapper.listAeaCreditUnitInfo(unit);
            if(list!=null&&list.size()>0){
                summary.setCreditUnitInfoId(list.get(0).getCreditUnitInfoId());
            }else{
                return new ArrayList<AeaCreditSummary>();
            }
        }else if(bizType.equals("l")){
            AeaLinkmanInfo linkman = new AeaLinkmanInfo();
            linkman.setLinkmanCertNo(bizCode);
            linkman.setRootOrgId(topOrgId);
            List<AeaLinkmanInfo> list = linkmanInfoMapper.findAeaLinkmanInfo(linkman);
            if(list!=null&&list.size()>0){
                summary.setLinkmanInfoId(list.get(0).getLinkmanInfoId());
            }else{
                return new ArrayList<AeaCreditSummary>();
            }
        }
        return aeaCreditSummaryMapper.listAeaCreditSummary(summary);
    }

    private void handSummaryDetail(String rootOrgId, AeaCreditSummaryAllDto dto, List<AeaCreditSummary> summaries) throws Exception {

        if (summaries != null && summaries.size() > 0) {
            for (AeaCreditSummary summary : summaries) {
                // 转换汇总表
                AeaCreditSummaryDto summaryDto = new AeaCreditSummaryDto();
                BeanUtils.copyProperties(summaryDto, summary);
                summaryDto.setLastUpdateTime(summary.getModifyTime() == null ? summary.getCreateTime() : summary.getModifyTime());
                // 转换汇总表详情
                AeaCreditDetail sdetail = new AeaCreditDetail();
                sdetail.setRootOrgId(rootOrgId);
                sdetail.setSummaryId(summary.getSummaryId());
                List<AeaCreditDetail> details = aeaCreditDetailMapper.listAeaCreditDetail(sdetail);
                if (details != null && details.size() > 0) {
                    for (AeaCreditDetail detail : details) {
                        AeaCreditDetailDto detailDto = new AeaCreditDetailDto();
                        BeanUtils.copyProperties(detailDto, detail);
                        summaryDto.getDetailDtos().add(detailDto);
                    }
                }
                dto.getSummaries().add(summaryDto);
            }
        }
    }
}
