<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;padding: 0;border: 0;overflow: auto;">
<head>
    <title>主题版本配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-html/css/theme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/theme/version/css/theme_ver_index.css" type="text/css" rel="stylesheet"/>
    <style>
        .changeColor{

            background: #ece6e6 !important;
            color: white;
        }

        /**绿色勾*/
        .myicon-tick-checked {
            display: inline-block;
            position: relative;
            width: 15px;
            height: 15px;
            border-radius: 50%;
            background-color: #2ac845;
        }


        /**灰色勾*/
        .myicon-tick-uncheck {
            display: inline-block;
            position: relative;
            width: 15px;
            height: 15px;
            border-radius: 50%;
            background-color: #5f646e;
        }


        .myicon-tick-checked:before, .myicon-tick-checked:after,.myicon-tick-uncheck:before,.myicon-tick-uncheck:after {
            content: '';
            pointer-events: none;
            position: absolute;
            color: white;
            border: 1px solid;
            background-color: white;
        }


        .myicon-tick-checked:before,.myicon-tick-uncheck:before {
            width: 1px;
            height: 1px;
            left: 25%;
            top: 50%;
            transform: skew(0deg,50deg);
        }


        .myicon-tick-checked:after,.myicon-tick-uncheck:after {
            width: 3px;
            height: 1px;
            left: 45%;
            top: 42%;
            transform: skew(0deg,-50deg);
        }
    </style>
    <script type="text/javascript">
        var themeId = '${theme.themeId}';  // 主题id
        var themeName = '${theme.themeName}'; // 主题名称
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/theme/version/js/theme_ver_index.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/stage/stage_info_index.js?<%=isDebugMode%>"></script>
    <script src="${pageContext.request.contextPath}/ui-static/theme/js/par_share_mat.js?<%=isDebugMode%>" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/in_item_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/common/ztree/out_item_ztree.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/mat/global_mat_rel_format.js" type="text/javascript"></script>
