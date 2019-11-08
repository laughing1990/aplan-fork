<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择行政区划 -->
<div id="select_out_item_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_out_item_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_out_item_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <input id="isSelectOutItemSearch" type="hidden" name="isSearch" value=""/>
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectOutItemKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectOutItemNode();">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSearchSelectOutItemNode();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="refreshSelectOutItemAllNode();">刷新</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandSelectOutItemAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseSelectOutItemAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="outItemDiv" style="height:420px;overflow:auto;">
                    <ul id="outItemTree" class="ztree"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="saveOutItemBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectOutItemZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>