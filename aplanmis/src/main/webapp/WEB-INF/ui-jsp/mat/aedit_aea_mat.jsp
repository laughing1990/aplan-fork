<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 新增/编辑全局材料弹窗 -->
<div id="aedit_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_mat_global_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="aedit_mat_modal_title">编辑事项材料</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_mat_form" method="post" enctype="multipart/form-data">
                <div class="m-portlet__body" id="aedit_mat_global_body"
                     style="padding: 15px;max-height: 500px;overflow-y:auto;overflow-x: hidden">

                    <input type="hidden" name="matId" value=""/>
                    <input type="hidden" name="isGlobalShare" value="" />
                    <input type="hidden" name="isActive" value=""/>
                    <input type="hidden" name="certId" value=""/>
                    <input type="hidden" name="stoFormId" value=""/>

                    <%@include file="../common/mat/mat_common_content1.jsp" %>

                    <div id="selectFormDiv" class="form-group m-form__group row" style="display: none;">
                        <label class="col-2 col-form-label">在线表单<span style="color:red">*</span>:</label>
                        <div class="col-10 input-group">
                            <input type="text" class="form-control m-input" name="formName" readonly placeholder="请选择表单..." >
                            <div class="input-group-append">
                                <span class="input-group-text open-form-type">
                                    <i class="la la-search"></i>
                                </span>
                            </div>
                            <button id="editActStoFormBtn" type="button" class="btn btn-info"
                                    onclick="editActStoFormFunc('#aedit_mat_form');" style="margin-left: 3px;">编辑</button>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
                            是否通用材料<font color="red">*</font>:
                        </label>
                        <div class="col-4">
                            <div class="m-radio-inline">
                                <label class="m-radio">
                                    <input type="radio" name="isCommon" value="1" checked>通用<span></span>
                                </label>
                                <label class="m-radio">
                                    <input type="radio" name="isCommon" value="0">情形<span></span>
                                </label>
                            </div>
                        </div>

                        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
                            是否支持容缺<font color="red">*</font>:
                        </label>
                        <div class="col-4">
                            <div class="m-radio-inline">
                                <label class="m-radio">
                                    <input type="radio" name="zcqy" value="1" checked>支持<span></span>
                                </label>
                                <label class="m-radio">
                                    <input type="radio" name="zcqy" value="0">不支持<span></span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <%@include file="../common/mat/mat_common_content2.jsp" %>

                </div>

                <!-- 按钮区域 -->
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="saveMatStoreBtn" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>

