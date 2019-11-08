<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>阶段情形设置</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
	<link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript">
        var stageId = '${stageId}';
        var themeId = '${themeId}';
        var isNeedState = '${stage.isNeedState}';
    </script>
	<style type="text/css">
		.row{
			margin-left: 0px;
			margin-left: 0px;
		}

		#stageStateTree::-webkit-scrollbar{
			width:4px;
			height:4px;
		}
		#stageStateTree::-webkit-scrollbar-track{
			background: #fff;
			border-radius: 2px;
		}
		#stageStateTree::-webkit-scrollbar-thumb{
			background: #e2e5ec;
			border-radius:2px;
		}
		#stageStateTree::-webkit-scrollbar-thumb:hover{
			background: #aaa;
		}
		#stageStateTree::-webkit-scrollbar-corner{
			background: #fff;
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
	<div style="width: 100%;height: 100%; padding: 10px 10px 5px 10px;">
		<div class="row" style="width: 100%;height: 100%;margin: 0px;">
			<div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
				<div class="m-portlet__body" style="padding: 10px 0px;height: 99%;">
					<div class="row" style="margin: 0px;">
						<div class="col-xl-2 m--align-left m--padding-left-20"></div>
						<div class="col-xl-3 m--align-left m--padding-left-20">
							<div class="m-radio-inline">
								<label class="m-radio">
									<input id="isNeedState1" name="isNeedState" value="1"
										   type="radio" onclick="selectIsNeedState('#isNeedState1');">分情形
									<span for="isNeedState1"></span>
								</label>&nbsp;&nbsp;&nbsp;&nbsp;
								<label class="m-radio">
									<input id="isNeedState0" name="isNeedState" value="0"
										   type="radio" onclick="selectIsNeedState('#isNeedState0');">不分情形
									<span for="isNeedState0"></span>
								</label>
							</div>
						</div>
						<div class="col-xl-7 m--align-left">
							<span style="font-size: 18px;font-weight: bold;">${stage.stageName}情形设置</span>
						</div>
					</div>

					<!-- 不需要分情形 -->
					<div id="isNoStageStateDiv" class="row" style="margin: 15px 0px 10px 0px;height: 95%;display: none;">
						<div class="col-xl-12">
							<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
								<div class="m-portlet__head">
									<div class="m-portlet__head-caption" style="padding: 3px 0px;">
										<div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                                <span class="m-portlet__head-icon m--hide">
                                                    <i class="la la-gear"></i>
                                                </span>
											<h3 class="m-portlet__head-text">阶段相关材料/证照</h3>
										</div>
									</div>
								</div>
								<div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                        <div class="row" style="margin: 0px;">
                                            <div class="col-md-6"style="text-align: left;">
                                                <button type="button" class="btn btn-info"
                                                        onclick="addStageRelMat();">新增材料</button>
                                                <button type="button" class="btn btn-info"
                                                        onclick="addStageGlobalMat();">导入全局材料</button>
                                                <button type="button" class="btn btn-secondary"
                                                        onclick="batchDeleteStageRelMatCert();">删除</button>
                                                <button type="button" class="btn btn-secondary"
                                                        onclick="refreshStageRelMatCert();">刷新</button>
                                            </div>
                                            <div class="col-md-6" style="padding: 0px;">
                                                <div class="row" style="margin: 0px;">
                                                    <div class="col-2"></div>
                                                    <div class="col-6" style="text-align: right;">
                                                        <form id="search_stage_rel_mat_cert_form" method="post">
                                                            <div class="m-input-icon m-input-icon--left">
                                                                <input type="text" class="form-control m-input"
                                                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                    <span><i class="la la-search"></i></span>
                                                                </span>
                                                            </div>
                                                        </form>
                                                    </div>
                                                    <div class="col-4"  style="text-align: center;">
                                                        <button type="button" class="btn btn-info"
                                                                onclick="searchStageRelMatCert();">查询</button>
                                                        <button type="button" class="btn btn-secondary"     onclick="clearSearchStageRelMatCert();">清空</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <!-- 列表区域 -->
                                    <div class="base" style="padding: 0px 5px;">
                                        <table  id="stage_rel_mat_cert_tb"
												data-toggle="table",
												data-click-to-select=true,
												queryParamsType="",
												data-pagination=true,
												data-page-size="10",
												data-side-pagination="server",
												data-page-list="[10, 20, 50, 100]",
												data-side-pagination="server",
                                                data-query-params="stageRelMatCertParam",
                                                data-pagination-show-page-go="true",
                                                data-url="${pageContext.request.contextPath}/aea/par/state/listNoStateInMatCertByStageId.do">
                                            <thead>
                                                <tr>
                                                    <th data-field="#" data-checkbox="true" data-align="left" data-width="10">ID</th>
                                                    <th data-field="aeaMatCertName" data-align="left" data-width="250">名称</th>
                                                    <th data-field="aeaMatCertCode" data-align="left" data-width="150">编号</th>
                                                    <th data-field="fileType" data-formatter="fileTypeFormatter2"
                                                        data-align="left" data-width=60>文件类型</th>
                                                    <th data-field="_operator" data-formatter="stageRelMatCertFormatter"
                                                        data-align="center" data-width="100">操作</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                    <!-- 列表区域end -->
								</div>
							</div>
						</div>
					</div>

					<!-- 分情形 -->
					<div id="isStageStateDiv" class="row" style="margin: 15px 0px 10px 0px;height: 95%;">
						<div class="col-xl-4" style="padding: 0px 2px 0px 8px;">
							<div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
								<div class="m-portlet__head">
									<div class="m-portlet__head-caption" style="padding: 3px 0px;">
										<div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                                <span class="m-portlet__head-icon m--hide">
                                                    <i class="la la-gear"></i>
                                                </span>
											<h3 class="m-portlet__head-text">情形设置</h3>
										</div>
									</div>
								</div>
								<div class="m-portlet__body" style="padding: 10px 0px;">
									<div class="row" style="margin: 0px;">
										<div class="col-xl-5">
											<input id="stageStateKeyWord" type="text"
												   class="form-control m-input m-input--solid empty"
												   placeholder="请输入关键字..."
												   style="background-color: #f0f0f075;
												   border-color: #c4c5d6;">
										</div>
										<div class="col-xl-7">
											<button type="button" class="btn btn-info"
													onclick="searchStageStateNode();">查询</button>
											<button type="button" class="btn btn btn-secondary"
													onclick="clearSearchStageStateNode();">清空</button>
											<button type="button" class="btn btn-secondary"
													onclick="expandStageStateAll();">展开</button>
											<button type="button" class="btn btn-secondary"
													onclick="collapseStageStateAll();">折叠</button>
										</div>
									</div>
									<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
									<div id="stageStateTree" class="ztree" style="height:645px;overflow: auto;"></div>
								</div>
							</div>
						</div>

						<div class="col-xl-8" style="padding: 0px 8px 0px 2px;">

							<!-- 新增、编辑情形 -->
							<div id="add_stage_state_page" style="display: none;width: 100%;height: 100%;">
								<%@include file="add_stage_state_index.jsp"%>
							</div>

							<!-- 阶段情形列表 -->
							<div id="stage_rel_state_page" style="display: none;width: 100%;height: 100%;">
								<%@include file="stage_rel_state_index.jsp"%>
							</div>

							<!-- 情形与材料列表 -->
							<div id="stage_rel_state_mat_page" style="display: none;width: 100%;height: 100%;">
								<%@include file="stage_rel_state_mat_index.jsp"%>
							</div>

							<!-- 新增情形材料 -->
							<div id="add_stage_mat_page" style="display: none;width: 100%;height: 100%;">
								<%@include file="add_stage_mat_index.jsp"%>
							</div>

							<!-- 新增情形证照 -->
							<div id="add_stage_cert_page" style="display: none;width: 100%;height: 100%;">
								<%@include file="add_stage_cert_index.jsp"%>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 右键菜单 -->
	<div id="stageContextMenu" class="contextMenuDiv">
		<a href="javascript:void(0);" class="list-group-item" onclick="addStageState();">
			<i class="fa flaticon-plus"></i>新增情形
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="setItemMatInfo();">
			<i class="fa flaticon-plus"></i>设置事项材料
		</a>
	</div>

	<div id="stageStateContextMenu" class="contextMenuDiv">
		<a href="javascript:void(0);" class="list-group-item" onclick="addStageState();">
			<i class="fa flaticon-plus"></i>新增情形
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="addStateMat();">
			<i class="fa flaticon-plus"></i>新增材料
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="addStateCert();">
			<i class="fa flaticon-plus"></i>新增证照
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="setStageItemInfo();">
			<i class="fa flaticon-plus"></i>绑定事项
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="editStageState();">
			<i class="fa flaticon-edit-1"></i>编辑
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="deleteStageState();">
			<i class="fa fa-times"></i>删除
		</a>
	</div>

	<div id="matCertContextMenu" class="contextMenuDiv">
		<a href="javascript:void(0);" class="list-group-item" onclick="editMatCert();">
			<i class="fa flaticon-edit-1"></i>编辑
		</a>
		<a href="javascript:void(0);" class="list-group-item" onclick="deleteMatCert();">
			<i class="fa fa-times"></i>删除
		</a>
	</div>

    <!-- 阶段新增/编辑材料 -->
    <%@include file="../../../kitymind/stage/state/aedit_stage_mat_index.jsp"%>

    <!-- 阶段新增/编辑证照 -->
    <%@include file="aedit_stage_cert_index.jsp"%>

	<!-- 选择项目字段 -->
	<%@include file="../../../kitymind/item/detail/select_meta_db_tbcol_ztree.jsp"%>

	<!-- 选择材料类别 -->
	<%@include file="../../item/select_mat_type_ztree.jsp"%>

	<!-- 选择电子证照 -->
	<%@include file="../../cert/select_aea_cert_ztree.jsp"%>

    <!-- 选择电子证照文件库 -->
    <%@include file="../../cert/select_bsc_att_dir_ztree.jsp"%>

	<!-- 指定情形事项 -->
	<%@include file="select_stage_item_ztree.jsp"%>

	<!-- 情形全局材料 -->
	<%@include file="select_state_global_mat.jsp"%>

	<!-- 阶段全局材料 -->
	<%@include file="select_stage_global_mat.jsp"%>

	<!-- 阶段材料设置 -->
	<%@include file="set_stage_item_mat.jsp"%>

	<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/set_stage_state_index2.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_rel_state_index.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_rel_state_state_index.js" type="text/javascript"></script>
	<!-- 阶段关联情形材料证照数据 -->
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_rel_state_mat_cert_index.js" type="text/javascript"></script>
	<!-- 阶段关联材料证照数据 -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_rel_mat_cert_index.js" type="text/javascript"></script>
    <!-- 导入情形全局材料 -->
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/select_state_global_mat.js" type="text/javascript"></script>
    <!-- 导入阶段全局材料 -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/select_stage_global_mat.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/set_stage_item_mat.js" type="text/javascript"></script>
</body>
</html>