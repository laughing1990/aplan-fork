<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title id="title-text" >流程设计</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.css" rel="stylesheet" type="text/css"/>
    <!-- 注意：这个css引入要在下面的style标签之前-->
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }

        /*bootstrap下拉多级联动*/
        .dropdown-menu {
            width: 733px;
        !important;
        }
    </style>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        .fixed-table-body::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        .fixed-table-body::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        .fixed-table-body::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        .fixed-table-body::-webkit-scrollbar-corner{
            background: #fff;
        }

        .search {
            display: none;
        }
        #tplAppProcChooseDialog .base tbody tr:nth-of-type(2n+1){
            border-bottom: 1px solid #e8e8e8;
        }
        .circleIcon{
            display: inline-block;
            width: 40px;
            height: 40px;
            border-radius: 20px;
            color: white;
            padding: 8px 5px;
            font-size: 14px;
            margin: 0px 5px;
        }
        .blueColor{
            background-color: #52B6FD;
        }
        .greenColor{
            background-color: #8ABD46;
        }
        .purpleColor{
            background-color: #833FBC;
        }
        .yellowColor{
            background-color: #FFC000;
        }
        .ellipseIcon{
            display: inline-block;
            height: 22px;
            border-radius: 11px;
            padding: 0px 15px;
            font-size: 14px;
            margin: 0px 5px;
            border: 1px solid;
        }
        .squareIcon{
            height: 22px;
            width: 22px;
            display: inline-block;
            border: 1px solid;
            border-radius: 9px;
            background-color: #E03A3A;
            color: white;
            font-size: 13px;
            padding: 0px 1.5px;
            margin-right: 5px;
            font-style: italic;
        }
        .treegrid-indent {
            width: 40px;!important;
        }
        /******************滚动条 start******************/
        ::-webkit-scrollbar-thumb:horizontal { /*水平滚动条的样式*/
            width: 4px;
            background-color: #e0e3e9;
            -webkit-border-radius: 4px;
        }
        ::-webkit-scrollbar-track-piece {
            background-color: #fff; /*滚动条的背景颜色*/
            -webkit-border-radius: 4px; /*滚动条的圆角宽度*/
        }
        ::-webkit-scrollbar {
            width: 5px; /*滚动条的宽度*/
            height: 8px; /*滚动条的高度*/
            background-color: #e0e3e9;
            -webkit-border-radius: 4px;
        }
        ::-webkit-scrollbar-thumb:vertical { /*垂直滚动条的样式*/
            height: 50px;
            background-color: #e0e3e9;
            outline: 1px solid #fff;
            outline-offset: -2px;
            border: 1px solid #fff;
            -webkit-border-radius: 4px;
        }
        ::-webkit-scrollbar-thumb:hover { /*滚动条的hover样式*/
            height: 50px;
            background-color: #bdc3d4;
            -webkit-border-radius: 4px;
        }
        /******************滚动条 end******************/
        .el-dialog__header {
            padding: 20px 20px 10px;
            border-bottom: 1px solid #e0e3e9;
        }

        button:focus {
            outline: none;
        }
        [v-cloak] {
            display: none
        }
        .leftRightSelect{
            width: 90%;
            align: center;
            border: 0;
            cellpadding: 0;
            cellspacing: 0;
        }
        .leftRightSelect select{
            width: 46%;
            height: 300px;
            float: left;
            border: 1px solid #409EFF;
            padding: 4px;
            border-radius: 5px;
        }
        .leftRightSelect input{
            width:50px;
            margin-top: 5px;
        }
        #addTimeGroupDialogForm input.form-control,#addTimeGroupDialogForm select.form-control {
            max-width: 250px;
        }

        /*vue的loading*/
        @keyframes loading-dash {
            0% {
                stroke-dasharray: 1, 200;
                stroke-dashoffset: 0;
            }
            50% {
                stroke-dasharray: 90, 150;
                stroke-dashoffset: -40px;
            }
            100% {
                stroke-dasharray: 90, 150;
                stroke-dashoffset: -120px;
            }
        }

        @keyframes loading-rotate {
            100% {
                transform: rotate(1turn);
            }
        }

        #app {
            position: relative;
        }

        .my-loading-box {
            position: fixed;
            z-index: 10000;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            transition: all .3s;
            background-color: rgba(0, 0, 0, 0.6);
        }

        .my-loading-box .img-box {
            position: absolute;
            top: 50%;
            margin-top: -21px;
            width: 100%;
            text-align: center;
        }

        .my-loading-box .circular {
            height: 42px;
            width: 42px;
            animation: loading-rotate 2s linear;
        }

        .my-loading-box .path {
            animation: loading-dash 1.5s ease-in-out infinite;
            stroke-dasharray: 90, 150;
            stroke-dashoffset: 0;
            stroke-width: 2;
            stroke: #409eff;
            stroke-linecap: round;
        }

        .customSelect{
            width:100%!important;
            margin-top: 10px!important;
        }

        .el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
            color: #169aff!important;
        }
        .el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
            background-color: #F5F7FA;
        }
        .el-select-dropdown__item {
            font-size: 14px;
            padding: 0 20px;
            position: relative;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            color: #606266;
            height: 34px;
            line-height: 34px;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            cursor: pointer;
        }
        .el-select-dropdown__item {
            height: auto;
            line-height: normal;
            padding: 10px 20px;
        }
        /*表格行高度*/
        .base tbody tr td {
            padding: 6px 8px!important;
            border-top:none!important;
        }
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var appId = '${appId}';
        var currentBusiCode = '${currentBusiCode}';
        var currentStateVerId = '${currentStateVerId}' || '';
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
</head>
<body>
<jsp:include page="../mindHeader.jsp"></jsp:include>
<div id="app"  class="clearfix" v-loading="loading" v-cloak>
    <!-- 加载中动画弹窗 -->
    <div class="my-loading-box" v-show="pageLoading">
        <div class="img-box">
            <svg viewBox="25 25 50 50" class="circular">
                <circle cx="50" cy="50" r="20" fill="none" class="path"></circle>
            </svg>
            <div style="color:#409eff">加载中...</div>
        </div>
    </div>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div id="tabeToobarPanel" class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-8"  style="text-align: left;padding-left: 20px">
                            <button type="button" class="btn btn-info" onclick="showCreateProcess();">新建流程</button>
                            <button type="button" class="btn btn-info" onclick="addChooseProc();">导入流程</button>
                            <button type="button" class="btn btn-info" @click="elementConfig">元素配置</button>
                            <button type="button" class="btn btn-info" @click="formConfig">表单配置</button>
                            <button type="button" class="btn btn-info" @click="viewConfig">视图配置</button>
                            <button type="button" class="btn btn-secondary" @click="allExpand">展开</button>
                            <button type="button" class="btn btn-secondary" @click="allNotExpand">折叠</button>
                        </div>
                        <div class="col-4" style="padding: 0px;padding-left: 104px;">
                            <div class="row">
                                <div class="col-8" style="text-align: right;">
                                    <form id="search_item_process_design" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input" placeholder="请输入关键字..." id="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left"><span><i class="la la-search"></i></span></span>
                                        </div>
                                    </form>
                                </div>
                                <div  clas="col-4" style="text-align: right;">
                                    <button type="button" id="searchBtn" class="btn btn-info">查询</button>
                                    <button type="button" id="clearBtn" class="btn btn-secondary">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div id="tabePanel" class="base" style="padding: 10px;overflow: auto">
                    <%--<table  id="process_design_tb"--%>
                            <%--data-toggle="table",--%>
                            <%--data-click-to-select=true,--%>
                            <%--queryParamsType="",--%>
                            <%--data-page-size="10",--%>
                            <%--data-pagination="true",--%>
                            <%--&lt;%&ndash;data-side-pagination="server",&ndash;%&gt;--%>
                            <%--data-page-list="[10, 20, 50, 100]",--%>
                            <%--data-side-pagination="client",--%>
                            <%--data-query-params="processParam",--%>
                            <%--data-pagination-show-page-go="true",--%>
                            <%--data-url="${pageContext.request.contextPath}/rest/mind/processDefs.do">--%>
                        <%--<thead>--%>
                        <%--<tr>--%>
                            <%--<th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="10">ID</th>--%>
                            <%--<th data-field="appFlowdefId" data-visible="false" data-align="left" data-width="10">流程定义ID</th>--%>
                            <%--&lt;%&ndash;<th data-field="casedefKey" data-visible="false" data-align="left" data-width="150">用例定义KEY</th>--%>
                            <%--<th data-field="procdefKey" data-visible="false" data-align="left" data-width="150">流程定义KEY</th>--%>
                            <%--<th data-field="casedefName" data-visible="false" data-align="left" data-width="150">用例名称</th>--%>
                            <%--<th data-field="priorityOrder" data-visible="false" data-align="left" data-width=10>优先级</th>&ndash;%&gt;--%>
                            <%--<th data-field="id" data-visible="false" data-align="left" data-width="10">id</th>--%>
                            <%--<th data-field="defName" data-align="left" data-width="150">流程名称</th>--%>
                            <%--<c:if test="${currentBusiType=='item'}">--%>
                                <%--<th data-field="startElName"  data-align="left" data-width=100>启动情形</th>--%>
                            <%--</c:if>--%>
                            <%--<th data-field="isPublic" data-align="center" data-width="80">发布情况</th>--%>
                            <%--<th data-field="timeLimit"   data-align="center" data-width=60 data-formatter="timeLimitUnitFormatter">时限</th>--%>
                            <%--&lt;%&ndash;<th data-field="timeLimitUnit"  data-formatter="timeLimitUnitFormatter" data-align="center" data-width=40>时限单位</th>&ndash;%&gt;--%>
                            <%--<th data-field="limitIsWorkDay"  data-formatter="limitIsWorkDayFormatter" data-align="center" data-width=40>是否工作日</th>--%>
                            <%--<th data-field="isMasterDef"  data-formatter="masterFormatter" data-align="center" data-width=10>设为主流程</th>--%>
                            <%--<th data-field="_operator" data-formatter="operate" data-align="center" data-width="100">操作</th>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                    <%--</table>--%>

                        <%--<table id="process_design_tb"></table>--%>

                        <el-table :data="processTableData" row-key="id" :indent="indent" border class="scrollable"
                                  :empty-text="authTableNoDataText"
                                  v-loading="loading" style="margin-top: 10px;width: 100%;">
                            <el-table-column prop="defName" label="流程/节点名称">
                                <template slot-scope="scope">
                            <span v-if="scope.row.isProcess == '1'">
                                <span class="circleIcon blueColor">流程</span>{{scope.row.defName}}
                                <span class="ellipseIcon" v-if="scope.row.isMasterDef == '1'">主流程</span>
                                <span v-if="scope.row.itemName">
                                    <span v-if="scope.row.itemType == '1'">
                                        【<span class="squareIcon">标</span>{{scope.row.itemName}}】
                                    </span>
                                    <span v-if="scope.row.itemType != '1'">
                                        【<span class="squareIcon">实</span>{{scope.row.itemName}}—{{scope.row.itemApproveOrg}}】
                                    </span>
                                </span>
                            </span>
                                    <span v-if="scope.row.isProcess != '1'">
                                <span class="circleIcon greenColor">节点</span>{{scope.row.defName}}
                            </span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="timeLimit" width="150" label="时限">
                                <template slot-scope="scope">
                            <span v-if="scope.row.timeLimitUnit == 'NH'">
                                {{scope.row.timeLimit}}小时<span style="color:#BEBEBE">（自然日）</span>
                            </span>
                                    <span v-if="scope.row.timeLimitUnit == 'WH'">
                                {{scope.row.timeLimit}}小时<span style="color:#BEBEBE">（工作日）</span>
                            </span>
                                    <span v-if="scope.row.timeLimitUnit == 'ND'">
                                {{scope.row.timeLimit}}<span style="color:#BEBEBE">（自然日）</span>
                            </span>
                                    <span v-if="scope.row.timeLimitUnit == 'WD'">
                                {{scope.row.timeLimit}}<span style="color:#BEBEBE">（工作日）</span>
                            </span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="version" width="80" label="当前版本" align="left">
                                <template slot-scope="scope">
                            <span v-if="scope.row.isProcess == '1'">
                                <span v-if="scope.row.version">
                                    V{{scope.row.version}}
                                </span>
                                <span v-else>
                                    {{scope.row.isPublic}}
                                </span>
                            </span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="operate_" width="480" label="操作" align="left">
                                <template slot-scope="scope">
                                    <el-button type="text" class="btn-table"
                                               v-if="scope.row.isProcess == '1' && scope.row.version"
                                               @click="setProcMaster(scope.row)">
                                        设为主流程
                                    </el-button>
                                    <el-button type="text" class="btn-table"
                                               @click="editLimit(scope.row)">
                                        配置时限
                                    </el-button>
                                    <el-button type="text" class="btn-table" v-if="scope.row.isProcess == '1'"
                                               @click="openTimeGroupDialog(scope.row)">
                                        配置时限组
                                    </el-button>
                                    <el-button type="text" class="btn-table"
                                               v-if="scope.row.isProcess == '1' && scope.row.version"
                                               @click="showStartEl(scope.row)">
                                        启动情形
                                    </el-button>
                                    <el-button type="text" class="btn-table" v-if="scope.row.isProcess == '1'"
                                               @click="chooseProcDef(scope.row)">
                                        设计流程
                                    </el-button>
                                    <el-button type="text" class="btn-table"
                                               v-if="scope.row.isProcess == '1' && scope.row.version"
                                               @click="authConfig(scope.row)">
                                        配置权限
                                    </el-button>
                                    <el-button type="text" class="btn-table" v-if="scope.row.isProcess == '1'"
                                               @click="delActTplAppProc(scope.row)">
                                        移除流程
                                    </el-button>
                                    <el-button type="text" class="btn-table" v-if="scope.row.isProcess == '0'"
                                               @click="showSubprocess(scope.row)">
                                        配置子流程
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

