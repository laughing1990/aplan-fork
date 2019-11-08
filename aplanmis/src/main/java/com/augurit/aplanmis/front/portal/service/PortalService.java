package com.augurit.aplanmis.front.portal.service;

import com.augurit.agcloud.bpm.common.domain.ActStoView;
import com.augurit.agcloud.bpm.common.mapper.ActStoViewMapper;
import com.augurit.agcloud.bpm.front.service.BpmViewDynRenderingService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuRsRole;
import com.augurit.agcloud.opus.common.mapper.OpuRsRoleMapper;
import com.augurit.aplanmis.common.constants.StandardStageCode;
import com.augurit.aplanmis.common.constants.StrandardThemeType;
import com.augurit.aplanmis.common.constants.TaskDefContants;
import com.augurit.aplanmis.common.domain.StageTotalForm;
import com.augurit.aplanmis.common.domain.TaskCountForm;
import com.augurit.aplanmis.common.domain.TotalItemForm;
import com.augurit.aplanmis.common.mapper.PortMapper;
import com.augurit.aplanmis.front.constant.PortalContants;
import com.augurit.aplanmis.front.portal.vo.EchartsBarVo;
import com.augurit.aplanmis.front.portal.vo.ItemAndApplyCountVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PortalService {

    private static Logger LOGGER = LoggerFactory.getLogger(PortalService.class);

    @Autowired
    private BpmViewDynRenderingService bpmViewDynRenderingService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private PortMapper portMapper;

    @Autowired
    BscDicCodeMapper bscDicCodeMapper;

    @Autowired
    ActStoViewMapper actStoViewMapper;
    @Autowired
    private OpuRsRoleMapper opuRsRoleMapper;

    public EchartsBarVo findTotalItemBaseOnTheme() {
        List<TotalItemForm> totalItemFormList = portMapper.findTotalItemBaseOnTheme();
        EchartsBarVo echartsBarVo = new EchartsBarVo();
        List<String> legendData = new ArrayList<>();
        List<String> xAxisData = new ArrayList<>();
        List<List<Integer>> seriesData = new ArrayList<>();
        legendData.add("本月新增数量");
        legendData.add("在办数量");
        legendData.add("已办数量");
        legendData.add("逾期数量");
        List<Integer> series1 = new ArrayList<>();
        List<Integer> series2 = new ArrayList<>();
        List<Integer> series3 = new ArrayList<>();
        List<Integer> series4 = new ArrayList<>();
        for (TotalItemForm totalItemForm : totalItemFormList) {
            xAxisData.add(totalItemForm.getThemeName());
            series1.add(totalItemForm.getNewItem());
            series2.add(totalItemForm.getProcessingItem());
            series3.add(totalItemForm.getFinishItem());
            series4.add(totalItemForm.getOverdueItem());
        }
        seriesData.add(series1);
        seriesData.add(series2);
        seriesData.add(series3);
        seriesData.add(series4);
        echartsBarVo.setLegendData(legendData);
        echartsBarVo.setxAxisData(xAxisData);
        echartsBarVo.setSeriesData(seriesData);
        return echartsBarVo;
    }

    public EchartsBarVo findTotalItemBaseOnStandardTheme() {
        List<TotalItemForm> totalItemFormList = portMapper.findTotalItemBaseOnStandardTheme();
        EchartsBarVo echartsBarVo = new EchartsBarVo();
        List<String> legendData = new ArrayList<>();
        List<String> xAxisData = new ArrayList<>();
        List<List<Integer>> seriesData = new ArrayList<>();
        legendData.add("本月新增数量");
        legendData.add("在办数量");
        legendData.add("已办数量");
        legendData.add("逾期数量");
        List<Integer> series1 = new ArrayList<>();
        List<Integer> series2 = new ArrayList<>();
        List<Integer> series3 = new ArrayList<>();
        List<Integer> series4 = new ArrayList<>();
        Map<String, String> strandardThemeTypeMap = StrandardThemeType.toMap();
        for (TotalItemForm totalItemForm : totalItemFormList) {
            xAxisData.add(strandardThemeTypeMap.get(totalItemForm.getThemeCode()));
            series1.add(totalItemForm.getNewItem());
            series2.add(totalItemForm.getProcessingItem());
            series3.add(totalItemForm.getFinishItem());
            series4.add(totalItemForm.getOverdueItem());
        }
        seriesData.add(series1);
        seriesData.add(series2);
        seriesData.add(series3);
        seriesData.add(series4);
        echartsBarVo.setLegendData(legendData);
        echartsBarVo.setxAxisData(xAxisData);
        echartsBarVo.setSeriesData(seriesData);
        return echartsBarVo;
    }

    public ItemAndApplyCountVo countItemAndApply() {
        ItemAndApplyCountVo countVo =  new ItemAndApplyCountVo();
        int wangShangDaiYuShen = 0;
        int caiLiaoBuZheng = 0;
        int duBan = 0;
        int yuQi = 0;
        int daiBan = 0;
        int yiBan = 0;
        int guaQi = 0;
        try {
            List<TaskCountForm> taskCountFormList =  portMapper.countItemAndApply();
            for(TaskCountForm form: taskCountFormList){
                if(2==form.getSuspensionState()){
                    guaQi++;
                }
                if(TaskDefContants.WANG_SHANG_DAI_YU_SHEN.contains(form.getTaskDefKey())){
                    wangShangDaiYuShen++;
                }else if(TaskDefContants.CAI_LIAO_BU_ZHENG.equalsIgnoreCase(form.getTaskDefKey())){
                    caiLiaoBuZheng++;
                }else{
                    int due = Integer.parseInt(form.getDueNum());
                    int use =Integer.parseInt(form.getUseWorkDay());
                    int cha = due - use;
                    if(cha>0&&cha<2){
                        duBan++;
                    }else if(cha<=0){
                        yuQi++;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("统计办件数量失败");
            e.printStackTrace();
        }
        // 待办
        //Map<String, String> queryMap = ViewProviderSqlCreater.parserAndGetQuestMap(request);
        try {
            BscDicCodeItem daiBanItem = bscDicCodeService.getItemByTypeCodeAndItemCode(PortalContants.VIEW_TPYE_CODE, PortalContants.DAI_BAN_VIEW, SecurityContext.getCurrentOrgId());
            if(daiBanItem!=null){
                String daiBanViewCode = daiBanItem.getItemName();
                int count = this.bpmViewDynRenderingService.getViewDataCount(daiBanViewCode, null);
                daiBan = count;
            }
        } catch (Exception e) {
            LOGGER.error("获取待办数量失败");
            e.printStackTrace();
        }
        //已办
        try {
            BscDicCodeItem yiBanItem = bscDicCodeService.getItemByTypeCodeAndItemCode(PortalContants.VIEW_TPYE_CODE, PortalContants.YI_BAN_VIEW, SecurityContext.getCurrentOrgId());
            if(yiBanItem!=null){
                String yiBanViewCode = yiBanItem.getItemName();
                int count = this.bpmViewDynRenderingService.getViewDataCount(yiBanViewCode, null);
                yiBan = count;
            }
        } catch (Exception e) {
            LOGGER.error("获取已办数量失败");
            e.printStackTrace();
        }

        countVo.setWangShangDaiYuShen(wangShangDaiYuShen);
        countVo.setCaiLiaoBuZheng(caiLiaoBuZheng);
        countVo.setDuBan(duBan);
        countVo.setYuQi(yuQi);
        countVo.setDaiBan(daiBan);
        countVo.setYiBan(yiBan);
        countVo.setGuaQi(guaQi);
        return countVo;
    }

    public EchartsBarVo findStageUseDay() {
        List<StageTotalForm> stageUseDayList = portMapper.findStageUseDay();
        EchartsBarVo echartsBarVo = new EchartsBarVo();
        List<String> legendData = new ArrayList<>();
        List<String> xAxisData = new ArrayList<>();
        List<List<Integer>> seriesData = new ArrayList<>();
        legendData.add("审批用时");
        legendData.add("跨度用时");
        List<Integer> series1 = new ArrayList<>();
        List<Integer> series2 = new ArrayList<>();
        for (StageTotalForm stageTotalForm : stageUseDayList) {
            xAxisData.add(stageTotalForm.getStageName());
            series1.add(stageTotalForm.getAvgUseWorkDay());
            series2.add(stageTotalForm.getAvgUseDay());
        }
        seriesData.add(series1);
        seriesData.add(series2);
        echartsBarVo.setLegendData(legendData);
        echartsBarVo.setxAxisData(xAxisData);
        echartsBarVo.setSeriesData(seriesData);
        return echartsBarVo;
    }

    public EchartsBarVo findStandardStageUseDay() {
        List<StageTotalForm> stageUseDayList = portMapper.findStandardStageUseDay();
        EchartsBarVo echartsBarVo = new EchartsBarVo();
        List<String> legendData = new ArrayList<>();
        List<String> xAxisData = new ArrayList<>();
        List<List<Integer>> seriesData = new ArrayList<>();
        legendData.add("审批用时");
        legendData.add("跨度用时");
        List<Integer> series1 = new ArrayList<>();
        List<Integer> series2 = new ArrayList<>();
        Map<String, String> map = StandardStageCode.toMap();
        //按实际对应阶段数量
        /*for (StageTotalForm stageTotalForm : stageUseDayList) {
            xAxisData.add(map.get(stageTotalForm.getSortNo()));
            series1.add(stageTotalForm.getAvgUseWorkDay());
            series2.add(stageTotalForm.getAvgUseDay());
        }*/
        boolean flag = false;
        //按标准阶段
        for(String key :map.keySet()){
            flag = false;
            xAxisData.add(map.get(key));
            for (StageTotalForm stageTotalForm : stageUseDayList) {
                if(stageTotalForm.getSortNo().equals(key)){
                    series1.add(stageTotalForm.getAvgUseWorkDay());
                    series2.add(stageTotalForm.getAvgUseDay());
                    flag = true;
                    break;
                }
            }
            if(!flag){
                series1.add(0);
                series2.add(0);
            }
        }
        seriesData.add(series1);
        seriesData.add(series2);
        echartsBarVo.setLegendData(legendData);
        echartsBarVo.setxAxisData(xAxisData);
        echartsBarVo.setSeriesData(seriesData);
        return echartsBarVo;
    }

    public Map<String, ActStoView> getDicViewCodeInfo() {
        Map<String, ActStoView> result = new HashMap<>();
        List<BscDicCodeItem> items = bscDicCodeMapper.getActiveItemsByTypeCode("VIEW_CODE_INFO", SecurityContext.getCurrentOrgId());
        if(items != null && items.size()>0){
            for(int i=0,len=items.size();i<len;i++){
                BscDicCodeItem item = items.get(i);
                //缺少一次查询多个viewCode的方法
                ActStoView view = actStoViewMapper.getActStoViewByViewCode(item.getItemName());
                result.put(item.getItemCode(),view);
            }
        }
        return result;
    }

    public boolean isWindowPn() {
        boolean bol = false;
        String userId = SecurityContext.getCurrentUserId();
        List<OpuRsRole> opuRsRoles = opuRsRoleMapper.listRoleByUserId(userId);
        if(opuRsRoles.size()>0){
            String result = opuRsRoles.stream().map(OpuRsRole::getRoleCode).collect(Collectors.joining(","));
            if (result.indexOf("window") >=0){
                bol = true;
            }
        }
        return bol;
    }
}
