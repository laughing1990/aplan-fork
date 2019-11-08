<%--<%@ page import="java.util.Date" %>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<!DOCTYPE html>--%>
<%--<head>--%>
    <%--<!-- 所有JSP必须引入的公共JSP子页面 -->--%>
    <%--<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>--%>

    <%--<!-- 引入页面模板 -->--%>
    <%--&lt;%&ndash;<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>&ndash;%&gt;--%>
    <%--<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-themes/common/metronic/css/vendors.bundle.css" rel="stylesheet" type="text/css" />--%>
    <%--<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/scheme2/components/css/style.bundle.css" rel="stylesheet" type="text/css">    <!--覆盖叠加更换主题色样式-->--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/img-icon.css" rel="stylesheet" type="text/css"/>--%>
    <%--<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/scheme2/admin/css/blue/theme.css" rel="stylesheet" type="text/css">--%>
    <%--<!--覆盖叠加调整布局与边距等样式-->--%>
    <%--<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>--%>
    <%--<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/parallel-approve.css" type="text/css" rel="stylesheet"/>--%>

    <%--<!--end::Base Styles -->--%>
    <%--<!--begin::Base Scripts -->--%>

    <%--<!--end::Base Scripts -->--%>

    <%--<style type="text/css">--%>
        <%--.second .tab-one-content {--%>
            <%--border: none;--%>

        <%--}--%>
        <%--.tab-pane {--%>
            <%--border: 1px solid #dee2e6;--%>
            <%--border-top: none;--%>
            <%--padding: 10px;--%>
        <%--}--%>

        <%--.nav.nav-pills, .nav.nav-tabs {--%>
            <%--margin-bottom: 0px;--%>
        <%--}--%>

        <%--.content .approval-content {--%>
            <%--margin-right: 0;--%>
        <%--}--%>

        <%--.modal .modal-content .modal-header {--%>
            <%--padding: 10px 12px 5px 12px;--%>
        <%--}--%>

        <%--.modal-title, .modal .modal-content .modal-header .close {--%>
            <%--line-height: 45px;--%>
        <%--}--%>

        <%--.modal .modal-content .modal-body {--%>
            <%--padding: 15px;--%>
        <%--}--%>

        <%--.table-bordered th{--%>
            <%--text-align: right;--%>
        <%--}--%>

        <%--div[align="center"] {--%>
            <%--bottom: 0;--%>
            <%--width: 100%;--%>
        <%--}--%>
        <%--.form-control.m-input--solid{--%>
            <%--background-color:rgba(0,0,0,0);--%>
        <%--}--%>
        <%--.title-nav {--%>
            <%--line-height: 40px;--%>
            <%--background:#FFFAF4;--%>
            <%--border:1px solid #F1DCCD;--%>
            <%--border-radius:4px;--%>
            <%--text-align: center;--%>
            <%--font-size: 15px;--%>
        <%--}--%>
        <%--.title-nav a {--%>
            <%--color: #FF8800;--%>
        <%--}--%>
        <%--#div_step_3{--%>
            <%--position: relative;--%>
        <%--}--%>
        <%--#add_project{--%>
            <%--position: absolute;--%>
            <%--top: 12px;--%>
            <%--right: 45px;--%>
            <%--line-height: 20px;--%>
            <%--cursor: pointer;--%>
        <%--}--%>
        <%--#menu {--%>
            <%--top: 60px;--%>
        <%--}--%>
        <%--.option{--%>
            <%--border:1px solid #eee;--%>
            <%--cursor:pointer;--%>
        <%--}--%>
        <%--.option:hover{--%>
            <%--border:1px solid #36a3f7!important;--%>
        <%--}--%>
        <%--.on{--%>
            <%--border:1px solid #36a3f7!important;--%>
        <%--}--%>


        <%--.modal-title, .modal .modal-content .modal-header .close{--%>
            <%--line-height:24px;--%>
        <%--}--%>
        <%--.bootstrap-select.form-control.input-group-btn{--%>
            <%--width:80%;--%>
        <%--}--%>

        <%--.applicant_list{--%>
            <%--position: absolute;--%>
            <%--top: 35px;--%>
            <%--left: 0px;--%>
            <%--width: 100%;--%>
            <%--background: #fff;--%>
            <%--z-index: 999;--%>
            <%--border: 1px solid #e8e8e8;--%>
            <%--border-top: none;--%>
            <%--height: 202px;--%>
            <%--overflow-y: scroll;--%>
            <%--overflow-x: hidden;--%>
        <%--}--%>
        <%--.applicant_list li{--%>
            <%--height: 35px;--%>
            <%--line-height: 35px;--%>
            <%--border-bottom: 1px solid #f3f3f3;--%>
            <%--text-indent: 17px;--%>
            <%--display: -webkit-box;--%>
            <%--display: -ms-flexbox;--%>
            <%--display: flex;--%>
            <%---webkit-box-align: center;--%>
            <%---ms-flex-align: center;--%>
            <%--align-items: center;--%>
            <%---webkit-box-pack: justify;--%>
            <%---ms-flex-pack: justify;--%>
            <%--justify-content: space-between;--%>
            <%--padding-right: 18px;--%>
            <%--cursor:pointer;--%>
        <%--}--%>
        <%--.applicant_list li:hover{--%>
            <%--background:#e8e8e9;--%>
        <%--}--%>
        <%--.col-form-label {--%>
            <%--text-align: right;--%>
        <%--}--%>
        <%--#dg_accordion_3_item_3_head {--%>
            <%--position: relative;--%>
        <%--}--%>
        <%--#lastSelectedBtn {--%>
            <%--position: absolute;--%>
            <%--right: 40px;--%>
            <%--top: 7px;--%>
        <%--}--%>
        <%--#saveOrUpdateProjLabel {--%>
            <%--position: absolute;--%>
            <%--top: 15px;--%>
            <%--right: 20px;--%>
            <%--line-height: 15px;--%>
        <%--}--%>
        <%--.ztree li span.button.ico_docu {--%>
            <%--vertical-align: text-bottom;--%>
        <%--}--%>
        <%--.ztree li {--%>
            <%--line-height: 24px;--%>
        <%--}--%>
        <%--.ztree li span.button.chk {--%>
            <%--margin: 0px 6px 0 3px;--%>
        <%--}--%>

        <%--.cond_tips{--%>
            <%--font-size: 14px;--%>
            <%--color: #666;--%>
            <%--line-height: 24px;--%>
            <%--padding: 10px;--%>
            <%--background: #f7f7f7;--%>
            <%--border-radius: 3px;--%>
            <%--margin: 10px 26px -8px 26px;--%>
        <%--}--%>
    <%--</style>--%>

<%--</head>--%>

<%--<body style="background-color:white;padding: 25px;">--%>
<%--<div class="title-nav">--%>
    <%--<span>投资项目申报前，请登陆 <a href="http://tzxm.hbzwfw.gov.cn/sbglweb/gsxxList?xzqh=130000" target="_blank">“河北省投资项目在线审批监管平台”</a> 进行项目登记赋码</span>--%>
