<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
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

			<%--<div class="tab-pane" id="m_tabs_type_rel_cert" role="tabpanel">--%>
	<div class="m-portlet__body" style="padding: 10px 5px;">
		<div class="m-form m-form--label-align-right m--margin-bottom-5">
			<div class="row" style="margin: 0px;">
				<div class="col-md-6" style="text-align: left;">
					<button type="button" class="btn btn-success" onclick="addAeaHiCertInfo();">新增证照实例</button>
					<button type="button" class="btn btn-danger" onclick="batchDeleteAeaHiCertinstByIds();">删除</button>
					<button type="button" class="btn btn-accent" onclick="searchAeaHiCert();">刷新</button>
				</div>
				<div class="col-md-6" style="padding: 0px;">
					<div class="row" style="margin: 0px;">
						<div class="col-2"></div>
						<div class="col-6">
							<form id="search_aea_hi_cert_list_form" method="post">
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
							<button type="button" class="btn  btn-info" onclick="searchAeaHiCert();">查询</button>
							<button type="button" class="btn  btn-danger" onclick="clearSearchAeaHiCert();">清空</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="margin: 1px 0px;border-bottom: 1px solid #e8e8e8;"></div>
		<div class="base" data-scrollable="true" data-max-height="700" >
			<div id="aea_hi_cert_list_tb" class="m_datatable"></div>
		</div>
	</div>
</div>


<%--<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">--%>
<div class="modal fade" id="add_hi_certinst" tabindex="-1" role="dialog" aria-labelledby="add_hi_certinst_title"
	 aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered  modal-lg" role="document">

		<div class="modal-content">
			<!-- 标题 -->
			<div class="modal-header" style="padding: 15px">
				<h5 class="modal-title" id="add_hi_certinst_title">编辑电子证照实例</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>

			<form id="aea_hi_cert_form" method="post">
				<div class="m-portlet__body" style="padding: 10px 5px;">
					<div id="aeCertScrollable" class="m-scrollable" data-scrollable="true" data-max-height="550">
						<input type="hidden" name="certinstId" value=""/>
						<%--<input type="hidden" name="certId" value=""/>--%>
						<div class="form-group m-form__group row">
							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照名称:</label>
							<div class="col-lg-10">
								<input type="text" class="form-control m-input" name="certinstName" value=""/>
							</div>
						</div>

						<div class="form-group m-form__group row">
							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照编号:</label>
							<div class="col-lg-10">
								<input type="text" class="form-control m-input" name="certinstCode" value=""/>
							</div>
						</div>

						<div class="form-group m-form__group row">

							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>业主单位:</label>
							<div class="col-lg-4">
								<select type="text" class="form-control" name="unitInfoId" value=""
										onchange="getProInfos(this[selectedIndex].value)">
									<option value="">请选择</option>
									<c:forEach items="${aeaUnitInfos}" var="aeaUnitInfo">
										<option value="${aeaUnitInfo.unitInfoId}">${aeaUnitInfo.applicant}</option>
									</c:forEach>
								</select>
							</div>

							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>建设项目:</label>
							<div class="col-lg-4">
								<select type="text" class="form-control" name="projInfoId" value="" id="select">
									<option value="" id="selectOption">请选择</option>
								</select>

							</div>
						</div>

						<div class="form-group m-form__group row">
							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照类型:</label>
							<div class="col-lg-4">
								<select type="text" class="form-control" name="certId" value="">
									<option value="">请选择</option>
									<c:forEach items="${aeaCerts}" var="aeaCert">
										<option value="${aeaCert.certId}">${aeaCert.certName}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div id="showUpload" class="form-group m-form__group row">
							<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>上传文件:</label>
							<div class="col-lg-4" style="text-align: right;">
								<input type="file" id="uploadFile" name="uploadFile" class="form-control m-input"
									   placeholder="" accept="*/*" multiple="true"/>
							</div>
						</div>
					</div>
				</div>

				<div class="m-portlet__foot" style="text-align:right;padding: 6px 20px;">
					<%--<button type="submit" class="btn btn-info">保存</button>--%>
					<button type="button" class="btn btn-info" onclick="submitFormChange();">保存</button>
					<%--<button id="restAeCertBtn" type="button" class="btn btn-secondary">重置</button>--%>
					<button id="resetButton" type="button" class="btn btn-danger" onclick="reset();">重置</button>
					<%--<button type="button" class="btn btn-file" onclick="closedPage();">返回</button>--%>
						<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	$(function(){
        searchAeaHiCert()
        $('#aea_hi_cert_list').show();
	})
</script>