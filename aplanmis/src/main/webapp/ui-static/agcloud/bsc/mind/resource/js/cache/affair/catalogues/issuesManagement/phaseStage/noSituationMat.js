var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
var INOUT_URL_PREFIX = ctx + '/aea/par/in/';
var matForm, itemMatValidator;
var matAttQueryParams = [];
$(function () {
    matForm = $('#form_mat')[0];
    itemMatValidator = $("#form_mat").validate({

        // 定义校验规则
        rules: {
            matName: {
                required: true,
                maxlength: 100
            },
            matCode: {
                required: true,
                maxlength: 50
            },
            matTypeId: {
                required: true
            },
            reviewKeyPoints:{
                required: true
            }
        },
        messages: {
            matName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过100个字母!"
            },
            matCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            },
            matTypeId: {
                required: '<font style="position: absolute;right:0px;width:100px;" color="red">此项必选！</font>',
            },
            reviewKeyPoints:{
                required: '<font style="position: absolute;right:0px;width:100px;" color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function (form) {
            var formData = new FormData(matForm);
            if($('#matId').val()==''){
                formData.append("isCommon", '1');
                formData.append("isStateIn",'0');
            }

            $.ajax({
                type: 'post',
                url: MAT_URL_PREFIX + 'saveAeaItemMatAndParIn.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        $('#dialog_mat').modal('hide');
                        refresh();
                    } else {
                        agcloud.ui.metronic.showSwal({type: 'error', message: '保存失败!'});
                    }
                }
            });
        }
    });

    $('input[type=radio][name=isStateIn]').change(function () {
        if (this.value == '1') {
            $("#itemStateIdDiv").show();
        }
        else if (this.value == '0') {
            $("#itemStateIdDiv").hide();
            $('#itemStateId').selectpicker('val', "");
            $('#itemStateId').selectpicker('refresh');
        }
    });
    $("#globalMatListDiv").hide();
    $('input[type=radio][name=globalMatIsStateInout]').change(function () {
        if (this.value == '1') {
            $("#globalMatItemStateDiv").show();
        }
        else if (this.value == '0') {
            $("#globalMatItemStateDiv").hide();
            $('#globalMatItemStateId').selectpicker('val', "");
            $('#globalMatItemStateId').selectpicker('refresh');
        }
    });
});

    /*打开材料类别选框*/
    function openMatType() {
        goSetp('mat_type');
        initMatTypeTree();
        initSearchMatTypeZtree();
    }

    /*选择一个材料类别*/
    function selectMatType() {
        var matTypes = matTypeTree.getSelectedNodes();
        var matTypeId = matTypes[0].id;
        var matTypeName = matTypes[0].name;
        $('#matTypeId').val(matTypeId);
        $('#form_mat input[name="matTypeName"]').val(matTypeName);
        goSetp('mat');
    }

    /*新增事项材料.isIn:是否是输入材料*/
    function addMat(isIn) {

        clearAllFile();

        // $('#templateDocFileDiv').removeClass("hide");
        // $('#templateDocButton').addClass("hide");
        //
        // $('#sampleDocFileDiv').removeClass("hide");
        // $('#sampleDocButton').addClass("hide");
        //
        // $('#reviewSampleDocFileDiv').removeClass("hide");
        // $('#reviewSampleDocButton').addClass("hide");

        $('#templateDocDiv').hide();
        $('#sampleDocDiv').hide();
        $('#reviewSampleDocDiv').hide();

        $('#sampleDoc').val('');
        $('#templateDoc').val('');
        $('#reviewSampleDoc').val('');


        $('#matId').val('');                            //重置id reset对隐藏的input无效
        $('#matTypeId').val('');
        // $('#matHolder').selectpicker('val', "");        //默认选中
        //add by liupeng 20180830 begin
        if (isIn == 1) {
            $('#isStateInoutDiv').show();
            $('#itemStateId').selectpicker('val', "");
            $('#itemStateId').empty();
            // $.ajax({
            //     url: ctx + '/aea/item/mat/getAllItemState.do',
            //     type: 'POST',
            //     data: {itemId: currentBusiId},
            //     success: function (data) {
            //         if (data) {
            //             var stateItemList = eval('(' + data + ')');
            //             if (stateItemList.length > 0) {
            //                 for (var i = 0; i < stateItemList.length; i++) {
            //                     $('#itemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
            //                 }
            //                 $('#itemStateId').selectpicker('refresh');
            //                 $('input[type=radio][name=isStateIn]').attr("disabled", false);
            //             } else {
            //                 $('input[type=radio][name=isStateIn]').attr("disabled", true);
            //             }
            //         }
            //     },
            //     error: function () {
            //         swal('错误信息', '获取事项情形信息出错！', 'error');
            //     }
            // });

            $('input[type=radio][name=isStateIn]').attr("disabled", true);

            $('input[type=radio][name=isStateIn][value="0"]').prop("checked", true);

            $('input[type=radio][name=isCommon]').attr("disabled", true);

            $('input[type=radio][name=isCommon][value="1"]').prop("checked", true);

            $("#itemStateIdDiv").hide();
        } else {
            $('#isStateInoutDiv').hide();
            $("#itemStateIdDiv").hide();
        }
        matForm.reset();
        itemMatValidator.resetForm();
        //开启编号验证
        $("#matCode").rules("remove");
        $("#matCode").rules("add", {
            required: true, maxlength: 100, remote: MAT_URL_PREFIX + 'checkMatCode.do',
            messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
        });
        //设置属性
        $('#stageId').val(currentBusiId);
        $('#isOwner').val('1');
        $('#isInput').val('' + isIn);
        $('#isGlobalShare').val('1');

        if (isIn) {
            $('#dialog_mat_title').html('新增材料');
        }
        $('#btn_save').show();
        goSetp('mat');
        $('.open-mat-type').bind('click', openMatType);
        $('#dialog_mat').modal('show');
        $('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部


        $.ajax({
            url: ctx + '/bsc/rule/code/getAutoCode.do',
            type: 'post',
            data: {key:"AEA-ITEM-MAT-CODE","projInfoId":'1'},
            async: false,
            cache: false,
            success: function (data) {
                $("#matCode").val(data);
            }
        });
    }

    function loadMatDoc(isView) {

    }

/*查看(isView=1)编辑(isView=0)材料*/
function loadMatData(id, isView, isIn, isGlobal) {

    $("#itemStateIdDiv").hide();

    clearAllFile();

    $('#templateDocDiv').hide();
    $('#sampleDocDiv').hide();
    $('#reviewSampleDocDiv').hide();

    $('#matId').val('');//重置id reset对隐藏的input无效
    $('#matTypeId').val('');
    $('#matHolder').selectpicker('val', "");        //默认选中
    //add by liupeng 20180830 begin
    if (isIn == 1) {
        $('#isStateInoutDiv').show();
        $('#itemStateId').selectpicker('val', "");
        $('#itemStateId').empty();
        // $.ajax({
        //     url: ctx + '/aea/item/mat/getAllItemState.do',
        //     type: 'POST',
        //     data: {itemId: currentBusiId},
        //     success: function (data) {
        //         if (data) {
        //             var stateItemList = eval('(' + data + ')');
        //             if (stateItemList.length > 0) {
        //                 for (var i = 0; i < stateItemList.length; i++) {
        //                     $('#itemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
        //                 }
        //                 $('#itemStateId').selectpicker('refresh');
        //                 $('input[type=radio][name=isStateIn]').attr("disabled", false);
        //             } else {
        //                 $('input[type=radio][name=isStateIn]').attr("disabled", true);
        //             }
        //         }
        //     },
        //     error: function () {
        //         swal('错误信息', '获取事项情形信息出错！', 'error');
        //     }
        // });
        $('input[type=radio][name=isStateIn]').attr("disabled", true);
        $('input[type=radio][name=isCommon]').attr("disabled", true);
    } else {
        $('#isStateInoutDiv').hide();
    }
    //add by liupeng 20180830 end
    matForm.reset();
    itemMatValidator.resetForm();
    $("#matCode").rules("remove");
    if (isView) {
        $('#btn_save').hide();
        $('#dialog_mat_title').html('查看事项材料');
        $('.open-mat-type').unbind('click', openMatType);
    } else {
        $('#btn_save').show();
        $('#dialog_mat_title').html('编辑事项材料');
        $('.open-mat-type').bind('click', openMatType);
        $("#matCode").rules("add", {
            required: true, maxlength: 100,
            messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!'}
        });
    }
    $('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部
    $.post(MAT_URL_PREFIX + 'getAeaItemMat.do', {id: id}, function (data) {
        if (data) {
            loadFormData(true, "#form_mat", data);
            //add by liupeng 20180831 begin

            if(data.templateDoc!=null) {
                $('#templateDoc').val(data.templateDoc);
            }
            if(data.sampleDoc!=null) {
                $('#sampleDoc').val(data.sampleDoc);
            }
            if(data.reviewSampleDoc!=null) {
                $('#reviewSampleDoc').val(data.reviewSampleDoc);
            }
            // loadMatDoc(isView);


            if (data.templateDocCount&&data.templateDocCount!=0) {

                $('#templateDocDiv').show();
                $('#templateDocButton').html(data.templateDocCount + "个&nbsp;查看附件");
            }else{
                $('#templateDocDiv').hide();
            }

            if (data.sampleDocCount&&data.sampleDocCount!=0) {

                $('#sampleDocDiv').show();
                $('#sampleDocButton').html(data.sampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#sampleDocDiv').hide();
            }

            if (data.reviewSampleDocCount&&data.reviewSampleDocCount!=0) {

                $('#reviewSampleDocDiv').show();
                $('#reviewSampleDocButton').html(data.reviewSampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#reviewSampleDocDiv').hide();
            }

            if (isIn == 1) {
                $.ajax({
                    url: ctx + '/aea/item/mat/getParInByMatId.do',
                    type: 'POST',
                    data: {
                        matId: data.matId,
                        stageId: currentBusiId
                    },
                    success: function (stateData) {
                        if (stateData) {
                            var mapdata = eval('(' + stateData + ')');
                            // $('#itemStateId').selectpicker('val', mapdata.itemStateId);
                            // $('#itemStateId').selectpicker('refresh');

                            if (mapdata.isStateIn == '1') {
                                $('input[type=radio][name=isStateIn][value="1"]').prop("checked", true);
                                // $("#itemStateIdDiv").show();
                            } else {
                                $('input[type=radio][name=isStateIn][value="0"]').prop("checked", true);
                                // $("#itemStateIdDiv").hide();
                            }

                            $("#itemStateIdDiv").hide();
                        }
                    },
                    error: function () {
                        swal('错误信息', '获取事项情形信息出错！', 'error');
                    }
                });
            }
            $('#dialog_mat').modal('show');

            goSetp('mat');
            if (isGlobal) {
                // $('#btn_back_mat_global').removeClass('hid');
            }
        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}

/*删除材料*/
function delMat(id, isIn) {
    var msg = '确定要删除吗？';
    var callback = function () {
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
                    url: INOUT_URL_PREFIX + 'deleteAeaItemInoutCascade.do',
                    dataType: 'json',
                    data: {id: id},
                    success: function (result) {
                        if (result.success) {
                            agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                            refresh(isIn);
                        } else {
                            agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                        }
                    }
                });
            }
        });
    };
    $.ajax({
        type: 'post',
        url: ctx + '/aea/par/share/mat/existShare.do',
        dataType: 'json',
        data: {inoutId: id},
        success: function (result) {
            if (result.success) {
                msg = '已存在主题共享!确定要删除事项材料及主题共享信息吗？';
                callback();
            } else {
                callback();
            }
        }
    });
}

/*批量删除材料*/
function delSelectMat(isIn) {
    var datatable;
    if (isIn) {
        datatable = $('#tb_mat_in').bootstrapTable();
    }
    var checkboxs = datatable.bootstrapTable('getSelections');
    if (checkboxs.length == 0) {
        swal('提示', '请勾选数据后操作！', 'info');
        return false;
    }
    var ids = '';
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i]["inoutId"];
        ids = ids + e + ',';
    }
    ids = ids.substring(0, ids.length - 1);
    var msg = '确定要批量删除吗？';
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
                url: INOUT_URL_PREFIX + 'batchDeleteAeaItemInoutCascade.do',
                dataType: 'json',
                data: {ids: ids},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                        refresh(isIn);
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}


