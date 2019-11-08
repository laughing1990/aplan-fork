<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项指南配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '${currentStateVerId}';
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var curIsEditable = ${curIsEditable};  // 版本下数据是否可以编辑
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/kitymind/item/detail/css/set_item_dir_index.css">
</head>
<body>
    <jsp:include page="../../mindHeader.jsp"></jsp:include>
    <div class="box">
        <div class="nav miniScrollbar">
            <div class="m-accordion m-accordion--default m-accordion--toggle-arrow" id="m_accordion_5" role="tablist">

                <!--begin::Item-->
                <div class="m-accordion__item" id="item6">
                    <div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_6_item_6_head"
                         data-toggle="collapse" href="#m_accordion_6_item_6_body" aria-expanded="true">
                        <span class="m-accordion__item-title">事项指南配置</span>
                    </div>
                    <div class="m-accordion__item-body collapse show" id="m_accordion_6_item_6_body"
                         role="tabpanel" aria-labelledby="m_accordion_6_item_6_head" data-parent="#m_accordion_6">
                        <div class="m-accordion__item-content">
                            <div class="m-section__content">
                                <ul>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/basicInfoIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li class="special">1.1 基本信息</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/basic/preConditionMind/index.do?busiType=item&busiId=${currentBusiId}&stateVerId=${currentStateVerId}" target="main">
                                        <li >1.2 前置条件</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/baseOnInfoIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li>1.3 设立依据</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/itemServiceWindowRelIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li>1.4 窗口办理</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/extendInfoIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li>1.5 扩展信息</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/chargeInfoIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li>1.6 收费信息</li>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/aea/item/guide/questionIndex.do?itemVerId=${currentBusiId}" target="main">
                                        <li>1.7 常见问题解答</li>
                                    </a>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/legal/indexAeaItemServiceLegal.do" target="main">--%>
                                        <%--<li>1.8 法律法规/条款</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/window/index.do" target="main">--%>
                                        <%--<li>1.9 服务窗口</li>--%>
                                    <%--</a>--%>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!--end::Item-->

                <!--begin::Item-->
                <%--<div class="m-accordion__item active" id="item1" style="margin-top: 3px;">--%>
                    <%--<div class="m-accordion__item-head" srole="tab" id="m_accordion_5_item_1_head"--%>
                         <%--data-toggle="collapse" href="#m_accordion_5_item_1_body" aria-expanded="true">--%>
                         <%--<span class="m-accordion__item-title">--%>
                            <%--1.目录管理&nbsp;(旧)--%>
                         <%--</span>--%>
                    <%--</div>--%>
                    <%--<div class="m-accordion__item-body collapse" id="m_accordion_5_item_1_body"--%>
                         <%--role="tabpanel" aria-labelledby="m_accordion_5_item_1_head" data-parent="#m_accordion_1">--%>
                        <%--<div class="m-accordion__item-content">--%>
                            <%--<div class="m-section__content">--%>
                                <%--<ul>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/basic/indexItemBasicInfo.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>1.1 一般信息</li>--%>
                                    <%--</a>--%>

