// 选择事项树配置
var selectItemKey,
    selectItemNodeList = [],
    selectItemLastValue = "",
    selectMultiplyItemTree = null,
    spanChecked = "0",
    selectMultiplyItemTreeSetting = {
        edit: {
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
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        // async: {
        //     //设置 zTree 是否开启异步加载模式
        //     enable: false,
        //     autoParam: ["id","name","type"],
        //     url:ctx+"/aea/item/gtreeItemAsyncZTree.do"
        // },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onCheck: onSelectItemCheck,
            onClick: onClickSelectItemNode,
            //onAsyncSuccess: onAsyncSuccessSelectItemNode
            // beforeAsync: beforeAsync,
            // onAsyncSuccess: onAsyncSuccess,
            // onAsyncError: onAsyncError
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
    if(spanChecked=='0'){
        if(treeNode&&treeNode.type=='item'){
            // 选中
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectItemLi" category-id="'+treeNode.itemId + '*' +treeNode.itemVerId+'">' +
                    '<span class="drag-handle_td" onclick="removeSelectedItem(\''+treeNode.itemId+'\');">×</span>' +
                    '<span class="org_name_td">'+ treeNode.name +'</span>' +
                    '</li>';
                var i=0;
                $('li[name="selectItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if(categoryId == treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedItemUl').append(liHtml);
                }
                // 取消
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if(categoryId == treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }else if(spanChecked=='1'){
        if(treeNode&&treeNode.type=='item'){
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectNotNeedItemLi" category-id="'+treeNode.itemId + '*' +treeNode.itemVerId+'">' +
                    '<span class="drag-handle_td" onclick="removeNoNeedSelectedItem(\''+treeNode.itemId+'\');">×</span>' +
                    '<span class="org_name_td">'+treeNode.name+'</span>' +
                    '</li>';
                var i=0;
                $('li[name="selectNotNeedItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if(categoryId==treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedNotNeedItemUl').append(liHtml);
                }
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectNotNeedItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var indexOfEnd=categoryId.indexOf("*");
                    categoryId=categoryId.substring(0,indexOfEnd);
                    if(categoryId==treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }else if(spanChecked=='2'){
        if(treeNode&&treeNode.type=='item'){
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectFrontCheckItemLi" category-id="'+treeNode.itemId + '*' +treeNode.itemVerId+'">' +
                    '<span class="drag-handle_td" onclick="removeFrontCheckSelectedItem(\''+treeNode.itemId+'\');">×</span>' +
                    '<span class="org_name_td">'+treeNode.name+'</span>' +
                    '</li>';
                var i=0;
                $('li[name="selectFrontCheckItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if(categoryId==treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedFrontCheckItemUl').append(liHtml);
                }
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectFrontCheckItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var indexOfEnd=categoryId.indexOf("*");
                    categoryId=categoryId.substring(0,indexOfEnd);
                    if(categoryId==treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }
}

function onClickSelectItemNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    // 必选事项
    if (spanChecked == '0') {
        if (treeNode && treeNode.type == 'item') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectItemLi" category-id="' + treeNode.itemId + '*' + treeNode.itemVerId + '">' +
                    '<span class="drag-handle_td" onclick="removeSelectedItem(\'' + treeNode.itemId + '\');">×</span>' +
                    '<span class="org_name_td">' + treeNode.name+ '</span>' +
                    '</li>';
                var i = 0;
                $('li[name="selectItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedItemUl').append(liHtml);
                }
            }
        }
        // 可选事项
    } else if(spanChecked=='1') {
        if (treeNode && treeNode.type == 'item') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectNotNeedItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectNotNeedItemLi" category-id="' + treeNode.itemId + '*' + treeNode.itemVerId + '">' +
                    '<span class="drag-handle_td" onclick="removeNoNeedSelectedItem(\'' + treeNode.itemId + '\');">×</span>' +
                    '<span class="org_name_td">' + treeNode.name + '</span>' +
                    '</li>';
                var i = 0;
                $('li[name="selectNotNeedItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedNotNeedItemUl').append(liHtml);
                }
            }
        }
    }else if(spanChecked=='2'){
        if (treeNode && treeNode.type == 'item') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectFrontCheckItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectFrontCheckItemLi" category-id="' + treeNode.itemId + '*' + treeNode.itemVerId + '">' +
                    '<span class="drag-handle_td" onclick="removeFrontCheckSelectedItem(\'' + treeNode.itemId + '\');">×</span>' +
                    '<span class="org_name_td">' + treeNode.name + '</span>' +
                    '</li>';
                var i = 0;
                $('li[name="selectFrontCheckItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedFrontCheckItemUl').append(liHtml);
                }
            }
        }
    }
}