<%--</div>--%>
<%--<input type="hidden" id="hidItemId" value="${itemVerId}"/>--%>
<%--<div class="vertical" id="menu">--%>
    <%--<div class="vertical-time">--%>
        <%--<ul>--%>
            <%--<li>--%>
                <%--<a href="javascript:;"></a>--%>
                <%--<b class="line"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox active" data-name="div_step_1">--%>
                <%--<a href="#div_step_1">申报事项信息</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_2">--%>
                <%--<a href="#div_step_2">申报项目信息</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_3" style="display:none">--%>
                <%--<a href="#div_step_3">申办主体信息</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_4" style="display:none">--%>
                <%--<a href="#div_step_4">前置条件信息</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_5" style="display:none">--%>
                <%--<a href="#div_step_5">申报条件信息</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_6" style="display:none">--%>
                <%--<a href="#div_step_6">材料清单</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="ciclebox" data-name="div_step_7" style="display:none">--%>
                <%--<a href="#div_step_7">办证结果取件方式</a>--%>
                <%--<b class="cicle"></b>--%>
            <%--</li>--%>
            <%--<li class="lilastCicle">--%>
                <%--<a href="javascript:;"></a>--%>
                <%--<b></b>--%>
            <%--</li>--%>
        <%--</ul>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<div class="vertical-right">--%>
    <%--<div class="m-accordion m-accordion--default m-accordion--toggle-arrow"--%>
         <%--id="dg_accordion_0" style="margin-top:13px;">--%>
        <%--<div class="m-accordion__item active div-step" id="div_step_1">--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_0_item_0_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_0_item_0_body" aria-expanded="true">--%>
             <%--<span class="m-accordion__item-icon">--%>
                <%--<i class="line-width"></i>--%>
            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                <%--申报事项信息--%>
            <%--</span>--%>
                <%--<span class="m-accordion__item-mode">--%>
            <%--</span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_0_item_0_body"--%>
                 <%--aria-labelledby="dg_accordion_0_item_0_head"--%>
                 <%--data-parent="#dg_accordion_0">--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<div class="m-section__content">--%>
                        <%--<div class="form-group m-form__group row">--%>
                            <%--<label class="col-2 col-form-label">--%>
                                <%--事项名称--%>
                            <%--</label>--%>
                            <%--<div class="col-10">--%>
                                <%--<input id="mainItemName" name="mainItemName"--%>
                                       <%--type="text"--%>
                                       <%--class="form-control form-control-warning">--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="form-group m-form__group row">--%>
                            <%--<label class="col-2 col-form-label">--%>
                                <%--服务对象--%>
                            <%--</label>--%>
                            <%--<div class="col-4">--%>
                                <%--<input id="serviceObject" name="serviceObject"--%>
                                       <%--type="text"--%>
                                       <%--class="form-control form-control-warning">--%>
                            <%--</div>--%>

                            <%--<label class="col-2 col-form-label">--%>
                                <%--受理机构--%>
                            <%--</label>--%>
                            <%--<div class="col-4">--%>
                                <%--<input id="acceptInsition" name="acceptInsition"--%>
                                       <%--type="text"--%>
                                       <%--class="form-control form-control-warning">--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--<div class="m-accordion m-accordion--default m-accordion--toggle-arrow"--%>
         <%--id="dg_accordion_1">--%>
        <%--<!--begin::Item-->--%>
        <%--<div class="m-accordion__item active div-step" id="div_step_2" style="position: relative;overflow: visible;">--%>
            <%--&lt;%&ndash;<i class="fa flaticon-add" id="add_project"></i>&ndash;%&gt;--%>
            <%--<label class="m-checkbox m-checkbox--solid m-checkbox--brand" id="saveOrUpdateProjLabel">--%>
                <%--<input id="saveOrUpdateProj" onclick="saveOrUpdateProjFrom(false)" type="checkbox">保存&nbsp;&nbsp;&nbsp;&nbsp;<span></span>--%>
            <%--</label>--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_1_item_1_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_1_item_1_body" aria-expanded="true">--%>
            <%--<span class="m-accordion__item-icon">--%>
                <%--<i class="line-width"></i>--%>
            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                <%--申报项目信息--%>
            <%--</span>--%>
                <%--<span class="m-accordion__item-mode">--%>

            <%--</span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_1_item_1_body"--%>
                 <%--aria-labelledby="dg_accordion_1_item_1_head"--%>
                 <%--data-parent="#dg_accordion_1">--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<div class="m-section__content">--%>
                        <%--<div class="form-group m-form__group row">--%>
                            <%--<label class="col-2 col-form-label">--%>
                                <%--登记情形--%>
                            <%--</label>--%>
                            <%--<div class="col-10">--%>
                                <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                    <%--<input type="radio" selectCase="1" name="projSituationRradio" onchange="selectSituation('1',false);" checked="checked" >按统一项目编码申报--%>
                                    <%--<span></span>--%>
                                <%--</label>&nbsp;--%>
                                <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                    <%--<input type="radio" selectCase="2"  name="projSituationRradio" onchange="selectSituation('2',false);">按旧项目编码申报--%>
                                    <%--<span></span>--%>
                                <%--</label>&nbsp;--%>
                                <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                    <%--<input type="radio" selectCase="3"  name="projSituationRradio" onchange="selectSituation('3',false);">按无项目编码申报--%>
                                    <%--<span></span>--%>
                                <%--</label>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="form-group m-form__group row" id="projCodeInfo">--%>
                            <%--<label class="col-2 col-form-label">--%>
                                <%--项目代码--%>
                            <%--</label>--%>
                            <%--<div class="col-10">--%>
                                <%--<div class="input-group">--%>
                                    <%--<input type="text" id="dg_localCode" name="localCode" class="form-control moreCode" placeholder="投资平台登记的项目代码">--%>
                                    <%--<div class="input-group-append">--%>
                                        <%--<button class="btn btn-primary" id="onlineSearchBtn" type="button" onclick="queryProjInfo()" style="padding: 5px 8px;">--%>
                                            <%--联网查询--%>
                                        <%--</button>--%>
                                    <%--</div>&nbsp;--%>
                                    <%--<button class="btn btn-primary" id="projTreeBtn" type="button" onclick="showProjTree('dg_localCode',false)" style="padding: 5px 8px;">--%>
                                        <%--项目工程--%>
                                    <%--</button>--%>
                                    <%--<div id="addProjCodeInputBtn">&nbsp;--%>
                                        <%--<a href="javascript:void(0);" title="新增" onclick="addProjCodeInput(false);" class="btn btn-outline-info m-btn m-btn--icon m-btn--icon-only m-btn--pill m-btn--air">--%>
                                            <%--<i class="fa flaticon-plus"></i>--%>
                                        <%--</a>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div id="projCodeContent"></div>--%>
                        <%--<form id="proj_basc_info_show_from">--%>
                            <%--<input type="hidden" id="dg_projInfoId"  name="projInfoId" value="">--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--项目名称/工程名称--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<input type="text" id="dg_projName" autocomplete="off" onkeyup="searchProjInfoByName(false);" name="projName"--%>
                                               <%--value="" class="form-control m-input" placeholder="请输入项目名称">--%>
                                        <%--<div class="input-group-append">--%>
                                            <%--<button class="btn btn-primary" id="onlineSearchWithoutCode" type="button" onclick="searchWithoutCode('',false);" style="padding: 5px 8px;display:none;">--%>
                                                <%--联网查询--%>
                                            <%--</button>--%>
                                        <%--</div>--%>
                                        <%--<ul class="proj_Info_list miniScrollbar dropdown-menu inner hide" id="js_proj_Info_list">--%>

                                        <%--</ul>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--总投资（万元）--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="investSum" name="investSum" value="" class="form-control m-input" placeholder="请输入总投资">--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--用地面积（㎡）--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="xmYdmj" name="xmYdmj" value=""--%>
                                           <%--class="form-control m-input" placeholder="请输入用地面积">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--项目类型--%>
                                <%--</label>--%>
                                <%--<div class="col-4" id="projTypeDiv">--%>
                                    <%--<input type="text" id="projType" name="projType" class="form-control form-control-warning"--%>
                                           <%--placeholder="项目类型">--%>
                                <%--</div>--%>

                                <%--<label class="col-2 col-form-label">--%>
                                    <%--建设性质--%>
                                <%--</label>--%>
                                <%--<div class="col-4" id="projectNatureDiv">--%>
                                    <%--<input type="text" id="projNature" name="projNature"--%>
                                           <%--class="form-control form-control-warning" placeholder="建设性质">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--项目开工时间--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="nstartTimes" name="nstartTime"--%>
                                           <%--class="form-control form-control-warning" placeholder="项目开工时间">--%>
                                <%--</div>--%>

                                <%--<label class="col-2 col-form-label">--%>
                                    <%--项目建成时间--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="endTimes" name="endTime"--%>
                                           <%--class="form-control form-control-warning" placeholder="项目建成时间">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label approve-num-clazz">--%>
                                    <%--备案文号--%>
                                <%--</label>--%>
                                <%--<div class="col-4 approve-num-clazz">--%>
                                    <%--<input type="text" id="foreignApproveNum" name="foreignApproveNum" value=""--%>
                                           <%--class="form-control m-input" placeholder="请输入备案文号">--%>
                                <%--</div>--%>

                                <%--<label class="col-2 col-form-label">--%>
                                    <%--建设面积（㎡）--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input id="buildAreaSum" name="buildAreaSum"--%>
                                           <%--type="text"--%>
                                           <%--class="form-control form-control-warning">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--项目地址--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<input id="projAddr" name="projAddr"--%>
                                           <%--type="text"--%>
                                           <%--class="form-control form-control-warning">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--建筑内容--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<textarea id="scaleContent" name="scaleContent" rows="3" cols="80" class="form-control form-control-warning"></textarea>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--备注--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<textarea id="foreignRemark" name="foreignRemark" rows="3" cols="80" class="form-control form-control-warning"></textarea>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</form>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--<div class="m-accordion m-accordion--default m-accordion--toggle-arrow"--%>
         <%--id="dg_accordion_2" style="display:none;">--%>
        <%--<!--begin::Item-->--%>
        <%--<div class="m-accordion__item active div-step" id="div_step_3" style="position: relative;overflow: visible;">--%>
            <%--<label class="m-radio m-radio--solid m-radio--brand apply-person">--%>
                <%--<input type="radio" class="apply-pe"  name="applySubject" value="0" onchange="applySubjectChange(this)">个人--%>
                <%--<span></span>--%>
            <%--</label>--%>
            <%--<label class="m-radio m-radio--solid m-radio--brand apply-company">--%>
                <%--<input type="radio" class="apply-com" checked="checked" name="applySubject" value="1" onchange="applySubjectChange(this)">企业--%>
                <%--<span></span>--%>
            <%--</label>--%>
            <%--<label class="m-radio m-radio--solid m-radio--brand apply-not-company">--%>
                <%--<input type="radio" class="apply-not-com" name="applySubject" value="2" onchange="applySubjectChange(this)">非企业（机关或事业单位）--%>
                <%--<span></span>--%>
            <%--</label>--%>
            <%--<label class="m-checkbox m-checkbox--solid m-checkbox--brand" id="agentLabel"><input id="agent" onclick="isDaiBan(this)"   type="checkbox">经办<span></span></label>--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_2_item_2_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_2_item_2_body" aria-expanded="true">--%>
                                                <%--<span class="m-accordion__item-icon">--%>
                                                    <%--<i class="line-width"></i>--%>
                                                <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                                                    <%--申办主体信息--%>
                                                <%--</span>--%>
                <%--<span class="m-accordion__item-mode"></span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_2_item_2_body"--%>
                 <%--aria-labelledby="dg_accordion_2_item_2_head"--%>
                 <%--data-parent="#dg_accordion_2">--%>
                <%--<div class="m-accordion__item-content" id="unit_info_from_container">--%>
                    <%--<div id="jianshe" class="m-section__content">--%>
                        <%--<p id="apply_title" class="hide" style="color: #17a2b8;font-size: 16px; line-height: 34px;">建设单位信息</p>--%>
                        <%--<div id="apply_title_sp" class="approve-unit-splitline"></div>--%>
                        <%--<div class="form-group m-form__group row add-unitinfo-group">--%>
                            <%--<div class="col-8"></div>--%>
                            <%--<div class="col-4">--%>
                                <%--<div class="fr add-new-company">--%>
                                    <%--<button type="button" class="btn m-btn--pill  btn-outline-info btn-sm" onclick="addUnitInfoForm(1)">+ 新增建设单位</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div id="daiban" class="m-section__content hide">--%>
                        <%--<p style="color: #17a2b8;font-size: 16px;line-height: 34px;">经办单位信息</p>--%>
                        <%--<div class="approve-unit-splitline"></div>--%>
                        <%--<div class="form-group m-form__group row add-unitinfo-group">--%>
                            <%--<div class="col-8"></div>--%>
                            <%--<div class="col-4">--%>
                                <%--<div class="fr add-new-company">--%>
                                    <%--<button type="button" class="btn m-btn--pill  btn-outline-info btn-sm" onclick="addUnitInfoForm(0)">+ 新增经办单位</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div id="applyPerson" class="m-section__content hide">--%>
                        <%--<form id="applyPersonForm">--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>申请人--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<div>--%>
                                        <%--<div class="input-group float-left ">--%>
                                            <%--<input type="text" id="apply_linkmanId" name="apply_linkmanId" hidden/>--%>
                                            <%--<input type="text" id="apply_linkmanName" name="apply_linkmanName" class="form-control form-control-warning" placeholder="请输入申请人姓名">--%>
                                            <%--<ul class="applicant_list miniScrollbar hide" id="apply_list_ul"></ul>--%>
                                            <%--<div class="input-group-append">--%>
                                                <%--<button class="btn btn-primary m-btn--icon m-btn--icon-only" type="button" onclick="queryLinkman(0)" title="查询">--%>
                                                    <%--<i class="flaticon-search"></i>--%>
                                                <%--</button>--%>
                                                <%--&nbsp;--%>
                                                <%--<button class="btn btn-primary m-btn--icon m-btn--icon-only" type="button" onclick="cleanLinkman(0)" title="清除">--%>
                                                    <%--<i class="fa fa-trash-o"></i>--%>
                                                <%--</button>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>申请人身份证号--%>
                                <%--</label>--%>
                                <%--<div class="input-group float-left col-4">--%>
                                    <%--<input type="text" id="apply_linkmanIDCard" name="apply_linkmanIDCard" class="form-control form-control-warning" placeholder="请输入申请人身份证号">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>申请人电话--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="apply_linkmanTel" name="apply_linkmanTel" class="form-control form-control-warning" placeholder="请输入申请人电话">--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label">申请人邮箱</label>--%>
                                <%--<div class="input-group float-left col-4">--%>
                                    <%--<input type="text" id="apply_linkmanEmail" name="apply_linkmanEmail" class="form-control form-control-warning" placeholder="请输入申请人邮箱">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div style="margin: 20px 0px;border-bottom: 1px solid #e8e8e8;"></div>--%>
                            <%--<div class="row">--%>
                                <%--<label class="col-2 col-form-label"></label>--%>
                                <%--<label class="m-checkbox m-checkbox--solid m-checkbox--brand" style="margin-bottom: 10px;color: #afb2c1;margin-left: 14px;">--%>
                                    <%--<input type="checkbox" onclick="copyApplyLinkmanInfo(this)">与申请人信息一致--%>
                                    <%--<span></span>--%>
                                <%--</label>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>联系人--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<div>--%>
                                        <%--<div class="input-group float-left ">--%>
                                            <%--<input type="text" id="link_linkmanId" name="link_linkmanId" hidden/>--%>
                                            <%--<input type="text" id="link_linkmanName" name="link_linkmanName" class="form-control form-control-warning" placeholder="请输入联系人姓名">--%>
                                            <%--<ul class="applicant_list miniScrollbar hide" id="link_list_ul"></ul>--%>
                                            <%--<div class="input-group-append">--%>
                                                <%--<button class="btn btn-primary m-btn--icon m-btn--icon-only" type="button" onclick="queryLinkman(1)" title="查询">--%>
                                                    <%--<i class="flaticon-search"></i>--%>
                                                <%--</button>--%>
                                                <%--&nbsp;--%>
                                                <%--<button class="btn btn-primary m-btn--icon m-btn--icon-only" type="button" onclick="cleanLinkman(1)" title="清除">--%>
                                                    <%--<i class="fa fa-trash-o"></i>--%>
                                                <%--</button>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>联系人身份证号--%>
                                <%--</label>--%>
                                <%--<div class="input-group float-left col-4">--%>
                                    <%--<input type="text" id="link_linkmanIDCard" name="link_linkmanIDCard" class="form-control form-control-warning" placeholder="请输入联系人身份证号">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<i class="red-star">*</i>联系人电话--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<input type="text" id="link_linkmanTel" name="link_linkmanTel" class="form-control form-control-warning" placeholder="请输入联系人电话">--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label">联系人邮箱</label>--%>
                                <%--<div class="input-group float-left col-4">--%>
                                    <%--<input type="text" id="link_linkmanEmail" name="link_linkmanEmail" class="form-control form-control-warning" placeholder="请输入联系人邮箱">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<div class="col-8"></div>--%>
                                <%--<div class="col-4">--%>
                                    <%--<div class="fr">--%>
                                        <%--<button type="submit" class="btn btn-info">保存</button>--%>
                                        <%--<button type="button" class="btn btn-warning">清空</button>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</form>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="m-accordion__item active div-step" id="div_step_4">--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_4_item_4_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_4_item_4_body" aria-expanded="true">--%>
                                            <%--<span class="m-accordion__item-icon">--%>
                                                <%--<i class="line-width"></i>--%>
                                            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                                                <%--前置条件信息--%>
                                            <%--</span>--%>

                <%--<span class="m-accordion__item-mode"></span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_4_item_4_body"--%>
                 <%--aria-labelledby="dg_accordion_4_item_4_head"--%>
                 <%--data-parent="#dg_accordion_4">--%>
                <%--<div class="cond_tips">--%>
                    <%--告知承诺制审批，是指企业法人和其他组织（以下简称“申请人”）提出建设工程项目的事项审批申请，审批部门一次性告知审批条件及所需材料，申请人以书面形式承诺其符合审批条件，由审批部门作出事项审批决定的方式。--%>
                <%--</div>--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<%@ include file="sereisItemCond.jsp" %>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div class="m-accordion__item active div-step hide" id="div_step_5">--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_5_item_5_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_5_item_5_body" aria-expanded="true" style="position: relative;">--%>
                                            <%--<span class="m-accordion__item-icon">--%>
                                                <%--<i class="line-width"></i>--%>
                                            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                                                <%--申报条件信息--%>
                                            <%--</span>--%>

                <%--<span class="m-accordion__item-mode"></span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_5_item_5_body"--%>
                 <%--aria-labelledby="dg_accordion_5_item_5_head"--%>
                 <%--data-parent="#dg_accordion_5">--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<%@ include file="series_state.jsp" %>--%>
                    <%--<div class="row" style="border: 1px solid #f0f0f0;margin: 1px;padding: 11px;height:60px;" >--%>
                        <%--<div class="col-sm-3" style="margin-top:10px;"><font color="red">*</font>是否选择分局承办？</div>--%>
                        <%--<div id="isBranchOrg" class="col-sm-3 m-checkbox-inline" style="margin-top:10px;">--%>

                            <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                <%--<input value="0" name="isBranch" type="radio" checked onclick="isBranchOrg(this)"/>否<span></span>--%>
                            <%--</label>--%>

                            <%--<label class="m-radio m-radio--solid m-radio--brand" style="margin-right:40px;">--%>
                                <%--<input value="1" id="selectBrgId"  name="isBranch" type="radio" onclick="isBranchOrg(this)"/>是<span></span>--%>
                            <%--</label>--%>


                        <%--</div>--%>
                        <%--<div class="col-sm-6 hide" id="selBranchDiv">--%>
                            <%--<div class="input-group">--%>
                                <%--<input type="text"  name="branchOrg" value="" class="form-control moreCode" placeholder="请选择分局部门" readonly>--%>
                                <%--<div class="input-group-append">--%>
                                    <%--<button class="btn btn-primary" type="button" onclick="selBranchOrg(this)" style="padding: 5px 8px;">--%>
                                        <%--选择分局--%>
                                    <%--</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="m-accordion__item active div-step hide" id="div_step_6">--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_6_item_6_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_6_item_6_body" aria-expanded="true" style="position: relative;">--%>
                                            <%--<span class="m-accordion__item-icon">--%>
                                                <%--<i class="line-width"></i>--%>
                                            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                                                <%--材料清单--%>
                                            <%--</span>--%>

                <%--<button class="show-hide-fileDiv btn btn-sm btn-info">隐藏智能分拣区</button>--%>
                <%--<span class="m-accordion__item-mode"></span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_6_item_6_body"--%>
                 <%--aria-labelledby="dg_accordion_6_item_6_head"--%>
                 <%--data-parent="#dg_accordion_6">--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<div class="m-section__content" id="mat_content">--%>
                        <%--&lt;%&ndash;<table id="matTable" style="margin-top: 40px;" class="table"></table>&ndash;%&gt;--%>
                        <%--<div class="fileDiv">--%>
                            <%--<div class="upload-file" id="dropZone" draggable="true">--%>
                                <%--<div class="sel-file-stutas">1</div>--%>
                                <%--<label class="label" style="padding: 20px 0 0 20px;">附件上传区</label>--%>

                                <%--<div class="ac sel-file">--%>
                                    <%--<input type="file" class="hide" id="inputFile" name="files" multiple="multiple">--%>
                                    <%--<button class="btn btn-info btn-file btn-lg">选择文件夹/文件</button>--%>
                                    <%--<p class="m-font-gary">或将文件直接拖入这里，支持上传后自动分拣</p>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="upload-file file-selected">--%>
                                <%--<div class="sel-file-stutas">2</div>--%>
                                <%--<div class="sel-all-file ac">--%>
                                    <%--<label class="label">附件分拣区</label>--%>

                                    <%--<label class="m-checkbox m-checkbox--solid m-checkbox--brand">--%>
                                        <%--<input type="checkbox" id="check_all_upload_file" onclick="isCheckAll(this);">--%>
                                        <%--全选--%>
                                        <%--<span></span>--%>
                                    <%--</label>--%>
                                    <%--<button class="btn btn-info btn-lg" id="upload_file_btn" onclick="autoUploadFile();">一键自动分拣</button>--%>
                                    <%--<div id="imitate_upload_pic" style="display:none">--%>
                                        <%--<img style="width:130px;height:9px;" src="${pageContext.request.contextPath}/ui-static/aplanmis/matinst/imge/imitate_upload_gaitubao_com_100x7.gif"/>--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--<div class="file-list">--%>
                                    <%--<div class="file-list-info" id="showFileName">--%>

                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div style="padding-bottom:50px">--%>
                            <%--&lt;%&ndash;<span class="input-group-btn" style="float:left;margin-left: 5px;"><button onclick="easySign();" class="btn btn-primary" type="button">一键加签</button></span>&ndash;%&gt;--%>
                            <%--<span class="input-group-btn" style="float:left;margin-left: 5px;"><button onclick="PBSign();" class="btn btn-primary" type="button">一键加签</button></span>--%>
                            <%--<span class="input-group-btn" style="float:left;margin-left: 5px;"><button onclick="autoImportMat();" class="btn btn-primary" type="button">一键提取</button></span>--%>
                        <%--</div>--%>
                        <%--<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"--%>
                             <%--aria-labelledby="uploadProgressModalLable" aria-hidden="true">--%>
                            <%--<div class="modal-dialog modal-dialog-centered">--%>
                                <%--<div class="modal-content">--%>
                                    <%--<div class="modal-body" style="text-align: center;padding: 10px;">--%>
                                        <%--<img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>--%>
                                        <%--<div style="padding-top: 5px;">一键提取中，请稍等...</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<table class="table table-bordered ac thead-gary" id="matTable">--%>

                        <%--</table>--%>
                        <%--<jsp:include page="../view/fileWindow.jsp"></jsp:include>--%>
                        <%--<jsp:include page="../view/view_ItemMatInst_file.jsp"></jsp:include>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="m-accordion__item active div-step" id="div_step_7">--%>
            <%--<div class="m-accordion__item-head"--%>
                 <%--id="dg_accordion_7_item_7_head" data-toggle="collapse"--%>
                 <%--href="#dg_accordion_7_item_7_body" aria-expanded="true" style="position: relative;">--%>
                                            <%--<span class="m-accordion__item-icon">--%>
                                                <%--<i class="line-width"></i>--%>
                                            <%--</span>--%>
                <%--<span class="m-accordion__item-title">--%>
                                                <%--办证结果取件方式--%>
                                            <%--</span>--%>

                <%--<span class="m-accordion__item-mode"></span>--%>
            <%--</div>--%>
            <%--<div class="m-accordion__item-body collapse show"--%>
                 <%--id="dg_accordion_7_item_7_body"--%>
                 <%--aria-labelledby="dg_accordion_7_item_7_head"--%>
                 <%--data-parent="#dg_accordion_7">--%>
                <%--<div class="m-accordion__item-content">--%>
                    <%--<div class="m-section__content">--%>
                        <%--<form id="aea_hi_sms_info_from">--%>

                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<font color="red">*</font>领取模式--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                        <%--<input type="radio" value="0" name="receiveMode" onchange="selectReceiveMode('0');" checked="checked">邮政快递--%>
                                        <%--<span></span>--%>
                                    <%--</label>&nbsp;--%>
                                    <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                        <%--<input type="radio" value="1" name="receiveMode" checked onchange="selectReceiveMode('1');" >窗口取证--%>
                                        <%--<span></span>--%>
                                    <%--</label>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <%--<div class="form-group m-form__group row" id = "smsTypeDiv">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<font color="red">*</font>取件方式--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                        <%--<input type="radio" value="1" name="smsType" >上门取件--%>
                                        <%--<span></span>--%>
                                    <%--</label>&nbsp;--%>
                                    <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                        <%--<input type="radio" value="2" name="smsType" checked="checked" >普通快递--%>
                                        <%--<span></span>--%>
                                    <%--</label>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label" id = "addresseeNameLable">--%>
                                    <%--<font color="red">*</font>收件人名字--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<input type="text" id="addresseeName" name="addresseeName" class="form-control m-input">--%>
                                        <%--<ul class="applicant_list miniScrollbar hide" id="addresseeNameList">--%>
                                        <%--</ul>--%>
                                        <%--<div class="input-group-append">--%>
                                            <%--<button class="btn btn-primary m-btn--icon m-btn--icon-only" type="button" onclick="queryAddresseName('#addresseeName')" title="查询">--%>
                                                <%--<i class="flaticon-search"></i>--%>
                                            <%--</button>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<label class="col-2 col-form-label" id = "addresseePhoneLable">--%>
                                    <%--<font color="red">*</font>收件人联系方式--%>
                                <%--</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<input type="text" id="addresseePhone" name="addresseePhone" class="form-control m-input" >--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label" id = "addresseeIdcardLable">--%>
                                    <%--<font color="red">*</font>收件人身份证--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<input type="text" id="addresseeIdcard" name="addresseeIdcard" class="form-control m-input" >--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <%--<div class="form-group m-form__group row" id = "areaDiv">--%>
                                <%--<label class="col-2 col-form-label" >--%>
                                    <%--<font color="red">*</font>收件人所在区域--%>
                                <%--</label>--%>
                                <%--<br/>--%>

                                <%--<div class="col-10">--%>
                                    <%--<input id = "addresseeProvinceText"  name = "addresseeProvince" style="display: none;">--%>
                                    <%--<!--省份选择-->--%>
                                    <%--<select class="form-control" id="addresseeProvince"  style="width:auto;display: inline;" onchange="queryCity(this,'addresseeCity')">--%>
                                        <%--<option value="0">-请选择省份-</option>--%>
                                    <%--</select>--%>
                                    <%--<input id = "addresseeCityText"  name = "addresseeCity" style="display: none;">--%>
                                    <%--<!--城市选择-->--%>
                                    <%--<select class="form-control" id="addresseeCity"  style="width:auto;display: inline;" onchange="queryCountry(this,'addresseeCounty')">--%>
                                        <%--<option value="0">-请选择城市-</option>--%>
                                    <%--</select>--%>
                                    <%--<input id = "addresseeCountyText"  name = "addresseeCounty" style="display: none;">--%>
                                    <%--<!--县区选择-->--%>
                                    <%--<select class="form-control" id="addresseeCounty"  style="width:auto;display: inline;" onchange="selecCountry(this)">--%>
                                        <%--<option value="0">-请选择县区-</option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <%--<div class="form-group m-form__group row" id = "addresseeAddrDiv">--%>
                                <%--<label class="col-2 col-form-label">--%>
                                    <%--<font color="red">*</font>收件人详细地址--%>
                                <%--</label>--%>
                                <%--<div class="col-10">--%>
                                    <%--<textarea id="addresseeAddr" name="addresseeAddr" rows="3" cols="80" class="form-control form-control-warning"></textarea>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</form>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div align="center" style="margin-top: 130px;">--%>
            <%--<button class="btn btn-primary btn-sm not_receive_btn" type="button"  onclick="refuseAccept('m_modal_refuseDialog');">--%>
                <%--不收件--%>
            <%--</button>--%>
            <%--<button class="btn btn-primary btn-sm receive_btn" type="button"  onclick="startFlow('bumenshenpi','win');">--%>
                <%--收件--%>
            <%--</button>--%>
            <%--&lt;%&ndash;<button class="btn btn-primary btn-sm receive_btn" type="button"  onclick="showCommentDialog();">&lt;%&ndash;showView('m_modal_viewDialog')&ndash;%&gt;&ndash;%&gt;--%>
            <%--&lt;%&ndash;收件&ndash;%&gt;--%>
            <%--&lt;%&ndash;</button>&ndash;%&gt;--%>
        <%--</div>--%>

        <%--<div id="dg_modal_add_link_main_info" class="modal fade" tabindex="-1" role="dialog"--%>
             <%--aria-labelledby="modal_add_link_main_info_title" aria-hidden="true">--%>
            <%--<div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 700px;">--%>
                <%--<div class="modal-content">--%>
                    <%--<div class="modal-header">--%>
                        <%--<h5 class="modal-title" id="modal_add_link_main_info_title">新增联系人</h5>--%>
                        <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                            <%--<span aria-hidden="true">&times;</span>--%>
                        <%--</button>--%>
                    <%--</div>--%>
                    <%--<form id="dg_form_add_link_main_info" method="post">--%>
                        <%--<div class="m-portlet__body"--%>
                             <%--style="padding:15px 20px 10px 15px;max-height:650px;overflow-y:auto;overflow-x: hidden">--%>
                            <%--<input type="hidden" id="linkmanInfoId" name="linkmanInfoId" value=""/>--%>
                            <%--<input type="hidden" id="linkmanType" name="linkmanType"/>--%>

                            <%--<div id="unitInfoId_from_grop" class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>所在单位：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<select name="unitInfoId" id="unitInfoId" readonly="readonly"--%>
                                            <%--class="form-control m-input">--%>
                                        <%--<option></option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>联系人姓名：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanName" id="linkmanName" class="form-control m-input"--%>
                                           <%--placeholder="请填写联系人姓名">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>证件号：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanCertNo" id="linkmanCertNo" class="form-control m-input"--%>
                                           <%--placeholder="请填写证件号">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>手机号码：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanMobilePhone" id="linkmanMobilePhone"--%>
                                           <%--class="form-control m-input" placeholder="请填写手机号码">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--办公电话：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanOfficePhon" id="linkmanOfficePhon"--%>
                                           <%--class="form-control m-input" placeholder="">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--传真：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanFax" id="linkmanFax" class="form-control m-input"--%>
                                           <%--placeholder="">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--电子邮件：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="linkmanMail" id="linkmanMail" class="form-control m-input"--%>
                                           <%--placeholder="">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--联系人住址：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                <%--<textarea class="form-control m-input" id="linkmanAddr" name="linkmanAddr"--%>
                                          <%--rows="3"></textarea>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--备注：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<textarea class="form-control m-input" id="linkmanMemo" name="linkmanMemo"--%>
                                              <%--rows="3"></textarea>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="modal-footer" style="padding: 10px;height: 60px;">--%>
                            <%--<button type="submit" class="btn btn-info">保存</button>--%>
                            <%--<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>--%>
                        <%--</div>--%>
                    <%--</form>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div id="dg_modal_completion" class="modal fade" tabindex="-1" role="dialog"--%>
             <%--aria-labelledby="show_situation_modal_title" aria-hidden="true">--%>
            <%--<div class="modal-dialog modal-dialog-centered" role="document" style="width:1200px;max-width: 1200px;">--%>
                <%--<div class="modal-content" style="width:1200px;max-width: 1200px;height: 700px">--%>
                    <%--<div class="modal-header">--%>
                        <%--<h5 class="modal-title">材料录入</h5>--%>
                        <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                            <%--<span aria-hidden="true">&times;</span>--%>
                        <%--</button>--%>
                    <%--</div>--%>
                    <%--<div class="modal-body miniScrollbar">--%>
                        <%--<div class="m-accordion m-accordion--default m-accordion--toggle-arrow" id="m_accordion_1"--%>
                             <%--role="tablist">--%>
                            <%--<div class="m-accordion__item">--%>
                                <%--<div class="m-accordion__item-head" role="tab" id="m_accordion_item_1_head"--%>
                                     <%--data-toggle="collapse"--%>
                                     <%--href="#m_accordion_item_1_body" aria-expanded="true">--%>
                                    <%--<span class="m-accordion__item-title">申报材料信息</span>--%>
                                    <%--<span class="m-accordion__item-mode"></span>--%>
                                <%--</div>--%>
                                <%--<div class="m-accordion__item-body collapse show" id="m_accordion_item_1_body" class=" "--%>
                                     <%--role="tabpanel" aria-labelledby="m_accordion_item_1_head" data-parent="#m_accordion_1">--%>
                                    <%--<div class="m-accordion__item-content" style="padding: 0px">--%>
                                        <%--<div class="m-portlet__body" id="itemInfo" style="display: block;">--%>
                                            <%--<table class="table table-bordered">--%>
                                                <%--<tbody>--%>
                                                <%--<tr>--%>
                                                    <%--<th style="width: 15%">申请项目</th>--%>
                                                    <%--<td id="projName" style="width: 35%"></td>--%>
                                                    <%--<th style="width: 15%">申请时间</th>--%>
                                                    <%--<td id="applyDate" style="width: 35%"></td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th style="width: 15%">项目代码</th>--%>
                                                    <%--<td id="localCode" style="width: 35%"></td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>申报流水号</th>--%>
                                                    <%--<td id="applyNo" colspan="3" style="width: 35%"></td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>申请人单位(人)</th>--%>
                                                    <%--<td id="applyUnit" colspan="3" style="width: 35%"></td>--%>
                                                    <%--<th>单位证照好吗</th>--%>
                                                    <%--<td id="applyUnitNo" colspan="3" style="width: 35%"></td>--%>
                                                <%--</tr>--%>

                                                <%--<tr>--%>
                                                    <%--<th>联系人名称</th>--%>
                                                    <%--<td id="cLinkmanName" style="width: 35%"></td>--%>
                                                    <%--<th>联系人证件号</th>--%>
                                                    <%--<td id="cLinkmanNo" style="width: 35%"></td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>联系人手机号</th>--%>
                                                    <%--<th id="clinkmanPhone" style="width:35%"></th>--%>
                                                    <%--<th>联系人电话</th>--%>
                                                    <%--<th id="clinkmanOfficePhon" style="width:35%"></th>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>事项名称</th>--%>
                                                    <%--<td id="itemName" colspan="3" style="width: 35%"></td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>前置条件</th>--%>
                                                    <%--<td id="c5" colspan="3" style="width: 35%">无</td>--%>
                                                <%--</tr>--%>
                                                <%--<tr>--%>
                                                    <%--<th>申报条件信息</th>--%>
                                                    <%--<td id="c7" colspan="3" style="width: 35%">--%>
                                                        <%--<button type="button" class="btn btn-primary btn-sm"--%>
                                                                <%--onclick="showSituation()">--%>
                                                            <%--查看情形--%>
                                                        <%--</button>--%>
                                                    <%--</td>--%>
                                                <%--</tr>--%>
                                                <%--</tbody>--%>
                                            <%--</table>--%>
                                            <%--<table id="completTab"></table>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>


        <%--<div class="modal fade ag-aplan-modal" id="dg_modal_print_interface" tabindex="-1" role="dialog" aria-labelledby="dg_modal_print_interface_title" >--%>
            <%--<div class="modal-dialog modal-lg" role="document" style="margin: 10rem auto;">--%>
                <%--<div class="modal-content" style="width:1000px">--%>
                    <%--<div class="modal-header">--%>
                        <%--<h5 class="modal-title" id="dg_modal_print_interface_title">回执打印</h5>--%>
                        <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                            <%--<span aria-hidden="true">×</span>--%>
                        <%--</button>--%>
                    <%--</div>--%>
                    <%--<div class="modal-body">--%>
                        <%--<div id="printSwal" class="m-portlet__body" style="padding:15px 20px 15px 15px;max-height:650px;overflow-y:auto;overflow-x: hidden;display: flex;--%>
    <%--justify-content: center;" >--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="modal-footer" style="padding:13px;">--%>
                        <%--<button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal" id="cancel">关 闭</button>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div id="dg_modal_add_unit_info" class="modal fade" tabindex="-1" role="dialog"--%>
             <%--aria-labelledby="dg_modal_add_unit_info_title" aria-hidden="true">--%>
            <%--<div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 700px;">--%>
                <%--<div class="modal-content">--%>
                    <%--<div class="modal-header">--%>
                        <%--<h5 class="modal-title" id="dg_modal_add_unit_info_title">新增单位信息</h5>--%>
                        <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                            <%--<span aria-hidden="true">&times;</span>--%>
                        <%--</button>--%>
                    <%--</div>--%>
                    <%--<form id="dg_form_add_unit_info" method="post">--%>
                        <%--<input type="hidden" name="isOwner" id="add_isOwner" />--%>
                        <%--<div class="m-portlet__body"--%>
                             <%--style="padding:15px 20px 10px 15px;max-height:650px;overflow-y:auto;overflow-x: hidden">--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>单位名称：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9" >--%>
                                    <%--<input type="text" name="applicant"  class="form-control m-input"--%>
                                           <%--placeholder="单位名称">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>单位证件类型：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<select name="idtype" class="form-control m-input" id="idtype"--%>
                                            <%--placeholder="单位证件类型">--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>单位证件号码：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="idcard"  class="form-control m-input"--%>
                                           <%--placeholder="单位证件号码">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>单位证件地址：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="applicantDetailSite"  class="form-control m-input"--%>
                                           <%--placeholder="单位证件地址">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--<font style="color:red">*</font>法人名称：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="idrepresentative"--%>
                                           <%--class="form-control m-input" placeholder="法人名称">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--办公电话：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="idmobile"--%>
                                           <%--class="form-control m-input" placeholder="办公电话">--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-lg-3 col-form-label">--%>
                                    <%--法人证件号码：--%>
                                <%--</label>--%>
                                <%--<div class="col-lg-9">--%>
                                    <%--<input type="text" name="idno"  class="form-control m-input"--%>
                                           <%--placeholder="法人证件号码">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="modal-footer" style="padding: 10px;height: 60px;">--%>
                            <%--<button type="submit" class="btn btn-info">保存</button>--%>
                            <%--<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>--%>
                        <%--</div>--%>
                    <%--</form>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--&lt;%&ndash;  拒绝收件弹窗&ndash;%&gt;--%>
