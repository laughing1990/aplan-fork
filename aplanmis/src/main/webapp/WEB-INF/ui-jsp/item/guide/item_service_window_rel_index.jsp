<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>窗口办理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script>
        var itemVerId = '${itemVerId}';
        var curIsEditable = ${curIsEditable};
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
                            窗口办理
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="openServiceWindow(curIsEditable);">导入服务窗口</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDelete(curIsEditable);">删除</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshServiceWindowRel();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-3"></div>
                                <div class="col-6">
                                    <form id="service_window_form" method="post">
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
                                    <button type="button" class="btn btn-info" onclick="searchServiceWindowRel();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchServiceWindowRel();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!--列表区域开始-->
                <div class="base" style="padding: 0px 5px;">
                    <table id="service_window_table"
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
                           data-url="${pageContext.request.contextPath}/aea/item/guide/listItemWindowByPage.do?itemVerId=${itemVerId}">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="left" >ID</th>
                                <th data-field="windowName" data-colspan="1" data-align="left">窗口名称</th>
                                <th data-field="linkPhone" data-colspan="1" data-align="left">窗口电话</th>
                                <th data-field="windowAddress" data-colspan="1" data-align="left" >窗口地址</th>
                                <th data-field="_operator" data-formatter="operatorFormatter"
                                    data-align="left" data-colspan="1" >操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!--列表区域结束-->
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/util/defaultBootstrap.js?<%=isDebugMode%>" type="text/javascript"></script>
<%@include file="select_service_window.jsp"%>
<script src="${pageContext.request.contextPath}/ui-static/item/guide/item_service_window_rel_index.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>