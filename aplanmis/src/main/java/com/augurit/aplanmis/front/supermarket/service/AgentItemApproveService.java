package com.augurit.aplanmis.front.supermarket.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import com.augurit.aplanmis.front.approve.service.ApproveService;
import com.augurit.aplanmis.front.basis.item.service.RestItemService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApproveForm;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentItemApproveService {

    private static final String SERVICE_OBJECT_DICT_NAME = "ITEM_FWJGXZ";
    private static final String SERVICE_OBJECT_CODE = "5";

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private RestImApplyService restImApplyService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private RestItemService restItemService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;

    /**
     * 获取中介事项申报基本信息
     *
     * @param taskId      任务ID
     * @param applyinstId 申请实例ID
     * @param isItemSeek  是否意见征求
     * @return AgentItemApproveForm
     * @throws Exception exception
     */
    public AgentItemApproveForm getBaseApplyFormInfo(String taskId, String applyinstId, boolean isItemSeek) throws Exception {
        AgentItemApproveForm form = new AgentItemApproveForm();
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (null == applyinst) throw new Exception("can not find applyinst info ");
        String applyinstCode = applyinst.getApplyinstCode();
        //查询采购项目,中介服务，机构要求等 信息
        PurchaseProjVo purchaseProj = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(applyinstCode, null);
        String isApproveProj = purchaseProj.getIsApproveProj();
        if (StringUtils.isNotBlank(isApproveProj) && "1".equals(isApproveProj)) {
            //查询关联的投资审批项目信息
            AeaParentProj parentProj = aeaParentProjMapper.getParentProjByProjInfoId(purchaseProj.getProjInfoId());
            if (null != parentProj) {
                AeaProjInfo projInfo = aeaProjInfoService.getTransProjInfoDetail(parentProj.getParentProjId());
                form.setAeaProjInfo(projInfo);
            }

        }
        //查询资质信息
        String unitRequireId = purchaseProj.getUnitRequireId();
        String isQualRequire = purchaseProj.getIsQualRequire();
        if (StringUtils.isNotBlank(unitRequireId) && StringUtils.isNotBlank(isQualRequire) && "1".equals(isQualRequire)) {
            List<AeaImMajorQualVo> aeaImMajorQualVos = aeaImMajorQualMapper.listAeaImMajorQualByUnitRequireId(unitRequireId);
            String collect = aeaImMajorQualVos.stream().map((major -> {
                String majorName = major.getMajorName();
                String qualName = major.getQualName();
                String qualLevelName = major.getQualLevelName();
                return qualName + "（" + qualLevelName + "）";
            })).collect(Collectors.joining(","));
            purchaseProj.setQualRequire(collect);
        } else {
            purchaseProj.setQualRequire("无");
        }
        form.setPurchaseProj(purchaseProj);
        //中介事项信息
        String iteminstId = approveService.getIteminstIdByTaskId(taskId);
        List<AeaHiIteminst> itemisntList = approveService.getItemisntList(applyinstId, iteminstId, isItemSeek);
        if (itemisntList.isEmpty()) throw new Exception("can not find iteminst info ");
        AeaHiIteminst iteminst = itemisntList.get(0);
        String itemProperty = iteminst.getItemProperty();//办件类型
        String dueNumUnit = iteminst.getDueNumUnit();//办理时限单位
        //设置事项办件类型
        BscDicCodeItem item_property = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", itemProperty, "012aa547-7104-418d-87cc-824f24f1a278");
        if (null != item_property) {
            iteminst.setItemProperty(item_property.getItemName());
        }
        //办理时限单位
        BscDicCodeItem dunNumUnit = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", dueNumUnit, "012aa547-7104-418d-87cc-824f24f1a278");
        if (null != item_property) {
            iteminst.setDueNumUnit(item_property.getItemName());
        }
        //服务对象
        String currentOrgId = SecurityContext.getCurrentOrgId();
        String itemVerId = iteminst.getItemVerId();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, currentOrgId);

        String serviceObjectCode = StringUtils.isNotBlank(aeaItemBasic.getXkdx()) ? aeaItemBasic.getXkdx() : SERVICE_OBJECT_CODE;
        String serviceObject = restItemService.getServiceObject(SERVICE_OBJECT_DICT_NAME, serviceObjectCode, currentOrgId);

        form.changeToIteminst(iteminst, serviceObject);
        return form;
    }

    /**
     * 审批系统上传服务结果
     *
     * @param matinstIds  材料是滴ID
     * @param applyinstId 申请实例ID
     * @throws Exception E
     */
    public void uploadServiceResult(String[] matinstIds, String applyinstId) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (null == applyinst) throw new Exception("can not find applyinst");
        PurchaseProjVo purchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(applyinst.getApplyinstCode(), null);
        if (null == purchase) throw new Exception("can not find purchase");
        restImApplyService.uploadServiceResult(purchase.getProjPurchaseId(), "", matinstIds);

    }

    /**
     * 中介事项审批页上传服务结果电子件
     *
     * @param matinstId   材料实例ID 第一次上传为空
     * @param applyinstId 申报实例ID
     * @param matId       材料ID
     * @param request     附件
     * @return
     * @throws Exception
     */
    public String uploadServiceResultAtt(String matinstId, String applyinstId, String matId, HttpServletRequest request) throws Exception {

        request.setCharacterEncoding("utf-8");//设置编码，防止附件名称乱码

        if (org.apache.commons.lang.StringUtils.isNotBlank(matinstId)) {

            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
            if (aeaHiItemMatinst == null) throw new Exception("找不到材料实例！");
            aeaHiItemMatinstService.setAttCount(aeaHiItemMatinst, request);
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);

        } else {

            if (org.apache.commons.lang.StringUtils.isBlank(applyinstId) || org.apache.commons.lang.StringUtils.isBlank(matId))
                throw new Exception("缺少参数！");

            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);
            if (null == aeaItemMat) throw new Exception("找不到材料定义！");
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null) throw new Exception("找不到申报实例！");

            AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
            matinstId = UUID.randomUUID().toString();
            aeaHiItemMatinst.setMatinstId(matinstId);
            aeaHiItemMatinst.setMatId(matId);
            aeaHiItemMatinst.setCreateTime(new Date());
            aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
            aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
            aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
            aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
            aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

            List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
            if (aeaApplyinstProjs.size() < 1) throw new Exception("找不到项目信息！");
            String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();

            if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                aeaHiItemMatinst.setMatinstSource("u");
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                if (aeaUnitInfos.size() < 1) throw new Exception("找不到建设单位！");
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

        }
        return matinstId;
    }
}
