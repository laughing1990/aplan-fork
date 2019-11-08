<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 添加/编辑 -->
<div id="add_item_priv_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 700px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_priv_modal_title">新增事项</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_item_priv_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="add_item_priv_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="itemVerId" id="itemVerId" value=""/>
                        <input type="hidden" name="itemVerIds" id="itemVerIds" value=""/>
                        <input type="hidden" id="orgId" name="orgId" value=""/>
                        <input type="hidden" id="regionId" name="regionId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>行政区域:</label>
                            <div class="col-lg-9 input-group">
                                <input type="text" class="form-control m-input" name="regionName"
                                       placeholder="请点击选择" aria-describedby="select_Region_Id" readonly
                                       onclick="isSelectRegion(this, false);">
                                <div class="input-group-append">
                                    <span id="select_Region_Id" class="input-group-text"
                                          onclick="isSelectRegion(null, false);">
                                        <i class="la la-group"></i>
                                    </span>
                                </div>
                            </div>

                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>所属部门:</label>
                            <div class="col-lg-9 input-group">
                                <input type="text" class="form-control m-input" name="orgName"
                                       placeholder="请点击选择" aria-describedby="select_org_Id" readonly
                                       onclick="isSelectPrivOpuOmOrg(this, false);">
                                <div class="input-group-append">
                                    <span id="select_org_Id" class="input-group-text"
                                          onclick="isSelectPrivOpuOmOrg(null, false);">
                                        <i class="la la-group"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="isActive" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>启用EL表达式:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="useEl" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                        </div>

                        <div class="form-group m-form__group row" id="elText" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>填写EL表达式:</label>
                            <div class="col-lg-9">
                               <textarea name="elExpr" class="form-control m-input" rows="5" style="width:100%; height:150px"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否允许人工选&nbsp;</br>择下级承办组织:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="allowManual" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>

                        </div>


                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveItemPriv">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>