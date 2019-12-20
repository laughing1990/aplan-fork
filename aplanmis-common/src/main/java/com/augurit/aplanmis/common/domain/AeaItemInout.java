package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.util.UuidUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 事项输入输出定义表-模型
 */
@Data
public class AeaItemInout implements Serializable {

    private static final long serialVersionUID = 1L;

    private String inoutId; // (ID)
    private String itemId; // (事项定义ID)
    private String itemVerId;//事项版本id
    private String stateVerId;//情形版本id
    private String isOwner; // (是否为当前事项直接所有，0表示其他事项所有，1表示当前事项所有)
    private String isInput; // (是否输入，0表示输出，1表示输入)
    private String fileType; // (文件类型，mat表示材料，cert表示电子证照)
    private String matId; // (事项定义ID【当FILE_TYPE=mat】)
    private String certId; // (电子证照定义ID【当FILE_TYPE=cert】)
    private String isStateIn;// (是否为情形输入，0表示直接的事项输入，1表示关联情形的输入【当IS_INPUT=1】)
    private String isDeleted;
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)
    private String itemStateId;// (情形ID【当IS_INPUT=1】)
    private Long sortNo;
    private String rootOrgId;

    /**
     * 扩展字段
     */
    private AeaCert aeaCert;
    private AeaItemMat aeaItemMat;
    private List<BscAttFileAndDir> bscAttFileAndDirs;
    /**
     * 材料或者电子证照名称
     */
    private String aeaMatCertName;
    /**
     * 材料或者电子证照编码
     */
    private String aeaMatCertCode;
    private String keyword;

    private String zcqy;
    private String isCommon;
    private String isOfficialDoc;
    private String attIsRequire;
    private String paperIsRequire;

    private String formId;

    /**
     * 主题版本
     */
    private String themeVerId;

    /**
     *
     * 是否可选事项 0表示并联审批事项 1表示并行推进事项 2前置检查事项
     *
     */
    private String isOptionItem;

    /**
     * 扩展字段
     */
    private List<String> itemVerIds;

    /**
     * 扩展字段: 事项名称
     */
    private String itemName;

    /**
     * 扩展字段: 事项编号
     */
    private String itemCode;

    /**
     * 扩展字段: 部门名称
     */
    private String orgName;

    /**
     * 扩展字段: 是否标准事项
     */
    private String isCatalog;

    /**
     * 扩展字段: 材料库融合三种：材料、证照、表单
     */
    private String matProp;

    /**
     * 扩展字段: 材料库融合三种：材料、证照、表单
     */
    private List<String> matProps;

    public AeaItemInout copyOne() {

        AeaItemInout copiedOne= new AeaItemInout();
        copiedOne.setInoutId(UuidUtil.generateUuid());
        copiedOne.setItemId(this.itemId);
        copiedOne.setItemVerId(this.itemVerId);
        copiedOne.setIsOwner(this.isOwner);
        copiedOne.setIsInput(this.isInput);
        copiedOne.setFileType(this.fileType);
        copiedOne.setMatId(this.matId);
        copiedOne.setCertId(this.certId);
        copiedOne.setCreater(this.creater);
        copiedOne.setCreateTime(new Date());
        copiedOne.setModifier(this.modifier);
        copiedOne.setModifyTime(new Date());
        copiedOne.setIsStateIn(this.isStateIn);
        copiedOne.setItemStateId(itemStateId);
        copiedOne.setStateVerId(stateVerId);
        copiedOne.setIsDeleted(this.isDeleted);
        copiedOne.setIsCommon(this.isCommon);
        copiedOne.setSortNo(this.sortNo);
        copiedOne.setRootOrgId(this.rootOrgId);
        return copiedOne;
    }
}