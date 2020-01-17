package com.augurit.aplanmis.common.service.oneForm;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.domain.vo.SFFormParam;
import com.augurit.agcloud.bpm.common.mapper.ActStoSmartFormOperaMapper;
import com.augurit.agcloud.bpm.common.service.*;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.bpm.common.sfengine.render.SFFormMultipleRender;
import com.augurit.agcloud.bpm.common.sfengine.util.SFEngineUtil;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.meta.domain.MetaDbColumn;
import com.augurit.agcloud.meta.domain.MetaDbConn;
import com.augurit.agcloud.meta.domain.MetaDbTable;
import com.augurit.agcloud.meta.sc.db.service.MetaDbColumnService;
import com.augurit.agcloud.meta.sc.db.service.MetaDbConnService;
import com.augurit.agcloud.meta.sc.db.service.MetaDbTableService;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.utils.ExcelUtils;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.augurit.aplanmis.front.basis.stage.vo.FormFrofileVo;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormItemRequest;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import com.augurit.aplanmis.front.basis.stage.vo.StageItemFormVo;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
    private ActStoFormService actStoFormService;
    @Autowired
    private AeaItemPartformAdminService aeaItemPartformService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private ActStoSmartFormOperaMapper actStoSmartFormOperaMapper;
    @Autowired
    private MetaDbTableService metaDbTableService;
    @Autowired
    private MetaDbColumnService metaDbColumnService;
    @Autowired
    private ActStoElementService actStoElementService;
    @Autowired
    private ActStoElementValueService actStoElementValueService;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private MetaDbConnService metaDbConnService;
    @Autowired
    private ActStoForminstService actStoForminstService;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String jdbcPwd;

    private String dbType = "mysql";

    // 获取dbName
    private String getDbName() {
        String dbName = null;
        MetaDbConn metaDbConn = metaDbConnService.getMetaDbConnByConInfo(jdbcUrl, userName, jdbcPwd);
        if (metaDbConn != null) {
            dbName = metaDbConn.getDbName();
        }
        return dbName;
    }

    /*
     * 获取并联申报的一张表单--表单列表
     * */
    public List<FormFrofileVo> getListForm4StageOneForm(OneFormStageRequest oneFormStageRequest) throws Exception {

        LinkedHashSet linkedHashSet = new LinkedHashSet<FormFrofileVo>();
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
                        linkedHashSet.add(formFrofileVo);
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
                        linkedHashSet.add(formFrofileVo);
                    }
                }
            }
        }

        Iterator iterator = linkedHashSet.iterator();
        while (iterator.hasNext()) {
            Object abc = iterator.next();
            result.add((FormFrofileVo) abc);
        }

        return result;
    }

    // 封装表单信息
    private FormFrofileVo getFormFrofileVo(String formId, String refEntityId, String projInfoId) {
        FormFrofileVo formFrofileVo = null;
        if (StringUtils.isNotBlank(formId)) {
            formFrofileVo = new FormFrofileVo();
            ActStoForm actStoForm = actStoFormService.getActStoFormById(formId);
            formFrofileVo.setFormId(formId);
            formFrofileVo.setFormName(actStoForm.getFormName());
            SFFormParam sfFormParam = new SFFormParam();
            sfFormParam.setFormId(formId);
            sfFormParam.setRefEntityId(refEntityId);
            if (isSmartForm(actStoForm)) {
                formFrofileVo.setSmartForm(true);
                formFrofileVo.setFormUrl(genUrl4SamrtForm(sfFormParam));
            } else {
                formFrofileVo.setSmartForm(false);
                formFrofileVo.setFormUrl(genUrl4DevForm(sfFormParam, actStoForm.getFormLoadUrl(), projInfoId));
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

    private String genUrl4DevForm(SFFormParam sFFormParam, String urlSrc, String projInfoId) {
        String result = "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(urlSrc.replace("{projInfoId}", "projInfoId=" + projInfoId));
        strBuilder.append("&refEntityId=" + sFFormParam.getRefEntityId());
        strBuilder.append("&formId=" + sFFormParam.getFormId());
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

    // 导出智能表单设计相关表数据（表全量数据）
    public ResultForm exportAllFormTableDatas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String message = "智能表单设计相关表数据";

        String[] tableNames = request.getParameterValues("tableNames");
        // 获取表单对应物理表名
        List<String> tableNameList = tableNameList(tableNames);
        if (tableNameList != null && tableNameList.size() > 0) {
            //初始化表单
            HSSFWorkbook workbook = new HSSFWorkbook();

            for (int i = 0; i < tableNameList.size(); i++) {
                String tableName = tableNameList.get(i);
                exportExcel(workbook, i, tableName,true);
            }

            // 文件名
            String fileName = message + "导出Excel结果" + DateUtils.convertDateToString(new Date(), "yyyyMMddHHmmsssss") + ".xls";

            //响应到客户端
            try {
                this.workbookResponse(workbook,request,response, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ContentResultForm(true, message + "导出Excel成功！");
        }

        return new ContentResultForm(false, message + "导出Excel失败！");
    }

    // 获取表单对应物理表名
    private List<String> tableNameList(String[] tableNames) {
        List<String> tableNameList = new ArrayList<>();
        //获取表单未删除的智能表单编号
        if (tableNames != null && tableNames.length > 0) {
            tableNameList = new ArrayList(Arrays.asList(tableNames));
            for (String tableName : tableNames) {
                if ("act_sto_form".equals(tableName)) {
                    ActStoForm actStoForm = new ActStoForm();
                    actStoForm.setFormProperty("smart-biz");
                    List<ActStoForm> actStoFormList = actStoFormService.listActStoForm(actStoForm);
                    if (actStoFormList != null && actStoFormList.size() > 0) {
                        for (ActStoForm info : actStoFormList) {
                            if (info != null && info.getFormCode() != null) {
                                tableNameList.add(info.getFormCode());
                            }
                        }
                    }
                }
            }
        }

        // 表名称去重
        tableNameList = duplicateList(tableNameList);
        return tableNameList;
    }

    // 以表单数据为主体导出相关表数据
    public void exportOneFormTableDatas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] formIds = request.getParameterValues("formIds");
        // 获取所有表单id列表（主表单+子表单,去重formId）
        List<String> formIdList = formIdList(formIds);
        if (formIdList != null && formIdList.size() > 0) {
            //初始化表单
            HSSFWorkbook workbook = new HSSFWorkbook();

            List<ActStoForm> actStoFormList = new ArrayList<>();
            List<ActStoFormElement> actStoFormElementList = new ArrayList();
            List<ActStoElement> actStoElementList = new ArrayList<>();
            List<ActStoElementValue> actStoElementValueList = new ArrayList<>();
            List<BscDicCodeType> bscDicCodeTypeList = new ArrayList<>();
            List<BscDicCodeItem> bscDicCodeItemList = new ArrayList<>();
            List<MetaDbTable> metaDbTableList = new ArrayList<>();
            List<MetaDbColumn> metaDbColumnList = new ArrayList<>();
            List<ActStoForminst> actStoForminstList = new ArrayList<>();
            for (int i = 0; i < formIdList.size(); i++) {
                String formId = formIdList.get(i);
                if (StringUtils.isNotBlank(formId)) {
                    //查询表单信息
                    ActStoForm actStoForm = actStoFormService.getActStoFormById(formId);
                    if (actStoForm != null) {
                        // act_sto_form数据
                        actStoFormList.add(actStoForm);
                        // act_sto_form_element数据
                        actStoFormElementList = actStoFormService.listFormElementsAndMetaByFormId(formId);
                        if (actStoFormElementList != null && actStoFormElementList.size() > 0) {
                            for (ActStoFormElement actStoFormElement : actStoFormElementList) {
                                if (actStoFormElement != null && actStoFormElement.getElementId() != null) {
                                    ActStoElement actStoElement = actStoElementService.getActStoElementById(actStoFormElement.getElementId());
                                    if (actStoElement != null) {
                                        // act_sto_element数据
                                        actStoElementList.add(actStoElement);

                                        // act_sto_element_value数据
                                        ActStoElementValue actStoElementValue = new ActStoElementValue();
                                        actStoElementValue.setElementId(actStoElement.getElementId());
                                        List<ActStoElementValue> elementValueList = actStoElementValueService.listActStoElementValue(actStoElementValue);
                                        if (elementValueList != null && elementValueList.size() > 0) {
                                            actStoElementValueList.addAll(elementValueList);
                                        }

                                        //获取数据字典字段：elementId和（widgetId+"10"）查询
                                        ActStoElementValue elementValue = actStoElementValueService.getElementValueByElementIdAndKeyId(actStoElement.getElementId(), actStoElement.getWidgetId() + "10");
                                        if (elementValue != null) {
                                            String valueContent = elementValue.getValueContent();
                                            if (StringUtils.isNotBlank(valueContent)) {
                                                // 字典类型
                                                BscDicCodeType bscDicCodeType = bscDicCodeService.getTypeByTypeCode(valueContent, SecurityContext.getCurrentOrgId());
                                                if (bscDicCodeType != null) {
                                                    bscDicCodeTypeList.add(bscDicCodeType);

                                                    // 字典项
                                                    List<BscDicCodeItem> dicCodeItemList = bscDicCodeService.getItemsByTypeCode(valueContent);
                                                    if (dicCodeItemList != null && dicCodeItemList.size() > 0) {
                                                        bscDicCodeItemList.addAll(dicCodeItemList);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //根据所属表元数据ID获取元数据信息
                        MetaDbTable metaDbTable = new MetaDbTable();
                        metaDbTable.setTableId(actStoForm.getMetaDbTableId());
                        metaDbTableList = metaDbTableService.listMetaDbTable(metaDbTable);
                        if (metaDbTableList != null && metaDbTableList.size() > 0) {
                            for (MetaDbTable table : metaDbTableList) {
                                if (table != null && table.getTableId() != null) {
                                    //根据元数据ID获取元数据列信息
                                    List<MetaDbColumn> columnList = metaDbColumnService.listMetaDbTbColByTableId(table.getTableId());
                                    if (columnList != null && columnList.size() > 0) {
                                        metaDbColumnList.addAll(columnList);
                                    }
                                }
                            }
                        }

                        // 根据formId获取act_sto_forminst数据,暂时不需要这些数据
                        //ActStoForminst actStoForminst = new ActStoForminst();
                        //actStoForminst.setFormId(formId);
                        //actStoForminstList = actStoForminstService.listActStoForminst(actStoForminst);
                    }
                }
            }

            // 1、act_sto_form
            String actStoFormTableName = "act_sto_form";
            // 根据表名获取表中所有字段名称
            List<String> actStoFormColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), actStoFormTableName);
            if (actStoFormColumnNames != null && actStoFormColumnNames.size() > 0) {
                //封装Excel请求参数
                ExcelUtils.ExcelParam actStoFormExcelParam = new ExcelUtils.ExcelParam();
                actStoFormExcelParam.setSheetNum(0);
                actStoFormExcelParam.setSheetName(actStoFormTableName);
                actStoFormExcelParam.setTitleName(actStoFormTableName);
                actStoFormExcelParam.setRowTitleList(actStoFormColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                actStoFormExcelParam.setFieldList(CommonTools.underline2Camel(actStoFormColumnNames, false));
                actStoFormExcelParam.setDataList(actStoFormList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, actStoFormExcelParam);
            }

            //2、act_sto_element
            String actStoElementTableName = "act_sto_element";
            // 根据表名获取表中所有字段名称
            List<String> actStoElementColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), actStoElementTableName);
            if (actStoElementColumnNames != null && actStoElementColumnNames.size() > 0) {
                //封装Excel请求参数
                ExcelUtils.ExcelParam actStoElementExcelParam = new ExcelUtils.ExcelParam();
                actStoElementExcelParam.setSheetNum(1);
                actStoElementExcelParam.setSheetName(actStoElementTableName);
                actStoElementExcelParam.setTitleName(actStoElementTableName);
                actStoElementExcelParam.setRowTitleList(actStoElementColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                actStoElementExcelParam.setFieldList(CommonTools.underline2Camel(actStoElementColumnNames, false));
                actStoElementExcelParam.setDataList(actStoElementList);

                // 导出表数据到Excel
                ExcelUtils.exportExcel(workbook, actStoElementExcelParam);
            }

            // 3、act_sto_form_element
            String actStoFormElementTableName = "act_sto_form_element";
            // 根据表名获取表中所有字段名称
            List<String> actStoFormElementColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), actStoFormElementTableName);
            if (actStoFormElementColumnNames != null && actStoFormElementColumnNames.size() > 0) {
                // 封装Excel请求参数
                ExcelUtils.ExcelParam actStoFormElementExcelParam = new ExcelUtils.ExcelParam();
                actStoFormElementExcelParam.setSheetNum(2);
                actStoFormElementExcelParam.setSheetName(actStoFormElementTableName);
                actStoFormElementExcelParam.setTitleName(actStoFormElementTableName);
                actStoFormElementExcelParam.setRowTitleList(actStoFormElementColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                actStoFormElementExcelParam.setFieldList(CommonTools.underline2Camel(actStoFormElementColumnNames, false));
                actStoFormElementExcelParam.setDataList(actStoFormElementList);

                // 导出表数据到Excel
                ExcelUtils.exportExcel(workbook, actStoFormElementExcelParam);
            }

            //4、act_sto_element_value
            String actStoElementValueTableName = "act_sto_element_value";
            // 根据表名获取表中所有字段名称
            List<String> actStoElementValueColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), actStoElementValueTableName);
            if (actStoElementValueColumnNames != null && actStoElementValueColumnNames.size() > 0) {
                //封装Excel请求参数
                ExcelUtils.ExcelParam actStoElementValueExcelParam = new ExcelUtils.ExcelParam();
                actStoElementValueExcelParam.setSheetNum(3);
                actStoElementValueExcelParam.setSheetName(actStoElementValueTableName);
                actStoElementValueExcelParam.setTitleName(actStoElementValueTableName);
                actStoElementValueExcelParam.setRowTitleList(actStoElementValueColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                actStoElementValueExcelParam.setFieldList(CommonTools.underline2Camel(actStoElementValueColumnNames, false));
                actStoElementValueExcelParam.setDataList(actStoElementValueList);

                // 导出表数据到Excel
                ExcelUtils.exportExcel(workbook, actStoElementValueExcelParam);
            }

            // 5、meta_db_table
            String metaDbTableTableName = "meta_db_table";
            // 根据表名获取表中所有字段名称
            List<String> metaDbTableColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), metaDbTableTableName);
            if (metaDbTableColumnNames != null && metaDbTableColumnNames.size() > 0) {
                //封装Excel请求参数
                ExcelUtils.ExcelParam metaDbTableExcelParam = new ExcelUtils.ExcelParam();
                metaDbTableExcelParam.setSheetNum(4);
                metaDbTableExcelParam.setSheetName(metaDbTableTableName);
                metaDbTableExcelParam.setTitleName(metaDbTableTableName);
                metaDbTableExcelParam.setRowTitleList(metaDbTableColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                metaDbTableExcelParam.setFieldList(CommonTools.underline2Camel(metaDbTableColumnNames, false));
                metaDbTableExcelParam.setDataList(metaDbTableList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, metaDbTableExcelParam);
            }

            // 6、meta_db_column
            String metaDbColumnTableName = "meta_db_column";
            // 根据表名获取表中所有字段名称
            List<String> metaDbColumnColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), metaDbColumnTableName);
            if (metaDbColumnColumnNames != null && metaDbColumnColumnNames.size() > 0) {
                //封装Excel请求参数
                ExcelUtils.ExcelParam metaDbColumnExcelParam = new ExcelUtils.ExcelParam();
                metaDbColumnExcelParam.setSheetNum(5);
                metaDbColumnExcelParam.setSheetName(metaDbColumnTableName);
                metaDbColumnExcelParam.setTitleName(metaDbColumnTableName);
                metaDbColumnExcelParam.setRowTitleList(metaDbColumnColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                metaDbColumnExcelParam.setFieldList(CommonTools.underline2Camel(metaDbColumnColumnNames, false));
                metaDbColumnExcelParam.setDataList(metaDbColumnList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, metaDbColumnExcelParam);
            }

            // 7、bsc_dic_code_type
            String dicCodeTypeTableName = "bsc_dic_code_type";
            // 根据表名获取表中所有字段名称
            List<String> dicCodeTypeColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), dicCodeTypeTableName);
            List<String> dicCodeTypeFieldList = new ArrayList<>();
            if (dicCodeTypeColumnNames != null && dicCodeTypeColumnNames.size() > 0) {
                for (String type : dicCodeTypeColumnNames) {
                    if (StringUtils.isNotBlank(type)) {
                        if (!"ORG_ID".equals(type)) {
                            if (!type.startsWith("TYPE")) {
                                type = "TYPE_" + type;
                            }
                        }
                        dicCodeTypeFieldList.add(type);
                    }
                }

                //封装Excel请求参数
                ExcelUtils.ExcelParam dicCodeTypeExcelParam = new ExcelUtils.ExcelParam();
                dicCodeTypeExcelParam.setSheetNum(6);
                dicCodeTypeExcelParam.setSheetName(dicCodeTypeTableName);
                dicCodeTypeExcelParam.setTitleName(dicCodeTypeTableName);
                dicCodeTypeExcelParam.setRowTitleList(dicCodeTypeColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                dicCodeTypeExcelParam.setFieldList(CommonTools.underline2Camel(dicCodeTypeFieldList, false));
                dicCodeTypeExcelParam.setDataList(bscDicCodeTypeList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, dicCodeTypeExcelParam);
            }

            // 8、bsc_dic_code_item
            String dicCodeItemTableName = "bsc_dic_code_item";
            // 根据表名获取表中所有字段名称
            List<String> dicCodeItemColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), dicCodeItemTableName);
            List<String> dicCodeItemFieldList = new ArrayList<>();
            if (dicCodeItemColumnNames != null && dicCodeItemColumnNames.size() > 0) {
                for (String item : dicCodeItemColumnNames) {
                    if (StringUtils.isNotBlank(item)) {
                        if (!"TYPE_ID".equals(item) && !"ORG_ID".equals(item)) {
                            if (!item.startsWith("ITEM")) {
                                item = "ITEM_" + item;
                            }
                        }
                        dicCodeItemFieldList.add(item);
                    }
                }

                //封装Excel请求参数
                ExcelUtils.ExcelParam dicCodeItemExcelParam = new ExcelUtils.ExcelParam();
                dicCodeItemExcelParam.setSheetNum(7);
                dicCodeItemExcelParam.setSheetName(dicCodeItemTableName);
                dicCodeItemExcelParam.setTitleName(dicCodeItemTableName);
                dicCodeItemExcelParam.setRowTitleList(dicCodeItemColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                dicCodeItemExcelParam.setFieldList(CommonTools.underline2Camel(dicCodeItemFieldList, false));
                dicCodeItemExcelParam.setDataList(bscDicCodeItemList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, dicCodeItemExcelParam);
            }

            // 9、act_sto_forminst
            String actStoForminstTableName = "act_sto_forminst";
            // 根据表名获取表中所有字段名称
            List<String> actStoForminstColumnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), actStoForminstTableName);
            if (actStoForminstColumnNames != null && actStoForminstColumnNames.size() > 0) {
                // 封装Excel请求参数
                ExcelUtils.ExcelParam actStoForminstExcelParam = new ExcelUtils.ExcelParam();
                actStoForminstExcelParam.setSheetNum(8);
                actStoForminstExcelParam.setSheetName(actStoForminstTableName);
                actStoForminstExcelParam.setTitleName(actStoForminstTableName);
                actStoForminstExcelParam.setRowTitleList(actStoForminstColumnNames);
                //字符串list下划线转驼峰（大驼峰）
                actStoForminstExcelParam.setFieldList(CommonTools.underline2Camel(actStoForminstColumnNames, false));
                actStoForminstExcelParam.setDataList(actStoForminstList);

                // 导出数据到Excel
                ExcelUtils.exportExcel(workbook, actStoForminstExcelParam);
            }


            // 导出表单对应物理表结构
            for (int j = 0; j < formIdList.size(); j++) {
                String formId = formIdList.get(j);
                if (StringUtils.isNotBlank(formId)) {
                    //查询表单信息
                    ActStoForm actStoForm = actStoFormService.getActStoFormById(formId);
                    if (actStoForm != null) {
                        String formCode = actStoForm.getFormCode();
                        if (StringUtils.isNotBlank(formCode)) {
                            exportExcel(workbook, j + 9, formCode,false);
                        }
                    }
                }
            }

            // 文件名
            String fileName = "导出Excel结果" + DateUtils.convertDateToString(new Date(), "yyyyMMddHHmmsssss");

            //响应到客户端
            try {
                this.workbookResponse(workbook, request,response, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 获取所有表单id列表（主表单+子表单,去重formId）
    private List<String> formIdList(String[] formIds) throws Exception {
        List<String> result = new ArrayList<>();
        if (formIds != null && formIds.length > 0) {
            result = new ArrayList(Arrays.asList(formIds));
            for (int i = 0; i < formIds.length; i++) {
                String formId = formIds[i];
                if (StringUtils.isNotBlank(formId)) {
                    //查询表单信息
                    ActStoForm actStoForm = actStoFormService.getActStoFormById(formId);
                    if (actStoForm != null) {
                        //查询所有表单信息
                        ActStoForm actStoFormAll = actStoFormService.findChildFormsWithAllInfos(actStoForm);
                        if (actStoFormAll != null) {
                            List<ActStoForm> childs = actStoFormAll.getChilds();
                            if (childs != null && childs.size() > 0) {
                                for (ActStoForm childForm : childs) {
                                    if (childForm != null && childForm.getFormId() != null) {
                                        result.add(childForm.getFormId());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // formId列表去重
            result = duplicateList(result);
        }

        return result;
    }

    // 字符串列表去重
    private List<String> duplicateList(List<String> list) {
        if (list != null && list.size() > 0) {
            List<String> listNew = new ArrayList<String>();
            Set<String> set = new HashSet<String>();
            for (String str : list) {
                if (StringUtils.isNotBlank(str)) {
                    if (set.add(str)) {
                        listNew.add(str);
                    }
                }
            }
            return listNew;
        }
        return null;
    }

    // 根据表名导出数据
    private void exportExcel(HSSFWorkbook workbook, int sheetNum, String tableName,boolean exportData) throws Exception {
        if (StringUtils.isNotBlank(tableName)) {
            // 判断数据表是否存在
            int tableExist = actStoSmartFormOperaMapper.isTableExist(dbType, tableName);
            if (tableExist > 0) {
                // 根据表名获取表中所有字段名称
                List<String> columnNames = actStoSmartFormOperaMapper.listTableColumns(dbType, getDbName(), tableName);
                List<?> dataList = null;
                if (exportData) {
                    // 根据表名获取表中所有数据
                    dataList = actStoSmartFormOperaMapper.listFormHistoryDatas(tableName, null, null, false);
                }

                //封装Excel请求参数
                ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
                excelParam.setSheetNum(sheetNum);
                excelParam.setSheetName(tableName);
                excelParam.setTitleName(tableName);
                excelParam.setRowTitleList(columnNames);
                excelParam.setFieldList(columnNames);
                excelParam.setDataList(dataList);

                // 导出单个表数据到Excel
                ExcelUtils.exportExcel(workbook, excelParam);
            }
        }
    }

    //发送响应流方法
    public void workbookResponse(HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        OutputStream os = null;
        try {
            //设置响应头
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));

            //输出Excel文件
            os = response.getOutputStream();
            workbook.write(os);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }

    // 导入Excel数据
    public void importOneFormTableDatas(HttpServletRequest request) throws Exception {
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            MultipartFile excelFile = req.getFile("excelFile");
            if (excelFile != null) {
                // 获取sheet所有数据，以sheet名称分类
                List<Map<String, Object>> sheetDataList = ExcelUtils.getExcelData(excelFile);
                if (sheetDataList != null && sheetDataList.size() > 0) {
                    for (Map<String, Object> sheetMap : sheetDataList) {
                        if (sheetMap != null && sheetMap.containsKey("sheetName") && sheetMap.containsKey("sheetList")) {
                            String sheetName = sheetMap.get("sheetName") == null ? "" : sheetMap.get("sheetName").toString();//获取Sheet名称
                            List<List<Object>> sheetList = (List<List<Object>>) sheetMap.get("sheetList");//获取Sheet数据
                            if (StringUtils.isNotBlank(sheetName)) {
                                // 判断数据表是否存在
                                int tableExist = actStoSmartFormOperaMapper.isTableExist(dbType, sheetName);
                                if (tableExist > 0) {
                                    // 根据表名获取表中所有字段
                                    List<Map<String, Object>> columnList = actStoSmartFormOperaMapper.getTableColumns(dbType, getDbName(), sheetName);
                                    if (sheetList != null && sheetList.size() > 0) {//列表数据
                                        for (int i = 0; i < sheetList.size(); i++) {
                                            List<Object> sheetColumnData = sheetList.get(i);//行数据
                                            if (sheetColumnData != null && sheetColumnData.size() > 0) {
                                                Map<String, Object> dataMap = new HashMap<>();
                                                for (int j = 0; j < sheetColumnData.size(); j++) {
                                                    // 处理数据，导入模板表头字段跟数据库保持一致
                                                    getColumnData(columnList.get(j), sheetColumnData.get(j), dataMap);
                                                }

                                                // 根据第一个字段和值查询是否已有记录，无则新增
                                                Map<String, Object> dataExist = actStoSmartFormOperaMapper.getFormDataByBizId(sheetName, columnList.get(0).get("fieldName").toString(), sheetColumnData.get(0).toString());
                                                if (dataExist == null) {
                                                    actStoSmartFormOperaMapper.insertTableData(sheetName, dataMap);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 根据表字段类型处理单元格值
    private void getColumnData(Map<String, Object> columnMap, Object dataObj, Map<String, Object> dataMap) {
        String columnName = columnMap.get("fieldName") == null ? "" : columnMap.get("fieldName").toString();//列名
        String columnType = columnMap.get("fieldType") == null ? "" : columnMap.get("fieldType").toString();//列类型
        if (StringUtils.isNotBlank(columnName)) {
            if (dataObj != null && StringUtils.isNotBlank(dataObj.toString())) {
                Object result = null;
                if (StringUtils.isNotBlank(columnType)) {
                    if ("longblob".equals(columnType) || "blob".equals(columnType)) {//处理longblob和blob
                        byte[] byteArr = SFEngineUtil.convertStringToByteArr(dataObj.toString());//字符串转byte[]
                        result = byteArr;
                    } else {
                        result = dataObj;
                    }
                }

                if (result != null) {
                    dataMap.put(columnName, result);
                }
            }
        }
    }
}