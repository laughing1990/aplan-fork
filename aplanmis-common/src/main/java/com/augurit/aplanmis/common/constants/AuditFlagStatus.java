package com.augurit.aplanmis.common.constants;

/**
 * 采购项目需求表：AEA_IM_PROJ_PURCHASE
 * 采购需求状态。
 */
public class AuditFlagStatus {

    /*未提交*/
    public static final String NO_COMMIT = "0";

    /*服务中*/
    public static final String SERVICE_PROGRESS = "1";

    /*服务完成*/
    public static final String SERVICE_FINISH = "2";

    /*服务中止*/
    public static final String SERVICE_END = "3";

    /*审核中*/
    public static final String AUDIT_PROGRESS = "4";

    /*退回*/
    public static final String AUDIT_RETURN = "5";

    /*报名中*/
    public static final String REGISTRATION_PROGRESS = "6";

    /*选取中*/
    public static final String CHOOSE_PROGRESS = "7";

    /*选取开始*/
    public static final String CHOOSE_START = "8";

    /*已选取，待确认*/
    public static final String CHOOSE_END = "9";

    /*无效*/
    public static final String INVALID = "10";

    /*待选取*/
    public static final String WAIT_CHOOSE = "11";

    /*已过时*/
    public static final String TIME_OUT = "12";

    /*部门审批中*/
    public static final String DEPARTMENT_APPROVAL = "13";

    /*办结通过*/
    public static final String AGREE = "14";

    /*办结不通过*/
    public static final String DISAGREE = "15";

    /*待上传合同*/
    public static final String UPLOAD_CONTRACT = "16";

    /*待确认合同*/
    public static final String CONFIRM_CONTRACT = "17";


}
