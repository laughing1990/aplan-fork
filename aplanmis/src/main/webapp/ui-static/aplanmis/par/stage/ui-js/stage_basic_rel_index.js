$(function(){

    $('#mainContentPanel').css('height',$(document.body).height()-4);

    $('#service_basic_Table').bootstrapTable('resetView',{
        height: $('#mainContentPanel').height()- 115
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

    $("#view_legal_clause_scroll").niceScroll({

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

// 关键字查询
function dealQueryParams (params) {

    var d = {};
    var t = $('#service_basic_form').serializeArray();
    $.each(t, function() {
        d[this.name] = this.value;
    });
    d['tableName'] = relTbName;
    d['pkName'] = relPkName;
    d['recordId'] = recordId;
    var pageNum = (params.offset / params.limit) + 1;
    d['pageNum'] = pageNum,
    d['pageSize'] = params.limit;
    return d;
}

// 查询
function searchCond() {

    // $('#stage_common_mat_list_tb').bootstrapTable('hideColumn', 'basicId');
    $('#service_basic_Table').bootstrapTable('refresh');
    $("#service_basic_Table").bootstrapTable('selectPage',1);
}

// 清空
function clearSearchCond(){

    $('#service_basic_form')[0].reset();
    $('#service_basic_Table').bootstrapTable('refresh');
}

// 刷新
function refreshBasic(){

    $('#service_basic_Table').bootstrapTable('refresh');
}

function viewBasicById(id){

    $('#view_legal_clause_modal').modal('show');
    $('#view_legal_clause_modal_title').html('查看法规条款');
    $('#view_legal_clause_form')[0].reset();
    $.ajax({
        url: ctx + '/aea/item/service/basic/getAeaItemServiceBasic.do',
        type: 'post',
        data: {'id': id},
        async: false,
        success: function (data) {
            // 记载表单数据
            loadFormData(true, '#view_legal_clause_form', data);
            if(data!=null&&data.exeDate) {
                $("#view_legal_clause_form input[name='exeDate']").val(new Date(data.exeDate).format("yyyy-MM-ddThh:mm:ss"));
            }else{
                $("#view_legal_clause_form input[name='exeDate']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}


// 删除依据
function deleteBasicById(basicId){

    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }
    if(basicId){
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
                    url: ctx+'/aea/item/service/basic/deleteAeaItemServiceBasicById.do',
                    type: 'POST',
                    data:{'id': basicId},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchCond();
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

    var checkBoxs = $("#service_basic_Table").bootstrapTable('getSelections');
    var ids = [];
    if(checkBoxs!=null&&checkBoxs.length>0){
        for(var i=0;i<checkBoxs.length;i++){
            ids.push(checkBoxs[i].basicId);
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
                    url: ctx+'/aea/item/service/basic/batchDeleteServiceBasic.do',
                    type: 'POST',
                    data:{'ids': ids.toString()},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchCond();
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

    var viewBtn = '<a href="javascript:viewBasicById(\'' + row.basicId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-search"></i></a>';
    var delBtn =  '<a href="javascript:deleteBasicById(\'' + row.basicId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a></span>';
    if(curIsEditable){
        return viewBtn + delBtn;
    }else{
        return viewBtn;
    }
}

function exeDateFormatter(value, row, index, field) {

    var dateVal = value + "";
    if (value != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return date.getFullYear() + "-" + month + "-" + currentDate;
    }
    return "";
}


function clauseContentFormatter(value, row, index, field) {

    if(value) {
        var operatorStr = "<div style='overflow:hidden;white-space: nowrap; text-overflow: ellipsis;max-width:200px;' title='"+value+"'>"+value+"</div>";
        return operatorStr;
    }
    return "";
}

function indexNum (value, row, index) {

    return index + 1;
}
