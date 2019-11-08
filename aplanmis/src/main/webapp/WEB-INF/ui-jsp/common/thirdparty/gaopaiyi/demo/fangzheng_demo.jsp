<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <title>方正高拍仪</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/fangzheng_gaopaiyi.js?<%=isDebugMode%>"></script>

</head>
<body onLoad="initOCX()">
<div class="row">
    <div class="col-6">
        <button type="button" class="btn m-btn--square btn-info" onclick="fz_openPhotoWindow();">打开拍照窗口</button>
    </div>
    <div class="col-4"></div>
</div>
<%--InitializationCompleted 是控件的事件，表示控件加载完成--%>
<script type="text/javascript" FOR="Capture" EVENT="InitializationCompleted()" >
    catchInitFinishedMessage();
</script>
<%--引入窗口--%>
<jsp:include page="../fangzheng_gaopaoyi.jsp"></jsp:include>
</body>
</html>
