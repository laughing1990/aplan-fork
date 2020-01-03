var commonQueryParams = [],
    solicit_org_user_tb;

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 20);

    // 初始化牵头人员表格
    initSolicitOrgUserTb();

    // 牵头组织列表
    $('#solicit_org_user_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 165
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

// =============  牵头人员 ==================

function initSolicitOrgUserTb() {

    var url = ctx+'/aea/solicit/org/user/listAeaSolicitOrgUserRelInfoByPage.do';
    solicit_org_user_tb = $('#solicit_org_user_list_tb').bootstrapTable({
        url: url,
        columns: getSolicitOrgUserColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: solicitOrgUserParam,
        sidePagination: 'server',
        singleSelect: false,
        clickToSelect: true,
    });
}

//定义表格的显示列
var getSolicitOrgUserColumns = function () {

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
            formatter: solicitOrgUserIsActiveFormatter
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 100,
            formatter: solicitOrgUserFormatter
        }
    ];
    return columns;
}

function solicitOrgUserIsActiveFormatter(value, row, index, field) {

    if(value=='1'){

        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" onclick="changeSolicitOrgUserIsActive(this, \''+ row.orgUserId +'\', \''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }else{

        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" onclick="changeSolicitOrgUserIsActive(this, \''+ row.orgUserId +'\', \''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

function changeSolicitOrgUserIsActive(obj, id, isActive){

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
                    url: ctx+"/aea/solicit/org/user/changeIsActive.do",
                    type: 'POST',
                    data: {"id": id},
                    success: function (result) {
                        if(result.success){
                            searchSolicitOrgUserList();
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

function solicitOrgUserFormatter(value, row, index, field) {

    var delBtn = '<a href="javascript:deleteSolicitOrgUserById(\'' + row.orgUserId + '\')" ' +
                     'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                     'title="移除"><i class="la la-trash"></i>' +
                 '</a>';

    return delBtn;

}

function solicitOrgUserParam(params) {

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
    commonQueryParams.push({name: 'solicitOrgId', value: solicitOrgId});
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchSolicitOrgUserList() {

    commonQueryParams = [];
    var params = $('#search_solicit_org_user_form').serializeArray();
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#solicit_org_user_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#solicit_org_user_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchSolicitOrgUserList() {

    $('#search_solicit_org_user_form')[0].reset();
    searchSolicitOrgUserList();
}

// 刷新
function refreshSolicitOrgUserList() {

    searchSolicitOrgUserList();
}

// 导入部门
function importSolicitUser(){

    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("加载数据中,请勿点击,耐心等候...");
    initSolicitOrgUserCheck();
    setTimeout(function () {
        $("#uploadProgress").modal('hide');
    }, 500);
}

// 排序
function sortSolicitOrgUser(){

}

function deleteSolicitOrgUserById(id){

    if(!isEmpty(id)){

        swal({
            text: '此操作将移除配置的牵头人员数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/org/user/delSolicitOrgUserById.do',
                    type: 'post',
                    data: { 'id': id },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除牵头人员数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitOrgUserList();
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
function batchDelSolicitOrgUser(){

    var rows = $("#solicit_org_user_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].orgUserId);
        }
        swal({
            text: '此操作将批量移除配置的牵头人员数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/solicit/org/user/batchDelSolicitOrgUserByIds.do',
                    type: 'post',
                    data: {
                        'ids': ids.toString()
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除牵头人员数据成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchSolicitOrgUserList();
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

function backSolicitOrg(){

    location.href = ctx + '/aea/solicit/org/stageOrg.do';
}