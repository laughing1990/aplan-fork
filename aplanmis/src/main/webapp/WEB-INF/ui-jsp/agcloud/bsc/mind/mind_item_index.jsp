<%@ page language="java" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>服务咨询管理界面</title>
    <!-- 所有JSP必须引入的公共JSP子页面 -->
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <!-- 引入页面模板 -->
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <script>
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
    </script>
    <style>
        .container {
            margin-top: 2%;
        }
        .container .m-form__group row {
            margin-top: 3%;
        }
    </style>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
<div class="container">
    <form  id="itemDetailForm">

        <input type="hidden" name="currentBusiType" value="${currentBusiType}"/>
        <input type="hidden" name="itemId" value="${currentBusiId}"/>
        <input type="hidden" name="orgId" value="${aeaItem.orgId}">

        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="orgName"><span style="color:red">*</span>实施机关</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" placeholder="请输入实施机关" name="orgName" id="orgName" value="${aeaItem.orgId}">
            </div>
            <label class="col-sm-2 col-form-label text-right" for="bbh"><span style="color:red">*</span>事项版本号</label>
            <div class="col-sm-4 input-group">
                <input type="text" class="form-control m-input" id="bbh" name="bbh" readonly placeholder="请输入事项版本号" value="${aeaItem.bbh}">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="itemCode"><span style="color:red">*</span>许可事项编号</label>
            <div class='col-sm-4 input-group date'>
                <input type="text" class="form-control" placeholder="请输入许可事项编号" name="itemCode" id="itemCode" readonly="true" value="${aeaItem.itemCode}">
            </div>
            <lable class="col-sm-2 col-form-label text-right" for="wtbm"><span class="text-danger">*</span>网厅编码</lable>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="wtbm" id="wtbm" placeholder="请输入网厅编码" value="${aeaItem.wtbm}">
            </div>
        </div>
        <div class="form-group row">
            <label for="itemName" class="col-sm-2 col-form-label text-right"><span class="text-danger">*</span>通用事项名称</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="itemName" name="itemName" placeholder="请输入通用事项名称" value="${aeaItem.itemName}">
            </div>
            <label class="col-sm-2 col-form-label text-right" for="standardItemCode"><span style="color:red">*</span>通用事项码</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="standardItemCode" name="standardItemCode" placeholder="请输入通用事项码" value="${aeaItem.standardItemCode}">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="isNeedState"><span style="color:red">*</span>是否有子项</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="isNeedState" name="isNeedState" placeholder="是否有子项" value="${aeaItem.isNeedState}">
            </div>
            <label class="col-sm-2 col-form-label text-right" for="itemType"><span style="color:red">*</span>事项性质</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="itemType" name="itemType" placeholder="请输入事项性质" value="${aeaItem.itemType}">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="fbsj"><span style="color:red">*</span>发布时间</label>
            <div class='col-sm-4 input-group date' id='fbsjDiv'>
                <input type="text" class="form-control" placeholder="请选择发布时间" name="fbsj" id="fbsj" readonly="true" value="${aeaItem.fbsj}">
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            <label class="col-sm-2 col-form-label text-right" for="sssj"><span style="color:red">*</span>实施时间（发布时填写）</label>
            <div class='col-sm-4 input-group date' id='datetimepicker'>
                <input type="text" class="form-control" placeholder="请选择实施时间" name="sssj" id="sssj" readonly="true" value="${aeaItem.sssj}">
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="xkdx"><span style="color:red">*</span>许可对象</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="xkdx" name="xkdx" placeholder="请输入许可对象" value="${aeaItem.xkdx}">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="sfwsbl"><span style="color:red">*</span>是否网上办理</label>
            <div class="col-sm-4">
                <select class="form-control" id="sfwsbl" name="sfwsbl"></select>
            </div>
            <div class="col-sm-4">
                <select class="form-control" id="bwsblly" name="bwsblly"></select>
            </div>
        </div>
        <div class="form-group m-form__group row" >
            <div class="col-lg-12" style="text-align: center;">
                <button type="submit" id="saveBtn" class="btn btn-primary">保存</button>
                <%--<button type="button" class="btn btn-primary">关闭</button>--%>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/vendors/base/bootstrap-datepicker-local.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/mind_item_index.js?<%=isDebugMode%>"></script>

<script> /*设置导航上的class属性*/
$(function () {
    $(".flow_steps .stage li").each(function() {
        var matchUrl = $(this).attr('data-url');
        if (matchUrl != undefined) {
            for (var i = 0; i < matchUrl.split(',').length; i++) {
                if (location.href.indexOf(matchUrl.split(',')[i].trim()) > 0) {
                    $(this).addClass('current').siblings().removeClass('current');
                    break;
                }
            }
        }
    });
});
</script>
</body>
</html>

