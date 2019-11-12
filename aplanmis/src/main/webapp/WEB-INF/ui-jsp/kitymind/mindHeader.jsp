<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    li {
        list-style: none;
    }

    .container-tab .menu {
        display: flex;
        display: -webkit-flex;
        width: 100%;
        justify-content: space-between;
    }

    .container-tab .menu li {
        width: 100%;
        height: 44px;
        line-height: 44px;
        text-align: center;
        background-color: #ccc;
        color: #fff;
        font-weight: 700;
        margin-left: 5px;
        margin-right: 5px;
        cursor: pointer;
    }

    .container-tab .menu li.active {
        background: #FE9900;
    }

    @media (max-width: 1200px) {
        .skipElem {
            margin-top: 4% !important;
        }
    }

    @media (max-width: 992px) {
        .skipElem {
            margin-top: 8% !important;
        }

        div.minder-editor-container {
            top: 18% !important;
        }
    }

    @media (max-width: 768px) {
        .skipElem {
            margin-top: 10% !important;
        }

        div.minder-editor-container {
            top: 20% !important;
        }
    }

    @media (max-width: 576px) {
        .skipElem {
            margin-top: 12% !important;
        }
    }

    .skipElem {
        margin-top: 3.4%;
        max-width: 100% !important;
        margin-left: 0%;
    }

    .skipElem {
        position: fixed;
        left: 0px;
        top: 0px;
        z-index: 999;
        width: 100%;
        padding: 0px 20px 15px 20px;
        box-sizing: border-box;
        text-align: center;
        border-top: 0px;
    }

    .minder-editor-container {
        margin-top: 3%;
    }

    /*步骤条*/
    .flow_steps {
        float: none;
        /*margin-bottom: 20px;*/
        border: 1px solid #E8E8E8;
        padding: 3px;
    }

    .flow_steps .stage {
        margin-bottom: 0;
        display: flex;
        display: -webkit-flex;
        justify-content: space-between;
        -webkit-justify-content: space-between;
    }

    .flow_steps .stage li:not(:last-child) {
        flex: 1;
        height: 36px;
        font-size: 14px;
        color: #949393;
        background: #eaecf1;
        display: inline-block;
        position: relative;
        text-align: center;
        cursor: pointer;
        margin-left: 3px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .flow_steps .stage .flow_steps_btn {
        flex: 0.2;
        height: 36px;
        font-size: 14px;
        display: inline-block;
        position: relative;
        text-align: center;
        cursor: pointer;
        margin-left: 3px;
        display: flex;
        align-items: center;
        justify-content: left;
        color: #fff;
    }

    .flow_steps .stage .flow_steps_btn .Allscreen {
        width: 36px;
        height: 100%;
        background: #FFB822;
    }

    .flow_steps .stage .flow_steps_btn .close {
        width: 36px;
        height: 100%;
        background: #F4516C;
        margin-left: 6px;
        color: #fff;
        opacity: 1;
    }

    .flow_steps .stage .flow_steps_btn .close i {
        margin-top: -5px;
    }

    .flow_steps .stage .flow_steps_btn .line {
        width: 1px;
        height: 42px;
        background: #e8e8e8;
        margin-right: 3px;
    }

    .flow_steps .stage li.current {
        background: #5cb8ff !important;
        color: #fff !important;
        font-weight: 700 !important;
    }

    .flow_steps .stage li.current:after {
        content: '';
        display: block;
        border-top: 18px solid transparent;
        border-bottom: 18px solid transparent;
        border-left: 18px solid #5cb8ff !important;
        position: absolute;
        right: -18px;
        top: 0;
        z-index: 10;
    }

    .flow_steps .stage li i.flow-steps-num {
        width: 24px;
        height: 24px;
        line-height: 24px;
        text-align: center;
        background-color: transparent;
        border: 1px solid #bebebe;
        border-radius: 50%;
        display: inline-block;
        margin-right: 7px;
        vertical-align: middle;
        color: #bebebe;
        font-size: 18px;
        font-style: normal;
    }

    .flow_steps .stage li.current i.flow-steps-num {
        background-color: #fff !important;
        border: 1px solid #fff !important;
        color: #5cb8ff !important;
        font-size: 18px !important;
        font-weight: 700 !important;
    }

    .flow_steps .stage li:after {
        content: '';
        display: block;
        border-top: 18px solid transparent;
        border-bottom: 18px solid transparent;
        border-left: 18px solid #eaecf1;
        position: absolute;
        right: -18px;
        top: 0;
        z-index: 10;
    }

    .flow_steps .stage li:before {
        content: '';
        display: block;
        border-top: 18px solid transparent;
        border-bottom: 18px solid transparent;
        border-left: 18px solid #fff;
        position: absolute;
        left: 0px;
        top: 0;
    }

    .flow_steps .stage li.overlay {
        background: #CADFF5;
        color: #2B76AF;
    }

    .flow_steps .stage li.overlay i.flow-steps-num {
        color: #2B76AF;
        opacity: 0.5;
    }

    .flow_steps .stage li:nth-child(1).overlay:before {
        border-left: 18px solid #CADFF5;
    }

    .flow_steps .stage li.overlay:after {
        border-left: 18px solid #CADFF5;
    }

    .flow_steps .stage li.overlay i.flow-steps-num {
        background-color: #fff;
        border: 1px solid rgba(255, 255, 255, 1);
        opacity: 0.5;
    }

    .flow_steps .stage li:last-child {
        margin-right: 3px;
        background: #fff;
    }

    .flow_steps .stage li:first-child:before {
        display: none;
    }

    .flow_steps .stage li.stage-success {
        background: #cadff5;
        color: #73a0cc;
        font-weight: 700;
    }

    .flow_steps .stage li.stage-success i.flow-steps-num {
        background-color: #ddeaf9;
        border: 1px solid #ddeaf9;
        color: #73a0cc;
        font-size: 18px;
        font-weight: 700;
    }

    .flow_steps .stage li.stage-success:after {
        border-left: 18px solid #cadff5;
    }

    .flow_steps .stage li:nth-last-child(2):after {
        display: none;
    }

    .flow_steps .stage li:last-child:before {
        display: none;
    }

    .flow_steps .stage li:last-child:after {
        display: none;
    }

    .flow_steps .stage .item {
        background-color: #36a3f7;
        border-radius: 4px !important;
        line-height: 18px;
        padding: 6px 2px 6px 12px;
    }

    .flow_steps .down {
        position: relative;
        margin-left: 5px;
    }

    .flow_steps .down .down_title {

        display: block;
        background: rgba(255, 192, 0, 1);
        border-radius: 11px;
        padding: 1px 28px 1px 17px;
        font-size: 14px;
        position: relative;
    }

    .flow_steps .down:hover .down_menu {
        display: block;
    }

    .flow_steps .down .sanjiao_down {

        width: 0;
        height: 0;
        overflow: hidden;
        font-size: 0; /*是因为, 虽然宽高度为0, 但在IE6下会具有默认的 */
        line-height: 0; /* 字体大小和行高, 导致盒子呈现被撑开的长矩形 */
        border-width: 5px;
        border-style: solid; /*ie6下会出现不透明的兼容问题*/
        border-color: #fff transparent transparent transparent;
        position: absolute;
        top: 9px;
        right: 12px;
    }

    #down_val {
        color: #fff;
    }

    .flow_steps .down .down_menu {
        width: 100%;
        position: absolute;
        top: 24px;
        left: 0px;
        background: rgba(255, 192, 0, 1);
        z-index: 9999;
        border-radius: 4px;
        display: none;
    }

    .flow_steps .down .down_menu p {

        border-bottom: 1px solid #fff;
        margin-bottom: 0px;
        padding-top: 5px;
        padding-bottom: 5px;
        color: #fff;
    }
