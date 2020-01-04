package com.augurit.aplanmis.common.constants;

/**
 * @author hzl
 * @date 2019/4/16
 *
 * 思维导图的常量类
 */
public class AeaMindConst {

    //mind的节点类型-阶段
    public static final String MIND_NODE_TYPE_CODE_STAGE ="stage";
    //mind的节点类型-事项
    public static final String MIND_NODE_TYPE_CODE_ITEM ="item";
    //mind的节点类型-情形
    public static final String MIND_NODE_TYPE_CODE_SITUATION ="situation";
    //mind的节点类型-材料
    public static final String MIND_NODE_TYPE_CODE_MAT ="mat";
    //mind的节点类型-证照
    public static final String MIND_NODE_TYPE_CODE_CERT ="cert";
    //mind的节点类型-情形关联事项
    public static final String MIND_NODE_TYPE_CODE_SITUATION_LINK_ITEM = "situationLinkItem";
    //mind的节点类型-表单
    public static final String MIND_NODE_TYPE_CODE_FORM = "form";

    //mind的节点类型-条件
    public static final String MIND_NODE_TYPE_CODE_CONDTION ="condition";


    //mind节点的PRIORITY属性-解析为-材料
    public static final String MIND_NODE_PRIORITY_MAPPING_MAT ="3";
    //mind节点的PRIORITY属性-解析为-电子证照
    public static final String MIND_NODE_PRIORITY_MAPPING_CERT ="4";
    //mind节点的PRIORITY属性-解析为-情形关联事项
    public static final String MIND_NODE_PRIORITY_MAPPING_SITUATION_LINK_ITEM ="5";
    //mind节点的PRIORITY属性-解析为-表单
    public static final String MIND_NODE_PRIORITY_MAPPING_FORM ="6";

    //mind节点的PRIORITY属性-解析为-多选
    public static final String MIND_NODE_PRIORITY_MAPPING_REQUIRED ="2";
    //mind节点的PROGRESS属性-解析为-必选
    public static final String MIND_NODE_PROGRESS_MAPPING_REQUIRED ="9";

    //节点的extra属性(键值对)的key，用于"表单节点"，区分表单属性
    public static final String MIND_NODE_EXTRA_KEY_FORM_PROPERTY="formProperty";
    //节点的extra属性(键值对)的key，用于"材料 是否通用材料"
    public static final String MIND_NODE_EXTRA_KEY_MAT_IS_COMMON="isCommon";
}
