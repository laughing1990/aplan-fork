package com.augurit.aplanmis.front.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MatReceiveVo extends ReceiveBaseVo {

    @ApiModelProperty(value = "纸质材料")
    private List<AeaHiItemMatinst> paperMatList;

    @ApiModelProperty(value = "电子材料")
    private List<AeaHiItemMatinst> attrMatList;

    @ApiModelProperty(value = "接收人单位")
    private String receiveOrgName;

    @ApiModelProperty(value = "窗口收件人")
    private String tellerName;

    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;

    @ApiModelProperty(value = "部门简称")
    private String orgShortName;


}
