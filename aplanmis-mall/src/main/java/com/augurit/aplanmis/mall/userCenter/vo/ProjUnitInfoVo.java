package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("项目单位信息补全vo")
public class ProjUnitInfoVo extends AeaProjInfo {

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "单位项目关联list")
    private List<AeaUnitProj> aeaUnitProjs;

    @ApiModelProperty(value = "新增的单位list")
    private List<AeaUnitInfoVo> unitInfos;

    @ApiModelProperty(value = "当前企业用户的人员设置")
    private List<LinkmanTypeVo> linkmanTypeVos;

    @ApiModelProperty(value = "申报时传的联系人ID参数")
    private String linkmanInfoId;
    @ApiModelProperty(value = "申报时传的申报方式参数")
    private String applySubject;
    @ApiModelProperty(value = "是否单项申报 1是 0否")
    private String isSeriesApprove;
    @ApiModelProperty(value = "事项版本ID（isSeriesApprove==1时）")
    private String itemVerId;


    public static List<AeaUnitInfo> returnForm(List<AeaUnitInfoVo> unitInfos) {
        List<AeaUnitInfo> list = new ArrayList<>();
        if (unitInfos.size() > 0) {
            for (AeaUnitInfoVo vo : unitInfos) {
                list.add(AeaUnitInfoVo.returnForm(vo));
            }
        }
        return list;
    }

}
