package com.augurit;


import com.augurit.agcloud.meta.sc.db.service.MetaDbConnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <spring boot初始化>
 * meta元数据连接自动校正
 *
 * @author gaokang
 * @version 1.0
 */
@Component
@Order(2) // 通过order值的大小来决定启动的顺序
public class MetaInitSpringBoot implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(MetaInitSpringBoot.class);

    @Autowired
    private MetaDbConnService metaDbConnService;

    @Override
    public void run(String... args) throws Exception {
        log.info("springboot所有bean初始化完成!进行元数据初始化...");
        metaDbConnService.metaConnAutoInitCorrection();
    }

}
