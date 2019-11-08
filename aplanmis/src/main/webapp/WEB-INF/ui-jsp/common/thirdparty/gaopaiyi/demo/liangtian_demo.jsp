<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <title>良田高拍仪多浏览器</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/qwebchannel.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/liangtian_gaopaoyi.js?<%=isDebugMode%>"></script>

</head>

<body>
<div class="row">
    <div class="col-6">
        <button type="button" class="btn m-btn--square btn-info" onclick="lt_openPhotoWindow();">打开拍照窗口</button>
    </div>
    <div class="col-4"></div>
</div>
<%--引入窗口--%>
<jsp:include page="../liangtian_gaopaoyi.jsp"></jsp:include>
</body>
</html>
