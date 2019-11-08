<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>阶段不分情形材料设置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>

    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%--<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>--%>

    <script>
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>


    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/layui.global.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/modules/layui-icon-extend/iconfont.css"/>
    <%--<!-- bower:css -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/bootstrap/dist/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/lib/codemirror.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/hotbox/hotbox.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/kityminder-core/dist/kityminder.core.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/color-picker/dist/color-picker.min.css"/>
    <!-- endbower -->--%>

    <%--<link rel="stylesheet"--%>
    <%--href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/kityminder.editor.min.css">--%>
    <%--<link rel="stylesheet"--%>
    <%--href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/canvas.css"/>--%>

    <style type="text/css">
        .hid{
            display: none;
        }
        .wizard-step{
            display: none;
        }
        .wizard-step_current{
            display:block;
        }
        div.minder-editor-container {
            position: absolute;
            top: 60px;
            bottom: 0;
            left: 0;
            right: 0;
        }

        div.minder-editor {
            position: absolute;
            top: 60px;
            bottom: 0;
            left: 0;
            right: 0;
        }
        .flow_steps .stage {
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
        }
        .flow_steps .stage li:not(:last-child) {
            flex: 1;
            -ms-flex: 1;
            text-align: center\9;
            display: inline-block\9;
            line-height: 36px\9;
        }
        .flow_steps .stage .flow_steps_btn .line {
            display: inline-block;
            float: left\9;
        }
        .flow_steps .stage .flow_steps_btn .Allscreen {
            padding: 0px\9;
            float: left\9;
        }
        .flow_steps .down {
            width: 90px\9;
            height: 23px\9;
            line-height: 23px\9;
            left: 65%\9;
            top: -30px\9;
        }
        .flow_steps .stage .flow_steps_btn .close {
            float: left\9;
        }
        .flow_steps .stage .flow_steps_btn {
            flex: 0.2;
            -ms-flex: 0.2;
        }
        .col-6 {
            flex: 0 0 50%;
            -ms-flex: 0 0 50%;
            max-width: 50%;
        }
        .current-temp-item .caret, .theme-item-selected .caret {
            margin-left: 0px;
        }

        .tab-content .km-priority .km-priority-item .km-priority-icon {
            background: url("${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/iconpriority-1.png" /*tpa=http://19.104.11.180/resource/js/kityminder/images/iconpriority-1.png*//*tpa=http://19.104.11.180/resource/js/kityminder/images/iconpriority-1.png*/) repeat-y;
            background-color: transparent;
        }

        .tool-group .tool-group-item .tool-group-icon {
            width: 30px;
            padding-top: 18px;
            background-position-x: 6px !important;
            font-size: 12px;
        }

        .tab-content .km-progress, .tab-content .km-priority {
            margin-top: -5px;
        }
        /*.layui-fluid .skipElem{*/
        /*margin-top: 0;*/
        /*}*/
        .m-form {
            margin-top: 2%;
            max-width: 100% !important;
        }
    </style>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
<div class="layui-fluid">
    <div class="container skipElem" style="position: absolute;">
        <jsp:include page="include/parInItem.jsp"></jsp:include>
    </div>
</div>
<%--<!--新增关联配置-->--%>
<%@ include file="../mind/include/edit_stage_item_in.jsp" %>
<%@ include file="../mind/include/add_par_stage_In_item.jsp"%>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/jquery/dist/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/common/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/common/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/layui.all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/index.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/js/metronic/vendors.bundle.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_type_tree.js"></script>

<jsp:include page="include/stage_mat_dialog.jsp"></jsp:include><!--查询,新增,编辑材料 -->
<jsp:include page="include/select_aea_inout_cert.jsp"></jsp:include>

<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/phaseStage/noSituationMat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/phaseStage/noSiuationCert.js"></script>

<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>
</body>
</html>