<!-- 配置时限 -->
<div id="editLimitDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="editLimitDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="width:350px;margin:0 auto;">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="editLimitDialog_title" class="modal-title">时限配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <br/>
                <div class="form-group m-form__group row">
                    <label class="col-3 col-form-label">
                        时限：
                    </label>
                    <div class="col-9" style="text-align:left">
                        <input class="form-control" type="number" min="0" id="edit_timeLimit" placeholder="单位：工作日" style="float:left;width:250px;"/>
                        <input type="hidden" id="flowDefId_hidden"/>
                        <input type="hidden" id="nodeId_hidden"/>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-3 col-form-label">
                        时限规则：
                    </label>
                    <div class="col-9" style="text-align:left">
                        <select class="form-control m-input" id="timeruleId_" placeholder="时限规则" type="text"></select>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px">
                <button type="button" class="btn btn-default" onclick="saveTimeLimit()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


    <!-- 激活条件 -->
<div id="tplAppDefElDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppDefElDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="width:728px;margin:0 auto;">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="tplAppDefElDialog_title" class="modal-title">启动情形</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <br/>
                <div class="form-group m-form__group row">
                    <label class="col-3 col-form-label">
                        启动情形：
                    </label>
                    <div class="col-9" style="text-align:left">
                        <select class="form-control" id="startEl_select" style="width: 85%;">
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px">
                <button type="button" class="btn btn-default" onclick="startEl_save()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<!-- 新建流程 -->
