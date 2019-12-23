package com.augurit.aplanmis.supermarket.dataexchange.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传数据配置
 */
@Component
@ConfigurationProperties(prefix = "upload.data", ignoreInvalidFields = true)
@Data
@ApiModel
public class UploadDataConfig {

    @ApiModelProperty(value = "是否启用上传数据，true 是，false 否")
    private boolean open;

    @ApiModelProperty(value = "当前城市在省厅注册的rootOrgId")
    private String provinceCityCode;

    @ApiModelProperty(value = "地市系统配置的rootOrgId")
    private String localCityCode;

    @ApiModelProperty("同步频率，默认每天凌晨2点")
    private String timeCorn;
}
