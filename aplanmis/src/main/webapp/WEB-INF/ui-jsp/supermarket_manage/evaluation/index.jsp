<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>服务评价列表</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/supermarket_manage/evaluation/css/style.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
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
                            服务评价列表
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

                        审核状态：
                        <select class="m-input" onchange="changeEvaluationStatus(this)" >
                            <option value="">全部</option>
                            <option value="1">审核通过</option>
                            <option value="0">审核失败</option>
                            <option value="2">审核中</option>
                        </select>
                        <button id="searchEvaluationBtn" type="button" class="btn btn-info" >查询</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="evaluationTableId"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<%--编辑窗口--%>
<div id="evaluation_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="evaluation_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="contract_modal_title">查看服务评价</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="evaluation_form">
                <div class="modal-body" style="padding: 10px;">
                    <input id="serviceEvaluationId" type="hidden" name="serviceEvaluationId" />

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">项目名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="projName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">评价单位：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="evaluateUnitName" readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务质量：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="serviceQuality" readonly type="hidden" />
                            <div class="mark" id = "serviceQualityStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务时效：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="servicePrescription" readonly type="hidden" />
                            <div class="mark" id = "servicePrescriptionStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务态度：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="serviceAttitude" readonly type="hidden" />
                            <div class="mark" id = "serviceAttitudeStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务收费：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="serviceCharge" readonly type="hidden" />
                            <div class="mark" id = "serviceChargeStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">服务规范：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="serviceStandard" readonly type="hidden" />
                            <div class="mark" id = "serviceStandardStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">综合评价：</label>
                        <div class="col-lg-10">
                            <input  class="form-control m-input" name="compEvaluation" readonly  type="hidden" />
                            <div class="mark" id = "compEvaluationStar">
                                <ul class ="starUl">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">评价详情：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="memo" rows="3" readonly></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id = "auditResultDiv">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">审核结果：</label>
                        <div class="col-lg-10">
                            <input id="auditResult" type="text" class="form-control m-input"  readonly />
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id = "auditFlagSelect">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">审核结果：</label>
                        <div class="col-lg-10">
                            <select id = "auditFlag" class="form-control m-input">
                                <option value="0">审核不通过</option>
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
                    <button id = "saveBtn" type="button" class="btn btn-info" onclick="auditEvaluation();">提交</button>
                    <button type="button" onclick="closeEvaluationModal();" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>



<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/util/defaultBootstrap.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/evaluation/index.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>