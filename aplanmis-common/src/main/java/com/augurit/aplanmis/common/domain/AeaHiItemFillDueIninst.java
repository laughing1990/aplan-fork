package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.List;

/**
 * 事项容缺要求补齐材料实例表-模型
 */
@Data
public class AeaHiItemFillDueIninst implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键")
    private String dueIninstId;

    @ApiModelProperty("补齐ID")
    private String fillId;

    @ApiModelProperty("输入输出实例ID")
    private String inoutinstId;

    @ApiModelProperty("补齐意见")
    private String fillOpinion; // (补齐意见)

    @ApiModelProperty("补齐截止期限类型，0表示证件领取前，1表示证件签发前，2表示证件领取后3个月")
    private String timeLimitType;

    @ApiModelProperty("是否通过，0表示未通过，1表示通过")
    private String isPass;

    @ApiModelProperty("附件总数")
    private Long attCount;

    @ApiModelProperty("要求纸质材料数量")
    private Long paperCount;

    @ApiModelProperty("要求复印件材料数量")
    private Long copyCount;

    @ApiModelProperty("是否需要电子件：1 是，0 否")
    private String isNeedAtt;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty("根组织ID")
    private String rootOrgId;

    //非表字段
    @ApiModelProperty("材料名称")
    private String matName;

    @ApiModelProperty("容缺承诺书附件ID")
    private String detailId;

    @ApiModelProperty("实际上传附件集合")
    private List<BscAttDetail> detailList;

}
