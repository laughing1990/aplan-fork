<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>主题共享材料设置</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp" %>
	<script>
        var themeId = '${themeIdForShareMat}';
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/mat/share/share_mat_info_index.js?<%=isDebugMode%>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/par/share/ui-js/share_mat_info_columns.js?<%=isDebugMode%>"></script>
	<!--事项输入输出-->
	<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/mat/share/item_inout_for_share_mat.js?<%=isDebugMode%>"></script>
	<style>
		.trover
		{
			background: #f9f9f9;
		}
		.trclick
		{
			background: #c4e8f5;
		}

	</style>

</head>
<body>
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
	<div class="row" style="width: 100%;height: 100%;">
		<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
			<div class="m-portlet__head">
				<div class="m-portlet__head-caption">
					<div class="m-portlet__head-title">
							   <span class="m-portlet__head-icon m--hide">
								   <i class="la la-gear"></i>
							   </span>
						<h3 class="m-portlet__head-text">
							主题共享材料管理
						</h3>
					</div>
				</div>
			</div>
			<div class="m-portlet__body" style="padding: 10px 0px;">
				<div class="m-scrollable" data-scrollable="true">
					<div class="row">
						<div class="col-xl-7">
							<button type="button" class="btn btn-accent" onclick="backToTheme();">返回主题</button>
							<button type="button" class="btn  btn-success" onclick="addParShareMat();">新增共享材料</button>
							<button type="button" class="btn btn-danger" onclick="batchDelParShareMat();">删除共享材料</button>
						</div>

						<div class="col-xl-5">
							<div class="form-group m-form__group row align-items-center" style="margin-bottom: 0rem;">
								<div class="col-md-8 m--margin-left-210">
									<div class="input-group">
										<input id="parallerStageKeyword" type="text" class="form-control m-input" placeholder="请输入事项名称..." name="keyword" value=""/>&nbsp;
										<button type="button" class="btn  btn-info" onclick="searchParShareMat();">查询</button>&nbsp;
										<button type="button" class="btn  btn-danger" onclick="clearSearchParShareMat();">清空</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
					<div class="m_datatable_par_share_mat"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 新增或编辑主题共享资料 -->
<%@include file="../../../mat/share/add_share_mat_index.jsp"%>

</body>
</html>