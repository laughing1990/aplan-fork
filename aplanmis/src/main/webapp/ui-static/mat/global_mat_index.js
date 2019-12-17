var commonQueryParams = [],aedit_mat_validator;
var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    // 组织机构事项列表
    $('#customTable').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    $('#show_mat_att_tb').bootstrapTable('resetView', {
        height: 400
    });

    $("#aedit_mat_global_body").niceScroll({

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

    $('#show_mat_att_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $(".left-button").click(function(e) {
        e.preventDefault();
    });

    // 初始化表单校验
    initValidate();

    // 材料类别选择点击事件绑定
    $('.open-mat-type, input[name="matTypeName"]').bind('click', openSelectMatTypeModal);

    // 电子证照选择点击事件绑定
    $('.open-cert-type, input[name="certName"]').click(function(){

        var value = $('#aedit_mat_form input[name="certId"]').val();
        openSelectCertModal(value);
    });

    // 表单选择点击事件绑定
    $('.open-form-type, input[name="formName"]').click(function(){

        var value = $('#aedit_mat_form input[name="stoFormId"]').val();
        openSelectFormModal(value);
    });

    // 标准材料选择击事件绑定
    $('.open-stdmat-type, input[name="stdmatName"]').click(function() {

        var value = $('#aedit_mat_form input[name="stdmatId"]').val();
        openSelectStdmatModal(value);
    });

    // 双击事件
    $('#customTable').on('dbl-click-row.bs.table', function (e, row, element) {
        loadGlobalMatData(row.matId)
    });

    // 材料类型选择
    $("#aedit_mat_form select[name='matProp']").change(function(){
        var value = $(this).val();
        handleSelectMatProNew(value);
    });

    // 材料类别确定事件
    $('#selectMatTypeBtn').bind('click', selectMatType);

    // 选择电子证照
    $('#selectCertBtn').bind('click', selectCert);

    // 选择表单
    $('#selectFormBtn').bind('click', selectForm);

    // 选择标准材料
    $('#selectStdmatBtn').bind('click', selectStdmat);
});

function handleSelectMatProNew(value){

    if(value=='m'){

        $('#selectCertDiv').hide();
        $('#selectFormDiv').hide();

        $('#aedit_mat_form input[name="certName"]').rules('remove');
        $('#aedit_mat_form input[name="formName"]').rules('remove');

    }else if(value=='c'){

        $('#selectCertDiv').show();
        $('#selectFormDiv').hide();
        $('#aedit_mat_form input[name="certName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择电子证照！</font>'
            }
        });
        $('#aedit_mat_form input[name="formName"]').rules('remove');

    }else{

        $('#selectCertDiv').hide();
        $('#selectFormDiv').show();
        $('#aedit_mat_form input[name="certName"]').rules('remove');
        $('#aedit_mat_form input[name="formName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择表单！</font>'
            }
        });
    }
}

function matPropormatter(value, row, index){

    var matProp = row.matProp;
    if(matProp){
        if(matProp=='m'){
            return '普通材料';
        }else if(matProp=='c'){
            return '证照材料';
        }else{
            return '在线表单材料';
        }
    }
}

function clearAllFile(){

    $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
    $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
    $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
    $('#templateDocDiv').hide();
    $('#sampleDocDiv').hide();
    $('#reviewSampleDocDiv').hide();
}

function uploadFileChange(obj){

    $(obj).siblings('.custorm-style').find(".right-text").html("");
    var files = $(obj)[0].files;
    if(files!=null&&files.length>0){
        var names = [];
        for(var i=0;i<files.length;i++){
            names.push(files[i].name);
        }
        $(obj).siblings('.custorm-style').find(".right-text").html(names.toString());
    }
}

