<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
	.row{
		margin-left: 0px;
		margin-right: 0px;
	}
</style>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
	<div class="m-portlet__head">
		<div class="m-portlet__head-caption">
			<div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
				<h3 id="ae_cert_header" class="m-portlet__head-text">
					新增法律法规
				</h3>
			</div>
		</div>
	</div>

	<form id="form_mat_global" method="post" enctype="multipart/form-data">
		<div class="m-portlet__body" style="padding: 10px 0px;">
			<div class="m-portlet__body" id="dialog_mat_global_body" style="padding: 15px;max-height:600px;overflow-y:none;overflow-x: hidden">
				<input type="hidden" id="matTypeId" name="matTypeId"/>
				<div class="form-group m-form__group row">
					<label for="legalName" class="col-2 col-form-label text-right">
						法律法规名称<font color="red">*</font>:
					</label>
					<div class="col-10">
						<input class="form-control m-input" type="text" value="" id="legalName" name="legalName" placeholder="请输入法律法规名称...">
					</div>
				</div>
				<div class="form-group m-form__group row">
					<label for="legalLevel" class="col-2 col-form-label text-right">
						法律法规层级<font color="red">*</font>:
					</label>
					<div class="col-10">
						<input class="form-control m-input" type="text" value="" id="legalLevel" name="legalLevel" placeholder="请输入法律法规层级...">
					</div>
				</div>
				<div class="form-group m-form__group row">
					<label for="basicNo" class="col-2 col-form-label text-right">
						依据文号<font color="red">*</font>:
					</label>
					<div class="col-10">
						<input class="form-control m-input" type="text" value="" id="basicNo" name="basicNo" placeholder="请输入依据文号...">
					</div>
				</div>
				<div class="form-group m-form__group row">
					<label for="issueOrg" class="col-2 col-form-label text-right">
						颁发机构<font color="red">*</font>:
					</label>
					<div class="col-10">
						<input class="form-control m-input" type="text" value="" id="issueOrg" name="issueOrg" placeholder="请输入颁发机构...">
					</div>
				</div>
				<div class="form-group m-form__group row">
					<label for="exeDate" class="col-2 col-form-label text-right">
						开始实施的日期<font color="red">*</font>:
					</label>
					<div class="col-10">
						<input class="form-control date-picker" id="exeDate" type="text" placeholder="开始实施的日期">
					</div>
				</div>

				<div class="form-group m-form__group row">
					<label class="col-2 col-form-label text-right">
						备注说明:
					</label>
					<div class="col-10">
						<textarea name="typeMemo" class="form-control m-input" placeholder="请输入备注..."></textarea>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer" style="padding: 10px;height: 60px;">
			<button id="btn_save" type="submit" class="btn btn-info">保存</button>
			<button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="showContentContainer('container_legal_def_list');">关闭</button>
		</div>
	</form>
</div>