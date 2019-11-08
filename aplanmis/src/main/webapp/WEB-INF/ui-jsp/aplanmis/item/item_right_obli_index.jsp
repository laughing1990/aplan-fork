<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>权利与义务</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
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
    </style>
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
						   <span class="m-portlet__head-icon m--hide">
							   <i class="la la-gear"></i>
						   </span>
                        <h3 class="m-portlet__head-text">
                            权利与义务
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addItemRightObli();">新增</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDeleteRightObli();">删除</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshRightObli();">刷新</button>
                        </div>

                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-3"></div>
                                <div class="col-6">
                                    <form id="form_item_right_obli" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value="" id="item_right_obli_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												   <span><i class="la la-search"></i></span>
											   </span>
                                        </div>
                                        <input type="hidden" name="itemBasicId" value="${itemBasicId}"/>
                                        <input type="hidden" name="itemName" value="${itemName}"/>
                                    </form>
                                </div>
                                <div class="col-3">
                                    <button type="button" class="btn btn-info" onclick="searchRightObli();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchRightObli();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="rightObliTable"
                           data-toggle="table",
                           data-click-to-select=true,
                           queryParamsType="",
                           data-pagination=true,
                           data-page-size="10",
                           data-side-pagination="server",
                           data-page-list="[10, 20, 50, 100]",
                           data-side-pagination="server",
                           data-query-params="dealQueryParams",
                           data-pagination-show-page-go="true",
                           data-url="${pageContext.request.contextPath}/aea/item/right/obli/listAeaItemRightsObligationsByPage.do">
                        <thead>
                        <tr>
                            <th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="50"></th>
                            <th data-field="itemName" data-colspan="1" data-width="250" hidden="hidden">本级事项</th>
                            <th data-field="rightObliType" data-colspan="1" data-width="250" data-formatter="rightObliTypeFormatter">申请人权利/义务类型</th>
                            <th data-field="rightObliDetails" data-colspan="1" data-width="250">申请人权利/义务详细信息</th>
                            <!--<th data-field="dataId" data-colspan="1" data-width="250" hidden="hidden">省的数据ID</th>-->
                            <th data-field="_operator" data-formatter="operatorFormatter" data-align="center" data-colspan="1" data-width="100">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="dialog_item_right_obli" tabindex="-1" role="dialog" aria-labelledby="dialog_right_obli" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content" style="width: 90%;">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_right_obli_title">编辑权利/义务</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <form id="form_right_obligation" method="post" enctype="multipart/form-data">
                <div class="m-portlet__body" id="dialog_right_obli_body" style="padding: 15px;max-height:600px;width:90%;overflow-y:auto;overflow-x: hidden">
                    <input type="hidden" id="rightObliId" name="rightObliId"/>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label" for="itemBasicId" style="text-align: right">事项名称:<span style="color:red">*</span></label>
                        <div class="col-4 input-group">
                            <input type="hidden" id="itemBasicId" name="itemBasicId" value=""/>
                            <input type="text" class="form-control m-input" id="itemName" name="itemName" readonly placeholder="请选择事项..." value="" >
                            <div class="input-group-append"><span class="input-group-text open-item-list"><i class="la la-tag"></i></span></div>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label" style="text-align: right">申请人权利或义务(类型):</label>
                        <div class="col-4">
                            <select id="rightObliType" name="rightObliType"  class="form-control m-input" placeholder="请选择权利与义务类型">
                                <option value="">--请选择--</option>
                                <option value="0">权力</option>
                                <option value="1">义务</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label" style="text-align: right">申请人权利或义务描述:</label>
                        <div class="col-9"><textarea name="rightObliDetails" id="rightObliDetails" rows="10" class="form-control m-input" placeholder="请输入权利与义务描述..."></textarea></div>
                    </div>
                    <input type="hidden" id="dataId" name="dataId" value="hard-code-test"/>
                </div>
                <!-- 按钮区域 -->
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="btn_save" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- demo 实例 -->
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_right_obli_index.js" type="text/javascript"></script>
</body>
</html>