<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑事项总表 -->
<div id="aedit_item_oneform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 700px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_item_oneform_modal_title">${curIsEditable?'编辑':'查看'}事项总表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_item_oneform_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div style="height: 250px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="itemOneformId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>链接名称:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="linkName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>排序字段:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="sortNo" value=""/>
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
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button type="submit" class="btn btn-info" id="saveItemOneform" style="display: ${curIsEditable?'':'none'}">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
