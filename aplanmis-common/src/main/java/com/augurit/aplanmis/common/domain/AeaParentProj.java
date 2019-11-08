package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:26</li>
 * </ul>
 */
@Data
public class AeaParentProj implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String nodeProjId; // ()
    private String parentProjId; // ()
    private String childProjId; // ()
    private String projSeq; // ()
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime; // ()
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;
    private String rootOrgId;//根组织ID

    public AeaParentProj() {}

    public AeaParentProj(String parentProjId, String childProjId, String projSeq, String creater, String rootOrgId) {
        this.nodeProjId = UUID.randomUUID().toString();
        this.parentProjId = parentProjId;
        this.childProjId = childProjId;
        this.projSeq = projSeq;
        this.createTime = new Date();
        this.creater = creater;
        this.rootOrgId = rootOrgId;
    }
}
