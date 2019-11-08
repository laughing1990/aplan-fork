package com.augurit.aplanmis.front.common.vo;

import com.augurit.agcloud.bpm.admin.process.domain.BpmAssigneeOrgEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("部门组织树")
public class BpmAssigneeOrgEntityVo {
    @ApiModelProperty(value = "主键ID", required = true, dataType = "string")
    private String id;
    @ApiModelProperty(value = "父ID", required = true, dataType = "string")
    private String pId;
    @ApiModelProperty(value = "部门名称", required = true, dataType = "string")
    private String name;
    @ApiModelProperty(value = "文本值", required = true, dataType = "string")
    private String textValue;
    @ApiModelProperty(value = "点击图标", required = true, dataType = "string")
    private String iconSkin;
    @ApiModelProperty(value = "数据类型", required = true, dataType = "string")
    private String dataType;
    @ApiModelProperty(value = "是否是父节点", required = true, dataType = "boolean")
    private Boolean isParent;
    @ApiModelProperty(value = "CHK禁用", required = true, dataType = "boolean")
    private Boolean chkDisabled = false;
    @ApiModelProperty(value = "是否打开", required = true, dataType = "boolean")
    private Boolean open;
    @ApiModelProperty(value = "不检查", required = true, dataType = "boolean")
    private Boolean nocheck;

    public static List<BpmAssigneeOrgEntityVo> toBpmAssigneeOrgEntityVo(List<BpmAssigneeOrgEntity> bpmAssigneeOrgEntitys) {
        List<BpmAssigneeOrgEntityVo> bpmAssigneeOrgEntityVos = new ArrayList<>();
        for (BpmAssigneeOrgEntity bpmAssigneeOrgEntity : bpmAssigneeOrgEntitys) {
            BpmAssigneeOrgEntityVo bpmAssigneeOrgEntityVo = new BpmAssigneeOrgEntityVo();
            bpmAssigneeOrgEntityVo.setId(bpmAssigneeOrgEntity.getId());
            bpmAssigneeOrgEntityVo.setPId(bpmAssigneeOrgEntity.getpId());
            bpmAssigneeOrgEntityVo.setName(bpmAssigneeOrgEntity.getName());
            bpmAssigneeOrgEntityVo.setTextValue(bpmAssigneeOrgEntity.getTextValue());
            bpmAssigneeOrgEntityVo.setIconSkin(bpmAssigneeOrgEntity.getIconSkin());
            bpmAssigneeOrgEntityVo.setDataType(bpmAssigneeOrgEntity.getDataType());
            bpmAssigneeOrgEntityVo.setIsParent(bpmAssigneeOrgEntity.getIsParent());
            bpmAssigneeOrgEntityVo.setChkDisabled(bpmAssigneeOrgEntity.getChkDisabled());
            bpmAssigneeOrgEntityVo.setOpen(bpmAssigneeOrgEntity.getOpen());
            bpmAssigneeOrgEntityVo.setNocheck(bpmAssigneeOrgEntity.getNocheck());
            bpmAssigneeOrgEntityVos.add(bpmAssigneeOrgEntityVo);
        }
        return bpmAssigneeOrgEntityVos;
    }
}
