<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
        <title>全景流程图设计</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/rappid/build/rappid.min.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/css/bpmn.css" />

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
                margin-left: 10px;
                margin-top: -53px;
                width:27.0%;
            }
            #stencil-container div.elements.joint-paper.joint-theme-bpmn{
                box-shadow: 0 0 8px rgb(53, 39, 39);
            }
            .joint-element .fobj div {
                display: -webkit-box;
                display: -ms-flexbox;
                /*display: flex !important;*/
                -webkit-box-pack: center;
                justify-content: center;
                -webkit-box-align: center;
                align-items: center !important;
                padding: 0 0px 0 0px;
            }
            #v-6{
                top: -3px;
            }
            #stencil-container .joint-paper-background{
                background-color: #ffffff;
            }
            #stencil-container div.content div svg{
                top:5px;
                left: -15px;
            }
            .joint-stencil>.content {
                height: 45%;
                box-shadow: 0 0 8px rgb(53, 39, 39);
            }
            #paper-container {
                bottom:-6px;
                right:0px;
            }
           #westPanel{
               margin-left: 25%;
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
            .joint-tooltip{
                display:none !important;
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
                width: 405px;
                position: absolute;
                top: 0;
                right: 0;
            }
            .itemColorRark>div {
                width: 100px;
                height: 30px;
                line-height: 30px;
                float: left;
            }
            .itemColorRark>div>span {
                width: 18px;
                height: 18px;
                float: left;
                margin: 5px 0;
                border-radius: 5px;
            }
            .itemColorRark>div>label {
                margin: 0px;
                width: 65px;
                height: 30px;
                padding-left: 5px;
            }
            .itemStatus{
                width:47px;
                font-size:14px;
                border-radius: 4px;
            }
            .itemStatusPrev{
                display:block;width: calc(100% - 60px);
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
                color: #575962;
            }

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
            g.joint-cell>g.rotatable>text {
                fill: rgb(255, 255, 255);
            }
            g body div.content div {
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
                overflow: hidden;
            }

            .joint-stencil.joint-theme-bpmn .elements text, .stencil-paper-drag .joint-element text {
                 display: block;
            }
            .joint-stencil.joint-theme-bpmn .elements text tspan, .stencil-paper-drag .joint-element text tspan, .joint-stencil.joint-theme-bpmn .elements body .content {
                font-weight:500 !important;
                font-size:11px !important;
            }
            .toolBtn{
                height: 60px;
                width: 60px;
                left: 80px;
                top: 10px;
                position: absolute;
                border-radius: 50%;
                background: #fff url(/aplanmis-front/rappid/apps/BPMNEditor/images/set_blue.png) no-repeat center center;
                border: 3px solid rgba(245,245,245);
                cursor:pointer;
            }
            .toolBtn:hover, .toolMain:hover, .toolSave:hover, .toolDelete:hover{
                background-color:rgba(0,0,0,.2) !important;
            }
            .toolPanel{
                /*display:none;*/
                overflow: hidden;
                width: 60px;
                height: 60px;
                background-color: #fff;
                position: absolute;
                top: 20px;
                left: 100px;
                border-radius: 30px;
                /*animation: fadeInRight 3s linear normal;*/
                box-shadow: 0 0 8px rgb(53, 39, 39);
                transition:width 0.5s ease;
            }
            @keyframes fadeInRight {
                0% {
                    width:60px;
                }
                100%{
                    width:200px
                }
            }
            g.rotatable>.fobj>body>div.content>div>span>span{
                font-weight:600;
            }
            .joint-stencil.joint-theme-bpmn {
                 border-top: 3px solid rgb(245, 245, 245);
            }
            #stencil-container g.rotatable>text.label>tspan.v-line {
                fill: #000;
                font-size: 11.5px;
                font-weight: normal;
            }
            #stencil-container g.rotatable .fobj body div.content {
                font-size: 9px;
                font-weight: normal;
                padding-left: 8px !important;
            }
            #paper-container g.rotatable .fobj.actFobj body>div.content {
                border-left: 5px solid  #B8C8DC;
                border-right: 5px solid  #B8C8DC;
                border-top: 1px solid  #B8C8DC;
                border-bottom: 1px solid  #B8C8DC;
            }
            .modal-header .modal-title{
                float: left;
                position: absolute;
            }
        </style>

        <div id="paper-container"></div>
        <div id="toolbar-container" style="display:none">
        </div>
        <div id="stencil-container">
        </div>
        <%--<div class="toolBtn">--%>
        <%--</div>--%>
    <div class="toolPanel" style="">
        <div class="toolMain" style="cursor: pointer;float: left;width: 60px;height: 60px;border-radius: 30px;background: #1C9AFD url(/aplanmis-front/rappid/apps/BPMNEditor/images/set_white.png) no-repeat center center;"></div>
        <div class="toolSave" style="cursor: pointer;width: 50px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/save.png) no-repeat center 12px;">
            <label style="width: 50px;text-align: center;margin-top: 39px;">保存</label>
        </div>
        <div class="toolDelete" style="cursor: pointer;width: 50px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/delete.png) no-repeat center 12px;">
            <label style="width: 50px;text-align: center;margin-top: 39px;">删除</label>
        </div>
        <div class="toolFold" style="cursor: pointer;border-left: 1px solid rgba(0,0,0,.1);width: 30px;height: 60px;float: left;background: url(/aplanmis-front/rappid/apps/BPMNEditor/images/tfold.png) no-repeat center center;">
        </div>
        <div class="toolfold"></div>
    </div>
      <%--  <div id="inspector-container">
          <div class="about">
            <div class="logo"><img src="./images/logo-big.png" alt="JointJS"/></div>
            <h1>Business Process Model and Notation</h1>
            <h2>What is it good for?</h2>
            <div class="about-bpmn">The main goal of the BPMN language is to erase the communication gap between the business process design and process implementation by providing a graphical notation that all the technical and business users can easily understand.</div>
            <h2>BPMN 2.0</h2>
            <div class="about-bpmn">From the version 2.0+ the language contains also the execution semantics for all BPMN elements and defines a mechanism for extending standard elements with custom attributes.</div>
          </div>
        </div>--%>
        <%--绑定事项--%>
