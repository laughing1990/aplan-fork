var commonQueryParams = [],
    aedit_solicit_org_validator = null,
    import_solicit_org_validator = null,
    solicit_org_tb,
    selectThemeStageKey,
    selectThemeStageNodeList = [],
    selectThemeStageLastValue = "",
    selectThemeStageTree = null,
    curSelectThemeStageTreeNode = null,
    selectThemeStageTreeSetting = {
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

            onClick: onClickSelectThemeStageNode,
        }
    };

function onClickSelectThemeStageNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if (treeNode && treeNode.type == 'stage') {
        treeObj.checkAllNodes(false);
        if (treeNode.checked) {
            treeObj.checkNode(treeNode, false, false, true);
        }else{
            treeObj.checkNode(treeNode, true, false, true);
        }
        curSelectThemeStageTreeNode = treeNode;
        searchSolicitOrgList();
    }else{
        treeObj.cancelSelectedNode(treeNode);
        if(curSelectThemeStageTreeNode){
            var node = treeObj.getNodeByParam("id", curSelectThemeStageTreeNode.id, null);
            treeObj.selectNode(node);
            curSelectThemeStageTreeNode = node;
        }
    }
}

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 20);

    $('#selectThemeStageTree').css('height', $('#westPanel').height()-116);

    // 处理滚动条
    $('#selectThemeStageTree').niceScroll({

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

    // 初始化主题阶段
    initSelectThemeStageTreeZtree();

    // 初始牵头部门
    initSolicitOrgTb();

    // 初始化校验
    initValidateSolicitOrg();

    // 初始化导入
    initValidateImportSolicitOrg();

    // 征求组织列表
    $('#solicit_org_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 165
    });

    // 关键字搜索用户节点
    selectThemeStageKey = $("#selectThemeStageKeyword");

    selectThemeStageKey.bind("focus", focusSelectThemeStageKey)
                       .bind("blur", blurSelectThemeStageKey)
                       .bind("change cut input propertychange", searchSelectThemeStageNode);

    selectThemeStageKey.bind('keydown', function (e){

        if(e.which == 13){
            searchSelectThemeStageNode();
        }
    });
});

