<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>服务资质关联操作</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-html/css/theme.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 100%;margin: 0px;">
    <div class="col-xl-6" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            服务列表
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <form id="search_root_rel_service_form" method="post">
                            <input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   name="keyword" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </form>
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-secondary" onclick="clearRootRelService();">清空</button>
                        <button type="button" class="btn btn-info" onclick="searchRootRelService();">查询</button>
                        <button type="button" class="btn btn-info" onclick="addServiceModal();">新增</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="service_list_tb" >
                        <thead>
                        <tr>
                            <th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
                            <th data-field="serviceId" data-align="left" data-colspan="1" data-width="200">服务Id</th>
                            <th data-field="serviceName" data-align="left" data-colspan="1" data-width="200">服务名称</th>
                            <th data-field="serviceCode" data-align="left" data-colspan="1" data-width="200">服务编码</th>
                            <th data-field="" data-formatter="rootRelServiceTypeOperatorFormatter" data-align="center" data-colspan="1" data-width="130">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-6" style="padding: 0px 8px 0px 2px;">
        <!-- 专业列表 -->
        <div id="type_rel_major_list_page" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">

                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                            <h3 class="m-portlet__head-text">
                                服务资质关联列表
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 5px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-md-12" style="padding: 0px;">
                                            <form id="search_service_rel_major_form" method="post">
                                                <div class="row" style="margin: 0px;">
                                                    <div class="col-4">
                                                        <button type="button" class="btn btn-info" onclick="editServiceQualRelated();">保存关联</button>
                                                        <button type="button" id="viewCheckedCertBtn" class="btn btn-info" onclick="deleteServiceQualByServiceId();">删除关联</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                <!-- 列表区域 -->
                                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                                    <table id="root_service_qual_list_tb">
                                        <thead>
                                        <tr>
                                            <th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
                                            <th data-field="qualId" data-align="left" data-colspan="1" data-width="200">资质Id</th>
                                            <th data-field="qualName" data-align="left" data-colspan="1" data-width="200">资质名称</th>
                                            <th data-field="qualCode" data-align="left" data-colspan="1" data-width="200">资质编码</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                                <!-- 列表区域end -->
                </div>

        </div>
    </div>
</div>

<%--新增、编辑服务类型窗口--%>
<div id="ae_service_type_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="ae_service_type_modal_title">新增服务</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="ae_service_form" onsubmit="return false" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <input id="serviceId" type="hidden" name="serviceId" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>服务名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="serviceName" name="serviceName" value=""  placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>服务编号：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="serviceCode"  name="serviceCode" value=""  placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;服务图标路径：</label>
                        <div class="col-lg-9">
                            <input id="imgUrl" class="form-control m-input" type="text" value=""
                                   name="imgUrl" placeholder="请选择服务图标" readonly onclick="selectImage('serviceIcon');">
                            <div class="input-group-append">
                                <span class="input-group-text" onclick="selectImage('serviceIcon');">
                                     <i class="la la-tag">服务图标</i>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;采购公告图标路径：</label>
                        <div class="col-lg-9">
                            <input id="purchaseImgUrl" class="form-control m-input" type="text" value=""
                                   name="purchaseImgUrl" placeholder="请选择采购公告图标" readonly onclick="selectImage('purchaseIcon');">
                            <div class="input-group-append">
                                 <span class="input-group-text" onclick="selectImage('purchaseIcon');">
                                       <i class="la la-tag">采购公告图标</i>
                                 </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;是否启用：</label>
                        <div class="col-lg-4">
                            <select type="text" class="form-control" id="isActive" name="isActive" value="">
                                <option value="1" selected>启用</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" id="memo" name="memo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="button" onclick="updateService();" id="serviceButton" class="btn btn-info">保存</button>
                    <button type="button" onclick="closeServiceTypeModal();" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 设置图标信息 -->
<div id="common_set_img_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="common_set_img_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="common_set_img_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="common_set_img_scroll" style="height: 380px;overflow-x: auto;overflow-y: auto;">
                    <input id="selectedImg" type="hidden" name="selectedImg" value=""/> <!-- 选择的图标 -->
                    <div id="imgDataList"></div><!-- 图标数据集合 -->
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;">

                <button type="button" class="btn btn-secondary" onclick="$('#common_set_img_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--证书定义窗口--%>
<%@include file="qual_modal_page.jsp"%>

<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>
<!--bootstrap-table-->
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">

<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/service/service_qual.js" type="text/javascript"></script>
</body>
</html>