// 查询事件
function searchMatSto() {

    var params = $('#search_mat_sto_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function refreshMatSto(){

    $('#search_mat_sto_form')[0].reset();
    searchMatSto();
}

// 清空查询
function clearSearchMatSto() {

    $('#search_mat_sto_form')[0].reset();
    searchMatSto();
}

// 列表查询参数处理
function matStoParam (params) {

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


function matStotResponseData(res) {

    return res;
}

function matAttTbParam(params){

    var pageNum = (params.offset / params.limit) + 1;
    var queryParam = {
        pageNum: pageNum,
        pageSize: params.limit,
        sort: params.sort,
        order: params.order,
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

function matAttTbResponseData(res) {
    return res;
}

// 格式化操作栏
function operatorFormatter(value, row, index, field) {

    var operatorStr = '<a href="javascript:loadGlobalMatData(\'' + row.matId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    operatorStr = operatorStr + '<a href="javascript:delGlobalMat(\'' + row.matId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
    return operatorStr;
}


//格式化材料来源yjy2018
function operatorFormatterMatFrom(value, row, index, field) {

    var bb = "未选择";
    if(row.matFrom!=null) {

        var matFrom = row.matFrom.split(",");
        var aa = [];
        for (qee in matFrom) {
            if (matFrom[qee] == 1) {
                aa.push("行政机关");
            }
            if (matFrom[qee] == 2) {
                aa.push("企事业单位");
            }
            if (matFrom[qee] == 3) {
                aa.push("社会组织");
            }
            if (matFrom[qee] == 4) {
                aa.push("申请人");
            }
            if (matFrom[qee] == 5) {
                aa.push("中介服务");
            }
        }
        bb = aa.join(',');
    }
    return bb;
}

// 初始化表单检验
function initValidate() {

    aedit_mat_validator = $("#aedit_mat_form").validate({
        // 定义校验规则
        rules: {
            matTypeName:{
                required: true,
            },
            matName: {
                required: true,
                maxlength: 500,
                remote: {
                    url: MAT_URL_PREFIX + 'checkMatName.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matId: function () {
                            return $("#aedit_mat_form input[name='matId']").val();
                        },
                        matName: function () {
                            return $("#aedit_mat_form input[name='matName']").val();
                        },
                        isCommon: function () {
                            return $("#aedit_mat_form input[name='isCommon']").val();
                        },
                    }
                }
            },
            matCode:{
                required: true,
                maxlength: 200,
                remote: {
                    url: MAT_URL_PREFIX + 'checkMatCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matId: function () {
                            return $("#aedit_mat_form input[name='matId']").val();
                        },
                        matCode: function () {
                            return $("#aedit_mat_form input[name='matCode']").val();
                        }
                    }
                }
            },
            reviewKeyPoints:{
                required: true,
            },
            isCommon:{
                required: true,
            },
            zcqy:{
                required: true,
            },
            isOfficialDoc:{
                required: true,
            }
        },
        messages: {
            matTypeName:{
                required: '<font color="red">材料类型必填！</font>',
            },
            matName: {
                required: '<font color="red">材料名称必填！</font>',
                maxlength: "最大长度不能超过500字符!",
                remote: '材料名称已存在!'
            },
            matCode:{
                required: '<font color="red">材料编号必填！</font>',
                maxlength: '最大长度不能超过200字符',
                remote: '材料编号已存在!'
            },
            reviewKeyPoints:{
                required: '<font color="red">审查要点必填！</font>',
            },
            isCommon:{
                required: '<font color="red">是否通用材料必选！</font>',
            },
            zcqy:{
                required: '<font color="red">是否支持容缺必选！</font>',
            },
            isOfficialDoc:{
                required: '<font color="red">是否批文批复必选！</font>',
            }
        },
        // 提交表单
        submitHandler: function () {

            $("#uploadProgress").modal("show");
            $('#saveMatStoreBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var duePaperCount = $('#aedit_mat_form input[name="duePaperCount"]').val();
            var dueCopyCount = $('#aedit_mat_form input[name="dueCopyCount"]').val();
            if(!duePaperCount){
                $('#aedit_mat_form input[name="duePaperCount"]').val('1');
            }
            if(!dueCopyCount){
                $('#aedit_mat_form input[name="dueCopyCount"]').val('1');
            }

            var formData = new FormData(document.getElementById("aedit_mat_form"));
            $.ajax({
                type: 'post',
                url: MAT_URL_PREFIX + 'handleGlobalMat.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
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
                            $('#saveMatStoreBtn').show();
                            $('#aedit_mat_modal').modal('hide');
                            // 列表数据重新加载
                            searchMatSto();
                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveMatStoreBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveMatStoreBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

// 显示新增窗口
function addGlobalMat() {

    $('#aedit_mat_modal_title').html('新增材料');
    $('#aedit_mat_modal').modal('show');
    $('#aedit_mat_global_body').animate({scrollTop: 0}, 800);//滚动到顶部

    $("#aedit_mat_form")[0].reset();
    $("#aedit_mat_form").resetForm();
    clearAllFile();
    $("#aedit_mat_form input[name='matId']").val("");
    $("#aedit_mat_form input[name='isGlobalShare']").val("1");
    $("#aedit_mat_form input[name='duePaperCount']").val("1");
    $("#aedit_mat_form input[name='dueCopyCount']").val("1");
    $("#aedit_mat_form input[name='paperIsRequire'][value='0']").prop("checked", true);
    $("#aedit_mat_form input[name='attIsRequire'][value='0']").prop("checked", true);
    $("#aedit_mat_form input[name='isCommon'][value='1']").prop("checked", true);
    $("#aedit_mat_form input[name='zcqy'][value='1']").prop("checked", true);
    $("#aedit_mat_form input[name='isOfficialDoc'][value='0']").prop("checked", true);
    $("#aedit_mat_form input[name='certId']").val("");
    $("#aedit_mat_form input[name='stoFormId']").val("");
    $("#aedit_mat_form input[name='matProp'][value='m']").prop("checked", true);
    handleSelectMatProNew('m');

    // 编号赋值
    $.ajax({
        url: ctx + '/bsc/rule/code/getOneOrgAutoCode.do',
        type: 'post',
        data: {'codeIc': "AEA-ITEM-MAT-CODE"},
        success: function (data) {
            $("#aedit_mat_form input[name='matCode']").val(data);
        }
    });
}

// 选择了一个材料类别
function selectMatType() {

    var selectMatTypeTree = $.fn.zTree.getZTreeObj("selectMatTypeTree");
    var matTypes = selectMatTypeTree.getSelectedNodes();
    if(matTypes!=null&&matTypes.length>0){
        var matTypeId = matTypes[0].id;
        var matTypeName = matTypes[0].name;
        $('#aedit_mat_form input[name="matTypeId"]').val(matTypeId);
        $('#aedit_mat_form input[name="matTypeName"]').val(matTypeName);
        // 关闭窗口
        closeSelectMatTypeZtree();
    }else{
        swal('错误信息', "请选择材料类型！", 'error');
    }
}

function selectCert(){

    var selectCertTree = $.fn.zTree.getZTreeObj("selectCertTree");
    var certs = selectCertTree.getCheckedNodes(true);
    if(certs!=null&&certs.length>0){
        var certId = certs[0].id;
        var certName = certs[0].name;
        $('#aedit_mat_form input[name="certId"]').val(certId);
        $('#aedit_mat_form input[name="certName"]').val(certName);
        // 关闭窗口
        closeSelectCertModal();
    }else{
        swal('错误信息', "请选择电子证照！", 'error');
    }
}

function selectForm(){

    var selectFormTree = $.fn.zTree.getZTreeObj("selectFormTree");
    var forms = selectFormTree.getCheckedNodes(true);
    if(forms!=null&&forms.length>0){
        var formId = forms[0].id;
        var formName = forms[0].name;
        var index = formName.lastIndexOf('】');
        if(index>-1){
            formName = formName.substring(index+1);
        }
        $('#aedit_mat_form input[name="stoFormId"]').val(formId);
        $('#aedit_mat_form input[name="formName"]').val(formName);
        // 关闭窗口
        closeSelectFormModal();
    }else{
        swal('错误信息', "请选择表单！", 'error');
    }
}

function selectStdmat(){

    var selectStdmatTree = $.fn.zTree.getZTreeObj("selectStdmatTree");
    var stdmats = selectStdmatTree.getCheckedNodes(true);
    if(stdmats!=null&&stdmats.length>0){
        var id = stdmats[0].id;
        var name = stdmats[0].name;
        $('#aedit_mat_form input[name="stdmatId"]').val(id);
        $('#aedit_mat_form input[name="stdmatName"]').val(name);
        // 关闭窗口
        closeSelectStdmatModal();
    }else{
        swal('错误信息', "请选择标准材料！", 'error');
    }
}

// 修改数据
function loadGlobalMatData(id) {

    $('#aedit_mat_modal_title').html('编辑材料');
    $('#aedit_mat_modal').modal('show');
    $('#aedit_mat_global_body').animate({scrollTop: 0}, 800);//滚动到顶部
    $("#aedit_mat_form")[0].reset();
    $("#aedit_mat_form").resetForm();
    $("#aedit_mat_form input[name='matId']").val('');
    $("#aedit_mat_form input[name='certId']").val('');
    $("#aedit_mat_form input[name='stoFormId']").val('');
    clearAllFile();

    $.ajax({
        url: MAT_URL_PREFIX + 'getAeaItemMat.do',
        type: 'post',
        data: {'id': id},
        async: false,
        success: function (data) {
            if(data) {

                if (data.templateDocCount&&data.templateDocCount!=0) {

                    $('#templateDocDiv').show();
                    $('#templateDocButton').html(data.templateDocCount + "个附件");
                }else{
                    $('#templateDocDiv').hide();
                }

                if (data.sampleDocCount&&data.sampleDocCount!=0) {

                    $('#sampleDocDiv').show();
                    $('#sampleDocButton').html(data.sampleDocCount + "个附件");
                }else{
                    $('#sampleDocDiv').hide();
                }

                if (data.reviewSampleDocCount&&data.reviewSampleDocCount!=0) {

                    $('#reviewSampleDocDiv').show();
                    $('#reviewSampleDocButton').html(data.reviewSampleDocCount + "个附件");
                }else{
                    $('#reviewSampleDocDiv').hide();
                }
                if (data.matFrom!=null){
                    var matFromData=data.matFrom;
                    var matFromDataArray=matFromData.split(",");
                    ckAll = document.getElementsByName("matFromCb");
                    ck1= document.getElementById("matFrom1");
                    ck2= document.getElementById("matFrom2");
                    ck3= document.getElementById("matFrom3");
                    ck4= document.getElementById("matFrom4");
                    ck5= document.getElementById("matFrom5");
                    for(qee in matFromDataArray){
                        if(matFromDataArray[qee]==1){
                            ck1.checked = true;
                        }
                        if(matFromDataArray[qee]==2){
                            ck2.checked = true;
                        }
                        if(matFromDataArray[qee]==3){
                            ck3.checked = true;
                        }
                        if(matFromDataArray[qee]==4){
                            ck4.checked = true;
                        }
                        if(matFromDataArray[qee]==5){
                            ck5.checked = true;
                        }
                    }
                }

                if (data.matProp){
                    handleSelectMatProNew(data.matProp);
                }

                // 记载表单数据
                loadFormData(true,'#aedit_mat_form',data);
            }
        },
        error:function(){
            swal('错误信息', "获取材料信息失败！", 'error');
        }
    });
}

function searchMatSto() {

    var params = $('#search_mat_sto_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function searchMatAttSto(type) {

    var matId = $('#aedit_mat_form input[name="matId"]').val();
    var pkName = "";
    if(type=='0'){
        pkName = "TEMPLATE_DOC";
    }else if(type=='1'){
        pkName = "SAMPLE_DOC";
    }else if(type=='2'){
        pkName = "REVIEW_SAMPLE_DOC";
    }
    commonQueryParams = [];
    commonQueryParams.push({name: "tableName",value:"AEA_ITEM_MAT"});
    commonQueryParams.push({name: "pkName",value: pkName});
    commonQueryParams.push({name: "recordId",value: matId});
    $("#show_mat_att_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#show_mat_att_tb").bootstrapTable('refresh');       //无参数刷新
}

var showMatType = null;
function showMatAtt(type){

    $('#show_mat_att_modal').modal('show');
    showMatType = type;
    searchMatAttSto(type);
}

// 批量删除数据
function batchDelGlobalMat() {

    var rows = $("#customTable").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        var names = [];
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].matId);
            names.push(rows[i].matName);
        }
        swal({
            text: '此操作将批量删除当前库材料，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    type: 'post',
                    url: MAT_URL_PREFIX + 'batchDeleteAeaItemMatByIds.do',
                    dataType: 'json',
                    data: {'ids': ids.toString(), 'names': names.toString()},
                    success: function (result) {
                        if (result.success) {
                            // swal({
                            //     type: 'success',
                            //     title: result.message,
                            //     showConfirmButton: false,
                            //     timer: 3000
                            // });
                            swal('提示信息', result.message ,'success');
                            // 列表数据重新加载
                            searchMatSto();
                        } else {
                            swal('错误信息', result.message ,'error');
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

// 删除单条数据
function delGlobalMat(id) {

    swal({
        text: '此操作将删除当前库材料，您确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                type: 'post',
                url: MAT_URL_PREFIX + 'deleteAeaItemMatById.do',
                dataType: 'json',
                data: {'id': id},
                success: function (result) {
                    if (result.success) {
                        // swal({
                        //     type: 'success',
                        //     title: result.message,
                        //     showConfirmButton: false,
                        //     timer: 3000
                        // });
                        swal('提示信息', result.message ,'success');
                        searchMatSto();
                    } else {
                        swal('错误信息', result.message ,'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });
}

//yjy2018
//获取复选框的值，并且把值返回给matFrom
function checkboxOnclick(checkbox){

    obj = document.getElementsByName("matFromCb");
    check_val = [];
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    var cb=check_val.join(',');
    $('#matFrom').val(cb);
}

function viewAttNameFormatter(value, row, index){

    // 图片
    if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"||row.attFormat=="gif"){

        var url = "";
        if(row.attType=='KP'){
            url = ctx + '/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
        }else{
            url = MAT_URL_PREFIX + 'downloadGlobalMatDoc.do?detailId='+ row.detailId;
        }
        return '<a href="#" onclick="showImgFile(\''+ row.detailId +'\')">'+ row.attName +'</a>';

    }else{ // 文件

        var url = "";
        if(row.attType=='KP'){

            url = ctx +'/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
        }else{

            url = MAT_URL_PREFIX + 'downloadGlobalMatDoc.do?detailId='+ row.detailId;
        }
        return '<a href="#" onclick="showDownloadFile(\''+ url +'\')">'+ row.attName +'</a>';
    }
}

function viewAttSizeFormatter(value, row, index){

    if(value){
        return value/1000;
    }
}

function showDownloadFile(url){

    window.open(url,"下载文件");
}

function showImgFile(detailId){

    window.open(MAT_URL_PREFIX + 'showFile.do?detailId='+ detailId,"展示图片");
}

function attOperFormatter(value, row, index){

    if(row.attType!='KP'){
        var deleteBtn = '<a href="javascript:deleteMatAttrByDetailId(\'' + row.detailId + '\')" ' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                            'title="删除"><i class="la la-trash"></i>' +
                        '</a>';
        return deleteBtn;
    }
}

function deleteMatAttrByDetailId(id){

    if(id){
        var matId = $('#aedit_mat_form input[name="matId"]').val();
        var msg = '此操作将删除附件,确定要删除吗？';
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
                    url: MAT_URL_PREFIX+'deleteGlobalMatDoc.do',
                    dataType: 'json',
                    data: {'detailId': id, 'type': showMatType,'matId': matId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchMatAttSto(showMatType);
                            loadGlobalMatData(matId);
                        } else {
                            swal('错误信息','删除失败','error');
                        }
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

function editActStoFormFunc(){

    var formId = $('#aedit_mat_form input[name="stoFormId"]').val();
    if(formId){
        openFullWindow(ctx + '/design?formId='+formId+'&needCallBackSaveActStoForm=0&requiredField=refEntityId');
    }else{
        swal('提示信息','请选择表单!','info');
    }
}

