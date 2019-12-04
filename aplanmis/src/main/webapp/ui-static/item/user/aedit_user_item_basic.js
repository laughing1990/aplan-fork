$(function() {

    $("#aedit_item_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#opusOmOrgDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#bscDicRegionDiv').niceScroll({

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
    $('#selectOrgBtn').click(function(){

        var opusOmOrgTree = $.fn.zTree.getZTreeObj("opusOmOrgTree");
        if(itemBasicFieldId&&itemBasicFieldName){
            var nodes = opusOmOrgTree.getCheckedNodes(true);
            if(nodes!=null&&nodes.length>0){
                var ids = [];
                var names = [];
                for(var i=0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }
                $("#aedit_item_form input[name='"+ itemBasicFieldId +"']").val(ids.toString());
                $("#aedit_item_form textarea[name='"+ itemBasicFieldName +"']").val(names.toString());
                closeSelectOpusOmOrgZtree();
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
                    $("#aedit_item_form input[name='orgId']").val(selectNode.id);
                    $("#aedit_item_form input[name='orgName']").val(selectNode.name);
                    $("#aedit_item_form input[name='regionId']").val(selectNode.regionId);
                    $("#aedit_item_form input[name='regionName']").val(selectNode.regionName);
                }
                closeSelectOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        }
    });

    // 设置初始化校验
    aedit_item_validator = $("#aedit_item_form").validate({

        // 定义校验规则
        rules: {
            itemName: {
                required: true,
                maxlength: 100
            },
            itemCode: {
                required: true,
                maxlength: 50,
            },
            orgName: {
                required: true,
            },
            acceptMode: {
                required: true,
                maxlength: 10
            },
            sxmlzt:{
                required: true,
            },
            standardItemCode: {
                maxlength: 100
            },
            regionName:{
                required: true,
            },
            itemCategoryMark:{
                required: true,
                remote: {
                    url: ctx + '/aea/item/basic/checkUniqueItemCategoryMark.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        itemId: function () {
                            return $("#aedit_item_form input[name='itemId']").val();
                        },
                        isCatalog: function (){
                            return $("#aedit_item_form input[name='isCatalog']").val();
                        },
                        itemCategoryMark: function () {
                            return $("#aedit_item_form input[name='itemCategoryMark']").val();
                        }
                    }
                }
            },
            itemExchangeWay:{
                required: true,
            },
            useOneForm:{
                required: true,
            }
        },
        messages: {
            itemName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过100个字母!"
            },
            itemCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!",
            },
            orgName: {
                required: '<font color="red">此项必填！</font>',
            },
            acceptMode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过10个字母!"
            },
            sxmlzt: {
                required: '<font color="red">此项必填！</font>',
            },
            standardItemCode: {
                maxlength: "长度不能超过100个字母!"
            },
            regionName:{
                required: '<font color="red">此项必填！</font>',
            },
            itemCategoryMark:{
                required: '<font color="red">此项必填！</font>',
                remote: "编号已存在！",
            },
            itemExchangeWay:{
                required: '<font color="red">请选择实施事项换算方式！</font>',
            },
            useOneForm:{
                required: '<font color="red">请选择是否设置一张表单！</font>',
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#saveItemBasic').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var form = new FormData(document.getElementById("aedit_item_form"));
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: ctx + '/aea/item/basic/saveAeaItemBasic.do',
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
                            $('#saveItemBasic').show();
                            $('#aedit_item_modal').modal('hide');
                            // 列表数据重新加载
                            searchAllItemList();
                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveItemBasic').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveItemBasic').show();
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
});

function clearItemBasicFile(){

    $("#docTemplateFile").siblings('.custorm-style').find(".right-text").html("");
    $("#applyTableDemoFile").siblings('.custorm-style').find(".right-text").html("");
    $("#applyTableTemplateFile").siblings('.custorm-style').find(".right-text").html("");
    $('#docTemplateAtt').hide();
    $('#applyTableDemoAtt').hide();
    $('#applyTableTemplateAtt').hide();
    $('#docTemplateAtta').attr('href','#');
    $('#applyTableDemoAtta').attr('href','#');
    $('#applyTableTemplateAtta').attr('href','#');
}

