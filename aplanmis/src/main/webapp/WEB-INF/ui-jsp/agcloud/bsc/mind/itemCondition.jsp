<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>行政审批事项库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <script>
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
    </script>
    <style>
        #ifrtest {
            height: 100%;
        }
    </style>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"/>
<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
                <h3 class="m-portlet__head-text">
                    事项相关设置列表
                </h3>
            </div>
        </div>
    </div>
    <div class="m-portlet__body" style="padding: 10px 5px;height:96%">
        <div class="m-form m-form--label-align-right m--margin-bottom-5">
            <div class="row" style="margin: 0px;">
                <div class="col-md-6" style="text-align: left;">
                    <button type="button" class="btn btn-info" onclick="addItemCond();">新增</button>
                    <button type="button" class="btn btn-secondary" onclick="batchDeleteItemCond();">删除</button>
                    <button type="button" class="btn btn-secondary" onclick="refreshItemCond();">刷新</button>
                </div>
                <div class="col-md-6" style="padding: 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-2"></div>
                        <div class="col-6">
                            <form id="search_item_cond_form" method="post">
                                <div class="m-input-icon m-input-icon--left">
                                    <input type="text" class="form-control m-input" placeholder="请输入关键字..."
                                           name="keyword" value=""/>
                                    <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                </div>
                            </form>
                        </div>
                        <div class="col-4">
                            <button type="button" class="btn btn-info" onclick="searchItemCond();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchItemCond();">清空</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 列表区域 -->
        <div class="base" style="padding: 10px">
            <table id="item_cond_tb"
                   data-toggle="table"
                   data-height="600"
                   data-method="post"
                   data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                   data-click-to-select=true
                   data-pagination-detail-h-align="left"
                   data-pagination-show-page-go="true"
                   data-page-size="10"
                   data-page-list="[10,20,30,50,100]"
                   data-pagination=true
                   data-side-pagination="server"
                   data-pagination-detail-h-align="left"
                   data-query-params="dealQueryParam"
                   data-response-handler="itemCondListResponseData"
                   data-url="${pageContext.request.contextPath}/aea/item/cond/listAeaItemCond.do">
                <thead>
                <tr>
                    <th data-field="" data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                    <th data-field="condName" data-align="left" data-colspan="1" data-width="150">条件名称</th>
                    <th data-field="condEl" data-align="left" data-colspan="1" data-width="150">表达式</th>
                    <th data-field="sortNo" data-align="center" data-colspan="1" data-width="60">排序</th>
                    <th data-field="condMemo" data-align="center" data-colspan="1" data-width="150">备注</th>
                    <th data-field="isActive" data-formatter="isItemCondActiveStr" data-align="center" data-colspan="1"
                        data-width="80">是否启用
                    </th>
                    <th data-field="" data-formatter="itemCondListOperatorFormatter" data-align="center"
                        data-colspan="1" data-width="100">操作
                    </th>
                </tr>
                </thead>
            </table>
        </div>
        <!-- 列表区域end -->
    </div>
</div>
<jsp:include page="addItemCondition.jsp"/>

<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/activate_condition.js"></script>
</body>
</html>