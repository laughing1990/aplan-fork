<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>并联审批主题管理</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp"%>
	<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-html/css/theme.css" type="text/css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
	<script type="text/javascript">
        var keyword = '${keyword}';
        var themeType = '${themeType}';
        var themeTypeName = '${themeTypeName}';
	</script>
	<script src="${pageContext.request.contextPath}/ui-static/theme/js/theme_index.js" type="text/javascript"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/theme/css/theme_index.css">
	<style type="text/css">
		.row{
			display: -ms-flexbox;
		}
	</style>
</head>
<body>
	<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
		<div class="row" style="width: 100%;height: 100%;margin: 0px;">
			<div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
				<div class="m-portlet__body" style="padding: 10px 0px;">
					<div class="m-form m-form--label-align-right m--margin-bottom-5">
						<div class="row" style="margin: 0px;margin-bottom: 30px;">
							<div class="col-xl-6 order-1 m--align-left m--padding-left-20">
								<button type="button" class="btn btn-primary" onclick="addTheme();">新增主题</button>
								<button type="button" class="btn btn-secondary" onclick="themeSort();">主题排序</button>
							</div>
							<div class="col-xl-6 order-2 order-xl-1  m--align-right">
								<div class="form-group m-form__group row align-items-center">
									<div class="col-md-8 m--margin-left-210">
										<div class="input-group">
											<div class="input-group-prepend">
												<button id="searchAllBtn" type="button" class="btn btn-secondary"
														data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
													全部分类<i class="fa fa-angle-down m--margin-left-10"></i>
												</button>
												<div class="dropdown-menu">
													<a class="dropdown-item" href="javascript:fecthCategory('')">全部分类</a>
													<c:forEach items="${themeTypes}" var="themeType">
														<a id="${themeType.itemCode}" class="dropdown-item" href="javascript:fecthCategory('${themeType.itemCode}')">${themeType.itemName}</a>
													</c:forEach>
												</div>
											</div>
											<input type="text" id="keyword" class="form-control" placeholder="输入关键字...">&nbsp;
											<button type="button" class="btn btn-primary" onclick="searchThemeData();">查询</button>&nbsp;
											<button type="button" class="btn btn-secondary"  onclick="clearSearchThemeData();">清空</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="m-portlet__body m--padding-top-0">
						<!--begin: Datatable -->
						<div class="m_datatable m-datatable m-datatable--default m-datatable--loaded">
							<table class="m-datatable__table" style="display: block;  overflow-x: auto;">
								<thead class="m-datatable__head">
								<tr class="m-datatable__row border-bottom text-center" style="left: 0px;">
									<th  class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 26%;padding-left: 50px;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">主题名称</span>
									</th>
									<th  class="m-datatable__cell text-left m-datatable__cell--sort  bg-transparent" style="width: 12%;padding-left: 30px;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">主题类型</span>
									</th>
									<th  class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 15%;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">主题编码</span>
									</th>
									<th  class="m-datatable__cell text-left m-datatable__cell--sort  bg-transparent" style="width: 10%;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">最新版本</span>
									</th>
									<th  class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;padding-left: 40px;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">申报说明</span>
									</th>
									<th  class="m-datatable__cell text-center m-datatable__cell--sort bg-transparent" style="width: 12%;">
										<span class="h6 m--margin-bottom-0 m--font-titlegrey">操作</span>
									</th>
								</tr>
								</thead>
								<tbody class="m-datatable__body">
								<c:choose>
									<c:when test="${themes!=null and themes.size()>0}">
										<c:forEach items="${themes}" var="entity">
											<tr data-row="0" class="m-datatable__row  border-bottom" style="left: 0px;" ondblclick="goThemeSetings('${entity.themeId}');">
												<td class="m-datatable__cell text-left" style="width: 26%;padding: 20px 10px;">
													<div class="m-card-user m-card-user--sm">
														<div class="m-card-user__pic">
															<div class="m-card-user__no-photo m--bg-fill-info">
																<span>${fn:substring(entity.themeName,0,1)}</span>
															</div>
														</div>
														<div class="m-card-user__details">
															<span class="h5">${entity.themeName}</span><br/>
															<a href="javascript:void(0);" class="m--font-metal">
																<c:if test="${!empty entity.modifyTime}">
																	<fmt:formatDate value="${entity.modifyTime}" pattern="yyyy.MM.dd hh:mm"/>&nbsp;更新
																</c:if>
															</a>
														</div>
													</div>
												</td>

												<td class="m-datatable__cell text-left" style="width: 12%;padding-left: 25px;">
													<c:if test="${!empty entity.themeType}">
														<c:forEach items="${themeTypes}" var="themeType">
															<c:if test="${entity.themeType==themeType.itemCode}">
																${themeType.itemName}
															</c:if>
														</c:forEach>
													</c:if>
												</td>

												<td class="m-datatable__cell text-left" style="width: 15%;">
													${entity.themeCode}
												</td>

												<td class="m-datatable__cell text-left" style="width: 10%;padding-left: 25px;">
													${entity.themeVerName}
												</td>

												<td class="m-datatable__cell text-left" style="width: 10%;">
													<c:if test="${fn:length(entity.themeMemo)>50}">
														${fn:substring(entity.themeMemo,0,50)}...
													</c:if>
													<c:if test="${fn:length(entity.themeMemo)<=50}">
														${entity.themeMemo}
													</c:if>
												</td>

												<td class="m-datatable__cell text-center" style="width: 12%;">
													<a href="javascript:editThemeById('${entity.themeId}');" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑">
														<i class="la la-edit"></i>
													</a>&nbsp;
													<a href="javascript:goThemeSetings('${entity.themeId}');" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="主题详情">
														<i class="la la-gear"></i>
													</a>&nbsp;
													<a href="javascript:deleteThemeById('${entity.themeId}');" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除">
														<i class="la la-trash"></i>
													</a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="m-datatable__row text-center" style="left: 0px;height: 50px;">
											<th colspan="7">
												<span class="h6 m--font-titlegrey">暂无数据!</span>
											</th>
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 新增或编辑主题 -->
	<%@include file="add_theme_index.jsp"%>

	<!-- 主题排序 -->
	<%@include file="sort_theme_index.jsp"%>

	<!-- 设置图标 -->
	<%@include file="common_set_img.jsp"%>

</body>
</html>