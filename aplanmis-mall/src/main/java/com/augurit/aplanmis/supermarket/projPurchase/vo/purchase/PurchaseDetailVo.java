package com.augurit.aplanmis.supermarket.projPurchase.vo.purchase;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel(value = "采购项目详情")
public class PurchaseDetailVo {
    private String iteminstName;

    @ApiModelProperty(name = "iteminst", value = "事项实例信息")
    private Iteminst iteminst;

    @ApiModelProperty(name = "purchaseProj", value = "采购项目信息")
    private PurchaseProjVo purchaseProj;

    @ApiModelProperty(name = "aeaProjInfo", value = "关联的投资审批项目信息")
    private AeaProjInfo aeaProjInfo;

    public void changeToIteminst(AeaHiIteminst iteminst) {
        Iteminst inst = new Iteminst();
        BeanUtils.copyProperties(iteminst, inst);
        this.iteminst = inst;
        this.iteminstName = inst.getIteminstName();
    }

    @Data
    @ApiModel(value = "中介事项信息", description = "中介事项信息")
    public class Iteminst {
        @ApiModelProperty(name = "iteminstId", value = "事项实例（办件）ID")
        private String iteminstId;

        @ApiModelProperty(name = "iteminstCode", value = "事项实例（办件）编号")
        private String iteminstCode;

        @ApiModelProperty(name = "iteminstName", value = "事项实例（办件）名称")
        private String iteminstName;

        @ApiModelProperty(name = "approveOrgId", value = "审批部门ID")
        private String approveOrgId;

        @ApiModelProperty(name = "isLackIteminst", value = "是否为容缺事项实例（办件），0表示否，1表示是")
        private String isLackIteminst;

        @ApiModelProperty(name = "isSeriesApprove", value = "是否并行审批事项，0表示不是，1表示是")
        private String isSeriesApprove = "1";

        @ApiModelProperty(name = "approveOrgName", value = "审批部门")
        private String approveOrgName;

        @ApiModelProperty(name = "orgShortName", value = "部门简称")
        private String orgShortName;

        @ApiModelProperty(name = "dueNum", value = "办理时限")
        private Double dueNum;

        @ApiModelProperty(name = "dueNumUnit", value = "办理时限单位")
        private String dueNumUnit;

        @ApiModelProperty(name = "itemProperty", value = "办件类型")
        private String itemProperty;

        @ApiModelProperty(name = "serviceObj", value = "服务对象")
        private String serviceObj;

        @ApiModelProperty(name = "relevancyItemName", value = "关联的服务事项")
        private String relevancyItemName;
    }
}
