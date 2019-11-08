<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 新增/编辑项目信息检测 -->
<div id="aedit_item_proj_check_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_item_proj_check_modal_title">编辑事项总表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_item_proj_check_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <input type="hidden" name="frontProjId" value=""/>
                    <input type="hidden" name="itemVerId" value=""/>
                    <input type="hidden" name="isActive" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>规则名称:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control m-input" name="ruleName" value=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>规则条件表达式:</label>
                        <div class="col-lg-9">
                            <textarea class="form-control" name="ruleEl" rows="3"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="frontProjMemo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button type="submit" class="btn btn-info" id="saveProjCheckform">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