//切换机构的表单形式
function changeOrgForm(isCatalog) {

    if(isCatalog){

        $("#item_basic_org_name").hide();
        $("#item_basic_guide_org_name").show();
        $("#aedit_item_form input[name='orgName']").rules('remove');
        $("#aedit_item_form input[name='regionName']").rules('remove');
        $("#org_necessary_mark").hide();

    }else{
        $("#item_basic_org_name").show();
        $("#item_basic_guide_org_name").hide();
        $("#aedit_item_form input[name='orgName']").rules('add',{
            required: true,
            messages:{
                required: '<font color="red">此项必填！</font>'
            }
        });
        $("#aedit_item_form input[name='regionName']").rules('add',{
            required: true,
            messages:{
                required: '<font color="red">此项必填！</font>'
            }
        });
        $("#org_necessary_mark").show();
    }
}

function changeItemNameCnToEn(obj){

    var isCatalog = $("#aedit_item_form input[name='isCatalog']").val();
    var itemBasicId = $("#aedit_item_form input[name='itemBasicId']").val();
    var parentItemId = $("#aedit_item_form input[name='parentItemId']").val();
    if(!itemBasicId&&isCatalog){ // 新增时标准事项或者顶级实施事项可修改
        if(isCatalog=='1'||(isCatalog=='0'&&(!parentItemId))){
            var value = $(obj).val();
            if(value){
                var pyValue = pinyin.getCamelChars(value);
                if(pyValue){
                    pyValue = replaceSpecialChar(pyValue);
                }
                $("#aedit_item_form input[name='itemCategoryMark']").val(pyValue);
            }else{
                $("#aedit_item_form input[name='itemCategoryMark']").val("");
            }
        }
    }
}

function replaceSpecialChar(value){

    if(value){
        var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&;|{}【】‘；：”“'。，、？]")
        var rs = "";
        for (var i = 0; i < value.length; i++) {
            rs = rs + value.substr(i, 1).replace(pattern, '');
        }
        return rs;
    }
}

/**
 * 新增
 *
 * @param isRoot 是否顶级
 * @param isCatalog 标准/实施
 */
