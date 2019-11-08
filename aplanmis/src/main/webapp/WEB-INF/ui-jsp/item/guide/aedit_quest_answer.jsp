<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增/编辑问题答案-->
<div id="aedit_que_ans_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="aedit_que_ans_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="aedit_que_ans_modal_title" class="modal-title">编辑问题答案</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_que_ans_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_que_ans_scroll" style="height: 460px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="id" value=""/>
                        <input type="hidden" name="itemVerId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">问题:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="question" rows="10"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">答案:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="answer" rows="10"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">排序:</label>
                            <div class="col-lg-10">
                                <input type="number" class="form-control m-input" name="ordernum" value="1"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer" style="padding: 10px;">
                    <button id="saveQueAnsBtn" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" onclick="$('#aedit_que_ans_modal').modal('hide');">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>