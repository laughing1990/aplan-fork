package com.augurit.aplanmis.common.constants;

/**
 *  审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时
 */
public interface AuditFlagEnum {
    String PENDING_APPROVAL ="0";
    String IN_lAPPROVAL ="1";
    String APPROVAL_SUCCESS ="2";
    String APPROVAL_FAILED ="3";
    String PUBLISHED ="4";
    String COMPLETED ="5";
    String OUT_OF_DATE ="6";
}
