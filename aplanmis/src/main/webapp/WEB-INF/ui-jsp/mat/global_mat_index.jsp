<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>全局材料库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css">
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                材料标准清单
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <!-- 按钮区域 -->
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn btn-info"
                                        onclick="addGlobalMat();">新增</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="batchDelGlobalMat();">删除</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="refreshMatSto();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-3"></div>
                                    <div class="col-6">
                                        <form id="search_mat_sto_form" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入材料名称、编号..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" class="btn  btn-info" onclick="searchMatSto();">查询</button>
                                        <button type="button" class="btn  btn-secondary" onclick="clearSearchMatSto();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 按钮区域end -->
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="customTable"
                                data-toggle="table"
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination-detail-h-align="left"
                                data-pagination-show-page-go="true"
                                data-page-size="10"
                                data-page-list="[10,20,30,50,100]",
                                data-pagination=true
                                data-side-pagination="server"
                                data-pagination-detail-h-align="left"
                                data-query-params="matStoParam"
                                data-response-handler="matStotResponseData"
                                data-url="${pageContext.request.contextPath}/aea/item/mat/globalMatList.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center"
                                        data-colspan="1" data-width="5"></th>
                                    <th data-field="matProp" data-formatter="matPropormatter"
                                        data-align="left" data-colspan="1" data-width="60">材料性质</th>
                                    <th data-field="matName" data-formatter="matNameFormatter"
                                        data-align="left" data-colspan="1" data-width="250">材料名称</th>
                                    <th data-field="matCode" data-align="left"
                                        data-colspan="1" data-width="150">材料编号</th>
                                    <th data-field="matFrom" data-formatter="operatorFormatterMatFrom"
                                        data-align="left" data-colspan="1" data-width="150">材料来源</th>
                                    <th data-field="stdmatName" data-align="left"
                                        data-colspan="1" data-width="150">关联标准材料</th>
                                    <th data-field="" data-formatter="operatorFormatter"
                                        data-align="center" data-colspan="1" data-width="100">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
        </div>
    </div>

    <!-- 新增编辑材料 -->
    <%@include file="aedit_aea_mat.jsp" %>

    <!-- 展示材料附件 -->
    <%@include file="show_mat_att.jsp" %>

    <!-- 选择材料类型 -->
    <%@include file="../aplanmis/item/select_mat_type_ztree.jsp" %>

    <!-- 选择电子证照 -->
    <%@include file="../common/ztree/select_cert_no_right_ztree.jsp" %>

    <!-- 选择表单 -->
    <%@include file="../common/ztree/select_form_no_right_ztree.jsp" %>

    <!-- 选择标准材料 -->
    <%@include file="../common/ztree/select_stdmat_no_right_ztree.jsp" %>

    <!-- 进度弹窗 -->
    <%@include file="../common/show_loading.jsp" %>

    <script src="${pageContext.request.contextPath}/ui-static/mat/global_mat_index.js" type="text/javascript"></script>
</body>
</html>