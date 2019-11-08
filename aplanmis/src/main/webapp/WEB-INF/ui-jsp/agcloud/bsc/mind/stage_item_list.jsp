<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_state_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_state_item_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_item_modal_title">事项列表</h5>
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
                                        <input id="keyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                        <span><i class="la la-search"></i></span>
                                    </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchStateItem();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearch();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="stage_state_item_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="stage_state_item_list_tb"
                                data-toggle="table",
                                data-click-to-select=true,
                                queryParamsType="",
                                data-pagination=true,
                                data-page-size="10",
                                data-page-list="[10]",
                                data-side-pagination="server",
                                data-query-params="stageStateItemParam",
                                data-pagination-show-page-go="true",
                                data-url="${pageContext.request.contextPath}/aea/par/stage/item/listAeaParStageItemEasyuiPageInfo.do">
                            <thead>
                            <tr>
                                <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                                    data-width="10" data-formatter="checkOverAllIds"></th>
                                <th data-field="stageItemId"  data-align="left" data-width="1">ID</th>
                                <th data-field="isOptionItem" data-align="left" data-width="85"
                                    data-formatter="isOptionItemFormatter">事项类型</th>
                                <th data-field="itemCode" data-align="left" data-width=200>事项编号</th>
                                <th data-field="itemName" data-align="left" data-width=350 data-formatter="itemOrgNameFormatter">事项名称</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 列表区域end -->
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" id="btn_save_stage_state_item_list_tb" >保存</button>
                <button type="button" class="btn btn-secondary" onclick="saveStateItemDialogClose();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var commonQueryParams = {};
    var stageIdVal = null;
    var stateIdVal = null;
    var overAllIds = new Array();  //全局数组
    var overAllNames = new Array();
    var stageItemFirstTime = true;
    var stageStateItemData = null;

    $(function(){

        $('#isOptionItemSearch').change(function(){
            searchStateItem();
        });
    });

    function isOptionItemFormatter(value, row, index, field) {

        if(value){
            // if(value=='0'){
            //     return '必选事项';
            // }else if(value=='1'){
            //     return '可选事项';
            // }
            if(value){
                if(value=='0'){
                    return '并联审批事项';
                }else if(value=='1'){
                    return '并行推进事项';
                }else{
                    return '前置检查事项';
                }
            }
        }
    }

    function itemOrgNameFormatter(value, row, index, field) {

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

    //判断字符是否为空的方法
    function isEmpty(obj){

        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }

    function viewStageItem(stageId,stateId){

        stageStateItemData = null;
        stageItemFirstTime = true;
        overAllIds = new Array();
        overAllNames = new Array();
        stageIdVal  = stageId;
        stateIdVal  = stateId;
        $('#stage_state_item_list_tb_scroll').animate({scrollTop: 0}, 800);

        clearSearch();
        $("#view_state_item_modal").modal("show");
    }


    // 查询
    function searchStateItem(){

        stageStateItemData = null;
        commonQueryParams = {};
        commonQueryParams['stageId'] = stageIdVal;
        commonQueryParams['keyword'] =  $('#keyword').val();
        $("#stage_state_item_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#stage_state_item_list_tb').bootstrapTable('hideColumn', 'stageItemId');
        $("#stage_state_item_list_tb").bootstrapTable('refresh',commonQueryParams);       //无参数刷新

        queryParStateItem();

        $("#stage_state_item_list_tb").on('load-success.bs.table', function () {
            checkByStageStateItem();
        });

        $('#stage_state_item_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
            var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
            examine(e.type, datas);                                 // 保存到全局 Set() 里
        });
    }


    //参数设置
    function stageStateItemParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        commonQueryParams['pageNum'] = pageNum;
        commonQueryParams['pageSize'] = params.limit;
        commonQueryParams['stageId'] = stageIdVal;
        commonQueryParams['keyword'] =  $('#keyword').val();
        // commonQueryParams['isOptionItem'] = '0';  /*$('#isOptionItemSearch option:selected').val();*/
        return commonQueryParams;
    }

    function saveStateItemAndReturn() {

        var arr = new Array();
        var ids = new Array();
        if(overAllIds!=null&&overAllIds.length>0) {

            for (var i = 0; i < overAllIds.length; i++) {
                ids.push(overAllIds[i]);
                var stateItem = new Object();
                stateItem.id = overAllIds[i];
                stateItem.name = overAllNames[i];
                arr.push(stateItem);
            }
        }

        $.ajax({
            url: ctx + '/aea/par/state/item/saveAeaParStateItemByStageItemIds.do',
            type: 'POST',
            data: {"stateId":stateIdVal,"stageItemIds":ids.join(","),"stageId":stageIdVal},
            async: false,
            success: function (result) {
                if (result.success) {
                    // swal({
                    //     title: '提示信息',
                    //     text: "保存成功!",
                    //     type: 'info',
                    //     confirmButtonText: '确认!'
                    // }).then(function(result) {
                        $("#view_state_item_modal").modal("hide");
                        refreshMind();
                        // location.reload();
                    // });
                }else{
                    arr = new Array();
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function () {
                arr = new Array();
                agcloud.ui.metronic.showSwal({message: '保存失败!', type: 'error'});
            }
        });
        return arr;
    }


    function clearSearch() {

        $('#keyword').val('');//搜索框置空
        $('#isOptionItemSearch option:eq(0)').prop("selected", 'selected');
        searchStateItem();

    }

    function queryParStateItem() {

        $.ajax({
            url: ctx + '/aea/par/state/item/listStateItemByStateId.do',
            type: 'POST',
            data: {"parStateId": stateIdVal},
            async: false,
            success: function (data) {
                if (data != null && data.length>0) {
                    stageStateItemData = data;
                }
            },
            error: function () {
                agcloud.ui.metronic.showSwal({message: '查询关联事项信息失败!', type: 'error'});
            }
        });
    }

    function checkByStageStateItem(){

        if (stageStateItemData != null && stageStateItemData.length>0) {
            for (var i = 0; i < stageStateItemData.length; i++) {
                $("#stage_state_item_list_tb").bootstrapTable("checkBy", {
                    field: "stageItemId",
                    values: [stageStateItemData[i].stageItemId]
                });
            }
        }
    }

    function saveStateItemDialogClose() {

        $('#view_state_item_modal').modal('hide');
    }

    function examine(type,datas){

        if(type.indexOf('uncheck')==-1){

            $.each(datas,function(i,v){
                // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                if(overAllIds.indexOf(v.stageItemId) == -1){
                    overAllIds.push(v.stageItemId);
                    overAllNames.push(v.itemName);
                }


            });
        }else{

            // alert(type);
            $.each(datas,function(i,v){
                var j = overAllIds.indexOf(v.stageItemId);
                overAllIds.splice(j,1);    //删除取消选中行
                overAllNames.splice(j,1);
            });
        }

    }


    function addCheckStateItem(stageItemId,itemName){

        if(overAllIds.indexOf(stageItemId) == -1 ){
            overAllIds.push(stageItemId);
            overAllNames.push(itemName);
        }
    }

    function checkOverAllIds(i,row){

        if($.inArray(row.stageItemId,overAllIds)!=-1){// 因为 判断数组里有没有这个 id
            return {
                checked : true               // 存在则选中
            }
        }
    }

</script>