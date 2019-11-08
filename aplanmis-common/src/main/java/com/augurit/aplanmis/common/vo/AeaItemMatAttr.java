package com.augurit.aplanmis.common.vo;

import java.io.Serializable;

/**
 * @author ZhangXinhui
 * @date 2019/7/30 030 11:54
 * @desc
 **/
public class AeaItemMatAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idNum; // (文件ID)
    private String name; // (文件名称)
    private String nameExt; // (文件名称后缀)
    private String path; // (文件路径)
    private String size; // (文件大小)
    private String uploadTime; // ()

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameExt() {
        return nameExt;
    }

    public void setNameExt(String nameExt) {
        this.nameExt = nameExt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}

