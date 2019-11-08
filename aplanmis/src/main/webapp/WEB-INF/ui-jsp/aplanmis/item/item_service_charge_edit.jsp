<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>
        事项许可收费
    </title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_charge_edit.js?<%=isDebugMode%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_index.js?<%=isDebugMode%>"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_checked_legal_clause.js" type="text/javascript"></script>
    <script type="text/javascript">
        var  itemServiceChargeId = '${itemServiceChargeId}';
        var  itemId = '${itemId}';
    </script>
    <style type="text/css">
        .col-lg-8{
            margin-top: 5px;
        }
    </style>

</head>

<body style="background-color:white;">
<div class="m-portlet" style="height:auto;">
    <div class="m-portlet__body">
        <%--按钮--%>
        <div class="row" style="padding-bottom:10px;padding-left:10px">
            <div class="col-xl-12">
                <a href="javascript:void(0)" onclick="saveTemp();" class="btn btn-info m-btn m-btn--icon">
                                                        <span>
                                                            <span>暂存</span>
                                                        </span>
                </a>&nbsp;
                <a href="javascript:void(0)" onclick="saveForm();" class="btn btn-info m-btn m-btn--icon">
                                                        <span>
                                                            <span>保存</span>
                                                        </span>
                </a>&nbsp;
                <a href="javascript:void(0)" onclick="commitApprove();" class="btn btn-info m-btn m-btn--icon">
                                                        <span>
                                                            <span>提交审查</span>
                                                        </span>
                </a>&nbsp;
                <a href="javascript:void(0)" onclick="closePage();" class="btn btn-info m-btn m-btn--icon">
                                                        <span>
                                                            <span>关闭</span>
                                                        </span>
                </a>&nbsp;
            </div>
        </div>
        <%--许可收费表单--%>
        <form class="m-form m-form--fit m-form--label-align-right" id="item_service_charge_edit_from">
            <!---------------- 隐藏域区域 开始 ----------------->
            <input type="hidden" id="id" name="id"/>
            <input type="hidden" id="itemId" name="itemId"/>
            <input type="hidden" id="chargeGroupIds" name="chargeGroupIds"/>
            <!---------------- 隐藏域区域 结束 ----------------->
            <div style="text-align: center;">
                <h3 class="m-portlet__head-text" id="item_charge_title" style="font-weight:bold;">
                    许可收费
                </h3>
            </div>
            <div class="m-portlet__body">

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>有无收费：
                    </label>
                    <div class="col-lg-8">
                        <input type="radio" name="ywsf" class="radio" value="1" style="width:16px;height:16px" checked>收费
                        <input type="radio" name="ywsf" class="radio" value="0" style="width:16px;height:16px">不收费
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>收费环节：
                    </label>
                    <div class="col-lg-8" style="display: inline-block">
                        <input type="text" name="jfhj" class="form-control m-input" placeholder="">

                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>收费地点：
                    </label>
                    <div class="col-lg-8">
                        <textarea class="form-control m-input" name="jfdd" rows="3"></textarea>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>收费时间：
                    </label>
                    <div class="col-lg-8">
                        <textarea class="form-control m-input" name="jfsj" rows="3"></textarea>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>收费方式：
                    </label>
                    <div class="col-lg-8">
                        <div class="row" style="display:inline-block;">
                            <input type="checkbox" name="jffs" value="1" style="width:16px;height:16px"/>现金&nbsp;
                            <input type="checkbox" name="jffs" value="2" style="width:16px;height:16px"/>银行转账&nbsp;
                            <input type="checkbox" name="jffs" value="3" style="width:16px;height:16px"/>POS机&nbsp;
                            <input type="checkbox" name="jffs" value="4" style="width:16px;height:16px"/>网上支付&nbsp;
                            <input type="checkbox" name="jffs" value="5" style="width:16px;height:16px"/>其他&nbsp;&nbsp;
                            <input type="text" id="ortherJffs" name="qtjffs" class="m-input" placeholder="">
                            <span style="color: grey">(选其他请在这里注明)</span>
                            <div id="jffs-error" style="color: red;display: none">该输入项为必输项！</div>
                        </div>
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label">
                        <font style="color:red">*</font>收费项目：
                    </label>
                    <div class="col-lg-8">
                        <a href="javascript:void(0)" onclick="addChargeGroup();" class="btn btn-info m-btn m-btn--icon">
                            <span><span>添加项目</span></span>
                        </a>&nbsp;
                        <span id="chargeGroupName"></span>
                    </div>
                </div>
            </div>
            <button type="submit" id="item_service_charge_edit_from_submit" style="display:none;"></button>
        </form>
    </div>
</div>

<%--收费项目列表 start--%>
<div id="item_service_charge_group_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="item_service_charge_group_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1200px">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="item_service_charge_group_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 10px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="comfirmChargeGroup()">选择</button>&nbsp;
                            <button type="button" class="btn btn-info" onclick="refreshItemChargeGroup();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="itemChargeGroupKeyword" type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                        <span><i class="la la-search"></i></span>
                                                                        </span>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn  btn-info" onclick="searchItemChargeGroupCondition();">查询</button>&nbsp;
                                    <button type="button" class="btn  btn-danger" onclick="clearSearchItemChargeGroup();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="padding: 10px">
                    <table  id="item_charge_group_tb"
                            data-toggle="table"
                    <%--data-sort-name="xmmc"--%>
                            data-method="post"
                            data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                    <%--data-sort-order="DESC"--%>
                            data-click-to-select=true
                            data-pagination-detail-h-align="left"
                            data-pagination-show-page-go="true"
                            data-pagination="true"
                            data-page-list="[10,20,30,50,100]"
                            data-side-pagination="server"
                            data-query-params="dealQueryParams"
                            data-response-handler="responseData"
                            data-url="${pageContext.request.contextPath}/aea/item/charge/group/listAeaItemChargeGroup.do">
                        <thead>
                        <tr>
                            <th data-field=""                   data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                            <th data-field="xmmc"              data-align="center"  data-colspan="1" data-width="100">项目名称</th>
                            <th data-field="sfbz"              data-align="center"  data-colspan="1" data-width="80">收费标准</th>
                            <th data-field="sfzt"              data-align="center"  data-colspan="1" data-width="100">收费主体</th>
                            <th data-field="sfxz"              data-align="center"  data-colspan="1" data-width="100">收费性质</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<%--收费项目列表 end--%>

<%--收费项目依据 start--%>
<%@include file="include/item_charge_group_checked_legal_clause.jsp"%>
<%--收费项目依据 end--%>

</body>
</html>