<%--<div class="itemColorRark">
    <div><span  class="itemColor" style="background-color:#3DBB93"></span><label>办理完成</label></div>
    <div><span  class="itemColor" style="background-color:#FF6464"></span><label>办理失败</label></div>
    <div><span  class="itemColor" style="background-color:#63DFC9"></span><label>容缺通过</label></div>
    <div><span  class="itemColor" style="background-color:#EDF2F5"></span><label>无需办理</label></div>
    <div><span  class="itemColor" style="background-color:#457eff"></span><label>正在受理</label></div>
    <div><span  class="itemColor" style="background-color:#4eb2fd"></span><label>容缺受理</label></div>
    <div><span  class="itemColor" style="background-color:#3DBB93"></span><label>未办</label></div>
</div>--%>
<div style="z-index: 1000; position: absolute; left: 0; width: 100%; background: rgba(0,0,0,0.5); right: 0; top: 0; bottom: 0;display:none">
        <div id="westPanel" class="col-xl-10" style="padding: 0px 2px 0px 8px; width:50%;top: 30px;height:85%">
    <div class="m-portlet" style="margin-bottom: 0px;width: 100%;">
        <div class="m-portlet__head">
            <div class="m-portlet__head-caption">
                <div class="m-portlet__head-title">
                                    <span class="m-portlet__head-icon m--hide">
                                        <i class="la la-gear"></i>
                                    </span>
                    <h3 class="m-portlet__head-text">
                        <%--${itemNatureTitle}--%>工程建设审批事项库
                    </h3>
                </div>
            </div>
            <div class="m-portlet__head-tools">
                <font color="red">*&nbsp;</font>
                请从工程建设审批事项库里面选择关联事项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <%--请从${itemNatureTitle}里面选择本${stageTypetitle}关联事项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
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
                    <button type="button" class="btn btn-secondary"
                            onclick="searchSelectItemNode();">查询</button>
                    <button type="button" class="btn btn-secondary"
                            onclick="clearSearchSelectItemNode();">清空</button>
                    <button type="button" class="btn btn-secondary"
                            onclick="expandSelectItemAllNode();">展开</button>
                    <button type="button" class="btn btn-secondary"
                            onclick="collapseSelectItemAllNode();">折叠</button>
                    <button type="button" class="btn btn-info"
                            onclick="bindItemNode();">选择</button>
                    <button type="button" class="btn btn-secondary"
                            onclick="javasclipt:$('#westPanel').hide(),$('#westPanel').parent().hide();">关闭</button>
                </div>
            </div>
            <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
            <div style="position:relative">
                <div id="selectItemTree" class="ztree" style="overflow: auto;width:100%"></div>
            </div>

        </div>
    </div>
