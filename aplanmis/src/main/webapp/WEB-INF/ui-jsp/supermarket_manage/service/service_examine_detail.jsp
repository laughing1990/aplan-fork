<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>机构服务审批</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <%--<link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.css" />

    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 100%;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title w-100">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            企业发布服务详情
                        </h3>
                        <button type="button" onclick="javascript:history.back(-1);" class="btn btn-secondary float-right">返回上一页</button>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>

                <form id="unit_service_form" onsubmit="return false" method="post">
                    <div class="modal-body" style="padding: 10px;">
                        <input id="unitServiceId" type="hidden" name="unitServiceId" value="${unitServiceId}"/>
                        <input id="_auditFlag" type="hidden" name="auditFlag" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>中介服务：</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" id="serviceName" name="serviceName" value=""  placeholder="" readonly="readonly"/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>中介服务事项：</label>
                            <div class="col-lg-10">
                                <div class="base" style="padding: 0px 5px;">
                                    <table id="service_item_tb" >
                                        <thead>
                                        <tr>
                                            <%--<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>--%>
                                            <th data-field="num"  data-align="left" data-colspan="1" data-width="10"></th>
                                            <th data-field="itemName" data-align="left" data-colspan="1" data-width="100">服务事项名称</th>
                                            <th data-field="orgName" data-align="left" data-colspan="1" data-width="100">所属机构</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>从业人员：</label>
                            <div class="col-lg-10" id="linkmen">
                                <div class="base" style="padding: 0px 5px;">
                                    <table id="service_linkman_tb">
                                    </table>
                                </div>
                                <%--<input type="text" class="form-control m-input" id="linkmen"  name="linkmen" value=""  placeholder=""/>--%>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">收费参考：</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" id="feeReference" name="feeReference" rows="3" readonly="readonly" style="resize:none" ></textarea>
                            </div>
                        </div>
                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">服务承诺：</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" id="serviceContent" name="serviceContent" rows="3" readonly="readonly" style="resize:none" ></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">证书列表：</label>
                            <div class="col-lg-10">
                                <ul class="nav nav-tabs" id="cert_tabs">
                                </ul>
                                <div class="tab-content" id="cert_detail">
                                    <div class="tab-pane fade show active" id="tab1">
                                        <form id="certinst_form" onsubmit="return false" method="post">
                                            <div class="modal-body" style="padding: 10px;">
                                                <input id="serviceId" type="hidden" name="serviceId" value=""/>

                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证书名称：</label>
                                                    <div class="col-lg-10">
                                                        <%--<input type="text" class="form-control m-input" id="certinstName"  name="certinstName" value=""  placeholder="" readonly="readonly"/>--%>
                                                        <input type="text" class="form-control m-input" id="certinstName"  name="certinstName" value=""  placeholder="" readonly="readonly" style="border:none;"/>
                                                    </div>
                                                </div>
                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证书编码：</label>
                                                    <div class="col-lg-10">
                                                        <input type="text" class="form-control m-input" id="certinstCode"  name="certinstCode" value=""  placeholder="" readonly="readonly" style="border:none;"/>
                                                    </div>
                                                </div>

                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>有效期：</label>
                                                    <div class="col-lg-10">
                                                        <input type="text" class="form-control m-input" id="_time"  name="_time" value=""  placeholder="" readonly="readonly" style="border:none;"/>
                                                    </div>
                                                </div>
                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>所属专业：</label>
                                                    <div class="col-lg-10" id="majorTree">
                                                        <%--<ul id="treeDemo" class="ztree"></ul> --%>
                                                            <template>
                                                                <el-tabs type="border-card" class="selected-quals-tab">
                                                                    <el-tab-pane v-for="(majors, ind) in certinstMajor" :label="majors.name">
                                                                        <el-tree :data="majors.child" :props="defaultProps" default-expand-all></el-tree>
                                                                    </el-tab-pane>
                                                                </el-tabs>
                                                            </template>
                                                            </template>
                                                    </div>
                                                </div>

                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>等级：</label>
                                                    <div class="col-lg-10">
                                                        <input type="text" class="form-control m-input" id="qualLevelName"  name="qualLevelName" value=""  placeholder="" readonly="readonly" style="border:none;"/>
                                                    </div>
                                                </div>
                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>业务范围：</label>
                                                    <div class="col-lg-10">
                                                        <textarea class="form-control" id="managementScope" name="managementScope" rows="3" style="resize:none;border:none;" ></textarea>
                                                    </div>
                                                </div>
                                                <div class="form-group m-form__group row">
                                                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>附件列表：</label>
                                                    <div class="col-lg-10" id="attrs">
                                                        <%--<img src="./a.png" style="width:100px; height:100px ;margin: 5px;" /><img src="./a.png" style="width:100px; height:100px ;margin: 5px;" />--%>
                                                    </div>
                                                </div>


                                            </div>

                                        </form>
                                </div>
                                    <%--<div class="tab-pane fade" id="tab2">tab2</div>
                                    <div class="tab-pane fade" id="tab3">tab3</div>
                                    <div class="tab-pane fade" id="tab4">tab3</div>--%>
                                </div>
                            </div>
                        </div>
                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>审核意见：</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" id="memo" name="memo" rows="3" style="resize:none" ></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 10px;height: 60px;" id="_buttonGroup">
                        <button type="button" onclick="examineService('T');" id="serviceButtonT" class="btn btn-info">审核通过</button>
                        <button type="button" onclick="examineService('F');" id="serviceButtonF" class="btn btn-info">审核不通过</button>
                    </div>
                </form>

            </div>
    </div>


</div>
<%--证书详情窗口--%>
<%@include file="service_examine_certinst_modal.jsp"%>


<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>
<!--bootstrap-table-->
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/vue-v2/vue.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/service/service_examine.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/service/service_examine_detail.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/globalView/applicant/global_applicant_view.js" type="text/javascript"></script>

</body>
</html>