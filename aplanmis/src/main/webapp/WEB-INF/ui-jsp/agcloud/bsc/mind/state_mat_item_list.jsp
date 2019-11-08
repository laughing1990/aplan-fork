<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_state_mat_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_state_mat_item_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_mat_item_modal_title">事项列表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="matKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                        <span><i class="la la-search"></i></span>
                                    </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchStateMatItem();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearMatSearch();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="stage_state_mat_item_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="stage_state_mat_item_list_tb"
                                data-toggle="table",
                                data-click-to-select=true,
                                queryParamsType="",
                                data-pagination="true",
                                data-page-size="10",
                                data-page-list="[10]",
                                data-side-pagination="server",
                                data-query-params="stageStateMatItemParam",
                                data-pagination-show-page-go="true",
                                data-url="${pageContext.request.contextPath}/aea/par/stage/item/listAeaParStageItemEasyuiPageInfo.do">
                            <thead>
                                <tr>
                                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                                        data-width="10" data-formatter="checkOverAllMatIds"></th>
                                    <th data-field="stageItemId"  data-align="left" data-width="1">ID</th>
                                    <th data-field="isOptionItem" data-align="left" data-width="85"
                                        data-formatter="isOptionItemFormatter">事项类型</th>
                                    <th data-field="itemCode" data-align="left" data-width=200>事项编号</th>
                                    <th data-field="itemName" data-align="left" data-width=350 data-formatter="itemOrgNameFormatter">事项名称</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="saveStateMatCertItem" type="button" class="btn btn-info" onclick="saveStateMatItem();">保存</button>
                <button type="button" class="btn btn-secondary"
                        onclick="$('#view_state_mat_item_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    var commonQueryParams = {};
    var stageIdVal = null;
    var stateIdVal = null;
    var matIdVal = null;
    var fileTypeVal = null;
    var stateMatItemflag = true;
    var isCommonVal = false;
    var overAllMatIds = [];  //全局数组
    var stateMatItemData = null; // 事项数据

    $(function(){

        $('#isOptionItemSearch2').change(function(){
            searchStateMatItem();
        });

    });

    function viewStageMatItem(stageId, stateId, matId, fileType, isCommon, parentNodeTypeCode){

        stateMatItemData = null;
        stateMatItemflag = true;
        overAllMatIds = [];
        stageIdVal  = stageId;
        stateIdVal  = stateId;
        matIdVal = matId;
        fileTypeVal = fileType;

        if(isCommon){
            isCommonVal = true;
        }else{
            isCommonVal = false;
        }
        if (parentNodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {
            isCommonVal = true;
        }
        $('#stage_state_mat_item_list_tb_scroll').animate({scrollTop: 0}, 800);//滚动到顶部

        clearMatSearch();
        $("#view_state_mat_item_modal").modal("show");
    }


    // 查询
    function searchStateMatItem(){

        stateMatItemData = null;
        commonQueryParams = {};
        $("#stage_state_mat_item_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#stage_state_mat_item_list_tb').bootstrapTable('hideColumn', 'stageItemId');
        $("#stage_state_mat_item_list_tb").bootstrapTable('refresh',commonQueryParams);       //无参数刷新

        queryParStateMatItem();

        $("#stage_state_mat_item_list_tb").on('load-success.bs.table', function () {
            checkByParStateMatItem();
        });

        $('#stage_state_mat_item_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
            var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
            matExamine(e.type, datas);                                 // 保存到全局 Set() 里
        });
    }

    //参数设置
    function stageStateMatItemParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        commonQueryParams['pageNum'] = pageNum;
        commonQueryParams['pageSize'] = params.limit;
        commonQueryParams['stageId'] = stageIdVal;

        if(!isCommonVal) {
            commonQueryParams['parStateId'] = stateIdVal;
        }
        commonQueryParams['keyword'] =  $('#matKeyword').val();
        return commonQueryParams;
    }

    function saveStateMatItem() {

        var stageItemIds = [];
        if(overAllMatIds!=null&&overAllMatIds.length>0) {
            for (var i = 0; i < overAllMatIds.length; i++) {
                stageItemIds.push(overAllMatIds[i]);
            }
        }
        var jsonData = {
            "stateId":stateIdVal,
            "stageItemIds": stageItemIds.join(","),
            "matId":matIdVal,
            "stageId":stageIdVal,
            "fileType":fileTypeVal
        };
        if(isCommonVal){
            jsonData = {
                "stageItemIds": stageItemIds.join(","),
                "matId": matIdVal,
                "stageId":stageIdVal,
                "fileType":fileTypeVal
            };
        }
        $.ajax({
            url: ctx + '/aea/par/state/item/saveAeaParStateItemByStageMatItemIds.do',
            type: 'POST',
            data: jsonData,
            async: false,
            success: function (result) {
                if (result.success) {
                    agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                }else{
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function () {
                agcloud.ui.metronic.showSwal({message: '保存失败!', type: 'error'});
            }
        });
    }

    function clearMatSearch() {

        //搜索框置空
        $('#matKeyword').val('');
        $('#isOptionItemSearch2 option:eq(0)').prop("selected", 'selected');
        searchStateMatItem();

    }

    function queryParStateMatItem() {

        var jsonData = {"parStateId":stateIdVal,"matOrCertId":matIdVal,"stageId":stageIdVal,"fileType":fileTypeVal,"isStateIn":"1"};
        if(isCommonVal){
            jsonData = {"matOrCertId":matIdVal,"stageId":stageIdVal,"fileType":fileTypeVal,"isStateIn":"0"};
        }
        $.ajax({
            url: ctx + '/aea/par/stage/item/in/listAeaParStageItemInByMatOrCertId.do',
            type: 'POST',
            data: jsonData,
            async: false,
            success: function (data) {
                if (data != null && data.length>0) {
                    stateMatItemData = data;
                }
            },
            error: function () {
                agcloud.ui.metronic.showSwal({message: '查询关联事项信息失败!', type: 'error'});
            }
        });
    }

    /**
     * 选中数据
     */
    function checkByParStateMatItem(){

        if(stateMatItemData!=null&&stateMatItemData.length>0){
            for(var i=0;i<stateMatItemData.length;i++) {
                $("#stage_state_mat_item_list_tb").bootstrapTable("checkBy", {
                    field: "stageItemId",
                    values: [stateMatItemData[i].stageItemId]
                });
            }
        }
    }

    function matExamine(type,datas){

        if(type.indexOf('uncheck')==-1){

            $.each(datas,function(i,v){
                // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                if(overAllMatIds.indexOf(v.stageItemId) == -1){
                    overAllMatIds.push(v.stageItemId);
                }
            });
        }else{

            $.each(datas,function(i,v){
                var j = overAllMatIds.indexOf(v.stageItemId);
                overAllMatIds.splice(j,1);    //删除取消选中行
                overAllNames.splice(j,1);
            });
        }
    }

    function checkOverAllMatIds(i,row){

        if($.inArray(row.stageItemId,overAllMatIds)!=-1){// 因为 判断数组里有没有这个 id
            return {
                checked : true               // 存在则选中
            }
        }
    }

</script>