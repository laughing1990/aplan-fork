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

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">
                                材料类别<span style="color:red">*</span>:</label>
                            <div class="col-10 input-group">
                                <input type="hidden" name="matTypeId" value=""/>
                                <input class="form-control m-input" type="text" value=""
                                       name="matTypeName" readonly placeholder="请选择材料类别..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-mat-type">
                                        <i class="la la-search"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">标准材料:</label>
                            <div class="col-10 input-group">
                                <input type="hidden" name="stdmatId" value=""/>
                                <input type="text" class="form-control m-input" name="stdmatName" readonly placeholder="请选择标准材料..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-stdmat-type">
                                        <i class="la la-search"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料名称<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matName" placeholder="请输入材料名称...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料编号<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matCode" placeholder="请输入材料编号...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="padding-top: 8px;">材料性质<font color="red">*</font>:</label>
                            <div class="col-4">
                                <select name="matProp" class="form-control m-input">
                                    <option value="m">普通材料</option>
                                    <option value="c">证照材料</option>
                                    <option value="f">在线表单</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否批文批复<font color="red">*</font>:</label>
                            <div class="col-4">
                                <select name="isOfficialDoc" class="form-control m-input">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                        </div>

                        <div id="selectCertDiv" class="form-group m-form__group row" style="display: none;">
                            <label class="col-2 col-form-label">电子证照<span style="color:red">*</span>:</label>
                            <div class="col-10 input-group">
                                <input type="text" class="form-control m-input" name="certName" readonly placeholder="请选择电子证照..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-cert-type">
                                        <i class="la la-search"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div id="selectFormDiv" class="form-group m-form__group row" style="display: none;">
                            <label class="col-2 col-form-label">在线表单<span style="color:red">*</span>:</label>
                            <div class="col-10 input-group">
                                <input type="text" class="form-control m-input" name="formName" readonly placeholder="请选择表单..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-form-type">
                                        <i class="la la-search"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否通用材料<font color="red">*</font>:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
                                是否支持容缺<font color="red">*</font>:
                            </label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">纸质是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="1">必须
                                        <span for="inlineRadio1"></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="0">非必须
                                        <span for="inlineRadio0"></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">电子是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="1" >必须
                                        <span for="inlineRadio2"></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="0">非必须
                                        <span for="inlineRadio3"></span>
                                    </label>
                                </div>

                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">纸质材料类型:</label>
                            <div class="col-4">
                                <select name="paperMatType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">A3</option>
                                    <option value="2">A4</option>
                                    <option value="3">A5</option>
                                </select>
                            </div>
                            <div class="col-6"></div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">原件验收:</label>
                            <div class="col-4">
                                <select name="duePaperType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">原件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="duePaperCount" placeholder="请输入原件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">复印件验收类型:</label>
                            <div class="col-4">
                                <select name="dueCopyType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">复印件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="dueCopyCount" placeholder="请输入复印件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源:</label>
                            <input type="hidden" id="matFrom" name="matFrom" value=""/>
                            <div class="col-10">
                                <div class="m-checkbox-inline">
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom1" value="1" onclick="checkboxOnclick(this)">行政机关<span for="matFrom1"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom2" value="2" onclick="checkboxOnclick(this)">企事业单位<span for="matFrom2"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom3" value="3" onclick="checkboxOnclick(this)">社会组织<span for="matFrom3"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom4" value="4" onclick="checkboxOnclick(this)">申请人<span for="matFrom4"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom5" value="5" onclick="checkboxOnclick(this)">中介服务<span for="matFrom5"></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源部门:</label>
                            <div class="col-10">
                                <input id="matFromDept" class="form-control m-input" name="matFromDept"
                                       placeholder="请输入材料来源部门..." />
                            </div>
                        </div>

                        <input type="text" id="templateDoc" name="templateDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">模板文档:</label>
                            <div class="col-8">
                                <input id="templateDocFile" type="file" class="form-control m-input"
                                       name="templateDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText1">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="templateDocDiv" class="col-2">
                                <button id="templateDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('0');">查看附件</button>
                            </div>
                        </div>

                        <input type="text" id="sampleDoc" name="sampleDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">样本文档:</label>
                            <div class="col-8">
                                <input type="file"  id="sampleDocFile" class="form-control m-input"
                                       name="sampleDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                    <button class="left-button">选择文件</button>
                                    <span class="right-text" id="rightText2">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="sampleDocDiv" class="col-2">
                                <button id="sampleDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('1');">查看附件</button>
                            </div>
                        </div>

                        <input type="text" id="reviewSampleDoc" name="reviewSampleDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">审查样本:</label>
                            <div class="col-8">
                                <input id="reviewSampleDocFile" type="file" class="form-control m-input"
                                       name="reviewSampleDocFile" multiple="multiple" accept="image/*" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText3">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="reviewSampleDocDiv" class="col-2">
                                <button id="reviewSampleDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('2');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">审查要点<font color="red">*</font>:</label>
                            <div class="col-10">
                            <textarea name="reviewKeyPoints" class="form-control m-input"
                                      placeholder="请输入审查要点..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">审查依据:</label>
                            <div class="col-10">
                            <textarea name="reviewBasis" class="form-control m-input"
                                      placeholder="请输入备注..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-10">
                            <textarea name="matMemo" id="matMemo" rows="4"
                                      class="form-control m-input" placeholder="请输入备注..."></textarea>
                            </div>
                        </div>
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