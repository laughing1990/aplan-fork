<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>服务窗口</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/mat/global/css/global_material_index.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                服务窗口
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-4"style="text-align: left;">
                                <button type="button" class="btn btn-info" onclick="addServiceWindow();">新增</button>
                                <button type="button" class="btn btn-info" onclick="addUserWindow();">窗口人员</button>
                                <button type="button" class="btn btn-info" onclick="addWindowItem();">窗口事项</button>
                                <button type="button" class="btn btn-info" onclick="addStageWindow();">窗口阶段</button>
                                <button type="button" class="btn btn-secondary" onclick="batchDelServiceWindow();">删除</button>
                                <button type="button" class="btn btn-secondary" onclick="refreshServiceWindow();">刷新</button>
                            </div>
                            <div class="col-md-8" style="padding: 0px;">
                                <form id="search_service_window_form" method="post">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-7">
                                            <div class="form-group m-form__group row" >
                                                <label class="col-lg-1 col-form-label" style="text-align: right;">地区:</label>
                                                <div class="col-lg-5 input-group">
                                                    <input type="hidden" name="regionId" value=""/>
                                                    <input type="text" class="form-control m-input" name="regionName"
                                                           placeholder="请点击选择" aria-describedby="select_region_Id2" readonly
                                                           onclick="isSelectBscDicRegion(this, true);">
                                                    <div class="input-group-append">
                                                        <span id="select_region_Id2" class="input-group-text"
                                                              onclick="isSelectBscDicRegion(null, true);">
                                                            <i class="la la-search"></i>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-lg-1 col-form-label" style="text-align: right;">部门:</label>
                                                <div class="col-lg-5 input-group">
                                                    <input type="hidden" name="orgId" value=""/>
                                                    <input type="text" class="form-control m-input" name="orgName"
                                                           placeholder="请点击选择" aria-describedby="select_org_Id2" readonly
                                                           onclick="isSelectOpuOmOrg(this, true);">
                                                    <div class="input-group-append">
                                                        <span id="select_org_Id2" class="input-group-text" onclick="isSelectOpuOmOrg(null, true);">
                                                            <i class="la la-group"></i>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-3" style="text-align: right;">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </div>

                                        <div class="col-2"  style="text-align: center;">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchServiceWindow();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchServiceWindow();">清空</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <div class="base" style="padding: 0px 5px;">
                        <table id="serviceWindowTable"
                               data-toggle="table"
                               data-method="post"
                               data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                               data-single-select=true
                               data-click-to-select=true
                               data-pagination-detail-h-align="left"
                               data-pagination-show-page-go="true"
                               data-page-size="10"
                               data-page-list="[10,20,30,50,100]"
                               data-pagination=true
                               data-side-pagination="server"
                               data-query-params="dealQueryParams"
                               data-url="${pageContext.request.contextPath}/aea/service/window/listAeaServiceWindowByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                                    <th data-field="windowName" data-colspan="1" data-width="200">窗口名称</th>
                                    <th data-field="regionName" data-colspan="1" data-width="100">所属地区</th>
                                    <th data-field="orgName" data-colspan="1" data-width="150">所属部门</th>
                                    <th data-field="linkPhone" data-colspan="1" data-width="120">联系方式</th>
                                    <th data-field="workTime" data-colspan="1" data-width="120">办公时间</th>
                                    <th data-field="windowAddress" data-colspan="1" data-width="200">窗口地址</th>
                                    <th data-field="_operator" data-align="center" data-colspan="1"
                                        data-width="130" data-formatter="operatorFormatter">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 窗口新增/编辑页面 -->
    <%@include file="aedit_service_window_index.jsp"%>

    <!-- 附件 -->
    <%@include file="show_service_wind_att.jsp"%>

    <!-- 选择行政区划 -->
    <%@include file="../../common/ztree/bsc_dic_region_ztree.jsp"%>

    <!-- 选择组织 -->
    <%@include file="../../common/ztree/opus_om_org_ztree.jsp"%>

    <!-- 窗口人员 -->
    <%@include file="../../common/ztree/select_opu_om_user_ztree.jsp"%>

    <!-- 窗口事项配置 -->
    <%@include file="../../common/ztree/select_aea_item_ztree.jsp"%>

    <!-- 窗口阶段配置 -->
    <%@include file="../../common/ztree/select_aea_stage_ztree.jsp"%>

    <!-- 进度弹窗 -->
    <div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_item_dept" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
            <div class="modal-content">
                <div class="modal-body" style="text-align: center;padding: 10px;">
                    <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                    <div id="uploadProgressMsg" style="padding-top: 5px;">数据修复中，请稍后...</div>
                </div>
            </div>
        </div>
    </div>

    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/service_window_index.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/bsc_dic_region_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
</body>
</html>