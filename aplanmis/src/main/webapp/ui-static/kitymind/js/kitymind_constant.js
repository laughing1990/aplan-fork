/*mind的节点类型*/
//mind的节点类型-阶段
var AeaMindConst_MIND_NODE_TYPE_CODE_STAGE ="stage";
//mind的节点类型-事项
var AeaMindConst_MIND_NODE_TYPE_CODE_ITEM ="item";
//mind的节点类型-情形
var AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION ="situation";
//mind的节点类型-材料
var AeaMindConst_MIND_NODE_TYPE_CODE_MAT ="mat";
//mind的节点类型-证照
var AeaMindConst_MIND_NODE_TYPE_CODE_CERT ="cert";
//mind的节点类型-条件
var AeaMindConst_MIND_NODE_TYPE_CODE_CONDTION ="condition";

//mind节点的PRIORITY属性-解析为-材料
var AeaMindConst_MIND_NODE_PRIORITY_MAPPING_MAT ="3";

//mind节点的PRIORITY属性-解析为-电子证照
var AeaMindConst_MIND_NODE_PRIORITY_MAPPING_CERT ="4";


//mind节点的PROGRESS属性-解析为-必选
var AeaMindConst_MIND_NODE_PROGRESS_MAPPING_REQUIRED ="9";

//mind节点的PRIORITY属性-解析为-多选
var AeaMindConst_MIND_NODE_PRIORITY_MAPPING_REQUIRED ="2";

/*mind的节点操作*/
//新增节点的id前缀
var MindConst_MIND_NODE_NEW_ID_PREFIX="new_";
//节点操作标记_新增
var MindConst_MIND_NODE_OPERATOR_TAG_NEW="new";

//配合currentBusiScene使用
//业务场景-配置情形(阶段或事项)
var BUSI_SCENE_CONFIG_SITUATION='configSituation';
//业务场景-配置前置条件
var BUSI_SCENE_CONFIG_PRECONDITION='configPreCondition';

function getNodeText(name,bindDisplayJson) {

    var result=name;
    if(bindDisplayJson!=null){
        // result=name+"\n["+bindDisplayJson+"]";
    }
    return result;
}

//工具栏按钮状态
//可接受的节点类型codes
function getToolbarBtnState(acceptnodeTypeCodes) {

    var result = '';
    result += ' ng-disabled="minder.queryCommandState(\'nodeSelectModCmd\').data.nodeTypeCode != \''+ acceptnodeTypeCodes +'\'"';
    result += ' ng-class="{ deactive: minder.queryCommandState(\'nodeSelectModCmd\').data.nodeTypeCode != \''+ acceptnodeTypeCodes +'\' }"';
    return result;
}

function checkIsOk(value, codes){

    if(codes.indexOf(value)>-1){
       return true;
    }else{
        return false;
    }
}
