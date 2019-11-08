var selectOpsOmOrgTree;
//右边组织岗位用户树 配置setting信息
var opsOmOrgTreeSetting = {

    edit: {
        enable: false, //设置 zTree 是否处于编辑状态
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0
        }
    },
    view: {
        selectedMulti: false,//设置是否允许同时选中多个节点
        showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: true, //设置 zTree 是否显示节点之间的连线。
        showHorizontal:false//设置是否水平平铺树（自定义属性）

    },
    //用于捕获节点被点击的事件回调函数
    callback: {
        //双击事件
        onDblClick: onDblClickOrgTreeNode

    }
};

var opsOmOrgTreeSetting2 = {

    edit: {
        enable: false, //设置 zTree 是否处于编辑状态
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    check: {
        enable: true,
        chkStyle: "checkbox",
        chkboxType: { "Y": "", "N": "" }
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0
        }
    },
    view: {
        selectedMulti: true,//设置是否允许同时选中多个节点
        showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: true, //设置 zTree 是否显示节点之间的连线。
        showHorizontal:false//设置是否水平平铺树（自定义属性）

    },
    //用于捕获节点被点击的事件回调函数
    callback: {
        //单击事件
        onCheck: onSelectOrgCheck,
        onClick: onClickSelectOrgNode

    }
};

function onSelectOrgCheck(event, treeId, treeNode){

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.checked) {
        treeObj.selectNode(treeNode);
    }else{
        treeObj.cancelSelectedNode(treeNode);
    }
}

function onClickSelectOrgNode(event, treeId, treeNode){

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.checked) {
        treeObj.checkNode(treeNode, false, false, true);
    }else{
        treeObj.checkNode(treeNode, true, false, true);
    }
}

var itemBasicFieldId = null;
var itemBasicFieldName = null;

/**
 * 打开组织树
 */
function selectOpusOmOrgZtree(){

    var itemBasicFieldId = null;
    var itemBasicFieldName = null;

    $('#select_opus_om_org_ztree_modal').modal('show');
    $('#select_opus_om_org_ztree_modal_title').html('选择组织');
    $('#opusOmOrgDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showOpusOmOrgTree();
    //初始化模糊搜索方法
    fuzzySearch('opusOmOrgTree','#opusOmOrgZtreeKeyWord',null,true);
}

function selectOpusOmOrgZtree2(fieldId, fieldName){

    itemBasicFieldId = fieldId;
    itemBasicFieldName = fieldName;
    $('#select_opus_om_org_ztree_modal').modal('show');
    $('#select_opus_om_org_ztree_modal_title').html('选择组织');
    $('#opusOmOrgDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showOpusOmOrgTree2();
    //初始化模糊搜索方法
    fuzzySearch('opusOmOrgTree','#opusOmOrgZtreeKeyWord', null, true);
}

/**
 * 关闭组织树
 */
function closeSelectOpusOmOrgZtree() {

    $('#select_opus_om_org_ztree_modal').modal('hide');
    showOpusOmOrgTree();
}


//加载选择组织树
function showOpusOmOrgTree() {

    $.ajax({
        type: "get",
        url: ctx + '/opus/om/org/admin/getOpusOmOrgZtreeNode.do',
        async: false,
        data: {},
        success: function (data) {
            if (data && data.length > 0) {
                var zNodes = data;
                selectOpsOmOrgTree = $.fn.zTree.init($("#opusOmOrgTree"), opsOmOrgTreeSetting, zNodes);
            }
        }
    });
}

//加载选择组织树
function showOpusOmOrgTree2() {

    $.ajax({
        type: "get",
        url: ctx + '/opus/om/org/admin/getOpusOmOrgZtreeNode.do',
        async: false,
        data: {},
        success: function (data) {
            if (data && data.length > 0) {
                var zNodes = data;
                selectOpsOmOrgTree = $.fn.zTree.init($("#opusOmOrgTree"), opsOmOrgTreeSetting2, zNodes);
            }
        }
    });
}

// /**
//  * 组织岗位用户树单击事件
//  * @param event js event 对象 标准的 js event 对象
//  * @param treeId String对应 zTree 的 treeId，便于用户操控
//  * @param treeNode JSON 被点击的节点 JSON 数据对象
//  * @param clickFlag 节点被点击后的选中操作类型
//  */
// function onClickOrgTreeNode(event, treeId, treeNode, clickFlag) {
//    
//     alert("组织Id："+treeNode.orgId+"  ,组织名称："+treeNode.orgName);
// }

function onDblClickOrgTreeNode(event, treeId, treeNode) {

    $("#selectOrgBtn").trigger("click");
}

//折叠ztree树全部节点
function collapseOpusOmOrgZtreeAllNode(treeId) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.expandAll(false);
}

//展开异步ztree树全部节点
function expandOpusOmOrgZtreeAllNode(treeId) {

    var zTree = $.fn.zTree.getZTreeObj(treeId);
    expandNodes(zTree.getNodes(),treeId);
}


//清空搜索左树文字框
function clearOpusOmOrgZtreeForTree() {

    $("#opusOmOrgZtreeKeyWord").val("");
    // showOpusOmOrgTree("0368948a-1cdf-4bf8-a828-71d796ba89f6");
    showOpusOmOrgTree();
}

//展开nods下全部子孙节点
function expandNodes(nodes,treeId) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    for (var i=0, l=nodes.length; i<l; i++) {
        zTree.expandNode(nodes[i], true, false, false);
        //nodes[i].zAsync：记录 treeNode 节点是否已经进行过异步加载，避免父节点反复异步加载数据。
        if (nodes[i].isParent && nodes[i].zAsync) {
            expandNodes(nodes[i].children,treeId);
        }
    }
}



