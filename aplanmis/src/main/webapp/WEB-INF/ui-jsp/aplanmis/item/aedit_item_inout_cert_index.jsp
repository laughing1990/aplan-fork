<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">

    .row{
        margin-left: 0px;
        margin-right: 0px;
    }

    /*#aedit_item_inout_cert_scroll::-webkit-scrollbar{*/
        /*width:4px;*/
        /*height:4px;*/
    /*}*/

    /*#aedit_item_inout_cert_scroll::-webkit-scrollbar-track{*/
        /*background: #fff;*/
        /*border-radius: 2px;*/
    /*}*/

    /*#aedit_item_inout_cert_scroll::-webkit-scrollbar-thumb{*/
        /*background: #e2e5ec;*/
        /*border-radius:2px;*/
    /*}*/

    /*#aedit_item_inout_cert_scroll::-webkit-scrollbar-thumb:hover{*/
        /*background: #aaa;*/
    /*}*/

    /*#aedit_item_inout_cert_scroll::-webkit-scrollbar-corner{*/
        /*background: #fff;*/
    /*}*/
</style>

<!-- 新增/编辑阶段证照信息 -->
<div id="aedit_item_inout_cert_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="aedit_item_inout_cert_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 id="aedit_item_inout_cert_modal_title" class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_item_inout_cert_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_item_inout_cert_scroll" style="height: 450px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="certId" value=""/>
                        <input type="hidden" name="certTypeId" value=""/>
                        <input type="hidden" name="isActive" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照名称:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="certName" value="" readonly/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>证照编号:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="certCode" value="" readonly/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>所属文件库:</label>
                            <div class="col-lg-4 input-group">
                                <input type="hidden" name="attDirId" value=""/>
                                <input type="text" class="form-control m-input" name="attDirName"
                                       aria-describedby="select_att_dir" readonly>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">证照所属类型:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="certHolder" value="" readonly>
                                    <option value="">请选择</option>
                                    <c:forEach items="${certHolderTypes}" var="certHolderType">
                                        <option value="${certHolderType.itemCode}">${certHolderType.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限计量:</label>
                            <div class="col-lg-4">
                                <input type="number" class="form-control m-input" name="dueNum" value="1" readonly/>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限单位:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="dueUnit" value="" readonly>
                                    <option value="">请选择</option>
                                    <c:forEach items="${dueUnits}" var="dueUnit">
                                        <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">软件环境:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="softwareEnv" rows="3" readonly></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">业务行为:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="busAction" rows="3"  readonly></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                            <div class="col-lg-10">
                                <input type="number" class="form-control m-input" name="sortNo" value="1" readonly/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="certMemo" rows="3" readonly></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <%--<button type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;--%>
                    <button type="button" class="btn btn-secondary"
                            onclick="$('#aedit_item_inout_cert_modal').modal('hide');">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>