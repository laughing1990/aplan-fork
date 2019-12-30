package com.augurit.aplanmis.common.domain;


import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.common.vo.AeaItemMatAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 事项材料定义表-模型
 */
@Data
@ApiModel(description = " 事项材料定义表")
@EqualsAndHashCode(callSuper = true)
public class AeaItemMat extends MindBaseNode {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String matId;

    @ApiModelProperty(value = "材料编号")
    private String matCode;

    @ApiModelProperty(value = "材料名称")
    private String matName;

    @ApiModelProperty(value = "是否全局共性材料，供所有其他事项使用")
    private String isGlobalShare;

    @ApiModelProperty(value = "所属材料类别ID")
    private String matTypeId;

    @ApiModelProperty(value = "接收方式")
    private String receiveMode;

    @ApiModelProperty(value = "原件数")
    private Long duePaperCount;

    @ApiModelProperty(value = "复印件数")
    private Long dueCopyCount;

    @ApiModelProperty(value = "材料来源")
    private String matFrom;

    @ApiModelProperty(value = "样本文档")
    private String sampleDoc;

    @ApiModelProperty(value = "模板文档")
    private String templateDoc;

    @ApiModelProperty(value = "材料要求")
    private String matRequire;

    @ApiModelProperty(value = "材料依据")
    private String matBasis;

    @ApiModelProperty(value = "纸质材料是否必需，0表示容缺，1表示必须")
    private String paperIsRequire;

    @ApiModelProperty(value = "排序")
    private Long sortNo;

    @ApiModelProperty(value = "备注说明")
    private String matMemo;

    @ApiModelProperty(value = "是否启用，0表示否，1表示是")
    private String isActive;

