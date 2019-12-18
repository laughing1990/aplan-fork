package com.augurit.aplanmis.common.service.receive;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.dic.ApplyinstCodeService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;

import com.augurit.aplanmis.common.service.receive.vo.RefusedRecepitVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xiaohutu--20190715
 * 回执接口
 */
public abstract class AbstractReceiveService {
    private static Logger logger = LoggerFactory.getLogger(AbstractReceiveService.class);
    /**
     * 回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执）
     */
    private static final String[] RECEIVE_TYPES = {"1", "2", "3", "4", "5"};

    private AeaHiIteminstMapper aeaHiIteminstMapper;
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    private AeaHiReceiveMapper aeaHiReceiveMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    private ApplyinstCodeService applyinstCodeService;

    /**
     * 保存回执实例
     *
     * @param applyinstIds 申请实例ID
     * @param receiptTypes 需要保存的回执类型
     * @param currentUser  操作人
     * @param comments     意见，非必须
     * @return 插入的数量
     * @throws Exception e
     */
    public boolean saveReceive(String[] applyinstIds, String[] receiptTypes, String currentUser, String comments) throws Exception {
        if (null == applyinstIds || applyinstIds.length == 0) {
            logger.info("[applyinstIds]，参数不能为空");
            return false;
        }
        if (null == receiptTypes || receiptTypes.length == 0) {
            logger.info("[receiptTypes]，参数不能为空");
            return false;
        }
        for (String applysinstId : applyinstIds) {

            //先根据申请实例ID查询事项实例
            List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applysinstId,"0");

            //在查询联系人信息
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applysinstId);
            if (null == aeaHiSmsInfo) {
                return false;
            }
            if (aeaHiIteminstList.size() == 0) {
                logger.info("[{}]参数异常，无法查询到申请实例", applysinstId);
                continue;
            }
            String itemVerIds = aeaHiIteminstList.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(","));
            for (String receiptType : receiptTypes) {
                AeaHiReceive aeaHiReceive = new AeaHiReceive();
                aeaHiReceive.setReceiveId(UUID.randomUUID().toString());
                aeaHiReceive.setApplyinstId(applysinstId);
                aeaHiReceive.setReceiptType(receiptType);
                aeaHiReceive.setOutinstId(itemVerIds);
                aeaHiReceive.setReceiveMemo(comments);
                aeaHiReceive.setReceiveUserName(aeaHiSmsInfo.getAddresseeName());
                aeaHiReceive.setReceiveCertNo(aeaHiSmsInfo.getAddresseeIdcard());
                aeaHiReceive.setReceiveUserMobile(aeaHiSmsInfo.getAddresseePhone());
                aeaHiReceive.setServiceAddress(aeaHiSmsInfo.getAddresseeAddr());
                aeaHiReceive.setCreater(currentUser);
                aeaHiReceive.setCreateTime(new Date());
                aeaHiReceive.setReceiveTime(new Date());
                aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiReceiveMapper.insertAeaHiReceive(aeaHiReceive);
            }
        }
        return true;
    }

    /**
     * 保存中介事项回执
     *
     * @param applyinstId
     * @param receiptTypes
     * @param currentUser
     * @param comments
     * @return
     * @throws Exception
     */
    public boolean saveAgentItemReceive(String applyinstId, String[] receiptTypes, String currentUser, String comments) throws Exception {
        if (StringUtils.isEmpty(applyinstId)) {
            logger.info("[applyinstIds]，参数不能为空");
            return false;
        }
        if (null == receiptTypes || receiptTypes.length == 0) {
            logger.info("[receiptTypes]，参数不能为空");
            return false;
        }
        //先根据申请实例ID查询事项实例
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId,"0");

        if (aeaHiIteminstList.size() == 0) {
            logger.info("[{}]参数异常，无法查询到申请实例", applyinstId);
            return false;
        }
        AeaLinkmanInfo applyLinkman = aeaLinkmanInfoMapper.getApplyLinkman(applyinstId);
        String userName = "";
        String idcart = "";
        String phone = "";
        String address = "";
        if (null != applyLinkman) {
            userName = applyLinkman.getLinkmanName();
            idcart = applyLinkman.getLinkmanCertNo();
            phone = applyLinkman.getLinkmanMobilePhone();
            address = applyLinkman.getLinkmanAddr();
        }
        AeaHiIteminst iteminst = aeaHiIteminstList.get(0);
        String itemVerId = iteminst.getItemVerId();
        for (String receiptType : receiptTypes) {
            AeaHiReceive aeaHiReceive = new AeaHiReceive();
            aeaHiReceive.setReceiveId(UUID.randomUUID().toString());
            aeaHiReceive.setApplyinstId(applyinstId);
            aeaHiReceive.setReceiptType(receiptType);
            aeaHiReceive.setOutinstId(itemVerId);
            aeaHiReceive.setReceiveMemo(comments);
            aeaHiReceive.setReceiveUserName(userName);
            aeaHiReceive.setReceiveCertNo(idcart);
            aeaHiReceive.setReceiveUserMobile(phone);
            aeaHiReceive.setServiceAddress(address);
            aeaHiReceive.setCreater(currentUser);
            aeaHiReceive.setCreateTime(new Date());
            aeaHiReceive.setReceiveTime(new Date());
            aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiReceiveMapper.insertAeaHiReceive(aeaHiReceive);
        }
        return true;
    }

    /**
     * 保存不收件回执
     *
     * @param refusedRecepitVo
     * @return
     * @throws Exception
     */
    public String saveRefuseReceive(RefusedRecepitVo refusedRecepitVo) throws Exception {
        // 此时aeaHiReceive 只有receiveId receiveMemo   联系人信息  缺少appliinstId 需要先保存申请实例，然后在  seriresinst  -》iteminst-->receive
        if (StringUtils.isEmpty(refusedRecepitVo.getReceiveId())) {
            refusedRecepitVo.setReceiveId(UUID.randomUUID().toString());
        }
        AeaHiReceive aeaHiReceive = new AeaHiReceive();
        BeanUtils.copyProperties(refusedRecepitVo, aeaHiReceive);
        Date createTime = new Date();
        String currentUserName = SecurityContext.getCurrentUserName();
        AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
        String applyinstId = UUID.randomUUID().toString();
        String applyinsCode = applyinstCodeService.getApplyinstCodeCurrentDay();
        aeaHiApplyinst.setApplyinstId(applyinstId);
        aeaHiApplyinst.setApplyinstCode(applyinsCode);
        aeaHiApplyinst.setApplyinstSource(refusedRecepitVo.getApplyinstSource());
        //是否串联审批，0表示并联审批，1表示串联审批
        aeaHiApplyinst.setIsSeriesApprove(refusedRecepitVo.getIsSeriesApprove());
        aeaHiApplyinst.setApplyinstMemo(aeaHiReceive.getReceiveMemo());
        aeaHiApplyinst.setApplyinstState("5");
        aeaHiApplyinst.setIsDeleted("0");
        aeaHiApplyinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiApplyinst.setCreateTime(createTime);
        aeaHiApplyinst.setLinkmanInfoId(refusedRecepitVo.getLinkmanInfoId());
        aeaHiApplyinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinstMapper.insertAeaHiApplyinst(aeaHiApplyinst);
        //20190221 新增保存申请实例与项目关联表  小糊涂
        AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
        aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
        aeaApplyinstProj.setApplyinstId(applyinstId);
        aeaApplyinstProj.setProjInfoId(refusedRecepitVo.getProjInfoId());
        aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
        aeaApplyinstProj.setCreateTime(new Date());
        aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);

        String seriesinstId = "";
        String stageinstId = "";
        String themeVerId = "";
        //并联申报
        if ("0".equals(refusedRecepitVo.getIsSeriesApprove())) {
            // AEA_HI_PAR_STAGEINST
            AeaHiParStageinst aeaHiParStageinst = new AeaHiParStageinst();
            //必输
            stageinstId = UUID.randomUUID().toString();
            aeaHiParStageinst.setStageinstId(stageinstId);
            aeaHiParStageinst.setStageId(refusedRecepitVo.getStageId());
            //获取主题实例ID;
            AeaParThemeVer aeaParThemeVer = new AeaParThemeVer();
            aeaParThemeVer.setThemeId(refusedRecepitVo.getThemeVerId());
            aeaParThemeVer.setIsActive("0");
            aeaParThemeVer.setIsDeleted("0");
            aeaParThemeVer.setIsPublish("1");
            aeaParThemeVer.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaParThemeVer> aeaParThemeVers = aeaParThemeVerMapper.listAeaParThemeVer(aeaParThemeVer);
            themeVerId = aeaParThemeVers.get(0).getThemeVerId();
            aeaHiParStageinst.setThemeVerId(themeVerId);
            aeaHiParStageinst.setStartTime(createTime);
            aeaHiParStageinst.setCreater(SecurityContext.getCurrentUserId());
            aeaHiParStageinst.setCreateTime(createTime);

            aeaHiParStageinst.setEndTime(createTime);
            //状态 阶段状态：0征求中、1无需征求、2已征求、3已接件（受理）、4已办结  环节状态：5已发起、6审批中、7整改、8通过、9不通过（项目终止）
            //aeaHiParStageinst.setStageinstState(refusedRecepitVo.getStageinstState());
            aeaHiParStageinst.setStageinstMemo(aeaHiReceive.getReceiveMemo());
            aeaHiParStageinst.setApplyinstId(applyinstId);
            aeaHiParStageinst.setAppinstId(applyinsCode);
            aeaHiParStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiParStageinstMapper.insertAeaHiParStageinst(aeaHiParStageinst);
        } else {//串联审批  单项申报
            AeaHiSeriesinst aeaHiSeriesinst = new AeaHiSeriesinst();
            seriesinstId = UUID.randomUUID().toString();
            aeaHiSeriesinst.setSeriesinstId(seriesinstId);
            //行政服务中心 业务流程模板实例ID
            aeaHiSeriesinst.setAppinstId(refusedRecepitVo.getItemVerId());
            aeaHiSeriesinst.setApplyinstId(applyinstId);
            aeaHiSeriesinst.setStartTime(createTime);
            aeaHiSeriesinst.setEndTime(createTime);
            aeaHiSeriesinst.setSeriesinstState("");
            aeaHiSeriesinst.setSeriesinstMemo(aeaHiReceive.getReceiveMemo());
            aeaHiSeriesinst.setCreater(currentUserName);
            aeaHiSeriesinst.setCreateTime(createTime);
            aeaHiSeriesinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiSeriesinstMapper.insertAeaHiSeriesinst(aeaHiSeriesinst);
        }
        //20190221 修改itemId--->itemVerId xiaohutu
        if (!StringUtils.isEmpty(refusedRecepitVo.getItemVerId())) {
            AeaHiIteminst aeaHiIteminst = new AeaHiIteminst();

            String[] itemVerIds = refusedRecepitVo.getItemVerId().split(",");
            if (itemVerIds != null && itemVerIds.length > 0) {
                List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByItemVerIds(itemVerIds);
                if (aeaItemBasics.size() > 0) {
                    for (AeaItemBasic itemBasic : aeaItemBasics) {
                        aeaHiIteminst.setIteminstId(UUID.randomUUID().toString());
                        aeaHiIteminst.setItemId(itemBasic.getItemId());
                        aeaHiIteminst.setItemVerId(itemBasic.getItemVerId());
                        aeaHiIteminst.setApproveOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiIteminst.setIsSeriesApprove(refusedRecepitVo.getIsSeriesApprove());
                        aeaHiIteminst.setCreater(SecurityContext.getCurrentUserId());
                        aeaHiIteminst.setCreateTime(createTime);
                        aeaHiIteminst.setIteminstName(itemBasic.getItemName());
                        aeaHiIteminst.setIteminstCode(applyinstCodeService.getApplyinstCodeCurrentDay());
                        if ("1".equals(refusedRecepitVo.getIsSeriesApprove())) {
                            // 并联时不保存该字段
                            aeaHiIteminst.setSeriesinstId(seriesinstId);
                        }
                        if ("0".equals(refusedRecepitVo.getIsSeriesApprove())) {
                            aeaHiIteminst.setStageinstId(stageinstId);
                            aeaHiIteminst.setThemeVerId(themeVerId);
                        }
                        aeaHiIteminst.setIteminstState(refusedRecepitVo.getIteminstState());
                        aeaHiIteminst.setSignTime(createTime);
                        aeaHiIteminst.setStartTime(createTime);
                        aeaHiIteminst.setEndTime(new Date());
                        aeaHiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiIteminstMapper.insertAeaHiIteminst(aeaHiIteminst);
                    }
                }
            }
        }
        aeaHiReceive.setCreater(SecurityContext.getCurrentUserId());
        aeaHiReceive.setCreateTime(createTime);
        aeaHiReceive.setReceiveTime(createTime);
        aeaHiReceive.setApplyinstId(applyinstId);
        // 20190221 itemId--->itemVerId
        aeaHiReceive.setOutinstId(refusedRecepitVo.getItemVerId());
        aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());

        aeaHiReceiveMapper.insertAeaHiReceive(aeaHiReceive);
        refusedRecepitVo.setReceiveId(aeaHiReceive.getReceiveId());

        return aeaHiReceive.getApplyinstId();
    }


    @Autowired
    public void setAeaHiIteminstMapper(AeaHiIteminstMapper aeaHiIteminstMapper) {
        this.aeaHiIteminstMapper = aeaHiIteminstMapper;
    }

    @Autowired
    public void setAeaLinkmanInfoMapper(AeaLinkmanInfoMapper aeaLinkmanInfoMapper) {
        this.aeaLinkmanInfoMapper = aeaLinkmanInfoMapper;
    }

    @Autowired
    public void setAeaHiReceiveMapper(AeaHiReceiveMapper aeaHiReceiveMapper) {
        this.aeaHiReceiveMapper = aeaHiReceiveMapper;
    }
}
