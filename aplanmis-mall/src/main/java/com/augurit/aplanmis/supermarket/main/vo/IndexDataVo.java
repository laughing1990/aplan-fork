package com.augurit.aplanmis.supermarket.main.vo;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class IndexDataVo {
    @ApiModelProperty(value = "中介机构数量")
    private int agentUnitCount;
    @ApiModelProperty(value = "中介服务事项数量")
    private int itemServiceCount;
    @ApiModelProperty(value = "已完成的采购需求数量")
    private int finishedPurchaseCount;
    @ApiModelProperty(value = "已发布的采购需求")
    private int publishedPurchaseCount;

    @ApiModelProperty(value = "中选公告")
    private List<BasePurchase> purchaseSelections;

    @ApiModelProperty(value = "采购公告")
    List<BasePurchase> purchases;

    @ApiModelProperty(value = "合同公告")
    private List<PurchaseContract> purchaseContracts;

    @ApiModelProperty(value = "服务列表")
    private List<ImService> imServices;

    public IndexDataVo() {
        this.agentUnitCount = 0;
        this.itemServiceCount = 0;
        this.finishedPurchaseCount = 0;
        this.publishedPurchaseCount = 0;
        this.imServices = new ArrayList<>();
        this.purchaseSelections = new ArrayList<>();
        this.purchaseContracts = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    //合同公告转换
    public void changeToPurchaseContract(List<AeaImContract> list) {
        if (null == list) return;
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }
        for (AeaImContract temp : list) {
            PurchaseContract contract = new PurchaseContract();
            BeanUtils.copyProperties(temp, contract);
            this.purchaseContracts.add(contract);
        }
    }

    //中选公告
    public void changeToPurchaseSelect(List<AeaImProjPurchase> list) {
        this.purchaseSelections = changeToBasePurchase(list);
    }


    //采购需求
    public void changeToProjPurchase(List<AeaImProjPurchase> list) {
        this.purchases = changeToBasePurchase(list);
    }

    private List<BasePurchase> changeToBasePurchase(List<AeaImProjPurchase> list){
        List<BasePurchase> purchases=new ArrayList<>();
        if (null == list) {
            return purchases;
        }
        if (list.size() > 4) {
            list = list.subList(0, 4);
        }
        for (AeaImProjPurchase temp : list) {
            BasePurchase basePurchase = new BasePurchase();
            BeanUtils.copyProperties(temp, basePurchase);
            purchases.add(basePurchase);
        }
        return purchases;
    }

    public void changeToImService(List<AeaImService> aeaImServices) {
        for (AeaImService imService : aeaImServices) {
            ImService service = new ImService();
            BeanUtils.copyProperties(imService, service);
            this.imServices.add(service);
        }
    }

    @ApiModel(value = "合同公告")
    @Data
    private class PurchaseContract {
        @ApiModelProperty(value = "企业报价信息ID")
        private String unitBiddingId;

        @ApiModelProperty(value = "服务合同ID")
        private String contractId;

        @ApiModelProperty(value = "合同名称")
        @NotBlank
        private String contractName;

        @ApiModelProperty(value = "合同编码")
        private String contractCode;

        @ApiModelProperty(value = "合同金额")
        private String price;

        @ApiModelProperty(value = "采购需求ID")
        private String projPurchaseId;

        @ApiModelProperty(value = "签订时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date signTime; // (签订时间)
    }

    @Data
    @ApiModel(value = "中介服务")
    private class ImService {
        @ApiModelProperty(value = "服务ID")
        private String serviceId;

        @ApiModelProperty(value = "服务名称")
        private String serviceName;

        @ApiModelProperty(value = "服务图片链接")
        private String imgUrl;
    }


    @Data
    @ApiModel
    public class BasePurchase {
        @ApiModelProperty(value = "主键ID")
        private String projPurchaseId;

        @ApiModelProperty(value = "项目名称")
        private String projName;
        @ApiModelProperty(value = "采购图片链接")
        private String purchaseImgUrl;
        @ApiModelProperty(value = "业主名称")
        private String applicant;

        @ApiModelProperty(value = "最低价格（万元）")
        @FiledNameIs(filedValue = "最低价格（万元）")
        private String basePrice;

        @ApiModelProperty(value = "服务名称")
        private String serviceName;

        @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
        @FiledNameIs(filedValue = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
        private String biddingType;
        @ApiModelProperty(value = "是否确认金额，1 是 0 否")
        @FiledNameIs(filedValue = "是否确认金额，1 是 0 否")
        private String isDefineAmount;

        @ApiModelProperty(value = "最高价格（万元）")
        @FiledNameIs(filedValue = "最高价格（万元）")
        private String highestPrice;

        @ApiModelProperty(value = "报价方式,0 金额 1 下浮率")
        private String quoteType;

        @ApiModelProperty(value = "最小下浮率%")
        private String baseRate;

        @ApiModelProperty(value = "最大下浮率%")
        private String highestRate;

        @ApiModelProperty(value = "选取结束时间")
        private String selectedEndTime;

        @ApiModelProperty(value = "截止日期")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private java.util.Date expirationDate;

    }

}
