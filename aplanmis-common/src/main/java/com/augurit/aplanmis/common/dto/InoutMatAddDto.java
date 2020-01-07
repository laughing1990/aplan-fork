package com.augurit.aplanmis.common.dto;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;

import java.util.Date;

@Data
public class InoutMatAddDto {

    // AeaItemInout 中的属性
    private String itemVerId;//事项版本id
    private String stateVerId;//情形版本id不能为空

    private String isOwner;
    private String isInput;//输入或者输出材料
    private String fileType;//文件类型 mat  cert
    private String isActive;//是否有效
    private String matId;//材料id
    private String isStateIn;//是否情形输入
    private String itemStateId; // 情形id

    // AeaItemMat 中的属性
    private String matCode;//材料编号
    private String matName;//材料名称
    private String isGlobalShare;
    private String matTypeId;//材料类别
    private String matTypeName;//材料类别名称
    private String matFrom;//材料来源
    private String matFromDept;//材料来源部门
    private String isCommon;//是否通用材料
    private String zcqy;//是否支持容缺

    private String templateDoc;//模板文档
    private String sampleDoc;//样本文档
    private String reviewSampleDoc;//审查样本

    private String reviewKeyPoints;//审查要点
    private String reviewBasis;//审查依据

    private String paperMatType; // 纸质材料类型
    private String paperIsRequire;//纸质是否必须
    private String attIsRequire;//电子是否必须
    private String duePaperType;//原件验收类型
    private long duePaperCount;//原件数
    private String dueCopyType;//复印件验收类型
    private long dueCopyCount;//复印件数
    private String matMemo;//备注
    private String rootOrgId; // 根组织id
    private String isOfficialDoc; // (是否批复文件，0表示否，1表示是)
    private String matProp;
    private String certId;
    private String stoFormId;
    private String stdmatId;
    private String isSign;

//    private String receiveMode;
//    private String matRequire;
//    private String matBasis;
//    private String attDirId;
//    private String matHolder;


    /**
     * 根据mat 和 inout dto来新增inout
     *
     * @param userId
     * @return
     */
    public AeaItemInout toAeaItemInout(String userId, String rootOrgId) {

        Assert.notNull(this.getMatId(), "matId不能为空");
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setInoutId(UuidUtil.generateUuid());
        aeaItemInout.setItemVerId(this.getItemVerId());
        aeaItemInout.setStateVerId(this.getStateVerId());
        aeaItemInout.setIsOwner(this.getIsOwner());
        aeaItemInout.setIsInput(this.getIsInput());
        aeaItemInout.setIsStateIn(this.getIsStateIn());
        aeaItemInout.setItemStateId(this.getItemStateId());
        aeaItemInout.setFileType(this.getFileType());
        aeaItemInout.setMatId(this.getMatId());
        aeaItemInout.setCreater(userId);
        aeaItemInout.setRootOrgId(rootOrgId);
        aeaItemInout.setCreateTime(new Date());
        aeaItemInout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaItemInout;
    }

    public AeaItemMat toAeaItemMat(String userId, String rootOrgId) {

        AeaItemMat aeaItemMat = new AeaItemMat();
        aeaItemMat.setMatId(UuidUtil.generateUuid());
        aeaItemMat.setMatCode(this.matCode);
        aeaItemMat.setMatName(this.matName);
        aeaItemMat.setIsGlobalShare("1");
        aeaItemMat.setMatTypeId(this.matTypeId);
        aeaItemMat.setDuePaperCount(this.duePaperCount);
        aeaItemMat.setDueCopyCount(this.dueCopyCount);
        aeaItemMat.setMatFrom(this.matFrom);
        aeaItemMat.setSampleDoc(this.sampleDoc);
        aeaItemMat.setTemplateDoc(this.templateDoc);
        aeaItemMat.setPaperIsRequire(this.paperIsRequire);
        aeaItemMat.setMatMemo(this.matMemo);
        aeaItemMat.setIsActive(this.isActive);
        aeaItemMat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemMat.setCreater(userId);
        aeaItemMat.setRootOrgId(rootOrgId);
        aeaItemMat.setCreateTime(new Date());
        aeaItemMat.setAttIsRequire(this.attIsRequire);
        aeaItemMat.setIsCommon(this.isCommon);
        aeaItemMat.setReviewKeyPoints(this.reviewKeyPoints);
        aeaItemMat.setReviewSampleDoc(this.reviewSampleDoc);
        aeaItemMat.setReviewBasis(this.reviewBasis);
        aeaItemMat.setMatFromDept(this.matFromDept);
        aeaItemMat.setDuePaperType(this.duePaperType);
        aeaItemMat.setDueCopyType(this.dueCopyType);
        aeaItemMat.setPaperMatType(this.paperMatType);
        aeaItemMat.setZcqy(this.zcqy);
        aeaItemMat.setMatTypeName(matTypeName);
        aeaItemMat.setRealPaperCount(this.duePaperCount);
        aeaItemMat.setRealCopyCount(this.duePaperCount);
        aeaItemMat.setIsOfficialDoc(this.isOfficialDoc);
        aeaItemMat.setMatProp(this.matProp);
        aeaItemMat.setCertId(this.certId);
        aeaItemMat.setStoFormId(this.stoFormId);
        aeaItemMat.setStdmatId(this.stdmatId);
        aeaItemMat.setIsSign(this.isSign);
//        aeaItemMat.setReceiveMode(this.receiveMode);
//        aeaItemMat.setMatRequire(this.matRequire);
//        aeaItemMat.setMatBasis(this.matBasis);
//        aeaItemMat.setAttDirId(this.attDirId);
//        aeaItemMat.setMatHolder(this.matHolder);
        return aeaItemMat;
    }
}
