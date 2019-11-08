<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>服务合同列表</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">

    </style>
</head>
<body>
<div id="mainContentPanel" style="width: 100%;height: 99%;margin: 0px;">
    <div style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            服务合同列表
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                            <input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   name="keyword" style="background-color: #f0f0f075;border-color: #c4c5d6;" id = "keyword">
                    </div>

                    <div class="col-xl-7">

                        合同状态：
                        <select class="m-input" onchange="changeContractStatus(this)" >
                            <option value="">全部</option>
                            <%--<option value="0">待确定</option>--%>
                            <option value="1">审核通过</option>
                            <option value="2">审核失败</option>
                            <option value="3">审核中</option>
                        </select>
                        <button id="searchContractBtn" type="button" class="btn btn-info" >查询</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="contractTableId"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<%--编辑窗口--%>
<div id="contract_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="contract_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="contract_modal_title">查看服务合同</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="contract_form">
                <div class="modal-body" style="padding: 10px;">
                    <input id="contractId" type="hidden" name="contractId" />

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">申请类型：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="operateAction" name="operateAction" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">项目名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="projName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务单位名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="serviceUnitInfoName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">业主单位名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="ownerUnitInfoName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">合同名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="contractName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">合同编码：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="contractCode" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">合同金额：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="price" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务起止时间：</label>
                        <div class="col-lg-10">
                            <input id="serviceStartTime" type="text" class="m-input" name="serviceStartTime" readonly />至
                            <input id="serviceEndTime" type="text" class="m-input" name="serviceEndTime" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务内容：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="serviceContent" rows="3" readonly></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="memo" rows="3" readonly></textarea>
                        </div>
                    </div>

                    <div  id = "attDiv" class="form-group m-form__group row"></div>

                    <div class="form-group m-form__group row" id = "auditResultDiv">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">审核结果：</label>
                        <div class="col-lg-10">
                            <input id="auditResult" type="text" class="form-control m-input"  readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id = "postponeServiceEndTimeDiv">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">延期时间：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id = "postponeServiceEndTime" name="postponeServiceEndTime" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id = "operateDescribeDiv">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">原因描述：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="operateDescribe" rows="3" readonly></textarea>
                        </div>
                    </div>

                    <div  id = "applyAttDiv" class="form-group m-form__group row"></div>

                    <div class="form-group m-form__group row" id = "auditFlagSelect">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">审核结果：</label>
                        <div class="col-lg-10">
                            <select id = "auditFlag" class="form-control m-input">
                                <option value="2">审核不通过</option>
                                <option value="1">审核通过</option>
                            </select>
                        </div>
                    </div>


                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">审核意见：</label>
                        <div class="col-lg-10">
                            <textarea id = "auditOpinion" class="form-control" name="auditOpinion" rows="3" ></textarea>
                        </div>
                    </div>


                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id = "saveBtn" type="button" class="btn btn-info" onclick="auditContract();">提交</button>
                    <button type="button" onclick="closeContractModal();" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>



<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/util/defaultBootstrap.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/contract/index.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>