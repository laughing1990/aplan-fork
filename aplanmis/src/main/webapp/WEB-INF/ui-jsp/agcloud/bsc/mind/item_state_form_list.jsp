<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_item_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_item_form_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_item_form_modal_title"></h5>
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
                                        <input id="itemFormKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchItemForm();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchItemForm();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="item_form_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="item_form_list_tb"
                                data-toggle="table",
                                data-click-to-select=true,
                                queryParamsType="",
                                data-pagination=true,
                                data-side-pagination="server",
                                data-page-size="10",
                                data-page-list="[10]",
                                data-query-params="itemFormParam",
                                data-pagination-show-page-go="true",
                                data-url="${pageContext.request.contextPath}/aea/item/state/form/listCurOrgFormByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                                        data-width="10" data-formatter="checkItemFormOverAllIds"></th>
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
                <button type="button" class="btn btn-info" id="btn_save_item_form_list_tb" >保存</button>
                <button type="button" class="btn btn-secondary" onclick="saveItemFormDialogClose();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var commonQueryParams = {};
    var overAllIds = new Array();  //全局数组
    var overAllNames = new Array();
    var isItemStateForm = "1";
    var isItemNeedStateFlag = true;
    var itemFormFirstTime = true;
    var itemOrStateFormData = null;

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
     * @param itemStateId  情形节点id
     * @param type  事项还是情形节点
     * @param isNeedState 分不分情形
     */
    function vieItemForm(itemStateId, type, isNeedState){

        stageItemStateId = itemStateId;
        isItemNeedStateFlag = isNeedState;
        itemFormFirstTime = true;
        itemOrStateFormData = null;
        $('#item_form_list_tb_scroll').animate({scrollTop: 0}, 800);//滚动到顶部

        var title = "情形表单列表";
        overAllIds = [];
        overAllNames = [];
        if(type=='item'){
            isItemStateForm = "0";
            title = "通用表单列表";
        }else if(type=='state'){
            isItemStateForm = "1";
            title = "情形表单列表";
        }
        clearSearchItemForm();
        $("#view_item_form_modal").modal("show");
        $("#view_item_form_modal_title").html(title);
    }

    // 查询
    function searchItemForm(){

        itemOrStateFormData = null;
        commonQueryParams = {};
        $("#item_form_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#item_form_list_tb').bootstrapTable('hideColumn', 'formId');
        $("#item_form_list_tb").bootstrapTable('refresh', commonQueryParams);       //无参数刷新

        queryItemStateForm();

        $("#item_form_list_tb").on('load-success.bs.table', function () {

            checkByItemOrStateForm();
        });

        $('#item_form_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {

            // 点击时获取选中的行或取消选中的行
            examineItemForm(e.type, $.isArray(rows) ? rows : [rows]);
        });
    }


    //参数设置
    function itemFormParam(params) {

        if(params){
            var pageNum = (params.offset / params.limit) + 1;
            commonQueryParams['pageNum'] = pageNum;
            commonQueryParams['pageSize'] = params.limit;
        }else{
            commonQueryParams['pageNum'] = 1;
            commonQueryParams['pageSize'] = 10;
        }
        commonQueryParams['keyword'] =  $('#itemFormKeyword').val();
        return commonQueryParams;
    }

    function saveItemFormAndReturn() {

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
           url: ctx + '/aea/item/state/form/saveAeaItemStateForms.do',
           type: 'POST',
           data:  {
               "itemVerId": currentBusiId,
               "itemStateVerId": currentStateVerId,
               "isStateForm": isItemStateForm,
               "itemStateId": isItemStateForm == '0' ? '' : stageItemStateId,
               "formIds": ids.toString(),
           },
           async: false,
           success: function (result) {
               if (result.success) {
                   $("#view_item_form_modal").modal("hide");
                   refreshMind();
               }else{
                   arr = new Array();
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

    function saveItemNoStateFormAndReturn() {

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
            url: ctx + '/aea/item/state/form/saveAeaItemStateForms.do',
            type: 'POST',
            data:  {
                "itemVerId": currentBusiId,
                "itemStateVerId": currentStateVerId,
                "isStateForm": isItemStateForm,
                "itemStateId": isItemStateForm == '0' ? '' : stageItemStateId,
                "formIds": ids.toString(),
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    $("#view_item_form_modal").modal("hide");
                    swal({
                        title: '提示信息',
                        text: '保存成功!',
                        type: 'success',
                        timer: 1000,
                        showConfirmButton: false
                    });
                    // 刷新表格
                    searchItemInMatCert();
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
    function clearSearchItemForm() {

        //搜索框置空
        $('#itemFormKeyword').val('');
        searchItemForm();
    }

    function queryItemStateForm() {

        var d = {};
        // 事项分情形
        if(isItemNeedStateFlag){
            d = {
                "itemVerId": currentBusiId,
                "itemStateVerId": currentStateVerId,
                "isStateForm": isItemStateForm,
                "itemStateId": isItemStateForm == '0' ? '' : stageItemStateId
            }
        // 事项不分情形
        }else{
            d = {
                "itemVerId": currentBusiId,
                "itemStateVerId": currentStateVerId,
            }
        }
        $.ajax({
            url: ctx + '/aea/item/state/form/listAeaItemStateForm.do',
            type: 'POST',
            data: d,
            async: false,
            success: function (data) {
                if (data != null && data.length>0) {
                    itemOrStateFormData = data;
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    function checkByItemOrStateForm(){

        var data = itemOrStateFormData;
        if (data != null && data.length>0) {
            for(var i=0;i<data.length;i++) {
                $("#item_form_list_tb").bootstrapTable("checkBy", {
                    field: 'formId', values: [data[i].formId]
                });
            }
        }
    }

    function saveItemFormDialogClose() {

        $('#view_item_form_modal').modal('hide');
    }

    function examineItemForm(type, datas){

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

    function checkItemFormOverAllIds(i, row){

        if($.inArray(row.formId, overAllIds)!=-1){// 因为 判断数组里有没有这个 id
            return {
                checked : true               // 存在则选中
            }
        }
    }

</script>