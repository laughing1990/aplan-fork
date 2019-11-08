<%@ page contentType="text/html;charset=UTF-8" %>

<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>流程设计</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>


    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-page.css" rel="stylesheet" type="text/css"/>

    <script>
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentAppId = '${currentAppId}';
    </script>

</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
    <div class="container">
        <div class="row">
            <div class="col-sm-6">
                <div class="input-group mb-3">
                    <%--<div class="input-group-prepend">--%>
                        <%--<span class="input-group-text" id="searchText">所属业务流程模板:</span>--%>
                    <%--</div>--%>
                    <input type="text" class="form-control" id="searchContent" aria-describedby="searchText" placeholder="请输入关键字..." >
                    <butten class="btn btn-secondary" onclick="searchActTplAppFlowdef();" >查询</butten>
                </div>
            </div>
            <%--<div class="col-sm-6">--%>
                <%--<butten class="btn btn-info">新建业务流程模板</butten>--%>
                <%--<butten class="btn btn-info">导入业务流程模板</butten>--%>
            <%--</div>--%>
        </div>
        <div>
            <table  id="processTable"
                    <%--data-height="100%",--%>
                    data-undifined="-",
                    data-striped="false",
                    data-toggle="table",
                    data-click-to-select=true,
                    <%--data-pagination="false",--%>
                    <%--data-page-size="10",--%>
                    <%--data-side-pagination="client",--%>
                    <%--data-page-list="[10, 20, 50, 100]",--%>
                    <%--data-query-params-type="",--%>
                    data-query-params="listActTplAppFlowdefParam",
                    <%--data-pagination-show-page-go="true",--%>
                    <%--data-maintain-selected = "true",--%>
                    data-url="${pageContext.request.contextPath}/aea/par/stage/listActTplAppFlowdef.do">
                <thead>
                <tr>

                    <th data-field="" data-checkbox="true"
                        data-align="center" data-colspan="1" data-width="10" ></th>
                    <th data-field="appFlowdefId"  data-align="left" data-width="10" data-visible="false">appFlowdefId</th>
                    <th data-field="appId"  data-align="left" data-width="10" data-visible="false">appId</th>
                    <th data-field="startEl"  data-align="left" data-width="50">激活条件</th>
                    <th data-field="procName" data-align="left" data-width="50">阶段流程</th>
                    <th data-field="_operator" data-formatter="operate"
                        data-align="center" data-width="20">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

<script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/processModeler.js"></script>

<script type="text/javascript">

    var commonQueryParams = {};


    function searchActTplAppFlowdef(){
        commonQueryParams = {};

        commonQueryParams['stageId'] = currentBusiId;
        commonQueryParams['keyword'] = $("#searchContent").val();
        // $("#processTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#processTable").bootstrapTable('refresh',commonQueryParams);       //无参数刷新
    }

    function listActTplAppFlowdefParam(params) {
        commonQueryParams['stageId'] = currentBusiId;
        commonQueryParams['keyword'] = $("#searchContent").val();
        return commonQueryParams;
    }

    function operate(value, row, index) {

        var editBtn = '<a href="javascript:editAppFlowdef(\'' + row.appFlowdefId + '\')" ' +
                'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                'title="编辑"><i class="la la-edit"></i>' +
                '</a>';

        var editProcBtn = '<a href="javascript:chooseProcDef(\'' + row.modelId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="编辑流程"><i class="la la-edit"></i>' +
                        '</a>';

        var deleteBtn = '<a href="javascript:deleteAppFlowdef(\''+row.appFlowdefId + '\')" ' +
                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                'title="删除"><i class="la la-trash"></i>' +
                '</a>';

        return editBtn + editProcBtn+ deleteBtn;

    }

    //选择流程定义
    function chooseProcDef(appProcDeModelId) {
        window.open(ctx + '/admin/process/modeler/modelerIndex.do?appId='+currentAppId+'#/editor/' + appProcDeModelId, '_blank');
    }
</script>


</body>
</html>