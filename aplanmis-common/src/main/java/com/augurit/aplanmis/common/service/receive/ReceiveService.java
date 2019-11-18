package com.augurit.aplanmis.common.service.receive;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.AeaHiReceiveDto;
import com.augurit.aplanmis.common.mapper.*;


import com.augurit.aplanmis.common.service.receive.constant.ReceiveConstant;
import com.augurit.aplanmis.common.service.receive.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaohutu
 */
@Service
public class ReceiveService extends AbstractReceiveService {
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;

    @Autowired
    private AeaHiItemCorrectMapper aeaHiItemCorrectMapper;

    @Autowired
    private AeaHiItemCorrectRealIninstMapper aeaHiItemCorrectRealIninstMapper;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    @Autowired
    private AeaHiApplyinstCorrectMapper aeaHiApplyinstCorrectMapper;

    @Autowired
    private AeaHiApplyinstCorrectRealIninstMapper aeaHiApplyinstCorrectRealIninstMapper;

    @Override
    public boolean saveReceive(String[] applyinstId, String[] receiptTypes, String currentUser, String comments) throws Exception {
        //如果回执不满足各地市，请注释以下方法自己实现
        return super.saveReceive(applyinstId, receiptTypes, currentUser, comments);
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
    @Override
    public boolean saveAgentItemReceive(String applyinstId, String[] receiptTypes, String currentUser, String comments) throws Exception {
        return super.saveAgentItemReceive(applyinstId, receiptTypes, currentUser, comments);
    }

    /**
     * 保存不收件回执
     *
     * @param refusedRecepitVo
     * @return
     * @throws Exception
     */
    @Override
    public String saveRefuseReceive(RefusedRecepitVo refusedRecepitVo) throws Exception {
        //如果回执不满足各地市，请注释以下方法自己实现
        String s = super.saveRefuseReceive(refusedRecepitVo);
        return s;
    }

    /**
     * 根据申请实例ID和回执类型 获取单个回执详细信息
     * 回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执）
     *
     * @param applyinstId 申请实例ID
     * @param receiveType 回执类型
     * @param isMakeUp    是否补件
     * @return ReceiveBaseVo
     * @throws Exception
     */
    public ReceiveBaseVo getAeaHiReceiveByApplyinstIdAndReceiveType(String applyinstId, String receiveType, String isMakeUp) throws Exception {
        List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.getAeaHiReceiveByApplyinstIdAndReceiptType(applyinstId, receiveType);
        ReceiveBaseVo baseVo = new ReceiveBaseVo();
        if (aeaHiReceives.size() == 0) {
            return baseVo;
        }
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        ReceiveBaseVo.copyCommonField(aeaHiReceives.get(0), aeaHiApplyinst, baseVo);
        baseVo.setWinName(SecurityContext.getCurrentUserName());

        this.getProjNameAndItemName(baseVo);//项目和事项名称
        this.getDueUnit(baseVo);//办理时限
        String receiptType = baseVo.getReceiptType();
        if (ReceiveConstant.RETURNED_CERT_TYPE.contains(receiptType)) {//OUTINSID保存的是itemVerId拼接
            this.getProjUnit(baseVo);//获取单位信息
            ReturnedCertReceiveVo vo = new ReturnedCertReceiveVo();
            BeanUtils.copyProperties(baseVo, vo);
            this.getReturnedCertReceiveVo(vo);
        } else if (ReceiveConstant.REJECT_TYPE.equals(receiptType)) {//不收件回执
            //aeaHiReceiveVo.setPrintTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        } else if (ReceiveConstant.ACCEPT_TYPE.equals(receiptType)) {//受理回执
            this.getProjUnit(baseVo);//获取单位信息
            this.getAllMatList(baseVo, isMakeUp);//获取所有材料
            AcceptReceiveVo vo = new AcceptReceiveVo();
            BeanUtils.copyProperties(baseVo, vo);
            vo.setGetMatDate(vo.getCreateTime());   //收件时间，不应该是系统当前时间
            return vo;
        } else if (ReceiveConstant.MAT_CORRECT_TYPE.equals(receiptType)) {
            //材料补正收件回执
            this.getProjUnit(baseVo);//获取单位信息
            this.getCorrectMatList(baseVo);
            MatReceiveVo matReceiveVo1 = new MatReceiveVo();
            BeanUtils.copyProperties(baseVo, matReceiveVo1);
            return matReceiveVo1;
        } else if (ReceiveConstant.ADMINISTRATIVE_LICENSE_TYPE.contains(receiptType)) {//行政许可申请书
            this.getProjUnit(baseVo);//获取单位信息
            this.getAllMatList(baseVo, isMakeUp);//获取所有材料
            AdministrativeLicenseReceiveVo vo = new AdministrativeLicenseReceiveVo();
            BeanUtils.copyProperties(baseVo, vo);
            vo.setGetMatDate(vo.getCreateTime());   //收件时间，不应该是系统当前时间
            return vo;

        } else if (ReceiveConstant.MAT_TYPE.equals(receiptType)) {//1：物料回执
            this.getProjUnit(baseVo);//获取单位信息
            this.getAllMatList(baseVo, isMakeUp);//获取所有材料
            MatReceiveVo matReceiveVo = new MatReceiveVo();
            BeanUtils.copyProperties(baseVo, matReceiveVo);
            return this.getMatReceive(matReceiveVo);
        }
        return baseVo;
    }

    /**
     * 根据回执ID获取回执信息
     *
     * @param receiveId 回执ID
     * @param isMakeUp  是否补件
     * @return
     */
    public ReceiveBaseVo getOneReceiveByReceiveId(String receiveId, String isMakeUp) throws Exception {
        AeaHiReceive receive = aeaHiReceiveMapper.getAeaHiReceiveById(receiveId);
        ReceiveBaseVo baseVo = new ReceiveBaseVo();
        if (null == receive) return baseVo;
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(receive.getApplyinstId());
        if (null == applyinst) return baseVo;
        ReceiveBaseVo.copyCommonField(receive, applyinst, baseVo);
        //获取项目名称和事项实例名称
        this.getProjNameAndItemName(baseVo);
        //获取办理时限
        this.getDueUnit(baseVo);
        baseVo.setWinName(SecurityContext.getCurrentUserName());
        AeaHiSmsInfo smsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinst.getApplyinstId());
        baseVo.setReceiveMode(smsInfo == null ? "" : smsInfo.getReceiveMode());
        switch (receive.getReceiptType()) {
            case ReceiveConstant.MAT_TYPE://物料回执
                this.getProjUnit(baseVo);//获取单位信息
                this.getAllMatList(baseVo, isMakeUp);//获取所有材料
                MatReceiveVo matReceiveVo = new MatReceiveVo();
                BeanUtils.copyProperties(baseVo, matReceiveVo);
                return this.getMatReceive(matReceiveVo);
            case ReceiveConstant.ACCEPT_TYPE://受理回执
                this.getProjUnit(baseVo);//获取单位信息
                this.getAllMatList(baseVo, isMakeUp);//获取所有材料
                AcceptReceiveVo vo = new AcceptReceiveVo();
                BeanUtils.copyProperties(baseVo, vo);
                vo.setGetMatDate(vo.getCreateTime());   //收件时间，不应该是系统当前时间
                return vo;
            case ReceiveConstant.REJECT_TYPE://3
                return baseVo;
            case ReceiveConstant.RETURNED_TYPE://4
            case ReceiveConstant.CERT_TYPE://5
                this.getProjUnit(baseVo);//获取单位信息
                ReturnedCertReceiveVo returnedCertReceiveVo = new ReturnedCertReceiveVo();
                BeanUtils.copyProperties(baseVo, returnedCertReceiveVo);
                return this.getReturnedCertReceiveVo(returnedCertReceiveVo);
            case ReceiveConstant.MAT_CORRECT_TYPE://6
                //材料补正收件回执
                this.getProjUnit(baseVo);//获取项目单位信息
                return this.getCorrectMatList(baseVo);
            case ReceiveConstant.APPLY_MAT_CORRECT_RECEIVE://7
                //材料补全回执-窗口
                this.getProjUnit(baseVo);//获取项目单位信息
                return this.getApplyCorrectMatList(baseVo);
            case ReceiveConstant.ITEM_MAT_CORRECT_MAT_RECEIVE_TYPE://12
                this.getProjUnit(baseVo);//获取单位信息
                this.getCorrectMatList(baseVo, receive.getReceiveMemo(), "0");//获取所有材料
                MatReceiveVo matReceiveVo1 = new MatReceiveVo();
                BeanUtils.copyProperties(baseVo, matReceiveVo1);
                matReceiveVo1.setReceiveMemo("");
                return this.getMatReceive(matReceiveVo1);

            case ReceiveConstant.APPLY_MAT_CORRECT_MAT_RECEIVE_TYPE://13
                this.getProjUnit(baseVo);//获取单位信息
                this.getCorrectMatList(baseVo, receive.getReceiveMemo(), "1");//获取所有材料
                MatReceiveVo matReceiveVo2 = new MatReceiveVo();
                BeanUtils.copyProperties(baseVo, matReceiveVo2);
                matReceiveVo2.setReceiveMemo("");
                return this.getMatReceive(matReceiveVo2);

            default:
                this.getProjUnit(baseVo);//获取单位信息
                this.getAllMatList(baseVo, isMakeUp);//获取所有材料
                AdministrativeLicenseReceiveVo administrativeLicenseReceiveVo = new AdministrativeLicenseReceiveVo();
                BeanUtils.copyProperties(baseVo, administrativeLicenseReceiveVo);
                administrativeLicenseReceiveVo.setGetMatDate(administrativeLicenseReceiveVo.getCreateTime());   //收件时间，不应该是系统当前时间
                return administrativeLicenseReceiveVo;

        }
    }

