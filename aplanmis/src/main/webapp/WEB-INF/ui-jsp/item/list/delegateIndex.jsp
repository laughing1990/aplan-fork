<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: hidden;">
<head>
    <title>事项下放</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="application/javascript">
        var itemNature = '${itemNature}';
    </script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }

        .selectedItemSortDiv {
            color: #464637;
            font-size: 14px;
            font-family: 'Roboto', sans-serif;
            font-weight: 300;
        }

        /**标题样式**/
        .selectedItemSortDiv .block_list-title {
            padding: 10px;
            text-align: center;
            background: #abcdf1;
        }

        .selectedItemSortDiv ul {
            margin: 0;
            padding: 0;
            list-style: none;
            display: block;
        }

        .selectedItemSortDiv li {
            background-color: #fff;
            padding: 6px 0px;
            display: list-item;
            text-align: -webkit-match-parent;

            /**边框样式**/
            border: 1px solid transparent;
            border-bottom: 1px solid #ebebeb;
        }

        /**
            * 移动到li样式
        */
        .block_ul_td li:hover {
            background: #e7e8eb;
            cursor: move;
            cursor: -webkit-grabbing;
        }

        .selectedItemSortDiv .drag-handle_th {
            text-align: center;
            display: inline-block;
            width: 8%;
            font-weight: 600;
        }

        /**拖拽手把**/
        .selectedItemSortDiv .drag-handle_td {
            text-align: center;
            font: bold 16px Sans-Serif;
            color: #5F9EDF;
            display: inline-block;
            width: 8%;
        }

        .selectedItemSortDiv .ostage_name_td{
            display: inline-block;
            width: 45%;
        }

        #selectItemTree::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        #selectItemTree::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        #selectItemTree::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        #selectItemTree::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        #selectItemTree::-webkit-scrollbar-corner{
            background: #fff;
        }

        #selectedItemSortDiv::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        #selectedItemSortDiv::-webkit-scrollbar-corner{
            background: #fff;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-7" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 5px;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
					    <span class="m-portlet__head-icon m--hide">
						    <i class="la la-gear"></i>
					    </span>
                        <h3 class="m-portlet__head-text">
                            事项下放&nbsp;&nbsp;
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-2" style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addMultiplyPriv();">批量下放</button>
                        </div>
                        <div class="col-md-10" style="padding: 0px;">
                            <form id="search_all_item_list_form" method="post">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-5">
                                        <div class="form-group m-form__group row" >
                                            <label class="col-lg-4 col-form-label" style="text-align: right;">部门名称:</label>
                                            <div class="col-lg-8 input-group">
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
                                    <div class="col-4" style="text-align: right;">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												<span><i class="la la-search"></i></span>
											</span>
                                        </div>
                                    </div>

                                    <div class="col-3"  style="text-align: center;">
                                        <button type="button" class="btn btn-info" onclick="searchAllItemList();">查询</button>
                                        <button type="button" class="btn btn-secondary" onclick="clearSearchAllItemList();">清空</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="all_item_list_tb"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>

    <input type="hidden" name="itemVerId" value=""/>

    <div class="col-xl-5" style="padding: 0px 2px 0px 8px;">
        <div class="m-portlet" style="margin-bottom: 5px;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
					    <span class="m-portlet__head-icon m--hide">
						    <i class="la la-gear"></i>
					    </span>
                        <h3 class="m-portlet__head-text">
                            事项下放地区/机构
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-2" style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="deleteAllPriv();">批量删除</button>
                        </div>
                        <div class="col-md-10" style="padding: 0px;">
                            <form id="search_priv_list_form" method="post">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-4"></div>
                                    <div class="col-5" style="text-align: right;">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="privKeyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												<span><i class="la la-search"></i></span>
											</span>
                                        </div>
                                    </div>
                                    <div class="col-3"  style="text-align: center;">
                                        <button type="button" class="btn btn-info" onclick="searchPrivList();">查询</button>
                                        <button type="button" class="btn btn-secondary" onclick="clearSearchPrivList();">清空</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="all_priv_list_tb"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

<!-- 添加 事件下放信息 -->
<%@include file="add_item_priv.jsp"%>

<!-- 批量事项下放 -->
<%@include file="add_multiply_priv.jsp"%>

<!-- 选择组织 -->
<%@include file="../../common/ztree/opus_om_org_ztree.jsp"%>
<%@include file="../../common/ztree/item_priv_org_ztree.jsp"%>
<%@include file="../../common/ztree/priv_bsc_dic_region_ztree.jsp"%>

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

<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>

<!--bootstrap-table-->
<link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/common/css/bootstrap/bootstrap-table.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/ui-static/item/list/js/delegate_index.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/item/list/js/add_item_basic.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/item/list/js/add_item_priv.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/item_priv_org_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/priv_bsc_dic_region_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/item/list/js/add_multiply_priv.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>