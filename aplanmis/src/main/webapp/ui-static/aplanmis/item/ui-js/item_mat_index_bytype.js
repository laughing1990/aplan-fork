var MAT_URL_PREFIX = ctx+'/aea/item/mat/';
var INOUT_URL_PREFIX = ctx + '/aea/item/inout/';
var itemId = '1';
    //parent.itemId;
var matForm;
$(function(){
    MatDatatable.init();
    matForm = $('#form_mat')[0];
});

var MatDatatable = function() {
    var datatable;
    var mDatatableInit = function() {
        if(datatable!=null)datatable.destroy();
        datatable = $('#tb_mat').mDatatable({
            data: {
                type: 'remote',
                source: {
                    read: {
                        method: 'post',
                        url: MAT_URL_PREFIX + 'listAeaItemMat.do?isDeleted=0',
                        params:{matTypeId:matTypeId},
                        map: function(raw) {
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
            pagination: true,
            search: {
                input: $('#generalSearch')
            },
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
                field: "#",
                title: "#",
                width: 10,
                sortable: false,
                textAlign: 'center',
                template: function (row, index, datatable) {
                    return index + 1;
                }
            }, {
                field: "matCode",
                title: "材料编号",
                textAlign: 'center',
                width: 100,
            }, {
                field: "matName",
                title: "材料名称",
            },/* {
                field: "matTypeName",
                title: "所属类别",
                textAlign: 'center',
                width: 100,
            }, */{
                field: "createTime",
                title: "创建时间",
                textAlign: 'center',
                width: 100,
                template: function (row, index, datatable) {
                    if(row.createTime){
                        return agcloud.utils.dateFormat(row.createTime);
                    }
                    return '';
                }
            },
                {
                field: '_operate', // 数据字段
                title: '操作', // 页面字段显示
                sortable: false, // 禁用排序
                width: 100,  // 宽度,单位: px
                textAlign: 'center', // 字段列标题和数据排列方式
                template: function (row, index, datatable) {
                    var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                    var editBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                    var deleteBtn = '<a href="javascript:delMat(\''+row.matId+ '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                    return viewBtn + editBtn + deleteBtn;
                }
            }
            ]
        });
        datatable.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
        $('#slt_is_lack').on('change', function() {
            datatable.search($(this).val().toLowerCase(), 'isLack');
        });

        $('#slt_is_global_share').on('change', function() {
            datatable.search($(this).val().toLowerCase(), 'isGlobalShare');
        });

        $('#slt_receive_mode').on('change', function() {
            datatable.search($(this).val().toLowerCase(), 'receiveMode');
        });

        $('#slt_is_lack, #slt_is_global_share, #slt_receive_mode').selectpicker();
    };

    return {
        init: mDatatableInit,
        datatable:datatable
    };
}();
function openMatType() {
    var title = '选择材料类别';
    $('#add_item_mat_modal_title').html(title);
    $('#add_item_mat_modal').modal('show');
    initTree();
    initSearchMatTypeZtree();
}
function selectMatType() {
    var matTypes = matTypeTree.getSelectedNodes();
    var matTypeId = matTypes[0].id;
    var matTypeName = matTypes[0].name;
    $('#matTypeId').val(matTypeId);
    $('#form_mat input[name="matTypeName"]').val(matTypeName);
    $('#add_item_mat_modal').modal('hide');
}
function addMat(isIn) {
    itemId='1';
    $('#matId').val('');                            //重置id reset对隐藏的input无效
    matForm.reset();
    $('#btn_save').show();
    $('#dialog_mat_title').html('新增事项材料');
    $('#dialog_mat').modal('show');

    //设置属性
    $('#itemId').val(itemId);
    $('#isOwner').val('1');
    $('#isInput').val(isIn);
    $('#isGlobalShare input[value="0"]').attr('checked','checked');
}
function selectGlogbalMat() {
    $('#isOwner').val('0');
    $('#isInput').val('1');
    $('#dialog_mat_global').modal('show');

    GlobalMatDatatable.init();
    var datatable = $('#tb_mat_global').mDatatable();
    datatable.setActiveAll(false);
}

function loadMatData(id,isView) {
    $('#matId').val('');//重置id reset对隐藏的input无效
    matForm.reset();
    if(isView){
        $('#btn_save').hide();
        $('#dialog_mat_title').html('查看事项材料');
    }else{
        $('#btn_save').show();
        $('#dialog_mat_title').html('编辑事项材料');
    }
    $.post(MAT_URL_PREFIX+'getAeaItemMat.do', {id: id}, function (data) {
        if (data) {
            loadFormData(true,"#form_mat", data);
            $('#dialog_mat').modal('show');
        }else{
            agcloud.ui.metronic.showErrorTip("数据加载失败!",1000);
        }
    });
}
function saveMat() {
    if (validateForm(matForm)) {
        var formData = new FormData(matForm);
        /*if(formData.get('sampleDoc').name){
            formData.append("sampleDocFile", formData.get('sampleDoc'));
        }
        formData.delete('sampleDoc');
        if(formData.get('templateDoc').name){
            formData.append("templateDocFile", formData.get('templateDoc'));
        }
        formData.delete('templateDoc');*/
        $.ajax({
            type: 'post',
            url: MAT_URL_PREFIX +'saveAeaItemMatAndInout.do',
            dataType: 'json',
            data: formData,
            contentType: false,
            processData: false,
            success: function (result) {
                if (result.success) {
                    agcloud.ui.metronic.showSuccessTip('保存成功！',1500);
                    $('#dialog_mat').modal('hide');
                    refresh();
                } else {
                    agcloud.ui.metronic.showSwal({type:'error',message:'保存失败!'});
                }
            }
        });
    }
}
function delMat(id) {
    var msg = '你确定要删除吗？';
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
                url: MAT_URL_PREFIX +'deleteAeaItemMatById.do',
                dataType: 'json',
                data: {id:id},
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('删除成功！',1000);
                        refresh();
                    } else {
                        agcloud.ui.metronic.showSwal({message:'删除失败!',type:'error'});
                    }
                }
            });
        }
    });
}
function refresh() {
    var datatable = $('#tb_mat').mDatatable();
    datatable.reload();
}
var GlobalMatDatatable = function() {
    var datatable;
    var mDatatableInit = function() {
        if(datatable!=null)return;
        datatable = $('#tb_mat_global').mDatatable({
            data: {
                type: 'remote',
                source: {
                    read: {
                        method: 'post',
                        url: MAT_URL_PREFIX + 'listAeaItemMat.do?isDeleted=0&isGlobalShare=1',
                        params:{matTypeId:matTypeId},
                        map: function(raw) {
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
            pagination: true,
            search: {
                input: $('#search_mat_global')
            },
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
                field: "#",
                title: "#",
                width: 10,
                sortable: false,
                textAlign: 'center',
                template: function (row, index, datatable) {
                    return index + 1;
                }
            }, {
                field: "matCode",
                title: "材料编号",
                textAlign: 'center',
                width: 100,
            }, {
                field: "matName",
                title: "材料名称",
            },/* {
                field: "matTypeName",
                title: "所属类别",
                textAlign: 'center',
                width: 100,
            }, */{
                field: "createTime",
                title: "创建时间",
                textAlign: 'center',
                width: 100,
                template: function (row, index, datatable) {
                    if(row.createTime){
                        return agcloud.utils.dateFormat(row.createTime);
                    }
                    return '';
                }
            },
                {
                    field: '_operate', // 数据字段
                    title: '操作', // 页面字段显示
                    sortable: false, // 禁用排序
                    width: 100,  // 宽度,单位: px
                    textAlign: 'center', // 字段列标题和数据排列方式
                    template: function (row, index, datatable) {
                        var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                        return viewBtn;
                    }
                }
            ]
        });
        datatable.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
    };

    return {
        init: mDatatableInit,
        datatable:datatable
    };
}();

function selectGlobalMat() {
    var datatable = $('#tb_mat_global').mDatatable();
    var checkboxs = datatable.getSelectedRecords().find("input[type='checkbox']");
    if(checkboxs.length == 0){
        swal('提示', '请勾选数据后操作！', 'info');
        return false;
    }
    var infoIds = '';
    for (var i=0;i<checkboxs.length;i++){
        var e=checkboxs[i];
        if(e.checked){
            infoIds = infoIds + e.value+ ',';
        }
    }
    infoIds = infoIds.substring(0,infoIds.length-1);
    var inMat = {};
    inMat['itemId'] = itemId;
    inMat['matIds'] = infoIds;
    $.ajax({
        type: 'post',
        url: INOUT_URL_PREFIX +'addInMat.do',
        dataType: 'json',
        data: inMat,
        success: function (result) {
            if (result.success) {
                agcloud.ui.metronic.showSuccessTip('添加成功！',1000);
                $('#dialog_mat_global').modal('hide');
                refresh();
            } else {
                agcloud.ui.metronic.showSwal({message:'添加失败!',type:'error'});
            }
        }
    });

}
//$('#themeImageViewRow').hide();//隐藏主题图片
//$('#themeImageLabel').html('请选择图片...');//重置文件上传input
//if (data.themeImage != null && data.themeImage != '') {
/*$('#themeImageView').attr('src', data.themeImage);
$('#themeImageViewRow').show();//显示主题图片
} else {
    $('#themeImageViewRow').hide();//隐藏主题图片
}*/
/**
 * 表单加载数据
 * @param form
 * @param data
 */
var loadItemMatFormData = function (form, data) {
    for (var i = 0; i < form.length; i++) {
        var element = form.elements[i];
        if (data[element.name] != null) {
            switch (element.type) {
                case 'radio':
                    if (element.value == data[element.name])
                        $(element).prop('checked', true);
                    break;
                case 'select-one':
                    $(element).selectpicker('val', data[element.name]);
                    break;
                default:
                    $(element).val(data[element.name]);
                    break;
            }
        }
    }
};