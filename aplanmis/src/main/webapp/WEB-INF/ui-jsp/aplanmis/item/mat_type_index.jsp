<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>材料类型定义库</title>
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

        /*#matTypeTree::-webkit-scrollbar{*/
            /*width:4px;*/
            /*height:4px;*/
        /*}*/
        /*#matTypeTree::-webkit-scrollbar-track{*/
            /*background: #fff;*/
            /*border-radius: 2px;*/
        /*}*/
        /*#matTypeTree::-webkit-scrollbar-thumb{*/
            /*background: #e2e5ec;*/
            /*border-radius:2px;*/
        /*}*/
        /*#matTypeTree::-webkit-scrollbar-thumb:hover{*/
            /*background: #aaa;*/
        /*}*/
        /*#matTypeTree::-webkit-scrollbar-corner{*/
            /*background: #fff;*/
        /*}*/

        /*.fixed-table-body::-webkit-scrollbar{*/
            /*width:4px;*/
            /*height:4px;*/
        /*}*/
        /*.fixed-table-body::-webkit-scrollbar-track{*/
            /*background: #fff;*/
            /*border-radius: 2px;*/
        /*}*/
        /*.fixed-table-body::-webkit-scrollbar-thumb{*/
            /*background: #e2e5ec;*/
            /*border-radius:2px;*/
        /*}*/
        /*.fixed-table-body::-webkit-scrollbar-thumb:hover{*/
            /*background: #aaa;*/
        /*}*/
        /*.fixed-table-body::-webkit-scrollbar-corner{*/
            /*background: #fff;*/
        /*}*/
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
                            材料类别定义库
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="matTypeTreeKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="searchMatTypeTreeNode();">查询</button>
                        <button type="button" class="btn btn-secondary" onclick="clearSearchMatTypeTreeNode();">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandMatTypeTreeAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseMatTypeTreeAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="matTypeTree" class="ztree" style="overflow: auto;"></div>
            </div>
        </div>
    </div>

    <div class="col-xl-8" style="padding: 0px 8px 0px 2px;">
        <%@include file="mat_type_list_page.jsp"%>
    </div>
</div>

<%@include file="ae_mat_type_page.jsp"%>

<!-- 右键分类 右键菜单使用规则 div 加上class="contextMenuDiv" 并赋值一个id -->
<div id="rootMatTypeContextMenu" class="contextMenuDiv">
    <a href="javascript:void(0);" class="list-group-item" onclick="addMatType();">
        <i class="fa flaticon-plus"></i>新增分类
    </a>
</div>

<!-- 右键分类 右键菜单使用规则 div 加上class="contextMenuDiv" 并赋值一个id -->
<div id="matTypeContextMenu" class="contextMenuDiv">
    <a href="javascript:void(0);" class="list-group-item" onclick="addMatType();">
        <i class="fa flaticon-plus"></i>新增分类
    </a>
    <a href="javascript:void(0);" class="list-group-item" onclick="editMatType();">
        <i class="fa flaticon-edit-1"></i>编辑
    </a>
    <a href="javascript:void(0);" class="list-group-item" onclick="deleteMatType();">
        <i class="fa fa-times"></i>删除
    </a>
</div>

<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/mat_type_index.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
</body>
</html>