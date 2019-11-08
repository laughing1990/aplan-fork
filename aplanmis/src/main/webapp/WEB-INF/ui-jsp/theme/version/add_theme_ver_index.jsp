<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增主题版本定义信息 -->
<div id="add_theme_ver_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_theme_ver_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_theme_ver_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="add_theme_ver_form" method="post">

                    <input type="hidden" name="themeId" value=""/>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="verMemo" rows="3"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button type="submit" class="btn btn-info">保存</button>
                            <button id="closeAddThemeVerBtn" type="button" class="btn btn-secondary">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>