<%@ page contentType="text/html;charset=UTF-8" %>
<!--不分情形材料-->
<form id="form_mat" method="post" enctype="multipart/form-data">
    <div class="m-portlet__body" id="dialog_mat_body" style="padding: 15px;max-height:800px;overflow-y:auto;overflow-x: hidden">

        <input type="hidden" id="matId" name="matId"/>
        <input type="hidden" id="itemId" name="itemId"/>
        <input type="hidden" id="isOwner" name="isOwner"/>
        <input type="hidden" id="isInput" name="isInput"/>
        <input type="hidden" id="fileType" name="fileType" value="mat"/>
        <input type="hidden" id="isGlobalShare" name="isGlobalShare" value="1" />
        <%--<input type="hidden" name="isCommon" value="1"/>--%>

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
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                原件数<font color="red"></font>:
            </label>
            <div class="col-9">
                <input class="form-control m-input" type="number" value="1" name="duePaperCount" placeholder="请输入原件数...">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                复印件数<font color="red"></font>:
            </label>
            <div class="col-9">
                <input class="form-control m-input" type="number" value="1" name="dueCopyCount" placeholder="请输入复印件数...">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                应交纸质原件份数<font color="red"></font>:
            </label>
            <div class="col-9">
                <input class="form-control m-input" type="number" value="1" name="sortNo" placeholder="请输入应交纸质原件份数...">
            </div>
        </div>

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
        <div class="form-group m-form__group row d-none">
            <label class="col-3 col-form-label" style="text-align: right;">材料要求<font color="red"></font>:</label>
            <div class="col-9"><textarea name="matRequire" class="form-control m-input" placeholder="请输入材料要求..."></textarea></div>
        </div>
        <div class="form-group m-form__group row d-none">
            <label class="col-3 col-form-label" style="text-align: right;">材料依据<font color="red"></font>:</label>
            <div class="col-9"><textarea name="matBasis" class="form-control m-input" placeholder="请输入材料依据..."></textarea></div>
        </div>
<%--        <div class="form-group m-form__group row d-none">
            <label class="col-3 col-form-label" style="text-align: right;">材料所属<font color="red"></font>:</label>
            <div class="col-9">
                <select id="matHolder" name="matHolder"  class="form-control m-input" placeholder="请选择项目类型">
                    <option value="">--请选择--</option>
                    <option value="c">企业</option>
                    <option value="u">个人</option>
                    <option value="p">工程项目</option>
                </select>
            </div>
        </div>--%>

        <%-- add by liupeng 20180830 begin --%>
        <div id="isStateInoutDiv" class="form-group m-form__group row d-none">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否为情形输入<font color="red"></font>:
            </label>
            <div class="col-9">
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
        </div>
        <div id="itemStateIdDiv" class="form-group m-form__group row d-none">
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
            <div class="col-9" id = "sampleDocFileDiv">
                <div class="custom-file" >
                    <input type="file" class="form-control m-input" id= "sampleDocFile" name="sampleDocFile" >
                </div>
            </div>

            <div id = "sampleDocButton" class = "form-group">
                <button id = "sampleDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(1);" >下载</button>
                <button id = "sampleDocDelete" type="button" class="btn btn-danger" onclick="deleteDoc(1);" >删除</button>
            </div>
        </div>


        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">模板文档:</label>
            <input type="text" id="templateDoc" name="templateDoc" class="hide"/>
            <div class="col-9" id = "templateDocFileDiv">
                <div class="custom-file" >
                    <input type="file" class="form-control m-input" id = "templateDocFile" name="templateDocFile" >
                </div>
            </div>

            <div id = "templateDocButton" class = "form-group ">
                <button id = "templateDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(0);">下载</button>
                <button id = "templateDocDelete" type="button" class="btn btn-danger" onclick="deleteDoc(0);">删除</button>
            </div>
        </div>

        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">审查样本:</label>
            <input type="text" id="reviewSampleDoc" name="reviewSampleDoc" class="hide"/>
            <div class="col-9" id = "reviewSampleDocFileDiv">
                <div class="custom-file">
                    <input type="file" class="form-control m-input" id="reviewSampleDocFile" name="reviewSampleDocFile">
                </div>
            </div>
            <div id = "reviewSampleDocButton" class = "form-group">
                <button id = "reviewSampleDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(2);" >下载</button>
                <button id = "reviewSampleDocDelete" type="button" class="btn btn-danger" onclick="deleteDoc(2);">删除</button>
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
                审查要点:
            </label>
            <div class="col-9">
                <textarea name="reviewKeyPoints" class="form-control m-input" placeholder="请输入审查要点..."></textarea>
            </div>
        </div>

<%--        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否批文<font color="red"></font>:
            </label>
            <div class="col-9">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="receiveMode" value="1">批文类
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="receiveMode" value="0" checked>非批文类
                        <span></span>
                    </label>
                </div>
            </div>
        </div>--%>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                纸质材料是否必须<font color="red"></font>:
            </label>
            <div class="col-9">
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
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                电子材料是否必须<font color="red"></font>:
            </label>
            <div class="col-9">
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
        <div class="form-group m-form__group row" style="display: none;">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否通用材料:
            </label>
            <div class="col-9">
                <div class="m-radio-inline">
                    <label class="m-radio">
                        <input type="radio" name="isCommon" value="1" checked>
                        是
                        <span></span>
                    </label>
                    <label class="m-radio">
                        <input type="radio" name="isCommon" value="0">
                        否
                        <span></span>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-3 col-form-label" style="text-align: right;">
                是否启用<font color="red"></font>:
            </label>
            <div class="col-9">
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
        <button id="btn_back_mat_global" type="button" class="btn btn-secondary hid" onclick="goSetp('mat_global')" >返回</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">
            关闭
        </button>
    </div>
</form>