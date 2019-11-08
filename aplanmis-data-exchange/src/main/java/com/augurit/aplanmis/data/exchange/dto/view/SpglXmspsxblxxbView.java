package com.augurit.aplanmis.data.exchange.dto.view;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/22
 */
public class SpglXmspsxblxxbView extends SpglViewSto{

    private SpglXmspsxblxxbView(){}


    public static SpglXmspsxblxxbView instance() {
        SpglXmspsxblxxbView spglDfxmsplcjdfxsxxxbVew = new SpglXmspsxblxxbView();
        ViewSubTable masterTable = new ViewSubTable();
        masterTable.setTableName("AEA_HI_ITEMINST");
        masterTable.addFiled("dfsjzj");
        spglDfxmsplcjdfxsxxxbVew.masterTable = masterTable;
        List<ViewSubTable> viewSubTableList = new ArrayList<>();

        ViewSubTable actStoTimeruleInst = new ViewSubTable();
        actStoTimeruleInst.setTableName("ACT_STO_TIMERULE_INST");
        actStoTimeruleInst.addFiled("sxblsx");
        viewSubTableList.add(actStoTimeruleInst);

        ViewSubTable aeaItemVer = new ViewSubTable();
        aeaItemVer.setTableName("AEA_ITEM_VER");
        aeaItemVer.addFiled("spsxbbh");
        viewSubTableList.add(aeaItemVer);

        ViewSubTable aeaItemBasic = new ViewSubTable();
        aeaItemBasic.setTableName("AEA_ITEM_BASIC");
        aeaItemBasic.addFiled("spsxbm");
        viewSubTableList.add(aeaItemBasic);

        ViewSubTable aeaHiApplyinst = new ViewSubTable();
        aeaHiApplyinst.setTableName("AEA_HI_APPLYINST");
        aeaHiApplyinst.addFiled("slfs");
        viewSubTableList.add(aeaHiApplyinst);

        /*ViewSubTable aeaParThemeVer = new ViewSubTable();
        aeaParThemeVer.setTableName("AEA_PAR_THEME_VER");
        aeaParThemeVer.addFiled("splcbbh");
        viewSubTableList.add(aeaParThemeVer);*/

        /*ViewSubTable aeaParTheme = new ViewSubTable();
        aeaParTheme.setTableName("AEA_PAR_THEME");
        aeaParTheme.addFiled("splcbm");
        viewSubTableList.add(aeaParTheme);*/

        ViewSubTable aeaProjInfo = new ViewSubTable();
        aeaProjInfo.setTableName("AEA_PROJ_INFO");
        aeaProjInfo.addFiled("gcdm");
        viewSubTableList.add(aeaProjInfo);

        ViewSubTable opuOmOrg = new ViewSubTable();
        opuOmOrg.setTableName("OPU_OM_ORG");
        opuOmOrg.addFiled("spbmbm");
        opuOmOrg.addFiled("spbmmc");
        viewSubTableList.add(opuOmOrg);

        spglDfxmsplcjdfxsxxxbVew.viewSubTableList = viewSubTableList;
        return spglDfxmsplcjdfxsxxxbVew;
    }
}
