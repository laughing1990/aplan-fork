package com.augurit.aplanmis.common.qo;

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