package com.augurit.aplanmis.front.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("打印回执基础数据")
public class ReceiveBaseVo implements Serializable {
    /* AEA_HI_RECEIVE字段 start */
    @ApiModelProperty(value = "回执ID", required = true, dataType = "string")
    private java.lang.String receiveId;

    @ApiModelProperty(value = "申请实例ID", required = true, dataType = "string")
    private java.lang.String applyinstId;

    @ApiModelProperty(value = "输出实例ID,保存的是itemVerId", required = true, dataType = "string")
    private java.lang.String outinstId;

    @ApiModelProperty(value = "领取人姓名", required = true, dataType = "string")
    private java.lang.String receiveUserName;

    @ApiModelProperty(value = "领取人证件号码", required = true, dataType = "string")
    private java.lang.String receiveCertNo;

    @ApiModelProperty(value = "领取时间", required = true, dataType = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date receiveTime;

    @ApiModelProperty(value = "领取登记部门所在的行政区域代码", required = true, dataType = "string")
    private java.lang.String approveDeptRegion;

    @ApiModelProperty(value = "分发至下级部门的行政区域代码", required = true, dataType = "string")
    private java.lang.String subDeptRegion;

    @ApiModelProperty(value = "备注", required = true, dataType = "string")
    private java.lang.String receiveMemo;

    @ApiModelProperty(value = "创建人", required = true, dataType = "string")
    private java.lang.String creater;

    @ApiModelProperty(value = "创建时间", required = true, dataType = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = true, dataType = "string")
    private java.lang.String modifier;

    @ApiModelProperty(value = "修改时间", required = true, dataType = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "领证人手机号码", required = true, dataType = "string")
    private java.lang.String receiveUserMobile;

    @ApiModelProperty(value = "收件地址", required = true, dataType = "string")
    private java.lang.String serviceAddress;

    @ApiModelProperty(value = "收据类型", required = true, dataType = "string")
    private String receiptType;

    @ApiModelProperty(value = "文档编号", required = true, dataType = "string")
    private String documentNum;

    @ApiModelProperty(value = "文档名称", required = true, dataType = "string")
    private String documentName;

    @ApiModelProperty(value = "开普返回的回执文件路径", required = true, dataType = "string")
    private String fileUrl;

    @ApiModelProperty(value = "回执类型名称-查询数据字典获得", required = true, dataType = "string")
    private String receiveTypeName;

    @ApiModelProperty("所属顶级机构ID")
    private String rootOrgId;

    /*---------------------- END----------------------------------------------------*/

    /*----------------------------- COMMON FILED------START ---*/
    @ApiModelProperty(value = "窗口人员签字")
    private String winName;
    //申报实例字段
    @ApiModelProperty(value = "应用编码", required = true, dataType = "string")
    private String applyinstCode;

    @ApiModelProperty(value = "申报来源，net表示网上申报，win表示窗口申报", required = true, dataType = "string", allowableValues = "net,win")
    private String applyinstSource;

    @ApiModelProperty(value = "是否串联审批，0表示并联审批，1表示串联审批", required = true, dataType = "string", allowableValues = "0,1")
    private String isSeriesApprove;
    //项目信息
    @ApiModelProperty(value = "工程名称", required = true, dataType = "string")
    private String projName;

    @ApiModelProperty(value = "工程编码", required = true, dataType = "string")
    private String projLocalCode;

    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;

    //事项实例
    @ApiModelProperty(value = "事项实例（办件）名称")
    private String iteminstName;

    @ApiModelProperty(value = "事项名称，与事项实例名字相同", required = true, dataType = "string")
    private String itemName;

    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;

    //单位信息

    @ApiModelProperty(value = "公司名称或法人名")
    private String applicant;//公司名称或法人名

    @ApiModelProperty(value = "建设单位证件号")
    private String applicantIDCard;

    @ApiModelProperty(value = "经办企业")
    private String agentName;

    @ApiModelProperty(value = "经办企业证件号")
    private String agentIdCard;

    @ApiModelProperty(value = "经办人手机号")
    private String agentLinkmanTel;

    @ApiModelProperty(value = "经办人姓名")
    private String agentLinkmanName;

    @ApiModelProperty(value = "经办人身份证号")
    private String agentLinkmanIDCard;

    //时间相关

    @ApiModelProperty(value = "时限")
    private String timeLimit;

    @ApiModelProperty(value = "承诺办结时限数字")
    private Double dueNum;

    @ApiModelProperty(value = "承诺办结时限单位")
    private String dueUnit;

    @ApiModelProperty(value = "打印时间")
    private String printTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @ApiModelProperty(value = "所有材料")
    private List<AeaHiItemMatinst> allMatList;
    @ApiModelProperty(value = "合并后材料")
    private List<AeaHiItemMatinst> combineMatList;

    @ApiModelProperty("领取模式：1 窗口取证  0 邮政快递")
    private String receiveMode;

    public static ReceiveBaseVo copyCommonField(AeaHiReceive receive, AeaHiApplyinst applyinst, ReceiveBaseVo baseVo) {
        if (null == receive || null == applyinst || null == baseVo) return baseVo;

        BeanUtils.copyProperties(receive, baseVo);

        baseVo.setApplyinstCode(applyinst.getApplyinstCode());
        baseVo.setApplyinstId(applyinst.getApplyinstId());
        baseVo.setApplyinstSource(applyinst.getApplyinstSource());
        baseVo.setIsSeriesApprove(applyinst.getIsSeriesApprove());
        baseVo.setLinkmanInfoId(applyinst.getLinkmanInfoId());

        return baseVo;

    }
}
