<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>资质列表</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
        #majorInfoTree li a{
            margin-top: 2px;
            padding-top:3px;
        }
        /*.base tbody tr td{*/
            /*padding: 8px 8px!important;*/
        /*}*/
        .fixed-table-container{
            border: 0px solid #ddd !important;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;margin: 0px;">
        <div class="col-xl-5" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                            <h3 class="m-portlet__head-text">
                                资质列表
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-5">
                            <form id="search_root_rel_qual_form" method="post">
                                <input type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                       name="keyword" style="background-color: #f0f0f075;border-color: #c4c5d6;">
                            </form>
                        </div>
                        <div class="col-xl-7">
                            <button type="button" class="btn btn-info" onclick="searchRootRelQual();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearRootRelQual();">清空</button>
                            <button type="button" class="btn btn-info" onclick="addQual();">新增</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDeleteQual();">删除</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <div class="base" style="padding: 0px 5px;">
                        <div id="qualListTbScrollable" style="height:500px;width: 100%;overflow-x: hidden;overflow-y: auto;">
                            <table id="root_rel_qual_list_tb"
                                   data-toggle="table"
                                   data-method="post"
                                   data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                   data-click-to-select=true
                                   data-single-select="true"
                                   data-pagination-detail-h-align="left"
                                   data-pagination-show-page-go="true"
                                   data-page-size="10"
                                   data-page-list="[10,20,30,50,100]",
                                   data-pagination=true
                                   data-side-pagination="server"
                                   data-pagination-detail-h-align="left"
                                   data-query-params="rootRelQualQueryParam"
                                   data-response-handler="rootRelQualResponseData"
                                   data-url="${pageContext.request.contextPath}/supermarket/qual/list.do">
                                <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="left" data-colspan="1" data-width="10"></th>
                                    <th data-field="qualName" data-align="left" data-colspan="1" data-width="200">资质名称</th>
                                    <th data-field="qualCode" data-align="left" data-colspan="1" data-width="200">资质编号</th>
                                    <th data-field="" data-formatter="rootRelQualOperatorFormatter"
                                        data-align="center" data-colspan="1" data-width="130">操作</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-7" style="padding: 0px 8px 0px 2px;overflow-y:hidden">
            <!-- 配置按钮和专业树 -->
            <div id="type_rel_major_list_page" style="width: 100%;height: 100%;">
                <%@include file="qual_rel_major_ztree.jsp"%>
            </div>
        </div>
    </div>

    <%--新增、编辑资质窗口--%>
    <div id="ae_qual_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header" style="padding: 15px;height: 45px;">
                    <h5 class="modal-title" id="ae_qual_modal_title">新增资质</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form id="ae_qual_form" method="post">
                    <div class="modal-body" style="padding: 10px;">
                        <input id="qualId" type="hidden" name="qualId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>资质名称：</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="qualName" value=""  placeholder=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>资质编号：</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="qualCode" value=""  placeholder=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注：</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="memo" rows="3"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button type="submit" class="btn btn-info">保存</button>
                        <button type="button" onclick="closeQualModal();" class="btn btn-secondary">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%--新增、编辑专业窗口--%>
    <div id="ae_major_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header" style="padding: 15px;height: 45px;">
                    <h5 class="modal-title" id="ae_major_modal_title">新增专业</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form id="ae_major_form" method="post">
                    <div class="modal-body" style="padding: 10px;">

                        <input id="majorId" type="hidden" name="majorId" value=""/>
                        <input id="majorQualId" type="hidden" name="qualId" value=""/>
                        <input id="parentMajorId" type="hidden" name="parentMajorId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>专业名称：</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="majorName" value=""  placeholder=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>专业编号：</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="majorCode" value=""  placeholder=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注：</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="memo" rows="3"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button type="submit" class="btn btn-info">保存</button>
                        <button type="button" onclick="javaScript:$('#ae_major_modal').modal('hide');" class="btn btn-secondary">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%--证书定义窗口--%>
    <%@include file="config_qual_cert.jsp"%>

    <%--资质等级类型窗口--%>
    <div id="cfg_qual_level_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="add_share_mat_modal_title" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header" style="padding: 15px;height: 45px;">
                    <h5 class="modal-title" id="cfg_qual_level_modal_title">配置资质等级类型</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form id="cfg_qual_level_form" method="post">
                    <div class="modal-body" style="padding: 10px;">
                        <div class="form-group m-form__group row" style="padding-top: 10px">
                            <div class="col-lg-4"></div>
                            <div class="col-lg-8" id="qualLevelRootLabel">
                                <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                    <%--<input type="radio" value="1" name="qualLevel" checked="checked">甲乙丙级--%>
                                    <%--<span></span>--%>
                                <%--</label>&nbsp;&nbsp;--%>
                                <%--<label class="m-radio m-radio--solid m-radio--brand">--%>
                                    <%--<input type="radio" value="0" name="qualLevel">一二三级--%>
                                    <%--<span></span>--%>
                                <%--</label>--%>
                            </div>
                        </div>
                        <div id="cfg_qual_level_tip" class="form-group m-form__group row" style="display: none">
                            <div class="col-lg-4"></div>&nbsp;&nbsp;
                            <label style=""><font color="red">未保存!</font></label>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button type="button" class="btn btn-info" onclick="saveQualLevelCfg();">保存</button>
                        <button type="button" onclick="javaScript:$('#cfg_qual_level_modal').modal('hide');" class="btn btn-secondary">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!--bootstrap-treegrid-->
    <%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>--%>
    <%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>--%>
    <%--<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>--%>
    <!--bootstrap-table-->
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">

    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/qual/qual_manage.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/qual/qual_rel_major_ztree.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/supermarket_manage/qual/config_qual_cert.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
</body>
</html>