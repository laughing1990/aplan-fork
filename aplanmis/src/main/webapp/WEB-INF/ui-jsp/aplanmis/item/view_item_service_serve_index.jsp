<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>许可服务</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }

        .fixed-table-body::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        .fixed-table-body::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        .fixed-table-body::-webkit-scrollbar-corner{
            background: #fff;
        }
        /*修改对话框样式*/
        @media (min-width: 992px) {
            .modal-lg {
                max-width: 90%; } }

        @media (min-width: 992px) {
            .modal-window {
                max-width: 60%; } }
    </style>
</head>
<body>
<div id="mainServiceServeContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            办理（许可）服务
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                        </div>

                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-3"></div>
                                <div class="col-6">
                                    <form id="search_form_item_service_serve" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value="" id="item_service_serve_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												   <span><i class="la la-search"></i></span>
											   </span>
                                        </div>
                                        <input type="hidden" name="itemBasicId" value="${itemBasicId}"/>
                                    </form>
                                </div>
                                <div class="col-3">
                                    <button type="button" class="btn btn-info" onclick="searchServiceServe();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchServiceServe();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="serviceServeTable"
                           data-toggle="table",
                           data-click-to-select=true,
                           queryParamsType="",
                           data-pagination=true,
                           data-page-size="10",
                           data-side-pagination="server",
                           data-page-list="[10, 20, 50, 100]",
                           data-side-pagination="server",
                           data-query-params="dealQueryParams",
                           data-pagination-show-page-go="true",
                           data-url="${pageContext.request.contextPath}/aea/item/service/serve/listAeaItemServiceServeByPage.do">
                        <thead>
                        <tr>
                            <th data-field="ywxtmc" data-colspan="1" data-width="150">业务系统名称</th>
                            <th data-field="ckfw" data-colspan="1" data-width="200">窗口名称</th>
                            <th data-field="netHallCode" data-colspan="1" data-width="120">网厅编号</th>
                            <th data-field="bjlx" data-colspan="1" data-width="300" data-formatter="itemTypeFormatter">办件类型</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!--新增,编辑办理服务 -->
