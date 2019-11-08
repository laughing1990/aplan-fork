package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务咨询-模型
 */
@Data
public class AeaItemServiceConsulting implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceConsultingId; // (服务咨询ID)
    private String itemBasicId; // (本级事项目录ID)
    private String gwzzhqx; // (服务咨询岗位职责和权限)
    private String zxgzcx; // (咨询工作程序)
    private String cjwt; // (常见问题)
    private String dz; // (地址)
    private String dh; // (电话号码)
    private String zxdz; // (实施机关咨询网址)
    private String wbwz; // (政务微博网址)
    private String wxh; // (微信号)
    private String email; // (电子邮箱)
    private String yzbm; // (邮政编码)
    private String yjdz; // (邮寄地址)
    private String hfsxhxs; // (回复时限及形式)
    private String hfbm; // (回复部门)
    private String dataId; // (省的数据ID)
    private String serviceType; //咨询或者监督  zx 咨询  jd 监督

    private String keyword; //查询关键词
}
