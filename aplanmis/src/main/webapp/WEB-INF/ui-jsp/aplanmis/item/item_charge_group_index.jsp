<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>
        许可收费项目列表
    </title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_index.js?<%=isDebugMode%>"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_checked_legal_clause.js" type="text/javascript"></script>
</head>

<body style="background-color:white;overflow: auto">
<div>
    <div class="m-form m-form--label-align-right m--margin-bottom-5">
        <div class="row" style="margin: 10px;">
            <div class="col-md-6"style="text-align: left;">
                <button type="button" class="btn btn-info" onclick="addItemCharge();">新增</button>
            </div>
            <div class="col-md-6" style="padding: 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-3"></div>
                    <div class="col-6">
                        <div class="m-input-icon m-input-icon--left">
                            <input id="itemChargeGroupKeyword" type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                        <span><i class="la la-search"></i></span>
                                                                        </span>
                        </div>
                    </div>
                    <div class="col-3">
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
                <%--<th data-field="sfjmqx"            data-align="center"  data-colspan="1" data-width="100">收费减免情形</th>--%>
                <%--<th data-field="sfxmyjIds"         data-align="center"  data-colspan="1" data-width="80">收费项目依据</th>--%>
                <%--<th data-field=""       data-align="center"  data-formatter="formatDatetime"  data-colspan="1" data-width="100">创建时间</th>--%>
                <th data-field=""                   data-align="center"  data-formatter="operationFormatter"  data-colspan="1" data-width="150">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<%--新增收费项目 start--%>
<div id="edit_item_charge_group_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="edit_item_charge_group_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 800px">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_item_charge_group_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="edit_item_charge_group_from" method="post" style="padding-left:25px;padding-right:50px">
                    <!---------------- 隐藏域区域 开始 ----------------->
                    <input type="hidden" id="itemChargeId" name="id"/>
                    <!---------------- 隐藏域区域 结束 ----------------->

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>项目名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="xmmc" value=""/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>收费标准:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="sfbz" value=""/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>收费主体:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="sfzt" value=""/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>收费性质:</label>
                        <div class="col-lg-10">
                            <select name="sfxz" id="sfxz" value="" class="form-control m-input" placeholder="">
                            </select>
                        </div>
                    </div>
                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">收费减免情形:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="sfjmqx" rows="3"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button type="submit" class="btn btn-info">保存</button>
                            <button id="closeAddThemeBtn" type="button" data-dismiss="modal" class="btn btn-secondary">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%--新增收费项目 end--%>

<!-- 查看已设置的法律依据 -->
<%@include file="include/item_charge_group_checked_legal_clause.jsp"%>
</body>
</html>