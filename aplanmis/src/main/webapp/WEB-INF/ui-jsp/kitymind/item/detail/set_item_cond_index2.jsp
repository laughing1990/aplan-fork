<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项情形设置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
    </style>
    <script type="text/javascript">
       var itemId = '${item.itemId}';
    </script>
</head>
<body>

    <div style="width: 100%;height: 88%; padding: 0px 10px 5px 10px;">
        <div class="row" style="width: 100%;height: 100%;margin: 0px;">
            <div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__body" style="padding: 0px 0px;height: 99%;">
                    <div class="row" style="margin: 0px;background-color: #f2f2f2;">
                        <div class="col-xl-6 m--align-left m--padding-left-20">
                            <div class="form-group m-form__group row" style="padding-top: 15px;">
                                <label class="col-3 col-form-label">是否分前置条件<font color="red">*</font>:</label>
                                <div class="col-9">
                                    <div class="m-radio-inline">
                                        <label class="m-radio">
                                            <input type="radio" name="isNeedFrontCond" value="1" checked>是<span></span>
                                        </label>
                                        <label class="m-radio">
                                            <input type="radio" name="isNeedFrontCond" value="0">否<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-6">
                            <div class="form-group m-form__group row" style="padding-top: 15px;">
                                <div class="col-lg-12">
                                    <button type="button" class="btn btn-info" onclick="saveItemNeedCondInfo();">保存配置</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="mainContentPanel" class="row" style="margin: 15px 0px 0px 0px;height: 100%;">
                        <div class="col-xl-12" style="padding: 0px;">
                            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption" style="padding: 3px 0px;">
                                        <div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                                <span class="m-portlet__head-icon m--hide">
                                                    <i class="la la-gear"></i>
                                                </span>
                                            <h3 class="m-portlet__head-text">事项前置条件</h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-3">
                                            <input id="frontCondKeyWord" type="text" placeholder="请输入关键字..."
                                                   class="form-control m-input m-input--solid empty"
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-9">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchFrontCondNode();">查询</button>
                                            <button type="button" class="btn btn-danger"
                                                    onclick="clearSearchFrontCondNNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandFrontCondAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseFrontCondAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="frontCondTree" class="ztree" style="overflow: auto;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 新增或编辑事项前置条件 -->
    <%@include file="add_item_cond_index.jsp"%>

    <!-- 选择项目字段 -->
    <%@include file="select_meta_db_tbcol_ztree.jsp"%>

    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/item/detail/js/set_item_cond_index2.js" type="text/javascript"></script>

</body>
</html>