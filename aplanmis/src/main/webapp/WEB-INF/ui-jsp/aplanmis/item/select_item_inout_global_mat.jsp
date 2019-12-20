<%@ page contentType="text/html;charset=UTF-8" %>

<div id="select_item_inout_global_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_item_inout_global_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="select_item_inout_global_mat_modal_title" class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4" style="text-align: left;">
                            <button type="button" class="btn btn-info"
                                    onclick="saveItemInOutGlobalMat();">选择材料</button>&nbsp;&nbsp;
                            <button type="button" class="btn btn-secondary"
                                    onclick="$('#select_item_inout_global_mat_modal').modal('hide');">关闭页面</button>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-3"></div>
                                <div class="col-6">
                                    <form id="search_item_inout_global_mat_form" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3">
                                    <button type="button" class="btn btn-info"
                                            onclick="searchItemInoutGlobalMat();">查询</button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="clearSearchItemInoutGlobalMat();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div id="select_item_inout_global_mat_scroll" style="height: 500px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="select_item_inout_global_mat_tb"
                                data-toggle="table"
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination-detail-h-align="left"
                                data-pagination-show-page-go="true"
                                data-page-size="6"
                                data-page-list="[6]",
                                data-pagination=true
                                data-side-pagination="server"
                                data-query-params="selectItemInoutGlobalMatParam",
                                data-response-handler="selectItemInoutGlobalMatResponseData",
                                data-url="${pageContext.request.contextPath}/aea/item/mat/listItemInOutNoSelectGlobalMat.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                    <th data-field="matProp" data-formatter="matPropormatter"
                                        data-align="left" data-colspan="1" data-width="60">材料性质</th>
                                    <th data-field="matName" data-formatter="matNameFormatter"
                                        data-align="left" data-width="300">材料名称</th>
                                    <th data-field="matCode" data-align="left" data-width="150">材料编号</th>
                                    <th data-field="_operator" data-formatter="globalMatFormatter"
                                        data-align="center" data-width="50">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>