package com.augurit.aplanmis.common.domain;

/**
 * @author tiantian
 * @date 2019/6/20
 */

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * -模型
 <ul>
 <li>项目名：奥格erp3.0--第一期建设项目</li>
 <li>版本信息：v1.0</li>
 <li>日期：2019-06-20 09:10:47</li>
 <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 <li>创建人:tiantian</li>
 <li>创建时间：2019-06-20 09:10:47</li>
 <li>修改人1：</li>
 <li>修改时间1：</li>
 <li>修改内容1：</li>
 </ul>
 */
@Data
@ApiModel("服务结果")
public class AeaImServiceResult implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务结果ID")
    private String serviceResultId; // (服务结果ID)

    @ApiModelProperty(value = "上传时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date uploadTime; // (上传时间)

    @ApiModelProperty(value = "结束时间")
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endTime; // (结束时间)

    @ApiModelProperty(value = "备注")
    private String memo; // (备注)

    @ApiModelProperty(value = "采购需求ID")
    @NotBlank
    private String projPurchaseId; // (采购需求ID)

    private String auditFlag; // (服务结果状态：0 待确定，1 已确定 ，2 已退回)

    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime; // ()
    private String modifier; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date modifyTime; // ()

    @ApiModelProperty(value = "中介机构委托人")
    private String linkmanInfoId; // (中介机构委托人)

    @ApiModelProperty(value = "企业报价信息ID")
    @NotBlank
    private String unitBiddingId; // (企业报价信息ID)

    @ApiModelProperty(value = "是否外部上传")
    private String isExternalUpload; // (是否外部上传)
    private String rootOrgId;//根组织ID



    //扩展字典
    @ApiModelProperty(value = "文件列表")
    private List<BscAttForm> bscAttForms;// 文件列表
}