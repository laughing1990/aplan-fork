package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 阶段办事指南常见问题回答
 *
 * @author jjt
 * @date 2016/10/31
 */
@Data
public class AeaParStageQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主建
     */
    private String questionId;

    /**
     * 阶段ID
     */
    private String stageId;

    /**
     * 常见问题
     */
    private String question;

    /**
     * 解答
     */
    private String answer;

    /**
     * 排序
     */
    private Long sortNo;

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
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 关键字查询
     */
    private String keyword;
}