    //退件回执||领证回执
    private ReturnedCertReceiveVo getReturnedCertReceiveVo(ReturnedCertReceiveVo vo) throws Exception {
        AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(vo.getApplyinstId());
        Optional.ofNullable(aeaHiSmsInfo).ifPresent(sms -> {
            vo.setIssueUserName(sms.getAddresseeName());
            vo.setIssueUserMobile(sms.getAddresseePhone());
            vo.setServiceAddress(sms.getAddresseeAddr());
        });
        return vo;
    }


    //获取物料回执
    public MatReceiveVo getMatReceive(MatReceiveVo vo) throws Exception {
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAllAeaHiIteminstByApplyinstId(vo.getApplyinstId(), SecurityContext.getCurrentOrgId());
        if (aeaHiIteminstList.size() == 0) {
            return vo;
        }
        //去除重复部门
        Set<String> orgNameSet = new HashSet<>();
        Set<String> orgShortNameSet = new HashSet<>();
        for (AeaHiIteminst temp : aeaHiIteminstList) {
            if (StringUtils.isNotBlank(temp.getApproveOrgId())) {
                String orgId = temp.getApproveOrgId();
                OpuOmOrg omOrg = opuOmOrgMapper.getOrg(orgId);
                if (omOrg != null) {
                    orgNameSet.add(omOrg.getOrgName() == null ? "" : omOrg.getOrgName());
                    orgShortNameSet.add(omOrg.getOrgShortName1() == null ? "" : omOrg.getOrgShortName1());
                }
            }
        }

        if (!orgNameSet.isEmpty()) {
            String receiveOrgname = orgNameSet.stream().map(s -> s.toString()).collect(Collectors.joining(","));
            vo.setReceiveOrgName(receiveOrgname);
        }

        if (!orgShortNameSet.isEmpty()) {
            String orgShortName = orgShortNameSet.stream().map(s -> s.toString()).collect(Collectors.joining(","));
            vo.setOrgShortName(orgShortName);
        }
        vo.setTellerName(SecurityContext.getCurrentUserName());
        vo.setGetMatDate(new Date());
        return vo;
    }


