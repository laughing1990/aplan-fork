$(function(){

    $('#mainContentPanel').css('height',$(document.body).height()-6);

    $('#service_window_table').bootstrapTable('resetView',{
        height: $('#mainContentPanel').height()- 125
    });

    // 设置弹窗大小
    $('#choose_item_service_window_modal .modal-lg').css('max-width',$('#mainContentPanel').width()*0.60);

    // $('#bstableId').bootstrapTable('resetView',{
    //     height: 400
    // });

    // $('#selectServiceWindDiv').niceScroll({
    //
    //     cursorcolor: "#e2e5ec",//#CC0071 光标颜色
    //     cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
    //     cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
    //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //     cursorwidth: "4px", //像素光标的宽度
    //     cursorborder: "0", //   游标边框css定义
    //     cursorborderradius: "2px",//以像素为光标边界半径
    //     autohidemode: true  //是否隐藏滚动条
    // });

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

    $('#selectWindowBtn').click(function(){
        if(serviceWindowTable) {
            var rows = serviceWindowTable.getSelections();
            var arr = [];
            for(var i = 0;i<rows.length;i++){
                arr.push(rows[i].windowId);
            }
            if (arr != null && arr.length > 0) {
                $.ajax({
                    url: ctx + '/aea/par/stage/batchSaveStageWindows.do',
                    type: 'POST',
                    data: {
                        "stageId": stageId,
                        "windowIds": arr.toString(),
                    },
                    async: false,
                    success: function (result) {
                        if (result.success) {
                            $('#choose_item_service_window_modal').modal('hide');
                            swal({
                                title: '提示信息',
                                text: '导入成功!',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            searchServiceWindowRel();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            } else {
                swal('提示信息', '请先选择服务窗口', 'info');
            }
        }else{
            swal('提示信息', '请先选择服务窗口', 'info');
        }
    });
});

// 关键字查询
function dealQueryParams (params) {

    var d = {};
    var t = $('#service_window_form').serializeArray();
    $.each(t, function() {
        d[this.name] = this.value;
    });
    var pageNum = (params.offset / params.limit) + 1;
    d['pageNum'] = pageNum,
    d['pageSize'] = params.limit;
    return d;
}

// 查询
function searchServiceWindowRel() {

    $('#service_window_table').bootstrapTable('refresh');
    $("#service_window_table").bootstrapTable('selectPage',1);
}

// 清空
function clearSearchServiceWindowRel(){

    $('#service_window_form')[0].reset();
    $('#service_window_table').bootstrapTable('refresh');
}

// 刷新
function refreshServiceWindowRel(){

    $('#service_window_table').bootstrapTable('refresh');
}

// 删除依据
function deleteWindowRelById(windowRelId){

    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }
    if(windowRelId){
        swal({
            title: '提示信息',
            text: '您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/par/stage/delAeaServiceWindowStageById.do',
                    type: 'POST',
                    data:{'windStageId': windowRelId},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchServiceWindowRel();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择数据！', 'info');
    }
}

// 批量删除
function batchDelete(curIsEditable){

    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }
    var checkBoxs = $("#service_window_table").bootstrapTable('getSelections');
    var ids = [];
    if(checkBoxs!=null&&checkBoxs.length>0){
        for(var i=0;i<checkBoxs.length;i++){
            ids.push(checkBoxs[i].windStageId);
        }
        swal({
            title: '提示信息',
            text: '您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/par/stage/batchDelAeaServiceWindowStageByIds.do',
                    type: 'POST',
                    data:{
                        'windStageIds': ids.toString()
                    },
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchServiceWindowRel();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择数据！', 'info');
    }
}

function operatorFormatter(value, row, index, field) {

    var operatorStr = '';
    if(curIsEditable){
        operatorStr =  operatorStr + '<span><a href="javascript:deleteWindowRelById(\'' + row.windStageId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a></span>';
    }
    return operatorStr;
}

var serviceWindowTable;
var windowQueryParams = [];
function openServiceWindow(curIsEditable) {

    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }
    showServiceWindow();
}

function showServiceWindow() {

    if(serviceWindowTable){
        windowQueryParams = [];
        windowQueryParams['stageId'] = stageId;
        serviceWindowTable.setQueryParams(windowQueryParams);
        serviceWindowTable.clear();
    }else {
        serviceWindowTable = defaultBootstrap("bstableId",[
            {
                checkbox: true,
                field: '#',
                align: "center",
                title: '序号', // 页面字段显示
                sortable: false, // 禁用排序
                width: 10,  // 宽度,单位: px
                textAlign: 'center', // 字段列标题和数据排列方式
                selector: {class: 'm-checkbox--solid m-checkbox--brand'},
            },
            {
                field: "windowId",
                visible: false
            },
            {
                field: "windowName",
                title: "窗口名称",
                textAlign: 'center',
                width: 500,
                textAlign: 'left',
                sortable: false
            },
            {
                field: "linkPhone",
                title: "窗口电话",
                textAlign: 'center',
                width: 500,
                textAlign: 'left',
                sortable: false
            },
            {
                field: "windowAddress",
                title: "窗口地址",
                textAlign: 'center',
                width: 500,
                textAlign: 'left',
                sortable: false
            }
        ],ctx + '/aea/service/window/listStageUnselectedWindowByPage.do');
        windowQueryParams = [];
        windowQueryParams['stageId'] = stageId;
        serviceWindowTable.setQueryParams(windowQueryParams)
            .setRowId("windowId").setClearBtn("clearBrn")
            .setSearchBtn("searchBrn","windowKeyword").init();
    }
    $("#choose_item_service_window_modal").modal("show");
}