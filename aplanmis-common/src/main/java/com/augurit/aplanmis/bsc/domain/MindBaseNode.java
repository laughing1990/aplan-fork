package com.augurit.aplanmis.bsc.domain;

/**
 *
 * 思维导图节点封装类
 *
 * @author hzl
 * @date 2019/4/17
 */
public class MindBaseNode extends MindBaseEntity {

    private static final long serialVersionUID = 1L;

    private String pid;
    private String open;
    private MindBaseNode[] childs;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public MindBaseNode[] getChilds() {
        return childs;
    }

    public void setChilds(MindBaseNode[] childs) {
        this.childs = childs;
    }
}

