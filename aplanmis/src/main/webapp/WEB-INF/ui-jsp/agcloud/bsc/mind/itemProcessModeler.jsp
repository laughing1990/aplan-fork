<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项流程设计</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp"%>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        .fixed-table-body::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        .fixed-table-body::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        .fixed-table-body::-webkit-scrollbar-corner{
            background: #fff;
        }
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var appId = '${appId}';
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                           <span class="m-portlet__head-icon m--hide">
                               <i class="la la-gear"></i>
                           </span>
                        <h3 class="m-portlet__head-text">事项流程设计</h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                       <%-- <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="add();">新增事项流程</button>
                            <button type="button" class="btn btn-info" onclick="importProcess();">导入事项流程</button>
                            <button type="button" class="btn btn-secondary" onclick="refresh();">刷新</button>
                        </div>--%>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_item_process_design" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input" placeholder="请输入关键字..." id="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left"><span><i class="la la-search"></i></span></span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" id="searchBtn" class="btn btn-info">查询</button>
                                    <button type="button" id="clearBtn" class="btn btn-secondary">清空</button>
                                </div>
                                <div class="col-4"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table  id="item_process_design_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            <%--data-page-size="10",--%>
                            data-pagination=false,
                            <%--data-side-pagination="server",--%>
                            <%--data-page-list="[10, 20, 50, 100]",--%>
                            data-side-pagination="server",
                            data-query-params="processParam",
                            data-pagination-show-page-go="false",
                            data-url="${pageContext.request.contextPath}/rest/mind/item/processDefs.do">
                        <thead>
                        <tr>
                            <th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="10">ID</th>
                            <%--<th data-field="appFlowdefId" data-visible="false" data-align="left" data-width="10">流程定义ID</th>
                            <th data-field="casedefKey" data-visible="false" data-align="left" data-width="150">用例定义KEY</th>
                            <th data-field="procdefKey" data-visible="false" data-align="left" data-width="150">流程定义KEY</th>
                            <th data-field="casedefName" data-visible="false" data-align="left" data-width="150">用例名称</th>
                            <th data-field="priorityOrder" data-visible="false" data-align="left" data-width=10>优先级</th>--%>
                            <th data-field="startEl"  data-align="left" data-width=100>激活条件</th>
                            <th data-field="defName" data-align="left" data-width="100">流程名称</th>
                            <%--<th data-field="isMaster" data-visible="false" data-formatter="masterFormatter" data-align="left" data-width=10>是否默认启动</th>--%>
                            <th data-field="_operator" data-formatter="operate" data-align="center" data-width="100">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>
<!-- 业务js -->
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/itemProcessModeler.js" type="text/javascript"></script>
</body>
</html>
