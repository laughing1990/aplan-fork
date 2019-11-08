package com.augurit.aplanmis.bsc.domain;

public class MindExportData extends MindBaseEntity {

    private static final long serialVersionUID = 1L;

    private String text;

    private String operatorTag;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperatorTag() {
        return operatorTag;
    }

    public void setOperatorTag(String operatorTag) {
        this.operatorTag = operatorTag;
    }
}