</style>
<div class="container-tab">
    <div class="flow_steps">
        <ul class="stage">
            <li data-url="/aea/par/stage/indexSetStageItem.do,/aea/item/frontCheckManage.do<%--/aea/item/frontCheckItem.do--%>" class="overlay">
                <i class="flow-steps-num">1</i>${currentBusiType == 'item'?'前置检测':'事项范围配置'}
            </li>
            <li data-url="/rest/mind/mindIndex.do, /rest/mind/noSituation.do" data-item="">
                <i class="flow-steps-num">2</i>${handWay=='1'?'情形材料配置':'情形事项配置'}
                <div class="down">
                    <div class="down_title">
                        <span class="down_val">分情形</span>
                        <i class="sanjiao_down"></i>
                    </div>
                    <div class="down_menu">
                        <p id="situationMenu">分情形</p>
                        <p id="noSituationMenu">不分情形</p>
                    </div>
                </div>
            </li>

            <li data-url="/rest/mind/stage/processModeler.do, /rest/mind/item/processModeler.do">
                <i class="flow-steps-num">3</i>流程配置
            </li>

            <c:if test="${currentBusiType == 'stage'}">
                <li data-url="/aea/par/stage/indexStageGuide.do">
                    <i class="flow-steps-num">4</i>办事指南
                </li>
            </c:if>

            <c:if test="${currentBusiType == 'stage'}">
                <li data-url="/aea/par/stage/frontCheckManage.do">
                    <i class="flow-steps-num">5</i>前置检测
                </li>
            </c:if>

            <c:if test="${currentBusiType == 'stage' && useOneForm=='1' }">
                <li data-url="/aea/par/stage/oneFormManage.do">
                    <i class="flow-steps-num">5</i>一张表单
                </li>
            </c:if>

            <c:if test="${currentBusiType == 'item'}">
                <li data-url="/aea/item/indexSetItemOut.do">
                    <i class="flow-steps-num">4</i>输出材料
                </li>
            </c:if>

            <c:if test="${currentBusiType == 'item'}">
                <li data-url="/aea/item/editItemDirectory.do">
                    <i class="flow-steps-num">5</i>办事指南
                </li>
            </c:if>

            <%--<c:if test="${currentBusiType == 'item' && useOneForm=='1' }">--%>
                <%--<li data-url="/aea/item/oneFormManage.do">--%>
                    <%--<i class="flow-steps-num">6</i>一张表单--%>
                <%--</li>--%>
            <%--</c:if>--%>

            <li class="flow_steps_btn">
                <div class="line"></div>
                <button class="Allscreen btn btn-sm">
                    <i class="la la-expand" style="color: #fff;"></i>
                </button>
                <button class="close btn btn-sm" id="btnClosePage">
                    <i class="la la-close"></i>
                </button>
            </li>
        </ul>
    </div>