<div id="createProcessDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="createProcessDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="width:760px;height: 470px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="createProcessDialog_title" class="modal-title">新建流程</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="procForm" style="width:760px;">
                <div class="m-portlet__body" style="text-align: center;width: 97%;">

                        <br/>
                        <div class="form-group m-form__group row">
                            <label class="col-3 col-form-label">
                                <font color="red">*</font>流程名称：
                            </label>
                            <div class="col-9">
                               <input class="form-control" name="name" id="procName" placeholder="流程名称" onchange="convertToKey(this)" style="float:left;width:430px;"/>
                            </div>
                        </div>
                        <div class="form-group m-form__group row">
                            <label class="col-3 col-form-label">
                                <font color="red">*</font>流程编号：
                            </label>
                            <div class="col-9">
                                <input class="form-control" name="key" id="procKey" placeholder="流程编号" style="float:left;width:430px;"/>
                            </div>
                        </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>流程时限：
                        </label>
                        <div class="col-9">
                            <input class="form-control" type="number" name="timeLimit" min="0" id="timeLimit" placeholder="单位：工作日" style="float:left;width:430px;"/>
                        </div>
                        <%--<label class="col-2 col-form-label">
                            时限单位：
                        </label>
                        <div class="col-3">
                            <select type="text" class="form-control" name="timeLimitUnit" id="timeLimitUnit" value="">
                                <option value="">请选择</option>
                                <option value="m">分钟</option>
                                <option value="d">天</option>
                            </select>
                        </div>--%>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>时限规则：
                        </label>
                        <div class="col-9">
                            <select class="form-control m-input" id="timeruleId" placeholder="时限规则" type="text" name="timeruleId" style="float:left;width:430px;"></select>
                        </div>
                    </div>
                    <%--<div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            是否启动流程时限：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="limitIsPro">
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    否 <input ng-model="triggerEvent_bsgzr" checked value="0" name="limitIsPro" type="radio" class="ng-pristine ng-untouched ng-valid" onclick="showLimits(0)">
                                    <span></span>
                                </label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    是 <input ng-model="triggerEvent_gzr" value="1" name="limitIsPro" type="radio" class="ng-pristine ng-untouched ng-valid"  onclick="showLimits(1)">
                                    <span></span>
                                </label>
                            </div>
                        </div>
                    </div>--%>

                    <%--<div id="isLimits" class="form-group m-form__group row" hidden>
                        <label class="col-3 col-form-label">
                            是否沿用事项的时限：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="itemLimits">
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    否 <input ng-model="triggerEvent_bsgzr" checked value="0" name="itemLimits" type="radio" class="ng-pristine ng-untouched ng-valid" onclick="showStageOrItems(0)">
                                    <span></span>
                                </label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    是 <input ng-model="triggerEvent_gzr" value="1" name="itemLimits" type="radio" class="ng-pristine ng-untouched ng-valid" onclick="showStageOrItems(1)">
                                    <span></span>
                                </label>
                            </div>
                        </div>
                    </div>--%>

                    <%--<div id="showStageOrItems" class="form-group m-form__group row" hidden>
                        <label class="col-3 col-form-label">
                            沿用时限的事项/阶段：
                        </label>
                        <div class="col-9">
                            <select type="text" class="form-control" name="stageOrItems" id="stageOrItems" value="">

                            </select>
                        </div>
                    </div>--%>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            流程详情：
                        </label>
                        <div class="col-9">
                            <textarea class="form-control"  rows="4" name="description" id="procDescription" placeholder="请填写流程描述信息" style="float:left;width:430px;"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px">
                    <button type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-default mr-3" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>


