var commonQueryParams = [];
var aedit_stage_mat_validator = null;
var MAT_URL_PREFIX = ctx + '/aea/item/mat/';

function stageInMatCertFormatter(value, row, index) {

    var editBtn = null;
    var deleteBtn = null;

    var title2 = '编辑关联事项';
    if(!curIsEditable){
        title2 = '查看关联事项';
    }

    var title = '编辑材料';
    var icoCss = 'la la-edit';
    if(!curIsEditable){
        title = '查看材料';
        icoCss = 'la la-search';
    }

    editBtn = '<a href="javascript:editStageInMatCertById(\''+ row.inId +'\',\'' + row.matId + '\',\'mat\')" ' +
                 'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                 'title="'+ title +'"><i class="'+ icoCss +'"></i>' +
              '</a>';

    var inRelItem = '<a href="javascript:editItemParInByInId(\'' + row.inId + '\')" ' +
                       'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill"' +
                       'title="'+ title2 +'"><i class="la la-cog"></i>' +
                   '</a>';

    if(row.matProp=='m'){

        deleteBtn = '<a href="javascript:deleteStageInMatCertFormById(\''+ row.inId +'\',\''+row.matId + '\',\'mat\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除材料"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.matProp=='c'){

        deleteBtn = '<a href="javascript:deleteStageInMatCertFormById(\''+ row.inId +'\',\''+row.certId + '\',\'cert\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除证照"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.matProp=='f'){

        deleteBtn = '<a href="javascript:deleteStageInMatCertFormById(\''+ row.inId +'\',\''+row.formId + '\',\'form\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除表单"><i class="la la-trash"></i>' +
                    '</a>';

        // inRelItem = '<a href="javascript:void(0);"' +
        //                 'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
        //                 'title="">-' +
        //             '</a>';
    }

    if(curIsEditable){
        return editBtn + inRelItem + deleteBtn;
    }else{
        return editBtn + inRelItem;
    }
}

function globalMatFormatter(value, row, index){

    return '<a href="javascript:viewGlobalMatById(\'' + row.matId + '\')" ' +
                'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                'title="查看"><i class="la la-search"></i>' +
            '</a>';
}

$(function() {

    if(curIsEditable){

        $("#stage_no_state_in_tb").on('click', 'div.c-sort-no', function(event) {
            event.stopPropagation();
            $('input', $(this)).prop("readonly", false).select();
        }).on('blur', 'div.c-sort-no input', function() {
            var oldValue = parseInt($(this).attr('data-oldValue'));
            var newValue = parseInt(this.value);
            var inId = $(this).attr('data-inId');
            if (!newValue) {
                agcloud.ui.metronic.showTip("请输入大于等于0的整数");
                $(this).val(oldValue);
            } else if (oldValue !== newValue) {
                $.post(ctx + "/stageIn/in/saveStageSort", {inId: inId, sortNo: newValue}, function(result) {
                    if (result.success) {
                        $(this).attr("data-old-value", newValue);
                    }
                });
            }
            $(this).prop("readonly", true);
        });
    }else{

        $("#stage_no_state_in_tb").on('click', 'div.c-sort-no', function(event) {
            event.stopPropagation();
            $('input', $(this)).prop("readonly", true);
        });
    }
});

// 库材料查看
function viewGlobalMatById(matId){

    loadGlobalMatData(true,null,matId);
}

// 文件类型
function fileTypeFormatter(value, row, index){

    if(value=='mat'){
        return '材料';
    }else if(value=='cert'){
        return '证照';
    }else if(value=='form'){
        return '表单';
    }
}

function sortNoFormatter(value, row, index) {

    return '<div class="c-sort-no"><i class="fa fa-pencil" aria-hidden="true" ></i><input style="float:left" class="form-control col-sm-2" data-inId="' + row.inId + '" data-oldValue="' + value + '" readonly="readonly" type="text" value="' + value + '"/></div>'
}

