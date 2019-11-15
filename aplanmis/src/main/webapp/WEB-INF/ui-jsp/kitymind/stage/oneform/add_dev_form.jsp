<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑 -->
<div id="add_dev_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_part_form_modal_title">新增开发表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_dev_form_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="add_dev_form_scroll" style="height: 425px;verflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="stagePartformId" value=""/>
                        <input type="hidden" name="stoFormId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>表单编号:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="formCode" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>表单名称:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="formName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>表单URL:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="formLoadUrl" value=""/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="saveDevform" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