<div id="tplAppProcChooseDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppFormChooseDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered " role="document" style="max-width: 1150px;width: 1150px;" >
        <div class="modal-content" >
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="tplAppProcChooseDialog_title" class="modal-title">选择流程</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin-top: 5px; margin-left: 0px;margin-right: 10px;">
                        <div class="col-md-6" style="text-align: left;">
                            <button type="button" class="btn  btn-info btn-sm" onclick="confirmSelectProcSave();">保存</button>
                            <button type="button" class="btn  btn-secondary btn-sm" onclick="reloadTplAppProcData();">刷新</button>
                        </div>

                        <div class="col-md-6" style="padding: 0px;text-align: right;">
                            <div class="d-flex" style="margin: 0px;float:right">
                                <div>
                                    <div class="m-input-icon m-input-icon--left input-group" >
                                        <form id="uploadFlowForm" method="post">
                                            <div class="m-input-icon m-input-icon--left  input-group-sm">
                                                <input type="text"  id="tplAppProcChooseKeyword" class="form-control m-input" placeholder="请输入关键字..." name="tplAppProcChooseKeyword"   value="">
                                                <span class="m-input-icon__icon m-input-icon__icon--left"><span><i class="la la-search"></i></span></span>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div style="margin-left: 5px;">
                                    <button type="button" class="btn btn-info btn-sm" onclick="searchtplAppProcChooseKeyword();">查询</button>
                                    <button type="button" class="btn btn-secondary btn-sm" onclick="cleartplAppProcChooseKeyword();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="base"
                     style="padding: 0px 5px;max-height: 500px;height: 500px;width:100%;overflow-y:auto;overflow-x: hidden">
                    <table id="tplAppProcChooseDialog_tb" style="border: 1px solid #e8e8e8;"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------------------------------------------->

