package com.augurit.aplanmis.common.constants;

/**
 * 作为临时常量，后期会改动
 */
public interface ActTplAdminConstants {
   /*
        待办视图 "view-code-00000011";
        已办视图 "view-code-00000012";
        预审待办视图 "view-code-00000030";
        预审已办视图 "view-code-00000031";
    */

    //视图
    public static final String[] VIEWCODELIST={
            "view-code-00000051",
            "view-code-00000058",
            "view-code-00000059"
    };

    /**
     * 表单
     */
    public static final String[] FORMCODELIST={
            "form-code-000001027",
            "form-code-00000210",
            "form-code-clfj",
            "form-code-spgc",
            "form-code-yzlt"
    };

    //单项模板-基本信息表单
    /*public static final String BASICINFOFORM = "form-code-000001027";
    //并联模板-申报信息表单
    public static final String APPLYINFOFORM = "form-code-00000309";
    //批文批复
    public static final String COMMENTANDAPPROVAL= "form-code-00000210";*/

    //东莞市部门ID 0368948a-1cdf-4bf8-a828-71d796ba89f6,以前是3d77db51-4e48-4de3-b6ee-9def07786035
    public static final String DGSORGID="0368948a-1cdf-4bf8-a828-71d796ba89f6";

    //页面公共元素code
    /**
     *   wfBusSend, 办理(内部流程)
     *    dg_tongguo1,通过（内部流程）
     *     dg_butongguo1,不通过（内部流程）
     *      "dgZhiZheng" 制证（内部流程）

     * "dg_banjie", 办结(窗口)
     * "dg_fazheng",发证（窗口）
     * "dgTuiJian" 退件（窗口）
     *   yushentongguo 预审通过
     *  buquancailiao 补全材料
     * "dayinhuizhi",打印回执（窗口）
     * "addTaskComment",填写意见
     *  "showDiagramDialog" 查看流程图
     suspendProcess 挂起流程
     activeProcess激活流程
     */
    public static final String[] COMMONELEMENTLIST={
            "wf_button_toolbar",
            "item_huitui",
            "item_zhiZheng",
            "item_jinrutebiechengxu",
            "win_dayinhuizhi",
            "item_tuichutebiechengxu",
            "item_cailiaobuzheng",
            "item_shouli",
            "win_wfBusSend",
            "item_wfBusSend",
            "item_banjie_tongguo",
            "win_banjie",
            "item_banjie_rongquetongguo",
            "yushentongguo_chuangkou",
            "item_rongqueshouli",
            "cailiaobuquan_chuangkou",
            "win_shouli",
            "item_banjie_butongguo",
            "buyushouli_chuangkou",
            "wf_mati_toolbar",
            "flow_zhuanban"
    };

}
