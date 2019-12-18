<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row {
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="itemMatModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="itemMatModalTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;height: 45px;">
                <h5 class="modal-title" id="itemMatModalTitle">事项材料列表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px; padding-top: 5px;">
                <ul id="myTablist" class="nav nav-tabs" role="tablist" style="margin-bottom: 5px;border-bottom: 0px;">
                    <li id="itemStateMatLi" class="nav-item">
                        <a id="itemMatTab" class="nav-link" data-toggle="tab" href="#matList">
                            <i class="la la-gear"></i>
                            情形材料
                        </a>
                    </li>
                    <li id="itemCommonMatLi" class="nav-item">
                        <a id="itemMatCommonTab" class="nav-link" data-toggle="tab" href="#commonMatList">
                            <i class="la la-gear"></i>
                            通用材料
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="matList" class="tab-pane" role="tabpanel" style="padding-top: 5px;">
                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                            <div class="row" style="margin: 0px;">
                                <div class="col-md-6 d-flex text-left">
                                    <button type="button" class="btn btn-info mr-1" id="addItemMatBtn">添加材料</button>
                                    <button type="button" class="btn btn-info mr-1" id="importItemMatBtn">导入库材料</button>
                                    <button type="button" class="btn btn-secondary" onclick="batchDelItemStateMat(false);">删除</button>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-7">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..." id="itemMatSearch"
                                                       name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-5">
                                            <button type="button" class="btn btn-info" id="itemMatSearchBtn">查询</button>
                                            <button type="button" class="btn btn-secondary" id="itemMatClearBtn">清空
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                        <!-- 列表区域 -->
                        <div id="item_mat_tab_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                            <div class="base" style="padding: 0px 5px;">
                                <table id="itemMatTable"></table>
                            </div>
                        </div>
                        <!-- 列表区域 -->
                    </div>
                    <div id="commonMatList" class="tab-pane" role="tabpanel" style="padding-top: 5px;">
                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                            <div class="row" style="margin: 0px;">
                                <div class="col-md-6 d-flex text-left">
                                    <button type="button" class="btn btn-info mr-1" id="addItemMatCommonBtn">添加材料</button>
                                    <button type="button" class="btn btn-info mr-1" id="importItemMatCommonBtn">导入库材料</button>
                                    <button type="button" class="btn btn-secondary" onclick="batchDelItemStateMat(true);">删除</button>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-7">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..."
                                                       id="itemMatCommonSearch" name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-5">
                                            <button type="button" class="btn btn-info" id="itemMatSearchCommonBtn">查询</button>
                                            <button type="button" class="btn btn-secondary" id="itemMatClearCommonBtn">清空</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                        <!-- 列表区域 -->
                        <div id="item_mat_common_tab_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                            <div class="base" style="padding: 0px 5px;">
                                <table id="itemMatCommonTable"></table>
                            </div>
                        </div>
                        <!-- 列表区域 -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">

    $(function () {

        // $(".close").click(function () {
        //     associateMats();
        // });

        $("#item_mat_tab_scroll").niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#item_mat_common_tab_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#item_mat_add_scroll').niceScroll({

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

        // 材料类别选择点击事件绑定
        $('.open-mat-type, input[name="matTypeName"]').bind('click', openSelectMatTypeModal);

        $('#selectMatTypeBtn').click(function () {

            var matTypeNodes = selectMatTypeTree.getSelectedNodes();
            if (matTypeNodes != null && matTypeNodes.length > 0) {

                var matTypeId = matTypeNodes[0].id;
                var matTypeName = matTypeNodes[0].name;
                $('#item_mat_add_form input[name="matTypeId"]').val(matTypeId);
                $('#item_mat_add_form input[name="matTypeName"]').val(matTypeName);
                // 关闭窗口
                closeSelectMatTypeZtree();
            }
        });

        $(".left-button").click(function(e) {
            e.preventDefault();
        });
    });

    var stageItemStateId;
    var itemInoutId;

    var $itemMatModal = $('#itemMatModal');
    var $itemMatSearch = $('#itemMatSearch');
    var $itemMatCommonSearch = $('#itemMatCommonSearch');
    var $itemMatTable = $('#itemMatTable');
    var $itemMatCommonTable = $('#itemMatCommonTable');
    var itemMatBootstrapTable, itemMatCommonBootstrapTabe;
    var itemMatValidator;
    var itemIsCommonTb,
        stageItemStoIsCommonFlag;  // 标记材料库搜索

    initEvents();

    function initEvents() {

        initNonCommonEvents();
        initCommonEvents();

        // //关闭modal
        // $('#itemModalCloseBtn').click(function () {
        //     $itemMatModal.modal('hide');
        // });
    }

    function initNonCommonEvents() {

        //情形材料tab
        $('#itemMatTab').click(function () {
            $itemMatSearch.val('');//搜索框置空
            loadMatTable();
        });

        //新增
        $('#addItemMatBtn, #addItemMatCommonBtn').click(function () {

            $('#item_mat_add').modal('show');
            $('#item_mat_add_title').html('添加材料');
            $('#item_mat_add_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
            $('#item_mat_add_form')[0].reset();
            if (itemMatValidator != null) {
                $("#item_mat_add_form").validate().resetForm();
            }

            $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
            $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
            $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
            $('#templateDocDiv').hide();
            $('#sampleDocDiv').hide();
            $('#reviewSampleDocDiv').hide();

            itemInoutId = "";
            if (setIsCommonValueBasedTabActivation()) {
                itemIsCommonTb = "isNotCommon";
                $("#item_mat_add_form input[name='isCommon'][value='0']").prop("checked", true);
            } else {
                itemIsCommonTb = "isCommon";
                $("#item_mat_add_form input[name='isCommon'][value='1']").prop("checked", true);//基础材料
            }

            $("#item_mat_add_form input[name='matId']").val('');
            $("#item_mat_add_form input[name='isOwner']").val('1');
            $("#item_mat_add_form input[name='isInput']").val('1');
            $("#item_mat_add_form input[name='isActive']").val('1');
            $("#item_mat_add_form input[name='isGlobalShare']").val('1');
            $("#item_mat_add_form input[name='fileType']").val('mat');
            $("#item_mat_add_form input[name='duePaperCount']").val("1");
            $("#item_mat_add_form input[name='dueCopyCount']").val("1");
            $("#item_mat_add_form input[name='paperIsRequire'][value='0']").prop("checked", true);
            $("#item_mat_add_form input[name='attIsRequire'][value='0']").prop("checked", true);
            $("#item_mat_add_form input[name='zcqy'][value='1']").prop("checked", true);  // 默认支持容缺
            $("#item_mat_add_form input[name='isOfficialDoc'][value='0']").prop("checked", true);  // 是否为批文批复
            $("#item_mat_add_form input[name='certId']").val("");
            $("#item_mat_add_form input[name='stoFormId']").val("");
            $("#item_mat_add_form input[name='stdmatId']").val('');
            $("#item_mat_add_form input[name='matProp'][value='m']").prop("checked", true);
            handleSelectMatProNew('m');

            getMatCodeByAjax();//获取材料编码
        });

        //导入
        $('#importItemMatBtn').click(function () {

            $('#item_overmat_import').modal('show');
            $('#item_overmat_import_title').html('导入库材料');
            stageItemStoIsCommonFlag = false;
            globalMatSearchClear();
            $("#btn_mat_global_select").unbind('click').click(function () {

                var rows = $("#tb_item_overmat111").bootstrapTable('getSelections');
                if (rows != null && rows.length > 0) {
                    globalMatDialogClose();
                    var dataArr = getGlobalMatChoose();
                    if (dataArr && dataArr.length > 0) {
                        var ids = [];
                        for (var i = 0; i < dataArr.length; i++) {
                            ids.push(dataArr[i].id);
                        }
                        $.ajax({
                            type: 'post',
                            url: ctx + '/aea/item/mat/saveChooseItemMatAndInout.do',
                            data: {
                                'ids': ids.join(","),
                                'itemVerId': currentBusiId,
                                'stateVerId': currentStateVerId,
                                'isStateIn': '1',
                                'itemStateId': stageItemStateId,
                                'isCommon': '0'
                            },
                            async: false,
                            cache: false,
                            success: function (result) {
                                if (result.success) {
                                    agcloud.ui.metronic.showSuccessTip('导入成功！', 1500);
                                    loadMatTable();
                                    refreshMind();
                                } else {
                                    agcloud.ui.metronic.showSwal({type: 'error', message: '导入失败!'});
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }
                } else {
                    agcloud.ui.metronic.showSwal({type: 'info', message: '请勾选材料!'});
                }
            });
        });

        $('#importItemMatCommonBtn').click(function () {

            $('#item_overmat_import').modal('show');
            $('#item_overmat_import_title').html('导入库材料');
            stageItemStoIsCommonFlag = true;
            globalMatSearchClear();
            $("#btn_mat_global_select").unbind('click').click(function () {

                var rows = $("#tb_item_overmat111").bootstrapTable('getSelections');
                if (rows != null && rows.length > 0) {
                    globalMatDialogClose();
                    var dataArr = getGlobalMatChoose();
                    if (dataArr && dataArr.length > 0) {
                        var ids = [];
                        for (var i = 0; i < dataArr.length; i++) {
                            ids.push(dataArr[i].id);
                        }
                        $.ajax({
                            type: 'post',
                            url: ctx + '/aea/item/mat/saveChooseItemMatAndInout.do',
                            data: {
                                'ids': ids.join(","),
                                'itemVerId': currentBusiId,
                                'stateVerId': currentStateVerId,
                                'isStateIn': '0',
                                'isCommon': '1'
                            },
                            async: false,
                            cache: false,
                            success: function (result) {
                                if (result.success) {
                                    agcloud.ui.metronic.showSuccessTip('导入成功！', 1500);
                                    loadMatCommonTable();
                                    refreshMind();
                                } else {
                                    agcloud.ui.metronic.showSwal({type: 'error', message: '导入失败!'});
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }
                } else {
                    agcloud.ui.metronic.showSwal({type: 'info', message: '请勾选材料!'});
                }
            });
        });

        //选择
        $('#chooseItemMatBtn').click(function () {
            associateMats();
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

    function initCommonEvents() {

        //通用材料tab
        $('#itemMatCommonTab').click(function () {
            $itemMatCommonSearch.val('')//搜索框置空
            loadMatCommonTable();
        });

        //查询
        $('#itemMatSearchCommonBtn').click(function () {
            loadMatCommonTable();
        });

        //清空
        $('#itemMatClearCommonBtn').click(function () {
            $itemMatCommonSearch.val('')//搜索框置空
            loadMatCommonTable();
        });
    }

    function loadMatTable() {

        if (itemMatBootstrapTable != null) {
            itemMatBootstrapTable.bootstrapTable('destroy');
        }
        itemMatBootstrapTable = $itemMatTable.bootstrapTable({
            url: ctx + '/aea/item/inout/getMatAndInoutList.do',
            queryParams: {
                'keyword': $itemMatSearch.val(),
                'itemVerId': currentBusiId,
                'stateVerId': currentStateVerId,
                'isStateIn': "1",
                'stateId': stageItemStateId,
                'isCommon': "0",
            },
            height: '100%',
            undefinedText: '-',
            striped: false,
            cache: false,
            contentType: 'application/json',
            pagination: true,
            onlyInfoPagination: false,
            sidePagination: 'client',
            clickToSelect: false,
            pageNumber: 1,
            pageSize: 5,
            pageList: [5],
            paginationShowPageGo: true,
            paginationVAlign: 'bottom',
            paginationHAlign: 'right',
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            columns: [
                {
                    field: "inoutId",
                    visible: false
                },
                {
                    checkbox: true,
                    filed: "matId",
                    title: "#",
                    width: 30,
                    align: "center",
                    formatter: function (value, row, index) {
                        return true;
                    }
                },
                {
                    field: "aeaItemMat.matCode",
                    title: "材料编号",
                    align: "left",
                    width: 300,
                },
                {
                    field: "aeaItemMat.matName",
                    title: "材料名称",
                    align: "left",
                    width: 450,
                },
                {
                    field: "aeaItemMat.matProp",
                    title: "材料性质",
                    align: "center",
                    width: 110,
                    formatter: matPropormatter
                },
                {
                    field: '_operate',
                    title: '操作',
                    sortable: false,
                    width: 150,
                    align: "center",
                    formatter: function (value, row, index) {

                        var editBtn = '<a href="javascript:editMat(\'' + row.inoutId + '\',\'' + row.matId + '\',\'isNotCommon\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                        var deleteBtn = '<a href="javascript:deleteMat(\'' + row.inoutId + '\',\'' + row.matId + '\',\'isNotCommon\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return editBtn + deleteBtn;
                    }
                }
            ]
        });
    }

    function loadMatCommonTable() {

        if (itemMatCommonBootstrapTabe != null) {
            itemMatCommonBootstrapTabe.bootstrapTable('destroy');
        }
        itemMatCommonBootstrapTabe = $itemMatCommonTable.bootstrapTable({
            url: ctx + '/aea/item/inout/getMatAndInoutList.do',
            queryParams: {
                'keyword': $itemMatCommonSearch.val(),
                'itemVerId': currentBusiId,
                'stateVerId': currentStateVerId,
                'isStateIn': "0",
                'isCommon': "1",
            },
            height: '100%',
            undefinedText: '-',
            striped: false,
            cache: false,
            contentType: 'application/json',
            pagination: true,
            onlyInfoPagination: false,
            sidePagination: 'client',
            clickToSelect: false,
            pageNumber: 1,
            pageSize: 5,
            pageList: [5],
            paginationShowPageGo: true,
            paginationVAlign: 'bottom',
            paginationHAlign: 'right',
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            columns: [
                {
                    field: "inoutId",
                    visible: false
                },
                {
                    checkbox: true,
                    filed: "matId",
                    title: "#",
                    width: 30,
                    align: "center",
                    formatter: function (value, row, index) {
                        return true;
                    }
                },
                {
                    field: "aeaItemMat.matCode",
                    title: "材料编号",
                    align: "left",
                    width: 300,
                },
                {
                    field: "aeaItemMat.matName",
                    title: "材料名称",
                    align: "left",
                    width: 450,
                },
                {
                    field: "aeaItemMat.matProp",
                    title: "材料性质",
                    align: "center",
                    width: 110,
                    formatter: matPropormatter
                },
                {
                    field: '_operate',
                    title: '操作',
                    sortable: false,
                    width: 150,
                    align: "center",
                    formatter: function (value, row, index) {

                        var editBtn = '<a href="javascript:editMat(\'' + row.inoutId + '\',\'' + row.matId + '\',\'isCommon\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                        var deleteBtn = '<a href="javascript:deleteMat(\'' + row.inoutId + '\',\'' + row.matId + '\',\'isCommon\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return editBtn + deleteBtn;
                    }
                }
            ]
        });
    }

    function getMatCodeByAjax() {

        $.ajax({
            url: ctx + '/bsc/rule/code/getOneOrgAutoCode.do',
            type: 'post',
            data: {'codeIc': "AEA-ITEM-MAT-CODE"},
            async: false,
            cache: false,
            success: function (data) {
                $("#item_mat_add_form  input[name='matCode']").val(data);
            }
        });
    }

    function editMat(inoutId, matId, isCommon) {

        $('#item_mat_add').modal('show');
        $('#item_mat_add_title').html('编辑材料');
        $('#item_mat_add_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#item_mat_add_form')[0].reset();
        if (itemMatValidator != null) {
            $("#item_mat_add_form").validate().resetForm();
        }

        itemInoutId = inoutId;
        itemIsCommonTb = isCommon;

        $("#item_mat_add_form input[name='matId']").val('');
        $("#item_mat_add_form input[name='fileType']").val('mat');
        $("#item_mat_add_form input[name='certId']").val("");
        $("#item_mat_add_form input[name='stoFormId']").val("");

        $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $('#templateDocDiv').hide();
        $('#sampleDocDiv').hide();
        $('#reviewSampleDocDiv').hide();

        $.ajax({
            url: ctx + '/aea/item/mat/getAeaItemMat.do',
            type: 'post',
            data: {'id': matId},
            async: false,
            success: function (data) {
                if (data) {

                    // 记载表单数据
                    loadFormData(true, '#item_mat_add_form', data);

                    if (data.templateDocCount && data.templateDocCount != 0) {

                        $('#templateDocDiv').show();
                        $('#templateDocButton').html(data.templateDocCount + "个附件");
                    } else {
                        $('#templateDocDiv').hide();
                    }

                    if (data.sampleDocCount && data.sampleDocCount != 0) {

                        $('#sampleDocDiv').show();
                        $('#sampleDocButton').html(data.sampleDocCount + "个附件");
                    } else {
                        $('#sampleDocDiv').hide();
                    }

                    if (data.reviewSampleDocCount && data.reviewSampleDocCount != 0) {

                        $('#reviewSampleDocDiv').show();
                        $('#reviewSampleDocButton').html(data.reviewSampleDocCount + "个附件");
                    } else {
                        $('#reviewSampleDocDiv').hide();
                    }
                    if (data.matFrom != null) {

                        var matFromData = data.matFrom;
                        var matFromDataArray = matFromData.split(",");
                        ckAll = document.getElementsByName("matFromCb");
                        ck1 = document.getElementById("matFrom1");
                        ck2 = document.getElementById("matFrom2");
                        ck3 = document.getElementById("matFrom3");
                        ck4 = document.getElementById("matFrom4");
                        ck5 = document.getElementById("matFrom5");
                        for (qee in matFromDataArray) {
                            if (matFromDataArray[qee] == 1) {
                                ck1.checked = true;
                            }
                            if (matFromDataArray[qee] == 2) {
                                ck2.checked = true;
                            }
                            if (matFromDataArray[qee] == 3) {
                                ck3.checked = true;
                            }
                            if (matFromDataArray[qee] == 4) {
                                ck4.checked = true;
                            }
                            if (matFromDataArray[qee] == 5) {
                                ck5.checked = true;
                            }
                        }
                    }

                    if (data.matProp){
                        handleSelectMatProNew(data.matProp);
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    function deleteMat(inoutId, matId, isCommon) {

        var msg = "此操作将删除材料与事项关联数据,您确定删除吗?";
        var url = ctx + '/aea/item/inout/deleteAeaItemInoutCascade.do';
        var data = {'id': inoutId};
        if (isCommon == 'isGlobal') {
            msg = "此操作将从材料库删除材料数据,您确定删除吗?";
            url = ctx + '/aea/item/mat/deleteAeaItemMatById.do';
            data = {'id': matId};
        }
        swal({
            title: '提示信息',
            text: msg,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: data,
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 刷新表格
                            if (isCommon == 'isNotCommon') { // 情形材料
                                loadMatTable();
                                refreshMind();
                            } else if (isCommon == 'isCommon') { // 通用
                                loadMatCommonTable();
                                refreshMind();
                            } else if (isCommon == 'isGlobal') {
                                globalMatSearch();
                            }
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    }

    function initFormValidator() {

        itemMatValidator = $("#item_mat_add_form").validate({
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
                                return $("#item_mat_add_form input[name='matId']").val();
                            },
                            matName: function () {
                                return $("#item_mat_add_form input[name='matName']").val();
                            },
                            isCommon: function () {
                                return $("#item_mat_add_form input[name='isCommon']").val();
                            },
                        }
                    }
                },
                matCode: {
                    required: true,
                    maxlength: 200,
                    remote: {
                        url: ctx + '/aea/item/mat/checkMatCode.do', //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "json",           //接受数据格式
                        data: {   //要传递的数据
                            matId: function(){
                                return $("#item_mat_add_form input[name='matId']").val();
                            },
                            matCode: function() {
                                return $("#item_mat_add_form input[name='matCode']").val();
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
            submitHandler: function (form) {

                var isStateIn;
                if (setIsCommonValueBasedTabActivation()) {
                    isStateIn = "1";
                } else {
                    isStateIn = "0";
                }
                $('input[name="inoutId"]').val(itemInoutId);
                $('input[name="itemVerId"]').val(currentBusiId);
                $('input[name="stateVerId"]').val(currentStateVerId);
                $('input[name="isStateIn"]').val(isStateIn);
                $('input[name="itemStateId"]').val(stageItemStateId);

                var duePaperCount = $('#item_mat_add_form input[name="duePaperCount"]').val();
                var dueCopyCount = $('#item_mat_add_form input[name="dueCopyCount"]').val();
                if(!duePaperCount){
                    $('#item_mat_add_form input[name="duePaperCount"]').val('1');
                }
                if(!dueCopyCount){
                    $('#item_mat_add_form input[name="dueCopyCount"]').val('1');
                }

                $("#uploadProgress").modal("show");
                $('#mat_save').hide();
                $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

                var formData = new FormData(document.getElementById("item_mat_add_form"));
                var url;
                if (!$('input[name="matId"]').val()) {
                    url = ctx + '/mat/controller/add/one';
                } else {
                    url = ctx + '/mat/controller/edit/one'
                }
                $.ajax({
                    type: 'post',
                    url: url,
                    dataType: "json",
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function(){
                                swal({
                                    type: 'success',
                                    title: '保存成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                // 隐藏模式框
                                $("#uploadProgress").modal('hide');
                                $('#mat_save').show();
                                $('#item_mat_add').modal('hide');
                                if (itemIsCommonTb == 'isNotCommon') {
                                    loadMatTable();
                                    refreshMind();
                                } else if (itemIsCommonTb == 'isCommon') {
                                    loadMatCommonTable();
                                    refreshMind();
                                }

                            },500);
                        } else {

                            setTimeout(function(){
                                $('#mat_save').hide();
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function(){
                            $('#mat_save').hide();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }

    function setIsCommonValueBasedTabActivation() {

        var href = $("#myTablist a.active").attr("href");
        return href == '#matList';
    }

    function loadData4ItemMatModalWhenOpening(stateId) {

        stageItemStateId = stateId;
        if(stateId){
            $('#itemMatTab').trigger('click');
        }else{
            $('#itemMatCommonTab').trigger('click');
        }
        initFormValidator();
    }

    function associateMats() {

        var idList = [];
        if (!itemMatBootstrapTable) {return;}
        var rows = itemMatBootstrapTable.bootstrapTable('getSelections');
        for (var i = 0; i < rows.length; i++) {
            idList.push(rows[i].inoutId);
        }
        $.ajax({
            type: 'post',
            url: ctx + '/rest/mind/associateMats.do',
            dataType: 'json',
            data: {
                'inoutIdList': idList.toString(),
                'itemStateId': stageItemStateId,
                'itemVerId': currentBusiId
            },
            success: function (result) {
                if (result.success) {
                    refreshMind();
                } else {
                    agcloud.ui.metronic.showSwal({type: 'error', message: result.message});
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    // 批量删除
    function batchDelItemStateMat(isCommon){

        var msg = '此操作将删除材料与当前情形的关联,确定要执行吗？';
        if(isCommon){
            msg = '此操作将删除材料与当前事项的关联,确定要执行吗？';
        }

        var rows = null;
        if(isCommon){
            if (!itemMatCommonBootstrapTabe) {return;}
            rows = itemMatCommonBootstrapTabe.bootstrapTable('getSelections');
        }else{
            if (!itemMatBootstrapTable) {return;}
            rows = itemMatBootstrapTable.bootstrapTable('getSelections');
        }
        if (rows != null && rows.length > 0) {
            var inoutIds = [];
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                inoutIds.push(row.inoutId);
            }
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
                        url: ctx + '/aea/item/inout/batchDeleteAeaItemInoutCascade.do',
                        type: 'post',
                        data: {'ids': inoutIds.toString()},
                        async: false,
                        cache: false,
                        success: function (result) {
                            if (result.success) {
                                agcloud.ui.metronic.showSuccessTip('删除成功！', 1500);
                                if(isCommon){
                                    $('#itemMatClearCommonBtn').trigger('click');
                                    refreshMind();
                                }else{
                                    $('#itemMatClearBtn').trigger('click');
                                    refreshMind();
                                }
                            }else{
                                agcloud.ui.metronic.showSwal({type: 'error', message: '删除失败!'});
                            }
                        }
                    });
                }
            });
        }else{
            agcloud.ui.metronic.showSwal({type: 'info', message: '请选择记录!'});
        }
    }
</script>