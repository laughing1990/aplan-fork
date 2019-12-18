<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>法律法规/条款库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css"/>
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
                            <span class="m-portlet__head-icon m--hide"><i class="la la-gear"></i></span>
                            <h3 class="m-portlet__head-text">法律法规库（<span style="color: red;">* 点击节点右键操作</span>）</h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-3">
                            <input id="keyWord" type="text"
                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xl-5">
                            <button type="button" class="btn btn-info" onclick="searchLegal();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchLegal();">清空</button>
                            <button type="button" class="btn btn-secondary" onclick="expandAll();">展开</button>
                            <button type="button" class="btn btn-secondary" onclick="collapseAll();">折叠</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 树列表区域 -->
                    <div id="legalTree" class="ztree" style="overflow: auto;"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--法律法规右击菜单-->
<div id="legalContextMenu" class="contextMenuDiv">
    <a href="javascript:void(0);" class="list-group-item" onclick="addLegal();">
        <i class="fa flaticon-plus"></i>新增法律法规
    </a>
    <a id="addLegalClauseBtn" href="javascript:void(0);" class="list-group-item" onclick="addLegalClause();">
        <i class="fa flaticon-plus"></i>新增条款
    </a>
    <a id="editLegalBtn" href="javascript:void(0);" class="list-group-item" onclick="editLegal();">
        <i class="fa flaticon-edit-1"></i>编辑法律法规
    </a>
    <a id="delLegalBtn" href="javascript:void(0);" class="list-group-item" onclick="deleteLegal();">
        <i class="fa fa-times"></i>删除法律法规
    </a>
</div>

<!--条款右键菜单-->
<div id="clauseContextMenu" class="contextMenuDiv">
    <a href="javascript:void(0);" class="list-group-item" onclick="editLegalClause();">
        <i class="fa flaticon-edit-1"></i>编辑条款
    </a>
    <a href="javascript:void(0);" class="list-group-item" onclick="deleteLegalClause();">
        <i class="fa fa-times"></i>删除条款
    </a>
</div>

<!--新增法律法规-->
<%@include file="add_legal.jsp"%>

<!--新增条款开始-->
<div id="add_legalClause_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_legalClause_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_legalClause_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="add_legalClause_form" method="post">

                    <input type="hidden" id="legalClauseId" name="legalClauseId"/>
                    <input type="hidden" id="legalId" name="legalId" />

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>条款号:</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="clauseTitle" value="" placeholder="请输入条款号..."/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>条款具体内容:</label>
                        <div class="col-lg-10">
                            <textarea name="clauseContent" class="form-control m-input" placeholder="请输入条款具体内容..." rows="10"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-2 col-form-label">条款内容附件:</label>
                        <div class="col-8">
                            <input id="clauseAttFile" type="file" class="form-control m-input"
                                   name="clauseAttFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                                 <button class="left-button">选择文件</button>
                                 <span class="right-text" id="rightText1">未选择任何文件</span>
                            </span>
                        </div>
                        <div id="clauseAttDiv" class="col-2">
                            <button id="clauseAttButton" type="button" class="btn btn-info"
                                    onclick="showAtt('clauseAtt');">查看附件</button>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                        <div class="col-lg-10">
                            <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button id="saveLegalClauseBtn" type="submit" class="btn btn-info">保存</button>
                            <button id="closeAddlegalClauseBtn" type="button" class="btn btn-secondary">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!--新增条款结束-->

<!-- 附件 -->
<div id="show_mat_att_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="show_mat_att_modal_title" aria-hidden="true">
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

<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/legal_index.js?<%=isDebugMode%>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/common/context-menu.js" type="text/javascript"></script>
</body>
</html>