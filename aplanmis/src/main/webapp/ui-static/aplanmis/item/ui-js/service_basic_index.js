$(function(){

    $('#mainContentPanel').css('height',$(document.body).height()-6);
    $('#service_basic_Table').bootstrapTable('resetView',{
        height: $('#mainContentPanel').height()- 125
    });
});

// 关键字查询
function dealQueryParams (params) {

    var d = {};
    var t = $('#service_basic_form').serializeArray();
    $.each(t, function() {
        d[this.name] = this.value;
    });
    var pageNum = (params.offset / params.limit) + 1;
    d['pageNum'] = pageNum,
    d['pageSize'] = params.limit;
    return d;
}

// 查询
function searchCond() {

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


// 新增
function addItem(){

}

// 编辑
function editItemById(itemId){


}

// 删除依据
function deleteBasicById(basicId){
    if(basicId!=null){
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
                                timer: 1000,
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
function batchDelete(){

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
                                timer: 1000,
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
    var operatorStr =  '<span><a href="javascript:deleteBasicById(\'' + row.basicId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a></span>';
    return operatorStr;
}

function indexNum (value, row, index) {
    return index + 1;
}
