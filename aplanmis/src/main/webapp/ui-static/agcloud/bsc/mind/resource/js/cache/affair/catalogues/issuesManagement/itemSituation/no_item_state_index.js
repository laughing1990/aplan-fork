var commonQueryParams = [];
var aedit_item_inout_mat_validator = null;
var aedit_item_inout_cert_validator = null;
var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
function itemInMatCertFormatter(value, row, index) {

    var editBtn = null;
    var deleteBtn = null;

    var title = '编辑';
    var icoCss = 'la la-edit';
    if(!curIsEditable){
        title = '查看';
        icoCss = 'la la-search';
    }

    editBtn = '<a href="javascript:editItemInMatCertById(\''+ row.inoutId +'\',\'' + row.matId + '\',\'mat\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="'+ title +'"><i class="'+ icoCss +'"></i>' +
        '</a>';

    if(row.matProp=='m'){

        deleteBtn = '<a href="javascript:deleteItemInMatCertFormById(\''+ row.inoutId +'\',\''+row.matId + '\',\'mat\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.matProp=='c'){

        // editBtn = '<a href="javascript:editItemInMatCertById(\''+ row.inoutId +'\',\'' + row.certId + '\',\'cert\')" ' +
        //              'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
        //              'title="查看"><i class="la la-search"></i>' +
        //           '</a>';

        deleteBtn = '<a href="javascript:deleteItemInMatCertFormById(\''+ row.inoutId +'\',\''+row.certId + '\',\'cert\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.matProp=='f'){

        // editBtn = '<a href="javascript:void(0);"' +
        //               'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
        //               'title="">-' +
        //           '</a>';

        deleteBtn = '<a href="javascript:deleteItemInMatCertFormById(\''+ row.inoutId +'\',\''+row.formId + '\',\'form\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除表单"><i class="la la-trash"></i>' +
                    '</a>';
    }

    if(curIsEditable){
        return editBtn + deleteBtn;
    }else{
        return editBtn;
    }
}

function globalMatFormatter(value, row, index){

    return '<a href="javascript:viewGlobalMatById(\'' + row.matId + '\')" ' +
              'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
              'title="查看"><i class="la la-search"></i>' +
           '</a>';
}

// 库材料查看
function viewGlobalMatById(matId){

    loadGlobalMatData(true,null,matId);
}

function fileTypeFormatter(value, row, index){

    if(value=='mat'){
        return '材料';
    }else if(value=='cert'){
        return '证照';
    }else if(value=='form'){
        return '表单';
    }
}