<%--<div class="modal fade" id="m_modal_refuseDialog" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel_2"--%>
     <%--aria-hidden="true">--%>
    <%--<div class="modal-dialog modal-dialog-centered" role="document">--%>
        <%--<div class="modal-content" >--%>
            <%--<div class="modal-header">--%>
                <%--<h5 class="modal-title" id="exampleModalLabel_2">不收件原因 </h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body miniScrollbar">--%>
                <%--<form id="refuseAcceptForm" method="post">--%>
                    <%--<input type="hidden" name="itemId" value="${itemId}"/>--%>
                    <%--<input type="hidden" name="itemVerId" value="${itemVerId}"/>--%>
                    <%--<input type="hidden" name="applyinstSource" value="win"/>--%>
                    <%--<input type="hidden" name="isSeriesApprove" id="isSeriesApprove" value="1"/>--%>
                    <%--<input type="hidden" name="projInfoId" id="projInfoId" value="${projInfoId}"/>--%>
                    <%--<input type="hidden" name="projName" id ="refuseProjName" value="${projName}"/>--%>
                    <%--<input type="hidden" name="linkmanInfoId"  />--%>
                    <%--<input type="hidden" name="iteminstState" value="3"/>--%>
                    <%--<input type="hidden" name="receiptType" value="3"/>--%>

                    <%--<input type="hidden" name="receiveCertNo" id ="receiveCertNo" value="${receiveCertNo}"/>--%>
                    <%--<input type="hidden" name="receiveUserName" id ="receiveUserName" value="${receiveUserName}"/>--%>
                    <%--<input type="hidden" name="receiveUserMobile" id ="receiveUserMobile" value="${receiveUserMobile}"/>--%>
                    <%--<input type="hidden" name="receiveMode" value="1"/>--%>
                    <%--<input type="hidden" name="serviceAddress" value="东莞市政务服务中心窗口"/>--%>
                    <%--<div class="row">--%>
                        <%--<label class="col-lg-4 col-form-label"> <font style="color:red">*</font>联系电话： </label>--%>
                        <%--<div class="col-md-8">&lt;%&ndash;col-lg-8&ndash;%&gt;--%>
                            <%--<input type="text" name="issueUserMobile" id="issueUserMobile" class="form-control m-input" style="width:300px;">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="row">&lt;%&ndash;form-group m-form__group &ndash;%&gt;--%>
                        <%--<label class="col-lg-4 col-form-label"> <font style="color:red">*</font>不收件原因： </label>--%>
                        <%--<div class="col-md-8">--%>
                                <%--<textarea class="form-control m-input" name="receiveMemo" id="receiveMemo"--%>
                                          <%--style="height: 300px;width:300px;"></textarea>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="row">--%>
                        <%--<label class="col-lg-4 col-form-label">选择材料： </label>--%>
                        <%--<div class="col-md-8" style="margin-top: 5px" >--%>
                            <%--<select id="selectTable" name="selectTable" class="form-control m-input" onchange="setItemInfo(this);"  >--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="modal-footer" style="padding-right:19px;height: 45px;">--%>
                        <%--<button type="button" class="btn btn-secondary cancel btn-sm" data-dismiss="modal">--%>
                            <%--取消--%>
                        <%--</button>--%>
                        <%--<button type="submit" class="btn btn-primary confirm btn-sm">--%>
                            <%--保存--%>
                        <%--</button>--%>
                    <%--</div>--%>
                <%--</form>--%>

            <%--</div>--%>

        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<!--  收件意见 弹窗 -->--%>
