var outItemMatTable;//事项输出材料表格
var outItemMatCurrentPageNumber = 1;  //记录当前页面
var outItemMatCurrentPageSize = 10;   //记录页面大小
var outItemMatQueryParams = [];
var inItemMatTable;//事项输入材料表格
var inItemMatCurrentPageNumber = 1;  //记录当前页面
var inItemMatCurrentPageSize = 10;   //记录页面大小
var inItemMatQueryParams = [];
var isInitInItemMatTable = 0;//判断是否已经初始化输入材料表格

$(function() {

    $('#all_out_mat_list_tb_div').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#all_in_mat_list_tb_div').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#saveOutItemBtn').click(function(){

        var outItemTree = $.fn.zTree.getZTreeObj("outItemTree");
        var isSelectOutItemSearch = $('#isSelectOutItemSearch').val();
        var nodes = outItemTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            if(isSelectOutItemSearch=='search'){
                $("#outItemVerId").val(selectNode.id);
                $("#search_all_out_item_list_form input[name='outItemName']").val(selectNode.name);
                refreshOutItemTable();
            }else{
                $("#outItemVerId").val(selectNode.id);
                $("#search_all_out_item_list_form input[name='outItemName']").val(selectNode.name);
            }
            closeSelectOutItemZtree();
        }else{
            swal('提示信息', '请选择输出事项！', 'info');
        }

    });

    $('#saveInItemBtn').click(function(){

        var inItemTree = $.fn.zTree.getZTreeObj("inItemTree");
        var isSelectInItemSearch = $('#isSelectInItemSearch').val();
        var nodes = inItemTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            if(isSelectInItemSearch=='search'){

                $("#inItemVerId").val(selectNode.id);
                $('#inItemStateVerId').val(selectNode.icon);
                $("#search_in_item_list_form input[name='inItemName']").val(selectNode.name);
                refreshInItemTable();

            }else{

                $("#inItemVerId").val(selectNode.id);
                $('#inItemStateVerId').val(selectNode.icon);
                $("#search_in_item_list_form input[name='inItemName']").val(selectNode.name);
            }
            closeSelectInItemZtree();
        }else{
            swal('提示信息', '请选择输入事项！', 'info');
        }
    });
});

//初始化事项输出材料列表
function initOutItemMatTable() {

    outItemMatQueryParams = [];
    var outItemVerId = $("#outItemVerId").val();
    outItemMatQueryParams.push({name: "itemVerId", value: outItemVerId});
    var url = ctx + '/aea/share/mat/listOutItemMat.do?';
    outItemMatTable = $("#all_out_mat_list_tb").bootstrapTable({
        url: url,
        columns: getOutItemMatColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            outItemMatCurrentPageNumber = number;
            outItemMatCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: outItemMatParam,
        sidePagination: 'server',
        idField: 'matId',
        clickToSelect: true,
        singleSelect: true,
        sortable: true,//是否启用排序
        sortOrder: "asc",//排序方式
        sortName:"sortNo",
        onLoadSuccess:function(){
            initCheckbox();
            if(isInitInItemMatTable==1){
                refreshInItemTable();
            }else if(isInitInItemMatTable==0){
                initInItemMatTable();
            }
        },
        onCheck:function(row,$element,field){

            if($element.children().first().children().prop('checked')!=true) {//判断状态是否选中
                var outInoutId = row.inoutId;
                $.ajax({
                    type: "get",
                    url: ctx + '/aea/share/mat/inOutCheckboxList.do',
                    async: false,
                    data: {'themeVerId':curThemeVerId, "outInoutId": outInoutId},
                    success: function (data) {
                        checkBoxInInoutIds = [];
                        if(data.length>0){
                            for(var i in data){
                                checkBoxInInoutIds.push(data[i].inInoutId);
                            }

                        }
                        refreshInItemTable();
                    }
                });
            }
        }
    });
}

//初始化事项输入材料列表
function initInItemMatTable() {

    inItemMatQueryParams = [];
    var inItemVerId = $("#inItemVerId").val();
    var inItemStateVerId = $("#inItemStateVerId").val();
    inItemMatQueryParams.push({name: "itemVerId", value: inItemVerId});
    inItemMatQueryParams.push({name: "stateVerId", value: inItemStateVerId});
    var url = ctx + '/aea/share/mat/listInItemMat.do?';
    inItemMatTable = $("#all_in_mat_list_tb").bootstrapTable({
        url: url,
        columns: getInItemMatColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            inItemMatCurrentPageNumber = number;
            inItemMatCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: inItemMatParam,
        sidePagination: 'server',
        idField: 'matId',
        clickToSelect: true,
        singleSelect: false,
        onLoadSuccess:function(){
            isInitInItemMatTable = 1;
        },
        formatNoMatches: function(){
            return "没有相关的匹配结果";
        },
        formatLoadingMessage: function(){
            return null;
        }
    });
}

var getOutItemMatColumns = function () {

    var columns =  [
        {
            checkbox: true,
            formatter: function (value, row, index) {
                if (index == 0) {
                    return {
                        disabled: false,//设置是否可用
                        checked: true//设置选中
                    };
                }
                return value;
            }
        },
        {
            field: 'matProp',
            title: '材料性质',
            width: 85,
            align: 'left',
            formatter: matPropormatter
        },
        {
            field: 'aeaMatCertName',
            title: '材料名称',
            align: 'left',
            width: 300,
            // formatter: matNameFormatter
        },
        {
            field: 'aeaMatCertCode',
            title: '材料编号',
            width: 300,
            align: 'left'
        },
    ];
    return columns;
}

