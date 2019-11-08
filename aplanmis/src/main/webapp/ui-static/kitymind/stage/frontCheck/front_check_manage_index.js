var par_front_proj_tb;
var edit_par_front_proj_form_validator;
var par_front_item_tb;
var edit_par_front_item_form_validator;
var par_front_itemform_tb;
var edit_par_front_itemform_form_validator;
var par_front_stage_tb;
var edit_par_front_stage_form_validator;
var par_front_partform_tb;
var edit_par_front_partform_form_validator;
$(function(){
    if(isCheckProj=='1'){
        par_front_proj_tb = defaultBootstrap("par_front_proj_tb",[{
            checkbox: true,
            field: '#',
            align: "center",
            title: '#',
            sortable: false,
            width: 10,
            textAlign: 'center', 
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        }, {
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "frontProjId",
            visible: false
        }, {
            field: "ruleName",
            title: "规则名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: "ruleEl",
            title: "规则表达式",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "sortNo",
            title: "排序",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true
        },{
            field: "isActive",
            title: "是否启用",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isActive=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:editParFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delParFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/par/front/proj/listAeaParFrontProjByPage.do');
        var par_front_proj_tb_params = {stageId:currentBusiId};
        par_front_proj_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(par_front_proj_tb_params).setRowId("frontProjId").setClearBtn("par_front_proj_clear_btn").setSearchBtn("par_front_proj_search_btn","par_front_proj_keyword").init();

        // 设置初始化校验
        edit_par_front_proj_form_validator = $("#edit_par_front_proj_form").validate({

            // 定义校验规则
            rules: {
                ruleName: {
                    required: true
                },
                sortNo:{
                    required: true
                },
                ruleEl:{
                    required: true
                },
                isActive:{
                    required: true
                }
            },
            messages: {
                ruleName: {
                    required: '<font color="red">此项必填！</font>'
                },
                sortNo:{
                    required: '<font color="red">此项必填！</font>'
                },
                ruleEl:{
                    required: '<font color="red">此项必填！</font>'
                },
                isActive:{
                    required: '<font color="red">是否启用必选！</font>'
                }
            },
            // 提交表单
            submitHandler: function (form) {

                var d = {};
                var t = $('#edit_par_front_proj_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/par/front/proj/saveOrUpdateAeaParFrontProj.do',
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
                                $('#edit_par_front_proj_modal').modal('hide');
                                par_front_proj_tb.clear();

                            },500);
                        } else {
                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }

    if(isCheckItem=='1'){
        par_front_item_tb = defaultBootstrap("par_front_item_tb",[{
            checkbox: true,
            field: '#',
            align: "center",
            title: '#',
            sortable: false,
            width: 10,
            textAlign: 'center',
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        }, {
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "frontItemId",
            visible: false
        }, {
            field: "itemName",
            title: "前置事项名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "sortNo",
            title: "排序",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true
        },{
            field: "isActive",
            title: "是否启用",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isActive=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:editParFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delParFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/par/front/item/listAeaParFrontItemByPage.do');
        var par_front_item_tb_params = {stageId:currentBusiId};
        par_front_item_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(par_front_item_tb_params).setRowId("frontItemId").setClearBtn("par_front_item_clear_btn").setSearchBtn("par_front_item_search_btn","par_front_item_keyword").init();

        // 设置初始化校验
        edit_par_front_item_form_validator = $("#edit_par_front_item_form").validate({

            // 定义校验规则
            rules: {
                itemName: {
                    required: true
                },
                sortNo:{
                    required: true
                },
                isActive:{
                    required: true
                }
            },
            messages: {
                itemName: {
                    required: '<font color="red">此项必选！</font>'
                },
                sortNo:{
                    required: '<font color="red">此项必填！</font>'
                },
                isActive:{
                    required: '<font color="red">是否启用必选！</font>'
                }
            },
            // 提交表单
            submitHandler: function (form) {

                var d = {};
                var t = $('#edit_par_front_item_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/par/front/item/saveOrUpdateAeaParFrontItem.do',
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
                                $('#edit_par_front_item_modal').modal('hide');
                                par_front_item_tb.clear();

                            },500);
                        } else {
                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }


    if(isCheckItemForm=='1'){
        par_front_itemform_tb = defaultBootstrap("par_front_itemform_tb",[{
            checkbox: true,
            field: '#',
            align: "center",
            title: '#',
            sortable: false,
            width: 10,
            textAlign: 'center',
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        }, {
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "frontItemformId",
            visible: false
        }, {
            field: "itemName",
            title: "事项名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "formName",
            title: "表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "sortNo",
            title: "排序",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true
        },{
            field: "isActive",
            title: "是否启用",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isActive=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:editParFrontItemform(\'' + row.frontItemformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delParFrontItemform(\'' + row.frontItemformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/par/front/itemform/listAeaParFrontItemformByPage.do');
        var par_front_itemform_tb_params = {stageId:currentBusiId};
        par_front_itemform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(par_front_itemform_tb_params).setRowId("frontItemformId").setClearBtn("par_front_itemform_clear_btn").setSearchBtn("par_front_itemform_search_btn","par_front_itemform_keyword").init();

        // 设置初始化校验
        edit_par_front_itemform_form_validator = $("#edit_par_front_itemform_form").validate({

            // 定义校验规则
            rules: {
                formName: {
                    required: true
                },
                sortNo:{
                    required: true
                },
                isActive:{
                    required: true
                }
            },
            messages: {
                formName: {
                    required: '<font color="red">此项必选！</font>'
                },
                sortNo:{
                    required: '<font color="red">此项必填！</font>'
                },
                isActive:{
                    required: '<font color="red">是否启用必选！</font>'
                }
            },
            // 提交表单
            submitHandler: function (form) {

                var d = {};
                var t = $('#edit_par_front_itemform_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/par/front/itemform/saveOrUpdateAeaParFrontItemform.do',
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
                                $('#edit_par_front_itemform_modal').modal('hide');
                                par_front_itemform_tb.clear();

                            },500);
                        } else {
                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }


    if(isCheckStage=='1'){
        par_front_stage_tb = defaultBootstrap("par_front_stage_tb",[{
            checkbox: true,
            field: '#',
            align: "center",
            title: '#',
            sortable: false,
            width: 10,
            textAlign: 'center',
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        }, {
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "frontStageId",
            visible: false
        }, {
            field: "histStageName",
            title: "前置阶段名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "sortNo",
            title: "排序",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true
        },{
            field: "isActive",
            title: "是否启用",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isActive=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:editParFrontStage(\'' + row.frontStageId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delParFrontStage(\'' + row.frontStageId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/par/front/stage/listAeaParFrontStageByPage.do');
        var par_front_stage_tb_params = {stageId:currentBusiId};
        par_front_stage_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(par_front_stage_tb_params).setRowId("frontStageId").setClearBtn("par_front_stage_clear_btn").setSearchBtn("par_front_stage_search_btn","par_front_stage_keyword").init();

        // 设置初始化校验
        edit_par_front_stage_form_validator = $("#edit_par_front_stage_form").validate({

            // 定义校验规则
            rules: {
                histStageId: {
                    required: true
                },
                sortNo:{
                    required: true
                },
                isActive:{
                    required: true
                }
            },
            messages: {
                histStageId: {
                    required: '<font color="red">此项必选！</font>'
                },
                sortNo:{
                    required: '<font color="red">此项必填！</font>'
                },
                isActive:{
                    required: '<font color="red">是否启用必选！</font>'
                }
            },
            // 提交表单
            submitHandler: function (form) {

                var d = {};
                var t = $('#edit_par_front_stage_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/par/front/stage/saveOrUpdateAeaParFrontStage.do',
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
                                $('#edit_par_front_stage_modal').modal('hide');
                                par_front_stage_tb.clear();

                            },500);
                        } else {
                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }


    if(isCheckPartform=='1'){
        par_front_partform_tb = defaultBootstrap("par_front_partform_tb",[{
            checkbox: true,
            field: '#',
            align: "center",
            title: '#',
            sortable: false,
            width: 10,
            textAlign: 'center',
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        }, {
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "frontPartformId",
            visible: false
        }, {
            field: "partformName",
            title: "阶段扩展表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }, {
            field: "isSmartForm",
            title: "是否智能表单",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isSmartForm=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: "sortNo",
            title: "排序",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true
        },{
            field: "isActive",
            title: "是否启用",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isActive=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }, {
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:editParFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delParFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/par/front/partform/listAeaParFrontPartformByPage.do');
        var par_front_partform_tb_params = {stageId:currentBusiId};
        par_front_partform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(par_front_partform_tb_params).setRowId("frontPartformId").setClearBtn("par_front_partform_clear_btn").setSearchBtn("par_front_partform_search_btn","par_front_partform_keyword").init();

        // 设置初始化校验
        edit_par_front_partform_form_validator = $("#edit_par_front_partform_form").validate({

            // 定义校验规则
            rules: {
                partformName: {
                    required: true
                },
                sortNo:{
                    required: true
                },
                isActive:{
                    required: true
                }
            },
            messages: {
                partformName: {
                    required: '<font color="red">此项必选！</font>'
                },
                sortNo:{
                    required: '<font color="red">此项必填！</font>'
                },
                isActive:{
                    required: '<font color="red">是否启用必选！</font>'
                }
            },
            // 提交表单
            submitHandler: function (form) {

                var d = {};
                var t = $('#edit_par_front_partform_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/par/front/partform/saveOrUpdateAeaParFrontPartform.do',
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
                                $('#edit_par_front_partform_modal').modal('hide');
                                par_front_partform_tb.clear();

                            },500);
                        } else {
                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }
});

function addParFrontProj() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/par/front/proj/getMaxSortNo.do',
            type: 'POST',
            data: {stageId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveParFrontProjBtn').show();
                        $("#edit_par_front_proj_modal").modal("show");
                        $('#edit_par_front_proj_title').html('新增项目信息前置检测');
                        $('#edit_par_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_proj_form')[0].reset();
                        $('#edit_par_front_proj_form input[name="frontProjId"]').val('');
                        $('#edit_par_front_proj_form input[name="stageId"]').val(currentBusiId);
                        $('#edit_par_front_proj_form input[name="sortNo"]').val(result.content);

                    },500);
                } else {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delParFrontProj(id) {

    swal({
        title: '',
        text: '确定要删除选定的前置检测吗',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgressMsg").html("删除中，请稍后...");
            $("#uploadProgress").modal("show");
            $.ajax({
                url: ctx + '/aea/par/front/proj/deleteAeaParFrontProjById.do',
                type: 'POST',
                data: {id:id},
                async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            par_front_proj_tb.clear();
                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function batchDelParFrontProj() {
    if(curIsEditable) {
        var checkList = par_front_proj_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontProjId);
            }
            var id = ids.join(",")
            delParFrontProj(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editParFrontProj(frontProjId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/par/front/proj/getAeaParFrontProj.do',
        type: 'POST',
        data: {id:frontProjId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveParFrontProjBtn').show();
                    }else{
                        $('#saveParFrontProjBtn').hide();
                    }
                    $("#edit_par_front_proj_modal").modal("show");
                    $('#edit_par_front_proj_title').html('编辑项目信息前置检测');
                    $('#edit_par_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_par_front_proj_form')[0].reset();
                    loadFormData(true, '#edit_par_front_proj_form', result.content);
                    $('#edit_par_front_proj_form input[name="frontProjId"]').val(result.content.frontProjId);
                    $('#edit_par_front_proj_form input[name="stageId"]').val(currentBusiId);
                },500);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}

function addParFrontItem() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/par/front/item/getMaxSortNo.do',
            type: 'POST',
            data: {stageId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveParFrontItemBtn').show();
                        $("#edit_par_front_item_modal").modal("show");
                        $('#edit_par_front_item_title').html('新增事项信息前置检测');
                        $('#edit_par_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_item_form')[0].reset();
                        $('#edit_par_front_item_form input[name="frontItemId"]').val('');
                        $('#edit_par_front_item_form input[name="itemVerId"]').val('');
                        $('#edit_par_front_item_form input[name="stageId"]').val(currentBusiId);
                        $('#edit_par_front_item_form input[name="sortNo"]').val(result.content);

                    },500);
                } else {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editParFrontItem(frontItemId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/par/front/item/getAeaParFrontItem.do',
        type: 'POST',
        data: {frontItemId:frontItemId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveParFrontItemBtn').show();
                    }else{
                        $('#saveParFrontItemBtn').hide();
                    }
                    $("#edit_par_front_item_modal").modal("show");
                    $('#edit_par_front_item_title').html('编辑事项信息前置检测');
                    $('#edit_par_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_par_front_item_form')[0].reset();
                    loadFormData(true, '#edit_par_front_item_form', result.content);
                    $('#edit_par_front_item_form input[name="frontItemId"]').val(result.content.frontItemId);
                    $('#edit_par_front_item_form input[name="stageId"]').val(currentBusiId);
                    $('#edit_par_front_item_form input[name="itemVerId"]').val(result.content.itemVerId);
                    $('#edit_par_front_item_form input[name="itemName"]').val(result.content.itemName);
                },500);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}


function batchDelParFrontItem() {
    if(curIsEditable) {
        var checkList = par_front_item_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontItemId);
            }
            var id = ids.join(",")
            delParFrontItem(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delParFrontItem(id) {

    swal({
        title: '',
        text: '确定要删除选定的前置检测吗',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgressMsg").html("删除中，请稍后...");
            $("#uploadProgress").modal("show");
            $.ajax({
                url: ctx + '/aea/par/front/item/deleteAeaParFrontItemById.do',
                type: 'POST',
                data: {id:id},
                async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            par_front_item_tb.clear();
                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}


function addParFrontItemform() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/par/front/itemform/getMaxSortNo.do',
            type: 'POST',
            data: {stageId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveParFrontItemformBtn').show();
                        $("#edit_par_front_itemform_modal").modal("show");
                        $('#edit_par_front_itemform_title').html('新增事项表单信息前置检测');
                        $('#edit_par_front_itemform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_itemform_form')[0].reset();
                        $('#edit_par_front_itemform_form input[name="frontItemformId"]').val('');
                        $('#edit_par_front_itemform_form input[name="stageItemId"]').val('');
                        $('#edit_par_front_itemform_form input[name="stageId"]').val(currentBusiId);
                        $('#edit_par_front_itemform_form input[name="sortNo"]').val(result.content);

                    },500);
                } else {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editParFrontItemform(frontItemformId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/par/front/itemform/getAeaParFrontItemform.do',
        type: 'POST',
        data: {frontItemformId:frontItemformId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveParFrontItemformBtn').show();
                    }else{
                        $('#saveParFrontItemformBtn').hide();
                    }
                    $("#edit_par_front_itemform_modal").modal("show");
                    $('#edit_par_front_itemform_title').html('编辑事项表单信息前置检测');
                    $('#edit_par_front_itemform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_par_front_itemform_form')[0].reset();
                    loadFormData(true, '#edit_par_front_itemform_form', result.content);
                    $('#edit_par_front_itemform_form input[name="frontItemformId"]').val(result.content.frontItemformId);
                    $('#edit_par_front_itemform_form input[name="stageId"]').val(currentBusiId);
                    $('#edit_par_front_itemform_form input[name="stageItemId"]').val(result.content.stageItemId);
                    $('#edit_par_front_itemform_form input[name="itemName"]').val(result.content.itemName);
                    $('#edit_par_front_itemform_form input[name="formName"]').val(result.content.formName);
                },500);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}


function batchDelParFrontItemform() {
    if(curIsEditable) {
        var checkList = par_front_itemform_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontItemformId);
            }
            var id = ids.join(",")
            delParFrontItemform(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delParFrontItemform(id) {

    swal({
        title: '',
        text: '确定要删除选定的前置检测吗',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgressMsg").html("删除中，请稍后...");
            $("#uploadProgress").modal("show");
            $.ajax({
                url: ctx + '/aea/par/front/itemform/deleteAeaParFrontItemformById.do',
                type: 'POST',
                data: {id:id},
                async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            par_front_itemform_tb.clear();
                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function addParFrontStage() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/par/front/stage/getMaxSortNo.do',
            type: 'POST',
            data: {stageId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    loadSelectParFrontStage(null,'add',result.content);
                } else {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function loadSelectParFrontStage(frontStageId,flag,obj) {
    $.ajax({
        url: ctx + '/aea/par/front/stage/listSelectParFrontStage.do',
        type: 'POST',
        data: {stageId:currentBusiId,frontStageId:frontStageId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');

                    $("#edit_par_front_stage_modal").modal("show");

                    $('#histStageId').empty();

                    if(result && result.content){
                        for(var i=0;i<result.content.length;i++){
                            var frontStage = result.content[i];
                            var selected = ''
                            if("edit" == flag){
                                if(frontStage.histStageId==obj.histStageId){
                                    selected = ' selected ';
                                }
                            }
                            var opt=$('<option value="'+frontStage.histStageId+'"'+selected+'>'+frontStage.histStageName+'</option>');

                            $('#histStageId').append(opt);
                        }
                    }

                    if("add" == flag) {
                        $('#saveParFrontStageBtn').show();
                        $('#edit_par_front_stage_title').html('新增阶段信息前置检测');
                        $('#edit_par_front_stage_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_stage_form')[0].reset();
                        $('#edit_par_front_stage_form input[name="frontStageId"]').val('');
                        $('#edit_par_front_stage_form input[name="stageId"]').val(currentBusiId);
                        $('#edit_par_front_stage_form input[name="sortNo"]').val(obj);
                    }else{
                        if(curIsEditable){
                            $('#saveParFrontStageBtn').show();
                        }else{
                            $('#saveParFrontStageBtn').hide();
                        }
                        $('#edit_par_front_stage_title').html('编辑阶段信息前置检测');
                        $('#edit_par_front_stage_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_stage_form')[0].reset();
                        loadFormData(true, '#edit_par_front_stage_form', obj);
                        $('#edit_par_front_stage_form input[name="frontStageId"]').val(obj.frontStageId);
                        $('#edit_par_front_stage_form input[name="stageId"]').val(currentBusiId);
                    }

                },500);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}

function editParFrontStage(frontStageId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/par/front/stage/getAeaParFrontStage.do',
        type: 'POST',
        data: {frontStageId:frontStageId},
        async: false,
        success: function (result) {
            if (result.success) {
                loadSelectParFrontStage(result.content.frontStageId,'edit',result.content);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}


function batchDelParFrontStage() {
    if(curIsEditable) {
        var checkList = par_front_stage_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontStageId);
            }
            var id = ids.join(",")
            delParFrontStage(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delParFrontStage(id) {

    swal({
        title: '',
        text: '确定要删除选定的前置检测吗',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgressMsg").html("删除中，请稍后...");
            $("#uploadProgress").modal("show");
            $.ajax({
                url: ctx + '/aea/par/front/stage/deleteAeaParFrontStageById.do',
                type: 'POST',
                data: {id:id},
                async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            par_front_stage_tb.clear();
                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function addParFrontPartform() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/par/front/partform/getMaxSortNo.do',
            type: 'POST',
            data: {stageId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveParFrontPartformBtn').show();
                        $("#edit_par_front_partform_modal").modal("show");
                        $('#edit_par_front_partform_title').html('新增阶段扩展表单前置检测');
                        $('#edit_par_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_par_front_partform_form')[0].reset();
                        $('#edit_par_front_partform_form input[name="frontPartformId"]').val('');
                        $('#edit_par_front_partform_form input[name="stagePartformId"]').val('');
                        $('#edit_par_front_partform_form input[name="stageId"]').val(currentBusiId);
                        $('#edit_par_front_partform_form input[name="sortNo"]').val(result.content);
                        $('#edit_par_front_partform_form input[name="isSmartForm"]').attr("checked",false);
                    },500);
                } else {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editParFrontPartform(frontPartformId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/par/front/partform/getAeaParFrontPartform.do',
        type: 'POST',
        data: {frontPartformId:frontPartformId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveParFrontPartformBtn').show();
                    }else{
                        $('#saveParFrontPartformBtn').hide();
                    }
                    $("#edit_par_front_partform_modal").modal("show");
                    $('#edit_par_front_partform_title').html('编辑阶段扩展表单前置检测');
                    $('#edit_par_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_par_front_partform_form')[0].reset();
                    loadFormData(true, '#edit_par_front_partform_form', result.content);
                    $('#edit_par_front_partform_form input[name="frontPartformId"]').val(result.content.frontPartformId);
                    $('#edit_par_front_partform_form input[name="stageId"]').val(currentBusiId);
                    $('#edit_par_front_partform_form input[name="stagePartformId"]').val(result.content.stagePartformId);
                    $('#edit_par_front_partform_form input[name="partformName"]').val(result.content.partformName);

                    if("1"==result.content.isSmartForm){
                        $('#edit_par_front_partform_form input[name="isSmartForm"][value="1"]').attr("checked",true);
                    }else{
                        $('#edit_par_front_partform_form input[name="isSmartForm"][value="0"]').attr("checked",true);
                    }
                },500);
            } else {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', result.message, 'error');
                },500);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            setTimeout(function(){
                $("#uploadProgress").modal('hide');
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            },500);
        }
    });
}


function batchDelParFrontPartform() {
    if(curIsEditable) {
        var checkList = par_front_partform_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontPartformId);
            }
            var id = ids.join(",")
            delParFrontPartform(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delParFrontPartform(id) {

    swal({
        title: '',
        text: '确定要删除选定的前置检测吗',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgressMsg").html("删除中，请稍后...");
            $("#uploadProgress").modal("show");
            $.ajax({
                url: ctx + '/aea/par/front/partform/deleteAeaParFrontPartformById.do',
                type: 'POST',
                data: {id:id},
                async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            par_front_partform_tb.clear();
                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}