<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:if test="${empty currentAssertPath}">
    <c:set var="currentAssertPath" value="ui-static/agcloud/framework/ui-themes/ocean-blue"/>
</c:if>
<!--begin::Base css -->
<link href="${pageContext.request.contextPath}/${currentAssertPath}/css/agcloud_metronic.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/${currentAssertPath}/css/agcloud_custom_expand.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-themes/common/metronic/css/vendors.bundle.css" rel="stylesheet" type="text/css" />

<!--begin::Base Scripts -->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/vendors/base/vendors.bundle.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
<!--end::Base Scripts -->

<%--不宜在全局模板中引入--%>
<%--
<script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>--%>
