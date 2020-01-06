package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务类型
 */
@Data
@ApiModel("中介服务")
public class AeaImService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务ID")
    private String serviceId;
    private String serviceCode; // (服务类型编码)
    @ApiModelProperty(value = "服务名称")
    private String serviceName; // (服务类型名称)
    private String isDelete; // (是否删除：1 已经删，0 未删除)
    private String isActive; // (是否启用：1 已启用，0 未启用)
    private String creater;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private String memo; // (备注)
    private String parentId;
    private String serviceSeq;
    private String imgUrl;
    private String purchaseImgUrl;
    private String rootOrgId;//根组织ID
    private String isExternal;//是否外部数据

    //非表字段
    private String keyword;
    private String itemVerId;
    private Boolean isCheck = false;
    private String serviceItemId;

    public AeaImService(String isActive, String rootOrgId) {
        this.isActive = isActive;
        this.rootOrgId = rootOrgId;
    }

    public AeaImService() {
        this.isDelete = "0";
        this.isActive = "1";
    }
}
