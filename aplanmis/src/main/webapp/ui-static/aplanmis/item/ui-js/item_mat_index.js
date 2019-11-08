var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
var INOUT_URL_PREFIX = ctx + '/aea/item/inout/';
var itemId = "";//记录数事项数节点选中
var matForm, itemMatValidator;
var globalMatDialogSign = 0; //add by liupeng 20180904
$(function () {
    InMatDatatable.init();
    OutMatDatatable.init();
    // GlobalMatDatatable.init();
    matForm = $('#form_mat')[0];
    itemMatValidator = $("#form_mat").validate({

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
        submitHandler: function (form) {
            var formData = new FormData(matForm);
            // var ent = formData.entries();
            // while(item = ent.next()){
            //     if(item.done) break;
            //     alert(item.value);
            // }

            $.ajax({
                type: 'post',
                url: MAT_URL_PREFIX + 'saveAeaItemMatAndInout.do',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        $('#dialog_mat').modal('hide');
                        refresh(1);
                        refresh(0);
                    } else {
                        agcloud.ui.metronic.showSwal({type: 'error', message: '保存失败!'});
                    }
                }
            });
        }
    });

    //add by liupeng 20180831 begin
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
    //add by liupeng 20180831 end
});

/*点击事项树节点时加载事项的输入,输出材料*/
function loadItemMat(id) {
    if (id) {
        itemId = id;
        InMatDatatable.init();
        OutMatDatatable.init();
    }
}

/*事项输入材料列表*/
var InMatDatatable = function () {
    var datatable;
    var getDatatable = function () {
        return datatable;
    };
    var mDatatableInit = function () {
        if (datatable != null) datatable.bootstrapTable("destroy");
        datatable = $('#tb_mat_in').bootstrapTable({
            url: INOUT_URL_PREFIX + 'listAeaItemInoutAndMat.do',
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
                    width: "20%",
                    align: "center",
                    formatter: function (value, row, index) {
                        var editBtn;
                        if ("1" == row.isOwner) {
                            editBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 0 + ',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                        } else {
                            editBtn = '';
                        }
                        var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                        var deleteBtn = '<a href="javascript:delMat(\'' + row.inoutId + '\',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return viewBtn + editBtn + deleteBtn;
                    }
                }
            ],
            sortOrder: "asc",
            pagination: true,
            width: '100%',
            pageSize: 10,
            pageList: [10, 20, 50, 100],
            paginationHAlign: 'right',
            paginationDetailHAlign: "right",
            paginationVAlign: 'bottom',
            paginationShowPageGo: true,
            formatShowingRows: function (pageFrom, pageTo, totalRows) {

                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            sidePagination: "client",
            queryParams: {itemId: itemId, isInput: '1'}
        })
    };

    var search = function () {
        datatable.search($('#input_mat_in_search').val(), 'keyword');
    };
    var searchClear = function () {
        $('#input_mat_in_search').val('');//搜索框置空
        search();
    };
    var reload = function () {
        datatable.bootstrapTable("refresh");
    };
    return {
        init: mDatatableInit,
        getDatatable: getDatatable,
        search: search,
        searchClear: searchClear,
        reload: reload
    };
}();

