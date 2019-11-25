package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.aplanmis.data.exchange.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据纠察
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
     * 鲁普洛德阶段
     */
    @PostMapping("/reupload")
    public void reuploadStage() {
        importService.initLogNum();//单例警告
        importService.importStage(null, null);
    }
}
