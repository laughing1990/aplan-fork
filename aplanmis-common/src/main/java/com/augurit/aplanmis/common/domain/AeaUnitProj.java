package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 单位与项目关联表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:49</li>
 * </ul>
 */
@Data
public class AeaUnitProj implements Serializable {
    private static final long serialVersionUID = 1L;
    private java.lang.String unitProjId; // (主键)
    private java.lang.String unitInfoId; // (单位ID)
    private java.lang.String isOwner; // (是否业主单位，0表示代建单位，1表示业主单位)
    private java.lang.String projInfoId; // (项目ID)
    private java.lang.String creater; // (创建人)
    private String unitType;//(单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String linkmanInfoId;//(单位项目负责人)
    private String isDeleted;
    private String qualLevelId;//所属资质等级ID（对应AEA_IM_QUAL_LEVEL表）
    private String certinstId;//证书实例表ID
    private String safeLicenceNum;//安全生产许可证编号

    public static AeaUnitProj initIsDeleted(AeaUnitProj aeaUnitProj) {
        aeaUnitProj.setIsDeleted("0");
        return aeaUnitProj;
    }
}
