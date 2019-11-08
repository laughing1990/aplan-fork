var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
var parentMatTypeTree = null;
var globalMatForm,
    globalMatValidator,
    $globalMatDialog,
    matTypeId = '';

$(function () {
    globalMatForm = $('#form_mat_global')[0];
    $globalMatDialog = $('#dialog_mat_global');

    GlobalMatDatatable.init();
    setTimeout("$('div.m-datatable__pager-info').hide()",500);

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
            // formData.append('matTypeId',matTypeId);

            $.ajax({
                type: 'post',
                url: ctx + '/aea/item/mat/type/saveAeaItemMatType.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        //$globalMatDialog.modal('hide');
                        showContentContainer('container_mat_type_def_list');
                        GlobalMatDatatable.reload();
                        initMenuTree();
                    } else {
                        agcloud.ui.metronic.showSwal({type: 'error', message: '保存失败!'});
                    }
                }
            });
        }
    });
});
function loadGlobalMat(id) {
    if (id) {
        if(id=='root'){
            matTypeId = '';
        }else {
            matTypeId = id;
        }
        GlobalMatDatatable.init();
    }
}

//修改入口
//切换内容容器
function showContentContainer(visualContainerId){
    $('#contentContainer >div').each(function () {
        if(visualContainerId==$(this).attr('id')){
            $(this).css('display','block');
        }
        else{
            $(this).css('display','none');
        }
    })
}
function loadMatTypeDefDetail(id) {
    $.post(ctx + '/aea/item/mat/type/getAeaItemMatType.do', {id: id}, function (data) {
        if (data) {
            loadFormData(true,"#form_mat_global", data);
            $globalMatDialog.modal('show');
        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}

var GlobalMatDatatable = function () {
    var datatable;
    var getDatatable = function () {
        return datatable;
    };
    var mDatatableInit = function () {
        if (datatable != null) datatable.destroy();
        datatable = $('#tb_mat_global').mDatatable({
            data: {
                type: 'remote',
                source: {
                    read: {
                        method: 'post',
                        params:{
                            // isGlobalShare:'1',
                            // isActive:'1',
                            // matTypeId:matTypeId
                        },
                        // url: MAT_URL_PREFIX + 'listAeaItemMat.do',
                        url: ctx + '/aea/item/mat/type/getAllMatType.do',
                        map: function (raw) {
                            var dataSet = raw;
                            if (typeof raw.data !== 'undefined') {
                                dataSet = raw.data;
                            }
                            return dataSet;
                        }
                    }
                },
                 // pageSize: 10,
                 // serverPaging: true,
                 // serverFiltering: true,
                 // serverSorting: true
            },
            layout: {
                theme: 'default', // datatable theme
                class: '', // custom wrapper class
                scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
                // height: 450, // datatable's body's fixed height
                footer: false // display/hide footer
            },
            sortable: true,
            // pagination: true,
            // toolbar: {
            //     items: {
            //         pagination: {
            //             pageSizeSelect: [10, 20, 30]
            //         }
            //     }
            // },
            columns: [{
                // field: "matId",
                field: "matTypeId",
                title: "#",
                width: 10,
                sortable: false,
                textAlign: 'center',
                selector: {class: 'm-checkbox--solid m-checkbox--brand'}
            },{
                // field: "matCode",
                field: "typeCode",
                title: "材料编号",
                textAlign: 'center',
                width: 100
            }, {
                // field: "matName",
                field: "typeName",
                title: "材料名称",
                width: 250
            }, {
                field: 'isOfficialDoc',
                title: '是否批文',
                width: 80,  // 宽度,单位: px
                textAlign: 'center',
                sortable: false,
                template: function (row, index, datatable) {
                    if(row.isOfficialDoc == 1) {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            }, {
                field: 'isBasic',
                title: '是否基础材料',
                width: 100,  // 宽度,单位: px
                textAlign: 'center',
                sortable: false,
                template: function (row, index, datatable) {
                    if(row.isBasic == 1) {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            }, {
                field: 'isActive',
                title: '是否启用',
                width: 80,  // 宽度,单位: px
                textAlign: 'center',
                sortable: false,
                template: function (row, index, datatable) {
                    return itemMatTypeIsActiveFormat(row.isActive, row.matTypeId);
                }
            },
                {
                    field: '_operate', // 数据字段
                    title: '操作', // 页面字段显示
                    sortable: false, // 禁用排序
                    width: 100,  // 宽度,单位: px
                    textAlign: 'center', // 字段列标题和数据排列方式
                    template: function (row, index, datatable) {
                        // var viewBtn = '<a href="javascript:loadGlobalMatData(\'' + row.matId + '\',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                        // var editBtn = '<a href="javascript:loadGlobalMatTypeData(\'' + row.matTypeId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';

                        var editBtn = '<a href="javascript:loadMatTypeDefDetail(\''+row.matTypeId+'\');'
                            +'matTypeInfoTreeSelectNode(\''+row.matTypeId+'\');'
                            +'showContentContainer(\'container_mat_type_def_edit\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';


                        var deleteBtn = '<a href="javascript:delGlobalMatType(\'' + row.matTypeId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return editBtn + deleteBtn;
                    }
                }
            ]
        });
        datatable.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
    };




 var search = function () {
         // datatable.search($('#input_mat_global_search').val(), 'keyword');

    };

    var searchClear = function () {
        $('#input_mat_global_search').val('');//搜索框置空
        search();
        setTimeout("$('div.m-datatable__pager-info').hide()",500);
    };
    var reload = function () {
        datatable.reload();
        setTimeout("$('div.m-datatable__pager-info').hide()",500);
    };

    return {
        init: mDatatableInit,
        getDatatable: getDatatable,
        search:search,
        searchClear:searchClear,
        reload:reload
    };
}();

// function addGlobalMat() {
//     initMatTypeForm();
//     //globalMatForm.reset();
//     //$globalMatDialog.resetForm();
//     //开启编号验证
//     $("#typeCode").rules("remove");
//     $("#typeCode").rules("add", {
//         required: true, maxlength: 100, remote: ctx + '/aea/item/mat/type/checkMatTypeCode.do',
//         messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
//     });
//     $("#typeName").rules("remove");
//     $("#typeName").rules("add", {
//         required: true, maxlength: 100,
//         messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!'}
//     });
//
//     $('#btn_save').show();
//     $('#dialog_mat_global_title').html('新增材料类型');
//     $('#dialog_mat_global').modal('show');
//     $('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部
// }

function addGlobalMat() {
    initMatTypeForm();
    //开启编号验证
    $("#typeCode").rules("remove");
    $("#typeCode").rules("add", {
        required: true, maxlength: 100, remote: ctx + '/aea/item/mat/type/checkMatTypeCode.do',
        messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
    });
    $("#typeName").rules("remove");
    $("#typeName").rules("add", {
        required: true, maxlength: 100,
        messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!'}
    });
    showContentContainer('container_mat_type_def_edit');
}


function initMatTypeForm() {
    var inputArraytemp = $('#form_mat_global').serializeArray();
    $.each(inputArraytemp, function(i, field){
        $("input[name='" + field.name + "']").val("");
    });
}

function loadGlobalMatTypeData(id, isView) {
    $('#matTypeId').val('');//重置id reset对隐藏的input无效
    $('#matHolder').selectpicker('val', "");        //默认选中
    globalMatForm.reset();
    $globalMatDialog.resetForm();
    $("#typeCode").rules("remove");
    if (isView) {
        $('#btn_save').hide();
        $('#dialog_mat_global_title').html('查看材料类型');
    } else {
        $('#btn_save').show();
        $('#dialog_mat_global_title').html('编辑材料类型');
        $("#typeCode").rules("add", {
            required: true, maxlength: 100,
            messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!'}
        });
    }
    $('#dialog_mat_global_body').animate({scrollTop: 0}, 800);//滚动到顶部
    $.post(ctx + '/aea/item/mat/type/getAeaItemMatType.do', {id: id}, function (data) {
        if (data) {
            loadFormData(true,"#form_mat_global", data);
            $globalMatDialog.modal('show');
        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}
function delGlobalMatType(id) {
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
                url: ctx + '/aea/item/mat/type/deleteAeaItemMatTypeById.do',
                dataType: 'json',
                data: {id: id},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                        GlobalMatDatatable.reload();
                        initMenuTree();
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}

function batchDelGlobalMat() {
    var datatable = GlobalMatDatatable.getDatatable();
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
                url: ctx + '/aea/item/mat/type/batchDeleteAeaItemMatType.do',
                dataType: 'json',
                data: {ids: ids},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！', 1000);
                        GlobalMatDatatable.reload();
                        initMenuTree();
                    } else {
                        agcloud.ui.metronic.showSwal({message: '删除失败!', type: 'error'});
                    }
                }
            });
        }
    });
}

function itemMatTypeIsActiveFormat(isActive, id) {
    if (isActive == '1') {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" checked="checked" name="isActive" onclick="changeItemMatTypeIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    } else {
        return '<span class="m-switch m-switch--success">' +
            '  <label>' +
            '     <input type="checkbox" name="isActive" onclick="changeItemMatTypeIsActive(this,\'' + id + '\',\'' + isActive + '\');">' +
            '     <span></span>' +
            '   </label>' +
            '</span>'
    }
}

function changeItemMatTypeIsActive(obj, id, isActive) {
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
                url: ctx + '/aea/item/mat/type/changeIsActive.do',
                type: 'POST',
                data: {'matTypeId': id},
                success: function (result) {
                    if (result.success) {
                        GlobalMatDatatable.reload();
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

function chooseParent() {
    $("#select_parent_mat_type").modal("show");

    var parentMatTypeTreeSettings = {
        edit: {
            enable: false,        //设置 zTree 是否处于编辑状态
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId"
                // rootPId: 0
            }
        },
        view: {
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false, //设置 zTree 是否显示节点之间的连线。
            showHorizontal:true//设置是否水平平铺树（自定义属性）

        },
        check: {
            enable: false,
            chkboxType:{ "Y" : "", "N" : "" },
            chkStyle: "checkbox"
        },
        //使用异步加载，必须设置 setting.async 中的各个属性
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam:["id"],
            dataType:"json",
            type:"post",
            url:ctx+'/aea/item/mat/type/getListMatTypeZtreeNode.do'
        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: matTypeInfoTreeOnClick, //点击事件
            //用于捕获异步加载正常结束的事件回调函数
            onAsyncSuccess: onAsyncSuccessAppOrgTree
            ,onExpand:function(event, treeId, treeNode, msg){
            }
        }
    };
    if(parentMatTypeTree) parentMatTypeTree.destroy();
    parentMatTypeTree = $.fn.zTree.init($("#selectParentMatTypeTree"), parentMatTypeTreeSettings);
}

function matTypeInfoTreeOnClick(event, treeId, treeNode, clickFlag) {
    $("#chooseParentTypeId").val(treeNode.id);
    $("#select_parent_mat_type").modal("hide");
}
//
// function parentDialogClose() {
//     $("#select_parent_mat_type").modal("hide");
// }