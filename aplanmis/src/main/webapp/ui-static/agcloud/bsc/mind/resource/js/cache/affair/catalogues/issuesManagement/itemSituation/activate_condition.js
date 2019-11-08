var add_item_cond_validator = null;
$(function(){

    // 设置初始化校验
    add_item_cond_validator = $('#add_item_cond_form').validate({
        // 定义校验规则
        rules: {
            condName: {
                required: true,
                maxlength: 500
            },
            condEl:{
                required: true,
                maxlength: 500
            },
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
            condEl:{
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过500个字母!"
            },
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

// 新增前置条件
function addItemCond(){

    if(currentBusiId){

        $('#add_item_cond_modal').modal('show');
        $('#add_item_cond_modal_title').html('新增激活条件');
        $('#add_item_cond_form')[0].reset();
        $('#cond_itemCondId').val('');
        $('#cond_itemId').val('');
        add_item_cond_validator.resetForm();
        $("#add_item_cond_form input[name='itemId']").val(currentBusiId);

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
        swal('提示信息', '请选择事项节点！', 'info');
    }
}

// 编辑前置条件
function editItemCond(itemCondId){

    $('#add_item_cond_modal').modal('show');
    $('#add_item_cond_modal_title').html('编辑激活条件');
    $.ajax({
        url: ctx+'/aea/item/cond/getAeaItemCond.do',
        type: 'POST',
        data: {'id': itemCondId},
        success: function (data) {
            if(data){
                loadFormData(true,'#add_item_cond_form',data);
            }
        },
        error:function(){
            swal('错误信息', '获取分激活条件息出错！', 'error');
        }
    });
}

// 删除分类
function deleteItemCond(itemCondId){

    var msg = '你确定要删除吗？';
    swal({
        title: '提示信息',
        text: '将删除激活条件,可能影响事项处理，您确定删除吗?',
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

//事项激活条件列表相关操作
function itemCondListOperatorFormatter(value, row, index, field) {
    var editBtn =   '<a href="javascript:editItemCond(\'' + row.itemCondId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    var deleteBtn = '<a href="javascript:deleteItemCond(\''+row.itemCondId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
    return editBtn + deleteBtn;
}

//查询
function searchItemCond() {

    var params = $('#search_item_cond_form').serializeArray();
    commonQueryParams = [{name: "itemId", value: currentBusiId}];
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    };
    $("#activateCondition").show();
    $("#stateAndMaterialSetting").hide();
    $("#inputMat").hide();
    $("#outputMat").hide();
    $("#item_cond_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#item_cond_tb").bootstrapTable('refresh');       //无参数刷新
}
//事项激活条件列表后台返回的数据
function itemCondListResponseData(res) {
    return res;
}

function isItemCondActiveStr(row){

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
            text: '将批量删除激活条件,可能影响事项处理，您确定删除吗?',
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