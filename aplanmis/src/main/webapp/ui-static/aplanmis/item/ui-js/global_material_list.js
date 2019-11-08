var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
var globalMatForm,
    globalMatValidator,
    $globalMatDialog,
    datatable;

$(function () {
    // 初始化列表
    initTable();
    // 初始化表单校验
    initValidate();

    // 材料类别选择点击事件绑定
    $('.open-mat-type').bind('click',openSelectMatTypeModal);
    // 材料类别确定事件
    $('#selectMatTypeBtn').bind('click',selectMatType);

    globalMatForm = $('#form_mat_global')[0];
    $globalMatDialog = $('#dialog_mat_global');
});

// 初始化全局材料列表
function initTable() {
    datatable = $('#tb_mat_global').mDatatable({
        data: {
            type: 'remote',
            source: {
                read: {
                    method: 'post',
                    params:{
                        isGlobalShare:'1',
                        isActive:'1',
                    },
                    url: MAT_URL_PREFIX + 'listAeaItemMat.do',
                    map: function (raw) {
                        var dataSet = raw;
                        if (typeof raw.data !== 'undefined') {
                            dataSet = raw.data;
                        }
                        return dataSet;
                    }
                }
            },
            pageSize: 10,
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true
        },
        layout: {
            theme: 'default', // datatable theme
            class: '', // custom wrapper class
            scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
            // height: 450, // datatable's body's fixed height
            footer: false // display/hide footer
        },
        sortable: true,
        pagination: false,
        toolbar: {
            items: {
                pagination: {
                    pageSizeSelect: [10, 20, 30]
                }
            }
        },
        columns: [{
            field: "matId",
            title: "#",
            width: 10,
            sortable: false,
            textAlign: 'center',
            selector: {class: 'm-checkbox--solid m-checkbox--brand'}
        },{
            field: "matCode",
            title: "材料编号",
            textAlign: 'center',
            width: 100
        }, {
            field: "matName",
            title: "材料名称",
            width: 250
        }, {
            field: '_operate', // 数据字段
            title: '操作', // 页面字段显示
            sortable: false, // 禁用排序
            width: 100,  // 宽度,单位: px
            textAlign: 'center', // 字段列标题和数据排列方式
            template: function (row, index, datatable) {
                var editBtn = '<a href="javascript:loadGlobalMatData(\'' + row.matId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                var deleteBtn = '<a href="javascript:delGlobalMat(\'' + row.matId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                return editBtn + deleteBtn;
            }
        }]
    });
   // datatable.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
}

// 初始化表单检验
function initValidate() {
    globalMatValidator = $("#form_mat_global").validate({
        // 定义校验规则
        rules: {
            matName: {
                required: true,
                maxlength: 100
            }

        },
        messages: {
            matName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            }
        },
        // 提交表单
        submitHandler: function () {
            var formData = new FormData(globalMatForm);
            console.info(formData);
            $.ajax({
                type: 'post',
                url: MAT_URL_PREFIX + 'saveGlobalAeaItemMat.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        // 隐藏模式框
                        $('#dialog_mat_global').modal('hide');
                        // 列表数据重新加载
                        datatable.reload();
                    } else {
                        agcloud.ui.metronic.showSwal({type: 'error', message: '保存失败!'});
                    }
                }
            });
        }
    });
}

// 显示新增窗口
function addGlobalMat() {
    $('#matId').val('');                            //重置id reset对隐藏的input无效
    $('#matHolder').selectpicker('val', "");        //默认选中
    //globalMatForm.reset();
    //$globalMatDialog.resetForm();
    // 添加校验规则
    $("#typeCode").rules("remove");
    $("#typeCode").rules("add", {
        required: true, maxlength: 100, remote: ctx + '/aea/item/mat/type/checkMatTypeCode.do',
        messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
    });

    $('#dialog_mat_global_title').html('新增全局材料');
    $('#dialog_mat_global').modal('show');
    //$('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部
}

// 选择了一个材料类别
function selectMatType() {
    var matTypes = selectMatTypeTree.getSelectedNodes();
    var matTypeId = matTypes[0].id;
    var matTypeName = matTypes[0].name;

    $('#matTypeId').val(matTypeId);
    $('#form_mat_global input[name="matTypeName"]').val(matTypeName);
    // 关闭窗口
    closeSelectMatTypeZtree();
}

// 修改数据
function loadGlobalMatData(id) {
    $('#matId').val('');//重置id reset对隐藏的input无效
    globalMatForm.reset();
    $globalMatDialog.resetForm();

    $("#typeCode").rules("remove");

    $('#dialog_mat_global_title').html('编辑材料');
    $('#dialog_mat_global_body').animate({scrollTop: 0}, 800);//滚动到顶部

    $.post(MAT_URL_PREFIX + 'getAeaItemMat.do', {id: id}, function (data) {
        if (data) {
            loadFormData(true,"#form_mat_global", data);
            $globalMatDialog.modal('show');
        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}

// 批量删除数据
function batchDelGlobalMat() {
    var checkboxs = datatable.getSelectedRecords().find("input[type='checkbox']");
    if (checkboxs.length == 0) {
        swal('提示', '请勾选数据后操作！', 'info');
        return false;
    }
    var ids = '';
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i];
        if (e.checked) {
            ids = ids + e.value + ',';
        }
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
                url: MAT_URL_PREFIX + 'batchDeleteAeaItemMatByIds.do',
                dataType: 'json',
                data: {ids: ids},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);

                        datatable.reload();
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}

// 删除单条数据
function delGlobalMat(id) {
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
                url: MAT_URL_PREFIX + 'deleteAeaItemMatById.do',
                dataType: 'json',
                data: {id: id},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);

                        datatable.reload();
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}

// 表格数据过滤
function searchTable() {
    datatable.search($('#input_mat_global_search').val(), 'keyword');
}

// 请空查询
function clearSearch() {
    $('#input_mat_global_search').val('');//搜索框置空
    searchTable();
}