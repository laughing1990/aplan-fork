var parStageId = '${currentBusiId}';
var selectParStageOneformTable;//阶段总表关联列表
var parStageOneformCurrentPageNumber = 1;  //记录当前页面
var parStageOneformCurrentPageSize = 10;   //记录页面大小
var commonQueryParams = [];
var selectParOneformTable;//事项子表列表
var parOneformCurrentPageNumber = 1;  //记录当前页面
var parOneformCurrentPageSize = 10;   //记录页面大小
var editParStageOneformValidator;
var selectAllOneformTable;//总表列表
var selectAllStoFormTable;//智能表单列表
var allOneformCurrentPageNumber = 1;
var allOneformCurrentPageSize = 10;
var allStoFormPageNumber = 1;
var allStoFormPageSize = 10;
var oneformParams = [];
var itemFormParams = [];
var stoFormParams = [];
var aedit_part_form_form_validator;
var addDevFormValidator;

$(function () {

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

    $('#add_stoform_scroll').niceScroll({

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
    //
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

    $('#aedit_part_form_form input[name="useEl"]').change(function () {

        var value = $(this).val();
        if (value == '1') {
            $('#stageElDiv').show();
            $("#aedit_part_form_form textarea[name='elContent']").rules('add', {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>'
                }
            });
        } else {
            $('#stageElDiv').hide();
            $("#aedit_part_form_form textarea[name='elContent']").rules('remove');
        }
    });

    initParStageOneformTable();
    initParOneformTable();
    initAllOneformTable();

    // 设置初始化校验
    editParStageOneformValidator = $("#edit_stage_oneform_form").validate({

        // 定义校验规则
        rules: {
            linkName: {
                required: true,
                maxlength: 100
            },
            sortNo: {
                digits: true,
                maxlength: 100
            }
        },
        messages: {
            linkName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过100个字母!"
            },
            sortNo: {
                digits: '<font color="red">必须输入整数！</font>',
                maxlength: "长度不能超过100个字母!"
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveParStageOneform').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");
            var sortNo = $("#sortNo").val();

            var form = new FormData(document.getElementById("edit_stage_oneform_form"));
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/par/stage/stageOneform/saveParStageOneform.do',
                data: form,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success) {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '修改成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 隐藏模式框
                            $('#saveParStageOneform').show();
                            $('#edit_stage_oneform_modal').modal('hide');
                            // 列表数据重新加载
                            refreshStageOneformTable();

                        }, 500);
                    } else {

                        setTimeout(function () {
                            $('#saveParStageOneform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function () {
                        $('#saveParStageOneform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });

    // 设置初始化校验
    aedit_part_form_form_validator = $("#aedit_part_form_form").validate({

        // 定义校验规则
        rules: {
            partformName: {
                required: true
            },
            isSmartForm: {
                required: true,
            },
            useEl: {
                required: true,
            },
            sortNo: {
                required: true,
            }
        },
        messages: {
            partformName: {
                required: '<font color="red">此项必填！</font>'
            },
            isSmartForm: {
                required: '<font color="red">表单类型必选！</font>'
            },
            useEl: {
                required: '<font color="red">此项必选！</font>'
            },
            sortNo: {
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
                url: ctx + '/aea/par/stage/partform/saveStagePartform.do',
                type: 'POST',
                data: d,
                async: false,
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
                            $('#saveParPartform').show();
                            $('#aedit_part_form_modal').modal('hide');
                            searchStagePartform();

                        }, 500);
                    } else {

                        setTimeout(function () {

                            $('#saveParPartform').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function () {

                        $('#saveParPartform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
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
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/par/stage/partform/createAndUpdateDevForm.do',
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
                            refreshStagePartform();
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
});

function initParStageOneformTable() {

    var url = ctx + '/aea/par/stage/stageOneform/listStageOneform.do?parStageId=' + currentBusiId;
    selectParStageOneformTable = $('#selectParStageOneformTable').bootstrapTable({
        url: url,
        columns: getStageOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            parStageOneformCurrentPageNumber = number;
            parStageOneformCurrentPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: stageOneformParam,
        sidePagination: 'server',
        idField: 'stageOneformId',
        singleSelect: false,
        clickToSelect: true
    });
}

function initParOneformTable() {

    var url = ctx + '/aea/par/stage/stageOneform/listItemForm.do?parStageId=' + currentBusiId;
    selectParOneformTable = $('#selectParOneformTable').bootstrapTable({
        url: url,
        columns: getOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            parOneformCurrentPageNumber = number;
            parOneformCurrentPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        queryParams: itemFormParam,
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        sidePagination: 'server',
        singleSelect: true,
        onLoadSuccess: function (result) {
            if (result && result.rows && result.rows.length > 0) {
                initAllStoFormTable(result.rows[0].itemVerId);
            }
        },
    });
}

function initAllOneformTable() {

    var url = ctx + '/aea/par/stage/stageOneform/listOneform.do';
    oneformParams.push({name: "parStageId", value: currentBusiId});
    selectAllOneformTable = $('#selectAllOneformTable').bootstrapTable({
        url: url,
        columns: getAllOneformColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            allOneformCurrentPageNumber = number;
            allOneformCurrentPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: oneformParam,
        sidePagination: 'server',
        idField: 'oneformId',
        clickToSelect: true
    });
}

function initAllStoFormTable(itemVerId) {

    var url = ctx + '/aea/par/stage/stageOneform/listStoForm.do';
    stoFormParams.push({name: "stageId", value: currentBusiId});
    stoFormParams.push({name: "itemVerId", value: itemVerId});
    selectAllStoFormTable = $('#selectAllStoFormTable').bootstrapTable({
        url: url,
        columns: getAllStoFormColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            allStoFormPageNumber = number;
            allStoFormPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: stoFormParam,
        sidePagination: 'server',
        idField: 'oneformId',
        clickToSelect: true,
        singleSelect: true,
    });
}

var getStageOneformColumns = function () {

    var columns = [
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
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 80,
            formatter: itemOperatorFormatter
        }
    ];
    return columns;
}


var getOneformColumns = function () {

    var columns = [
        {
            field: 'itemName',
            title: '事项名称',
            align: 'left',
            width: 200,
            formatter: itemNameFormatter
        },
        {
            field: 'formName',
            title: '智能表单名称',
            width: 200,
            align: 'left',
        },
        {
            field: 'sortNo',
            title: '排序字段',
            align: 'center',
            width: 100
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 80,
            formatter: parStageItemFormatter
        }
    ];
    return columns;
}

function itemNameFormatter(value, row, index, field) {

    var itemName = row.itemName;
    if (!isEmpty(row.isCatalog)) {
        if (row.isCatalog == '1') {
            itemName = '【标准事项】' + itemName;
            if (!isEmpty(row.guideOrgName)) {
                itemName = itemName + '【' + row.guideOrgName + '】';
            }
        } else {
            itemName = '【实施事项】' + itemName;
            if (!isEmpty(row.orgName)) {
                itemName = itemName + '【' + row.orgName + '】';
            }
        }
    }
    return itemName;
}

//判断字符是否为空的方法
function isEmpty(obj) {

    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}

var getAllOneformColumns = function () {

    var columns = [
        {
            checkbox: true
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

var getAllStoFormColumns = function () {

    var columns = [
        {
            checkbox: true
        },
        {
            field: 'formName',
            title: '智能表单名称',
            align: 'left',
            width: 250
        },
        {
            field: 'formCode',
            title: '智能表单编号',
            align: 'left',
            width: 250,
        },
        {
            field: 'formProperty',
            title: '表单类型',
            align: 'left',
            width: 100,
            formatter: formPropertyFormatter,
        },
        // {
        //     field: 'operate_',
        //     align: 'center',
        //     title: '操作',
        //     width: 80,
        //     formatter: addItemFormFormatter
        // }
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

function itemFormParam(params) {

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
    if (itemFormParams) {
        for (var i = 0; i < itemFormParams.length; i++) {
            buildParam[itemFormParams[i].name] = itemFormParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
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

function stoFormParam(params) {

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
    if (stoFormParams) {
        for (var i = 0; i < stoFormParams.length; i++) {
            buildParam[stoFormParams[i].name] = stoFormParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function itemOperatorFormatter(value, row, index) {

    var updateParStageOneform = '<a href="javascript:editParStageOneformById(\'' + row.stageOneformId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
        'title="编辑"><i class="la la-edit"></i>' +
        '</a>';
    var deleteParStageOneform = '<a href="javascript:deleteParStageOneform(\'' + row.stageOneformId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';


    return updateParStageOneform + deleteParStageOneform;
}

function addItemFormFormatter(value, row, index) {

    var stageItemId = $("#stageItemId").val();
    var addItemStoForm = '<a href="javascript:addItemStoForm(\'' + stageItemId + '\',\'' + row.formId + '\')" ' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                            'title="导入"><i class="la la-plus"></i>' +
                         '</a>';
    return addItemStoForm;
}

function editParStageOneformById(stageOneformId) {

    $("#edit_stage_oneform_modal").modal("show");
    $("#edit_stage_oneform_modal_title").html("修改总表信息");
    $('#edit_stage_oneform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#edit_stage_oneform_form')[0].reset();

    $("#edit_stage_oneform_form input[name='stageOneformId']").val(stageOneformId);
    editParStageOneformValidator.resetForm();
    if (stageOneformId) {
        $.ajax({
            url: ctx + '/aea/par/stage/stageOneform/getAeaParStageOneform.do',
            type: 'POST',
            data: {'id': stageOneformId},
            async: false,
            success: function (data) {
                loadFormData(true, '#edit_stage_oneform_form', data);
            },
            error: function () {
                swal('错误信息', "获取服务类型信息失败！", 'error');
            }
        });
    }

}

// 查询
function refreshStageOneformTable() {

    selectParStageOneformTable.bootstrapTable('refresh');
    $("#selectParStageOneformTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#edit_stage_oneform_form input[name='stageOneformId']").val('');
}

function refreshParOneformTable() {

    selectParOneformTable.bootstrapTable('refresh');
    $("#selectParOneformTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function addParOneform() {

    $("#add_stage_oneform_modal").modal("show");
    $("#add_stage_oneform_modal_title").html("导入总表");
    oneformParams = [];
    oneformParams.push({name: "parStageId", value: currentBusiId});
    selectAllOneformTable.bootstrapTable('refresh');
    $("#selectAllOneformTable").bootstrapTable('selectPage', 1);
}

function addStageOneform(oneformId) {

    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");
    $.ajax({
        type: "POST",
        url: ctx + '/aea/par/stage/stageOneform/addParStageOneform.do',
        data: {parStageId: currentBusiId, oneformId: oneformId},
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
                    refreshStageOneformTable();
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
}

// 查询
function searchAllOneformList() {

    var keyword = $("#keyword").val();
    oneformParams = [];
    if (keyword != '') {
        oneformParams.push({name: "keyword", value: keyword});
    }
    $("#selectAllOneformTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 清空
function clearSearchAllOneformList() {

    $("#keyword").val('');
    searchAllOneformList();
}

function addMultiplyStageOneform() {

    var oneformIds = [];
    var rows = $("#selectAllOneformTable").bootstrapTable('getSelections');
    if (rows != null && rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            oneformIds.push(rows[i].oneformId);
        }
        $.ajax({
            type: "POST",
            url: ctx + '/aea/par/stage/stageOneform/addMultiplyStageOneform.do',
            data: {parStageId: currentBusiId, oneformIds: oneformIds.toString()},
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
                        refreshStageOneformTable();
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

function deleteParStageOneform(stageOneformId) {

    if (stageOneformId) {
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
                    url: ctx + '/aea/par/stage/stageOneform/deleteStageOneformById.do',
                    dataType: 'json',
                    data: {'id': stageOneformId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            refreshStageOneformTable();
                        } else {
                            swal('错误信息', '删除失败', 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

function showStoForm(stageItemId, itemVerId) {

    $("#add_ItemForm_modal").modal("show");
    $("#add_ItemForm_modal_title").html("导入智能表单");
    $("#stageItemId").val(stageItemId);
    $("#itemVerId").val(itemVerId);
    stoFormParams = [];
    stoFormParams.push({name: "stageId", value: currentBusiId});
    stoFormParams.push({name: "itemVerId", value: itemVerId});
    selectAllStoFormTable.bootstrapTable('refresh');
}

function addItemStoForm(stageItemId, formId) {

    if (formId != null && formId != '') {
        $.ajax({
            type: "POST",
            url: ctx + '/aea/par/stage/stageOneform/saveParStageItem.do',
            data: {
                'stageItemId': stageItemId,
                'subFormId': formId
            },
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
                        $('#add_ItemForm_modal').modal('hide');
                        refreshParOneformTable();
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

// 查询
function searchStoFormList() {

    var stoFormKeyword = $("#stoFormKeyword").val();
    var itemVerId = $("#itemVerId").val();
    stoFormParams = [];
    stoFormParams.push({name: "stageId", value: currentBusiId});
    stoFormParams.push({name: "itemVerId", value: itemVerId});
    if (stoFormKeyword != '') {
        stoFormParams.push({name: "keyword", value: stoFormKeyword});

    }
    $("#selectAllStoFormTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 清空
function clearSearchStoFormList() {

    $("#stoFormKeyword").val('');
    var itemVerId = $("#itemVerId").val();
    stoFormParams = [];
    stoFormParams.push({name: "stageId", value: currentBusiId});
    stoFormParams.push({name: "itemVerId", value: itemVerId});
    selectAllStoFormTable.bootstrapTable('refresh');
}

function deleteStoForm(stageItemId) {

    $("#uploadProgress").modal('show');
    if (stageItemId != null && stageItemId != '') {
        $.ajax({
            type: "POST",
            url: ctx + '/aea/par/stage/stageOneform/deleteStoformByStageItemId.do',
            data: {stageItemId: stageItemId},
            success: function (result) {
                if (result.success) {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal({
                            type: 'success',
                            title: '删除事项与智能表单关联成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        refreshParOneformTable();
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

// 查询
function searchStagePartform() {

    commonQueryParams = [];
    var t = $('#search_stage_parform').serializeArray();
    $.each(t, function () {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    // commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    $("#stage_partform_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_partform_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStagePartform() {

    $('#search_stage_parform')[0].reset();
    searchStagePartform();
}

// 刷新
function refreshStagePartform() {

    searchStagePartform();
}

function parStageItemFormatter(value, row, index) {

    var sumformId = row.subFormId;
    var stageItemId = row.stageItemId;
    var strCallback = '';
    strCallback += 'needCallBackSaveActStoForm=1';
    strCallback += '&urlCallBackSaveActStoForm=' + restWebApp + 'aea/par/stage/item/saveAeaParStageItemBySubformId.do?stageItemId=' + stageItemId;
    strCallback += '&requiredField=refEntityId';

    if (sumformId != null && sumformId != '') {

        var updateStoform = '<a target="_blank" href= "' + ctx + '/design?formId=' + sumformId + '&' + strCallback + '" title="编辑智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-edit"></i>' +
            '</a>';

        var deleteStoform = '<a href="javascript:deleteStoForm(\'' + row.stageItemId + '\')" title="删除智能表单关联" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-trash"></i>' +
            '</a>';

        return updateStoform + deleteStoform;

    } else {

        var newParStageOneform = '<a target="_blank" href= "' + ctx + '/design?' + strCallback + '" title="新增智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-plus"></i>' +
            '</a>';

        var addParStageOneform = '<a href="javascript:showStoForm(\'' + row.stageItemId + '\',\'' + row.itemVerId + '\')" title="导入智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-cog"></i>' +
            '</a>';

        return newParStageOneform + addParStageOneform;

    }
}

function isSmartFormFormatter(value, row, index) {

    if (value) {
        if (value == '1') {
            return "智能表单";
        } else {
            return "开发表单";
        }
    }
}

function formNameFormatter(value, row, index) {

    var isSmartForm = row.isSmartForm;
    if (isSmartForm) {
        if (isSmartForm == '1') {
            return row.formName;
        } else {
            return row.formUrl;
        }
    }
}

function useElFormatter(value, row, index) {

    if (value) {
        if (value == '0') {
            return '禁用';
        } else {
            return '启用';
        }
    }
}

function stagePartformFormatter(value, row, index) {

    var sumformId = row.stoFormId;
    var isSmartForm = row.isSmartForm;
    var stageItemId = row.stagePartformId;
    var strCallback = '';
    strCallback += 'needCallBackSaveActStoForm=1';
    strCallback += '&urlCallBackSaveActStoForm=' + restWebApp + 'aea/par/stage/partform/saveStagePartformByPartformId.do?stageItemId=' + stageItemId;
    strCallback += '&requiredField=refEntityId';

    var editPartForm = '<a href="javascript:editStagePartFormById(\'' + row.stagePartformId + '\')" title="编辑扩展表"' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
        '<i class="la la-edit"></i>' +
        '</a>';

    var delPartForm = '<a href="javascript:delStagePartFormById(\'' + row.stagePartformId + '\')" title="删除扩展表" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
        '<i class="la la-trash"></i>' +
        '</a>';

    if (sumformId != null && sumformId != '' && sumformId != undefined) {

        var updateForm = '<a target="_blank" href= "' + ctx + '/design?formId=' + sumformId + '&' + strCallback + '"title="编辑智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-pencil"></i>' +
            '</a>';

        var updateDevForm = '<a href= "javascript:editStageDevFormById(\'' + sumformId + '\',\'' + row.stagePartformId + '\')" title="编辑开发表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-pencil"></i>' +
            '</a>';

        var delForm = '<a href="javascript:deleteStagePartFormRel(\'' + row.stagePartformId + '\')" title="删除表单关联" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-times"></i>' +
            '</a>';

        if (isSmartForm && isSmartForm == '1') {
            return editPartForm + updateForm + delForm + delPartForm;
        } else {
            return editPartForm + updateDevForm + delForm + delPartForm;
        }

    } else {

        var newForm = '<a target="_blank" href= "' + ctx + '/design?' + strCallback + '" title="新增智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-plus"></i>' +
            '</a>';

        var newDevForm = '<a href= "javascript:addStageDevform(\'' + row.stagePartformId + '\')" title="新增开发表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-plus"></i>' +
            '</a>';

        var addForm = '<a href="javascript:importFormForStagePart(\'' + row.stagePartformId + '\',\'' + row.stageId + '\',\'1\')" title="导入智能表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-cog"></i>' +
            '</a>';

        var addDevForm = '<a href="javascript:importFormForStagePart(\'' + row.stagePartformId + '\',\'' + row.stageId + '\',\'0\')" title="导入开发表单"' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill">' +
            '<i class="la la-cog"></i>' +
            '</a>';

        if (isSmartForm && isSmartForm == '1') {
            return editPartForm + newForm + addForm + delPartForm;
        } else {
            return editPartForm + newDevForm + addDevForm + delPartForm;
        }
    }
}

// 新增
function addStagePartform() {

    if (curIsEditable) {

        $('#aedit_part_form_modal').modal('show');
        $('#aedit_part_form_modal_title').html('新增扩展表');
        $('#aedit_part_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#stageElDiv').hide();
        $('#aedit_part_form_form')[0].reset();
        if (aedit_part_form_form_validator != null) {
            $("#aedit_part_form_form").validate().resetForm();
        }
        $('#saveParPartform').show();
        $('#formUrlDiv').hide();
        $('#aedit_part_form_form input[name="stagePartformId"]').val('');
        $('#aedit_part_form_form input[name="stageId"]').val(currentBusiId);
        $('#aedit_part_form_form input[name="stoFormId"]').val('');
        $("#aedit_part_form_form input[name='useEl'][value='0']").prop("checked", true);
        $("#aedit_part_form_form input[name='isSmartForm'][value='1']").prop("checked", true);

        $.ajax({
            url: ctx + '/aea/par/stage/partform/getMaxSortNo.do',
            type: 'POST',
            data: {'stageId': currentBusiId},
            async: false,
            success: function (data) {
                if (data) {
                    $("#aedit_part_form_form input[name='sortNo']").val(data);
                }
            },
        });
    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 编辑扩展表
function editStagePartFormById(stagePartformId) {

    if (stagePartformId) {

        $('#aedit_part_form_modal').modal('show');
        $('#aedit_part_form_modal_title').html('编辑扩展表');
        $('#aedit_part_form_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#stageElDiv').hide();
        $('#aedit_part_form_form')[0].reset();
        if (aedit_part_form_form_validator != null) {
            $("#aedit_part_form_form").validate().resetForm();
        }
        if (curIsEditable) {
            $('#saveParPartform').show();
        } else {
            $('#saveParPartform').hide();
        }

        $.ajax({
            url: ctx + '/aea/par/stage/partform/getStagePartform.do',
            type: 'POST',
            data: {'id': stagePartformId},
            async: false,
            success: function (data) {
                if (data) {

                    if (data.useEl == '1') {
                        $('#stageElDiv').show();
                    } else {
                        $('#stageElDiv').hide();
                    }

                    if(data.isSmartForm=='1'){
                        $('#formUrlDiv').hide();
                    }else{
                        $('#formUrlDiv').show();
                    }
                    loadFormData(true, '#aedit_part_form_form', data);
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

// 删除扩展表
function delStagePartFormById(stagePartformId) {
    if (curIsEditable) {
        if (stagePartformId) {
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
                        url: ctx + '/aea/par/stage/partform/deleteStagePartformById.do',
                        type: 'POST',
                        data: {'id': stagePartformId},
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
                                searchStagePartform();
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
        } else {
            swal('提示信息', '操作对象id为空!', 'info');
        }
    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 批量删除
function batchDelStagePartform() {

    if (curIsEditable) {
        var rows = $("#stage_partform_tb").bootstrapTable('getSelections');
        var stagePartformIds = [];
        if (rows != null && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                stagePartformIds.push(rows[i].stagePartformId);
            }
            swal({
                title: '此操作影响：',
                text: '将批量删除扩展表数据,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/par/stage/partform/deleteStagePartformByIds.do',
                        type: 'POST',
                        data: {'ids': stagePartformIds.toString()},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    title: '提示信息',
                                    text: '批量删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                searchStagePartform();
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
        } else {
            swal('提示信息', '请选择数据！', 'info');
        }
    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 删除关联
function deleteStagePartFormRel(stagePartformId) {

    if (curIsEditable) {
        if (stagePartformId) {
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
                        url: ctx + '/aea/par/stage/partform/delStagePartformRelFormById.do',
                        type: 'POST',
                        data: {'id': stagePartformId},
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
                                searchStagePartform();
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
        } else {
            swal('提示信息', '操作对象id为空!', 'info');
        }
    } else {
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 导入表单
var curPartFormId = null;
function importFormForStagePart(stagePartformId, stageId, _isSmartForm) {

    isSmartForm = _isSmartForm;
    if (curIsEditable) {
        curPartFormId = stagePartformId;
        $('#import_partform_form_modal').modal('show');
        clearSearchPartformForm();
    } else {
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
        $('#add_dev_form_modal_title').html('编辑开发表单');
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
