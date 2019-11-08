<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
        <title>全景流程图</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/rappid/build/rappid.min.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/css/bpmn.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/global/element-2.9.2/index.css" />

        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-themes/ocean-blue/css/agcloud_metronic.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/material-dashboard-pro-html-v1/assets/css/bootstrap.min.css"/>
        <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agtree3/css/zTreeStyle/zTreeStyle.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/sortable1/Sortable.css" type="text/css" rel="stylesheet"/>
        <!-- 引入AgCloud Framework框架公共CSS -->
        <%--<link href = "${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/scheme2/admin/css/layout.css" type="text/css" rel="stylesheet"/>--%>
        <link href = "${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-themes/default/theme.css" type="text/css" rel="stylesheet"/>

        <link rel="stylesheet" href="https://cdn.bootcss.com/jqueryui/1.12.1/jquery-ui.min.css" />


        <style type="text/css">
            #stencil-container{
                bottom:unset;
                transform: rotate(90deg);
                transform-origin: left;
                margin-left: 0px;
                margin-top: 8px;
                width:22.8%;
            }
            #stencil-container div div.content div{
                padding: 10px 0;
            }
            .joint-element .fobj div {
                    display: -webkit-box;
                    display: -ms-flexbox;
                    /*display: flex !important;*/
                    -webkit-box-pack: center;
                    justify-content: center;
                    -webkit-box-align: center;
                    align-items: center !important;
                    /*-webkit-transform: translateZ(0);*/
                    /*transform: translateZ(0);*/
                    padding: 0 0px 0 0px;
            }
            #v-6{
                top: -3px;
            }
            #paper-container {
                bottom:15px;
                right:0px;
                top:44px;
            }
           #westPanel{
               margin-left: 13%;
               display:none;
           }
            label.box{
                max-width: none !important;
            }

            input[type="range"]{
                -webkit-appearance: none;
                background-color:#DCE3F9;
                height: 5px;
            }
             input[type=range]:focus {
                outline: none;
            }
              input[type="range"]::-webkit-slider-thumb{
                -webkit-appearance: none;
                border-radius: 5px;
                background:  #4F61E1;
                width: 24px;
                height: 11px;
            }
            .range_tip{
                position: absolute;
                padding: 3px 6px;
                border-radius: 4px;
                background: #7c8aea;
                width: 30px;
                height: 25px;
                top: 2px;
            }
            #stencil-container div.content div svg, .joint-toolbar.joint-theme-bpmn{
                background-color: #ffffff;
            }
            #paper-container div div div.joint-paper svg g.joint-viewport g.joint-cell g.rotatable{
                transform: unset !important;
            }
            #v-12 g.joint-cell g.rotatable{
                transform: rotate(-90deg);
            }
            /*拖拽出来时元素翻转样式的控制*/
            div.stencil-paper-drag{
                transform: matrix(0, 1, -1, 0, -36, 0);
            }
            .rightExeItem{
                position: absolute;
                top: 0;
                right: 0;
                width: 50%;
                height: 100%;
                overflow: auto;
                border-left: 1px solid rgba(0,0,0,.3);
                padding-left: 15px;
            }
            .itemColorRark{
                    width: 160px;
                    height: 310px;
                    position: absolute;
                    background-color: white;
                    top: 46px;
                    right: 8px;
                    border-radius: 5px;
                    border: 3px solid rgb(245, 245, 245);
                    display:none;
            }
            .itemColorRark label{
                font-weight:500;
            }
            .itemColorRark>div>div {
                    width: 100%;
                    height: 30px;
                    line-height: 30px;
                    float: left;
            }
            .itemColorRark>div>div>span {
                    width: 18px;
                    height: 18px;
                    float: left;
                    margin: 5px 0;
                    /*border-radius: 5px;*/
            }
            .itemColorRark>div>div>label {
                    margin: 0px;
                    width: 110px;
                    height: 30px;
                    padding-left: 5px;
            }
            .itemColorRark>.itemArrow{
                background-color: #fff;
                width: 20px;
                height: 20px;
                transform: rotate(45deg);
                margin-top: -10px;
                right: 18px;
                position: absolute;
            }
            .joint-toolbar.joint-theme-bpmn button[data-name="load"]:before {
                    content: '图片';
            }
            .itemStatus{
                    width:47px;
                    font-size:14px;
                    border-radius: 4px;
            }
            .itemStatusPrev{
                    display: inline-block;
                    width: calc(100% - 60px);
            }
            .itemStatusPrev div{
                    width: 100%;
                    display: block;
            }
            .itemStatusPrev span{
                    width: 100%;
                    display: block;
            }
            .joint-element .fobj body>div>div{
                    display: block;span;
                    width: calc(100% - 10px);
            }
            /*.joint-element .fobj body>div>div>span{
                    background-color:#D9E2EC;
                    border-radius:3px;
                    color: #575962;

            }*/
            .joint-element .fobj body>div>span{
                    width: 47px;
                    font-size: 14px;
                    border-radius: 4px;
                    padding: 3px;
                    /*background-color: #169AFF;*/
                    padding: 2 5 2 5;
                    color: #fff;
            }
            g>text.label>tspan {
                font-size: 18px;
                font-weight: 600;
                color:#575962;
            }
            .stage_png>div>img{
                width:18px;
                height:18px;
            }
            #photoTip{
                bottom: 40px;
                right: 15px;
                position:absolute;
            }
            g body div.content div {
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
                overflow: hidden;
            }
            @keyframes fadeInRight {
                0% {
                    width:60px;
                }
                100%{
                    width:200px
                }
            }
            g.rotatable>.fobj>body>div.content>div>span>span {
                font-weight: 600;
            }
           /* .joint-element .scalable .rectEsp {
                vector-effect: non-scaling-stroke !important;
            }
            .joint-element .scalable * {
                vector-effect: unset !important;
            }*/
            g>text.label>tspan {
                font-size: 18px;
                font-weight: bold;
                fill: rgb(255, 255, 255);
            }
            g>text.spool-t-l>tspan {
                font-size: 16px;
                font-weight: bold;
                fill: rgb(255, 255, 255);
            }
            g.joint-cell>g.rotatable>text {ite
                fill: rgb(255, 255, 255);
            }
            #paper-container g.rotatable .fobj.actFobj body>div.content {
                border-left: 5px solid  #B8C8DC;
                border-right: 5px solid  #B8C8DC;
                border-top: 1px solid  #B8C8DC;
                border-bottom: 1px solid  #B8C8DC;
            }
            .toolBtn:hover, .toolMain:hover, .toolSave:hover, .toolDelete:hover{
                background-color:rgba(0,0,0,.2) !important;
            }
            .toolPanel {
                /*display:none;*/
                overflow: hidden;
                width: 66px;
                height: 60px;
                background-color: #fff;
                position: absolute;
                top: 52px;
                left: 8px;
                border-radius: 30px;
                transition: width 0.5s ease;
            }
            .proHead{
                height:44px;
                background-color:#169AFF;
                position:relative;
                display: flex;
                align-items: center;
                color:#fff;
            }
            .proHeadContent{
                height: 100%;
                align-items: center;
                float:left;
                font-size:16px;
                margin-right: 12px;
                margin-bottom: 0px;
                width: 1000px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                display: block;
                margin-top: 20px;
            }
            .proHeadPicTip{
                position: absolute;
                right: 11px;
                font-size: 14px;
                width: 56px;
                height: 24px;
                background-color: #fff;
                color: #169AFF;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 4px;
            }
            g{
                cursor:pointer;
            }
            .tree-icon-name i {
                color: #61BBFF;
                padding-right: 6px;
                font-size: 16px;
            }
            #elTreeApp div.el-tree{
                padding: 15px 5px 15px 5px;
            }
            .table-cell-hover {
                width: 368px;
                height: 255px;
                background: rgba(0,0,0,0.64);
                -webkit-box-shadow: 0px 3px 8px 0px rgba(53,76,129,0.25);
                box-shadow: 0px 3px 8px 0px rgba(53,76,129,0.25);
                border-radius: 4px;
                position: absolute;
                left: 50%;
                top: -268px;
                z-index: 9999;
                margin-left: -184px;
                position: sticky;
                font-size: 14px;
                font-weight: 400;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
                color: #ffffff !important;
                display:none;
            }
            .table-cell-hover p {
                margin-left: 30px;
            }

        </style>

