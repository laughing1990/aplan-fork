package com.augurit.aplanmis.common.service.theme;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
@DisplayName("主题测试")
class AeaParThemeServiceTest extends BaseTest {
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Test
    void insertAeaParTheme() {
    }

    @Test
    void updateAeaParTheme() {
    }

    @Test
    void deleteAeaParThemeByThemeId() {
    }

    @Test
    void listAeaParTheme() {
    }

    @Test
    void getAeaParThemeByThemeId() throws Exception{
        AeaParTheme aeaParThemeByThemeId = aeaParThemeService.getAeaParThemeByThemeId("03b61f98-a815-491a-8a41-bb1b29d9885d");
        Assert.notNull(aeaParThemeByThemeId,"");
    }

    @Test
    void getAeaParThemeListByThemeType() throws Exception{
        List<AeaParTheme> AeaParThemeList = aeaParThemeService.getAeaParThemeListByThemeType("A00001");
        Assert.notEmpty(AeaParThemeList, "");
    }

    @Test
    void getAeaParThemeSeqByThemeId() throws Exception{
        AeaParThemeSeq aeaParThemeSeq = aeaParThemeService.getAeaParThemeSeqByThemeId("03b61f98-a815-491a-8a41-bb1b29d9885d", "0368948a-1cdf-4bf8-a828-71d796ba89f6");
        Assert.isTrue(aeaParThemeSeq!=null,"");
    }

    @Test
    void getAeaParThemeVerByThemeIdAndVerNum() throws Exception{
        AeaParThemeVer aeaParThemeVer1 = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum("03b61f98-a815-491a-8a41-bb1b29d9885d", null, "0368948a-1cdf-4bf8-a828-71d796ba89f6");
        //Assert.isTrue(aeaParThemeVer1!=null,"");
        AeaParThemeVer aeaParThemeVer2 = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum("03b61f98-a815-491a-8a41-bb1b29d9885d", 9.0, "0368948a-1cdf-4bf8-a828-71d796ba89f6");
        Assert.isTrue(aeaParThemeVer1.getThemeVerId().equals(aeaParThemeVer2.getThemeVerId()),"");
    }

    @Test
    void getAeaParThemeByProjIdorProjCode() throws Exception{
       /* AeaParTheme aeaParTheme1 = aeaParThemeService.getAeaParThemeByProjIdorProjCode("050de049-eb7e-459a-9340-9d4fb8120224", null);
        Assert.isTrue(aeaParTheme1== null, "");
        AeaParTheme aeaParTheme2 = aeaParThemeService.getAeaParThemeByProjIdorProjCode("186f3db2-c112-42f7-8d16-85182905bd69", null);
        Assert.isTrue("18e45227-6e6c-497a-a6c8-fa67b64b9a3a".equals(aeaParTheme2.getThemeId()), "");*/
        //localCode
        AeaParTheme aeaParTheme3 = aeaParThemeService.getAeaParThemeByProjIdorProjCode(null, "ZBM-R-20190531-65804711");
        AeaParTheme aeaParTheme5 = aeaParThemeService.getAeaParThemeByProjIdorProjCode("e0da1db1-1aa3-4abd-820c-bc91aa870694", "2018-130184-70-02-000228");
        //gcbm
        AeaParTheme aeaParTheme4 = aeaParThemeService.getAeaParThemeByProjIdorProjCode(null, "ZBM-R-20190531-65804711");
        Assert.isTrue(aeaParTheme3.getThemeName().equals(aeaParTheme4.getThemeName()),"");


    }

    @Test
    void getMaxVerAeaParTheme() throws Exception{
        List<AeaParTheme> AeaParThemeList1 = aeaParThemeService.getMaxVerAeaParTheme(null, null);
        Assert.notEmpty(AeaParThemeList1, "长度不为空");
        List<AeaParTheme> AeaParThemeList = aeaParThemeService.getMaxVerAeaParTheme("A00001", null);
        Assert.notEmpty(AeaParThemeList, "长度不为空");
        List<AeaParTheme> aeaParThemes = aeaParThemeService.getMaxVerAeaParTheme("A00001", "03b61f98-a815-491a-8a41-bb1b29d9885d");
        Assert.isTrue(aeaParThemes.size() == 1, "length:1");

    }
}