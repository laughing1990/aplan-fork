<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择组织 -->
<div id="select_priv_opus_om_org_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_priv_opus_om_org_ztree_modal_title" aria-hidden="true"  data-max-height="340">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 650px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_priv_opus_om_org_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="m-portlet__body" style="padding: 10px 0px;">
                <input id="isSelectPrivOrgSearch" type="hidden" name="isSearch" value=""/>
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="privOpusOmOrgZtreeKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearPrivOpusOmOrgZtreeForTree();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandPrivOpusOmOrgZtreeAllNode('privOpusOmOrgTree');">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapsePrivOpusOmOrgZtreeAllNode('privOpusOmOrgTree');">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="privOpusOmOrgDiv" style="height:420px;overflow:auto;">
                    <ul id="privOpusOmOrgTree" class="ztree"></ul>
                </div>
            </div>

            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectPrivOrgBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectPrivOpusOmOrgZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="select_mult_priv_opus_om_org_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_mult_priv_opus_om_org_ztree_modal_title" aria-hidden="true"  data-max-height="340">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 650px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_mult_priv_opus_om_org_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="m-portlet__body" style="padding: 10px 0px;">
                <input id="isSelectMultPrivOrgSearch" type="hidden" name="isSearch" value=""/>
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="multPrivOpusOmOrgZtreeKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearMultPrivOpusOmOrgZtreeForTree();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandMultPrivOpusOmOrgZtreeAllNode('multPrivOpusOmOrgTree');">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseMultPrivOpusOmOrgZtreeAllNode('multPrivOpusOmOrgTree');">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="multPrivOpusOmOrgDiv" style="height:420px;overflow:auto;">
                    <ul id="multPrivOpusOmOrgTree" class="ztree"></ul>
                </div>
            </div>

            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectMultPrivOrgBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectMultPrivOpusOmOrgZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>