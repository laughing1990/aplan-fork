package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 15:14
 * @desc
 **/
@Data
public class AeaItemServiceServe implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceServeId;      //办理服务ID
    private String itemBasicId;              //事项ID
    private String ckfw;                //服务窗口
    private String wsfwsd;              //网上服务深度
    private String dccs;                //到现场次数
    private String xtjsf;               //系统运行层级
    private String ywxtmc;              //业务系统名称
    private String sfxysfyz;            //是否需要身份验证
    private String sfyzsfxyca;          //身份验证是否需要CA
    private String casfsw;              //CA是否涉外
    private String zxsbfw;              //选择是否在线申办服务(1:是;0:否)
    private String zxsbfwdz;            //在线申办服务地址
    private String wszxfw;              //选择是否网上咨询服务(1:是;0:否)
    private String wszxfwdz;            //网上咨询服务地址
    private String jgcxfw;              //选择是否在线结果查询服务(1:是;0:否)
    private String jgcxfwdz;            //结果查询服务地址
    private String jdcxfw;              //选择是否在线进度查询服务(1:是;0:否)
    private String jdcxfwdz;            //进度查询服务地址
    private String yyfw;                //如果本事项可提供预约服务，请填写具体的预约方式
    private String ckbljdcx;            //填写提供给申请人查看事项办理进度的方式
    private String applicantAid;        //实施机关应指导申请人填写申请材料，对格式文本填写错误的，允许申请人更正
    private String tbfw;                //通办范围(0:全国,1:跨省,2:跨市,3:跨县,4:无)
    private String payonline;           //是否支持网上支付(0:否;1:是)
    private String express;             //是否支持物流快速(0:否;1:是)
    private String bjlx;                //办件类型
    private String dataId;              //省的数据ID
    private String ckfwIds;             //办事窗口IDS
    private String netHallCode;         //网厅编号

    private List<AeaServiceWindow> listServiceWindow;

    private String keyword;             //查询关键字
}

