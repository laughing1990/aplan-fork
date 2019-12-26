package com.augurit.aplanmis.front.solicit.vo;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author:chendx
 * @date: 2019-12-24
 * @time: 15:53
 */
@Data
@ApiModel("意见征求用户模型")
public class SolicitDetailUserVo {

    @ApiModelProperty(value = "主键", required = false, dataType="string")
    private String detailUserId;

    @ApiModelProperty(value = "征求意见详情ID", required = false, dataType="string")
    private String solicitDetailId;

    @ApiModelProperty(value = "用户ID", required = false, dataType="string")
    private String userId;

    @ApiModelProperty(value = "办理结论，0表示不通过或不同意，1表示通过或同意，2表示不涉及", required = true, dataType="string")
    private String userConclusion;

    @ApiModelProperty(value = "填写意见", required = true, dataType="string")
    private String userOpinion;

    @ApiModelProperty(value = "签收时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime;

    @ApiModelProperty(value = "填写意见时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillTime;

    @ApiModelProperty(value = "任务动作，0表示正常办理，1表示转交给同一委办局的其他人员，2表示添加同一委办局的其他人员进来", required = false, dataType="string")
    private String taskAction;

    @ApiModelProperty(value = "父ID【当TASK_ACTION=1或2时必填】", required = false, dataType="string")
    private String parentId;

    @ApiModelProperty(value = "是否删除：0表示未删除；1表示已删除", required = false, dataType="string")
    private String isDeleted;

    @ApiModelProperty(value = "创建人", required = false, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "更新人", required = false, dataType="string")
    private String modifier;

    @ApiModelProperty(value = "更新时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    public AeaHiSolicitDetailUser convertToAeaHiDetailUser(){
        AeaHiSolicitDetailUser user = new AeaHiSolicitDetailUser();
        BeanUtils.copyProperties(this,user);
        return user;
    }

}
