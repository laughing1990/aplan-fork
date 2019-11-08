<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增/编辑收费信息-->
<div id="aedit_charge_info_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="aedit_charge_info_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="aedit_charge_info_modal_title" class="modal-title">编辑收费项目</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_charge_info_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_charge_info_scroll" style="height: 460px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="chargeId" value=""/>
                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="feeTypeText" value=""/>
                        <input type="hidden" name="isDescText" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费名目:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="feeName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费主体:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="feeOrg" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-10">
                                <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费性质:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="feeType" value="">
                                    <%--<option value="">请选择</option>--%>
                                    <option value="1">行政事业性收费</option>
                                    <option value="2">经营服务性收费</option>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>是否允许减免:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isDesc" value="">
                                    <%--<option value="">请选择</option>--%>
                                    <option value="0">不允许</option>
                                    <option value="1">允许</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费标准:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="feeStand" rows="8"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费依据:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="bylaw" rows="8"></textarea>
                            </div>
                        </div>

                        <div id="descExplainDiv" class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>收费减免情形:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="descExplain" rows="8"></textarea>
                            </div>
                        </div>

                        <div id="descLawDiv" class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>减免情形依据:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="descLaw" rows="8"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="remark" rows="4"></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer" style="padding: 10px;">
                    <button id="saveChargeInfoBtn" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" onclick="$('#aedit_charge_info_modal').modal('hide');">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>