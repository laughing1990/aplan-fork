package com.augurit.aplanmis.front.constant;

/**
 * 意见征求常量类
 * @author:chendx
 * @date: 2019-12-25
 * @time: 10:45
 */
public class SolicitConstant {

    //征求类型，按事项征求
    public static final String SOLICIT_TYPE_ITEM = "i";

    //征求类型，按部门征求
    public static final String SOLICIT_TYPE_DEPT = "d";

    //征求意见状态：0表示未开始
    public static final String SOLICIT_STATE_WAIT = "0";

    //征求意见状态：1表示征求中
    public static final String SOLICIT_STATE_DO = "1";

    //征求意见状态：2表示已完成
    public static final String SOLICIT_STATE_DONE = "2";

    //征求意见状态：3表示已终止
    public static final String SOLICIT_STATE_END = "3";

    //任务动作，0表示正常办理
    public static final String SOLICIT_TASK_ACTION_NARMAL = "0";

    //任务动作，1表示转交给同一委办局的其他人员
    public static final String SOLICIT_TASK_ACTION_DELIVER = "1";

    //任务动作，2表示添加同一委办局的其他人员进来
    public static final String SOLICIT_TASK_ACTION_ADD = "2";

}
