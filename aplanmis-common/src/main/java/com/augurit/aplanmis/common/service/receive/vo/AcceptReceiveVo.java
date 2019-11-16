package com.augurit.aplanmis.common.service.receive.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 受理回执
 */
@Data
@ApiModel("受理回执返回实体")
@EqualsAndHashCode(callSuper = true)
public class AcceptReceiveVo extends ReceiveBaseVo {
    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;

}
