<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 id="type_rel_major_list_title" class="m-portlet__head-text">
					专业类别
				</h3>
			</div>
		</div>
	</div>
	<div class="m-portlet__body" style="padding: 0px 5px;">
		<div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
			<div class="m-portlet__body" style="padding: 10px 0px;">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-12" style="padding: 0px;">
							<form id="search_service_rel_major_form" method="post">
								<div class="row" style="margin: 0px;">
									<div class="col-4">
										<button type="button" class="btn btn-info" onclick="configCert();">配置证书</button>
										<button type="button" class="btn btn-info" onclick="configQualLevel();">配置资质类型</button>
									</div>
									<div class="col-4" style="text-align: right;">
										<div class="m-input-icon m-input-icon--left">
											<input id="majorInfoTreeKeyWord" type="text"
												   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
												   style="background-color: #f0f0f075;border-color: #c4c5d6;">
											<span class="m-input-icon__icon m-input-icon__icon--left">
												<span><i class="la la-search"></i></span>
											</span>
										</div>
									</div>
									<div class="col-4"  style="text-align: left;">
										<button type="button" class="btn btn-info"onclick="collapsemajorInfoTreeAllNode();">折叠</button>
										<button type="button" class="btn btn-info"onclick="expandmajorInfoTreeAllNode();">展开</button>
										<button type="button" class="btn btn-secondary"onclick="clearSearchmajorInfoTreeNode();">清空</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				<!-- 专业树区域start -->
				<div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
					<%--滚动条--%>
					<div id="majorInfoTreeScrollable" style="height: 400px;overflow-x: hidden;overflow-y: auto;">
						<div id="majorInfoTree" class="ztree" style="overflow: auto;margin-left:50px"></div>
					</div>
				</div>
				<!-- 专业树区域end -->
			</div>
		</div>
		<%--右键菜单--%>
		<div id="rootContextMenu" class="contextMenuDiv">
			<a href="javascript:void(0);" class="list-group-item" onclick="addServiceMajor();">
				<i class="fa flaticon-plus"></i>新增专业类别
			</a>
		</div>
		<div id="serviceMajorContextMenu" class="contextMenuDiv">
			<a href="javascript:void(0);" class="list-group-item" onclick="addServiceMajor();">
				<i class="fa flaticon-plus"></i>新增专业类别
			</a>
			<a href="javascript:void(0);" class="list-group-item" onclick="editServiceMajor();">
				<i class="fa flaticon-edit-1"></i>编辑专业类别
			</a>
			<a href="javascript:void(0);" class="list-group-item" onclick="deleteServiceMajor();">
				<i class="fa fa-times"></i>删除专业类别
			</a>
			<a href="javascript:void(0);" class="list-group-item" onclick="deleteServiceMajor('1');">
				<i class="fa fa-times"></i>删除专业及子专业
			</a>
		</div>
 	</div>
</div>