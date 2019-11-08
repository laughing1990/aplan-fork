<%@ page contentType="text/html;charset=UTF-8" %>

<div id="mainContentPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__body" style="padding: 10px 0px;height:96%">
        <div class="m-form m-form--label-align-right m--margin-bottom-5" style="display: inline;">
            <div class="" style="margin: 0px;display: flex;justify-content: space-between; padding-left: 21px;">
                <div class=""style="text-align: left;">
                    <button type="button" class="btn btn-info" onclick="addMat(1);">添加材料</button>
                    <button type="button" class="btn btn-info" onclick="openGlogbalMatSelect(1);">导入库材料</button>
                    <button type="button" class="btn btn-info" onclick="openCertSelect(1);">添加证照</button>
                    <button type="button" class="btn btn-info" onclick="addStageItem('${currentBusiId}');">设置材料事项</button>
                    <button type="button" class="btn btn-secondary" onclick="refresh();">刷新</button>
                </div>
                <div class=""style="text-align: right;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-6" style="text-align: right;">
                            <form id="search_aea_par_in_item_form" method="post">
                                <div class="m-input-icon m-input-icon--left">
                                    <input type="text" class="form-control m-input"
                                           placeholder="请输入关键字..." name="keyword" value=""/>
                                    <span class="m-input-icon__icon m-input-icon__icon--left">
										<span><i class="la la-search"></i></span>
									</span>
                                </div>
                            </form>
                        </div>
                        <div class="col-6"  style="text-align: center;">
                            <button type="button" class="btn btn-info"
                                    onclick="searchAeaParItemIn()">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchAeaParItemIn()">清空</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
        <!-- 列表区域 -->
        <div class="base" style="padding: 0px 5px;">
            <table  id="inId_Item_list_table"
                    data-toggle="table"
                    data-click-to-select=true
                    queryParamsType=""
                    data-page-size="10"
                    data-page-list="[10,20,30,50,100]",
                    data-pagination=true
                    data-side-pagination="server"
                    data-pagination-detail-h-align="left"
                    data-query-params="stageStateItemParam"
                    data-url="${pageContext.request.contextPath}/aea/par/in/listItemParIn.do">
                <thead>
                <tr>
                    <th data-field="#" data-checkbox="true" data-align="center"
                        data-colspan="1" data-width="10"></th>
                    <th data-field="aeaMatCertName" data-align="left"
                        data-colspan="1" data-width="150">材料名称</th>
                    <th data-field="fileType" data-align="left" data-formatter="checkFileType"
                        data-colspan="1" data-width="150">材料类别</th>
                    <th data-field="itemName" data-align="left"
                        data-colspan="1" data-width="200">关联事项</th>
                    <th data-field="" data-formatter="orgRelItemListOperatorFormatter"
                        data-align="center" data-colspan="1" data-width="100">操作</th>
                </tr>
                </thead>
            </table>
        </div>
        <!-- 列表区域end -->
    </div>
</div>

