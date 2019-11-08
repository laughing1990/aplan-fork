<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>机构服务审批</title>
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
                            企业发布服务列表
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                        <div class="col-xs-2" style="margin: 3px;">
                            <input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入从业机构名称..."
                                   name="applicant" id="applicant" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xs-2" style="margin: 3px;">
                            <input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入服务类型..."
                                   name="unitServiceName" id="unitServiceName" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>

                       <%-- <div class="col-xs-2" style="margin-right: 4px;">
                            <span class="form-control" >审核状态：</span>
                        </div>--%>
                        <div class="col-xs-3" style="margin:3px;">
                            <%--<input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入审核状态..."--%>
                            <%--name="auditFlag" style="background-color: #f0f0f075;border-color: #c4c5d6;">--%>

                                <select id="auditFlag" name="auditFlag" class="form-control" >
                                    　　
                                    <option value="">请选择</option>
                                    　　<option value="2">审核中</option>
                                    　　
                                    <option value="1">已审核</option>
                                    　　<option value="0">审核失败</option>
                                    　
                                </select>
                        </div>
                    <div class="col-xl-3" style="margin:3px;">
                        <button type="button" class="btn btn-secondary" onclick="clearRootRelService();">清空</button>
                        <button type="button" class="btn btn-info" onclick="searchUnitService();">查询</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="service_list_tb" >
                        <thead>
                        <tr>
                            <th data-field="num"  data-align="left" data-colspan="1" data-width="20"></th>
                            <th data-field="unitServiceId" data-align="left" data-colspan="1" data-width="160">机构服务Id</th>
                            <th data-field="serviceName" data-align="left" data-colspan="1" data-width="170">服务类型</th>
                            <th data-field="applicant" data-align="left" data-colspan="1" data-width="210">从业机构</th>
                            <th data-field="applicant" data-align="left" data-colspan="1" data-width="210">资质证书</th>
                            <th data-field="auditFlag" data-align="left" data-colspan="1" data-width="120">审核状态</th>
                            <th data-field="createTime" data-align="left" data-colspan="1" data-width="100">提交时间</th>
                            <th data-field="" data-formatter="rootUnitServiceOperatorFormatter" data-align="center" data-colspan="1" data-width="40">操作</th>
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

<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/service/service_examine.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/service/service_examine_certinst.js" type="text/javascript"></script>
</body>
</html>