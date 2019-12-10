// 选择用户树配置
var selectUserKey,
    selectUserNodeList = [],
    selectUserLastValue = "",
    selectUserTree = null,
    curSelectedUserTreeNode = null,
    selectUserTreeSetting = {
        edit: {
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
            selectedMulti: true,//设置是否允许同时选中多个节点
            showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: true, //设置 zTree 是否显示节点之间的连线。
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {

            onClick: onClickSelectUserNode,
        }
    };

function onClickSelectUserNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if (treeNode && treeNode.type == 'user') {
        treeObj.checkAllNodes(false);
        if (treeNode.checked) {
            treeObj.checkNode(treeNode, false, false, true);
        }else{
            treeObj.checkNode(treeNode, true, false, true);
        }
        curSelectedUserTreeNode = treeNode;
        searchAllItemList();
    }else{
        treeObj.cancelSelectedNode(treeNode);
        if(curSelectedUserTreeNode){
            var node = treeObj.getNodeByParam("id", curSelectedUserTreeNode.id, null);
            treeObj.selectNode(node);
            curSelectedUserTreeNode = node;
        }
    }
}

// 初始化加载函数
$(function(){

    // 关键字搜索用户节点
    selectUserKey = $("#selectUserKeyWord");

    selectUserKey.bind("focus", focusSelectUserKey)
        .bind("blur", blurSelectUserKey)
        .bind("change cut input propertychange",searchSelectUserNode);

    selectUserKey.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectUserNode();
        }
    });
});

function focusSelectUserKey(e) {

    if (selectUserKey.hasClass("empty")) {
        selectUserKey.removeClass("empty");
    }
}

function blurSelectUserKey(e) {

    if (selectUserKey.get(0).value === "") {
        selectUserKey.addClass("empty");
    }
    searchSelectUserNode(e);
}

function expandNodes(nodes) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj("selectUserTree");
    for (var i=0;i<nodes.length;i++) {
        zTree.expandNode(nodes[i], true, false, false);
        if (nodes[i].isParent) {
            expandNodes(nodes[i].children);//递归
        }
    }
}

//全部展开
function expandSelectUserAllNode(){

    selectUserTree.expandAll(true);
}

//全部折叠
function collapseSelectUserAllNode(){

    selectUserTree.expandAll(false);
}

// 搜索节点
function searchSelectUserNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectUserKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectUserLastValue === value) {
        return;
    }

    // 保存最后一次
    selectUserLastValue = value;

    var nodes = selectUserTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectUserAllNode(nodes);
        return;
    }
    hideSelectUserAllNode(nodes);
    selectUserNodeList = selectUserTree.getNodesByParamFuzzy(keyType, value);
    updateSelectUserNodes(selectUserNodeList);
}

// 清空查询
function clearSearchSelectUserNode(){

    // 清空查询内容
    $('#selectUserKeyWord').val('');
    showSelectUserAllNode(selectUserTree.getNodes());
}

//隐藏所有节点
function hideSelectUserAllNode(nodes){

    nodes = selectUserTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectUserTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectUserAllNode(nodes){

    nodes = selectUserTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectUserTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectUserTree.expandNode(nodes[i],true,true,false,false);
        }
        selectUserTree.showNode(nodes[i]);
        showSelectUserAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectUserNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectUserTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectUserTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectUserTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectUserTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载选择用户数据
function initSelectUserZtree(){

    $.ajax({
        url: ctx+'/aea/item/user/gtreeAllUserRelOrgByOrgId.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectUserTree)selectUserTree.destroy();
                selectUserTree = $.fn.zTree.init($("#selectUserTree"), selectUserTreeSetting, data);
                // 默认选择第一个用户节点
                if(data.length>1){
                    curSelectedUserTreeNode = data[1];
                    var node = selectUserTree.getNodeByParam("id", data[1].id, null);
                    if(node!=null){
                        selectUserTree.selectNode(node);
                    }
                }else{
                    curSelectedUserTreeNode = null;
                }
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}