    @ApiModelProperty(value = "是否逻辑删除")
    private String isDeleted;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "所属文件库ID")
    private String attDirId;

    @ApiModelProperty(value = "材料所属，c表示企业，u表示个人，p表示工程项目")
    private String matHolder;

    @ApiModelProperty(value = "是否批复文件，0表示否，1表示是")
    private String isOfficialDoc;

    @ApiModelProperty(value = "电子材料是否必需，0表示容缺，1表示必须")
    private String attIsRequire;

    @ApiModelProperty(value = "是否通用材料  1 是 0 不是")
    private String isCommon;

    @ApiModelProperty(value = "审查要点")
    private String reviewKeyPoints;

    @ApiModelProperty(value = "审查样本")
    private String reviewSampleDoc;

    @ApiModelProperty(value = "审查依据")
    private String reviewBasis;

    @ApiModelProperty(value = "材料来源部门")
    private String matFromDept;

    @ApiModelProperty(value = "原件验收 0 无 1验 2收")
    private String duePaperType;

    @ApiModelProperty(value = "复印件验收类型 0 无 1验 2收")
    private String dueCopyType;

    @ApiModelProperty(value = "纸质材料类型 0无 1 A3, 2 A4 ,3 A5")
    private String paperMatType;

    @ApiModelProperty(value = "是否支持容缺 0 否 1是")
    private String zcqy;

    @ApiModelProperty(value = "根组织ID")
    private String rootOrgId;

    @ApiModelProperty(value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单")
    private String matProp;

    @ApiModelProperty(value = "证照定义ID")
    private String certId;

    @ApiModelProperty(value = "表单定义ID")
    private String stoFormId;

    @ApiModelProperty(value = "扩展字段：证照名称")
    private String certName;

    @ApiModelProperty(value = "扩展字段：表单名称")
    private String formName;

    @ApiModelProperty(value = "标准材料ID")
    private String stdmatId;

    @ApiModelProperty("标准材料编号")
    private String stdmatCode;

    @ApiModelProperty("标准材料名称")
    private String stdmatName;

    /**
     * 扩展字段: 电子材料
     */
    private List<AeaHiItemMatinst> attMatinstList;

    /**
     * 扩展字段: 纸质原件材料
     */
    private List<AeaHiItemMatinst> pageMatinstList;

    /**
     * 扩展字段: 纸质复印件材料
     */
    private List<AeaHiItemMatinst> copyMatinstList;

    //扩展字段
    private List<AeaHiItemMatinst> forminstList;

    private List<AeaHiItemMatinst> certinstList;

    private String ybKbDetailIds;//空表样表ID(用逗号拼接)

    /**
     * aea_par_in 主键
     */
    private String parInId;
    // 关联的情形id
    private String parStateId;
    //aea_item_inout主键
    private String inId;
    private String itemStateId;
    private String stateVerId;


    private Long sampleDocCount; // (样本文档多少)
    private Long templateDocCount; // (模板文档多少)
    private Long reviewSampleDocCount; // (审查样本多少)

    // 同步开普所需
    private List<AeaItemMatAttr> kbslAttachList; // 模板附件
    private List<AeaItemMatAttr> sjydsAttachList; // 审查样本
    private List<AeaItemMatAttr> ybslAttachList; // 材料样例文

    //非表字段
    private  String answerType;//问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案
    private String keyword; //  查询关键词
    private String matTypeName; //所属类别
    private String typeName;
    private String itemName;//事项名称,查询全局材料时使用
    private Long realPaperCount;
    private Long realCopyCount;
    private Long attCount;
    private List<AeaHiItemInoutinst> aeaHiItemInoutinsts;
    private List<AeaHiItemMatinst> aeaHiItemMatinsts;

    private String busiType;
    private String itemVerId;
    private String isInput;
    private String itemId;
    private String isStateIn;
    private String stageId;
    private String unitInfoId;
    private String iteminstId;
    private String inoutId;
    private String isOptionItem;

    public Long setSortNo(Long sortNo){
        return sortNo==null ? 0:sortNo;
    }
    //补正材料回显添加三个字段paperMatinsts，copyMatinsts，attMatinsts
    private List<AeaHiItemMatinst> paperMatinsts;//纸质原件
    private List<AeaHiItemMatinst> copyMatinsts;//纸质原件
    private List<AeaHiItemMatinst> attMatinsts;//纸质原件

    public AeaItemMat copyOne(String matCode) {

        AeaItemMat newOne = new AeaItemMat();
        newOne.setMatId(UUID.randomUUID().toString());
        newOne.setMatCode(matCode);
        newOne.setMatName(this.getMatName());
        newOne.setIsGlobalShare(this.getIsGlobalShare());
        newOne.setMatTypeId(this.getMatTypeId());
        newOne.setReceiveMode(this.getReceiveMode());
        newOne.setDuePaperCount(this.getDuePaperCount());
        newOne.setDueCopyCount(this.getDueCopyCount());
        newOne.setMatFrom(this.getMatFrom());
        newOne.setSampleDoc(this.getSampleDoc());
        newOne.setTemplateDoc(this.getTemplateDoc());
        newOne.setMatRequire(this.getMatRequire());
        newOne.setMatBasis(this.getMatBasis());
        newOne.setPaperIsRequire(this.getPaperIsRequire());
        newOne.setAttIsRequire(this.getAttIsRequire());
        newOne.setSortNo(this.getSortNo());
        newOne.setMatMemo(this.getMatMemo());
        newOne.setIsActive(this.getIsActive());
        newOne.setIsDeleted(this.getIsDeleted());
        newOne.setCreater(this.creater);
        newOne.setRootOrgId(this.rootOrgId);
        newOne.setCreateTime(new Date());
        newOne.setAttDirId(this.getAttDirId());
        newOne.setMatTypeName(this.getMatTypeName());
        newOne.setMatHolder(this.getMatHolder());
        newOne.setTypeName(this.getTypeName());
        newOne.setItemName(this.getItemName());
        newOne.setRealPaperCount(this.getRealPaperCount());
        newOne.setRealCopyCount(this.getRealCopyCount());
        newOne.setAttCount(this.getAttCount());
        newOne.setAeaHiItemInoutinsts(this.getAeaHiItemInoutinsts());
        newOne.setAeaHiItemMatinsts(this.getAeaHiItemMatinsts());
        newOne.setIsOfficialDoc(this.getIsOfficialDoc());
        newOne.setIsCommon(this.getIsCommon());
        newOne.setReviewKeyPoints(this.getReviewKeyPoints());
        newOne.setReviewSampleDoc(this.getReviewSampleDoc());
        newOne.setReviewBasis(this.getReviewBasis());
        newOne.setOpen(this.getOpen());
        newOne.setIsGlobal(this.getIsGlobal());
        newOne.setMatProp(this.getMatProp());
        newOne.setCertId(this.getCertId());
        newOne.setStoFormId(this.getStoFormId());
        return newOne;
    }
}
