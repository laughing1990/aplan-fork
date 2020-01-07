<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
            纸质是否必需<font color="red">*</font>:
        </label>
        <div class="col-4">
            <div class="m-radio-inline">
                <label class="m-radio">
                    <input type="radio" name="paperIsRequire" value="1">必须<span></span>
                </label>
                <label class="m-radio">
                    <input type="radio" name="paperIsRequire" value="0" checked>非必须<span></span>
                </label>
            </div>
        </div>

        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
            电子是否必需<font color="red">*</font>:
        </label>
        <div class="col-4">
            <div class="m-radio-inline">
                <label class="m-radio">
                    <input type="radio" name="attIsRequire" value="1">必须<span></span>
                </label>
                <label class="m-radio">
                    <input type="radio" name="attIsRequire" value="0" checked>非必须<span></span>
                </label>
            </div>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
            是否批文批复<font color="red">*</font>:
        </label>
        <div class="col-4">
            <div class="m-radio-inline">
                <label class="m-radio">
                    <input type="radio" name="isOfficialDoc" value="1">是&nbsp;&nbsp;&nbsp;&nbsp;<span></span>
                </label>
                <label class="m-radio">
                    <input type="radio" name="isOfficialDoc" value="0" checked>否<span></span>
                </label>
            </div>
        </div>
        <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
            是否需要签章:
        </label>
        <div class="col-4">
            <div class="m-radio-inline">
                <label class="m-radio">
                    <input type="radio" name="isSign" value="1">是&nbsp;&nbsp;&nbsp;&nbsp;<span></span>
                </label>
                <label class="m-radio">
                    <input type="radio" name="isSign" value="0" checked>否<span></span>
                </label>
            </div>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label" style="text-align: right;">原件验收:</label>
        <div class="col-4">
            <select name="duePaperType" class="form-control m-input">
                <option value="">请选择</option>
                <option value="0">无</option>
                <option value="1">验</option>
                <option value="2">收</option>
            </select>
        </div>

        <label class="col-2 col-form-label">原件数:</label>
        <div class="col-4">
            <input class="form-control m-input" type="number" value="1"
                   name="duePaperCount" placeholder="请输入原件数...">
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label" style="text-align: right;">复印件验收类型:</label>
        <div class="col-4">
            <select name="dueCopyType" class="form-control m-input">
                <option value="">请选择</option>
                <option value="0">无</option>
                <option value="1">验</option>
                <option value="2">收</option>
            </select>
        </div>

        <label class="col-2 col-form-label">复印件数:</label>
        <div class="col-4">
            <input class="form-control m-input" type="number" value="1"
                   name="dueCopyCount" placeholder="请输入复印件数...">
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">材料来源:</label>
        <input type="hidden" id="matFrom" name="matFrom" value=""/>
        <div class="col-10">
            <div class="m-checkbox-inline">
                <label class="m-checkbox">
                    <input type="checkbox" name="matFromCb" id="matFrom1" value="1" onclick="checkboxOnclick(this)">行政机关<span for="matFrom1"></span>
                </label>
                <label class="m-checkbox">
                    <input type="checkbox" name="matFromCb" id="matFrom2" value="2" onclick="checkboxOnclick(this)">企事业单位<span for="matFrom2"></span>
                </label>
                <label class="m-checkbox">
                    <input type="checkbox" name="matFromCb" id="matFrom3" value="3" onclick="checkboxOnclick(this)">社会组织<span for="matFrom3"></span>
                </label>
                <label class="m-checkbox">
                    <input type="checkbox" name="matFromCb" id="matFrom4" value="4" onclick="checkboxOnclick(this)">申请人<span for="matFrom4"></span>
                </label>
                <label class="m-checkbox">
                    <input type="checkbox" name="matFromCb" id="matFrom5" value="5" onclick="checkboxOnclick(this)">中介服务<span for="matFrom5"></span>
                </label>
            </div>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">材料来源部门:</label>
        <div class="col-10">
            <input id="matFromDept" class="form-control m-input" name="matFromDept"
                   placeholder="请输入材料来源部门..." />
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">模板文档:</label>
        <div class="col-8">
            <input id="templateDocFile" type="file" class="form-control m-input"
                   name="templateDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
            <span class="custorm-style">
                 <button class="left-button">选择文件</button>
                 <span class="right-text" id="rightText1">未选择任何文件</span>
            </span>
        </div>
        <div id="templateDocDiv" class="col-2">
            <button id="templateDocButton" type="button" class="btn btn-info"
                    onclick="showMatAtt('0');">查看附件</button>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">样本文档:</label>
        <div class="col-8">
            <input type="file"  id="sampleDocFile" class="form-control m-input"
                   name="sampleDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
            <span class="custorm-style">
                <button class="left-button">选择文件</button>
                <span class="right-text" id="rightText2">未选择任何文件</span>
            </span>
        </div>
        <div id="sampleDocDiv" class="col-2">
            <button id="sampleDocButton" type="button" class="btn btn-info"
                    onclick="showMatAtt('1');">查看附件</button>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">审查样本:</label>
        <div class="col-8">
            <input id="reviewSampleDocFile" type="file" class="form-control m-input"
                   name="reviewSampleDocFile" multiple="multiple" accept="image/*" onchange="uploadFileChange(this);"/>
            <span class="custorm-style">
                 <button class="left-button">选择文件</button>
                 <span class="right-text" id="rightText3">未选择任何文件</span>
            </span>
        </div>
        <div id="reviewSampleDocDiv" class="col-2">
            <button id="reviewSampleDocButton" type="button" class="btn btn-info" onclick="showMatAtt('2');">查看附件</button>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">审查要点<font color="red">*</font>:</label>
        <div class="col-10">
            <textarea name="reviewKeyPoints" class="form-control m-input"
                      placeholder="请输入审查要点..." rows="4"></textarea>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">审查依据:</label>
        <div class="col-10">
            <textarea name="reviewBasis" class="form-control m-input"
                      placeholder="请输入备注..." rows="4"></textarea>
        </div>
    </div>

    <div class="form-group m-form__group row">
        <label class="col-2 col-form-label">备注:</label>
        <div class="col-10">
            <textarea name="matMemo" id="matMemo" rows="4"
                      class="form-control m-input" placeholder="请输入备注..."></textarea>
        </div>
    </div>
