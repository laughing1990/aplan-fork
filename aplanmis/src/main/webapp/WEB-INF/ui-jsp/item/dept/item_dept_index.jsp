<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>部门事项管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/PinYin.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        .form-group label{
            display: block;
            float: left;
            position: relative;
        }
        .form-group input[type="file"]{
            position: absolute;
            width: 10%;
            opacity: 0;
        }
        .form-group .custorm-style{
            display: block;
            width: 100%;
            height: 38px;
            border: 1px solid #d9d9d9;
            border-radius: 4px;
        }
        .form-group .custorm-style .left-button{
            width: 71px;
            font-size: 13px !important;
            height: 22px;
            line-height: 13px;
            float: left;
            border:1px solid #b1b1b1;
            background: linear-gradient(to bottom, #fff, #ccc);
            color: #444;
            margin-top: 0.9%;
            margin-left: 1%;
        }
        .form-group .custorm-style .right-text{
            width: 88%;
            height: 99%;
            line-height: 2.7em;
            display: block;
            overflow: hidden;
        }
    </style>
    <script type="text/javascript">
        var orgIds = [];
        <c:forEach items="${userOrgs}" var="userOrg">
           orgIds.push('${userOrg.orgId}');
        </c:forEach>
    </script>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 5px;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                               <span class="m-portlet__head-icon m--hide">
                                   <i class="la la-gear"></i>
                               </span>
                            <h3 class="m-portlet__head-text">
                                部门事项列表
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-3"style="text-align: left;">
                                <button type="button" class="btn btn-info"
                                        onclick="addOrgItem(true, false);">新增实施</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="refreshAllItemList();">刷新</button>
                            </div>
                            <div class="col-md-9" style="padding: 0px;">
                                <form id="search_org_rel_item_form" method="post">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-7">
                                            <div class="form-group m-form__group row" >
                                                <label class="col-lg-2 col-form-label" style="text-align: right;">部门名称:</label>
                                                <div class="col-lg-4 input-group">
                                                    <%--<select id="searchOrgId" type="text" class="form-control" name="orgName" value="">--%>
                                                        <%--<option value="">请选择</option>--%>
                                                        <%--<c:forEach items="${userOrgs}" var="userOrg">--%>
                                                            <%--<option value="${userOrg.orgName}">${userOrg.orgName}</option>--%>
                                                        <%--</c:forEach>--%>
                                                    <%--</select>--%>

                                                    <select id="searchOrgId" type="text" class="form-control" name="orgId" value="">
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${userOrgs}" var="userOrg">
                                                            <option value="${userOrg.orgId}">${userOrg.orgName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <label class="col-lg-2 col-form-label" style="text-align: right;">事项类型:</label>
                                                <div class="col-lg-4">
                                                    <select id="searchItemType" type="text" class="form-control m-input"
                                                            name="itemType" value="">
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${itemTypes}" var="itemTypeVo">
                                                            <option value="${itemTypeVo.itemCode}">${itemTypeVo.itemName}</option>
                                                        </c:forEach>
                                                    </select>
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
                                                    onclick="searchAllItemList();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchOrgRelItem();">清空</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                        <table  id="org_rel_item_tb"></table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
        </div>
    </div>

    <!-- 添加/编辑 事项信息 -->
    <%@include file="add_dept_item_basic.jsp"%>

    <!-- 事项版本 -->
    <%@include file="../list/item_ver_index.jsp"%>

    <!-- 操作指南 -->
    <%@include file="../list/item_ver_opera_index.jsp"%>

    <!-- 选择行政区划 -->
    <%@include file="../../common/ztree/bsc_dic_region_ztree.jsp"%>

    <!-- 选择组织 -->
    <%@include file="../../common/ztree/opus_om_org_ztree.jsp"%>

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
    <script src="${pageContext.request.contextPath}/ui-static/item/dept/item_dept_index.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/bsc_dic_region_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/item/dept/add_dept_item_basic.js?<%=isDebugMode%>" type="text/javascript"></script>


    <script type="text/javascript">
        function itemTypeFormatter(value, row, index){

            <c:forEach items="${itemTypes}" var="itemTypeVo">
            if(row.itemType=='${itemTypeVo.itemCode}'){
                return '${itemTypeVo.itemName}';
            }
            </c:forEach>
        }

        function itemStatusFormatter(value, row, index){
            <c:forEach items="${itemStatus}" var="itemStatusVo">
            if(row.sxmlzt=='${itemStatusVo.itemCode}'){
                return '${itemStatusVo.itemName}';
            }
            </c:forEach>
        }
    </script>
</body>
</html>