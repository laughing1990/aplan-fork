// 格式
function stageRelMatCertFormatter(value, row, index) {

    var editBtn = null;
    var deleteBtn = null;

    if(row.fileType=='mat'){

        editBtn = '<a href="javascript:editStageRelMatCertById(\'' + row.matId + '\',\'mat\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

        deleteBtn = '<a href="javascript:deleteStageRelMatCertById(\''+row.matId + '\',\'mat\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.fileType=='cert'){

        editBtn = '<a href="javascript:editStageRelMatCertById(\'' + row.certId + '\',\'cert\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

        deleteBtn = '<a href="javascript:deleteStageRelMatCertById(\''+row.certId + '\',\'cert\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';
    }

    return editBtn + deleteBtn;
}

function fileTypeFormatter(value, row, index){

    if(value=='mat'){
        return '材料';
    }else if(value=='cert'){
        return '证照';
    }
}

$(function(){

    aedit_stage_mat_validator = $("#aedit_stage_mat_form").validate({
        // 定义校验规则
        rules: {
            isGlobalShare:{
                required: true,
            },
            matTypeName:{
                required: true,
            },
            matName: {
                required: true,
                maxlength: 100
            },
            matCode: {
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/item/mat/checkMatCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matId: function(){
                            return $("#aedit_stage_mat_form input[name='matId']").val();
                        },
                        matCode: function() {
                            return $("#aedit_stage_mat_form input[name='matCode']").val();
                        }
                    }
                }
            }
        },
        messages: {
            isGlobalShare:{
                required: '<font color="red">此项必填！</font>',
            },
            matTypeName:{
                required: '<font color="red">此项必填！</font>',
            },
            matName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            },
            matCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            }
        },
        // 提交表单
        submitHandler: function () {

            var formData = new FormData(document.getElementById("aedit_stage_mat_form"));
            $.ajax({
                type: "POST",
                url: ctx+'/aea/par/in/saveStageNoStateMatIn.do',
                dataType: "json",
                cache: false,
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success){

                        $('#aedit_stage_mat_modal').modal('hide');
                        searchStageRelMatCert();

                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });

    aedit_stage_cert_validator = $("#aedit_stage_cert_form").validate({

        // 定义校验规则
        rules: {
            certName: {
                required: true,
                maxlength: 500
            },
            certCode: {
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/cert/checkUniqueCertCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        certId: function(){
                            return $("#aedit_stage_cert_form input[name='certId']").val();
                        },
                        certCode: function() {
                            return $("#aedit_stage_cert_form input[name='certCode']").val();
                        }
                    }
                }
            },
            attDirName:{
                required: true,
            },
            certTypeId: {
                required: true,
            },
            certHolder:{
                maxlength: 1
            },
            softwareEnv:{
                maxlength: 1000
            },
            busAction:{
                maxlength: 1000
            },
            certMemo:{
                maxlength: 500
            }
        },
        messages: {
            certName: {
                required: '此项必填!',
                maxlength: "长度不能超过500个字母!"
            },
            certCode: {
                required: '此项必填!',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            },
            attDirName:{
                required: '此项必填!',
            },
            certTypeId: {
                required: '此项必填!',
            },
            certHolder:{
                maxlength: "长度不能超过1个字母!"
            },
            softwareEnv:{
                maxlength: "长度不能超过1000个字母!"
            },
            busAction:{
                maxlength: "长度不能超过1000个字母!"
            },
            certMemo:{
                maxlength: "长度不能超过500个字母!"
            }
        },
        // 提交表单
        submitHandler: function(form){

            var d = {};
            var t = $('#aedit_stage_cert_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/cert/saveAeaCert.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success) {
                        $('#aedit_stage_cert_modal').modal('hide');
                        searchStageRelMatCert();
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', '保存证照信息失败！', 'error');
                }
            });
        }
    });
});


