<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增或者编辑事项情形处理信息 -->
<div id="add_item_state_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_state_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_state_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="add_item_state_div" style="height: 400px;overflow: auto;">
                    <form id="add_item_state_form" method="post">

                        <input type="hidden" name="itemStateId" id="state_itemStateId" value=""/>
                        <input type="hidden" name="itemId" id="state_itemId" value=""/>
                        <input type="hidden" name="isActive" id="state_isActive" value=""/>
                        <input type="hidden" name="parentStateId" id="state_parentStateId" value=""/>
                        <input type="hidden" name="stateSeq" id="state_stateSeq" value=""/>


                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="stateName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用EL:</label>
                            <div class="col-lg-9">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input name="useEl" id="useElRadio1" value="1" type="radio" onclick="clickUseElRadio(this.value)">是<span for="useElRadio1"></span></label>
                                    <label class="m-radio"><input name="useEl" id="useElRadio0" value="0" type="radio" onclick="clickUseElRadio(this.value)">否<span for="useElRadio0"></span></label>
                                </div>
                            </div>
                        </div>

                        <div id="stateElDiv" class="form-group m-form__group row" style="display:none;">
                            <label class="col-lg-3 col-form-label" style="text-align: right;">
                                <font color="red">*</font>表达式:<br/>
                                <button type="button" class="btn btn-info" onclick="openSelectMetaDbTbcolModal('isState');" style="margin-top: 10px;">选择</button>
                            </label>
                            <div class="col-lg-9">
                                <textarea id="stateEl" class="form-control" name="stateEl" rows="5"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;排序：</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="sortNo" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>情形类型:</label>
                            <div class="col-lg-9">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input name="isQuestion" id="isQuestionRadio1" value="1" type="radio" onclick="clickIsQuestionRadio(this.value)">问题类型<span for="isQuestionRadio1"></span></label>
                                    <label class="m-radio"><input name="isQuestion" id="isQuestionRadio0" value="0" type="radio" onclick="clickIsQuestionRadio(this.value)">答案类型<span for="isQuestionRadio0"></span></label>
                                </div>
                            </div>
                        </div>

                        <div id="mustAnswerDiv" class="form-group m-form__group row" style="display:none;">
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>回答方式:</label>
                            <div class="col-lg-9">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input name="mustAnswer" id="mustAnswerRadio1" value="1" type="radio">必选<span for="mustAnswerRadio1"></span></label>
                                    <label class="m-radio"><input name="mustAnswer" id="mustAnswerRadio0" value="0" type="radio">可选<span for="mustAnswerRadio0"></span></label>
                                </div>
                            </div>
                        </div>

                        <div id="answerTypeDiv" class="form-group m-form__group row" style="display:none;">
                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>答案类型</label>
                            <div class="col-lg-9">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input name="answerType" id="answerTypeRadio1" value="s" type="radio">单选答案<span for="answerTypeRadio1"></span></label>
                                    <label class="m-radio"><input name="answerType" id="answerTypeRadio0" value="m" type="radio">多选答案<span for="answerTypeRadio0"></span></label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-3 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-9">
                                <textarea class="form-control" name="stateMemo" rows="3"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" onclick="$('#add_item_state_form').trigger('submit');">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#add_item_state_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>