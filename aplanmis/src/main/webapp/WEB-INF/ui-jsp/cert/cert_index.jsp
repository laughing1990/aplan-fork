<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>电子证照定义库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-4" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                            <h3 class="m-portlet__head-text">
                                电子证照库
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-5">
                            <input id="certInfoTreeKeyWord" type="text"
                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xl-7">
                            <button type="button" class="btn btn-info" onclick="searchCertInfoTreeNode();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchCertInfoTreeNode();">清空</button>
                            <button type="button" class="btn btn-secondary" onclick="expandCertInfoTreeAllNode();">展开</button>
                            <button type="button" class="btn btn-secondary" onclick="collapseCertInfoTreeAllNode();">折叠</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <div id="certInfoTree" class="ztree" style="overflow: auto;"></div>
                </div>
            </div>
        </div>

        <div class="col-xl-8" style="padding: 0px 8px 0px 2px;">
            <!-- 根路径分类列表 -->
            <div id="root_rel_cert_type_list_page" style="display: none;width: 100%;height: 100%;">
                <%@include file="root_rel_cert_type_list_page.jsp"%>
            </div>
            <!-- 新增、编辑分类 -->
            <div id="ae_cert_type_page" style="display: none;width: 100%;height: 100%;">
                <%@include file="ae_cert_type_page.jsp"%>
            </div>
            <!-- 分类相关证照列表 -->
            <div id="type_rel_cert_list_page" style="display: none;width: 100%;height: 100%;">
                <%@include file="type_rel_cert_list_page.jsp"%>
            </div>
            <!-- 新增、编辑证照 -->
            <div id="ae_cert_rel_page" style="display: none;width: 100%;height: 100%;">
                <%@include file="ae_cert_page.jsp"%>
            </div>
        </div>
    </div>

    <!-- 证照右键分类 右键菜单使用规则 div 加上class="contextMenuDiv" 并赋值一个id -->
    <div id="rootCertTypeContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="addChildCertType();">
            <i class="fa flaticon-plus"></i>新增分类
        </a>
    </div>

    <!-- 证照右键分类 右键菜单使用规则 div 加上class="contextMenuDiv" 并赋值一个id -->
    <div id="certTypeContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="addChildCertType();">
            <i class="fa flaticon-plus"></i>新增子分类
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="addChildCertInfo();">
            <i class="fa flaticon-plus"></i>新增证照
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="editCertType();">
            <i class="fa flaticon-edit-1"></i>编辑分类
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="deleteCertType();">
            <i class="fa fa-times"></i>删除分类
        </a>
        <%--<a href="javascript:void(0);" class="list-group-item" onclick="moveCertType();">--%>
            <%--<i class="fa flaticon-edit-1"></i>移动分类--%>
        <%--</a>--%>
    </div>

    <!-- 证照右键 -->
    <div id="certContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="deleteCertInfo();">
            <i class="fa fa-times"></i>删除证照
        </a>
        <%--<a href="javascript:void(0);" class="list-group-item" onclick="moveCert();">--%>
            <%--<i class="fa flaticon-edit-1"></i>移动证照--%>
        <%--</a>--%>
    </div>

    <!-- 选择文件库 -->
    <%@include file="../aplanmis/cert/select_bsc_att_dir_ztree.jsp"%>

    <script src="${pageContext.request.contextPath}/ui-static/cert/cert_index.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/cert/root_rel_cert_type_list.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/cert/child_cert_type_list.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/cert/type_rel_cert_list.js" type="text/javascript"></script>
</body>
</html>