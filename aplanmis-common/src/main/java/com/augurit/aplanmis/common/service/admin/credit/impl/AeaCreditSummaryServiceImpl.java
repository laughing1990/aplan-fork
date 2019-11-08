package com.augurit.aplanmis.common.service.admin.credit.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
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
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditSummaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 信用管理-红黑名单管理-Service服务接口实现类
*/
@Service
@Transactional
public class AeaCreditSummaryServiceImpl implements AeaCreditSummaryService {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditSummaryServiceImpl.class);

    @Autowired
    private AeaCreditSummaryMapper aeaCreditSummaryMapper;

    @Autowired
    private AeaCreditDetailMapper aeaCreditDetailMapper;

    @Autowired
    private AeaCreditUnitInfoMapper unitInfoMapper;

    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;

    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Override
    public void saveAeaCreditSummary(AeaCreditSummary aeaCreditSummary) {
        aeaCreditSummary.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCreditSummary.setCreater(SecurityContext.getCurrentUserName());
        aeaCreditSummary.setCreateTime(new Date());
        aeaCreditSummaryMapper.insertAeaCreditSummary(aeaCreditSummary);
    }

    @Override
    public void updateAeaCreditSummary(AeaCreditSummary aeaCreditSummary) {
        aeaCreditSummary.setModifier(SecurityContext.getCurrentUserId());
        aeaCreditSummary.setModifyTime(new Date());
        aeaCreditSummaryMapper.updateAeaCreditSummary(aeaCreditSummary);
    }

    @Override
    public void deleteAeaCreditSummaryById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        String[] ids = id.split(",");
        for (String pk : ids) {
            aeaCreditSummaryMapper.deleteAeaCreditSummary(pk);

            AeaCreditDetail aeaCreditDetail = new AeaCreditDetail();
            aeaCreditDetail.setSummaryId(pk);
            List<AeaCreditDetail> aeaCreditDetails = aeaCreditDetailMapper.listAeaCreditDetail(aeaCreditDetail);
            for (AeaCreditDetail detail : aeaCreditDetails) {
                aeaCreditDetailMapper.deleteAeaCreditDetail(detail.getDetailId());
            }
        }
    }

    @Override
    public PageInfo<AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary,Page page) {

        aeaCreditSummary.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCreditSummary> list = aeaCreditSummaryMapper.listAeaCreditSummary(aeaCreditSummary);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditSummary>(list);
    }

    @Override
    public AeaCreditSummary getAeaCreditSummaryById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaCreditSummaryMapper.getAeaCreditSummaryById(id);
    }

    @Override
    public List<AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary) {

        List<AeaCreditSummary> list = aeaCreditSummaryMapper.listAeaCreditSummary(aeaCreditSummary);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaCreditSummary> listCreditSummaryByBizId(String bizType, String creditType, String bizId){

        if(StringUtils.isBlank(bizType)){
            throw new InvalidParameterException("参数bizType为空!");
        }
        if(StringUtils.isBlank(creditType)){
            throw new InvalidParameterException("参数creditType为空!");
        }
        if(StringUtils.isBlank(bizId)){
            throw new InvalidParameterException("参数bizId为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaCreditSummary summary = new AeaCreditSummary();
        summary.setRootOrgId(SecurityContext.getCurrentOrgId());
        summary.setCreditType(creditType);
        summary.setSummaryType(bizType);
        if(bizType.equals("u")){
            AeaCreditUnitInfo unit = new AeaCreditUnitInfo();
            unit.setRootOrgId(rootOrgId);
            unit.setUnitInfoId(bizId);
            List<AeaCreditUnitInfo> list = unitInfoMapper.listAeaCreditUnitInfo(unit);
            if(list!=null&&list.size()>0){
                summary.setCreditUnitInfoId(list.get(0).getCreditUnitInfoId());
            }else{
                throw new InvalidParameterException("无法在信用单位表中查询到单位!");
            }
        }else if(bizType.equals("l")){
            summary.setLinkmanInfoId(bizId);
        }
        return aeaCreditSummaryMapper.listAeaCreditSummary(summary);
    }

    @Override
    public List<AeaCreditSummary> listCreditSummaryByBizCode(String bizType, String creditType, String bizCode){

        if(StringUtils.isBlank(bizType)){
            throw new InvalidParameterException("参数bizType为空!");
        }
        if(StringUtils.isBlank(creditType)){
            throw new InvalidParameterException("参数creditType为空!");
        }
        if(StringUtils.isBlank(bizCode)){
            throw new InvalidParameterException("参数bizCode为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaCreditSummary summary = new AeaCreditSummary();
        summary.setRootOrgId(rootOrgId);
        summary.setCreditType(creditType);
        summary.setSummaryType(bizType);
        if(bizType.equals("u")){
            AeaCreditUnitInfo unit = new AeaCreditUnitInfo();
            unit.setRootOrgId(rootOrgId);
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
            linkman.setRootOrgId(rootOrgId);
            List<AeaLinkmanInfo> list = linkmanInfoMapper.findAeaLinkmanInfo(linkman);
            if(list!=null&&list.size()>0){
                summary.setLinkmanInfoId(list.get(0).getLinkmanInfoId());
            }else{
                return new ArrayList<AeaCreditSummary>();
            }
        }
        return aeaCreditSummaryMapper.listAeaCreditSummary(summary);
    }

    @Override
    public List<AeaCreditSummaryAllDto> listCreditSummaryDetailByBizId(String bizType, String bizId)throws Exception{

        List<AeaCreditSummaryAllDto> all = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> itemList = this.bscDicCodeMapper.getActiveItemsByTypeCode("AEA_CREDIT_TYPE", rootOrgId);
        if(itemList!=null&&itemList.size()>0){
            for(BscDicCodeItem item:itemList){
                AeaCreditSummaryAllDto dto = new AeaCreditSummaryAllDto();
                dto.setItemName(item.getItemName());
                dto.setItemCode(item.getItemCode());
                List<AeaCreditSummary> summaries = listCreditSummaryByBizId(bizType, item.getItemCode(), bizId);
                handSummaryDetail(rootOrgId, dto, summaries);
                all.add(dto);
            }
        }
        return all;
    }

    @Override
    public List<AeaCreditSummaryAllDto> listCreditSummaryDetailByByBizCode(String bizType, String bizCode)throws Exception{

        List<AeaCreditSummaryAllDto> all = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> itemList = this.bscDicCodeMapper.getActiveItemsByTypeCode("AEA_CREDIT_TYPE", rootOrgId);
        if(itemList!=null&&itemList.size()>0){
            for(BscDicCodeItem item:itemList){
                AeaCreditSummaryAllDto dto = new AeaCreditSummaryAllDto();
                dto.setItemName(item.getItemName());
                dto.setItemCode(item.getItemCode());
                List<AeaCreditSummary> summaries = listCreditSummaryByBizCode(bizType, item.getItemCode(), bizCode);
                handSummaryDetail(rootOrgId, dto, summaries);
                all.add(dto);
            }
        }
        return all;
    }

    private void handSummaryDetail(String rootOrgId, AeaCreditSummaryAllDto dto, List<AeaCreditSummary> summaries) throws Exception{

        if(summaries!=null&&summaries.size()>0){
            for(AeaCreditSummary summary : summaries){
                // 转换汇总表
                AeaCreditSummaryDto summaryDto = new AeaCreditSummaryDto();
                BeanUtils.copyProperties(summaryDto, summary);
                summaryDto.setLastUpdateTime(summary.getModifyTime() == null ? summary.getCreateTime() : summary.getModifyTime());
                // 转换汇总表详情
                AeaCreditDetail sdetail = new AeaCreditDetail();
                sdetail.setRootOrgId(rootOrgId);
                sdetail.setSummaryId(summary.getSummaryId());
                List<AeaCreditDetail> details = aeaCreditDetailMapper.listAeaCreditDetail(sdetail);
                if(details!=null&&details.size()>0){
                    for(AeaCreditDetail detail:details){
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

