//参数设置
var commonQueryParams;

function queryParams(params) {
    //组装分页参数
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

$(function () {
    // 设置初始化校验
    $('#purchase_list_tb').bootstrapTable({
        url: ctx + '/supermarket/purchase/list.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        striped: true, // 隔行加亮
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 10
        pageNumber: 1,
        pageList: [10, 20, 30, 50, 100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns: false,
        smartDisplay: true,
        singleSelect: true,
        clickToSelect: true, // 单击行即可以选中
        sortName: 'createTime', // 设置默认排序为 name
        sortOrder: 'desc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",
        queryParams: queryParams
    });
});

// 条件查询服务列表
function searchProjPurchase() {

    var params = $('#searchForm').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#purchase_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    // $("#purchase_list_tb").bootstrapTable('refresh');
}


function indexFormatter(value, row, index) {
    var pageSize = $('#purchase_list_tb').bootstrapTable('getOptions').pageSize;
    var pageNumber = $('#purchase_list_tb').bootstrapTable('getOptions').pageNumber;
    return pageSize * (pageNumber - 1) + index + 1;;
}

function operatorFormatter(value, row, index) {
    var editBtn = '<a href="' + ctx + '/supermarket/purchase/getProPurchase.do?projPurchaseId=' + row.projPurchaseId + '" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    return editBtn;
}
