var seleted_nav;
var showParNavTable;
var customTable_tree;
var showParNavTbCurrentPageNumber = 1;  //记录当前页面
var showParNavTbCurrentPageSize = 10;   //记录页面大小
var treegridCurrentPageNumber = 1;  //记录当前页面
var treegridCurrentPageSize = 10;   //记录页面大小
var commonQueryParams= [];

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    $('#showParNavTable').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    $('#factor_treegrid_tb').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
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

    $('#showParNavTable').on('check.bs.table', function (e, row, $element) {

        seleted_nav = row.navId;
        refreshFactor();
        return;
    });

    $('#factor_treegrid_tb').on('dbl-click-row.bs.table', function (e, row, $element) {

        editParFactor(row.factorId , row.isQuestion);
    });

    $('#closeAddNavBtn').click(function () {
        $('#add_nav_modal').modal('hide');
    });

    $('#closeAddFactorBtn').click(function () {
        $('#add_factor_modal').modal('hide');
    });

    $('#add_par_factor_form select[name="useEl"]').change(function(){
        var value = $(this).val();
        if(value=='1'){
            $('#stateElDiv').show();
        }else{
            $('#stateElDiv').hide();
        }
    });

    $("#saveParThemeBtn").click(function () {

        var treeObj = $.fn.zTree.getZTreeObj('parThemeTree');
        var nodes = treeObj.getSelectedNodes();
        if(nodes.length <=0){
            swal('提示信息', '请选择主题！', 'info');
            return;
        }
        $('#select_par_theme_ztree_modal').modal('hide');
        $('#add_par_factor_form input[name="themeId"]').val(nodes[0].id);
        $('#add_par_factor_form input[name="themeName"]').val(nodes[0].name);
    });

    initNavTbGrid();
    initNavFormValidate();
    initNavFactorTreeGrid();
    initFactorFormValidate();
});

function initNavTbGrid(){

    var url = ctx + '/aea/par/nav/listAeaParNavPage.do?isDeleted=0';
    showParNavTable = $('#showParNavTable').bootstrapTable({
        url: url,
        columns: getNavTbGridColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign:"left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            showParNavTbCurrentPageNumber = number;
            showParNavTbCurrentPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: queryParamsNavTb,
        sidePagination: 'server',
        singleSelect: true,
        clickToSelect: true,
    });
}

function getNavTbGridColumns(){
    
    var columns = [
        {
            checkbox: true,
            formatter: function (value, row, index) {
                if(index==0){
                    seleted_nav = row.navId;
                    refreshFactor();
                    return {
                        checked : true // 设置默认选中第一行
                    };
                }
            }
        },
        {
            field: 'Number',
            title: '序号',
            align: 'center',
            width: 10,
            formatter: function (value, row, index) { //返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
                return showParNavTbCurrentPageSize * (showParNavTbCurrentPageNumber - 1) + index + 1;
            }
        },
        {
            field: 'navName',
            title: '名称',
            align: 'left',
            width: 180
        },
        {
            field: 'isActive',
            align: 'center',
            width: 50,
            title: '是否启用',
            formatter: isActiveFormatter
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 85,
            formatter: operatorFormatter
        }
    ];
    return columns;
}