<%--<div class="modal fade" id="m_modal_viewDialog" tabindex="-1" role="dialog" aria-labelledby="m_modal_saveAndSend_title"--%>
     <%--aria-hidden="true" >--%>
    <%--<div class="modal-dialog" role="document" style="width: 800px;margin-top: 90px;margin-left: 350px;">&lt;%&ndash;  &ndash;%&gt;--%>
        <%--<div class="modal-content" style=" width: 800px;" >--%>
            <%--<div class="modal-header" style="padding:10px 10px 0px 10px;height: 40px; background-color: #fff">--%>
                <%--<h5 class="modal-title" id="m_modal_saveAndSend_title">--%>
                    <%--收件意见对话框--%>
                <%--</h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body " style="padding:20px;width:800px">&lt;%&ndash; miniScrollbar &ndash;%&gt;--%>
                <%--<div class="m-portlet m-portlet--bordered m-portlet--unair" style="margin-bottom:1.2rem;">--%>
                    <%--<div class="m-portlet__head" style="height:3.0rem;background-color: #f4f4f4;border-bottom:none;padding: 0 1rem;">--%>
                        <%--<div class="m-portlet__head-caption">--%>
                            <%--<div class="m-portlet__head-title">--%>
                                <%--<h3 class="m-portlet__head-text" style="color:#666;font-weight:400">--%>
                                    <%--收件意见--%>
                                <%--</h3>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="m-portlet__body" style="padding: 0.8rem 0.8rem;">--%>
                        <%--<div class="form-group row" style="margin-buttom:-0.8rem">--%>
                            <%--<label class="col-8" style="line-height:1.7em;">--%>
                                <%--<select class="form-control" id="userOpinionSelect" onchange="addCustomComment()">--%>
                                <%--</select>--%>
                            <%--</label>--%>
                            <%--<div class="col-4">--%>
                                <%--<button class="btn btn-secondary" onclick="saveUserOpinion('message_send')">设为常用意见</button>--%>
                                <%--<button class="btn btn-secondary" onclick="showUserOpinions()">管理</button>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="row">--%>
                            <%--<div class="col-md-12">--%>
                                <%--<textarea class="form-control m-input" name="message" id="message_send" rows="4"></textarea>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="modal-footer" style="padding-right:19px;height: 45px;">--%>
                <%--<button type="button" class="btn btn-sm btn-secondary cancel" data-dismiss="modal" >&lt;%&ndash;onclick="hideCommentDialog()"&ndash;%&gt;--%>
                    <%--取消--%>
                <%--</button>--%>
                <%--<button id="startFlow" type="button" class="btn btn-sm btn-primary confirm" >--%>
                    <%--发送--%>
                <%--</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<!--  查看常用意见弹窗 -->--%>
