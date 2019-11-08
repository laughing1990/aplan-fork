<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项前置检测</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/set_front_check_item.js" type="text/javascript"></script>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '${currentStateVerId}';
        var curIsEditable = ${curIsEditable};
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var isNeedState = '${isNeedState}';
    </script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }

        .selectedItemSortDiv {
            color: #464637;
            font-size: 14px;
            font-family: 'Roboto', sans-serif;
            font-weight: 300;
        }

        /**标题样式**/
        .selectedItemSortDiv .block_list-title {
            padding: 10px;
            text-align: center;
            background: #abcdf1;
        }

        .selectedItemSortDiv ul {
            margin: 0;
            padding: 0;
            list-style: none;
            display: block;
        }

        .selectedItemSortDiv li {
            background-color: #fff;
            padding: 6px 0px;
            display: list-item;
            text-align: -webkit-match-parent;

            /**边框样式**/
            border: 1px solid transparent;
            border-bottom: 1px solid #ebebeb;
        }

        /**
            * 移动到li样式
        */
        .block_ul_td li:hover {
            background: #e7e8eb;
            cursor: move;
            cursor: -webkit-grabbing;
        }

        .selectedItemSortDiv .drag-handle_th {
            text-align: center;
            display: inline-block;
            width: 8%;
            font-weight: 600;
        }

        /**拖拽手把**/
        .selectedItemSortDiv .drag-handle_td {
            text-align: center;
            font: bold 16px Sans-Serif;
            color: #5F9EDF;
            display: inline-block;
            width: 8%;
        }

        .selectedItemSortDiv .ostage_name_td{
            display: inline-block;
            width: 45%;
        }

        #selectItemTree::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        #selectItemTree::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        #selectItemTree::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        #selectItemTree::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        #selectItemTree::-webkit-scrollbar-corner{
            background: #fff;
        }

        #selectedItemSortDiv::-webkit-scrollbar{
            width:4px;
            height:4px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-track{
            background: #fff;
            border-radius: 2px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-thumb{
            background: #e2e5ec;
            border-radius:2px;
        }
        #selectedItemSortDiv::-webkit-scrollbar-thumb:hover{
            background: #aaa;
        }
        #selectedItemSortDiv::-webkit-scrollbar-corner{
            background: #fff;
        }
    </style>
</head>
<body>
    <jsp:include page="../../mindHeader.jsp"></jsp:include>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div id="westPanel" class="col-xl-5" style="padding: 0px 2px 0px 8px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                工程建设审批事项库
                            </h3>
                        </div>
                    </div>
                    <div class="m-portlet__head-tools">
                        <font color="red">*&nbsp;</font>
                        请从工程建设审批事项库里面选择关联事项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-xl-5">
                            <input id="selectItemKeyWord" type="text"
                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                        </div>
                        <div class="col-xl-7">
                            <button type="button" class="btn btn-info"
                                    onclick="searchSelectItemNode();">查询</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="clearSearchSelectItemNode();">清空</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="expandSelectItemAllNode();">展开</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="collapseSelectItemAllNode();">折叠</button>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <div id="selectItemTree" class="ztree" style="overflow: auto;"></div>
                </div>
            </div>
        </div>

        <div class="col-xl-7" style="padding: 0px 8px 0px 2px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                           <span class="m-portlet__head-icon m--hide">
                               <i class="la la-gear"></i>
                           </span>
                            <h3 class="m-portlet__head-text">
                                选择事项
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div id="selectedFrontCheckItemDiv" class="selectedItemSortDiv" style="overflow: auto;">
                        <ul id="selectedFrontCheckItemUl" class="block_ul_td"></ul>
                    </div>
                </div>
                <div class="m-portlet__foot" style="padding: 10px;text-align: center;">
                    <button id="selectedFrontCheckItemBtn" type="button" class="btn btn-info">保存</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary" onclick="setItemRelFrontCheckItems();">刷新</button>&nbsp;&nbsp;
                </div>
            </div>
        </div>
    </div>
</body>
</html>