    /**
     * 材料补全接口-窗口
     *
     * @param baseVo
     * @return
     * @throws Exception
     */
    private MatCorrectVo getApplyCorrectMatList(ReceiveBaseVo baseVo) throws Exception {
        String receiveId = baseVo.getReceiveId();
        AeaHiReceive aeaHiReceive = aeaHiReceiveMapper.getAeaHiReceiveById(receiveId);
        MatCorrectVo matCorrectVo = new MatCorrectVo();
        BeanUtils.copyProperties(baseVo, matCorrectVo);
        if (null != aeaHiReceive && StringUtils.isNotBlank(aeaHiReceive.getReceiveMemo())) {
            String applyinstCorrectId = aeaHiReceive.getReceiveMemo();
            //6 补正回执的memo和orgName从aea_hi_applyinst_correct获取值CORRECT_MEMO
            AeaHiApplyinstCorrect applyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(applyinstCorrectId);

            if (null != applyinstCorrect) {
                matCorrectVo.setReceiveMemo(applyinstCorrect.getCorrectMemo());
                matCorrectVo.setReceiveOrgName("综合服务大厅");
                matCorrectVo.setChargeOrgName("综合服务大厅");
            }

            //存储第一次打印的人员信息
            if (StringUtils.isBlank(applyinstCorrect.getPrintUserId())) {
                applyinstCorrect.setPrintTime(new Date());
                applyinstCorrect.setPrintUserId(SecurityContext.getCurrentUserId());
                applyinstCorrect.setPrintUserName(SecurityContext.getCurrentUser().getUserName());
                applyinstCorrect.setModifier(SecurityContext.getCurrentUserName());
                applyinstCorrect.setModifyTime(new Date());
                aeaHiApplyinstCorrectMapper.updateAeaHiApplyinstCorrect(applyinstCorrect);
            }

            List<AeaHiApplyinstCorrectRealIninst> correctRealIninstList = aeaHiApplyinstCorrectRealIninstMapper.getCorrectRealIninstByApplyinstCorrectId(applyinstCorrectId, SecurityContext.getCurrentOrgId());

            if (correctRealIninstList.size() > 0) {
                List<AeaHiItemMatinst> list = new ArrayList<>();

                for (AeaHiApplyinstCorrectRealIninst correctRealIninst : correctRealIninstList) {
                    Long attCount = correctRealIninst.getAttCount();
                    Long realCopyCount = correctRealIninst.getRealCopyCount();
                    Long realPaperCount = correctRealIninst.getRealPaperCount();
                    if (!MoreThan0(attCount) && !MoreThan0(realCopyCount) && !MoreThan0(realPaperCount)) {
                        continue;
                    }
                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(correctRealIninst.getMatinstId());
                    if (MoreThan0(correctRealIninst.getAttCount())) {
                        matinst.setAttCount(correctRealIninst.getAttCount());
                    }
                    if (MoreThan0(correctRealIninst.getRealPaperCount())) {
                        matinst.setRealPaperCount(correctRealIninst.getRealPaperCount());
                    }
                    if (MoreThan0(correctRealIninst.getRealCopyCount())) {
                        matinst.setRealCopyCount(correctRealIninst.getRealCopyCount());
                    }
                    list.add(matinst);
                }
                matCorrectVo.setAllMatList(list);
            }
        }
        return matCorrectVo;
    }

