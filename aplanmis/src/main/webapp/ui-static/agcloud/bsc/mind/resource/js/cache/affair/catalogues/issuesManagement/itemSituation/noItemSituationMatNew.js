var stateId, itemId;

var $itemMatSearch = $('#itemMatSearch');
var $itemMatTable = $('#itemMatTable');

var itemMatBootstrapTable, itemMatValidator;

initEvents();
initFormValidator();
loadMatTable();

function initEvents() {

    //新增
    $('#addItemMatBtn').click(function () {
        clearAllFile();//清空文件

        $('#templateDocDiv').hide();
        $('#sampleDocDiv').hide();
        $('#reviewSampleDocDiv').hide();

        $('#item_mat_form')[0].reset();

        $('#item_mat_add_title').html('新建材料');
        $('#matId').val('');

        $("input[type=radio][name='isCommon'][value='1']").prop("checked", true);
        $('#isStateIn').val('0');

        $("input[type=radio][name='isCommon'][value='0']").attr("disabled",true);
        $("input[type=radio][name='isCommon'][value='1']").attr("disabled",true);

        getMatCodeByAjax();//获取材料编码

        $('#item_mat_add').modal('show');
    });

    $('#refreshItemMatBtn').click(function () {
        loadMatTable();
    });

    // 批量删除材料
    $('#batchDeleteMatBtn').click(function delSelectMat() {
        var checkboxs = itemMatBootstrapTable.bootstrapTable('getSelections');
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
                    url: ctx + '/aea/item/inout/batchDeleteAeaItemInoutCascade.do',
                    dataType: 'json',
                    data: {ids: ids},
                    success: function (result) {
                        if (result.success) {
                            agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                            loadMatTable();
                        } else {
                            agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                        }
                    }
                });
            }
        });
    });

    //导入
    $('#importItemMatBtn').click(function () {
        $('#item_overmat_import').modal('show');
        $('#item_overmat_import_title').html('导入材料');
        loadFormFieldData(1, "", stateId, currentBusiType);

        $("#btn_mat_global_select").unbind('click').click(function () {
            globalMatDialogClose();
            var dataArr = getGlobalMatChoose();
            if (dataArr && dataArr.length > 0) {

                var ids = new Array();

                for (var i = 0; i < dataArr.length; i++) {
                    ids.push(dataArr[i].id);
                }
                $.ajax({
                    type: 'post',
                    url: ctx + '/aea/item/mat/saveChooseItemMatAndInout.do',
                    // dataType: 'json',
                    data: {ids: ids.join(","), itemId: currentBusiId, isStateIn: '0', isCommon: '1'},
                    async: false,
                    cache: false,
                    success: function (result) {
                        if (result.success) {
                            agcloud.ui.metronic.showSuccessTip('导入成功！', 1500);
                            loadMatTable();
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'error', message: '导入失败!'});
                        }
                    }
                });
            }
        });
    });

    //查询
    $('#itemMatSearchBtn').click(function () {
        loadMatTable();
    });

    //清空
    $('#itemMatClearBtn').click(function () {
        $itemMatSearch.val('');//搜索框置空
        loadMatTable();
    });
}

function initFormValidator() {
    itemMatValidator = $("#item_mat_form").validate({
        // 定义校验规则
        rules: {
            matName: {
                required: true,
                maxlength: 100
            },
            matCode: {
                required: true,
                maxlength: 50
            }
        },
        messages: {
            matName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            },
            matCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            }
        },
        // 提交表单
        submitHandler: function (form) {
            var formData = new FormData($('#item_mat_form')[0]);
            var itemId = currentBusiId;
            var isStateIn = "0";
            var isCommon = "1";
            $.ajax({
                type: 'post',
                url: ctx + '/aea/item/mat/saveAeaItemMatAndInout.do?itemId=' + itemId +  "&isStateIn=" + isStateIn + "&isCommon=" + isCommon,
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {
                        $('#item_mat_add').modal('hide');
                        loadMatTable();
                    } else {
                        agcloud.ui.metronic.showSwal({type: 'error', message: '保存失败!'});
                    }
                }
            });
        }
    });
}

function loadMatTable() {
    if (itemMatBootstrapTable != null) {
        itemMatBootstrapTable.bootstrapTable('destroy');
    }
    itemMatBootstrapTable = $itemMatTable.bootstrapTable({
        url: ctx + '/aea/item/inout/listAeaItemInoutAndMatNoPagination.do',
        queryParams: {itemId: currentBusiId, isInput: '1', keyword: $itemMatSearch.val()},
        height: '100%',
        undefinedText: '-',
        striped: false,
        cache: false,
        contentType: 'application/json',
        pagination: true,
        onlyInfoPagination: false,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50],
        paginationShowPageGo: true,
        paginationVAlign: 'bottom',
        paginationHAlign: 'right',
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
        },
        columns: [
            {
                checkbox: true,
                align: "center"
            }, {
                field: "inoutId",
                visible: false
            }, {
                field: "#",
                title: "#",
                width: "10%",
                align: "center",
                sortable: false,
                textAlign: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: "mat.mat_code",
                title: "材料编号",
                align: "center",
                visible: false,
                width: "10%",
                formatter: function (value, row, index) {
                    return row.aeaItemMat.matCode;
                }
            }, {
                field: "mat.mat_name",
                title: "材料名称",
                width: "20%",
                align: "center",
                formatter: function (value, row, index) {
                    return row.aeaItemMat.matName;
                }
            }, {
                field: "mat.is_require",
                title: "是否必须",
                align: "center",
                width: "20%",
                formatter: function (value, row, index) {
                    return Status.convertToChinese(row.aeaItemMat.isRequire);
                }
            },
            {
                field: 'mat.is_active',
                title: '是否启用',
                width: "20%",
                align: "center",
                sortable: false,
                formatter: function (value, row, index) {

                    return itemMatIsActiveFormat(row.aeaItemMat.isActive, row.matId);
                }
            },
            {
                field: '_operate', // 数据字段
                title: '操作', // 页面字段显示
                sortable: false, // 禁用排序
                width: "30%",
                align: "center",
                formatter: function (value, row, index) {
                    var editBtn = '<a href="javascript:editMat(\'' + row.matId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                    var deleteBtn = '<a href="javascript:deleteMat(\'' + row.matId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                    return editBtn + deleteBtn;
                }
            }
        ],
    });
}

