<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>查看事项操作指南</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/register-list.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .m-portlet {
            border: 0px solid #e8e8e8;
        }
        table{
            width:100%;
        }
        .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__body .m-datatable__row.m-datatable__row--even > .m-datatable__cell{
            background: #ffffff;
        }
        .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__body .m-datatable__row.m-datatable__row--hover:not(.m-datatable__row--active) > .m-datatable__cell {
            background: #f7f6fa;
        }
    </style>
</head>
<body style="background-color:white;">
<!-- 主体内容 end -->
<div class="m-portlet" id="liveRegister">
    <div class="m-portlet__body">
        <!-- 左侧导航  start -->
        <div class="vertical">
            <div class="vertical-time" id="menu">
                <ul>
                    <li class="ciclebox ciclebox1 active" name="div_step_1">
                        <a href="#div_step_1">基本信息</a>
                    </li>
                    <li class="ciclebox ciclebox2" name="div_step_2">
                        <a href="#div_step_2">受理条件</a>
                    </li>
                    <li class="ciclebox ciclebox3" name="div_step_3">
                        <a href="#div_step_3">办理流程</a>
                    </li>
                    <li class="ciclebox ciclebox4" name="div_step_4">
                        <a href="#div_step_4">申请材料</a>
                    </li>
                    <li class="ciclebox ciclebox5" name="div_step_5">
                        <a href="#div_step_5">咨询监督</a>
                    </li>
                    <li class="ciclebox ciclebox6" name="div_step_6">
                        <a href="#div_step_6">窗口办理</a>
                    </li>
                    <li class="ciclebox ciclebox7" name="div_step_7">
                        <a href="#div_step_7">许可收费</a>
                    </li>
                    <li class="ciclebox ciclebox8" name="div_step_8">
                        <a href="#div_step_8">中介服务</a>
                    </li>
                    <li class="ciclebox ciclebox9" name="div_step_9">
                        <a href="#div_step_9">设立依据</a>
                    </li>
                    <li class="ciclebox ciclebox10" name="div_step_10">
                        <a href="#div_step_10">权利与义务</a>
                    </li>
                    <li class="ciclebox ciclebox11" name="div_step_11">
                        <a href="#div_step_11">法律救济</a>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 左侧导航  end -->
        <div class="m-portlet-left">

            <!-- 办事对象信息  start -->
            <div id="div_step_1" class="div_step active">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_1" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part1" class="matters-subtitle">基本信息</h2>
                            <div class="table-collapse-wrapper">
                                <table class="matters-table matters-table-bordered matters-table-info"
                                       style="min-width: 450px;" data-toggle="table-collapse" data-row="3">

                                    <tbody>
                                    <tr style="display: table-row;">
                                        <th>事项名称</th>
                                        <td>
                                            <p class="matters-truncate">
                                                ${item.itemName != null ? item.itemName : "无"}
                                            </p>
                                        </td>
                                        <th>事项类型</th>
                                        <td><p class="matters-truncate">${item.itemType != null ? item.itemType : "无"}</p></td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>基本编码</th>
                                        <td><p class="matters-truncate">${item.itemCode != null ? item.itemCode : "无"}</p></td>
                                        <th>实施编码</th>
                                        <td><p class="matters-truncate">${item.sscode != null ? item.sscode : "无"}</p></td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>行使层级</th>
                                        <td><p class="matters-truncate">${item.sqjb != null ? item.sqjb : "无"}</p></td>
                                        <th>处罚的行为、种类、幅度</th>
                                        <td>
                                            <p class="matters-truncate">
                                                无
                                            </p>
                                        </td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>实施主体</th>
                                        <td><p class="matters-truncate">${item.aeaItemExeorg.ssjgmc != null ? item.aeaItemExeorg.ssjgmc : "无"}</p></td>
                                        <th>实施主体性质</th>
                                        <td><p class="matters-truncate">${item.aeaItemExeorg.ssjglb != null ? item.aeaItemExeorg.ssjglb : "无"}</p></td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>法定办结时限(工作日)</th>
                                        <td>
                                            <p class="matters-truncate">${item.dueNum != null ? item.dueNum : "无"}</p>
                                        </td>
                                        <th>业务系统</th>
                                        <td><p class="matters-truncate">${item.aeaItemServiceServe.ywxtmc != null ? item.aeaItemServiceServe.ywxtmc : "无"}</p></td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>结果名称</th>
                                        <td><p class="matters-truncate">无</p></td>
                                        <th>结果样本</th>
                                        <td>
                                            <p class="matters-truncate">无</p>
                                        </td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>办理结果类型</th>
                                        <td colspan="3"><p class="matters-truncate">${item.aeaItemServiceServe.bjlx != null ? item.aeaItemServiceServe.bjlx : "无"}</p></td>
                                    </tr>

                                    <tr style="display: table-row;">
                                        <th>权力来源</th>
                                        <td><p class="matters-truncate">无</p></td>
                                        <th>联办机构</th>
                                        <td><p class="matters-truncate">${item.aeaItemExeorg.sfylbjg == 1 ? item.aeaItemExeorg.lbjg : "无"}</p></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 受理条件 -->
            <div id="div_step_2" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_2" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part2" class="matters-subtitle">受理条件</h2>
                            <p class="matters-truncate">
                                ${item.aeaItemAcceptRange.sqtj != null ? item.aeaItemAcceptRange.sqtj : "无"}
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 办理流程 -->
            <div id="div_step_3" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_3" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part3" class="matters-subtitle">办理流程</h2>
                            <h3>网上办理流程</h3>
                            <p class="matters-truncate">${item.aeaItemServiceFlow.wsbllcsm}</p>
                            <p>
                                <input id="wsbllct" type="hidden" name="wsbllct" value="${item.aeaItemServiceFlow.wsbllct}"/>
                                <a id="wsbllcttu" class="btn btn-default btn-small btn-disabled" href="javascript:downloadFlow(0);">查看流程图</a>
                            </p>
                            <p id="wsbllctwu"></p>
                            <h3>窗口办理流程</h3>
                            <p class="matters-truncate">${item.aeaItemServiceFlow.ckbllcsm}</p>
                            <p>
                                <input id="ckbllct" type="hidden" name="ckbllct" value="${item.aeaItemServiceFlow.ckbllct}"/>
                                <a id="ckbllcttu" class="btn btn-default btn-small matters-detail-attach" data-attach="[{&quot;FILEPATH&quot;:&quot;//static.gdzwfw.gov.cn/obs/obs-sxml/txyCos62CCE676F9A5E3B19E198B936203AA80A0C189946867811034F140A86907F294147D5F1D4F8682A7&quot;,&quot;ATTACHGUID&quot;:&quot;aecbc4c6-b0e8-44c6-acae-56c02cd2ed95&quot;,&quot;ATTACHNAME&quot;:&quot;112.jpg&quot;}]" href="javascript:downloadFlow(1);">查看流程图</a>
                            </p>
                            <p id="ckbllctwu"></p>
                            <h3>特殊环节</h3>
                            无
                        </div>
                    </div>
                </div>
            </div>

            <!-- 申请材料 -->
            <div id="div_step_4" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_4" role="tablist">
                    <div class="m-accordion__item">
                        <h2 id="matters-part4" class="matters-subtitle">申请材料</h2>
                        <input id="itemStr" type="hidden" name="itemStr" value="${itemStr}"/>
                        <p id="ishavemat"></p>
                        <div class="m_datatable" id="application_mat"></div>
                    </div>
                </div>
            </div>

            <div id="div_step_5" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_5" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part5" class="matters-subtitle">咨询监督</h2>
                            <div class="grid">
                                <div class="col6">
                                    <h3>咨询方式</h3>
                                    <p>
                                        本单位投诉受理部门名称：${zxConsulting.gwzzhqx != null ? zxConsulting.gwzzhqx : "无"}；
                                        投诉电话：${zxConsulting.dh != null ? zxConsulting.dh : "无"}；<br />
                                        通讯地址：${zxConsulting.dz != null ? zxConsulting.dz : "无"}，
                                        邮政编码：${zxConsulting.yzbm != null ? zxConsulting.yzbm : "无"}。
                                    </p>
                                </div>
                                <div class="col6">
                                    <h3>监督方式</h3>
                                    <p>
                                        本单位投诉受理部门名称：${jdConsulting.gwzzhqx != null ? jdConsulting.gwzzhqx : "无"}；
                                        投诉电话：${jdConsulting.dh != null ? jdConsulting.dh : "无"}；<br />
                                        通讯地址：${jdConsulting.dz != null ? jdConsulting.dz : "无"}，
                                        邮政编码：${jdConsulting.yzbm != null ? jdConsulting.yzbm : "无"}。
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 窗口办理 -->
            <div id="div_step_6" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_6" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part6" class="matters-subtitle">窗口办理</h2>
                            <p class="matters-truncate">
                                ${item.aeaItemServiceWindow.windowName}<br>
                                办理地点：${item.aeaItemServiceWindow.windowAddress != null ? item.aeaItemServiceWindow.windowAddress : "无"}<br>
                                办公电话：${item.aeaItemServiceWindow.linkPhone != null ? item.aeaItemServiceWindow.linkPhone : "无"}<br>
                                办公时间：${item.aeaItemServiceWindow.workTime != null ? item.aeaItemServiceWindow.workTime : "无"}<br>
                                位置指引：${item.aeaItemServiceWindow.trafficGuide != null ? item.aeaItemServiceWindow.trafficGuide : "无"}
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 许可收费 -->
            <div id="div_step_7" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_7" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part7" class="matters-subtitle">许可收费</h2>
                            <p class="matters-truncate">
                                ${item.aeaItemServiceCharge.ywsf == 0 ? "不收费" : ""}<br>
                                收费环节：${item.aeaItemServiceCharge.jfhj != null ? item.aeaItemServiceCharge.jfhj : "无"}<br>
                                缴费地点：${item.aeaItemServiceCharge.jfdd != null ? item.aeaItemServiceCharge.jfdd : "无"}<br>
                                缴费时间：${item.aeaItemServiceCharge.jfsj != null ? item.aeaItemServiceCharge.jfsj : "无"}<br>
                                缴费方式：${item.aeaItemServiceCharge.jffs != null ? item.aeaItemServiceCharge.jffs : "无"}<br>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 中介服务 -->
            <div id="div_step_8" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_8" role="tablist">
                    <div class="m-accordion__item">
                        <h2 id="matters-part8" class="matters-subtitle">中介服务</h2>
                        <p id="ishaveservice"></p>
                        <div class="m_datatable" id="intermediary_services"></div>
                        <%--<div class="table-collapse-wrapper">--%>
                        <%--<table class="matters-table matters-table-bordered matters-table-info"--%>
                        <%--style="min-width: 450px;" data-toggle="table-collapse" data-row="3">--%>
                        <%--<thead>--%>
                        <%--<th>中介服务事项名称</th>--%>
                        <%--<th>中介服务事项编码</th>--%>
                        <%--<th>详情</th>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--<tr style="display: table-row;">--%>
                        <%--<td>建设项目选址评估报告</td>--%>
                        <%--<td>无</td>--%>
                        <%--<td>查看详情</td>--%>
                        <%--</tr>--%>
                        <%--</tbody>--%>
                        <%--</table>--%>
                        <%--</div>--%>
                    </div>
                </div>
            </div>


            <div id="div_step_9" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_9" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part9" class="matters-subtitle">设定依据</h2>
                            <div class="table-collapse-wrapper">
                                <table class="matters-table matters-table-bordered" data-toggle="table-collapse" data-row="6">
                                    <tbody>
                                    <c:forEach items="${item.aeaItemServiceLegalClause}" var="LegalClause" varStatus="status">
                                        <tr>
                                            <th rowspan="6" width="90">设立依据${ status.index + 1}</th>
                                            <th width="110">法律法规名称</th>
                                            <td>${LegalClause.legalName}</td>
                                        </tr>
                                        <tr>
                                            <th width="110">依据文号</th>
                                            <td>${LegalClause.basicNo}</td>
                                        </tr>
                                        <tr>
                                            <th width="110">条款号</th>
                                            <td>${LegalClause.clauseTitle}</td>
                                        </tr>
                                        <tr>
                                            <th width="110">颁布机关</th>
                                            <td>${LegalClause.issueOrg}</td>
                                        </tr>
                                        <tr>
                                            <th width="110">实施日期</th>
                                            <td><fmt:formatDate value="${LegalClause.exeDate}" pattern="yyyy-MM-dd"/></td>
                                        </tr>
                                        <tr>
                                            <th width="110">条款内容</th>
                                            <td>
                                                <p class="matters-truncate">${LegalClause.clauseContent}</p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <p id="ishavelegalclause"></p>
                                <a href="javascript:;" class="table-collapse-toggle" style="display: none;"></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 权利与义务 -->
            <div id="div_step_10" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_10" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2  id="matters-part10" class="matters-subtitle">权利与义务</h2>
                            <div class="grid">
                                <div class="col6">
                                    <h3>权利</h3>
                                    <p>
                                        申请人依法享有以下权利<br>
                                        ${qlRights.rightObliDetails != null ? qlRights.rightObliDetails : "无"}
                                    </p>
                                </div>

                                <div class="col6">
                                    <h3>义务</h3>
                                    <p>
                                        申请人依法履行以下义务<br>
                                        ${ywRights.rightObliDetails != null ? ywRights.rightObliDetails : "无"}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 法律救济 -->
            <div id="div_step_11" class="div_step">
                <div class="m-accordion m-accordion--default m-accordion--solid m-accordion--section
                     m-accordion--toggle-arrow" id="m_accordion_11" role="tablist">
                    <div class="m-accordion__item">
                        <div class="matters-content-part">
                            <h2 id="matters-part11" class="matters-subtitle">法律救济</h2>
                            <div class="grid">
                                <div class="col6">
                                    <h3>行政复议</h3>
                                    <p>
                                        部门：${item.aeaItemLegalRemedy.admReconDept != null ? item.aeaItemLegalRemedy.admReconDept : "无"}<br>
                                        地址：${item.aeaItemLegalRemedy.admReconAddress != null ? item.aeaItemLegalRemedy.admReconAddress : "无"}<br>
                                        电话：${item.aeaItemLegalRemedy.admReconTel != null ? item.aeaItemLegalRemedy.admReconTel : "无"}<br>
                                        网址：${item.aeaItemLegalRemedy.admReconNetAddress != null ? item.aeaItemLegalRemedy.admReconNetAddress : "无"}
                                    </p>
                                </div>

                                <div class="col6">
                                    <h3>行政诉讼</h3>
                                    <p>
                                        部门：${item.aeaItemLegalRemedy.admProceDept != null ? item.aeaItemLegalRemedy.admProceDept : "无"}<br>
                                        地址：${item.aeaItemLegalRemedy.admProceAddress != null ? item.aeaItemLegalRemedy.admProceAddress : "无"}<br>
                                        电话：${item.aeaItemLegalRemedy.admProceTel != null ? item.aeaItemLegalRemedy.admProceTel : "无"}<br>
                                        网址：${item.aeaItemLegalRemedy.admProceNetAddress != null ? item.aeaItemLegalRemedy.admProceNetAddress : "无"}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 主体内容 end -->

