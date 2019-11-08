<%@ page contentType="text/html;charset=UTF-8" %>
<div id="certinst_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="certinst_modal_title">证照实例详情</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="certinst_form" onsubmit="return false" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <input id="serviceId" type="hidden" name="serviceId" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证书名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="certinstName"  name="certinstName" value=""  placeholder="" readonly="readonly"/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证书编码：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="certinstCode"  name="certinstCode" value=""  placeholder="" readonly="readonly"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>有效期：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="_time"  name="_time" value=""  placeholder="" readonly="readonly"/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>所属专业：</label>
                        <div class="col-lg-10" id="majorTree">
                            <ul id="treeDemo" class="ztree"></ul> <%--<input type="text" class="form-control m-input" id="majors"  name="majors" value=""  placeholder="" readonly="readonly"/>--%>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>等级：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" id="qualLevelName"  name="qualLevelName" value=""  placeholder="" readonly="readonly"/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>附件列表：</label>
                        <div class="col-lg-10">
                            <%--<input type="text" class="form-control m-input" id="attrs"  name="attrs" value=""  placeholder="" readonly="readonly"/>--%>
                        <%--附件li--%>
                                <ul class="list-group" id="attrs">

                                </ul>
                        </div>
                    </div>


                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="button" onclick="closeCertinstModal();" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>