<%--<div class="modal fade" id="m_modal_userOpinions" tabindex="-1" role="dialog" aria-labelledby="userOpinions_title"--%>
     <%--aria-hidden="true">--%>
    <%--<div class="modal-dialog modal-dialog-centered" role="document">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header" style="padding:10px 10px 0px 10px;height: 40px;">--%>
                <%--<h5 class="modal-title" id="userOpinions_title">--%>
                    <%--常用意见管理--%>
                <%--</h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body miniScrollbar">--%>
                <%--<div class="row">--%>
                    <%--<div class="col-md-12">--%>
                        <%--<div class="m_datatable" id="userOpinions"></div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-secondary cancel" data-dismiss="modal">--%>
                    <%--关闭--%>
                <%--</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<!--  查看常用意见编辑弹窗 -->--%>
<%--<div class="modal fade" id="m_modal_edit_userOpinions" tabindex="-1" role="dialog" aria-labelledby="edit_userOpinions_title"--%>
     <%--aria-hidden="true">--%>
    <%--<div class="modal-dialog modal-dialog-centered" role="document">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header" style="padding:10px 10px 0px 10px;height: 40px;">--%>
                <%--<h5 class="modal-title" id="edit_userOpinions_title">--%>
                    <%--编辑意见--%>
                <%--</h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body miniScrollbar">--%>
                <%--<form id="userOpinionForm">--%>
                    <%--<input  type="hidden" name="opinionId"/>--%>
                    <%--<div class="form-group m-form__group row">--%>
                        <%--<label class="col-2 col-form-label">意见</label>--%>
                        <%--<div class="col-10">--%>
                            <%--<textarea class="form-control" type="text" name="opinionContent" rows="3"></textarea>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group m-form__group row">--%>
                        <%--<label class="col-2 col-form-label">排序号</label>--%>
                        <%--<div class="col-10">--%>
                            <%--<input class="form-control m-input" type="text" name="sortNo"/>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</form>--%>
            <%--</div>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-secondary cancel" data-dismiss="modal">--%>
                    <%--关闭--%>
                <%--</button>--%>
                <%--<button type="button" class="btn btn-primary" onclick="updateUserOpinion()">--%>
                    <%--保存--%>
                <%--</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/vendors/base/vendors.bundle.js" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>--%>

