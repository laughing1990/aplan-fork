package com.augurit.aplanmis.front.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("行政回执返回实体")
public class AdministrativeLicenseReceiveVo extends ReceiveBaseVo{

    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;
}