<%---------------------------------------------------时限组管理---------------------------------------------------%>
<div id="timeGroupModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppFormChooseDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered " role="document" style="max-width: 1150px;width: 1150px;" >
        <div class="modal-content" >
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5  class="modal-title">流程节点时限配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -3px; margin-right: -3px; ">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin-top: 5px; margin-left: 0px;margin-right: 10px;">
                        <div class="col-md-6" style="text-align: left;">
                            <button type="button" class="btn  btn-info btn-sm" onclick="javascript:openTimeGroupFormModal()">新增</button>
                            <button type="button" class="btn  btn-secondary btn-sm" onclick="searchTimegroupTable();">刷新</button>
                        </div>

                        <div class="col-md-6" style="padding: 0px;text-align: right;">
                            <div class="d-flex" style="margin: 0px;float:right">
                                <div>
                                   <%-- <div class="m-input-icon m-input-icon--left input-group" >
                                        <form id="timeGroupModalSearchForm" method="post">
                                            <div class="m-input-icon m-input-icon--left  input-group-sm">
                                                <input type="text"  id="timeGroupModalSearch" class="form-control m-input" placeholder="请输入关键字..." name="tplAppProcChooseKeyword"   value="">
                                                <span class="m-input-icon__icon m-input-icon__icon--left"><span><i class="la la-search"></i></span></span>
                                            </div>
                                        </form>
                                    </div>--%>
                                </div>
                                <%--<div style="margin-left: 5px;">
                                    <button type="button" class="btn btn-info btn-sm" onclick="searchtplAppProcChooseKeyword();">查询</button>
                                    <button type="button" class="btn btn-secondary btn-sm" onclick="cleartplAppProcChooseKeyword();">清空</button>
                                </div>--%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="base"
                     style="padding: 0px 5px;max-height: 500px;height: 500px;width:100%;overflow-y:auto;overflow-x: hidden">
                    <table id="timeGroupModal_tb" style="border: 1px solid #e8e8e8;"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<%--新增、修改--%>
<div id="addTimeGroupDialog" class="modal" tabindex="-1" role="dialog"
     aria-labelledby="editLimitDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="max-width: 1150px; width: 1150px;margin:0 auto;">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="" class="modal-title">新增/修改时限组</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -3px; margin-right: -3px;">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <form id="addTimeGroupDialogForm">
                    <input type="hidden" name="timegroupId">
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>时限组名称：
                        </label>
                        <div class="col-9" style="text-align:left">
                            <input class="form-control" type="text"  name="timegroupName" placeholder="时限组名称" style="float:left;"/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font> 时限规则：
                        </label>
                        <div class="col-9" style="text-align:left">
                            <select class="form-control m-input" name="timeruleId" id="timeruleGroupId_" placeholder="时限规则" type="text"></select>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>时限：
                        </label>
                        <div class="col-9" style="text-align:left">
                            <input class="form-control" type="number" min="0" name="timeLimit" id="edit_groupTimeLimit" placeholder="单位：工作日" style="float:left;"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-6 col-form-label">
                            选择节点：
                        </label>
                        <label class="col-6 col-form-label">
                            已选择：
                        </label>
                    </div>
                    <div class="form-group m-form__group row" style="display: flex; justify-content: center;display: flex; justify-content: center;">
                        <table class="leftRightSelect" >
                            <tr>
                                <th>　</th>
                                <td><div>
                                    <div>
                                        <select multiple="multiple" id="select1">
                                        </select>
                                    </div>
                                    <div style="float:left;margin: 5px;">
                                            <span id="add">
                                                <input type="button" class="btn" value=">"/>
                                            </span><br />
                                            <span id="add_all">
                                                <input type="button" class="btn" value=">>"/>
                                            </span> <br />
                                            <span id="remove">
                                                <input type="button" class="btn" value="<"/>
                                            </span><br />
                                            <span id="remove_all">
                                                <input type="button" class="btn" value="<<"/>
                                            </span> </div>
                                    <div>
                                        <select multiple="multiple" id="select2" >
                                        </select>
                                    </div>
                                </div></td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 10px">
                <button type="submit" class="btn btn-info" onclick="$('#addTimeGroupDialogForm').submit();">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


