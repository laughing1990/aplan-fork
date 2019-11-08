<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>设立依据</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
	<script>
		var recordId = ${recordId};
	</script>
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
							   设立依据
						   </h3>
					   </div>
				   </div>
			   </div>
			   <div class="m-portlet__body" style="padding: 10px 0px;">
				   <div class="m-form m-form--label-align-right m--margin-bottom-5">
					   <div class="row" style="margin: 0px;">
						   <div class="col-md-6"style="text-align: left;">
							   <button type="button" class="btn btn-info" onclick="openSelectTree();">导入条款</button>
							   <button type="button" class="btn btn-secondary" onclick="batchDelete();">删除</button>
							   <button type="button" class="btn btn-secondary" onclick="refreshBasic();">刷新</button>
						   </div>
						   <div class="col-md-6" style="padding: 0px;">
							   <div class="row" style="margin: 0px;">
								   <div class="col-3"></div>
								   <div class="col-6">
									   <form id="service_basic_form" method="post">
										   <div class="m-input-icon m-input-icon--left">
											   <input type="text" class="form-control m-input"
													  placeholder="请输入关键字..." name="keyword" value=""/>
											   <span class="m-input-icon__icon m-input-icon__icon--left">
												   <span><i class="la la-search"></i></span>
											   </span>
										   </div>
										   <input type="hidden" name="tableName" value="AEA_ITEM"/>
										   <input type="hidden" name="pkName" value="ITEM_ID"/>
										   <input type="hidden" name="recordId" value="${recordId}"/>
									   </form>
								   </div>
								   <div class="col-3">
									   <button type="button" class="btn btn-info" onclick="searchCond();">查询</button>
									   <button type="button" class="btn btn-secondary" onclick="clearSearchCond();">清空</button>
								   </div>
							   </div>
						   </div>
					   </div>
				   </div>
				   <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				   <!--列表区域开始-->
				   <div class="base" style="padding: 0px 5px;">
					   <table id="service_basic_Table"
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
						   data-url="${pageContext.request.contextPath}/aea/item/service/basic/listAeaItemServiceBasic.do">
						   <thead>
							   <tr>
								   <th data-field="#" data-checkbox="true" data-align="center" >ID</th>
								   <th data-field="#" data-colspan="1" data-width="80" data-formatter="indexNum" data-align="center">序号</th>
								   <th data-field="legalName" data-colspan="1" data-width="250">依据名称</th>
								   <th data-field="basicNo" data-colspan="1" data-width="250">依据文号</th>
								   <th data-field="issueOrg" data-colspan="1" data-width="250">颁布机关</th>
								   <th data-field="exeDate" data-colspan="1" data-width="250">实施日期</th>
								   <th data-field="clauseTitle" data-colspan="1" data-width="250">条款号</th>
								   <th data-field="clauseContent" data-colspan="1" data-width="350">条款具体内容</th>
                                   <th data-field="_operator" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="150">操作</th>
							   </tr>
						   </thead>
					   </table>
				   </div>
				   <!--列表区域结束-->
			   </div>
		   </div>
	   </div>
   </div>
   <%@include file="select_service_basic_ztree.jsp"%>
   <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/service_basic_index.js" type="text/javascript"></script>
</body>
</html>