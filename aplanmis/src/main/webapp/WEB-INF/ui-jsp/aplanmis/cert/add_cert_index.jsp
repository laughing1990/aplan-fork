<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增视图信息 -->
<div id="add_cert_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_cert_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_cert_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="addItemScrollable" class="m-scrollable" data-scrollable="true" data-max-height="450">
                    <form id="add_cert_form" method="post">

                        <input type="hidden" name="certId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照名称:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="certName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照编号:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="certCode" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>所属文件库:</label>
                            <div class="col-lg-4 input-group">
                                <input type="hidden" name="attDirId" value=""/>
                                <input type="text" class="form-control m-input" name="attDirName" placeholder="请点击选择" aria-describedby="select_att_dir" readonly>
                                <div class="input-group-append">
                                    <span id="select_att_dir" class="input-group-text" onclick="javascript:openSelectBscAttDirModal();">
                                        <%--<i class="la la-group"></i>--%>
                                        选择
                                    </span>
                                </div>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照类型:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="certTypeId" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${certTypes}" var="certType">
                                        <option value="${certType.certTypeId}">${certType.typeName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">颁证单位:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="issueUnit" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">软件环境:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="softwareEnv" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">业务行为:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="busAction" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">电子签章信息:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="caInfo" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">持证者:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="certOwner" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">证照所属类型:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="certHolder" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${certHolderTypes}" var="certHolderType">
                                        <option value="${certHolderType.itemCode}">${certHolderType.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">颁证时间:</label>
                            <div class="col-lg-4">
                                <input type="datetime-local" class="form-control m-input" name="issueDate" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">有效期起始时间:</label>
                            <div class="col-lg-4">
                                <input type="datetime-local" class="form-control m-input" name="termStart" value=""/>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">有效期结束时间:</label>
                            <div class="col-lg-4">
                                <input type="datetime-local" class="form-control m-input" name="termEnd" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="itemMemo" rows="3"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-info" onclick="$('#add_cert_form').trigger('submit');">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#add_cert_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>