<%--<script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/js/show_image.js?_=<%=new Date().getTime()%>" type="text/javascript"></script><!-- 图片预览-->--%>

<%--&lt;%&ndash;引入高拍仪拍照&ndash;%&gt;--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/qwebchannel.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/liangtian_gaopaoyi.js?<%=isDebugMode%>"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/fangzheng_gaopaiyi.js?<%=isDebugMode%>"></script>--%>

<%--&lt;%&ndash;bootstrap-table.js 有改动，为了支持treegrid分页。bootstrap-table.min.js 没有更新，不支持treegrid分页&ndash;%&gt;--%>
<%--<script type="text/javascript"--%>
        <%--src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table.js"></script>--%>
<%--<script type="text/javascript"--%>
        <%--src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-page.js"></script>--%>
<%--<script type="text/javascript"--%>
        <%--src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>--%>

<%--<script type="text/javascript"--%>
        <%--src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/process/area.js?_=<%=new Date().getTime()%>"></script>--%>

<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js"></script>--%>
<%--<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>--%>

<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/view/branch_org_tree.js?<%=isDebugMode%>"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/process/dg_approve.js?<%=isDebugMode%>"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/matinst/common_matinst.js?_=<%=new Date().getTime()%>"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/process/common_approve.js?_=<%=new Date().getTime()%>"></script>--%>


