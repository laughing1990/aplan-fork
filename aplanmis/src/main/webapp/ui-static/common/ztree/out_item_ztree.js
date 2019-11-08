var selectOutItemTree,
    selectOutItemKey,
    selectOutItemNodeList = [],
    selectOutItemLastValue = "",
    selectOutItemLoadData;

//事项输出树配置setting信息
var outItemTreeSetting = {

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
        onClick: onClickOutItemTreeNode,
        onDblClick: onDblClickOutItemTreeNode
    }
};

$(function(){

    // 关键字搜索事项节点
    selectOutItemKey = $("#selectOutItemKeyWord");

    selectOutItemKey.bind("focus", focusSelectOutItemKey)
        .bind("blur", blurSelectOutItemKey)
        .bind("change cut input propertychange",searchSelectOutItemNode);

    selectOutItemKey.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectOutItemNode();
        }
    });

    $('#outItemDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });
});

function focusSelectOutItemKey(e) {

    if (selectOutItemKey.hasClass("empty")) {
        selectOutItemKey.removeClass("empty");
    }
}

function blurSelectOutItemKey(e) {

    if (selectOutItemKey.get(0).value === "") {
        selectOutItemKey.addClass("empty");
    }
    searchSelectOutItemNode(e);
}

//全部展开
function expandSelectOutItemAllNode(){

    selectOutItemTree.expandAll(true);
}

//全部折叠
function collapseSelectOutItemAllNode(){

    selectOutItemTree.expandAll(false);
}

// 搜索节点
function searchSelectOutItemNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectOutItemKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectOutItemLastValue === value) {
        return;
    }

    // 保存最后一次
    selectOutItemLastValue = value;

    var nodes = selectOutItemTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectOutItemAllNode(nodes);
        return;
    }
    hideSelectOutItemAllNode(nodes);
    selectOutItemNodeList = selectOutItemTree.getNodesByParamFuzzy(keyType, value);
    updateSelectOutItemNodes(selectOutItemNodeList);
}

// 清空查询
function clearSearchSelectOutItemNode(){

    // 清空查询内容
    $('#selectOutItemKeyWord').val('');
    showSelectOutItemAllNode(selectOutItemTree.getNodes());
}

// 刷新
function refreshSelectOutItemAllNode() {

    $.ajax({
        type: "get",
        url: ctx + '/aea/share/mat/listOutItemTreeByThemeVerId',
        async: false,
        data: {'themeVerId':curThemeVerId},
        success: function (data) {
            selectOutItemLoadData = data;
            if (data && data.length > 0) {
                selectOutItemTree = $.fn.zTree.init($("#outItemTree"), outItemTreeSetting, data);
            }
        }
    });
}

//隐藏所有节点
function hideSelectOutItemAllNode(nodes){

    nodes = selectOutItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectOutItemTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectOutItemAllNode(nodes){

    nodes = selectOutItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectOutItemTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectOutItemTree.expandNode(nodes[i],true,true,false,false);
        }
        selectOutItemTree.showNode(nodes[i]);
        showSelectOutItemAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectOutItemNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectOutItemTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectOutItemTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectOutItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectOutItemTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectOutItemTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectOutItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

/**
 * 打开事项输出树
 */
function selectOutItemZtree(){

    $('#select_out_item_ztree_modal').modal('show');
    $('#select_out_item_ztree_modal_title').html('选择事项输出');
    $('#outItemDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showOutItemTree();
}

/**
 * 关闭事项输出树
 */
function closeSelectOutItemZtree() {

    $('#select_out_item_ztree_modal').modal('hide');
}

//加载选择事项输出树
function showOutItemTree() {

    if(selectOutItemLoadData){
        selectOutItemTree = $.fn.zTree.init($("#outItemTree"), outItemTreeSetting, selectOutItemLoadData);
    }else{
        $.ajax({
            type: "get",
            url: ctx + '/aea/share/mat/listOutItemTreeByThemeVerId',
            async: false,
            data: {'themeVerId':curThemeVerId},
            success: function (data) {
                selectOutItemLoadData = data;
                if (data && data.length > 0) {
                    selectOutItemTree = $.fn.zTree.init($("#outItemTree"), outItemTreeSetting, data);
                }
            }
        });
    }
}

function onClickOutItemTreeNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.type=='root'){
        treeObj.cancelSelectedNode(treeNode);
        return;
    }
}

function onDblClickOutItemTreeNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.type=='root'){
        treeObj.cancelSelectedNode(treeNode);
        return;
    }
    $("#saveOutItemBtn").trigger("click");
}