function setIsCommonValueBasedTabActivation() {
    var href = $('a.active', $itemMatModalBody).attr('href');
    return href == '#matList';
}

function getMatCodeByAjax() {
    $.ajax({
        url: ctx + '/bsc/rule/code/getOneOrgAutoCode.do',
        type: 'post',
        data: {'codeIc': "AEA-ITEM-MAT-CODE"},
        async: false,
        cache: false,
        success: function (data) {
            $("#matCode").val(data);
        }
    });
}

function editMat(matId) {
    clearAllFile();
    if(itemMatValidator!=null) {
        $("#item_mat_form").validate().resetForm();
    }
    $('#matId').val('');
    $('#matTypeId').val('');
    $('#matHolder').selectpicker('val', "");
    $('#templateDocDiv').hide();
    $('#sampleDocDiv').hide();
    $('#reviewSampleDocDiv').hide();
    $('#item_mat_form')[0].reset();
    $.post(ctx + '/aea/item/mat/getAeaItemMat.do', {id: matId}, function (result) {
        if (result) {
            loadFormData(true, "#item_mat_form", result);

            if (result.templateDocCount && result.templateDocCount != 0) {
                $('#templateDocDiv').show();
                $('#templateDocButton').html(result.templateDocCount + "个&nbsp;查看附件");
            }else{
                $('#templateDocDiv').hide();
            }

            if (result.sampleDocCount && result.sampleDocCount != 0) {
                $('#sampleDocDiv').show();
                $('#sampleDocButton').html(result.sampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#sampleDocDiv').hide();
            }

            if (result.reviewSampleDocCount && result.reviewSampleDocCount != 0) {
                $('#reviewSampleDocDiv').show();
                $('#reviewSampleDocButton').html(result.reviewSampleDocCount + "个&nbsp;查看附件");
            }else{
                $('#reviewSampleDocDiv').hide();
            }

            $("input[type=radio][name='isCommon'][value='0']").attr("disabled",true);
            $("input[type=radio][name='isCommon'][value='1']").attr("disabled",true);
            $('#item_mat_add_title').html('编辑事项材料');
            $('#item_mat_add').modal('show');

        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}

function clearAllFile() {
    clearFile($("#templateDocFile4Mat"));
    clearFile($("#sampleDocFile4Mat"));
    clearFile($("#reviewSampleDocFile4Mat"));
}

function clearFile(file) {
    file.after(file.clone().val(""));
    file.remove();
}

function loadMatDoc() {
    if ($('#templateDoc').val() != null && $('#templateDoc').val() != '') {
        $('#templateDocFileDiv').addClass("hide");
        $('#templateDocButton').removeClass("hide");
        $('#templateDocDelete').css("display", "inline");
    } else {
        $('#templateDocFileDiv').removeClass("hide");
        $('#templateDocButton').addClass("hide");
    }

    if ($('#reviewSampleDoc').val() != null && $('#reviewSampleDoc').val() != '') {
        $('#reviewSampleDocDelete').css("display", "inline");
        $('#reviewSampleDocButton').removeClass("hide");
        $('#reviewSampleDocFileDiv').addClass("hide");

    } else {
        $('#reviewSampleDocFileDiv').removeClass("hide");
        $('#reviewSampleDocButton').addClass("hide");
    }

    if ($('#sampleDoc').val() != null && $('#sampleDoc').val() != '') {
        $('#sampleDocFileDiv').addClass("hide");
        $('#sampleDocDelete').css("display", "inline");
        $('#sampleDocButton').removeClass("hide");
    } else {
        $('#sampleDocFileDiv').removeClass("hide");
        $('#sampleDocButton').addClass("hide");
    }
}

function deleteMat(matId) {
    var msg = '确定要删除吗？';
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
                url: ctx + '/aea/item/mat/deleteAeaItemMatById.do',
                dataType: 'json',
                data: {id: matId},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                        loadMatTable();
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}

function itemMatIsActiveFormat(isActive, id) {
    if (isActive == '1') {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" onclick="changeItemMatIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    } else {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" onclick="changeItemMatIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

// 修改材料启用状态
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
                url: ctx + '/aea/item/mat/changeIsActive.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if (result.success) {
                        loadMatTable();
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