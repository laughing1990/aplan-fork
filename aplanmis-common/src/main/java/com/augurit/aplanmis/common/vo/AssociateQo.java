package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssociateQo {
        private List<String> inoutIdList = new ArrayList<>();
        private String itemStateId;
        private String itemId;
        private String itemVerId;
}