/*事项输入材料列表*/
var OutMatDatatable = function () {
    var datatable;
    var datatable;
    var getDatatable = function () {
        return datatable;
    };
    var mDatatableInit = function () {
        if (datatable != null) datatable.bootstrapTable("destroy");
        datatable = $('#tb_mat_out').bootstrapTable({
            url: INOUT_URL_PREFIX + 'listAeaItemInoutAndMat.do',
            columns: [
                {
                    checkbox: true,
                    align: "center"
                },
                {
                    field: "inoutId",
                    visible: false
                }, {
                    field: "#",
                    title: "#",
                    width: "10%",
                    align: "center",
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                }, {
                    field: "mat.mat_code",
                    title: "材料编号",
                    align: "center",
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
                    width: "20%",
                    align: "center",
                    formatter: function (value, row, index) {
                        var editBtn;
                        if ("1" == row.isOwner) {
                            editBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 0 + ',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                        } else {
                            editBtn = '';
                        }
                        var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                        var deleteBtn = '<a href="javascript:delMat(\'' + row.inoutId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return viewBtn + editBtn + deleteBtn;
                    }
                }
            ],
            sortOrder: "asc",
            pagination: true,
            width: '100%',
            pageSize: 10,
            pageList: [10, 20, 50, 100],
            paginationHAlign: 'right',
            paginationDetailHAlign: "right",
            paginationVAlign: 'bottom',
            paginationShowPageGo: true,
            formatShowingRows: function (pageFrom, pageTo, totalRows) {

                return '显示第 '+ pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            sidePagination: "client",
            queryParams: {itemId: itemId, isInput: '0'}
        })
    };
    var search = function () {
        datatable.search($('#input_mat_out_search').val(), 'keyword');
    };
    var searchClear = function () {
        $('#input_mat_out_search').val('');//搜索框置空
        search();
    };
    var reload = function () {
        datatable.bootstrapTable("refresh");
    };
    return {
        init: mDatatableInit,
        getDatatable: getDatatable,
        search: search,
        searchClear: searchClear,
        reload: reload
    };
}();

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

    $('#templateDocFileDiv').removeClass("hide");
    $('#templateDocButton').addClass("hide");

    $('#sampleDocFileDiv').removeClass("hide");
    $('#sampleDocButton').addClass("hide");

    $('#sampleDoc').val('');
    $('#templateDoc').val('');


    $('#matId').val('');                            //重置id reset对隐藏的input无效
    $('#matTypeId').val('');
    $('#matHolder').selectpicker('val', "");        //默认选中
    //add by liupeng 20180830 begin
    if (isIn == 1) {
        $('#isStateInoutDiv').show();
        $('#itemStateId').selectpicker('val', "");
        $('#itemStateId').empty();
        $.ajax({
            url: ctx + '/aea/item/mat/getAllItemState.do',
            type: 'POST',
            data: {itemId: itemId},
            success: function (data) {
                if (data) {
                    var stateItemList = eval('(' + data + ')');
                    if (stateItemList.length > 0) {
                        for (var i = 0; i < stateItemList.length; i++) {
                            $('#itemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
                        }
                        $('#itemStateId').selectpicker('refresh');
                        $('input[type=radio][name=isStateIn]').attr("disabled", false);
                    } else {
                        $('input[type=radio][name=isStateIn]').attr("disabled", true);
                    }
                }
            },
            error: function () {
                swal('错误信息', '获取事项情形信息出错！', 'error');
            }
        });
        $('input[type=radio][name=isStateIn][value="0"]').attr("checked", true);
        $("#itemStateIdDiv").hide();
    } else {
        $('#isStateInoutDiv').hide();
        $("#itemStateIdDiv").hide();
    }
    //add by liupeng 20180830 end
    matForm.reset();
    itemMatValidator.resetForm();
    //开启编号验证
    $("#matCode").rules("remove");
    $("#matCode").rules("add", {
        required: true, maxlength: 100, remote: MAT_URL_PREFIX + 'checkMatCode.do',
        messages: {required: '<font color="red">此项必填！</font>', maxlength: '长度不能超过50个字母!', remote: '编号已存在!'}
    });
    //设置属性
    $('#itemId').val(itemId);
    $('#isOwner').val('1');
    $('#isInput').val('' + isIn);
    $('#isGlobalShare').val('0');

    if (isIn) {
        $('#dialog_mat_title').html('新增输入材料');
    } else {
        $('#dialog_mat_title').html('新增输出材料');
    }
    $('#btn_save').show();
    //toggleInout(isIn);
    goSetp('mat');
    $('.open-mat-type').bind('click', openMatType);
    $('#dialog_mat').modal('show');
    $('#dialog_mat_body').animate({scrollTop: 0}, 800);//滚动到顶部
}



function loadMatDoc(isView) {
    $('#templateDocDownLoad').removeClass("hide");
    $('#templateDocDelete').css("display","inline");

    $('#sampleDocDownLoad').removeClass("hide");
    $('#sampleDocDelete').css("display","inline");


    if( $('#templateDoc').val()!=null && $('#templateDoc').val()!=''){
        $('#templateDocFileDiv').removeClass("hide");
        $('#templateDocFileDiv').addClass("hide");
        $('#templateDocButton').removeClass("hide");
    }else{
        $('#templateDocFileDiv').removeClass("hide");
        $('#templateDocButton').removeClass("hide");
        $('#templateDocButton').addClass("hide");
    }

    if( $('#sampleDoc').val()!=null && $('#sampleDoc').val()!=''){
        $('#sampleDocFileDiv').removeClass("hide");
        $('#sampleDocFileDiv').addClass("hide");
        $('#sampleDocButton').removeClass("hide");
    }else{
        $('#sampleDocFileDiv').removeClass("hide");
        $('#sampleDocButton').removeClass("hide");
        $('#sampleDocButton').addClass("hide");
    }

    if(isView==1){
        $('#templateDocFileDiv').removeClass("hide");
        $('#sampleDocFileDiv').removeClass("hide");
        $('#templateDocFileDiv').addClass("hide");
        $('#sampleDocFileDiv').addClass("hide");


        if( $('#templateDoc').val()!=null && $('#templateDoc').val()!=''){

            $('#templateDocButton').removeClass("hide");
            $('#templateDocDelete').removeClass("hide");
            $('#templateDocDelete').css("display","none");
        }else{
            $('#templateDocButton').removeClass("hide");
            $('#templateDocButton').addClass("hide");
        }

        if( $('#sampleDoc').val()!=null && $('#sampleDoc').val()!=''){

            $('#sampleDocButton').removeClass("hide");

            $('#sampleDocDelete').removeClass("hide");

            $('#sampleDocDelete').css("display","none");
        }else{
            $('#sampleDocButton').removeClass("hide");

            $('#sampleDocButton').addClass("hide");
        }

    }
}

/*查看(isView=1)编辑(isView=0)材料*/
function loadMatData(id, isView, isIn, isGlobal) {

    clearAllFile();

    $('#matId').val('');//重置id reset对隐藏的input无效
    $('#matTypeId').val('');
    $('#matHolder').selectpicker('val', "");        //默认选中
    //add by liupeng 20180830 begin
    if (isIn == 1) {
        $('#isStateInoutDiv').show();
        $('#itemStateId').selectpicker('val', "");
        $('#itemStateId').empty();
        $.ajax({
            url: ctx + '/aea/item/mat/getAllItemState.do',
            type: 'POST',
            data: {itemId: itemId},
            success: function (data) {
                if (data) {
                    var stateItemList = eval('(' + data + ')');
                    if (stateItemList.length > 0) {
                        for (var i = 0; i < stateItemList.length; i++) {
                            $('#itemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
                        }
                        $('#itemStateId').selectpicker('refresh');
                        $('input[type=radio][name=isStateIn]').attr("disabled", false);
                    } else {
                        $('input[type=radio][name=isStateIn]').attr("disabled", true);
                    }
                }
            },
            error: function () {
                swal('错误信息', '获取事项情形信息出错！', 'error');
            }
        });
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
    //toggleInout(isIn);
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
            loadMatDoc(isView);

            if (isIn == 1) {
                $.ajax({
                    url: ctx + '/aea/item/mat/getInoutByMatId.do',
                    type: 'POST',
                    data: {matId: data.matId},
                    success: function (stateData) {
                        if (stateData) {
                            var mapdata = eval('(' + stateData + ')');
                            $('#itemStateId').selectpicker('val', mapdata.itemStateId);
                            $('#itemStateId').selectpicker('refresh');

                            if (mapdata.isStateInout == '1') {
                                $('input[type=radio][name=isStateIn][value="1"]').prop("checked", true);
                                $("#itemStateIdDiv").show();
                            } else {
                                $('input[type=radio][name=isStateIn][value="0"]').prop("checked", true);
                                $("#itemStateIdDiv").hide();
                            }
                        }
                    },
                    error: function () {
                        swal('错误信息', '获取事项情形信息出错！', 'error');
                    }
                });
            } else {
                $("#itemStateIdDiv").hide();
            }
            //add by liupeng 20180831 end
            $('#dialog_mat').modal('show');

            goSetp('mat');
            if (isGlobal) {
                $('#btn_back_mat_global').removeClass('hid');
            }
        } else {
            agcloud.ui.metronic.showErrorTip("数据加载失败!", 1000);
        }
    });
}

