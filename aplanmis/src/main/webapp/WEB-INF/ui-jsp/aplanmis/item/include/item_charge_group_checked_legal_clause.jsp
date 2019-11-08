<%@ page contentType="text/html;charset=UTF-8" %>

<%--收费项目已设置的法律依据列表 start--%>
<div id="charge_group_checked_legal_clause_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="charge_group_checked_legal_clause_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1600px">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="charge_group_checked_legal_clause_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 10px;">
                        <div class="col-md-6"style="text-align: left;">

                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="chargeGroupCheckedLegalClauseKeyword" type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                        <span><i class="la la-search"></i></span>
                                                                        </span>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn  btn-info" onclick="searchChargeGroupCheckedLegalClauseCondition();">查询</button>&nbsp;
                                    <button type="button" class="btn  btn-danger" onclick="clearSearchChargeGroupCheckedLegalClause();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="padding: 10px">
                    <table  id="charge_group_checked_legal_clause_tb"
                            data-toggle="table"
                            data-sort-name="createTime"
                            data-method="post"
                            data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                            data-sort-order="DESC"
                            data-click-to-select=true
                            data-pagination-detail-h-align="left"
                            data-pagination-show-page-go="true"
                            data-pagination="true"
                            data-page-size="3"
                            data-page-list="[1,2,3,5,10]"
                            data-side-pagination="server"
                            data-query-params="getChargeGroupCheckedLegalClauseQueryParam"
                            data-response-handler="chargeGroupCheckedLegalClauseResponseData"
                            data-url="${pageContext.request.contextPath}/aea/item/service/legal/clause/listAeaItemServiceLegalClauseByChargeGroupId.do">
                        <thead>
                        <tr id="charge_group_checked_legal_clause_tb_tr">
                            <%--<th data-field=""                   data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>--%>
                            <th data-field="sortNo"              data-align="center"  data-colspan="1" data-width="30">序号</th>
                            <th data-field="legalName"              data-align="center"  data-colspan="1" data-width="100">依据名称</th>
                            <th data-field="basicNo"              data-align="center"  data-colspan="1" data-width="80">依据文号</th>
                            <th data-field="issueOrg"              data-align="center"  data-colspan="1" data-width="100">颁布机关</th>
                            <th data-field="exeDate"              data-align="center"  data-colspan="1" data-width="80">实施日期</th>
                            <th data-field="clauseTitle"              data-align="center"  data-colspan="1" data-width="80">条款号</th>
                            <th data-field="clauseContent"              data-align="center"  data-colspan="1" data-width="400">条款内容</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<%--收费项目依据 end--%>