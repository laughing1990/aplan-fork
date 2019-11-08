package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xiaohutu
 * 入住服务列表
 */
@Data
@ApiModel(value = "入住服务",description = "入住服务")
public class AgentUnitService {
    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "单位名称")
    private String applicant;
    @ApiModelProperty(value = "单位类型：企业，事业单位，社会团体")
    private String unitNature;
    @ApiModelProperty(value = "中介事项主键",hidden = true)
    private String unitType;
    @ApiModelProperty(value = "中介事项主键",hidden = true)
    private String idtype;
    @ApiModelProperty(value = "服务类型编码")
    private String serviceId;
    @ApiModelProperty(value = "服务类型编码",hidden = true)
    private String serviceCode;
    @ApiModelProperty(value = "服务类型名称")
    private String serviceName;
    @ApiModelProperty(value = "星级评价 0：暂无评价、1：0-3、2：3-4、3：4-5")
    private String compEvaluation;
    @ApiModelProperty(value = "行政区（园区）",hidden = true)
    private String applicantDistrict;
    @ApiModelProperty(value = "时间排序 倒叙 desc 正序 默认acs")
    private String publishTimeOrderSort;
    @ApiModelProperty(value = "星级排序 倒叙 desc 正序 默认acs")
    private String startOrderSort;
    @ApiModelProperty(value = "成交数量排序 倒叙 desc 正序 默认acs")
    private String biddingNumOrderSort;
    @ApiModelProperty(value = "审核状态")
    private String auditFlag;
    @ApiModelProperty(value = "服务笔数")
    private String serviceNum;
    @ApiModelProperty(value = "成交数量")
    private String biddingNum;
    @ApiModelProperty(value = "中介事项主键")
    private String unitServiceId;
    @ApiModelProperty(value = "中介事项主键" ,hidden = true)
    private String imgUrl;
    @ApiModelProperty(value = "用于参数转换",hidden = true)
    private List serviceIds;
    @ApiModelProperty(value = "统一社会信用代码")
    private String unifiedSocialCreditCode;
    @ApiModelProperty(value = "失信记录")
    private AeaCreditSummaryAllDto breakFaithRecord;
}
