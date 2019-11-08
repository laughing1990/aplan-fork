<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>项目需求采购发布详情</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.css" />
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/supermarket_manage/purchase/detail.css" rel="stylesheet" type="text/css">
    <%--<link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>--%>
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
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            项目基本信息
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">中介服务事项：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.itemName}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">事项对应部门：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.orgName}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">投资审批项目：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.isApproveProj == '1' ? '是' : '否'}</label>
                            </div>

                            <c:if test="${aeaImProjPurchase.isApproveProj == '1'}">
                                <label class="col-lg-2 text-right">投资审批项目编码：</label>
                                <div class="col-lg-5">
                                    <label>${aeaImProjPurchase.parentLocalCode}</label>
                                </div>
                            </c:if>
                        </div>
                        <c:if test="${aeaImProjPurchase.isApproveProj == '1'}">
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">投资审批项目名称：</label>
                                <div class="col-lg-10">
                                    <label>${aeaImProjPurchase.parentProjName}</label>
                                </div>
                            </div>
                        </c:if>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">采购项目编码：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.localCode}</label>
                            </div>

                            <label class="col-lg-2 text-right">审批业务流水号：</label>
                            <div class="col-lg-5">
                                <label>${aeaImProjPurchase.applyinstCode}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">项目业主：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.applicant}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">项目联系人：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.contacts}</label>
                            </div>

                            <label class="col-lg-2 text-right">联系电话：</label>
                            <div class="col-lg-5">
                                <label>${aeaImProjPurchase.moblie}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">采购项目名称：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.projName}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">项目规模：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.projScale}&nbsp;${aeaImProjPurchase.projScaleContent}</label>
                            </div>

                            <label class="col-lg-2 text-right">资金来源：</label>
                            <div class="col-lg-5">
                                <label>
                                    <c:if test="${aeaImProjPurchase.isFinancialFund == '1' && not empty aeaImProjPurchase.financialFundProportion}">
                                        财政资金 ${fn:replace(aeaImProjPurchase.financialFundProportion, '%', '')}%
                                    </c:if>
                                    &nbsp;&nbsp;
                                    <c:if test="${aeaImProjPurchase.isSocialFund == '1' && not empty aeaImProjPurchase.socialFundProportion}">
                                        社会资金 ${fn:replace(aeaImProjPurchase.socialFundProportion, '%', '')}%
                                    </c:if>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            服务要求
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">所需服务：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.serviceName}</label>
                            </div>

                            <label class="col-lg-2 text-right">服务要求：</label>
                            <div class="col-lg-5">
                                <label>${not empty aeaImProjPurchase.selectCondition ? (aeaImProjPurchase.selectCondition == '1' ? '多个服务具备其一' : '多个服务都具备') : ''}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">服务时限说明：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.serviceTimeExplain}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">服务内容：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.serviceContent}</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            中介机构要求
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">中介服务机构要求：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.unitRequire}</label>
                            </div>
                        </div>
                        <c:if test="${aeaImProjPurchase.isQualRequire == '1'}">
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">资质要求：</label>
                                <div class="col-lg-10">
                                    <label>${not empty aeaImProjPurchase.qualRequireType ? (aeaImProjPurchase.qualRequireType == '1' ? '多个资质子项符合其一即可' : '需同时符合所有选中资质子项') : ''}</label>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-lg-2 text-right">资质要求说明：</label>
                                <div class="col-lg-10">
                                    <label>
                                        ${aeaImProjPurchase.qualRequireExplain}
                                        <a href="javascript:;" onclick="showQualificationTree('${aeaImProjPurchase.unitRequireId}')" style="padding-left: 20px;">查看资质等级树</a>
                                    </label>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">资质备案说明：</label>
                                <div class="col-lg-10">
                                    <label>${aeaImProjPurchase.qualRecordRequire}</label>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${aeaImProjPurchase.isRegisterRequire == '1'}">
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">执业/职业人员总数：</label>
                                <div class="col-lg-3">
                                    <label>${aeaImProjPurchase.registerTotal}</label>
                                </div>

                                <label class="col-lg-2 text-right">执业/职业人员要求：</label>
                                <div class="col-lg-5">
                                    <label>${aeaImProjPurchase.registerRequire}</label>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${aeaImProjPurchase.isRecordRequire == '1'}">
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">备案要求说明：</label>
                                <div class="col-lg-10">
                                    <label>${aeaImProjPurchase.recordRequireExplain}</label>
                                </div>
                            </div>
                        </c:if>

                        <div class="form-group row">
                            <label class="col-lg-2 text-right">其他要求说明：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.otherRequireExplain}</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            选取中介服务机构方式
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">选取中介服务机构方式：</label>
                            <div class="col-lg-3">
                                <label>
                                    <c:choose>
                                        <c:when test="${aeaImProjPurchase.biddingType == '1'}">随机抽取</c:when>
                                        <c:when test="${aeaImProjPurchase.biddingType == '2'}">直接抽取</c:when>
                                        <c:when test="${aeaImProjPurchase.biddingType == '3'}">竞价选取</c:when>
                                        <c:otherwise>${aeaImProjPurchase.biddingType}</c:otherwise>
                                    </c:choose>
                                </label>
                            </div>

                            <label class="col-lg-2 text-right">${(aeaImProjPurchase.biddingType == '3' && aeaImProjPurchase.quoteType == '1') ? '下浮率' : '服务金额'}：</label>
                            <div class="col-lg-5">
                                <label>
                                    <c:choose>
                                        <c:when test="${aeaImProjPurchase.biddingType == '1'}">
                                            ${aeaImProjPurchase.isDefineAmount == '0' ? '暂不做金额评估与预算' : aeaImProjPurchase.basePrice}
                                            <c:if test="${aeaImProjPurchase.isDefineAmount == '1' && not empty aeaImProjPurchase.basePrice}">元</c:if>
                                        </c:when>
                                        <c:when test="${aeaImProjPurchase.biddingType == '2'}">
                                            ${aeaImProjPurchase.basePrice}元
                                        </c:when>
                                        <c:when test="${aeaImProjPurchase.biddingType == '3'}">
                                            <c:choose>
                                                <c:when test="${aeaImProjPurchase.quoteType == '0'}">
                                                    ${aeaImProjPurchase.basePrice}元 - ${aeaImProjPurchase.highestPrice}元
                                                </c:when>
                                                <c:when test="${aeaImProjPurchase.quoteType == '1'}">
                                                    ${aeaImProjPurchase.baseRate}% - ${aeaImProjPurchase.highestRate}%
                                                </c:when>
                                            </c:choose>
                                        </c:when>
                                    </c:choose>
                                </label>
                            </div>
                        </div>

                        <c:choose>
                            <c:when test="${aeaImProjPurchase.biddingType == '2'}">
                                <div class="form-group row">
                                    <label class="col-lg-2 text-right">指定的中介机构：</label>
                                    <div class="col-lg-7">
                                        <label>${aeaImProjPurchase.selectedApplicant}</label>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group row">
                                    <label class="col-lg-2 text-right">是否有回避情况：</label>
                                    <div class="col-lg-3">
                                        <label>${aeaImProjPurchase.isAvoid == '1' ? '是' : '否'}</label>
                                    </div>

                                    <c:if test="${aeaImProjPurchase.isAvoid == '1'}">
                                        <label class="col-lg-2 text-right">回避机构：</label>
                                        <div class="col-lg-5">
                                            <label>${aeaImProjPurchase.avoidUnitInfos}</label>
                                        </div>
                                    </c:if>
                                </div>

                                <c:if test="${aeaImProjPurchase.isAvoid == '1'}">
                                    <div class="form-group row">
                                        <label class="col-lg-2 text-right">回避原因：</label>
                                        <div class="col-lg-10">
                                            <label>${aeaImProjPurchase.avoidReason}</label>
                                        </div>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group row">
                            <label class="col-lg-2 text-right">报名截止时间：</label>
                            <div class="col-lg-3">
                                <label><fmt:formatDate pattern="yyyy-MM-dd" value="${aeaImProjPurchase.expirationDate}" /></label>
                            </div>

                            <label class="col-lg-2 text-right">选取中介时间：</label>
                            <div class="col-lg-5">
                                <label><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${aeaImProjPurchase.choiceImunitTime}" /></label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">金额说明：</label>
                            <div class="col-lg-10">
                                <label>${aeaImProjPurchase.amountExplain}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">是否公示中选机构：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.isDiscloseIm == '1' ? '是' : '否'}</label>
                            </div>

                            <label class="col-lg-2 text-right">是否公示中标公告：</label>
                            <div class="col-lg-5">
                                <label>${aeaImProjPurchase.isDiscloseBidding == '1' ? '是' : '否'}</label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">是否见证现场竞价选取、摇珠：</label>
                            <div class="col-lg-3">
                                <label>${aeaImProjPurchase.isLiveWitness == '1' ? '是' : '否'}</label>
                            </div>

                            <c:if test="${aeaImProjPurchase.isLiveWitness == '1'}">
                                <label class="col-lg-2 text-right">见证人：</label>
                                <div class="col-lg-5">
                                    <label>
                                            ${aeaImProjPurchase.witnessName1}（${aeaImProjPurchase.witnessPhone1}）
                                            <c:if test="${! empty aeaImProjPurchase.witnessName2}">
                                                、${aeaImProjPurchase.witnessName2}（${aeaImProjPurchase.witnessPhone2}）
                                            </c:if>
                                            <c:if test="${! empty aeaImProjPurchase.witnessName3}">
                                                、${aeaImProjPurchase.witnessName3}（${aeaImProjPurchase.witnessPhone3}）
                                            </c:if>
                                    </label>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						    </span>
                        <h3 class="m-portlet__head-text">
                            附件
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-12">
                        <div class="form-group row">
                            <label class="col-lg-2 text-right">批文文件：</label>
                            <div class="col-lg-3">
                                <c:forEach items="${aeaImProjPurchase.officialRemarkBscAttForms}" var="item">
                                    <label style="margin-right: 10px;"><a href="javascript:downFile('${item.detailId}')">${item.attDiskName}</a></label>
                                </c:forEach>
                                <c:if test="${empty aeaImProjPurchase.officialRemarkBscAttForms}"><label>暂无</label></c:if>
                            </div>

                            <label class="col-lg-2 text-right">要求说明文件：</label>
                            <div class="col-lg-5">
                                <c:forEach items="${aeaImProjPurchase.requireExplainBscAttForms}" var="item">
                                    <label style="margin-right: 10px;"><a href="javascript:downFile('${item.detailId}')">${item.attDiskName}</a></label>
                                </c:forEach>
                                <c:if test="${empty aeaImProjPurchase.requireExplainBscAttForms}"><label>暂无</label></c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${aeaImProjPurchase.auditFlag != '4' && aeaImProjPurchase.auditFlag != '0'}">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						    </span>
                            <h3 class="m-portlet__head-text">
                                审核结果
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-12">
                            <div class="form-group row">
                                <label class="col-lg-2 text-right">审核结果：</label>
                                <div class="col-lg-10">
                                    <label>${aeaImProjPurchase.auditFlag == '5' ? '审核不通过' : '审核通过'}</label>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-lg-2 text-right">审核意见：</label>
                                <div class="col-lg-10">
                                    <label>${aeaImProjPurchase.auditOpinion}</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="modal-footer text-center" style="display: inherit;">
                <c:if test="${aeaImProjPurchase.auditFlag == '4'}">
                    <button type="button" data-toggle="modal" data-target="#auditModal" class="btn btn-info">审核</button>
                </c:if>
                <a href="${pageContext.request.contextPath}/supermarket/purchase/index.do" class="btn btn-secondary">返回</a>
            </div>
        </div>




    </div>

