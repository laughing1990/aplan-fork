package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.bsc.domain.BscJobRemind;
import com.augurit.agcloud.bsc.mapper.BscJobRemindMapper;
import com.augurit.agcloud.bsc.sc.job.service.BscJobRemindService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.exception.SmsException;
import com.augurit.aplanmis.common.listener.builder.BscJobRemindBuilder;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaToleranceTimeInstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.item.AeaToleranceTimeInstService;
import com.augurit.aplanmis.common.shortMessage.converter.SendSmsRemindContentConverter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AeaToleranceTimeInstServiceImpl implements AeaToleranceTimeInstService {

    private static Logger logger = LoggerFactory.getLogger(AeaToleranceTimeInstServiceImpl.class);

    @Autowired
    private AeaToleranceTimeInstMapper aeaToleranceTimeInstMapper;

    @Autowired
    private BscJobRemindBuilder bscJobRemindBuilder;

    @Autowired
    private BscJobRemindService bscJobRemindService;

    @Autowired
    private BscJobRemindMapper bscJobRemindMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;

    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;

    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private SendSmsRemindContentConverter sendSmsTemplateJsonConverter;

    public void saveAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        aeaToleranceTimeInst.setCreater(SecurityContext.getCurrentUserName());
        aeaToleranceTimeInst.setCreateTime(new Date());
        aeaToleranceTimeInstMapper.insertAeaToleranceTimeInst(aeaToleranceTimeInst);
    }
    public void updateAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        aeaToleranceTimeInst.setModifier(SecurityContext.getCurrentUserName());
        aeaToleranceTimeInst.setModifyTime(new Date());
        aeaToleranceTimeInstMapper.updateAeaToleranceTimeInst(aeaToleranceTimeInst);
    }
    public void deleteAeaToleranceTimeInstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaToleranceTimeInstMapper.deleteAeaToleranceTimeInst(id);
    }
    public PageInfo<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaToleranceTimeInst> list = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(aeaToleranceTimeInst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaToleranceTimeInst>(list);
    }
    public AeaToleranceTimeInst getAeaToleranceTimeInstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaToleranceTimeInstMapper.getAeaToleranceTimeInstById(id);
    }
    public List<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        List<AeaToleranceTimeInst> list = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(aeaToleranceTimeInst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaToleranceTimeInst> getUnCompletedToleranceTimeinstsByJobTimerId(String orgId, String jobTimerId) throws Exception {
        return aeaToleranceTimeInstMapper.getUnCompletedToleranceTimeinstsByJobTimerId(orgId,jobTimerId);
    }

    @Override
    public void batchUpdateAeaToleranceTimeInst(List<AeaToleranceTimeInst> list) throws Exception {
        aeaToleranceTimeInstMapper.batchUpdateAeaToleranceTimeInst(list);
    }

    /**
     * 当办件容缺的材料补正完成且审批确认通过后，可调用此接口，结束容缺时限实例的计时
     * @param iteminstId 事项实例id
     * @throws Exception
     */
    @Override
    public void completeAeaToleranceTimeinst(String iteminstId) throws Exception {
        AeaToleranceTimeInst query = new AeaToleranceTimeInst();
        query.setOrgId(SecurityContext.getCurrentOrgId());
        query.setIsCompleted("0");
        query.setIteminstId(iteminstId);
        List<AeaToleranceTimeInst> aeaToleranceTimeInsts = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(query);
        for(int i=0,len=aeaToleranceTimeInsts.size(); i<len; i++){
            AeaToleranceTimeInst temp = new AeaToleranceTimeInst();
            temp.setToleranceTimeInstId(aeaToleranceTimeInsts.get(i).getToleranceTimeInstId());
            temp.setIsCompleted("1");
            updateAeaToleranceTimeInst(temp);
        }
    }

    @Override
    public void createToleranceSmsRemindInfo(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception {
        if(aeaToleranceTimeInst == null) return;

        //先查下是否已存在短信提醒信息，存在则不重复创建
        BscJobRemind qery = new BscJobRemind();
        qery.setTableName("AEA_TOLERANCE_TIME_INST");
        qery.setPkName("TOLERANCE_TIME_INST_ID");
        qery.setRecordId(aeaToleranceTimeInst.getToleranceTimeInstId());
        List<BscJobRemind> bscJobReminds = bscJobRemindMapper.listBscJobRemind(qery);
        if(bscJobReminds.size() > 0) return;

        //开始获取发送短信需要的相关信息
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.getAeaHiIteminstById(aeaToleranceTimeInst.getIteminstId());
        if(aeaHiIteminst != null){
            String applyinstId = null;
            if("1".equalsIgnoreCase(aeaHiIteminst.getIsSeriesApprove())){
                AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstById(aeaHiIteminst.getSeriesinstId());
                if(aeaHiSeriesinst != null){
                    applyinstId = aeaHiSeriesinst.getApplyinstId();
                }
            }else{
                AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstById(aeaHiIteminst.getStageinstId());
                if(aeaHiParStageinst != null){
                    applyinstId = aeaHiParStageinst.getApplyinstId();
                }
            }
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if (applyinstProjList == null || applyinstProjList.size() == 0)
                throw new RuntimeException("无法获取到申报项目信息！");
            String applyCode = aeaHiApplyinst.getApplyinstCode();
            String projName = applyinstProjList.get(0).getProjName();
            String projCode = applyinstProjList.get(0).getLocalCode();
            String itemName = aeaHiIteminst.getIteminstName();
            String phoneNum = null;
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiApplyinst.getLinkmanInfoId());
            if(aeaLinkmanInfo!=null)
                phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();

            String toleranceBzRemindSmsContent = sendSmsTemplateJsonConverter.getToleranceBzRemindSmsContent(projName, projCode, applyCode, itemName, phoneNum);
            if (StringUtils.isNotBlank(toleranceBzRemindSmsContent)){
                createSendSmsJobRemind(phoneNum,toleranceBzRemindSmsContent,aeaToleranceTimeInst.getToleranceTimeInstId());
            }
        }
    }

    /**
     * 创建短信通知信息对象并保存
     * @param phoneNum
     * @param remindContent
     * @param toleranceTimeInstId
     */
    private void createSendSmsJobRemind(String phoneNum,String remindContent,String toleranceTimeInstId){
        BscJobRemind acceptJobRemind = bscJobRemindBuilder.build();
        acceptJobRemind.setRemindUserIdList(phoneNum);
        acceptJobRemind.setRemindContent(remindContent);
        acceptJobRemind.setRecordId(toleranceTimeInstId);
        acceptJobRemind.setTableName("AEA_TOLERANCE_TIME_INST");
        acceptJobRemind.setPkName("TOLERANCE_TIME_INST_ID");

        try {
            bscJobRemindService.saveBscJobRemind(acceptJobRemind);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("保存短信提醒jobRemind信息失败！");
            throw new SmsException(e.getMessage());
        }
    }
}

