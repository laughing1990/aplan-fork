<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: hidden;">
<head>
    <title>用户事项管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/PinYin.js" type="text/javascript"></script>
    <style type="text/css">
        .row {

            display: -ms-flexbox;
        }

        .col-xl-4 {

             flex: 0 0 29%;
             max-width: 29%;
        }

        .col-xl-8{

            flex: 0 0 71%;
            max-width: 71%;
        }

        .btn-group > .btn:not(:last-child):not(.dropdown-toggle), .btn-group > .btn-group:not(:last-child) > .btn {
            border-top-right-radius: 5;
            border-bottom-right-radius: 5;
        }
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div id="westPanel" class="col-xl-4" style="padding: 0px 2px 0px 8px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                            <i class="la la-gear"></i>
                        </span>
                        <h3 class="m-portlet__head-text">
                           组织用户
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectUserKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectUserNode();">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSearchSelectUserNode();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandSelectUserAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseSelectUserAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 5px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="selectUserTree" class="ztree" style="overflow: auto;"></div>
            </div>
        </div>
    </div>

    <!-- 用户事项 -->
    <div class="col-xl-8" style="padding: 0px 8px 0px 2px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                       <span class="m-portlet__head-icon m--hide">
                           <i class="la la-gear"></i>
                       </span>
                        <h3 class="m-portlet__head-text">
                            用户事项
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4" style="text-align: left;">
                            <button type="button" class="btn btn-info"
                                    onclick="importUserItem();">导入事项</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="batchDelUserItem();">移除</button>
                            <div class="btn-group m-btn-group" role="group">
                                <button id="btnGroupDrop1" type="button" class="btn btn-secondary"
                                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    其他操作&nbsp;<i class="la la-angle-down"></i>
                                </button>
                                <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                                    <a class="dropdown-item" href="#" onclick="syncLocalAeaItemGuide();">
                                        <i class="la la-file-text-o"></i>同步本地指南
                                    </a>
                                    <a class="dropdown-item" href="#" onclick="initItemVerSeq();">
                                        <i class="la la-file-text-o"></i>修复版本序列
                                    </a>
                                    <a class="dropdown-item" href="#" onclick="syncItemRegion();">
                                        <i class="la la-file-text-o"></i>同步事项行政区域
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <form id="search_all_item_list_form" method="post">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-5">
                                        <div class="form-group m-form__group row">
                                            <label class="col-lg-4 col-form-label" style="text-align: right;">事项类型:</label>
                                            <div class="col-lg-8">
                                                <select id="searchItemNature" type="text" class="form-control m-input"
                                                        name="itemNature" value="">
                                                    <option value="">请选择</option>
                                                    <option value="0">行政事项</option>
                                                    <option value="9">服务协同</option>
                                                    <option value="8">中介服务事项</option>
                                                    <option value="6">市政公用服务</option>
                                                </select>
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

                                    <div class="col-3" style="text-align: center;">
                                        <button type="button" class="btn btn-info"
                                                onclick="searchAllItemList();">查询
                                        </button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="clearSearchAllItemList();">清空
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 5px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 5px">
                    <table id="all_item_list_tb"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 事项版本 -->
<%@include file="item_user_ver_index.jsp" %>

<!-- 添加/编辑 事项信息 -->
<%@include file="aedit_user_item_basic.jsp" %>

<!-- 操作指南 -->
<%@include file="item_user_ver_opera.jsp" %>

<!-- 选择行政区划 -->
<%@include file="../../common/ztree/bsc_dic_region_ztree.jsp" %>

<!-- 选择组织 -->
<%@include file="../../common/ztree/opus_om_org_ztree.jsp" %>

<!-- 选择事项 -->
<%@include file="select_user_item_ztree.jsp"%>

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
<script src="${pageContext.request.contextPath}/ui-static/item/user/item_user.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/item/user/select_opu_om_user.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/bsc_dic_region_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/item/user/aedit_user_item_basic.js" type="text/javascript"></script>
<script type="text/javascript">

    function itemTypeFormatter(value, row, index) {

        <c:forEach items="${itemTypes}" var="itemTypeVo">
        if (row.itemType == '${itemTypeVo.itemCode}') {
            return '${itemTypeVo.itemName}';
        }
        </c:forEach>
    }

    function itemStatusFormatter(value, row, index) {
        <c:forEach items="${itemStatus}" var="itemStatusVo">
        if (row.sxmlzt == '${itemStatusVo.itemCode}') {
            return '${itemStatusVo.itemName}';
        }
        </c:forEach>
    }
</script>
</body>
</html>