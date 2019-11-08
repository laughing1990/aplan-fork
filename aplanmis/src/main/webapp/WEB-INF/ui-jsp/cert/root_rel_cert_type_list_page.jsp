<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 id="root_rel_cert_type_list_title" class="m-portlet__head-text">
					电子证照分类列表
				</h3>
			</div>
		</div>
	</div>
	<div class="m-portlet__body" style="padding: 10px 5px;">
		<div class="m-form m-form--label-align-right m--margin-bottom-5">
			<div class="row" style="margin: 0px;">
				<div class="col-md-6"style="text-align: left;">
					<button type="button" class="btn btn-info" onclick="addChildCertType();">新增分类</button>
					<button type="button" class="btn btn-secondary" onclick="batchDeleteCertType();">删除</button>
					<button type="button" class="btn btn-secondary" onclick="refreshRootRelCertType();">刷新</button>
				</div>
				<div class="col-md-6" style="padding: 0px;">
					<div class="row" style="margin: 0px;">
						<div class="col-2"></div>
						<div class="col-6">
							<form id="search_root_rel_certType_form" method="post">
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
							<button type="button" class="btn  btn-info" onclick="searchRootRelCertType();">查询</button>
							<button type="button" class="btn  btn-secondary" onclick="clearRootRelCertType();">清空</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
		<div class="base" style="padding: 0px 5px;">
			<table id="root_rel_cert_type_list_tb"
					data-toggle="table"
					data-method="post"
					data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
					data-click-to-select=true
					data-pagination-detail-h-align="left"
					data-pagination-show-page-go="true"
					data-page-size="10"
					data-page-list="[10,20,30,50,100]",
					data-pagination=true
					data-side-pagination="server"
					data-pagination-detail-h-align="left"
					data-query-params="rootRelCertTypeQueryParam"
					data-response-handler="rootRelCertTypeResponseData"
					data-url="${pageContext.request.contextPath}/aea/cert/type/listAeaCertType.do">
				<thead>
					<tr>
						<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
						<th data-field="typeName" data-align="left" data-colspan="1" data-width="200">分类名称</th>
						<th data-field="typeCode" data-align="left" data-colspan="1" data-width="150">分类编号</th>
						<th data-field="sortNo"  data-align="center" data-colspan="1" data-width="80">排序</th>
						<th data-field="isActive"  data-align="center" data-formatter="isActiveFormatter"
							data-colspan="1" data-width="100">是否启用</th>
						<th data-field="" data-formatter="rootRelCertTypeOperatorFormatter"
							data-align="center" data-colspan="1" data-width="100">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>