<%--&lt;%&ndash;引入文件拖拽js&ndash;%&gt;--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/aplanmis/matinst/dropFile.js?<%=isDebugMode%>" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/aplanmis/matinst/manageAndViewMatinst.js?<%=isDebugMode%>" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/aplanmis/matinst/view_ItemMatInst_file.js?<%=isDebugMode%>" type="text/javascript"></script>--%>
<%--<script src="${pageContext.request.contextPath}/ui-static/aplanmis/util/defaultBootstrap.js?<%=isDebugMode%>" type="text/javascript"></script>--%>

<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/view/proj_info_tree_edit.js?<%=isDebugMode%>"></script>--%>

<%--<script type="text/javascript">--%>
    <%--var attAccessUrl = "${attAccessUrl}";--%>
    <%--var linkmanlist=[];  //弃用--%>
    <%--var linkmanInfoValidator = null;--%>
    <%--var _itemId;//弃用--%>
    <%--var _itemVerId;--%>
    <%--var currentProjectId = "";--%>
    <%--var projInfo;--%>
    <%--var linkmanName;--%>
    <%--var linkmanInfoId;--%>
    <%--var AeaHiReceive;--%>
    <%--var realMatListTemp=[];--%>
    <%--var d_currentLinkmanInfoId;--%>
    <%--var d_currentLinkmanName;--%>
    <%--var d_linkmanlist=[];//经办联系人列表  //弃用--%>
    <%--var d_selUnitInfo;//当前选择的经办单位--%>
    <%--$(function () {--%>
        <%--$('.btn-file').click(function(){--%>
            <%--$('#inputFile').trigger('click');--%>
        <%--});--%>
        <%--//_itemId = $("#hidItemId").val();--%>
        <%--_itemVerId=$("#hidItemId").val();--%>
        <%--$.ajax({--%>
            <%--//url:ctx+"/aea/business/getItemInfo.do?itemId="+_itemId,--%>
            <%--url:ctx+"/aea/business/getItemInfoByItemVerId.do?itemVerId="+_itemVerId,--%>
            <%--type:'get',--%>
            <%--success:function(result){--%>
                <%--if(result!=null){--%>
                    <%--$("#mainItemName").val(result.itemName);--%>
                    <%--// $("#serviceObject").val(result.xkdx);--%>
                    <%--$("#acceptInsition").val(result.orgName);--%>
                    <%--if(result.xkdx == undefined || result.xkdx == "")--%>
                        <%--result.xkdx = "5";--%>
                    <%--$.ajax({--%>
                        <%--type: 'post',--%>
                        <%--url: ctx + '/itemGuide/getItemPropertiesXKDX.do',--%>
                        <%--data:{dicName:"ITEM_FWJGXZ",code:result.xkdx},--%>
                        <%--success: function (data) {--%>
                            <%--if (data != '' || data != null) {--%>
                                <%--$("#serviceObject").val(data);--%>
                            <%--}--%>
                        <%--}--%>
                    <%--})--%>
                <%--}--%>
            <%--}--%>
        <%--});--%>
        <%--var localCode='${localCode}';--%>
        <%--if(localCode!=null && localCode!=''){--%>
            <%--$("#dg_localCode").val(localCode);--%>
            <%--queryProjInfo();--%>
        <%--}--%>
        <%--$("#dg_modal_print_interface").modal({backdrop:'static',show:false});--%>
        <%--$('#dg_modal_print_interface').on('hidden.bs.modal', function (e) {--%>
            <%--parent.vm.removeTab(parent.vm.activeTabIframe);--%>
            <%--//parent.vm.removeTab('335cea09-0af0-45b8-abfc-6d8a868c2ed3');--%>
        <%--});--%>

        <%--$('#dg_localCode').focus();--%>
    <%--});--%>

