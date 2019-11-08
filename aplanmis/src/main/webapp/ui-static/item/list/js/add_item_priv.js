var selectType;
$(function() {

    $("#add_item_priv_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#privOpusOmOrgDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });



    // 选择组织
    $('#selectPrivOrgBtn').click(function(){

        var opusOmOrgTree = $.fn.zTree.getZTreeObj("privOpusOmOrgTree");
        if(itemBasicFieldId&&itemBasicFieldName){
            var nodes = opusOmOrgTree.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){
                var ids = [];
                var names = [];
                for(var i=0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }
                $("#add_item_priv_form input[name='"+ itemBasicFieldId +"']").val(ids.toString());
                $("#add_item_priv_form textarea[name='"+ itemBasicFieldName +"']").val(names.toString());
                closeSelectPrivOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        }else{
            var isSelectOrgSearch = $('#isSelectOrgSearch').val();
            var nodes = opusOmOrgTree.getSelectedNodes();
            if(nodes!=null&&nodes.length>0){
                var selectNode = nodes[0];
                if(isSelectOrgSearch=='search'){
                    $("#search_all_item_list_form input[name='orgId']").val(selectNode.id);
                    $("#search_all_item_list_form input[name='orgName']").val(selectNode.name);
                }else{
                    $("#orgId").val(selectNode.id);
                    $("#add_item_priv_form input[name='orgName']").val(selectNode.name);
                }
                closeSelectPrivOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        }
    });

    $('#selectRegionBtn').click(function(){

        var regionTree = $.fn.zTree.getZTreeObj("bscDicRegionTree");
        if(itemBasicFieldId&&itemBasicFieldName){
            var nodes = regionTree.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){
                var ids = [];
                var names = [];
                for(var i=0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }
                $("#add_item_priv_form input[name='"+ itemBasicFieldId +"']").val(ids.toString());
                $("#add_item_priv_form textarea[name='"+ itemBasicFieldName +"']").val(names.toString());
                closeSelectBscDicRegionZtree();
            }else{
                swal('提示信息', '请选择行政区域！', 'info');
            }
        }else{
            var isSelectRegionSearch = $('#isSelectRegionSearch').val();
            var nodes = regionTree.getSelectedNodes();
            if(nodes!=null&&nodes.length>0){
                var selectNode = nodes[0];
                if(isSelectRegionSearch=='search'){
                    $("#search_all_item_list_form input[name='regionId']").val(selectNode.id);
                    $("#search_all_item_list_form input[name='regionName']").val(selectNode.name);
                }else{
                    $("#regionId").val(selectNode.id);
                    $("#add_item_priv_form input[name='regionName']").val(selectNode.name);
                }
                closeSelectBscDicRegionZtree();
            }else{
                swal('提示信息', '请选择行政区域！', 'info');
            }
        }
    });

    // 设置初始化校验
    add_item_priv_validator = $("#add_item_priv_form").validate({

        // 定义校验规则
        rules: {
            regionName:{
                required: true,
            },
            orgName: {
                required: true,
            },
        },
        messages: {
            regionName:{
                required: '<font color="red">此项必填！</font>',
            },
            orgName: {
                required: '<font color="red">此项必填！</font>',
            },
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveItemPriv').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var form = new FormData(document.getElementById("add_item_priv_form"));
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/item/priv/saveAeaItemPriv.do',
                data: form,
                processData: false,
                contentType: false,
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
                            $('#saveItemPriv').show();
                            $('#add_item_priv_modal').modal('hide');
                            // 列表数据重新加载
                            searchPrivList();
                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveItemPriv').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveItemPriv').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
});

$(document).ready(function(){

    var fileBtn = $("input[type=file]");
    fileBtn.on("change", function(){
        var index = $(this).val().lastIndexOf("\\");
        var sFileName = $(this).val().substr((index+1));
        $(this).siblings('.custorm-style').find(".right-text").html(sFileName);
    });

    $.ajax({
        url: ctx + '/aea/item/priv/getBscDicRegionList.do',
        type: 'get',
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0) {
                for(var i = 0;i<data.length;i++){

                    var option=document.createElement("option");
                    $(option).val(data[i].regionId);

                    $(option).text(data[i].regionName);

                    $("#regionId").append(option);
                }
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });

    $('#add_item_priv_form select[name="useEl"]').bind("change", function() {

        if(this.value == "1") {
            $("#elText").show();
        } else {

            $("#elText").hide();
        }
    });

    $('#add_mult_item_priv_form select[name="useEl"]').bind("change", function() {

        if(this.value == "1") {
            $("#multElText").show();
        } else {
            $("#multElText").hide();
        }
    });
});

function editItemPrivById(privId,itemVerStatus) {

    $("#add_item_priv_modal").modal("show");
    $("#add_item_priv_modal_title").html("修改事项下放信息");
    $('#add_item_priv_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#add_item_priv_scroll_form')[0].reset();
    aedit_item_validator.resetForm();

    $("#add_item_priv_form input[name='itemVerId']").val('');
    $("#add_item_priv_form input[name='orgId']").val('');
    $("#add_item_priv_form input[name='regionId']").val('');

    $.ajax({
        url: ctx + '/aea/item/priv/getAeaItemPrivById.do',
        type: 'post',
        data: {'id': privId},
        async: false,
        success: function (data) {
            if(data) {

                // 记载表单数据
                loadFormData(true,'#aedit_item_form',data);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

function addItemPriv(itemId, itemVerId) {

    if(itemVerId!=null && itemVerId!=''){

        $("#add_item_priv_modal").modal("show");
        $("#add_item_priv_modal_title").html("事项下放");
        $('#add_item_priv_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#add_item_priv_form')[0].reset();
        if(add_item_priv_validator!=null){
            add_item_priv_validator.resetForm();   
        }
        $('#add_item_priv_form input[name="itemVerId"]').val("");
        $('#add_item_priv_form input[name="itemVerIds"]').val("");
        $('#add_item_priv_form input[name="orgId"]').val("");
        $('#add_item_priv_form input[name="regionId"]').val("");
        $('#elText').hide();
        $("#itemVerId").val(itemVerId);

    }else{

        // var select= $("#all_item_list_tb").bootstrapTable('getSelections');
        // if(select.length>0){
        //     $("#add_item_priv_modal").modal("show");
        //     if(itemVerId!=''){
        //         selectType=0;
        //         $("#add_item_priv_modal_title").html("添加事项下放");
        //     }
        //     $('#add_item_priv_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        //     $('#add_item_priv_form')[0].reset();
        //     add_item_priv_validator.resetForm();
        //     $("#itemVerId").val(select[0].itemVerId);
        // }else{
            alert("请选择需要下放的事项");
        // }
    }
}

function addMultiplyPriv() {

    $("#add_multiply_priv_modal").modal("show");

    // 清空关键字据
    $("#selectItemKeyWord").val('');

    // 取消上次的选中节点
    if(selectMultiplyItemTree!=null){
        selectMultiplyItemTree.checkAllNodes(false);
        selectMultiplyItemTree.cancelSelectedNode();
    }

    $('#add_mutl_item_priv_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#add_mult_item_priv_form')[0].reset();
    if(add_mult_item_priv_validator!=null){
        add_mult_item_priv_validator.resetForm();
    }
    $('#add_mult_item_priv_form input[name="itemVerIds"]').val("");
    $('#add_mult_item_priv_form input[name="multOrgId"]').val("");
    $('#add_mult_item_priv_form input[name="multRegionId"]').val("");
    $('#multElText').hide();
}

function deleteItemById(itemId) {

    if(itemId){
        var msg = '此操作将删除本事项以及包含的子事项，您确定执行吗？';
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
                    url: ctx + '/aea/item/basic/deleteAeaItemBasicById.do',
                    dataType: 'json',
                    data: {'id': itemId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchAllItemList();
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


function isSelectPrivOpuOmOrg(obj, isSearch){

    itemBasicFieldId = null;
    itemBasicFieldName = null;

    if(isSearch){
        $('#isSelectOrgSearch').val('search');
    }else{
        $('#isSelectOrgSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectPrivOpusOmOrgZtree();
        }
    }else{
        selectPrivOpusOmOrgZtree();
    }
}

function isSelectRegion(obj, isSearch){

    itemBasicFieldId = null;
    itemBasicFieldName = null;

    if(isSearch){
        $('#isSelectRegionSearch').val('search');
    }else{
        $('#isSelectRegionSearch').val('noSearch');
    }
    if(obj){
        var value = $(obj).val();
        if(!value){
            selectBscDicRegionZtree();
        }
    }else{
        selectBscDicRegionZtree();
    }
}

/**
 * @param name  获取值得对象
 * @param name1 需要赋值的对象
 * @param isMulit 是否多个
 */
function checkboxOnclick(name, name1, isMulit){

    var obj = document.getElementsByName(name);
    if(obj){
        if(isMulit){
            var check_val = [];
            for(var k in obj){
                if(obj[k].checked){
                    check_val.push(obj[k].value);
                }
            }
            var cb = check_val.join(',');
            $('#aedit_item_form input[name="'+ name1 +'"]').val(cb);
        }else{
            if(obj[0].checked){
                $('#aedit_item_form input[name="'+ name1 +'"]').val('1');
            }else{
                $('#aedit_item_form input[name="'+ name1 +'"]').val('0');
            }
        }
    }
}

