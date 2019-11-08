package com.augurit.aplanmis.front.common.vo;

import com.augurit.agcloud.bpm.common.domain.ActUserOpinion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * @author mohaoqi
 * @date 2019/7/25 0025
 ***/
@Data
@ApiModel("操作员审批意见")
public class ActUserOpinionVo {
    @ApiModelProperty(value = "意见ID", required = true, dataType = "string")
    private String opinionId;
    @ApiModelProperty(value = "用户ID", required = true, dataType = "string")
    private String userId;
    @ApiModelProperty(value = "意见内容", required = true, dataType = "string")
    private String opinionContent;
    @ApiModelProperty(value = "排序编号", required = true, dataType = "string")
    private Integer sortNo;
    @ApiModelProperty(value = "是否激活", required = true, dataType = "int")
    private String isActive;
    @ApiModelProperty(value = "创建人", required = true, dataType = "string")
    private String creater;
    @ApiModelProperty(value = "创建时间", required = true, dataType = "date")
    private Date createTime;
    @ApiModelProperty(value = "修改人", required = true, dataType = "string")
    private String modifier;
    @ApiModelProperty(value = "修改时间", required = true, dataType = "date")
    private Date modifyTime;

    public static List<ActUserOpinionVo> toActUserOpinionVo(List<ActUserOpinion> actUserOpinions) {
        List<ActUserOpinionVo> actUserOpinionVos = new ArrayList<>();
        for (ActUserOpinion actUserOpinion : actUserOpinions) {
            ActUserOpinionVo actUserOpinionVo = new ActUserOpinionVo();
            actUserOpinionVo.setOpinionId(actUserOpinion.getOpinionId());
            actUserOpinionVo.setUserId(actUserOpinion.getUserId());
            actUserOpinionVo.setOpinionContent(actUserOpinion.getOpinionContent());
            actUserOpinionVo.setSortNo(actUserOpinion.getSortNo());
            actUserOpinionVo.setIsActive(actUserOpinion.getIsActive());
            actUserOpinionVo.setCreater(actUserOpinion.getCreater());
            actUserOpinionVo.setCreateTime(actUserOpinion.getCreateTime());
            actUserOpinionVo.setModifier(actUserOpinion.getModifier());
            actUserOpinionVo.setModifyTime(actUserOpinion.getModifyTime());
            actUserOpinionVos.add(actUserOpinionVo);
        }

        return actUserOpinionVos;
    }

}
