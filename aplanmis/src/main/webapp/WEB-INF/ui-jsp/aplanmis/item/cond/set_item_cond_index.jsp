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
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
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
    <script type="text/javascript">
       var itemId = '${item.itemId}';
       var needCondType = '${item.needCondType}';
       var needCondNum = '${item.needCondNum}';
    </script>
</head>
<body>
    <div style="width: 100%;height: 85%; padding: 0px 10px 5px 10px;">
        <div class="row" style="width: 100%;height: 100%;margin: 0px;">
            <div class="m-portlet border-0" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__body" style="padding: 0px 0px;height: 99%;">
                    <div class="row" style="margin: 0px;background-color: #f2f2f2;">
                        <div class="col-xl-6 m--align-left m--padding-left-20">

                            <label class="m-radio" style="margin-top: 10px;margin-bottom: 10px;">
                                <input id="needCondTypeN" name="needCondType" value="N"
                                       type="radio" onclick="selectIsNeedState('N');">无前置条件
                                <span for="isNeedStateN"></span>
                            </label><br/>

                            <label class="m-radio" style="margin-bottom: 10px;">
                                <input id="needCondTypeS" name="needCondType" value="S"
                                       type="radio" onclick="selectIsNeedState('S');">有前置条件,符合其中一项条件即可办理
                                <span for="isNeedStateS"></span>
                            </label><br/>

                            <label class="m-radio" style="margin-bottom: 10px;">
                                <input id="needCondTypeM" name="needCondType" value="M"
                                       type="radio" onclick="selectIsNeedState('M');">有前置条件,满足其中几项条件即可办理
                                <span for="isNeedStateM"></span>
                            </label>&nbsp;&nbsp;&nbsp;

                            <label id="needCondNumLabel">
                                <input id="needCondNum" type="number" class="form-control m-input" style="width: 250px;"
                                       name="needCondNum" value="" placeholder="请输入至少满足条件数量"/>
                            </label>
                        </div>
                        <div class="col-xl-6">
                            <div class="form-group m-form__group row" style="padding-top: 35px;">
                                <div class="col-lg-12">
                                    <button type="button" class="btn btn-info" onclick="saveItemNeedCondInfo();">保存配置</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="mainContentPanel" class="row" style="margin: 15px 0px 10px 0px;height: 95%;">
                        <div class="col-xl-5" style="padding: 0px 2px 0px 8px;">
                            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption" style="padding: 3px 0px;">
                                        <div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                            <span class="m-portlet__head-icon m--hide">
                                                <i class="la la-gear"></i>
                                            </span>
                                            <h3 class="m-portlet__head-text">申请条件</h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="form-group m-form__group row" >
                                        <div class="col-lg-12">
                                            <textarea class="form-control" name="sqtj" rows="32" readonly>${acceptRange.sqtj}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-xl-7" style="padding: 0px 8px 0px 2px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption" style="padding: 3px 0px;">
                                        <div class="m-portlet__head-title" style="padding: 7px 12px 7px 12px;">
                                            <span class="m-portlet__head-icon m--hide">
                                                <i class="la la-gear"></i>
                                            </span>
                                            <h3 class="m-portlet__head-text">前置条件列表</h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                        <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                            <div class="row" style="margin: 0px;">
                                                <div class="col-md-6"style="text-align: left;">
                                                    <button type="button" class="btn btn-info" onclick="addItemCond();">新增</button>
                                                    <button type="button" class="btn btn-secondary" onclick="batchDeleteItemCond();">删除</button>
                                                    <button type="button" class="btn btn-secondary" onclick="refreshItemCond();">刷新</button>
                                                </div>
                                                <div class="col-md-6" style="padding: 0px;">
                                                    <div class="row" style="margin: 0px;">
                                                        <div class="col-2"></div>
                                                        <div class="col-6">
                                                            <form id="search_item_cond_form" method="post">
                                                                <div class="m-input-icon m-input-icon--left">
                                                                    <input type="text" class="form-control m-input"
                                                                           placeholder="请输入关键字..." name="keyword" value=""/>
                                                                    <span class="m-input-icon__icon m-input-icon__icon--left">
                                                                    <span><i class="la la-search"></i></span>
                                                                </span>
                                                                </div>
                                                            </form>
                                                        </div>
                                                        <div class="col-4">
                                                            <button type="button" class="btn btn-info"
                                                                    onclick="searchItemCond();">查询</button>
                                                            <button type="button" class="btn btn-secondary"
                                                                    onclick="clearSearchItemCond();">清空</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                        <!-- 列表区域 -->
                                        <div class="base" style="padding: 10px">
                                            <table  id="item_cond_tb"
                                                    data-toggle="table"
                                                    data-height="600"
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
                                                    data-query-params="dealQueryParam"
                                                    data-response-handler="itemCondListResponseData"
                                                    data-url="${pageContext.request.contextPath}/aea/item/cond/listAeaItemCond.do">
                                                <thead>
                                                    <tr>
                                                        <th data-field="" data-checkbox="true" data-align="center"
                                                            data-colspan="1" data-width="10"></th>
                                                        <th data-field="condName" data-align="left" data-colspan="1"
                                                            data-width="150">条件名称</th>
                                                        <th data-field="condEl" data-align="left" data-colspan="1"
                                                            data-width="150">表达式</th>
                                                        <th data-field="isAccept" data-formatter="isAcceptFormatter"
                                                            data-align="center" data-colspan="1" data-width="60">表达式</th>
                                                        <%--<th data-field="sortNo" data-align="center" data-colspan="1"--%>
                                                            <%--data-width="60">排序</th>--%>
                                                        <th data-field="condMemo"  data-align="center" data-colspan="1"
                                                            data-width="150">备注</th>
                                                        <th data-field="isActive" data-formatter="isItemCondActiveStr"
                                                            data-align="center" data-colspan="1" data-width="80">是否启用</th>
                                                        <th data-field="" data-formatter="itemCondListOperatorFormatter"
                                                            data-align="center" data-colspan="1" data-width="100">操作</th>
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

    <!-- 新增或编辑事项前置条件 -->
    <%@include file="../../../kitymind/item/detail/add_item_cond_index.jsp"%>

    <!-- 选择项目字段 -->
    <%@include file="../../../kitymind/item/detail/select_meta_db_tbcol_ztree.jsp"%>

    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/cond/set_item_cond_index.js" type="text/javascript"></script>
</body>
</html>