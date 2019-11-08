package com.augurit.aplanmis.common.service.stage;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("阶段测试")
class AeaParStageServiceTest extends BaseTest {
    @Autowired
    private AeaParStageService aeaParStageService;

    @Test
    void listAeaParStageByThemeIdOrThemeVerId() throws Exception{
        List<AeaParStage> aeaParStages3 = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(null, "f022394d-1f75-4854-a408-af374c673a6f",null);
        List<AeaParStage> aeaParStages4 = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId("03b61f98-a815-491a-8a41-bb1b29d9885d", null,null);
        List<AeaParStage> aeaParStages5 = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId("03b61f98-a815-491a-8a41-bb1b29d9885d", "f022394d-1f75-4854-a408-af374c673a6f",null);
        System.out.println(aeaParStages3.size());
        System.out.println(aeaParStages4.size());
        Assert.isTrue(aeaParStages3.size() == aeaParStages4.size(), "");
        Assert.isTrue(aeaParStages5.size() == aeaParStages4.size(), "");
    }

    @Test
    void listAeaParStageByProjIdOrProjCode() throws Exception{
        List<AeaParStage> aeaParStages = aeaParStageService.listAeaParStageByProjIdOrProjCode(null, "ZBM-R-20190531-65804711");
        List<AeaParStage> aeaParStages1 = aeaParStageService.listAeaParStageByProjIdOrProjCode("e0da1db1-1aa3-4abd-820c-bc91aa870694", "2018-130184-70-02-000228");
        Assert.isTrue(aeaParStages.size()==aeaParStages1.size(),"");
    }
}