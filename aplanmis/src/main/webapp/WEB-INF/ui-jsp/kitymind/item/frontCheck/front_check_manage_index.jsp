<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>前置检测管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/util/defaultBootstrap.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/item/frontCheck/front_check_manage_index.js?<%=isDebugMode%>" type="text/javascript"></script>
    <style type="text/css">
        .row{
            display: -ms-flexbox;
            overflow-x: hidden;
            margin-left: 0px;
            margin-left: 0px;
        }
        .col-xl-12 {
            display: block;
            width: 100%;
        }
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var curIsEditable = ${curIsEditable};
        var isNeedState = '${isNeedState}';
        var currentStateVerId = '${currentStateVerId}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var isCheckItem = '${isCheckItem}';
        var isCheckPartform = '${isCheckPartform}';
        var isCheckProj = '${isCheckProj}';
    </script>
</head>
<body>
<jsp:include page="../../mindHeader.jsp"></jsp:include>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 0px 5px 0px;margin: 0px;">
    <div class="col-xl-12" style=" margin-top: 0px;${(!"1".equals(isCheckItem) && !"1".equals(isCheckPartform) && !"1".equals(isCheckProj))?'':'display:none;'}" >
        <h2 class="m-portlet__head-text" style="text-align: center;">
            <span style="font-weight: bold;">未开启相关的前置检测,请在事项编辑页面开启</span>
        </h2>
    </div>
    <div class="col-xl-12" style=" margin-top: 0px;${"1".equals(isCheckProj)?'':'display:none;'}" >
        <div class="m-portlet" style="margin-bottom: 5px;border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text">
                            <span style="font-weight: bold;">项目信息前置检测</span>
                        </h2>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-7"style="text-align: left;">
                            <button type="button" class="btn btn-info"
                                    onclick="addItemFrontProj();">新增</button>
                            <button type="button" class="btn btn-info"
                                    onclick="sortItemFrontCheck('proj');">排序</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="batchDelItemFrontProj();">删除</button>
                        </div>
                        <div class="col-md-5" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_item_front_proj" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..."  value="" id = "item_front_proj_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "item_front_proj_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "item_front_proj_clear_btn">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table id="item_front_proj_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <div class="col-xl-12" style=" margin-top: 0px;${"1".equals(isCheckItem)?'':'display:none;'}" >
        <div class="m-portlet" style="margin-bottom: 5px;border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text" >
                            <span style="font-weight: bold;">事项信息前置检测</span>
                        </h2>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-7"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="importFrontItem();">导入事项</button>
                            <button type="button" class="btn btn-info" onclick="sortItemFrontCheck('item');">排序</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDelItemFrontItem();">删除</button>
                        </div>
                        <div class="col-md-5" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_item_front_item" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..."  value="" id = "item_front_item_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "item_front_item_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "item_front_item_clear_btn">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table id="item_front_item_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <div class="col-xl-12" style=" margin-top: 0px;${"1".equals(isCheckPartform)?'':'display:none;'}" >
        <div class="m-portlet" style="margin-bottom: 5px;border: 0px solid #e8e8e8;">
            <div class="m-portlet__head" style="border-bottom: 0px solid #e8e8e8">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h2 class="m-portlet__head-text" >
                            <span style="font-weight: bold;">事项扩展表单前置检测</span>
                        </h2>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 0px 0px 5px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-7"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="openSelectItemFrontPartform();">导入扩展表单</button>
                            <button type="button" class="btn btn-info" onclick="sortItemFrontCheck('partform');">排序</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDelItemFrontPartform();">删除</button>
                        </div>
                        <div class="col-md-5" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_item_front_partform" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..."  value="" id = "item_front_partform_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "item_front_partform_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "item_front_partform_clear_btn">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table id="item_front_partform_tb"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

<!-- 项目信息前置检测 -->
<%@include file="edit_item_front_proj.jsp"%>

<!-- 事项信息前置检测 -->
<%@include file="edit_item_front_item.jsp"%>

<!-- 事项扩展表单前置检测 -->
<%@include file="edit_item_front_partform.jsp"%>

<!-- 设置EL表达式 -->
<%@include file="../../item/detail/select_meta_db_tbcol_ztree.jsp"%>

<!-- 事项选择 -->
<%@include file="select_front_item_ztree.jsp"%>

<!-- 事项扩展表单选择 -->
<%@include file="select_partform.jsp"%>

<!-- 前置检测排序 -->
<%@include file="item_front_check_sort.jsp"%>

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

<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
</body>
</html>