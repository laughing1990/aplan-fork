package com.augurit.aplanmis.front.supermarket.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaImMajorQualMapper;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import com.augurit.aplanmis.front.approve.service.ApproveService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApproveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentItemApproveService {

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

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
        PurchaseProjVo purchaseProj = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(applyinstCode);
        String isApproveProj = purchaseProj.getIsApproveProj();
        if (StringUtils.isNotBlank(isApproveProj) && "1".equals(isApproveProj)) {
            //查询关联的投资审批项目信息
            AeaProjInfo parentProj = aeaProjInfoMapper.findParentProj(purchaseProj.getProjInfoId());
            form.setAeaProjInfo(parentProj);
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

        form.changeToIteminst(iteminst);
        return form;
    }
}
