package com.augurit.aplanmis.front.certificate.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttStoreDb;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.InOutType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaApplyinstUnitProj;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendApply;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendBean;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaApplyinstUnitProjMapper;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsSendApplyMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsSendItemMapper;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.mapper.AeaLogItemStateHistMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.front.certificate.vo.CertListAndUnitVo;
import com.augurit.aplanmis.front.certificate.vo.CertLogisticsDetailResultVo;
import com.augurit.aplanmis.front.certificate.vo.CertLogisticsDetailVo;
import com.augurit.aplanmis.front.certificate.vo.CertOutinstVo;
import com.augurit.aplanmis.front.certificate.vo.CertReceivedVo;
import com.augurit.aplanmis.front.certificate.vo.CertRegistrationItemVo;
import com.augurit.aplanmis.front.certificate.vo.CertRegistrationVo;
import com.augurit.aplanmis.front.certificate.vo.CertinstParamVo;
import com.augurit.aplanmis.front.third.logistics.LogisticsService;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderDetailVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderResultVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RestCertificateService {
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaHiSmsSendItemMapper aeaHiSmsSendItemMapper;
    @Autowired
    private AeaHiSmsSendApplyMapper aeaHiSmsSendApplyMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaLogItemStateHistMapper aeaLogItemStateHistMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private BscAttMapper bscAttService;
    @Autowired
    private UploaderFactory uploaderFactory;
    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private LogisticsService logisticsService;

    public List<AeaHiSmsSendItem> confirmReceived(CertReceivedVo certReceivedVo) throws Exception {
        Assert.hasText(certReceivedVo.getApplyinstId(), "申报实例id不能为空!");
        Assert.isTrue(certReceivedVo.getCertRegistrationItemVos().size() > 0, "应该至少选中一个事项实例进行出件");

        AeaHiSmsSendBean sendBean = certReceivedVo.getSendBean();
        // 判断是否委托上传
        if (Status.ON.equals(certReceivedVo.getSendBean().getIsConsigner())) {
            mergeConsignerAtt(certReceivedVo, sendBean);
        }

        // 更新或保存 申报出件记录 AeaHiSmsSendApply
        String sendApplyId = saveOrUpdateAeaHiSmsSendApply(sendBean, certReceivedVo.getApplyinstId());
        List<AeaHiSmsSendItem> aeaHiSmsSendItems = new ArrayList<>();
        if (ApplyType.SERIES.getValue().equals(certReceivedVo.getIsSeriesApprove())) {
            AeaHiSmsSendItem aeaHiSmsSendItem = confirmReceived4Series(certReceivedVo.getIsOnceSend(), sendApplyId, sendBean, certReceivedVo.getCertRegistrationItemVos());
            aeaHiSmsSendItems.add(aeaHiSmsSendItem);
        } else {// 并联
            List<AeaHiSmsSendItem> aeaHiSmsSendItemList = confirmReceived4Parallel(certReceivedVo.getIsOnceSend(), sendApplyId, sendBean, certReceivedVo.getCertRegistrationItemVos());
            aeaHiSmsSendItems.addAll(aeaHiSmsSendItemList);
        }

        // 判断并更新申报实例是否已全部出件
        checkAllSmsItemSendThenUpdateAeaHiApplyinst(certReceivedVo.getApplyinstId(), certReceivedVo.getIsSeriesApprove());
        return aeaHiSmsSendItems;
    }

    public String mailOrder(CertReceivedVo certReceivedVo) throws Exception {
        // 保存出件信息
        List<AeaHiSmsSendItem> aeaHiSmsSendItems = confirmReceived(certReceivedVo);
        // 物流下单
        LogisticsOrderVo logisticsOrderVo = new LogisticsOrderVo();
        BeanUtils.copyProperties(logisticsOrderVo, certReceivedVo.getSendBean());
        LogisticsOrderResultVo order = logisticsService.order(logisticsOrderVo);
        Assert.notNull(order, "物流下单失败");
        Assert.notNull(order, "物流下单获取快递单号失败");
        for (AeaHiSmsSendItem aeaHiSmsSendItem : aeaHiSmsSendItems) {
            aeaHiSmsSendItem.setExpressNum(order.getExpressNum());
            aeaHiSmsSendItem.setOrderId(order.getOrderId());
            aeaHiSmsSendItemMapper.updateAeaHiSmsSendItem(aeaHiSmsSendItem);
        }
        return order.getExpressNum();
    }

    public CertLogisticsDetailResultVo logisticsDetail(CertLogisticsDetailVo certLogisticsDetailVo) throws Exception {
        CertLogisticsDetailResultVo resultVo = new CertLogisticsDetailResultVo();
        List<LogisticsOrderDetailVo> detail = logisticsService.detail(certLogisticsDetailVo.getExpressNum());
        resultVo.setLogisticsOrderDetails(detail);

        AeaHiSmsSendItem param = new AeaHiSmsSendItem();
        param.setApplyinstId(certLogisticsDetailVo.getApplyinstId());
        param.setExpressNum(certLogisticsDetailVo.getExpressNum());
        param.setIteminstId(certLogisticsDetailVo.getIteminstId());
        List<AeaHiSmsSendItem> aeaHiSmsSendItems = aeaHiSmsSendItemMapper.listAeaHiSmsSendItem(param);
        Assert.isTrue(aeaHiSmsSendItems.size() > 0, "无法找到该事项的出件记录");

        if (aeaHiSmsSendItems.size() > 1) {
            log.warn("事项应该只存在一条出件记录, 但数据库中发现两条, 默认取第一条, iteminstId: {}, applyinstId: {}", certLogisticsDetailVo.getIteminstId(), certLogisticsDetailVo.getApplyinstId());
        }
        resultVo.setAeaHiSmsSendItem(aeaHiSmsSendItems.get(0));
        List<AeaHiCertinst> certinsts = aeaHiCertinstMapper.listAeaHiCertinstByIteminstIds(new String[]{certLogisticsDetailVo.getIteminstId()});
        Assert.notEmpty(certinsts, "无法找到该事项的证照实例, iteminstId: " + certLogisticsDetailVo.getIteminstId());

        resultVo.setAeaHiCertinsts(certinsts);
        return resultVo;
    }

    private void mergeConsignerAtt(CertReceivedVo certReceivedVo, AeaHiSmsSendBean sendBean) {
        AeaHiSmsSendBean consignerForm = certReceivedVo.getConsignerForm();
        sendBean.setAddresseeName(consignerForm.getAddresseeName());
        sendBean.setAddresseePhone(consignerForm.getAddresseePhone());
        sendBean.setAddresseeIdcard(consignerForm.getAddresseeIdcard());
        sendBean.setAddresseeProvince(consignerForm.getAddresseeProvince());
        sendBean.setAddresseeCity(consignerForm.getAddresseeCity());
        sendBean.setAddresseeCounty(consignerForm.getAddresseeCounty());
        sendBean.setAddresseeAddr(consignerForm.getAddresseeAddr());
    }

    private String saveOrUpdateAeaHiSmsSendApply(AeaHiSmsSendBean sendBean, String applyinstId) throws Exception {
        AeaHiSmsSendApply sendApply = new AeaHiSmsSendApply();
        BeanUtils.copyProperties(sendApply, sendBean);
        // 暂时用时间字符串
        sendApply.setSendApplyCode(getDateString());
        sendApply.setCreater(SecurityContext.getCurrentUserId());
        sendApply.setCreateTime(new Date());
        sendApply.setRootOrgId(SecurityContext.getCurrentOrgId());

        AeaHiSmsSendApply alreadExistsAeaHiSmsSendApply = aeaHiSmsSendApplyMapper.getAeaHiSmsSendApplyByApplyinstId(applyinstId);
        if (alreadExistsAeaHiSmsSendApply == null) {
            sendApply.setSendApplyId(UuidUtil.generateUuid());
            sendApply.setIssueTime(new Date());
            aeaHiSmsSendApplyMapper.insertAeaHiSmsSendApply(sendApply);
        } else {
            sendApply.setSendApplyId(alreadExistsAeaHiSmsSendApply.getSendApplyId());
            sendApply.setIssueTime(new Date());
            aeaHiSmsSendApplyMapper.updateAeaHiSmsSendApply(sendApply);
        }
        return sendApply.getSendApplyId();
    }

    private String getDateString() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private AeaHiSmsSendItem confirmReceived4Series(String isOneSend, String sendApplyId, AeaHiSmsSendBean sendBean, List<CertRegistrationItemVo> certRegistrationItemVos) throws Exception {
        Assert.isTrue(certRegistrationItemVos.size() == 1, "应该有且仅有一个事项申报实例");

        AeaHiSmsSendItem aeaHiSmsSendItem = assembleAeaHiSmsSendItem(sendBean, isOneSend, sendApplyId, certRegistrationItemVos.get(0));
        aeaHiSmsSendItemMapper.insertAeaHiSmsSendItem(aeaHiSmsSendItem);

        // 修改iteminst状态
        updateIteminstSendStatus(aeaHiSmsSendItem.getIteminstId());

        // 插入aea_log_item_state_hist记录
        insertIntoItemStateHist(Collections.singletonList(aeaHiSmsSendItem));
        return aeaHiSmsSendItem;
    }

    private List<AeaHiSmsSendItem> confirmReceived4Parallel(String isOneSend, String sendApplyId, AeaHiSmsSendBean sendBean, List<CertRegistrationItemVo> certRegistrationItemVos) throws Exception {
        Map<String, AeaHiSmsSendItem> aeaHiSmsSendItemMap = new HashMap<>(certRegistrationItemVos.size());
        for (CertRegistrationItemVo vo : certRegistrationItemVos) {
            AeaHiSmsSendItem aeaHiSmsSendItem = assembleAeaHiSmsSendItem(sendBean, isOneSend, sendApplyId, vo);

            AeaHiSmsSendItem fromMap = aeaHiSmsSendItemMap.get(aeaHiSmsSendItem.getIteminstId());
            if (fromMap != null && StringUtils.isNotBlank(fromMap.getInoutinstId())) {
                fromMap.setInoutinstId(fromMap.getInoutinstId() + ", " + aeaHiSmsSendItem.getInoutinstId());
            } else {
                aeaHiSmsSendItemMap.put(aeaHiSmsSendItem.getIteminstId(), aeaHiSmsSendItem);
            }
            updateIteminstSendStatus(vo.getIteminstId());
        }
        if (aeaHiSmsSendItemMap.size() > 0) {
            List<AeaHiSmsSendItem> aeaHiSmsSendItems = new ArrayList<>();
            aeaHiSmsSendItems.addAll(aeaHiSmsSendItemMap.values());
            // 插入senditem记录
            aeaHiSmsSendItemMapper.batchInsertAeaHiSmsSendItem(aeaHiSmsSendItems);
            //插入aea_log_item_state_hist记录
            insertIntoItemStateHist(aeaHiSmsSendItems);
            return aeaHiSmsSendItems;
        }
        return new ArrayList<>();
    }

    /**
     * 更新iteminst的出件状态
     */
    private void updateIteminstSendStatus(String iteminstId) throws Exception {
        AeaHiIteminst hiIteminst = new AeaHiIteminst();
        hiIteminst.setIteminstId(iteminstId);
        hiIteminst.setIsSmsSend("1");
        hiIteminst.setSmsSendTime(new Date());
        aeaHiIteminstMapper.updateAeaHiIteminst(hiIteminst);
    }

    /**
     * 插入记录aea_log_item_state_hist
     */
    private void insertIntoItemStateHist(List<AeaHiSmsSendItem> senditems) {
        List<AeaLogItemStateHist> result = new ArrayList<>();
        for (AeaHiSmsSendItem sendItem : senditems) {
            AeaLogItemStateHist stateHist = new AeaLogItemStateHist();
            stateHist.setStateHistId(UUID.randomUUID().toString());
            stateHist.setIteminstId(sendItem.getIteminstId());
            stateHist.setIsFlowTrigger("0");
            stateHist.setTriggerTime(new Date());
            stateHist.setOpsUserId(SecurityContext.getCurrentUserId());
            stateHist.setOpsUserName(SecurityContext.getCurrentUserName());
            stateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            stateHist.setOpsFillTime(new Date());
            stateHist.setOpsUserOpinion("事项出件");
            stateHist.setOpsMemo("更新事项是否出件状态, is_sms_send=1");
            stateHist.setBusTableName("AEA_HI_SMS_SEND_ITEM");
            stateHist.setBusPkName("SEND_ITEM_ID");
            stateHist.setBusRecordId(sendItem.getSendItemId());
            result.add(stateHist);
        }
        if (result.size() > 0) {
            aeaLogItemStateHistMapper.batchInsertAeaLogItemStateHist(result);
        }
    }

    /**
     * 判断是否已经全部出完件
     *
     */
    private void checkAllSmsItemSendThenUpdateAeaHiApplyinst(String applyinstId, String isSeriesApprove) throws Exception {
        boolean allSend;
        if (ApplyType.SERIES.getValue().equals(isSeriesApprove)) {//单项申报默认出证都是全部出证
            allSend = true;
        } else {
            // 需要出证的事项数量
            int needSendCount = aeaHiSmsSendItemMapper.getNeedSendCount(applyinstId);
            // 已经出证的事项数量
            int hadSendCount = aeaHiSmsSendItemMapper.countSendItemByApplyinstId(applyinstId);
            allSend = (needSendCount == hadSendCount);
        }
        // 修改applyiinst状态
        AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
        aeaHiApplyinst.setApplyinstId(applyinstId);
        aeaHiApplyinst.setIsSmsSend(allSend ? Status.ON : Status.OFF);
        aeaHiApplyinst.setSmsSendTime(new Date());
        aeaHiApplyinstMapper.updateAeaHiApplyinst(aeaHiApplyinst);
    }

    /**
     * 组装 AeaHiSmsSendItem
     */
    private AeaHiSmsSendItem assembleAeaHiSmsSendItem(AeaHiSmsSendBean sendBean, String isOnceSend, String sendApplyId, CertRegistrationItemVo certRegistrationItemVo) throws Exception {
        AeaHiSmsSendItem aeaHiSmsSendItem = new AeaHiSmsSendItem();
        BeanUtils.copyProperties(aeaHiSmsSendItem, sendBean);
        // 暂时用datestr
        aeaHiSmsSendItem.setSendItemCode(getDateString());
        aeaHiSmsSendItem.setSendItemId(UuidUtil.generateUuid());
        aeaHiSmsSendItem.setSendApplyId(sendApplyId);
        aeaHiSmsSendItem.setIsOnceSend(isOnceSend);
        aeaHiSmsSendItem.setIssueTime(new Date());
        aeaHiSmsSendItem.setCreater(SecurityContext.getCurrentUserId());
        aeaHiSmsSendItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiSmsSendItem.setCreateTime(new Date());
        aeaHiSmsSendItem.setIteminstId(certRegistrationItemVo.getIteminstId());
        aeaHiSmsSendItem.setInoutinstId(certRegistrationItemVo.getInoutinstId());
        if (StringUtils.isBlank(aeaHiSmsSendItem.getIsConsigner())) {
            aeaHiSmsSendItem.setIsConsigner(Status.OFF);
        }
        if (StringUtils.isBlank(aeaHiSmsSendItem.getWindowUserId())) {
            aeaHiSmsSendItem.setWindowUserId(SecurityContext.getCurrentUserId());
        }
        if (StringUtils.isBlank(aeaHiSmsSendItem.getWindowUserName())) {
            aeaHiSmsSendItem.setWindowUserName(SecurityContext.getCurrentUserName());
        }
        aeaHiSmsSendItem.setWindowHandleTime(new Date());
        return aeaHiSmsSendItem;
    }

    public CertRegistrationVo getCertificationInfo(String applyinstId) throws Exception {
        CertRegistrationVo certRegistrationVo = new CertRegistrationVo();

        List<CertRegistrationItemVo> registrationItems = getRegistrationItems(applyinstId);
        certRegistrationVo.setCertRegistrationItemVos(registrationItems);

        AeaHiSmsInfo smsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoByApplyinstId(applyinstId);
        Assert.notNull(smsInfo, "根据申报实例没有找到对应的领件人信息, applyinstId: " + applyinstId);

        // 寄件单位和电话
        AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
        smsInfo.setSenderName(currentUserWindow.getWindowName());
        smsInfo.setSenderPhone(currentUserWindow.getLinkPhone());

        certRegistrationVo.setSmsInfo(smsInfo);
        return certRegistrationVo;

    }

    private List<CertRegistrationItemVo> getRegistrationItems(String applyinstId) throws Exception {
        ArrayList<CertRegistrationItemVo> certRegistrationItemVos = new ArrayList<>();
        List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        // 过滤剩下办结通过和容缺通过的
        Map<String, AeaHiIteminst> aeaHiIteminstMap = iteminsts.stream().filter(
                inst -> ItemStatus.AGREE_TOLERANCE.getValue().equals(inst.getIteminstState()) ||
                        ItemStatus.AGREE.getValue().equals(inst.getIteminstState())
        ).collect(Collectors.toMap(AeaHiIteminst::getIteminstId, iteminst -> iteminst));

        if (aeaHiIteminstMap.size() == 0) {
            return certRegistrationItemVos;
        }

        Map<String, AeaHiSmsSendItem> aeaHiSmsSendItemMap = aeaHiSmsSendItemMapper.getAeaHiSmsSendItemListByApplyinstId(applyinstId).stream().collect(Collectors.toMap(AeaHiSmsSendItem::getIteminstId, item -> item));

        List<AeaHiCertinst> certinsts = aeaHiCertinstMapper.listAeaHiCertinstByIteminstIds(aeaHiIteminstMap.keySet().toArray(new String[0]));
        certinsts.forEach(certinst -> {
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMap.get(certinst.getIteminstId());
            CertRegistrationItemVo vo = CertRegistrationItemVo.from(aeaHiIteminst, certinst);
            AeaHiSmsSendItem aeaHiSmsSendItem = aeaHiSmsSendItemMap.get(certinst.getIteminstId());
            if (aeaHiSmsSendItem != null
                    && StringUtils.isNotBlank(aeaHiSmsSendItem.getInoutinstId())
                    && aeaHiSmsSendItem.getInoutinstId().contains(certinst.getInoutinstId())) {
                vo.setHandled(true);
                vo.setExpressNum(aeaHiSmsSendItem.getExpressNum());
                vo.setPostSignTime(aeaHiSmsSendItem.getPostSignTime());
            }
            certRegistrationItemVos.add(vo);
        });
        return certRegistrationItemVos;
    }

    public String uploadConsignerAtt(String applyinstId, HttpServletRequest request) throws Exception {
        StringBuilder detailId = new StringBuilder();
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            fileUtilsService.uploadAttachments("AEA_HI_APPLYINST", "APPLYINST_ID", applyinstId, files);
        }
        List<BscAttFileAndDir> fileAndDirs = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST", "APPLYINST_ID", new String[]{applyinstId});
        if (fileAndDirs.size() > 0) {
            for (BscAttFileAndDir att : fileAndDirs) {
                detailId.append(att.getFileId()).append(",");
            }
            detailId = new StringBuilder(detailId.substring(0, detailId.length() - 1));
        }
        return detailId.toString();
    }

    public void delelteConsignerAtt(String detailIds) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
        String[] ids = detailIds.split(",");
        fileUtilsService.deleteAttachments(ids);
    }

    public List<BscAttFileAndDir> getMatAttDetailByApplyinstId(String applyinstId) throws Exception {
        return bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST", "APPLYINST_ID", new String[]{applyinstId});
    }

    public void updateCertInfo(CertinstParamVo param) throws Exception {
        aeaHiCertinstMapper.updateAeaHiCertinst(param);
    }

    public void createCertInfo(CertinstParamVo paramVo) throws Exception {
        // 保存证照实例
        String certinstId = UUID.randomUUID().toString();
        paramVo.setCertinstId(certinstId);
        //paramVo.setCertinstCode(getDateString());
        paramVo.setCreater(SecurityContext.getCurrentUserId());
        paramVo.setCreateTime(new Date());
        paramVo.setRootOrgId(SecurityContext.getCurrentOrgId());
        paramVo.setQualLevelId(null);
        aeaHiCertinstMapper.insertAeaHiCertinst(paramVo);

        //iteminstId查询当前inout记录
        AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(paramVo.getIteminstId());
        AeaItemInout itemInout = new AeaItemInout();
        itemInout.setItemVerId(iteminst.getItemVerId());
        itemInout.setFileType("cert");
        itemInout.setIsInput("0");
        List<AeaItemInout> aeaItemInouts = aeaItemInoutMapper.listAeaItemInout(itemInout);

        // 插入一条item_inoutinst
        AeaHiItemInoutinst itemInoutinst = new AeaHiItemInoutinst();
        itemInoutinst.setInoutinstId(UUID.randomUUID().toString());
        itemInoutinst.setIteminstId(paramVo.getIteminstId());
        itemInoutinst.setItemInoutId(aeaItemInouts.get(0).getInoutId());
        itemInoutinst.setCertinstId(certinstId);
        itemInoutinst.setCreater(SecurityContext.getCurrentUserId());
        itemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        itemInoutinst.setCreateTime(new Date());
        itemInoutinst.setIsCollected("1");
        itemInoutinst.setIsParIn("0");
        itemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(itemInoutinst);

    }

    public ModelAndView preview(String detailId, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        BscAttDetail att = bscAttService.getAttDetailByDetailId(detailId);
        if (att != null) {
            //文档类型使用NTKO预览
            if (Pattern.matches("(?i).+\\.(doc|docx|xls|xlsx|pdf|ppt|pptx)$", att.getAttName())) {
                String url = "/rest/mats/att/read";
                String jumpUrl = "/rest/mats/att/ntko?online=true&suffixName=." + att.getAttFormat() + "&fileUrl=" + url + "?detailId=" + detailId;
                jumpUrl = URLEncoder.encode(jumpUrl, "UTF-8");
                modelAndView.addObject("jumpUrl", jumpUrl);
                modelAndView.setViewName("templates/ntko/ntkoOpenWin");
                return modelAndView;
            }
            if (Pattern.matches("(?i).+\\.(txt)$", att.getAttName())) {
                redirectAttributes.addAttribute("detailId", detailId);
                modelAndView.setViewName("redirect:/rest/mats/att/read");
                return modelAndView;
            }
            //其他类型直接下载
            if (!Pattern.matches("(?i).+\\.(png|jpg|jpeg|bmp|gif|svg|tiff|txt)$", att.getAttName())) {
                redirectAttributes.addAttribute("detailIds", detailId);
                modelAndView.setViewName("redirect:/rest/mats/att/download");
                return modelAndView;
            }
            //图片类型使用jquery-viewer预览
            modelAndView.setViewName("ui-jsp/agcloud/bsc/yunpan/showFile");
            String atName = att.getAttName();
            String attFormat = att.getAttFormat();
            byte[] content;
            String base64Content = null;
            try {
                if (UploadType.MONGODB.getValue().equals(att.getStoreType())) {
                    MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploaderFactory.create(att.getStoreType());
                    content = mongoDbAchieve.getDownloadBytes(att.getDetailId());

                    if (content != null && content.length > 0) {
                        Base64.Encoder encoder = Base64.getEncoder();
                        base64Content = encoder.encodeToString(content);
                    } else {
                        modelAndView.addObject("emtpyResult", "1");
                    }
                } else if (UploadType.DATABASE.getValue().equals(att.getStoreType())) {
                    List<BscAttStoreDb> stores = bscAttService.findAttStoreDbByIds(att.getDetailId());
                    content = stores.get(0).getAttContent();
                    if (content != null && content.length > 0) {
                        Base64.Encoder encoder = Base64.getEncoder();
                        base64Content = encoder.encodeToString(content);
                    } else {
                        modelAndView.addObject("emtpyResult", "1");
                    }
                }
                Assert.notNull(base64Content, "内容为空");

                base64Content = base64Content.replaceAll("\r\n|\n", "");
                String fullBase64 = "data:image/" + attFormat + ";base64," + base64Content;
                modelAndView.addObject("fullBase64", fullBase64);
                modelAndView.addObject("fileName", atName);
                modelAndView.addObject("fileType", attFormat);
                modelAndView.addObject("emtpyResult", "0");
            } catch (Exception e) {
                modelAndView.addObject("emtpyResult", "1");
            }
        } else {
            modelAndView.addObject("emtpyResult", "1");
        }
        return modelAndView;
    }

    /*public Map<String, Object> getCertBasicInfo() {
        Map<String, Object> result = new HashMap<>();
        AeaCert cert = new AeaCert();
        cert.setCertHolder("p");
        List<AeaCert> certList = aeaCertMapper.listAeaCert(cert);

        List<AeaUnitInfo> unitInfoList = aeaUnitInfoMapper.listAeaUnitInfo(new AeaUnitInfo());
        result.put("certList", certList);
        result.put("orgList", unitInfoList);

        return result;
    }*/

    //上传证照实例附件
    public String uploadCert(CertinstParamVo vo, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(vo.getCertinstId()) && StringUtils.isBlank(vo.getCertId())) {
            return null;
        }
        if (StringUtils.isBlank(vo.getCertinstId())) {
            //第一次上传，初始化证照实例列表
            AeaCert cert = aeaCertMapper.getAeaCertById(vo.getCertId(), SecurityContext.getCurrentOrgId());
            if (null == cert) return null;
            vo.setCertinstId(UUID.randomUUID().toString());
            vo.setCertinstCode(cert.getCertCode());
            vo.setCertinstName(cert.getCertName());
            //颁发单位信息
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                vo.setIssueOrgId(opuOmOrg.getOrgId());
                vo.setIssueOrgName(opuOmOrg.getOrgName());
            }
            this.createCertInfo(vo);

        }
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            fileUtilsService.uploadAttachments("AEA_HI_CERTINST", "CERTINST_ID", vo.getCertinstId(), files);
        }

        return vo.getCertinstId();

    }


    //根据事项实例获取证照实例列表
    public List<AeaHiCertinst> getCertinstListByIteminstId(String iteminstId) {
        return aeaHiCertinstMapper.getAeaHiCertinstByIteminstId(iteminstId);
    }


    //根据事项实例ID获取事项下定义的证照列表；单位ID及名字，项目ID及名字
    public CertListAndUnitVo getcertCategoryList(String iteminstId, String applyinstId, Boolean isFilter, String rootOrgId) throws Exception {
        CertListAndUnitVo vo = new CertListAndUnitVo();
        List<AeaCert> list = new ArrayList<>();
        vo.setCertList(list);
        AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        if (null == iteminst) return vo;
        String itemVerId = iteminst.getItemVerId();
        //获取证照定义 --证照IDs
        List<String> certIds = aeaItemInoutMapper.getCertIdListByItemVerId(itemVerId, rootOrgId);
        if (certIds.size() == 0) return vo;
        //获取证照列表
        list = aeaCertMapper.listAeaCertByIds(certIds);

        if (!list.isEmpty()) {
            vo.setCertList(list);
        }


        //回显企业/单位信息和审批部门信息

        List<AeaApplyinstUnitProj> unitProjByApplyinstId = aeaApplyinstUnitProjMapper.getAeaApplyinstUnitProjByApplyinstId(applyinstId);
        if (!unitProjByApplyinstId.isEmpty()) {
            String[] unitProjIds = unitProjByApplyinstId.stream().map(AeaApplyinstUnitProj::getUnitProjId).toArray(String[]::new);

            if (unitProjIds.length > 0) {
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.getAeaUnitProjByUnitProjIds(unitProjIds);
                if (!aeaUnitInfos.isEmpty()) {
                    String unitId = aeaUnitInfos.stream().filter(aea -> "1".equals(aea.getIsOwner())).map(AeaUnitInfo::getUnitInfoId).collect(Collectors.joining(","));
                    String applicant = aeaUnitInfos.stream().filter(aea -> "1".equals(aea.getIsOwner())).map(AeaUnitInfo::getApplicant).collect(Collectors.joining(","));
                    vo.setApplicant(applicant);
                    vo.setUnitInfoId(unitId);

                }
            }
        }
        List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.listProjByApplyinstId(applyinstId);
        if (!aeaProjInfos.isEmpty()) {
            String projId = aeaProjInfos.stream().map(AeaProjInfo::getProjInfoId).collect(Collectors.joining(","));
            String projName = aeaProjInfos.stream().map(AeaProjInfo::getProjName).collect(Collectors.joining(","));
            vo.setProjInfoId(projId);
            vo.setProjName(projName);
        }
        //颁发单位信息
        String currentUserId = SecurityContext.getCurrentUserId();
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(currentUserId);
        if (!opuOmOrgs.isEmpty()) {
            OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
            vo.setIssueOrgId(opuOmOrg.getOrgId());
            vo.setIssueOrgName(opuOmOrg.getOrgName());
        }
        if (isFilter) {
            //过滤掉已经实例化的，每个证照定义只能实例化一次--
            //查询事项实例下的证照实例
            List<AeaHiCertinst> certList = aeaHiCertinstMapper.getAeaHiCertinstByIteminstId(iteminstId);
            //存在证照实例，过滤
            if (!certList.isEmpty()) {
                //已经实例化的证照ID
                String[] strings = certList.stream().map(AeaHiCertinst::getCertId).toArray(String[]::new);
                //过滤掉已经实例化的证照定义
                List<AeaCert> collect = list.stream().filter(cert -> Arrays.binarySearch(strings, cert.getCertId()) < 0).collect(Collectors.toList());
                //如果过滤后==null，表示 定义全部实例化，不能返回null,方便页面处理
                vo.setCertList(collect);
            }
        }
        return vo;

    }

    //批量/单项删除证照
    public void deleteCertinst(String[] certinstIds) throws Exception {

        if (null != certinstIds && certinstIds.length > 0) {
            //先删除item_inout_inst
            aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinstByCertinstIds(certinstIds);
            String certinstId = String.join(",", certinstIds);
            //再删除附件
            List<BscAttFileAndDir> fileAndDirList = fileUtilsService.getBscAttFileAndDirListByinstId(certinstId, "CERTINST_ID", "AEA_HI_CERTINST");
            if (!fileAndDirList.isEmpty()) {
                String[] strings = fileAndDirList.stream().map(BscAttFileAndDir::getFileId).toArray(String[]::new);
                fileUtilsService.deleteAttachments(strings);
            }
            //在删除 certinst
            aeaHiCertinstMapper.batchDeleteAeaHiCertinst(certinstIds);

        }
    }

    public AeaHiSmsSendItem getSmsSendItemDetail(String applyinstId, String iteminstId) throws Exception {
        AeaHiSmsSendItem search = new AeaHiSmsSendItem();
        search.setIteminstId(iteminstId);
        search.setApplyinstId(applyinstId);
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiSmsSendItem> list = aeaHiSmsSendItemMapper.listAeaHiSmsSendItem(search);
        if (list.size() == 0) {
            throw new Exception("找不到该事项出证记录");
        }
        return list.get(0);
    }

    public void updateSendItemInfo(AeaHiSmsSendItem sendItem) throws Exception {
        aeaHiSmsSendItemMapper.updateAeaHiSmsSendItem(sendItem);
    }

    public List<CertOutinstVo> viewOutMaterials(String iteminstId, String applyinstId) throws Exception {
        String[] matinstIds = aeaHiItemInoutinstMapper.listOutMaterialsByIteminstId(iteminstId, SecurityContext.getCurrentOrgId())
                .stream().map(AeaHiItemInoutinst::getMatinstId).toArray(String[]::new);
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(matinstIds);

        List<String> certinstIds = new ArrayList<>();
        List<CertOutinstVo> certOutinstVos = new ArrayList<>();
        aeaHiItemMatinsts.forEach(matinst -> {
            if ("m".equals(matinst.getMatProp())) {
                CertOutinstVo vo = new CertOutinstVo();
                vo.setId(matinst.getMatinstId());
                vo.setName(matinst.getMatinstName());
                vo.setType(InOutType.MAT.getValue());
                vo.setData(matinst);
                certOutinstVos.add(vo);
            } else if ("c".equals(matinst.getMatProp()) && StringUtils.isNotBlank(matinst.getCertinstId())) {
                certinstIds.add(matinst.getCertinstId());
            }
        });
        if (certinstIds.size() > 0) {
            AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
            aeaApplyinstProj.setApplyinstId(applyinstId);
            List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(aeaApplyinstProj);

            List<AeaUnitInfo> applyOwnerUnitProj = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, aeaApplyinstProjs.get(0).getProjInfoId());
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);

            List<AeaHiCertinst> certinsts = aeaHiCertinstMapper.getAeaHiCertinstByIds(certinstIds);
            certinsts.forEach(certinst -> {
                CertOutinstVo vo = new CertOutinstVo();
                vo.setId(certinst.getCertinstId());
                vo.setName(certinst.getCertinstName());
                vo.setType(InOutType.CERT.getValue());
                certinst.setApplicant(applyOwnerUnitProj.get(0).getApplicant());
                certinst.setUnitInfoId(applyOwnerUnitProj.get(0).getUnitInfoId());
                certinst.setIssueOrgId(iteminst.getApproveOrgId());
                certinst.setIssueOrgName(iteminst.getApproveOrgName());
                vo.setData(certinst);
                certOutinstVos.add(vo);
            });
        }
        return certOutinstVos;
    }

}


