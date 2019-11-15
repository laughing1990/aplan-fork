<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div class="m-form m-form--label-align-right m--margin-bottom-5">
    <div class="row" style="margin: 0px;">
        <div class="col-md-6"style="text-align: left;">
            <button id="add_stage_mat_btn" type="button" class="btn btn-info" onclick="addStageMat(false,'1');">添加材料</button>
            <button id="choose_stage_mat_btn" type="button" class="btn btn-info" onclick="chooseStageMat(false);">导入库材料</button>
            <button id="btn_batch_del_stage_state_mat" type="button" class="btn btn-secondary" onclick="batchDelStageStateMat(false);">删除</button>
        </div>
        <div class="col-md-6" style="padding: 0px;">
            <div class="row" style="margin: 0px;">
                <div class="col-7">
                    <div class="m-input-icon m-input-icon--left">
                        <input  id="matlistkeyword" type="text" class="form-control m-input"
                                placeholder="请输入关键字..." name="keyword" value=""/>
                        <span class="m-input-icon__icon m-input-icon__icon--left">
                            <span><i class="la la-search"></i></span>
                        </span>
                    </div>
                </div>
                <div class="col-5">
                    <button type="button" class="btn btn-info" onclick="searchStageStateMatList();">查询</button>
                    <button type="button" class="btn btn-secondary" onclick="clearStageMatListSearch();">清空</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
<!-- 列表区域 -->
<div id="stage_state_mat_tab_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
    <div class="base" style="padding: 0px 5px;">
        <table  id="stage_state_mat_list_tb"
                data-toggle="table",
                data-click-to-select=true,
                queryParamsType="",
                data-pagination="true",
                data-page-size="5",
                data-page-list="[5]",
                data-side-pagination="server",
                data-query-params="stageStateMatParam",
                data-maintain-selected = "true",
                data-url="${pageContext.request.contextPath}/aea/par/in/listStageMatByStageId.do">
            <thead>
                <tr>
                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                        data-width="10" data-formatter="checkStateMat"></th>
                    <th data-field="inId"  data-align="left" data-width="10">ID</th>
                    <th data-field="matId"  data-align="left" data-width="10">MATID</th>
                    <th data-field="aeaMatCertName" data-align="left" data-width="230">材料名称</th>
                    <th data-field="aeaMatCertCode" data-align="left" data-width="230">材料编号</th>
                    <th data-field="matProp" data-formatter="matPropormatter"
                        data-align="center" data-colspan="1" data-width="80">材料性质</th>
                    <th data-field="_operator" data-formatter="stageStateMatFormatter"
                        data-align="center" data-width="100">操作</th>
                </tr>
            </thead>
        </table>
    </div>
</div>
<!-- 列表区域end -->

<script type="text/javascript">

    var matlistCommonQueryParams = {};
    var allStateMatIds = [];  //全局数组
    var matFirstTime = true;

    // 查询
    function searchStageStateMatList(){

        matlistCommonQueryParams = {};
        matlistCommonQueryParams['stageId'] = currentBusiId;
        matlistCommonQueryParams['keyword'] =  $('#matlistkeyword').val();
        matlistCommonQueryParams['stateId'] =  stageItemStateId;
        matlistCommonQueryParams['isCommon'] =  false;
        matlistCommonQueryParams['isStateIn'] =  '1';
        $("#stage_state_mat_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#stage_state_mat_list_tb').bootstrapTable('hideColumn', 'inId');
        $('#stage_state_mat_list_tb').bootstrapTable('hideColumn', 'matId');
        $("#stage_state_mat_list_tb").bootstrapTable('refresh', matlistCommonQueryParams);

        if(matFirstTime) {

            $('#stage_state_mat_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
                var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
                examineMat(e.type, datas);                                 // 保存到全局 Set() 里
            });

            matFirstTime = false;
        }
    }


    //参数设置
    function stageStateMatParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        matlistCommonQueryParams['pageNum'] = pageNum;
        matlistCommonQueryParams['pageSize'] = params.limit;
        matlistCommonQueryParams['stageId'] = currentBusiId;
        matlistCommonQueryParams['stateId'] = stageItemStateId;
        matlistCommonQueryParams['isCommon'] =  false;
        matlistCommonQueryParams['keyword'] =  $('#matlistkeyword').val();
        matlistCommonQueryParams['isStateIn'] =  '1';
        return matlistCommonQueryParams;
    }

    function saveStageStateMat() {

        var ids = [];
        if(allStateMatIds!=null&&allStateMatIds.length>0) {
            for (var i = 0; i < allStateMatIds.length; i++) {
                ids.push(allStateMatIds[i]);
            }
            $.ajax({
                url: ctx + '/aea/par/in/updateStageStateParIn.do',
                type: 'POST',
                data: {"stateId": stageItemStateId, "inIds": ids.join(","), "stageId": currentBusiId},
                async: false,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        // location.reload();
                        refreshMind();
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    agcloud.ui.metronic.showSwal({message: '保存失败!', type: 'error'});
                }
            });
        }else {
            swal('提示信息', '请勾选材料', 'info');
        }
    }

    function clearStageMatListSearch() {

        $('#matlistkeyword').val('');//搜索框置空
        searchStageStateMatList();
    }

    function checkStateMat(i,row){

       if(row.parStateId!=null && row.parStateId==stageItemStateId) {

           if(allStateMatIds.indexOf(row.inId) == -1 ){
               allStateMatIds.push(row.inId);
           }
           return {
               checked: true
           }
       }else{
           if(allStateMatIds.indexOf(row.inId) != -1){
               return {
                   checked: true
               }
           }
       }
    }


    function stageStateMatFormatter(value, row, index) {

        var editBtn = '<a href="javascript:editStageMat(false,\''+row.matId + '\',\'1\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
            'title="编辑"><i class="la la-edit"></i>' +
            '</a>';


        var deleteBtn = '<a href="javascript:deleteStageMat(false,\''+row.inId + '\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
            'title="删除"><i class="la la-trash"></i>' +
            '</a>';

        return editBtn + deleteBtn;
    }

    function examineMat(type, datas){

        if(type.indexOf('uncheck')==-1){
            $.each(datas,function(i,v){
                // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                if(allStateMatIds.indexOf(v.inId) == -1){
                    allStateMatIds.push(v.inId);
                }


            });
        }else{

            // alert(type);
            $.each(datas,function(i,v){
                var j = allStateMatIds.indexOf(v.inId);
                allStateMatIds.splice(j,1);    //删除取消选中行
            });
        }

    }
</script>