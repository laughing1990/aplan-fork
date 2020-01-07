<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">材料类别<span style="color:red">*</span>:</label>
        <div class="col-10 input-group">
            <input type="hidden" name="matTypeId" value=""/>
            <input type="text" class="form-control m-input"
                   name="matTypeName" readonly placeholder="请选择材料类别..." >
            <div class="input-group-append">
                <span class="input-group-text open-mat-type">
                    <i class="la la-search"></i>
                </span>
            </div>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">标准材料:</label>
        <div class="col-10 input-group">
            <input type="hidden" name="stdmatId" value=""/>
            <input type="text" class="form-control m-input" name="stdmatName" readonly placeholder="请选择标准材料..." >
            <div class="input-group-append">
                <span class="input-group-text open-stdmat-type">
                    <i class="la la-search"></i>
                </span>
            </div>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">材料名称<span style="color:red">*</span>:</label>
        <div class="col-10">
            <input class="form-control m-input" type="text" value=""
                   name="matName" placeholder="请输入材料名称...">
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">材料编号<span style="color:red">*</span>:</label>
        <div class="col-10">
            <input class="form-control m-input" type="text" value=""
                   name="matCode" placeholder="请输入材料编号...">
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label" style="padding-top: 8px;">材料性质<font color="red">*</font>:</label>
        <div class="col-4">
            <select name="matProp" class="form-control m-input">
                <option value="m">普通材料</option>
                <option value="c">证照材料</option>
                <option value="f">在线表单</option>
            </select>
        </div>

        <label class="col-2 col-form-label" style="text-align: right;">纸质材料类型:</label>
        <div class="col-4">
            <select name="paperMatType" class="form-control m-input">
                <option value="">请选择</option>
                <option value="0">无</option>
                <option value="1">A3</option>
                <option value="2">A4</option>
                <option value="3">A5</option>
            </select>
        </div>
    </div>

    <div id="selectCertDiv" class="form-group m-form__group row" style="display: none;">
        <label class="col-2 col-form-label">电子证照<span style="color:red">*</span>:</label>
        <div class="col-10 input-group">
            <input type="text" class="form-control m-input" name="certName" readonly placeholder="请选择电子证照..." >
            <div class="input-group-append">
                <span class="input-group-text open-cert-type">
                    <i class="la la-search"></i>
                </span>
            </div>
        </div>
    </div>