</div>
</div>
        <%--阶段模态框--%>
        <div class="modal fade" id="stageModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">阶段属性</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="row">
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">阶段名称</label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="text" class="form-control" name="auEleName" data-field=".content/html" nowchange="true" placeholder="阶段名称">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">编&ensp;&ensp;&ensp;&ensp;码</label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="text" class="form-control" name="stageCode" data-field="stage/stageCode" placeholder="编码">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label"></label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="checkbox" name="stageOther" data-field="stage/isOptionItem" checked > 是否允许设置并行推进事项</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isSelItem" > 是否允许申报时勾选审批事项</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isCheckItem"> 是否允许设置前置检查事项</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/useOneForm" > 是否设置"一张表单"</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isCheckItemform" > 是否检测事项表单</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isCheckPartform" > 是否检测扩展表单</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isCheckProj" > 是否检测项目信息</br>
                                            <input type="checkbox" name="stageOther" data-field="stage/isCheckStage" > 是否监测前置阶段</br>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">承诺时限</label>
                                        <div class="col-lg-9 col-md-9">
                                        <input type="number" name="dueNum" class="form-control" data-field="stage/dueNum" value="1" placeholder="承诺时限">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">承诺时限单位:</label>
                                        <div class="col-lg-9 col-md-9">
                                        <select type="text" class="form-control" name="dueUnit" data-field="stage/dueUnit" value="">
                                            <c:forEach items="${dueUnit}" var="unit">
                                                <option value="${unit.itemCode}">${unit.itemName}</option>
                                            </c:forEach>
                                        </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group" style="display:none">
                                        <label class="col-lg-3 col-md-3 control-label">是否主线:</label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="radio" name="isNode" data-field="stage/isNode" checked value="1"> 是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="isNode" data-field="stage/isNode" value="2"> 否
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">办理方式:</label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="radio" name="handWay" data-field="stage/handWay" checked value="1"> 按阶段多级情形组织事项办理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="handWay" data-field="stage/handWay" value="0"> 多事项直接合并办理
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">对应国家标准审批阶&ensp;&ensp;&ensp;&ensp;段:</label>
                                        <div class="col-lg-9 col-md-9">
                                        <%--<input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" checked value="1"> 工程建设许可&nbsp;&nbsp;--%>
                                        <%--<input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" value="2"> 施工许可&nbsp;&nbsp;--%>
                                        <%--<input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" value="3"> 竣工验收&nbsp;&nbsp;--%>
                                        <%--<input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" value="4"> 并行推进&nbsp;&nbsp;--%>
                                        <%--<input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" value="4"> 并行推进&nbsp;&nbsp;--%>
                                                <c:forEach items="${dybzspjdxhs}" var="dybzspjdxh">
                                                        <input type="checkbox" name="dybzspjdxh" data-field="stage/dybzspjdxh" value="${dybzspjdxh.itemCode}">${dybzspjdxh.itemName}&nbsp;&nbsp;
                                                        <%--<input type="checkbox" name="dybzspjdxh1" value="${dybzspjdxh.itemCode}" onclick="checkboxOnclick('dybzspjdxh1','dybzspjdxh',true)">${dybzspjdxh.itemName}<span></span>--%>
                                                    <%--</label>--%>
                                                </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="form-group">
                                        <label class="col-lg-3 col-md-3 control-label">里程碑事项类型:</label>
                                        <div class="col-lg-9 col-md-9">
                                            <input type="radio" name="lcbsxlx" data-field="stage/lcbsxlx" checked value="1"> 所有里程碑事项办结，该审批阶段才算办结</br>
                                            <input type="radio" name="lcbsxlx" data-field="stage/lcbsxlx" value="2"> 任一项里程碑事项办结，该审批阶段就算办结
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="saveStageAttrBtn">保存</button>
                    </div>
                </div>
            </div>
        </div>

        <%--共用属性--%>
