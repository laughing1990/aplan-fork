package com.augurit.aplanmis.admin.window.vo;

import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import lombok.Data;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/9/16
 */
@Data
public class AeaWindowStageVo {
    String windowId;
    List<AeaServiceWindowStage> aeaServiceWindowStageList;
}
