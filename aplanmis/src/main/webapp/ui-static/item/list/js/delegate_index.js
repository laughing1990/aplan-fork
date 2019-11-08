var commonQueryParams = [], aedit_item_validator, customTable_tree;
var treegridCurrentPageNumber = 1;  //记录当前页面
var treegridCurrentPageSize = 10;   //记录页面大小
var privCurrentPageNumber = 1;
var privCurrentPageSize = 10;
var modalLevel = 1;//设置Bootstrap，多个modal展示的前后z-index
var add_item_priv_validator;
var add_mult_item_priv_validator;
var privTable;
var privQueryParams = [];
$(function() {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    // 组织机构事项列表
    $('#all_item_list_tb').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    initItemBasicTreeGrid();

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

    $('#searchItemType').change(function(){
        searchAllItemList();
    });

});

function initItemBasicTreeGrid() {

    var url = ctx + '/aea/item/priv/listUsedAeaItemBasicTreeByPage.do?';
    customTable_tree = $('#all_item_list_tb').bootstrapTable({
        url: url,
        columns: getItemBasicTreeGridColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            treegridCurrentPageNumber = number;
            treegridCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: allItemListParam,
        sidePagination: 'server',
        treeShowField: 'itemCode',
        parentIdField: 'pid',
        idField: 'itemId',
        singleSelect:true,
        clickToSelect: true,
        onPreBody:function (data) {
            if(data.length > 0){
                var ids = [];
                $(data).each(function (i,item) {
                    ids.push(item.itemId);
                    item.pid = item.parentItemId;   //pid属性用于树表格显示
                });
                $(data).each(function (i,item) {
                    if(item.pid != null && $.inArray(item.pid, ids) == -1){ //父节点不在返回数据中的，设置pid为null
                        item.pid = null;
                    }
                });
            }
        },
        onLoadSuccess: function(result) {
            $('#all_item_list_tb').treegrid({
                initialState: 'collapsed',// 所有节点都折叠
                treeColumn: 1,
                onChange: function() {
                    $('#all_item_list_tb').bootstrapTable('resetWidth');
                }
            });
            var select= $("#all_item_list_tb").bootstrapTable('getSelections');
            if(select!=''){
                $("#itemVerId").val(select[0].itemVerId);
                loadPrivTable(select[0].itemVerId);
            }


        },
        onClickRow:function(row,$element,field){
            if(field=='operate_'){
                if($element.children().first().children().prop('checked')==true) {//判断状态是否选中

                }else{
                    var rowindex = document.getElementsByClassName("selected");
                    $(rowindex).removeClass("selected");
                    $("#all_item_list_tb").bootstrapTable('uncheckAll');
                    $element.children().first().children().prop("checked",true);
                    $($element).addClass('selected');//添加class
                }
            }

        },
        onCheck:function(row,$element){
            if($element.children().children().prop('checked')==true){//判断状态是否选中
                $element.children().first().children().prop("checked",false);
                $($element).removeClass('selected');//移除class
            }else{
                $element.children().first().children().prop("checked",true);
                $($element).addClass('selected');//添加class
                $("#itemVerId").val(row.itemVerId);
                reloadPrivTable(row.itemVerId);
            }

        },
    });
}

function loadPrivTable(itemVerId){

    var privUrl = ctx + '/aea/item/priv/getPrivListByItemId.do?';
    var keyword = $("input[name='privKeyword']").val();
    privQueryParams.push({name: "itemVerId", value: itemVerId});
    if(keyword!=null&&typeof(keyword)!="undefined"){
        privQueryParams.push({name: "keyword", value: keyword});
    }else{
        privQueryParams.push({name: "keyword", value: ''});
    }

    privTable = $('#all_priv_list_tb').bootstrapTable({
        url: privUrl,
        columns: getPrivColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            privCurrentPageNumber = number;
            privCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: allPrivListParam,
        sidePagination: 'server',
        idField: 'privId',
        singleSelect: false,
        clickToSelect: true,
    });
}

function reloadPrivTable(itemVerId){

    privQueryParams.push({name: "itemVerId", value: itemVerId});
    var keyword = $("input[name='privKeyword']").val();
    if(keyword!=null&&typeof(keyword)!="undefined"){
        privQueryParams.push({name: "keyword", value: keyword});
    }else{
        privQueryParams.push({name: "keyword", value: ''});
    }
    privTable.bootstrapTable('refresh');
}