<div class="modal fade modal-middle" id="commonModal" tabindex="-1" role="dialog" aria-labelledby="commonModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="commonModalLabel">阶段属性</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label >名称</label>
                        <input type="text" class="form-control" name="auEleName" data-field=".content/html" nowchange="true" placeholder="阶段名称">
                    </div>
                    <div class="form-group">
                        <label >字体大小</label>
                        <div style="position: relative; padding-top: 30px;">
                            <div class="range_tip" style="left: 135px;">13</div>
                            <div style="display: inline-block;width: 20px;">1</div>
                            <input type="range" id="range_01" class="form-control" nowchange="true" name="auEleFontSize" min="1" max="50" value="13" style="display: inline-block;height: 6px;width: calc( 100% - 70px);">
                            <div style="display: inline-block;margin-left: 10px;">50</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label >字体颜色：</label>
                        <div>
                            <input type="color" class="form-control" style="width:5rem;" name="fontColor" data-field=".content/font-color"  nowchange="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label >字体加粗：</label>
                        <input type="radio" name="fontBold" data-field=".content/font-weight" value="normal" checked="true" nowchange="true">正常
                        <input type="radio" name="fontBold" data-field=".content/font-weight" value="bold" nowchange="true">加粗
                    </div>
                    <div class="form-group" style="display: flex; align-items: flex-end;">
                        <label style="float: left;">背景颜色：</label>
                        <span>
                            <input type="color" class="form-control" name="backColor" data-field=".body/fill" style="width:5rem; float:left" nowchange="true">
                            <input type="checkbox" style="margin-left: 1rem; margin-top: 1rem;" value="none">&nbsp;透明
                        </span>
                    </div>
                    <div class="form-group" style="display: flex; align-items: flex-end;">
                        <label style="float: left;">边框颜色：</label>
                        <span>
                            <input type="color" class="form-control" name="borderColor" data-field=".body/stroke" style="width:5rem; float:left" nowchange="true">
                            <input type="checkbox" style="margin-left: 1rem; margin-top: 1rem;" value="none">&nbsp;透明
                        </span>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--并行阶段设置--%>
<div class="modal fade modal-middle" id="parallelModal" tabindex="-1" role="dialog" aria-labelledby="parallelModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="parallelModalLabel">并行属性</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label >名称</label>
                        <input type="text" class="form-control" name="auEleName" data-field=".content/html" nowchange="true" placeholder="并行名称">
                    </div>
                    <div class="form-group">
                        <label >并行阶段：</label>
                        <div id="parallelStages">
                            <input type="checkbox" name="parallel" data-field="stage/dybzspjdxh1" value="1"> 阶段一&nbsp;&nbsp;
                            <input type="checkbox" name="parallel" data-field="stage/dybzspjdxh1" value="2"> 阶段二&nbsp;&nbsp;
                            <input type="checkbox" name="parallel" data-field="stage/dybzspjdxh1" value="3"> 阶段三&nbsp;&nbsp;
                            <input type="checkbox" name="parallel" data-field="stage/dybzspjdxh1" value="4"> 阶段四&nbsp;&nbsp;
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveParallelAttrBtn">保存</button>
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
                <div id="uploadProgressMsg" style="padding-top: 5px;">保存阶段流程中，请稍后...</div>
            </div>
        </div>
    </div>
</div>
        <script type="text/javascript">
            var themeVerId = "${themeVerId}", currentBusiId =  themeVerId, curIsEditable = '${curIsEditable}', dueUnit='${dueUnit}',themeVerDiagram='${themeVerDiagram}', statusList=null,drawGrid=true;
            var ctx = '${pageContext.request.contextPath}';
        </script>
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.min.js"></script>--%>
        <%--<script type="text/javascript" src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/gd.js"></script>--%>
        <!--<script type="text/javascript" src="https://apis.google.com/js/client.js"></script>-->

        <!-- Dependencies: -->
        <script src="${pageContext.request.contextPath}/rappid/node_modules/jquery/dist/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/node_modules/lodash/index.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/node_modules/backbone/backbone.js"></script>

        <script src="${pageContext.request.contextPath}/rappid/build/rappid.min.js"></script>

        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/inspector.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/toolbar.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/example.js"></script>
        <script src="${pageContext.request.contextPath}/rappid/apps/BPMNEditor/src/bpmn.js"></script>
<script src="https://cdn.bootcss.com/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/theme-libs/material-dashboard-pro-html-v1/assets/js/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agcloud-common/agcloud.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery-ui1/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/sortable1/Sortable.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agtree3/js/jquery.agtree.core.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.excheck.js" type="text/javascript"></script> <!--扩展实现复选框-->
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.exedit.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ztree3/js/jquery.ztree.exhide.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/ui-static/stage/rappid_set_stage_item.js"></script>


        <script src="${pageContext.request.contextPath}/ui-static/stage/rappid_operate.js"></script>

