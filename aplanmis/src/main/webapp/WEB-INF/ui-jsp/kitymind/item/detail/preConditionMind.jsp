<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>事项前置条件</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-page.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">

        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '${currentStateVerId}';
        var curIsEditable = ${curIsEditable};
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var isNeedState = '${isNeedState}';

        var currentUrlLoadData = ctx + '/aea/item/basic/preConditionMind/list.do';
        var currentUrlSaveData = ctx + '/aea/item/basic/preConditionMind/save.do';
        var currentBusiScene = 'configPreCondition';
        var currentBusiLoadPara = {};

        //业务场景级-参数初始化
        currentBusiLoadPara.busiType = currentBusiType;
        currentBusiLoadPara.busiId = currentBusiId;
        currentBusiLoadPara.stateVerId = currentStateVerId;
        currentBusiLoadPara.showMat = false;
        currentBusiLoadPara.showCert = false;
        currentBusiLoadPara.showSituationLinkItem = false;
        currentBusiLoadPara.showForm = false;

    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-bsc-mind.jsp"%>
</head>
<body>
    <div class="layui-fluid">
        <div class="container">
            <div class="layui-btn-group fr" style="float: right;display:none;">
                <a class="layui-text-blue" onclick="funPreView()"><i class="iconfont layui-extend-siutation-preview"></i> 预览</a>
                &nbsp;
                <a class="layui-text-blue" onclick="funCombination()"><i class="iconfont layui-extend-fabu"></i> 发布</a>
                &nbsp;
                <a class="layui-text-orange" onclick="funShowbackVersionPanel()"><i
                        class="iconfont layui-extend-banben"></i> 历史版本</a>
            </div>
        </div>
        <div ng-app="kityminderDemo" ng-controller="MainController" style="margin-top: 6%; top: 14%; height: 100%;">
            <kityminder-editor on-init="initEditor(editor, minder)" data-theme="fresh-green"></kityminder-editor>
            <iframe name="frameFile" style="display:none;"></iframe>
        </div>
    </div>
    <script>
        var module = WEB_ROOT + '/affairItemMaterialMain';
    </script>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
</body>
</html>