<div class="proHead">
    <img src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/arrowDown.png" style="cursor:pointer;margin-left: 16px;" id="proHead-icon">
    <div  style="position: relative;width: 10px; height: 40px;">
        <div id="elTreeApp" style="top: 43px; position: absolute; width:600px;left:-33px;z-index:9999;border: 3px solid rgba(245,245,245); display:none;">
            <div style="background-color: #fff; width: 20px; height: 20px; transform: rotate(45deg); left: 10px; margin-top: -10px; position: absolute;"></div>
            <el-tree
                    :data="data" :props="defaultProps" default-expand-all show-checkbox highlight-current  @check-change="handleNodeClick"
                    :expand-on-click-node="false" :check-on-click-node="true" :check-strictly="true" node-key="id" :default-checked-keys="['${projInfoId}']">
         <span class="custom-tree-node" slot-scope="scope">
              <span class="tree-icon-name" style="width: 520px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: block;" v-bind:title="scope.data.name">
                <i :class="scope.data.icon?scope.data.icon:scope.data.children?'el-icon-folder-opened':'el-icon-tickets'"></i>{{ scope.data.name}}
              </span>
            </span>
            </el-tree>
        </div>
    </div>
    <label class="proHeadContent"></label>


    <label class="proHeadPicTip" style="float:right;cursor:pointer;">图例</label>
