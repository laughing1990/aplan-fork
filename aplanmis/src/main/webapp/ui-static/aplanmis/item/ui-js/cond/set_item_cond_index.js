var add_item_cond_validator = null,commonQueryParams = [];
$(function(){

    // 设置弹窗大小
    $('#select_meta_db_tbcol_ztree_modal .modal-lg').css('max-width',$('#westPanel').width()*3*0.70);

    // 组织机构事项列表
    $('#item_cond_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-126
    });

    // 双击事件
    $('#item_cond_tb').on('dbl-click-row.bs.table',  function (e, row, element) {

        editItemCond(row.itemCondId);
    });


    // 加载是否激活条件
    loadItemInfo(needCondType,needCondNum);

    $("#useEl").change(function(){

        if($(this).val()=='1'){
            $('#condElDiv').show();
            $('#condEl').rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });
        }else{
            $('#condElDiv').hide();
            $('#condEl').rules("remove");
        }
    });

    $('#isAccept').change(function(){

        if($(this).val()=='1'){
            $('#noAcceptDemoDiv').hide();
            $('#noAcceptDemo').rules("remove");
        }else{
            $('#noAcceptDemoDiv').show();
            $('#noAcceptDemo').rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });
        }
    });

    // 设置初始化校验
    add_item_cond_validator = $('#add_item_cond_form').validate({
        // 定义校验规则
        rules: {
            condName: {
                required: true,
                maxlength: 500
            },
            useEl:{
                required: true,
            },
            isAccept:{
                required: true,
            },
            // condEl:{
            //     required: true,
            //     maxlength: 500
            // },
            sortNo:{
                required: true,
                maxlength: 38
            },
            condMemo:{
                maxlength: 500
            }
        },
        messages: {
            condName: {
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过500个字母!"
            },
            useEl:{
                required: '<font color="red">此项必填!</font>',
            },
            isAccept:{
                required: '<font color="red">此项必填!</font>',
            },
            // condEl:{
            //     required: '<font color="red">此项必填!</font>',
            //     maxlength: "长度不能超过500个字母!"
            // },
            sortNo:{
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过38个字母!"
            },
            condMemo:{
                maxlength: "长度不能超过500个字母!"
            }
        },
        // 提交表单
        submitHandler: function(form){

            var d = {};
            var t = $('#add_item_cond_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/aea/item/cond/saveAeaItemCond.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success){
                        $('#add_item_cond_modal').modal('hide');
                        swal({
                            title: '提示信息',
                            text: '保存成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        searchItemCond();
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "保存激活条件信息失败！", 'error');
                }
            });
        }
    });

    $('#closeAddCategoryBtn').click(function(){
        $('#add_item_cond_modal').modal('hide');
        add_item_cond_validator.resetForm();
    });
});

function selectIsNeedState(needCondType){

    loadItemInfo(needCondType,0);
}

function loadItemInfo(needCondType,needCondNum){

    if(needCondType=='N'){

       $('#mainContentPanel').hide();
       $('#needCondNumLabel').hide();
        $('#needCondNum').val('');
        $('#needCondTypeN').prop('checked',true);
        $('#needCondTypeS').prop('checked',false);
        $('#needCondTypeM').prop('checked',false);

    }else if(needCondType=='S'){

        $('#mainContentPanel').show();
        $('#needCondNumLabel').hide();
        $('#needCondNum').val('');
        $('#needCondTypeN').prop('checked',false);
        $('#needCondTypeS').prop('checked',true);
        $('#needCondTypeM').prop('checked',false);

    }else if(needCondType=='M'){

        $('#mainContentPanel').show();
        $('#needCondNumLabel').show();
        $('#needCondNum').val(needCondNum);
        $('#needCondTypeN').prop('checked',false);
        $('#needCondTypeS').prop('checked',false);
        $('#needCondTypeM').prop('checked',true);

    }else{

        $('#mainContentPanel').hide();
        $('#needCondNumLabel').hide();
        $('#needCondNum').val('');
        $('#needCondTypeN').prop('checked',true);
        $('#needCondTypeS').prop('checked',false);
        $('#needCondTypeM').prop('checked',false);
    }
}

