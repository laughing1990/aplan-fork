<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    .hid{
        display: none;
    }
    .wizard-step{
        display: none;
    }
    .wizard-step_current{
        display:block;
    }
</style>
<div class="tab-pane" id="m_tabs_1" role="tabpanel">
    <div class="m-form m-form--label-align-right m--margin-bottom-5">
        <div class="row" style="margin: 0px;">
            <div class="col-md-6"style="text-align: left;">
                <button id="addItemStateBtn" type="button" class="btn btn-info" onclick="addMat(1);">新增材料</button>
                <button type="button" class="btn btn-secondary" onclick="openGlogbalMatSelect(1);">材料导入</button>
                <button type="button" class="btn btn-secondary" onclick="openCertSelect(1);">电子证照导入</button>
                <button type="button" class="btn btn-secondary" onclick="delSelectMat(1);">删除</button>
                <button type="button" class="btn btn-secondary" onclick="InMatDatatable.reload()">刷新</button>
            </div>
            <div class="col-md-6" style="padding: 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-2"></div>
                    <div class="col-6">
                        <form id="search_mat_in" method="post">
                            <div class="m-input-icon m-input-icon--left">
                                <input type="text" class="form-control m-input" id="input_mat_in_search" placeholder="请输入关键字..." name="keyword" value=""/>
                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                    <span><i class="la la-search"></i></span>
                                </span>
                            </div>
                        </form>
                    </div>
                    <div class="col-4">
                        <button type="button" class="btn btn-info" onclick="InMatDatatable.search()">查询</button>
                        <button type="button" class="btn btn-secondary" onclick="InMatDatatable.searchClear()">清空</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
    <div class="base" data-scrollable="true" data-max-height="600" style="margin-top: -20px;">
        <div class="m_datatable" id="tb_mat_in"></div>
    </div>
</div>
<div class="tab-pane" id="m_tabs_2" role="tabpanel">
    <div class="m-form m-form--label-align-right m--margin-bottom-5">
        <div class="row" style="margin: 0px;">
            <div class="col-md-6"style="text-align: left;">
                <button type="button" class="btn btn-info"  onclick="addMat(0);">新增材料</button>
                <button type="button" class="btn btn-secondary" onclick="openGlogbalMatSelect(0);">材料导入</button>
                <button type="button" class="btn btn-secondary" onclick="openCertSelect(0);">电子证照导入</button>
                <button type="button" class="btn btn-secondary" onclick="delSelectMat(0);">删除</button>
                <button type="button" class="btn btn-secondary" onclick="OutMatDatatable.reload()">刷新</button>
            </div>
            <div class="col-md-6" style="padding: 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-2"></div>
                    <div class="col-6">
                        <form id="search_mat_out" method="post">
                            <div class="m-input-icon m-input-icon--left">
                                <input type="text" id="input_mat_out_search"
                                       class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                    <span><i class="la la-search"></i></span>
                                </span>
                            </div>
                        </form>
                    </div>
                    <div class="col-4">
                        <button type="button" class="btn btn-info" onclick="OutMatDatatable.search()">查询</button>
                        <button type="button" class="btn btn-secondary" onclick="OutMatDatatable.searchClear()">清空</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
    <div class="base" data-scrollable="true" data-max-height="600" style="margin-top: -20px">
        <div class="m_datatable" id="tb_mat_out"></div>
    </div>
</div>
<!--类别选取 -->
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_type_tree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_index.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_cert.js" type="text/javascript"></script>
<!--查询,新增,编辑材料 -->
<jsp:include page="include/item_mat_dialog.jsp"/>
<jsp:include page="../cert/select_aea_inout_cert.jsp"/>