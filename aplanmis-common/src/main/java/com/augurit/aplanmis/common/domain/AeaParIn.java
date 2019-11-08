package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段输入定义表-模型
 */

@Data
public class AeaParIn implements Serializable {

    private static final long serialVersionUID = 1L;
    private String inId; // (ID)
    private String stageId; // (阶段定义ID)
    private String isOwner; // (是否为当前事项直接所有，0表示其他事项所有，1表示当前事项所有)
    private String isStateIn; // (是否为情形输入，0表示直接的事项输入，1表示关联情形的输入【当IS_INPUT=1】)
    private String parStateId; // (情形ID【当IS_INPUT=1】)
    private String fileType; // (文件类型，mat表示材料，cert表示电子证照)
    private String matId; // (材料定义ID【当FILE_TYPE=mat】)
    private String certId; // (电子证照定义ID【当FILE_TYPE=cert】)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String isDeleted; // (是否删除 0不删除 1删除)
    @Size(max = 10)
    private Long sortNo; // (材料排序字段)
    private String isCommon; // (1 通用材料，0非通用材料)
    private String rootOrgId;//根组织ID

    // 扩展字段
    private String keyword;  // 查询字段
    private String aeaMatCertName;//材料或者电子证照名称
    private String aeaMatCertCode;//材料或者电子证照编码
    private String aeaMatCertMemo;//材料或者电子证照备注
    private String itemName;//关联的事项的名称
    private boolean isCheck;//是否是以选中的材料
    private String isGlobalShare;//是否全局材料
    private String formId; // 表单id

}
