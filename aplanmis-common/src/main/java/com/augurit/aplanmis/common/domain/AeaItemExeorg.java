package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 15:13
 * @desc 实施机关-模型
 **/
@Data
public class AeaItemExeorg implements Serializable {
    private static final long serialVersionUID = 1L;

    private String exeorgId; // (主键)
    private String itemBasicId; // (本级事项目录ID)
    private String ssjgmc; // (实施机关名称)
    private String ssjglb; // (实施机关类别)
    private String sscj; // (实施层级)
    private String qzhfgj; // (权责划分（国家）)
    private String qzhfsj; // (权责划分（省级）)
    private String qzhfdsj; // (权责划分（市级）)
    private String qzhfqx; // (权责划分（区、县）)
    private String qzhfzj; // (权责划分（镇、街）)
    private String sfdbmdksbl; // (是否单部门单科室办理)
    private String sfdbmkksbl; // (是否单部门跨科室办理)
    private String sfdbmkcjbl; // (是否单部门跨层级办理)
    private String sfdcjkbmbl; // (是否单层级跨部门办理)
    private String sfkbmkcjbl; // (是否跨部门跨层级办理)
    private String sfylbjg; // (是否有联办机构)
    private String xsnr; // (行使内容)
    private String dataId; // (省的数据ID)
    private String deptName; // (办理部门机构名称(对应是否单部门单科室办理...）)
    private String ssjgdm; // (实施机关组织机构代码)
    private String xzqhdm; // (行政区划代码)
    private String xzqhmc; // (实施机关所在行政区划名称)
    private String dksmc; // 单科室名称
    private String kksmc; // 跨科室名称
    private String skcj; //所跨层级
    private String skbmmc; //所跨部门名称
    private String skcjhbm; //所跨层级和部门
    private String lbjg; //联办机构
}

