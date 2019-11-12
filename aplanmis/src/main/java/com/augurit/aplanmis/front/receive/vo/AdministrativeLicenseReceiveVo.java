package com.augurit.aplanmis.front.receive.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("行政回执返回实体")
@EqualsAndHashCode(callSuper = true)
public class AdministrativeLicenseReceiveVo extends ReceiveBaseVo{

    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;
}
