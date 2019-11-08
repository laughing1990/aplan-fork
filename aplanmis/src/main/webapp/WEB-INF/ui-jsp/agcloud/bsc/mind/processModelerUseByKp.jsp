<%@ page contentType="text/html;charset=UTF-8" %>

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
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var appId = '${appId}';
        var currentBusiCode = '${currentBusiCode}';
    </script>
</head>
<body>
<div id="mainContentPanel" class="row" style="height: 99%;padding: 10px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-8"  style="text-align: left;padding-left: 20px">
                            <button type="button" class="btn btn-info"
                                    onclick="showCreateProcess();">新建流程</button>
                            <button type="button" class="btn btn-info"
                                    onclick="addChooseProc();">导入流程</button>
                        </div>
                        <div class="col-4" style="padding: 0px;padding-left: 104px;">
                            <div class="row">
                                <div class="col-8" style="text-align: right;">
                                    <form id="search_item_process_design" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." id="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
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
                <div class="base" style="padding: 10px">
                    <table  id="process_design_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            data-page-size="10",
                            data-pagination="true",
                            data-page-list="[10, 20, 50, 100]",
                            data-side-pagination="client",
                            data-query-params="processParam",
                            data-pagination-show-page-go="true",
                            data-url="${pageContext.request.contextPath}/rest/mind/processDefs.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="10">ID</th>
                                <th data-field="appFlowdefId" data-visible="false" data-align="left" data-width="10">流程定义ID</th>
                                <th data-field="id" data-visible="false" data-align="left" data-width="10">id</th>
                                <th data-field="defName" data-align="left" data-width="150">流程名称</th>
                                <th data-field="startElName"  data-align="left" data-width=100>激活条件</th>
                                <th data-field="isPublic" data-align="center" data-width="80">发布情况</th>
                                <th data-field="isMasterDef" data-formatter="masterFormatter"
                                    data-align="center" data-width=10>设为主流程</th>
                                <th data-field="_operator" data-formatter="operate"
                                    data-align="center" data-width="100">操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

<!-- 激活条件 -->
<div id="tplAppDefElDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppDefElDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="tplAppDefElDialog_title" class="modal-title">激活条件</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <br/>
                <div class="form-group m-form__group row">
                    <label class="col-3 col-form-label">
                        流程类型：
                    </label>
                    <div class="col-9" style="text-align:left">
                        <label class="m-radio">
                            <input name="flowType" type="radio" checked value="stage" onchange="startEl_flowType_change()">
                            阶段流程
                            <span></span>
                        </label>&nbsp;&nbsp;
                    </div>
                </div>
                <div class="form-group m-form__group row">
                    <label class="col-3 col-form-label">
                        激活阶段：
                    </label>
                    <div class="col-9" style="text-align:left">
                        <select class="form-control" id="startEl_stage" style="width: 85%;">
                        </select>
                        <select class="form-control" id="startEl_not_stage" style="width: 85%;display:none">
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
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="createProcessDialog_title" class="modal-title">新建流程</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="procForm">
                <div class="m-portlet__body" style="text-align: center;width: 97%;">
                        <br/>
                        <div class="form-group m-form__group row">
                            <label class="col-3 col-form-label">
                                <font color="red">*</font>流程名称：
                            </label>
                            <div class="col-9">
                               <input class="form-control" name="name" id="procName"/>
                            </div>
                        </div>
                        <div class="form-group m-form__group row">
                            <label class="col-3 col-form-label">
                                <font color="red">*</font>流程编号：
                            </label>
                            <div class="col-9">
                                <input class="form-control" name="key" id="procKey"/>
                            </div>
                        </div>
                        <div class="form-group m-form__group row">
                            <label class="col-3 col-form-label">
                                流程详情：
                            </label>
                            <div class="col-9">
                                <textarea class="form-control"  rows="4" name="description"
                                          id="procDescription" placeholder="请填写流程描述信息"></textarea>
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

<!-- 弹窗 -->
<div id="tplAppProcChooseDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppFormChooseDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered " role="document" style="max-width: 900px;width: 900px;" >
        <div class="modal-content" >
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 40px;">
                <h5 id="tplAppProcChooseDialog_title" class="modal-title">选择流程</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px;max-height: 500px;height: 500px;overflow-y:auto;overflow-x: hidden">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin-top: 5px; margin-left: 0px;margin-right: 10px;">
                        <div class="col-md-6" style="text-align: left;">
                            <button type="button" class="btn  btn-info btn-sm"
                                    onclick="confirmSelectProcSave();">选择</button>
                            <button type="button" class="btn  btn-secondary btn-sm"
                                    onclick="reloadTplAppProcData();">刷新</button>
                        </div>

                        <div class="col-md-6" style="padding: 0px;text-align: right;">
                            <div class="d-flex" style="margin: 0px;float:right">
                                <div>
                                    <div class="m-input-icon m-input-icon--left input-group" >
                                        <form method="post">
                                            <div class="m-input-icon m-input-icon--left  input-group-sm">
                                                <input type="text"  id="tplAppProcChooseKeyword"
                                                       class="form-control m-input" placeholder="请输入关键字..."
                                                       name="tplAppProcChooseKeyword"   value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div style="margin-left: 5px;">
                                    <button type="button" class="btn btn-info btn-sm"
                                            onclick="searchtplAppProcChooseKeyword();">查询</button>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="cleartplAppProcChooseKeyword();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="tplAppProcChooseDialog_tb" style="border: 1px solid #e8e8e8;"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 业务js -->
<script src="${pageContext.request.contextPath}/ui-static/kitymind/process/processModeler.js" type="text/javascript"></script>
<jsp:include page="../../../kitymind/process/subprocess_list.jsp"></jsp:include>
</body>
</html>
