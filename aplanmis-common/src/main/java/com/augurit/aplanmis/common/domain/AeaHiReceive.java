package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 领取登记表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:20</li>
 * </ul>
 */
@Data
public class AeaHiReceive implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String receiveId; // (ID)
    private String applyinstId; // (申请实例ID)
    private String outinstId; // (输出实例ID)
    private String receiveUserName; // (领取人姓名)
    private String receiveCertNo; // (领取人证件号码)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date receiveTime; // (领取时间)
    private String approveDeptRegion; // (领取登记部门所在的行政区域代码)
    private String subDeptRegion; // (分发至下级部门的行政区域代码)
    private String receiveMemo; // (备注)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String receiveUserMobile; // (领证人手机号码)
    private String serviceAddress; // (送达地点)
    private String documentNum; // (文书号)
    private String receiptType; // (回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执）)
    private String fileUrl; // ()
    private String documentName; // (文书名称)
    private String rootOrgId;//根组织ID

    private String receiveName;//回执类型名称

}
