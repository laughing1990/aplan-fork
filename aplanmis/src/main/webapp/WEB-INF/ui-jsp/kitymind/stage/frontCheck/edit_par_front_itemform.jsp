<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑 -->
<div id="edit_par_front_itemform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 650px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_par_front_itemform_title">事项表单信息前置检测</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="edit_par_front_itemform_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="edit_par_front_itemform_scroll" style="height: 400px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="frontItemformId" id="frontItemformId" value=""/>
                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="stageItemId" id="stageItemId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>表单名称:<br/>
                                <button ${curIsEditable?'':'disabled'} type="button" class="btn btn-info" onclick="openSelectParFrontItemform('stageItemId','itemFormName','formItemName','frontItemformId');" style="margin-top: 10px;" id = "select_par_front_itemform_btn">选择</button>
                            </label>
                            <div class="col-lg-9">
                                <input id="itemFormName" class="form-control" name="formName"  ${curIsEditable?'readonly':'disabled'} />
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                表单所属事项:<br/>
                            </label>
                            <div class="col-lg-9">
                                <input id="formItemName" class="form-control" name="itemName"  ${curIsEditable?'readonly':'disabled'} />
                            </div>
                        </div>

                        <div class="form-group m-form__group row">

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="sortNo" value="" ${curIsEditable?'':'disabled'}/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">

                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-9">
                                <textarea type="text" class="form-control m-input" name="frontItemformMemo" value="" ${curIsEditable?'':'disabled'} ></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="isActive" value="" ${curIsEditable?'':'disabled'}>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveParFrontItemformBtn">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>