    /**
     * 获取补正材料-部门
     *
     * @param baseVo
     * @return
     */
    private MatCorrectVo getCorrectMatList(ReceiveBaseVo baseVo) throws Exception {
        String receiveId = baseVo.getReceiveId();
        AeaHiReceive aeaHiReceive = aeaHiReceiveMapper.getAeaHiReceiveById(receiveId);
        MatCorrectVo matCorrectVo = new MatCorrectVo();
        BeanUtils.copyProperties(baseVo, matCorrectVo);
        if (null != aeaHiReceive && StringUtils.isNotBlank(aeaHiReceive.getReceiveMemo())) {
            String correctId = aeaHiReceive.getReceiveMemo();
            //6 补正回执的memo和orgName从aea_hi_item_correct获取值
            AeaHiItemCorrect correct = aeaHiItemCorrectMapper.getAeaHiItemCorrectById(correctId);
            if (null != correct) {
                matCorrectVo.setReceiveMemo(correct.getCorrectMemo());
                matCorrectVo.setReceiveOrgName(correct.getChargeOrgName());
                matCorrectVo.setChargeOrgName(correct.getChargeOrgName());
            }

            //保存第一次打印的人员信息
            if (StringUtils.isBlank(correct.getPrintUserId())) {
                correct.setPrintTime(new Date());
                correct.setPrintUserId(SecurityContext.getCurrentUserId());
                correct.setPrintUserName(SecurityContext.getCurrentUser().getUserName());
                correct.setModifier(SecurityContext.getCurrentUserName());
                correct.setModifyTime(new Date());
                aeaHiItemCorrectMapper.updateAeaHiItemCorrect(correct);
            }

            List<AeaHiItemCorrectRealIninst> correctRealIninstList = aeaHiItemCorrectRealIninstMapper.getCorrectRealIninstByCorrectId(correctId, SecurityContext.getCurrentOrgId());
            if (correctRealIninstList.size() > 0) {
                List<AeaHiItemMatinst> list = new ArrayList<>();

                for (AeaHiItemCorrectRealIninst correctRealIninst : correctRealIninstList) {
                    Long attCount = correctRealIninst.getAttCount();
                    Long realCopyCount = correctRealIninst.getRealCopyCount();
                    Long realPaperCount = correctRealIninst.getRealPaperCount();
                    if (!MoreThan0(attCount) && !MoreThan0(realCopyCount) && !MoreThan0(realPaperCount)) {
                        continue;
                    }
                    String inoutinstId = correctRealIninst.getInoutinstId();
                    AeaHiItemInoutinst inoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstById(inoutinstId);
                    if (null != inoutinst && StringUtils.isNotBlank(inoutinst.getMatinstId())) {
                        AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(inoutinst.getMatinstId());
                        if (MoreThan0(correctRealIninst.getAttCount())) {
                            matinst.setAttCount(correctRealIninst.getAttCount());
                        }
                        if (MoreThan0(correctRealIninst.getRealPaperCount())) {
                            matinst.setRealPaperCount(correctRealIninst.getRealPaperCount());
                        }
                        if (MoreThan0(correctRealIninst.getRealCopyCount())) {
                            matinst.setRealCopyCount(correctRealIninst.getRealCopyCount());
                        }
                        list.add(matinst);
                    }
                }
                matCorrectVo.setAllMatList(list);
            }
        }
        return matCorrectVo;
    }

    private boolean MoreThan0(Long num) {
        return null != num && num > 0;
    }

    /**
     * 获取所有材料
     *
     * @param baseVo
     * @param isMakeUp
     * @return
     */
    private ReceiveBaseVo getAllMatList(ReceiveBaseVo baseVo, String isMakeUp) {
        String isSeriesApprove = baseVo.getIsSeriesApprove();
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAllAeaHiIteminstByApplyinstId(baseVo.getApplyinstId(), SecurityContext.getCurrentOrgId());
        if (aeaHiIteminstList.size() == 0) {
            return baseVo;
        }
        List<AeaHiItemMatinst> allMatinstList = new ArrayList<>();
        if ("0".equals(isSeriesApprove)) {//并联
            getAllStageMatinstList(allMatinstList, aeaHiIteminstList, isMakeUp);
        } else {
            getAllSeriesMatinstList(allMatinstList, aeaHiIteminstList, isMakeUp);
        }
        baseVo.setAllMatList(
                allMatinstList.size() > 0 ? allMatinstList.stream().sorted(
                        Comparator.comparing(AeaHiItemMatinst::getOrgShortName)
                                .thenComparing(AeaHiItemMatinst::getMatinstName))
                        .collect(Collectors.toList()) : allMatinstList
        );

        return baseVo;
    }

