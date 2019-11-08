<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;padding: 0;border: 0;overflow: auto;">
    <head>
        <title>收费项目法律依据条款</title>
        <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
        <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
        <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
        <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
        <script type="text/javascript">
            var itemChargeGroup_Id = '${itemChargeGroupId}'
        </script>
        <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_legal_clause_index.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_charge_group_checked_legal_clause.js" type="text/javascript"></script>
    </head>
    <body>
        <div style="width: 100%;height: 95%; padding: 10px 10px 5px 10px;">
            <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                <div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
                    <div class="m-portlet__body" style="padding: 10px 0px;height: 99%;">
                        <div class="row" style="margin: 0px;">
                            <div class="col-xl-2 m--align-left m--padding-left-20">
                                <a href="#" class="btn btn-secondary m-btn m-btn--icon" onclick="backToChargeGroup();">
                                    <span><i class="la la-angle-left"></i><span>返回</span></span>
                                </a>
                            </div>
                            <div class="col-xl-8 m--align-center m--align-center">
                                <span style="font-size: 18px;font-weight: bold;">设置法律依据</span>
                            </div>
                            <div class="col-xl-2 m--align-right m--padding-right-20">
                                <a href="#" class="btn btn-secondary m-btn m-btn--icon" onclick="">
                                    <span><i class="flaticon-info"></i><span>帮助</span></span>
                                </a>
                            </div>
                        </div>
                        <hr width="100%" color="#C1C1C1" size="1"/><!-- color是颜色，size是大小 -->
                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                            <div class="row" style="margin: 10px;">
                                <div class="col-md-6"style="text-align: left;">
                                    <button type="button" class="btn btn-info" onclick="saveChargeGroupLegalClause(false)">设置</button>&nbsp;
                                    <button type="button" class="btn btn-info" onclick="saveChargeGroupLegalClause(true)">取消设置</button>&nbsp;
                                    <button type="button" class="btn btn-info" onclick="viewChargeGroupCheckedLegalClause()">查看</button>&nbsp;
                                </div>
                                <div class="col-md-6" style="padding: 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-2"></div>
                                        <div class="col-6">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input id="chargeGroupLegalClauseKeyword" type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                        <span><i class="la la-search"></i></span>
                                                                        </span>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <button type="button" class="btn  btn-info" onclick="searchChargeGroupLegalClauseCondition();">查询</button>&nbsp;
                                            <button type="button" class="btn  btn-danger" onclick="clearSearchChargeGroupLegalClause();">清空</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="base" style="padding: 10px">
                            <table  id="charge_group_legal_clause_tb"
                                    data-toggle="table"
                                    data-sort-name="createTime"
                                    data-method="post"
                                    data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                    data-sort-order="DESC"
                                    data-click-to-select=true
                                    data-pagination-detail-h-align="left"
                                    data-pagination-show-page-go="true"
                                    data-pagination="true"
                                    data-page-size="10"
                                    data-page-list="[10,20,30,50,100]"
                                    data-side-pagination="server"
                                    data-query-params="getChargeGroupLegalClauseQueryParam"
                                    data-response-handler="chargeGroupLegalClauseResponseData"
                                    data-url="${pageContext.request.contextPath}/aea/service/legal/clause/listAeaItemServiceLegalClause.do">
                                <thead>
                                <tr>
                                    <th data-field=""                   data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                                    <th data-field="sortNo"              data-align="center"  data-colspan="1" data-width="30">序号</th>
                                    <th data-field="legalName"              data-align="center"  data-colspan="1" data-width="100">依据名称</th>
                                    <th data-field="basicNo"              data-align="center"  data-colspan="1" data-width="80">依据文号</th>
                                    <th data-field="issueOrg"              data-align="center"  data-colspan="1" data-width="100">颁布机关</th>
                                    <th data-field="exeDate"              data-align="center"  data-colspan="1" data-width="80">实施日期</th>
                                    <th data-field="clauseTitle"              data-align="center"  data-colspan="1" data-width="80">条款号</th>
                                    <th data-field="clauseContent"              data-align="center"  data-colspan="1" data-width="400">条款内容</th>
                                    <th data-field=""     data-align="center"  data-formatter="chargeGroupLegalClauseTbOperationFormatter"  data-colspan="1" data-width="100">操作</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 查看已设置的法律依据 -->
        <%@include file="include/item_charge_group_checked_legal_clause.jsp"%>

    </body>
</html>
