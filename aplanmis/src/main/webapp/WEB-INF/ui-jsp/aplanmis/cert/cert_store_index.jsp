<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>电子证照库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>

    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>

    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/cert/ui-js/cert_store_index.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/cert/ui-js/aea_hi_certinst_list.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/cert/ui-js/bsc_att_detail_info.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agcloud-common/agcloud.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <%--<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">--%>
            <div class="col-xl-12" style="padding: 0px 8px 0px 2px;">
                <!-- 相关证照实例列表 -->
                <%--<div style="border-bottom: 1px solid #e8e8e8;"></div>--%>
                <div id="aea_hi_cert_list" style="display:none ;width: 100%;height: 100%;">
                    <%@include file="aea_hi_certinst_list.jsp" %>
                </div>
                <!-- 新增、编辑证照 -->
                <div id="add_aea_hi_cert" style="display:none ;width: 100%;height: 100%;">
                    <%@include file="add_aea_hi_certinst.jsp" %>
                </div>
                <!-- 文件列表信息 -->
                <div id="bsc_att_detail_info" style="display:none ;width: 100%;height: 100%;">
                    <%@include file="bsc_att_detail_info.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>

    <!-- 选择文件库 -->
    <%@include file="select_bsc_att_dir_ztree.jsp"%>
</body>
</html>