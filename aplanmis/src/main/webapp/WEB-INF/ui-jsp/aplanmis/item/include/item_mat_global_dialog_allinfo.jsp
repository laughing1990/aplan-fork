<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 新增信息 -->
<div class="modal fade" id="dialog_mat_global" tabindex="-1" role="dialog" aria-labelledby="dialog_mat_global_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_mat_global_title">编辑事项材料</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="form_mat_global" method="post" enctype="multipart/form-data">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                        <div class="m-portlet__body" id="dialog_mat_global_body" style="padding: 15px;max-height:400px;overflow-y:auto;overflow-x: hidden">

                            <input type="hidden" id="matTypeId" name="matTypeId"/>
                            <input type="hidden" name="isGlobalShare" value="1"/>
<%--
                            <input type="hidden" name="isCommon" value="1"/>
--%>

                            <div class="form-group m-form__group row">
                                <label for="typeName" class="col-3 col-form-label">
                                    材料类型名称<font color="red">*</font>:
                                </label>
                                <div class="col-9">
                                    <input class="form-control m-input" type="text" value="" id="typeName" name="typeName" placeholder="请输入材料类型名称...">
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label for="typeCode" class="col-3 col-form-label">
                                    材料类型编号<font color="red">*</font>:
                                </label>
                                <div class="col-9">
                                    <input class="form-control m-input" type="text" value="" id="typeCode" name="typeCode" placeholder="请输入材料类型编号...">
                                </div>
                            </div>
<%--                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    是否批文<font color="red"></font>:
                                </label>
                                <div class="col-9">
                                    <select id="isOfficialDoc" name="isOfficialDoc"  class="form-control m-input" placeholder="是否批文">
                                        <option value="">--请选择--</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>--%>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    是否基础材料<font color="red"></font>:
                                </label>
                                <div class="col-9">
                                    <select id="isBasic" name="isBasic"  class="form-control m-input" placeholder="是否基础材料">
                                        <option value="">--请选择--</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    排列序列号<font color="red"></font>:
                                </label>
                                <div class="col-9">
                                    <input class="form-control m-input" type="number" value="1" id="sortNo" name="sortNo" placeholder="排列序列号">
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">父ID<font color="red"></font>:</label>
                                <div class="col-9 input-group">
                                    <input type="text" class="form-control m-input" value="" id="chooseParentTypeId" name="parentTypeId" readonly placeholder="请选择父ID..." >
                                    <div class="input-group-append">
                                        <span class="input-group-text open-mat-type" onclick="chooseParent();"><i class="la la-tag"></i></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">序列<font color="red"></font>:</label>
                                <div class="col-9">
                                    <input class="form-control m-input" type="text" value="" id="typeSeq" name="typeSeq" placeholder="请输入序列...">
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">子节点数<font color="red"></font>:</label>
                                <div class="col-9">
                                    <input class="form-control m-input" type="number" value="" name="subCount" placeholder="子节点数">
                                </div>
                            </div>
                            <%--<div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">样本文档<font color="red"></font>:</label>
                                <div class="col-9">
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" name="sampleDoc" accept="image/gif,image/jpeg,image/jpg,image/png,image/svg">
                                        <label class="custom-file-label" id="sampleDocLabel">
                                            请选择样本文档...
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">模板文档<font color="red">*</font>:</label>
                                <div class="col-9">
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" name="templateDoc" accept="image/gif,image/jpeg,image/jpg,image/png,image/svg">
                                        <label class="custom-file-label" id="templateDocLabel">
                                            请选择模板文档...
                                        </label>
                                    </div>
                                </div>
                            </div>--%>
                            <%--<div class="form-group m-form__group row">
                                <label for="attDirId" class="col-3 col-form-label">
                                    所属文件库<font color="red">*</font>:
                                </label>
                                <div class="col-9">
                                    <select class="form-control m-bootstrap-select m_selectpicker" id="attDirId" name="attDirId"></select>
                                </div>
                            </div>--%>
                            <%--<div id="container_is_global_share" class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    是否全局<font color="red">*</font>:
                                </label>
                                <div class="col-9">
                                    <div class="m-radio-inline" id="isGlobalShare">
                                        <label class="m-radio">
                                            <input type="radio" name="isGlobalShare" value="1">全局材料
                                            <span></span>
                                        </label>
                                        <label class="m-radio">
                                            <input type="radio" name="isGlobalShare" value="0" checked>非全局材料
                                            <span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
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
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    是否必须<font color="red"></font>:
                                </label>
                                <div class="col-9">
                                    <div class="m-radio-inline">
                                        <label class="m-radio">
                                            <input type="radio" name="isRequire" value="1" checked>
                                            必须
                                            <span></span>
                                        </label>
                                        <label class="m-radio">
                                            <input type="radio" name="isRequire" value="0">
                                            非必须
                                            <span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
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
                            </div>--%>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    备注说明:
                                </label>
                                <div class="col-9">
                                    <textarea name="typeMemo" class="form-control m-input" placeholder="请输入备注..."></textarea>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-3 col-form-label">
                                    是否启用<font color="red"></font>:
                                </label>
                                <div class="col-9">
                                    <select id="isActive" name="isActive"  class="form-control m-input" placeholder="是否启用">
                                        <option value="">--请选择--</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="btn_save" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="select_parent_mat_type" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_cert_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_cert_ztree_modal_title">选择父ID</h5>
                <button type="button" class="close" aria-label="Close" onclick="parentDialogClose();" >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="certTreeDiv" class="m-portlet__body" style="max-height:450px;height:450px;">
                <div style="width: 100%;height: 100%;">
                    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                        <div class="col-xl-12" style="max-height: 420px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectCertKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info" onclick="searchSelectCertNode();">查询</button>
                                            <button type="button" class="btn btn-danger" onclick="clearSearchSelectCertNode();">清空</button>
                                            <button type="button" class="btn btn-secondary" onclick="expandSelectCertAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary" onclick="collapseSelectCertAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="350">
                                        <div id="selectParentMatTypeTree" class="ztree" style="overflow-y:auto;overflow-x:auto;max-height: 280px;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>