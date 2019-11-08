<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>全局材料库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/global/css/global_material_index.css">
</head>
<body>
<div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
    <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__head">
                <div class="m-portlet__head-caption">
                    <div class="m-portlet__head-title">
					    <span class="m-portlet__head-icon m--hide">
						    <i class="la la-gear"></i>
					    </span>
                        <h3 class="m-portlet__head-text">
                            材料标准清单
                        </h3>
                    </div>
                </div>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                    <!-- 按钮区域 -->
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn btn-info"
                                        onclick="addGlobalMat();">新增</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="batchDelGlobalMat();">删除</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="refreshMatSto();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-3"></div>
                                    <div class="col-6">
                                        <form id="search_mat_sto_form" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入材料名称、编号..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" class="btn  btn-info" onclick="searchMatSto();">查询</button>
                                        <button type="button" class="btn  btn-secondary" onclick="clearSearchMatSto();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 按钮区域end -->
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="customTable"
                                data-toggle="table"
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination-detail-h-align="left"
                                data-pagination-show-page-go="true"
                                data-page-size="10"
                                data-page-list="[10,20,30,50,100]",
                                data-pagination=true
                                data-side-pagination="server"
                                data-pagination-detail-h-align="left"
                                data-query-params="matStoParam"
                                data-response-handler="matStotResponseData"
                                data-url="${pageContext.request.contextPath}/aea/item/mat/globalMatList.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center"
                                        data-colspan="1" data-width="10"></th>
                                    <th data-field="matCode" data-align="left"
                                        data-colspan="1" data-width="200">材料编号</th>
                                    <th data-field="matName" data-align="left"
                                        data-colspan="1" data-width="200">材料名称</th>
                                    <th data-field="matFrom" data-align="center"
                                        data-formatter="operatorFormatterMatFrom"
                                        data-colspan="1" data-width="200">材料来源</th>
                                    <th data-field="matTypeName" data-align="left"
                                        data-colspan="1" data-width="200">材料类别</th>
                                    <th data-field="" data-formatter="operatorFormatter"
                                        data-align="center" data-colspan="1" data-width="120">操作</th>
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

