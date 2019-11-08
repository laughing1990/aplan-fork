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
				<h3 id="ae_cert_type_header" class="m-portlet__head-text">
					新增证照分类
				</h3>
			</div>
		</div>
	</div>

	<form id="ae_cert_type_form" method="post">
		<div class="modal-body" style="padding: 10px;">
			<div id="ae_cert_type_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

				<input type="hidden" name="certTypeId" value=""/>
				<input type="hidden" name="parentTypeId" value=""/>
				<input type="hidden" name="isTbEditNode" value=""/>

				<div class="form-group m-form__group row">
					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;类型名称：</label>
					<div class="col-lg-10">
						<input type="text" class="form-control m-input" name="typeName" value=""  placeholder=""/>
					</div>
				</div>

				<div class="form-group m-form__group row">
					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;类型编号：</label>
					<div class="col-lg-10">
						<input type="text" class="form-control m-input" name="typeCode" value=""  placeholder=""/>
					</div>
				</div>

				<div class="form-group m-form__group row">
					<%--<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;是否有效:</label>--%>
					<%--<div class="col-lg-4">--%>
						<%--<select type="text" class="form-control" name="isActive" value="">--%>
							<%--<option value="">请选择</option>--%>
							<%--<option value="1">有效</option>--%>
							<%--<option value="0">无效</option>--%>
						<%--</select>--%>
					<%--</div>--%>
					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;排序:</label>
					<div class="col-lg-10">
						<input type="number" class="form-control m-input" name="sortNo" value="1"/>
					</div>
				</div>

				<div class="form-group m-form__group row" >
					<label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
					<div class="col-lg-10">
						<textarea class="form-control" name="typeMemo" rows="3"></textarea>
					</div>
				</div>

				<div class="form-group m-form__group row" >
					<div class="col-lg-12" style="text-align: center;">
						<button type="submit" class="btn btn-info">保存</button>&nbsp;
						<button id="restAeCertTypeBtn" type="button" class="btn btn-secondary">关闭</button>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>