//参数设置
function stageRelMatCertParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchStageRelMatCert(){

    commonQueryParams = {};
    var t = $('#search_stage_rel_mat_cert_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['stageId'] = stageId;
    $("#stage_rel_mat_cert_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_rel_mat_cert_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStageRelMatCert(){

    $('#search_stage_rel_mat_cert_form')[0].reset();
    searchStageRelMatCert();
}


// 刷新
function refreshStageRelMatCert(){

    searchStageRelMatCert();
}

// 阶段新增材料
var aedit_stage_mat_validator = null;
var aedit_stage_cert_validator = null;
function addStageRelMat(){

    $('#aedit_stage_mat_modal').modal('show');
    $('#aedit_stage_mat_modal_title').html('新增材料');
    $('#aedit_stage_mat_form')[0].reset();
    aedit_stage_mat_validator.resetForm();
    $("#aedit_stage_mat_form input[name='inId']").val('');
    $("#aedit_stage_mat_form input[name='matId']").val('');
    $("#aedit_stage_mat_form input[name='stageId']").val(stageId);
    $("#aedit_stage_mat_form input[name='isStateIn']").val('0');
    $("#aedit_stage_mat_form input[name='isActive']").val('1');
    $("#isGlobalShare2 option:eq(0)").prop("selected", 'selected');
    initDoc2();
}

// 导入阶段全局材料
function addStageGlobalMat(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    $('#select_stage_global_mat_modal').modal('show');
    $('#select_stage_global_mat_modal_title').html('导入全局材料');
    searchStageGlobalMat();
}

// 批量阶段删除材料证照
function batchDeleteStageRelMatCert(){

    var rows = $("#stage_rel_mat_cert_tb").bootstrapTable('getSelections');
    var matIds = [];
    var certIds = [];
    if(rows!=null&&rows.length>0){
        for(var i=0;i<rows.length;i++){
            if(rows[i].fileType='mat'){
                matIds.push(rows[i].matId);
            }else if(rows[i].fileType='cert'){
                certIds.push(rows[i].certId);
            }
        }

        swal({
            title: '提示信息',
            text: '此操作将批量删除材料、证照数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {

                if(matIds!=null&&matIds.length>0){
                    $.ajax({
                        url: ctx+'/aea/item/mat/batchDeleteAeaItemMatByIds.do',
                        type: 'POST',
                        data:{'ids': matIds.toString()},
                        success: function (result){
                            // 刷新表格
                            searchStageRelMatCert();
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }

                if(certIds!=null&&certIds.length>0){
                    $.ajax({
                        url: ctx+'/aea/cert/batchDeleteCertByIds.do',
                        type: 'POST',
                        data:{'ids': certIds.toString()},
                        success: function (result){
                            if(result.success){
                                // 刷新表格
                                searchStageRelMatCert();
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

function editStageRelMatCertById(id,fileType){

    if(id){
        if(fileType=='mat'){

            $('#aedit_stage_mat_modal').modal('show');
            $('#aedit_stage_mat_modal_title').html('编辑材料');
            $('#aedit_stage_mat_form')[0].reset();
            aedit_stage_mat_validator.resetForm();
            $("#aedit_stage_mat_form input[name='inId']").val('');
            $("#aedit_stage_mat_form input[name='matId']").val('');

            $.ajax({
                type: 'post',
                url: ctx + '/aea/item/mat/getAeaItemMat.do',
                dataType: 'json',
                async: false,
                data: {'id': id},
                success: function (data) {
                    if (data) {

                        if(data.templateDoc!=null && data.templateDoc!=''){
                            $('#templateDocFile2').addClass("hide");
                            $('#templateDocButton2').removeClass("hide");
                        }else{
                            $('#templateDocFile2').removeClass("hide");
                            $('#templateDocButton2').addClass("hide");
                        }
                        if(data.sampleDoc!=null && data.sampleDoc!=''){
                            $('#sampleDocFile2').addClass("hide");
                            $('#sampleDocButton2').removeClass("hide");
                        }else{
                            $('#sampleDocFile2').removeClass("hide");
                            $('#sampleDocButton2').addClass("hide");
                        }
                        loadFormData(true, "#aedit_stage_mat_form", data);
                    } else {
                        swal('错误信息', "加载材料数据失败！", 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else if(fileType=='cert'){

            // 加载展示数据
            $('#aedit_stage_cert_form')[0].reset();
            aedit_stage_cert_validator.resetForm();
            $("#aedit_stage_cert_form input[name='certId']").val('');
            $("#aedit_stage_cert_form input[name='certTypeId']").val('');
            $.ajax({
                url: ctx + '/aea/cert/getAeaCert.do',
                type: 'post',
                data: {'id':id},
                async: false,
                success: function (data) {
                    if (data) {
                        loadFormData(true,'#aedit_stage_cert_form',data);
                    }
                },
                error: function () {
                    swal('错误信息', "获取证照信息失败！", 'error');
                }
            });
        }
    }
}

function deleteStageRelMatCertById(id,fileType){

    if(id&&fileType){
        var  url = null;
        var  title = '';
        if(fileType=='mat'){

            title = '材料';
            url = ctx+'/aea/item/mat/deleteAeaItemMatById.do';

        }else if(fileType=='cert'){

            title = '证照';
            url = ctx+'/aea/cert/deleteAeaCertById.do';
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除'+ title +'数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data:{'id': id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 刷新表格
                            searchStageRelMatCert();
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
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

// 初始化上传文件显示
function initDoc2() {

    $('#templateDocFile2').removeClass("hide");
    $('#templateDocButton2').addClass("hide");
    $('#sampleDocFile2').removeClass("hide");
    $('#sampleDocButton2').addClass("hide");
}


function downloadDoc2(type) {

    var fileId = null;
    if(type==0){
        fileId = $('#templateDoc2').val();
    }else if(type==1){
        fileId = $('#sampleDoc2').val();
    }
    window.location.href = ctx+'/aea/item/mat/downloadGlobalMatDoc.do?detailId=' + fileId;
}


function deleteDoc2(type) {

    var data = {};
    data.type = type;
    data.matId = $('#aedit_stage_mat_form input[name="matId"]').val();
    if(type==0){
        data.detailId = $('#templateDoc2').val();
    }else if(type==1){
        data.detailId = $('#sampleDoc2').val();
    }
    $.ajax({

        type: 'post',
        url: ctx+'/aea/item/mat/deleteGlobalMatDoc.do',
        data: data,
        success: function (result) {
            if (result.success) {
                swal({
                    type: 'success',
                    title: '删除成功！',
                    showConfirmButton: false,
                    timer: 1500
                });
                if(type==0){
                    $('#templateDocButton2').addClass("hide");
                    $('#templateDocFile2').removeClass("hide");
                    $('#templateDoc2').val('');
                }else if(type==1){
                    $('#sampleDocButton2').addClass("hide");
                    $('#sampleDocFile2').removeClass("hide");
                    $('#sampleDoc2').val('');
                }
            }else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}