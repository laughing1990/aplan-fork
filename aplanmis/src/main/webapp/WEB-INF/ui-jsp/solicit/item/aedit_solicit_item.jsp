<%@ page contentType="text/html;charset=UTF-8" %>

<div id="aedit_solicit_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_solicit_item_modal_title">编辑征求事项</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_solicit_item_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <input type="hidden" name="solicitItemId" value=""/>
                    <input type="hidden" name="itemId" value=""/>
                    <input type="hidden" name="itemVerId" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">征求事项名称:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="itemName" value="" readonly/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">征求事项编号:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="itemCode" vvalue="" readonly/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>征求业务类型:</label>
                        <div class="col-10">
                            <div class="m-radio-inline">
                                <c:forEach items="${solicitBusTypes}" var="solicitBusType">
                                    <c:if test="${solicitBusType.itemCode!='LHPS'&&solicitBusType.itemCode!='YCZX'}">
                                        <label class="m-radio">
                                            <input type="radio" name="busType" value="${solicitBusType.itemCode}">${solicitBusType.itemName}<span></span>
                                        </label>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>征求业务模式:</label>
                        <div class="col-10">
                            <div class="m-radio-inline">
                                <label class="m-radio">
                                    <input type="radio" name="solicitType" value="0">多人征求模式<span></span>
                                </label>
                                <label class="m-radio">
                                    <input type="radio" name="solicitType" value="1">单人征求模式<span></span>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>