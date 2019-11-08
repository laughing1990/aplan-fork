package com.augurit.aplanmis.bpm.common.timeCalculate.controller;

import com.augurit.agcloud.bpm.common.timeCalculate.timer.TimeCalculateTimer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/bpmThreadPool")
@Api(value = "流程已用时计算线程池操作方法", tags = "流程已用时计算线程池操作方法")
@Slf4j
public class TimeCalculateThreadPool {

    @Autowired
    private TimeCalculateTimer timeCalculateTimer;

    @GetMapping("/shutdown")
    public ResultForm shutdown() {
        try {
            timeCalculateTimer.poolShutDown();

        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage());
        }
        return new ResultForm(true);
    }

    @GetMapping("/restart")
    public ResultForm restart() {
        try {
            timeCalculateTimer.poolShutDown();
            Thread.sleep(100);
            timeCalculateTimer.startTimer();
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage());
        }
        return new ResultForm(true);
    }

}
