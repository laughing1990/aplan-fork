// 选择事项树配置
var selectItemKey,
    selectItemNodeList = [],
    selectItemLastValue = "",
    selectItemTree = null,
    spanChecked = "0",
    rightItemTree = null,
    selectItemTreeSetting = {
        edit: {
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "", "N": "" },
            radioType: 'level'
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
            showHorizontal: false,//设置是否水平平铺树（自定义属性）,
            addDiyDom: addDiyDom

        },
        // async: {
        //     //设置 zTree 是否开启异步加载模式
        //     enable: false,
        //     autoParam: ["id","name","type"],
        //     url:ctx+"/aea/item/gtreeItemAsyncZTree.do"
        // },
        //用于捕获节点被点击的事件回调函数
        callback: {
            // beforeCheck: beforeSelectItemCheck,
            onCheck: onSelectItemCheck,
            // beforeClick: beforeSelectItemClick,
            onClick: onClickSelectItemNode,
            //onAsyncSuccess: onAsyncSuccessSelectItemNode
            // beforeAsync: beforeAsync,
            // onAsyncSuccess: onAsyncSuccess,
            // onAsyncError: onAsyncError
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
    var nodes = treeObj.getCheckedNodes();
    if(spanChecked=='0'){
        if(treeNode && treeNode.checked){
            // checkNodes(nodes, false);
            // treeObj.checkNode(treeNode, true, false);
            // 选中
            getStageItemStatusByProIds(treeNode);
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
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


// //处理异步加载子节点 start
// var curStatus = "init",
//     curAsyncCount = 0,
//     asyncForAll = false,
//     goAsync = false;
//
// function beforeAsync() {
//
//     curAsyncCount++;
// }
//
// function onAsyncSuccess(event, treeId, treeNode, msg) {
//
//     curAsyncCount--;
//     if (curStatus == "expand") {
//         expandNodes(treeNode.children);
//     } else if (curStatus == "async") {
//         asyncNodes(treeNode.children);
//     }
//     if (curAsyncCount <= 0) {
//         if (curStatus != "init" && curStatus != "") {
//             asyncForAll = true;
//         }
//         curStatus = "";
//     }
// }
//
// function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
//
//     curAsyncCount--;
//     if (curAsyncCount <= 0) {
//         curStatus = "";
//         if (treeNode!=null) asyncForAll = true;
//     }
// }

function expandAll() {

    var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
    if (asyncForAll) {
        zTree.expandAll(true);
    } else {
        expandNodes(zTree.getNodes());
        if (!goAsync) {
            curStatus = "";
        }
    }
}
function expandNodes(nodes) {

    if (!nodes) return;
    curStatus = "expand";
    var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
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
    // $('#mainContentPanel').css('height',$(document.body).height()-85);
    $('#selectItemTree').css('height', $('#westPanel').parent().height()-200);
    // $('#selectedItemSortDiv').css('height', $('#westPanel').height()- 160);
    // $('#selectedNotNeedSortDiv').css('height', $('#westPanel').height()-160);
    // $('#selectedFrontCheckItemDiv').css('height', $('#westPanel').height()-160);

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

  /*  $('#selectedItemSortDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#selectedNotNeedSortDiv').niceScroll({

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
    });*/

    initSelectItemZtree();

    // 加载必选事项数据
    // setStageRelItems();

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

    /*Sortable.create(document.getElementById('selectedItemUl'), {
        animation: 150,
        onEnd: function (evt) { //拖拽完毕之后发生该事件

        }
    });

    Sortable.create(document.getElementById('selectedNotNeedItemUl'), {
        animation: 150,
        onEnd: function (evt) { //拖拽完毕之后发生该事件

        }
    });

    Sortable.create(document.getElementById('selectedFrontCheckItemUl'), {
        animation: 150,
        onEnd: function (evt) { //拖拽完毕之后发生该事件

        }
    });*/

/*    //必选事项保存按钮事件
    $('#selectItemBtn').click(function(){
        if(curIsEditable){
            var itemIds = [];
            var sortNos = [];
            var itemVerIds = [];
            var liObjs = document.getElementsByName('selectItemLi');
            for (var i = 0; i < liObjs.length; i++) {
                itemIds.push($(liObjs[i]).attr('category-id'));
                sortNos.push(i + 1);
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置关联的必选事项,将影响情形材料配置下事项、情形、材料等配置数据,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/par/stage/item/batchSaveStageItem.do',
                        type: 'POST',
                        data: {'itemIds': itemIds.toString(), 'sortNos': sortNos.toString(), 'stageId': currentBusiId, 'isOptionItem':'0'},
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

    //非必选按钮保存事件
    $('#selectNotNeedItemBtn').click(function(){

        if(curIsEditable){
            var itemIds = [];
            var sortNos = [];
            var itemVerIds = [];
            var liObjs = document.getElementsByName('selectNotNeedItemLi');
            for (var i = 0; i < liObjs.length; i++) {
                itemIds.push($(liObjs[i]).attr('category-id'));
                sortNos.push(i + 1);
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置关联的可选事项,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/par/stage/item/batchSaveStageItem.do',
                        type: 'POST',
                        data: {'itemIds': itemIds.toString(), 'sortNos': sortNos.toString(), 'stageId': currentBusiId, 'isOptionItem':'1'},
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

    $('#selectedFrontCheckItemBtn').click(function(){

        if(curIsEditable){
            var itemIds = [];
            var sortNos = [];
            var itemVerIds = [];
            var liObjs = document.getElementsByName('selectFrontCheckItemLi');
            for (var i = 0; i < liObjs.length; i++) {
                itemIds.push($(liObjs[i]).attr('category-id'));
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
                        url: ctx + '/aea/par/stage/item/batchSaveStageItem.do',
                        type: 'POST',
                        data: {'itemIds': itemIds.toString(), 'sortNos': sortNos.toString(), 'stageId': currentBusiId, 'isOptionItem':'2'},
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
    });*/
/*
    if(curentIsOptionItem == '1'){
        $('#optionItemLi').show();
    }else{
        $('#optionItemLi').hide();
    }

    if(isFrontCheckItem=='1'){
        $('#frontCheckItemLi').show();
    }else{
        $('#frontCheckItemLi').hide();
    }*/
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
        selectItemTree.cancelSelectedNode();
        var node = selectItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectItemTree.checkNode(node, false, true, false);
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
        selectItemTree.cancelSelectedNode();
        var node = selectItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectItemTree.checkNode(node, false, true, false);
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

    if(projTree){
        selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting, JSON.parse(projTree));
    }
    /*$.ajax({
        url: ctx+'/aea/item/gtreeTestRunOrPublishedItem.do',
        type:'post',
        // data:{'itemNature': itemNature},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectItemTree)selectItemTree.destroy();
                selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,data);
            }
        },
        error: function(){
            // swal('错误信息', '初始化事项数据异常!', 'error');
        }
    });*/
}

// 需要设置事阶段id
function setStageRelItems(){

    // 清空关键字据
    $('#selectItemKeyWord').val('');

    // 清空右侧选中事项数据
    $("#selectedItemUl").html("");

    // 打开弹窗，加载树数据
    // openSelectItemModal();

    // 取消上次的选中节点
    selectItemTree.checkAllNodes(false);

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        loadSelectedItemData();

        //延迟加载
        // setTimeout(function(){loadSelectedItemData();},1000);

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
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
    selectItemTree.checkAllNodes(false);

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        loadNotNeedSelectedItemData();

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
    selectItemTree.checkAllNodes(false);

    if(currentBusiId&&currentBusiId!=''&&currentBusiId!=undefined&&currentBusiId!=null){

        loadFrontCheckSelectedItemData();

        //延迟加载
        // setTimeout(function(){loadSelectedItemData();},1000);

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
}

// 勾选和渲染已经选择的事项
function loadSelectedItemData(){

    $.ajax({
        url: ctx + '/aea/par/stage/item/listAeaStageItemByStageId.do',
        type: 'post',
        data: {'stageId': currentBusiId, 'isOptionItem':'0'},
        async: false,
        success: function (data) {
            //debugger;
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectItemTree.getNodeByParam("id", data[i].itemId, null);
                    if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectItemLi" category-id="' + data[i].itemId + '*' + data[i].itemVerId + '">' +
                                    '<span class="drag-handle_td" onclick="removeSelectedItem(\'' + data[i].itemId + '\');">×</span>' +
                                    '<span class="org_name_td">' + data[i].itemName +'【'+ data[i].orgName+'】'+ '</span>' +
                                '</li>';
                    $('#selectedItemUl').append(liHtml);
                }
            }
        }
    });
}

// 勾选和渲染已经选择的非必选事项
function loadNotNeedSelectedItemData(){

    $.ajax({
        url: ctx + '/aea/par/stage/item/listAeaStageItemByStageId.do',
        type: 'post',
        data: {'stageId': currentBusiId, 'isOptionItem':'1'},
        async: false,
        success: function (data) {
            //debugger;
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectItemTree.getNodeByParam("id", data[i].itemId, null);
                    if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectNotNeedItemLi" category-id="' + data[i].itemId + '*' + data[i].itemVerId + '">' +
                                    '<span class="drag-handle_td" onclick="removeNoNeedSelectedItem(\'' + data[i].itemId + '\');">×</span>' +
                                    '<span class="org_name_td">' + data[i].itemName +'【'+ data[i].orgName+'】'+ '</span>' +
                                 '</li>';
                    $('#selectedNotNeedItemUl').append(liHtml);
                }
            }
        }
    });
}

