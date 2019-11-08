<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 id="type_rel_cert_list_title" class="m-portlet__head-text">
					电子证照实例列表
				</h3>
			</div>
		</div>
	</div>
<%--	<div class="m-portlet__body" style="padding: 10px 5px;">
		<ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
			<li class="nav-item">
				<a id="typeRelCert" class="nav-link" data-toggle="tab" href="#m_tabs_type_rel_cert" onclick="searchTypeRelCert();">
					<i class="la la-gear"></i>
					证照实例列表
				</a>
			</li>
		</ul>
		<div class="tab-content">--%>

			<div class="tab-pane" id="m_tabs_type_rel_cert" role="tabpanel">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin: 0px;">
						<div class="col-md-6"style="text-align: left;">
							<button type="button" class="btn btn-success" onclick="addChildCertCaseInfo();">新增证照实例</button>
							<button type="button" class="btn btn-danger" onclick="batchDeleteTypeRelCertCase();">删除</button>
							<button type="button" class="btn btn-accent" onclick="searchTypeRelCertCase();">刷新</button>
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
									<button type="button" class="btn  btn-info" onclick="searchTypeRelCertCase();">查询</button>
									<button type="button" class="btn  btn-danger" onclick="clearSearchTypeRelCertCase();">清空</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
				<div class="m-scrollable" data-scrollable="true" data-max-height="600">
					<div id="type_rel_cert_list_tb" class="m_datatable"></div>
				</div>
			</div>
		</div>
 	</div>
</div>

<script>
	$(function(){
        searchTypeRelCertCase()
        $('#type_rel_cert_case_list_page').show();
	})
</script>