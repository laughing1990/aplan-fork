<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>中介事项配置信息</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="text/javascript">
        var currItemId = '${itemId}';
        var currItemVerId = '${itemVerId}';
        var currItemName = '${itemName}';
    </script>
    <!--bootstrap-table-->
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>

    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/item/item_cfg_index.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/item/cfg_service.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/item/cfg_qual.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/item/cfg_according.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/item/cfg_item.js?<%=isDebugMode%>" type="text/javascript"></script>


    <style type="text/css">
        .mini-title {
            border-left: 6px solid #4879e4;
            color: #444;
            padding-left: 10px;
            margin: 10px 0;
            font-weight: 400;
        }
        .container {
            margin: 0 auto;
            padding-bottom: 115px;
            max-width: 1200px;
        }
        .viewClauseContent{
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .viewClauseContent:hover {
            display: block;
        }
    </style>
</head>
<body>
<div style="display: block;text-align: center;">
    <h1 id="itemTitle" style="display: inline-block;text-align: left;margin: 40px 0;font-weight: 700;"></h1>
</div>
<div class="container">
    <h5 class="mini-title">服务信息</h5>
    <div>
        <form id="ae_item_explain_form" method="post">
            <div class="modal-body" style="padding: 10px;">

                <input id="itemExplainId" type="hidden" name="itemExplainId" value=""/>
                <input id="itemExplainItemVerId" type="hidden" name="itemVerId" value=""/>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>服务内容：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="serviceContent" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>服务结果：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="serviceResult" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">资金来源：</label>
                    <div class="col-lg-10">
                        <label class="m-checkbox m-checkbox--solid m-checkbox--brand">
                            <input id="isFinancialFund" type="checkbox" name="isFinancialFund" value="0" onclick="checkBoxClickEvent('isFinancialFund');">财政资金
                            <span></span>
                        </label>&nbsp;
                        <label class="m-checkbox m-checkbox--solid m-checkbox--brand">
                            <input id="isSocialFund" type="checkbox" name="isSocialFund" value="0" onclick="checkBoxClickEvent('isSocialFund');">社会资金
                            <span></span>
                        </label>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">服务时限要求：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="serviceTimeLimit" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">时限说明：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="timeLimitExplain" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">价格管理方式：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="priceManagement" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">设立层级：</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="slcj" value=""  placeholder=""/>
                    </div>
                </div>
                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">备注：</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="memo" rows="3"></textarea>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="submit" class="btn btn-info">保存</button>
            </div>
        </form>
    </div>
    <h5 class="mini-title">所属服务</h5>
    <div>
        &nbsp; &nbsp;<button type="button" class="btn btn-info" onclick="openCfgServiceWin();">添加</button>
        <table id="service_list_tb"
               data-toggle="table"
               data-method="post"
               data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
               data-click-to-select=true
               data-single-select="true"
               data-pagination-detail-h-align="left"
               data-pagination-show-page-go="false"
               data-page-size="10"
               data-page-list="[10,20,30,50,100]",
               data-pagination=false
               data-side-pagination="server"
               data-pagination-detail-h-align="left"
               data-query-params="serviceQueryParam"
               data-response-handler="serviceResponseData"
               data-url="${pageContext.request.contextPath}/supermarket/service/listItemServiceNoPage.do">
            <thead>
            <tr>
                <%--<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>--%>
                <th data-field="_No" data-align="center" data-colspan="1" data-width="10" data-formatter="numberFormatter">序号</th>
                <th data-field="serviceName" data-align="left" data-colspan="1" data-width="200">服务名称</th>
                <th data-field="serviceCode" data-align="left" data-colspan="1" data-width="200">服务编号</th>
                <%--<th data-field="" data-formatter="serviceOperatorFormatter" data-align="center" data-colspan="1" data-width="130">操作</th>--%>
            </tr>
            </thead>
        </table>
        <%--中介服务窗口--%>
        <%@include file="cfg_service.jsp"%>
    </div>
    <h5 class="mini-title">中介机构要求</h5>
    <div>&nbsp;&nbsp;
        <form id="ae_unit_require_form" method="post">
            <div class="modal-body" style="padding: 10px;">

                <input id="unitRequireId" type="hidden" name="unitRequireId" value=""/>
                <input id="qualRequire" type="hidden" name="qualRequire" value=""/>

                <div class="form-group m-form__group row">
                    <div class="col-lg-2"></div>
                    <div class="col-lg-10">
                        <label class="m-checkbox m-checkbox--solid m-checkbox--brand">
                            <input id="isQualRequire" type="checkbox" name="isQualRequire" value="0" onclick="checkBoxClickEvent('isQualRequire',showQual);">资质（资格）要求
                            <span></span>
                        </label>&nbsp;&nbsp;
                        <label class="m-checkbox m-checkbox--solid m-checkbox--brand">
                            <input id="isRegisterRequire" type="checkbox" name="isRegisterRequire" value="0" onclick="checkBoxClickEvent('isRegisterRequire',showRegister);">执业/职业人员要求
                            <span></span>
                        </label>&nbsp;&nbsp;
                        <label class="m-checkbox m-checkbox--solid m-checkbox--brand">
                            <input id="isRecordRequire" type="checkbox" name="isRecordRequire" value="0" onclick="checkBoxClickEvent('isRecordRequire',showRecord);">备案要求
                            <span></span>
                        </label>
                    </div>
                </div>
                <div id="showQual" style="display: none">
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">
                            <%--<button type="button" class="btn btn-info" onclick="cfgQualRequire();">添加</button>--%>
                            <font color="red">*</font>资质要求：
                        </label>
                        <div class="col-lg-10">
                            <%--<input  id="qualRequireName" type="text" class="form-control m-input" name="qualRequireName" value=""/>--%>
                                <textarea id="qualRequireName" class="form-control" name="qualRequireName" rows="3" readonly="readonly"></textarea>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>资质要求说明：</label>
                        <div class="col-lg-10">
                            <%--<input type="text" class="form-control m-input" name="qualRequireExplain" value=""  placeholder=""/>--%>
                            <textarea id="qualRequireExplain" class="form-control" name="qualRequireExplain" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>资质备案要求：</label>
                        <div class="col-lg-10">
                            <%--<input type="text" class="form-control m-input" name="qualRecordRequire" value=""  placeholder=""/>--%>
                            <textarea id="qualRecordRequire" class="form-control" name="qualRecordRequire" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div id="showRegister" style="display: none">
                    <div  class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>执业/职业人员要求：</label>
                        <div class="col-lg-10">
                            <%--<input type="text" class="form-control m-input" name="registerRequire" value=""  placeholder=""/>--%>
                            <textarea  id="registerRequire" class="form-control" name="registerRequire" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div id="showRecord" style="display: none">
                    <div  class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>备案要求说明：</label>
                        <div class="col-lg-10">
                            <%--<input type="text" class="form-control m-input" name="recordRequireExplain" value=""  placeholder=""/>--%>
                            <textarea id="recordRequireExplain"  class="form-control" name="recordRequireExplain" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">其他要求说明：</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="otherRequireExplain" rows="3"></textarea>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="saveRequireBtn" type="submit" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="loadQualRequireForm();">重置</button>
            </div>
        </form>
        <%--资质要求窗口--%>
        <%@include file="cfg_qual.jsp"%>
    </div>
    <h5 class="mini-title">设立依据</h5>
    <div>
        &nbsp; &nbsp;<button type="button" class="btn btn-info" onclick="openCfgAccordingWin();">添加</button>
        <table id="according_list_tb"
               data-toggle="table"
               data-method="post"
               data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
               data-click-to-select=true
               data-single-select="true"
               data-pagination-detail-h-align="left"
               data-pagination-show-page-go="false"
               data-page-size="10"
               data-page-list="[10,20,30,50,100]",
               data-pagination=false
               data-side-pagination="server"
               data-pagination-detail-h-align="left"
               data-query-params="accordingQueryParam"
               data-response-handler="accordingResponseData"
               data-url="${pageContext.request.contextPath}/aea/service/legal/clause/listItemServiceLegalNoPage.do">
            <thead>
            <tr>
                <%--<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>--%>
                <th data-field="_No" data-align="center" data-colspan="1" data-width="10" data-formatter="numberFormatter">序号</th>
                <th data-field="legalName" data-align="left" data-colspan="1" data-width="200">法律法规名称</th>
                <th data-field="basicNo" data-align="left" data-colspan="1" data-width="200">依据文号</th>
                <th data-field="issueOrg" data-align="left" data-colspan="1" data-width="200">颁发机构</th>
                <th data-field="clauseTitle" data-align="left" data-colspan="1" data-width="100">条款号</th>
                <th data-field="clauseContent" data-align="left" data-colspan="1" data-width="300" data-formatter="clauseContentFormatter">条款具体内容</th>
            </tr>
            </thead>
        </table>
        <%--设立依据窗口--%>
        <%@include file="cfg_according.jsp"%>
    </div>
    <h5 class="mini-title">涉及行政事项</h5>
    <div>
        &nbsp; &nbsp;<button type="button" class="btn btn-info" onclick="openCfgItemWin();">添加</button>
        <table id="item_list_tb"
               data-toggle="table"
               data-method="post"
               data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
               data-click-to-select=true
               data-single-select="true"
               data-pagination-detail-h-align="left"
               data-pagination-show-page-go="false"
               data-page-size="10"
               data-page-list="[10,20,30,50,100]",
               data-pagination=false
               data-side-pagination="server"
               data-pagination-detail-h-align="left"
               data-query-params="itemQueryParam"
               data-response-handler="itemResponseData"
               data-url="${pageContext.request.contextPath}/aea/item/basic/listAeaItemBasicForSupermaketNoPage.do">
            <thead>
            <tr>
                <%--<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>--%>
                <th data-field="_No" data-align="center" data-colspan="1" data-width="10" data-formatter="numberFormatter">序号</th>
                <th data-field="itemName" data-align="left" data-colspan="1" data-width="200">事项名称</th>
                <th data-field="itemCode" data-align="left" data-colspan="1" data-width="200">事项编号</th>
                <th data-field="orgName" data-align="left" data-colspan="1" data-width="200">部门名称</th>
            </tr>
            </thead>
        </table>
        <%--行政事项窗口--%>
        <%@include file="cfg_item.jsp"%>
    </div>
</div>
</div>
</body>
</html>