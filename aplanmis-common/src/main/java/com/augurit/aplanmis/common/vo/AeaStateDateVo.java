package com.augurit.aplanmis.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 情形数据集
 * 2018.11.15
 */
public class AeaStateDateVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;//节点id
    private String name;//节点名称
    private String sfbx;//是否必选
    private String sfdx;//是否多选
    private String sjlx;//数据类型
    private List<AeaStateDateVo> childs;//子节点集合
    private AeaItemMatKpVo data;//关联的材料要素
    private String open;
    public AeaStateDateVo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfbx() {
        return sfbx;
    }

    public void setSfbx(String sfbx) {
        this.sfbx = sfbx;
    }

    public String getSfdx() {
        return sfdx;
    }

    public void setSfdx(String sfdx) {
        this.sfdx = sfdx;
    }

    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    public List<AeaStateDateVo> getChilds() {
        return childs;
    }

    public void setChilds(List<AeaStateDateVo> childs) {
        this.childs = childs;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public AeaItemMatKpVo getData() {
        return data;
    }

    public void setData(AeaItemMatKpVo data) {
        this.data = data;
    }
}
