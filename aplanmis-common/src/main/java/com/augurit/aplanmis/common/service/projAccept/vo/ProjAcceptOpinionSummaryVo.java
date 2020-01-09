package com.augurit.aplanmis.common.service.projAccept.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 工程建设项目竣工联合验收终审意见书 信息封装对象
 */
@Data
public class ProjAcceptOpinionSummaryVo implements Serializable {
    private String documentNum;//文书编号
    private String centralCode;//项目统一代码
    private Date checkTime;//联合验收日期
    private String projName;//项目名称
    private String projAddr;//工程地址
    private String  buildUnitName;//建设单位
    private String  shigongUnitName;//施工单位
    private String  jianliUnitName;//监理单位
    private String  kanchaUnitName;//勘查单位
    private String  designUnitName;//设计单位
    private String buildArea;//建筑面积
    private String aboveFloor;//地上层数
    private String linkman;//联系人
    private String linkmanPhone;//联系人电话
    private Map<String,String> deptOpinions;//部门意见集合

}
