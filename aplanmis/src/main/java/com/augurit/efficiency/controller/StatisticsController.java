package com.augurit.efficiency.controller;


import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.efficiency.constant.OperateSource;
import com.augurit.efficiency.service.AeaAnaOrgUseTimeStatisticService;
import com.augurit.efficiency.service.AeaAnaWinUseTimeStatisticService;
import com.augurit.efficiency.service.impl.AeaAnaOrgStatisticsImpl;
import com.augurit.efficiency.service.impl.AeaAnaThemeStatisticsImpl;
import com.augurit.efficiency.service.impl.AeaAnaWinStatisticsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangwn on 2019/9/27.
 * 临时生成统计数据使用
 */
@RestController
@RequestMapping("/executeStatistics")
public class StatisticsController {

    private static Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private AeaAnaOrgStatisticsImpl aeaAnaOrgStatisticsImpl;
    @Autowired
    private AeaAnaThemeStatisticsImpl aeaAnaThemeStatisticsImpl;
    @Autowired
    private AeaAnaWinStatisticsImpl aeaAnaWinStatisticsImpl;
    @Autowired
    private AeaAnaWinUseTimeStatisticService aeaAnaWinUseTimeStatisticService;
    @Autowired
    private AeaAnaOrgUseTimeStatisticService aeaAnaOrgUseTimeStatisticService;

    @Value("${dg.sso.access.platform.org.top-org-id:}")
    private String rootOrgId;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/org")
    public ResultForm executeStatisticsOrg(String startDay,String endDay) throws Exception{
        ResultForm resultForm = new ResultForm(false);
        if(StringUtils.isNotBlank(startDay) && StringUtils.isNotBlank(endDay) ){
            List<Date> dates = DateUtils.calBetweenDaysDate(startDay, endDay);
            StringBuilder sb = new StringBuilder();
            for(Date date:dates){
                try {
                    statisticsOrg(date);
                    resultForm.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                    String errorMsg = "统计日期：" + DateUtils.formatDate(date,DateUtils.FMT_yyyy_MM_dd) + "，错误信息：" + e.getMessage();
                    logger.info(errorMsg);
                    sb.append(errorMsg);
                }
            }
            resultForm.setSuccess(sb.length()==0);
            resultForm.setMessage(sb.toString());
        }else{
            resultForm.setMessage("日期不能为空");
        }
        return resultForm;
    }
    @RequestMapping("/theme")
    public ResultForm executeStatisticsTheme(String startDay,String endDay)throws Exception{
        ResultForm resultForm = new ResultForm(false);
        if(StringUtils.isNotBlank(startDay) && StringUtils.isNotBlank(endDay) ){
            List<Date> dates = DateUtils.calBetweenDaysDate(startDay, endDay);
            StringBuilder sb = new StringBuilder();
            for(Date date:dates){
                try {
                    statisticsTheme(date);
                    resultForm.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                    String errorMsg = "统计日期：" + DateUtils.formatDate(date,DateUtils.FMT_yyyy_MM_dd) + "，错误信息：" + e.getMessage();
                    logger.info(errorMsg);
                    sb.append(errorMsg);
                }
            }
            resultForm.setSuccess(sb.length()==0);
            resultForm.setMessage(sb.toString());
        }else{
            resultForm.setMessage("日期不能为空");
        }
        return resultForm;
    }
    @RequestMapping("/win")
    public ResultForm executeStatisticsWin(String startDay,String endDay)throws Exception{
        ResultForm resultForm = new ResultForm(false);
        if(StringUtils.isNotBlank(startDay) && StringUtils.isNotBlank(endDay) ){
            List<Date> dates = DateUtils.calBetweenDaysDate(startDay, endDay);
            StringBuilder sb = new StringBuilder();
            for(Date date:dates){
                try {
                    statisticsWin(date);
                    resultForm.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                    String errorMsg = "统计日期：" + DateUtils.formatDate(date,DateUtils.FMT_yyyy_MM_dd) + "，错误信息：" + e.getMessage();
                    logger.info(errorMsg);
                    sb.append(errorMsg);
                }
            }
            resultForm.setSuccess(sb.length()==0);
            resultForm.setMessage(sb.toString());
        }else{
            resultForm.setMessage("日期不能为空");
        }
        return resultForm;
    }
    @RequestMapping("/winUseTime")
    public ResultForm executeWinUseTimeStatistics(){
        ResultForm resultForm = new ResultForm(false);
        try {
            aeaAnaWinUseTimeStatisticService.doWinUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
            resultForm.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            resultForm.setMessage("统计出错："+e.getMessage());
            return resultForm;
        }
        return resultForm;
    }
    @RequestMapping("/orgUseTime")
    public ResultForm executeOrgUseTimeStatistics(){
        ResultForm resultForm = new ResultForm(false);
        try {
            aeaAnaOrgUseTimeStatisticService.doUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
            resultForm.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            resultForm.setMessage("统计出错："+e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    private void statisticsOrg(Date date) throws Exception {
        //1、统计日办件
        aeaAnaOrgStatisticsImpl.statisticsOrgDayData(rootOrgId, OperateSource.LOGIN_USER_CREATE.getValue(), SecurityContext.getCurrentUserName(),date);
        //2、统计周办件
        aeaAnaOrgStatisticsImpl.statisticsOrgWeekData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //3、统计月办件
        aeaAnaOrgStatisticsImpl.statisticsOrgMonthData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //4、统计年办件
        aeaAnaOrgStatisticsImpl.statisticsOrgYearData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
    }
    private void statisticsTheme(Date date) throws Exception {
        //1、统计日办件
        aeaAnaThemeStatisticsImpl.statisticsThemeDayData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //2、统计周办件
        aeaAnaThemeStatisticsImpl.statisticsThemeWeekData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //3、统计月办件
        aeaAnaThemeStatisticsImpl.statisticsThemeMonthData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //4、统计年办件
        aeaAnaThemeStatisticsImpl.statisticsThemeYearData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
    }
    private void statisticsWin(Date date) throws Exception {
        //1、统计日办件
        aeaAnaWinStatisticsImpl.statisticsWinDayData(rootOrgId,OperateSource.LOGIN_USER_CREATE.getValue(),SecurityContext.getCurrentUserName(),date);
        //2、统计周办件
        aeaAnaWinStatisticsImpl.statisticsWinWeekData(rootOrgId, OperateSource.LOGIN_USER_CREATE.getValue(), SecurityContext.getCurrentUserName(), date);
        //3、统计月办件
        aeaAnaWinStatisticsImpl.statisticsWinMonthData(rootOrgId, OperateSource.LOGIN_USER_CREATE.getValue(), SecurityContext.getCurrentUserName(), date);
        //4、统计年办件
        aeaAnaWinStatisticsImpl.statisticsWinYearData(rootOrgId, OperateSource.LOGIN_USER_CREATE.getValue(), SecurityContext.getCurrentUserName(), date);
    }

}
