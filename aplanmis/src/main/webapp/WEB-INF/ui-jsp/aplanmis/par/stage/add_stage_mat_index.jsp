<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
    #add_stage_mat_scroll::-webkit-scrollbar{
        width:4px;
        height:4px;
    }
    #add_stage_mat_scroll::-webkit-scrollbar-track{
        background: #fff;
        border-radius: 2px;
    }
    #add_stage_mat_scroll::-webkit-scrollbar-thumb{
        background: #e2e5ec;
        border-radius:2px;
    }
    #add_stage_mat_scroll::-webkit-scrollbar-thumb:hover{
        background: #aaa;
    }
    #add_stage_mat_scroll::-webkit-scrollbar-corner{
        background: #fff;
    }
</style>

<!-- 新增材料 -->
<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
                <h3 id="add_stage_mat_header" class="m-portlet__head-text">
                    新增材料
                </h3>
            </div>
        </div>
    </div>
    <form id="add_stage_mat_form" method="post" enctype="multipart/form-data">
        <div class="m-portlet__body" style="padding: 10px 5px;">
            <div id="add_stage_mat_scroll" style="height: 685px;overflow-x: hidden;overflow-y: auto;">

                <input type="hidden" name="inId" value=""/>
                <input type="hidden" name="stageId" value=""/>
                <input type="hidden" name="stageStateId" value=""/>
                <input type="hidden" name="parentId" value=""/>
                <input type="hidden" name="isTbEditMatNode" value=""/>

                <input type="hidden" name="matId" value=""/>
                <input type="hidden" name="isActive" value=""/>

                <input type="hidden" name="isGlobalShare" value="1"/>
                <%--<input type="hidden" name="isCommon" value="1"/>--%>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label"
                           style="text-align: right;" >材料类别<span style="color:red">*</span></label>
                    <div class="col-lg-4 input-group">
                        <input type="hidden" name="matTypeId" value=""/>
                        <input type="text" class="form-control m-input"
                               name="matTypeName" readonly placeholder="请选择材料类别..." >
                        <div class="input-group-append" onclick="openSelectMatTypeModal('noModal');">
                            <span class="input-group-text open-mat-type"><i class="la la-tag"></i></span>
                        </div>
                    </div>

                    <%--<label class="col-lg-2 col-form-label"
                           style="text-align: right;" >是否全局材料<span style="color:red">*</span>:</label>
                    <div class="col-lg-4">
                        <select id="isGlobalShare" class="form-control" type="text" value="" name="isGlobalShare">
                            <option value="0">专有</option>
                            <option value="1">全局</option>
                        </select>
                    </div>--%>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label"
                           style="text-align: right;" >材料名称<span style="color:red">*</span>:</label>
                    <div class="col-lg-10">
                        <input class="form-control m-input" type="text" value=""
                               name="matName" placeholder="请输入材料名称...">
                    </div>

                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label"
                           style="text-align: right;" >材料编号:<span style="color:red">*</span></label>
                    <div class="col-lg-4">
                        <input class="form-control m-input" type="text" value=""
                               id="matCode" name="matCode" placeholder="请输入材料编号...">
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">原件数:</label>
                    <div class="col-lg-4">
                        <input class="form-control m-input" type="number" value="1"
                               id="duePaperCount" name="duePaperCount" placeholder="请输入原件数...">
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >复印件数:</label>
                    <div class="col-lg-4">
                        <input class="form-control m-input" type="number" value="1"
                               id="dueCopyCount" name="dueCopyCount" placeholder="请输入复印件数...">
                    </div>
                    <label class="col-lg-2 col-form-label"  style="text-align: right;" >纸质原件数:</label>
                    <div class="col-lg-4">
                        <input class="form-control m-input" type="number" value="1"
                               id="sortNo" name="sortNo" placeholder="请输入应交纸质原件份数...">
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >材料来源:</label>
                    <div class="col-lg-10">
                        <textarea name="matFrom" id="matFrom"
                                  class="form-control m-input" placeholder="请输入材料来源..."></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >材料要求:</label>
                    <div class="col-lg-10">
                        <textarea name="matRequire" id="matRequire" class="form-control m-input"
                                  placeholder="请输入材料要求..."></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >材料依据:</label>
                    <div class="col-lg-10">
                        <textarea name="matBasis" id="matBasis" class="form-control m-input"
                                  placeholder="请输入材料依据..."></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >材料所属:</label>
                    <div class="col-lg-4">
                        <select name="matHolder"  class="form-control m-input" placeholder="请选择项目类型">
                            <option value="">--请选择--</option>
                            <option value="c">企业</option>
                            <option value="u">个人</option>
                            <option value="p">工程项目</option>
                        </select>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;" >是否批文:</label>
                    <div class="col-lg-4">
                        <select name="receiveMode"  class="form-control m-input" placeholder="请选择是否批文">
                            <option value="">--请选择--</option>
                            <option value="1">批文类</option>
                            <option value="0">非批文类</option>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label"  style="text-align: right;" >纸质是否必需:</label>
                    <div class="col-lg-4">
                        <select name="paperIsRequire" class="form-control m-input">
                            <option value="">--请选择--</option>
                            <option value="0">非必须</option>
                            <option value="1">必须</option>
                        </select>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;" >电子是否必需:</label>
                    <div class="col-lg-4">
                        <select name="attIsRequire" class="form-control m-input">
                            <option value="">--请选择--</option>
                            <option value="0">非必须</option>
                            <option value="1">必须</option>
                        </select>
                    </div>
                </div>

                <input type="text" id="templateDoc" name="templateDoc" class="hide"/>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label"  style="text-align: right;" >模板文档:</label>
                    <input type="file" id="templateDocFile" name="templateDocFile" multiple="multiple" />
                    <div id="templateDocButton" class = "form-group ">
                        <button id="templateDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(0);">下载</button>
                        <button id="templateDocDelete" type="button" class="btn btn-danger" onclick="deleteDoc(0);">删除</button>
                    </div>
                </div>

                <input type="text" id="sampleDoc" name="sampleDoc" class="hide"/>
                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >样本文档:</label>
                    <input type="file" id="sampleDocFile" name="sampleDocFile" multiple="multiple" />
                    <div id="sampleDocButton" class="form-group ">
                        <button id="sampleDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(1);">下载</button>
                        <button id="sampleDocDelete" type="button" class="btn btn-danger" onclick="deleteDoc(1);">删除</button>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;" >备注:</label>
                    <div class="col-lg-10">
                        <textarea name="matMemo" id="matMemo" class="form-control m-input"
                                  rows="4" placeholder="请输入备注..."></textarea>
                    </div>
                </div>

            </div>
        </div>

        <div class="m-portlet__foot" style="padding: 6px 20px;text-align: center;">
            <button type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-secondary">关闭</button>
        </div>
    </form>
</div>