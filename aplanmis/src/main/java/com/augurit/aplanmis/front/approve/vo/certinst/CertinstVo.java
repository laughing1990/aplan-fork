package com.augurit.aplanmis.front.approve.vo.certinst;

import com.augurit.agcloud.framework.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.util.Date;

@Data
@ApiModel(value = "保存或修改证照实例vo", description = "保存或修改证照实例vo")
public class CertinstVo {
    @ApiModelProperty(name = "certinstId", value = "证照实例id")
    private String certinstId;

    @ApiModelProperty(name = "certId", value = "证照定义ID", required = true)
    private String certId;
    @ApiModelProperty(name = "certinstCode", value = "证照编码", required = true)
    private String certinstCode;

    @ApiModelProperty(name = "certinstName", value = "证照名称")
    private String certinstName;

    //    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "issueDate", value = " (发证日期)", required = true)
    private String issueDate;
    //private Date issueDate;

    //private String publishDate;

    public Date strToDate(String dateStr) {
        try {
            return DateUtils.toDefaultDate(dateStr);
        } catch (ParseException e) {
            //loggger.error("新建批文批复操作中解析日期失败, dateStr: " + dateStr + e.getMessage(), e);
            throw new RuntimeException("新建批文批复操作中解析日期失败, dateStr: " + dateStr, e);
        }
    }

    @ApiModelProperty(name = "issueOrgId", value = "颁发单位ID")
    private String issueOrgId;

    @ApiModelProperty(name = "iteminstId", value = "事项实例ID", required = true)
    private String iteminstId;

    @ApiModelProperty(name = "applyinstId", value = "申请实例ID", required = true)
    private String applyinstId;

    @ApiModelProperty(name = "projScale", value = "建设规模", required = true)
    private Double projScale;

    @ApiModelProperty(name = "memo", value = "备注说明")
    private String memo;

    @ApiModelProperty(name = "creater", value = "创建人", hidden = true)
    private String creater;

    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private String matId;

    private String matinstId;

}
