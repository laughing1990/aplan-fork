package com.augurit.aplanmis.common.listener.builder;

import com.augurit.agcloud.bsc.domain.BscJobRemind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
@Scope("prototype")
@EnableConfigurationProperties
public class BscJobRemindBuilder {

    @Autowired
    private BscJobRemindConfigProperties bscJobRemindConfigProperties;

    private BscJobRemind bscJobRemind;

    public BscJobRemind build() {
        bscJobRemind = new BscJobRemind();
        return remindMode(bscJobRemindConfigProperties.getMode())
                .remindCount(bscJobRemindConfigProperties.getCount())
                .remindInternal(bscJobRemindConfigProperties.getInternal())
                .remindInternalUnit(bscJobRemindConfigProperties.getInternalUnit())
                .remindType(bscJobRemindConfigProperties.getType())
                .remindId(UUID.randomUUID().toString())
                .tableName(bscJobRemindConfigProperties.getTableName())
                .pkName(bscJobRemindConfigProperties.getPkName())
                .bscJobRemind;
    }

    private BscJobRemindBuilder remindMode(String mode) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindMode(mode);
        return this;
    }

    private BscJobRemindBuilder remindCount(Long count) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindCount(count);
        return this;
    }

    private BscJobRemindBuilder remindInternal(Long internal) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindIntervalNum(internal);
        return this;
    }

    private BscJobRemindBuilder remindInternalUnit(String internalUnit) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindIntervalUnit(internalUnit);
        return this;
    }

    private BscJobRemindBuilder remindType(String type) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindType(type);
        return this;
    }

    private BscJobRemindBuilder remindId(String id) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setRemindId(id);
        return this;
    }

    private BscJobRemindBuilder tableName(String tableName) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setTableName(tableName);
        return this;
    }

    private BscJobRemindBuilder pkName(String pkName) {
        Assert.notNull(bscJobRemind, "bscJobRemind is null.");
        bscJobRemind.setPkName(pkName);
        return this;
    }

}
