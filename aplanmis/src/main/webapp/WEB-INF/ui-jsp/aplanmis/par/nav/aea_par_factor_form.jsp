<%@ page contentType="text/html;charset=UTF-8" %>


<div id="add_factor_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_factor_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_factor_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_par_factor_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <input type="hidden" name="factorId" value=""/>
                    <input type="hidden" name="navId" value=""/>
                    <input type="hidden" name="themeId" value=""/>
                    <input type="hidden" name="parentFactorId" value=""/>
                    <input type="hidden" name="isQuestion" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="factorName" value=""/>
                        </div>
                    </div>

                    <div id="themeNameDiv" class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">选择主题:</label>
                        <div class="col-lg-10 input-group">
                            <input type="text" class="form-control m-input" name="themeName"
                                   placeholder="请点击选择" aria-describedby="select_org_Id2" readonly
                                   onclick="selectParThemeZtree();">
                            <div class="input-group-append">
                                <span id="select_org_Id2" class="input-group-text"
                                      onclick="selectParThemeZtree();"><i class="la la-search"></i></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id="isQuestionDiv">
                        <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否必答:</label>
                        <div class="col-4">
                            <select type="text" class="form-control" name="mustAnswer" value="">
                                <option value="">请选择</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>

                        <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>回答方式:</label>
                        <div class="col-4">
                            <select type="text" class="form-control" name="answerType" value="">
                                <option value="">请选择</option>
                                <option value="y">是否选择</option>
                                <option value="s">单选答案</option>
                                <option value="m">多选答案</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id="useElDiv">
                        <label class="col-2 col-form-label" style="text-align: right;padding-top: 0px;">是否启用<br/>EL表达式:</label>
                        <div class="col-10">
                            <select type="text" class="form-control" name="useEl" value="">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>
                    </div>

                    <div id="stateElDiv" class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">
                            <font color="red">*</font>EL表达式:<br/>
                            <button type="button" class="btn btn-info"
                                    onclick="openSelectMetaDbTbcolModal('isState');" style="margin-top: 10px;">选择
                            </button>
                        </label>
                        <div class="col-lg-10">
                            <textarea id="stateEl" class="form-control" name="stateEl" rows="5"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                        <div class="col-4">
                            <select type="text" class="form-control" name="isActive" value="">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序号:</label>
                        <div class="col-lg-4">
                            <input type="number" class="form-control m-input" name="sortNo" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="factorMemo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button type="submit" class="btn btn-info">保存</button>
                    <button id="closeAddFactorBtn" type="button" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>