function loadFrontCheckSelectedItemData(){

    $.ajax({
        url: ctx + '/aea/par/stage/item/listAeaStageItemByStageId.do',
        type: 'post',
        data: {'stageId': currentBusiId, 'isOptionItem':'2'},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectItemTree.getNodeByParam("id", data[i].itemId, null);
                    if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectFrontCheckItemLi" category-id="' + data[i].itemId + '*' + data[i].itemVerId + '">' +
                                     '<span class="drag-handle_td" onclick="removeFrontCheckSelectedItem(\'' + data[i].itemId + '\');">×</span>' +
                                     '<span class="org_name_td">' + data[i].itemName +'【'+ data[i].orgName+'】'+'</span>' +
                                 '</li>';
                    $('#selectedFrontCheckItemUl').append(liHtml);
                }
            }
        }
    });
}

function getStageItemStatusByProIds(treeNode){
    console.log(treeNode);
    $.get(ctx+'/rest/project/diagram/status/json',{projInfoId:treeNode.id}, function(res, a){
       console.log('res',res);
       if(res.success){
           clearStageAndItem();
           setViewByProjResult(res.content, treeNode.id);
       }else{
           if(res.message){
               swal('提示信息', res.message, 'info');
           }else{
               swal('提示信息', '请联系管理员！', 'info');
           }
       }
    });
}

function checkNodes(nodes,isChecked){
    if(nodes && nodes.length > 0){
        for(var i in nodes){
            selectItemTree.checkNode(nodes[i], isChecked, false);
        }
    }
}

function addDiyDom(treeId, treeNode) {
    var spaceWidth = 5;
    var switchObj = $("#" + treeNode.tId + "_switch"),
        checkObj = $("#" + treeNode.tId + "_check"),
        icoObj = $("#" + treeNode.tId + "_ico");
    switchObj.remove();
    checkObj.remove();
    icoObj.parent().before(switchObj);
    icoObj.parent().before(checkObj);

    var spantxt = $("#" + treeNode.tId + "_span").html();
    if (spantxt.length > 17) {
        spantxt = spantxt.substring(0, 17) + "...";
        $("#" + treeNode.tId + "_span").html(spantxt);
    }

}