/**
 * 格式化是否是情形材料
 *
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function isCommonFormatter(value,row,index){

    return value=="1"?"通用材料":"情形材料";
}

function sortNoFormatter(value, row, index) {

    return '<div class="c-sort-no"><i class="fa fa-pencil" aria-hidden="true" ></i><input style="float:left" class="form-control col-sm-2" data-inoutId="' + row.inoutId + '" data-oldValue="' + value + '" readonly="readonly" type="text" value="' + value + '"/></div>'
}

$(function() {

    if(curIsEditable){
        $("#item_in_mat_cert_tb").on('click', 'div.c-sort-no', function(event) {
            event.stopPropagation();
            $('input', $(this)).prop("readonly", false).select();
        }).on('blur', 'div.c-sort-no input', function() {
            var oldValue = parseInt($(this).attr('data-oldValue'));
            var newValue = parseInt(this.value);
            var inoutId = $(this).attr('data-inoutId');
            if (!newValue) {
                agcloud.ui.metronic.showTip("请输入大于等于0的整数");
                $(this).val(oldValue);
            } else if (oldValue !== newValue) {
                $.post(ctx + "/item/inout/edit/sortno", {inoutId: inoutId, newSortNo: newValue}, function(result) {
                    if (result.success) {
                        $(this).attr("data-old-value", newValue);
                    }
                });
            }
            $(this).prop("readonly", true);
        });
    }else{
        $("#item_in_mat_cert_tb").on('click', 'div.c-sort-no', function(event) {
            event.stopPropagation();
            $('input', $(this)).prop("readonly", true);
        });
    }
});

//参数设置
function itemInMatCertParam(params) {

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

function itemInMatCertResponseData(res) {
    return res;
}

// 查询
function searchItemInMatCert(){

    commonQueryParams = [];
    var t = $('#search_item_in_mat_cert').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    commonQueryParams.push({'name': 'itemVerId','value': currentBusiId});
    commonQueryParams.push({'name': 'stateVerId','value': currentStateVerId});
    commonQueryParams.push({'name': 'isInput','value': '1'});
    $("#item_in_mat_cert_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#item_in_mat_cert_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchItemInMatCert(){

    $('#search_item_in_mat_cert')[0].reset();
    searchItemInMatCert();
}

// 刷新
function refreshItemInMatCet(){

    searchItemInMatCert();
}

$(function(){

    $('#mainContentPanel').css('height',$(document.body).height()-100);

    $('#item_in_mat_cert_tb').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    // $('#show_mat_att_tb').bootstrapTable('resetView', {
    //     height: 400
    // });

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

    $('#item_in_mat_cert_tb').on('dbl-click-row.bs.table', function (e, row, element) {

        editItemInMatCertById(row.inoutId , row.matId , row.fileType);
    });

    $("#aedit_item_inout_mat_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $("#select_item_inout_global_mat_scroll").niceScroll({

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

    $('#item_cert_list_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#item_form_list_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // $('#save_item_cert_btn').click(function(){
    //
    //     var rows = $("#item_cert_list_tb").bootstrapTable('getSelections');
    //     if(rows!=null&&rows.length>0) {
    //         var matCertIds = [];
    //         for (var i = 0; i < rows.length; i++) {
    //             matCertIds.push(rows[i].certId);
    //         }
    //         $.ajax({
    //             url: ctx + '/aea/item/inout/batchSaveItemInoutMatCertAndNotDelOld.do',
    //             type: 'POST',
    //             data: {
    //                 "itemVerId": currentBusiId,
    //                 "stateVerId": currentStateVerId,
    //                 'isInput': '1',
    //                 'isStateIn':'0',
    //                 'fileType': 'cert',
    //                 'matCertIds': matCertIds.toString(),
    //             },
    //             async: false,
    //             success: function (result) {
    //                 if (result.success) {
    //                     $("#view_item_cert_modal").modal("hide");
    //                     swal({
    //                         text: '保存成功！',
    //                         type: 'success',
    //                         timer: 1500,
    //                         showConfirmButton: false
    //                     });
    //                     // 刷新材料证照表单列表
    //                     searchItemInMatCert();
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

    // 材料类别选择点击事件绑定
    $('.open-mat-typ, input[name="matTypeName"]').bind('click',openSelectMatTypeModal);

    $(".left-button").click(function(e) {
        e.preventDefault();
    });

    aedit_item_inout_mat_validator = $("#aedit_item_inout_mat_form").validate({
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
                remote: {
                    url: ctx + '/aea/item/mat/checkMatName.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matId: function () {
                            return $("#aedit_item_inout_mat_form input[name='matId']").val();
                        },
                        matName: function () {
                            return $("#aedit_item_inout_mat_form input[name='matName']").val();
                        },
                        isCommon: function () {
                            return $("#aedit_item_inout_mat_form input[name='isCommon']").val();
                        },
                    }
                }
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
                            return $("#aedit_item_inout_mat_form input[name='matId']").val();
                        },
                        matCode: function() {
                            return $("#aedit_item_inout_mat_form input[name='matCode']").val();
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
                remote: '材料名称已存在!'
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
            var url;
            if (!$('input[name="matId"]').val()) {
                url = ctx + '/mat/controller/add/one';
            } else {
                url = ctx + '/mat/controller/edit/one'
            }
            $('input[name="itemVerId"]').val(currentBusiId);
            $('input[name="stateVerId"]').val(currentStateVerId);
            $('input[name="isStateIn"]').val('0');
            $('input[name="isOwner"]').val('1');
            $('input[name="fileType"]').val('mat');
            $('input[name="isActive"]').val('1');
            $('input[name="isGlobalShare"]').val('1');

            var duePaperCount = $('#aedit_item_inout_mat_form input[name="duePaperCount"]').val();
            var dueCopyCount = $('#aedit_item_inout_mat_form input[name="dueCopyCount"]').val();
            if(!duePaperCount){
                $('#aedit_item_inout_mat_form input[name="duePaperCount"]').val('1');
            }
            if(!dueCopyCount){
                $('#aedit_item_inout_mat_form input[name="dueCopyCount"]').val('1');
            }

            $("#uploadProgress").modal("show");
            $('#saveItemOutGlobalMatBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var formData = new FormData(document.getElementById("aedit_item_inout_mat_form"));
            $.ajax({
                type: "POST",
                url: url,
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
                            $('#saveItemOutGlobalMatBtn').show();
                            $('#aedit_item_inout_mat_modal').modal('hide');
                            // 列表数据重新加载
                            searchItemInMatCert();
                        },500);
                    } else {

                        setTimeout(function(){
                            $('#saveItemOutGlobalMatBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){
                        $('#saveItemOutGlobalMatBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });

    aedit_item_inout_cert_validator = $("#aedit_item_inout_cert_form").validate({

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
                            return $("#aedit_item_inout_cert_form input[name='certId']").val();
                        },
                        certCode: function() {
                            return $("#aedit_item_inout_cert_form input[name='certCode']").val();
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
            var t = $('#aedit_item_inout_cert_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/item/inout/saveAeaCert.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success) {
                        $('#aedit_stage_cert_modal').modal('hide');
                        searchItemInMatCert();
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });

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
    //         url: ctx + '/aea/item/inout/batchSaveItemInoutMatCert.do',
    //         type: 'POST',
    //         data: {
    //             'itemVerId': currentBusiId,
    //             'stateVerId':currentStateVerId,
    //             'isInput': '1',
    //             'isStateIn':'0',
    //             'fileType': 'cert',
    //             'matCertIds': certIds.toString(),
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
    //                 searchItemInMatCert();
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
            $('#aedit_item_inout_mat_form input[name="matTypeId"]').val(matTypeId);
            $('#aedit_item_inout_mat_form input[name="matTypeName"]').val(matTypeName);
            // 关闭窗口
            closeSelectMatTypeZtree();
        }
    });

    $("#btn_save_item_form_list_tb").unbind("click")
    $("#btn_save_item_form_list_tb").click(function (e) {
        var datas = $('#item_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
        examineItemForm(e.type, datas);
        saveItemNoStateFormAndReturn();
    });

    // 选择电子证照
    $('#selectCertBtn').bind('click', selectCert);

    // 选择表单
    $('#selectFormBtn').bind('click', selectForm);

    // 电子证照选择点击事件绑定
    $('.open-cert-type, input[name="certName"]').click(function(){

        var value = $('#aedit_item_inout_mat_form input[name="certId"]').val();
        openSelectCertModal(value);
    });

    // 表单选择点击事件绑定
    $('.open-form-type, input[name="formName"]').click(function(){

        var value = $('#aedit_item_inout_mat_form input[name="stoFormId"]').val();
        openSelectFormModal(value);
    });

    // 材料类型选择
    $("#aedit_item_inout_mat_form select[name='matProp']").change(function(){
        var value = $(this).val();
        handleSelectMatProNew(value);
    });

    searchItemInMatCert();
});

function handleSelectMatProNew(value){

    if(value=='m'){

        $('#selectCertDiv').hide();
        $('#selectFormDiv').hide();

        $('#aedit_item_inout_mat_form input[name="certName"]').rules('remove');
        $('#aedit_item_inout_mat_form input[name="formName"]').rules('remove');

    }else if(value=='c'){

        $('#selectCertDiv').show();
        $('#selectFormDiv').hide();
        $('#aedit_item_inout_mat_form input[name="certName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择电子证照！</font>'
            }
        });
        $('#aedit_item_inout_mat_form input[name="formName"]').rules('remove');

    }else{

        $('#selectCertDiv').hide();
        $('#selectFormDiv').show();
        $('#aedit_item_inout_mat_form input[name="certName"]').rules('remove');
        $('#aedit_item_inout_mat_form input[name="formName"]').rules('add',{
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
        $('#aedit_item_inout_mat_form input[name="certId"]').val(certId);
        $('#aedit_item_inout_mat_form input[name="certName"]').val(certName);
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
        $('#aedit_item_inout_mat_form input[name="stoFormId"]').val(formId);
        $('#aedit_item_inout_mat_form input[name="formName"]').val(formName);
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

function addItemInMat(){
    
    if(curIsEditable){

        $('#aedit_item_inout_mat_modal').modal('show');
        $('#aedit_item_inout_mat_modal_title').html('新增材料');
        $('#saveItemOutGlobalMatBtn').show();
        $('#aedit_item_inout_mat_form')[0].reset();
        aedit_item_inout_mat_validator.resetForm();
        $('#aedit_item_inout_mat_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        clearAllFile();
        $("#aedit_item_inout_mat_form input[name='inoutId']").val('');
        $("#aedit_item_inout_mat_form input[name='isStateIn']").val('0');
        $("#aedit_item_inout_mat_form input[name='matId']").val('');
        $("#aedit_item_inout_mat_form input[name='isOwner']").val('1');
        $("#aedit_item_inout_mat_form input[name='itemVerId']").val(currentBusiId);
        $("#aedit_item_inout_mat_form input[name='isInput']").val('1');
        $("#aedit_item_inout_mat_form input[name='fileType']").val('mat');
        $("#aedit_item_inout_mat_form input[name='isActive']").val('1');
        $("#aedit_item_inout_mat_form input[name='isGlobalShare']").val('1');
        $("#aedit_item_inout_mat_form input[name='duePaperCount']").val("1");
        $("#aedit_item_inout_mat_form input[name='dueCopyCount']").val("1");
        $("#aedit_item_inout_mat_form input[name='paperIsRequire'][value='0']").prop("checked", true);
        $("#aedit_item_inout_mat_form input[name='attIsRequire'][value='0']").prop("checked", true);
        $("#aedit_item_inout_mat_form input[name='isCommon'][value='1']").prop("checked", true); // 默认通用材料
        $("#aedit_item_inout_mat_form input[name='zcqy'][value='1']").prop("checked", true);  // 默认支持容缺
        $("#aedit_item_inout_mat_form input[name='isOfficialDoc'][value='0']").prop("checked", true);  // 是否为批文批复
        $("#aedit_item_inout_mat_form input[name='certId']").val('');
        $("#aedit_item_inout_mat_form input[name='stoFormId']").val('');
        $("#aedit_item_inout_mat_form input[name='matProp'][value='m']").prop("checked", true);
        handleSelectMatProNew('m');

        // 编号赋值
        $.ajax({
            url: ctx + '/bsc/rule/code/getOneOrgAutoCode.do',
            type: 'post',
            data: {'codeIc': "AEA-ITEM-MAT-CODE"},
            async: false,
            cache: false,
            success: function (data) {
                $("#aedit_item_inout_mat_form input[name='matCode']").val(data);
            }
        });
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// 导入阶段库材料
function addItemInGlobalMat(){

    if(curIsEditable){
        $('#select_item_inout_global_mat_modal').modal('show');
        $('#select_item_inout_global_mat_modal_title').html('导入库材料');
        searchItemInoutGlobalMat();
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// 增加证照导入
function addItemInCert(){

    if(curIsEditable) {
        $('#view_item_cert_modal').modal('show');
        clearSearchItemCert();
    }else{
        swal('提示信息','当前版本下数据不可编辑!','info');
    }
}

function addItemInForm(){

    if(curIsEditable){
        // vieItemForm(null, "item", false);
        $('#view_item_form_modal').modal('show');
        clearSearchItemForm();
    }else{
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

// // 需要设置情形数据
// function loadItemInRelCerts(){
//
//     // if(itemId){
//
//         $('#selectCertKeyWord').val('');
//
//         // 打开弹窗，加载树数据
//         openSelectCertModal();
//
//         // 取消上次的选中节点
//         selectCertTree.checkAllNodes(false);
//
//         // 取消上次的选中节点
//         selectCertTree.cancelSelectedNode();
//
//         // 清空右侧选中事项数据
//         $("#selectedCertUl").html("");
//
//         //加载已选择数据
//         loadSelectedItemInRelCertData();
//
//     // }else{
//     //     swal('提示信息','事项id为空!','info');
//     // }
// }

// function loadSelectedItemInRelCertData(){
//
//     // 勾选和渲染已经选择的电子证照
//     $.ajax({
//         url: ctx + '/aea/item/inout/listAeaItemInoutNoPage.do',
//         type: 'post',
//         data: {
//             'itemVerId': currentBusiId,
//             'stateVerId': currentStateVerId,
//             'isInput':'1',
//             'fileType': 'cert',
//         },
//         async: false,
//         success: function (data) {
//             if(data!=null&&data.length>0){
//                 for(var i=0;i<data.length;i++) {
//                     if(data[i].fileType=='cert'){
//                         // 选择电子证照库树节点
//                         var node = selectCertTree.getNodeByParam("id", data[i].certId, null);
//                         if (node) {
//                             selectCertTree.checkNode(node, true, true, false);
//                             // 加载右侧数据，已经选择的电子证照库
//                             var liHtml = '<li name="selectCertLi" category-id="' + data[i].certId + '">' +
//                                             '<span class="drag-handle_td" ' +
//                                             'onclick="removeSelectedCert(\'' + data[i].certId + '\');">×</span>' +
//                                             '<span class="org_name_td">' + node.name + '</span>' +
//                                         '</li>';
//                             $('#selectedCertUl').append(liHtml);
//                         }
//                     }
//                 }
//             }
//         }
//     });
// }

// 批量阶段删除材料证照
function batchDeleteItemInMatCert(){

    if(curIsEditable){
        var rows = $("#item_in_mat_cert_tb").bootstrapTable('getSelections');
        var inoutIds = [];
        var fileTypes = [];
        if(rows!=null&&rows.length>0){
            for(var i=0;i<rows.length;i++){
                inoutIds.push(rows[i].inoutId);
                fileTypes.push(rows[i].fileType);
            }
            swal({
                title: '提示信息',
                text: '此操作将批量删除材料、证照以及表单与当前事项关联数据,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    if (inoutIds != null && inoutIds.length > 0) {
                        $.ajax({
                            url: ctx + '/aea/item/inout/batchDelItemMatCertFormCascade.do',
                            type: 'POST',
                            data: {'ids': inoutIds.toString(),'fileTypes': fileTypes.toString()},
                            success: function (result) {
                                swal({
                                    title: '提示信息',
                                    text: '删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 刷新表格
                                searchItemInMatCert();
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
        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
    }
}

function editItemInMatCertById(inoutId,matCertId,fileType){

    if(inoutId&&matCertId){

        if(fileType=='mat'){

            loadGlobalMatData(curIsEditable?false:true, inoutId,matCertId);

        }else if(fileType=='cert'){

            // 加载展示数据
            $('#aedit_item_inout_cert_modal').modal('show');
            $('#aedit_item_inout_cert_modal_title').html('查看证照');
            $('#aedit_item_inout_cert_form')[0].reset();
            aedit_item_inout_cert_validator.resetForm();
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
 * @param inoutId 输出id
 * @param matId  材料或者电子证照id
 */