function outItemMatParam(params) {

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
    if (outItemMatQueryParams) {
        for (var i = 0; i < outItemMatQueryParams.length; i++) {
            buildParam[outItemMatQueryParams[i].name] = outItemMatQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

var getInItemMatColumns = function () {

    var columns =  [
        {
            checkbox:true,
            formatter:function(value,row,index){
                var inInoutId = row.inoutId;
                var type = $.inArray(inInoutId, checkBoxInInoutIds);
                if (type != -1){
                    return {
                        disabled : false,//设置是否可用
                        checked : true//设置选中
                    };
                }
                return value;
            }

        },
        {
            field: 'matProp',
            title: '材料性质',
            width: 85,
            align: 'center',
            formatter: matPropormatter
        },
        {
            field: 'aeaMatCertName',
            title: '材料名称',
            align: 'left',
            width: 300,
            // formatter: matNameFormatter
        },
        
        {
            field: 'aeaMatCertCode',
            title: '材料编号',
            width: 300,
            align: 'left'
        },
    ];
    return columns;
}

function inItemMatParam(params) {

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
    if (inItemMatQueryParams) {
        for (var i = 0; i < inItemMatQueryParams.length; i++) {
            buildParam[inItemMatQueryParams[i].name] = inItemMatQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}


function isSelectInItem(obj, isSearch){

    if(isSearch){
        $('#isSelectInItemSearch').val('search');
    }else{
        $('#isSelectInItemSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectInItemZtree();
        }
    }else{
        selectInItemZtree();
    }
}

function isSelectOutItem(obj, isSearch){

    if(isSearch){
        $('#isSelectOutItemSearch').val('search');
    }else{
        $('#isSelectOutItemSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectOutItemZtree();
        }
    }else{
        selectOutItemZtree();
    }
}

//保存材料共享
function saveSharemat(){

    var outItemVerId = $('#outItemVerId').val();
    var inItemVerId = $('#inItemVerId').val();
    var inItemStateVerId = $('#inItemStateVerId').val();
    var outInoutId = null;
    var outRows = $("#all_out_mat_list_tb").bootstrapTable('getSelections');
    if(outRows!=null && outRows.length>0){
        outInoutId = outRows[0].inoutId;
    }
    var inInoutIds = [];
    var inRows = $("#all_in_mat_list_tb").bootstrapTable('getSelections');
    if(inRows!=null&&inRows.length>0){
        for(var i in inRows){
            inInoutIds.push(inRows[i].inoutId);
        }
    }
    if(outItemVerId&&outInoutId&&inItemVerId&&inItemStateVerId) {

        $.ajax({
            type: "get",
            url: ctx + '/aea/share/mat/saveSharemat.do',
            async: false,
            data: {
                'themeVerId': curThemeVerId,
                'outItemVerId': outItemVerId,
                'outInoutId': outInoutId,
                'inItemVerId': inItemVerId,
                'inItemStateVerId': inItemStateVerId,
                'inInoutIds': inInoutIds.toString()
            },
            success: function (data) {
                if (data.success) {
                    var title = "设置材料共享成功";
                    if(inInoutIds!=null&&inInoutIds.length>0){
                        title = "设置材料共享成功";
                    }else{
                        title = "解除材料共享成功";
                    }
                    swal({
                        type: 'success',
                        title: title,
                        showConfirmButton: false,
                        timer: 1000
                    });
                } else {
                    swal('错误信息','操作失败!','error');
                }
            }
        });
    }else if(!outItemVerId){
        swal('错误信息','请选择事项输出的事项!','error');
    }else if(!outInoutId){
        swal('错误信息','请勾选左侧的输出材料!','error');
    }else if(!inItemVerId||!inItemStateVerId){
        swal('错误信息','请选择事项输入的事项!','error');
    }
}

//刷新事项输出材料列表
function refreshOutItemTable(){

    outItemMatQueryParams = [];
    var outItemVerId = $("#outItemVerId").val();
    outItemMatQueryParams.push({name: "itemVerId", value:outItemVerId});
    outItemMatTable.bootstrapTable('refresh');
    $("#all_out_mat_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

//刷新事项输入材料列表
function refreshInItemTable(){

    initCheckbox();
    inItemMatQueryParams = [];
    var inItemVerId = $("#inItemVerId").val();
    var inItemStateVerId = $("#inItemStateVerId").val();
    inItemMatQueryParams.push({name: "itemVerId", value: inItemVerId});
    inItemMatQueryParams.push({name: "stateVerId", value: inItemStateVerId});
    inItemMatTable.bootstrapTable('refresh');
    $("#all_in_mat_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function initCheckbox(){

    var rows = $("#all_out_mat_list_tb").bootstrapTable('getSelections');
    if(rows!=null && rows.length>0){
        var outInoutId = rows[0].inoutId;
        $.ajax({
            type: "get",
            url: ctx + '/aea/share/mat/inOutCheckboxList.do',
            async: false,
            data: {
                'themeVerId': curThemeVerId,
                "outInoutId": outInoutId
            },
            success: function (data) {
                checkBoxInInoutIds = [];
                if(data!=null&&data.length>0){
                    for(var i in data){
                        checkBoxInInoutIds.push(data[i].inInoutId);
                    }
                }
            }
        });
    }
}
