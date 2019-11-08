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
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        label {
            text-align: right;
        }
    </style>
</head>

<body style="width: 100%;overflow-x: hidden;">
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <!-- 标题信息 -->
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide"><i class="la la-gear"></i></span>
                            <h3 class="m-portlet__head-text">法律法规/条款</h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-3">
                            <input id="keyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xl-5">
                            <button type="button" class="btn btn-info" onclick="searchLegal();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchLegal();">清空</button>
                            <button type="button" class="btn btn-secondary" onclick="expandAll();">展开</button>
                            <button type="button" class="btn btn-secondary" onclick="collapseAll();">折叠</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 树列表区域 -->
                    <div id="legalTreeScrollable" class="m-scrollable" data-scrollable="true" data-max-height="600">
                        <div id="legalTree" class="ztree" style="overflow-y:auto;overflow-x:auto;"></div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>




<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/legal_index.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
</body>
</html>