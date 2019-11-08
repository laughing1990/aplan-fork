package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -模型
 */
@Data
public class AeaItem extends ZtreeNode implements Serializable {

    private static final long serialVersionUID = 1L;
    private String itemId; // (事项ID)
    private String parentItemId; // ()
    private String itemSeq; // ()

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID


    //扩展字段
    private String itemBasicId;// (事项内容ID，主键ID)
    private String itemName; // (事项名称)
    private String orgId; // (组织ID)
    private String itemType; // (事项类型)
    private String verNum; //事项版本
    private String itemCode; //事项code
    private String OrgName;//组织名称
    private String itemVerId; //事项版本id
    private Long dueNum;//
    private String bjType;
    private String itemNature;
}