</div>

<%--资质等级树窗口--%>
<div id="qualMajorModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title">资质等级树</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="qualTree">
                    <template>
                        <el-tabs type="border-card" class="selected-quals-tab">
                            <el-tab-pane v-for="(item, index) in selectedQuals" :label="item.qualName">
                                <template>
                                    <el-tabs type="card" class="aea-qual-levels">
                                        <el-tab-pane v-for="(itemLevels, ind) in item.qualLevelList" :label="itemLevels.qualLevelName">
                                            <el-tree :data="itemLevels.majors" :props="defaultProps" default-expand-all></el-tree>
                                        </el-tab-pane>
                                    </el-tabs>
                                </template>
                            </el-tab-pane>
                        </el-tabs>
                    </template>
                </div>
            </div>
        </div>
    </div>
</div>

<%--审核窗口--%>
<div id="auditModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="ae_service_type_modal_title">审核</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="auditForm" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <input id="projPurchaseId" type="hidden" name="projPurchaseId" value="${aeaImProjPurchase.projPurchaseId}"/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;是否通过：</label>
                        <div class="col-lg-4">
                            <select type="text" class="form-control" id="auditFlag" name="auditFlag" value="">
                                <option value="1" selected>审核通过</option>
                                <option value="2">审核不通过</option>
                            </select>
                        </div>
                        <label class="col-lg-2 col-form-label" style="text-align: right;"></label>
                        <div class="col-lg-4"></div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>审核意见：</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" id="auditOpinion" name="auditOpinion" rows="3" required></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info">保存</button>
                    <button type="button" data-dismiss="modal" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>
<!--bootstrap-table-->
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">

<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/vue-v2/vue.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/purchase/detail.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/globalView/applicant/global_applicant_view.js" type="text/javascript"></script>




</body>
</html>