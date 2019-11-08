var parOneformTable;
var parOneformCurrentPageNumber = 1;  //记录当前页面
var parOneformCurrentPageSize = 10;   //记录页面大小
var commonQueryParams = [];
var editParOneformValidator;

$(function() {

    initParOneformTable();
    // 设置初始化校验
    editParOneformValidator = $("#edit_oneform_form").validate({

        // 定义校验规则
        rules: {
            oneformName: {
                required: true,
                maxlength: 100
            },
            oneformDesc: {
                required: true,
                maxlength: 500
            },
            oneformUrl: {
                required: true,
                maxlength: 500
            },
            sortNo: {
                digits:true,
                maxlength: 100
            }
        },
        messages: {
            oneformName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过100个字母!"
            },
            oneformDesc: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            oneformUrl: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            sortNo: {
                digits: '<font color="red">必须输入整数！</font>',
                maxlength: "长度不能超过100个字母!"
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveParOneform').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");
            var sortNo = $("#sortNo").val();

            var form = new FormData(document.getElementById("edit_oneform_form"));
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/par/saveParOneform.do',
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
                            $('#saveParOneform').show();
                            $('#edit_oneform_modal').modal('hide');
                            // 列表数据重新加载
                            refreshOneformTable();

                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveParOneform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveParOneform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
});

function initParOneformTable() {

    var url = ctx + '/aea/par/listOneform.do?';
    parOneformTable = $("#all_oneform_list_tb").bootstrapTable({
        url: url,
        columns: getOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            parOneformCurrentPageNumber = number;
            parOneformCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: oneformParam,
        sidePagination: 'server',
        idField: 'oneformId',
        clickToSelect: true,
        singleSelect: false,
    });
}

var getOneformColumns = function () {

    var columns =  [
        {
            checkbox:true
        },
        {
            field: 'oneformName',
            title: '表单名称',
            align: 'center',
            width: 200
        },
        {
            field: 'oneformDesc',
            title: '表单描述',
            width: 200,
            align: 'center'
        },
        {
            field: 'oneformUrl',
            title: '表单加载地址',
            width: 200,
            align: 'center'
        },
        {
            field: 'sortNo',
            title: '排序字段',
            width: 100,
            align: 'center'
        },
        {
            field:'operate_',
            align:'center',
            title:'操作',
            width: 80,
            formatter: itemOperatorFormatter
        }
    ];
    return columns;
}

function oneformParam(params) {

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




function itemOperatorFormatter(value, row, index) {

    var updateParOneform = '<a href="javascript:editParOneformById(\'' + row.oneformId +  '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
        'title="编辑"><i class="la la-edit"></i>' +
        '</a>';
    var deleteParOneform = '<a href="javascript:deleteOneformById(\'' + row.oneformId +  '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';


    return updateParOneform+deleteParOneform;
}

function editParOneformById(oneformId) {

    $("#edit_oneform_modal").modal("show");
    $("#edit_oneform_modal_title").html("编辑总表");
    $('#edit_oneform_form')[0].reset();
    if(editParOneformValidator!=null){
        editParOneformValidator.resetForm();
    }
    $('#edit_oneform_form input[name="oneformId"]').val("");

    if(oneformId){
        $.ajax({
            url: ctx + '/aea/par/getAeaParOneform.do',
            type: 'POST',
            data: {'id': oneformId},
            async: false,
            success: function (data) {
                loadFormData(true, '#edit_oneform_form', data);
            },
            error: function () {
                swal('错误信息', "获取服务类型信息失败！", 'error');
            }
        });
    }

}

// 刷新页面
function refreshOneformTable(){

    parOneformTable.bootstrapTable('refresh');
    $("#all_oneform_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#edit_oneform_form input[name='oneformId']").val('');
}

function addParOneform(){

    $("#edit_oneform_modal").modal("show");
    $("#edit_oneform_modal_title").html("新增总表");
    $('#edit_oneform_form')[0].reset();
    if(editParOneformValidator!=null){
        editParOneformValidator.resetForm();
    }
    $('#edit_oneform_form input[name="oneformId"]').val("");
    $.ajax({
        url: ctx + '/aea/par/getMaxSortNo.do',
        type: 'POST',
        async: false,
        success: function (data) {
            $('#edit_oneform_form input[name="sortNo"]').val(data);
        },
        error: function () {
            swal('错误信息', "获取服务类型信息失败！", 'error');
        }
    });
}

// 查询
function searchAllOneformList(){

    var keyword = $("#keyword").val();
    commonQueryParams = [];
    if (keyword != '') {

        commonQueryParams.push({name: "keyword", value: keyword});
    }
    $("#all_oneform_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 清空
function clearSearchAllOneformList(){

    $("#keyword").val('');
    commonQueryParams = [];
    parOneformTable.bootstrapTable('refresh');
}


function deleteOneformById(oneformId) {

    if(oneformId){
        var msg = '此操作将删除此总表数据，您确定执行吗？';
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
                    url: ctx + '/aea/par/deleteOneformById.do',
                    dataType: 'json',
                    data: {'id': oneformId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            refreshOneformTable();
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

function delMulParOneform(){

    var rows = $("#all_oneform_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].oneformId);
        }
        swal({
            text: '此操作将删除选择的总表数据,您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    type: "POST",
                    url: ctx + '/aea/par/deleteMulOneformByIds.do',
                    data: {'ids': ids.toString()},
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal({
                                    type: 'success',
                                    title: '批量删除成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                refreshOneformTable();
                            },500);
                        } else {

                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}


