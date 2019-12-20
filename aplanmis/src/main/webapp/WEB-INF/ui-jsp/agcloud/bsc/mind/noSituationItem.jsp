<%@ page contentType="text/html;charset=UTF-8" %>

<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>不分情形材料配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp"%>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '${stateVerId}';
        var curIsEditable = ${curIsEditable};
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/layui.global.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/modules/layui-icon-extend/iconfont.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css">
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

        .current-temp-item .caret, .theme-item-selected .caret {
            margin-left: 0px;
        }

        .tab-content .km-priority .km-priority-item .km-priority-icon {
            background: url("${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/iconpriority-1.png") repeat-y;
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

        .m-form {
            margin-top: 2%;
            max-width: 100% !important;
        }
    </style>
</head>
<body>
    <!-- 导航 -->
    <jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>

    <!-- 主要显示内容区域 -->
    <div class="layui-fluid">
        <div class="container skipElem" style="position: absolute;">
            <jsp:include page="include/no_item_state_content.jsp"></jsp:include>
        </div>
    </div>

    <!-- 选择电子证照 -->
    <%--<%@include file="item_no_state_cert_list.jsp"%>--%>

    <!-- 导入库材料 -->
    <%@include file="../../../aplanmis/item/select_item_inout_global_mat.jsp"%>

    <!-- 新增/编辑材料 -->
    <%@include file="../../../aplanmis/item/aedit_item_inout_mat_index.jsp"%>

    <!-- 查看证照 -->
    <%--<%@include file="../../../aplanmis/item/aedit_item_inout_cert_index.jsp"%>--%>

    <!-- 选择材料类别 -->
    <%@include file="../../../aplanmis/item/select_mat_type_ztree.jsp"%>

    <!-- 选择电子证照 -->
    <%@include file="../../../common/ztree/select_cert_no_right_ztree.jsp" %>

    <!-- 选择表单 -->
    <%@include file="../../../common/ztree/select_form_no_right_ztree.jsp" %>

    <!-- 选择标准材料 -->
    <%@include file="../../../common/ztree/select_stdmat_no_right_ztree.jsp" %>

    <!-- 查看材料附件 -->
    <%@include file="../../../aplanmis/item/show_mat_att_modal.jsp"%>

    <%--<!-- 导入表单 -->--%>
    <%--<%@include file="item_no_state_form_list.jsp"%>--%>

    <!-- 材料、证照、表单排序 -->
    <%@include file="item_no_state_sort.jsp"%>

    <!-- 进度弹窗 -->
    <div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_item_dept" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
            <div class="modal-content">
                <div class="modal-body" style="text-align: center;padding: 10px;">
                    <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                    <div id="uploadProgressMsg" style="padding-top: 5px;">数据修复中，请稍后...</div>
                </div>
            </div>
        </div>
    </div>

    <!-- 业务js -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/no_item_state_index.js?<%=isDebugMode%>"></script>
    <script src="${pageContext.request.contextPath}/ui-static/mat/global_mat_rel_format.js" type="text/javascript"></script>
</body>
</html>