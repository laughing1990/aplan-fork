var item_front_proj_tb;
var edit_item_front_proj_form_validator;
var item_front_item_tb;
var edit_item_front_item_form_validator;
var item_front_partform_tb;
var edit_item_front_partform_form_validator;

function projIsActiveFormatter(value, row, index, field) {

    return handleIsActiveHtml(row.isActive, row.frontProjId, 'proj');
}

function itemIsActiveFormatter(value, row, index, field) {

    return handleIsActiveHtml(row.isActive, row.frontItemId, 'item');
}

function partformIsActiveFormatter(value, row, index, field) {

    return handleIsActiveHtml(row.isActive, row.frontPartformId, 'partform');
}

function handleIsActiveHtml(isActive, id, type){

    if(isActive=='1'){

        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" checked="checked" name="isActive" onclick="changeIsActive(this, \''+ id +'\', \''+ isActive +'\', \''+ type +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }else{

        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" name="isActive" onclick="changeIsActive(this, \''+ id +'\', \''+ isActive +'\', \''+ type +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }
}

function changeIsActive(obj, id, isActive, type){

    if(id){
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
                var url;
                if(type=='proj'){
                    url = ctx + '/aea/item/front/proj/changIsActive.do';
                }else if(type=='item'){
                    url = ctx + '/aea/item/front/item/changIsActive.do';
                }else{
                    url = ctx + '/aea/item/front/partform/changIsActive.do';
                }
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: {"id": id},
                    success: function (result) {
                        if(result.success){
                            if(type=='proj'){
                                if(item_front_proj_tb){
                                    item_front_proj_tb.clear();
                                }
                            }else if(type=='item'){
                               if(item_front_item_tb){
                                   item_front_item_tb.clear();
                               }
                            }else{
                                if(item_front_partform_tb){
                                    item_front_partform_tb.clear();
                                }
                            }
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

$(function(){

    if(isCheckProj=='1'){
        item_front_proj_tb = defaultBootstrap("item_front_proj_tb",[
        {
            checkbox: true,
        },
        {
            field: "ruleName",
            title: "规则名称",
            width: 300,
            align: 'left',
        },
        {
            field: "ruleEl",
            title: "规则表达式",
            width: 300,
            align: 'left',
        },
        {
            field: "frontProjMemo",
            title: "备注",
            width: 200,
            align: 'left',
            formatter: function (value, row, index) {
                if(value){
                    if(value.length>200){
                        return value.substr(0, 200) + "...";
                    }else{
                        return value;
                    }
                }
            }
        },
        {
            field: "isActive",
            title: "是否启用",
            align: 'center',
            width: 60,
            formatter: projIsActiveFormatter
        },
        {
            field: 'sortNo',
            title: '排序',
            align: 'center',
            sortable: true,
            width: 60
        },
        {
            field: '_operate',
            title: '操作',
            width: 100,
            align: 'center',
            formatter: function (value, row, index) {

                var btn =  '<a href="javascript:editItemFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delItemFrontProj(\'' + row.frontProjId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/item/front/proj/listAeaItemFrontProjByPage.do');
        var item_front_proj_tb_params = {'itemVerId': currentBusiId};
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
        item_front_item_tb = defaultBootstrap("item_front_item_tb",[
        {
            checkbox: true,
        },
        {
            field: "frontCkIsCatalog",
            title: "是否实施事项",
            width: 80,
            align: 'center',
            formatter: function (value, row, index) {
                if(row.frontCkIsCatalog=='1'){
                    return '标准事项';
                }else{
                    return '实施事项';
                }
            }
        },
        {
            field: "frontCkItemName",
            title: "前置检测事项",
            width: 400,
            align: 'left',
            formatter: function (value, row, index) {

                if(row.frontCkItemCode){
                    return row.frontCkItemName+"【"+ row.frontCkItemCode +"】";
                }else{
                    return row.frontCkItemName;
                }
            }
        },
        {
            field: "frontItemMemo",
            title: "备注",
            width: 200,
            align: 'left',
            formatter: function (value, row, index) {
                if(value){
                    if(value.length>200){
                        return value.substr(0, 200) + "...";
                    }else{
                        return value;
                    }
                }
            }
        },
        {
            field: "isActive",
            title: "是否启用",
            align: 'center',
            width: 60,
            formatter: itemIsActiveFormatter
        },
        {
            field: "sortNo",
            title: "排序",
            align: 'center',
            width: 60,
            sortable: true
        },
        {
            field: '_operate',
            title: '操作',
            width: 120,
            align: 'center',
            formatter: function (value, row, index) {

                var btn =  '<a href="javascript:editItemFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                if(curIsEditable){
                    btn = btn + '<a href="javascript:delItemFrontItem(\'' + row.frontItemId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                }
                return btn;
            }
        }],ctx + '/aea/item/front/item/listAeaItemFrontItemByPage.do');
        var item_front_item_tb_params = {'itemVerId': currentBusiId};
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

        item_front_partform_tb = defaultBootstrap("item_front_partform_tb",[
            {
                checkbox: true,
            },
            {
                field: "partformName",
                title: "扩展表单名称",
                width: 350,
                textAlign: 'left',
            },
            {
                field: "isSmartForm",
                title: "是否智能表单",
                textAlign: 'center',
                width: 80,
                formatter: function (value, row, index) {
                    if(row.isSmartForm=='1'){
                        return "智能表单";
                    }else{
                        return "开发表单";
                    }
                }
            },
            {
                field: "sortNo",
                title: "排序",
                align: 'center',
                width: 60,
                sortable: true
            },
            {
                field: "isActive",
                title: "是否启用",
                align: 'center',
                width: 60,
                formatter: partformIsActiveFormatter
            },
            {
                field: '_operate',
                title: '操作',
                width: 120,
                align: 'center',
                formatter: function (value, row, index) {

                    var btn =  '<a href="javascript:editItemFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                    if(curIsEditable){
                        btn = btn + '<a href="javascript:delItemFrontPartform(\'' + row.frontPartformId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>'
                    }
                    return btn;
                }
            }
        ],ctx + '/aea/item/front/partform/listAeaItemFrontPartformByPage.do');
        var item_front_partform_tb_params = {
            'itemVerId': currentBusiId
        };
        item_front_partform_tb.setPageSize(5)
                              .setPageList([5,10,20,50,100])
                              .setQueryParams(item_front_partform_tb_params)
                              .setRowId("frontPartformId")
                              .setClearBtn("item_front_partform_clear_btn")
                              .setSearchBtn("item_front_partform_search_btn", "item_front_partform_keyword").init();

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

        $("#edit_item_front_proj_modal").modal("show");
        $('#edit_item_front_proj_title').html('新增项目信息前置检测');
        $('#edit_item_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#edit_item_front_proj_form')[0].reset();
        if(edit_item_front_proj_form_validator){
            edit_item_front_proj_form_validator.resetForm();
        }
        $('#edit_item_front_proj_form input[name="frontProjId"]').val('');
        $('#edit_item_front_proj_form input[name="isActive"]').val('1');
        $('#edit_item_front_proj_form input[name="itemVerId"]').val(currentBusiId);

        $.ajax({
            url: ctx + '/aea/item/front/proj/getMaxSortNo.do',
            type: 'POST',
            data: {'itemVerId': currentBusiId},
            success: function (result) {
                if (result.success) {
                    $('#edit_item_front_proj_form input[name="sortNo"]').val(result.content);
                } else {
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', XMLHttpRequest.responseText, 'error');
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
                data: {'id': id},
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
            var ids = [];
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

    if(curIsEditable){
        $('#saveItemFrontProjBtn').show();
    }else{
        $('#saveItemFrontProjBtn').hide();
    }
    $("#edit_item_front_proj_modal").modal("show");
    $('#edit_item_front_proj_title').html('编辑项目信息前置检测');
    $('#edit_item_front_proj_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#edit_item_front_proj_form')[0].reset();
    if(edit_item_front_proj_form_validator){
        edit_item_front_proj_form_validator.resetForm();
    }
    $('#edit_item_front_proj_form input[name="frontProjId"]').val('');
    $('#edit_item_front_proj_form input[name="itemVerId"]').val(currentBusiId);

    $.ajax({
        url: ctx + '/aea/item/front/proj/getAeaItemFrontProj.do',
        type: 'POST',
        data: {'id': frontProjId},
        async: false,
        success: function (result) {
            if (result.success) {
                loadFormData(true, '#edit_item_front_proj_form', result.content);
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}


function addItemFrontItem() {

    if(curIsEditable) {

        $("#edit_item_front_item_modal").modal("show");
        $('#edit_item_front_item_title').html('新增事项信息前置检测');
        $('#edit_item_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#edit_item_front_item_form')[0].reset();
        if(edit_item_front_item_form_validator){
            edit_item_front_item_form_validator.resetForm();
        }
        $('#edit_item_front_item_form input[name="frontItemId"]').val('');
        $('#edit_item_front_item_form input[name="frontCkItemVerId"]').val('');
        $('#edit_item_front_item_form input[name="itemVerId"]').val(currentBusiId);
        $('#edit_item_front_item_form input[name="isActive"]').val('1');

        $.ajax({
            url: ctx + '/aea/item/front/item/getMaxSortNo.do',
            type: 'POST',
            data: {
                'itemVerId': currentBusiId
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    $('#edit_item_front_item_form input[name="sortNo"]').val(result.content);
                } else {
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editItemFrontItem(frontItemId) {

    if(curIsEditable){
        $('#saveItemFrontItemBtn').show();
    }else{
        $('#saveItemFrontItemBtn').hide();
    }

    $("#uploadProgressMsg").html("数据加载中，请稍后...");
    $("#uploadProgress").modal("show");

    $("#edit_item_front_item_modal").modal("show");
    $('#edit_item_front_item_title').html('编辑事项信息前置检测');
    $('#edit_item_front_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#edit_item_front_item_form')[0].reset();
    if(edit_item_front_item_form_validator){
        edit_item_front_item_form_validator.resetForm();
    }
    $('#edit_item_front_item_form input[name="frontItemId"]').val('');
    $('#edit_item_front_item_form input[name="frontCkItemVerId"]').val('');
    $('#edit_item_front_item_form input[name="itemVerId"]').val(currentBusiId);

    $.ajax({
        url: ctx + '/aea/item/front/item/getAeaItemFrontItem.do',
        type: 'POST',
        data: {'frontItemId': frontItemId},
        async: false,
        success: function (result) {
            if (result.success&&result.content) {

                setTimeout(function(){

                    $("#uploadProgress").modal('hide');
                    $('#saveItemFrontProjBtn').show();

                    loadFormData(true, '#edit_item_front_item_form', result.content);

                    if(result.content.frontCkItemCode!=null&&result.content.frontCkItemCode!=''&&result.content.frontCkItemCode!=undefined){
                        $('#edit_item_front_item_form textarea[name="frontCkItemName"]').val(result.content.frontCkItemName +"【"+result.content.frontCkItemCode+"】");
                    }else{
                        $('#edit_item_front_item_form textarea[name="frontCkItemName"]').val(result.content.frontCkItemName);
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

        $("#edit_item_front_partform_modal").modal("show");
        $('#edit_item_front_partform_title').html('新增事项扩展表单前置检测');
        $('#edit_item_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#edit_item_front_partform_form')[0].reset();
        if(edit_item_front_partform_form_validator){
            edit_item_front_partform_form_validator.resetForm();
        }
        $('#edit_item_front_partform_form input[name="frontPartformId"]').val('');
        $('#edit_item_front_partform_form input[name="itemPartformId"]').val('');
        $('#edit_item_front_partform_form input[name="itemVerId"]').val(currentBusiId);
        $('#edit_item_front_partform_form input[name="isSmartForm"]').attr("checked",false);

        $.ajax({
            url: ctx + '/aea/item/front/partform/getMaxSortNo.do',
            type: 'POST',
            data: {
                'itemVerId': currentBusiId
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    $('#edit_item_front_partform_form input[name="sortNo"]').val(result.content);
                } else {
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

function editItemFrontPartform(frontPartformId) {

    if(curIsEditable){
        $('#saveItemFrontPartformBtn').show();
    }else{
        $('#saveItemFrontPartformBtn').hide();
    }
    $("#edit_item_front_partform_modal").modal("show");
    $('#edit_item_front_partform_title').html('编辑事项扩展表单前置检测');
    $('#edit_item_front_partform_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#edit_item_front_partform_form')[0].reset();
    if(edit_item_front_partform_form_validator){
        edit_item_front_partform_form_validator.resetForm();
    }
    $('#edit_item_front_partform_form input[name="frontPartformId"]').val('');
    $('#edit_item_front_partform_form input[name="itemPartformId"]').val('');
    $('#edit_item_front_partform_form input[name="itemVerId"]').val(currentBusiId);

    $.ajax({
        url: ctx + '/aea/item/front/partform/getAeaItemFrontPartform.do',
        type: 'POST',
        data: {
            'frontPartformId': frontPartformId
        },
        async: false,
        success: function (result) {
            if (result.success) {

                loadFormData(true, '#edit_item_front_partform_form', result.content);
                if("1"==result.content.isSmartForm){
                    $('#edit_item_front_partform_form input[name="isSmartForm"][value="1"]').attr("checked",true);
                }else{
                    $('#edit_item_front_partform_form input[name="isSmartForm"][value="0"]').attr("checked",true);
                }
            } else {

                swal('错误信息', result.message, 'error');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', XMLHttpRequest.responseText, 'error');
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

function importFrontItem(){

    initFrontCheckItem();
}