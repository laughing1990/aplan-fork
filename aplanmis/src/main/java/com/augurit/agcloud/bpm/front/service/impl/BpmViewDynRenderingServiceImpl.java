package com.augurit.agcloud.bpm.front.service.impl;

import com.augurit.agcloud.bpm.common.component.DatabaseColumnConvert;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.mapper.ActStoElementEventMapper;
import com.augurit.agcloud.bpm.common.service.*;
import com.augurit.agcloud.bpm.common.utils.DatatablesPage;
import com.augurit.agcloud.bpm.front.constant.ViewRenderingConstant;
import com.augurit.agcloud.bpm.front.domain.BpmViewEntity;
import com.augurit.agcloud.bpm.front.domain.BpmViewTableColumnEntity;
import com.augurit.agcloud.bpm.front.service.BpmViewDynRenderingService;
import com.augurit.agcloud.bpm.front.util.FrontUtils;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.mapper.ConditionalQueryMapper;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Transactional
public class BpmViewDynRenderingServiceImpl implements BpmViewDynRenderingService {
    private static Logger logger = LoggerFactory.getLogger(BpmViewDynRenderingService.class);

    @Autowired
    private Environment environment;

    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private ActStoViewService actSumViewService;
    @Autowired
    private ActStoColumnService actSumColumnService;
    @Autowired
    private ActStoElementService actStoElementService;
    @Autowired
    private ActStoWidgetService actStoWidgetService;
    @Autowired
    private ActStoWidgetKeyService actStoWidgetKeyService;
    @Autowired
    private ActStoElementValueService actStoElementValueService;
    @Autowired
    private ActStoViewElementService actStoViewElementService;
    @Autowired
    private ActStoElementEventMapper elementEventMapper;
    @Autowired
    private ConditionalQueryMapper conditionalQueryMapper;

