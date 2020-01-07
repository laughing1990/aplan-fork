<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${handWay=='1'?'情形材料配置':'情形事项配置'}</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-page.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">

        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var currentStateVerId = '${stateVerId}' || '';
        var currentUrlLoadData = ctx+'/rest/mind/affairItemSituation/v3/datajson/view/list.do';
        var currentUrlSaveData = ctx+ '/rest/mind/affairItemSituation/v3/save.do?stateVerId=' + currentStateVerId;
        var currentBusiScene = 'configSituation';
        var currentBusiLoadPara = {};

        //业务场景级-参数初始化
        currentBusiLoadPara.busiType = currentBusiType;
        currentBusiLoadPara.busiId = currentBusiId;
        currentBusiLoadPara.stateVerId = currentStateVerId;
        currentBusiLoadPara.showMat = true;
        currentBusiLoadPara.showCert = true;
        currentBusiLoadPara.showSituationLinkItem = false;
        currentBusiLoadPara.showForm = false;

        // 事项版本是否可以编辑
        var itemVerIsEditable = ${itemVerIsEditable};
        // 事项与情形版本下是否可以编辑
        var curIsEditable = ${curIsEditable};
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-bsc-mind.jsp"%>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css">

<!-- 思维导图导入  -->
<jsp:include page="include/mindFileImportModal.jsp"></jsp:include>

<!-- 阶段下情形材料列表  -->
<jsp:include page="../../../kitymind/stage/state/view_stage_state_mat_index.jsp"></jsp:include>

<!-- 阶段下情形关联事项  -->
<jsp:include page="stage_item_list.jsp"></jsp:include>

<!-- 阶段下材料关联事项  -->
<jsp:include page="state_mat_item_list.jsp"></jsp:include>

<!--  事项情形与通用材料 -->
<jsp:include page="include/itemMatAddModalNew.jsp"></jsp:include>

<!--  事项材料库导入 -->
<jsp:include page="include/itemGlobalMatModal.jsp"></jsp:include>

<!--  事项新增/编辑材料 -->
<%@include file="include/itemMatModal.jsp"%>

<!-- 情形版本modal -->
<jsp:include page="include/itemStateVerModal.jsp"></jsp:include>

<!-- 操作说明 -->
<%@include file="item_state_ver_opera_index.jsp"%>

<!-- 选择材料类别 -->
<%@include file="../../../aplanmis/item/select_mat_type_ztree.jsp"%>

<!-- 选择电子证照 -->
<%@include file="../../../common/ztree/select_cert_no_right_ztree.jsp" %>

<!-- 选择表单 -->
<%@include file="../../../common/ztree/select_form_no_right_ztree.jsp" %>

<!-- 选择标准材料 -->
<%@include file="../../../common/ztree/select_stdmat_no_right_ztree.jsp" %>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="uploadProgressModalLable" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 350px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">正在导入，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function(){

        if(handWay=='0'){
            currentBusiLoadPara['showMat']=false;
            currentBusiLoadPara['showCert']=false;
            currentBusiLoadPara['showSituationLinkItem']=true;
            currentBusiLoadPara['showForm']=false;
        }else{
            currentBusiLoadPara['showMat']=true;
            currentBusiLoadPara['showCert']=true;
            currentBusiLoadPara['showSituationLinkItem']=false;
            currentBusiLoadPara['showForm']=false;
        }

        $('#mindFilter .checkbox input').each(function(){

            $(this).on('click',function(){
                $('#mindFilter .checkbox input').each(function(){
                    var that = $(this);
                    var bindShow = that.attr('bindShow');
                    var isChecked = that.get(0).checked;
                    currentBusiLoadPara[bindShow] = isChecked;
                })
                refreshMind();
            });
        });
    });
</script>
<script src="${pageContext.request.contextPath}/ui-static/mat/global_mat_rel_format.js" type="text/javascript"></script>
</body>
</html>