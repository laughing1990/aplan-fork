var commonQueryParams = [],
    aedit_solicit_org_validator,
    customTable_tree;
$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 20);

    // 初始征求部门
    initSolicitOrgTb();

    // 初始化校验
    initValidateSolicitOrg();

    // 组织机构事项列表
    $('#solicit_org_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 116
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

});

function initValidateSolicitOrg(){

    // 设置初始化校验
    aedit_solicit_org_validator = $("#aedit_solicit_org_form").validate({

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
                            title: '保存成功！',
                            showConfirmButton: false,
                            timer: 1000
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

function initSolicitOrgTb() {

    var url = ctx+'/aea/solicit/org/listAeaSolicitOrgRelOrgInfoByPage.do';
    customTable_tree = $('#solicit_org_list_tb').bootstrapTable({
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
            title: '征求部门名称',
        },
        {
            field: 'orgCode',
            title: '征求部门编号',
            align: 'left',
            width: 250,
        },
        {
            field: 'busType',
            title: '征求业务类型',
            align: 'center',
            width: 100,
            formatter: solicitBusTypeFormatter
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

    var delSolicitOrgBtn = '<a href="javascript:deleteSolicitOrgById(\'' + row.solicitOrgId + '\')" ' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                'title="移除"><i class="la la-trash"></i>' +
                            '</a>';

    return editBtn + delSolicitOrgBtn;

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
        $("#aedit_solicit_org_modal_title").html("编辑征求部门");
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

function deleteSolicitOrgById(id){

    if(!isEmpty(id)){

        swal({
            text: '此操作将移除配置的征求部门数据，您确定执行吗？',
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
                                title: '移除征求部门数据成功！',
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
            text: '此操作将批量移除配置的征求部门数据，您确定执行吗？',
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
                                title: '移除征求部门数据成功！',
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