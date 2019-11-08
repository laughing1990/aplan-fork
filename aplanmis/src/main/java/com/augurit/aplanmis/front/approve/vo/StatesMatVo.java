package com.augurit.aplanmis.front.approve.vo;

import com.augurit.aplanmis.common.domain.AeaItemState;
import lombok.Data;

import java.util.List;

@Data
public class StatesMatVo {
    private AeaItemState question;
    private List<AeaItemState> answers;
    private List<String> selectId;
}
