<%@ page contentType="text/html;charset=UTF-8" %>

<div id="globalMatListDiv" class="m-portlet__body" style="padding: 10px 0px;max-height:450px;height:450px;;overflow-y:auto;overflow-x: hidden">
    <div class="row">
        <div class="col-xl-5">
            <input id="input_mat_global_search" type="text" class="form-control m-input m-input--solid empty"
                   placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">
        </div>
        <div class="col-xl-7">
            <button type="button" class="btn btn-info" onclick="GlobalMatDatatable.search()">查询</button>
            <button type="button" class="btn btn-secondary" onclick="GlobalMatDatatable.searchClear()">清空</button>
        </div>
    </div>
    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
    <div class="m_datatable" id="tb_mat_global"></div>
</div>

<div id="globalMatOperaDiv" class="m-portlet__body" style="padding: 10px 0px;max-height:350px;
     height:350px;verflow-y:auto;overflow-x: hidden">
    <div class="form-group m-form__group row" id = "select_global_select_state_div">
        <label class="col-3 col-form-label" style="text-align: right;">
            <font color="red">*</font>是否为情形输入:
        </label>
        <div class="col-9">
            <div class="m-radio-inline">
                <label class="m-radio">
                    <input type="radio" name="globalMatIsStateInout" value="1">是
                    <span></span>
                </label>
                <label class="m-radio">
                    <input type="radio" name="globalMatIsStateInout" value="0">不是
                    <span></span>
                </label>
            </div>
        </div>
    </div>
    <div id="globalMatItemStateDiv" class="form-group m-form__group row">
        <label class="col-3 col-form-label" style="text-align: right;">事项情形<font color="red"></font>:</label>
        <div class="col-9">
            <select id="globalMatItemStateId" name="itemStateId" class="form-control m-input" placeholder="请选择事项情形">
                <option value="">--请选择--</option>
            </select>
        </div>
    </div>
    <div class="form-group m-form__group row" >
        <label class="col-lg-3 col-form-label" style="text-align: right;">导入材料:<br />
            <button type="button" class="btn btn-info" onclick="chooseGlobalMat();" style="margin-top: 10px;">选择</button>
        </label>
        <div class="col-lg-9">
            <div class="input-group">
                <input type="hidden" id="globalMatIds" name="globalMatIds" />
                <textarea class="form-control" rows="3" id="global_mat_show_input" readonly="readonly" style="margin-right: 10%;"></textarea>
            </div>
        </div>
    </div>
</div>

<div class="modal-footer" style="padding: 10px;height: 60px;">
    <%--<button id="btn_mat_global_select" type="button" class="btn btn-info" onclick="selectGlobalMat();" >选择</button>--%>
    <button id="btn_mat_global_select" type="button" class="btn btn-info" onclick="globalMatDialogChoose();" >选择</button>
    <button type="button" class="btn btn-secondary" onclick="globalMatDialogClose();">关闭</button>
</div>
