<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 添加/编辑 -->
<div id="edit_item_front_partform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 650px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_item_front_partform_title">事项扩展表单前置检测</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="edit_item_front_partform_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="edit_par_front_partform_scroll" style="height: 400px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="frontPartformId" id="frontPartformId" value=""/>
                        <input type="hidden" name="itemVerId" value=""/>
                        <input type="hidden" name="itemPartformId" id="itemPartformId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>事项扩展表单名称:<br/>
                                <button ${curIsEditable?'':'disabled'} type="button" class="btn btn-info" onclick="openSelectItemFrontPartform('itemPartformId','partformName','isSmartForm','frontPartformId');" style="margin-top: 10px;" id = "select_item_front_partform_btn">选择</button>
                            </label>
                            <div class="col-lg-9">
                                <input id="partformName" class="form-control" name="partformName"  ${curIsEditable?'readonly':'disabled'} />
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                是否智能表单:<br/>
                            </label>
                            <div class="col-lg-9">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isSmartForm" value="1"
                                               disabled >是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isSmartForm" value="0"
                                               disabled >否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="sortNo" value="" ${curIsEditable?'':'disabled'}/>
                            </div>
                        </div>

                        <%--<div class="form-group m-form__group row">--%>

                            <%--<label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>--%>
                            <%--<div class="col-lg-9">--%>
                                <%--<textarea type="text" class="form-control m-input" name="frontPartformMemo" value="" ${curIsEditable?'':'disabled'} ></textarea>--%>
                            <%--</div>--%>
                        <%--</div>--%>

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
                    <button type="submit" class="btn btn-info" id="saveItemFrontPartformBtn">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>