var GlobalMatDatatable = function () {
    var datatable;
    var getDatatable = function () {
        return datatable;
    };
    var mDatatableInit = function () {
        if (datatable != null) return;
        datatable = $('#tb_mat_global').bootstrapTable({
            // url:MAT_URL_PREFIX + 'listStageNoSelectGlobalMat.do',
            url:MAT_URL_PREFIX + 'listAllStageNoSelectMat.do',
            queryParams:function(params) {
                var pageNum = (params.offset / params.limit) + 1;
                var queryParams = {};
                queryParams['pageNum'] = pageNum;
                queryParams['pageSize'] = params.limit;
                queryParams['stageId'] =  currentBusiId;
                queryParams['isCommon'] =  '1';
                queryParams['keyword'] =  $('#input_mat_global_search').val();
                return queryParams;
            },
            columns: [{
                    checkbox: true,
                    field: '#', // 数据字段
                    align: "center",
                    title: '#', // 页面字段显示
                    sortable: false, // 禁用排序
                    width: 10,  // 宽度,单位: px
                    textAlign: 'center', // 字段列标题和数据排列方式
                    selector: {class: 'm-checkbox--solid m-checkbox--brand'},
                },{
                field: "matId",
                visible: false
            }, {
                field: "matCode",
                title: "材料编号",
                textAlign: 'center',
                width: 500,
                textAlign: 'left'
            }, {
                field: "matName",
                title: "材料名称",
                width: 500,
                textAlign: 'left'
            }/*, {
                field: "itemName",
                title: "所属事项",
                width: 300
            }*/,
                {
                    field: '_operate', // 数据字段
                    title: '操作', // 页面字段显示
                    sortable: false, // 禁用排序
                    width: 80,  // 宽度,单位: px
                    textAlign: 'center', // 字段列标题和数据排列方式
                    formatter: function (value, row, index) {
                        var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ',' + 1 + ',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-edit"></i></a>';
                        return viewBtn;
                    }
                }
            ],
            sortOrder: "asc",
            pageNumber:1,
            pageSize: 10,
            sidePagination: 'server',
            pagination:true,
            // search:true,
            maintainSelected:true,
            // pageList:[10, 25, 50, 100]
        });
    };
    var search = function () {
        var queryParams = {};
        queryParams['stageId'] =  currentBusiId;
        queryParams['isCommon'] =  '1';
        queryParams['keyword'] =  $('#input_mat_global_search').val();
        $("#tb_mat_global").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#tb_mat_global").bootstrapTable('refresh',queryParams);
    };
    var searchClear = function () {
        $('#input_mat_global_search').val('');//搜索框置空
        search();
    };
    var reload = function () {
        // datatable.reload();
        search();
    };

    return {
        init: mDatatableInit,
        getDatatable: getDatatable,
        search: search,
        searchClear: searchClear,
        reload: reload
    };
}();
var isInputView = 1;

