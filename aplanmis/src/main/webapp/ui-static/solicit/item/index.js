var commonQueryParams = [],
    aedit_solicit_item_validator,
    import_solicit_item_validator,
    solicit_item_tb,
    solicit_item_user_tb;

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 20);

    $('#mainContentPanel2').css('height', $(document.body).height() - 150);

    $('#selectSolicitItem2Tree').css('height', $('#westPanel').height() - 170);

    // 初始征求事项
    initSolicitItemTb();

    // 初始化校验
    initValidateSolicitItem();

    // 初始化导入
    initValidateImportSolicitItem();

    // 征求组织列表
    $('#solicit_item_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 165
    });

    // 处理滚动条
    $('#selectSolicitItem2Tree').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 处理事项表格滚动条
    $(".fixed-table-body").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 关键字搜索用户节点
    selectSolicitItem2Key = $("#selectSolicitItem2KeyWord");

    selectSolicitItem2Key.bind("focus", focusSelectSolicitItem2Key)
                         .bind("blur", blurSelectSolicitItem2Key)
                         .bind("change cut input propertychange",searchSelectSolicitItem2Node);

    selectSolicitItem2Key.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectSolicitItem2Node();
        }
    });
});

function clickToLoadSolicitItemUser(){

    // 初始化征求事项树
    initSelectSolicitItem2Ztree();

    // 初始化征求人员表格
    initSolicitItemUserTb();

    // 征求组织人员列表
    $('#solicit_item_user_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 175
    });

    // 处理事项表格滚动条
    $(".fixed-table-body").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });
}

function initValidateSolicitItem(){

    // 设置初始化校验
    aedit_solicit_item_validator = $("#aedit_solicit_item_form").validate({

        // 定义校验规则
        rules: {
            busType: {
                required: true
            },
            solicitType:{
                required: true
            },
        },
        messages: {
            busType: {
                required: '<font color="red">此项必填！</font>'
            },
            solicitType:{
                required: '<font color="red">此项必填！</font>'
            },
        },
        // 提交表单
        submitHandler: function (form) {

            var d = {};
            var t = $('#aedit_solicit_item_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/solicit/item/saveAeaSolicitItem.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            type: 'success',
                            title: '保存成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        // 隐藏模式框
                        $('#aedit_solicit_item_modal').modal('hide');
                        // 刷新列表
                        clearSearchSolicitItemList();
                    } else {

                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });
}

function initValidateImportSolicitItem(){

    // 设置初始化校验
    import_solicit_item_validator = $("#import_solicit_item_form").validate({

        // 定义校验规则
        rules: {
            busType: {
                required: true
            },
            solicitType:{
                required: true
            },
        },
        messages: {
            busType: {
                required: '<font color="red">此项必填！</font>'
            },
            solicitType:{
                required: '<font color="red">此项必填！</font>'
            },
        },
        // 提交表单
        submitHandler: function (form) {

            var treeObj = $.fn.zTree.getZTreeObj("selectSolicitItemTree");
            var nodes = treeObj.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){

                var itemIds = [];
                for(var i=0;i<nodes.length;i++){
                   itemIds.push(nodes[i].itemId + '*' +nodes[i].itemVerId);
                }
                var d = {};
                var t = $('#import_solicit_item_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                d['itemIds'] = itemIds.toString();

                $("#uploadProgress").modal("show");
                $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");

                $.ajax({
                    url: ctx + '/aea/solicit/item/batchSaveSolicitItem.do',
                    type: 'POST',
                    data: d,
                    async: false,
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function () {
                                $("#uploadProgress").modal('hide');
                                swal({
                                    text: '导入事项成功！',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                                closeSelectSolicitItemZtree();
                                // 刷新列表
                                searchSolicitItemList();
                            }, 500);

                        }else{

                            setTimeout(function () {
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            }, 500);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }, 500);
                    }
                });
            }else{
                swal('错误信息', '请选择事项!', 'error');
            }
        }
    });
}

function initSolicitItemTb() {

    var url = ctx+'/aea/solicit/item/listAeaSolicitItemRelInfoByPage.do';
    solicit_item_tb = $('#solicit_item_list_tb').bootstrapTable({
        url: url,
        columns: getSolicitItemColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: solicitItemParam,
        sidePagination: 'server',
        singleSelect: false,
        clickToSelect: true,
    });
}

