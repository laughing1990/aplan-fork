<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_state_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_state_form_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_form_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="stateFormKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchStateForm();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchStateForm();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="stage_state_form_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="stage_state_form_list_tb"
                                data-toggle="table",
                                data-click-to-select=true,
                                queryParamsType="",
                                data-pagination=true,
                                data-side-pagination="server",
                                data-page-size="10",
                                data-page-list="[10]",
                                data-query-params="stageStateFormParam",
                                data-pagination-show-page-go="true",
                                data-url="${pageContext.request.contextPath}/aea/par/state/form/listFormAndStateFormByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                                        data-width="10" data-formatter="checkStateFormOverAllIds"></th>
                                    <th data-field="formId"  data-align="left" data-width="10">ID</th>
                                    <th data-field="formCode" data-align="left" data-width="250">表单编号</th>
                                    <th data-field="formName" data-align="left" data-width="250">表单名称</th>
                                    <th data-field="formProperty" data-align="left" data-width="100" data-formatter="formPropertyFormatter">表单类型</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 列表区域end -->
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" id="btn_save_stage_state_form_list_tb" >保存</button>
                <button type="button" class="btn btn-secondary" onclick="saveStateFormDialogClose();">关闭</button>
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
    var stageFormFirstTime = true;
    var isStateForm = "1";
    var isStageNeedStateFlag = true;
    var stageOrStateFormData = null;

    // 内置
    function isInnerFormFormatter(value, row, index, field) {

        if(value){
            if(value=='0'){
                return '自建';
            }else if(value=='1'){
                return '内置';
            }
        }
    }

    // 表单属性，meta-biz表示元数据普通表单，meta-flow表示元数据流程表单，smart-biz表示智能普通表单，smart-flow表示智能流程表单
    function formPropertyFormatter(value, row, index, field) {

        if(value){
            if(value=='meta-biz'){
                return '元数据普通表单';
            }else if(value=='meta-flow'){
                return '元数据流程表单';
            }else if(value=='smart-biz'){
                return '智能普通表单';
            }else if(value=='smart-flow'){
                return '智能流程表单';
            }
        }
    }

    /**
     *
     * @param stageId
     * @param stateId
     * @param type
     * @param isNeedState
     */
    function viewStateForm(stageId, stateId, type, isNeedState){

        stageOrStateFormData = null;
        stageFormFirstTime = true;
        isStageNeedStateFlag = isNeedState;
        //滚动到顶部
        $('#stage_state_form_list_tb_scroll').animate({scrollTop: 0}, 800);

        var title = "情形表单列表";
        overAllIds = new Array();
        overAllNames = new Array();
        stageIdVal  = stageId;
        stateIdVal  = stateId;
        if(type=='stage'){
            isStateForm = "0";
            title = "通用表单列表";
        }else if(type=='state'){
            isStateForm = "1";
            title = "情形表单列表";
        }
        clearSearchStateForm();
        $("#view_state_form_modal").modal("show");
        $("#view_stage_form_modal_title").html(title);
    }

    // 查询
    function searchStateForm(){

        stageOrStateFormData = null;
        commonQueryParams = {};
        commonQueryParams['stageId'] = stageIdVal;
        commonQueryParams['keyword'] =  $('#stateFormKeyword').val();
        $("#stage_state_form_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#stage_state_form_list_tb').bootstrapTable('hideColumn', 'formId');
        $("#stage_state_form_list_tb").bootstrapTable('refresh',commonQueryParams);       //无参数刷新

        queryParStateForm();

        $("#stage_state_form_list_tb").on('load-success.bs.table', function () {
            checkByStageOrStateForm();
        });

        $('#stage_state_form_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {

            // 点击时获取选中的行或取消选中的行
            examineStateForm(e.type, $.isArray(rows) ? rows : [rows]);
        });
    }


    //参数设置
    function stageStateFormParam(params) {

        if(params){
            var pageNum = (params.offset / params.limit) + 1;
            commonQueryParams['pageNum'] = pageNum;
            commonQueryParams['pageSize'] = params.limit;
        }else{
            commonQueryParams['pageNum'] = 1;
            commonQueryParams['pageSize'] = 10;
        }
        return commonQueryParams;
    }

    function saveStateFormAndReturn() {

        var arr = new Array();
        var ids = new Array();
        if(overAllIds!=null&&overAllIds.length>0) {
            for (var i = 0; i < overAllIds.length; i++) {
                ids.push(overAllIds[i]);
                var stateForm = new Object();
                stateForm.id = overAllIds[i];
                stateForm.name = overAllNames[i];
                arr.push(stateForm);
            }
        }

        $.ajax({
           url: ctx + '/aea/par/state/form/saveAeaParStateForms.do',
           type: 'POST',
           data:  {
               "parStageId": stageIdVal,
               "parStateId": isStateForm=='0'?'':stateIdVal,
               "formIds": ids.toString(),
               'isStateForm': isStateForm
           },
           async: false,
           success: function (result) {
               if (result.success) {
                   $("#view_state_form_modal").modal("hide");
                   refreshMind();
               }else{
                   arr = new Array();
                   swal('错误信息', result.message, 'error');
               }
           },
           error: function(XMLHttpRequest, textStatus, errorThrown) {

                arr = new Array();
                swal('错误信息', XMLHttpRequest.responseText, 'error');
           }
        });
       return arr;
    }

    function saveStageNoStateFormAndReturn() {

        var arr = [];
        var ids = [];
        if(overAllIds!=null&&overAllIds.length>0) {
            for (var i = 0; i < overAllIds.length; i++) {
                ids.push(overAllIds[i]);
                var stateForm = new Object();
                stateForm.id = overAllIds[i];
                stateForm.name = overAllNames[i];
                arr.push(stateForm);
            }
        }
        $.ajax({
            url: ctx + '/aea/par/state/form/saveAeaParStateForms.do',
            type: 'POST',
            data:  {
                "parStageId": stageIdVal,
                "parStateId": isStateForm=='0'?'':stateIdVal,
                "formIds": ids.toString(),
                'isStateForm': isStateForm
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    $("#view_state_form_modal").modal("hide");
                    swal({
                        text: '保存成功！',
                        type: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    });
                    // 刷新材料证照列表
                    searchStageNoStateInMatCet();
                }else{
                    arr = [];
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                arr = [];
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
        return arr;
    }

    // 清空查询
    function clearSearchStateForm() {

        //搜索框置空
        $('#stateFormKeyword').val('');
        searchStateForm();

    }

    function queryParStateForm() {

        var d = {};
        // 分情形
        if(isStageNeedStateFlag){
            d = {
                "parStageId": stageIdVal,
                'isStateForm': isStateForm,
                "parStateId": isStateForm=='0'?'':stateIdVal,
            }
        // 不分情形
        }else{
            d = {
                "parStageId": stageIdVal,
            }
        }
        $.ajax({
            url: ctx + '/aea/par/state/form/listAeaParStateForm.do',
            type: 'POST',
            data: d,
            async: false,
            success: function (data) {
                if (data != null && data.length>0) {
                    stageOrStateFormData = data;
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    function checkByStageOrStateForm(){

        var data = stageOrStateFormData;
        if (data != null && data.length>0) {
            for(var i=0;i<data.length;i++) {
                $("#stage_state_form_list_tb").bootstrapTable("checkBy", {
                    field: 'formId', values: [data[i].formId]
                });
            }
        }
    }

    function saveStateFormDialogClose() {

        $('#view_state_form_modal').modal('hide');
    }

    function examineStateForm(type, datas){

        if(type.indexOf('uncheck')==-1){

            $.each(datas,function(i,v){
                // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                if(overAllIds.indexOf(v.formId) == -1){
                    overAllIds.push(v.formId);
                    overAllNames.push(v.formName);
                }
            });
        }else{

            $.each(datas,function(i,v){
                var j = overAllIds.indexOf(v.formId);
                overAllIds.splice(j,1);    //删除取消选中行
                overAllNames.splice(j,1);
            });
        }
    }

    function addCheckStateForm(formId, formName){

        if(overAllIds.indexOf(formId) == -1 ){
            overAllIds.push(formId);
            overAllNames.push(formName);
        }
    }

    function checkStateFormOverAllIds(i, row){

        if($.inArray(row.formId, overAllIds)!=-1){// 因为 判断数组里有没有这个 id
            return {
                checked : true               // 存在则选中
            }
        }
    }

</script>