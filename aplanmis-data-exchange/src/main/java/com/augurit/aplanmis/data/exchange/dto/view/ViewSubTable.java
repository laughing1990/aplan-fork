package com.augurit.aplanmis.data.exchange.dto.view;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yinlf
 * @Date 2019/10/22
 */
@Data
public class ViewSubTable {
    private String tableName;
    private Set<String> fields = new HashSet<>();

    public void addFiled(String field){
        this.fields.add(field);
    }
}