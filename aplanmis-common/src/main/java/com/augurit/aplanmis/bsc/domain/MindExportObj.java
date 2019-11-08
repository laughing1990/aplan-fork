package com.augurit.aplanmis.bsc.domain;
import java.io.Serializable;

public class MindExportObj implements Serializable {

    private static final long serialVersionUID = 1L;

    private MindExportData data;

    private MindExportObj[] children;

    public MindExportData getData() {
        return data;
    }

    public void setData(MindExportData data) {
        this.data = data;
    }

    public MindExportObj[] getChildren() {
        return children;
    }

    public void setChildren(MindExportObj[] children) {
        this.children = children;
    }
}