<%--</script>--%>
<%--<!--加签二维码窗口-->--%>
<%--<jsp:include page="../view/sign_qrCode_win.jsp"></jsp:include>--%>
<%--&lt;%&ndash;引入窗口&ndash;%&gt;--%>
<%--<jsp:include page="../../common/thirdparty/gaopaiyi/liangtian_gaopaoyi.jsp"></jsp:include>--%>
<%--<jsp:include page="../../common/thirdparty/gaopaiyi/fangzheng_gaopaoyi.jsp"></jsp:include>--%>
<%--&lt;%&ndash;项目树窗口&ndash;%&gt;--%>
<%--<jsp:include page="../view/proj_info_tree_edit.jsp"></jsp:include>--%>
<%--&lt;%&ndash;新建项目窗口&ndash;%&gt;--%>
<%--&lt;%&ndash;<jsp:include page="addProjDialog.jsp"></jsp:include>&ndash;%&gt;--%>
<%--&lt;%&ndash;选择分局窗口&ndash;%&gt;--%>
<%--<jsp:include page="../view/branch_org_tree.jsp"></jsp:include>--%>
<%--<script>--%>
    <%--//  begin 滚动时，固定左侧的menu 并导航到相对位置--%>
    <%--var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码--%>
    <%--//3、 滚动条滚动 --  找到当前楼层的索引    控制楼层号  固定操作按钮--%>
    <%--$(window).scroll(function () {--%>
        <%--if (flag) {--%>
            <%--var items = $(".div-step");--%>
            <%--var menu = $("#menu");--%>
            <%--var top = $(document).scrollTop();--%>
            <%--var currentId = ""; //滚动条现在所在位置的item id--%>
            <%--var cl = '';--%>
            <%--var h = $(window).height()/2;--%>
            <%--items.each(function () {--%>
                <%--var m = $(this);--%>
                <%--//m.offset().top代表每一个item的顶部位置--%>
                <%--if (top > m.offset().top-h/2) {--%>
                    <%--currentId = "#" + m.attr("id");--%>
                    <%--cl = m.attr("id");--%>
                <%--} else {--%>
                    <%--return false;--%>
                <%--}--%>
            <%--});--%>
            <%--var currentLink = menu.find(".active");--%>
            <%--if (currentId && currentLink.attr("href") != currentId) {--%>
                <%--currentLink.removeClass("active");--%>
                <%--menu.find("[data-name=" + cl + "]").addClass("active");--%>
                <%--$(currentId).addClass("active").siblings().removeClass("active");--%>
            <%--}--%>
        <%--}--%>
    <%--});--%>
    <%--// end 滚动时，固定左侧的menu 并导航到相对位置--%>
    <%--// 点击左侧滚动导航条 start--%>
    <%--$('.ciclebox').click(function () {--%>
        <%--var ele = $(this).children('a').attr('href');--%>
        <%--$(ele).addClass('active').siblings('.div_step').removeClass('active');--%>
        <%--$(this).addClass('active').siblings('.ciclebox').removeClass('active');--%>
    <%--});--%>
    <%--// 点击左侧滚动导航条 end--%>

    <%--// 隐藏展示智能分拣区--%>
    <%--$('.show-hide-fileDiv').click(function(e){--%>
        <%--e.stopPropagation();--%>
        <%--var txt = $(this).text();--%>
        <%--if(txt === '隐藏智能分拣区'){--%>
            <%--$(this).text('展示智能分拣区');--%>
            <%--$('.fileDiv').addClass('hide');--%>
        <%--}else {--%>
            <%--$(this).text('隐藏智能分拣区');--%>
            <%--$('.fileDiv').removeClass('hide');--%>
        <%--}--%>

    <%--})--%>
<%--</script>--%>
<%--</body>--%>
<%--</html>--%>