var commonQueryParams = [],
    aedit_item_validator,
    customTable_tree;
$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height',$(document.body).height()-20);

    $('#selectUserTree').css('height', $('#westPanel').height()-116);

    // 初始化用户树
    initSelectUserZtree();

    // 初始化事项表格
    initItemBasicTreeGrid();

    // 组织机构事项列表
    $('#all_item_list_tb').bootstrapTable('resetView', {

        height: $('#westPanel').height() - 116
    });

    // 处理滚动条
    $('#selectUserTree').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 处理事项表格滚动条
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

    // 查询
    $('#searchItemNature').change(function () {

        searchAllItemList();
    });

    // 选择行政区划
    $('#selectRegionBtn').click(function () {

        var isSelectRegionSearch = $('#isSelectRegionSearch').val();
        var bscDicRegionTree = $.fn.zTree.getZTreeObj("bscDicRegionTree");
        var nodes = bscDicRegionTree.getSelectedNodes();
        if (nodes != null && nodes.length > 0) {
            var selectNode = nodes[0];
            if (isSelectRegionSearch == 'search') {
                $("#aedit_item_form input[name='regionId']").val(selectNode.id);
                $("#aedit_item_form input[name='regionName']").val(selectNode.name);
            } else {
                $("#aedit_item_form input[name='regionId']").val(selectNode.id);
                $("#aedit_item_form input[name='regionName']").val(selectNode.name);
            }
            closeSelectBscDicRegionZtree();
        } else {
            swal('提示信息', '请选择行政区划！', 'info');
        }
    });
});

