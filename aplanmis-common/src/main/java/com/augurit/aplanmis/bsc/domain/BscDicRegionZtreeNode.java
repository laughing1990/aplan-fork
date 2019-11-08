package com.augurit.aplanmis.bsc.domain;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BscDicRegionZtreeNode extends BscDicRegion {

    @ApiModelProperty("节点Id")
    private String id;
    @ApiModelProperty("节点名称")
    private String name;
    @ApiModelProperty("父类Id")
    private String pId;
    @ApiModelProperty("父节点名称")
    private String pName;
    @ApiModelProperty("是否隐藏 checkbox / radio ，false表示显示checkbox / radio  [setting.check.enable = true 时有效]")
    private boolean nocheck = false;
    @ApiModelProperty("节点的 展开/折叠 状态(默认折叠)")
    private boolean open = true;
    @ApiModelProperty("是否水平展示，默认不水平展示")
    private boolean isHorizontal = false;
    @ApiModelProperty("节点类型")
    private String type;
    @ApiModelProperty("节点是否为父节点")
    private boolean isParent;
    @ApiModelProperty("节点图片")
    private String iconSkin;
    @ApiModelProperty("是否选中")
    private boolean isCheck = false;
    @ApiModelProperty("ElementTree节点Id")
    private String eleTreeId;

    public void initZtreeNodeProperties(String id, String name, String pId, String pName, boolean nocheck, boolean open, boolean isParent, String iconSkin) {
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.pName = pName;
        this.nocheck = nocheck;
        this.open = open;
        this.isParent = isParent;
        this.iconSkin = iconSkin;
    }

    @Override
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getEleTreeId() {
        return eleTreeId;
    }

    public void setEleTreeId(String eleTreeId) {
        this.eleTreeId = eleTreeId;
    }
}
