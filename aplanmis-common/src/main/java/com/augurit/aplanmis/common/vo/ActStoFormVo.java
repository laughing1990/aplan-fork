package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActStoFormVo  extends ActStoForm implements Serializable {

        private String stageId; //阶段Id
        private String itemVerId;//事项版本Id

}