//定义表格的显示列
var getSolicitItemColumns = function () {

    var columns = [
        {
            checkbox:true,
            width: 5
        },
        {
            field: 'itemNature',
            align: 'left',
            width: 65,
            title: '事项类型',
            formatter: function (value, row) {

                var itemNature = "";
                if(row.itemNature){

                    if(value=='0'){
                        itemNature = '行政事项';
                    }else if(value=='9'){
                        itemNature = '服务协同';
                    }else if(value=='8'){
                        itemNature = '中介服务事项';
                    }else if(value=='6'){
                        itemNature = '市政公用服务';
                    }
                }
                return itemNature;
            }
        },
        {
            field: 'itemName',
            title: '实施事项名称',
            align: 'left',
            width: 250,
        },
        {
            field: 'itemCode',
            title: '实施事项编号',
            align: 'left',
            width: 120,
        },
        {
            field: 'orgName',
            title: '实施主体',
            align: 'left',
            width: 120,
        },
        {
            field: 'busType',
            title: '征求业务类型',
            align: 'center',
            width: 80,
            formatter: solicitBusTypeFormatter
        },
        {
            field: 'solicitType',
            title: '征求意见模式',
            align: 'center',
            width: 80,
            formatter: solicitTypeFormatter
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 80,
            formatter: solicitItemFormatter
        }
    ];
    return columns;
}

function solicitItemFormatter(value, row, index, field) {

    var editBtn = '<a href="javascript:editSolicitItemById(\'' + row.solicitItemId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                        'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

    var delSolicitItemBtn = '<a href="javascript:deleteSolicitItemById(\'' + row.solicitItemId + '\')" ' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                'title="移除"><i class="la la-trash"></i>' +
                            '</a>';

    return editBtn + delSolicitItemBtn;

}

function solicitTypeFormatter(value, row, index, field) {

    if(value=='0'){

        return '多人征求模式';
    }else if(value=='1'){

        return '单人征求模式';
    }else{
        return value;
    }
}

function solicitItemParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    var pagination = {
        page: pageNum,
        pages: pageNum,
        perpage: params.limit
    };
    var sort = {
        field: params.sort,
        sort: params.order
    };
    var queryParam = {
        pagination: pagination,
        sort: sort
    };
    //组装查询参数
    var buildParam = {};
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchSolicitItemList() {

    commonQueryParams = [];
    var params = $('#search_solicit_item_form').serializeArray();
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#solicit_item_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#solicit_item_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchSolicitItemList() {

    $('#search_solicit_item_form')[0].reset();
    searchSolicitItemList();
}

// 刷新
function refreshSolicitItemList() {

    searchSolicitItemList();
}

// 导入事项
function importSolicitItem(){

    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("加载数据中,请勿点击,耐心等候...");
    initSolicitItemCheck();
    setTimeout(function () {
        $("#uploadProgress").modal('hide');
    }, 500);
}