    /**
     * 视图解析引擎
     *
     * @param sourceColumnList
     * @param layoutCode
     * @return
     */
    private BpmViewEntity initSearchItemHtmlEngine(List<ActStoColumn> sourceColumnList, String layoutCode) {
        try {
            layoutCode = FrontUtils.firstCharUpperCase(layoutCode);
            // 根据给定的类名初始化类
            //Class catClass = Class.forName("com.augurit.agcloud.bpm.front.renderer." + layoutCode + "ViewRenderer");
            Class catClass = Class.forName("com.augurit.agcloud.bpm.front.renderer.CustomQueryViewRenderer");
            Object obj = catClass.newInstance();
            Method[] methods = catClass.getMethods();
            for (Method method : methods) {
                if ("init".equals(method.getName())) {//初始化相关的service
                    method.invoke(obj, actStoElementService, actStoWidgetService, actStoWidgetKeyService, actStoElementValueService, bscDicCodeService);
                }
                if ("execute".equals(method.getName())) {
                    BpmViewEntity list = (BpmViewEntity) method.invoke(obj, sourceColumnList);
                    return list;
                }
            }
            // 调用指定方法
            catClass.getMethod("shout").invoke(obj, (Object) null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BpmViewEntity initSearchItemEntityData(String viewId, String layoutCode) throws Exception {
        if (viewId != null && !"".equals(viewId)) {
            ActStoView sumView = actSumViewService.getActStoViewById(viewId);
            if (sumView != null) {
                ActStoColumn actSumColumn = new ActStoColumn();
                actSumColumn.setViewId(sumView.getViewId());
                actSumColumn.setListWidth(null);
                List<ActStoColumn> columnList = actSumColumnService.listActStoColumn(actSumColumn);

                if (columnList != null && columnList.size() > 0) {
                    return this.initSearchItemHtmlEngine(columnList, layoutCode);//视图解析引擎
                }
            } else {
                throw new RuntimeException("找不到视图，请检查！");
            }
        }
        return null;
    }


    @Override
    public Map initRenderingViewCol(ActStoView actStoView) throws Exception {
        Map allMap = new HashMap();
        //返回结果设置
        return initRenderingViewCol(allMap, actStoView);
    }

    @Override
    public Map initRenderingViewCol(Map resultMetaDataMap, ActStoView actStoView) throws Exception {
        //返回结果设置
        resultMetaDataMap.put("tHeadItems", initRenderingViewColList(actStoView));
        return resultMetaDataMap;
    }

    @Override
    public List<BpmViewTableColumnEntity> initRenderingViewColList(ActStoView actStoView) throws Exception {
        List<ActStoColumn> actStoColumnList = new ArrayList<>();//元素对象list
        //获取元素字段数据  根据actSumView对象
        actStoColumnList = actSumViewService.listActStoColumnByView(actStoView);
        return renderTableCol(actStoColumnList);
    }


    @Override
    public Map getPageViewData(ActStoView actSumView, Page page, Map<String, String> queryMap) throws Exception {
        logger.debug(page.toString());
        logger.debug(queryMap.toString());
        List<Map> data = actSumViewService.fetchQueryResult(actSumView, page, queryMap); //查询数据
        DatatablesPage dpage = new DatatablesPage(page);        //设置返回的分页对象
        Map resultAllMap = new HashMap();
        ActStoViewElement viewElement = new ActStoViewElement();        //获取按钮信息
        viewElement.setViewId(actSumView.getViewId());
        List<ActStoViewElement> viewElementList = actStoViewElementService.listActStoViewElement(viewElement);
        if (viewElementList.size() > 0) {
            if (data != null && data.size() > 0)
                for (Map currData : data)
                    currData.put("btnWidgetRenderers", viewElementList);
        }
        resultAllMap.put("meta", dpage);
        resultAllMap.put("data", data);
        return resultAllMap;
    }

    @Override
    public Map getBootstrapPageViewData(ActStoView actSumView, Page page, Map<String, String> queryMap) throws Exception {
        List<Map> datas = actSumViewService.fetchQueryResult(actSumView, page, queryMap); //查询数据
        Map resultAllMap = new HashMap();
        ActStoViewElement viewElement = new ActStoViewElement();        //获取按钮信息
        viewElement.setViewId(actSumView.getViewId());
        List<ActStoViewElement> viewElementList = actStoViewElementService.listActStoViewElement(viewElement);

        List<String> btnList = new ArrayList<>();

        if(viewElementList!=null&&viewElementList.size() > 0){
            for (ActStoViewElement stoViewElement:viewElementList){
                StringBuffer btnBuff = new StringBuffer("");//列表按钮字符串
                StringBuffer eventBuff = new StringBuffer();//元素事件字符串

                String widgetRenderer = stoViewElement.getWidgetRenderer();

                //------------------解析key-value属性替换相关逻辑 start------------------------
                String widgetId = stoViewElement.getWidgetId();
                String elementId = stoViewElement.getElementId();

                ActStoWidgetKey widgetKeyCondition = new ActStoWidgetKey();
                widgetKeyCondition.setIsActive("1");
                widgetKeyCondition.setWidgetId(widgetId);
                List<ActStoWidgetKey> widgetKeyList = actStoWidgetKeyService.listActStoWidgetKey(widgetKeyCondition);
                if(widgetKeyList!=null&&widgetKeyList.size()>0){
                    for(ActStoWidgetKey widgetKey:widgetKeyList){
                        //拼接替换字符串，格式应该为：#{keyCode}
                        String keyValueReplaceStr = ViewRenderingConstant.RENDERER_KEY_VALUE_ATT_PREFIX + widgetKey.getKeyCode() + ViewRenderingConstant.RENDERER_KEY_VALUE_ATT_SUFFIX;

                        ActStoElementValue elementValue = actStoElementValueService.getElementValueByElementIdAndKeyId(elementId,widgetKey.getKeyId());

                        if(elementValue==null)
                            continue;

                        String replaceValue = elementValue.getValueContent();
                        widgetRenderer = widgetRenderer.replace(keyValueReplaceStr,replaceValue);
                    }
                }
                //------------------解析key-value属性替换相关逻辑 end------------------------

                //获取元素事件
                ActStoElementEvent eeCondition = new ActStoElementEvent();
                eeCondition.setElementId(elementId);
                List<ActStoElementEvent> elementEventList = elementEventMapper.listActStoElementEvent(eeCondition);

                if(elementEventList!=null&&elementEventList.size()>0){
                    for(ActStoElementEvent elementEvent:elementEventList){
                        eventBuff.append(" "+elementEvent.getEventCode());
                        eventBuff.append("=\"");

                        if(ViewRenderingConstant.IS_LOCAL_FUNC_YES.equals(elementEvent.getIsLocalFunc())){
                            eventBuff.append(elementEvent.getEventFunc());
                        }else{
                            eventBuff.append(elementEvent.getEventScript());
                        }

                        eventBuff.append("\"");
                    }
                }

                if(eventBuff.length()>0&&widgetRenderer!=null&&!"".equals(widgetRenderer.trim())){
                    btnBuff.append(" ");
                    btnBuff.append(widgetRenderer.replace(ViewRenderingConstant.RENDERER_FLAG_EVENT,eventBuff.toString()));
                }else{
                    btnBuff.append(" ");
                    btnBuff.append(widgetRenderer);
                }
                btnList.add(btnBuff.toString());
            }

            //把行元素加到对应行数据中
            if (datas != null && datas.size() > 0){

                for(Map currData : datas){
                    StringBuffer btnStringBuffer = new StringBuffer("");
                    Double signState = (Double) currData.get("SIGN_STATE_");
                    if(btnList.size()>0){
                        for(String btnStr:btnList){
                            boolean isSignBtn = btnStr.contains("签收");
                            boolean isBanliBtn = btnStr.contains("办理");
                            if(signState!=null&&signState==0d&&isBanliBtn){
                                continue;
                            }
                            if(signState!=null&&signState==1d&&isSignBtn){
                                continue;
                            }
                            btnStringBuffer.append(btnStr);
                        }
                    }
                    String btnStr = btnStringBuffer.toString();
                    this.replaceVarsMethod(btnStr,currData);
                }

//                String btnStr = btnStringBuffer.toString();
//                for (Map currData : datas){
//
//                }
            }
        }

        /*if (viewElementList.size() > 0) {
            if (data != null && data.size() > 0)
                for (Map currData : data)
                    currData.put("btnWidgetRenderers", viewElementList);
        }*/

        resultAllMap.put("rows", datas);
        resultAllMap.put("total", page.getTotal());
        return resultAllMap;
    }

    @Override
    public int getViewDataCount(String viewCode, Map<String, String> queryMap) throws Exception {
        if(StringUtils.isNotBlank(viewCode)){
//            return actSumViewService.fetchQueryResultDataCount(viewCode,queryMap);

            ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
            conditionalQueryRequest.setLoginName(SecurityContext.getCurrentUserName());
            List<TaskInfo> taskList = conditionalQueryMapper.listWaitDoTasks(conditionalQueryRequest);

//            return actSumViewService.fetchQueryResultDataCount(viewCode,queryMap);
            return taskList.size();
        }

        return 0;
    }

    /**
     * 递归替换变量值
     * @param btnStr
     * @param data
     */
    private void replaceVarsMethod(String btnStr,Map data){
         if(btnStr.length()>0&&(btnStr.indexOf(ViewRenderingConstant.RENDERER_VAR_PREFIX)!=-1)){
            String btnStrPrefix = btnStr.substring(0,btnStr.indexOf(ViewRenderingConstant.RENDERER_VAR_PREFIX));
            String btnStrSuffix = btnStr.substring(btnStr.indexOf(ViewRenderingConstant.RENDERER_VAR_SUFFIX)+1,btnStr.length());
            String varNameStr = btnStr.substring(btnStr.indexOf(ViewRenderingConstant.RENDERER_VAR_PREFIX)+2,btnStr.indexOf(ViewRenderingConstant.RENDERER_VAR_SUFFIX));

            String varData = (String)data.get(varNameStr);
            if(varData==null)
                varData = (String)data.get(varNameStr.toUpperCase());

            btnStr = btnStrPrefix + varData + btnStrSuffix;

            this.replaceVarsMethod(btnStr,data);
            return;
        }

        data.put("btnWidgetRenderers", btnStr);//把替换后的按钮字符串，放到当前行数据中
    }

    @Override
    public PageInfo getPageViewDateForEasyui(Page page, String viewId, Map<String, String> queryMap) throws Exception {
        if (StringUtils.isBlank(viewId)) return null;
        ActStoView actSumView = new ActStoView();
        actSumView.setViewId(viewId);
        PageHelper.startPage(page);//查询数据
        System.out.println(page);
        List<Map> data = actSumViewService.fetchQueryResult(actSumView, page, queryMap);
        return new PageInfo(data);
    }


    //获取 视图的表格表头字段
    @Override
    public List<BpmViewTableColumnEntity> renderTableCol(List<ActStoColumn> sourceColumnList) throws Exception {
        DatabaseColumnConvert databaseColumnConvert = DatabaseColumnConvert.getInstance(environment.getProperty(PROJ_JDBC_URL));
        List<BpmViewTableColumnEntity> tableColumnEntityList = new ArrayList<>();
        for (ActStoColumn column : sourceColumnList) {
            //如果显示
            if ("1".equals(column.getListHidden())) {
                BpmViewTableColumnEntity columnEntity = new BpmViewTableColumnEntity();
                columnEntity.setListComment(column.getListComment());
                columnEntity.setListHidden(column.getListHidden());
                columnEntity.setListName(column.getListName());
                columnEntity.setListOrder(column.getListOrder());
                columnEntity.setDefaultListOrder(column.getDefaultListOrder());
                columnEntity.setDefaultListOrderType(column.getDefaultListOrderType());
                columnEntity.setListOrdinalPosition(column.getListOrdinalPosition());
                columnEntity.setListRenderer(column.getListRenderer());
                columnEntity.setListWidth(column.getListWidth());
                columnEntity.setTextAlign(column.getListAlign());
                columnEntity.setListFormatFun(column.getListFormatFun());
                columnEntity.setListDicTypeCode(column.getListDicTypeCode());
                columnEntity.setQueryMulitSelect(column.getQueryMulitSelect());
                //数据字典类型编码值不为空时,格式化函数类型为 数据字典
                if (BpmViewTableColumnEntity.DicTypeCode.dicCodeTypeFormat.name().equals(column.getListFormatFun()) && StringUtils.isNoneEmpty(column.getListDicTypeCode())) {
                    //数据字典集合塞入 列对象中
                    columnEntity.convertDicCodeItemsToMap(bscDicCodeService.getActiveItemsByTypeCode(column.getListDicTypeCode(), SecurityContext.getCurrentOrgId()));
                }
                //设置对应的JAVA数据类型
                if (StringUtils.isNoneEmpty(column.getDataType())) {
                    columnEntity.setJavaDataType(databaseColumnConvert.typeNameConvert(column.getDataType()));
                }

                tableColumnEntityList.add(columnEntity);
            }
        }

        if (tableColumnEntityList.size() > 0) {
            Collections.sort(tableColumnEntityList, new Comparator<BpmViewTableColumnEntity>() {
                @Override
                public int compare(BpmViewTableColumnEntity o1, BpmViewTableColumnEntity o2) {
                    return o1.getListOrdinalPosition().compareTo(o2.getListOrdinalPosition());
                }
            });
        }
        return tableColumnEntityList;
    }

}
