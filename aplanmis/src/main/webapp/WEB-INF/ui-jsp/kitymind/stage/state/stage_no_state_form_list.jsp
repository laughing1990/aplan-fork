<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_stage_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_stage_form_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_form_modal_title">导入表单</h5>
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
                                        <input id="stageFormKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchStageForm2();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchStageForm2();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="stage_form_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="stage_form_list_tb"
                                data-toggle="table",
                                data-method="post"
                                data-pagination=true
                                data-page-size="10",
                                data-page-list="[10]",
                                data-click-to-select=true
                                data-side-pagination="server"
                                data-pagination-show-page-go="true",
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-query-params="stageFormParam2",
                                data-response-handler="stageFormResponseData2",
                                data-url="${pageContext.request.contextPath}/aea/par/state/form/listStageNoSelectFormByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
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
                <button id="save_stage_form_btn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#view_stage_form_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>