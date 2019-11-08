var selectInItemTree,
    selectInItemKey,
    selectInItemNodeList = [],
    selectInItemLastValue = "",
    selectInItemLoadData;

//右边行政区划树 配置setting信息
var inItemTreeSetting = {

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
        onClick: onClickInItemTreeNode,
        onDblClick: onDblClickInItemTreeNode

    }
};

$(function(){

    // 关键字搜索事项节点
    selectInItemKey = $("#selectInItemKeyWord");

    selectInItemKey.bind("focus", focusSelectInItemKey)
        .bind("blur", blurSelectInItemKey)
        .bind("change cut input propertychange",searchSelectInItemNode);

    selectInItemKey.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectInItemNode();
        }
    });

    $('#inItemDiv').niceScroll({

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

function focusSelectInItemKey(e) {

    if (selectInItemKey.hasClass("empty")) {
        selectInItemKey.removeClass("empty");
    }
}

function blurSelectInItemKey(e) {

    if (selectInItemKey.get(0).value === "") {
        selectInItemKey.addClass("empty");
    }
    searchSelectInItemNode(e);
}

//全部展开
function expandSelectInItemAllNode(){

    selectInItemTree.expandAll(true);
}

//全部折叠
function collapseSelectInItemAllNode(){

    selectInItemTree.expandAll(false);
}

// 搜索节点
function searchSelectInItemNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectInItemKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectInItemLastValue === value) {
        return;
    }

    // 保存最后一次
    selectInItemLastValue = value;

    var nodes = selectInItemTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectInItemAllNode(nodes);
        return;
    }
    hideSelectInItemAllNode(nodes);
    selectInItemNodeList = selectInItemTree.getNodesByParamFuzzy(keyType, value);
    updateSelectInItemNodes(selectInItemNodeList);
}

// 清空查询
function clearSearchSelectInItemNode(){

    // 清空查询内容
    $('#selectInItemKeyWord').val('');
    showSelectInItemAllNode(selectInItemTree.getNodes());
}

// 刷新
function refreshSelectInItemAllNode(){

    $.ajax({
        type: "get",
        url: ctx + '/aea/share/mat/listInItemTreeByThemeVerId.do',
        async: false,
        data: {'themeVerId': curThemeVerId},
        success: function (data) {
            selectInItemLoadData = data;
            if (data && data.length > 0) {
                selectInItemTree = $.fn.zTree.init($("#inItemTree"), inItemTreeSetting, data);
            }
        }
    });
}

//隐藏所有节点
function hideSelectInItemAllNode(nodes){

    nodes = selectInItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectInItemTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectInItemAllNode(nodes){

    nodes = selectInItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectInItemTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectInItemTree.expandNode(nodes[i],true,true,false,false);
        }
        selectInItemTree.showNode(nodes[i]);
        showSelectInItemAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectInItemNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectInItemTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectInItemTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectInItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectInItemTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectInItemTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectInItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

/**
 * 打开行政区划树
 */
function selectInItemZtree(){

    $('#select_in_item_ztree_modal').modal('show');
    $('#select_in_item_ztree_modal_title').html('选择事项输入');
    $('#inItemDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showInItemTree();
}

/**
 * 关闭行政区划树
 */
function closeSelectInItemZtree() {

    $('#select_in_item_ztree_modal').modal('hide');
}

//加载选择行政区划树
function showInItemTree() {

    if(selectInItemLoadData){
        selectInItemTree = $.fn.zTree.init($("#inItemTree"), inItemTreeSetting, selectInItemLoadData);
    }else{
        $.ajax({
            type: "get",
            url: ctx + '/aea/share/mat/listInItemTreeByThemeVerId.do',
            async: false,
            data: {'themeVerId': curThemeVerId},
            success: function (data) {
                selectInItemLoadData = data;
                if (data && data.length > 0) {
                    selectInItemTree = $.fn.zTree.init($("#inItemTree"), inItemTreeSetting, data);
                }
            }
        });
    }
}

function onClickInItemTreeNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.type=='root'){
        treeObj.cancelSelectedNode(treeNode);
        return;
    }
}

function onDblClickInItemTreeNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.type=='root'){
        treeObj.cancelSelectedNode(treeNode);
        return;
    }
    $("#saveInItemBtn").trigger("click");
}