function addItemInfo(isRoot, isCatalog){

    changeOrgForm(isCatalog);

    $("#aedit_item_modal").modal("show");
    $("#aedit_item_modal_title").html(isCatalog?"新增标准事项":"新增实施事项");
    $('#aedit_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#aedit_item_form')[0].reset();
    aedit_item_validator.resetForm();

    $("#aedit_item_form input[name='isCatalog']").val(isCatalog ? '1' : '0');
    $("#aedit_item_form input[name='itemBasicId']").val('');
    $("#aedit_item_form input[name='itemId']").val('');
    $("#aedit_item_form input[name='itemVerId']").val('');
    $("#aedit_item_form input[name='parentItemId']").val('');
    $("#aedit_item_form input[name='orgId']").val('');
    $("#aedit_item_form input[name='regionId']").val('');
    $("#aedit_item_form input[name='isLocal']").val('1');
    $("#aedit_item_form input[name='itemNature']").val(itemNature);
    $("#aedit_item_form input[name='sxmlzt']").val('1');
    $("#aedit_item_form select[name='isNeedState'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='useOneForm'] option:eq(1)").prop("selected", 'selected');
    $("#aedit_item_form select[name='isMilestoneItem'] option:eq(1)").prop("selected", 'selected');
    $("#aedit_item_form select[name='outerSystemHandle'] option:eq(2)").prop("selected", 'selected');
    $("#aedit_item_form select[name='itemType'] option:eq(1)").prop("selected", 'selected');
    $("#aedit_item_form select[name='itemProperty'] option:eq(1)").prop("selected", 'selected');
    // $("#aedit_item_form select[name='sxmlzt'] option:eq(1)").prop("selected", 'selected');
    $("#aedit_item_form select[name='sfsxgzcnz'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='bjType'] option:eq(1)").prop("selected", 'selected');
    $("#aedit_item_form input[name='appId']").val("");
    $("#aedit_item_form select[name='isLink'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='useOneForm'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='isCheckItem'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='isCheckPartform'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='isCheckProj'] option:eq(0)").prop("selected", 'selected');
    $("#aedit_item_form select[name='anticipateType'] option:eq(1)").prop("selected", 'selected');

    if(isCatalog){

        $("#isLinkDiv").hide();
        $("#isCheckItemDiv").hide();
        $('#isCheckProjDiv').hide();
        $('#itemExchangeWayDiv').show();
        $("#aedit_item_form input[name='itemExchangeWay'][value='1']").prop("checked", true);
        $('#aedit_item_form input[name="itemExchangeWay"]').rules("add", {
            required: true,
            messages: {
                required: '<font color="red">请选择实施事项换算方式！</font>'
            }
        });
    }else{

        $("#isLinkDiv").show();
        $("#isCheckItemDiv").show();
        $('#isCheckProjDiv').show();
        $('#itemExchangeWayDiv').hide();
        $('#aedit_item_form input[name="itemExchangeWay"]').rules("remove");
    }

    if(isRoot){
        $("#aedit_item_form input[name='itemCategoryMark']").removeAttr("readonly");
    }else{
        if(isCatalog){
            $("#aedit_item_form input[name='itemCategoryMark']").removeAttr("readonly");
        }else{
            $("#aedit_item_form input[name='itemCategoryMark']").attr("readonly", "true");
        }
    }
    clearItemBasicFile();
    $("#saveItemBasic").show();
}