<!--元素配置-->
<!-- 选择公共页面元素 弹出框 start -->
    <el-dialog title="元素配置" :visible.sync="isShowElementConfig"  class="dialog" id="elementConfig" width="700px">
        <div>
            <div class="nav">
                <div class="nav-left">
                    <el-popover trigger="manual"  v-model="isshowChooseFlowList4" placement="bottom" width="450">
                        <div class="top-btn clearfix">
                            <p class="float-left">选择公共页面元素</p>
                            <div class="float-right">
                                <button type="button" class="btn btn-secondary" @click="isshowChooseFlowList4 = false;">关闭</button>
                                <button type="button" class="btn btn-primary" @click="isshowChooseFlowList4 = false;savePublicElement('addViewTypeForm')">保存</button>
                            </div>
                        </div>
                        <div class="chooseFlowListSelect">
                            <el-select class="customSelect" v-model="multipleSelection" value-key="elementName" filterable clearable  multiple  placeholder="直接选择或搜索选择" @visible-change = "toggleFlowListSelect" >
                                <el-option
                                        v-for="item in choosePBElementTableData"
                                        :key="item.elementCode"
                                        :label="item.elementName"
                                        :value="item">
                                    <div>{{ item.elementName }}</div>
                                    <div style="color: #8492a6; font-size: 13px;margin-top:5px;">{{ item.elementCode }}</div>
                                </el-option>
                            </el-select>
                        </div>
                        <button class="btn btn-secondary" :disabled="showTip"  @click="isshowChooseFlowList4 = !isshowChooseFlowList4;importElement()"   slot="reference">导入元素</button>
                    </el-popover>
                    <button class="btn btn-secondary" :disabled="showTip" @click="start_drag()">
                        元素排序
                    </button>
                    <button class="btn btn-outline-danger" :disabled="showTip" @click="deletePublicElementBatch()">
                        批量移除
                    </button>
                </div>
                <div class="nav-right" style="margin-left: 165px;">
                    <el-input placeholder="搜索公共元素" v-model="elementKeyword"  class="input-with-select">
                        <i slot="suffix" class="el-input__icon el-icon-search" @click="searchElementData()"></i>
                    </el-input>
                </div>
            </div>
            <div style="margin-top: 10px; padding: 5px 0px; text-align: center; background-color: rgb(255, 236, 231);" v-show="showTip">
                <span>上下移动分类顺序</span> <span style="margin-left: 20px;">
                        <button type="button" class="el-button btn-table el-button--text" @click="saveSort">
                            <span>保存</span>
                        </button>
                        <button type="button" class="el-button btn-table el-button--text" @click="cancleSort">
                            <span>撤销</span>
                        </button>
                    </span>
            </div>
            <el-table
                    :data="elementTableData" height="500"
                    ref="dragTable"
                    @selection-change="changeFun2"
                    style="width: 100%" v-drag="dragstatus">
                <el-table-column
                        type="selection"
                >
                </el-table-column>
                <el-table-column
                        prop="elementName"
                        label="元素名称"
                >
                </el-table-column>
                <el-table-column
                        prop="elementCode"
                        label="元素编号"
                >
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="操作" width="80px">
                    <template slot-scope="scope">
                        <el-button type="text" style="border: none;outline: none;" class="btn-table" @click="deleteElementItem(scope.row.appElementId)">
                            移除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div slot="footer" class="dialog-footer">
            <button type="button" class="btn btn-primary" @click="savePublicElement()">保存</button>
            <button type="button" class="btn btn-secondary" @click="isShowElementConfig = false">关闭</button>
        </div>
    </el-dialog>