<script type="text/javascript">

    $(function(){

        $('#inId_Item_list_table').bootstrapTable('resetView',{
            height: $(document.body).height()
        });
    });

    var inId = "";
    var commonQueryParams= {};

    //参数设置
    function stageStateItemParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        commonQueryParams['pageNum'] = pageNum;
        commonQueryParams['pageSize'] = params.limit;
        commonQueryParams['stageId'] = currentBusiId;
        return commonQueryParams;
    }

    //组织关联事项列表相关操作
    function orgRelItemListOperatorFormatter(value, row, index) {
        var isGlobalShare = false;
        if("1"==row.isGlobalShare){
            isGlobalShare = true;
        }

        var editInBtn = '<a href="javascript:editMat('+isGlobalShare+',\'' + row.matId + '\',\''+row.inId+'\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
            'title="编辑材料"><i class="la la-edit"></i>' +
            '</a>';

        var editBtn = '<a href="javascript:editItemParInByInId(\'' + row.inId + '\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill"' +
            'title="编辑事项关联"><i class="la la-cog"></i>' +
            '</a>';
        var deleteBtn = '<a href="javascript:deleteItemParInByInId(\''+row.inId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a>';

        if("mat"==row.fileType) {
            return editInBtn + editBtn + deleteBtn;
        }else{
            return editBtn + deleteBtn;
        }
    }

    //编辑材料下的事项
    function editItemParInByInId(id) {

        inId = id;
        $('#add_item_in_modal').modal('show');
        $('#add_item_in_modal_title').html('编辑材料关联事项配置');
        itemInDatatable.init();
        itemInDatatable.getDatatable();
    }

    var itemInDatatable = function () {

        var datatable;
        var getDatatable = function () {
            return datatable;
        };
        var mDatatableInit = function () {
            if (datatable != null) datatable.bootstrapTable("destroy");
            datatable = $('#item_in_table').bootstrapTable({
                url:ctx + '/aea/par/stage/item/listStageItemByInId.do',
                columns: [
                    {
                        checkbox: true,
                        align: "center",
                        formatter: function (value,row,index) {
                            if (row.check == true)
                                return {
                                    checked : true//设置选中
                                };
                            return value;
                        }
                    }, {
                        field: "isCheck",
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
                        field: "itemName",
                        title: "事项名称",
                        align: "center",
                        width: "100%",
                    }, {
                        field: "itemCode",
                        title: "事项编号",
                        align: "center",
                        width: "100%",
                    }
                ],
                sortOrder: "asc",
                pagination: false,
                queryParams: {stageId: currentBusiId, inId: inId}
            })
        };

        return {
            init: mDatatableInit,
            getDatatable: getDatatable,
        };
    }();

    function saveParInItem(){

        var checkBoxs = $("#item_in_table").bootstrapTable('getSelections');
        var ids = [];
        if(checkBoxs!=null&&checkBoxs.length>0) {
            for (var i = 0; i < checkBoxs.length; i++) {
                ids.push(checkBoxs[i].stageItemId);
            }
            $.ajax({
                url: ctx + '/aea/par/stage/item/in/batchSaveStageItemIn.do',
                type: 'POST',
                data: {'stageItemIds': ids.toString(),'inId':inId},
                async: false,
                success: function (result) {
                    if (result.success) {
                        $('#add_item_in_modal').modal('hide');
                        //  swal('提示信息', '保存成功！', 'info');
                        // location.reload();
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        refresh();
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '保存失败！', 'error');
                }
            });
        }else {
            swal('提示信息', '请选择数据！', 'info');
        }
    }

    //刪除
    function deleteItemParInByInId(inId) {
        if(inId){
            swal({
                title: '此操作影响：',
                text: '将删除材料,关联事项数据将失效,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/par/in/deleteAeaParIn.do',
                        type: 'POST',
                        data: {'inId': inId},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    title: '提示信息',
                                    text: '删除成功!',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                refresh();
                            } else {
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error: function () {
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{

        }
    }

    // 查询
    function searchAeaParItemIn(){
        commonQueryParams = {};
        var t = $('#search_aea_par_in_item_form').serializeArray();
        $.each(t, function() {
            commonQueryParams[this.name] = this.value;
        });
        commonQueryParams['stageId'] = currentBusiId;
        $("#inId_Item_list_table").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#inId_Item_list_table").bootstrapTable('refresh');       //无参数刷新
    }

    // 清空
    function clearSearchAeaParItemIn(){

        $('#search_aea_par_in_item_form')[0].reset();
        searchAeaParItemIn();
    }

    //刷新
    function refresh() {
        searchAeaParItemIn();
    }


    function checkFileType(value,row,index) {
        if("cert"==row.fileType){
            return "电子证照";
        }else if("mat"==row.fileType){
            // if("1"==row.isGlobalShare){
            //     return "全局材料";
            // }else{
            return "材料";
            // }
        }
    }


    function checkIsCommon(value,row,index) {
        if("1"==row.isCommon){
            return "是";
        }else{
            return "否";
        }
    }

</script>