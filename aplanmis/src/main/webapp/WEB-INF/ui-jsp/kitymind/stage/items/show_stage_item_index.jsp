<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>办理事项列表</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
    </style>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '';
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var curIsEditable = ${curIsEditable};
        var useOneForm = '${useOneForm}';
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
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="keyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info"
                                            onclick="searchStageItem();">查询</button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="clearSearchStageItem();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px">
                    <table  id="show_stage_item_list_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            data-pagination=true,
                            data-page-size="10",
                            data-page-list="[10]",
                            data-side-pagination="server",
                            data-query-params="stageItemParam",
                            data-pagination-show-page-go="true",
                            data-url="${pageContext.request.contextPath}/aea/par/stage/item/listStageAllItemByPage.do">
                        <thead>
                            <tr>
                                <th data-field="isOptionItem" data-align="left" data-width="100"
                                    data-formatter="isOptionItemFormatter">事项类型</th>
                                <th data-field="itemCode" data-align="left" data-width=250>事项编号</th>
                                <th data-field="itemName" data-align="left" data-width=250
                                    data-formatter="itemNameFormatter">事项名称</th>
                                <th data-field="orgName" data-align="left" data-width=200
                                    data-formatter="orgNameFormatter">所属组织</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        $('#mainContentPanel').css('height',$(document.body).height()-20);

        $('#show_stage_item_list_tb').bootstrapTable('resetView',{
            height: $('#westPanel').height()-116
        });

        $(".fixed-table-body").niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        searchStageItem();
    });

    //参数设置
    var commonQueryParams = {};
    function stageItemParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        commonQueryParams['pageNum'] = pageNum;
        commonQueryParams['pageSize'] = params.limit;
        commonQueryParams['stageId'] = currentBusiId;
        commonQueryParams['keyword'] =  $('#keyword').val();
        return commonQueryParams;
    }

    function isOptionItemFormatter(value, row, index, field) {

        if(value){
            if(value=='0'){
                return '并联审批事项';
            }else if(value=='1'){
                return '并行推进事项';
            }else{
                return '前置检查事项';
            }
        }
    }

    function searchStageItem(){

        commonQueryParams = {};
        commonQueryParams['stageId'] = currentBusiId;
        commonQueryParams['keyword'] =  $('#keyword').val();
        $("#show_stage_item_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#show_stage_item_list_tb").bootstrapTable('refresh',commonQueryParams);       //无参数刷新
    }

    function clearSearchStageItem(){

        $('#keyword').val('');
        searchStageItem();
    }

    function itemNameFormatter(value, row, index, field) {

        var itemName = row.itemName;
        if(!isEmpty(row.isCatalog)){
            if(row.isCatalog=='1'){
                itemName = '【标准事项】'+ itemName;
            }else{
                itemName = '【实施事项】'+ itemName;
            }
        }
        return itemName;
    }

    function orgNameFormatter(value, row, index, field) {

        var orgName = row.orgName;
        if(!isEmpty(row.isCatalog)){
            if(row.isCatalog=='1'){
                orgName = row.guideOrgName;
            }else{
                orgName = row.orgName;
            }
        }
        return orgName;
    }

    //判断字符是否为空的方法
    function isEmpty(obj){

        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }
</script>
</body>
</html>