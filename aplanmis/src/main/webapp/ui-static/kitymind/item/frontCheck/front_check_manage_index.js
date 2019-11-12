var item_front_proj_tb;
var edit_item_front_proj_form_validator;
var item_front_item_tb;
var edit_item_front_item_form_validator;
var item_front_partform_tb;
var edit_item_front_partform_form_validator;

$(function(){
    if(isCheckProj=='1'){
        item_front_proj_tb = defaultBootstrap("item_front_proj_tb",[{
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
                var btn =  '<a href="javascript:editItemFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delItemFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/item/front/proj/listAeaItemFrontProjByPage.do');
        var item_front_proj_tb_params = {itemVerId:currentBusiId};
        item_front_proj_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(item_front_proj_tb_params).setRowId("frontProjId").setClearBtn("item_front_proj_clear_btn").setSearchBtn("item_front_proj_search_btn","item_front_proj_keyword").init();

        // 设置初始化校验
        edit_item_front_proj_form_validator = $("#edit_item_front_proj_form").validate({

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
                var t = $('#edit_item_front_proj_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/item/front/proj/saveOrUpdateAeaItemFrontProj.do',
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
                                $('#edit_item_front_proj_modal').modal('hide');
                                item_front_proj_tb.clear();

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
        item_front_item_tb = defaultBootstrap("item_front_item_tb",[{
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
            field: "frontCkItemName",
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
                var btn =  '<a href="javascript:editItemFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delItemFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/item/front/item/listAeaItemFrontItemByPage.do');
        var item_front_item_tb_params = {itemVerId:currentBusiId};
        item_front_item_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(item_front_item_tb_params).setRowId("frontItemId").setClearBtn("item_front_item_clear_btn").setSearchBtn("item_front_item_search_btn","item_front_item_keyword").init();

        // 设置初始化校验
        edit_item_front_item_form_validator = $("#edit_item_front_item_form").validate({

            // 定义校验规则
            rules: {
                frontCkItemName: {
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
                frontCkItemName: {
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
                var t = $('#edit_item_front_item_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/item/front/item/saveOrUpdateAeaItemFrontItem.do',
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
                                $('#edit_item_front_item_modal').modal('hide');
                                item_front_item_tb.clear();

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
        item_front_partform_tb = defaultBootstrap("item_front_partform_tb",[{
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
            title: "事项扩展表单名称",
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
                var btn =  '<a href="javascript:editItemFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delItemFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/item/front/partform/listAeaItemFrontPartformByPage.do');
        var item_front_partform_tb_params = {itemVerId:currentBusiId};
        item_front_partform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(item_front_partform_tb_params).setRowId("frontPartformId").setClearBtn("item_front_partform_clear_btn").setSearchBtn("item_front_partform_search_btn","item_front_partform_keyword").init();

        // 设置初始化校验
        edit_item_front_partform_form_validator = $("#edit_item_front_partform_form").validate({

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
                var t = $('#edit_item_front_partform_form').serializeArray();
                $.each(t, function () {
                    d[this.name] = this.value;
                });
                $("#uploadProgressMsg").html("数据保存中，请稍后...");
                $("#uploadProgress").modal("show");
                $.ajax({
                    url: ctx + '/aea/item/front/partform/saveOrUpdateAeaItemFrontPartform.do',
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
                                $('#edit_item_front_partform_modal').modal('hide');
                                item_front_partform_tb.clear();

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

function addItemFrontProj() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/item/front/proj/getMaxSortNo.do',
            type: 'POST',
            data: {itemVerId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveItemFrontProjBtn').show();
                        $("#edit_item_front_proj_modal").modal("show");
                        $('#edit_item_front_proj_title').html('新增项目信息前置检测');
                        $('#edit_item_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_item_front_proj_form')[0].reset();
                        $('#edit_item_front_proj_form input[name="frontProjId"]').val('');
                        $('#edit_item_front_proj_form input[name="itemVerId"]').val(currentBusiId);
                        $('#edit_item_front_proj_form input[name="sortNo"]').val(result.content);

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

function delItemFrontProj(id) {

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
                url: ctx + '/aea/item/front/proj/deleteAeaItemFrontProjById.do',
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
                            item_front_proj_tb.clear();
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

function batchDelItemFrontProj() {
    if(curIsEditable) {
        var checkList = item_front_proj_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontProjId);
            }
            var id = ids.join(",")
            delItemFrontProj(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editItemFrontProj(frontProjId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/item/front/proj/getAeaItemFrontProj.do',
        type: 'POST',
        data: {id:frontProjId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveItemFrontProjBtn').show();
                    }else{
                        $('#saveItemFrontProjBtn').hide();
                    }
                    $("#edit_item_front_proj_modal").modal("show");
                    $('#edit_item_front_proj_title').html('编辑项目信息前置检测');
                    $('#edit_item_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_item_front_proj_form')[0].reset();
                    loadFormData(true, '#edit_item_front_proj_form', result.content);
                    $('#edit_item_front_proj_form input[name="frontProjId"]').val(result.content.frontProjId);
                    $('#edit_item_front_proj_form input[name="itemVerId"]').val(currentBusiId);
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


function addItemFrontItem() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/item/front/item/getMaxSortNo.do',
            type: 'POST',
            data: {itemVerId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveItemFrontItemBtn').show();
                        $("#edit_item_front_item_modal").modal("show");
                        $('#edit_item_front_item_title').html('新增事项信息前置检测');
                        $('#edit_item_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_item_front_item_form')[0].reset();
                        $('#edit_item_front_item_form input[name="frontItemId"]').val('');
                        $('#edit_item_front_item_form input[name="frontCkItemVerId"]').val('');
                        $('#edit_item_front_item_form input[name="itemVerId"]').val(currentBusiId);
                        $('#edit_item_front_item_form input[name="sortNo"]').val(result.content);

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

function editItemFrontItem(frontItemId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/item/front/item/getAeaItemFrontItem.do',
        type: 'POST',
        data: {frontItemId:frontItemId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveItemFrontItemBtn').show();
                    }else{
                        $('#saveItemFrontItemBtn').hide();
                    }
                    $("#edit_item_front_item_modal").modal("show");
                    $('#edit_item_front_item_title').html('编辑事项信息前置检测');
                    $('#edit_item_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_item_front_item_form')[0].reset();
                    loadFormData(true, '#edit_item_front_item_form', result.content);
                    $('#edit_item_front_item_form input[name="frontItemId"]').val(result.content.frontItemId);
                    $('#edit_item_front_item_form input[name="itemVerId"]').val(currentBusiId);
                    $('#edit_item_front_item_form input[name="frontCkItemVerId"]').val(result.content.frontCkItemVerId);
                    $('#edit_item_front_item_form input[name="frontCkItemName"]').val(result.content.frontCkItemName);
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


function batchDelItemFrontItem() {
    if(curIsEditable) {
        var checkList = item_front_item_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontItemId);
            }
            var id = ids.join(",")
            delItemFrontItem(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delItemFrontItem(id) {

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
                url: ctx + '/aea/item/front/item/deleteAeaItemFrontItemById.do',
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
                            item_front_item_tb.clear();
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



function addItemFrontPartform() {
    if(curIsEditable) {
        $("#uploadProgressMsg").html("数据加载中，请稍后...");
        $("#uploadProgress").modal("show");
        $.ajax({
            url: ctx + '/aea/item/front/partform/getMaxSortNo.do',
            type: 'POST',
            data: {itemVerId:currentBusiId},
            async: false,
            success: function (result) {
                if (result.success) {
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        $('#saveItemFrontPartformBtn').show();
                        $("#edit_item_front_partform_modal").modal("show");
                        $('#edit_item_front_partform_title').html('新增事项扩展表单前置检测');
                        $('#edit_item_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                        $('#edit_item_front_partform_form')[0].reset();
                        $('#edit_item_front_partform_form input[name="frontPartformId"]').val('');
                        $('#edit_item_front_partform_form input[name="itemPartformId"]').val('');
                        $('#edit_item_front_partform_form input[name="itemVerId"]').val(currentBusiId);
                        $('#edit_item_front_partform_form input[name="sortNo"]').val(result.content);
                        $('#edit_item_front_partform_form input[name="isSmartForm"]').attr("checked",false);
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

function editItemFrontPartform(frontPartformId) {
    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");
    $.ajax({
        url: ctx + '/aea/item/front/partform/getAeaItemFrontPartform.do',
        type: 'POST',
        data: {frontPartformId:frontPartformId},
        async: false,
        success: function (result) {
            if (result.success) {
                setTimeout(function(){
                    $("#uploadProgress").modal('hide');
                    if(curIsEditable){
                        $('#saveItemFrontPartformBtn').show();
                    }else{
                        $('#saveItemFrontPartformBtn').hide();
                    }
                    $("#edit_item_front_partform_modal").modal("show");
                    $('#edit_item_front_partform_title').html('编辑事项扩展表单前置检测');
                    $('#edit_item_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    $('#edit_item_front_partform_form')[0].reset();
                    loadFormData(true, '#edit_item_front_partform_form', result.content);
                    $('#edit_item_front_partform_form input[name="frontPartformId"]').val(result.content.frontPartformId);
                    $('#edit_item_front_partform_form input[name="itemVerId"]').val(currentBusiId);
                    $('#edit_item_front_partform_form input[name="itemPartformId"]').val(result.content.itemPartformId);
                    $('#edit_item_front_partform_form input[name="partformName"]').val(result.content.partformName);

                    if("1"==result.content.isSmartForm){
                        $('#edit_item_front_partform_form input[name="isSmartForm"][value="1"]').attr("checked",true);
                    }else{
                        $('#edit_item_front_partform_form input[name="isSmartForm"][value="0"]').attr("checked",true);
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


function batchDelItemFrontPartform() {
    if(curIsEditable) {
        var checkList = item_front_partform_tb.getSelections();
        if(checkList.length==0){
            swal('提示信息', '请勾选要删除的前置检测', 'info');
        }else{
            var ids = new Array();
            for(var i=0;i<checkList.length;i++){
                ids.push(checkList[i].frontPartformId);
            }
            var id = ids.join(",")
            delItemFrontPartform(id);
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function delItemFrontPartform(id) {

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
                url: ctx + '/aea/item/front/partform/deleteAeaItemFrontPartformById.do',
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
                            item_front_partform_tb.clear();
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