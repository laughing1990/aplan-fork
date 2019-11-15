<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row {
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="item_overmat_import" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="item_overmat_import_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 950px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="item_overmat_import_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4" style="text-align: left;">
                            <button id="btn_mat_global_select" type="button" class="btn btn-info">选择材料</button>&nbsp;&nbsp;
                            <button type="button" class="btn btn-secondary" onclick="globalMatDialogClose();">关闭页面
                            </button>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="item_overmat_import_search" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info"
                                            onclick="globalMatSearch();">查询
                                    </button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="globalMatSearchClear();">清空
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 开始 -->
                <div id="select_item_inout_global_mat_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="tb_item_overmat111"></table>
                    </div>
                </div>
                <!-- 列表区域 结束 -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var datatable;
    function loadFormFieldData(keyword, stateId, busiType, isCommon) {

        var isCommonValue = '0';
        if (isCommon) {
            isCommonValue = '1';
        }
        var getMatListUrl = null;
        var getMatListData = {};
        if (busiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

            getMatListData = {
                'stageId': currentBusiId,
                'isStateIn': '1',
                'parStateId': stateId,
                'isCommon': isCommonValue,
                'isGlobalShare': '1',
                'isActive': '1',
                'isDeleted': '0',
                'keyword': keyword,
                'busiType': busiType,
            };

            if (isCommon) {

                getMatListData = {
                    'stageId': currentBusiId,
                    'isStateIn': '0',
                    'isCommon': isCommonValue,
                    'isGlobalShare': '1',
                    'isActive': '1',
                    'isDeleted': '0',
                    'keyword': keyword,
                    'busiType': busiType,
                };

            }

        } else if (busiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {

            getMatListData = {
                'itemVerId': currentBusiId,
                'stateVerId':currentStateVerId,
                'isStateIn': '1',
                'itemStateId': stateId,
                'isInput': '1',
                'isGlobalShare': '1',
                'isCommon': isCommonValue,
                'isActive': '1',
                'isDeleted': '0',
                'keyword': keyword,
                'busiType': busiType,

            };

            if (isCommon) {

                getMatListData = {
                    'itemVerId': currentBusiId,
                    'stateVerId':currentStateVerId,
                    'isStateIn': '0',
                    'isInput': '1',
                    'isGlobalShare': '1',
                    'isCommon': isCommonValue,
                    'isActive': '1',
                    'isDeleted': '0',
                    'keyword': keyword,
                    'busiType': busiType,
                };
            }
        }

        if (datatable != null) datatable.bootstrapTable("destroy");
        datatable = $('#tb_item_overmat111').bootstrapTable({
            url: ctx + '/aea/item/mat/allListAeaItemMatPage.do',
            method: 'POST',
            dataType: 'json',
            columns: [
                {
                    checkbox: true,
                    filed: "matId",
                    title: "#",
                    width: 30,
                    align: "center",
                    formatter: function (value, row, index) {
                        return row.itemStateId != null && row.itemStateId != '';
                    }
                },
                {
                    field: "matCode",
                    title: "材料编号",
                    align: "left",
                    width: 300,
                },
                {
                    field: "matName",
                    title: "材料名称",
                    align: "left",
                    width: 450,
                },
                {
                    field: "matProp",
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
                        var editBtn = '<a href="javascript:editMat(\'null\',\'' + row.matId + '\',\'isGlobal\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
                        var deleteBtn = '<a href="javascript:deleteMat(\'null\',\'' + row.matId + '\',\'isGlobal\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
                        return editBtn + deleteBtn;
                    }
                }
            ],
            clickToSelect: true,
            sortOrder: "asc",
            pageNumber: 1,
            pageSize: 5,
            pageList: [5],
            sidePagination: 'server',
            contentType: "application/x-www-form-urlencoded",
            pagination: true,
            maintainSelected: true,
            queryParams: function (params) {
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
                getMatListData.pagination = pagination;
                return getMatListData;
            },
        });
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

    function globalMatSearch() {

        var keyword = $('#item_overmat_import_search').val();
        loadFormFieldData(keyword, stageItemStateId, currentBusiType, stageItemStoIsCommonFlag);
    }

    function globalMatSearchClear() {

        $('#item_overmat_import_search').val('');//搜索框置空
        loadFormFieldData("", stageItemStateId, currentBusiType, stageItemStoIsCommonFlag);
    }

    function globalMatDialogClose() {

        $('#item_overmat_import').modal('hide');
    }

    function getGlobalMatChoose() {

        var idlist = [];
        var namelist = [];
        var rows = $("#tb_item_overmat111").bootstrapTable('getSelections');
        var linkMatArr = [];
        if (rows != null && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                idlist.push(row.matId);
                namelist.push(row.matName);
            }
            for (var i = 0; i < idlist.length; i++) {
                var globalMat = new Object();
                globalMat.id = idlist[i];
                globalMat.name = namelist[i];
                linkMatArr.push(globalMat);
            }
        }
        return linkMatArr;
    }
</script>