/*打开全局材料选框*/
function openGlogbalMatSelect(isIn) {

    $("#select_global_select_state_div").css("display","none");

    //$('#isOwner').val('0');
    //$('#isInput').val(isIn);
    isInputView = isIn;
    $('#dialog_mat').modal('show');
    goSetp('mat_global');

    GlobalMatDatatable.init();
    GlobalMatDatatable.reload();

    //add by liupeng 20180904
    if (isIn == 1) {
        $('#globalMatItemStateId').selectpicker('val', "");
        $('#globalMatItemStateId').empty();
        $.ajax({
            url: ctx + '/aea/item/mat/getAllItemState.do',
            type: 'POST',
            data: {itemId: currentBusiId},
            success: function (data) {
                if (data) {
                    var stateItemList = eval('(' + data + ')');
                    if (stateItemList.length > 0) {
                        for (var i = 0; i < stateItemList.length; i++) {
                            $('#globalMatItemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
                        }
                        $('#globalMatItemStateId').selectpicker('refresh');
                        $('input[type=radio][name=globalMatIsStateInout]').attr("disabled", false);
                    } else {
                        $('input[type=radio][name=globalMatIsStateInout]').attr("disabled", true);
                    }
                }
            },
            error: function () {
                swal('错误信息', '获取事项情形信息出错！', 'error');
            }
        });
        $('input[type=radio][name=globalMatIsStateInout]').attr("disabled", false);
    } else {
        $('input[type=radio][name=globalMatIsStateInout]').attr("disabled", true);
    }
    $("#globalMatListDiv").hide();
    $("#globalMatOperaDiv").show();
    $('input[type=radio][name=globalMatIsStateInout][value="0"]').prop("checked", true);
    $("#globalMatItemStateDiv").hide();
    $("#globalMatIds").val("");
    $("#global_mat_show_input").val("");
    //add by liupeng 20180904



    chooseGlobalMat();
}

/*选择多个全局材料加入到事项的输入*/
function selectGlobalMat() {
    var datatable = $('#tb_mat_global').mDatatable();
    var checkboxs = datatable.getSelectedRecords().find("input[type='checkbox']");
    if (checkboxs.length == 0) {
        swal('提示', '请勾选材料后操作！', 'info');
        return false;
    }
    var infoIds = '';
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i];
        if (e.checked) {
            infoIds = infoIds + e.value + ',';
        }
    }
    infoIds = infoIds.substring(0, infoIds.length - 1);
    var inMat = {};
    inMat['stageId'] = currentBusiId;
    inMat['isInput'] = isInputView;
    inMat['matIds'] = infoIds;
    $.ajax({
        type: 'post',
        url: INOUT_URL_PREFIX + 'addGlobalMatParIn.do',
        dataType: 'json',
        data: inMat,
        success: function (result) {
            if (result.success) {
                agcloud.ui.metronic.showSuccessTip('添加成功！', 1000);
                $('#dialog_mat').modal('hide');
                refresh(isInputView);
            } else {
                agcloud.ui.metronic.showSwal({message: '添加失败!', type: 'error'});
            }
        }
    });

}

