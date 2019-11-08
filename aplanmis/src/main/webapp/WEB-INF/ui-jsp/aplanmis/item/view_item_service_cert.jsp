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
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
</head>
<body>
<div class="container">
    <div class="row my-3 mx-auto">
        <div class="col-sm-12">
            <h3 class="m-portlet__head-text text-center">许可证件</h3>
        </div>
    </div>

    <form enctype="multipart/form-data" id="itemServiceCertForm">

        <input type="hidden" name="serviceCertId" value=""/>
        <input type="hidden" name="itemId" value="${itemId}"/>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="certName">名称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="certName" id="certName" readonly="readonly"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="certType">类型</label>
            <div class="col-sm-10 input-group">
                <input type="hidden" id="certTypeId" name="certTypeId" value=""/>
                <input type="text" class="form-control m-input" id="certType" name="certType" readonly >
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="validPeriod">有效期</label>
            <div class='col-sm-10 input-group'>
                <input type="text" class="form-control" name="validPeriod" id="validPeriod" readonly="true">
            </div>
        </div>
        <div class="form-group row">
            <lable class="col-sm-2 col-form-label text-right" for="certDirectoryCode">证照目录编码</lable>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="certDirectoryCode" id="certDirectoryCode" readonly="readonly"/>
            </div>
        </div>
        <div class="form-group row">
            <label for="dataId" class="col-sm-2 col-form-label text-right">省的数据ID</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="dataId" name="dataId"  readonly="readonly"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="lawEffect">法律效力</label>
            <div class="col-sm-10">
                    <textarea class="form-control" name="lawEffect" id="lawEffect" rows="8" readonly="readonly"></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="delayDesc">延期说明</label>
            <div class="col-sm-10">
                <textarea class="form-control" name="delayDesc" id="delayDesc" rows="8" readonly="readonly"></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right">证件样本上传</label>
            <div class="col-sm-10">
                <input type="hidden" id="certSampleUploadTicket" name="certSampleUploadTicket"/>
                <div id="certSampleUploadDocButton" class="form-group">
                    <button id="certSampleUploadDocDownLoad" type="button" class="btn btn-info" onclick="downloadCertAtt()">下载</button>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- 备注说明modal-->
<div class="modal fade" tabindex="-1" role="dialog" id="remarkModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">备注说明</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>证件许可说明</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<%@include file="select_service_cert_ztree.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/vendors/base/bootstrap-datepicker-local.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_cert.js?<%=isDebugMode%>"></script>
</body>
</html>

