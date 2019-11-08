<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: hidden;">
<head>
    <title>总表管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/item/list/css/item_index2.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        label {
            text-align: right;
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
                        <h3 class="m-portlet__head-text">
                            总表管理&nbsp;&nbsp;
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-3"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addParOneform();">新增</button>
                            <button type="button" class="btn btn-secondary" onclick="delMulParOneform();">删除</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchAllOneformList();">刷新</button>
                        </div>
                        <div class="col-md-9" style="padding: 0px;">
                            <form id="search_all_item_list_form" method="post">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-7"></div>
                                    <div class="col-3" style="text-align: right;">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." id="keyword" name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												<span><i class="la la-search"></i></span>
											</span>
                                        </div>
                                    </div>
                                    <div class="col-2"  style="text-align: center; max-width: 150px;">
                                        <button type="button" class="btn btn-info" onclick="searchAllOneformList();">查询</button>
                                        <button type="button" class="btn btn-secondary" onclick="clearSearchAllOneformList();">清空</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                    <table id="all_oneform_list_tb"></table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>


</div>

<!-- 添加/编辑 -->
<div id="edit_oneform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 700px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="edit_oneform_modal_title">编辑一张表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="edit_oneform_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="edit_oneform_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="oneformId" id="stageOneformId" value=""/>

                        <div class="form-group m-form__group row">

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>表单名称:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="oneformName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" id="oneformDesc" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>表单描述:</label>
                            <div class="col-lg-9">
                                <textarea name="oneformDesc" class="form-control m-input" style="width:100%; height:150px"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" id="oneformUrl" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>表单加载地址:</label>
                            <div class="col-lg-9">
                                <textarea name="oneformUrl" class="form-control m-input" style="width:100%; height:150px"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>排序字段:</label>
                            <div class="col-lg-9">
                                <input type="text" class="form-control m-input" name="sortNo" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-3 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                            <div class="col-lg-9">
                                <select type="text" class="form-control" name="isActive" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveParOneform">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>


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
<script src="${pageContext.request.contextPath}/ui-static/item/list/js/delegate_index.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/stage/oneform/par_oneform.js" type="text/javascript"></script>
</body>
</html>