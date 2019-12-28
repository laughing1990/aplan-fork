package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.data.exchange.constant.EtlConstant;
import com.augurit.aplanmis.data.exchange.dto.ThreadEtlJobLog;
import com.augurit.aplanmis.data.exchange.service.ImportService;
import com.augurit.aplanmis.data.exchange.vo.EtlReuploadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 数据重新上传
 *
 * @author yinlf
 * @Date 2019/11/22
 */
@RestController
@RequestMapping("/data/picket")
@Slf4j
public class DataPicketController {


    @Autowired
    ImportService importService;

    /**
     * 鲁普洛德所有
     */
    @PostMapping("/reupload/table_name")
    public ResultForm reuploadByTableName(EtlReuploadVo etlReuploadVo) {
        importService.importByTableName(etlReuploadVo, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德所有
     */
    @PostMapping("/reupload/all")
    public ResultForm reuploadAll(Date startTime, Date endTime) {
        importService.importAllTableAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 不包含单位和附件
     */
    @PostMapping("/reupload/main")
    public ResultForm reuploadMain(Date startTime, Date endTime) {
        importService.importThemeMainAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德主题
     */
    @PostMapping("/reupload/theme_ver")
    public ResultForm reuploadThemeVer(Date startTime, Date endTime) {
        importService.importThemeVerAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德阶段
     */
    @PostMapping("/reupload/stage")
    public ResultForm reuploadStage(Date startTime, Date endTime) {
        importService.importStageAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德事项
     */
    @PostMapping("/reupload/item")
    public ResultForm reuploadItem(Date startTime, Date endTime) {
        importService.importItemAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德项目
     */
    @PostMapping("/reupload/proj")
    public ResultForm reuploadProj(Date startTime, Date endTime) {
        importService.importProjAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德单位
     */
    @PostMapping("/reupload/unit")
    public ResultForm reuploadUnit(Date startTime, Date endTime) {
        importService.importUnitAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德事项实例
     */
    @PostMapping("/reupload/iteminst")
    public ResultForm reuploadIteminst(Date startTime, Date endTime) {
        importService.importIteminstAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德事项办理详细信息
     */
    @PostMapping("/reupload/item_opinion")
    public ResultForm reuploadItemOpinion(Date startTime, Date endTime) {
        importService.importItemOpinionAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德批文批复
     */
    @PostMapping("/reupload/offic_doc")
    public ResultForm reuploadOfficDoc(Date startTime, Date endTime) {
        importService.importOfficDocAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德
     */
    @PostMapping("/reupload/item_matinst")
    public ResultForm reuploadItemMatinst(Date startTime, Date endTime) {
        importService.importItemMatinstAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德
     */
    @PostMapping("/reupload/item_special")
    public ResultForm reuploadItemSpecial(Date startTime, Date endTime) {
        importService.importItemSpecialAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德
     */
    @PostMapping("/reupload/proj_purchase")
    public ResultForm reuploadProjPurchase(Date startTime, Date endTime) {
        importService.importProjPurchaseAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

    /**
     * 鲁普洛德
     */
    @PostMapping("/reupload/proj_purchase_opinion")
    public ResultForm reuploadProjPurchaseOpinion(Date startTime, Date endTime) {
        importService.importProjPurchaseOpinionAndLog(startTime, endTime, EtlConstant.MAN_OPERATE);
        return new ContentResultForm<>(true, ThreadEtlJobLog.get());
    }

}
