<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增法律法规信息 -->
<div id="add_legal_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_legal_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_legal_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="add_legal_form" method="post">

                    <input type="hidden" id="legalId" name="legalId"/>
                    <input type="hidden" id="parentLegalId" name="parentLegalId" />

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>法律法规名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="legalName" value="" placeholder="请输入法律法规名称..."/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>法律法规层级:</label>
                        <div class="col-lg-4">
                            <input type="text" class="form-control m-input" name="legalLevel" value="" placeholder="请输入法律法规层级..."/>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>依据文号:</label>
                        <div class="col-lg-4">
                            <input type="text" class="form-control m-input" name="basicNo" value="" placeholder="请输入依据文号..."/>
                        </div>
                    </div>


                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>颁发机构:</label>
                        <div class="col-lg-4">
                            <input type="text" class="form-control m-input" name="issueOrg" value="" placeholder="请输入颁发机构..."/>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>开始实施的日期:</label>
                        <div class="col-lg-4">
                            <input type="date" class="form-control m-input" name="exeDate" id="exeDate" type="text" placeholder="开始实施的日期">
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">法律法规附件:</label>
                        <div class="col-8">
                            <input id="serviceLegalAttFile" type="file" class="form-control m-input"
                                   name="serviceLegalAttFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                                 <button class="left-button">选择文件</button>
                                 <span class="right-text" id="rightText1">未选择任何文件</span>
                            </span>
                        </div>
                        <div id="serviceLegalAttDiv" class="col-2">
                            <button id="serviceLegalAttButton" type="button" class="btn btn-info"
                                    onclick="showAtt('serviceLegalAtt');">查看附件</button>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注说明:</label>
                        <div class="col-lg-10">
                            <textarea name="serviceLegalMemo" class="form-control m-input" placeholder="请输入备注..." rows="5"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button id="saveLegalBtn" type="submit" class="btn btn-info">保存</button>
                            <button id="closeAddLegalBtn" type="button" class="btn btn-secondary">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>