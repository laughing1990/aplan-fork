
<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    var itemVerId = '${itemVerId}';
    var recordId = '${itemVerId}';
    var curIsEditable = ${curIsEditable};
    var relTbName = 'AEA_ITEM_VER';
    var relPkName = 'ITEM_VER_ID';
</script>
<!-- 配置设立依据列表弹窗 -->
<div class="modal fade" id="dialog_config_according" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_config_according_header">配置设立依据</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-12" style="padding: 0px;">
                                <form id="search_org_rel_item_form" method="post">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-6">
<%--                                            <button type="button" class="btn btn-info" onclick="saveConfigAccording();">保存</button>--%>
                                            <button type="button" class="btn btn-info" onclick="openCfgSelectTree(curIsEditable);">导入条款</button>
                                            <button type="button" class="btn btn-secondary" onclick="batchDelete(curIsEditable);">删除</button>

                                        </div>
                                        <div class="col-4" style="text-align: right;">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input id="accordingKeyword" type="text" class="form-control m-input"
                                                       placeholder="请输入关键字" name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
													<span><i class="la la-search"></i></span>
												</span>
                                            </div>
                                        </div>
                                        <div class="col-2"  style="text-align: center;">
                                            <button type="button" class="btn btn-info"onclick="searchAccordingList();">查询</button>
                                            <button type="button" class="btn btn-secondary"onclick="clearSearchAccordingList();">清空</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div id="according_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                        <div class="base" style="padding: 0px 5px;">
                            <table  id="_according_list_tb"
                                    data-toggle="table",
                                    data-click-to-select=true,
                                    queryParamsType="",
                                    data-pagination=true,
                                    data-page-size="10",
                                    data-page-list="[10]",
                                    data-side-pagination="server",
                                    data-query-params="queryAccordingParam",
                                    data-response-handler="queryAccordingResponseData"
                                    data-pagination-show-page-go="true",
                                    data-url="${pageContext.request.contextPath}/aea/service/legal/clause/listAeaItemServiceLegalClause.do">
                                <thead>
                                <tr>
                                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1"
                                        data-width="20" data-formatter="checkAccordingId"></th>
                                    <th data-field="legalName" data-align="left" data-colspan="1" data-width="200">法律法规名称</th>
                                    <th data-field="basicNo" data-align="left" data-colspan="1" data-width="200">依据文号</th>
                                    <th data-field="issueOrg" data-align="left" data-colspan="1" data-width="200">颁发机构</th>
                                    <th data-field="clauseTitle" data-align="left" data-colspan="1" data-width="100">条款号</th>
                                    <th data-field="clauseContent" data-align="left" data-colspan="1" data-width="200" data-formatter="clauseContentFormatter">条款具体内容</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
<%@ include file="select_service_basic_ztree.jsp"%>
