<%@ page contentType="text/html;charset=UTF-8" %>

<div id="add_item_opera_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_opera_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_opera_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px 30px;">

                <p style="font-size: 15px;">1、版本定义是指当前事项下事项、情形材料、共享材料更新迭代的版本记录；</p>
                <p style="font-size: 15px;">2、事项下事项、情形材料、共享材料变更可发布新版本定义；</p>
                <p style="font-size: 15px;">3、复制版本定义
                    <b style="color: red;">先点击复制版本按钮</b>，
                    <b style="color: #5867dd;">然后设置事项、情形材料、共享材料</b>，
                    <b style="color: red;">最后选择启用此版本定义</b>；</p>
                <p style="font-size: 15px;">4、试运行某版本定义将禁用其他版本定义，发布后不可编辑，审批流程也将按照最新版本定义进行。</p>

            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary" onclick="$('#add_item_opera_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>