<%--&lt;%&ndash;                                    <a href="${pageContext.request.contextPath}/aea/item/basic/frontCondIndex.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li >1.2 前置条件</li>--%>
                                    <%--</a>&ndash;%&gt;--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/basic/preConditionMind/index.do?busiType=item&busiId=${itemBasic.itemVerId}&stateVerId=${currentStateVerId}" target="main">--%>
                                        <%--<li >1.2 前置条件</li>--%>
                                    <%--</a>--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--end::Item-->

                <!--begin::Item-->
                <%--<div class="m-accordion__item" id="item2">--%>
                    <%--<div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_5_item_2_head"--%>
                         <%--data-toggle="collapse" href="#m_accordion_5_item_2_body" aria-expanded="true">--%>
                        <%--<span class="m-accordion__item-title">--%>
                            <%--2.许可要素&nbsp;(旧)--%>
                        <%--</span>--%>
                    <%--</div>--%>
                    <%--<div class="m-accordion__item-body collapse " id="m_accordion_5_item_2_body"--%>
                         <%--role="tabpanel" aria-labelledby="m_accordion_5_item_2_head" data-parent="#m_accordion_2">--%>
                        <%--<div class="m-accordion__item-content">--%>
                            <%--<div class="m-section__content">--%>
                                <%--<ul>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/accept/range/index.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>2.1 受理范围</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/basic/indexAeaItemServiceBasic.do?recordId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>2.2 设立依据</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/exeorg/indexAeaItemExeorg.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>2.3 实施机关</li>--%>
                                    <%--</a>--%>
                                   <%--&lt;%&ndash; <a href="#" target="main">--%>
                                        <%--<li>2.4 许可条件</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="#" target="main">--%>
                                        <%--<li>2.5 申请材料</li>--%>
                                    <%--</a>&ndash;%&gt;--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/cert/indexAeaItemServiceCert.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>2.4 许可证件</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/timelimit/indexAeaItemServiceTimelimit.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>2.5 许可时限</li>--%>
                                    <%--</a>--%>
                                    <%--&lt;%&ndash;<a href="https://www.w3cschool.cn/atomflightmanualzhcn/kju21lon.html" target="main">--%>
                                        <%--<li>2.8 许可收费</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="https://www.w3cschool.cn/atomflightmanualzhcn/kju21lon.html" target="main">--%>
                                        <%--<li>2.9 前置许可</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="#" target="main">--%>
                                        <%--<li>2.10 中介服务</li>--%>
                                    <%--</a>&ndash;%&gt;--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--end::Item-->

                <!--begin::Item-->
                <%--<div class="m-accordion__item" id="item3">--%>
                    <%--<div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_5_item_3_head"--%>
                         <%--data-toggle="collapse" href="#m_accordion_5_item_3_body" aria-expanded="true">--%>
                         <%--<span class="m-accordion__item-title">3.许可流程&nbsp;(旧)</span>--%>
                    <%--</div>--%>
                    <%--<div class="m-accordion__item-body collapse " id="m_accordion_5_item_3_body"--%>
                         <%--role="tabpanel" aria-labelledby="m_accordion_5_item_3_head" data-parent="#m_accordion_3">--%>
                        <%--<div class="m-accordion__item-content">--%>
                            <%--<div class="m-section__content">--%>
                                <%--<ul>--%>
                                    <%--<li>3.1 许可流程</li>--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--end::Item-->

                <!--begin::Item-->
                <%--<div class="m-accordion__item" id="item4">--%>
                    <%--<div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_5_item_4_head"--%>
                         <%--data-toggle="collapse" href="#m_accordion_5_item_4_body" aria-expanded="true">--%>
                         <%--<span class="m-accordion__item-title">4.许可服务(旧)</span>--%>
                    <%--</div>--%>
                    <%--<div class="m-accordion__item-body collapse " id="m_accordion_5_item_4_body"--%>
                         <%--role="tabpanel" aria-labelledby="m_accordion_5_item_4_head" data-parent="#m_accordion_4">--%>
                        <%--<div class="m-accordion__item-content">--%>
                            <%--<div class="m-section__content">--%>
                                <%--<ul>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/serve/index.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>4.1 许可服务</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/consulting/indexAeaItemServiceConsulting.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>4.2 许可咨询</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/legal/remedy/editAeaItemLegalRemedy.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>4.3 法律救济</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/right/obli/index.do?itemBasicId=${itemBasic.itemBasicId}" target="main">--%>
                                        <%--<li>4.4 权利与义务</li>--%>
                                    <%--</a>--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--end::Item-->

                <!--begin::Item-->
                <%--<div class="m-accordion__item" id="item5">--%>
                    <%--<div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_5_item_5_head"--%>
                         <%--data-toggle="collapse" href="#m_accordion_5_item_5_body" aria-expanded="true">--%>
                        <%--<span class="m-accordion__item-title">5.监督检查&nbsp;(旧)</span>--%>
                    <%--</div>--%>
                    <%--<div class="m-accordion__item-body collapse " id="m_accordion_5_item_5_body"--%>
                         <%--role="tabpanel" aria-labelledby="m_accordion_5_item_4_head" data-parent="#m_accordion_5">--%>
                        <%--<div class="m-accordion__item-content">--%>
                            <%--<div class="m-section__content">--%>
                                <%--<ul>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/supervise/inspect/index.do" target="main">--%>
                                        <%--<li>5.1 监督检查</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/legal/indexAeaItemServiceLegal.do" target="main">--%>
                                        <%--<li>5.2 法律法规/条款</li>--%>
                                    <%--</a>--%>
                                    <%--<a href="${pageContext.request.contextPath}/aea/item/service/window/index.do" target="main">--%>
                                        <%--<li>5.3 服务窗口</li>--%>
                                    <%--</a>--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--end::Item-->
            </div>
        </div>
        <div class="content">
            <ul class="page">
                <iframe id="showMainContent" src="${pageContext.request.contextPath}/aea/item/guide/basicInfoIndex.do?itemVerId=${currentBusiId}"
                        width="100%" height="100%" scrolling="auto" scrolling="no"  frameborder="no"  name="main"/></iframe>
            </ul>
        </div>
    </div>

    <script type="text/javascript">

          $(document).ready(function(){

                var navs = $(".m-section__content").children().children().children();
                $(navs).click(function(e){
                  navs.removeClass("special")
                  $(this).addClass("special");
                })
                //根据屏幕的高度给NAV一个固定的高度
                var winH = $(window).height()-100;
                $(".nav").css('height', winH);
                $(".content").css('height', winH);

                $('#showMainContent').niceScroll({

                    cursorcolor: "#e2e5ec",//#CC0071 光标颜色
                    cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
                    cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
                    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                    cursorwidth: "4px", //像素光标的宽度
                    cursorborder: "0", //   游标边框css定义
                    cursorborderradius: "2px",//以像素为光标边界半径
                    autohidemode: true  //是否隐藏滚动条
                });
          });
    </script>

</body>
</html>