function expandNodes(nodes) {

    if (!nodes) return;
    curStatus = "expand";
    var zTree = $.fn.zTree.getZTreeObj("selectMultiplyItemTree");
    for (var i=0, l=nodes.length; i<l; i++) {
        zTree.expandNode(nodes[i], true, false, false);
        if (nodes[i].isParent && nodes[i].zAsync) {
            expandNodes(nodes[i].children);
        } else {
            goAsync = true;
        }
    }
}

//处理异步加载子节点 end


// 初始化加载函数
$(function(){

    // 初始化高度
    $('#selectMultiplyItemTree').css('height', 500);

    $('#selectMultiplyItemTree').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    initSelectItemZtree();

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

function isOptionCheckItem(flag) {

    //必选事项
    if(flag=='0'){

        spanChecked = flag;
        initSelectItemZtree();
        // 加载必选事项数据
        setStageRelItems();

        // 可选情形
    }else if(flag=='1'){

        spanChecked = flag;
        initSelectItemZtree();

        // 加载可选事项数据
        setStageRelNotNeedItems();

        // 前置检查事项
    }else if(flag=='2'){

        spanChecked = flag;
        initSelectItemZtree();
        // 加载可选事项数据
        setStageRelFrontCheckItems();
    }
}


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

// 删除必选事项选中节点
function removeSelectedItem(itemId){

    if(curIsEditable){
        $('li[name="selectItemLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            var indexOfEnd=categoryId.indexOf("*");
            categoryId=categoryId.substring(0,indexOfEnd);
            if(categoryId==itemId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectMultiplyItemTree.cancelSelectedNode();
        var node = selectMultiplyItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectMultiplyItemTree.checkNode(node, false, true, false);
        }
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// 删除可选事项选中节点
function removeNoNeedSelectedItem(itemId){

    if(curIsEditable){
        $('li[name="selectNotNeedItemLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            var indexOfEnd=categoryId.indexOf("*");
            categoryId=categoryId.substring(0,indexOfEnd);
            if(categoryId==itemId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectMultiplyItemTree.cancelSelectedNode();
        var node = selectMultiplyItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectMultiplyItemTree.checkNode(node, false, true, false);
        }
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// 删除前置检查事项选中节点
function removeFrontCheckSelectedItem(itemId){

    if(curIsEditable){
        $('li[name="selectFrontCheckItemLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            var indexOfEnd=categoryId.indexOf("*");
            categoryId=categoryId.substring(0,indexOfEnd);
            if(categoryId==itemId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectMultiplyItemTree.cancelSelectedNode();
        var node = selectMultiplyItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectMultiplyItemTree.checkNode(node, false, true, false);
        }
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

function onDblClickSelectItemNode(event, treeId, treeNode) {

    $("#selectItemBtn").trigger("click");
}

//全部展开
function expandSelectItemAllNode(){
    selectMultiplyItemTree.expandAll(true);
}

//全部折叠
function collapseSelectMultiplyNode(){
    selectMultiplyItemTree.expandAll(false);
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

    var nodes = selectMultiplyItemTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectItemAllNode(nodes);
        return;
    }
    hideSelectItemAllNode(nodes);
    selectItemNodeList = selectMultiplyItemTree.getNodesByParamFuzzy(keyType, value);
    updateSelectItemNodes(selectItemNodeList);
}

// 清空查询
function clearSearchSelectItemNode(){

    // 清空查询内容
    $('#selectItemKeyWord').val('');
    showSelectItemAllNode(selectMultiplyItemTree.getNodes());
}

//隐藏所有节点
function hideSelectItemAllNode(nodes){

    nodes = selectMultiplyItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectMultiplyItemTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectItemAllNode(nodes){

    nodes = selectMultiplyItemTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectMultiplyItemTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectMultiplyItemTree.expandNode(nodes[i],true,true,false,false);
        }
        selectMultiplyItemTree.showNode(nodes[i]);
        showSelectItemAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectItemNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectMultiplyItemTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectMultiplyItemTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectMultiplyItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectMultiplyItemTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectMultiplyItemTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectMultiplyItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 打开选择事项弹窗
function openSelectItemModal(){

    // $('#select_item_ztree_modal').modal('show');
    // $('#select_item_ztree_modal_title').html('选择事项');
    initSelectItemZtree();
}

// 关闭选择事项弹窗
function closeSelectItemZtree(){

    $('#select_item_ztree_modal').modal('hide');
}

// 加载选择事项数据
function initSelectItemZtree(){

    // selectMultiplyItemTree = $.fn.zTree.init($("#selectMultiplyItemTree"), selectMultiplyItemTreeSetting);
    $.ajax({
        url: ctx+'/aea/item/priv/getTreeByAeaItemBasicList.do',
        type:'post',
        // data:{'itemNature': itemNature},
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectMultiplyItemTree)selectMultiplyItemTree.destroy();
                selectMultiplyItemTree = $.fn.zTree.init($("#selectMultiplyItemTree"), selectMultiplyItemTreeSetting,data);
            }
        },
        error: function(){
            swal('错误信息', '初始化事项数据异常!', 'error');
        }
    });
}

// 需要设置事阶段id
function setStageRelItems(){

    // 清空关键字据
    $('#selectItemKeyWord').val('');

    // 清空右侧选中事项数据
    $("#selectedItemUl").html("");

}

//刷新可选事项
function setStageRelNotNeedItems() {

    // 清空关键字据
    $('#selectItemKeyWord').val('');

    // 清空右侧选中事项数据
    $("#selectedNotNeedItemUl").html("");

    // 打开弹窗，加载树数据
    // openSelectItemModal();

    // 取消上次的选中节点

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        // loadNotNeedSelectedItemData();

        //延迟加载
        // setTimeout(function(){loadSelectedItemData();},1000);

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
}

//刷新前置检查事项
function setStageRelFrontCheckItems() {

    // 清空关键字据
    $('#selectItemKeyWord').val('');

    // 清空右侧选中事项数据
    $("#selectedFrontCheckItemUl").html("");

    // 打开弹窗，加载树数据
    // openSelectItemModal();

    // 取消上次的选中节点

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        loadFrontCheckSelectedItemData();

        //延迟加载
        // setTimeout(function(){loadSelectedItemData();},1000);

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
}

//获取区域树

var selectMultRegionTree;
//右边组织岗位用户树 配置setting信息
var multRegionTreeSetting = {

    edit: {
        enable: false, //设置 zTree 是否处于编辑状态
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "regionId",
            pIdKey: "parentRegionId",
            rootPId: 0
        },
        key:{
            name:"regionName",
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
        onDblClick: onDblClickMultRegionTreeNode

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
function selectMultRegionZtree(){

    var itemBasicFieldId = null;
    var itemBasicFieldName = null;

    $('#select_mult_region_ztree_modal').modal('show');
    $('#select_mult_region_ztree_modal_title').html('选择行政区域');
    $('#regionDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showMultRegionTree();
    //初始化模糊搜索方法
    multRegionFuzzySearch('multRegionTree','#multRegionZtreeKeyWord',null,true);
}



/**
 * 关闭组织树
 */
function closeSelectMultRegionZtree() {

    $('#select_mult_region_ztree_modal').modal('hide');
    showMultRegionTree();
}


//加载选择组织树
function showMultRegionTree() {

    $.ajax({
        type: "get",
        url: ctx + '/aea/item/priv/getBscDicRegionList.do',
        async: false,
        data: {},
        success: function (data) {
            if (data && data.length > 0) {
                var zNodes = data;
                selectMultRegionTree = $.fn.zTree.init($("#multRegionTree"), multRegionTreeSetting, zNodes);
                var treeObj = $.fn.zTree.getZTreeObj("multRegionTree");
                var nodes = treeObj.getNodes();
                for (var i = 0; i < nodes.length; i++) { //设置节点展开
                    treeObj.expandNode(nodes[i], true, false, true);
                }
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

function onDblClickMultRegionTreeNode(event, treeId, treeNode) {

    $("#selectMultRegionBtn").trigger("click");
}

//折叠ztree树全部节点
function collapseMultRegionZtreeAllNode(treeId) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.expandAll(false);
}

//展开异步ztree树全部节点
function expandMultRegionZtreeAllNode(treeId) {

    var zTree = $.fn.zTree.getZTreeObj(treeId);
    expandRegionNodes(zTree.getNodes(),treeId);
}


//清空搜索左树文字框
function clearMultRegionZtreeForTree() {

    $("#multRegionZtreeKeyWord").val("");
    // showOpusOmOrgTree("0368948a-1cdf-4bf8-a828-71d796ba89f6");
    showMultRegionTree();
}

//展开nods下全部子孙节点
function expandMultRegionNodes(nodes,treeId) {

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
function multRegionFuzzySearch(zTreeId, searchField, isHighLight, isExpand){

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

var selectMultPrivOpsOmOrgTree;
//右边组织岗位用户树 配置setting信息
var multPrivOpsOmOrgTreeSetting = {

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
        onDblClick: onDblClickMultPrivOrgTreeNode

    }
};



function onSelectMultOrgCheck(event, treeId, treeNode){

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode.checked) {
        treeObj.selectNode(treeNode);
    }else{
        treeObj.cancelSelectedNode(treeNode);
    }
}

function onClickSelectMultOrgNode(event, treeId, treeNode){

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
function selectMultPrivOpusOmOrgZtree(){

    var itemBasicFieldId = null;
    var itemBasicFieldName = null;

    $('#select_mult_priv_opus_om_org_ztree_modal').modal('show');
    $('#select_mult_priv_opus_om_org_ztree_modal_title').html('选择组织');
    $('#multPrivOpusOmOrgDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showMultPrivOpusOmOrgTree();
    //初始化模糊搜索方法
    multPrivFuzzySearch('multPrivOpusOmOrgTree','#multPrivOpusOmOrgZtreeKeyWord',null,true);
}



/**
 * 关闭组织树
 */
function closeSelectMultPrivOpusOmOrgZtree() {

    $('#select_mult_priv_opus_om_org_ztree_modal').modal('hide');
    showMultPrivOpusOmOrgTree();
}


//加载选择组织树
function showMultPrivOpusOmOrgTree() {

    $.ajax({
        type: "get",
        url: ctx + '/opus/om/org/admin/getOpusOmOrgZtreeNode.do',
        async: false,
        data: {},
        success: function (data) {
            if (data && data.length > 0) {
                var zNodes = data;
                selectMultPrivOpsOmOrgTree = $.fn.zTree.init($("#multPrivOpusOmOrgTree"), multPrivOpsOmOrgTreeSetting, zNodes);
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

function onDblClickMultPrivOrgTreeNode(event, treeId, treeNode) {

    $("#selectMultPrivOrgBtn").trigger("click");
}

//折叠ztree树全部节点
function collapseMultPrivOpusOmOrgZtreeAllNode(treeId) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.expandAll(false);
}

//展开异步ztree树全部节点
function expandMultPrivOpusOmOrgZtreeAllNode(treeId) {

    var zTree = $.fn.zTree.getZTreeObj(treeId);
    expandMultPrivNodes(zTree.getNodes(),treeId);
}


//清空搜索左树文字框
function clearMultPrivOpusOmOrgZtreeForTree() {

    $("#multPrivOpusOmOrgZtreeKeyWord").val("");
    // showOpusOmOrgTree("0368948a-1cdf-4bf8-a828-71d796ba89f6");
    showMultPrivOpusOmOrgTree();
}

//展开nods下全部子孙节点
function expandMultPrivNodes(nodes,treeId) {

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
function multPrivFuzzySearch(zTreeId, searchField, isHighLight, isExpand){

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

var selectType;
$(function() {

    $("#add_mutl_item_priv_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#privOpusOmOrgDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#multPrivOpusOmOrgDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#multRegionDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 选择组织
    $('#selectMultPrivOrgBtn').click(function(){

        var opusOmOrgTree = $.fn.zTree.getZTreeObj("multPrivOpusOmOrgTree");
        if(itemBasicFieldId&&itemBasicFieldName){
            var nodes = opusOmOrgTree.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){
                var ids = [];
                var names = [];
                for(var i=0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }
                $("#add_item_priv_form input[name='"+ itemBasicFieldId +"']").val(ids.toString());
                $("#add_item_priv_form textarea[name='"+ itemBasicFieldName +"']").val(names.toString());
                closeSelectPrivOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        }else{
            var isSelectOrgSearch = $('#isSelectMultPrivOrgSearch').val();
            var nodes = opusOmOrgTree.getSelectedNodes();
            if(nodes!=null&&nodes.length>0){
                var selectNode = nodes[0];
                if(isSelectOrgSearch=='search'){
                    $("#add_mult_item_priv_form input[name='orgId']").val(selectNode.id);
                    $("#add_mult_item_priv_form input[name='orgName']").val(selectNode.name);
                }else{
                    $("#multOrgId").val(selectNode.id);
                    $("#add_mult_item_priv_form input[name='orgName']").val(selectNode.name);
                }
                closeSelectMultPrivOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        }
    });

    $('#selectMultRegionBtn').click(function(){

        var regionTree = $.fn.zTree.getZTreeObj("multRegionTree");
        if(itemBasicFieldId&&itemBasicFieldName){
            var nodes = regionTree.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){
                var ids = [];
                var names = [];
                for(var i=0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }
                $("#add_mult_item_priv_form input[name='"+ itemBasicFieldId +"']").val(ids.toString());
                $("#add_mult_item_priv_form textarea[name='"+ itemBasicFieldName +"']").val(names.toString());
                closeSelectMultRegionZtree();
            }else{
                swal('提示信息', '请选择行政区域！', 'info');
            }
        }else{
            var isSelectRegionSearch = $('#isSelectMultRegionSearch').val();
            var nodes = regionTree.getSelectedNodes();
            if(nodes!=null&&nodes.length>0){
                var selectNode = nodes[0];
                $("#multRegionId").val(selectNode.regionId);
                $("#add_mult_item_priv_form input[name='regionName']").val(selectNode.regionName);

                closeSelectMultRegionZtree();
            }else{
                swal('提示信息', '请选择行政区域！', 'info');
            }
        }
    });

    // 设置初始化校验
    add_mult_item_priv_validator = $("#add_mult_item_priv_form").validate({

        // 定义校验规则
        rules: {
            regionName:{
                required: true,
            },
            orgName: {
                required: true,
            },
        },
        messages: {
            regionName:{
                required: '<font color="red">此项必填！</font>',
            },
            orgName: {
                required: '<font color="red">此项必填！</font>',
            },
        },

        // 提交表单
        submitHandler: function (form) {

            var itemVerIds = [];
            var zTreeOjb = $.fn.zTree.getZTreeObj("selectMultiplyItemTree");
            var checkedNodes = zTreeOjb.getCheckedNodes();
            if(checkedNodes!=null&&checkedNodes.length>0) {
                for (var i in checkedNodes) {
                    itemVerIds.push(checkedNodes[i].itemVerId);
                }
                $("#mutlItemVerIds").val(itemVerIds);
                $("#uploadProgress").modal("show");
                $('#saveMutlItemPriv').hide();
                $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

                var form = new FormData(document.getElementById("add_mult_item_priv_form"));
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    url: ctx + '/aea/item/priv/saveAeaItemPriv.do',
                    data: form,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal({
                                    type: 'success',
                                    title: '保存成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                // 隐藏模式框
                                $('#saveMutlItemPriv').show();
                                $('#add_multiply_priv_modal').modal('hide');
                                // 列表数据重新加载
                                searchPrivList();
                            },500);
                        } else {

                            setTimeout(function(){
                                $('#saveItemPriv').show();
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function(){
                            $('#saveItemPriv').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }else{
                swal('提示信息', "请选择事项!", "info");
            }
        }
    });
});

$(document).ready(function(){

    $("#multElText").show();
    $('select[name="useEl"]').bind("change", function() {
        if(this.value == "1") {

            $("#multElText").show();
        } else {

            $("#multElText").hide();
        }
    });
});




function isSelectMutlPrivOpuOmOrg(obj, isSearch){

    itemBasicFieldId = null;
    itemBasicFieldName = null;

    if(isSearch){
        $('#isSelectOrgSearch').val('search');
    }else{
        $('#isSelectOrgSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectMultPrivOpusOmOrgZtree();
        }
    }else{
        selectMultPrivOpusOmOrgZtree();
    }
}

function isSelectMutlRegion(obj, isSearch){

    itemBasicFieldId = null;
    itemBasicFieldName = null;

    if(isSearch){
        $('#isSelectRegionSearch').val('search');
    }else{
        $('#isSelectRegionSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectMultRegionZtree();
        }
    }else{
        selectMultRegionZtree();
    }
}


