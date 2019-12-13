package com.augurit.aplanmis.data.exchange.convert.datatree;

import lombok.Data;

import java.util.List;

@Data
public class DataTree {
    protected String id;
    protected Boolean hasChildren = false;
    protected List<DataTree> children;
}
