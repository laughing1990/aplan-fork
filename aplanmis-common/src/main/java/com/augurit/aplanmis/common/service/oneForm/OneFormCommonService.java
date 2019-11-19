package com.augurit.aplanmis.common.service.oneForm;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.vo.SFFormParam;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.bpm.common.sfengine.render.SFFormMultipleRender;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import com.augurit.aplanmis.front.basis.stage.vo.StageDevFormVo;
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

    public List<SFFormParam> genListSFFormParam4OneForm(OneFormStageRequest oneFormStageRequest,boolean isIncludeDevForm) {
        List<SFFormParam> result = null;

        //参数去重去空
        if (oneFormStageRequest.getItemids() != null && !oneFormStageRequest.getItemids().isEmpty()) {
            HashMap<String, String> map = new HashMap<String, String>();
            for (String itemId : oneFormStageRequest.getItemids()) {
                if (StringUtils.isNotBlank(itemId)) {
                    if (!map.containsKey(itemId)) {
                        map.put(itemId, itemId);
                    }
                }
            }
            List<String> listItemIdNew = new ArrayList<String>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                listItemIdNew.add(entry.getKey());
            }
            oneFormStageRequest.setItemids(listItemIdNew);
        }
        //2.
        try {
            //阶段下的-事项form
            List<StageItemFormVo> listStageItemFormVo = restStageService.findStageItemFormsByStageIdAndItemIds(oneFormStageRequest.getStageId(), oneFormStageRequest.getItemids());
            result = genListSFFormParamByStageItemForm(listStageItemFormVo);
            if (result == null || result.isEmpty()) {
                logger.warn("未找到该阶段下的事项的智能表单,StageId:" + oneFormStageRequest.getStageId() + ",Itemids:" + oneFormStageRequest.getItemids());
            }

            //阶段下的 扩展表partform
            List<AeaParStagePartform> listAeaParStagePartform = null;
            AeaParStagePartform aeaParStagePartform = new AeaParStagePartform();
            aeaParStagePartform.setStageId(oneFormStageRequest.getStageId());
            if(isIncludeDevForm){

            }
            else{
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
                //初始化参数动态注入
//                Map map=null;
//                map=new HashMap<String,String>();
//                map.put("test1","GZXKZ-201901-1024");
//                map.put("test2","uuid-我的祖国");
//                itemSFFormParam.setFormReqExtra(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ContentResultForm<Map> renderHtmlFormContainer(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) throws Exception {
        ContentResultForm<Map> result = new ContentResultForm(false, "", "");
        if (StringUtils.isBlank(oneFormStageRequest.getApplyinstId())) {
            result.setMessage("申报实例ID不能为空!!");
        } else {
            Map<String, Object> resultMap = new HashMap();
            List<SFFormParam> listSFFormParam = genListSFFormParam4OneForm(oneFormStageRequest,false);
            ContentResultForm<String> sfFormResult = sFFormMultipleRender.renderHtmlFormContainer(listSFFormParam, sFRenderConfig);
            resultMap.put("sfForm", sfFormResult.getContent());

            // 查询开发表单
            List<StageDevFormVo> stageDevFormList = new ArrayList<>();
            AeaParStagePartform aeaParStagePartform = new AeaParStagePartform();
            aeaParStagePartform.setStageId(oneFormStageRequest.getStageId());
            aeaParStagePartform.setIsSmartForm("0");
            aeaParStagePartform.setSortNo(null);
            List<AeaParStagePartform> aeaParStagePartformList = aeaParStagePartformService.listStagePartform(aeaParStagePartform);

            if (aeaParStagePartformList != null && aeaParStagePartformList.size() > 0) {
                String projInfoId = oneFormStageRequest.getProjInfoId();

                for (AeaParStagePartform parStagePartform : aeaParStagePartformList) {
                    if (StringUtils.isNotBlank(parStagePartform.getStoFormId())) {
                        ActStoForm actStoForm = actStoFormMapper.getActStoFormById(parStagePartform.getStoFormId());
                        StageDevFormVo stageDevFormVo = new StageDevFormVo();
                        stageDevFormVo.setFormId(parStagePartform.getStoFormId());
                        stageDevFormVo.setFormName(parStagePartform.getPartformName());
                        stageDevFormVo.setFormUrl(actStoForm.getFormLoadUrl().replace("{projInfoId}", "projInfoId=" + projInfoId));
                        stageDevFormList.add(stageDevFormVo);
                    }
                }
            }

            resultMap.put("devForm", stageDevFormList);
            result.setSuccess(true);
            result.setContent(resultMap);
        }
        return result;
    }

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
    protected List<SFFormParam> genListSFFormParamByStageItemForm(List<StageItemFormVo> listStageItemFormVo) {
        //是否所有form为空
        boolean isAllFormNull = true;
        for (StageItemFormVo item : listStageItemFormVo) {
            if (StringUtils.isNotBlank(item.getSubFormId())) {
                isAllFormNull = false;
                break;
            }
        }

        List<SFFormParam> listSFFormParam = new ArrayList<>();
        if (isAllFormNull) {

        } else {
            for (int i = 0; i < listStageItemFormVo.size(); i++) {
                if (StringUtils.isNotBlank(listStageItemFormVo.get(i).getSubFormId()) &&
                        StringUtils.isNotBlank(listStageItemFormVo.get(i).getItemVerId())) {
                    SFFormParam ref = new SFFormParam();
                    ref.setFormId(listStageItemFormVo.get(i).getSubFormId());
                    ref.setRefEntityId(listStageItemFormVo.get(i).getItemVerId());
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
            List<SFFormParam> listSFFormParam = genListSFFormParam4OneForm(oneFormStageRequest,false);
            sFFormMultipleRender.renderPage(listSFFormParam, sFRenderConfig);
        }
    }
}