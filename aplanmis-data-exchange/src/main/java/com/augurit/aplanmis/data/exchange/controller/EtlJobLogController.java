package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.data.exchange.constant.TableName;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJob;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlErrorLogService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobDetailLogService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobLogService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:yinlf</li>
 * <li>创建时间：2019-10-23 17:14:43</li>
 * </ul>
 */
@RestController
@RequestMapping("/etl/job/log")
public class EtlJobLogController {

    private static Logger logger = LoggerFactory.getLogger(EtlJobLogController.class);

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    private EtlJobDetailLogService etlJobDetailLogService;

    @Autowired
    EtlErrorLogService etlErrorLogService;

    @Autowired
    private EtlJobService etlJobService;

    @GetMapping("/index")
    public ModelAndView indexEtlJobLog() {
        return new ModelAndView("log/index");
    }

    @GetMapping("/list")
    public PageInfo<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", etlJobLog);
        return etlJobLogService.listEtlJobLog(etlJobLog, page);
    }

    @GetMapping("/details")
    public ResultForm listEtlJobLog(String jobLogId) throws Exception {
        List<EtlJobDetailLog> etlJobDetailLog = etlJobDetailLogService.findEtlJobDetailLogByJobLogId(jobLogId);
        etlJobDetailLog.stream().forEach(detailLog -> {
            String tableName = detailLog.getTableName();
            detailLog.setTableName(TableName.toMap().get(tableName));
        });
        return new ContentResultForm<>(true, etlJobDetailLog);
    }

    @DeleteMapping("/{jobLogIds}")
    public ResultForm deleteEtlJobLogById(@PathVariable String[] jobLogIds) throws Exception {
        logger.debug("删除Form对象，对象id为：{}", jobLogIds);
        try {
            if (jobLogIds != null) {
                etlJobLogService.batchDeleteEtlJobLogByJobLogIds(jobLogIds);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @GetMapping("/status")
    public ResultForm getRunStatus() {
        EtlJob job = etlJobService.getEtlJobById("1");
        return new ContentResultForm<>(true, job);
    }
    @RequestMapping("/getEtlJobLog.do")
    public EtlJobLog getEtlJobLog(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取EtlJobLog对象，ID为：{}", id);
            return etlJobLogService.getEtlJobLogById(id);
        } else {
            logger.debug("构建新的EtlJobLog对象");
            return new EtlJobLog();
        }
    }

    @RequestMapping("/updateEtlJobLog.do")
    public ResultForm updateEtlJobLog(EtlJobLog etlJobLog) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", etlJobLog);
        etlJobLogService.updateEtlJobLog(etlJobLog);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑
     *
     * @param etlJobLog
     * @param result    校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveEtlJobLog.do")
    public ResultForm saveEtlJobLog(EtlJobLog etlJobLog, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(etlJobLog);
        }

        if (etlJobLog.getJobLogId() != null && !"".equals(etlJobLog.getJobLogId())) {
            etlJobLogService.updateEtlJobLog(etlJobLog);
        } else if (etlJobLog.getJobLogId() == null || "".equals(etlJobLog.getJobLogId())) {
            etlJobLogService.saveEtlJobLog(etlJobLog);
        }

        return new ContentResultForm<EtlJobLog>(true, etlJobLog);
    }
}