<!-- 上传弹出框 end -->

<!-- 弹出框 end -->
</body>
<script type="text/javascript">

    $(document).ready(function(){
        //  begin 滚动时，固定左侧的menu 并导航到相对位置
        var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码
        //3、 滚动条滚动 --  找到当前楼层的索引    控制楼层号  固定操作按钮
        $(window).scroll(function () {
            if (flag) {
                var items = $(".div_step");
                var menu = $("#menu");
                var top = $(document).scrollTop();
                var currentId = ""; //滚动条现在所在位置的item id
                var cl = ''; // 元素标识Id

                items.each(function (index,item) {
                    var m = $(this);
                    //注意：m.offset().top代表每一个item的到顶部位置
                    if ( m.offset().top -50  < top ) {
                        currentId = "#" + m.attr("id");
                        cl = m.attr("id");
                    } else {
                        return false;
                    }
                });
                var currentLink = menu.find(".active");
                if (currentId && currentLink.children().attr("href") != currentId) {
                    currentLink.removeClass("active");
                    menu.find("[name=" + cl + "]").addClass("active");
                    $(currentId).addClass("active").siblings().removeClass("active");
                }
            }
        });
        // end 滚动时，固定左侧的menu 并导航到相对位置

        $('#menu').find('.ciclebox').click(function(){
            $(this).addClass("active").siblings().removeClass("active");
        })
    })

    $(function(){
        if($("#wsbllct").val() == ""){
            $("#wsbllcttu").hide();
            $("#wsbllctwu").html("无");
        }
        if($("#ckbllct").val() == ""){
            $("#ckbllcttu").hide();
            $("#ckbllctwu").html("无");
        }
        var itemStr = $("#itemStr").val();
        var list = eval('(' + itemStr + ')');
        var legalClauselist = list.aeaItemServiceLegalClause;
        if(legalClauselist == undefined || legalClauselist.length == 0){
            $("#ishavelegalclause").html("无");
        }
        shenqingcailiao();//申请材料
        zhongjiefuwu();//中介服务
    });

    function shenqingcailiao(){
        var datatable;
        var itemStr = $("#itemStr").val();
        var list = eval('(' + itemStr + ')');
        var inoutlist = list.aeaItemInout;
        if(inoutlist == undefined || inoutlist.length == 0){
            $("#ishavemat").html("无");
        }
        else {
            if (datatable != null) datatable.destroy();
            datatable = $('#application_mat').mDatatable({
                data: {
                    type: 'local',
                    source: inoutlist
                },
                layout: {
                    theme: 'default', // datatable theme
                    class: '', // custom wrapper class
                    scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
                    // height: 450, // datatable's body's fixed height
                    footer: false // display/hide footer
                },
                sortable: true,
                pagination: false,
                toolbar: {
                    items: {
                        pagination: {
                            pageSizeSelect: [10, 20, 30]
                        }
                    }
                },
                columns: [{
                    field: "aeaMatCertName",
                    title: "材料名称",
                    width: 200
                }, {
                    field: "_operate",
                    title: "材料要求",
                    sortable: false,
                    width: 200,
                    template: function (row, index, datatable) {
                        if (row.fileType == "mat") {
                            if (row.aeaItemMat.sortNo != 0) {
                                var sortNo = '原件:' + row.aeaItemMat.sortNo + '<br>';
                            }
                            if (row.aeaItemMat.dueCopyCount != 0) {
                                var dueCopyCount = '复印件：' + row.aeaItemMat.dueCopyCount + '<br>';
                            }
                            if (row.aeaItemMat.attIsRequire == 1) {
                                var type = '材料形式：纸质/电子化<br>';
                            }
                            else if (row.aeaItemMat.attIsRequire == 0) {
                                var type = '材料形式：纸质<br>';
                            }
                            var matType;
                            $.ajax({
                                url: ctx + '/aea/item/mat/type/getListMatTypeZtreeNode.do',
                                type: 'POST',
                                data: {},
                                async: false,
                                success: function (data) {
                                    if (data) {
                                        if (data.length > 0) {
                                            for (var i = 0; i < data.length; i++) {
                                                if (row.aeaItemMat.matTypeId == data[i].id) {
                                                    matType = '材料分类：' + data[i].name;
                                                }
                                            }
                                        }
                                    }
                                },
                                error: function () {
                                    swal('错误信息', '获取材料分类信息出错！', 'error');
                                }
                            })
                            return sortNo + dueCopyCount + type + matType;
                        }
                        else if (row.fileType == "cert") {
                            var cert = '电子证照';
                            return cert;
                        }
                    }
                }, {
                    field: "_from",
                    title: "来源",
                    sortable: false,
                    width: 200,
                    template: function (row, index, datatable) {
                        var from = '';
                        if (row.fileType == "mat") {
                            if (row.aeaItemMat.matHolder == 'c') {
                                from = '企业';
                            }
                            else if (row.aeaItemMat.matHolder == 'u') {
                                from = '个人';
                            }
                            else if (row.aeaItemMat.matHolder == 'p') {
                                from = '工程项目';
                            }
                        }
                        else if (row.fileType == "cert") {
                            if (row.aeaCert.matFrom == 'c') {
                                from = '企业';
                            }
                            else if (row.aeaCert.matFrom == 'u') {
                                from = '个人';
                            }
                            else if (row.aeaCert.matFrom == 'p') {
                                from = '项目';
                            }
                        }
                        return from;
                    }
                }, {
                    field: "_memo",
                    title: "填报须知",
                    sortable: false,
                    width: 200,
                    template: function (row, index, datatable) {
                        if (row.fileType == "mat") {
                            var memo = row.aeaItemMat.matMemo;
                        }
                        else if (row.fileType == "cert") {
                            var memo = row.aeaCert.certMemo;
                        }
                        return memo;
                    }
                }, {
                    field: "_sampleDoc",
                    title: "资料下载",
                    sortable: false,
                    width: 200,
                    template: function (row, index, datatable) {
                        if (row.fileType == "mat") {
                            var sampleDoc = '<a class="btn btn-default btn-small btn-disabled" href="javascript:downloadDoc(row.aeaItemMat.sampleDoc);">样本</a>';
                        }
                        else if (row.fileType == "cert") {
                            var sampleDoc = '';
                        }
                        return sampleDoc;
                    }
                    // }, {
                    //     field: "matName",
                    //     title: "中介服务",
                    //     width: 200
                }
                ]
            });
        }
    }

    function zhongjiefuwu(){
        var datatable1;
        var itemStr = $("#itemStr").val();
        var list = eval('(' + itemStr + ')');
        var auxServicelist = list.aeaItemAuxService;
        if(auxServicelist == undefined || auxServicelist.length == 0){
            $("#ishaveservice").html("无");
        }
        else{
            if (datatable1 != null) datatable1.destroy();
            datatable1 = $('#intermediary_services').mDatatable({
                data: {
                    type: 'local',
                    source: auxServicelist
                },
                layout: {
                    theme: 'default', // datatable theme
                    class: '', // custom wrapper class
                    scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
                    // height: 450, // datatable's body's fixed height
                    footer: false // display/hide footer
                },
                sortable: true,
                pagination: false,
                toolbar: {
                    items: {
                        pagination: {
                            pageSizeSelect: [10, 20, 30]
                        }
                    }
                },
                columns: [{
                    field: "fwxmmc",
                    title: "服务项目名称",
                    width: 200
                }, {
                    field: "xzfwjgfs",
                    title: "选择服务机构方式",
                    width: 200
                }, {
                    field: "fwjgmc",
                    title: "服务机构名称",
                    width: 200
                }, {
                    field: "fwjgxz",
                    title: "服务机构性质",
                    width: 200
                }, {
                    field: "fwsx",
                    title: "服务时限",
                    width: 200
                }, {
                    field: "_c",
                    title: "服务收费情况",
                    sortable: false,
                    width: 200,
                    template: function (row, index, datatable) {
                        if(row.ywsf == 1){
                            var ywsf = "有收费";
                        }
                        else if(row.ywsf == 0){
                            var ywsf = "无收费";
                        }
                        return ywsf;
                    }
                }
                ]
            });
        }
    }

    function downloadFlow(type) {
        var fileId;
        if(type==0){
            fileId = $("#wsbllct").val();
        }else if(type==1){
            fileId = $("#ckbllct").val();
        }
        window.location.href = ctx + '/aea/item/service/flow/downloadFlowDoc.do?detailId=' + fileId;
    }

    function downloadDoc(fileId) {
        window.location.href = ctx + '/aea/item/mat/downloadItemMatDoc.do?detailId=' + fileId;
    }

</script>
</html>