/*刷新*/
function refresh(isIn) {
    if (isIn) {
        InMatDatatable.reload();
    } else {
        OutMatDatatable.reload();
    }
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
    debugger
    var datatable;
    if (isIn) {
        datatable = $('#tb_mat_in').bootstrapTable();
    } else {
        datatable = $('#tb_mat_out').bootstrapTable();
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

/*全局材料列表*/
var GlobalMatDatatable = function () {
    var datatable;
    var getDatatable = function () {
        return datatable;
    };
    var mDatatableInit = function () {
        if (datatable != null) return;
        datatable = $('#tb_mat_global').mDatatable({
            data: {
                type: 'remote',
                source: {
                    read: {
                        method: 'post',
                        params: {
                            isGlobalShare: '1',
                            isActive: '1'
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
            pagination: true,
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
            }, {
                field: "matCode",
                title: "材料编号",
                textAlign: 'center',
                width: 100
            }, {
                field: "matName",
                title: "材料名称",
                width: 300
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
                    template: function (row, index, datatable) {
                        var viewBtn = '<a href="javascript:loadMatData(\'' + row.matId + '\',' + 1 + ',' + 1 + ',' + 1 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-ellipsis-h"></i></a>';
                        return viewBtn;
                    }
                }
            ]
        });
        datatable.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
    };
    var search = function () {
        datatable.search($('#input_mat_global_search').val(), 'keyword');
    };
    var searchClear = function () {
        $('#input_mat_global_search').val('');//搜索框置空
        search();
    };
    var reload = function () {
        datatable.reload();
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
            data: {itemId: itemId},
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
    inMat['itemId'] = itemId;
    inMat['isInput'] = isInputView;
    inMat['matIds'] = infoIds;
    $.ajax({
        type: 'post',
        url: INOUT_URL_PREFIX + 'addGlobalMatAsInout.do',
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
                        OutMatDatatable.reload();
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
    } else {
        if ($('#container_is_global_share').hasClass('hid')) {
            $('#container_is_global_share').removeClass('hid');
            $('#container_is_global_share').find('input').attr('disabled', false);
            $('#input_hidden_is_global_share').attr('disabled', true);
        }
    }
}

/*设置状态*/
function goSetp(setp) {
    $('#wizard_' + setp).addClass('wizard-step_current').siblings('.wizard-step').removeClass('wizard-step_current');//控制窗体内容
    $('#dialog_' + setp + '_title').removeClass('hid').siblings('h5').addClass('hid');//控制窗口标题
    if ('mat' === setp) {
        if (!$('#btn_back_mat_global').hasClass('hid')) {
            $('#btn_back_mat_global').addClass('hid');
        }
    }
}

//弹出材料导入列表框
function chooseGlobalMat() {
    globalMatDialogSign = 1;
    $("#btn_mat_global_select").text("选择");
    $("#globalMatListDiv").show();
    $("#globalMatOperaDiv").hide();
    $("#dialogMatGlobalCloseBtn").hide();
}

function globalMatDialogChoose() {
    if (globalMatDialogSign == 1) {
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
        var infoNames = '';
        datatable.getSelectedRecords().each(function () {
            var codetemp = $(this).find("td[data-field='matCode']").find("span").text();
            var nametemp = $(this).find("td[data-field='matName']").find("span").text();
            var showtemp = codetemp + "【" + nametemp + "】";
            infoNames = infoNames + showtemp + ',';
        });
        if (infoNames.length > 0 && infoIds.length > 0) {
            $("#globalMatIds").val(infoIds);
            $("#global_mat_show_input").val(infoNames.substring(0, infoNames.length - 1));
            $("#globalMatListDiv").hide();
            $("#globalMatOperaDiv").show();
            $("#dialogMatGlobalCloseBtn").show();
            $("#btn_mat_global_select").text("保存");
            globalMatDialogSign = 0;
        }
    } else {
        if ($("#globalMatIds").val().length > 0) {
            var inMat = {};
            inMat['itemId'] = itemId;
            inMat['isInput'] = isInputView;
            inMat['matIds'] = $("#globalMatIds").val();
            inMat['isStateIn'] = $('input[type=radio][name=globalMatIsStateInout]:checked').val();
            inMat['itemStateId'] = $('#globalMatItemStateId').val();
            $.ajax({
                type: 'post',
                url: INOUT_URL_PREFIX + 'addGlobalMatAsInout.do',
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
        } else {
            swal('提示', '请选择导入材料后操作！', 'info');
        }
    }
}

function globalMatDialogClose() {
    if (globalMatDialogSign == 1) {
        globalMatDialogSign = 0;
        $("#globalMatListDiv").hide();
        $("#globalMatOperaDiv").show();
        $("#dialogMatGlobalCloseBtn").show();
        $("#btn_mat_global_select").text("保存");
    } else {
        $("#dialog_mat").modal("hide");
    }
}


function downloadDoc(type) {
    var fileId;
    if(type==0){
        fileId = $('#templateDoc').val();
    }else if(type==1){
        fileId = $('#sampleDoc').val();
    }
    window.location.href = MAT_URL_PREFIX + 'downloadItemMatDoc.do?detailId=' + fileId;
}

function deleteDoc(type) {
    var data = {};
    data.type = type;
    data.matId = $('#matId').val();
    if(type==0){
        data.detailId = $('#templateDoc').val();
    }else if(type==1){
        data.detailId = $('#sampleDoc').val();
    }
    $.ajax({

        type: 'post',
        url: MAT_URL_PREFIX + 'deleteItemMatDoc.do',
        // dataType: 'json',
        data: data,
        // contentType: false,
        // processData: false,
        success: function (result) {
            if (result.success) {
                agcloud.ui.metronic.showSuccessTip('删除成功！', 1500);
                // 隐藏模式框
                // $('#dialog_mat_global').modal('hide');
                // // 列表数据重新加载
                // global_material_index_table.bootstrapTable('refresh');
                if(type==0){
                    $('#templateDocButton').removeClass("hide");
                    $('#templateDocButton').addClass("hide");
                    $('#templateDocFileDiv').removeClass("hide");
                    $('#templateDoc').val('');
                }else if(type==1){
                    $('#sampleDocButton').removeClass("hide");
                    $('#sampleDocButton').addClass("hide");
                    $('#sampleDocFileDiv').removeClass("hide");
                    $('#sampleDoc').val('');
                }
            } else {
                agcloud.ui.metronic.showSwal({type: 'error', message: '删除失败!'});
            }
        }
    });

}


function clearFile(file) {
    file.after(file.clone().val(""));
    file.remove();
}

function clearAllFile() {
    clearFile($("#templateDocFile"));
    clearFile($("#sampleDocFile"));


}