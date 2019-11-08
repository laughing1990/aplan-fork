var front_item_tb = null;
    part_form_tb = null,
    proj_check_tb = null,
    commonQueryParams = {},
    aedit_item_proj_check_form = null;

$(function() {

    // 初始化高度
    $('#mainContentPanel').css('height',$(document.body).height()-85);

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

    // 前置事项检测
    initFrontItemTable();
    // 前置扩展表检测
    initPartFormTable();
    // 前置项目信息检测
    initProjCheckTable();

});

// ====================== 前置事项 =============================

function initFrontItemTable() {

    var url = ctx + '/aea/item/front/proj/';
    front_item_tb = $('#front_item_tb').bootstrapTable({
        url: url,
        columns: frontItemColumns,
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: tbParam,
        sidePagination: 'server',
    });
}

function frontItemColumns(){

    var columns =  [
        {
            checkbox:true
        },
        {
            field: '事项名称',
            title: 'itemName',
            align: 'center',
            width: 350,
            formatter: itemNameFormatter
        },
        {
            field: '是否启用',
            title: 'isActive',
            width: 60,
            align: 'center'
        },
        {
            field: 'sortNo',
            title: '排序',
            width: 60,
            align: 'center'
        },
        {
            field: '操作',
            title: '_opt',
            align: 'center',
            width: 120,
            formatter: frontItemOptFormatter
        },
    ];
    return columns;
}

function itemNameFormatter(value, row, index){


}

function frontItemOptFormatter (value, row, index){

}

function tbParam(params) {

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
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function importFrontItem(){

}

function sortFrontItem(){

}

function batchDelFrontItem(){

}

function refreshFrontItem(){
    
}

function searchFrontItem(){
    
}

function clearSearchFrontItem(){

}

// ====================== 扩展表检测 =============================

function initPartFormTable() {

    var url = ctx + '/';
    part_form_tb = $('#part_form_tb').bootstrapTable({
        url: url,
        columns: partFormColumns,
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: tbParam,
        sidePagination: 'server',
    });
}

function partFormColumns(){

    var columns =  [
        {
            checkbox:true
        },
        {
            field: '扩展表名称',
            title: 'partformName',
            align: 'center',
            width: 350,
            formatter: partformNameFormatter
        },
        {
            field: '是否启用',
            title: 'isActive',
            width: 60,
            align: 'center'
        },
        {
            field: 'sortNo',
            title: '排序',
            width: 60,
            align: 'center'
        },
        {
            field: '操作',
            title: '_opt',
            align: 'center',
            width: 120,
            formatter: partFormOptFormatter
        },
    ];
    return columns;
}

function partformNameFormatter(value, row, index){

}

function partFormOptFormatter(value, row, index){

}

function importPartForm(){

}

function sortPartForm(){

}

function batchDelPartForm(){

}

function refreshPartForm(){

}

function searchPartForm(){

}

function clearSearchPartform(){

}

// ====================== 项目信息检测 =============================

function initProjCheckTable() {

    var url = ctx + '/';
    proj_check_tb = $('#proj_check_tb').bootstrapTable({
        url: url,
        columns: projCheckColumns,
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: tbParam,
        sidePagination: 'server',
    });
}

function projCheckColumns(){

    var columns =  [
        {
            checkbox:true
        },
        {
            field: '规则名称',
            title: 'ruleName',
            align: 'left',
            width: 300,
        },
        {
            field: '规则条件表达式',
            title: 'ruleEl',
            align: 'left',
            width: 300,
        },
        {
            field: '备注',
            title: 'frontProjMemo',
            align: 'left',
            width: 250,
        },
        {
            field: '是否启用',
            title: 'isActive',
            width: 60,
            align: 'center',
            formatter: projCheckIsActiveFormatter
        },
        {
            field: 'sortNo',
            title: '排序',
            width: 60,
            align: 'center'
        },
        {
            field: '操作',
            title: '_opt',
            align: 'center',
            width: 120,
            formatter: frontProjCheckOptFormatter
        },
    ];
    return columns;
}

/**
 * 是否启用
 *
 * @param value
 * @param row
 * @param index
 */
function projCheckIsActiveFormatter(value, row, index){

}

/**
 * 项目检测操作
 *
 * @param value
 * @param row
 * @param index
 */
function frontProjCheckOptFormatter(value, row, index){

}

/**
 * 新增
 */
function addProjCheck(){

}

/**
 * 排序
 */
function sortProjCheck(){

}

/**
 * 批量删除
 */
function batchDelProjCheck(){

}

/**
 * 刷新
 */
function  refreshProjCheck(){

}

/**
 * 查询
 */
function searchProjCheck(){
    
}

/**
 * 清空查询
 */
function clearSearchProjCheck(){

}