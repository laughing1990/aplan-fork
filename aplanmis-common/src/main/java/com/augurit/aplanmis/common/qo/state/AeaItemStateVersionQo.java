package com.augurit.aplanmis.common.qo.state;

import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.qo.BaseQo;

public class AeaItemStateVersionQo extends BaseQo {

    private String itemStateVerId;
    private String itemStateVerIdNotEq;
    private String itemId; //事项id
    private String itemVerId;//事项版本id
    private PublishStatus verStatus;//未发布， 试运行， 已发布
    private String isDeleted;//是否删除
    private String rootOrgId;

    private AeaItemStateVersionQo() {
        this.isDeleted = "0";//默认查询所有未删除的记录
    }

    public static AeaItemStateVersionQo createQuery() {
        return new AeaItemStateVersionQo();
    }

    public AeaItemStateVersionQo itemStateVerIdEq(String itemStateVerId) {
        this.itemStateVerId = itemStateVerId;
        return this;
    }

    public AeaItemStateVersionQo itemStateVerIdNotEq(String itemStateVerIdNotEq) {
        this.itemStateVerIdNotEq = itemStateVerIdNotEq;
        return this;
    }

    public AeaItemStateVersionQo itemIdEq(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public AeaItemStateVersionQo itemVerIdEq(String itemVerId) {

        this.itemVerId = itemVerId;
        return this;
    }

    public AeaItemStateVersionQo verStatusEq(PublishStatus verStatus) {

        this.verStatus = verStatus;
        return this;
    }

    public AeaItemStateVersionQo isDeletedEq(String isDeleted) {

        this.isDeleted = isDeleted;
        return this;
    }

    public AeaItemStateVersionQo rootOrgIdEq(String rootOrgId) {

        this.rootOrgId = rootOrgId;
        return this;
    }

    public AeaItemStateVersionQo createrEq(String creater) {
        this.creater = creater;
        return this;
    }

    public AeaItemStateVersionQo modifierEq(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public AeaItemStateVersionQo orderByField(String orderByField) {
        this.orderByField = orderByField;
        return this;
    }

    public AeaItemStateVersionQo orderByDirection(String orderByDirection) {
        this.orderByDirection = orderByDirection;
        return this;
    }
}