function loadGlobalMatData(isView,inoutId,matId){

    $('#aedit_item_inout_mat_modal').modal('show');
    $('#aedit_item_inout_mat_form')[0].reset();
    aedit_item_inout_mat_validator.resetForm();
    $('#aedit_item_inout_mat_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $("#aedit_item_inout_mat_form input[name='matId']").val('');
    $("#aedit_item_inout_mat_form input[name='inoutId']").val('');
    $("#aedit_item_inout_mat_form input[name='itemId']").val('');
    $("#aedit_item_inout_mat_form input[name='certId']").val('');
    $("#aedit_item_inout_mat_form input[name='stoFormId']").val('');
    clearAllFile();

    if(isView){

        $('#aedit_item_inout_mat_modal_title').html('查看材料');
        $('#saveItemOutGlobalMatBtn').hide();

    }else{

        $('#aedit_item_inout_mat_modal_title').html('编辑材料');
        $('#saveItemOutGlobalMatBtn').show();
        $("#aedit_item_inout_mat_form input[name='inoutId']").val(inoutId);
        $("#aedit_item_inout_mat_form input[name='itemId']").val(currentBusiId);
        $("#aedit_item_inout_mat_form input[name='isInput']").val('1');
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
                loadFormData(true,'#aedit_item_inout_mat_form',data);
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

function deleteItemInMatCertFormById(inoutId, matCertFormId, fileType){

    if(inoutId&& matCertFormId &&fileType){

        var  url = ctx+'/aea/item/inout/deleteAeaItemInoutCascade.do';
        var  title = '';
        if(fileType=='mat'){
            title = '材料';
        }else if(fileType=='cert'){
            title = '证照';
        }else if(fileType=='form'){
            title = '表单';
            // url = ctx+'/aea/item/state/form/delAeaItemStateFormById.do';
        }
        swal({
            title: '提示信息',
            text: '此操作将删除'+ title +'与事项关联数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data:{'id': inoutId},
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
                            searchItemInMatCert();
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
function selectItemInoutGlobalMatParam(params) {

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

function selectItemInoutGlobalMatResponseData(res){

    return res;
}

// 查询
function searchItemInoutGlobalMat(){

    commonQueryParams = [];
    var t = $('#search_item_inout_global_mat_form').serializeArray();
    $.each(t, function() {
        commonQueryParams.push({
            'name': this.name,
            'value': this.value
        });
    });
    commonQueryParams.push({'name': 'itemVerId','value': currentBusiId});
    commonQueryParams.push({'name': 'stateVerId','value': currentStateVerId});
    commonQueryParams.push({'name': 'isInput','value': '1'});
    $("#select_item_inout_global_mat_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    // $("#select_item_inout_global_mat_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchItemInoutGlobalMat(){

    $('#search_item_inout_global_mat_form')[0].reset();
    searchItemInoutGlobalMat();
}

// 保存事项选择的全局材料数据
function saveItemInOutGlobalMat(){

    if(currentBusiId){
        var rows = $("#select_item_inout_global_mat_tb").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0){
            var matIds = [];
            for(var i=0;i<rows.length;i++){
                matIds.push(rows[i].matId);
            }
            $.ajax({
                url: ctx + '/aea/item/inout/batchSaveItemInoutMatCert.do',
                type: 'POST',
                data: {
                    'itemVerId': currentBusiId,
                    'stateVerId': currentStateVerId,
                    'isInput': '1',
                    'isStateIn':'0',
                    'fileType': 'mat',
                    'matCertIds': matIds.toString()
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
                        $('#select_item_inout_global_mat_modal').modal('hide');
                        // 刷新材料证照列表
                        searchItemInMatCert();
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
        swal('提示信息', "事项id为空！", 'info');
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

    var matId = $('#aedit_item_inout_mat_form input[name="matId"]').val();
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

function viewAttSizeFormatter(value, row, index){

    if(value){
        return value/1000;
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

function deleteMatAttrByDetailId(id){

    if(id){
        var matId = $('#aedit_item_inout_mat_form input[name="matId"]').val();
        var inoutId = $('#aedit_item_inout_mat_form input[name="inoutId"]').val();
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
                            loadGlobalMatData(false,inoutId,matId);
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

//参数设置
function itemCertParam(params) {

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

// 查询
function searchItemCert(){

    commonQueryParams = [];
    commonQueryParams.push({'name': 'itemVerId','value': currentBusiId});
    commonQueryParams.push({'name': 'stateVerId','value': currentStateVerId});
    commonQueryParams.push({'name': 'isInput','value': '1'});
    commonQueryParams.push({'name': 'keyword','value': $('#itemCertKeyword').val()});

    //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#item_cert_list_tb").bootstrapTable('selectPage',1);
}

// 清空
function clearSearchItemCert(){

    $('#itemCertKeyword').val('')
    searchItemCert();
}