<!-- 新增全局材料弹窗 -->
    <div id="aedit_mat_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_mat_global_title" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
            <div class="modal-content">
                <div class="modal-header" style="padding: 15px">
                    <h5 class="modal-title" id="aedit_mat_modal_title">编辑事项材料</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form id="aedit_mat_form" method="post" enctype="multipart/form-data">
                    <div class="m-portlet__body" id="aedit_mat_global_body"
                         style="padding: 15px;max-height: 500px;overflow-y:auto;overflow-x: hidden">

                        <input type="hidden" name="matId" value=""/>
                        <input type="hidden" name="isGlobalShare" value="" />
                        <input type="hidden" name="isActive" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料类别<span style="color:red">*</span>:</label>
                            <div class="col-10 input-group">
                                <input type="hidden" name="matTypeId" value=""/>
                                <input type="text" class="form-control m-input"
                                       name="matTypeName" readonly placeholder="请选择材料类别..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-mat-type">
                                        <i class="la la-tag"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料名称<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matName" placeholder="请输入材料名称...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料编号<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matCode" placeholder="请输入材料编号...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否通用材料<font color="red">*</font>:</label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;padding-top: 8px;">
                                是否支持容缺<font color="red">*</font>:
                            </label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否批文批复<font color="red">*</font>:</label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isOfficialDoc" value="1">是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isOfficialDoc" value="0" checked>否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源:</label>
                            <input type="hidden" id="matFrom" name="matFrom" value=""/>
                            <div class="col-10">
                                <div class="m-checkbox-inline">
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom1" value="1" onclick="checkboxOnclick(this)">行政机关<span for="matFrom1"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom2" value="2" onclick="checkboxOnclick(this)">企事业单位<span for="matFrom2"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom3" value="3" onclick="checkboxOnclick(this)">社会组织<span for="matFrom3"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom4" value="4" onclick="checkboxOnclick(this)">申请人<span for="matFrom4"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom5" value="5" onclick="checkboxOnclick(this)">中介服务<span for="matFrom5"></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源部门:</label>
                            <div class="col-10">
                                <input id="matFromDept" class="form-control m-input" name="matFromDept"
                                          placeholder="请输入材料来源部门..." />
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">模板文档:</label>
                            <div class="col-8">
                                <input id="templateDocFile" type="file" class="form-control m-input"
                                       name="templateDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText1">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="templateDocDiv" class="col-2">
                                <button id="templateDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('0');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">样本文档:</label>
                            <div class="col-8">
                                <input type="file"  id="sampleDocFile" class="form-control m-input"
                                       name="sampleDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                    <button class="left-button">选择文件</button>
                                    <span class="right-text" id="rightText2">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="sampleDocDiv" class="col-2">
                                <button id="sampleDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('1');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">审查样本:</label>
                            <div class="col-8">
                                <input id="reviewSampleDocFile" type="file" class="form-control m-input"
                                       name="reviewSampleDocFile" multiple="multiple" accept="image/*" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText3">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="reviewSampleDocDiv" class="col-2">
                                <button id="reviewSampleDocButton" type="button" class="btn btn-info" onclick="showMatAtt('2');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">审查要点<font color="red">*</font>:</label>
                            <div class="col-10">
                                <textarea name="reviewKeyPoints" class="form-control m-input"
                                          placeholder="请输入审查要点..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">审查依据:</label>
                            <div class="col-10">
                                <textarea name="reviewBasis" class="form-control m-input"
                                          placeholder="请输入备注..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">纸质是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="1">必须
                                        <span for="inlineRadio1"></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="0">非必须
                                        <span for="inlineRadio0"></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label">电子是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="1" >必须
                                        <span for="inlineRadio2"></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="0">非必须
                                        <span for="inlineRadio3"></span>
                                    </label>
                                </div>

                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">纸质材料类型:</label>
                            <div class="col-10">
                                <select name="paperMatType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">A3</option>
                                    <option value="2">A4</option>
                                    <option value="3">A5</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">原件验收:</label>
                            <div class="col-4">
                                <select name="duePaperType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label">原件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="duePaperCount" placeholder="请输入原件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">复印件验收类型:</label>
                            <div class="col-4">
                                <select name="dueCopyType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label">复印件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="dueCopyCount" placeholder="请输入复印件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">备注:</label>
                            <div class="col-10">
                                <textarea name="matMemo" id="matMemo" rows="4"
                                class="form-control m-input" placeholder="请输入备注..."></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- 按钮区域 -->
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button id="saveMatStoreBtn" type="submit" class="btn btn-info">保存</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div id="show_mat_att_modal" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="dialog_item_dept" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width:900px;">
            <div class="modal-content">
                <!-- 标题 -->
                <div class="modal-header" style="padding: 15px;height: 45px;">
                    <h5 class="modal-title" id="show_mat_att_modal_title">查看附件</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" style="padding: 10px;">
                    <div id="show_mat_att_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                        <!-- 列表区域 -->
                        <div class="base" style="padding: 0px 5px;">
                            <table  id="show_mat_att_tb"
                                    data-toggle="table"
                                    data-method="post"
                                    data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                    data-click-to-select=true
                                    data-pagination-detail-h-align="left"
                                    data-pagination-show-page-go="true"
                                    data-page-size="10"
                                    data-page-list="[10,20,30,50,100]",
                                    data-pagination=true
                                    data-side-pagination="server"
                                    data-pagination-detail-h-align="left"
                                    data-query-params="matAttTbParam"
                                    data-response-handler="matAttTbResponseData"
                                    data-url="${pageContext.request.contextPath}/bsc/att/listAttLinkAndDetail.do">
                                <thead>
                                    <tr>
                                        <th data-field="attName" data-align="left"
                                            data-formatter="viewAttNameFormatter"
                                            data-colspan="1" data-width="150">附件名称</th>
                                        <th data-field="attFormat" data-align="center"
                                            data-colspan="1" data-width="80">附件类型</th>
                                        <th data-field="attSize" data-align="center"
                                            data-colspan="1" data-width="100" data-formatter="viewAttSizeFormatter">附件大小(KB)</th>
                                        <th data-field="_operator" data-formatter="attOperFormatter"
                                            data-align="center" data-width="100">操作</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                        <!-- 列表区域end -->
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="button" class="btn btn-secondary" onclick="$('#show_mat_att_modal').modal('hide');">关闭</button>
                </div>
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

    <%@include file="../../aplanmis/item/select_mat_type_ztree.jsp" %>
    <script src="${pageContext.request.contextPath}/ui-static/mat/global/global_material_index.js" type="text/javascript"></script>
</body>
</html>