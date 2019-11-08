//参数设置
var commonQueryParams;
var servicequalIds;
var _serviceId;
var _unitServiceId;
function rootCertinstListParam(params) {

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
    buildParam.unitServiceId = _unitServiceId;
    queryParam = $.extend(queryParam, buildParam);
    return queryParam;
}


function rootserviceQueryParam(params) {

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

function rootqualQueryParam(params) {

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

function rootRelServiceTypeResponseData(res) {
    var data = res.rows;
    //初始化
    /*if(!currServiceTypeId){
        currServiceTypeId = data[0].serviceId;
    }*/
    $("#root_service_qual_list_tb").bootstrapTable('hideColumn', 'qualId');
    return res;
}

function rootCertinstResponseData(res) {
    var data = res.rows;
    //初始化
    /*if(!currServiceTypeId){
        currServiceTypeId = data[0].serviceId;
    }*/
    // $("#certinst_list").bootstrapTable('hideColumn', 'certinstId');
    return res;
}

function isActiveFormatter(value, row, index) {
    return isServiceTypeActiveStr(row);
}

$(function () {
    // 设置初始化校验
    $('#service_list_tb').bootstrapTable({
        url: ctx+'/supermarket/serviceExamine/listServiceMatter.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        striped: true, // 隔行加亮
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10,20,30,50,100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams:rootserviceQueryParam,
        responseHandler:rootRelServiceTypeResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns:false,
        smartDisplay: true,
        singleSelect:true,
        clickToSelect: true, // 单击行即可以选中
        sortName: 'createTime', // 设置默认排序为 name
        sortOrder: 'desc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: 'num',
            title: '序号',
            align: 'center',
            formatter: function (value, row, index) {
                return index+1;
            }

        },{
            field: 'unitServiceId',
            title: '服务Id',
            align: 'center',
            visible: false

        }, {
            field: 'serviceName',
            title: '服务类型',
            align: 'left',
        }, {
            field: 'applicant',
            title: '从业机构',
            align: 'left',
        }, {
            field: 'certinstStr',
            title: '资源证书',
            align: 'left',
        },{
            field: 'auditFlag',
            title: '审核状态',
            align: 'left',
            formatter: function (value, row, index) {
                // 审核状态:0 审核失败，1已审核 ，2 审核中，
                var result = "审核中";
                if(value=="1"){
                    result = "已审核";
                }else if(value=="2"){
                    result = "审核中";
                }else if(value=="0"){
                    result = "审核失败";
                }
                return result;
            }
        },{
            field: 'createTime',
            title: '提交时间',
            align: 'left',
        }, {
            field: '',
            align: 'center',

        }],
        onCheck:function(row){
            var serviceId = row.serviceId;
            $.ajax({
                url: ctx+'/supermarket/serviceQual/checkServiceQualRelated.do',
                type: 'POST',
                data: {'serviceId': serviceId},
                success: function (result) {
                    servicequalIds = result.toString();
                    $("#root_service_qual_list_tb").bootstrapTable('refresh');
                }

            });
        },
    });


    $('#root_qual_list_tb').bootstrapTable({
        url: ctx+'/supermarket/qual/listAeaImNotServiceQual.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10,20,30,50,100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams:rootqualQueryParam,
        responseHandler:rootRelServiceTypeResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns:false,
        smartDisplay: true,
        clickToSelect: true, // 单击行即可以选中
        sortName: 'qualId', // 设置默认排序为 name
        sortOrder: 'asc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: 'num',
            title: '序号',
            align: 'center',
            formatter: function (value, row, index) {
                return index+1;
            }

        },{
            field: 'qualId',
            title: '资质Id',
            align: 'center',
            visible: false

        }, {
            field: 'qualName',
            title: '资质名称',
            align: 'left',
        }, {
            field: 'qualCode',
            title: '资质编码',
            align: 'left',
        }]
    });

})






// 清空服务查询
function clearRootRelService(){
    console.log('11111');
    $('#unitServiceName').val('');
    $('#applicant').val('');

    $('#auditFlag').find("option[text='请选择']").attr("selected",true);
    $('#auditFlag').html('')
    $('#auditFlag').html('<option value="">请选择</option>' +
        '<option value="2">审核中</option>' +
        '<option value="1">已审核</option>' +
        '<option value="0">审核失败</option>')
    // searchUnitService();
}
// 条件查询服务列表
function searchUnitService() {
    var applicant =  $('#applicant').val();
    var unitServiceName = $('#unitServiceName').val();
    var auditFlag = $("#auditFlag").val();
    var param = {};
    param.applicant = applicant;
    param.serviceName=unitServiceName;
    param.auditFlag=auditFlag;
    commonQueryParams = [];

    if (param != "") {
        $.each(param, function (k,v) {
            commonQueryParams.push({name: k, value:param[k]});
        });
    }
    // $("#root_qual_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#service_list_tb").bootstrapTable('refresh');
}