<div class="modal fade" id="dialog_item_service_serve" tabindex="-1" role="dialog" aria-labelledby="dialog_service_serve" aria-hidden="true" style="text-align:right; padding-left: 50px;overflow-y: scroll">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_service_serve_title">新增许可服务</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body" style="padding: 15px;">
                <form id="form_service_serve" method="post" enctype="multipart/form-data">
                    <div class="m-portlet__body" id="dialog_service_serve_body" style="padding: 15px;overflow-y:auto;overflow-x: hidden">
                        <input type="hidden" id="serviceServeId" name="serviceServeId" value="" />
                        <input type="hidden" id="itemBasicId" name="itemBasicId" value=""/>
                        <input type="hidden" id="ckfw" name="ckfw" value=""/>
                        <input type="hidden" id="ckfwIds" name="ckfwIds" value=""/>
                        <center>
                            <table border="1"  width="95%" style="text-align: left;border: #C7E4F6">
                                <tr style="background-color: #EBFFFF;">
                                    <td colspan="4" style="text-align: center; border-right-color: #EBFFFF">许可服务</td>
                                    <td style="text-align: right"><button type="button" class="btn btn-info btn-xs">备注说明</button></td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">窗口服务<font style="color: #FABB1A;">☆</font></td>
                                    <td colspan="4">
                                        <table border="1" style="text-align: center;border: #C7E4F6; width: 100%; margin: 2px;">
                                            <tr style="background-color: #EBFFFF">
                                                <td>序号</td>
                                                <td>窗口名称</td>
                                                <td>窗口地址</td>
                                                <td>窗口电话</td>
                                                <td>办公时间</td>
                                                <td>
                                                    <span id="select_window_Id" class="btn btn-info btn-sm" onclick="showWindowModal();">
                                                        窗口库
                                                    </span>
                                                </td>
                                            </tr>
                                            <tbody id="serviceWindow"></tbody>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">运行系统层级</td>
                                    <td colspan="4">
                                        <select id="xtjsf" name="xtjsf" class="form-control">
                                            <option value="">--请选择--</option>
                                            <option value="none">非垂直系统</option>
                                            <option value="week">周</option>
                                            <option value="year">年</option>
                                        </select>
                                        （注：支撑政府服务事项办理的信息管理系统，选择是否国家、省、市级的垂直系统）
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">业务系统名称</td>
                                    <td colspan="4">
                                        <textarea id="ywxtmc" name="ywxtmc" rows="1" class="form-control m-input" placeholder="请输入业务系统名称..."></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">是否需要身份证</td>
                                    <td colspan="2">
                                        <input type="radio" name="sfxysfyz" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="sfxysfyz" value="1">是
                                    </td>
                                    <td style="background-color: #EBFFFF">身份验证是否需要CA</td>
                                    <td>
                                        <input type="radio" name="sfyzsfxyca" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="sfyzsfxyca" value="1">是
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">是否涉外</td>
                                    <td colspan="4">
                                        <input type="radio" name="casfsw" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="casfsw" value="1">是
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">通办范围</td>
                                    <td colspan="4">
                                        <select id="tbfw" name="tbfw" class="form-control">
                                            <option value="">--请选择--</option>
                                            <option value="week">周</option>
                                            <option value="year">年</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">网厅编码</td>
                                    <td colspan="4">
                                        <input type="text" class="form-control m-input" id="netHallCode" name="netHallCode" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF" rowspan="7">网上服务</td>
                                    <td style="background-color: #EBFFFF">网上办事深度</td>
                                    <td>
                                        <select id="wsfwsd" name="wsfwsd" class="form-control">
                                            <option value="">--请选择--</option>
                                            <option value="none">无级</option>
                                            <option value="week">周</option>
                                            <option value="year">年</option>
                                        </select>
                                    </td>
                                    <td style="background-color: #EBFFFF">到现场次数</td>
                                    <td>
                                        <select id="dccs" name="dccs" class="form-control">
                                            <option value="">--请选择--</option>
                                            <option value="0">0</option>
                                            <option value="week">周</option>
                                            <option value="year">年</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">在线申办服务</td>
                                    <td>
                                        <input type="radio" name="zxsbfw" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="zxsbfw" value="1">是
                                    </td>
                                    <td>
                                        <font id="zxsbfwdz_title" style="visibility: hidden">在线申办服务地址</font>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control m-input" id="zxsbfwdz" name="zxsbfwdz" value=""  placeholder="请输入在线申办服务地址..." style="visibility: hidden"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">网上咨询服务</td>
                                    <td>
                                        <input type="radio" name="wszxfw" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="wszxfw" value="1">是
                                    </td>
                                    <td>
                                        <font id="wszxfwdz_title" style="visibility: hidden">网上咨询服务地址</font>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control m-input" id="wszxfwdz" name="wszxfwdz" value=""  placeholder="请输入网上咨询服务地址..." style="visibility: hidden"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">进度查询服务</td>
                                    <td>
                                        <input type="radio" name="jdcxfw" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="jdcxfw" value="1">是
                                    </td>
                                    <td>
                                        <font id="jdcxfwdz_title" style="visibility: hidden">进度查询服务地址</font>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control m-input" id="jdcxfwdz" name="jdcxfwdz" value=""  placeholder="请输入进度查询服务地址..." style="visibility: hidden"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">结果查询服务</td>
                                    <td>
                                        <input type="radio" name="jgcxfw" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="jgcxfw" value="1">是
                                    </td>
                                    <td>
                                        <font id="jgcxfwdz_title" style="visibility: hidden">结果查询服务地址</font>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control m-input" id="jgcxfwdz" name="jgcxfwdz" value=""  placeholder="请输入结果查询服务地址..." style="visibility: hidden"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">网上支付</td>
                                    <td colspan="3">
                                        <input type="radio" name="payonline" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="payonline" value="1">是
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">物流快递</td>
                                    <td colspan="3">
                                        <input type="radio" name="express" value="0" checked="checked">否&nbsp;&nbsp;
                                        <input type="radio" name="express" value="1">是
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">预约服务</td>
                                    <td colspan="4">
                                        <textarea rows="1" id="yyfw" name="yyfw" class="form-control m-input" placeholder="请填写具体的预约方式"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">为申请人提供帮助</td>
                                    <td colspan="4">
                                        <textarea id="applicantAid" name="applicantAid" rows="1" class="form-control m-input" placeholder="实施机关应指导申请人填写申请材料，对格式文本填写错误的，允许申请人更正"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #EBFFFF">办件类型</td>
                                    <td colspan="4">
                                        <c:forEach items="${itemPropertys}" var="itemProperty">
                                            <input type="checkbox"  name="bjlx" value="${itemProperty.itemCode}"/>${itemProperty.itemName}&nbsp;&nbsp;
                                        </c:forEach>
                                    </td>
                            </table>
                            <div class="form-group m-form__group row" style="padding-top: 5px;">
                                <div class="col-lg-12">
                                    <%--<button type="button" class="btn btn-info" data-dismiss="modal">暂存</button>--%>
                                    <button type="button" class="btn btn-info" onclick="submitFormHandler();">保存</button>
                                    <%--<button type="button" class="btn btn-info" data-dismiss="modal">提交审查</button>--%>
                                    <button type="button" class="btn btn-default" data-dismiss="modal" aria-label="Close">关闭</button>
                                </div>
                            </div>
                        </center>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 服务窗口选择 -->
<div class="modal fade" id="dialog_item_select_window" tabindex="-1" role="dialog" aria-labelledby="dialog_select_window" aria-hidden="true" style="text-align:center; padding-left: 15px;">
    <div class="modal-dialog modal-dialog-centered  modal-window" role="document">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_select_window_title">添加服务窗口（点击窗口添加）</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body" style="padding: 15px;">
                <table id="selectServiceWindow"
                       data-toggle="table",
                       data-click-to-select=true,
                       queryParamsType="",
                       data-pagination=true,
                       data-page-size="10",
                       data-side-pagination="server",
                       data-page-list="[10, 20, 50, 100]",
                       data-side-pagination="server",
                       data-query-params="dealQueryParams",
                       data-pagination-show-page-go="true",
                       data-url="${pageContext.request.contextPath}/aea/item/service/window/listAeaItemServiceWindowByPage.do">
                    <thead>
                    <tr>
                        <th data-field="windowName" data-colspan="1">窗口名称</th>
                        <th data-field="windowAddress" data-colspan="1">窗口地址</th>
                        <th data-field="workTime" data-colspan="1">工作时间</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-default" data-dismiss="modal" aria-label="Close">关闭</button>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_serve_index.js" type="text/javascript"></script>
</body>
</html>