function initValidateSolicitOrg(){

    // 设置初始化校验
    aedit_solicit_org_validator = $("#aedit_solicit_org_form").validate({

        // 定义校验规则
        rules: {
            solicitType:{
                required: true
            },
        },
        messages: {
            solicitType:{
                required: '<font color="red">此项必填！</font>'
            },
        },
        // 提交表单
        submitHandler: function (form) {

            var d = {};
            var t = $('#aedit_solicit_org_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/solicit/org/saveAeaSolicitOrg.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            type: 'success',
                            title: '保存成功！请继续给牵头部门配置牵头人员！',
                            showConfirmButton: false,
                            timer: 2000
                        });
                        // 隐藏模式框
                        $('#aedit_solicit_org_modal').modal('hide');
                        // 刷新列表
                        clearSearchSolicitOrgList();
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

function initValidateImportSolicitOrg(){

    // 设置初始化校验
    import_solicit_org_validator = $("#import_solicit_org_form").validate({

        // 定义校验规则
        rules: {
            solicitType:{
                required: true
            },
        },
        messages: {
            solicitType:{
                required: '<font color="red">此项必填！</font>'
            },
        },
        // 提交表单
        submitHandler: function (form) {

            if(curSelectThemeStageTreeNode){

                var treeObj = $.fn.zTree.getZTreeObj("selectSolicitOrgTree");
                var nodes = treeObj.getCheckedNodes(true);
                if(nodes!=null&&nodes.length>0){
                    var orgIds = [];
                    for(var i=0;i<nodes.length;i++){
                        orgIds.push(nodes[i].id);
                    }
                    var d = {};
                    var t = $('#import_solicit_org_form').serializeArray();
                    $.each(t, function () {
                        d[this.name] = this.value;
                    });
                    d['orgIds'] = orgIds.toString();
                    d['isBusSolicit'] = isBusSolicit;
                    d['stageId'] = curSelectThemeStageTreeNode.id;

                    $("#uploadProgress").modal("show");
                    $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");

                    $.ajax({
                        url: ctx+'/aea/solicit/org/batchSaveSolicitOrg.do',
                        type: 'POST',
                        data: d,
                        async: false,
                        success: function (result) {
                            if (result.success) {

                                setTimeout(function () {
                                    $("#uploadProgress").modal('hide');
                                    swal({
                                        text: '保存成功！请继续给牵头部门配置牵头人员！',
                                        type: 'success',
                                        timer: 2000,
                                        showConfirmButton: false
                                    });
                                    closeSelectSolicitOrgZtree();
                                    // 刷新列表
                                    searchSolicitOrgList();
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
                    swal('错误信息', '请选择牵头部门!', 'error');
                }
            }else{
                swal('错误信息', '请选择主题阶段!', 'error');
            }
        }
    });
}

function focusSelectThemeStageKey(e) {

    if (selectThemeStageKey.hasClass("empty")) {
        selectThemeStageKey.removeClass("empty");
    }
}

function blurSelectThemeStageKey(e) {

    if (selectThemeStageKey.get(0).value === "") {
        selectThemeStageKey.addClass("empty");
    }
    searchSelectThemeStageNode(e);
}

function expandThemeStageTreeNodes(nodes) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj("selectThemeStageTree");
    for (var i=0;i<nodes.length;i++) {
        zTree.expandNode(nodes[i], true, false, false);
        if (nodes[i].isParent) {
            expandThemeStageTreeNodes(nodes[i].children);//递归
        }
    }
}

//全部展开
function expandSelectThemeStageAllNode(){

    selectThemeStageTree.expandAll(true);
}

//全部折叠
function collapseSelectThemeStageAllNode(){

    selectThemeStageTree.expandAll(false);
}

// 搜索节点
function searchSelectThemeStageNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectThemeStageKeyword').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectThemeStageLastValue === value) {
        return;
    }

    // 保存最后一次
    selectThemeStageLastValue = value;

    var nodes = selectThemeStageTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectThemeStageAllNode(nodes);
        return;
    }
    hideSelectThemeStageAllNode(nodes);
    selectThemeStageNodeList = selectThemeStageTree.getNodesByParamFuzzy(keyType, value);
    updateSelectThemeStageNodes(selectThemeStageNodeList);
}

// 清空查询
function clearSearchSelectThemeStageNode(){

    // 清空查询内容
    $('#selectThemeStageKeyword').val('');
    showSelectThemeStageAllNode(selectThemeStageTree.getNodes());
    setTimeout(function() {
        expandSelectThemeStageAllNode();
    }, 300);
}

//隐藏所有节点
function hideSelectThemeStageAllNode(nodes){

    nodes = selectThemeStageTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectThemeStageTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectThemeStageAllNode(nodes){

    nodes = selectThemeStageTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectThemeStageTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectThemeStageTree.expandNode(nodes[i],true,true,false,false);
        }
        selectThemeStageTree.showNode(nodes[i]);
        showSelectThemeStageAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectThemeStageNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectThemeStageTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectThemeStageTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectThemeStageTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectThemeStageTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectThemeStageTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectThemeStageTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载选择用户数据
function initSelectThemeStageTreeZtree(){

    $.ajax({
        url: ctx+'/aea/par/theme/ver/gtreeOkThemeRelStage.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(selectThemeStageTree)selectThemeStageTree.destroy();
                selectThemeStageTree = $.fn.zTree.init($("#selectThemeStageTree"), selectThemeStageTreeSetting, data);
                // 默认选择第一个用户节点
                if(data.length>1){
                    curSelectThemeStageTreeNode = data[1];
                    var node = selectThemeStageTree.getNodeByParam("id", data[1].id, null);
                    if(node!=null){
                        selectThemeStageTree.selectNode(node);
                    }
                }else{
                    curSelectThemeStageTreeNode = null;
                }
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

function initSolicitOrgTb() {

    var stageId = null;
    if(curSelectThemeStageTreeNode){
        stageId = curSelectThemeStageTreeNode.id;
    }
    var url = ctx+'/aea/solicit/org/listAeaSolicitOrgRelOrgInfoByPage.do?isBusSolicit=0';
    solicit_org_tb = $('#solicit_org_list_tb').bootstrapTable({
        url: url,
        columns: getSolicitOrgColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: solicitOrgParam,
        sidePagination: 'server',
        singleSelect: false,
        clickToSelect: true,
    });
}

//定义表格的显示列
var getSolicitOrgColumns = function () {

    var columns = [
        {
            checkbox:true,
            width: 5
        },
        {
            field: 'orgName',
            align: 'left',
            width: 250,
            title: '牵头部门名称',
        },
        {
            field: 'orgCode',
            title: '牵头部门编号',
            align: 'left',
            width: 250,
        },
        {
            field: 'solicitType',
            title: '征求意见模式',
            align: 'center',
            width: 100,
            formatter: solicitTypeFormatter
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 120,
            formatter: solicitOrgFormatter
        }
    ];
    return columns;
}

function solicitOrgFormatter(value, row, index, field) {

    var editBtn = '<a href="javascript:editSolicitOrgById(\'' + row.solicitOrgId + '\')" ' +
                       'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                       'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

    var setSolicitOrgUserBtn =  '<a href="javascript:setSolicitOrgUser(\'' + row.solicitOrgId + '\')" ' +
                                    'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                    'title="设置牵头人员"><i class="la la-cog"></i>' +
                                '</a>';

    var delSolicitOrgBtn = '<a href="javascript:deleteSolicitOrgById(\'' + row.solicitOrgId + '\')" ' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                'title="移除"><i class="la la-trash"></i>' +
                           '</a>';

    return editBtn + setSolicitOrgUserBtn + delSolicitOrgBtn;

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

function solicitOrgParam(params) {

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
    var stageId = null;
    if(curSelectThemeStageTreeNode!=null){
        stageId = curSelectThemeStageTreeNode.id;
    }
    commonQueryParams.push({name: 'stageId', value: stageId});
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchSolicitOrgList() {

    commonQueryParams = [];
    var params = $('#search_solicit_org_form').serializeArray();
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#solicit_org_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#solicit_org_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchSolicitOrgList() {

    $('#search_solicit_org_form')[0].reset();
    searchSolicitOrgList();
}

// 刷新
function refreshSolicitOrgList() {

    searchSolicitOrgList();
}

// 导入部门
function importSolicitOrg(){

    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("加载数据中,请勿点击,耐心等候...");
    $('#import_solicit_org_form')[0].reset();
    if(import_solicit_org_validator!=null){
        import_solicit_org_validator.resetForm();
    }
    initSolicitOrgCheck();
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

function editSolicitOrgById(id){

    if(!isEmpty(id)){

        $("#aedit_solicit_org_modal").modal("show");
        $("#aedit_solicit_org_modal_title").html("编辑牵头部门");
        $('#aedit_solicit_org_form')[0].reset();
        aedit_solicit_org_validator.resetForm();
        $('#aedit_solicit_org_form input[name="solicitOrgId"]').val("");

        $.ajax({
            url: ctx+'/aea/solicit/org/getSolicitOrgRelOrgInfoById.do',
            type: 'post',
            data: {'id': id},
            async: false,
            success: function (data) {
                if (data) {

                    loadFormData(true, '#aedit_solicit_org_form', data);
                }
            }
        });
    }else{
        swal('提示信息', "请选择需要编辑的数据！", 'info');
    }
}

function setSolicitOrgUser(id){

    if(!isEmpty(id)){
        var obj = parent.vm;
        if(obj!=null&&obj!=undefined){
            parent.vm.removeTab('牵头部门人员');
            var _jumpData = {
                'menuName': '牵头部门人员',
                'menuInnerUrl':ctx + '/aea/solicit/org/stageOrgUser.do?solicitOrgId='+id+'&isShowBack=false',
                'id':  '牵头部门人员'
            };
            parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
        }else{
            location.href = ctx + '/aea/solicit/org/stageOrgUser.do?solicitOrgId='+id+'&isShowBack=true';
        }
    }else{
        swal('提示信息', "请选择需要编辑的数据！", 'info');
    }
}

function deleteSolicitOrgById(id){

    if(!isEmpty(id)){

        swal({
            text: '此操作将移除配置的牵头部门数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/org/delSolicitOrgById.do',
                    type: 'post',
                    data: {
                        'id': id
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除牵头部门数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitOrgList();
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
function batchDelSolicitOrg(){

    var rows = $("#solicit_org_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].solicitOrgId);
        }
        swal({
            text: '此操作将批量移除配置的牵头部门数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/org/batchDelSolicitOrgByIds.do',
                    type: 'post',
                    data: {
                        'ids': ids.toString()
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除牵头部门数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitOrgList();
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