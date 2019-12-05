package com.augurit.aplanmis.common.service.oneForm;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.vo.SFFormParam;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.bpm.common.sfengine.render.SFFormMultipleRender;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.augurit.aplanmis.front.basis.stage.vo.FormFrofileVo;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormItemRequest;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import com.augurit.aplanmis.front.basis.stage.vo.StageItemFormVo;
import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OneFormCommonService {
    private static Logger logger = LoggerFactory.getLogger(OneFormCommonService.class);

    @Autowired
    private RestStageService restStageService;
    @Autowired
    private SFFormMultipleRender sFFormMultipleRender;
    @Autowired
    private AeaParStagePartformService aeaParStagePartformService;
    @Autowired
    private ActStoFormMapper actStoFormMapper;
    @Autowired
    private AeaItemPartformAdminService aeaItemPartformService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    /*
     * 获取并联申报的一张表单--表单列表
     * */
    public List<FormFrofileVo> getListForm4StageOneForm(OneFormStageRequest oneFormStageRequest) throws Exception {

        List<FormFrofileVo> result = new ArrayList<>();
        String projInfoId = oneFormStageRequest.getProjInfoId();

        // 阶段下的-表单form
        AeaParStagePartform aeaParStagePartform = new AeaParStagePartform();
        aeaParStagePartform.setStageId(oneFormStageRequest.getStageId());
        List<AeaParStagePartform> aeaParStagePartformList = aeaParStagePartformService.listStagePartform(aeaParStagePartform);
        if (!aeaParStagePartformList.isEmpty() && aeaParStagePartformList.size() > 0) {
            for (AeaParStagePartform parStagePartform : aeaParStagePartformList) {
                // 封装表单信息
                if (parStagePartform != null) {
                    FormFrofileVo formFrofileVo = getFormFrofileVo(parStagePartform.getStoFormId(), oneFormStageRequest.getApplyinstId(), projInfoId);
                    if (formFrofileVo != null) {
                        result.add(formFrofileVo);
                    }
                }
            }
        }

        //阶段下的-事项form
        List<StageItemFormVo> listStageItemFormVo = restStageService.findStageItemFormsByStageIdAndItemIds(oneFormStageRequest.getStageId(), oneFormStageRequest.getItemids());
        if (!listStageItemFormVo.isEmpty() && listStageItemFormVo.size() > 0) {
            for (StageItemFormVo stageItemFormVo : listStageItemFormVo) {
                // 封装表单信息
                if (stageItemFormVo != null) {
                    FormFrofileVo formFrofileVo = getFormFrofileVo(stageItemFormVo.getSubFormId(), oneFormStageRequest.getApplyinstId(), projInfoId);
                    if (formFrofileVo != null) {
                        result.add(formFrofileVo);
                    }
                }
            }
        }

        return result;
    }

    // 封装表单信息
    private FormFrofileVo getFormFrofileVo(String formId, String refEntityId, String projInfoId) {
        FormFrofileVo formFrofileVo = null;
        if (StringUtils.isNotBlank(formId)) {
            formFrofileVo = new FormFrofileVo();
            ActStoForm actStoForm = actStoFormMapper.getActStoFormById(formId);
            formFrofileVo.setFormId(formId);
            formFrofileVo.setFormName(actStoForm.getFormName());
            if (isSmartForm(actStoForm)) {
                formFrofileVo.setSmartForm(true);

                SFFormParam item = new SFFormParam();
                item.setFormId(formId);
                item.setRefEntityId(refEntityId);
                formFrofileVo.setFormUrl(genUrl4SamrtForm(item));
            } else {
                formFrofileVo.setSmartForm(false);
                formFrofileVo.setFormUrl(actStoForm.getFormLoadUrl().replace("{projInfoId}", "projInfoId=" + projInfoId));
            }
        }

        return formFrofileVo;
    }

    private boolean isSmartForm(ActStoForm actStoForm) {
        boolean result = false;
        if (StringUtils.isNotBlank(actStoForm.getFormProperty())) {
            if (actStoForm.getFormProperty().equalsIgnoreCase(ActStoConstant.FORM_PROPERTY_SMART_BIZ)
                    || actStoForm.getFormProperty().equalsIgnoreCase(ActStoConstant.FORM_PROPERTY_SMART_FLOW)) {
                result = true;
            }
        }
        return result;
    }

    private String genUrl4SamrtForm(SFFormParam item) {
        String result = "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("/bpm/common/sf/renderHtmlFormContainer");
        strBuilder.append("?listRefEntityId=" + item.getRefEntityId());
        strBuilder.append("&listFormId=" + item.getFormId());
        strBuilder.append("&showBasicButton=true&includePlatformResource=false&busiScence=oneform");
        result = strBuilder.toString();
        return result;
    }

    public List<SFFormParam> genListSFFormParam4OneForm(OneFormStageRequest oneFormStageRequest, boolean isIncludeDevForm) {
        List<SFFormParam> result = null;

        // itemids参数去重去空
        if (oneFormStageRequest.getItemids() != null && !oneFormStageRequest.getItemids().isEmpty()) {
            List<String> listItemIdNew = new ArrayList<String>();
            Set<String> set = new HashSet<String>();
            for (String itemId : oneFormStageRequest.getItemids()) {
                if (StringUtils.isNotBlank(itemId)) {
                    if (set.add(itemId)) {
                        listItemIdNew.add(itemId);
                    }
                }
            }
            oneFormStageRequest.setItemids(listItemIdNew);
        }

        try {
            //阶段下的-事项form
            List<StageItemFormVo> listStageItemFormVo = restStageService.findStageItemFormsByStageIdAndItemIds(oneFormStageRequest.getStageId(), oneFormStageRequest.getItemids());
            if (listStageItemFormVo == null || listStageItemFormVo.isEmpty()) {
                logger.warn("未找到该阶段下的事项的智能表单,StageId:" + oneFormStageRequest.getStageId() + ",Itemids:" + oneFormStageRequest.getItemids());
            }

            // 构造列表，填充事项formId
            result = genListSFFormParamByFillFormId(listStageItemFormVo);

            //阶段下的 扩展表partform
            List<AeaParStagePartform> listAeaParStagePartform = null;
            AeaParStagePartform aeaParStagePartform = new AeaParStagePartform();
            aeaParStagePartform.setStageId(oneFormStageRequest.getStageId());
            if (!isIncludeDevForm) {
                // 过滤开发表单
                aeaParStagePartform.setIsSmartForm("1");
            }
            listAeaParStagePartform = aeaParStagePartformService.listStagePartform(aeaParStagePartform);

            //事项表单和扩展表单合并
            if (listAeaParStagePartform != null && !listAeaParStagePartform.isEmpty()) {
                //追加阶段固定的智能表单
                result = joinFormList(result, listAeaParStagePartform);
            }

            //用申报实例ID 替换所有外键数据ID
            for (SFFormParam itemSFFormParam : result) {
                itemSFFormParam.setRefEntityId(oneFormStageRequest.getApplyinstId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public ContentResultForm<Map> renderHtmlFormContainer(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) throws Exception {
//        ContentResultForm<Map> result = new ContentResultForm(false, "", "");
//        if (StringUtils.isBlank(oneFormStageRequest.getApplyinstId())) {
//            result.setMessage("申报实例ID不能为空!!");
//        } else {
//            Map<String, Object> resultMap = new HashMap();
//            List<SFFormParam> listSFFormParam = genListSFFormParam4OneForm(oneFormStageRequest,false);
//            ContentResultForm<String> sfFormResult = sFFormMultipleRender.renderHtmlFormContainer(listSFFormParam, sFRenderConfig);
//            resultMap.put("sfForm", sfFormResult.getContent());
//
//            result.setSuccess(true);
//            result.setContent(resultMap);
//        }
//        return result;
//    }

    /**
     * 合并两个表单列表--partform，事项form
     */
    protected List<SFFormParam> joinFormList(List<SFFormParam> itemFormList, List<AeaParStagePartform> partFormList) {
        List<SFFormParam> result = new ArrayList<>();
        result.addAll(itemFormList);

        Collections.sort(partFormList, new BeanComparator("sortNo"));
        List<SFFormParam> partFormConvertList = new ArrayList<>();
        for (AeaParStagePartform item : partFormList) {
            SFFormParam sfFormParam = new SFFormParam();
            sfFormParam.setRefEntityId(item.getStageId());
            sfFormParam.setFormId(item.getStoFormId());
            partFormConvertList.add(sfFormParam);
        }
        result.addAll(partFormConvertList);
        return result;
    }

    /*
     * 构造列表，填充事项formId
     * */
    protected List<SFFormParam> genListSFFormParamByFillFormId(List<StageItemFormVo> listStageItemFormVo) {
        List<SFFormParam> listSFFormParam = new ArrayList<>();
        if (!listStageItemFormVo.isEmpty() && listStageItemFormVo.size() > 0) {
            for (int i = 0; i < listStageItemFormVo.size(); i++) {
                if (StringUtils.isNotBlank(listStageItemFormVo.get(i).getSubFormId()) &&
                        StringUtils.isNotBlank(listStageItemFormVo.get(i).getItemVerId())) {
                    SFFormParam ref = new SFFormParam();
                    ref.setFormId(listStageItemFormVo.get(i).getSubFormId());
                    listSFFormParam.add(ref);
                }
            }
        }

        return listSFFormParam;
    }

    public void renderPage(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) {
        if (StringUtils.isBlank(oneFormStageRequest.getApplyinstId())) {
            logger.warn("申报实例ID不能为空!!");
        } else {
            List<SFFormParam> listSFFormParam = genListSFFormParam4OneForm(oneFormStageRequest, false);
            sFFormMultipleRender.renderPage(listSFFormParam, sFRenderConfig);
        }
    }

    public List<FormFrofileVo> getListForm4ItemOneForm(OneFormItemRequest oneFormItemRequest) throws Exception {
        List<FormFrofileVo> result = new ArrayList<>();

        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(oneFormItemRequest.getItemId());
        //没有启用，返回空数组
        if (aeaItemBasic != null && StringUtils.isNotBlank(aeaItemBasic.getUseOneForm())) {
            if (aeaItemBasic.getUseOneForm().equalsIgnoreCase(Status.OFF)) {
                return result;
            }
        }

        String itemVerId = oneFormItemRequest.getItemId();
        AeaItemPartform aeaItemPartform = new AeaItemPartform();
        aeaItemPartform.setItemVerId(itemVerId);
        aeaItemPartform.setSortNo(null);
        List<AeaItemPartform> aeaItemPartformList = aeaItemPartformService.listAeaItemPartformNoPage(aeaItemPartform);
        if (!aeaItemPartformList.isEmpty() && aeaItemPartformList.size() > 0) {
            for (AeaItemPartform itemPartform : aeaItemPartformList) {
                // 封装表单信息
                if (itemPartform != null) {
                    FormFrofileVo formFrofileVo = getFormFrofileVo(itemPartform.getStoFormId(), oneFormItemRequest.getApplyinstId(), oneFormItemRequest.getProjInfoId());
                    if (formFrofileVo != null) {
                        result.add(formFrofileVo);
                    }
                }
            }
        }

        return result;
    }
}