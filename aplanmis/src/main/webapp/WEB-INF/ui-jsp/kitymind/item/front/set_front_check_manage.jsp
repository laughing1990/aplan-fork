<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>前置检测</title>
    <title>事项范围配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/set_front_check_manage.js" type="text/javascript"></script>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '${currentStateVerId}';
        var curIsEditable = ${curIsEditable};
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var isNeedState = '${isNeedState}';
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
    <jsp:include page="../../mindHeader.jsp"></jsp:include>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div id="westPanel" class="col-xl-12" style="padding: 0px;">
            <div class="m-portlet" style="margin-bottom: 5px;">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
                        <li class="nav-item">
                            <a id="stageListTab" class="nav-link active" data-toggle="tab" href="#m_tabs_4_1" onclick="">
                                <i class="la la-gear"></i>
                                前置事项检测
                            </a>
                        </li>
                        <li id="optionItemLi" class="nav-item">
                            <a id="auxListTab" class="nav-link" data-toggle="tab" href="#m_tabs_4_2" onclick="">
                                <i class="la la-gear"></i>
                                扩展表检测
                            </a>
                        </li>
                        <li id="frontCheckItemLi" class="nav-item">
                            <a id="checkListTab" class="nav-link" data-toggle="tab" href="#m_tabs_4_3" onclick="">
                                <i class="la la-gear"></i>
                                项目信息监测
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <!-- 前置事项检测 -->
                        <div id="m_tabs_4_1" class="tab-pane active" role="tabpanel">
                            <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-md-7"style="text-align: left;">
                                        <button type="button" class="btn btn-info"
                                                onclick="importFrontItem();">导入前置事项</button>
                                        <button type="button" class="btn btn-info"
                                                onclick="sortFrontItem();">排序</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="batchDelFrontItem();">删除</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="refreshFrontItem();">刷新</button>
                                    </div>
                                    <div class="col-md-5" style="padding: 0px;">
                                        <div class="row" style="margin: 0px;">
                                            <div class="col-4"></div>
                                            <div class="col-5" style="text-align: right;">
                                                <div class="m-input-icon m-input-icon--left">
                                                    <input type="text" class="form-control m-input"
                                                           placeholder="请输入关键字..." name="frontItemKeyword" value=""/>
                                                    <span class="m-input-icon__icon m-input-icon__icon--left">
                                                        <span><i class="la la-search"></i></span>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="col-3"  style="text-align: left;">
                                                <button type="button" class="btn btn-info" onclick="searchFrontItem();">查询</button>
                                                <button type="button" class="btn btn-secondary" onclick="clearSearchFrontItem();">清空</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                            <div class="base" style="padding: 10px">
                                <table id="front_item_tb"></table>
                            </div>
                        </div>

                        <!-- 扩展表检测 -->
                        <div id="m_tabs_4_2" class="tab-pane" role="tabpanel">
                            <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-md-7"style="text-align: left;">
                                        <button type="button" class="btn btn-info"
                                                onclick="importPartForm();">导入扩展表</button>
                                        <button type="button" class="btn btn-info"
                                                onclick="sortPartForm();">排序</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="batchDelPartForm();">删除</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="refreshPartForm();">刷新</button>
                                    </div>
                                    <div class="col-md-5" style="padding: 0px;">
                                        <div class="row" style="margin: 0px;">
                                            <div class="col-4"></div>
                                            <div class="col-5" style="text-align: right;">
                                                <div class="m-input-icon m-input-icon--left">
                                                    <input type="text" class="form-control m-input"
                                                           placeholder="请输入关键字..." name="partFormKeyword" value=""/>
                                                    <span class="m-input-icon__icon m-input-icon__icon--left">
                                                        <span><i class="la la-search"></i></span>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="col-3"  style="text-align: left;">
                                                <button type="button" class="btn btn-info" onclick="searchPartForm();">查询</button>
                                                <button type="button" class="btn btn-secondary" onclick="clearSearchPartForm();">清空</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                            <div class="base" style="padding: 10px">
                                <table id="part_form_tb"></table>
                            </div>
                        </div>

                        <!-- 项目信息监测 -->
                        <div id="m_tabs_4_3" class="tab-pane" role="tabpanel">
                            <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-md-7"style="text-align: left;">
                                        <button type="button" class="btn btn-info"
                                                onclick="addProjCheck();">新增</button>
                                        <button type="button" class="btn btn-info"
                                                onclick="sortProjCheck();">排序</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="batchDelProjCheck();">删除</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="refreshProjCheck();">刷新</button>
                                    </div>
                                    <div class="col-md-5" style="padding: 0px;">
                                        <div class="row" style="margin: 0px;">
                                            <div class="col-4"></div>
                                            <div class="col-5" style="text-align: right;">
                                                <div class="m-input-icon m-input-icon--left">
                                                    <input type="text" class="form-control m-input"
                                                           placeholder="请输入关键字..." name="projCkKeyword" value=""/>
                                                    <span class="m-input-icon__icon m-input-icon__icon--left">
                                                        <span><i class="la la-search"></i></span>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="col-3"  style="text-align: left;">
                                                <button type="button" class="btn btn-info" onclick="searchProjCheck();">查询</button>
                                                <button type="button" class="btn btn-secondary" onclick="clearSearchProjCheck();">清空</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                            <div class="base" style="padding: 10px">
                                <table id="proj_check_tb"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 新增/编辑项目信息检测 -->
    <%@include file="aedit_item_proj_check.jsp"%>

    <!-- 进度弹窗 -->
    <div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_item_dept" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
            <div class="modal-content">
                <div class="modal-body" style="text-align: center;padding: 10px;">
                    <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                    <div id="uploadProgressMsg" style="padding-top: 5px;">数据加载中，请稍后...</div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>