<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>设立依据</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script>
        var recordId = '${recordId}';
        var curIsEditable = ${curIsEditable};
        var relTbName = 'AEA_PAR_STAGE';
        var relPkName = 'STAGE_ID';
    </script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
        .label{
            text-align: right;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 0px 5px 0px 0px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 5px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                               <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                设立依据
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn btn-info" onclick="openSelectTree(curIsEditable);">导入条款</button>
                                <button type="button" class="btn btn-secondary" onclick="batchDelete(curIsEditable);">删除</button>
                                <button type="button" class="btn btn-secondary" onclick="refreshBasic();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-3"></div>
                                    <div class="col-6">
                                        <form id="service_basic_form" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                            <%--<input type="hidden" name="tableName" value="AEA_PAR_STAGE"/>--%>
                                            <%--<input type="hidden" name="pkName" value="STAGE_ID"/>--%>
                                            <%--<input type="hidden" name="recordId" value="${recordId}"/>--%>
                                        </form>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" class="btn btn-info" onclick="searchCond();">查询</button>
                                        <button type="button" class="btn btn-secondary" onclick="clearSearchCond();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!--列表区域开始-->
                    <div class="base" style="padding: 0px 5px;">
                        <table id="service_basic_Table"
                               data-toggle="table",
                               data-click-to-select=true,
                               queryParamsType="",
                               data-pagination=true,
                               data-page-size="10",
                               data-side-pagination="server",
                               data-page-list="[10, 20, 50, 100]",
                               data-side-pagination="server",
                               data-query-params="dealQueryParams",
                               data-pagination-show-page-go="true",
                               data-url="${pageContext.request.contextPath}/aea/item/service/basic/listAeaItemServiceBasic.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center"
                                        data-colspan="1" data-width="10">ID</th>
                                    <th data-field="legalName" data-colspan="1" data-width="250">依据名称</th>
                                    <th data-field="basicNo" data-colspan="1" data-width="250">依据文号</th>
                                    <th data-field="issueOrg" data-colspan="1" data-width="250">颁布机关</th>
                                    <th data-field="exeDate" data-colspan="1" data-width="250" data-formatter="exeDateFormatter" >实施日期</th>
                                    <th data-field="clauseTitle" data-colspan="1" data-width="250">条款号</th>
                                    <th data-field="clauseContent" data-colspan="1" data-width="350" data-formatter="clauseContentFormatter" >条款具体内容</th>
                                    <th data-field="_operator" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="150">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <!--列表区域结束-->
                </div>
            </div>
        </div>
    </div>

    <!-- 查看条款 -->
    <div id="view_legal_clause_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="view_legal_clause_modal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width:900px;">
            <div class="modal-content">
                <!-- 标题 -->
                <div class="modal-header" style="padding: 15px;height: 45px;">
                    <h5 class="modal-title" id="view_legal_clause_modal_title">查看法规条款</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form id="view_legal_clause_form" method="post" enctype="multipart/form-data">
                    <div class="modal-body" style="padding: 10px;">
                        <div id="view_legal_clause_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">法律法规名称:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="legalName" value="" placeholder="请输入法律法规名称..."/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">法律法规层级:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="legalLevel" value="" placeholder="请输入法律法规层级..."/>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">依据文号:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="basicNo" value="" placeholder="请输入依据文号..."/>
                                </div>
                            </div>


                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">颁发机构:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="issueOrg" value="" placeholder="请输入颁发机构..."/>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">开始实施的日期:</label>
                                <div class="col-lg-4">
                                    <input id="exeDate" type="datetime-local" class="form-control m-input" name="exeDate" type="text" placeholder="开始实施的日期">
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">条款号:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="clauseTitle" value="" placeholder="请输入条款号..."/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">条款具体内容:</label>
                                <div class="col-lg-10">
                                    <textarea name="clauseContent" class="form-control m-input" placeholder="请输入条款具体内容..." rows="9"></textarea>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-lg-2 col-form-label" style="text-align: right;">法律法规说明:</label>
                                <div class="col-lg-10">
                                    <textarea name="serviceLegalMemo" class="form-control m-input" placeholder="请输入备注..." rows="5"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 按钮区域 -->
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- 选择 -->
    <%@include file="../../../item/guide/select_service_basic_ztree.jsp"%>
    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_basic_rel_index.js" type="text/javascript"></script>
</body>
</html>