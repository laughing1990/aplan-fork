package com.augurit.aplanmis.common.constants;

/**
 * 阶段情形常量
 *
 * @author xiaohutu
 */
public class AeaParStateConstants {
    //isQuestion 是否问题，0表示答案，1表示问题，2表示事项情形
    /**
     * 问题
     */
    public final static String QUESTION = "1";
    /**
     * 答案
     */
    public final static String ANSWER = "0";
    /**
     * 事项情形
     */
    public final static String ITEM_STATE = "2";

    //answerType 问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案
    /**
     * 多选答案
     */
    public final static String MULTI_ANSWER = "m";
    /**
     * 单选答案
     */
    public final static String SINGLE_ANSWER = "s";
    /**
     * y表示是否选择
     */
    public final static String WHETHER_ANSWER = "y";

    // 是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】
    /**
     * 必须回答【IS_QUESTION=1】
     */
    public final static String MUST_ANSWER = "1";

}
