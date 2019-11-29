var oneformParams = [];
var selectAllOneformTable;//总表列表
var selectParStageOneformTable;
var commonQueryParams = [];
var aeditItemOneformValidator;
var aedit_part_form_form_validator;
var parStageOneformCurrentPageNumber = 1;  //记录当前页面
var parStageOneformCurrentPageSize = 10;   //记录页面大小
var allOneformCurrentPageNumber = 1;
var allOneformCurrentPageSize = 10;
var addDevFormValidator;

$(function() {

    $('#selectAllOneFormTbScroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#aedit_part_form_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#add_dev_form_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

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

    // $('#aedit_part_form_form input[name="isSmartForm"]').change(function(){
    //     var value = $(this).val();
    //     if(value=='1'){
    //         $('#formUrlDiv').hide();
    //         $("#aedit_part_form_form textarea[name='formUrl']").rules('remove');
    //     }else{
    //         $('#formUrlDiv').show();
    //         $("#aedit_part_form_form textarea[name='formUrl']").rules('add',{
    //             required: true,
    //             messages:{
    //                 required: '<font color="red">此项必填！</font>'
    //             }
    //         });
    //     }
    // });

    $('#aedit_part_form_form input[name="useEl"]').change(function(){
        var value = $(this).val();
        if(value=='1'){
            $('#stageElDiv').show();
            $("#aedit_part_form_form textarea[name='elContent']").rules('add',{
                required: true,
                messages:{
                    required: '<font color="red">此项必填！</font>'
                }
            });
        }else{
            $('#stageElDiv').hide();
            $("#aedit_part_form_form textarea[name='elContent']").rules('remove');
        }
    });

    initItemOneformTable();
    initAllOneformTable();
    initItemOneFormValidate();
    initItemPartformValidate();
});


// 查询
function refreshItemOneformTable(){

    selectParStageOneformTable.bootstrapTable('refresh');
    $("#selectParStageOneformTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#aedit_item_oneform_form input[name='itemOneformId']").val('');
}

function initItemPartformValidate(){
    // 设置初始化校验
    aedit_part_form_form_validator = $("#aedit_part_form_form").validate({

        // 定义校验规则
        rules: {
            partformName: {
                required: true
            },
            isSmartForm:{
                required: true,
            },
            useEl: {
                required: true,
            },
            sortNo:{
                required: true,
            }
        },
        messages: {
            partformName: {
                required: '<font color="red">此项必填！</font>'
            },
            isSmartForm:{
                required: '<font color="red">表单类型必选！</font>'
            },
            useEl: {
                required: '<font color="red">此项必选！</font>'
            },
            sortNo:{
                required: '<font color="red">此项必填！</font>'
            }
        },
        // 提交表单
        submitHandler: function (form) {

            var d = {};
            var t = $('#aedit_part_form_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/aea/item/partform/saveAeaItemPartform.do',
                type: 'POST',
                data: d,
                async: false,
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
                            $('#saveParPartform').show();
                            $('#aedit_part_form_modal').modal('hide');
                            searchItemPartform();

                        },500);
                    } else {

                        setTimeout(function(){

                            $('#saveParPartform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#saveParPartform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });

}

function initItemOneFormValidate() {
    // 设置初始化校验
    aeditItemOneformValidator = $("#aedit_item_oneform_form").validate({

        // 定义校验规则
        rules: {
            linkName: {
                required: true,
                maxlength: 200
            },
            sortNo: {
                required: true,
                maxlength: 10
            },
            isActive:{
                required: true,
            }
        },
        messages: {
            linkName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过200个字母!"
            },
            sortNo: {
                required: '<font color="red">此项必填,且为整数！</font>',
                maxlength: "长度不能超过10个字母!"
            },
            isActive:{
                required: '<font color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveItemOneform').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var d = {};
            var t = $('#aedit_item_oneform_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });

            $.ajax({
                url: ctx + '/aea/item/oneform/saveAeaItemOneform.do',
                type: 'POST',
                data: d,
                async: false,
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
                            $('#saveItemOneform').show();
                            $('#aedit_item_oneform_modal').modal('hide');
                            // 列表数据重新加载
                            refreshItemOneformTable();
                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveItemOneform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveItemOneform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });

    // 设置开发表单初始化校验
    addDevFormValidator = $("#add_dev_form_form").validate({

        // 定义校验规则
        rules: {
            formCode: {
                required: true,
                maxlength: 200
            },
            formName: {
                required: true,
                maxlength: 200
            },
            formLoadUrl: {
                required: true,
                maxlength: 500
            }
        },
        messages: {
            formCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过200个字母!"
            },
            formName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过200个字母!"
            },
            formLoadUrl: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveDevform').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var form = new FormData(document.getElementById("add_dev_form_form"));
            // 处理itemPartformId字段
            var itemPartformId = $('#add_dev_form_form input[name="stagePartformId"]').val();
            form.delete("stagePartformId");
            form.set("itemPartformId",itemPartformId);
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/item/partform/createAndUpdateDevForm.do',
                data: form,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 隐藏模式框
                            $('#saveDevform').show();
                            $('#add_dev_form_modal').modal('hide');

                            // 列表数据重新加载
                            searchItemPartform();
                        }, 500);
                    } else {
                        setTimeout(function () {
                            $('#saveDevform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function () {
                        $('#saveDevform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}
var getAllOneformColumns = function () {

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
        }
    ];
    return columns;
}

function initAllOneformTable(){

    var url = ctx + '/aea/item/oneform/listOneform.do';
    oneformParams.push({name: "itemVerId", value: currentBusiId});
    selectAllOneformTable = $('#selectAllOneformTable').bootstrapTable({
        url: url,
        columns: getAllOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            allOneformCurrentPageNumber = number;
            allOneformCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: oneformParam,
        sidePagination: 'server',
        idField: 'oneformId',
        clickToSelect: true
    });
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
    if (oneformParams) {
        for (var i = 0; i < oneformParams.length; i++) {
            buildParam[oneformParams[i].name] = oneformParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}
// 清空
function clearSearchAllOneformList(){

    $("#keyword").val('');
    searchAllOneformList();
}

// 查询
function searchAllOneformList(){

    var keyword = $("#keyword").val();
    oneformParams = [];
    oneformParams.push({name: "itemVerId", value: currentBusiId});
    if (keyword != '') {
        oneformParams.push({name: "keyword", value: keyword});
    }
    $("#selectAllOneformTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function importOneform(){

    if(curIsEditable){

        $("#add_stage_oneform_modal").modal("show");
        oneformParams = [];
        oneformParams.push({name: "itemVerId", value: currentBusiId});
        selectAllOneformTable.bootstrapTable('refresh');
        $("#selectAllOneformTable").bootstrapTable('selectPage',1);

    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function addMultiplyStageOneform(){

    var oneformIds = [];
    var rows = $("#selectAllOneformTable").bootstrapTable('getSelections');
    if (rows != null && rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            oneformIds.push(rows[i].oneformId);
        }
        $.ajax({
            type: "POST",
            url: ctx + '/aea/item/oneform/addMultiplyItemOneform.do',
            data: {itemVerId: currentBusiId, oneformIds: oneformIds.toString()},
            success: function (result) {
                if (result.success) {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal({
                            type: 'success',
                            title: '导入成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        // 隐藏模式框
                        $('#add_stage_oneform_modal').modal('hide');
                        refreshItemOneformTable();
                    }, 500);
                } else {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    }, 500);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                setTimeout(function () {
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }, 500);
            }
        });
    } else {
        swal('错误信息', '当前数据无法导入！', 'error');
    }
}

function initItemOneformTable() {

    var url = ctx + '/aea/item/oneform/listAeaItemOneformByItemVerId.do?itemVerId='+currentBusiId;
    selectParStageOneformTable = $('#selectParStageOneformTable').bootstrapTable({
        url: url,
        columns: getStageOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            parStageOneformCurrentPageNumber = number;
            parStageOneformCurrentPageSize = size;
        },
        pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: stageOneformParam,
        sidePagination: 'server',
        idField: 'itemOneformId',
        singleSelect: true
    });
}

function itemOperatorFormatter(value, row, index) {

    var title = '查看';
    var ico = 'la la-search';
    if(curIsEditable){

        title = '编辑';
        ico = 'la la-edit';
    }

    var updateParStageOneform = '<a href="javascript:editItemOneformById(\'' + row.itemOneformId +  '\')" ' +
                                    'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                                    'title="'+ title +'"><i class="'+ ico +'"></i>' +
                                '</a>';

    var deleteParStageOneform = '<a href="javascript:deleteItemOneform(\'' + row.itemOneformId +  '\')" ' +
                                    'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                                    'title="删除"><i class="la la-trash"></i>' +
                                '</a>';

    if(curIsEditable){
        return updateParStageOneform + deleteParStageOneform;
    }else{
        return updateParStageOneform;
    }
}

var getStageOneformColumns = function () {

    var columns =  [
        // {
        //     checkbox:true
        // },
        {
            field: 'oneformName',
            title: '总表名称',
            align: 'left',
            width: 200
        },
        {
            field: 'linkName',
            title: '链接名称',
            width: 200,
            align: 'left',
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



function stageOneformParam(params) {

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

function editItemOneformById(itemOneformId) {

    $("#aedit_item_oneform_modal").modal("show");
    $("#aedit_item_oneform_modal_title").html(curIsEditable?"修改总表信息":"查看总表信息");
    $('#aedit_item_oneform_form')[0].reset();
    $("#aedit_item_oneform_form input[name='itemOneformId']").val(itemOneformId);
    aeditItemOneformValidator.resetForm();

    if(itemOneformId){

        $.ajax({
            url: ctx + '/aea/item/oneform/getAeaItemOneform.do',
            type: 'POST',
            data: {'id': itemOneformId},
            async: false,
            success: function (data) {
                loadFormData(true, '#aedit_item_oneform_form', data);
            },
            error: function () {
                swal('错误信息', "获取服务类型信息失败！", 'error');
            }
        });
    }

}

function deleteItemOneform(itemOneformId) {

    if(itemOneformId){
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
                    url: ctx + '/aea/item/oneform/deleteById.do',
                    dataType: 'json',
                    data: {'id': itemOneformId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            refreshItemOneformTable();
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

// 新增
function addStagePartform(){

    if(curIsEditable){

        $('#aedit_part_form_modal').modal('show');
        $('#aedit_part_form_modal_title').html('新增扩展表');
        $('#aedit_part_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#stageElDiv').hide();
        $('#aedit_part_form_form')[0].reset();
        if(aedit_part_form_form_validator!=null) {
            $("#aedit_part_form_form").validate().resetForm();
        }
        $('#saveParPartform').show();
        // $('#formUrlDiv').hide();
        $('#aedit_part_form_form input[name="itemPartformId"]').val('');
        $('#aedit_part_form_form input[name="itemVerId"]').val(currentBusiId);
        $('#aedit_part_form_form input[name="partformId"]').val('');
        $("#aedit_part_form_form input[name='useEl'][value='0']").prop("checked", true);
        $("#aedit_part_form_form input[name='isSmartForm'][value='1']").prop("checked", true);

        $.ajax({
            url: ctx + '/aea/item/partform/getMaxSortNo.do',
            type: 'POST',
            data: {'itemVerId': currentBusiId},
            async: false,
            success: function (data) {
                if(data){
                    $("#aedit_part_form_form input[name='sortNo']").val(data);
                }
            },
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function isSmartFormFormatter(value, row, index){

    if(value){
        if(value=='1'){
            return "智能表单";
        }else{
            return "开发表单";
        }
    }
}

function formNameFormatter(value, row, index){

    // var isSmartForm = row.isSmartForm;
    // if(isSmartForm){
    //     if(isSmartForm=='1'){
    //         return row.formName;
    //     }else{
    //         return row.formUrl;
    //     }
    // }

    var formName = row.formName;
    var url = row.formUrl;
    if(url!=null&&url!=''&&url!=undefined){
        formName += url;
    }
    return formName;
}

function useElFormatter(value, row, index){

    if(value){
        if(value=='0'){
            return '禁用';
        }else{
            return '启用';
        }
    }
}

function stagePartformFormatter(value, row, index){

    var sumformId = row.stoFormId;
    var isSmartForm = row.isSmartForm;
    var params = '?itemPartformId='+row.itemPartformId+'&itemVerId='+row.itemVerId+'&isSmartForm='+isSmartForm;
    params=encodeURIComponent(params);

    var strCallback = '';
    strCallback += 'needCallBackSaveActStoForm=1';
    strCallback += '&urlCallBackSaveActStoForm='+restWebApp+'aea/item/partform/updateAeaItemPartformWithFormId.do'+params;
    strCallback += '&requiredField=refEntityId';

    var title = '查看';
    var ico = 'la la-search';
    var ico2 = 'la la-search';
    if(curIsEditable){

        title = '编辑';
        ico = 'la la-edit';
        ico2 = 'la la-pencil';
    }

    var editPartForm = '<a href="javascript:editItemPartFormById(\'' + row.itemPartformId + '\')" title="'+ title +'扩展表"'+
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                            '<i class="'+ ico +'"></i>' +
                       '</a>';

    var delPartForm = '<a href="javascript:delItemPartFormById(\'' + row.itemPartformId + '\')" title="删除扩展表" '+
                          'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                         '<i class="la la-trash"></i>' +
                      '</a>';

    if(sumformId!=null && sumformId!='' && sumformId!=undefined){

        var updateForm = '<a target="_blank" href= "'+ctx+'/design?formId='+sumformId+'&'+strCallback+'" title="'+ title +'智能表单"'+
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                            '<i class="'+ ico2 +'"></i>' +
                         '</a>';

        var updateDevForm = '<a href= "javascript:editStageDevFormById(\'' + sumformId + '\',\'' + row.itemPartformId + '\')" title="'+ title +'开发表单"' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                                '<i class="'+ ico2 +'"></i>' +
                            '</a>';

        var delForm =    '<a href="javascript:deleteItemPartFormRel(\'' + row.itemPartformId + '\')" title="删除表单关联" '+
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                            '<i class="la la-times"></i>' +
                         '</a>';


        if (isSmartForm && isSmartForm == '1') {

            if(curIsEditable){
                return editPartForm + updateForm + delForm + delPartForm;
            }else{
                return editPartForm + updateForm /*+ delForm + delPartForm*/;
            }
        } else {

            if(curIsEditable) {
                return editPartForm + updateDevForm + delForm + delPartForm;
            }else{
                return editPartForm + updateDevForm /*+ delForm + delPartForm*/;
            }
        }
    }else{

        var newForm = '<a target="_blank" href= "'+ctx+'/design?'+strCallback+'" title="新增智能表单"'+
                          'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                          '<i class="la la-plus"></i>' +
                      '</a>';

        var newDevForm = '<a href= "javascript:addStageDevform(\'' + row.itemPartformId + '\')" title="新增开发表单"' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                            '<i class="la la-plus"></i>' +
                         '</a>';

        var addForm = '<a href="javascript:importFormForItemPart(\'' + row.itemPartformId +  '\',\'' + row.itemVerId +'\', \'1\')"title="导入智能表单"'+
                         'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                         '<i class="la la-cog"></i>' +
                      '</a>';

        var addDevForm = '<a href="javascript:importFormForItemPart(\'' + row.itemPartformId + '\',\'' + row.itemVerId + '\', \'0\')"title="导入开发表单"' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
                            '<i class="la la-cog"></i>' +
                         '</a>';

        if (isSmartForm && isSmartForm == '1') {
            if(curIsEditable){
                return editPartForm + newForm + addForm + delPartForm;
            }else{
                return editPartForm /*+ newForm + addForm + delPartForm*/;
            }
        } else {
            if(curIsEditable){
                return editPartForm + newDevForm + addDevForm + delPartForm;
            }else{
                return editPartForm /*+ newDevForm + addDevForm + delPartForm*/;
            }
        }
    }
}

// 导入表单
var curPartFormId = null;
function importFormForItemPart(itemPartformId, stageId, _isSmartForm) {

    isSmartForm = _isSmartForm;
    if(curIsEditable){
        curPartFormId = itemPartformId;
        $('#import_partform_form_modal').modal('show');
        clearSearchPartformForm();
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 编辑扩展表
function editItemPartFormById(itemPartformId){

    if(itemPartformId){

        $('#aedit_part_form_modal').modal('show');
        $('#aedit_part_form_modal_title').html(curIsEditable?'编辑扩展表':'查看扩展表');
        $('#aedit_part_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#stageElDiv').hide();
        $('#aedit_part_form_form')[0].reset();
        if(aedit_part_form_form_validator!=null) {
            $("#aedit_part_form_form").validate().resetForm();
        }
        if(curIsEditable) {
            $('#saveParPartform').show();
        }else{
            $('#saveParPartform').hide();
        }

        $.ajax({
            url: ctx + '/aea/item/partform/getAeaItemPartform.do',
            type: 'POST',
            data: {'id': itemPartformId},
            async: false,
            success: function (data) {
                if (data) {

                    if(data.useEl=='1'){
                        $('#stageElDiv').show();
                    }else{
                        $('#stageElDiv').hide();
                    }

                    // if(data.isSmartForm=='1'){
                    //     $('#formUrlDiv').hide();
                    // }else{
                    //     $('#formUrlDiv').show();
                    // }
                    loadFormData(true, '#aedit_part_form_form', data);
                }
            },
            error: function () {
                swal('错误信息', "获取信息失败！", 'error');
            }
        });
    }else{
        swal('提示信息', "请选择操作记录！", 'info');
    }
}

// 删除扩展表
function delItemPartFormById(itemPartformId){

    if(curIsEditable){
        if(itemPartformId){
            swal({
                title: '此操作影响：',
                text: '将删除扩展表数据,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/item/partform/deleteAeaItemPartformById.do',
                        type: 'POST',
                        data: {'id': itemPartformId},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    title: '提示信息',
                                    text: '删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                searchItemPartform();
                            } else {
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error: function () {
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '操作对象id为空!', 'info');
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 查询
function searchItemPartform(){

    commonQueryParams = [];
    var t = $('#search_stage_parform').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    // commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    $("#stage_partform_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_partform_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchItemPartform(){

    $('#search_stage_parform')[0].reset();
    searchItemPartform();
}

// 刷新
function refreshStagePartform(){

    searchItemPartform();
}

// 批量删除
function batchDelStagePartform(){

    if(curIsEditable){

        var rows = $("#stage_partform_tb").bootstrapTable('getSelections');
        var itemPartformIds = [];
        if(rows!=null&&rows.length>0){
            for(var i=0;i<rows.length;i++){
                itemPartformIds.push(rows[i].itemPartformId);
            }
            swal({
                title: '此操作影响：',
                text: '将批量删除扩展表数据,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/item/partform/batchDelItemPartform.do',
                        type: 'POST',
                        data: {'ids': itemPartformIds.toString()},
                        success: function (result){
                            if(result.success){
                                swal({
                                    title: '提示信息',
                                    text: '批量删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                searchItemPartform();
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择数据！', 'info');
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

//参数设置
function stagePartformParam(params) {

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

// 删除关联
function deleteItemPartFormRel(itemPartformId){

    if(curIsEditable){
        if(itemPartformId){
            swal({
                title: '此操作影响：',
                text: '将解除扩展表与表单关联,您确定操作吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/item/partform/delItemPartformRelFormById.do',
                        type: 'POST',
                        data: {'id': itemPartformId},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    title: '提示信息',
                                    text: '删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                searchItemPartform();
                            } else {
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error: function () {
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '操作对象id为空!', 'info');
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 新增开发表单
function addStageDevform(stagePartformId) {

    if (curIsEditable && stagePartformId) {

        $('#add_dev_form_modal').modal('show');
        $('#add_dev_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#add_dev_form_form')[0].reset();
        if (addDevFormValidator != null) {
            $("#add_dev_form_form").validate().resetForm();
        }

        $('#saveDevform').show();
        $('#add_dev_form_form input[name="stagePartformId"]').val(stagePartformId);
        $('#add_dev_form_form input[name="formId"]').val('');
        $('#add_dev_form_form input[name="formCode"]').val('');
        $('#add_dev_form_form input[name="formName"]').val('');
        $('#add_dev_form_form input[name="formLoadUrl"]').val('');

    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 编辑开发表单
function editStageDevFormById(formId,stagePartformId) {

    if (formId) {

        $('#add_dev_form_modal').modal('show');
        $('#add_dev_form_modal_title').html(curIsEditable?'编辑开发表单':'查看开发表单');
        $('#add_dev_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#add_dev_form_form')[0].reset();
        $('#add_dev_form_form input[name="stagePartformId"]').val(stagePartformId);
        if (addDevFormValidator != null) {
            $("#add_dev_form_form").validate().resetForm();
        }
        if (curIsEditable) {
            $('#saveDevform').show();
        } else {
            $('#saveDevform').hide();
        }

        $.ajax({
            url: ctx + '/aea/par/stage/partform/getStageDevform.do',
            type: 'POST',
            data: {
                'formId': formId,
            },
            async: false,
            success: function (data) {
                if (data) {
                    loadFormData(true, '#add_dev_form_form', data);
                }
            },
            error: function () {
                swal('错误信息', "获取信息失败！", 'error');
            }
        });
    } else {
        swal('提示信息', "请选择操作记录！", 'info');
    }
}