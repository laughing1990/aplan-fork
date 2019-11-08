<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增或者编辑事项前置信息 -->
<div id="add_item_cond_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_cond_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_cond_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="add_item_cond_form" method="post">

                    <input type="hidden" name="itemCondId" id="cond_itemCondId" value=""/>
                    <input type="hidden" name="itemId" id="cond_itemId" value=""/>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="condName" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">
                            <font color="red">*</font>表达式:<br/>
                            <button type="button" class="btn btn-info" onclick="openSelectMetaDbTbcolModal('isCond');" style="margin-top: 10px;">选择</button>
                        </label>
                        <div class="col-lg-10">
                            <textarea id="condEl" class="form-control" name="condEl" rows="5"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;排序：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="sortNo" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="condMemo" rows="3"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" onclick="$('#add_item_cond_form').trigger('submit');">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#add_item_cond_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>