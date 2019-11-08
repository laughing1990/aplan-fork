<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 编辑材料关联事项 -->
<div id="add_item_in_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_in_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_in_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <!--列表区域-->
            <div class="base table-responsive" data-scrollable="true" data-max-height="600" style="margin-top: -20px;">
                <div class="m_datatable" id="item_in_table"></div>
            </div>

            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" onclick="saveParInItem()">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#add_item_in_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>