</head>
<body>
<div style="height: 95%; padding: 10px 10px 5px 10px;">
    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
        <div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__body" style="padding: 10px 0px;height: 99%;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-2 m--align-left m--padding-left-20">
                        <a href="#" class="btn btn-secondary m-btn m-btn--icon" onclick="backToTheme();" style="display: ${isShowBack?'':'none'}">
                            <span><i class="la la-angle-left"></i><span>返回</span></span>
                        </a>
                    </div>
                    <div class="col-xl-8 m--align-center m--align-center">
                        <span style="font-size: 18px;font-weight: bold;">${theme.themeName}主题</span>
                    </div>
                    <div class="col-xl-2 m--align-right m--padding-right-20"></div>
                </div>
                <div class="row" style="margin: 15px 0px 10px 0px;height: 95%;">
                    <div class="col-xl-3" style="padding: 0px 2px 0px 8px;flex: 0 0 20%;max-width: 20%;">
                        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                            <div class="m-portlet__head">
                                <div class="m-portlet__head-caption" style="padding: 3px 0px;">
                                    <div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                        <span class="m-portlet__head-icon m--hide">
                                            <i class="la la-gear"></i>
                                        </span>
                                        <h3 class="m-portlet__head-text">主题版本</h3>
                                    </div>
                                </div>
                            </div>
                            <div class="m-portlet__body" style="padding: 10px 0px;">
                                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-md-6"style="text-align: left;">
                                            <button type="button" class="btn btn-info" onclick="loadThemeVerTable();">刷新</button>&nbsp;
                                        </div>
                                        <div class="col-md-6" style="text-align: right;padding-right: 15px;">
                                            <button type="button" class="btn btn-secondary" onclick="openOperaInfo();">
                                                <span><i class="flaticon-info"></i>&nbsp;<span>帮助</span></span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                <div id="view_theme_ver_scroll" style="hoverflow:auto;">
                                    <div class="base" style="padding: 0px 5px;">
                                        <table id="view_theme_ver_tb"></table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xl-9" style="padding: 0px 8px 0px 2px;flex: 0 0 80%;max-width: 80%;">
                        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                            <div class="m-portlet__body" style="padding: 10px 5px;">
                                <ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">

                                    <li class="nav-item">
                                        <a id="stageListTab" class="nav-link active" data-toggle="tab" href="#m_tabs_stage1" onclick="searchParStageCondition('1');">
                                            <i class="la la-gear"></i>主线阶段
                                        </a>
                                    </li>

                                    <li class="nav-item">
                                        <a id="auxListTab" class="nav-link" data-toggle="tab" href="#m_tabs_stage2" onclick="searchParStageCondition('2');">
                                            <i class="la la-gear"></i>辅线服务
                                        </a>
                                    </li>

                                </ul>
                                <div class="tab-content">
                                    <!-- 主线设置 -->
                                    <div id="m_tabs_stage1" class="tab-pane active" role="tabpanel">
                                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                            <div class="row" style="margin: 0px;">
                                                <div class="col-md-6"style="text-align: left;">
                                                    <button type="button" class="btn btn-info verNoVisible" onclick="addParStage('1');">新增</button>
                                                    <button type="button" class="btn btn-secondary verNoVisible" onclick="batchDelParStage('1');">删除</button>
                                                    <button type="button" class="btn btn-secondary verNoVisible" onclick="sortParStage('1');">排序</button>
                                                </div>
                                                <div class="col-md-6" style="padding: 0px;">
                                                    <div class="row" style="margin: 0px;">
                                                        <div class="col-3"></div>
                                                        <div class="col-6">
                                                            <div class="m-input-icon m-input-icon--left">
                                                                <input id="stageKeyword1" type="text" placeholder="请输入名称..." class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                    <span><i class="la la-search"></i></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div class="col-3">
                                                            <button type="button" class="btn  btn-info" onclick="searchParStageCondition('1');">查询</button>&nbsp;
                                                            <button type="button" class="btn  btn-secondary" onclick="clearSearchStage('1');">清空</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- 列表区域 -->
                                        <div class="base" style="padding: 10px">
                                            <table  id="showParStageTable1"
                                                    data-toggle="table"
                                                    data-method="post"
                                                    data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                                    data-click-to-select=true
                                                    data-pagination-detail-h-align="left"
                                                    data-pagination-show-page-go="true"
                                                    data-page-size="10"
                                                    data-page-list="[10,20,30,50,100]"
                                                    data-pagination=true
                                                    data-side-pagination="server"
                                                    data-pagination-detail-h-align="left"
                                                    data-query-params="dealQueryParams"
                                                    data-url="${pageContext.request.contextPath}/aea/par/stage/listAeaParStagePage.do?isDeleted=0">
                                                <thead>
                                                    <tr>
                                                        <th data-field="" data-checkbox="true"
                                                            data-align="center" data-colspan="1" data-width="10"></th>
                                                        <th data-field="stageName" data-align="left"
                                                            data-colspan="1" data-width="160">名称</th>
                                                        <th data-field="stageCode" data-align="left"
                                                            data-colspan="1" data-width="160">编号</th>
                                                        <th data-field="isNeedState" data-formatter="isNeedStateFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">是否分情形</th>
                                                        <th data-field="isOptionItem" data-formatter="isOptionItemFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">是否设置</br>可选事项</th>
                                                        <th data-field="dueNum" data-align="center" data-colspan="1" data-width="50">承诺办结时限</th>
                                                        <th data-field="dueUnit" data-formatter="dueUnitFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">时限单位</th>
                                                        <th data-field="" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="130">操作</th>
                                                    </tr>
                                                </thead>
                                            </table>
                                        </div>
                                        <!-- 列表区域end -->
                                    </div>

                                    <!-- 主线设置 -->
                                    <div id="m_tabs_stage2" class="tab-pane" role="tabpanel">
                                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                            <div class="row" style="margin: 0px;">
                                                <div class="col-md-6"style="text-align: left;">
                                                    <button type="button" class="btn btn-info verNoVisible" onclick="addParStage('2');">新增</button>
                                                    <button type="button" class="btn btn-secondary verNoVisible" onclick="batchDelParStage('2');">删除</button>
                                                    <button type="button" class="btn btn-secondary verNoVisible" onclick="sortParStage('2');">排序</button>
                                                </div>
                                                <div class="col-md-6" style="padding: 0px;">
                                                    <div class="row" style="margin: 0px;">
                                                        <div class="col-3"></div>
                                                        <div class="col-6">
                                                            <div class="m-input-icon m-input-icon--left">
                                                                <input id="stageKeyword2" type="text" placeholder="请输入名称..." class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                    <span><i class="la la-search"></i></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div class="col-3">
                                                            <button type="button" class="btn  btn-info" onclick="searchParStageCondition('2');">查询</button>&nbsp;
                                                            <button type="button" class="btn  btn-secondary" onclick="clearSearchStage('2');">清空</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- 列表区域 -->
                                        <div class="base" style="padding: 10px">
                                            <table  id="showParStageTable2"
                                                    data-toggle="table"
                                                    data-method="post"
                                                    data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                                    data-click-to-select=true
                                                    data-pagination-detail-h-align="left"
                                                    data-pagination-show-page-go="true"
                                                    data-page-size="10"
                                                    data-page-list="[10,20,30,50,100]"
                                                    data-pagination=true
                                                    data-side-pagination="server"
                                                    data-pagination-detail-h-align="left"
                                                    data-query-params="dealQueryParams"
                                                    data-url="${pageContext.request.contextPath}/aea/par/stage/listAeaParStagePage.do?isDeleted=0">
                                                <thead>
                                                    <tr>
                                                        <th data-field="" data-checkbox="true"
                                                            data-align="center" data-colspan="1" data-width="10"></th>
                                                        <th data-field="stageName" data-align="left"
                                                            data-colspan="1" data-width="160">名称</th>
                                                        <th data-field="stageCode" data-align="left"
                                                            data-colspan="1" data-width="160">编号</th>
                                                        <th data-field="isNeedState" data-formatter="isNeedStateFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">是否分情形</th>
                                                        <th data-field="isOptionItem" data-formatter="isOptionItemFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">是否设置<br/>可选事项</th>
                                                        <th data-field="dueNum" data-align="center" data-colspan="1" data-width="50">承诺办结时限</th>
                                                        <th data-field="dueUnit"  data-formatter="dueUnitFormatter"
                                                            data-align="center" data-colspan="1" data-width="50">时限单位</th>
                                                        <th data-field="" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="130">操作</th>
                                                    </tr>
                                                </thead>
                                            </table>
                                        </div>
                                        <!-- 列表区域end -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 新增或编辑阶段 -->
<%@include file="../../stage/add_stage_index.jsp"%>

<!-- 阶段排序 -->
<%@include file="../../stage/sort_stage.jsp"%>

<!-- 设置图标 -->
<%@include file="../common_set_img.jsp"%>

<!-- 操作指南 -->
<%@include file="theme_ver_opera_index.jsp"%>

<!-- 设置辅线关联主线 -->
<%@include file="../../stage/set_aux_rel_main.jsp"%>

<!-- 设置EL表达式 -->
<%@include file="../../kitymind/item/detail/select_meta_db_tbcol_ztree.jsp"%>

<!-- 主题版本流程图 -->
<%@include file="show_theme_ver_att.jsp"%>

<!-- 共享材料 -->
<%@include file="../par_share_mat.jsp"%>
<%@include file="../../common/ztree/in_item_ztree.jsp"%>
<%@include file="../../common/ztree/out_item_ztree.jsp"%>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">复制版本数据中，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    function dueUnitFormatter(value, row, index, field) {

        if(value){
            <c:forEach items="${dueUnits}" var="dueUnit">
                if(value="${dueUnit.itemCode}"){
                    return '${dueUnit.itemName}';
                }
            </c:forEach>
        }
    }
</script>

</body>
</html>