function saveItemNeedCondInfo(){

    if(itemId) {
        var needCondType = $('input[name="needCondType"]:checked').val();
        var needCondNum = null;
        if(needCondType=='M'){
            needCondNum = $('#needCondNum').val();
            if(!needCondNum){
                swal('提示信息','请填写至少满足条件数量','info');
              return false;
            }
        }
        $.ajax({
            url: ctx + '/aea/item/updateItemNeedCondType.do',
            type: 'POST',
            data: {'itemId': itemId,'needCondType': needCondType,'needCondNum': needCondNum},
            async: false,
            success: function (result) {
                if(result.success){
                    swal({
                        title: '提示信息',
                        text: '保存成功!',
                        type: 'success',
                        timer: 1000,
                        showConfirmButton: false
                    });
                }else{
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        swal('提示信息', '事项id为空！', 'info');
    }
}

// 新增前置条件
function addItemCond(){

    if(itemId){

        $('#add_item_cond_modal').modal('show');
        $('#add_item_cond_modal_title').html('新增前置条件');
        $('#add_item_cond_form')[0].reset();
        add_item_cond_validator.resetForm();
        $('#cond_itemCondId').val('');
        $('#cond_itemId').val('');

        $("#add_item_cond_form input[name='itemCondId']").val('');
        $("#add_item_cond_form input[name='itemId']").val(itemId);
        $("#add_item_cond_form input[name='isActive']").val("1");
        $('#useEl option:eq(1)').prop("selected", 'selected');
        $('#isAccept option:eq(1)').prop("selected", 'selected');
        $('#condElDiv').hide();
        $('#noAcceptDemoDiv').hide();

        $.ajax({
            url: ctx + '/aea/item/cond/getMaxSortNo.do',
            type: 'POST',
            data: {},
            async: false,
            success: function (data) {
                $("#add_item_cond_form input[name='sortNo']").val(data);
            },
            error:function(){
                $("#add_item_cond_form input[name='sortNo']").val(1);
            }
        });
    }else{
        swal('提示信息', '事项id为空！', 'info');
    }
}

// 编辑前置条件
function editItemCond(itemCondId){

    $('#add_item_cond_modal').modal('show');
    $('#add_item_cond_modal_title').html('编辑前置条件');
    $('#add_item_cond_form')[0].reset();
    add_item_cond_validator.resetForm();
    $('#cond_itemCondId').val('');
    $('#cond_itemId').val('');

    $('#condElDiv').hide();
    $('#noAcceptDemoDiv').hide();
    $.ajax({
        url: ctx+'/aea/item/cond/getAeaItemCond.do',
        type: 'POST',
        data: {'id': itemCondId},
        success: function (data) {
            if(data){
                loadFormData(true,'#add_item_cond_form',data);

                if(data.useEl=='1'){
                    $('#condElDiv').show();
                    $('#condEl').rules("add", {
                        required: true,
                        messages: {
                            required: '<font color="red">此项必填！</font>',
                        }
                    });
                }else{
                    $('#condElDiv').hide();
                    $('#condEl').rules("remove");
                }


                if(data.isAccept=='1'){
                    $('#noAcceptDemoDiv').hide();
                    $('#noAcceptDemo').rules("remove");
                }else{
                    $('#noAcceptDemoDiv').show();
                    $('#noAcceptDemo').rules("add", {
                        required: true,
                        messages: {
                            required: '<font color="red">此项必填！</font>',
                        }
                    });
                }
            }
        },
        error:function(){
            swal('错误信息', '获取前置条件信息出错！', 'error');
        }
    });
}

// 删除分类
function deleteItemCond(itemCondId){

    var msg = '你确定要删除吗？';
    swal({
        title: '提示信息',
        text: '将删除前置条件,可能影响事项处理，您确定删除吗?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx+'/aea/item/cond/deleteAeaItemCondById.do',
                type: 'POST',
                data: {'id': itemCondId},
                success: function (result) {
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '删除成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        searchItemCond();
                    }else{
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        }
    });
}

//事项前置条件列表相关操作
function itemCondListOperatorFormatter(value, row, index, field) {

    var editBtn =   '<a href="javascript:editItemCond(\'' + row.itemCondId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    var deleteBtn = '<a href="javascript:deleteItemCond(\''+row.itemCondId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
    return editBtn + deleteBtn;
}

function isAcceptFormatter(value, row, index, field) {

    if(value=='1'){
       return '受理';
    }else{
        return '不受理';
    }
}

//查询
function searchItemCond() {

    var params = $('#search_item_cond_form').serializeArray();
    commonQueryParams = [];
    commonQueryParams.push({name: "itemId", value: itemId});
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#m_tabs_item_cond").show();
    $("#item_cond_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#item_cond_tb").bootstrapTable('refresh');       //无参数刷新
}
//事项前置条件列表后台返回的数据
function itemCondListResponseData(res) {
    return res;
}

function isItemCondActiveStr(value, row, index, field) {

    if(row.isActive=='1'){
        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" onclick="changeItemCondIsActive(this,\''+ row.itemCondId +'\',\''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }else{
        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" onclick="changeItemCondIsActive(this,\''+ row.itemCondId +'\',\''+ row.isActive +'\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

function viewIsItemCondActiveStr(value, row, index, field) {

    if(row.isActive=='1'){
        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" disabled/>' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }else{
        return  '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" disabled/>' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}


function changeItemCondIsActive(obj,id,isActive){

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
                url: ctx+'/aea/item/cond/changIsActiveState.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if(result.success){
                        searchItemCond();
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
}

// 刷新
function refreshItemCond(){
    searchItemCond();
}

// 清空查询
function clearSearchItemCond(){

    $('#search_item_cond_form')[0].reset();
    searchItemCond();
}

// 批量删除
function batchDeleteItemCond(){

    var checkBoxs = $("#item_cond_tb").bootstrapTable('getSelections');
    var itemCondIds = [];
    if(checkBoxs!=null&&checkBoxs.length>0){
        for(var i=0;i<checkBoxs.length;i++){
            itemCondIds.push(checkBoxs[i].itemCondId);
        }
        swal({
            title: '提示信息',
            text: '将批量删除前置条件,可能影响事项处理，您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/item/cond/batchDeleteAeaItemCond.do',
                    type: 'POST',
                    data:{'ids': itemCondIds.toString()},
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
                            searchItemCond();
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
}