<!-- 选择公共页面元素 弹出框 end -->
    <!--表单配置-->
    <el-dialog title="表单配置" :visible.sync="isShowFormConfig"  class="dialog" id="formConfig" width="700px">
        <div class="nav">
            <div class="nav-left">
                <el-popover trigger="manual"  v-model="isshowChooseFlowList1" placement="bottom" width="450">
                    <div class="top-btn clearfix">
                        <p class="float-left">选择业务表单</p>
                        <div class="float-right">
                            <button type="button" class="btn btn-secondary" @click="isshowChooseFlowList1 = false;">关闭</button>
                            <button type="button" class="btn btn-primary" @click="isshowChooseFlowList1 = false;savePublicForm()">保存</button>
                        </div>
                    </div>

                    <div class="chooseFlowListSelect">
                        <el-select  class="customSelect" v-model="multipleSelection3" value-key="formCode" filterable clearable  multiple  placeholder="直接选择或搜索选择" @visible-change = "toggleFlowListSelect" >
                            <el-option
                                    v-for="(item,index) in choosePBFormTableData"
                                    :key="item.index"
                                    :label="item.formName"
                                    :value="item">
                                <div>{{ item.formName }}</div>
                                <div style="color: #8492a6; font-size: 13px;margin-top:5px;">{{ item.formCode }}</div>
                            </el-option>
                        </el-select>
                    </div>
                    <button class="btn btn-secondary"  @click="isshowChooseFlowList1 = !isshowChooseFlowList1;addPBForm()"   slot="reference">添加表单</button>
                </el-popover>
                <!-- <button class="btn btn-secondary" @click="addPBForm">
                    添加表单
                </button> -->
                <button class="btn btn-outline-danger" @click="deleteActTPlAppformBatch()">
                    批量移除
                </button>
            </div>
            <div class="nav-right " style="margin-left: 260px;">
                <el-input placeholder="输入名称或编码" v-model="keyword1"  class="input-with-select">
                    <i slot="suffix" class="el-input__icon el-icon-search" @click="searchFormData()"></i>
                </el-input>
            </div>
        </div>
        <div class="right-aside-table-con">
            <el-table
                    :data="tplAppFormTableData" height="500"
                    style="width: 100%"
                    @selection-change="changeFun6">
                <el-table-column
                        type="selection"
                >
                </el-table-column>
                <el-table-column
                        prop="formName"
                        label="表单名称"
                >
                    <template slot-scope="scope">
                        <!-- <span class="thmnIconBox">
                            <img src="../../../../../static/agcloud/bpm/admin/template/images/pc-icon.png" th:src="@{/agcloud/bpm/admin/template/images/pc-icon.png}" class="thmnIcon" v-show="scope.row.tmnId == 1">
                            <img src="../../../../../static/agcloud/bpm/admin/template/images/android-icon.png" th:src="@{/agcloud/bpm/admin/template/images/android-icon.png}" class="thmnIcon" v-show="scope.row.tmnId == 2">
                            <img src="../../../../../static/agcloud/bpm/admin/template/images/weixin-icon.png" th:src="@{/agcloud/bpm/admin/template/images/weixin-icon.png}" class="thmnIcon" v-show="scope.row.tmnId == 3">
                            <img src="../../../../../static/agcloud/bpm/admin/template/images/android-icon.png" th:src="@{/agcloud/bpm/admin/template/images/android-icon.png}" class="thmnIcon" v-show="scope.row.tmnId == 4" >
                        </span> -->
                        <span>{{scope.row.formName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="formProperty"
                        label="属性"
                        width="80px">
                    <template slot-scope="scope">
                        <div>
                            <span v-if="scope.row.formProperty.match(/^meta/ig)">开发</span>
                            <span v-else>智能</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作" width="180px">
                    <template slot-scope="scope">
                        <el-button type="text" class="btn-table" @click="editFormRule(scope.row)">
                            编辑表单规则
                        </el-button>
                        <el-button type="text" class="btn-table" @click="deleteForm(scope.row)">
                            移除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </el-dialog>
    <!-- 编辑表单规则 弹出框 start -->
    <el-dialog title="编辑表单规则" :visible.sync="isShowEditForm"   class="dialog" width="600px">
        <div>
            <el-form ref="EditFormRule" id="EditFormRule"  :model="EditFormRuleData" :rules="EditFormRules" label-width="150px">
                <el-form-item label="概要标题表达式"  require   prop="ruleTitle">
                    <el-input v-model="EditFormRuleData.ruleTitle">
                        <template slot="append">
                            <el-popover trigger="click"
                                        placement="bottom"
                                        width="280" v-model="visibleFormRuleTitle">
                                <p>导入表达式</p>
                                <div class="chooseFomeRule">
                                    <el-select v-model="EditFormRuleData.ruleTitle" filterable clearable  placeholder="请选择" @change="handleSectFormTitle" >
                                        <el-option
                                                v-for="item in formRuleOptions"
                                                :key="item.columnName"
                                                :label="item.columnName"
                                                :value="item.columnName">
                                            <span style="float: left">{{ item.columnName }}</span>
                                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.columnComment }}</span>
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="btn btn-primary"  slot="reference">查看规则</div>
                            </el-popover>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="概要描述表达式" require  prop="ruleDesc">
                    <el-input v-model="EditFormRuleData.ruleDesc">
                        <template slot="append">
                            <el-popover trigger="click"
                                        placement="bottom"
                                        width="280"
                                        v-model="visibleFormRuleDesc">
                                <p>导入表达式</p>
                                <div class="chooseFomeRule">
                                    <el-select v-model="EditFormRuleData.ruleDesc" filterable clearable  placeholder="请选择" @change="handleSectFormDesc" >
                                        <el-option
                                                v-for="item in formRuleOptions"
                                                :key="item.columnName"
                                                :label="item.columnName"
                                                :value="item.columnName">
                                            <span style="float: left">{{ item.columnName }}</span>
                                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.columnComment }}</span>
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="btn btn-primary"  slot="reference">查看规则</div>
                            </el-popover>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="其他内容表达式" require prop="ruleOthers">
                    <el-input v-model="EditFormRuleData.ruleOthers">
                        <template slot="append">
                            <el-popover trigger="click"
                                        placement="bottom"
                                        width="280"
                                        v-model="visibleFormRuleOthers">
                                <p>导入表达式</p>
                                <div class="chooseFomeRule">
                                    <el-select v-model="EditFormRuleData.ruleOthers" filterable clearable  placeholder="请选择" @change="handleSectFormRuleOthers">
                                        <el-option
                                                v-for="item in formRuleOptions"
                                                :key="item.columnName"
                                                :label="item.columnName"
                                                :value="item.columnName">
                                            <span style="float: left">{{ item.columnName }}</span>
                                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.columnComment }}</span>
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="btn btn-primary"  slot="reference">查看规则</div>
                            </el-popover>
                        </template>
                    </el-input>
                </el-form-item>
            </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
            <button type="button" class="btn btn-secondary" @click="isShowEditForm = false;reSetEditFormRuleData('EditFormRule')">关闭</button>
            <button type="button" class="btn btn-primary" @click="saveFormRules('EditFormRule')">保存</button>
        </div>
    </el-dialog>
    <!-- 编辑表单规则 弹出框  end -->
    <!--表单配置结束-->
    <!--视图配置-->
    <el-dialog title="视图配置" :visible.sync="isShowViewConfig"  class="dialog" id="viewConfig" width="700px">
        <div class="nav">
            <div class="nav-left">
                <el-popover trigger="manual"  v-model="isshowChooseFlowList3" placement="bottom" width="450">
                    <div class="top-btn clearfix">
                        <p class="float-left">选择业务视图</p>
                        <div class="float-right">
                            <button type="button" class="btn btn-secondary" @click="isshowChooseFlowList3 = false;">关闭</button>
                            <button type="button" class="btn btn-primary" @click="isshowChooseFlowList3 = false; savePublicView()">保存</button>
                        </div>
                    </div>

                    <div class="chooseFlowListSelect">
                        <el-select  class="customSelect" v-model="multipleSelection5" value-key="viewComment" filterable clearable  multiple  placeholder="直接选择或搜索选择" @visible-change = "toggleFlowListSelect" >
                            <el-option
                                    v-for="item in chooseViewListTableData"
                                    :key="item.viewCode"
                                    :label="item.viewComment"
                                    :value="item">
                                <div>{{ item.viewComment }}</div>
                                <div style="color: #8492a6; font-size: 13px;margin-top:5px;">{{ item.viewCode }}</div>
                            </el-option>
                        </el-select>
                    </div>
                    <button class="btn btn-secondary"  @click="isshowChooseFlowList3 = !isshowChooseFlowList3;addPBView()"   slot="reference">添加视图</button>
                </el-popover>
                <!-- <button class="btn btn-secondary" @click="addPBView">
                    添加视图
                </button> -->
                <button class="btn btn-outline-danger" @click="deleteActTPlAppViewBatch">
                    批量移除
                </button>
            </div>
            <div class="nav-right " style="margin-left: 260px;">
                <el-input placeholder="输入名称或编码" v-model="keyword2"  class="input-with-select">
                    <i slot="suffix" class="el-input__icon el-icon-search" @click="searchViewData2()"></i>
                </el-input>
            </div>
        </div>
        <div class="right-aside-table-con">
            <el-table
                    :data="tplAppViewTableData" height="500"
                    style="width:100%"
                    @selection-change="changeFun7">
                <el-table-column
                        type="selection"
                >
                </el-table-column>
                <el-table-column
                        prop="viewName"
                        label="视图名称"
                >
                </el-table-column>
                <el-table-column
                        prop="viewCode"
                        label="视图编号"
                >
                </el-table-column>
                <el-table-column
                        label="操作" width="80px">
                    <template slot-scope="scope">
                        <el-button type="text" class="btn-table" @click="deleteViewItem(scope.row)">
                            移除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </el-dialog>
    <!--视图配置结束-->

    <jsp:include page="subprocess_list.jsp"></jsp:include>

</div>
<!--bootstrap-treegrid-->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap-table/bootstrap-table-treegrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.treegrid.min.js" type="text/javascript"></script>
<!--element vue -->
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/vue-v2/vue.js"></script>
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/element-2/element.js"></script>
<script src="${pageContext.request.contextPath}/agcloud/framework/js-lib/agcloud-lib/js/common.js"></script>
<!-- 业务js -->
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/processModelerVue.js?v=2"
        type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/processTreeGrid.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/processModeler_stage.js?v=4"
        type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/getPingyin.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/opus_om_org_ztree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/ztree/bsc_dic_region_ztree.js" type="text/javascript"></script>
</body>
</html>
