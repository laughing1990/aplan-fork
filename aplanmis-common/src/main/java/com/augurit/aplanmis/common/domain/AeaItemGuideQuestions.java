package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 问题清单-模型
 *
 * @author jjt
 * @date 2019/05/06
 */
@Data
public class AeaItemGuideQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主建
     *
     */
    private String id;

    /**
     * 版本id
     */
    private String itemVerId;

    /**
     * 常见问题
     */
    private String question;

    /**
     * 常见问题排序
     */
    private Long ordernum;

    /**
     * 解答
     */
    private String answer;

    /**
     * 根组织id
     */
    private String rootOrgId;

    /**
     * 扩展字段：关键字查询
     */
    private String keyword;

}