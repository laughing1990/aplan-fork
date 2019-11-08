package com.augurit.aplanmis.common.qo.item;

import com.augurit.aplanmis.common.qo.BaseQo;

import java.util.List;

public class ItemMatInoutQo {

    private String itemId;
    private String itemVerId;
    private String stateVerId;
    private String itemStateId;
    private String itemStateIdNullable;
    private String isInput;
    private String isCommon;
    private String isStateIn;
    private String isDeleted;
    private String matName;
    private String matCode;
    private String keyword;
    private String isActive;
    private String fileType;
    private List<String> itemStateIds;
    private String rootOrgId;

    private ItemMatInoutQo() {
        this.isDeleted = "0";
    }

    public ItemMatInoutQo itemIdEq(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemMatInoutQo itemVerIdEq(String itemVerId) {
        this.itemVerId = itemVerId;
        return this;
    }

    public ItemMatInoutQo rootOrgIdEq(String rootOrgId) {
        this.rootOrgId = rootOrgId;
        return this;
    }

    public ItemMatInoutQo matNameFullLike(String matName) {
        this.matName = "%" + matName + "%";
        return this;
    }

    public ItemMatInoutQo matCodeFullLike(String matCode) {
        this.matCode = "%" + matCode + "%";
        return this;
    }

    public ItemMatInoutQo matNameOrMatCodeFullLike(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public ItemMatInoutQo fileTypeEq(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public ItemMatInoutQo itemStateIdEq(String itemStateId) {
        this.itemStateId = itemStateId;
        return this;
    }

    public ItemMatInoutQo itemStateIdNullableNotNull() {
        this.itemStateIdNullable = BaseQo.NOT_NULL;
        return this;
    }

    public ItemMatInoutQo itemStateIdNullableIsNull() {
        this.itemStateIdNullable = BaseQo.IS_NULL;
        return this;
    }

    public ItemMatInoutQo stateVerIdEq(String stateVerId) {
        this.stateVerId = stateVerId;
        return this;
    }

    public ItemMatInoutQo isInputEq(String isInput) {
        this.isInput = isInput;
        return this;
    }

    public ItemMatInoutQo isActiveEq(String isActive) {
        this.isActive = isActive;
        return this;
    }

    public ItemMatInoutQo isCommonEq(String isCommon) {
        this.isCommon = isCommon;
        return this;
    }

    public ItemMatInoutQo isStateInEq(String isStateIn) {
        this.isStateIn = isStateIn;
        return this;
    }

    public ItemMatInoutQo itemStateIdIn(List<String> itemStateIds) {
        if (itemStateIds == null || itemStateIds.size() < 1) {
            return this;
        }
        this.itemStateIds = itemStateIds;
        return this;
    }

    public static ItemMatInoutQo createMatInoutQuery() {
        return new ItemMatInoutQo();
    }

}
