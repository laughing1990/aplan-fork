package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 主题样本表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-09-11 14:19:07</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-09-11 14:19:07</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaParThemeSample implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *  (主键)
     */
    private String themeSampleId;

    /**
     * (样本名称)
     */
    private String sampleName;

    /**
     *  (样本描述)
     */
    private String sampleDesc;

    /**
     *(样本类型，来自于数据字典)
     */
    private String sampleType;

    /**
     *(样本内容)
     */
    private String sampleContent;

    /**
     *(创建人)
     */
    private String creater;

    /**
     *(创建时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * (修改人)
     */
    private String modifier;

    /**
     *(修改时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    private String keyword;

}
