<%@ page contentType="text/html;charset=UTF-8" %>


<div id="add_nav_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_nav_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_nav_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_par_nav_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <input type="hidden" name="navId" value=""/>
                    <input type="hidden" name="isActive" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="navName" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <%--<label class="col-2 col-form-label" style="text-align: right;">是否启用:</label>--%>
                        <%--<div class="col-4">--%>
                            <%--<select type="text" class="form-control" name="isActive" value="">--%>
                                <%--<option value="1">启用</option>--%>
                                <%--<option value="0">禁用</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>

                        <label class="col-lg-2 col-form-label" style="text-align: right;">排序号:</label>
                        <div class="col-lg-10">
                            <input type="number" class="form-control m-input" name="sortNo" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="navMemo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button  type="submit" class="btn btn-info">保存</button>
                    <button id="closeAddNavBtn" type="button" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>