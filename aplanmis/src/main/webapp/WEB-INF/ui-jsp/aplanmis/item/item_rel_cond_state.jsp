<%@ page contentType="text/html;charset=UTF-8" %>

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
		<ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
			<li class="nav-item">
				<a id="item_cond" class="nav-link active" data-toggle="tab" href="#m_tabs_item_cond" onclick="searchItemCond();">
					<i class="la la-gear"></i>
					激活条件
				</a>
			</li>
			<li class="nav-item">
				<a id="item_state" class="nav-link" data-toggle="tab" href="#m_tabs_item_state" onclick="searchItemState();">
					<i class="la la-gear"></i>
					情形设置
				</a>
			</li>
			<li class="nav-item">
				<a id="input_material" class="nav-link" data-toggle="tab" href="#m_tabs_1" onclick="input_material();">
					<i class="la la-gear"></i>
					输入材料
				</a>
			</li>
			<li class="nav-item">
				<a id="output_material" class="nav-link" data-toggle="tab" href="#m_tabs_2" onclick="output_material();">
					<i class="la la-gear"></i>
					输出材料
				</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="m_tabs_item_cond" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-6"style="text-align: left;">
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
											<input type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
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
					<table  id="item_cond_tb"
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
							<th data-field="condMemo"  data-align="center" data-colspan="1" data-width="150">备注</th>
							<th data-field="isActive" data-formatter="isItemCondActiveStr" data-align="center" data-colspan="1" data-width="80">是否启用</th>
							<th data-field="" data-formatter="itemCondListOperatorFormatter" data-align="center" data-colspan="1" data-width="100">操作</th>
						</tr>
						</thead>
					</table>
				</div>
				<!-- 列表区域end -->
			</div>
			<div class="tab-pane" id="m_tabs_item_state" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-10" style="padding: 0px;">
							<div class="row" style="margin: 0px;">
								<div class="col-xl-4">
									<input id="search_item_state_form" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
										   style="background-color: #f0f0f075;border-color: #c4c5d6;">
								</div>
								<div class="col-xl-8">
									<button type="button" class="btn btn-info" onclick="searchItemStateNote();">查询</button>
									<button type="button" class="btn btn-secondary" onclick="clearSearchItemStateNote();">清空</button>
									<button type="button" class="btn btn-secondary" onclick="expandAllState();">展开</button>
									<button type="button" class="btn btn-secondary" onclick="collapseAllState();">折叠</button>
									<button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#uploadModal">导入情形文件</button>
								</div>
							</div>
						</div>
					</div>
				</div>


				<!-- 列表区域 -->
				<div id="listdiv" class="ztree" style="overflow: auto;"></div>
			</div>
			<%@include file="include/item_mat_index_byitem.jsp"%>
		</div>
	</div>
</div>