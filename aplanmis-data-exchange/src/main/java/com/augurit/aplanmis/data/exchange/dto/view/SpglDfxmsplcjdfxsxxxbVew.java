package com.augurit.aplanmis.data.exchange.dto.view;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/22
 */
public class SpglDfxmsplcjdfxsxxxbVew extends SpglViewSto{

    private SpglDfxmsplcjdfxsxxxbVew(){}


    public static SpglDfxmsplcjdfxsxxxbVew instance() {
        SpglDfxmsplcjdfxsxxxbVew spglDfxmsplcjdfxsxxxbVew = new SpglDfxmsplcjdfxsxxxbVew();
        ViewSubTable masterTable = new ViewSubTable();
        masterTable.setTableName("AEA_HI_ITEMINST");
        masterTable.addFiled("dfsjzj");
        spglDfxmsplcjdfxsxxxbVew.masterTable = masterTable;
        List<ViewSubTable> viewSubTableList = new ArrayList<>();

        ViewSubTable actStoTimeruleInst = new ViewSubTable();
        actStoTimeruleInst.setTableName("ACT_STO_TIMERULE_INST");
        actStoTimeruleInst.addFiled("spsxblsx");
        viewSubTableList.add(actStoTimeruleInst);

        ViewSubTable aeaItemVer = new ViewSubTable();
        aeaItemVer.setTableName("AEA_ITEM_VER");
        aeaItemVer.addFiled("spsxbbh");
        viewSubTableList.add(aeaItemVer);

        ViewSubTable aeaItemBasic = new ViewSubTable();
        aeaItemBasic.setTableName("AEA_ITEM_BASIC");
        aeaItemBasic.addFiled("spsxbm");
        viewSubTableList.add(aeaItemBasic);

        ViewSubTable opuOmOrg = new ViewSubTable();
        opuOmOrg.setTableName("OPU_OM_ORG");
        opuOmOrg.addFiled("spbmbm");
        opuOmOrg.addFiled("spbmmc");
        viewSubTableList.add(opuOmOrg);

        spglDfxmsplcjdfxsxxxbVew.viewSubTableList = viewSubTableList;
        return spglDfxmsplcjdfxsxxxbVew;
    }
}