</div>
<img id="toolImg" title="工具栏" style="top:5px;height:5px;" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/fold.png" >
<div class="toolPanel" style="border: 3px solid rgba(245,245,245);height: 66px;z-index:999">
    <div class="toolMain" style="cursor: pointer;float: left;width: 60px;height: 60px;border-radius: 30px;background: #1C9AFD url(/aplanmis-front/rappid/apps/BPMNEditor/images/set_white.png) no-repeat center center;"></div>
    <div class="zoomIn" style="cursor: pointer;width: 50px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/zoomIn.png) no-repeat center 12px;">
        <label style="width: 50px;text-align: center;margin-top: 39px;color: #575962;">放大</label>
    </div>
    <div class="zoomOut" style="cursor: pointer;width: 50px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/zoomOut.png) no-repeat center 12px;">
        <label style="width: 50px;text-align: center;margin-top: 39px;color: #575962;">缩小</label>
    </div>
    <div class="toolFold" style="cursor: pointer;border-left: 1px solid rgba(0,0,0,.1);width: 30px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/tfold.png) no-repeat center center;">
    </div>
    <div class="toolfold"></div>
</div>
        <div id="paper-container"></div>

        <div class="itemColorRark">
                <div  class="itemArrow"></div>
                <div style="padding-left:11px;" class="stage_png">
                    <label style="color:#88898D;margin-top: 5px;width:100%;">阶段</label>
                    <div>
                        <span style="background-color: #00C161" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/finesh.png"></span>&nbsp;已办结
                    </div>
                    <div>
                        <span style="background-color: #169AFF" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/working.png"></span>&nbsp;进行中
                    </div>
                    <div>
                        <span style="background-color: #CED7DC"  src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/unfinesh.png"></span>&nbsp;未申报
                    </div>
                </div>
                <div style="padding-left:11px">
                    <label style="color:#88898D;width: 100%;">事项</label>
                        <div><span  class="itemColor" style="border: 2px solid #00C161"></span><label>办结通过</label></div>
                        <div><span  class="itemColor" style="border: 2px solid #FF4B47"></span><label>不通过/不予受理</label></div>
                        <%--<div><span  class="itemColor" style="border: 1px solid #63DFC9"></span><label>容缺通过</label></div>--%>
                        <div><span  class="itemColor" style="border: 2px solid #CED7DC;background-color: #F3F4F8;"></span><label>无需办理</label></div>
                        <div><span  class="itemColor" style="border: 2px solid #169AFF"></span><label>办理中</label></div>
                        <%--<div><span  class="itemColor" style="border: 1px solid #4eb2fd"></span><label>容缺受理</label></div>--%>
                        <div><span  class="itemColor" style="border: 2px solid #CED7DC"></span><label>待&nbsp;&nbsp;办</label></div>
                </div>
        </div>
        <%--<div id="selectItemTree" class="ztree" style="overflow: auto;width:17%;background-color:#fff;border: 3px solid rgb(245, 245, 245);z-index:999"></div>--%>
<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog" style="BACKGROUND: RGBA(0,0,0,.4);"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">正在加载，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<div class="table-cell-hover" id="itemTableInfo"><p style="    margin-top: 20px;padding-right:7px;"><i class="circle circleBg1" style="margin-left:5px;background: #4EB1FD; width: 10px; height: 10px; border-radius: 50%; display: inline-block; position: absolute; left: 7px; top: 25px;"></i>
    <span style="display: inline-block;"></span></p>
    <p>承办单位：<span>市自然资源局</span></p>
    <p>承诺时限：<span>10</span>&nbsp;个工作日</p>
    <p>实际时限：<span>10</span>&nbsp;个工作日</p>
    <p>事项开始时间：<span>2019-09-27</span></p>
    <p>事项结束时间：<span>-</span></p>
    <p>审批状态：
        <a href="javascript:;" class="state-btn color1" style="color: #337ab7; text-decoration: none; background: rgba(239,247,255,1); padding: 5px 15px; border-radius: 15px;"><i class="icon-bg icon-bg1"></i> <span>-</span></a></p>
