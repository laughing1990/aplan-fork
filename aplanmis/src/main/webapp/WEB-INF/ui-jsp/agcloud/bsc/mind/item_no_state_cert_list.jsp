<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>

<%--<style type="text/css">--%>
    <%--.row{--%>
        <%--margin-left: 0px;--%>
        <%--margin-right: 0px;--%>
    <%--}--%>
<%--</style>--%>

<%--<div id="view_item_cert_modal" class="modal fade" tabindex="-1" role="dialog"--%>
     <%--aria-labelledby="view_item_cert_modal_title" aria-hidden="true">--%>
    <%--<div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header" style="padding: 15px;height: 45px;">--%>
                <%--<h5 class="modal-title" id="view_item_cert_modal_title">导入电子证照</h5>--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            <%--<div class="modal-body" style="padding: 10px;">--%>
                <%--<div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">--%>
                    <%--<div class="row" style="margin: 0px;">--%>
                        <%--<div class="col-md-6"style="text-align: left;"></div>--%>
                        <%--<div class="col-md-6" style="padding: 0px;">--%>
                            <%--<div class="row" style="margin: 0px;">--%>
                                <%--<div class="col-7">--%>
                                    <%--<div class="m-input-icon m-input-icon--left">--%>
                                        <%--<input id="itemCertKeyword" type="text" class="form-control m-input"--%>
                                               <%--placeholder="请输入关键字..." name="keyword" value=""/>--%>
                                        <%--<span class="m-input-icon__icon m-input-icon__icon--left">--%>
                                            <%--<span><i class="la la-search"></i></span>--%>
                                        <%--</span>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="col-5">--%>
                                    <%--<button type="button" class="btn btn-info" onclick="searchItemCert();">查询</button>--%>
                                    <%--<button type="button" class="btn btn-secondary" onclick="clearSearchItemCert();">清空</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>--%>
                <%--<div id="item_cert_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">--%>
                    <%--<!-- 列表区域 -->--%>
                    <%--<div class="base" style="padding: 0px 5px;">--%>
                        <%--<table  id="item_cert_list_tb"--%>
                                <%--data-toggle="table",--%>
                                <%--data-method="post"--%>
                                <%--data-content-type="application/x-www-form-urlencoded; charset=UTF-8"--%>
                                <%--data-pagination=true--%>
                                <%--data-page-size="10",--%>
                                <%--data-page-list="[10]",--%>
                                <%--data-click-to-select=true--%>
                                <%--data-side-pagination="server"--%>
                                <%--data-query-params="itemCertParam",--%>
                                <%--&lt;%&ndash;data-response-handler="itemCertResponseData",&ndash;%&gt;--%>
                                <%--data-pagination-show-page-go="true",--%>
                                <%--data-url="${pageContext.request.contextPath}/aea/cert/listAeaCertByPage.do">--%>
                            <%--<thead>--%>
                                <%--<tr>--%>
                                    <%--<th data-field="" data-checkbox="true" data-align="center" data-colspan="1"--%>
                                        <%--data-width="10" data-formatter="checkItemCertOverAllIds"></th>--%>
                                    <%--<th data-field="certId"  data-align="left" data-width="10">ID</th>--%>
                                    <%--<th data-field="certCode" data-align="left" data-width="250">证照编号</th>--%>
                                    <%--<th data-field="certName" data-align="left" data-width="250">证照名称</th>--%>
                                <%--</tr>--%>
                            <%--</thead>--%>
                        <%--</table>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<!-- 列表区域end -->--%>
            <%--<div class="modal-footer" style="padding: 10px;height: 60px;">--%>
                <%--<button type="button" class="btn btn-info" id="save_item_cert_btn">保存</button>--%>
                <%--<button type="button" class="btn btn-secondary" onclick="closeItemCertDialog();">关闭</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<script type="text/javascript">--%>

    <%--var overAllCertIds = [];--%>
    <%--var overAllCertNames = []--%>
    <%--$(function(){--%>

        <%--$('#item_cert_list_tb_scroll').niceScroll({--%>

            <%--cursorcolor: "#e2e5ec",//#CC0071 光标颜色--%>
            <%--cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0--%>
            <%--cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0--%>
            <%--touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备--%>
            <%--cursorwidth: "4px", //像素光标的宽度--%>
            <%--cursorborder: "0", //   游标边框css定义--%>
            <%--cursorborderradius: "2px",//以像素为光标边界半径--%>
            <%--autohidemode: true  //是否隐藏滚动条--%>
        <%--});--%>

        <%--$("#save_item_cert_btn").click(function (e) {--%>

            <%--//获取选中的数据--%>
            <%--var datas = $('#item_cert_list_tb').bootstrapTable('getSelections');--%>
            <%--examineItemCert(e.type, datas);--%>
            <%--saveItemCert();--%>
        <%--});--%>
    <%--});--%>

    <%--function vieItemCert(){--%>

        <%--overAllCertIds = [];--%>
        <%--overAllCertNames = [];--%>
        <%--clearSearchItemCert();--%>
        <%--$("#view_item_cert_modal").modal("show");--%>
    <%--}--%>

    <%--// 查询--%>
    <%--function searchItemCert(){--%>

        <%--commonQueryParams = [];--%>
        <%--commonQueryParams.push({'name': 'keyword','value': $('#itemCertKeyword').val()});--%>
        <%--$("#item_cert_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。--%>
        <%--$('#item_cert_list_tb').bootstrapTable('hideColumn', 'certId');--%>
        <%--$("#item_cert_list_tb").bootstrapTable('refresh', commonQueryParams);       //无参数刷新--%>
        <%--$("#item_cert_list_tb").on('load-success.bs.table', function () {--%>
            <%--queryItemCert();--%>
        <%--});--%>
        <%--$('#item_cert_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {--%>

            <%--// 点击时获取选中的行或取消选中的行--%>
            <%--examineItemCert(e.type, $.isArray(rows) ? rows : [rows]);--%>
        <%--});--%>
    <%--}--%>


    <%--//参数设置--%>
    <%--function itemCertParam(params) {--%>

        <%--var pageNum = (params.offset / params.limit) + 1;--%>
        <%--var pagination = {--%>
            <%--page: pageNum,--%>
            <%--pages: pageNum,--%>
            <%--perpage: params.limit--%>
        <%--};--%>
        <%--var sort = {--%>
            <%--field: params.sort,--%>
            <%--sort: params.order--%>
        <%--};--%>
        <%--var queryParam = {--%>
            <%--pagination: pagination,--%>
            <%--sort: sort--%>
        <%--};--%>
        <%--//组装查询参数--%>
        <%--var buildParam = {};--%>
        <%--if (commonQueryParams) {--%>
            <%--for (var i = 0; i < commonQueryParams.length; i++) {--%>
                <%--buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;--%>
            <%--}--%>
            <%--queryParam = $.extend(queryParam, buildParam);--%>
        <%--}--%>
        <%--return queryParam;--%>
    <%--}--%>

    <%--function itemCertResponseData(res){--%>

        <%--return res;--%>
    <%--}--%>

    <%--function saveItemCert() {--%>

        <%--var ids = [];--%>
        <%--if(overAllCertIds!=null&&overAllCertIds.length>0) {--%>
            <%--for (var i = 0; i < overAllCertIds.length; i++) {--%>
                <%--ids.push(overAllCertIds[i]);--%>
            <%--}--%>
        <%--}--%>
        <%--$.ajax({--%>
            <%--url: ctx + '/aea/item/inout/batchSaveItemInoutMatCert.do',--%>
            <%--type: 'POST',--%>
            <%--data:  {--%>
                <%--"itemVerId": currentBusiId,--%>
                <%--"stateVerId": currentStateVerId,--%>
                <%--'isInput': '1',--%>
                <%--'isStateIn':'0',--%>
                <%--'fileType': 'cert',--%>
                <%--'matCertIds': ids.toString(),--%>
            <%--},--%>
            <%--async: false,--%>
            <%--success: function (result) {--%>
                <%--if (result.success) {--%>
                    <%--$("#view_item_cert_modal").modal("hide");--%>
                    <%--swal({--%>
                        <%--text: '保存成功！',--%>
                        <%--type: 'success',--%>
                        <%--timer: 1500,--%>
                        <%--showConfirmButton: false--%>
                    <%--});--%>
                    <%--// 刷新材料证照表单列表--%>
                    <%--searchItemInMatCert();--%>
                <%--}else{--%>
                    <%--swal('错误信息', result.message, 'error');--%>
                <%--}--%>
            <%--},--%>
            <%--error: function(XMLHttpRequest, textStatus, errorThrown) {--%>
                <%--swal('错误信息', XMLHttpRequest.responseText, 'error');--%>
            <%--}--%>
        <%--});--%>
    <%--}--%>

    <%--// 清空查询--%>
    <%--function clearSearchItemCert() {--%>

        <%--//搜索框置空--%>
        <%--$('#itemCertKeyword').val('')--%>
        <%--searchItemCert();--%>
    <%--}--%>

    <%--function queryItemCert() {--%>

        <%--$.ajax({--%>
            <%--url: ctx + '/aea/item/inout/listAeaItemInoutNoPage.do',--%>
            <%--type: 'POST',--%>
            <%--data: {--%>
                <%--'itemVerId': currentBusiId,--%>
                <%--'stateVerId': currentStateVerId,--%>
                <%--'isInput':'1',--%>
                <%--'fileType': 'cert',--%>
            <%--},--%>
            <%--async: false,--%>
            <%--success: function (data) {--%>
                <%--if (data != null && data.length>0) {--%>
                    <%--for(var i=0;i<data.length;i++) {--%>
                        <%--$("#item_cert_list_tb").bootstrapTable("checkBy", {--%>
                            <%--field: 'certId', values: [data[i].certId]--%>
                        <%--});--%>
                    <%--}--%>
                <%--}--%>
            <%--},--%>
            <%--error: function(XMLHttpRequest, textStatus, errorThrown) {--%>
                <%--swal('错误信息', XMLHttpRequest.responseText, 'error');--%>
            <%--}--%>
        <%--});--%>

    <%--}--%>
    <%--function closeItemCertDialog() {--%>

        <%--$('#view_item_cert_modal').modal('hide');--%>
    <%--}--%>

    <%--function examineItemCert(type, datas){--%>

        <%--if(type.indexOf('uncheck')==-1){--%>

            <%--$.each(datas,function(i,v){--%>
                <%--// 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　--%>
                <%--if(overAllCertIds.indexOf(v.certId) == -1){--%>
                    <%--overAllCertIds.push(v.certId);--%>
                    <%--overAllCertNames.push(v.certName);--%>
                <%--}--%>


            <%--});--%>
        <%--}else{--%>

            <%--$.each(datas,function(i,v){--%>
                <%--var j = overAllCertIds.indexOf(v.certId);--%>
                <%--overAllCertIds.splice(j,1);    //删除取消选中行--%>
                <%--overAllCertNames.splice(j,1);--%>
            <%--});--%>
        <%--}--%>
    <%--}--%>

    <%--function checkItemCertOverAllIds(i, row){--%>

        <%--// 因为 判断数组里有没有这个 id--%>
        <%--if($.inArray(row.certId, overAllCertIds)!=-1){--%>
            <%--// 存在则选中--%>
            <%--return {--%>
                <%--checked : true--%>
            <%--}--%>
        <%--}--%>
    <%--}--%>

<%--</script>--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_item_cert_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_item_cert_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_item_cert_modal_title">导入电子证照</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="itemCertKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchItemCert();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchItemCert();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="item_cert_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="item_cert_list_tb"
                                data-toggle="table",
                                data-method="post"
                                data-pagination=true
                                data-page-size="10",
                                data-page-list="[10]",
                                data-click-to-select=true
                                data-side-pagination="server"
                                data-query-params="itemCertParam",
                                data-pagination-show-page-go="true",
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-url="${pageContext.request.contextPath}/aea/cert/listItemNoSelectCertByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                    <th data-field="certCode" data-align="left" data-width="250">证照编号</th>
                                    <th data-field="certName" data-align="left" data-width="250">证照名称</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 列表区域end -->
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="save_item_cert_btn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#view_item_cert_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>