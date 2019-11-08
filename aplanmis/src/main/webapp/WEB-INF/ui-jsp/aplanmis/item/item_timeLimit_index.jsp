<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>许可时限</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }

		.fixed-table-body::-webkit-scrollbar{
			width:4px;
			height:4px;
		}
		.fixed-table-body::-webkit-scrollbar-track{
			background: #fff;
			border-radius: 2px;
		}
		.fixed-table-body::-webkit-scrollbar-thumb{
			background: #e2e5ec;
			border-radius:2px;
		}
		.fixed-table-body::-webkit-scrollbar-thumb:hover{
			background: #aaa;
		}
		.fixed-table-body::-webkit-scrollbar-corner{
			background: #fff;
		}
    </style>
	<script>
		var itemBasicId = ${itemBasicId};
	</script>
</head>
<body>
   <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
	   <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
		   <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
			   <div class="m-portlet__head">
				   <div class="m-portlet__head-caption">
					   <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
						   <h3 class="m-portlet__head-text">
							 许可时限
						   </h3>
					   </div>
				   </div>
			   </div>
			   <div class="m-portlet__body" style="padding: 10px 0px;">
				   <div class="m-form m-form--label-align-right m--margin-bottom-5">
					   <div class="row" style="margin: 0px;">
						   <div class="col-md-6"style="text-align: left;">
							   <button type="button" class="btn btn-info" onclick="addTimeLimit();">新增</button>
							   <button type="button" class="btn btn-secondary" onclick="batchDelete();">删除</button>
							   <button type="button" class="btn btn-secondary" onclick="refreshCond();">刷新</button>
						   </div>
						   <div class="col-md-6" style="padding: 0px;">
							   <div class="row" style="margin: 0px;">
								   <div class="col-3"></div>
								   <div class="col-6">
									   <form id="time_limit_form" method="post">
										   <%--<div class="m-input-icon m-input-icon--left">
											   <input type="text" class="form-control m-input"
													  placeholder="请输入关键字..." name="keyword" value=""/>
											   <span class="m-input-icon__icon m-input-icon__icon--left">
												   <span><i class="la la-search"></i></span>
											   </span>
										   </div>--%>
										   <input type="hidden" name="itemBasicId" value="${itemBasicId}">
									   </form>
								   </div>
								   <%--<div class="col-3">
									   <button type="button" class="btn btn-info" onclick="searchTimeLimit();">查询</button>
									   <button type="button" class="btn btn-secondary" onclick="clearSearchTimeLimit();">清空</button>
								   </div>--%>
							   </div>
						   </div>
					   </div>
				   </div>
				   <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				   <div class="base" style="padding: 0px 5px;">
					   <table id="timeLimitTable"
						   data-toggle="table",
						   data-click-to-select=true,
						   queryParamsType="",
						   data-pagination=true,
						   data-page-size="10",
						   data-side-pagination="server",
						   data-page-list="[10, 20, 50, 100]",
						   data-side-pagination="server",
						   data-query-params="dealQueryParams",
						   data-pagination-show-page-go="true",
						   data-url="${pageContext.request.contextPath}/aea/item/service/timelimit/listAeaItemServiceTimelimit.do">
						   <thead>
							   <tr>
								   <th data-field="#" data-checkbox="true" data-colspan="1" data-width="10"></th>
								   <th data-field="slsx" data-colspan="1" data-width="250">受理时限</th>
								   <th data-field="slsxdw" data-colspan="1" data-width="250">受理时限单位</th>
								   <th data-field="slsxsm" data-colspan="1" data-width="250">受理时限说明</th>
                                   <th data-field="_operator" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="100">操作</th>
							   </tr>
						   </thead>
					   </table>
				   </div>
			   </div>
		   </div>
	   </div>
   </div>

   <!--新增许可时限-->
  <%@include file="add_timeLimit_index.jsp"%>

   <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_timeLimit_index.js" type="text/javascript"></script>
</body>
</html>