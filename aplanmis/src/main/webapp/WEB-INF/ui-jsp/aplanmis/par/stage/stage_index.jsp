<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>主题阶段设置</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp" %>
    <script>
        var themeId = '${themeIdForStage}';
    </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/stage/stage_info_index.js?<%=isDebugMode%>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_info_columns.js?<%=isDebugMode%>"></script>
	<style type="text/css">
		.parallerStageSortDiv {
			color: #464637;
			font-size: 14px;
			font-family: 'Roboto', sans-serif;
			font-weight: 300;
		}

		/**标题样式**/
		.parallerStageSortDiv .block_list-title {
			padding: 10px;
			text-align: center;
			background: #abcdf1;
		}

		.parallerStageSortDiv ul {
			margin: 0;
			padding: 0;
			list-style: none;
			display: block;
		}

		.parallerStageSortDiv li {
			background-color: #fff;
			padding: 6px 0px;
			display: list-item;
			text-align: -webkit-match-parent;

			/**边框样式**/
			border: 1px solid transparent;
			border-bottom: 1px solid #ebebeb;
		}

		/**
            * 移动到li样式
        */
		.block_ul_td li:hover {
			background: #e7e8eb;
			cursor: move;
			cursor: -webkit-grabbing;
		}

		.parallerStageSortDiv .drag-handle_th {
			text-align: center;
			display: inline-block;
			width: 8%;
			font-weight: 600;
		}

		/**拖拽手把**/
		.parallerStageSortDiv .drag-handle_td {
			text-align: center;
			font: bold 16px Sans-Serif;
			color: #5F9EDF;
			display: inline-block;
			width: 8%;
		}

		.parallerStageSortDiv .ostage_name_td{
			display: inline-block;
			width: 45%;
		}

	</style>

</head>
<body>
	<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
		<div class="row" style="width: 100%;height: 100%;">
			<div id="centerPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
				<div class="m-portlet__head">
					<div class="m-portlet__head-caption">
						<div class="m-portlet__head-title">
							   <span class="m-portlet__head-icon m--hide">
								   <i class="la la-gear"></i>
							   </span>
							<h3 class="m-portlet__head-text">
								主题阶段管理
							</h3>
						</div>
					</div>
				</div>
				<div class="m-portlet__body" style="padding: 10px 0px;">
					<div class="row">
						<div class="col-xl-7">
							<button type="button" class="btn btn-accent" onclick="backToTheme();">返回主题</button>
							<button type="button" class="btn  btn-success" onclick="addParallerStage();">新增阶段</button>
							<button type="button" class="btn btn-danger" onclick="batchDelParallerStage();">删除阶段</button>
							<button type="button" class="btn btn-secondary" onclick="sortParallerStage();">阶段排序</button>
						</div>
						<div class="col-xl-5">
							<div class="form-group m-form__group row align-items-center" style="margin-bottom: 0rem;">
								<div class="col-md-8 m--margin-left-210">
									<div class="input-group">
										<input id="parallerStageKeyword" type="text" class="form-control m-input" placeholder="请输入阶段名称..." name="keyword" value=""/>&nbsp;
										<button type="button" class="btn  btn-info" onclick="searchParallerStageCondition();">查询</button>&nbsp;
										<button type="button" class="btn  btn-danger" onclick="clearSearchStage();">清空</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
					<div class="m-scrollable" data-scrollable="true" data-max-height="645">
						<div class="m_datatable_par_stage" id="ajax_data"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 新增或编辑阶段 -->
	<%@include file="../../../stage/add_stage_index.jsp"%>

    <!-- 阶段排序 -->
    <%@include file="../../../stage/sort_stage.jsp"%>

    <!-- 设置阶段事项关联 -->
	<%@include file="../../item/select_aea_item_ztree.jsp"%>

</body>
</html>