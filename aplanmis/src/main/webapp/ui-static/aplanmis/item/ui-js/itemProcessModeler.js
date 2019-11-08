commonQueryParams = {};

function processParam(params) {
    // $.extend(commonQueryParams, params);
    // var pageNum = (params.offset / params.limit) + 1;
    // commonQueryParams['pageNum'] = pageNum;
    // commonQueryParams['pageSize'] = params.limit;
    commonQueryParams['itemId'] = currentBusiId;
    return commonQueryParams;
}


function operate(value, row, index) {
    var activateBtn = '<a href="javascript:;" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="激活条件"><i class="la la-diamond"></i>' +
        '</a>';

    var editBtn = '<a href="javascript:chooseProcDef(\'' + row.modelId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="编辑流程"><i class="la la-edit"></i>' +
        '</a>';

    var editProcBtn = '<a href="javascript:;" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="编辑流程"><i class="la la-edit"></i>' +
        '</a>';

    var priBtn = '<a target="_blank" href="'+ctx+'/admin/act/tpl/app/elementAuthorize.do?appId='+ appId +'"'+
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="权限配置"><i class="la la-cog"></i>' +
        '</a>';

    var subprocessBtn = '<a href="'+
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="子流程配置"><i class="la la-cogs"></i>' +
        '</a>';

    var deleteBtn = '<a href="javascript:;" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';

    return activateBtn + editBtn + priBtn + subprocessBtn + deleteBtn;
}

function search() {
    commonQueryParams['keyword'] = $("#keyword").val();
    commonQueryParams['itemId'] = currentBusiId;
    // $("#item_process_design_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#item_process_design_tb").bootstrapTable('refresh');       //无参数刷新
}

function refresh() {
    search();
}

$(function () {
    $("#clearBtn").click(function() {
        $('#search_item_process_design')[0].reset();
        search();
    });

    $("#searchBtn").click(function () {
        search();
    });
});

function masterFormatter(value, row, index) {
    return row.isMaster == '1' ? "是" : "否";
}

function privFormater(value, row, index) {
    return row.enableAppPriv == '1' ? "启用" : "不启用";
}

//选择流程定义
function chooseProcDef(appProcDeModelId) {
    window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + appProcDeModelId+'/'+appId, '_blank');
    // window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + appProcDeModelId+'/'+appId, '_blank');
}