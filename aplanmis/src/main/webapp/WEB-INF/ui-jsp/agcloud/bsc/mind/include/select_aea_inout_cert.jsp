<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .selectedCertSortDiv {
          color: #464637;
          font-size: 14px;
          font-family: 'Roboto', sans-serif;
          font-weight: 300;
    }

    .selectedCertSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedCertSortDiv li {
        background-color: #fff;
        padding: 6px 0px;
        display: list-item;
        text-align: -webkit-match-parent;

        /**边框样式**/
        border: 1px solid transparent;
        border-bottom: 1px solid #ebebeb;
    }
</style>

<!-- 选择事项-->
<div id="select_cert_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_cert_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_cert_ztree_modal_title">电子证照导入</h5>
                <button type="button" class="close" aria-label="Close" onclick="certDialogClose();" >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="certOperaDiv" class="m-portlet__body" style="padding: 10px 0px;
                 max-height:350px;height:350px;verflow-y:auto;overflow-x: hidden">
                <div class="form-group m-form__group row" id = "select_cert_select_state_div">
                    <label class="col-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否为情形输入:</label>
                    <div class="col-9">
                        <div class="m-radio-inline">
                            <label class="m-radio">
                                <input type="radio" name="certIsStateInout" value="1">是
                                <span></span>
                            </label>
                            <label class="m-radio">
                                <input type="radio" name="certIsStateInout" value="0">不是
                                <span></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div id="certItemStateDiv" class="form-group m-form__group row">
                    <label class="col-3 col-form-label" style="text-align: right;">事项情形<font color="red"></font>:</label>
                    <div class="col-9">
                        <select id="certItemStateId" name="itemStateId" class="form-control m-input" placeholder="请选择事项情形">
                            <option value="">--请选择--</option>
                        </select>
                    </div>
                </div>
                <div class="form-group m-form__group row" >
                    <label class="col-lg-3 col-form-label" style="text-align: right;">
                        导入材料:<br/>
                        <button type="button" class="btn btn-info" onclick="chooseCert();" style="margin-top: 10px;">选择</button>
                    </label>
                    <div class="col-lg-9">
                        <input type="hidden" id="certHidIds" name="globalMatIds" />
                        <textarea class="form-control" rows="5" id="cert_show_input" readonly></textarea>
                    </div>
                </div>
            </div>
            <div id="certTreeDiv" class="m-portlet__body" style="max-height:450px;height:450px;">
                <div style="width: 100%;height: 100%;">
                    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                        <div class="col-xl-6" style="max-height: 420px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption">
                                        <div class="m-portlet__head-title">
                                           <span class="m-portlet__head-icon m--hide">
                                               <i class="la la-gear"></i>
                                           </span>
                                            <h3 class="m-portlet__head-text">
                                                电子证照库
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectCertKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info" style="padding-left: 8px;padding-right: 8px;" onclick="searchSelectCertNode();">查询</button>
                                            <button type="button" class="btn btn-secondary" style="padding-left: 8px;padding-right: 8px;" onclick="clearSearchSelectCertNode();">清空</button>
                                            <button type="button" class="btn btn-secondary" style="padding-left: 8px;padding-right: 8px;" onclick="expandSelectCertAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary" style="padding-left: 8px;padding-right: 8px;" onclick="collapseSelectCertAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="350">
                                        <div id="selectCertTree" class="ztree" style="overflow-y:auto;overflow-x:auto;max-height: 280px;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-6" style="max-height: 420px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption">
                                        <div class="m-portlet__head-title">
                                           <span class="m-portlet__head-icon m--hide">
                                               <i class="la la-gear"></i>
                                           </span>
                                            <h3 class="m-portlet__head-text">
                                                已选证照
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="350">
                                        <div class="selectedCertSortDiv">
                                            <ul id="selectedCertUl"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectCertBtn" type="button" class="btn btn-info" onclick="certDialogChoose();">保存</button>
                <button type="button" class="btn btn-secondary" onclick="certDialogClose();">关闭</button>
            </div>
        </div>
    </div>
</div>
