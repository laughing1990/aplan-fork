<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="aedit_std_mat_type_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="aedit_std_mat_type_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_std_mat_type_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="aedit_std_mat_type_form" method="post">

                    <input type="hidden" name="stdmatTypeId" value=""/>
                    <input type="hidden" name="parentTypeId" value=""/>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">
                            <font color="red">*</font>分类名称:
                        </label>
                        <div class="col-lg-10">
                            <input type="text" name="typeName" class="form-control m-input" placeholder="请输入分类名称"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">
                            <font color="red">*</font>分类编号:
                        </label>
                        <div class="col-lg-10">
                            <input type="text" name="typeCode" class="form-control m-input" placeholder="请输入分类编号"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序号:</label>
                        <div class="col-lg-10">
                            <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="typeMemo" rows="3"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button id="saveMatTypeBtn" type="submit" class="btn btn-info">保存</button>
                            <button id="closeMatTypeBtn" type="button" class="btn btn-secondary">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>