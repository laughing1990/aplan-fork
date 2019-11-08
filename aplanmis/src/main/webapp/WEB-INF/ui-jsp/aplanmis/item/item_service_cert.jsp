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
        <input type="hidden" name="itemBasicId" value="${itemBasicId}"/>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="certName"><span style="color:red">*</span>名称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="请输入证件名称" name="certName" id="certName">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="certType"><span style="color:red">*</span>类型</label>
            <div class="col-sm-10 input-group">
                <input type="hidden" id="certTypeId" name="certTypeId" value=""/>
                <input type="text" class="form-control m-input" id="certType" name="certType" readonly placeholder="请选择证件类型..." >
                <div class="input-group-append"><span class="input-group-text open-mat-type"><i class="la la-tag"></i></span></div>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="validPeriod"><span style="color:red">*</span>有效期</label>
            <div class='col-sm-10 input-group date' id='datetimepicker'>
                <input type="text" class="form-control" placeholder="请输入有效期" name="validPeriod" id="validPeriod" readonly="true">
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group row">
            <lable class="col-sm-2 col-form-label text-right" for="certDirectoryCode"><span class="text-danger">*</span>证照目录编码</lable>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="certDirectoryCode" id="certDirectoryCode" />
            </div>
        </div>
        <div class="form-group row">
            <label for="dataId" class="col-sm-2 col-form-label text-right"><span class="text-danger">*</span>省的数据ID</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="dataId" name="dataId" />
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="lawEffect"><span style="color:red">*</span>法律效力</label>
            <div class="col-sm-10">
                    <textarea class="form-control" name="lawEffect" id="lawEffect" rows="8"></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="delayDesc"><span style="color:red">*</span>延期说明</label>
            <div class="col-sm-10">
                <textarea class="form-control" name="delayDesc" id="delayDesc" rows="8"></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label text-right" for="certSampleUpload"><span style="color:red">*</span>证件样本上传</label>
            <div class="col-sm-10">
                <input type="file" class="form-control" name="certSampleUpload" id="certSampleUpload" multiple="multiple">
                <input type="hidden" id="certSampleUploadTicket" name="certSampleUploadTicket"/>
                <div id="certSampleUploadDocButton" class="form-group">
                    <button id="certSampleUploadDocDownLoad" type="button" class="btn btn-info" onclick="downloadCertAtt()">下载</button>
                    <button id="certSampleUploadDocDelete" type="button" class="btn btn-danger" onclick="deleteCertAtt()">删除</button>
                </div>
            </div>
        </div>
        <div class="form-group m-form__group row" >
            <div class="col-lg-12" style="text-align: center;">
                <%--<button type="submit" class="btn btn-primary">暂存</button>--%>
                <button type="submit" id="saveBtn" class="btn btn-primary">保存</button>
                <%--<button type="submit" class="btn btn-primary">提交审查</button>--%>
                <button type="button" class="btn btn-primary">关闭</button>
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

