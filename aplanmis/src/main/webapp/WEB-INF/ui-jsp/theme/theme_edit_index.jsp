<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>修改主题配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp" %>
    <script type="text/javascript">
        var themeId = '${themeId}';
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/stage/stage_info_index.js?<%=isDebugMode%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_info_columns.js?<%=isDebugMode%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/mat/share/share_mat_info_index.js?<%=isDebugMode%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/par/share/ui-js/share_mat_info_columns.js?<%=isDebugMode%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/mat/share/item_inout_for_share_mat.js?<%=isDebugMode%>"></script>
    <style type="text/css">
        .parallerStageSortDiv {
            color: #464637;
            font-size: 14px;
            font-family: 'Roboto', sans-serif;
            font-weight: 300;
        }

        /**标题样式**/
        .parallerStageSortDiv .block_list-title {
            padding: 10px;
            text-align: center;
            background: #abcdf1;
        }

        .parallerStageSortDiv ul {
            margin: 0;
            padding: 0;
            list-style: none;
            display: block;
        }

        .parallerStageSortDiv li {
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

        .parallerStageSortDiv .drag-handle_th {
            text-align: center;
            display: inline-block;
            width: 8%;
            font-weight: 600;
        }

        /**拖拽手把**/
        .parallerStageSortDiv .drag-handle_td {
            text-align: center;
            font: bold 16px Sans-Serif;
            color: #5F9EDF;
            display: inline-block;
            width: 8%;
        }

        .parallerStageSortDiv .ostage_name_td{
            display: inline-block;
            width: 45%;
        }

        .trover
        {
            background: #f9f9f9;
        }
        .trclick
        {
            background: #c4e8f5;
        }

        .row{
            margin-left: 0px;
            margin-right: 0px;
        }

    </style>
</head>
<body>
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
    <div class="row" style="width: 100%;height: 100%;">
        <div id="centerPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
                        <span class="m-portlet__head-icon m--hide">
                           <i class="la la-gear"></i>
                        </span>
                        <h3 class="m-portlet__head-text">
                            <c:if test="${!empty theme.themeName}">【${theme.themeName}】主题设置</c:if>
                            <c:if test="${empty theme.themeName}">主题设置</c:if>
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 5px;">
                <ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#tabs_theme_stage" onclick="searchParStageCondition();">
                            <i class="la la-gear"></i>
                            阶段设置
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#tabs_theme_share" onclick="initParShareMatData();">
                            <i class="la la-gear"></i>
                            共享材料设置
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane" id="tabs_theme_stage" role="tabpanel">
                        <div class="row">
                            <div class="col-xl-7">
                                <button type="button" class="btn btn-accent" onclick="backToTheme();">返回主题</button>
                                <button type="button" class="btn  btn-success" onclick="addParStage();">新增阶段</button>
                                <button type="button" class="btn btn-danger" onclick="batchDelParStage();">删除阶段</button>
                                <button type="button" class="btn btn-secondary" onclick="sortParStage();">阶段排序</button>
                            </div>
                            <div class="col-xl-5">
                                <div class="form-group m-form__group row align-items-center" style="margin-bottom: 0rem;">
                                    <div class="col-md-8 m--margin-left-210">
                                        <div class="input-group">
                                            <input id="parallerStageKeyword" type="text" class="form-control m-input" placeholder="请输入阶段名称..." name="keyword" value=""/>&nbsp;
                                            <button type="button" class="btn  btn-info" onclick="searchParStageCondition();">查询</button>&nbsp;
                                            <button type="button" class="btn  btn-danger" onclick="clearSearchStage();">清空</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                        <div class="m-scrollable" data-scrollable="true" data-max-height="600">
                            <div class="m_datatable_par_stage" id="ajax_data"></div>
                        </div>
                    </div>
                    <div class="tab-pane" id="tabs_theme_share" role="tabpanel">
                        <div class="row">
                            <div class="col-xl-7">
                                <button type="button" class="btn btn-accent" onclick="backToTheme();">返回主题</button>
                                <button type="button" class="btn btn-success" onclick="addParShareMat();">新增共享材料</button>
                                <button type="button" class="btn btn-danger" onclick="batchDelParShareMat();">删除共享材料</button>
                            </div>
                            <div class="col-xl-5">
                                <div class="form-group m-form__group row align-items-center" style="margin-bottom: 0rem;">
                                    <div class="col-md-8 m--margin-left-210">
                                        <div class="input-group">
                                            <input id="share_mat_keyword" type="text" class="form-control m-input" placeholder="请输入事项、材料名称..." name="keyword" value=""/>&nbsp;
                                            <button type="button" class="btn  btn-info" onclick="searchParShareMat();">查询</button>&nbsp;
                                            <button type="button" class="btn  btn-danger" onclick="clearSearchParShareMat();">清空</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                        <div class="m-scrollable" data-scrollable="true" data-max-height="600">
                            <div class="m_datatable_par_share_mat"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 新增或编辑阶段 -->
<%@include file="../stage/add_stage_index.jsp"%>

<!-- 阶段排序 -->
<%@include file="../stage/sort_stage.jsp"%>

<!-- 设置阶段事项关联 -->
<%@include file="../aplanmis/item/select_aea_item_ztree.jsp"%>

<!-- 新增或编辑主题共享资料 -->
<%@include file="../mat/share/add_share_mat_index.jsp"%>

</body>
</html>