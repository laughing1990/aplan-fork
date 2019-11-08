package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 主题共享材料表-模型
 */
@Data
public class AeaParShareMat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shareMatId; // (ID)
    private String rootOrgId; // ()
    private String themeVerId; // (主题版本定义ID)
    private String outItemVerId; // (输出事项版本ID)
    private String outInoutId; // (事项输出定义ID)
    private String inItemVerId; // (输入事项版本ID)
    private String inInoutId; // (主题版本阶段的事项输入定义ID)
    private String inItemStateVerId; // (输入事项情形版本ID)
    private String isActive; // (是否启用，0表示禁用，1表示启用)

    private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)

    //扩展字段
    private String keyword;
    private String outItemId;//事项ID （输出对应的事项ID ）
    private String inItemId;//事项ID （输入对应的事项ID ）
    private String outItemName;//事项名称 （输出对应的事项名称 ）
    private String inItemName;//事项名称（输入对应的事项名称 ）

    private String outFileType;//文件类型，mat表示材料，cert表示电子证照（输出对应的文件类型 ）
    private String inFileType;//文件类型，mat表示材料，cert表示电子证照（输入对应的文件类型 ）

    private String outMatId;//材料ID（输出对应的材料ID ）
    private String inMatId;//材料ID（输入对应的材料ID ）

    private String outCertId;//证件ID（输出对应的证件ID ）
    private String inCertId;//证件ID（输入对应的证件ID ）

    private String outMatCertName;//材料或者证件名称（输出对应的材料或者证件名称 ）
    private String inMatCertName;//材料或者名称（输入对应的材料或者证件名称 ）

    private String[] outInoutIds;//输出材料inoutId集合
    private String[] inInoutIds;//输入材料inoutId集合
}
