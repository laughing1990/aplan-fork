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
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>

    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
</head>
<body>
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 8px 0px 2px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <!-- 标题-->
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                               <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                全局材料列表
                            </h3>
                        </div>
                    </div>
                </div>

                <div class="m-portlet__body" style="padding: 10px 5px;">
                    <!-- 按钮区域 -->
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn  btn-success" onclick="addGlobalMat();">新增</button>
                                <button type="button" class="btn  btn-danger" onclick="batchDelGlobalMat();">删除</button>
                                <button type="button" class="btn  btn-accent" onclick="datatable.reload();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-3"></div>
                                    <div class="col-6">
                                        <form id="search_item_cond_form" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" id="input_mat_global_search" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" class="btn  btn-info" onclick="searchTable();">查询</button>
                                        <button type="button" class="btn  btn-danger" onclick="clearSearch();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>

                    <!-- 列表区域 -->
                    <div class="m-scrollable" data-scrollable="true" data-max-height="600">
                        <div id="tb_mat_global" class="m_datatable"></div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- 新增全局材料弹窗 -->
<div class="modal fade" id="dialog_mat_global" tabindex="-1" role="dialog" aria-labelledby="dialog_mat_global_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_mat_global_title">编辑事项材料</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <form id="form_mat_global" method="post" enctype="multipart/form-data">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-portlet__body" id="dialog_mat_global_body" style="padding: 15px;max-height:600px;overflow-y:auto;overflow-x: hidden">
                        <input type="hidden" id="matId" name="matId"/>
                        <input type="hidden" id="isGlobalShare" name="isGlobalShare" value="1" />
                     <%--   <input type="hidden" id="isCommon" name="isCommon" value="1"/>--%>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" >材料类别<font color="red"></font>:</label>
                            <div class="col-4 input-group">
                                <input type="hidden" id="matTypeId" name="matTypeId" value=""/>
                                <input type="text" class="form-control m-input" name="matTypeName" readonly placeholder="请选择材料类别..." >
                                <div class="input-group-append"><span class="input-group-text open-mat-type"><i class="la la-tag"></i></span></div>
                            </div>

                            <label for="matName" class="col-2 col-form-label">材料名称<font color="red">*</font>:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="text" value="" id="matName" name="matName" placeholder="请输入材料名称...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label for="matCode" class="col-2 col-form-label">材料编号<font color="red">*</font>:</label>
                            <div class="col-4"><input class="form-control m-input" type="text" value="" id="matCode" name="matCode" placeholder="请输入材料编号..."></div>

                            <label class="col-2 col-form-label">原件数<font color="red"></font>:</label>
                            <div class="col-4"><input class="form-control m-input" type="number" value="1" name="duePaperCount" placeholder="请输入原件数..."></div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">复印件数<font color="red"></font>:</label>
                            <div class="col-4"><input class="form-control m-input" type="number" value="1" name="dueCopyCount" placeholder="请输入复印件数..."></div>

                            <label class="col-2 col-form-label">纸质原件数<font color="red"></font>:</label>
                            <div class="col-4"><input class="form-control m-input" type="number" value="1" name="sortNo" placeholder="请输入应交纸质原件份数..."></div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源<font color="red"></font>:</label>
                            <div class="col-10"><textarea name="matFrom" class="form-control m-input" placeholder="请输入材料来源..."></textarea></div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料要求<font color="red"></font>:</label>
                            <div class="col-10"><textarea name="matRequire" class="form-control m-input" placeholder="请输入材料要求..."></textarea></div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料依据<font color="red"></font>:</label>
                            <div class="col-10"><textarea name="matBasis" class="form-control m-input" placeholder="请输入材料依据..."></textarea></div>
                        </div>

<%--                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料所属<font color="red"></font>:</label>
                            <div class="col-4">
                                <select id="matHolder" name="matHolder"  class="form-control m-input" placeholder="请选择项目类型">
                                    <option value="">--请选择--</option>
                                    <option value="c">企业</option>
                                    <option value="u">个人</option>
                                    <option value="p">工程项目</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label">是否批文<font color="red"></font>:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input type="radio" name="receiveMode" value="1">批文类<span></span></label>
                                    <label class="m-radio"><input type="radio" name="receiveMode" value="0" checked>非批文类<span></span></label>
                                </div>
                            </div>
                        </div>--%>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">是否必须<font color="red"></font>:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input type="radio" name="isRequire" value="1" checked>必须<span></span></label>
                                    <label class="m-radio"><input type="radio" name="isRequire" value="0">非必须<span></span></label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label">是否启用<font color="red"></font>:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio"><input type="radio" name="isActive" value="1" checked>启用<span></span></label>
                                    <label class="m-radio"><input type="radio" name="isActive" value="0">禁用<span></span></label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">备注:</label>
                            <div class="col-10"><textarea name="matMemo" class="form-control m-input" placeholder="请输入备注..."></textarea></div>
                        </div>
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

<!-- 材料类别弹窗 -->
<!--<div id="add_item_mat_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="add_item_mat_modal_title" aria-hidden="true"  data-max-height="340" style="z-index: 3050">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 650px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_mat_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="m-portlet__body" style="padding: 10px 0px;">
                <!-- 按钮区域 -->
                <!--<div class="row">
                    <div class="col-xl-5">
                        <input id="matTypeZtreeKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button  type="button" class="btn btn-info" >查询</button>
                        <button type="button" class="btn btn-danger" onclick="clearSearchItemType('#matTypeZtreeKeyWord');">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandTreeAllNode('opusOmOrgTree');">全部展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseAllNode('opusOmOrgTree');">全部折叠</button>
                    </div>
                </div>

                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 树区域 -->
                <!--<div style="height:420px;overflow:auto; ">
                    <ul id="matTypeTree"  class="ztree"></ul>
                </div>
            </div>

            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectMatTypeBtn" type="button" onclick="selectMatType();" class="btn btn-info">选择</button>
                <button type="button" class="btn btn-secondary" onclick="closeMatTypeZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
-->
<%@include file="select_mat_type_ztree.jsp" %>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/global_material_list.js" type="text/javascript"></script>
<!--类别选取 -->
<!--<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_type_tree.js" type="text/javascript"></script>-->

</body>
</html>