</div>
<script type="text/javascript">

    //导航-情形配置-描述-分情形
    var MIND_HEADER_NAV_SITUATION_SET_DESC_CLASSIFY_SITUATION='分情形';
    //导航-情形配置-描述-不分情形
    var MIND_HEADER_NAV_SITUATION_SET_DESC_NO_CLASSIFY_SITUATION='不分情形';
    //控制全屏
    function enterfullscreen() {//进入全屏
        var docElm = document.documentElement;
        //W3C
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
        }
        //FireFox
        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
        }
        //Chrome等
        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
        }
        //IE11
        else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    }

    function exitfullscreen() { //退出全屏

        if (document.exitFullscreen) {
            document.exitFullscreen();
        }
        else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        }
        else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        }
        else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }

    var a=0;
    $('.Allscreen').on('click',function () {

        var AllSCREENICON = $('.Allscreen i');
        if(AllSCREENICON.hasClass('la-expand')){
            AllSCREENICON.addClass('la-compress').removeClass('la-expand');
        }else{
            AllSCREENICON.addClass('la-expand').removeClass('la-compress');
        }
        a++;
        a%2==1?enterfullscreen():exitfullscreen();
    })

    var isNeedState = '${isNeedState}';
    $(function () {

        handleActiveNavigation();//导航栏选中状态设置
        handleIsNeedStateView();//是否分情形下拉显示
        handleUrlDispatch();//各导航栏url设置
        handleClickEvent();

        $('#btnClosePage').on('click',function () {
            swal({
                title: '提示信息'
                ,text: "确认关闭窗口?"
                ,type: 'warning'
                ,showCancelButton: true
                ,confirmButtonText: '确认!'
                ,cancelButtonText: '取消!'
            }).then(function(result) {
                if (result.value) {
                    CloseWebPage();
                }
            });
        })
    });
    var urlArr = [];
    function handleUrlDispatch() {

        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {
            $($(".flow_steps .stage li")[0]).html('<i class="flow-steps-num">1</i>事项范围配置');
            if(handWay){
                // 不分情形下的沿用事项情形
                if(handWay=='0'&&isNeedState=='0'){
                    urlArr = [
                        '/aea/par/stage/indexSetStageItem.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/showStageItems.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/rest/mind/stage/processModeler.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/indexStageGuide.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/frontCheckManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/oneFormManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                    ];
                // 其他情况
                }else{
                    urlArr = [
                        '/aea/par/stage/indexSetStageItem.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/rest/mind/mindIndex.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/rest/mind/stage/processModeler.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/indexStageGuide.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/frontCheckManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                        '/aea/par/stage/oneFormManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId,
                    ];
                }
                $($(".flow_steps .stage li")[2]).html('<i class="flow-steps-num">3</i>流程配置');
                $($(".flow_steps .stage li")[3]).html('<i class="flow-steps-num">4</i>办事指南');
                $($(".flow_steps .stage li")[4]).html('<i class="flow-steps-num">5</i>前置检测');
                if(useOneForm=='1'){
                    $($(".flow_steps .stage li")[5]).html('<i class="flow-steps-num">6</i>一张表单');
                    // $($(".flow_steps .stage li")[4]).html('<i class="flow-steps-num">5</i>一张表单');
                }
            }
        } else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {
            urlArr = [
                // '/aea/item/frontCheckItem.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                '/aea/item/frontCheckManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                '/rest/mind/mindIndex.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                '/rest/mind/item/processModeler.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                '/aea/item/indexSetItemOut.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                '/aea/item/editItemDirectory.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
                // '/aea/item/oneFormManage.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + currentStateVerId,
            ];
            $($(".flow_steps .stage li")[0]).html('<i class="flow-steps-num">1</i>前置检测');
            $($(".flow_steps .stage li")[2]).html('<i class="flow-steps-num">3</i>流程配置');
            $($(".flow_steps .stage li")[3]).html('<i class="flow-steps-num">4</i>输出材料');
            $($(".flow_steps .stage li")[4]).html('<i class="flow-steps-num">5</i>办事指南');
            // if(useOneForm=='1') {
            //     $($(".flow_steps .stage li")[5]).html('<i class="flow-steps-num">6</i>一张表单');
            // }
        }
    }

    function handleClickEvent() {

        $(".flow_steps .stage li").click(function () {
            var index = $(this).index('.flow_steps .stage li');
            var url = urlArr[index];
            if (url != undefined && url != '') {
                location.href = ctx + url;
            }
        });

        $(".flow_steps .stage li .down_menu p").click(function (e) {

            if(curIsEditable){
                var situationText = $(this).text();
                swal({
                    title: '提示信息'
                    ,text: '确定切换为'+situationText+'?'
                    ,type: 'info'
                    ,showCancelButton: true
                    ,confirmButtonText: '确认'
                    ,cancelButtonText: '取消'
                }).then(function(result) {
                    if (result.value) {

                        if (situationText == MIND_HEADER_NAV_SITUATION_SET_DESC_CLASSIFY_SITUATION) {

                            location.href = ctx + '/rest/mind/mindIndex.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId+'&isSetState=1&paramIsNeedState=1' + '&stateVerId=' + currentStateVerId;

                        } else if (situationText ==MIND_HEADER_NAV_SITUATION_SET_DESC_NO_CLASSIFY_SITUATION) {

                            if(handWay=='0'&&isNeedState=='0'){

                                location.href = ctx + '/aea/par/stage/showStageItems.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId;
                            }else{
                                location.href = ctx + '/rest/mind/mindIndex.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId+'&isSetState=1&paramIsNeedState=0'+ '&stateVerId=' + currentStateVerId;
                            }
                        }
                    }
                });
            }else{
                swal({
                    type: 'info',
                    title: '当前版本下数据不可编辑！',
                    showConfirmButton: false,
                    timer: 1500
                });
            }
            e.stopPropagation();
        });
    }

    function handleActiveNavigation() {

        $(".flow_steps .stage li").not('.down').each(function(e) {
            var matchUrl = $(this).attr('data-url');
            if (matchUrl != undefined) {
                for (var i = 0; i < matchUrl.split(',').length; i++) {
                    if (location.href.indexOf(matchUrl.split(',')[i].trim()) > 0) {
                        $(this).addClass('current').siblings().removeClass('current');
                        $(this).prev().addClass('overlay').next().removeClass('overlay')
                        break;
                    }
                }
            }
        });
    }

    function handleIsNeedStateView() {

        if (isNeedState == '1') {

            $('.down_val').text(MIND_HEADER_NAV_SITUATION_SET_DESC_CLASSIFY_SITUATION);
            $('#situationMenu').addClass('hide');
            $('#noSituationMenu').addClass('show');

        } else {

            $('.down_val').text(MIND_HEADER_NAV_SITUATION_SET_DESC_NO_CLASSIFY_SITUATION);
            $('#situationMenu').addClass('show');
            $('#noSituationMenu').addClass('hide');
        }
    }
    function CloseWebPage(){

        if (navigator.userAgent.indexOf("MSIE") > 0) {
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                window.opener = null;
                window.close();
            } else {
                window.open('', '_top');
                window.top.close();
            }
        }
        else if (navigator.userAgent.indexOf("Firefox") > 0) {
            window.location.href = 'about:blank ';
        } else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    }
</script>

