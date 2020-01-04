<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: hidden;">
<head>
    <title>牵头人员配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="text/javascript">
        var solicitOrgId = '${solicitOrgId}';
    </script>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div id="westPanel" class="col-xl-12" style="padding: 0px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h3 class="m-portlet__head-text">
                            牵头人员配置
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6" style="text-align: left;">
                            <%--<button type="button" class="btn btn-info"--%>
                                    <%--onclick="backSolicitOrg();">返回</button>--%>
                            <button type="button" class="btn btn-info"
                                    onclick="importSolicitUser();">导入人员</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="batchDelSolicitOrgUser();">批量移除</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="refreshSolicitOrgUserList();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <form id="search_solicit_org_user_form" method="post">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-5"></div>
                                    <div class="col-4" style="text-align: right;">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-3" style="text-align: center;">
                                        <button type="button" class="btn btn-info"
                                                onclick="searchSolicitOrgUserList();">查询
                                        </button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="clearSearchSolicitOrgUserList();">清空
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 5px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 5px">
                    <table id="solicit_org_user_list_tb"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 选择牵头人员 -->
<%@include file="select_solicit_user_ztree.jsp"%>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">数据修复中，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<!-- 业务js -->
<script src="${pageContext.request.contextPath}/ui-static/solicit/stage_org/org_user.js" type="text/javascript"></script>
</body>
</html>