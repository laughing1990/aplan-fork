<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>一张表单管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/stage/oneform/par_stage_oneform.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            display: -ms-flexbox;
            overflow-x: hidden;
        }
        .col-xl-12 {
            display: block;
            width: 100%;
        }
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '';
        var itemNature = '${itemNature}';
        <%--var curentIsOptionItem ='${isOptionItem}';--%>
        <%--var isFrontCheckItem = '${isFrontCheckItem}';--%>
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var curIsEditable = ${curIsEditable};  // 版本下数据是否可以编辑
        var restWebApp = '${restWebApp}';//智能表单回调url
        var isSmartForm = '1';//默认是查询智能表单
    </script>
</head>
<body>
<jsp:include page="../../mindHeader.jsp"></jsp:include>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 0px 5px 0px;margin: 0px;">
    <div class="col-xl-12" style="">
        <div class="m-portlet" style="margin-bottom: 5px; border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text">
                            <span style="font-weight: bold;">总表管理</span>
                        </h2>
                    </div>
                </div>
                <div class="m-portlet__head-tools">
                    <button type="button" class="btn btn-info" onclick="addParOneform();" style="margin-right: 50px;">导入总表</button>
                    <%--<button type="button" class="btn btn-secondary" onclick="batchDelOneform();">批量删除</button>--%>
                    <%--<button type="button" class="btn btn-secondary" onclick="initParStageOneformTable();" style="margin-right: 50px;">刷新</button>--%>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="selectParStageOneformTable"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <div class="col-xl-12" style=" margin-top: 0px">
        <div class="m-portlet" style="margin-bottom: 5px;border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text">
                            <span style="font-weight: bold;">扩展表管理</span>
                        </h2>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-7"style="text-align: left;">
                            <button type="button" class="btn btn-info"
                                    onclick="addStagePartform();">新增</button>
                            <button type="button" class="btn btn-info"
                                    onclick="sortStagePartform();">排序</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="batchDelStagePartform();">删除</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="refreshStagePartform();">刷新</button>
                        </div>
                        <div class="col-md-5" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_stage_parform" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" onclick="searchStagePartform();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchStagePartform();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table  id="stage_partform_tb"
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
                            data-query-params="stagePartformParam"
                            data-url="${pageContext.request.contextPath}/aea/par/stage/partform/listStagePartformRelForm.do?stageId=${currentBusiId}">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                <th data-field="partformName" data-align="left" data-width="250">扩展表名称</th>
                                <th data-field="isSmartForm" data-formatter="isSmartFormFormatter"
                                    data-align="center" data-width="60">是否智能表单</th>
                                <th data-field="useEl" data-formatter="useElFormatter"
                                    data-align="center" data-width="60">是否启用EL</th>
                                <th data-field="elContent" data-align="left" data-width="250">启用EL条件</th>
                                <th data-field="formName" data-formatter="formNameFormatter"
                                    data-align="left" data-width="250">智能表单名称/开发表单地址</th>
                                <th data-field="sortNo" data-align="left" data-width="60">排序</th>
                                <th data-field="_operator" data-formatter="stagePartformFormatter"
                                    data-align="center" data-width="150">操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <div class="col-xl-12" style=" margin-top: 0px">
        <div class="m-portlet" style="margin-bottom: 5px;border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text">
                            <span style="font-weight: bold;">事项表单管理</span>
                        </h2>
                    </div>
                </div>
                <div class="m-portlet__head-tools">
                    <button type="button" style="margin-bottom:5px;margin-right:25px;" class="btn btn-info" onclick="sortStageItem();">事项子表排序</button>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="selectParOneformTable"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

<!-- 编辑总表 -->
<%@include file="aedit_par_oneform.jsp"%>

<!-- 新增编辑扩展表 -->
<%@include file="aedit_part_form.jsp"%>

<!-- 新增编辑开发表单 -->
<%@include file="add_dev_form.jsp" %>

<!-- 导入总表 -->
<%@include file="par_import_oneform.jsp"%>

<!-- 导入智能表单 -->
<%@include file="import_item_form.jsp"%>

<!-- 事项字表排序 -->
<%@include file="par_stage_item_sort.jsp"%>

<!-- 设置EL表达式 -->
<%@include file="../../item/detail/select_meta_db_tbcol_ztree.jsp"%>

<!-- 扩展表排序 -->
<%@include file="par_part_form_sort.jsp"%>

<!-- 为扩展表导入表单 -->
<%@include file="import_partform_form.jsp"%>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">数据加载中，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<!--bootstrap-treegrid-->
<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>--%>
<%--<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>--%>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
</body>
</html>