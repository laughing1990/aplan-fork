package com.augurit.agcloud.bpm.common.engine;

import com.augurit.agcloud.bpm.common.mapper.ActTplAppFlowdefMapper;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 功能：用于在系统启动后异步加载流程图数据到内存中
 *
 * @author:chendx
 * @date: 2019-10-21
 * @time: 14:37
 */
@Component
public class BpmnModelLoadingRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(BpmnModelLoadingRunner.class);

    @Autowired
    private ActTplAppFlowdefMapper actTplAppFlowdefMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long l = System.currentTimeMillis();
        logger.debug("启动完成后运行加载流程模型数据到内存中，开始！");

        //1、先获取所有关联到模板上的流程key
        List<String> allProcKeyByApp = actTplAppFlowdefMapper.getAllProcKeyByApp();
        for (String defKey : allProcKeyByApp) {
            if (StringUtils.isBlank(defKey)) continue;
            ProcessDefinition def = null;
            try {
                //2、根据key获取到当前流程最新的版本
                def = repositoryService.createProcessDefinitionQuery().processDefinitionKey(defKey).latestVersion().singleResult();
            } catch (Exception e) {
                logger.debug("流程[" + defKey + "]查询出错！" + e.getMessage());
            }
            if (def != null) {
                try {
                    //3、加载model到内存中，缓存起来
                    repositoryService.getBpmnModel(def.getId());
                } catch (Exception e) {
                    logger.debug("流程[" + def.getName() + "]加载流程模型数据到内存中失败！" + e.getMessage());
                }
            }
        }
        logger.debug("启动完成后运行加载流程模型数据到内存中，结束！耗时" + (System.currentTimeMillis() - l) + "毫秒。");
    }
}
