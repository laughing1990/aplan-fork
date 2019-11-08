package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bpm.admin.tpl.vo.AppProcCaseDefVo;

import java.util.List;

/**
 * 主流程和子流程父子关系节点类
 * @author:chendx
 * @date: 2019-08-27
 * @time: 15:03
 */
public class AppProcCaseDefTreeVo extends AppProcCaseDefVo {

    List<AppProcCaseDefTreeVo> children;

    /**
     * 树节点id
     */
    private String treeNodeId;

    /**
     * 树的父节点id
     */
    private String pid;

    private String isPublic;

    /**
     * 树节点是否流程，1是0否
     */
    private String isProcess;

    /**
     * 触发的流程关联的事项名称
     */
    private String itemName;

    /**
     * 事项类型，1标准事项 0实施事项
     */
    private String itemType;

    /**
     * 事项审批部门
     */
    private String itemApproveOrg;

    public String getTreeNodeId() {
        return treeNodeId;
    }

    public void setTreeNodeId(String treeNodeId) {
        this.treeNodeId = treeNodeId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsProcess() {
        return isProcess;
    }

    public void setIsProcess(String isProcess) {
        this.isProcess = isProcess;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemApproveOrg() {
        return itemApproveOrg;
    }

    public void setItemApproveOrg(String itemApproveOrg) {
        this.itemApproveOrg = itemApproveOrg;
    }

    public List<AppProcCaseDefTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<AppProcCaseDefTreeVo> children) {
        this.children = children;
    }
}
