// 选择事项树配置
var selectItemKey,
    selectItemNodeList = [],
    selectItemLastValue = "",
    selectItemTree = null,
    selectItemNode = null,
    itemVerId_id,
    itemName_id,
    currentFrontItemId_id,
    lastFrontItemId,
    selectItemTreeSetting = {
        edit: {
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "all"
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
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            beforeCheck: beforeSelectItemCheck,
            onCheck: onSelectItemCheck,
            beforeClick: beforeSelectItemClick,
            onClick: onClickSelectItemNode
        }
    };

function beforeSelectItemCheck(treeId, treeNode) {

    if(curIsEditable) {
        return true;
    }else {
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
        return false;
    }
};

function beforeSelectItemClick(treeId, treeNode, clickFlag) {

    if(curIsEditable) {
        return true;
    }else {
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
        return false;
    }
};


/**
 * onCheck事件响应函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function onSelectItemCheck(event, treeId, treeNode){

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode&&treeNode.type=='item'){
        // 选中
        if(treeNode.checked){
            treeObj.selectNode(treeNode);
            selectItemNode = treeNode;
        }else{
            treeObj.cancelSelectedNode(treeNode);
            selectItemNode = null;
        }
    }
}

function onClickSelectItemNode(event, treeId, treeNode) {
    onSelectItemCheck(event,treeId,treeNode);
}

function expandAll() {

    var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
    zTree.expandAll(true);

}
function expandNodes(nodes) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
    for (var i=0, l=nodes.length; i<l; i++) {
        zTree.expandNode(nodes[i], true, false, false);
        if (nodes[i].isParent && nodes[i].zAsync) {
            expandNodes(nodes[i].children);
        }
    }
}

// 初始化加载函数
$(function(){

    // 初始化高度
    $('#selectItemTree').css('height', $('#westPanel').height()-116);

    $('#selectItemTree').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 关键字搜索事项节点
    selectItemKey = $("#selectItemKeyWord");

    selectItemKey.bind("focus", focusSelectItemKey)
        .bind("blur", blurSelectItemKey)
        .bind("change cut input propertychange",searchSelectItemNode);

    selectItemKey.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectItemNode();
        }
    });
});


function focusSelectItemKey(e) {

    if (selectItemKey.hasClass("empty")) {
        selectItemKey.removeClass("empty");
    }
}

function blurSelectItemKey(e) {

    if (selectItemKey.get(0).value === "") {
        selectItemKey.addClass("empty");
    }
    searchSelectItemNode(e);
}

//全部展开
function expandSelectItemAllNode(){
    expandAll();
}

//全部折叠
function collapseSelectItemAllNode(){
    selectItemTree.expandAll(false);
}

// 搜索节点
function searchSelectItemNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectItemKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectItemLastValue === value) {
        return;
    }

    // 保存最后一次
    selectItemLastValue = value;

    var nodes = selectItemTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectItemAllNode(nodes);
        return;
    }
    hideSelectItemAllNode(nodes);
    selectItemNodeList = selectItemTree.getNodesByParamFuzzy(keyType, value);
    updateSelectItemNodes(selectItemNodeList);
}

// 清空查询
function clearSearchSelectItemNode(){

    // 清空查询内容
    $('#selectItemKeyWord').val('');
    showSelectItemAllNode(selectItemTree.getNodes());
}

//隐藏所有节点
function hideSelectItemAllNode(nodes){

    nodes = selectItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectItemTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectItemAllNode(nodes){

    nodes = selectItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectItemTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectItemTree.expandNode(nodes[i],true,true,false,false);
        }
        selectItemTree.showNode(nodes[i]);
        showSelectItemAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectItemNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectItemTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectItemTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectItemTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectItemTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载选择事项数据
function initSelectItemZtree(){

    $("#uploadProgressMsg").html("查询事项库数据中，请稍后...");
    $("#uploadProgress").modal("show");

    var frontItemId = null;
    if(currentFrontItemId_id){
        frontItemId = $("#"+currentFrontItemId_id).val();
    }

    $.ajax({
        url: ctx+'/aea/item/getUnSelectedParFrontItemTree.do',
        type:'post',
        data:{stageId:currentBusiId,frontItemId:frontItemId},
        async: false,
        dataType: 'json',
        success: function(data){

            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                if(data!=null&&data.length>0){
                    if(selectItemTree)selectItemTree.destroy();
                    selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,data);
                    selectItemTree.checkAllNodes(false);
                    if(itemVerId_id){
                        var itemVerId = $("#"+itemVerId_id).val();
                        if(itemVerId){
                            selectItemNode = selectItemTree.getNodeByParam("itemVerId", itemVerId, null);
                            if (selectItemNode) {
                                selectItemTree.checkNode(selectItemNode, true, true, false);
                            }
                        }
                    }
                    $("#select_front_item_modal").modal("show");
                }else{
                    swal('错误信息', '没有可供选择的事项!', 'error');
                }

            },500);
        },
        error: function(){
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', '初始化事项数据异常!', 'error');
            },500);
        }
    });
}

function openSelectFrontItemZtree(valueId,textId,frontItemId) {
    if(frontItemId){
        var tmp = $("#"+frontItemId).val();
        if(lastFrontItemId){
            if(lastFrontItemId == tmp && selectItemTree){
                showLastSelected();
                return;
            }
        }else{
            if(!tmp && selectItemTree){
                showLastSelected();
                return;
            }
        }

        lastFrontItemId = tmp;

    }else{
        lastFrontItemId = null;
    }

    itemVerId_id = valueId;
    itemName_id = textId;
    currentFrontItemId_id = frontItemId;
    selectItemNodeList = [];
    selectItemLastValue = "";
    selectItemNode = null;
    $('#selectItemKeyWord').val('');
    initSelectItemZtree();
}

function selectFrontItem() {
    if(!selectItemNode){
        swal('错误信息', '没有勾选事项!', 'error');
    }else{
        if(itemVerId_id){
            $("#"+itemVerId_id).val(selectItemNode.itemVerId);
        }
        if(itemName_id){
            $("#"+itemName_id).val(selectItemNode.itemName);
        }

        $("#select_front_item_modal").modal("hide");
    }
}

function showLastSelected() {
    if(itemVerId_id) {
        var itemVerId = $("#" + itemVerId_id).val();
        if(itemVerId) {
            selectItemNode = selectItemTree.getNodeByParam("itemVerId", itemVerId, null);
            if (selectItemNode) {
                selectItemTree.checkNode(selectItemNode, true, true, false);
            }
        }
    }
    $("#select_front_item_modal").modal("show");
}