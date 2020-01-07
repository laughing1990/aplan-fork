<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }

    label {

        text-align: right;
    }
</style>

<!-- 新增/编辑阶段材料信息 -->
<div id="aedit_stage_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="aedit_stage_mat_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="aedit_stage_mat_modal_title" class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_stage_mat_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_stage_mat_scroll" style="height: 480px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="matId" value=""/>
                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="isStateIn" value=""/>
                        <input type="hidden" name="isActive" value=""/>
                        <input type="hidden" name="isGlobalShare" value=""/>
                        <input type="hidden" name="certId" value=""/>
                        <input type="hidden" name="stoFormId" value=""/>

                        <%@include file="../../../common/mat/mat_common_content1.jsp" %>

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
                                        onclick="editActStoFormFunc('#aedit_stage_mat_form');" style="margin-left: 3px;">编辑</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否通用材料<font color="red">*</font>:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="1" checked onclick="return false;">通用<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="0" onclick="return false;">情形<span></span>
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

                        <%@include file="../../../common/mat/mat_common_content2.jsp" %>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button id="saveStageInGlobalMatBtn" type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary" onclick="$('#aedit_stage_mat_modal').modal('hide');">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>