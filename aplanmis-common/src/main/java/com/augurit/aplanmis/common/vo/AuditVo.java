package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AuditVo {

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String modifier;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 根组织id
     */
    private String rootOrgId;

    public static AuditVo newOne(String userId, String rootOrgId) {

        AuditVo auditVo= new AuditVo();
        auditVo.setCreater(userId);
        auditVo.setCreateTime(new Date());
        auditVo.setModifier(userId);
        auditVo.setModifyTime(new Date());
        auditVo.setRootOrgId(rootOrgId);
        return auditVo;
    }

}