/*状态*/
var Status = function () {
    var convertToChinese = function (status) {
        if ('1' === status) {
            return '是';
        } else {
            return '否';
        }
    };
    return {
        convertToChinese: convertToChinese,
        ON: '1',
        OFF: '0'
    };
}();

/*格式化材料启用状态栏*/
function itemMatIsActiveFormat(isActive, id) {

    if (isActive == '1') {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" disabled="disabled" checked="checked" name="isActive" onclick="changeItemMatIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    } else {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" disabled="disabled" name="isActive" onclick="changeItemMatIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

/*修改材料启用状态*/
function changeItemMatIsActive(obj, id, isActive) {
    var action = isActive == '1' ? "禁用" : "启用";
    swal({
        title: '提示信息',
        text: "确定要" + action + "选中的记录吗?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: MAT_URL_PREFIX + 'changeIsActive.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if (result.success) {
                        searchItemState();
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        } else {
            isActive == '1' ? $(obj).prop("checked", true) : $(obj).prop("checked", false);
        }
    });
}

/*格式化材料全局状态栏*/
function isGlobalShareFormat(isGlobalShare, id) {
    if (isGlobalShare == '1') {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isGlobalShare" onclick="changeIsGlobalShare(this,\'' + id + '\',\'' + isGlobalShare + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    } else {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isGlobalShare" onclick="changeIsGlobalShare(this,\'' + id + '\',\'' + isGlobalShare + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

/*修改材料全局状态*/
function changeIsGlobalShare(obj, id, isGlobalShare) {
    var action = isGlobalShare == '1' ? "非全局" : "全局";
    swal({
        title: '提示信息',
        text: "确定设置为" + action + "材料吗?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: MAT_URL_PREFIX + 'changeIsGlobalShare.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if (result.success) {
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        } else {
            isGlobalShare == '1' ? $(obj).prop("checked", true) : $(obj).prop("checked", false);
        }
    });
}

/*切换全局状态单选框的显示状态;输入材料固定为0,无需显示*/
function toggleInout(isIn) {
    if (isIn) {
        if (!$('#container_is_global_share').hasClass('hid')) {
            $('#container_is_global_share').addClass('hid');
            $('#container_is_global_share').find('input').attr('disabled', true);
            $('#input_hidden_is_global_share').attr('disabled', false);
        }
    }
}

/*设置状态*/
function goSetp(setp) {
    $('#wizard_' + setp).addClass('wizard-step_current').siblings('.wizard-step').removeClass('wizard-step_current');//控制窗体内容
    $('#dialog_' + setp + '_title').removeClass('hid').siblings('h5').addClass('hid');//控制窗口标题
    // if ('mat' === setp) {
    //     if (!$('#btn_back_mat_global').hasClass('hid')) {
    //         $('#btn_back_mat_global').addClass('hid');
    //     }
    // }
}

//弹出材料导入列表框
function chooseGlobalMat() {
    $("#btn_mat_global_select").text("导入");
    $("#globalMatListDiv").show();
    $("#globalMatOperaDiv").hide();
    $("#dialogMatGlobalCloseBtn").hide();
}

function globalMatDialogChoose() {
    var checkboxs = $('#tb_mat_global').bootstrapTable('getSelections');
    if (checkboxs.length == 0) {
        swal('提示', '请勾选材料后操作！', 'info');
        return false;
    }
    var infoIds = '';
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i];

        infoIds = infoIds + e.matId + ',';
    }

    if (infoIds.length > 0) {
        var inMat = {};
        inMat['stageId'] = currentBusiId;
        inMat['isInput'] = isInputView;
        inMat['matIds'] = infoIds;
        inMat['isStateIn'] = $('input[type=radio][name=globalMatIsStateInout]:checked').val();
        inMat['parStateId'] = $('#globalMatItemStateId').val();
        $.ajax({
            type: 'post',
            url: INOUT_URL_PREFIX + 'addGlobalMatParIn.do',
            dataType: 'json',
            data: inMat,
            success: function (result) {
                if (result.success) {
                    $('#dialog_mat').modal('hide');
                    agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                    refresh();
                } else {
                    agcloud.ui.metronic.showSwal({message: '添加失败!', type: 'error'});
                }
            }
        });
    } else {
        swal('提示', '请选择导入材料后操作！', 'info');
    }
}

function globalMatDialogClose() {
    $("#dialog_mat").modal("hide");
}

function clearFile(file) {
    file.after(file.clone().val(""));
    file.remove();
}

function clearAllFile() {
    clearFile($("#templateDocFile"));
    clearFile($("#sampleDocFile"));
    clearFile($("#reviewSampleDocFile"));


}

function editMat(isGlobalShare,matId,inId) {
    clearAllFile();

    $('#templateDocDiv').hide();
    $('#sampleDocDiv').hide();
    $('#reviewSampleDocDiv').hide();

    $('#sampleDoc').val('');
    $('#templateDoc').val('');
    $('#reviewSampleDoc').val('');


    $('#matId').val('');                            //重置id reset对隐藏的input无效
    $('#matTypeId').val('');
    // $('#matHolder').selectpicker('val', "");        //默认选中
    //add by liupeng 20180830 begin

    $('#isStateInoutDiv').show();
    $('#itemStateId').selectpicker('val', "");
    $('#itemStateId').empty();

    $('input[type=radio][name=isStateIn]').attr("disabled", true);
    $('input[type=radio][name=isCommon]').attr("disabled", true);
    $("#itemStateIdDiv").hide();


    matForm.reset();
    itemMatValidator.resetForm();
    //开启编号验证
    $("#matCode").rules("remove");
    // $("#matCode").rules("add", {
    //     required: true, maxlength: 100, remote: MAT_URL_PREFIX + 'checkMatCode.do',
    //     messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
    // });
    //设置属性
    $('#stageId').val(currentBusiId);
    $('#isOwner').val('1');
    $('#isInput').val('1');

    if(isGlobalShare) {
        $('#isGlobalShare').val('1');
    }else{
        $('#isGlobalShare').val('0');
    }

    $('#dialog_mat_title').html('编辑材料');

    $('#btn_save').show();
    goSetp('mat');
    $('.open-mat-type').bind('click', openMatType);
    $('#dialog_mat').modal('show');
    $('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部

    $('#matId').val(matId);

    $.post(ctx+'/aea/item/mat/getAeaItemMat.do', {id: matId}, function (result) {
        if(result != null){
            loadFormData(true,'#form_mat',result);

            if (result.templateDocCount&&result.templateDocCount!=0) {

                $('#templateDocDiv').show();
                $('#templateDocButton').html(result.templateDocCount + "个&nbsp;查看附件");
            }else{
                $('#templateDocDiv').hide();
            }

            if (result.sampleDocCount&&result.sampleDocCount!=0) {

                $('#sampleDocDiv').show();
                $('#sampleDocButton').html(result.sampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#sampleDocDiv').hide();
            }

            if (result.reviewSampleDocCount&&result.reviewSampleDocCount!=0) {

                $('#reviewSampleDocDiv').show();
                $('#reviewSampleDocButton').html(result.reviewSampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#reviewSampleDocDiv').hide();
            }


            $.ajax({
                url: ctx + '/aea/par/in/getAeaParIn.do',
                type: 'POST',
                data: {
                    id: inId,
                },
                success: function (inData) {

                    if(inData){
                        if("1"==inData.isStateIn){
                            $('input[type=radio][name=isStateIn][value="1"]').prop("checked", true);
                        }else{
                            $('input[type=radio][name=isStateIn][value="0"]').prop("checked", true);

                        }
                    }
                },
                error: function () {

                }
            });
        }
    }, 'json');
}


// 查询事件
function searchMatSto() {

    var params = $('#search_mat_sto_form').serializeArray();
    matAttQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            matAttQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#customTable").bootstrapTable('refresh');       //无参数刷新
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
    if (matAttQueryParams) {
        for (var i = 0; i < matAttQueryParams.length; i++) {
            buildParam[matAttQueryParams[i].name] = matAttQueryParams[i].value;
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
    if (matAttQueryParams) {
        for (var i = 0; i < matAttQueryParams.length; i++) {
            buildParam[matAttQueryParams[i].name] = matAttQueryParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function matAttTbResponseData(res) {
    return res;
}




function searchMatSto() {

    var params = $('#search_mat_sto_form').serializeArray();
    matAttQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            matAttQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#customTable").bootstrapTable('refresh');       //无参数刷新
}

function searchMatAttSto(type) {

    var matId = $('#form_mat input[name="matId"]').val();
    var pkName = "";
    if(type=='0'){
        pkName = "TEMPLATE_DOC";
    }else if(type=='1'){
        pkName = "SAMPLE_DOC";
    }else if(type=='2'){
        pkName = "REVIEW_SAMPLE_DOC";
    }
    matAttQueryParams = [];
    matAttQueryParams.push({name: "tableName",value:"AEA_ITEM_MAT"});
    matAttQueryParams.push({name: "pkName",value: pkName});
    matAttQueryParams.push({name: "recordId",value: matId});
    $("#show_mat_att_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#show_mat_att_tb").bootstrapTable('refresh');       //无参数刷新
}

var showMatType = null;
function showMatAtt(type){

    $('#show_mat_att_modal').modal('show');
    showMatType = type;
    searchMatAttSto(type);
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


function viewAttNameFormatter(value, row, index){

    // 图片
    if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"){

        var url = "";
        if(row.attType=='KP'){
            url = ctx + '/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
        }else{
            url = MAT_URL_PREFIX + 'downloadGlobalMatDoc.do?detailId='+ row.detailId;
        }
        // return '<a href="#" onclick="showFile(\''+ url +'\')">'+ row.attName +'</a>';
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

function showFile(ulr){

    window.open(MAT_URL_PREFIX + 'showFile.do?ulr='+ encodeURI(ulr),"展示图片");
}


function deleteMatAttrByDetailId(id){

    if(id){
        var matId = $('#form_mat input[name="matId"]').val();
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

function showImgFile(detailId){

    window.open(MAT_URL_PREFIX + 'showFile.do?detailId='+ detailId,"展示图片");
}