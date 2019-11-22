package com.augurit.aplanmis.supermarket.serviceResult.service.impl;

import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.common.vo.ServiceProjInfoVo;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.serviceResult.service.AeaImServiceResultService;
import com.augurit.aplanmis.supermarket.serviceResult.vo.ServiceResultVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * -Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-06-20 09:10:47</li>
 * </ul>
 */
@Service
@Transactional
public class AeaImServiceResultServiceImpl implements AeaImServiceResultService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceResultServiceImpl.class);

    @Autowired
    private AeaImServiceResultMapper aeaImServiceResultMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private IBscAttService bscAtService;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;
    @Autowired
    private RestImApplyService restImApplyService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    public void saveAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception {
        aeaImServiceResult.setRootOrgId(topOrgId);
        aeaImServiceResultMapper.insertAeaImServiceResult(aeaImServiceResult);
    }

    public void updateAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception {
        aeaImServiceResultMapper.updateAeaImServiceResult(aeaImServiceResult);
    }

    @Override
    public void deleteAeaImServiceResultById(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            aeaImServiceResultMapper.deleteAeaImServiceResult(id);
        }
    }


    public PageInfo<AeaImServiceResult> listAeaImServiceResult(AeaImServiceResult aeaImServiceResult, Page page) throws Exception {
        aeaImServiceResult.setRootOrgId(topOrgId);
        PageHelper.startPage(page);
        List<AeaImServiceResult> list = aeaImServiceResultMapper.listAeaImServiceResult(aeaImServiceResult);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaImServiceResult getAeaImServiceResultById(String serviceResultId) throws Exception {
        if (StringUtils.isNotBlank(serviceResultId)) {
            AeaImServiceResult aeaImServiceResult = aeaImServiceResultMapper.getAeaImServiceResultById(serviceResultId);

            if (aeaImServiceResult != null) {
                aeaImServiceResult.setBscAttForms(bscAtService.listAttLinkAndDetailByTablePKRecordId("AEA_IM_SERVICE_RESULT", "SERVICE_RESULT_ID", serviceResultId, topOrgId));
                return aeaImServiceResult;
            }

        }

        return null;
    }


    public List<AeaImServiceResult> listAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception {
        aeaImServiceResult.setRootOrgId(topOrgId);
        List<AeaImServiceResult> list = aeaImServiceResultMapper.listAeaImServiceResult(aeaImServiceResult);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<ServiceProjInfoVo> getServiceProjInfoList(String keyword, String auditFlag, Page page, HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo != null && StringUtils.isNotBlank(loginInfoVo.getUnitId())) {
            if (page != null) {
                PageHelper.startPage(page);
            }

            return aeaImServiceResultMapper.getServiceProjInfoList(loginInfoVo.getUnitId(), auditFlag, keyword);
        }

        return new ArrayList<>();
    }

    @Override
    @Deprecated
    public AeaImServiceResult saveAeaImServiceResult(AeaImServiceResult aeaImServiceResult, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        AeaImServiceResult query = new AeaImServiceResult();
        query.setUnitBiddingId(aeaImServiceResult.getUnitBiddingId());
        query.setRootOrgId(topOrgId);
        List list = aeaImServiceResultMapper.listAeaImServiceResult(query);
        if (list != null && list.size() > 0) {
            throw new RuntimeException("服务结果已存在");
        }

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        aeaImServiceResult.setServiceResultId(UuidUtil.generateUuid());
        aeaImServiceResult.setCreateTime(new Date());
        aeaImServiceResult.setUploadTime(aeaImServiceResult.getCreateTime());
        aeaImServiceResult.setLinkmanInfoId(loginInfoVo.getUserId());
        aeaImServiceResult.setCreater(SecurityContext.getCurrentUserName());
        aeaImServiceResult.setAuditFlag("0");
        aeaImServiceResult.setRootOrgId(topOrgId);
        aeaImServiceResultMapper.insertAeaImServiceResult(aeaImServiceResult);
        saveServiceResultFiles(files, aeaImServiceResult.getServiceResultId());

        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(aeaImServiceResult.getUnitBiddingId());
        aeaImUnitBidding.setIsUploadResult("1");
        aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImServiceResult.getProjPurchaseId());
        if (aeaImProjPurchase == null) {
            throw new RuntimeException("项目不存在");
        }
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setProjPurchaseId(aeaImServiceResult.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setOperateDescribe("新增服务结果");
        aeaImPurchaseinst.setIsOwnFile((files != null && files.size() > 0) ? "1" : "0");
        aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

        return aeaImServiceResult;
    }

    @Override
    public boolean dealAeaImServiceResult(AeaImServiceResult aeaImServiceResult, HttpServletRequest request) throws Exception {

        try {
            aeaImServiceResultMapper.updateAeaImServiceResult(aeaImServiceResult);

            AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
            aeaImUnitBidding.setUnitBiddingId(aeaImServiceResult.getUnitBiddingId());
            aeaImUnitBidding.setIsUploadResult("1");
            aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);

            AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
            aeaImProjPurchase.setProjPurchaseId(aeaImServiceResult.getProjPurchaseId());


            String auditFlag = aeaImServiceResult.getAuditFlag();
            if ("0".equals(auditFlag)) {
                aeaImProjPurchase.setAuditFlag("3");
            } else if ("1".equals(auditFlag)) {
                aeaImProjPurchase.setAuditFlag("2");
            } else if ("2".equals(auditFlag)) {
                aeaImProjPurchase.setAuditFlag("1");
            }
            aeaImProjPurchaseMapper.updateAeaImProjPurchase(aeaImProjPurchase);

            LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
            AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
            aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
            aeaImPurchaseinst.setProjPurchaseId(aeaImServiceResult.getProjPurchaseId());
            aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
            aeaImPurchaseinst.setCreateTime(new Date());
            aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
            aeaImPurchaseinst.setOperateDescribe("确认服务结果");
            aeaImPurchaseinst.setIsOwnFile("0");
            aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
            aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    private void saveServiceResultFiles(List<MultipartFile> files, String recordId) {
        if (files != null && !files.isEmpty() && StringUtils.isNotBlank(recordId)) {
            String tableName = "AEA_IM_SERVICE_RESULT";
            String pkName = "SERVICE_RESULT_ID";
            FileUtils.uploadFile(tableName, pkName, recordId, files);
        }
    }

    @Override
    @Deprecated
    public AeaImServiceResult updateAeaImServiceResult(AeaImServiceResult aeaImServiceResult, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        aeaImServiceResult.setModifyTime(new Date());
        aeaImServiceResult.setUploadTime(aeaImServiceResult.getModifyTime());
        aeaImServiceResult.setModifier(SecurityContext.getCurrentUserName());
        aeaImServiceResultMapper.updateAeaImServiceResult(aeaImServiceResult);
        saveServiceResultFiles(files, aeaImServiceResult.getServiceResultId());

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImServiceResult.getProjPurchaseId());
        if (aeaImProjPurchase == null) {
            throw new RuntimeException("项目不存在");
        }
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setProjPurchaseId(aeaImServiceResult.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setOperateDescribe("修改服务结果");
        aeaImPurchaseinst.setIsOwnFile((files != null && files.size() > 0) ? "1" : "0");
        aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

        return aeaImServiceResult;
    }

    @Override
    public boolean deleteServiceResultFile(String detailId) throws Exception {

        if (StringUtils.isNotBlank(detailId)) {
            String[] detailIds = detailId.split(",");

            if (detailIds != null && detailIds.length > 0) {
                return FileUtils.deleteFiles(detailIds);
            }
        }

        return false;

    }

    @Override
    public void saveOrUpdateSerivceResult(String[] matinstIds, ServiceResultVo serviceResultVo) throws Exception {
        String projPurchaseId = serviceResultVo.getProjPurchaseId();
        String serviceResultId = serviceResultVo.getServiceResultId();
        AeaImServiceResult result = new AeaImServiceResult();
        BeanUtils.copyProperties(serviceResultVo, result);
        if (StringUtils.isBlank(serviceResultId)) {
            //保存
            result.setAuditFlag("1");//已确定
            result.setServiceResultId(UUID.randomUUID().toString());
            result.setCreater(SecurityContext.getCurrentUserName());
            result.setCreateTime(new Date());
            result.setUploadTime(new Date());
            aeaImServiceResultMapper.insertAeaImServiceResult(result);
        } else {
            //更新
            result.setModifyTime(new Date());
            result.setModifier(SecurityContext.getCurrentUserName());
            aeaImServiceResultMapper.updateAeaImServiceResult(result);
        }
        //判断当前matinstId是否已存在 todo

        restImApplyService.uploadServiceResult(projPurchaseId, serviceResultVo.getMemo(), matinstIds);
    }

    /**
     * 查询采购项目材料及实例列表,及服务结果信息
     *
     * @param projPurchaseId 采购项目需求ID
     * @return List<MatinstVo>
     */
    @Override
    public List<MatinstVo> getProjPurchaseMatinstList(String projPurchaseId) throws Exception {
        AeaImProjPurchase purchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
        if (null == purchase) throw new Exception("can not find proj purchase");
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(purchase.getApplyinstCode());
        if (null == applyinst) throw new Exception("can not find applyinst");
        List<MatinstVo> vos = aeaImProjPurchaseService.listPurchaseMatinst(null, applyinst.getApplyinstId());
        return vos;
    }


    //上传服务结果电子件 -todo copy 待优化
    @Override
    public String uploadServiceResultAtt(String matId, String matinstId, String projPurchaseId, HttpServletRequest request) throws Exception {
        StringBuffer message = new StringBuffer();
        request.setCharacterEncoding("utf-8");//设置编码，防止附件名称乱码

        if (org.apache.commons.lang.StringUtils.isNotBlank(matinstId)) {

            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
            if (aeaHiItemMatinst == null) return message.append("找不到材料实例！").toString();
            aeaHiItemMatinstService.setAttCount(aeaHiItemMatinst, request);
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);

        } else {

            if (org.apache.commons.lang.StringUtils.isBlank(projPurchaseId) || org.apache.commons.lang.StringUtils.isBlank(matId))
                return message.append("缺少参数！").toString();

            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);

            if (aeaItemMat != null) {
                //
                AeaImProjPurchase purchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
                if (null == purchase) return message.append("can not find purchase").toString();
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(purchase.getApplyinstCode());
                if (aeaHiApplyinst == null) return message.append("找不到申报实例！").toString();
                String applyinstId = aeaHiApplyinst.getApplyinstId();
                AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                aeaHiItemMatinst.setMatId(matId);
                aeaHiItemMatinst.setCreateTime(new Date());
                aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
                aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
                if (aeaApplyinstProjs.size() < 1) return message.append("找不到项目信息！").toString();
                String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();

                if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                    aeaHiItemMatinst.setMatinstSource("u");
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() < 1) return message.append("找不到建设单位！").toString();
                    aeaHiItemMatinst.setUnitInfoId(aeaUnitInfos.get(0).getUnitInfoId());
                } else {
                    aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                    aeaHiItemMatinst.setMatinstSource("l");
                }
                aeaHiItemMatinstService.setAttCount(aeaHiItemMatinst, request);
                aeaHiItemMatinst.setProjInfoId(projInfoId);
                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

                //创建该申报实例下所有需要用到该材料实例的关联关系
                aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(new String[]{aeaHiItemMatinst.getMatinstId()}, applyinstId, SecurityContext.getCurrentUserId());

            } else {
                message.append("找不到材料定义！");
            }
        }


        return message.toString();
    }


}