//定义树表格的显示列
var getItemBasicTreeGridColumns = function () {

    var columns =  [
        {
            field: 'checkbox',
            checkbox: true,
            formatter:function(value,row,index){
                if (index == 0){
                    $("#itemVerId").val(row.itemVerId);
                    return {
                        disabled : false,//设置是否可用
                        checked : true//设置选中
                    };
                }
                return value;
            }
        },
        {
            field: 'Number',
            title: '序号',
            align: 'center',
            width: 10,
            formatter: function (value, row, index) {
                if(row.pid != null)return '';
                return treegridCurrentPageSize * (treegridCurrentPageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {
            field: 'itemName',
            title: '事项名称',
            align: 'left',
            width: 200
        },
        {
            field: 'orgName',
            title: '部门名称',
            width: 200,
            align: 'left'
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


//定义下方地区和部门的显示列
var getPrivColumns = function () {

    var columns =  [
        {
            checkbox: true
        },
        {
            field: 'Number',
            title: '序号',
            align: 'center',
            width: 50,
            formatter: function (value, row, index) {
                return privCurrentPageSize * (privCurrentPageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {
            field: 'regionName',
            title: '区域名称',
            align: 'center',
            width: 150
        },
        {
            field: 'orgName',
            title: '部门名称',
            width: 150,
            align: 'center'
        },
        {
            field:'privOperate_',
            align:'center',
            title:'操作',
            width: 80,
            formatter: privOperatorFormatter
        }
    ];
    return columns;
}


function itemOperatorFormatter(value, row, index) {

    var itemTitle = "编辑";
    var icoCSS = "la la-edit";
    if(row.itemVerStatus == "PUBLISHED"||row.itemVerStatus =="DEPRECATED"){
        itemTitle = "查看";
        icoCSS = "la la-search";
    }

    var addItemPriv = '<a href="javascript:addItemPriv(\'' + row.itemId +  '\',\'' + row.itemVerId +'\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
        'title="事项下放"><i class="la la-arrow-circle-down"></i>' +
        '</a>';

    return addItemPriv;
}

function privOperatorFormatter(value, row, index) {


    var deletePriv = '<a href="javascript:deletePrivById(\'' + row.privId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';

    return deletePriv;
}

function allItemListParam(params) {

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

function allPrivListParam(params) {

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
    if (privQueryParams) {
        for (var i = 0; i < privQueryParams.length; i++) {
            if(typeof(privQueryParams[i].value)!="undefined"){
                buildParam[privQueryParams[i].name] = privQueryParams[i].value.trim();
            }

        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchAllItemList(){

    var params = $('#search_all_item_list_form').serializeArray();
    commonQueryParams = [];
    // commonQueryParams.push({name: 'itemNature', value: itemNature});
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#all_item_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 查询
function searchPrivList(){
    var itemVerId = $("#itemVerId").val();
    loadPrivTable(itemVerId);
    $("#all_priv_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 清空
function clearSearchAllItemList(){

    $('#search_all_item_list_form')[0].reset();
    $("#search_all_item_list_form input[name='orgId']").val('');
    searchAllItemList();
}

function clearSearchPrivList(){

    $(" input[name='privKeyword']").val('');
    var itemVerId = $("#itemVerId").val();
    reloadPrivTable(itemVerId);
}

// 刷新
function refreshAllItemList(){

    searchAllItemList();
}

function deleteAllPriv(){
    var select= $("#all_priv_list_tb").bootstrapTable('getSelections');
    var privIds = new Array();
    if(select.length>0){
        for(var i in select){
            privIds.push(select[i].privId);
        }
    }
    if(privIds.length>0){
        var msg = '此操作将批量删除事项下放数据，您确定执行吗？';
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
                    url: ctx + '/aea/item/priv/deleteAeaItemPrivByIds.do',
                    dataType: 'json',
                    data: {'privIds': privIds.toString()},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            var itemVerId = $("#itemVerId").val();
                            reloadPrivTable(itemVerId);
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
        swal('提示信息', '请勾选需要删除的记录！', 'info');
    }
}

function deletePrivById(privId){

    if(privId){
        var msg = '此操作将批量删除事项下放数据，您确定执行吗？';
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
                    url: ctx + '/aea/item/priv/deleteAeaItemPrivById.do',
                    dataType: 'json',
                    data: {'privId': privId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            var itemVerId = $("#itemVerId").val();
                            reloadPrivTable(itemVerId);
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
        swal('提示信息', '请选择需要删除的记录！', 'info');
    }
}
