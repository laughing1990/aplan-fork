<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="select_front_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true" >
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 70%;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_par_front_item_title">工程建设审批事项库</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectItemKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectItemNode();">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSearchSelectItemNode();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandSelectItemAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseSelectItemAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div style="height: 500px;overflow-x: hidden;overflow-y: auto;">
                    <div id="selectItemTree" class="ztree" style="overflow: auto;"></div>
                </div>

                <div class="m-portlet__foot" style="padding: 10px;text-align: center;">
                    <button id="selectedFrontCheckItemBtn" type="button" class="btn btn-info" onclick="selectFrontItem();">选择</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

</div>