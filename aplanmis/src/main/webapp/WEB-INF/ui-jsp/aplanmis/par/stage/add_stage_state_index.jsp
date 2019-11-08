<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
    #add_stage_state_scroll::-webkit-scrollbar{
        width:4px;
        height:4px;
    }
    #add_stage_state_scroll::-webkit-scrollbar-track{
        background: #fff;
        border-radius: 2px;
    }
    #add_stage_state_scroll::-webkit-scrollbar-thumb{
        background: #e2e5ec;
        border-radius:2px;
    }
    #add_stage_state_scroll::-webkit-scrollbar-thumb:hover{
        background: #aaa;
    }
    #add_stage_state_scroll::-webkit-scrollbar-corner{
        background: #fff;
    }
</style>

<!-- 新增情形 -->
<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
                <h3 id="add_stage_state_header" class="m-portlet__head-text">
                    新增情形
                </h3>
            </div>
        </div>
    </div>
    <div class="m-portlet__body" style="padding: 10px 5px;">
        <div id="add_stage_state_scroll" style="height:650px;overflow-x: hidden;overflow-y: auto;">
            <form id="add_stage_state_form" method="post">

                <input type="hidden" name="parStateId" value=""/>
                <input type="hidden" name="stageId" value=""/>
                <input type="hidden" name="parentType" value=""/>
                <input type="hidden" name="parentStateId" value=""/>
                <input type="hidden" name="isTbEditStateNode" value=""/>  <!-- 设置表格编辑情形节点 -->

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="stateName" value=""/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用EL表达式:</label>
                    <div class="col-lg-10">
                        <select id="useEl" type="text" class="form-control" name="useEl" value="">
                            <option value="0">禁用</option>
                            <option value="1">启用</option>
                        </select>
                    </div>
                </div>

                <div id="stateElDiv" class="form-group m-form__group row" style="display:none;">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>表达式:<br/>
                        <button type="button" class="btn btn-info" style="margin-top: 10px;"
                                onclick="openSelectMetaDbTbcolModal('isState');">选择</button>
                    </label>
                    <div class="col-lg-10">
                        <textarea id="stateEl" class="form-control" name="stateEl" rows="5"></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="sortNo" value=""/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>情形类型:</label>
                    <div class="col-lg-10">
                        <select id="isQuestion" type="text" class="form-control" name="isQuestion" value="">
                            <option value="">请选择</option>
                            <option value="0">答案类型</option>
                            <option value="1">问题类型</option>
                        </select>
                    </div>
                </div>

                <div id="mustAnswerDiv" class="form-group m-form__group row" style="display: none;">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>回答方式:</label>
                    <div class="col-lg-10">
                        <select type="text" class="form-control" name="mustAnswer" value="">
                            <option value="">请选择</option>
                            <option value="0">可选</option>
                            <option value="1">必答</option>
                        </select>
                    </div>
                </div>

                <div id="answerTypeDiv" class="form-group m-form__group row" style="display: none;">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>答案类:</label>
                    <div class="col-lg-10">
                        <select type="text" class="form-control" name="answerType" value="">
                            <option value="">请选择</option>
                            <option value="y">是否选择</option>
                            <option value="s">单选答案</option>
                            <option value="m">多选答案</option>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="stateMemo" rows="3"></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <div class="col-lg-12" style="text-align: center;padding-top: 20px;">
                        <button type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;
                        <button type="button" class="btn btn-secondary">关闭</button>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>