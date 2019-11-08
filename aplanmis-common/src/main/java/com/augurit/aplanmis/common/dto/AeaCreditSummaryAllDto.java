package com.augurit.aplanmis.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AeaCreditSummaryAllDto {

    private String itemName;

    private String itemCode;

    private List<AeaCreditSummaryDto> summaries = new ArrayList<>();
}
