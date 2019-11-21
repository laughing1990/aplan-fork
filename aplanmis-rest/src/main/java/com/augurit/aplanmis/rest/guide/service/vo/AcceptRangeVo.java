package com.augurit.aplanmis.rest.guide.service.vo;

import com.augurit.aplanmis.common.domain.AeaItemAcceptRange;
import lombok.Data;

@Data
public class AcceptRangeVo {

    //id
    private String acceptRangeId;
    // 申请人分类
    private String applicantType;
    // 部门分类信息
    private String departmentType;
    // 申请人
    private String applicant;
    // 申请内容
    private String applyContent;
    // 申请条件
    private String applyCondition;


    public static AcceptRangeVo from(AeaItemAcceptRange acceptRange) {
        AcceptRangeVo vo = new AcceptRangeVo();
        vo.setAcceptRangeId(acceptRange.getId());
        vo.setApplicantType(acceptRange.getSsml());
        vo.setDepartmentType(acceptRange.getBmfl());
        vo.setApplicant(acceptRange.getSqr());
        vo.setApplyCondition(acceptRange.getSqnr());
        vo.setApplyCondition(acceptRange.getSqtj());
        return vo;
    }
}
