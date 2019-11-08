<%@ page contentType="text/html;charset=UTF-8" %>
<form id="form_mat" method="post" enctype="multipart/form-data">
    <div class="m-portlet__body" id="dialog_mat_body" style="padding: 15px;max-height:450px;overflow-y:auto;overflow-x: hidden">

        <input type="hidden" id="matId" name="matId"/>
        <input type="hidden" id="stageId" name="stageId"/>
        <input type="hidden" id="isOwner" name="isOwner"/>
        <input type="hidden" id="isInput" name="isInput"/>
        <input type="hidden" id="fileType" name="fileType" value="mat"/>
        <input type="hidden" id="isGlobalShare" name="isGlobalShare" value="1" />
        <%--<input type="hidden" id="isCommon" name="isCommon" value="1" />--%>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                材料类别<font color="red"></font>:
            </label>
            <div class="col-9 input-group">
                <input type="hidden" id="matTypeId" name="matTypeId" value=""/>
                <input type="text" class="form-control m-input" name="matTypeName" readonly placeholder="请选择材料类别..." >
                <div class="input-group-append">
                    <span class="input-group-text open-mat-type"><i class="la la-tag"></i></span>
                </div>
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label for="matName" class="col-3 col-form-label" style="text-align: right;">
                <font color="red">*</font>材料名称:
            </label>
            <div class="col-9">
                <input class="form-control m-input" type="text" value="" id="matName" name="matName" placeholder="请输入材料名称...">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label for="matCode" class="col-3 col-form-label" style="text-align: right;">
                <font color="red">*</font>材料编号:
            </label>
            <div class="col-9">
                <input class="form-control m-input" type="text" value="" id="matCode" name="matCode" placeholder="请输入材料编号...">
            </div>
        </div>
        <%--<div class="form-group m-form__group row">--%>
            <%--<label class="col-3 col-form-label" style="text-align: right;">--%>
                <%--应交纸质原件份数<font color="red"></font>:--%>
            <%--</label>--%>
            <%--<div class="col-9">--%>
                <%--<input class="form-control m-input" type="number" value="1" name="sortNo" placeholder="请输入应交纸质原件份数...">--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="form-group m-form__group row">
            <label class="col-lg-3 col-form-label" style="text-align: right;" >材料来源:</label>
            <!--yjy2018-->
            <div class="col-9">
                <div class="m-checkbox-inline">
                    <label class="m-checkbox"><input type="checkbox" name="matFrom" value="1">行政机关<span for="matFrom"></span></label>
                    <label class="m-checkbox"><input type="checkbox" name="matFrom" value="2">企事业单位<span for="matFrom"></span></label>
                    <label class="m-checkbox"><input type="checkbox" name="matFrom" value="3">社会组织<span for="matFrom"></span></label>
                    <label class="m-checkbox"><input type="checkbox" name="matFrom" value="4">申请人<span for="matFrom"></span></label>
                    <label class="m-checkbox"><input type="checkbox" name="matFrom" value="5">中介服务<span for="matFrom"></span></label>
                </div>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                材料来源部门:
            </label>
            <div class="col-9">
                <textarea name="matFromDept" class="form-control m-input" placeholder="请输入材料来源部门..."></textarea>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">材料要求<font color="red"></font>:</label>
            <div class="col-9"><textarea name="matRequire" class="form-control m-input" placeholder="请输入材料要求..."></textarea></div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">材料依据<font color="red"></font>:</label>
            <div class="col-9"><textarea name="matBasis" class="form-control m-input" placeholder="请输入材料依据..."></textarea></div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">材料所属<font color="red"></font>:</label>
            <div class="col-9">
                <select id="matHolder" name="matHolder"  class="form-control m-input" placeholder="请选择项目类型">
                    <option value="">--请选择--</option>
                    <option value="c">企业</option>
                    <option value="u">个人</option>
                    <option value="p">工程项目</option>
                </select>
            </div>
        </div>

        <%-- add by liupeng 20180830 begin --%>
        <div id="isStateInoutDiv" class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否为情形输入<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="isStateIn" value="1">是
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="isStateIn" value="0" checked>不是
                        <span></span>
                    </label>
                </div>
            </div>


            <label class="col-3 col-form-label" style="text-align: right;">
                是否通用材料<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio m-radio--primary">
                        <input type="radio" name="isCommon" value="1" checked>
                        是
                        <span></span>
                    </label>
                    <label class="m-radio m-radio--primary">
                        <input type="radio" name="isCommon" value="0">
                        否
                        <span></span>
                    </label>
                </div>
            </div>
        </div>
        <div id="itemStateIdDiv" class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">事项情形<font color="red"></font>:</label>
            <div class="col-9">
                <select id="itemStateId" name="itemStateId" class="form-control m-input" placeholder="请选择事项情形">
                    <option value="">--请选择--</option>
                </select>
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">样本文档:</label>
            <input type="text" id="sampleDoc" name="sampleDoc" class="hide"/>
            <div class="col-5" id = "sampleDocFileDiv">
                <div class="custom-file" >
                    <input type="file" class="form-control m-input" id= "sampleDocFile" name="sampleDocFile" multiple="multiple" />
                </div>
            </div>
            <div id="sampleDocDiv" class="col-2">
                <button id="sampleDocButton" type="button" class="btn btn-info" onclick="showMatAtt('1');">查看附件</button>
            </div>
        </div>


        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">模板文档:</label>
            <input type="text" id="templateDoc" name="templateDoc" class="hide"/>
            <div class="col-5" id = "templateDocFileDiv">
                <div class="custom-file" >
                    <input type="file" class="form-control m-input" id = "templateDocFile" name="templateDocFile" multiple="multiple" />
                </div>
            </div>

            <div id="templateDocDiv" class="col-2">
                <button id="templateDocButton" type="button" class="btn btn-info" onclick="showMatAtt('0');">查看附件</button>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">审查样本:</label>
            <input type="text" id="reviewSampleDoc" name="reviewSampleDoc" class="hide"/>
            <div class="col-5" id = "reviewSampleDocFileDiv">
                <div class="custom-file">
                    <input type="file" class="form-control m-input" id="reviewSampleDocFile" name="reviewSampleDocFile" multiple="multiple" accept="image/*" />
                </div>
            </div>

            <div id="reviewSampleDocDiv" class="col-2">
                <button id="reviewSampleDocButton" type="button" class="btn btn-info" onclick="showMatAtt('2');">查看附件</button>
            </div>
        </div>


        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                <font color="red">*</font>审查要点:
            </label>
            <div class="col-9">
                <textarea name="reviewKeyPoints" class="form-control m-input" placeholder="请输入备注..."></textarea>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                审查依据:
            </label>
            <div class="col-9">
                <textarea name="reviewBasis" class="form-control m-input" placeholder="请输入备注..."></textarea>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否支持容缺<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio m-radio--primary">
                        <input type="radio" name="zcqy" value="1" checked>
                        是
                        <span></span>
                    </label>
                    <label class="m-radio m-radio--primary">
                        <input type="radio" name="zcqy" value="0">
                        否
                        <span></span>
                    </label>
                </div>
            </div>


            <label class="col-3 col-form-label" style="text-align: right;">
                是否启用<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="isActive" value="1" checked>
                        启用
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="isActive" value="0">
                        禁用
                        <span></span>
                    </label>
                </div>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                纸质是否必须<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="paperIsRequire" value="1" checked>
                        必须
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="paperIsRequire" value="0">
                        非必须
                        <span></span>
                    </label>
                </div>
            </div>
            <label class="col-3 col-form-label" style="text-align: right;">
                电子是否必须<font color="red"></font>:
            </label>
            <div class="col-3">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="attIsRequire" value="1" checked>
                        必须
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="attIsRequire" value="0">
                        非必须
                        <span></span>
                    </label>
                </div>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">纸质材料类型:</label>
            <div class="col-4">
                <select name="paperMatType" id="paperMatType"  class="form-control m-input">
                    <option value="">请选择</option>
                    <option value="0">无</option>
                    <option value="1">A3</option>
                    <option value="2">A4</option>
                    <option value="3">A5</option>
                </select>
            </div>
        </div>


        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">原件验收:</label>
            <div class="col-3">
                <select name="duePaperType" id="duePaperType"  class="form-control m-input">
                    <option value="">请选择</option>
                    <option value="0">无</option>
                    <option value="1">验</option>
                    <option value="2">收</option>
                </select>
            </div>

            <label class="col-3 col-form-label" style="text-align: right;">
                原件数<font color="red"></font>:
            </label>
            <div class="col-3">
                <input class="form-control m-input" type="number" value="1" name="duePaperCount" placeholder="请输入原件数...">
            </div>
        </div>


        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">复印件验收类型:</label>
            <div class="col-3">
                <select name="dueCopyType" id="dueCopyType"  class="form-control m-input">
                    <option value="">请选择</option>
                    <option value="0">无</option>
                    <option value="1">验</option>
                    <option value="2">收</option>
                </select>
            </div>

            <label class="col-3 col-form-label" style="text-align: right;">
                复印件数<font color="red"></font>:
            </label>
            <div class="col-3">
                <input class="form-control m-input" type="number" value="1" name="dueCopyCount" placeholder="请输入复印件数...">
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                备注:
            </label>
            <div class="col-9">
                <textarea name="matMemo" class="form-control m-input" placeholder="请输入备注..."></textarea>
            </div>
        </div>

    </div>
    <div class="modal-footer" style="padding: 15px">
        <button type="submit" id="btn_save" class="btn btn-info">
            保存
        </button>
        <%--<button id="btn_back_mat_global" type="button" class="btn btn-secondary hid " onclick="goSetp('mat_global')" >返回</button>--%>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">
            关闭
        </button>
    </div>
</form>

<%@include file="../itemMatAttModal.jsp" %>
