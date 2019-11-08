package com.augurit.aplanmis.data.exchange.domain.spgl;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author yinlf
 * @date 2019/08/31
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class SpglXmjbxxb extends SpglEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String lsh;
    /**
     * 行政区划代码
     */
    private String xzqhdm;
    /**
     * 项目代码
     */
    private String xmdm;
    /**
     * 项目名称
     */
    private String xmmc;
    /**
     * 工程代码
     */
    private String gcdm;
    /**
     * 工程范围
     */
    private String gcfw;
    /**
     * 前阶段关联工程代码
     */
    private String qjdgcdm;
    /**
     * 项目投资来源
     */
    @Size(max = 10)
    private Long xmtzly;
    /**
     * 土地获取方式
     */
    @Size(max = 10)
    private Long tdhqfs;
    /**
     * 土地是否带设计方案
     */
    @Size(max = 10)
    private Long tdsfdsjfa;
    /**
     * 是否完成区域评估
     */
    @Size(max = 10)
    private Long sfwcqypg;
    /**
     * 审批流程类型
     */
    @Size(max = 10)
    private Long splclx;
    /**
     * 立项类型
     */
    @Size(max = 10)
    private Long lxlx;
    /**
     * 工程分类
     */
    @Size(max = 10)
    private Long gcfl;
    /**
     * 建设性质
     */
    @Size(max = 10)
    private Long jsxz;
    /**
     * 项目资金属性
     */
    @Size(max = 10)
    private Long xmzjsx;
    /**
     * 国标行业代码发布年代
     */
    private String gbhydmfbnd;
    /**
     * 国标行业
     */
    private String gbhy;
    /**
     * 拟开工时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String nkgsj;
    /**
     * 拟建成时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String njcsj;
    /**
     * 项目是否完全办结
     */
    @Size(max = 10)
    private Long xmsfwqbj;
    /**
     * 项目完全办结时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date xmwqbjsj;
    /**
     * 总投资额（万元）
     */
    @Size(max = 14)
    private Double ztze;
    /**
     * 建设地点行政区划
     */
    private String jsddxzqh;
    /**
     * 建设地点
     */
    private String jsdd;
    /**
     * 项目建设地点X坐标
     */
    @Size(max = 20)
    private Double xmjsddx;
    /**
     * 项目建设地点Y坐标
     */
    @Size(max = 20)
    private Double xmjsddy;
    /**
     * 建设规模及内容
     */
    private String jsgmjnr;
    /**
     * 用地面积
     */
    @Size(max = 14)
    private Double ydmj;
    /**
     * 建筑面积
     */
    @Size(max = 14)
    private Double jzmj;
    /**
     * 申报时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sbsj;
    /**
     * 审批流程编码
     */
    private String splcbm;
    /**
     * 审批流程版本号
     */
    @Size(max = 4)
    private Double splcbbh;
    /**
     * 数据有效标识
     */
    @Size(max = 10)
    private Long sjyxbs;
    /**
     * 数据无效原因
     */
    private String sjwxyy;
    /**
     * 失败原因
     */
    private String sbyy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date gxsj;

    @Override
    @JsonIgnore
    public String getTableName() {
        return TableNameConstant.SPGL_XMJBXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.APPLY_PROJ_STEP;
    }
}
