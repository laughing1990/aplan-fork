// 选择事项树配置
var selectItemKey,
    selectItemNodeList = [],
    selectItemLastValue = "",
    selectItemTree = null,
    spanChecked = "0",
    selectItemTreeSetting = {
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
            onClick: onClickSelectItemNode,
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
                var indexOfEnd = categoryId.indexOf("*");
                categoryId = categoryId.substring(0,indexOfEnd);
                if(categoryId==treeNode.id){
                    $(this).remove();
                    return false;
                }
            });
        }
    }
}

function onClickSelectItemNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
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

function expandAll() {

    var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
    expandNodes(zTree.getNodes());
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
    $('#mainContentPanel').css('height',$(document.body).height()-85);
    $('#selectItemTree').css('height', $('#westPanel').height()-116);
    $('#selectedFrontCheckItemDiv').css('height', $('#westPanel').height()-125);

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

    $('#selectedFrontCheckItemDiv').niceScroll({

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

    // 加载必选事项数据
    setItemRelFrontCheckItems();

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

    Sortable.create(document.getElementById('selectedFrontCheckItemUl'), {
        animation: 150,
        onEnd: function (evt) { //拖拽完毕之后发生该事件

        }
    });

    $('#selectedFrontCheckItemBtn').click(function(){

        if(curIsEditable){
            var sortNos = [];
            var itemVerIds = [];
            var liObjs = document.getElementsByName('selectFrontCheckItemLi');
            for (var i = 0; i < liObjs.length; i++) {
                var categoryId = $(liObjs[i]).attr('category-id');
                var indexOfEnd = categoryId.indexOf("*");
                categoryId = categoryId.substring(indexOfEnd+1);
                itemVerIds.push(categoryId);
                sortNos.push(i + 1);
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置关联的前置检查事项,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/item/front/batchSaveFrontItem.do',
                        type: 'POST',
                        data: {'frontItemVerIds': itemVerIds.toString(), 'sortNos': sortNos.toString(), 'itemVerId': currentBusiId},
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    // title: '提示信息',
                                    text: '保存事项成功！',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                            }
                        },
                        error: function () {
                            swal('错误信息', '保存事项服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
        }
    });
});

function initFrontCheckItem() {

    initSelectItemZtree();
    // 加载可选事项数据
    setItemRelFrontCheckItems();
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

// 删除前置检查事项选中节点
function removeFrontCheckSelectedItem(itemId){

    if(curIsEditable){
        $('li[name="selectFrontCheckItemLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            var indexOfEnd = categoryId.indexOf("*");
            categoryId = categoryId.substring(0, indexOfEnd);
            if(categoryId==itemId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectItemTree.cancelSelectedNode();
        var node = selectItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectItemTree.checkNode(node, false, true, false);
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

// 关闭选择事项弹窗
function closeSelectItemZtree(){

    $('#select_item_ztree_modal').modal('hide');
}

// 加载选择事项数据
function initSelectItemZtree(){

    $.ajax({
        url: ctx+'/aea/item/gtreeTestRunOrPublishedItem.do',
        type:'post',
        data:{'isCatalog': '0'},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectItemTree)selectItemTree.destroy();
                selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,data);
            }
        },
        error: function(){
            swal('错误信息', '初始化事项数据异常!', 'error');
        }
    });
}

//刷新前置检查事项
function setItemRelFrontCheckItems() {

    // 清空关键字据
    $('#selectItemKeyWord').val('');

    // 清空右侧选中事项数据
    $("#selectedFrontCheckItemUl").html("");

    // 取消上次的选中节点
    selectItemTree.checkAllNodes(false);

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        loadFrontCheckSelectedItemData();

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
}

//判断字符是否为空的方法
function isEmpty(obj){

    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

function loadFrontCheckSelectedItemData(){

    $.ajax({
        url: ctx + '/aea/item/front/listItemsByItemVerId.do',
        type: 'post',
        data: {'itemVerId': currentBusiId},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    var itemName = data[i].frontCkItemName;
                    // 选择事项库树节点
                    var node = selectItemTree.getNodeByParam("id", data[i].frontCkItemId, null);
                    if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                        itemName = node.name;
                    }else{
                        if(!isEmpty(data[i].frontCkIsCatalog)){
                            if(data[i].frontCkIsCatalog=='1'){
                                itemName = '【标准事项】'+ itemName;
                                if(!isEmpty(data[i].frontCkGuideOrgName)){
                                    itemName = itemName +'【'+ data[i].frontCkGuideOrgName+'】';
                                }
                            }else{
                                itemName = '【实施事项】'+ itemName;
                                if(!isEmpty(data[i].frontCkOrgName)) {
                                    itemName = itemName + '【' + data[i].frontCkOrgName + '】';
                                }
                            }
                        }
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectFrontCheckItemLi" category-id="' + data[i].frontCkItemId + '*' + data[i].frontCkItemVerId + '">' +
                                    '<span class="drag-handle_td" onclick="removeFrontCheckSelectedItem(\'' + data[i].frontCkItemId + '\');">×</span>' +
                                    '<span class="org_name_td">' + itemName +'</span>' +
                                 '</li>';
                    $('#selectedFrontCheckItemUl').append(liHtml);
                }
            }
        }
    });
}