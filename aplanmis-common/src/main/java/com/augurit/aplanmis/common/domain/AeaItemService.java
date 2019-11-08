package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 中介事项及对应的行政事项实体
 */
@Data
@ApiModel(value = "中介事项及对应的行政事项实体",description = "中介事项及对应的行政事项实体")
public class AeaItemService {
    @ApiModelProperty(value = "中介事项主键")
    private String agentItemBasicId;
    @ApiModelProperty(value = "中介事项版本ID")
    private String agentItemVerId;
    @ApiModelProperty(value = "中介事项ID")
    private String agentItemId;
    @ApiModelProperty(value = "中介事项编码")
    private String agentItemCode;
    @ApiModelProperty(value = "中介事项名称")
    private String agentItemName;
    @ApiModelProperty(value = "中介事项对应的部门ID")
    private String agentOrgId;
    @ApiModelProperty(value = "中介事项名称")
    private String agentOrgName;
    @ApiModelProperty(value = "对应的行政事项名称")
    private String itemName;
    @ApiModelProperty(value = "中介事项创建时间")
    private String createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "中介事项发布时间")
    private Date publishTime;
    @ApiModelProperty(value = "查询拓展字段",hidden = true)
    private String[] orgIds;
    @ApiModelProperty(value = "所属服务ID")
    private String serviceId;
    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;

    private List serviceIds;// 用于参数转换

}