    //获取所有材料补正|补全的材料
    private ReceiveBaseVo getCorrectMatList(ReceiveBaseVo baseVo, String correctId, String correctType) throws Exception {

        List<AeaHiItemMatinst> allMatinstList = new ArrayList<>();
        //材料补正
        if ("0".equals(correctType)) {
            AeaHiItemCorrect itemCorrect = aeaHiItemCorrectMapper.getAeaHiItemCorrectById(correctId);
            if (itemCorrect == null) return baseVo;
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(itemCorrect.getIteminstId());
            if (iteminst == null) return baseVo;
            List<AeaHiItemCorrectRealIninst> correctRealIninstList = aeaHiItemCorrectRealIninstMapper.getCorrectRealIninstByCorrectId(correctId, SecurityContext.getCurrentOrgId());
            if (correctRealIninstList.size() > 0) {
                for (AeaHiItemCorrectRealIninst correctRealIninst : correctRealIninstList) {
                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(correctRealIninst.getMatinstId());
                    matinst.setOrgName(iteminst.getApproveOrgName());
                    matinst.setOrgShortName(iteminst.getOrgShortName());

                    if (MoreThan0(correctRealIninst.getAttCount())) {
                        matinst.setAttCount(correctRealIninst.getAttCount());
                    }

                    if (MoreThan0(correctRealIninst.getRealCopyCount())) {
                        matinst.setRealCopyCount(correctRealIninst.getRealCopyCount());
                    }

                    if (MoreThan0(correctRealIninst.getRealPaperCount())) {
                        matinst.setRealPaperCount(correctRealIninst.getRealPaperCount());
                    }

                    if (MoreThan0(matinst.getAttCount()) || MoreThan0(correctRealIninst.getRealCopyCount()) || MoreThan0(correctRealIninst.getRealPaperCount())) {
                        allMatinstList.add(matinst);
                    }

                }
            }
        } else {
            //材料补全
            List<AeaHiApplyinstCorrectRealIninst> realIninsts = aeaHiApplyinstCorrectRealIninstMapper.getCorrectRealIninstByApplyinstCorrectId(correctId, SecurityContext.getCurrentOrgId());
            Map<String, AeaHiIteminst> map = new HashMap();
            for (AeaHiApplyinstCorrectRealIninst realIninst : realIninsts) {
                AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(realIninst.getMatinstId());
                List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByMatinstId(matinst.getMatinstId());
                StringBuffer orgName = new StringBuffer();
                StringBuffer orgShortName = new StringBuffer();
                for (AeaHiItemInoutinst inoutinst : itemInoutinsts) {
                    AeaHiIteminst iteminst = map.get(inoutinst.getIteminstId());

                    if (iteminst == null) {
                        iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(inoutinst.getIteminstId());
                        map.put(inoutinst.getIteminstId(), iteminst);
                    }

                    orgName.append(iteminst.getApproveOrgName() + "、");
                    orgShortName.append(iteminst.getOrgShortName() + "、");
                }

                if (orgName.length() > 1) {
                    String str = orgName.substring(orgName.length() - 1);
                    matinst.setOrgName(str);
                }

                if (orgShortName.length() > 1) {
                    String str = orgShortName.substring(orgShortName.length() - 1);
                    matinst.setOrgShortName(str);
                }

                if (MoreThan0(realIninst.getAttCount())) {
                    matinst.setAttCount(realIninst.getAttCount());
                }

                if (MoreThan0(realIninst.getRealCopyCount())) {
                    matinst.setRealCopyCount(realIninst.getRealCopyCount());
                }

                if (MoreThan0(realIninst.getRealPaperCount())) {
                    matinst.setRealPaperCount(realIninst.getRealPaperCount());
                }

                allMatinstList.add(matinst);
            }
        }

        baseVo.setAllMatList(
                allMatinstList.size() > 0 ? allMatinstList.stream().sorted(
                        Comparator.comparing(AeaHiItemMatinst::getMatinstName))
                        .collect(Collectors.toList()) : allMatinstList
        );

        return baseVo;
    }

    /**
     * 根据申请实例ID获取办理时限信息
     */
    private void getDueUnit(ReceiveBaseVo baseVo) {
        String applyinstId = baseVo.getApplyinstId();
        if (StringUtils.isNotBlank(applyinstId)) {
            AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getDueNumAndUnit(applyinstId);
            if (null == applyinst) return;
            baseVo.setTimeLimit(applyinst.getDueNum() + applyinst.getDueUnit());
            baseVo.setDueNum(applyinst.getDueNum());
            baseVo.setDueUnit(applyinst.getDueUnit());
        }
    }

