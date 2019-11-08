<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
    #add_stage_cert_scroll::-webkit-scrollbar{
        width:4px;
        height:4px;
    }
    #add_stage_cert_scroll::-webkit-scrollbar-track{
        background: #fff;
        border-radius: 2px;
    }
    #add_stage_cert_scroll::-webkit-scrollbar-thumb{
        background: #e2e5ec;
        border-radius:2px;
    }
    #add_stage_cert_scroll::-webkit-scrollbar-thumb:hover{
        background: #aaa;
    }
    #add_stage_cert_scroll::-webkit-scrollbar-corner{
        background: #fff;
    }
</style>

<!-- 证照 -->
<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
                <h3 id="add_stage_cert_header" class="m-portlet__head-text">
                    编辑证照
                </h3>
            </div>
        </div>
    </div>
    <form id="add_stage_cert_form" method="post" enctype="multipart/form-data">
        <div class="m-portlet__body" style="padding: 10px 5px;">
            <div id="add_stage_cert_scroll" style="height:650px;overflow-x: hidden;overflow-y: auto;">

                <input type="hidden" name="certId" value=""/>
                <input type="hidden" name="certTypeId" value=""/>
                <input type="hidden" name="isActive" value=""/>
                <input type="hidden" name="parentId" value=""/>
                <input type="hidden" name="isTbEditCertNode" value=""/>

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
							<span id="select_att_dir" class="input-group-text" onclick="javascript:openSelectBscAttDirModal('noModal');">
								选择
							</span>
                        </div>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">证照所属类型:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="certHolder" value="">
                            <option value="">请选择</option>
                            <c:forEach items="${certHolderTypes}" var="certHolderType">
                                <option value="${certHolderType.itemCode}">${certHolderType.itemName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >

                    <label class="col-lg-2 col-form-label" style="text-align: right;">承诺时限计量:</label>
                    <div class="col-lg-4">
                        <input type="number" class="form-control m-input" name="dueNum" value="1"/>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">承诺时限单位:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="dueUnit" value="">
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
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                    <div class="col-lg-10">
                        <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="certMemo" rows="3"></textarea>
                    </div>
                </div>

            </div>
        </div>

        <div class="m-portlet__foot" style="padding: 6px 20px;text-align: center;">
            <button type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-secondary">关闭</button>
        </div>

    </form>
</div>