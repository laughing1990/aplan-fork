<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 选择类别 -->
<div id="add_item_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_mat_modal_title" aria-hidden="true"  data-max-height="340" style="z-index: 3050">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 650px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_mat_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row">
                    <div class="col-xl-5">
                        <input id="matTypeZtreeKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button  type="button" class="btn btn-info" >查询</button>
                        <button type="button" class="btn btn-danger" onclick="clearSearchItemType('#matTypeZtreeKeyWord');">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandTreeAllNode('opusOmOrgTree');">全部展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseAllNode('opusOmOrgTree');">全部折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div style="height:420px;overflow:auto; ">
                    <ul id="matTypeTree"  class="ztree"></ul>
                </div>
            </div>

            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectMatTypeBtn" type="button" onclick="selectMatType();" class="btn btn-info">选择</button>
                <button type="button" class="btn btn-secondary" onclick="closeMatTypeZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>