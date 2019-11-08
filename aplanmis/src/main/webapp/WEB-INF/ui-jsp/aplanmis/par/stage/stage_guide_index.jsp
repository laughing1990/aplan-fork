<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>办事指南配置</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/kitymind/item/detail/css/set_item_dir_index.css">
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="text/javascript">
        var currentBusiType = '${currentBusiType}';
        var currentBusiId = '${currentBusiId}';
        var currentStateVerId = '';
        var isNeedState = '${isNeedState}';
        var handWay = '${handWay}';
        var useOneForm = '${useOneForm}';
        var curIsEditable = ${curIsEditable};  // 版本下数据是否可以编辑
    </script>
</head>
<body>
<jsp:include page="../../../kitymind/mindHeader.jsp"></jsp:include>
<div class="box">
    <div class="nav miniScrollbar">
        <div id="m_accordion_1" class="m-accordion m-accordion--default m-accordion--toggle-arrow" role="tablist">
            <!--begin::Item-->
            <div id="item1" class="m-accordion__item">
                <div class="m-accordion__item-head collapsed" role="tab" id="m_accordion_1_item_1_head"
                     data-toggle="collapse" href="#m_accordion_1_item_1_body" aria-expanded="true">
                    <span class="m-accordion__item-title">办事指南配置</span>
                </div>
                <div class="m-accordion__item-body collapse show" id="m_accordion_1_item_1_body"
                     role="tabpanel" aria-labelledby="m_accordion_1_item_1_head" data-parent="#m_accordion_1">
                    <div class="m-accordion__item-content">
                        <div class="m-section__content">
                            <ul>
                                <a href="${pageContext.request.contextPath}/aea/par/stage/guide/guideBasicInfo.do?stageId=${currentBusiId}" target="main">
                                    <li class="special">1.1 基本信息</li>
                                </a>
                                <a href="${pageContext.request.contextPath}/aea/par/stage/basicRel/index.do?stageId=${currentBusiId}" target="main">
                                    <li >1.2 设立依据</li>
                                </a>
                                <a href="${pageContext.request.contextPath}/aea/par/stage/windowRel/index.do?stageId=${currentBusiId}" target="main">
                                    <li>1.3 窗口办理</li>
                                </a>
                                <a href="${pageContext.request.contextPath}/aea/par/stage/charge/index.do?stageId=${currentBusiId}" target="main">
                                    <li>1.4 收费项目</li>
                                </a>
                                <a href="${pageContext.request.contextPath}/aea/par/stage/question/index.do?stageId=${currentBusiId}" target="main">
                                    <li>1.5 常见问题解答</li>
                                </a>
                                <%--<a href="${pageContext.request.contextPath}/aea/item/service/legal/indexAeaItemServiceLegal.do" target="main">--%>
                                    <%--<li>1.6 法律法规/条款</li>--%>
                                <%--</a>--%>
                                <%--<a href="${pageContext.request.contextPath}/aea/item/service/window/index.do" target="main">--%>
                                    <%--<li>1.7 服务窗口</li>--%>
                                <%--</a>--%>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--end::Item-->
        </div>
    </div>
    <div class="content">
        <ul class="page">
            <iframe id="showMainContent" src="${pageContext.request.contextPath}/aea/par/stage/guide/guideBasicInfo.do?stageId=${currentBusiId}"
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
