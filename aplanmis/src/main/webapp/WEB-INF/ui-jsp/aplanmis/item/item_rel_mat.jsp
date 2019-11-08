<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 class="m-portlet__head-text">
					关联材料列表
				</h3>
			</div>
		</div>
	</div>
	<div class="m-portlet__body" style="padding: 10px 5px;">
		<ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
			<li class="nav-item">
				<a id="widgetInfo" class="nav-link active" data-toggle="tab" href="#m_tabs_4_1">
					<i class="la la-map-marker"></i>
					材料列表
				</a>
			</li>
			<li class="nav-item">
				<a id="widgetkeys" class="nav-link" data-toggle="tab" href="#m_tabs_4_2">
					<i class="la la-gear"></i>
					材料分类
				</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="m_tabs_4_1" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row">
						<div class="col-md-6"style="text-align: left;">
							<button type="button" class="btn  btn-success" onclick="">新增</button>
							<button type="button" class="btn  btn-accent" onclick="">刷新</button>
						</div>
						<div class="col-md-6" style="padding: 0px;">
							<div class="row">
								<div class="col-2"></div>
								<div class="col-6">
									<form id="search_item_mat_info_form" method="post">
										<div class="m-input-icon m-input-icon--left">
											<input type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
											<span class="m-input-icon__icon m-input-icon__icon--left">
										   <span><i class="la la-search"></i></span>
										</span>
										</div>
									</form>
								</div>
								<div class="col-4">
									<button type="button" class="btn  btn-info" onclick="">查询</button>
									<button type="button" class="btn  btn-danger" onclick="">清空</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="itemMatScrollable" class="m-scrollable" data-scrollable="true" data-max-height="615">
					<div id="item_mat_info_tb" class="m_datatable"></div>
				</div>
			</div>

			<div class="tab-pane" id="m_tabs_4_2" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row">
						<div class="col-4">
							<form id="search_item_mat_info_type_form" method="post">
								<div class="m-input-icon m-input-icon--left">
									<input type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
									<span class="m-input-icon__icon m-input-icon__icon--left">
									<span><i class="la la-search"></i></span>
								</span>
								</div>
							</form>
						</div>
						<div class="col-8">
							<button type="button" class="btn  btn-info" onclick="">查询</button>
							<button type="button" class="btn  btn-danger" onclick="">清空</button>
							<button type="button" class="btn  btn-success" onclick="">新增顶级分类</button>
							<button type="button" class="btn btn-accent" onclick="expandAll();">展开</button>
							<button type="button" class="btn btn-secondary" onclick="collapseAll();">折叠</button>
						</div>
					</div>
				</div>
				<div id="itemMatTypeScrollable" class="m-scrollable" data-scrollable="true" data-max-height="615">
					<div id="item_mat_info_type_tb" class="m_datatable"></div>
				</div>
			</div>
		</div>
	</div>
</div>