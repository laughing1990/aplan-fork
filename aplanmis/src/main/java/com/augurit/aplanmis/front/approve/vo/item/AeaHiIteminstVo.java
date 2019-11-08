package com.augurit.aplanmis.front.approve.vo.item;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/***
 * @description 并联申报事项列表
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("并联申报事项列表")
public class AeaHiIteminstVo {

    @ApiModelProperty(value = "ID", required = true, dataType="string")
    private String iteminstId;

    @ApiModelProperty(value = "事项实例（办件）ID", required = true, dataType="string")
    private String itemId;

    @ApiModelProperty(value = "事项实例（办件）编号", required = true, dataType="string")
    private String iteminstCode;

    @ApiModelProperty(value = "事项实例（办件）名称", required = true, dataType="string")
    private String iteminstName;

    @ApiModelProperty(value = "审批部门ID", required = true, dataType="string")
    private String approveOrgId;

    @ApiModelProperty(value = "是否为容缺事项实例（办件），0表示否，1表示是",
            required = true, dataType="string",allowableValues = "0,1")
    private String isLackIteminst;
    @ApiModelProperty(value = "是否并行审批事项，0表示不是，1表示是",
            required = true, dataType="string",allowableValues = "0,1")
    private String isSeriesApprove;

    @ApiModelProperty(value = "所属主题实例ID【当IS_PAR_ITEM=1】", required = true, dataType="string")
    private String themeVerId;

    @ApiModelProperty(value = "所属阶段实例ID【当IS_PAR_ITEM=1】", required = true, dataType="string")
    private String stageinstId;

    @ApiModelProperty(value = "所属串联实例ID【当IS_PAR_ITEM=0】", required = true, dataType="string")
    private String seriesinstId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间)", required = true, dataType="date")
    private java.util.Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "登记时间)", required = true, dataType="date")
    private java.util.Date registerTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "受理时间)", required = true, dataType="date")
    private java.util.Date signTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间)", required = true, dataType="date")
    private java.util.Date endTime;

    @ApiModelProperty(value = "公开方式)", required = true, dataType="string")
    private String publicMode;

    @ApiModelProperty(value = "实例状态，1表示已接件，2表示预收件，3不收件，4受理，" +
            "5不予受理，6材料补录，7待补正受理，8审核通过，9审核不通过，" +
            "10待发证，11待退件，12办结)", required = true, dataType="string",allowableValues = "0,1,2,3,4,5,6,7,8,9,10,11,12")
    private String iteminstState;

    @ApiModelProperty(value = "（事项审批部门）内部业务流程模板实例ID", required = true, dataType="string")
    private String innerAppinstId;

    @ApiModelProperty(value = "创建人", required = true, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = true, dataType="string")
    private String modifier;

    @ApiModelProperty(value = "修改时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "办理层级，0表示区级，1表示市级", required = true, dataType="string",allowableValues = "0,1")
    private String iteminstLevel;

    @ApiModelProperty(value = "业务状态,1、申办，2、预审（网上预受理），3、受理，4、审批，" +
            "5、补正告知，6、补正受理，7、办结，8、制证，9、领取登记", required = true, dataType="string",allowableValues = "0,1,2,3,4,5,6,7,8,9")
    private String businessState;

    @ApiModelProperty(value = "事项版本ID", required = true, dataType="string")
    private String itemVerId;

    @ApiModelProperty(value = "是否参与业务协同审批：1 是，0 否", required = true, dataType="string",allowableValues = "0,1")
    private String isJoinApproval;

    //@ApiModelProperty(value = "办件材料是否齐全(0表示必要材料不全（即不予受理），" +
      //      "1表示必要材料齐全但有容缺，2表示所有材料齐全)", required = true, dataType="string",allowableValues = "0,1,2")
    //private String isMatFull;

    @ApiModelProperty(value = "审批部门", required = true, dataType="string")
    private String approveOrgName;

    @ApiModelProperty(value = "承诺时限", required = true, dataType="long")
    private Double dueNum;

    @ApiModelProperty(value = "申报流水号", required = true, dataType="string")
    private String applyinstCode;

    public static List<AeaHiIteminstVo> toAeaHiIteminstVo(List<AeaHiIteminst> aeaHiIteminsts){
        List<AeaHiIteminstVo> AeaHiIteminstVos = new ArrayList<AeaHiIteminstVo>();
        for(AeaHiIteminst aeaHiIteminst:aeaHiIteminsts){
            AeaHiIteminstVo  aeaHiIteminstVo = new AeaHiIteminstVo();
            BeanUtils.copyProperties(aeaHiIteminst,aeaHiIteminstVo);
            AeaHiIteminstVos.add(aeaHiIteminstVo);
        }
        return AeaHiIteminstVos;
    }

}
