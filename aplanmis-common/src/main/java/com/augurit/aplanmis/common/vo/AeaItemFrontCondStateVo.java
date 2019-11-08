package com.augurit.aplanmis.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 事项前置条件情形数据
 * 2018.11.15
 */
public class AeaItemFrontCondStateVo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;//节点id
    private String name;//节点名称
    private String sjlx;//数据类型
    private String sfzz;//是否终止
    private Long muiltSelect;// 多选几
    private String bz; // 备注
    private List<AeaItemFrontCondStateVo> childs;//子节点集合

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

    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    public String getSfzz() {
        return sfzz;
    }

    public void setSfzz(String sfzz) {
        this.sfzz = sfzz;
    }

    public Long getMuiltSelect() {
        return muiltSelect;
    }

    public void setMuiltSelect(Long muiltSelect) {
        this.muiltSelect = muiltSelect;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public List<AeaItemFrontCondStateVo> getChilds() {
        return childs;
    }

    public void setChilds(List<AeaItemFrontCondStateVo> childs) {
        this.childs = childs;
    }
}
