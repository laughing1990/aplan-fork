package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 办理收费-模型
 */
@Data
public class AeaItemServiceCharge implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // (办理收费ID)
    private String itemId; // (本级事项目录ID)
    private String ywsf; // (有无收费)1:收费 0:不收费
    private String jfsj; // (缴费时间)
    private String jfhj; // (缴费环节)
    private String jfdd; // (缴费地点)
    private String jffs; // (缴费方式)数据字典 ITEM_JFFS（1 现金 2 银行转账 3 POS 4 网上支付 5 其他）
    private String qtjffs; // (其他缴费方式)
    private String dataId; // (省的数据ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime; // (修改时间)

    //非表字段
    private String itemName; // 事项名称
    private String keyword; // 查询关键词
    private String chargeGroupIds;//收费项目id数组字符串
}
