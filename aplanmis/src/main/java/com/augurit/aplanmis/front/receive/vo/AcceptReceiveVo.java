package com.augurit.aplanmis.front.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 受理回执
 */
@Data
@ApiModel("受理回执返回实体")
public class AcceptReceiveVo extends ReceiveBaseVo {
    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;

}
