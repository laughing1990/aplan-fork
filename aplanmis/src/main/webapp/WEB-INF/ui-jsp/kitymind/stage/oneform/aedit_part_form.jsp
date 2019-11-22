<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑 -->
<div id="aedit_part_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_part_form_modal_title">新增扩展表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_part_form_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_part_form_scroll" style="height: 400px;verflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="stagePartformId" value=""/>
                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="stoFormId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>扩展表名称:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="partformName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>表单类型:</label>
                            <div class="col-lg-10">
                                <div class="m-radio-inline">
                                    <label class="m-radio" style="width: 100px;">
                                        <input type="radio" name="isSmartForm" value="1" checked>智能表单<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isSmartForm" value="0">开发表单<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用EL:</label>
                            <div class="col-10">
                                <div class="m-radio-inline">
                                    <label class="m-radio" style="width: 100px;">
                                        <input type="radio" name="useEl" value="1" checked>启用<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="useEl" value="0">禁用<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <%--<div id="formUrlDiv" class="form-group m-form__group row" >--%>
                        <%--<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>开发表单地址:</label>--%>
                        <%--<div class="col-lg-10">--%>
                        <%--<textarea class="form-control" name="formUrl" rows="3"></textarea>--%>
                        <%--</div>--%>
                        <%--</div>--%>

                        <div id="stageElDiv" class="form-group m-form__group row" style="display: none;">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>EL表达式:<br/>
                                <button type="button" class="btn btn-info" onclick="openSelectMetaDbTbcolModal('elContent');" style="margin-top: 10px;">选择</button>
                            </label>
                            <div class="col-lg-10">
                                <textarea id="elContent" class="form-control" name="elContent" rows="6"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-10">
                                <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="saveParPartform" type="submit" class="btn btn-info" style="display: ${curIsEditable?'':'none'}">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
