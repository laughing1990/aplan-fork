<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择事项输入输出-->
<div id="dialog_mat_global" class="modal fade" tabindex="-20" role="dialog" aria-labelledby="dialog_mat_global_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="dialog_mat_global_title">选取全局材料</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;max-height:600px;overflow-y:auto;overflow-x: hidden">
                <div class="row">
                    <div class="col-xl-5">
                        <input id="input_mat_global_search" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="GlobalMatDatatable.search()">查询</button>
                        <button type="button" class="btn btn-danger" onclick="GlobalMatDatatable.searchClear()">清空</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="m_datatable" id="tb_mat_global"></div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="btn_mat_global_select" type="button" class="btn btn-info" onclick="selectGlobalMat();" >选择</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>