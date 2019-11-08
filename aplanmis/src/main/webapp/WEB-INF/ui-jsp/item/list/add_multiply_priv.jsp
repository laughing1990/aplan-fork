<%@ page contentType="text/html;charset=UTF-8" %>


<div id="add_multiply_priv_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true" style="overflow-x: hidden;overflow-y: auto;">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1300px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_priv_modal_title">批量事项下放</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div id="multiplyWestPanel" class="col-xl-7" style="padding: 0px 2px 0px 8px;overflow-y: auto;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                        <h3 class="m-portlet__head-text">
                            <%--${itemNatureTitle}--%>工程建设审批事项库
                        </h3>
                    </div>
                </div>
                <div class="m-portlet__head-tools">
                    <font color="red">*&nbsp;</font>
                    请从工程建设审批事项库里面选择事项下放&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <%--请从${itemNatureTitle}里面选择本${stageTypetitle}关联事项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                </div>
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
                                onclick="collapseSelectMultiplyNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="selectMultiplyItemTree" class="ztree" style="overflow: auto;"></div>
            </div>
        </div>
    </div>

    <div class="col-xl-5" style="padding: 0px 8px 0px 2px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                           <span class="m-portlet__head-icon m--hide">
                               <i class="la la-gear"></i>
                           </span>
                        <h3 class="m-portlet__head-text">
                            选择事项
                        </h3>
                    </div>
                </div>
            </div>
            <form id="add_mult_item_priv_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="add_mutl_item_priv_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" id="mutlItemVerIds" name="itemVerIds"  value=""/>
                        <input type="hidden" id="multOrgId" name="orgId" value=""/>
                        <input type="hidden" id="multRegionId" name="regionId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>行政区域:</label>
                            <div class="col-lg-9 input-group">
                                <input type="text" class="form-control m-input" name="regionName"
                                       placeholder="请点击选择" aria-describedby="select_Mutl_Region_Id" readonly
                                       onclick="isSelectMutlRegion(this, false);">
                                <div class="input-group-append">
                                    <span id="select_Mutl_Region_Id" class="input-group-text"
                                          onclick="isSelectMutlRegion(null, false);">
                                        <i class="la la-group"></i>
                                    </span>
                                </div>
                            </div>

                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>所属部门:</label>
                            <div class="col-lg-9 input-group">
                                <input type="text" class="form-control m-input" name="orgName"
                                       placeholder="请点击选择" aria-describedby="select_org_Id" readonly
                                       onclick="isSelectMutlPrivOpuOmOrg(this, false);">
                                <div class="input-group-append">
                                    <span id="select_org_Id" class="input-group-text"
                                          onclick="isSelectMutlPrivOpuOmOrg(null, false);">
                                        <i class="la la-group"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="isActive" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>启用EL表达式:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="useEl" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                        </div>

                        <div class="form-group m-form__group row" id="multElText" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>填写EL表达式:</label>
                            <div class="col-lg-9">
                                <textarea name="elExpr" class="form-control m-input" style="width:100%; height:150px"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;">是否人工选择&nbsp;<br/>下级承办组织:</label>
                            <div class="col-lg-9">
                                <select type="text" style="margin-top:10px" class="form-control" name="allowManual" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>

                        </div>


                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveMutlItemPriv">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>

</div>
            </div>
        </div>
    </div>
</div>