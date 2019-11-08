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

				</h3>
			</div>
		</div>
	</div>
	<form id="aea_hi_cert_form" method="post">
		<div class="m-portlet__head">
			<div class="col-md-6" style="text-align: left;">
				<button type="button" class="btn btn-file" onclick="closedPage();">返回</button>
			</div>
		</div>
		<div class="m-portlet__body" style="padding: 10px 5px;">
			<div id="aeCertScrollable" class="m-scrollable" data-scrollable="true" data-max-height="550">
				<input type="hidden" name="certinstId" value=""/>
				<%--<input type="hidden" name="certId" value=""/>--%>
				<div class="form-group m-form__group row" >
					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照名称:</label>
					<div class="col-lg-10">
						<input type="text" class="form-control m-input" name="certinstName" value=""/>
					</div>
				</div>

				<div class="form-group m-form__group row" >
					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照编号:</label>
					<div class="col-lg-10">
						<input type="text" class="form-control m-input" name="certinstCode" value=""/>
					</div>
				</div>

				<div class="form-group m-form__group row" >

					<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>业主单位:</label>
					<div class="col-lg-4">
						<select type="text" class="form-control" name="unitInfoId" value="" onchange="getProInfos(this[selectedIndex].value)">
							<option value="" >请选择</option>
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

				<div class="form-group m-form__group row" >
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
                    <label class="col-lg-2 col-form-label" style="text-align: right;">上传文件:</label>
                    <div class="col-lg-2" style="text-align: right;">
                        <input type="file" id="uploadFile" name="uploadFile" class="form-control m-input" placeholder="" accept="*/*"   multiple="true" />
                    </div>
                 </div>
				</div>
		</div>

		<div class="m-portlet__foot" style="text-align:right;padding: 6px 20px;">
			<%--<button type="submit" class="btn btn-info">保存</button>--%>
			<button type="button" class="btn btn-info" onclick="submitFormChange();">保存</button>
			<%--<button id="restAeCertBtn" type="button" class="btn btn-secondary">重置</button>--%>
			<button id="resetButton" type="button"  class="btn btn-danger" onclick="reset();">重置</button>
			<%--<button type="button" class="btn btn-file" onclick="closedPage();">返回</button>--%>
		</div>
	</form>
</div>


<script>

    function reset() {
        $('#aea_hi_cert_form')[0].reset();
        aeCertValidator.resetForm();
    }
</script>