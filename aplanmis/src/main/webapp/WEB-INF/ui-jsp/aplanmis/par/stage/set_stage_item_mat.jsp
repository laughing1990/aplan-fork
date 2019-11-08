<%@ page contentType="text/html;charset=UTF-8" %>

<div id="set_stage_item_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="set_stage_item_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="min-width: 950px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="set_stage_item_mat_modal_title" class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4"style="text-align: left;"></div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <form id="search_stage_item_mat_form" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info"
                                            onclick="searchStageItemMat();">查询</button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="clearSearchStageItemMat();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="set_stage_item_mat_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            data-pagination=true,
                            data-page-size="10",
                            data-side-pagination="server",
                            data-page-list="[10, 20, 50, 100]",
                            data-side-pagination="server",
                            data-query-params="setStageItemMatParam",
                            data-pagination-show-page-go="true",
                            data-url="${pageContext.request.contextPath}/aea/par/in/listStageItemMat.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="left" data-width="10">ID</th>
                                <th data-field="aeaMatCertName" data-align="left" data-width="250">名称</th>
                                <th data-field="aeaMatCertCode" data-align="left" data-width="200">编号</th>
                                <th data-field="isStateIn" data-align="center" data-width="60"
                                    data-formatter="isStateInFormatter">是否情形</th>
                                <th data-field="fileType" data-align="center" data-width="60"
                                    data-formatter="fileTypeFormatter">类型</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
            <div class="modal-footer" style="padding: 15px;">
                <button type="button" class="btn btn-info" onclick="saveStageItemMat();">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary"
                        onclick="$('#set_stage_item_mat_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>