var SERVICE_WINDOW_URL_PREFIX = ctx + '/aea/service/window/';
var commonQueryParams = [], service_window_form_validator;
$(function() {

    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    $('#serviceWindowTable').bootstrapTable('resetView', {
        height: $('#mainContentPanel').height() - 116
    });

    $("#service_window_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#opusOmOrgDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#bscDicRegionDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

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

    $('#show_service_wind_att_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 初始化表单校验
    initValidate();

    // 双击事件
    $('#serviceWindowTable').on('dbl-click-row.bs.table', function (e, row, element) {
        editServiceWindowById(row.windowId);
    });

    // 选择行政区划
    $('#selectRegionBtn').click(function () {

        var isSelectRegionSearch = $('#isSelectRegionSearch').val();
        var bscDicRegionTree = $.fn.zTree.getZTreeObj("bscDicRegionTree");
        var nodes = bscDicRegionTree.getSelectedNodes();
        if (nodes != null && nodes.length > 0) {
            var selectNode = nodes[0];
            if(isSelectRegionSearch=='search'){
                $("#search_service_window_form input[name='regionId']").val(selectNode.id);
                $("#search_service_window_form input[name='regionName']").val(selectNode.name);
            }else{
                $("#service_window_form input[name='regionId']").val(selectNode.id);
                $("#service_window_form input[name='regionName']").val(selectNode.name);
            }
            closeSelectBscDicRegionZtree();
        } else {
            swal('提示信息', '请选择行政区划！', 'info');
        }
    });

    // 选择组织
    $('#selectOrgBtn').click(function () {

        var isSelectOrgSearch = $('#isSelectOrgSearch').val();
        var opusOmOrgTree = $.fn.zTree.getZTreeObj("opusOmOrgTree");
        var nodes = opusOmOrgTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            if(isSelectOrgSearch=='search'){
                $("#search_service_window_form input[name='orgId']").val(selectNode.id);
                $("#search_service_window_form input[name='orgName']").val(selectNode.name);
            }else{
                $("#service_window_form input[name='orgId']").val(selectNode.id);
                $("#service_window_form input[name='orgName']").val(selectNode.name);
            }
            closeSelectOpusOmOrgZtree();
        }else{
            swal('提示信息', '请选择机构！', 'info');
        }
    });

    $('#selectItemBtn').click(function(){

        var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0) {
            var windowId = rows[0].windowId;
            var itemVerIds = [];
            var liObjs = document.getElementsByName('selectItemLi');
            for (var i = 0; i < liObjs.length; i++) {
                itemVerIds.push($(liObjs[i]).attr('category-id'));
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置窗口事项,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: SERVICE_WINDOW_URL_PREFIX + 'saveAeaServiceWindowItem.do',
                        type: 'POST',
                        data: {'windowId': windowId, 'itemVerIds': itemVerIds.toString()},
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                swal({
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
            swal('提示信息', '请选择某个窗口！', 'info');
        }
    });

    $('#selectUserBtn').click(function(){

        var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0) {
            var windowId = rows[0].windowId;
            var userIds = [];
            var liObjs = document.getElementsByName('selectUserLi');
            for (var i = 0; i < liObjs.length; i++) {
                userIds.push($(liObjs[i]).attr('category-id'));
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新授权窗口人员,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: SERVICE_WINDOW_URL_PREFIX + 'saveAeaServiceWindowUser.do',
                        type: 'POST',
                        data: {'windowId': windowId, 'userIds': userIds.toString()},
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    text: '授权窗口人员成功！',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择某个窗口！', 'info');
        }
    });

    $('#selectStageBtn').click(function(){

        var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0) {
            var windowId = rows[0].windowId;
            var stageIds = [];
            var liObjs = document.getElementsByName('selectStageLi');
            for (var i = 0; i < liObjs.length; i++) {
                stageIds.push($(liObjs[i]).attr('category-id'));
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置窗口阶段,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: SERVICE_WINDOW_URL_PREFIX + 'saveAeaServiceWindowStage.do',
                        type: 'POST',
                        data: {'windowId': windowId, 'stageIds': stageIds.toString()},
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    text: '保存阶段成功！',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                            }
                        },
                        error: function () {
                            swal('错误信息', '保存阶段数据服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择某个窗口！', 'info');
        }
    });
});

// 初始化表单检验
function initValidate() {

    service_window_form_validator = $("#service_window_form").validate({
        // 定义校验规则
        rules: {
            windowName: {
                required: true,
                maxlength: 200
            },
            regionName: {
                required: true
            },
            orgName: {
                required:true
            },
            linkPhone: {
                required: true,
                maxlength: 200
            },
            workTime: {
                required: true,
                maxlength: 200
            },
            sortNo: {
                required: true
            },
            windowType: {
                required: true
            },
            isActive: {
                required: true
            },
            isPublic: {
                required: true
            },
            isAllItem: {
                required: true
            },
            isAllStage: {
                required: true
            },
            windowAddress:{
                required: true,
                maxlength: 500
            },
            trafficGuide:{
                required: true,
                maxlength: 2000
            },
            regionRange:{
                required: true,
            }
        },
        messages: {
            windowName: {
                required: '<font color="red">窗口名称必填！</font>',
                maxlength: '长度不能超过50个字母!',
            },
            regionName: {
                required: '<font color="red">行政区划必选！</font>'
            },
            orgName: {
                required: '<font color="red">所属部门必选！</font>'
            },
            linkPhone: {
                required: '<font color="red">联系电话必填！</font>',
                maxlength: '长度不能超过200个字母!',
            },
            workTime: {
                required: '<font color="red">办公时间必填！</font>',
                maxlength: '长度不能超过200个字母!',
            },
            sortNo: {
                required: '<font color="red">排列顺序号必填！</font>'
            },
            windowType: {
                required: '<font color="red">窗口类型必选！</font>'
            },
            isActive: {
                required: '<font color="red">是否启用必选！</font>'
            },
            isPublic: {
                required: '<font color="red">是否公开公示必选！</font>'
            },
            isAllItem: {
                required: '<font color="red">是否所有单项通办必选！</font>'
            },
            isAllStage: {
                required: '<font color="red">是否所有阶段通办必选！</font>'
            },
            windowAddress:{
                required: '<font color="red">窗口地址必填！</font>',
                maxlength: '长度不能超过500个字母!',
            },
            trafficGuide:{
                required: '<font color="red">交通指引必填！</font>',
                maxlength: '长度不能超过2000个字母!',
            },
            regionRange:{
                required: '<font color="red">办理范围必填！</font>',
            }
        },
        errorPlacement: function(error, element) {
            if (element.is(':radio')) {
                error.appendTo(element.parents('.m-radio-inline').parent());
            } else {
                error.appendTo(element.parent().hasClass('input-group') ? element.parent().parent() : element.parent());
            }
        },
        // 提交表单
        submitHandler: function () {

            $("#uploadProgress").modal("show");
            $('#save_service_wind_btn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var formData = new FormData(document.getElementById("service_window_form"));
            $.ajax({
                type: 'post',
                url: SERVICE_WINDOW_URL_PREFIX + 'saveAeaServiceWindow.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {

                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            $('#save_service_wind_btn').show();
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 隐藏模式框
                            $('#service_window_modal').modal('hide');
                            // 列表数据重新加载
                            searchServiceWindow();
                        },500);
                    } else {
                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            $('#save_service_wind_btn').show();
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $("#uploadProgress").modal('hide');
                        $('#save_service_wind_btn').show();
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function regionOrgNameFormatter(value, row, index, field) {

    return row.regionName+"/"+row.orgName;
}

function operatorFormatter(value, row, index, field) {

    var editBtn = '<a href="javascript:editServiceWindowById(\'' + row.windowId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    var delBtn =  '<a href="javascript:delServiceWindowById(\'' + row.windowId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a></span>';
    var setWindItem = '<a href="javascript:addWindowItem(\'' + row.windowId + '\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="窗口事项配置"><i class="la la-cog"></i>' +
                   '</a>';
    return editBtn + delBtn;
}

// 人员授权
function addUserWindow(){

    var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var windowId = rows[0].windowId;
        // 打开人员弹窗
        openSelectUserModal();
        // 渲染已经导入的数据
        loadSelectedUserData(windowId);
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

// 窗口事项
function addWindowItem(){

    var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var windowId = rows[0].windowId;
        // 打开事项弹窗
        openSelectItemModal();
        // 渲染已经导入的数据
        loadSelectedItemData(windowId);
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

function addStageWindow(){

    var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var windowId = rows[0].windowId;
        // 打开事项弹窗
        openSelectStageModal();
        // 渲染已经导入的数据
        loadSelectedStageData(windowId);
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

function loadSelectedStageData(windowId){

    $.ajax({
        url: SERVICE_WINDOW_URL_PREFIX + 'listWindowStageByWindowId.do',
        type: 'post',
        data: {'windowId': windowId},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectStageTree.getNodeByParam("id", data[i].stageId, null);
                    if (node) {
                        selectStageTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectStageLi" category-id="' + data[i].stageId + '">' +
                                    '<span class="drag-handle_td" onclick="removeSelectedStage(\'' + data[i].stageId + '\');">×</span>' +
                                    '<span class="org_name_td">' + data[i].stageName + '</span>' +
                                '</li>';
                    $('#selectedStageUl').append(liHtml);
                }
            }
        }
    });
}

// 勾选和渲染已经选择的事项
function loadSelectedItemData(windowId){

    $.ajax({
        url: SERVICE_WINDOW_URL_PREFIX + 'listWindowItemByWindowId.do',
        type: 'post',
        data: {'windowId': windowId},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectItemTree.getNodeByParam("itemVerId", data[i].itemVerId, null);
                    if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectItemLi" category-id="' + data[i].itemVerId + '">' +
                                    '<span class="drag-handle_td" onclick="removeSelectedItem(\'' + data[i].itemVerId + '\');">×</span>' +
                                    '<span class="org_name_td">' + data[i].itemName +'【'+ data[i].orgName+'】'+ '</span>' +
                                '</li>';
                    $('#selectedItemUl').append(liHtml);
                }
            }
        }
    });
}

// 勾选和渲染已经选择的人员
function loadSelectedUserData(windowId){

    $.ajax({
        url: SERVICE_WINDOW_URL_PREFIX + 'listWindowUserByWindowId.do',
        type: 'post',
        data: {'windowId': windowId},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择事项库树节点
                    var node = selectUserTree.getNodeByParam("id", data[i].userId, null);
                    if (node) {
                        selectUserTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的事项
                    var liHtml = '<li name="selectUserLi" category-id="' + data[i].userId + '">' +
                                    '<span class="drag-handle_td" onclick="removeSelectedUser(\'' + data[i].userId + '\');">×</span>' +
                                    '<span class="org_name_td">' + data[i].userName +'【'+ data[i].orgName+'】'+ '</span>' +
                                '</li>';
                    $('#selectedUserUl').append(liHtml);
                }
            }
        }
    });
}

// 关键字查询
function dealQueryParams (params) {

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

// 查询窗口数据
function searchServiceWindow(){

    var params = $('#search_service_window_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#serviceWindowTable").bootstrapTable('refresh');
    $("#serviceWindowTable").bootstrapTable('selectPage',1);
}

// 清空窗口数据
function clearSearchServiceWindow(){

    $('#search_service_window_form')[0].reset();
    $("#search_service_window_form input[name='orgId']").val('');
    $("#search_service_window_form input[name='regionId']").val('');
    searchServiceWindow();
}

// 刷新窗口数据
function refreshServiceWindow(){

    searchServiceWindow();
}

// 新增窗口
function addServiceWindow(){

    $("#service_window_modal").modal("show");
    $("#service_window_modal_title").html("新增服务窗口");
    $('#service_window_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#service_window_form')[0].reset();
    if(service_window_form_validator!=null){
        service_window_form_validator.resetForm();
    }
    clearServiceWindFile();
    $("#service_window_form input[name='windowId']").val('');
    $("#service_window_form input[name='isActive'][value='1']").prop("checked", true);
    $("#service_window_form input[name='isPublic'][value='1']").prop("checked", true);
    $("#service_window_form input[name='isAllItem'][value='1']").prop("checked", true);
    $("#service_window_form input[name='isAllStage'][value='1']").prop("checked", true);
    $("#service_window_form input[name='regionRange'][value='1']").prop("checked", true);
    $("#service_window_form select[name='windowType'] option:eq(1)").prop("selected", 'selected');
    
    // 窗口排序
    $.ajax({
        url: SERVICE_WINDOW_URL_PREFIX + 'getMaxSortNo.do',
        type: 'post',
        data: {},
        success: function (data) {
            $("#service_window_form input[name='sortNo']").val(data);
        }
    });
}

// 编辑
function editServiceWindowById(windowId){

    $("#service_window_modal").modal("show");
    $("#service_window_modal_title").html("编辑服务窗口");
    $('#service_window_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#service_window_form')[0].reset();
    if(service_window_form_validator!=null){
        service_window_form_validator.resetForm();
    }
    $("#service_window_form input[name='windowId']").val('');
    clearServiceWindFile();

    $.ajax({
        url: SERVICE_WINDOW_URL_PREFIX+'getAeaServiceWindowById.do',
        type: 'post',
        data: {'windowId': windowId},
        async: false,
        success: function (data) {
            if(data){
                if (data.mapAttNum>0) {
                    $('#mapAttFileDiv').show();
                    $('#mapAttFileButton').html(data.mapAttNum + "个附件");
                }else{
                    $('#mapAttFileDiv').hide();
                }
            }
            // 记载表单数据
            loadFormData(true, '#service_window_form', data);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

// 单个删除
function delServiceWindowById(windowId){

    if(windowId){
        var msg = '此操作将删除选择的服务窗口,确定要删除吗？';
        swal({
            title: '',
            text: msg,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    type: 'post',
                    url: SERVICE_WINDOW_URL_PREFIX + 'deleteAeaServiceWindowById.do',
                    dataType: 'json',
                    data: {
                        'windowId': windowId
                    },
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchServiceWindow();
                        } else {
                            swal('错误信息','删除失败','error');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

// 批量删除
function batchDelServiceWindow(){

    var rows = $("#serviceWindowTable").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var windowIds = [];
        for(var i=0;i<rows.length;i++){
            windowIds.push(rows[i].windowId);
        }
        var msg = '此操作将批量删除选择的服务窗口,确定要删除吗？';
        swal({
            title: '',
            text: msg,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    type: 'post',
                    url: SERVICE_WINDOW_URL_PREFIX + 'batchDeleteAeaServiceWindow.do',
                    dataType: 'json',
                    data: {
                        'windowIds': windowIds.toString()
                    },
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchServiceWindow();
                        } else {
                            swal('错误信息','删除失败','error');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

function isSelectBscDicRegion(obj, isSearch){

    if(isSearch){
        $('#isSelectRegionSearch').val('search');
    }else{
        $('#isSelectRegionSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectBscDicRegionZtree();
        }
    }else{
        selectBscDicRegionZtree();
    }
}

function isSelectOpuOmOrg(obj, isSearch){

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
            selectOpusOmOrgZtree();
        }
    }else{
        selectOpusOmOrgZtree();
    }
}