function editItemBasicById(itemBasicId,itemVerStatus,isCatalog) {

    changeOrgForm(isCatalog != '0');

    $("#aedit_item_modal").modal("show");
    $("#aedit_item_modal_title").html(isCatalog=='1'?"编辑标准事项":"编辑实施事项");
    $('#aedit_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#aedit_item_form')[0].reset();
    aedit_item_validator.resetForm();

    $("#aedit_item_form input[name='itemId']").val('');
    $("#aedit_item_form input[name='itemVerId']").val('');
    $("#aedit_item_form input[name='parentItemId']").val('');
    $("#aedit_item_form input[name='itemBasicId']").val('');
    $("#aedit_item_form input[name='orgId']").val('');
    $("#aedit_item_form input[name='regionId']").val('');
    $("#aedit_item_form input[name='sxmlzt']").val('');
    $("#aedit_item_form input[name='itemCategoryMark']").attr("readOnly", "true");
    clearItemBasicFile();

    if(isCatalog=='1'){
        $("#isLinkDiv").hide();
        $("#isCheckItemDiv").hide();
        $('#isCheckProjDiv').hide();
        $('#itemExchangeWayDiv').show();
        $('#aedit_item_form input[name="itemExchangeWay"]').rules("add", {
            required: true,
            messages: {
                required: '<font color="red">请选择实施事项换算方式！</font>'
            }
        });
    }else{
        $("#isLinkDiv").show();
        $("#isCheckItemDiv").show();
        $('#isCheckProjDiv').show();
        $('#itemExchangeWayDiv').hide();
        $('#aedit_item_form input[name="itemExchangeWay"]').rules("remove");
    }

    $.ajax({
        url: ctx + '/aea/item/basic/getAeaItemBasic.do',
        type: 'post',
        data: {'id': itemBasicId},
        async: false,
        success: function (data) {
            if(data) {
                if (data.docTemplate!=null&&data.docTemplate.length>0) {
                    $('#docTemplateAtt').show();
                    $('#docTemplateAtta').attr('href', ctx + '/aea/item/basic/indexItemDocOrTemplateFile.do?fileTypeName=文书模版附件&itemBasicId=' + itemBasicId + '&fileType=docTemplate');
                }else{
                    $('#docTemplateAtt').hide();
                }

                if (data.applyTableDemo!=null&&data.applyTableDemo.length>0) {
                    $('#applyTableDemoAtt').show();
                    $('#applyTableDemoAtta').attr('href', ctx + '/aea/item/basic/indexItemDocOrTemplateFile.do?fileTypeName=申请表范本附件&itemBasicId=' + itemBasicId + '&fileType=applyTableDemo');
                }else{
                    $('#applyTableDemoAtt').hide();
                }

                if (data.applyTableTemplate!=null&&data.applyTableTemplate.length>0) {
                    $('#applyTableTemplateAtt').show();
                    $('#applyTableTemplateAtta').attr('href', ctx + '/aea/item/basic/indexItemDocOrTemplateFile.do?fileTypeName=申请表模版附件&itemBasicId=' + itemBasicId + '&fileType=applyTableTemplate');
                }else{
                    $('#applyTableTemplateAtt').hide();
                }

                if (data.sendResultMode){
                    var arr = data.sendResultMode.split(",");
                    for(var i in arr){
                        $("#aedit_item_form input[name='sendResultMode1'][value='"+ arr[i] +"']").prop("checked", true);
                    }
                }

                if (data.xkdx){
                    var arr = data.xkdx.split(",");
                    for(var i in arr){
                        $("#aedit_item_form input[name='xkdx1'][value='"+ arr[i] +"']").prop("checked", true);
                    }
                }
            }
            // 记载表单数据
            loadFormData(true,'#aedit_item_form',data);
            if(itemVerStatus == '1' || itemVerStatus == '3'){

                $("#aedit_item_modal_title").html("查看事项");
                $('#saveItemBasic').hide();
                $('#docTemplateAtts').hide();
                $('#applyTableDemoAtts').hide();
                $('#applyTableTemplateAtts').hide();
            }else{

                $('#saveItemBasic').show();
                $('#docTemplateAtts').show();
                $('#applyTableDemoAtts').show();
                $('#applyTableTemplateAtts').show();
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
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

function viewOrgZtree(fieldId, fieldName){

    selectOpusOmOrgZtree2(fieldId, fieldName);
    var idstr = $("#aedit_item_form input[name='"+ itemBasicFieldId +"']").val();
    if(idstr){
        var ids = idstr.split(",");
        if(ids!=null&&ids.length>0){
            var opusOmOrgTree = $.fn.zTree.getZTreeObj("opusOmOrgTree");
            for(var i=0;i<ids.length;i++){
                var node = opusOmOrgTree.getNodeByParam("id", ids[i]);
                if(node){
                    opusOmOrgTree.checkNode(node, true);
                    opusOmOrgTree.selectNode(node);
                }
            }
        }
    }
}

function isSelectOpuOmOrg(obj, isSearch){

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
            selectOpusOmOrgZtree();
        }
    }else{
        selectOpusOmOrgZtree();
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

// 删除附件
function delItemBasicAtt(type){

    var title;
    if(type=='docTemplate'){
        title = "文书模版附件";
    }else if(type=='applyTableDemo'){
        title = "申请表范本附件";
    }else{
        title = "申请表模版附件";
    }
    var msg = '此操作将删除事项'+ title +',您确定执行吗？';
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
                url: ctx + '/aea/item/basic/delItemBasicAtt.do',
                dataType: 'json',
                data: {
                    'itemBasicId': $("#aedit_item_form input[name='itemBasicId']").val(),
                    'type': type
                },
                success: function (result) {
                    if (result.success) {
                        swal({
                            type: 'success',
                            title: '删除成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        if(type=='docTemplate'){
                            $('#docTemplateAtt').hide();
                        }else if(type=='applyTableDemo'){
                            $('#applyTableDemoAtt').hide();
                        }else{
                            $('#applyTableTemplateAtt').hide();
                        }
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
}