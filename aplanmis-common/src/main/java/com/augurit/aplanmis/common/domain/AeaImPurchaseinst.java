package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-06-26 09:20:12</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Augurit</li>
 * <li>创建时间：2019-06-26 09:20:12</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
@ApiModel("采购需求项目历史状态")
public class AeaImPurchaseinst implements Serializable {
    private static final long serialVersionUID = 1L;
    private String purchaseinstId; // (采购需求历史ID)
    private String projPurchaseId; // (采购需求ID)
    private String oldPurchaseFlag; // (采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时)
    private String newPurchaseFlag; // (采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时)
    private String operateAction; // (【PURCHASE_FLAG=4时】操作：0：新增采购需求 ，1：修改采购需求，2：采购务求置为无效，3：新增合同，4：修改合同，5：新增评价，6：修改评价，7：修改中选机构)
    private String parentPurchaseinstId; // (父ID)
    private String linkmanInfoId; // (操作人信息ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String operateDescribe; // (操作详情描述（备注）)
    private String isOwnFile; // (是否拥有附件)
    private String applyData; // (请求数据json格式)
    private String processinstId;//流程实例ID
    private String taskId;//任务ID
    private String rootOrgId;//根组织ID


    //扩展字段
    private List<BscAttForm> bscAttForms;// 文件列表

    public void buildImPurchaseinst(String projPurchaseId, String purchaseFlag, String parentPurchaseinstId, String linkmanInfoId, String isOwnFile, String creater, String rootOrgId) {
        this.purchaseinstId = UUID.randomUUID().toString();
        this.projPurchaseId = projPurchaseId;
        this.oldPurchaseFlag = purchaseFlag;
        this.newPurchaseFlag = purchaseFlag;
        this.parentPurchaseinstId = parentPurchaseinstId;
        this.linkmanInfoId = linkmanInfoId;
        this.isOwnFile = isOwnFile;
        this.creater = creater;
        this.createTime = new Date();
        this.rootOrgId = rootOrgId;
    }

    public enum OperateAction {

        新增采购需求("0"),
        修改采购需求("1"),
        采购需求置为无效("2"),
        新增合同("3"),
        修改合同("4"),
        延长结束服务时间("5"),
        新增评价("6"),
        修改评价("7"),
        修改中选机构("8");

        private String action;

        OperateAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }
    }
}