    //获取申报项目关联的单位信息
    private void getProjUnit(ReceiveBaseVo baseVo) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(baseVo.getApplyinstId());
        if ("1".equals(applyinst.getApplySubject())) { //申办主体：1 单位，0 个人
            List<AeaApplyinstUnitProj> aeaApplyinstUnitProjs = aeaApplyinstUnitProjMapper.getAeaApplyinstUnitProjByApplyinstId(baseVo.getApplyinstId());
            if (aeaApplyinstUnitProjs.size() == 0) return;
            //获取到所有的单位项目IDs
            String[] unitProjIds = aeaApplyinstUnitProjs.stream().map(AeaApplyinstUnitProj::getUnitProjId).toArray(String[]::new);
            //在查询单位信息
            List<AeaUnitInfo> unitList = aeaUnitProjMapper.getAeaUnitProjByUnitProjIds(unitProjIds);
            if (unitList.size() == 0) return;
            //是否业主单位，0表示代建单位，1表示业主/建设单位
            List<AeaUnitInfo> mainUnitList = unitList.stream().filter(unit -> StringUtils.isNotBlank(unit.getIsOwner()) && "1".equals(unit.getIsOwner())).collect(Collectors.toList());
            List<AeaUnitInfo> agentUnitList = unitList.stream().filter(unit -> StringUtils.isNotBlank(unit.getIsOwner()) && "0".equals(unit.getIsOwner())).collect(Collectors.toList());
            Optional.ofNullable(mainUnitList).ifPresent(aeaUnitInfos -> {
                String idcard = aeaUnitInfos.stream().map(AeaUnitInfo::getUnifiedSocialCreditCode).collect(Collectors.joining(","));
                String applicant = aeaUnitInfos.stream().map(AeaUnitInfo::getApplicant).collect(Collectors.joining(","));

                baseVo.setApplicant(applicant);
                baseVo.setApplicantIDCard(idcard);
            });
            Optional.ofNullable(agentUnitList).ifPresent(agentUnitInfos -> {
                String idcard = agentUnitInfos.stream().map(AeaUnitInfo::getUnifiedSocialCreditCode).collect(Collectors.joining(","));
                String applicant = agentUnitInfos.stream().map(AeaUnitInfo::getApplicant).collect(Collectors.joining(","));
                baseVo.setAgentName(applicant);
                baseVo.setAgentIdCard(idcard);
            });
            //设置联系人
            AeaLinkmanInfo linkman = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(baseVo.getLinkmanInfoId());
            if (null == linkman) return;
            baseVo.setAgentLinkmanIDCard(linkman.getLinkmanCertNo());
            baseVo.setAgentLinkmanName(linkman.getLinkmanName());
            baseVo.setAgentLinkmanTel(linkman.getLinkmanMobilePhone());
        } else {
            AeaLinkmanInfo linkman = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(baseVo.getLinkmanInfoId());
            if (null == linkman) return;
            baseVo.setAgentName(linkman.getLinkmanName());
            baseVo.setAgentIdCard(linkman.getLinkmanCertNo());
            baseVo.setApplicant(linkman.getLinkmanName());
            baseVo.setApplicantIDCard(linkman.getLinkmanCertNo());
            baseVo.setAgentLinkmanIDCard(linkman.getLinkmanCertNo());
            baseVo.setAgentLinkmanName(linkman.getLinkmanName());
            baseVo.setAgentLinkmanTel(linkman.getLinkmanMobilePhone());
        }
    }

    //获取项目名称和事项名称
    private void getProjNameAndItemName(ReceiveBaseVo baseVo) {
        List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.listProjByApplyinstId(baseVo.getApplyinstId());
        if (aeaProjInfos.size() > 0) {
            String projNames = aeaProjInfos.stream().map(AeaProjInfo::getProjName).collect(Collectors.joining("、"));
            baseVo.setProjName(projNames);
            String localCode = aeaProjInfos.stream().map(AeaProjInfo::getLocalCode).collect(Collectors.joining(","));
            baseVo.setProjLocalCode(localCode);
        }
        String itemVerId = baseVo.getOutinstId();
        if (StringUtils.isNotBlank(itemVerId)) {
            baseVo.setItemVerId(itemVerId);
            String[] itemVerIds = itemVerId.split(",");
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByItemVerIds(itemVerIds);
            if (aeaItemBasics.size() > 0) {
                String itemNames = aeaItemBasics.parallelStream().map(AeaItemBasic::getItemName).collect(Collectors.joining("、"));
                baseVo.setIteminstName(itemNames);
                baseVo.setItemName(itemNames);
            }
        }

    }

    /**
     * 获取申报回执列表及列表所属阶段或事项
     *
     * @param applyinstIds
     * @return
     */
    public List<AeaHiReceiveDto> getStageOrSeriesReceiveByApplyinstIds(String[] applyinstIds) throws Exception {
        List<AeaHiReceiveDto> list = new ArrayList<>();
        if (null != applyinstIds && applyinstIds.length == 0) return list;
//        List<AeaHiReceiveDto> receiveDtoList = aeaHiReceiveMapper.getAeaHiReceiveListByApplyinstIds(applyinstIds);
        List<AeaHiReceiveDto> receiveDtoList = new ArrayList<>();
        for (String applyinstId : applyinstIds) {
            List<AeaHiReceiveDto> aeaHiReceiveListByApplyinstId = aeaHiReceiveMapper.getAeaHiReceiveListByApplyinstId(applyinstId);
            receiveDtoList.addAll(aeaHiReceiveListByApplyinstId);
        }
        return receiveDtoList;
    }

    private List<AeaHiItemMatinst> updateMatList(List<AeaHiItemMatinst> matinstList, String matId, String orgId, String matType) {
        List<AeaHiItemMatinst> mats = new ArrayList<>();
        if (matinstList.size() > 0) {
            for (AeaHiItemMatinst matinst : matinstList) {
                if (matId.equals(matinst.getMatId()) && "原件".equals(matType) && matinst.getRealPaperCount() != null && matinst.getRealPaperCount() > 0
                        && ((StringUtils.isBlank(orgId) && StringUtils.isBlank(matinst.getOrgId())) || !matinst.getOrgId().contains(orgId))) {
                    matinst.setOrgShortName(matinst.getOrgShortName() + "、" + orgId);
                } else if (matId.equals(matinst.getMatId()) && "复印件".equals(matType) && matinst.getRealCopyCount() != null && matinst.getRealCopyCount() > 0
                        && ((StringUtils.isBlank(orgId) && StringUtils.isBlank(matinst.getOrgId())) || !matinst.getOrgId().contains(orgId))) {
                    matinst.setOrgShortName(matinst.getOrgShortName() + "、" + orgId);
                } else if (matId.equals(matinst.getMatId()) && "电子材料".equals(matType) && matinst.getAttCount() != null && matinst.getAttCount() > 0
                        && ((StringUtils.isBlank(orgId) && StringUtils.isBlank(matinst.getOrgId())) || !matinst.getOrgId().contains(orgId))) {
                    matinst.setOrgShortName(matinst.getOrgShortName() + "、" + orgId);
                }
                mats.add(matinst);
            }
        }
        return mats;
    }

    private void getAllSeriesMatinstList(List<AeaHiItemMatinst> allMatinstList, List<AeaHiIteminst> aeaHiIteminstList, String isMakeUp) {

        //创建纸质原件，纸质复印件，电子件三个list，记录材料，去重
        List<String> paperMatIds = new ArrayList<>();
        List<String> copyMatIds = new ArrayList<>();
        List<String> attMatIds = new ArrayList<>();
        List<String> iteminstIds = new ArrayList<>();
        iteminstIds.add(aeaHiIteminstList.get(0).getIteminstId());
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.selectMatinstByIteminstIds(iteminstIds);
        if (aeaHiItemMatinsts.size() > 0) {
            for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
                if (StringUtils.isNotBlank(aeaHiItemMatinst.getOfficialDocTitle())
                        || aeaHiItemMatinst.getMatinstName().contains("批文批复")) {
                    continue;
                }
                if (StringUtils.isNotBlank(isMakeUp) && "1".equals(isMakeUp) && !"1".equals(aeaHiItemMatinst.getIsMakeUp())) {
                    continue;
                }
                if (StringUtils.isBlank(aeaHiItemMatinst.getOrgShortName())) {
                    aeaHiItemMatinst.setOrgShortName(aeaHiItemMatinst.getOrgName());
                }

                if (aeaHiItemMatinst.getRealCopyCount() != null && aeaHiItemMatinst.getRealCopyCount() > 0) {
                    if (!copyMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        copyMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    }
                } else if (aeaHiItemMatinst.getRealPaperCount() != null && aeaHiItemMatinst.getRealPaperCount() > 0) {
                    if (!paperMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        paperMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    }
                } else if (aeaHiItemMatinst.getAttCount() != null && aeaHiItemMatinst.getAttCount() > 0) {
                    if (!attMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        attMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    }
                }
            }
        }
        sortMatList(allMatinstList); //排序
    }

    //合并相同的 原件 复印件  电子件
    private void getAllStageMatinstList(List<AeaHiItemMatinst> allMatinstList, List<AeaHiIteminst> aeaHiIteminstList, String isMakeUp) {
        //创建纸质原件，纸质复印件，电子件三个list，记录材料，去重
        List<String> paperMatIds = new ArrayList<>();
        List<String> copyMatIds = new ArrayList<>();
        List<String> attMatIds = new ArrayList<>();
        List<String> iteminstIds = new ArrayList<>();
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            iteminstIds.add(aeaHiIteminst.getIteminstId());
        }
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.selectMatinstByIteminstIds(iteminstIds);

        if (aeaHiItemMatinsts.size() > 0) {
            for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
                if (StringUtils.isNotBlank(aeaHiItemMatinst.getOfficialDocTitle())
                        || aeaHiItemMatinst.getMatinstName().contains("批文批复")) {
                    continue;
                }
                if (StringUtils.isNotBlank(isMakeUp) && "1".equals(isMakeUp) && !"1".equals(aeaHiItemMatinst.getIsMakeUp())) {
                    continue;
                }
                if (StringUtils.isBlank(aeaHiItemMatinst.getOrgShortName())) {
                    aeaHiItemMatinst.setOrgShortName(aeaHiItemMatinst.getOrgName());
                }
                if (aeaHiItemMatinst.getRealCopyCount() != null && aeaHiItemMatinst.getRealCopyCount() > 0) {
                    if (!copyMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        copyMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
//                        allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "复印件");
                    }
                } else if (aeaHiItemMatinst.getRealPaperCount() != null && aeaHiItemMatinst.getRealPaperCount() > 0) {
                    if (!paperMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        paperMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
//                        allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "原件");
                    }
                } else if (aeaHiItemMatinst.getAttCount() != null && aeaHiItemMatinst.getAttCount() > 0) {
                    if (!attMatIds.contains(aeaHiItemMatinst.getMatId())) {
                        attMatIds.add(aeaHiItemMatinst.getMatId());
                        allMatinstList.add(aeaHiItemMatinst);
                    } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
//                        allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "电子材料");
                    }
                }
            }
        }
        sortMatList(allMatinstList); //排序
    }

    /**
     * 获取并联申报材料
     *
     * @param aeaHiIteminstList
     * @param isMakeUp
     * @return
     */
    private List<AeaHiItemMatinst> getAllStageMatinstList(List<AeaHiIteminst> aeaHiIteminstList, String isMakeUp) {

        List<AeaHiItemMatinst> allMatinstList = new ArrayList<>();
        if (null == aeaHiIteminstList || aeaHiIteminstList.isEmpty()) return allMatinstList;
        List<String> iteminstIds = aeaHiIteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());
        //创建纸质原件，纸质复印件，电子件三个list，记录材料，去重
        List<String> paperMatIds = new ArrayList<>();
        List<String> copyMatIds = new ArrayList<>();
        List<String> attMatIds = new ArrayList<>();

        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.selectMatinstByIteminstIds(iteminstIds);
        if (aeaHiItemMatinsts.isEmpty()) return allMatinstList;
        //过滤掉批文批复---批文批复不在固定，不能根据name判断
        aeaHiItemMatinsts = aeaHiItemMatinsts.stream().filter(mat -> this.filterDocAndMakeUp(mat.getIsOfficialDoc(), isMakeUp, mat.getIsMakeUp())).collect(Collectors.toList());

        for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {

            if (StringUtils.isBlank(aeaHiItemMatinst.getOrgShortName())) {
                aeaHiItemMatinst.setOrgShortName(aeaHiItemMatinst.getOrgName());
            }
            if (aeaHiItemMatinst.getRealPaperCount() != null && aeaHiItemMatinst.getRealPaperCount() > 0) {
                if (!paperMatIds.contains(aeaHiItemMatinst.getMatId())) {
                    paperMatIds.add(aeaHiItemMatinst.getMatId());
                    allMatinstList.add(aeaHiItemMatinst);
                } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
                    allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "原件");
                }
            } else if (aeaHiItemMatinst.getRealCopyCount() != null && aeaHiItemMatinst.getRealCopyCount() > 0) {
                if (!copyMatIds.contains(aeaHiItemMatinst.getMatId())) {
                    copyMatIds.add(aeaHiItemMatinst.getMatId());
                    allMatinstList.add(aeaHiItemMatinst);
                } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
                    allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "复印件");
                }
            } else if (aeaHiItemMatinst.getAttCount() != null && aeaHiItemMatinst.getAttCount() > 0) {
                if (!attMatIds.contains(aeaHiItemMatinst.getMatId())) {
                    attMatIds.add(aeaHiItemMatinst.getMatId());
                    allMatinstList.add(aeaHiItemMatinst);
                } else {//如果已存在，则对比部门是否相同，若不同，则合并材料，部门用顿号拼接
                    allMatinstList = updateMatList(allMatinstList, aeaHiItemMatinst.getMatId(), aeaHiItemMatinst.getOrgId(), "电子材料");
                }
            }
        }
        sortMatList(allMatinstList); //排序
        return allMatinstList;

    }

    private boolean filterDocAndMakeUp(String isOffiecDoc, String isMakeUp, String matIsMakeUp) {
        return StringUtils.isBlank(isOffiecDoc) || "0".equals(isOffiecDoc) || (StringUtils.isNotBlank(isMakeUp) && "1".equals(isMakeUp) && !"1".equals(matIsMakeUp));
    }

    private void sortMatList(List<AeaHiItemMatinst> allMatinstList) {
        allMatinstList.sort(new Comparator<AeaHiItemMatinst>() {
            @Override
            public int compare(AeaHiItemMatinst arg0, AeaHiItemMatinst arg1) {
                int returnCode = (arg0.getOrgShortName()).compareTo(arg1.getOrgShortName());
                /*if (returnCode == 0) {
                    if (arg0.getRealPaperCount() != null && arg0.getRealPaperCount() > 0) {
                        return -1;
                    }
                    if (arg1.getRealPaperCount() != null && arg1.getRealPaperCount() > 0) {
                        return 1;
                    }
                    if (arg0.getRealCopyCount() != null && arg0.getRealCopyCount() > 0) {
                        return -1;
                    }
                    if (arg1.getRealCopyCount() != null && arg1.getRealCopyCount() > 0) {
                        return 1;
                    }
                    if (arg0.getAttCount() != null && arg0.getAttCount() > 0) {
                        return -1;
                    }
                    if (arg1.getAttCount() != null && arg1.getAttCount() > 0) {
                        return 1;
                    }
                }*/
                return returnCode;
            }
        });
    }

    //合并材料，根据matId合并，不区分部门
    private List<AeaHiItemMatinst> combineMatinstList(List<AeaHiItemMatinst> oldList) {
        List<AeaHiItemMatinst> newList = new ArrayList<>();
        Set<String> matIds = new HashSet<>();//已经存在的材料ID
        for (AeaHiItemMatinst matinst : oldList) {
            String matId = matinst.getMatId();
            Long realPaperCount = matinst.getRealPaperCount() == null ? 0L : matinst.getRealPaperCount();
            Long realCopyCount = matinst.getRealCopyCount() == null ? 0L : matinst.getRealCopyCount();
            Long attCount = matinst.getAttCount() == null ? 0L : matinst.getAttCount();

            if (matIds.contains(matId)) {//已经存在相同的材料
                for (AeaHiItemMatinst newMatinst : newList) {
                    if (newMatinst.getMatId().equals(matId)) {
                        Long attCount1 = newMatinst.getAttCount() == null ? 0L : newMatinst.getAttCount();
                        Long realPaperCount1 = newMatinst.getRealPaperCount() == null ? 0L : newMatinst.getRealPaperCount();
                        Long realCopyCount1 = newMatinst.getRealCopyCount() == null ? 0L : newMatinst.getRealCopyCount();
                        newMatinst.setAttCount(attCount1 + attCount);
                        newMatinst.setRealPaperCount(realPaperCount + realPaperCount1);
                        newMatinst.setRealCopyCount(realCopyCount + realCopyCount1);
                        break;
                    }
                }
            } else {
                matIds.add(matId);
                newList.add(matinst);
            }

        }
        return newList;
    }

}
