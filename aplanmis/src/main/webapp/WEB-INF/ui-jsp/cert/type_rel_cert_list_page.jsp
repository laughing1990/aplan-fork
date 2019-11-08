<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 id="type_rel_cert_list_title" class="m-portlet__head-text">
					电子证照列表
				</h3>
			</div>
		</div>
	</div>
	<div class="m-portlet__body" style="padding: 10px 5px;">
		<ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
			<li class="nav-item">
				<a id="childCertType" class="nav-link active" data-toggle="tab" href="#m_tabs_child_cert_type" onclick="searchChildCertType();">
					<i class="la la-gear"></i>
					分类列表
				</a>
			</li>
			<li class="nav-item">
				<a id="typeRelCert" class="nav-link" data-toggle="tab" href="#m_tabs_type_rel_cert" onclick="searchTypeRelCert();">
					<i class="la la-gear"></i>
					证照列表
				</a>
			</li>
		</ul>
		<div class="tab-content">
			<div id="m_tabs_child_cert_type" class="tab-pane active" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-6"style="text-align: left;">
							<button type="button" class="btn btn-info" onclick="addChildCertType();">新增分类</button>
							<button type="button" class="btn btn-secondary" onclick="batchDeleteChildCertType();">删除</button>
							<button type="button" class="btn btn-secondary" onclick="refreshChildCertType();">刷新</button>
						</div>
						<div class="col-md-6" style="padding: 0px;">
							<div class="row" style="margin: 0px;">
								<div class="col-2"></div>
								<div class="col-6">
									<form id="search_child_cert_type_form" method="post">
										<div class="m-input-icon m-input-icon--left">
											<input type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
											<span class="m-input-icon__icon m-input-icon__icon--left">
										<span><i class="la la-search"></i></span>
									</span>
										</div>
									</form>
								</div>
								<div class="col-4">
									<button type="button" class="btn  btn-info" onclick="searchChildCertType();">查询</button>
									<button type="button" class="btn  btn-secondary" onclick="clearChildCertType();">清空</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 列表区域 -->
				<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				<div class="base" style="padding: 0px 5px;">
					<table id="child_cert_type_list_tb"
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
						   data-query-params="childCertTypeQueryParam"
						   data-response-handler="childCertTypeResponseData"
						   data-url="${pageContext.request.contextPath}/aea/cert/type/listAeaCertType.do">
						<thead>
							<tr>
								<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
								<th data-field="typeName" data-align="left" data-colspan="1" data-width="200">分类名称</th>
								<th data-field="typeCode" data-align="left" data-colspan="1" data-width="150">分类编号</th>
								<th data-field="sortNo"  data-align="center" data-colspan="1" data-width="80">排序</th>
								<th data-field="isActive"  data-align="center" data-formatter="childCertTypeIsActiveFormatter"
									data-colspan="1" data-width="100">是否启用</th>
								<th data-field="" data-formatter="childCertTypeOperatorFormatter"
									data-align="center" data-colspan="1" data-width="100">操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

			<div class="tab-pane" id="m_tabs_type_rel_cert" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-6"style="text-align: left;">
							<button type="button" class="btn btn-info" onclick="addChildCertInfo();">新增证照</button>
							<button type="button" class="btn btn-secondary" onclick="batchDeleteTypeRelCert();">删除</button>
							<button type="button" class="btn btn-secondary" onclick="searchTypeRelCert();">刷新</button>
						</div>
						<div class="col-md-6" style="padding: 0px;">
							<div class="row" style="margin: 0px;">
								<div class="col-2"></div>
								<div class="col-6">
									<form id="search_type_rel_cert_list_form" method="post">
										<div class="m-input-icon m-input-icon--left">
											<input type="text" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
											<span class="m-input-icon__icon m-input-icon__icon--left">
										<span><i class="la la-search"></i></span>
									</span>
										</div>
									</form>
								</div>
								<div class="col-4">
									<button type="button" class="btn btn-info" onclick="searchTypeRelCert();">查询</button>
									<button type="button" class="btn btn-secondary" onclick="clearSearchTypeRelCert();">清空</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				<div class="base" style="padding: 0px 5px;">
					<table id="type_rel_cert_list_tb"
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
						   data-query-params="typeRelCertQueryParam"
						   data-response-handler="typeRelCertResponseData"
						   data-url="${pageContext.request.contextPath}/aea/cert/listAeaCertByPage.do">
						<thead>
							<tr>
								<th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
								<th data-field="certName" data-align="left" data-colspan="1" data-width="200">证照名称</th>
								<th data-field="certCode" data-align="left" data-colspan="1" data-width="150">证照编号</th>
								<th data-field="certHolder"  data-align="center" data-formatter="certHolderFormatter"
									data-colspan="1" data-width="60">证照类型</th>
								<th data-field="certMemo"  data-align="center" data-colspan="1" data-width="150">备注</th>
								<th data-field="" data-formatter="typeRelCertOperatorFormatter"
									data-align="center" data-colspan="1" data-width="100">操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
 	</div>
</div>