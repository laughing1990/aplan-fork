package com.augurit.aplanmis.common.dto;

import com.augurit.aplanmis.common.domain.ProjStageApplyForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjStageApplyDto {
    private String projInfoId;
    private String standardSortNo;
    private Date firstApplyTime;
    private List<ProjStageApplyForm> projStageApplyFormList = new ArrayList<>();

    public void addForm(ProjStageApplyForm form){
        this.projStageApplyFormList.add(form);
    }
}