//判断字符是否为空的方法
function isEmpty(obj){

    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

function editSolicitItemById(id){

    if(!isEmpty(id)){

        $("#aedit_solicit_item_modal").modal("show");
        $("#aedit_solicit_item_modal_title").html("编辑征求事项");
        $('#aedit_solicit_item_form')[0].reset();
        aedit_solicit_item_validator.resetForm();
        $('#aedit_solicit_item_form input[name="solicitItemId"]').val("");
        $('#aedit_solicit_item_form input[name="itemId"]').val("");
        $('#aedit_solicit_item_form input[name="itemVerId"]').val("");

        $.ajax({
            url: ctx+'/aea/solicit/item/getAeaSolicitItemRelInfoById.do',
            type: 'post',
            data: {'id': id},
            async: false,
            success: function (data) {
                if (data) {

                    loadFormData(true, '#aedit_solicit_item_form', data);
                }
            }
        });
    }else{
        swal('提示信息', "请选择需要编辑的数据！", 'info');
    }
}

function deleteSolicitItemById(id){

    if(!isEmpty(id)){

        swal({
            text: '此操作将移除配置的征求事项数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/item/deleteAeaSolicitItemById.do',
                    type: 'post',
                    data: {
                        'id': id
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除征求事项数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitItemList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除的数据！", 'info');
    }
}

// 批量移除
function batchDelSolicitItem(){

    var rows = $("#solicit_item_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].solicitItemId);
        }
        swal({
            text: '此操作将批量移除配置的征求事项数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/item/batchDelSolicitItemByIds.do',
                    type: 'post',
                    data: {
                        'ids': ids.toString()
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除征求事项数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitItemList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除的数据！", 'info');
    }
}

// =============  征求人员 ==================


// 选择用户树配置
var selectSolicitItem2Key,
    selectSolicitItem2NodeList = [],
    selectSolicitItem2LastValue = "",
    selectSolicitItem2Tree = null,
    curSelectSolicitItem2TreeNode = null,
    selectSolicitItem2TreeSetting = {
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

            onClick: onClickSelectSolicitItem2Node,
        }
    };

function onClickSelectSolicitItem2Node(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if (treeNode && treeNode.type == 'item') {
        treeObj.checkAllNodes(false);
        if (treeNode.checked) {
            treeObj.checkNode(treeNode, false, false, true);
        }else{
            treeObj.checkNode(treeNode, true, false, true);
        }
        curSelectSolicitItem2TreeNode = treeNode;
        searchSolicitItemUserList();
    }else{
        treeObj.cancelSelectedNode(treeNode);
        if(curSelectSolicitItem2TreeNode){
            var node = treeObj.getNodeByParam("id", curSelectSolicitItem2TreeNode.id, null);
            treeObj.selectNode(node);
            curSelectSolicitItem2TreeNode = node;
        }
    }
}

function focusSelectSolicitItem2Key(e) {

    if (selectSolicitItem2Key.hasClass("empty")) {
        selectSolicitItem2Key.removeClass("empty");
    }
}

function blurSelectSolicitItem2Key(e) {

    if (selectSolicitItem2Key.get(0).value === "") {
        selectSolicitItem2Key.addClass("empty");
    }
    searchSelectSolicitItem2Node(e);
}

function expandSolicitItem2TreeNodes(nodes) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj("selectSolicitItem2Tree");
    for (var i=0;i<nodes.length;i++) {
        zTree.expandNode(nodes[i], true, false, false);
        if (nodes[i].isParent) {
            expandSolicitItem2TreeNodes(nodes[i].children);//递归
        }
    }
}

//全部展开
function expandSelectSolicitItem2AllNode(){

    selectSolicitItem2Tree.expandAll(true);
}

//全部折叠
function collapseSelectSolicitItem2AllNode(){

    selectSolicitItem2Tree.expandAll(false);
}

// 搜索节点
function searchSelectSolicitItem2Node(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectSolicitItem2KeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectSolicitItem2LastValue === value) {
        return;
    }

    // 保存最后一次
    selectSolicitItem2LastValue = value;

    var nodes = selectSolicitItem2Tree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectSolicitItem2AllNode(nodes);
        return;
    }
    hideSelectSolicitItem2AllNode(nodes);
    selectSolicitItem2NodeList = selectSolicitItem2Tree.getNodesByParamFuzzy(keyType, value);
    updateSelectSolicitItem2Nodes(selectSolicitItem2NodeList);
}

// 清空查询
function clearSearchSelectSolicitItem2Node(){

    // 清空查询内容
    $('#selectSolicitItem2KeyWord').val('');
    showSelectSolicitItem2AllNode(selectSolicitItem2Tree.getNodes());
    setTimeout(function() {
        expandSelectSolicitItem2AllNode
    }, 300);
}

//隐藏所有节点
function hideSelectSolicitItem2AllNode(nodes){

    nodes = selectSolicitItem2Tree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectSolicitItem2Tree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectSolicitItem2AllNode(nodes){

    nodes = selectSolicitItem2Tree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectSolicitItem2Tree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectSolicitItem2Tree.expandNode(nodes[i],true,true,false,false);
        }
        selectSolicitItem2Tree.showNode(nodes[i]);
        showSelectSolicitItem2AllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectSolicitItem2Nodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectSolicitItem2Tree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectSolicitItem2Tree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectSolicitItem2Tree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectSolicitItem2Tree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectSolicitItem2Tree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectSolicitItem2Tree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载选择用户数据
function initSelectSolicitItem2Ztree(){

    $.ajax({
        url: ctx+'/aea/solicit/item/gtreeSolicitItem.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectSolicitItem2Tree)selectSolicitItem2Tree.destroy();
                selectSolicitItem2Tree = $.fn.zTree.init($("#selectSolicitItem2Tree"), selectSolicitItem2TreeSetting, data);
                // 默认选择第一个用户节点
                if(data.length>1){
                    curSelectSolicitItem2TreeNode = data[1];
                    var node = selectSolicitItem2Tree.getNodeByParam("id", data[1].id, null);
                    if(node!=null){
                        selectSolicitItem2Tree.selectNode(node);
                    }
                }else{
                    curSelectSolicitItem2TreeNode = null;
                }
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

function initSolicitItemUserTb() {

    var url = ctx+'/aea/solicit/item/user/listAeaSolicitItemUserRelInfoByPage.do';
    solicit_item_user_tb = $('#solicit_item_user_list_tb').bootstrapTable({
        url: url,
        columns: getSolicitItemUserColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: solicitItemUserParam,
        sidePagination: 'server',
        singleSelect: false,
        clickToSelect: true,
        height: $('#westPanel').height() - 175
    });
}

//定义表格的显示列
var getSolicitItemUserColumns = function () {

    var columns = [
        {
            checkbox:true,
            width: 5
        },
        {
            field: 'userName',
            align: 'left',
            width: 150,
            title: '姓名',
        },
        {
            field: 'loginName',
            title: '登录名',
            align: 'left',
            width: 150,
        },
        {
            field: 'userSex',
            title: '性别',
            align: 'center',
            width: 60,
            formatter: function (value, row, index, field) {

                if(value=='0'){
                    return '男';
                }else{
                    return '女';
                }
            }
        },
        {
            field: 'userMobile',
            title: '手机号',
            align: 'center',
            width: 100,
        },
        {
            field: 'isActive',
            title: '是否启用',
            align: 'center',
            width: 100,
            formatter: solicitItemUserIsActiveFormatter
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 100,
            formatter: solicitItemUserFormatter
        }
    ];
    return columns;
}

function solicitItemUserIsActiveFormatter(value, row, index, field) {

    if(value=='1'){

        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" onclick="changeSolicitItemUserIsActive(this, \''+ row.itemUserId +'\', \''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }else{

        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" onclick="changeSolicitItemUserIsActive(this, \''+ row.itemUserId +'\', \''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

function changeSolicitItemUserIsActive(obj, id, isActive){

    if(!isEmpty(id)){
        var action = isActive=='1'? "禁用" : "启用" ;
        swal({
            title: '提示信息',
            text: "确定要" + action + "选中的记录吗?",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value){
                $.ajax({
                    url: ctx+"/aea/solicit/item/user/changeIsActive.do",
                    type: 'POST',
                    data: {"id": id},
                    success: function (result) {
                        if(result.success){
                            searchSolicitItemUserList();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }else{
                isActive=='1'?$(obj).prop("checked",true):$(obj).prop("checked",false);
            }
        });
    }else{
        swal('错误信息', '操作对象！', 'error');
    }
}

function solicitItemUserFormatter(value, row, index, field) {

    var delBtn = '<a href="javascript:deleteSolicitItemUserById(\'' + row.itemUserId + '\')" ' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                    'title="移除"><i class="la la-trash"></i>' +
                 '</a>';

    return delBtn;

}

function solicitItemUserParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    var pagination = {
        page: pageNum,
        pages: pageNum,
        perpage: params.limit
    };
    var sort = {
        field: params.sort,
        sort: params.order
    };
    var queryParam = {
        pagination: pagination,
        sort: sort
    };
    //组装查询参数
    var buildParam = {};
    var solicitItemId = null;
    if(curSelectSolicitItem2TreeNode!=null){
        solicitItemId = curSelectSolicitItem2TreeNode.id;
    }
    commonQueryParams.push({name: 'solicitItemId', value: solicitItemId});
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchSolicitItemUserList() {

    commonQueryParams = [];
    var params = $('#search_solicit_item_user_form').serializeArray();
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#solicit_item_user_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#solicit_item_user_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchSolicitItemUserList() {

    $('#search_solicit_item_user_form')[0].reset();
    searchSolicitItemUserList();
}

// 刷新
function refreshSolicitItemUserList() {

    searchSolicitItemUserList();
}

// 导入事项
function importSolicitUser(){

    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("加载数据中,请勿点击,耐心等候...");
    initSolicitItemUserCheck();
    setTimeout(function () {
        $("#uploadProgress").modal('hide');
    }, 500);
}

// 排序
function sortSolicitItemUser(){

}

function deleteSolicitItemUserById(id){

    if(!isEmpty(id)){

        swal({
            text: '此操作将移除配置的征求人员数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/item/user/delSolicitItemUserById.do',
                    type: 'post',
                    data: {
                        'id': id
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除征求人员数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitItemUserList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除的数据！", 'info');
    }
}

// 批量移除
function batchDelSolicitItemUser(){

    var rows = $("#solicit_item_user_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].itemUserId);
        }
        swal({
            text: '此操作将批量移除配置的征求人员数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/item/user/batchDelSolicitItemUserByIds.do',
                    type: 'post',
                    data: {
                        'ids': ids.toString()
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除征求人员数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitItemUserList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除的数据！", 'info');
    }
}