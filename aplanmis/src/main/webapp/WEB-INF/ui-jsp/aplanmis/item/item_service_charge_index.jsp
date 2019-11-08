<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>
        许可收费列表
    </title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_charge_index.js?<%=isDebugMode%>"></script>
</head>

<body style="background-color:white;overflow: auto">
<div>
    <div class="m-form m-form--label-align-right m--margin-bottom-5">
        <div class="row" style="margin: 10px;">
            <div class="col-md-6"style="text-align: left;">
                <%--<button type="button" class="btn btn-danger verNoVisible" onclick="">删除</button>--%>
            </div>
            <div class="col-md-6" style="padding: 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-3"></div>
                    <div class="col-6">
                        <div class="m-input-icon m-input-icon--left">
                            <input id="itemServiceChargeKeyword" type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                        <span><i class="la la-search"></i></span>
                                                                        </span>
                        </div>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn  btn-info" onclick="searchItemServiceChargeCondition();">查询</button>&nbsp;
                        <button type="button" class="btn  btn-danger" onclick="clearSearchItemServiceCharge();">清空</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="padding: 10px">
        <table  id="item_service_charge_tb"
                data-toggle="table"
                data-sort-name="createTime"
                data-method="post"
                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                data-sort-order="DESC"
                data-click-to-select=true
                data-pagination-detail-h-align="left"
                data-pagination-show-page-go="true"
                data-pagination="true"
                data-page-list="[10,20,30,50,100]"
                data-side-pagination="server"
                data-query-params="dealQueryParams"
                data-response-handler="responseData"
                data-url="${pageContext.request.contextPath}/aea/item/service/charge/listAeaItemServiceCharge.do">
            <thead>
            <tr>
                <th data-field=""                   data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                <th data-field="itemName"         data-align="center"  data-colspan="1" data-width="100">事项名称</th>
                <th data-field="ywsf"              data-align="center"  data-colspan="1" data-width="80">有无收费</th>
                <th data-field="jfsj"              data-align="center"  data-colspan="1" data-width="100">缴费时间</th>
                <th data-field="jfhj"              data-align="center"  data-colspan="1" data-width="100">缴费环节</th>
                <th data-field="jfdd"              data-align="center"  data-colspan="1" data-width="100">缴费地点</th>
                <th data-field="jffs"              data-align="center"  data-colspan="1" data-width="80">缴费方式</th>
                <%--<th data-field=""       data-align="center"  data-formatter="formatDatetime"  data-colspan="1" data-width="100">创建时间</th>--%>
                <th data-field=""                   data-align="center"  data-formatter="operationFormatter"  data-colspan="1" data-width="150">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

</body>
</html>