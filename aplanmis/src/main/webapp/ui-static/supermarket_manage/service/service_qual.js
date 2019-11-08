//参数设置
var commonQueryParams;
var servicequalIds;

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
        sort: sort,
        servicequalIds: servicequalIds //传递与服务关联的资质Id到后台
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

function isActiveFormatter(value, row, index) {
    return isServiceTypeActiveStr(row);
}

var ae_service_type_form_validator;
$(function () {
    // 设置初始化校验
    $('#service_list_tb').bootstrapTable({
        url: ctx + '/supermarket/service/listAeaImService.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        striped: true, // 隔行加亮
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10, 20, 30, 50, 100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams: rootserviceQueryParam,
        responseHandler: rootRelServiceTypeResponseData,
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

        columns: [{
            field: '#',
            title: '',
            align: 'center',
            formatter: function (value, row, index) {
                if (index == 0) {
                    checkServiceQualRelated(row.serviceId);
                    initqualtable();
                    return {checked: true};
                }
            }

        }, {
            field: 'serviceId',
            title: '服务Id',
            align: 'center',
            visible: false

        }, {
            field: 'serviceName',
            title: '服务名称',
            align: 'left',
        }, {
            field: 'serviceCode',
            title: '服务编码',
            align: 'left',
        }, {
            field: '',
            align: 'center',

        }],
        onCheck: function (row) {
            var serviceId = row.serviceId;
            $.ajax({
                url: ctx + '/supermarket/serviceQual/checkServiceQualRelated.do',
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
        url: ctx + '/supermarket/qual/listAeaImNotServiceQual.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10, 20, 30, 50, 100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams: rootqualQueryParam,
        responseHandler: rootRelServiceTypeResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns: false,
        smartDisplay: true,
        clickToSelect: true, // 单击行即可以选中
        sortName: 'qualId', // 设置默认排序为 name
        sortOrder: 'asc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: '#',
            title: '',
            align: 'center',

        }, {
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

function initqualtable() {
    $('#root_service_qual_list_tb').bootstrapTable({
        url: ctx + '/supermarket/qual/listAeaImServiceQual.do',
        method: 'post', //默认是post,不允许对静态文件访问
        cache: false,
        pagination: true, // 开启分页功能
        pageSize: 10, // 设置默认分页为 20
        pageNumber: 1,
        pageList: [10, 20, 30, 50, 100], // 自定义分页列表
        search: false,//开启搜索功能
        sidePagination: 'server',//设置为服务器端分页
        contentType: "application/x-www-form-urlencoded",
        queryParams: rootqualQueryParam,
        responseHandler: rootRelServiceTypeResponseData,
        showColumns: true, // 开启自定义列显示功能
        showRefresh: false, // 开启刷新功能
        minimumCountColumns: 2,// 设置最少显示列个数
        clickToSelect: true,
        showColumns: false,
        smartDisplay: true,
        clickToSelect: true, // 单击行即可以选中
        sortName: 'qualId', // 设置默认排序为 name
        sortOrder: 'asc', // 设置排序为反序 desc
        smartDisplay: true, // 智能显示 pagination 和 cardview 等
        dataType: "json",

        columns: [{
            field: '#',
            title: '',
            align: 'center',

        }, {
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
}


// 条件查询服务列表
function searchRootRelService() {

    var params = $('#search_root_rel_service_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#service_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#service_list_tb").bootstrapTable('refresh');
}

// 条件查询资质列表
function searchRootRelQual() {

    var params = $('#search_root_rel_qual_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#root_qual_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#root_qual_list_tb").bootstrapTable('refresh');
}

// 清空服务查询
function clearRootRelService() {
    $('#search_root_rel_service_form')[0].reset();
    searchRootRelService();
}

// 清空资质查询
function clearRootRelQual() {
    $('#search_root_rel_qual_form')[0].reset();
    searchRootRelQual();
}

function rootRelServiceTypeOperatorFormatter(value, row, index) {
    var editBtn = '<a href="javascript:editService(\'' + row.serviceId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    var deleteBtn = '<a href="javascript:deleteServiceById(\'' + row.serviceId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
    return editBtn + deleteBtn;
}

// 批量删除
function batchDeleteServiceType() {
    var rows = $("#service_list_tb").bootstrapTable('getSelections');
    if (rows && rows.length > 0) {
        var ServiceTypeIds = [];
        for (var i = 0; i < rows.length; i++) {
            ServiceTypeIds.push(rows[i].serviceTypeId);
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除选择的数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/service/type/batchDeleteServiceType.do',
                    type: 'POST',
                    data: {'ids': ServiceTypeIds.toString()},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            //  刷新
                            searchRootRelServiceType();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '操作失败！', 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

// 添加关联关系列表
function editServiceQualRelated() {

    $('#serviceQualRelated').modal('show');
    $("#root_qual_list_tb").bootstrapTable('refresh');

}

// 删除服务资质对应关联关系
function deleteServiceQualByServiceId() {
    var serviceId = $("#service_list_tb").bootstrapTable('getSelections')[0].serviceId;
    var selected = $("#root_service_qual_list_tb").bootstrapTable('getSelections');
    var qualIds = new Array();

    for (var i = 0; i < selected.length; i++) {
        qualIds[i] = selected[i].qualId;
    }
    if (serviceId != 'undefined' && qualIds.length != 0) {
        swal({
            title: '提示信息',
            text: '您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/supermarket/serviceQual/deleteServiceQualByServiceId.do',
                    type: 'POST',
                    data: {'serviceId': serviceId, "qualIds": qualIds.toString()},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 刷新列表
                            checkServiceQualRelated(serviceId);
                            $("#root_service_qual_list_tb").bootstrapTable('refresh');
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '操作失败！', 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', '请选择操作数据！', 'info');
    }
}


// 单条删除
function deleteServiceById(id) {
    if (id) {
        swal({
            title: '提示信息',
            text: '此操作将删除数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/supermarket/service/deleteAeaImServiceById.do',
                    type: 'POST',
                    data: {'id': id},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 刷新列表
                            searchRootRelService();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '操作失败！', 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

//关闭窗口
function closeServiceTypeModal() {
    $('#ae_service_type_modal').modal('hide');
}

function addServiceQualRelated() {
    var serviceId = $("#service_list_tb").bootstrapTable('getSelections')[0].serviceId;
    var selected = $("#root_qual_list_tb").bootstrapTable('getSelections');
    var qualIds = new Array();

    for (var i = 0; i < selected.length; i++) {
        qualIds[i] = selected[i].qualId;
    }
    $.ajax({
        url: ctx + '/supermarket/serviceQual/addServiceQualRelated.do',
        type: 'POST',
        data: {'serviceId': serviceId, "qualIds": qualIds.toString()},
        success: function (result) {
            if (result.success) {
                swal({
                    title: '提示信息',
                    text: '添加成功!',
                    type: 'success',
                    timer: 1000,
                    showConfirmButton: false
                });
                checkServiceQualRelated(serviceId);
                $("#root_qual_list_tb").bootstrapTable('selectPage', 1);
                $("#root_qual_list_tb").bootstrapTable('refresh');
                $("#root_service_qual_list_tb").bootstrapTable('selectPage', 1);
                $("#root_service_qual_list_tb").bootstrapTable('refresh');
                $('#serviceQualRelated').modal('hide');
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function () {
            swal('错误信息', '操作失败！', 'error');
        }
    });
}

function checkServiceQualRelated(serviceId) {

    $.ajax({
        url: ctx + '/supermarket/serviceQual/checkServiceQualRelated.do',
        type: 'POST',
        async: false,
        data: {'serviceId': serviceId},
        success: function (result) {
            servicequalIds = result.toString();
            $("#root_service_qual_list_tb").bootstrapTable('refresh');
        }

    });


}

// 单条编辑
function editService(serviceId) {
    $.ajax({
        url: ctx + '/supermarket/service/getAeaImService.do',
        type: 'POST',
        data: {'id': serviceId},
        async: false,
        success: function (data) {
            $("#ae_service_form")[0].reset();
            $('#ae_service_type_modal').modal('show');
            $("#serviceButton").attr("onclick", "updateService()");
            $("#serviceId").val(data.serviceId);
            $("#serviceName").val(data.serviceName);
            $("#serviceCode").val(data.serviceCode);
            $("#imgUrl").val(data.imgUrl);
            $("#purchaseImgUrl").val(data.purchaseImgUrl);
            $("#isActive").val(data.isActive);
            $("#memo").val(data.memo);
            $('#ae_service_type_modal_title').html('编辑服务');

        },
        error: function () {
            swal('错误信息', "获取服务类型信息失败！", 'error');
        }
    });

}

function updateService() {
    if ($('#serviceName').val() == '') {
        swal('提示信息', "服务名称不得为空！", 'error');
        return false;
    }
    if ($('#serviceCode').val() == '') {
        swal('提示信息', "服务编号不得为空！", 'error');
        return false;
    }
    if ($('#imgUrl').val() == '') {
        swal('提示信息', "服务图标路径不得为空！", 'error');
        return false;
    }
    if ($('#purchaseImgUrl').val() == '') {
        swal('提示信息', "采购公告图标路径不得为空！", 'error');
        return false;
    }

    $.ajax({
        url: ctx + '/supermarket/service/updateAeaImService.do',
        type: 'POST',
        data: $('#ae_service_form').serialize(),
        async: false,
        success: function (data) {
            swal({
                title: '提示信息',
                text: '修改成功!',
                type: 'success',
                timer: 1000,
                showConfirmButton: false
            });
            $('#ae_service_type_modal').modal('hide');

            searchRootRelService();
        },
        error: function () {
            swal('错误信息', "获取服务类型信息失败！", 'error');
        }
    });
}

function addServiceModal() {
    $("#ae_service_form")[0].reset();
    $('#ae_service_type_modal').modal('show');
    $("#serviceButton").attr("onclick", "addService()");
    $('#ae_service_type_modal_title').html('添加服务');
}

function addService() {
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

function selectImage(type) {
    $('#common_set_img_modal').modal('show');
    $('#common_set_img_modal_title').html('设置图标&nbsp;&nbsp;(&nbsp;<font color="red">*</font>双击选择&nbsp;)');

    // 图表展示集合
    var imgList = $("#imgDataList");

    // 清空
    needSelectImgDivId = null;
    $('#selectedImg').val("");
    imgList.html("");

    // 重新赋值
    needSelectImgDivId = type == 'purchaseIcon' ? 'purchaseImgUrl' : 'imgUrl';

    // 加载图标数据
    loadImgData(type, imgList);
}

function loadImgData(type, imgList){
    $.ajax({
        url: ctx + "/supermarket/serviceQual/getServiceMattersImg.do",
        type: "POST",
        dataType: "JSON",
        data: {"type" : type},
        success: function(result){
            if(result){
                var fileNameList = result;
                if(fileNameList!=null&&fileNameList.length>0){
                    var addHtml = '';
                    for(var i=0;i<fileNameList.length;i++){
                        var imgObj = fileNameList[i];
                        var fileName = imgObj.fileName;
                        var fullName = imgObj.fullName;
                        var src = ctx + '/' +fullName;
                        var divClass = "mouseOutImage";
                        if($('#' + needSelectImgDivId).val() == fullName){
                            divClass = "selectedImage  mouseOutImage";
                        }
                        var divStyle = type == 'serviceIcon' ? 'width: 58px;height: 58px' : '';
                        addHtml += "<div class=\""+divClass+"\"  id=\""+fileName+"\" title=\""+fileName+"\" style=\""+divStyle+"\" " +
                            "onmouseover= \"mouseImgOver(this.id)\" " +
                            "onmouseout= \"mouseImgOut(this.id)\"  " +
                            "onclick=\"selectImg(this.id)\"  " +
                            "ondblclick=\"finishSelectImg(this.id)\">" +
                            "<img src=\""+ src +"\">" +
                            "</div>";
                    }
                    imgList.html(addHtml);
                }else{
                    imgList.html("<h5 style='text-alogn: center;'>暂无图片...</h5>");
                }
            }
        }
    });
}

/**
 * 设置鼠标移动到图标上时，图标DIV的样式
 * @param id
 */
function mouseImgOver(id){

    $("#"+id).removeClass("mouseOutImage");
    $("#"+id).addClass("mouseOverImage");
}

/**
 * 设置鼠标离开图标时，图标DIV的样式
 * @param id
 */
function mouseImgOut(id){

    $("#"+id).removeClass("mouseOverImage");
    $("#"+id).addClass("mouseOutImage");
}

/**
 * 单击选择图标的操作
 * @param id
 */
function selectImg(id){

    //移除之前的选中状态
    $("[onclick]").removeClass("selectedImage");
    $("#"+id).addClass("selectedImage");

    //获取当前id的DIV中的图片的src值
    var src = $("#"+id).children("img").attr("src");
    var newSrc = getNewSrc(src);
    //将图片路径保存到变量中
    $("#selectedImg").val(newSrc);
}

/**
 * 获取菜单路径(除去工程名)
 * @param src
 * @returns
 */
function getNewSrc(src){

    var newsrc = src.substring(ctx.length + 1,src.length);
    return newsrc;
}

/**
 * 双击选择图标的操作
 * @param id
 */
function finishSelectImg(id){

    //获取当前id的DIV中的图片的src值
    var src = $("#"+id).children("img").attr("src");
    var newSrc = getNewSrc(src);
    //将图片路径保存到变量中
    $("#selectedImg").val(newSrc);
    setImgPath(needSelectImgDivId, newSrc);
}

/**
 * 赋值图标路径数据
 * @param divId  赋值容器id
 * @param path  图标相对路径
 */
function setImgPath(divId, path){

    $("#"+divId).val(path);
    $('#common_set_img_modal').modal('hide');
}