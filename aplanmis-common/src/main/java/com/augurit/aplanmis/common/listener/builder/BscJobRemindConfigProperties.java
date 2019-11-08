package com.augurit.aplanmis.common.listener.builder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aplanmis.event.job.remind")
@Configuration
@Data
public class BscJobRemindConfigProperties {

    // 提醒方式 0表示钉钉，1表示微信，2表示邮件，3表示短信, 4: 应用内
    private String mode = "3";

    // 提醒次数
    private long count = 1L;

    // 每次提醒间隔的数值
    private long internal = 3L;

    // 每次提醒间隔的单位 m-分钟，h-小时，d-天
    private String internalUnit = "h";

    // 提醒类型，0表示指定日期提醒，1表示周期性提醒
    private String type = "0";

    // 周期性提醒时间 如：9:00（TYPE为1时）
    private String cycleTime;

    // 周期规则 1-7表示星期一至星期日，d表示每天，wd表示工作日，mf表示周一至周五（TYPE为1时）
    private String cycleRule;

    // 错误处理方式，0表示忽略错误不处理，1表示下次重新尝试提醒
    private String errorProcessMode = "0";

    private String tableName = "AEA_HI_APPLYINST";
    private String pkName = "APPLYINST_ID";
}