function queryParamsNavTb(params) {

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

function initFactorFormValidate() {

    $("#add_par_factor_form").validate({

        // 定义校验规则
        rules: {
            factorName: {
                required: true,
                maxlength: 500
            },
            isActive:{
                required: true
            },
            sortNo: {
                required: true
            }
        },
        messages: {
            factorName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            isActive:{
                required: '<font color="red">此项必填！</font>'
            },
            sortNo: {
                required: '<font color="red">此项必填！</font>'
            }
        },
        // 提交表单
        submitHandler: function (form) {
            var formData = {};
            var t = $('#add_par_factor_form').serializeArray();
            $.each(t, function () {
                formData[this.name] = this.value;
            });
            $("#uploadProgress").modal("show");
            setTimeout(function () {
                $.ajax({
                    url: ctx + '/aea/par/factor/saveOrUpdateAeaParFactor.do',
                    type: 'POST',
                    data: formData,
                    async: true,
                    success: function (result) {
                        $("#uploadProgress").modal("hide");
                        if (result.success) {
                            $('#add_factor_modal').modal('hide');
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            refreshFactor();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        $("#uploadProgress").modal("hide");
                        swal('错误信息', '保存信息失败！', 'error');
                    }
                });
            }, 500);
        }
    });
}

function initNavFormValidate() {

    $("#add_par_nav_form").validate({

        // 定义校验规则
        rules: {
            navName: {
                required: true,
                maxlength: 500
            },
            sortNo: {
                required: true
            }
        },
        messages: {
            navName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            sortNo: {
                required: '<font color="red">此项必填！</font>'
            }
        },
        // 提交表单
        submitHandler: function (form) {
            var formData = {};
            var t = $('#add_par_nav_form').serializeArray();
            $.each(t, function () {
                formData[this.name] = this.value;
            });
            $("#uploadProgress").modal("show");
            setTimeout(function () {
                $.ajax({
                    url: ctx + '/aea/par/nav/saveOrUpdateAeaParNav.do',
                    type: 'POST',
                    data: formData,
                    async: true,
                    success: function (result) {
                        $("#uploadProgress").modal("hide");
                        if (result.success) {
                            $('#add_nav_modal').modal('hide');
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            refreshNav();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        $("#uploadProgress").modal("hide");
                        swal('错误信息', '保存信息失败！', 'error');
                    }
                });
            }, 500);
        }
    });
}

/**
 * 参数设置
 * @param params
 * @returns {{pagination: *, sort: *}}
 */
function dealQueryParams(params) {

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
    var keyword = $('#search_nav_name').val();
    queryParam.keyword = keyword;
    return queryParam;
}

/**
 * 阶段设置相关操作
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */
function operatorFormatter(value, row, index, field) {

    var editBtn = '<a href="javascript:editParNav(\'' + row.navId + '\')" ' +
                       'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                       'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

    var deleteBtn = '<a href="javascript:deleteParNav(\'' + row.navId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    return editBtn + deleteBtn;
}


/**
 * @param sname  获取值得对象
 * @param tname 需要赋值的对象
 * @param isMulit 是否多个
 * @param formRender 表单render
 */
function checkboxOnclick(sname, tname, isMulit, formRender) {

    var obj = document.getElementsByName(sname);
    if (obj) {
        if (isMulit) {
            var check_val = [];
            for (var k in obj) {
                if (obj[k].checked) {
                    check_val.push(obj[k].value);
                }
            }
            var cb = check_val.join(',');
            $(formRender + ' input[name="' + tname + '"]').val(cb);
        } else {
            if (obj[0].checked) {
                $(formRender + ' input[name="' + tname + '"]').val('1');
                if (sname == 'useEl1') {
                    $('#stateElDiv').show();
                }
            } else {
                $(formRender + ' input[name="' + tname + '"]').val('0');
                if (sname == 'useEl1') {
                    $('#stateElDiv').hide();
                }
            }
        }
    }
}

function deleteParNav(id) {

    if (id) {
        swal({
            text: '确认删除该导航及其关联因素？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $("#uploadProgress").modal("show");
                setTimeout(function () {
                    $.ajax({
                        url: ctx + '/aea/par/nav/deleteAeaParNavById.do',
                        type: 'POST',
                        data: {'id': id},
                        async: true,
                        success: function (data) {
                            $("#uploadProgress").modal('hide');
                            if (data && data.success) {
                                swal({
                                    type: 'success',
                                    title: '操作成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                refreshNav();
                            } else {
                                swal('错误信息', "获取信息失败！", 'error');
                            }
                        },
                        error: function () {
                            $("#uploadProgress").modal("hide");
                            swal('错误信息', "获取信息失败！", 'error');
                        }
                    });
                }, 500);
            }
        });

    } else {
        swal('提示信息', "请选择操作记录！", 'info');
    }
}

function deleteParFactor(factorId){

    swal({
        text: '此操作将删除本对象以及下级对象(包含关联主题数据),您确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgress").modal("show");
            setTimeout(function () {
                $.ajax({
                    url: ctx + '/aea/par/factor/deleteAeaParFactorById.do',
                    type: 'POST',
                    data: {'id': factorId},
                    async: true,
                    success: function (data) {
                        $("#uploadProgress").modal('hide');
                        if (data && data.success) {
                            swal({
                                type: 'success',
                                title: '操作成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            refreshFactor();
                        } else {
                            swal('错误信息', "获取信息失败！", 'error');
                        }
                    },
                    error: function () {
                        $("#uploadProgress").modal("hide");
                        swal('错误信息', "获取信息失败！", 'error');
                    }
                });
            }, 500);
        }
    });
}

/**
 * 编辑导航
 * @param id
 */
function editParNav(id) {

    if (id) {

        $('#add_nav_modal').modal('show');
        $('#add_nav_modal_title').html('编辑导航');
        $('#add_par_nav_form')[0].reset();
        $("#add_par_nav_form").validate().resetForm();
        $('#add_par_nav_form input[name="navId"]').val('');
        $.ajax({
            url: ctx + '/aea/par/nav/getAeaParNavById.do',
            type: 'POST',
            data: {'id': id},
            async: true,
            success: function (data) {
                if (data && data.success) {
                    loadFormData(true, '#add_par_nav_form', data.content);
                } else {
                    swal('错误信息', "获取信息失败！", 'error');
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

/**
 * 添加导航
 * @param oper
 */
function addParNav() {

    $('#add_nav_modal').modal('show');
    $('#add_nav_modal_title').html('新增导航');
    $('#add_par_nav_form')[0].reset();
    $("#add_par_nav_form").validate().resetForm();
    $("#add_par_nav_form input[name='navId']").val('');
    $("#add_par_nav_form input[name='isActive']").val('0');
    // $("#add_par_nav_form select[name='isActive'] option:eq(0)").prop("selected", 'selected');

    $.ajax({
        url: ctx + '/aea/par/nav/getMaxSortNo.do',
        type: 'POST',
        async: false,
        success: function (data) {
            if(data){
                $("#add_par_nav_form input[name='sortNo']").val(data);
            }
        }
    });
}


function showParFactor(oper, factorId, parentFactorId, isQuestion, navId) {

    $('#add_par_factor_form')[0].reset();
    $("#add_par_factor_form").validate().resetForm();

    if (isQuestion == '1') {
        $('#useElDiv').hide();
        $('#stateElDiv').hide();
        $('#isQuestionDiv').show();
    } else {
        $('#useElDiv').show();
        $('#stateElDiv').hide();
        $('#isQuestionDiv').hide();
    }

    if (oper == 'edit') {

        $('#add_factor_modal').modal('show');
        $('#add_factor_modal_title').html(isQuestion == '1'?'编辑【问】情形':'编辑【答】情形');
        $('#add_par_factor_form input[name="factorId"]').val('');
        $('#add_par_factor_form input[name="parentFactorId"]').val('');
        $('#add_par_factor_form input[name="navId"]').val('');

        $.ajax({
            url: ctx + '/aea/par/factor/getAeaParFactorWithThemeById.do',
            type: 'POST',
            data: {'id': factorId},
            async: true,
            success: function (data) {
                if (data && data.success) {

                    if (data.content.useEl == '1') {
                        $('#stateElDiv').show();
                    } else {
                        $('#stateElDiv').hide();
                    }
                    loadFormData(true, '#add_par_factor_form', data.content);
                } else {
                    swal('错误信息', "获取信息失败！", 'error');
                }
            },
            error: function () {
                swal('错误信息', "获取信息失败！", 'error');
            }
        });
    } else {

        $('#add_factor_modal').modal('show');
        $('#add_factor_modal_title').html(isQuestion == '1'?'新增【问】情形':'新增【答】情形');
        $('#add_par_factor_form input[name="factorId"]').val('');
        $('#add_par_factor_form input[name="parentFactorId"]').val(parentFactorId);
        $('#add_par_factor_form input[name="navId"]').val(navId);
        $('#add_par_factor_form input[name="isQuestion"]').val(isQuestion);
        $('#add_par_factor_form select[name="isActive"] option:eq(0)').prop('selected', 'selected');
        $('#add_par_factor_form select[name="useEl"] option:eq(1)').prop('selected', 'selected');
        $('#stateElDiv').hide();

        $.ajax({
            url: ctx + '/aea/par/factor/getMaxSortNo.do',
            type: 'POST',
            data: {
                navId: navId,
            },
            async: true,
            success: function (data) {
                if(data){
                    $("#add_par_factor_form input[name='sortNo']").val(data);
                }
            },
        });
    }
}

function addParFactor() {

    if (!seleted_nav) {
        swal('提示信息', "请先选择主题导航！", 'info');
        return;
    }
    showParFactor('add', null, null, '1', seleted_nav);
    showThemeSelectDiv('1');
}

function showThemeSelectDiv(isQuestion){

    if(isQuestion=='0'){
        $('#themeNameDiv').show();
        // $("#add_par_factor_form input[name='themeName']").rules('add',{
        //     required: true,
        //     messages:{
        //         required: '<font color="red">此项必填！</font>'
        //     }
        // });
        $("#add_par_factor_form select[name='mustAnswer']").rules('remove');
        $("#add_par_factor_form select[name='answerType']").rules('remove');
    }else {
        $('#themeNameDiv').hide();
        $("#add_par_factor_form input[name='themeName']").rules('remove');
        $("#add_par_factor_form select[name='mustAnswer']").rules('add',{
            required: true,
            messages:{
                required: '<font color="red">此项必填！</font>'
            }
        });
        $("#add_par_factor_form select[name='answerType']").rules('add',{
            required: true,
            messages:{
                required: '<font color="red">此项必填！</font>'
            }
        });
    }
}

function addParFactorChild(factorId, isQuestion, navId) {

    var isQuestion_ = isQuestion == '1' ? '0' : '1';
    showParFactor('add', null, factorId, isQuestion_, navId);
    showThemeSelectDiv(isQuestion_);
}

function editParFactor(factorId, isQuestion) {

    showParFactor('edit', factorId, null, isQuestion, null);
    showThemeSelectDiv(isQuestion);
}

function searchParNav(){

    commonQueryParams = [];
    commonQueryParams.push({name: 'keyword', value: $('#search_nav_name').val()});
    $("#showParNavTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#showParNavTable").bootstrapTable('refresh');       //无参数刷新
}

function clearSearchParNav(){

    $('#search_nav_name').val('');
    searchParNav();
}

/**
 * 刷新导航列表
 */
function refreshNav() {

    searchParNav();
}

function isActiveFormatter(value, row, index, field) {

    if(row.isActive=='1'){
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" checked="checked" name="isActive" onclick="changeNavIsActive(this,\''+ row.navId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }else{
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" name="isActive" onclick="changeNavIsActive(this,\''+ row.navId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }
}

function changeNavIsActive(obj, id, isActive){

    if(id){
        if(isActive=='0'){
            $.ajax({
                url: ctx + '/aea/par/nav/listAeaParNavByNoPage.do',
                type: 'POST',
                data: {},
                success: function (data) {
                    if(data&&data.length>0){
                        swal('错误信息', '已经存在其他启用的导航定义,如要启用当前定义，请先禁用其他已启用定义！', 'error');
                        isActive=='1'?$(obj).prop("checked",true):$(obj).prop("checked",false);
                        return;
                    }
                }
            });
        }
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
                    url: ctx + '/aea/par/nav/changIsActiveState.do',
                    type: 'POST',
                    data: {'navId': id},
                    success: function (result) {
                        if(result.success){
                            searchParNav();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '服务器异常！', 'error');
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

//定义树表格的显示列
var getFactorTreeGridColumns = function () {

    var columns = [
        // {
        //     field: 'Number',
        //     title: '序号',
        //     align: 'center',
        //     width: 10,
        //     formatter: function (value, row, index) {
        //         if (row.pid != null) {
        //             return '';
        //         }
        //         return treegridCurrentPageSize * (treegridCurrentPageNumber - 1) + + index + 1 ;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
        //     }
        // },
        {
            checkbox: true,
        },
        {
            field: 'factorName',
            title: '名称',
            align: 'left',
            width: 150
        },
        {
            field: 'isQuestion',
            align: 'center',
            width: 40,
            title: '情形类型',
            formatter: function (value, row) {
                return value == '1' ? '问题' : '答案';
            }
        },
        {
            field: 'mustAnswer',
            align: 'center',
            width: 40,
            title: '是否必答',
            formatter: function (value, row) {
                if (row.isQuestion != '1') {
                    return '-';
                }
                return value == '1' ? '必答' : '可选';
            }
        },
        {
            field: 'answerType',
            align: 'center',
            width: 40,
            title: '回答方式',
            formatter: function (value, row) {
                if (row.isQuestion != '1') {
                    return '-';
                }
                if (value == 'y') {
                    return '是否选择';
                } else if (value = 's') {
                    return '单选';
                } else if (value = 'm') {
                    return '多选';
                } else {
                    return '-';
                }
            }
        },
        {
            field: 'themeName',
            title: '关联主题',
            align: 'left',
            width: 150,
            formatter: function (value, row) {

                if(value){
                    return '<a href="javascript:void(0);" onclick="showBindingTheme(\''+row.themeId+'\')">'+value+'</a>';
                }
            }
        },
        {
            field: 'operate_',
            align: 'left',
            title: '操作',
            width: 110,
            formatter: factorOperatorFormatter
        }
    ];
    return columns;
}

function unbindTheme(factorId){

    swal({
        text: '确认解除主题绑定？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: ctx + '/aea/par/factor/unbindTheme.do',
                type: 'POST',
                data: {'factorId': factorId},
                async: true,
                success: function (data) {
                    if (data && data.success) {
                        swal({
                            type: 'success',
                            title: '操作成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        refreshFactor();
                    } else {
                        swal('错误信息', "操作失败！", 'error');
                    }
                },
                error: function () {
                    swal('错误信息', "操作失败！", 'error');
                }
            });
        }
    });
}

function factorOperatorFormatter(value, row, index) {

    var title = row.isQuestion == '1' ? '编辑问题情形':'编辑答案情形';

    var editBtn = '<a href="javascript:editParFactor(\'' + row.factorId + '\',\'' + row.isQuestion + '\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                      'title="' + title + '"><i class="la la-edit"></i>' +
                  '</a>';

    var addChildBtn = '<a href="javascript:addParFactorChild(\'' + row.factorId + '\',\'' + row.isQuestion + '\',\'' + row.navId + '\')" ' +
                          'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                          'title="添加子情形"><i class="la la-plus"></i>' +
                      '</a>';

    var deleteBtn = '<a href="javascript:deleteParFactor(\'' + row.factorId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除情形"><i class="la la-trash"></i>' +
                    '</a>';

    var showBindingThemeBtn = "";
    if(row.themeId){
        showBindingThemeBtn = '<a href="javascript:showBindingTheme(\'' + row.themeId + '\')" ' +
                                 'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                 'title="查看绑定主题"><i class="la la-search"></i>' +
                             '</a>';
    }

    var unbindThemeBtn = "";
    if(row.themeId){
        unbindThemeBtn = '<a href="javascript:unbindTheme(\'' + row.factorId + '\')" ' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                            'title="解除绑定主题"><i class="la la-remove"></i>' +
                        '</a>';
    }

    return editBtn + addChildBtn + showBindingThemeBtn+ unbindThemeBtn + deleteBtn
}


function queryParamsFactorTree(params) {

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
        // pagination: pagination,
        // sort: sort
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

function initNavFactorTreeGrid() {

    var url = ctx + '/aea/par/factor/listAeaParFactorByNoPage.do';
    customTable_tree = $('#factor_treegrid_tb').bootstrapTable({
        url: url,
        columns: getFactorTreeGridColumns(),
        pagination: false,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        onPageChange: function (number, size) {
            treegridCurrentPageNumber = number;
            treegridCurrentPageSize = size;
        },
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: queryParamsFactorTree,
        sidePagination: 'server',
        singleSelect: true,
        clickToSelect: true,
        treeShowField: 'factorName',
        parentIdField: 'pid',
        idField: 'factorId',
        onPreBody: function (data) {
            if (data.length > 0) {
                var ids = [];
                $(data).each(function (i, item) {
                    ids.push(item.factorId);
                    item.pid = item.parentFactorId;   //pid属性用于树表格显示
                });
                $(data).each(function (i, item) {
                    if (item.pid != null && $.inArray(item.pid, ids) == -1) { //父节点不在返回数据中的，设置pid为null
                        item.pid = null;
                    }
                });
            }
        },
        onLoadSuccess: function (result) {

            $('#factor_treegrid_tb').treegrid({
                initialState: 'expanded', // 所有节点都展开
                treeColumn: 1,
                onChange: function () {
                    $('#factor_treegrid_tb').bootstrapTable('resetWidth');
                }
            });
        }
    });
}

function searchParFactor() {

    commonQueryParams = [];
    commonQueryParams.push({name: 'keyword', value: $('#search_factor_name').val()});
    commonQueryParams.push({name: 'navId', value: seleted_nav});
    $("#factor_treegrid_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#factor_treegrid_tb").bootstrapTable('refresh');       //无参数刷新
}

function clearSearchParFactor(){

    $('#search_factor_name').val('');
    searchParFactor();
}

function refreshFactor() {

    searchParFactor();
}

function showBindingTheme(themeId) {

    $.ajax({
        url: ctx + '/aea/par/theme/getAeaParTheme.do',
        type: 'POST',
        data: {id: themeId},
        async: true,
        success: function (data) {
            if (data ) {
                $('#binding_theme_modal').modal('show');
                loadFormData(true, '#view_theme_form', data);
            } else {
                swal('错误信息', "获取数据失败！", 'error');
            }
        },
        error: function () {
            swal('错误信息', "获取数据失败！", 'error');
        }
    });
}