$(function(){

    $('#mainContentPanel').css('height',$(document.body).height()-100);

    $('#stage_no_state_in_tb').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    // $('#show_mat_att_tb').bootstrapTable('resetView', {
    //     height: 400
    // });

    $('#stage_no_state_in_tb').on('dbl-click-row.bs.table', function (e, row, element) {

        editStageInMatCertById(row.inId , row.matId , row.fileType);
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

    $("#select_stage_global_mat_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $("#aedit_stage_mat_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#aedit_item_inout_cert_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#select_stage_no_state_in_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#stage_in_item_scroll').niceScroll({

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

    // $('#selectCertTree').niceScroll({
    //
    //     cursorcolor: "#e2e5ec",//#CC0071 光标颜色
    //     cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
    //     cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
    //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //     cursorwidth: "4px", //像素光标的宽度
    //     cursorborder: "0", //   游标边框css定义
    //     cursorborderradius: "2px",//以像素为光标边界半径
    //     autohidemode: true  //是否隐藏滚动条
    // });
    //
    // $('#selectedCertSortDiv').niceScroll({
    //
    //     cursorcolor: "#e2e5ec",//#CC0071 光标颜色
    //     cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
    //     cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
    //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //     cursorwidth: "4px", //像素光标的宽度
    //     cursorborder: "0", //   游标边框css定义
    //     cursorborderradius: "2px",//以像素为光标边界半径
    //     autohidemode: true  //是否隐藏滚动条
    // });

    $('#selectMatTypeTree').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#stage_item_select').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#stage_state_form_list_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#stage_cert_list_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 材料类别选择点击事件绑定
    $('.open-mat-type, input[name="matTypeName"]').bind('click',openSelectMatTypeModal);

    $(".left-button").click(function(e) {
        e.preventDefault();
    });

    $('#select_stage_no_state_in_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',
        function (e, rows) {
            var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
            checkIn(e.type, datas);                                 // 保存到全局 Set() 里
    });

    $('#stage_in_item_table').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',
        function (e, rows) {
            var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
            checkItemIn(e.type, datas);                                 // 保存到全局 Set() 里
    });

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
                maxlength: 500,
                // remote: {
                //     url: ctx + '/aea/item/mat/checkMatName.do', //后台处理程序
                //     type: "post",               //数据发送方式
                //     dataType: "json",           //接受数据格式
                //     data: {   //要传递的数据
                //         matId: function () {
                //             return $("#aedit_stage_mat_form input[name='matId']").val();
                //         },
                //         matName: function () {
                //             return $("#aedit_stage_mat_form input[name='matName']").val();
                //         },
                //         isCommon: function () {
                //             return $("#aedit_stage_mat_form input[name='isCommon']").val();
                //         },
                //     }
                // }
            },
            matCode: {
                required: true,
                maxlength: 200,
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
            },
            reviewKeyPoints:{
                required: true,
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
                required: '<font color="red">材料名称必填！</font>',
                maxlength: "最大长度不能超过500字符!",
                // remote: '材料名称已存在!'
            },
            matCode:{
                required: '<font color="red">材料编号必填！</font>',
                maxlength: '最大长度不能超过200字符',
                remote: '材料编号已存在!'
            },
            reviewKeyPoints:{
                required: '<font color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function () {

            $("#uploadProgress").modal("show");
            $('#saveStageInGlobalMatBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var duePaperCount = $('#aedit_stage_mat_form input[name="duePaperCount"]').val();
            var dueCopyCount = $('#aedit_stage_mat_form input[name="dueCopyCount"]').val();
            if(!duePaperCount){
                $('#aedit_stage_mat_form input[name="duePaperCount"]').val('1');
            }
            if(!dueCopyCount){
                $('#aedit_stage_mat_form input[name="dueCopyCount"]').val('1');
            }

            var formData = new FormData(document.getElementById("aedit_stage_mat_form"));
            $.ajax({
                type: "POST",
                //url: ctx + '/stage/saveMatAndParIn.do',
                url: ctx + '/aea/item/mat/saveMatAndParIn.do',
                dataType: "json",
                cache: false,
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success){

                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 隐藏模式框
                            $('#saveStageInGlobalMatBtn').show();
                            $('#aedit_stage_mat_modal').modal('hide');
                            searchStageNoStateInMatCet();
                        },500);

                    } else {

                        setTimeout(function(){
                            $('#saveStageInGlobalMatBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveStageInGlobalMatBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });

    // $('#save_stage_cert_btn').click(function(){
    //
    //     var rows = $("#stage_cert_list_tb").bootstrapTable('getSelections');
    //     if(rows!=null&&rows.length>0) {
    //         var certIds = [];
    //         for (var i = 0; i < rows.length; i++) {
    //             certIds.push(rows[i].certId);
    //         }
    //         $.ajax({
    //             url: ctx + '/aea/par/in/batchSaveStageNoStateCertInNotDelOld.do',
    //             type: 'POST',
    //             data: {
    //                 'stageId': currentBusiId,
    //                 'certIds': certIds.toString()
    //             },
    //             async: false,
    //             success: function (result) {
    //                 if (result.success) {
    //                     $('#view_stage_cert_modal').modal('hide');
    //                     swal({
    //                         text: '保存成功！',
    //                         type: 'success',
    //                         timer: 1500,
    //                         showConfirmButton: false
    //                     });
    //                     // 刷新材料证照列表
    //                     searchStageNoStateInMatCet();
    //                 } else {
    //                     swal('错误信息', result.message, 'error');
    //                 }
    //             },
    //             error: function (XMLHttpRequest, textStatus, errorThrown) {
    //                 swal('错误信息', XMLHttpRequest.responseText, 'error');
    //             }
    //         });
    //     }else{
    //         swal('提示信息', "请选择数据！", 'info');
    //     }
    // });

    $('#stage_form_list_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // $('#save_stage_form_btn').click(function(){
    //
    //     var rows = $("#stage_form_list_tb").bootstrapTable('getSelections');
    //     if(rows!=null&&rows.length>0) {
    //         var formIds = [];
    //         for (var i = 0; i < rows.length; i++) {
    //             formIds.push(rows[i].formId);
    //         }
    //         $.ajax({
    //             url: ctx + '/aea/par/state/form/saveAeaParStateFormsAndNotDelOld.do',
    //             type: 'POST',
    //             data:  {
    //                 "parStageId": currentBusiId,
    //                 'isStateForm': '0',
    //                 "formIds": formIds.toString(),
    //             },
    //             async: false,
    //             success: function (result) {
    //                 if (result.success) {
    //                     $('#view_stage_form_modal').modal('hide');
    //                     swal({
    //                         text: '保存成功！',
    //                         type: 'success',
    //                         timer: 1500,
    //                         showConfirmButton: false
    //                     });
    //                     // 刷新材料证照列表
    //                     searchStageNoStateInMatCet();
    //                 } else {
    //                     swal('错误信息', result.message, 'error');
    //                 }
    //             },
    //             error: function (XMLHttpRequest, textStatus, errorThrown) {
    //                 swal('错误信息', XMLHttpRequest.responseText, 'error');
    //             }
    //         });
    //     }else{
    //         swal('提示信息', "请选择数据！", 'info');
    //     }
    // });

    // // 选择电子证照
    // $('#selectCertBtn').click(function(){
    //
    //     var certIds = [];
    //     var liObjs = document.getElementsByName('selectCertLi');
    //     if(liObjs!=null&&liObjs.length>0) {
    //         for (var i = 0; i < liObjs.length; i++) {
    //             certIds.push($(liObjs[i]).attr('category-id'));
    //         }
    //     }
    //     $.ajax({
    //         url: ctx + '/aea/par/in/batchSaveStageNoStateCertIn.do',
    //         type: 'POST',
    //         data: {
    //             'stageId': currentBusiId,
    //             'certIds': certIds.toString()
    //         },
    //         async: false,
    //         success: function (result) {
    //             if (result.success) {
    //                 swal({
    //                     text: '保存成功！',
    //                     type: 'success',
    //                     timer: 1500,
    //                     showConfirmButton: false
    //                 });
    //                 closeSelectCetrtZtree();
    //                 // 刷新材料证照列表
    //                searchStageNoStateInMatCet();
    //             }else{
    //                 swal('错误信息', result.message ,'error');
    //             }
    //         },
    //         error: function(XMLHttpRequest, textStatus, errorThrown) {
    //             swal('错误信息', XMLHttpRequest.responseText, 'error');
    //         }
    //     });
    // });

    $('#selectMatTypeBtn').click(function(){

        var matTypeNodes = selectMatTypeTree.getSelectedNodes()
        if(matTypeNodes!=null&matTypeNodes.length>0){
            var matTypeId = matTypeNodes[0].id;
            var matTypeName = matTypeNodes[0].name;
            $('#aedit_stage_mat_form input[name="matTypeId"]').val(matTypeId);
            $('#aedit_stage_mat_form input[name="matTypeName"]').val(matTypeName);
            // 关闭窗口
            closeSelectMatTypeZtree();
        }
    });

    $("#btn_save_stage_state_form_list_tb").click(function (e) {
        var datas = $('#stage_state_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
        examineStateForm(e.type, datas);
        saveStateFormDialogClose();
        saveStageNoStateFormAndReturn();
    });

    // 选择电子证照
    $('#selectCertBtn').bind('click', selectCert);

    // 选择表单
    $('#selectFormBtn').bind('click', selectForm);

    // 选择标准材料
    $('#selectStdmatBtn').bind('click', selectStdmat);

    // 电子证照选择点击事件绑定
    $('.open-cert-type, input[name="certName"]').click(function(){

        var value = $('#aedit_stage_mat_form input[name="certId"]').val();
        openSelectCertModal(value);
    });

    // 表单选择点击事件绑定
    $('.open-form-type, input[name="formName"]').click(function(){

        var value = $('#aedit_stage_mat_form input[name="stoFormId"]').val();
        openSelectFormModal(value);
    });

    // 标准材料选择击事件绑定
    $('.open-stdmat-type, input[name="stdmatName"]').click(function() {

        var value = $('#aedit_stage_mat_form input[name="stdmatId"]').val();
        openSelectStdmatModal(value);
    });

    // 材料类型选择
    $("#aedit_stage_mat_form select[name='matProp']").change(function(){
        var value = $(this).val();
        handleSelectMatProNew(value);
    });

    searchStageNoStateInMatCet();
});

function selectStdmat(){

    var selectStdmatTree = $.fn.zTree.getZTreeObj("selectStdmatTree");
    var stdmats = selectStdmatTree.getCheckedNodes(true);
    if(stdmats!=null&&stdmats.length>0){
        var id = stdmats[0].id;
        var name = stdmats[0].name;
        $('#aedit_stage_mat_form input[name="stdmatId"]').val(id);
        $('#aedit_stage_mat_form input[name="stdmatName"]').val(name);
        // 关闭窗口
        closeSelectStdmatModal();
    }else{
        swal('错误信息', "请选择标准材料！", 'error');
    }
}

function handleSelectMatProNew(value){

    if(value=='m'){

        $('#selectCertDiv').hide();
        $('#selectFormDiv').hide();

        $('#aedit_stage_mat_form input[name="certName"]').rules('remove');
        $('#aedit_stage_mat_form input[name="formName"]').rules('remove');

    }else if(value=='c'){

        $('#selectCertDiv').show();
        $('#selectFormDiv').hide();
        $('#aedit_stage_mat_form input[name="certName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择电子证照！</font>'
            }
        });
        $('#aedit_stage_mat_form input[name="formName"]').rules('remove');

    }else{

        $('#selectCertDiv').hide();
        $('#selectFormDiv').show();
        $('#aedit_stage_mat_form input[name="certName"]').rules('remove');
        $('#aedit_stage_mat_form input[name="formName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择表单！</font>'
            }
        });
    }
}

function selectCert(){

    var selectCertTree = $.fn.zTree.getZTreeObj("selectCertTree");
    var certs = selectCertTree.getCheckedNodes(true);
    if(certs!=null&&certs.length>0){
        var certId = certs[0].id;
        var certName = certs[0].name;
        $('#aedit_stage_mat_form input[name="certId"]').val(certId);
        $('#aedit_stage_mat_form input[name="certName"]').val(certName);
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
        $('#aedit_stage_mat_form input[name="stoFormId"]').val(formId);
        $('#aedit_stage_mat_form input[name="formName"]').val(formName);
        // 关闭窗口
        closeSelectFormModal();
    }else{
        swal('错误信息', "请选择表单！", 'error');
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

// 表单属性，meta-biz表示元数据普通表单，meta-flow表示元数据流程表单，smart-biz表示智能普通表单，smart-flow表示智能流程表单
function formPropertyFormatter(value, row, index, field) {

    if(value){
        if(value=='meta-biz'){
            return '元数据普通表单';
        }else if(value=='meta-flow'){
            return '元数据流程表单';
        }else if(value=='smart-biz'){
            return '智能普通表单';
        }else if(value=='smart-flow'){
            return '智能流程表单';
        }
    }
}

//参数设置
function stageNoStateInParam(params) {

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

function stageNoStateInResponseData(res){
    return res;
}

// 查询
function searchStageNoStateInMatCet(){

    commonQueryParams = [];
    var t = $('#search_stage_no_state_in_form').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    $("#stage_no_state_in_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_no_state_in_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearStageNoStateInMatCet(){

    $('#search_stage_no_state_in_form')[0].reset();
    searchStageNoStateInMatCet();
}

// 刷新
function refreshStageNoStateInMatCet(){

    searchStageNoStateInMatCet();
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

function clearAllFile(){

    $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
    $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
    $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
    $('#templateDocDiv').hide();
    $('#sampleDocDiv').hide();
    $('#reviewSampleDocDiv').hide();
}

// 新增阶段不分情形材料
function addStageNoStateInMat(){

    if(curIsEditable){

        $('#aedit_stage_mat_modal').modal('show');
        $('#aedit_stage_mat_modal_title').html('新增材料');
        $('#aedit_stage_mat_form')[0].reset();
        aedit_stage_mat_validator.resetForm();
        $('#aedit_stage_mat_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        clearAllFile();
        $("#aedit_stage_mat_form input[name='matId']").val('');
        $("#aedit_stage_mat_form input[name='stageId']").val(currentBusiId);
        $("#aedit_stage_mat_form input[name='isStateIn']").val('0');
        $("#aedit_stage_mat_form input[name='isActive']").val('1');
        $("#aedit_stage_mat_form input[name='isGlobalShare']").val('1');
        $("#aedit_stage_mat_form input[name='duePaperCount']").val("1");
        $("#aedit_stage_mat_form input[name='dueCopyCount']").val("1");
        $("#aedit_stage_mat_form input[name='paperIsRequire'][value='0']").prop("checked", true);
        $("#aedit_stage_mat_form input[name='attIsRequire'][value='0']").prop("checked", true);
        $("#aedit_stage_mat_form input[name='isCommon'][value='1']").prop("checked", true); // 默认通用材料
        $("#aedit_stage_mat_form input[name='zcqy'][value='1']").prop("checked", true);  // 默认支持容缺
        $("#aedit_stage_mat_form input[name='isOfficialDoc'][value='0']").prop("checked", true);  // 是否为批文批复
        $("#aedit_stage_mat_form input[name='matProp'][value='m']").prop("checked", true);
        $("#aedit_stage_mat_form input[name='certId']").val('');
        $("#aedit_stage_mat_form input[name='stoFormId']").val('');
        $("#aedit_item_inout_mat_form input[name='stdmatId']").val('');
        handleSelectMatProNew('m');

        // 编号赋值
        $.ajax({
            url: ctx + '/bsc/rule/code/getOneOrgAutoCode.do',
            type: 'post',
            data: {'codeIc': "AEA-ITEM-MAT-CODE"},
            async: false,
            cache: false,
            success: function (data) {
                $("#aedit_stage_mat_form input[name='matCode']").val(data);
            }
        });
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// 导入阶段库材料
function addStageNoStateInGlobalMat(){

    if(curIsEditable){
        $('#select_stage_global_mat_modal').modal('show');
        $('#select_stage_global_mat_modal_title').html('导入库材料');
        // searchStageGlobalMat();
        clearSearchStageGlobalMat();
    }else{
        swal('提示信息','当前版本下数据不可编辑!','info');
    }
}

// 增加证照导入
function addStageNoStateInCert(){

    if(curIsEditable) {
        // loadStageNoStateInRelCerts(currentBusiId);
        $('#view_stage_cert_modal').modal('show');
        clearSearchStageCert();
    }else{
        swal('提示信息','当前版本下数据不可编辑!','info');
    }
}

function addStageNoStateInForm(){

    if(curIsEditable) {
        // viewStateForm(currentBusiId, '', 'stage');
        $('#view_stage_form_modal').modal('show');
        clearSearchStageForm2();
    }else{
        swal('提示信息','当前版本下数据不可编辑!','info');
    }
}

// 需要设置情形数据
function loadStageNoStateInRelCerts(stageId){

    if(stageId){

        $('#selectCertKeyWord').val('');

        // 打开弹窗，加载树数据
        openSelectCertModal();

        // 取消上次的选中节点
        selectCertTree.checkAllNodes(false);

        // 清空右侧选中事项数据
        $("#selectedCertUl").html("");

        //加载已选择数据
        loadSelectedStageNoStateInRelCertData(stageId);

    }else{
        swal('提示信息','参数stageId为空!','info');
    }
}

function loadSelectedStageNoStateInRelCertData(stageId){

    // 勾选和渲染已经选择的电子证照
    $.ajax({
        url: ctx + '/aea/par/in/listCertListByStageId.do',
        type: 'post',
        data: {'stageId': stageId,},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    // 选择电子证照库树节点
                    var node = selectCertTree.getNodeByParam("id", data[i], null);
                    if (node) {
                        selectCertTree.checkNode(node, true, true, false);
                        // 加载右侧数据，已经选择的电子证照库
                        var liHtml = '<li name="selectCertLi" category-id="' + data[i] + '">' +
                                        '<span class="drag-handle_td" ' +
                                        'onclick="removeSelectedCert(\'' + data[i] + '\');">×</span>' +
                                        '<span class="org_name_td">' + node.name + '</span>' +
                                    '</li>';
                        $('#selectedCertUl').append(liHtml);
                    }
                }
            }
        }
    });
}

// 批量阶段删除材料证照
function batchDeleteStageNoStateInMatCertForm(){

    if(curIsEditable){
        var rows = $("#stage_no_state_in_tb").bootstrapTable('getSelections');
        var inIds = [];
        var fileTypes = [];
        if(rows!=null&&rows.length>0){
            for(var i=0;i<rows.length;i++){
                inIds.push(rows[i].inId);
                fileTypes.push(rows[i].fileType);
            }
            swal({
                title: '提示信息',
                text: '此操作将批量删除与材料、证照以及表单关联数据,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    if (inIds!=null&&inIds.length>0&&fileTypes!=null&&fileTypes.length>0) {
                        $.ajax({
                            url: ctx + '/aea/par/in/batchDelMatCertFormByInIdsAndTypes.do',
                            type: 'POST',
                            data: {'ids': inIds.toString(), 'fileTypes': fileTypes.toString()},
                            success: function (result) {
                                swal({
                                    title: '提示信息',
                                    text: '删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 刷新表格
                                searchStageNoStateInMatCet();
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }
                }
            });
        }else{
            swal('提示信息', '请选择操作数据！', 'info');
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑！', 'info');
    }
}

function editStageInMatCertById(inId, matCertId, fileType){

    if(inId&&matCertId){

        if(fileType=='mat'){

            loadGlobalMatData(curIsEditable?false:true, inId, matCertId);

        }else if(fileType=='cert'){

            // 加载展示数据
            $('#aedit_item_inout_cert_modal').modal('show');
            $('#aedit_item_inout_cert_modal_title').html('查看证照');
            $('#aedit_item_inout_cert_form')[0].reset();
            // aedit_item_inout_cert_validator.resetForm();
            $("#aedit_item_inout_cert_form input[name='certId']").val('');
            $("#aedit_item_inout_cert_form input[name='certTypeId']").val('');
            $.ajax({
                url: ctx + '/aea/cert/getAeaCert.do',
                type: 'post',
                data: {'id': matCertId},
                async: false,
                success: function (data) {
                    if (data) {
                        loadFormData(true,'#aedit_item_inout_cert_form',data);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    }
}

/**
 * 获取材料数据
 * @param isView 是否查看
 * @param inId
 * @param matId  材料或者电子证照id
 */
function loadGlobalMatData(isView,inId,matId){

    $('#aedit_stage_mat_modal').modal('show');
    $('#aedit_stage_mat_form')[0].reset();
    aedit_stage_mat_validator.resetForm();
    $('#aedit_stage_mat_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $("#aedit_stage_mat_form input[name='matId']").val('');
    $("#aedit_stage_mat_form input[name='inId']").val('');
    $("#aedit_stage_mat_form input[name='stageId']").val('');
    $("#aedit_stage_mat_form input[name='certId']").val('');
    $("#aedit_stage_mat_form input[name='stoFormId']").val('');
    $("#aedit_stage_mat_form input[name='stdmatId']").val('');
    clearAllFile();

    if(isView){

        $('#aedit_stage_mat_modal_title').html('查看材料');
        $('#saveStageInGlobalMatBtn').hide();

    }else{

        $('#aedit_stage_mat_modal_title').html('编辑材料');
        $('#saveStageInGlobalMatBtn').show();
        // $("#aedit_stage_mat_form input[name='inId']").val(inId);
        $("#aedit_stage_mat_form input[name='stageId']").val(currentBusiId);
    }

    $.ajax({
        url: MAT_URL_PREFIX + 'getAeaItemMat.do',
        type: 'post',
        data: {'id': matId},
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
                loadFormData(true,'#aedit_stage_mat_form',data);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

function checkboxOnclick(checkbox){

    obj = document.getElementsByName("matFromCb");
    check_val = [];
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    var cb = check_val.join(',');
    $('#matFrom').val(cb);
}

function deleteStageInMatCertFormById(inId, matCertFormId, fileType){

    if(inId && matCertFormId && fileType){
        var  url = ctx+'/aea/par/in/deleteAeaParInById.do';
        var  title = '';
        if(fileType=='mat'){
            title = '材料';
        }else if(fileType=='cert'){
            title = '证照';
        }else if(fileType=='form'){
            title = '表单';
            // url = ctx+'/aea/par/state/form/delAeaParStateFormById.do';
        }
        swal({
            title: '提示信息',
            text: '此操作将删除与此'+ title +'关联数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data:{'id': inId},
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
                            searchStageNoStateInMatCet();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

//参数设置
function selectStageGlobalMatParam(params) {

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

function selectStageGlobalMatResponseData(res){
    return res;
}

// 查询
function searchStageGlobalMat(){

    commonQueryParams = [];
    var t = $('#search_state_global_mat_form').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    $("#select_stage_global_mat_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

// 清空
function clearSearchStageGlobalMat(){

    $('#search_state_global_mat_form')[0].reset();
    searchStageGlobalMat();
}

// 保存阶段选择的全局材料数据
function saveStageInGlobalMat(){

    if(currentBusiId){
        var rows = $("#select_stage_global_mat_tb").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0){
            var matIds = [];
            for(var i=0;i<rows.length;i++){
                matIds.push(rows[i].matId);
            }
            $.ajax({
                url: ctx + '/aea/par/in/batchSaveStageNoStateMatIn.do',
                type: 'POST',
                data: {
                    'stageId': currentBusiId,
                    'matIds': matIds.toString()
                },
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            text: '保存成功！',
                            type: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        $('#select_stage_global_mat_modal').modal('hide');
                        // 刷新材料证照列表
                        searchStageNoStateInMatCet();
                    }else{
                        swal('错误信息', result.message ,'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else{
            swal('提示信息', "请选勾选材料！", 'info');
        }
    } else{
        swal('提示信息', "阶段id为空！", 'info');
    }
}

function matAttTbParam(params){

    // var pageNum = (params.offset / params.limit) + 1;
    // var pagination = {
    //     page: pageNum,
    //     pages: pageNum,
    //     perpage: params.limit
    // };
    // var sort = {
    //     field: params.sort,
    //     sort: params.order
    // };
    // var queryParam = {
    //     pagination: pagination,
    //     sort: sort
    // };
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

function searchMatAttSto(type) {

    var matId = $('#aedit_stage_mat_form input[name="matId"]').val();
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

function viewAttNameFormatter(value, row, index){

    // 图片
    if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"){

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

function showDownloadFile(url){

    window.open(url,"展示图片");
}

function showImgFile(detailId){

    window.open(MAT_URL_PREFIX + 'showFile.do?detailId='+ detailId,"展示图片");
}

function attOperFormatter(value, row, index){

    if(curIsEditable){
        if(row.attType!='KP'){
            var deleteBtn = '<a href="javascript:deleteMatAttrByDetailId(\'' + row.detailId + '\')" ' +
                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                'title="删除"><i class="la la-trash"></i>' +
                '</a>';
            return deleteBtn;
        }
    }
}

function isOptionItemFormatter(value, row, index, field) {

    // if(value){
    //     if(value=='0'){
    //         return '必选事项';
    //     }else if(value=='1'){
    //         return '可选事项';
    //     }
    // }

    if(value){
        if(value=='0'){
            return '并联审批事项';
        }else if(value=='1'){
            return '并行推进事项';
        }else{
            return '前置检查事项';
        }
    }
}

function itemOrgNameFormatter(value, row, index, field) {

    var itemName = row.itemName;
    if(!isEmpty(row.isCatalog)){
        if(row.isCatalog=='1'){
            itemName = '【标准事项】'+ itemName;
            if(!isEmpty(row.guideOrgName)){
                itemName = itemName +'【'+ row.guideOrgName+'】';
            }
        }else{
            itemName = '【实施事项】'+ itemName;
            if(!isEmpty(row.orgName)) {
                itemName = itemName + '【' + row.orgName + '】';
            }
        }
    }
    return itemName;
}

function viewAttSizeFormatter(value, row, index){

    if(value){
        return value/1000;
    }
}

function deleteMatAttrByDetailId(id){

    if(id){
        var matId = $('#aedit_stage_mat_form input[name="matId"]').val();
        var inId =  $('#aedit_stage_mat_form input[name="inId"]').val();
        var msg = '此操作将删除附件,确定要删除吗？';
        swal({
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
                            loadGlobalMatData(false,inId,matId);
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

// 设置材料证照事项
var inId = "";
var isItemInCheck = [];
function editItemParInByInId(id) {

    inId = id;
    isItemInCheck = [];
    $('#stage_in_item_modal').modal('show');
    $('#stage_in_item_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    if(curIsEditable){
        $('#saveStageInItemBtn').show();
    }else{
        $('#saveStageInItemBtn').hide();
    }
    searchStageInItem();
}

function stageInItemParam(params){

    //组装查询参数
    var queryParam = {};
    var buildParam = {};
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function stageInItemResponseData(res){
   return res;
}

function searchStageInItem() {

    commonQueryParams = [];
    commonQueryParams.push({name: "stageId",value: currentBusiId});
    commonQueryParams.push({name: "isOptionItem",value: '0'});
    commonQueryParams.push({name: "inId",value: inId});
    $("#stage_in_item_table").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_in_item_table").bootstrapTable('refresh');       //无参数刷新
}

function isCheckFormatter(value, row, index){

    if(row.isCheck == true){
        isItemInCheck.push(row.stageItemId);
        return {
            checked : true//设置选中
        };
    }
    return value;
}

function saveStageInItem(){

    var checkBoxs = $("#stage_in_item_table").bootstrapTable('getSelections');
    var ids = [];
    if(checkBoxs!=null&&checkBoxs.length>0) {
        for (var i = 0; i < checkBoxs.length; i++) {
            ids.push(checkBoxs[i].stageItemId);
        }
    }
        $.ajax({
            url: ctx + '/aea/par/stage/item/in/batchSaveStageItemIn.do',
            type: 'POST',
            data: {
                'stageItemIds': ids.toString(),
                'inId': inId
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    $('#stage_in_item_modal').modal('hide');
                    agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                    searchStageNoStateInMatCet();
                } else {
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function () {
                swal('错误信息', '保存失败！', 'error');
            }
        });
}

var allCheckedId = [];
function addStageNoStateIn(){

    if(curIsEditable){
        allCheckedId = [];
        $("#aedit_stage_item_modal").modal("show");
        $('#select_stage_no_state_in_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        // 加载阶段事项
        loadStageItem(currentBusiId);
        // 展示材料证照列表
        clearSelectStageNoStateInMatCet();
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

function loadStageItem(stageId){

    $("#stage_item_select").html("");
    $.ajax({
        url: ctx + '/aea/par/stage/item/listAeaParStageItemByNoPage.do',
        type: 'POST',
        data: {
            'stageId': stageId,
            // 'isOptionItem': '0'
        },
        async: false,
        success: function (data) {
            if (data&&data.length>0) {
                var options = "";
                for (var i = 0; i < data.length; i++) {
                    var value = data[i].isOptionItem;
                    var title = '并联审批事项：';
                    if(value=='0'){
                        title = '并联审批事项：';
                    }else if(value=='1'){
                        title = '并行推进事项：';
                    }else{
                        title = '前置检查事项：';
                    }
                    var itemName = data[i].itemName;
                    if(!isEmpty(data[i].isCatalog)){
                        if(data[i].isCatalog=='1'){
                            itemName = '【标准事项】'+ itemName;
                            if(!isEmpty(data[i].guideOrgName)){
                                itemName = itemName +'【'+ data[i].guideOrgName+'】';
                            }
                        }else{
                            itemName = '【实施事项】'+ itemName;
                            if(!isEmpty(data[i].orgName)) {
                                itemName = itemName + '【' + data[i].orgName + '】';
                            }
                        }
                    }
                    options += "<option value ='" + data[i].stageItemId + "'>" + title + itemName + "</option>";
                }
                $("#stage_item_select").html(options);
            }
        },
        error: function () {
            swal('错误信息', '获取阶段事项失败！', 'error');
        }
    });
}

//判断字符是否为空的方法
function isEmpty(obj){

    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

//参数设置
function selectStageNoStateInParam(params) {

    var queryParam = {};
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

function selectStageNoStateInResponseData(res){
    return res;
}

function isCheckItemInMatCertFormatter(value, row, index){

    if(row.check == true){
        allCheckedId.push(row.inId);
        return {
            checked : true//设置选中
        };
    }
    return value;
}

function searchSelectStageNoStateInMatCet(){

    commonQueryParams = [];
    var t = $('#search_select_stage_no_state_in_form').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    commonQueryParams.push({'name': 'stageItemId','value': $("#stage_item_select option:selected").val()});
    $("#select_stage_no_state_in_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#select_stage_no_state_in_tb").bootstrapTable('refresh');       //无参数刷
}

function clearSelectStageNoStateInMatCet() {

    $('#search_select_stage_no_state_in_form')[0].reset();
    searchSelectStageNoStateInMatCet();
}

function changeStageItem(){

    clearSelectStageNoStateInMatCet();
}

//保存材料和事项的关系
function saveStateItemInIds() {

    // alert(allCheckedId.length);

    var stageItemId = $("#stage_item_select").val();
    if(stageItemId==null || stageItemId=='' || stageItemId==undefined){
        swal('提示信息', '请选择事项！', 'error');
        return;
    }
    var rows = $('#select_stage_no_state_in_tb').bootstrapTable('getSelections');
    var ids = [];
    if(rows!=null&&rows.length>0) {
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].inId);
        }
    }
    // if(allCheckedId!=null&&allCheckedId.length>0) {
    //     for (var i = 0; i < allCheckedId.length; i++) {
    //         ids.push(allCheckedId[i]);
    //     }
    // }

    $.ajax({
        url: ctx + '/aea/par/stage/item/in/batchSaveStageItemInByStageItemId.do',
        type: 'POST',
        data: {
            "stageItemId": stageItemId,
            "inIds": ids.toString()
        },
        async: false,
        success: function (result) {
            if (result.success) {
                $("#aedit_stage_item_modal").modal("hide");
                agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                clearStageNoStateInMatCet();
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function () {
            swal('错误信息', '保存失败！', 'error');
        }
    });
}

function checkIn(type, datas) {

    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            if(allCheckedId.indexOf(v.inId) == -1){
                allCheckedId.push(v.inId);
            }
        });
    }else{
        $.each(datas,function(i,v){
            var j = allCheckedId.indexOf(v.inId);
            allCheckedId.splice(j,1);    //删除取消选中行
        });
    }
}

function checkItemIn(type, datas) {

    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            if(isItemInCheck.indexOf(v.stageItemId) == -1){
                isItemInCheck.push(v.stageItemId);
            }
        });
    }else{
        $.each(datas,function(i,v){
            var j = isItemInCheck.indexOf(v.stageItemId);
            isItemInCheck.splice(j,1);    //删除取消选中行
        });
    }
}

//参数设置
function stageCertParam(params) {

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

function stageCertResponseData(res){
    return res;
}

// 查询
function searchStageCert(){

    commonQueryParams = [];
    commonQueryParams.push({'name': 'stageId','value': currentBusiId});
    commonQueryParams.push({'name': 'keyword','value': $('#stageCertKeyword').val()});
    //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_cert_list_tb").bootstrapTable('selectPage',1);
}

// 清空
function clearSearchStageCert(){

    $('#stageCertKeyword').val('')
    searchStageCert();
}

//参数设置
function stageFormParam2(params) {

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

function stageFormResponseData2(res){
    return res;
}

// 查询
function searchStageForm2(){

    commonQueryParams = [];
    commonQueryParams.push({'name': 'parStageId','value': currentBusiId});
    commonQueryParams.push({'name': 'keyword','value': $('#stageFormKeyword').val()});
    //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_form_list_tb").bootstrapTable('selectPage',1);
}

// 清空
function clearSearchStageForm2(){

    $('#stageFormKeyword').val('')
    searchStageForm2();
}


