package com.augurit.efficiency.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaAnaStatisticsRecord;
import com.augurit.efficiency.service.impl.StatisticsRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by zhangwn on 2019/10/15.
 * 运维查看统计数据生成记录
 */
@Slf4j
@RestController
@RequestMapping("/statistics/record")
public class StatisticsRecordController {

    @Autowired
    private StatisticsRecordService statisticsRecordService;

    private static String STATISTICS_RECORD_INDEX = "efficiency/backStage/index";

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index(){
        return new ModelAndView(STATISTICS_RECORD_INDEX);
    }

    @GetMapping("/listStatisticsRecord")
    @ApiOperation(value = "分页查询统计记录")
    public ContentResultForm listStatisticsRecord(String keyword, Page page){
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            List<AeaAnaStatisticsRecord> list = statisticsRecordService.statisticsRecordService(keyword,page);
            resultForm.setSuccess(true);
            resultForm.setContent(PageHelper.toEasyuiPageInfo(new PageInfo<>(list)));
        }catch (Exception e){
            log.info(e.getMessage());
            resultForm.setMessage("查询出错");
            return resultForm;
        }
        return resultForm;
    }
}
