<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>标准材料库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        label {
            text-align: right;
        }

        .ztree li span.button.noline_open {
            background-position: -92px -72px;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 5px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <!-- 标题信息 -->
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">标准材料库（<span style="color: red;">* 点击节点右键操作</span>）</h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-3">
                            <input id="keyWord" type="text"
                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xl-5">
                            <button type="button" class="btn btn-info" onclick="searchTypeMat();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchTypeMat();">清空</button>
                            <button type="button" class="btn btn-secondary" onclick="expandAll();">展开</button>
                            <button type="button" class="btn btn-secondary" onclick="collapseAll();">折叠</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 树列表区域 -->
                    <div id="stdTypeMatTree" class="ztree" style="overflow: auto;"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- 根节点右击菜单 -->
    <div id="rootNodeContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="addMatType();">
            <i class="fa flaticon-plus"></i>新增类型
        </a>
    </div>

    <!-- 类型节点右击菜单 -->
    <div id="matTypeNodeContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="addMatType();">
            <i class="fa flaticon-plus"></i>新增子类型
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="addStdMat();">
            <i class="fa flaticon-plus"></i>新增标准材料
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="editMatType();">
            <i class="fa flaticon-edit-1"></i>编辑分类
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="deleteMatType();">
            <i class="fa fa-times"></i>删除分类
        </a>
    </div>

    <!-- 材料节点右键菜单 -->
    <div id="matNodeContextMenu" class="contextMenuDiv">
        <a href="javascript:void(0);" class="list-group-item" onclick="editStdMat();">
            <i class="fa flaticon-edit-1"></i>编辑标准材料
        </a>
        <a href="javascript:void(0);" class="list-group-item" onclick="deleteStdMat();">
            <i class="fa fa-times"></i>删除标准材料
        </a>
    </div>

    <!-- 新增编辑标准材料类型 -->
    <%@include file="aedit_stdmat_type.jsp"%>

    <!-- 新增编辑标准材料 -->
    <%@include file="aedit_stdmat.jsp"%>

    <!-- 进度弹窗 -->
    <div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_item_dept" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
            <div class="modal-content">
                <div class="modal-body" style="text-align: center;padding: 10px;">
                    <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                    <div id="uploadProgressMsg" style="padding-top: 5px;">数据保存中，请稍后...</div>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/ui-static/std/aea_stdmat_store.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
</body>
</html>