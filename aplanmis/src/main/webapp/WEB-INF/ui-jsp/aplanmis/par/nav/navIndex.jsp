<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-4" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 5px;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
					    <span class="m-portlet__head-icon m--hide">
						    <i class="la la-gear"></i>
					    </span>
                        <h3 class="m-portlet__head-text">导航定义</h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addParNav('add');">增加</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshNav();">刷新</button>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7" style="text-align: right;">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="search_nav_name" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5"  style="text-align: center;">
                                    <button type="button" class="btn btn-info" onclick="searchParNav();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchParNav();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="showParNavTable"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <div class="col-xl-8" style="padding: 0px 2px 0px 2px;">
        <div class="m-portlet" style="margin-bottom: 5px;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
					    <span class="m-portlet__head-icon m--hide">
						    <i class="la la-gear"></i>
					    </span>
                        <h3 class="m-portlet__head-text">导航情形</h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addParFactor();">增加</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshFactor();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2" style="text-align: right;"></div>
                                <div class="col-6" style="text-align: right;">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="search_factor_name" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-4"  style="text-align: center;">
                                    <button type="button" class="btn btn-info" onclick="searchParFactor();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchParFactor();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="m-portlet__body miniScrollbar" style="padding: 0px 5px;">
                    <table id="factor_treegrid_tb"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="binding_theme_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_theme_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title">查看主题信息</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="view_theme_form" method="post">
                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">主题名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="themeName" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">主题编码:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="themeCode" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">申报说明:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="themeMemo" rows="8"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 10px;">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<%--主题导航表单--%>
<%@include file="aea_par_nav_form.jsp"%>

<%--主题导航因子表单--%>
<%@include file="aea_par_factor_form.jsp"%>

<%--主题绑定--%>
<%@include file="../../../theme/select_par_theme.jsp"%>

<!-- 设置EL表达式 -->
<%@include file="../../../kitymind/item/detail/select_meta_db_tbcol_ztree.jsp"%>

<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">正在获取数据，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>
<!--bootstrap-table-->
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/nav/navIndex.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>