/**
 *  模糊查询ztree
 * @param zTreeId the ztree id used to get the ztree object
 * @param searchField selector of your input for fuzzy search
 * @param isHighLight whether highlight the match words, default true
 * @param isExpand whether to expand the node, default false
 *
 * @returns
 */
function fuzzySearch(zTreeId, searchField, isHighLight, isExpand){

    var zTreeObj = $.fn.zTree.getZTreeObj(zTreeId);//get the ztree object by ztree id
    if(!zTreeObj){
        alter("fail to get ztree object");
    }
    var nameKey = zTreeObj.setting.data.key.name; //get the key of the node name
    isHighLight = isHighLight===false?false:true;//default true, only use false to disable highlight
    isExpand = isExpand?true:false; // not to expand in default
    zTreeObj.setting.view.nameIsHTML = isHighLight; //allow use html in node name for highlight use

    var metaChar = '[\\[\\]\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)]'; //js meta characters
    var rexMeta = new RegExp(metaChar, 'gi');//regular expression to match meta characters

    // keywords filter function
    function ztreeFilter(zTreeObj,_keywords,callBackFunc) {
        if(!_keywords){
            _keywords =''; //default blank for _keywords
        }

        // function to find the matching node
        function filterFunc(node) {
            if(node && node.oldname && node.oldname.length>0){
                node[nameKey] = node.oldname; //recover oldname of the node if exist
            }
            zTreeObj.updateNode(node); //update node to for modifications take effect
            if (_keywords.length == 0) {
                //return true to show all nodes if the keyword is blank
                zTreeObj.showNode(node);
                zTreeObj.expandNode(node,isExpand);
                return true;
            }
            //transform node name and keywords to lowercase
            if (node[nameKey] && node[nameKey].toLowerCase().indexOf(_keywords.toLowerCase())!=-1) {
                if(isHighLight){ //highlight process
                    //a new variable 'newKeywords' created to store the keywords information
                    //keep the parameter '_keywords' as initial and it will be used in next node
                    //process the meta characters in _keywords thus the RegExp can be correctly used in str.replace
                    var newKeywords = _keywords.replace(rexMeta,function(matchStr){
                        //add escape character before meta characters
                        return '\\' + matchStr;
                    });
                    node.oldname = node[nameKey]; //store the old name
                    var rexGlobal = new RegExp(newKeywords, 'gi');//'g' for global,'i' for ignore case
                    //use replace(RegExp,replacement) since replace(/substr/g,replacement) cannot be used here
                    node[nameKey] = node.oldname.replace(rexGlobal, function(originalText){
                        //highlight the matching words in node name
                        var highLightText =
                            // '<span style="color:#1e83ef; font-weight: bolder;">'
                           '' + originalText;
                            // +'</span>';
                        return 	highLightText;
                    });
                    zTreeObj.updateNode(node); //update node for modifications take effect
                }
                zTreeObj.showNode(node);//show node with matching keywords
                return true; //return true and show this node
            }

            zTreeObj.hideNode(node); // hide node that not matched
            return false; //return false for node not matched
        }

        var nodesShow = zTreeObj.getNodesByFilter(filterFunc); //get all nodes that would be shown
        processShowNodes(nodesShow, _keywords);//nodes should be reprocessed to show correctly
    }

    /**
     * reprocess of nodes before showing
     */
    function processShowNodes(nodesShow,_keywords){
        if(nodesShow && nodesShow.length>0){
            //process the ancient nodes if _keywords is not blank
            if(_keywords.length>0){
                $.each(nodesShow, function(n,obj){
                    var pathOfOne = obj.getPath();//get all the ancient nodes including current node
                    if(pathOfOne && pathOfOne.length>0){
                        //i < pathOfOne.length-1 process every node in path except self
                        for(var i=0;i<pathOfOne.length-1;i++){
                            zTreeObj.showNode(pathOfOne[i]); //show node
                            zTreeObj.expandNode(pathOfOne[i],true); //expand node
                        }
                    }
                });
            }else{ //show all nodes when _keywords is blank and expand the root nodes
                var rootNodes = zTreeObj.getNodesByParam('level','0');//get all root nodes
                $.each(rootNodes,function(n,obj){
                    zTreeObj.expandNode(obj,true); //expand all root nodes
                });
            }
        }
    }

    //listen to change in input element
    $(searchField).bind('input propertychange', function() {
        var _keywords = $(this).val();
        searchNodeLazy(_keywords); //call lazy load
    });

    var timeoutId = null;
    // excute lazy load once after input change, the last pending task will be cancled
    function searchNodeLazy(_keywords) {
        if (timeoutId) {
            //clear pending task
            clearTimeout(timeoutId);
        }
        timeoutId = setTimeout(function() {
            ztreeFilter(zTreeObj,_keywords); //lazy load ztreeFilter function
            $(searchField).focus();//focus input field again after filtering
        }, 500);
    }
}