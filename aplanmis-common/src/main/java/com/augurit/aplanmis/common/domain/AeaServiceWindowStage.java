package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@ApiModel
@Data
public class AeaServiceWindowStage extends AeaServiceWindow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String windStageId;

    /**
     * 窗口ID
     */
    private String windId;

    /**
     * 阶段ID
     */
    private String stageId;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 办理范围，0表示全市通办，1表示属地办理
     */
    private String regionRange;
}
