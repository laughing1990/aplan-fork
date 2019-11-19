<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑 -->
<div id="edit_item_front_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 650px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_item_front_item_title">事项信息前置检测</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="edit_item_front_item_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="edit_item_front_item_scroll" style="height: 300px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="frontItemId" value=""/>
                        <input type="hidden" name="itemVerId" value=""/>
                        <input type="hidden" name="frontCkItemVerId" value=""/>
                        <input type="hidden" name="isActive" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>前置事项:<br/></label>
                            <div class="col-lg-10">
                                <textarea id="frontCkItemName" type="text" class="form-control m-input" name="frontCkItemName" rows="3" readonly value=""></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="sortNo" value="" ${curIsEditable?'':'disabled'}/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-10">
                                <textarea type="text" class="form-control m-input" name="frontItemMemo" rows="4" value="" ${curIsEditable?'':'disabled'} ></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveItemFrontItemBtn" style="display: ${curIsEditable?'':'none'}">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>