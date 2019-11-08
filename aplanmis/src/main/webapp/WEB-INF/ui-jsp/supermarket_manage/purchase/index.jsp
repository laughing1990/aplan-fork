<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>项目需求采购发布列表</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <%--<link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>--%>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 100%;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            项目需求采购列表
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <form id="searchForm" class="form-horizontal" method="post">
                            <div class="row">
                                <div class="form-group col-lg-6 row">
                                    <label for="itemName" class="control-label col-lg-3 col-form-label text-right">中介服务事项名称：</label>
                                    <input id="itemName" type="text" class="form-control m-input m-input--solid empty col-lg-9" placeholder="请输入"
                                           name="itemName" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                </div>
                                <div class="form-group col-lg-6 row">
                                    <label for="auditFlag" class="control-label col-lg-3 col-form-label text-right">审核状态：</label>
                                    <div class="col-lg-9">
                                        <select type="text" class="form-control" id="auditFlag" name="auditFlag">
                                            <option value="">请选择</option>
                                            <option value="0">未审核</option>
                                            <option value="1">审核通过</option>
                                            <option value="2">审核不通过</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-lg-6 row">
                                    <label for="projName" class="control-label col-lg-3 col-form-label text-right">采购项目名称：</label>
                                    <input id="projName" type="text" class="form-control m-input m-input--solid empty col-lg-9" placeholder="请输入"
                                           name="projName" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                </div>
                                <div class="form-group col-lg-6 row">
                                    <label for="applicant" class="control-label col-lg-3 col-form-label text-right">项目业主：</label>
                                    <input id="applicant" type="text" class="form-control m-input m-input--solid empty col-lg-9" placeholder="请输入"
                                           name="applicant" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                </div>
                            </div>

                            <div class="row col-lg-12">
                                <button type="button" class="btn btn-info" onclick="searchProjPurchase();">查询</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="purchase_list_tb" >
                        <thead>
                        <tr>
                            <th data-field="" data-formatter="indexFormatter" data-align="center" data-colspan="1" data-width="10">序号</th>
                            <th data-field="localCode" data-align="left" data-colspan="1" data-width="200">采购编码</th>
                            <th data-field="projName" data-align="left" data-colspan="1" data-width="200">采购项目名称</th>
                            <th data-field="itemName" data-align="left" data-colspan="1" data-width="200">中介服务事项</th>
                            <th data-field="serviceName" data-align="left" data-colspan="1" data-width="200">服务类型</th>
                            <th data-field="applicant" data-align="left" data-colspan="1" data-width="200">项目业主</th>
                            <th data-field="auditFlag" data-align="left" data-colspan="1" data-width="50">审核状态</th>
                            <th data-field="publishTime" data-formatter="dateFormatter" data-align="left" data-colspan="1" data-width="100">发布时间</th>
                            <th data-field="" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="50">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
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

<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/purchase/index.js" type="text/javascript"></script>
</body>
</html>