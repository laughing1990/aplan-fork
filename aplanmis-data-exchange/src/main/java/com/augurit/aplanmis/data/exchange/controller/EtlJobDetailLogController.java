package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobDetailLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:yinlf</li>
 * <li>创建时间：2019-10-24 14:58:33</li>
 * </ul>
 */
@RestController
@RequestMapping("/etl/job/detail/log")
public class EtlJobDetailLogController {

    private static Logger logger = LoggerFactory.getLogger(EtlJobDetailLogController.class);

    @Autowired
    private EtlJobDetailLogService etlJobDetailLogService;


    @RequestMapping("/indexEtlJobDetailLog.do")
    public ModelAndView indexEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog, String infoType) {
        return new ModelAndView("etl/job/detail/detail_log_index");
    }

    @RequestMapping("/listEtlJobDetailLog.do")
    public PageInfo<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", etlJobDetailLog);
        return etlJobDetailLogService.listEtlJobDetailLog(etlJobDetailLog, page);
    }

    @RequestMapping("/getEtlJobDetailLog.do")
    public EtlJobDetailLog getEtlJobDetailLog(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取EtlJobDetailLog对象，ID为：{}", id);
            return etlJobDetailLogService.getEtlJobDetailLogById(id);
        } else {
            logger.debug("构建新的EtlJobDetailLog对象");
            return new EtlJobDetailLog();
        }
    }

    @RequestMapping("/updateEtlJobDetailLog.do")
    public ResultForm updateEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", etlJobDetailLog);
        etlJobDetailLogService.updateEtlJobDetailLog(etlJobDetailLog);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑定时上传执行详细日志
     *
     * @param etlJobDetailLog 定时上传执行详细日志
     * @param result          校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveEtlJobDetailLog.do")
    public ResultForm saveEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存定时上传执行详细日志Form对象出错");
            throw new InvalidParameterException(etlJobDetailLog);
        }

        if (etlJobDetailLog.getDetailLogId() != null && !"".equals(etlJobDetailLog.getDetailLogId())) {
            etlJobDetailLogService.updateEtlJobDetailLog(etlJobDetailLog);
        } else {
            if (etlJobDetailLog.getDetailLogId() == null || "".equals(etlJobDetailLog.getDetailLogId())) {
                etlJobDetailLogService.saveEtlJobDetailLog(etlJobDetailLog);
            }
        }

        return new ContentResultForm<EtlJobDetailLog>(true, etlJobDetailLog);
    }

    @RequestMapping("/deleteEtlJobDetailLogById.do")
    public ResultForm deleteEtlJobDetailLogById(String id) throws Exception {
        logger.debug("删除定时上传执行详细日志Form对象，对象id为：{}", id);
        if (id != null) {
            etlJobDetailLogService.deleteEtlJobDetailLogById(id);
        }
        return new ResultForm(true);
    }

}
