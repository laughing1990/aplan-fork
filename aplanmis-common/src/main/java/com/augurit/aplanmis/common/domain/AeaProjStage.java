package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author yinlf
 * @date 2019/09/03
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class AeaProjStage implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String projStageId;
    /**
     * 项目ID
     */
    private String projInfoId;
    /**
     * 标准阶段序号
     */
    private String standardSortNo;
    /**
     * 阶段状态，0表示未申报，1表示进行中，2表示已通过
     */
    private String stageState;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date firstApplyTime;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date passTime;
    /**
     *
     */
    private String passNatureDays;
    /**
     *
     */
    private String passWorkingDays;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    private List<ProjStageApplyForm> projStageApplyFormList = new ArrayList<>();

    private ProjStageApplyForm lastApplyStageinst;

    public void addForm(ProjStageApplyForm form){
        this.projStageApplyFormList.add(form);
    }
}