</div>

        <%--<img id="photoTip" title="关闭" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/fold.png" >--%>
        <script type="text/javascript">
            var themeVerId = "${themeVerId}", currentBusiId =  themeVerId, curIsEditable = '${curIsEditable}', dueUnit='${dueUnit}', drawGrid=false, projName='${projName}',projInfoId ='${projInfoId}',
            themeVerDiagram = '${diagramStatusVo.diagramJson}', projTree = '${projTree}',ctx = '${pageContext.request.contextPath}',statusList = '${statusList}',errorMsg = '${errorMsg}';
            console.log(projTree);
        </script>

        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.min.js"></script>--%>
        <script type="text/javascript" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/gd.js"></script>
        <!--<script type="text/javascript" src="https://apis.google.com/js/client.js"></script>-->

        <!-- Dependencies: -->
        <script src="${pageContext.request.contextPath}/rappid/node_modules/jquery/dist/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/node_modules/lodash/index.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/node_modules/backbone/backbone.js"></script>
        <%--<script src="${pageContext.request.contextPath}/rappid/node_modules/graphlib/dist/graphlib.core.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/node_modules/dagre/dist/dagre.core.js"></script>--%>

        <script src="${pageContext.request.contextPath}/rappid/build/rappid.min.js"></script>

        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/inspector.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/toolbar_view.js"></script>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/example.js"></script>--%>
        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/bpmn.js"></script>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/views/main.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/config/stencil.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/config/halo.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/config/selection.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/models/joint.shapes.app.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/apps/KitchenSink/js/views/navigator.js"></script>--%>
        <%--<script src="${pageContext.request.contextPath}/rappid/dist/client.js"></script>--%>
        <script src="https://cdn.bootcss.com/jqueryui/1.12.1/jquery-ui.min.js"></script>
<%--<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/material-dashboard-pro-html-v1/assets/js/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>--%>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agcloud-common/agcloud.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/vendors/base/vendors.bundle.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/metronic-v5/default/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery-ui1/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/sortable1/Sortable.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agtree3/js/jquery.agtree.core.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.excheck.js" type="text/javascript"></script> <!--扩展实现复选框-->
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.exedit.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.exhide.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
        <%--<script src="${pageContext.request.contextPath}/ui-static/stage/rappid_view_set_stage_item.js"></script>--%>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/vue/vue.min.js"></script>
        <%--<script src="https://unpkg.com/vue/dist/vue.js"></script>--%>
        <script src="${pageContext.request.contextPath}/global/element-2.9.2/index.js"></script>

        <script src="${pageContext.request.contextPath}/ui-static/stage/rappid_operate_view.js"></script>
        <script type="text/javascript">

            var alwaysHide = true;
            var fold = '${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/fold.png';
            var unfold = '${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/unfold.png'
          /*  $('#photoTip, .itemColorRark').on('mousemove', function(){
                if(alwaysHide){
                   var $item = $('.itemColorRark');
                   $item.show();
                   $('#photoTip').attr('src', unfold);
                }
            });
            $('#photoTip, .itemColorRark').on('mouseleave', function(){
                if(alwaysHide){
                    var $item = $('.itemColorRark');
                    $item.hide();
                    $('#photoTip').attr('src', fold);
                }
            });*/
            $('.proHeadPicTip').on('click', function(){
                var $picTip = $('.itemColorRark');
                if($picTip.is(':hidden')){
                    $picTip.show();
                }else{
                    $picTip.hide();
                }
            })
            $('#toolImg').on('click', function(){
                $('#toolbar-container').trigger();
            })
            var aUp = '${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/arrowUp.png';
            var aDown = '${pageContext.request.contextPath}/rappid/apps/BPMNEditor/images/arrowDown.png'
            $('#proHead-icon').on('click',  function(){
                var $picTip = $('#elTreeApp');
                if($picTip.is(':hidden')){
                    $('#proHead-icon').attr('src', aUp);
                    $picTip.show();
                }else{
                    $('#proHead-icon').attr('src', aDown);
                    $picTip.hide();
                }
            })
        </script>