// 清空资质查询
function clearRootRelQual(){
    $('#search_root_rel_qual_form')[0].reset();
    searchRootRelQual();
}

function rootUnitServiceOperatorFormatter(value, row, index) {

    var editBtn = '<a href="javascript:examineUnitService(\'' + row.unitServiceId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="审核"><i class="la la-edit"></i></a>';

    return editBtn;
}
function cerinstFormatter(value, row, index) {
    var editBtn = '<a href="javascript:showCertinst(\'' + row.certinstId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="证照详情"><i class="la la-edit"></i></a>';

    return editBtn;
}

// 添加关联关系列表
function editServiceQualRelated(){

        $('#serviceQualRelated').modal('show');
        $("#root_qual_list_tb").bootstrapTable('refresh');

}

//关闭窗口
function closeUnitServiceModal() {
    $('#unit_service_modal').modal('hide');
}

// 审核记录
function examineUnitService(unitServiceId){
    var url = "unitServiceDetailIndex.do?"+unitServiceId;
    window.location = url;

}

function examineService(param) {
    if(param == "T"){
        // data = data+"&auditFlag=2";
        $("#_auditFlag").attr("value","1");

    }else{
        $("#_auditFlag").attr("value","0");
        // data = data+"&auditFlag=3";

    }
    //审核状态:2 审核中，1 审核通过，0 审核失败
    var data = $('#unit_service_form').serialize();
    $.ajax({
        url: ctx + '/supermarket/serviceExamine/examineUnitService.do',
        type: 'POST',
        data: data,
        async: false,
        success: function (data) {
            swal({
                title: '提示信息',
                text: '审核成功!',
                type: 'success',
                timer: 1000,
                showConfirmButton: false
            });
            // $('#unit_service_modal').modal('hide');
            setTimeout(function () {
                history.back(-1);
            },"1000");

        },
        error: function () {
            swal('错误信息', "审核失败！", 'error');
        }
    });


}

    function addServiceModal(){
        $('#ae_service_type_modal').modal('show');
        $("#serviceButton").attr("onclick","addService()");
        $('#ae_service_type_modal_title').html('添加服务');
    }

    function addService(){
        $.ajax({
            url: ctx + '/supermarket/service/saveAeaImService.do',
            type: 'POST',
            data: $('#ae_service_form').serialize(),
            async: false,
            success: function (data) {
                swal({
                    title: '提示信息',
                    text: '添加成功!',
                    type: 'success',
                    timer: 1000,
                    showConfirmButton: false
                });
                $('#ae_service_type_modal').modal('hide');

                $("#service_list_tb").bootstrapTable('refresh');
            },
            error: function () {
                swal('错误信息', "获取服务类型信息失败！", 'error');
            }
        });
}
function showCertinstList(unitServiceid) {

    $('#certinst_list').bootstrapTable({
        url: ctx+'/supermarket/serviceExamine/listCertinstByUnitServiceid.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        striped: true, // 隔行加亮
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10,20,30,50,100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams:rootCertinstListParam,
        responseHandler:rootCertinstResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns:false,
        smartDisplay: true,
        singleSelect:true,
        clickToSelect: true, // 单击行即可以选中
        sortName: '', // 设置默认排序为 name
        sortOrder: 'asc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: '#',
            title: '',
            align: 'center',

        }, {
            field: 'certinstCode',
            title: '证照编码',
            align: 'left',
        }, {
            field: 'certinstName',
            title: '证照名称',
            align: 'left',
        }, {
            field: 'major',
            title: '所属资质',
            align: 'left',
        }, {
            field: 'qualLevelName',
            title: '等级',
            align: 'left',
        }, {
            field: '',
            title: '操作',
            align: 'center',

        }],
    });

}

// 中介服务事项展示
function itembasicQueryParam(params) {

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
    buildParam.serviceId = _serviceId;
    queryParam = $.extend(queryParam, buildParam);
    return queryParam;
}
function showServiceItemList(unitServiceid) {

    $('#service_item_tb').bootstrapTable({
        url: ctx+'/supermarket/serviceExamine/listServiceItemServiceid.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        striped: true, // 隔行加亮
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10,20,30,50,100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams:itembasicQueryParam,
        responseHandler:rootCertinstResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns:false,
        smartDisplay: true,
        singleSelect:true,
        clickToSelect: true, // 单击行即可以选中
        sortName: '', // 设置默认排序为 name
        sortOrder: 'asc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: 'num',
            title: '序号',
            align: 'center',
            formatter: function (value, row, index) {
                return index+1;
            }

        }, {
            field: 'itemName',
            title: '服务事项名称',
            align: 'left',
        }, {
            field: 'orgName',
            title: '所属机构',
            align: 'left',
        }],
    });

}