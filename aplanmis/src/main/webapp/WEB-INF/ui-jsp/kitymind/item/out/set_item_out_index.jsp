<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项输出材料</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-right: 0px;
        }
        .form-group label{
            display: block;
            float: left;
            position: relative;
        }
        .form-group input[type="file"]{
            position: absolute;
            width: 10%;
            opacity: 0;
        }
        .form-group .custorm-style{
            display: block;
            width: 100%;
            height: 38px;
            border: 1px solid #d9d9d9;
            border-radius: 4px;
        }
        .form-group .custorm-style .left-button{
            width: 71px;
            font-size: 13px !important;
            height: 22px;
            line-height: 13px;
            float: left;
            border:1px solid #b1b1b1;
            background: linear-gradient(to bottom, #fff, #ccc);
            color: #444;
            margin-top: 0.9%;
            margin-left: 1%;
        }
        .form-group .custorm-style .right-text{
            width: 80%;
            height: 99%;
            line-height: 2.7em;
            display: block;
            overflow: hidden;
        }

        .blueColor {

            background-color: #52B6FD;
        }

        .circleIcon {

            display: inline-block;
            width: 34px;
            height: 24px;
            border-radius: 4px;
            color: white;
            text-align: center;
            line-height: 24px;
            font-size: 12px;
            margin: 0px 2px;
        }
    </style>
    <script type="text/javascript">
       var currentBusiType = '${currentBusiType}';
       var currentBusiId = '${currentBusiId}';
       var currentStateVerId = '${currentStateVerId}';
       var curIsEditable = ${curIsEditable};
       var handWay = '${handWay}';
       var useOneForm = '${useOneForm}';
       var isNeedState = '${isNeedState}';
    </script>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
</head>
<body>
    <jsp:include page="../../mindHeader.jsp"></jsp:include>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn btn-info"
                                onclick="addItemOutMat();">新增材料</button>
                                <button type="button" class="btn btn-info"
                                onclick="addItemOutGlobalMat();">导入库材料</button>
                                <button type="button" class="btn btn-info"
                                        onclick="sortItemOut();">排序</button>
                                <button type="button" class="btn btn-secondary"
                                onclick="batchDeleteItemOutMatCert();">删除</button>
                                <button type="button" class="btn btn-secondary"
                                onclick="refreshItemOutMatCet();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-4"></div>
                                    <div class="col-5" style="text-align: right;">
                                        <form id="search_item_out_mat_cert" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-3"  style="text-align: left;">
                                        <button type="button" class="btn btn-info"
                                                onclick="searchItemOutMatCert();">查询</button>
                                        <button type="button" class="btn btn-secondary"
                                                onclick="clearSearchItemOutMatCert();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 10px">
                        <table  id="item_out_mat_cert_tb"
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
                                data-query-params="itemOutMatCertParam"
                                data-response-handler="itemOutMatCertResponseData",
                                data-url="${pageContext.request.contextPath}/aea/item/inout/listAeaItemInoutCascadeByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                    <th data-field="matProp" data-formatter="matPropormatter"
                                        data-align="left" data-width=80>材料性质</th>
                                    <th data-field="aeaMatCertName" data-formatter="threeMatNameFormatter"
                                        data-align="left" data-width="250">名称</th>
                                    <th data-field="aeaMatCertCode" data-align="left" data-width="250">编号</th>
                                    <th data-field="_operator" data-formatter="itemOutMatCertFormatter"
                                        data-align="center" data-width="120">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
        </div>
    </div>

    <!-- 导入库材料 -->
    <%@include file="../../../aplanmis/item/select_item_inout_global_mat.jsp"%>

    <!-- 新增/编辑材料 -->
    <%@include file="../../../aplanmis/item/aedit_item_inout_mat_index.jsp"%>

    <!-- 新增/编辑证照 -->
    <%@include file="../../../aplanmis/item/aedit_item_inout_cert_index.jsp"%>

    <!-- 选择项目字段 -->
    <%@include file="../detail/select_meta_db_tbcol_ztree.jsp"%>

    <!-- 选择材料类别 -->
    <%@include file="../../../aplanmis/item/select_mat_type_ztree.jsp"%>

    <!-- 选择电子证照 -->
    <%@include file="../../../common/ztree/select_cert_no_right_ztree.jsp" %>

    <!-- 选择表单 -->
    <%@include file="../../../common/ztree/select_form_no_right_ztree.jsp" %>

    <!-- 选择标准材料 -->
    <%@include file="../../../common/ztree/select_stdmat_no_right_ztree.jsp" %>

    <!-- 选择电子证照文件库 -->
    <%@include file="../../../aplanmis/cert/select_bsc_att_dir_ztree.jsp"%>

    <!-- 查看附件 -->
    <%@include file="../../../aplanmis/item/show_mat_att_modal.jsp"%>

    <!-- 材料排序-->
    <%@include file="sort_item_out_index.jsp"%>

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

    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/set_item_out_index.js?<%=isDebugMode%>" type="text/javascript"></script>
</body>
</html>