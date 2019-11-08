package com.augurit.aplanmis.admin.par;

import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin/aea/stage/state")
@RestController
public class AeaParStateAdminController {
    @Autowired
    private AeaParStateService aeaParStateService;

    /**
     * 获取作为流程启动条件的情形集合
     * @param stageId 阶段ID
     * @return
     * @throws Exception
     */
    @RequestMapping("/getProcStartCondStageStates.do")
    public List<AeaParState> getProcStartCondStageStates(String stageId) throws Exception {
        return aeaParStateService.getProcStartCondStageStates(stageId);
    }
}
