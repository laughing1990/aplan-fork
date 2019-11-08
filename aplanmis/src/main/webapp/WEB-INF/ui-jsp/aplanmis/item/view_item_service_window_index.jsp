<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>服务窗口</title>
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
                            服务窗口
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                        </div>

                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-3"></div>
                                <div class="col-6">
                                    <form id="form_item_service_window" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value="" id="item_service_window_keyword"/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
												   <span><i class="la la-search"></i></span>
											   </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3">
                                    <button type="button" class="btn btn-info" onclick="searchServiceWindow();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchServiceWindow();">清空</button>
                                    <button type="button" class="btn btn-secondary" onclick="refreshServiceWindow();">刷新</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base" style="padding: 0px 5px;">
                    <table id="serviceWindowTable"
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
                           data-url="${pageContext.request.contextPath}/aea/item/service/window/listAeaItemServiceWindowByPage.do">
                        <thead>
                        <tr>
                            <th data-field="windowName" data-colspan="1" data-width="200">窗口名称</th>
                            <!--
                            <th data-field="regionCode" data-colspan="1" data-width="250">行政区划代码</th>
                            <th data-field="orgCode" data-colspan="1" data-width="250">组织机构代码</th>
                            -->
                            <th data-field="linkPhone" data-colspan="1" data-width="150">联系电话</th>
                            <th data-field="workTime" data-colspan="1" data-width="300">办公时间</th>
                            <th data-field="windowAddress" data-colspan="1" data-width="300">窗口地址</th>
                            <!--
                            <th data-field="trafficGuide" data-colspan="1" data-width="250">交通指引</th>
                            -->
                            <th data-field="windowMemo" data-colspan="1" data-width="300">备注说明</th>
                            <!--
                            <th data-field="mapUrl" data-colspan="1" data-width="250" data-formatter="mapUrlFormatter">地图链接</th>
                            <th data-field="mapAtt" data-colspan="1" data-width="250" data-formatter="operatorFormatterMapAttr">地图附件</th>
                            -->
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="dialog_item_service_window" tabindex="-1" role="dialog" aria-labelledby="dialog_service_window" aria-hidden="true" style="text-align:right;">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content" style="width:95%;">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_service_window_title">编辑服务窗口</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <form id="form_service_window" method="post" enctype="multipart/form-data">
                <div class="m-portlet__body" id="dialog_service_window_body" style="padding: 15px;max-height:600px;width:95%;overflow-y:auto;overflow-x: hidden">
                    <input type="hidden" id="windowId" name="windowId"/>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" for="windowName" >窗口名称<span style="color:red">*</span></label>
                        <div class="col-4">
                            <input class="form-control m-input" type="text" value="" id="windowName" name="windowName" placeholder="请输入窗口名称...">
                        </div>

                        <label for="regionCode" class="col-2 col-form-label">行政区划代码<span style="color:red">*</span>:</label>
                        <div class="col-4">
                            <input class="form-control m-input" type="text" value="" id="regionCode" name="regionCode" placeholder="请输入行政区划代码...">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" for="orgCode" >组织机构代码<span style="color:red">*</span></label>
                        <div class="col-4">
                            <input class="form-control m-input" type="text" value="" id="orgCode" name="orgCode" placeholder="请输入组织机构代码...">
                        </div>

                        <label for="linkPhone" class="col-2 col-form-label">联系电话<span style="color:red">*</span>:</label>
                        <div class="col-4">
                            <input class="form-control m-input" type="text" value="" id="linkPhone" name="linkPhone" placeholder="请输入联系电话...">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label" for="workTime" >办公时间<span style="color:red">*</span></label>
                        <div class="col-10">
                            <input class="form-control m-input" type="text" value="" id="workTime" name="workTime" placeholder="请输入办公时间...">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">窗口地址<span style="color:red">*</span></label>
                        <div class="col-10"><textarea name="windowAddress" id="windowAddress" class="form-control m-input" placeholder="请输入窗口地址..."></textarea></div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">交通指引<span style="color:red">*</span></label>
                        <div class="col-10"><textarea name="trafficGuide" id="trafficGuide" class="form-control m-input" placeholder="请输入交通指引.."></textarea></div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">地图链接</label>
                        <div class="col-10"><textarea name="mapUrl" id="mapUrl" class="form-control m-input" placeholder="请输入地图链接..."></textarea></div>
                    </div>

                    <input type="text" id="mapAtt" name="mapAtt" class="hide"/>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">地图附件</label>
                        <input type="file" id="mapAttFile" name="mapAttFile" multiple="multiple" />

                        <div id = "mapAttButton" class = "form-group ">
                            <button id = "mapAttDownLoad" type="button" class="btn btn-info" onclick="downloadMapAtt();">下载</button>
                            <button id = "mapAttDelete" type="button" class="btn btn-danger" onclick="deleteMapAtt();">删除</button>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">备注说明</label>
                        <div class="col-10"><textarea name="windowMemo" id="windowMemo" class="form-control m-input" placeholder="请输入窗口描述..."></textarea></div>
                    </div>
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
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/service_window_index.js" type="text/javascript"></script>
</body>
</html>