function initItemBasicTreeGrid() {

    var url = ctx + '/aea/item/user/listUserItemByPage.do';
    customTable_tree = $('#all_item_list_tb').bootstrapTable({
        url: url,
        columns: getItemBasicTreeGridColumns(),
        pagination: true,
        pageSize: 10,
        paginationHAlign: 'right',
        paginationVAlign: 'bottom',
        paginationDetailHAlign: "left",
        paginationShowPageGo: true,
        pageList: [10, 20, 50, 100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: allItemListParam,
        sidePagination: 'server',
        singleSelect: false,
        clickToSelect: true,
    });
}

//定义树表格的显示列
var getItemBasicTreeGridColumns = function () {

    var columns = [
        {
            checkbox:true,
            width: 5
        },
        {
            field: 'itemNature',
            align: 'center',
            width: 65,
            title: '事项类型',
            formatter: function (value, row) {

                var itemNature = "";
                if(row.itemNature){

                    if(value=='0'){
                        itemNature = '行政事项';
                    }else if(value=='9'){
                        itemNature = '服务协同';
                    }else if(value=='8'){
                        itemNature = '中介服务事项';
                    }else if(value=='6'){
                        itemNature = '市政公用服务';
                    }
                }
                return itemNature;
            }
        },
        {
            field: 'itemName',
            title: '事项名称',
            align: 'left',
            width: 350,
            formatter: function (value, row) {

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
        },
        {
            field: 'itemCode',
            title: '事项编码',
            width: 200,
            align: 'left'
        },
        {
            field: 'verNum',
            align: 'center',
            width: 60,
            title: '最新版本',
            formatter: function(value, row) {

                if(value){
                    if((''+value).indexOf('.00')>-1){
                        return 'V' + (''+value).substring(0,(''+value).indexOf('.00'))+'.0';
                    }else{
                        return 'V' + value;
                    }
                }
            }
        },
        {
            field: 'operate_',
            align: 'center',
            title: '操作',
            width: 120,
            formatter: itemOperatorFormatter
        }
    ];
    return columns;
}

//判断字符是否为空的方法
function isEmpty(obj){

    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

function addItemBasicChildById(itemId, itemName, isCatalog, itemBasicId) {

    if (isCatalog == '0') {
        swal({
            text: '请确认是否为事项【' + itemName + '】添加子实施事项？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                addItemInfo(false, false);
                $("#aedit_item_form input[name='itemCategoryMark']").attr("readOnly", "true");
                $("#aedit_item_form input[name='parentItemId']").val(itemId);
                $.ajax({
                    url: ctx + '/aea/item/basic/getAeaItemBasic.do',
                    type: 'post',
                    data: {'id': itemBasicId},
                    async: false,
                    success: function (data) {
                        if (data) {
                            $("#aedit_item_form input[name='basecode']").val(data.itemCode);
                            $("#aedit_item_form input[name='itemCategoryMark']").val(data.itemCategoryMark);
                        }
                    }
                });
            }
        });
    } else {
        swal({
            text: '请选择子事项类型？',
            type: 'question',
            input: 'radio',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputOptions: {
                "0": "实施事项",
                "1": "标准事项"
            },
            inputValidator: function (value) {
                if (!value) {
                    return '请选择事项的类型!'
                }
            }
        }).then(function (result) {
            if (result.dismiss == 'cancel') {
                return;
            }
            addItemInfo(false, (result.value != '0'));
            $.ajax({
                url: ctx + '/aea/item/basic/getAeaItemBasic.do',
                type: 'post',
                data: {'id': itemBasicId},
                async: false,
                success: function (data) {
                    if (data) {
                        loadFormData(true, '#aedit_item_form', data);
                        $("#aedit_item_form input[name='itemId']").val('');
                        $("#aedit_item_form input[name='itemVerId']").val('');
                        $("#aedit_item_form input[name='itemBasicId']").val('');
                        $("#aedit_item_form input[name='parentItemId']").val(itemId);
                        $("#aedit_item_form input[name='basecode']").val(data.itemCode);
                        $("#aedit_item_form input[name='orgId']").val('');
                        $("#aedit_item_form input[name='isCatalog']").val((result.value != '0') ? '1' : '0');
                    }
                }
            });

        });
    }
}

function itemOperatorFormatter(value, row, index) {

    var itemTitle = "编辑";
    var icoCSS = "la la-edit";
    if (row.itemVerStatus == "1" || row.itemVerStatus == "3") {
        itemTitle = "查看";
        icoCSS = "la la-search";
    }

    var editBtn = '<a href="javascript:editItemBasicById(\'' + row.itemBasicId + '\',\'' + row.itemVerStatus + '\',\'' + row.isCatalog + '\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
                      'title="' + itemTitle + '"><i class="' + icoCSS + '"></i>' +
                  '</a>';

    // var addChildBtn = '<a href="javascript:addItemBasicChildById(\'' + row.itemId + '\',\'' + row.itemName + '\',\'' + row.isCatalog + '\',\'' + row.itemBasicId + '\',)" ' +
    //                       'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
    //                       'title="添加子事项"><i class="la la-plus"></i>' +
    //                   '</a>';

    var deleteUserItemBtn = '<a href="javascript:deleteUserItemById(\'' + row.itemUserId + '\')" ' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                'title="移除用户事项"><i class="la la-times"></i>' +
                            '</a>';

    // var deleteBtn = '<a href="javascript:deleteItemById(\'' + row.itemId + '\')" ' +
    //                     'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
    //                     'title="删除事项"><i class="la la-trash"></i>' +
    //                 '</a>';

    var setState = '<a href="javascript:openItemVerModal(\'' + row.itemId + '\',\'' + row.itemVerId + '\',\'' + row.itemNature + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="事项版本"><i class="la la-cog"></i>' +
                   '</a>';

    // var editMaterial='';
    // if(row.itemNature=='8'){
    //
    //     editMaterial='<a href="javascript:openItemVerMaterialModal(\'' + row.itemVerId + '\')" ' +
    //                      'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
    //                      'title="输出材料"><i class="la la-cogs"></i>' +
    //                  '</a>';
    // }

    return editBtn + setState + deleteUserItemBtn /*+ editMaterial*/;
}

function allItemListParam(params) {

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
    var userId = null;
    if(curSelectedUserTreeNode!=null){
        userId = curSelectedUserTreeNode.id;
    }
    commonQueryParams.push({name: 'userId', value: userId});
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

// 查询
function searchAllItemList() {

    commonQueryParams = [];
    var params = $('#search_all_item_list_form').serializeArray();
    if (params != "") {
        $.each(params, function () {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#all_item_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#all_item_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchAllItemList() {

    $('#search_all_item_list_form')[0].reset();
    $("#search_all_item_list_form input[name='orgId']").val('');
    searchAllItemList();
}

// 刷新
function refreshAllItemList() {

    searchAllItemList();
}

// 修复数据
function initItemVerSeq() {

    swal({
        text: '此操作将【启用事项最新版本为试运行版本、修复版本与序号关系、更新所有阶段关联事项为最新版本事项数据】,确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据修复中,请勿点击,耐心等候...");
            $.ajax({
                url: ctx + '/aea/item/initItemVerSeq.do',
                type: 'post',
                data: {/*'itemNature': itemNature*/},
                // async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('提示信息', "修复事项序列成功！", 'info');
                            refreshAllItemList();
                        }, 500);
                    } else {
                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', "修复事项序列失败！", 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}

//同步本地指南
function syncLocalAeaItemGuide() {

    swal({
        text: '确定将事项库的基本信息同步过去办事指南表吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {
            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据同步中,请勿点击,耐心等候...");
            $.ajax({
                url: ctx + '/aea/item/guide/syncLocalAeaItemGuide.do',
                type: 'post',
                data: {},
                // async: false,
                success: function (result) {
                    if (result.success) {
                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('提示信息', "同步本地指南成功！", 'info');
                        }, 500);
                    } else {
                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', "同步本地指南失败！", 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}

// 初始标准事项
function initStandItem() {

    swal({
        text: '此操作将为未创建标准事项的实施事项创建标准事项，您确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据创建中,请勿点击,耐心等候...");

            $.ajax({
                url: ctx + '/aea/item/initStandItem.do',
                type: 'post',
                data: {},
                // async: false,
                success: function (result) {
                    if (result.success) {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('提示信息', "创建标准事项成功！", 'info');
                            refreshAllItemList();
                        }, 500);

                    } else {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', "创建标准事项失败！", 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}

function syncItemRegion() {

    swal({
        text: '此操作将同步实施事项所属部门关联行政区域，您确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据创建中,请勿点击,耐心等候...");

            $.ajax({
                url: ctx + '/aea/item/syncItemRegion.do',
                type: 'post',
                data: {'isCatalog': '0'},
                success: function (result) {
                    if (result.success) {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('提示信息', "同步事项行政区域成功！", 'info');
                            refreshAllItemList();
                        }, 500);

                    } else {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', "同步事项行政区域失败！", 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}


function isSelectBscDicRegion(obj, isSearch) {

    if (isSearch) {
        $('#isSelectRegionSearch').val('search');
    } else {
        $('#isSelectRegionSearch').val('noSearch');
    }
    if (obj) {
        var value = $(obj).val();
        if (!value) {
            selectBscDicRegionZtree();
        }
    } else {
        selectBscDicRegionZtree();
    }
}

/**
 * 批量删除用户与事项关联
 */
function deleteUserItemById(id){

    if(!isEmpty(id)){
        swal({
            text: '此操作将移除用户所管理的事项数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/item/user/delItemUserById.do',
                    type: 'post',
                    data: {'id': id},
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除关联事项成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchAllItemList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除事项数据！", 'info');
    }
}

/**
 * 批量删除用户与事项关联
 */
function batchDelUserItem(){

    var rows = $("#all_item_list_tb").bootstrapTable('getSelections');
    if(rows!=null&&rows.length>0){
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].itemUserId);
        }
        swal({
            text: '此操作将批量移除用户所管理的事项数据，您确定执行吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/item/user/batchDelItemUserByIds.do',
                    type: 'post',
                    data: {
                        'ids': ids.toString()
                    },
                    success: function (result1) {
                        if (result1.success) {
                            swal({
                                type: 'success',
                                title: '移除关联事项成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 列表数据重新加载
                            searchAllItemList();
                        }else{
                            swal('错误信息', result1.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择需要移除事项数据！", 'info');
    }
}

function importUserItem(){

    if(curSelectedUserTreeNode!=null){

        $("#uploadProgress").modal("show");
        $('#uploadProgressMsg').html("加载数据中,请勿点击,耐心等候...");
        initUserCheckItem();
        setTimeout(function () {
            $("#uploadProgress").modal('hide');
        }, 500);

    }else{
        swal('提示信息', '请选择用户!', 'info');
    }
}

