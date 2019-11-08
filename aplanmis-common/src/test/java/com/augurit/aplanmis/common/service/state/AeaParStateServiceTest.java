package com.augurit.aplanmis.common.service.state;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaParState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayName("并联情形测试")
public class AeaParStateServiceTest extends BaseTest {
    private AeaParStateService aeaParStateService;

    @Test
    @DisplayName("据阶段ID获取阶段root情形")
    public void testListRootAeaParStateByStageId() throws Exception {
        //0a7eddf7-8d1f-4a71-bc9e-fd599ffa3153   b805ffb3-3974-447d-810c-024ea9c6dc65
        //List<AeaParState> aeaParStates = aeaParStateService.listRootAeaParStateByStageId("0a7eddf7-8d1f-4a71-bc9e-fd599ffa3153","");
        //System.out.println(aeaParStates.size());
    }

    @Test
    @DisplayName("据阶段ID获取阶段情形")
    public void testListAeaParStateByStageId() throws Exception {
        List<AeaParState> aeaParStates = aeaParStateService.listAeaParStateByStageId("b805ffb3-3974-447d-810c-024ea9c6dc65");
        System.out.println(aeaParStates.size());
    }

    @Test
    public void listTreeAeaParStateByStageId() throws Exception {
        List<AeaParState> aeaParStates = aeaParStateService.listTreeAeaParStateByStageId("0a7eddf7-8d1f-4a71-bc9e-fd599ffa3153");
        System.out.println(aeaParStates.size());
    }

    @Test
    @Tag("根据父ID查询子ID")
    public void listAeaParStateByParentStateId() throws Exception {
        List<AeaParState> aeaParStates = aeaParStateService.listAeaParStateByParentStateId("fb4a8f9b-e78c-40cd-adff-2b722165fa93","","");
        System.out.println(aeaParStates.size());
    }

    @Test
    @Tag("根据父ID查询子ID")
    public void listAeaParStateByStageinstIdORApplyinstId() throws Exception {
        List<AeaParState> aeaParStates = aeaParStateService.listAeaParStateByStageinstIdORApplyinstId(null, "03852a39-b2f9-4060-bd5e-14ac0175eaf6");
        System.out.println(aeaParStates.size());
        aeaParStates = aeaParStateService.listAeaParStateByStageinstIdORApplyinstId("0bf6c8e9-43cd-49f0-83c7-5ab216826ecf", null);
        System.out.println(aeaParStates.size());
    }

    @Autowired
    void setAeaParStateService(AeaParStateService aeaParStateService) {
        this.aeaParStateService = aeaParStateService;
    }

}
