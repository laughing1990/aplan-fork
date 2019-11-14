<%@ page contentType="text/html;charset=UTF-8" %>

<div id="aedit_stage_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_stage_item_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_item_modal_title">设置事项材料</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <input type="hidden" id="stageItemId" name = "stageItemId"/>
                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font style="color:red">*</font>关联事项:</label>
                    <div class="col-lg-9">
                        <select id="stage_item_select" class="form-control" onchange="changeStageItem();"></select>
                    </div>
                    <div class="col-lg-1"></div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="form-group m-form__group row">
                    <div class="col-md-6" style="text-align: left;">
                        <button type="button" class="btn btn-info" onclick="saveStateItemInIds();">保存配置</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="$('#aedit_stage_item_modal').modal('hide');">关闭页面</button>
                    </div>
                    <div class="col-lg-4">
                        <form id="search_select_stage_no_state_in_form" method="post">
                            <div class="m-input-icon m-input-icon--left">
                                <input type="text" class="form-control m-input"
                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                    <span><i class="la la-search"></i></span>
                                </span>
                            </div>
                        </form>
                    </div>
                    <div class="col-lg-2">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectStageNoStateInMatCet()">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSelectStageNoStateInMatCet()">清空</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="select_stage_no_state_in_scroll" style="height: 500px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="select_stage_no_state_in_tb"
                                data-toggle="table"
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination=false
                                data-sortable=false
                                data-query-params="selectStageNoStateInParam"
                                data-response-handler="selectStageNoStateInResponseData",
                                data-url="${pageContext.request.contextPath}/aea/par/in/listParInByStageItemId.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center"
                                        data-formatter="isCheckItemInMatCertFormatter" data-width="10">ID</th>
                                    <th data-field="aeaMatCertName" data-align="left" data-width="250">名称</th>
                                    <th data-field="aeaMatCertCode" data-align="left" data-width="150">编号</th>
                                    <th data-field="matProp" data-formatter="matPropormatter"
                                        data-align="center" data-width=80>材料性质</th>
                                    <th data-field="_operator" data-formatter="globalMatFormatter"
                                        data-align="center" data-width="50">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>