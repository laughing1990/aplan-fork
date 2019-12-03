<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/layui.global.css"/>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/css/modules/layui-icon-extend/iconfont.css"/>
<!-- bower:css -->
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/bootstrap/dist/css/bootstrap.css"/>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/lib/codemirror.css"/>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/hotbox/hotbox.css"/>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/kityminder-core/dist/kityminder.core.css"/>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/color-picker/dist/color-picker.min.css"/>
<!-- endbower -->
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/kityminder.editor.min.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/canvas.css"/>
<style type="text/css">
    div.minder-editor-container {
        position: absolute;
        top: -2px;
        bottom: 0;
        left: 0;
        right: 0;
    }

    div.minder-editor {
        position: absolute;
        top: 60px;
        bottom: 0;
        left: 0;
        right: 0;
    }
    .flow_steps .stage {
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
    }
    .flow_steps .stage li:not(:last-child) {
        flex: 1;
        -ms-flex: 1;
        text-align: center\9;
        display: inline-block\9;
        line-height: 36px\9;
    }
    .flow_steps .stage .flow_steps_btn .line {
        display: inline-block;
        float: left\9;
    }
    .flow_steps .stage .flow_steps_btn .Allscreen {
        padding: 0px\9;
        float: left\9;
    }
    .flow_steps .down {
        width: 90px\9;
        height: 23px\9;
        line-height: 23px\9;
        left: 65%\9;
        top: -30px\9;
    }
    .flow_steps .stage .flow_steps_btn .close {
        float: left\9;
    }
    .flow_steps .stage .flow_steps_btn {
        flex: 0.2;
        -ms-flex: 0.2;
    }
    .col-6 {
        flex: 0 0 50%;
        -ms-flex: 0 0 50%;
        max-width: 50%;
    }

    .current-temp-item .caret, .theme-item-selected .caret {
        margin-left: 0px;
    }

    .tab-content .km-priority .km-priority-item .km-priority-icon {
        background: url("${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/iconpriority-1.png") repeat-y;
        background-color: transparent;
    }

    .tool-group .tool-group-item .tool-group-icon {
        width: 56px;
        padding-left: 28px;
        line-height: 10px;
        background-position-x: 6px !important;
        font-size: 12px;
    }
    .km-btn-group{
        margin: 2px 0px !important;
    }
    .tab-content .km-progress{
        margin: 0px;
        padding-top: 5px;
    }
    .tab-content .km-priority{
        margin: 0;
        padding-top: 5px;
    }
    .tab-content .km-progress, .tab-content .km-priority {
        margin-top: 0px;
    }
    .tab-content .km-priority {
        margin: 0px;
    }
    .skipElem {
        padding: 5px 20px 5px 20px !important;
    }

    /*按钮排版*/
    .theme-panel{
        margin-top: 12px;
    }
    .btn-group-vertical.note-btn-group{
        padding-right: 0px;
        margin: 2px;
        margin-top: 6px;
    }
    .btn-group-vertical{
        margin: 0px;
    }
    .btn-group-vertical > .btn{
        padding: 5px 10px;
    }
    .ng-binding{
        font-size: 14px;
    }

    .radio label, .checkbox label {
        margin-left: 7px;
    }
    .deactive{
        opacity: 0.4;
        filter: alpha(opacity=40);
    }
    
</style>

<%--JavaScript 部分--%>
<script src="${pageContext.request.contextPath}/ui-static/kitymind/js/kitymind_constant.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/jquery/dist/jquery.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/common/global.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/common/util.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/layui/layui.all.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/common/dataDivSub.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/angular/angular.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/lib/codemirror.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/xml/xml.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/javascript/javascript.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/css/css.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/htmlmixed/htmlmixed.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/markdown/markdown.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/addon/mode/overlay.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/codemirror/mode/gfm/gfm.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/angular-ui-codemirror/ui-codemirror.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/marked/lib/marked.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/kity/dist/kity.min.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/hotbox/hotbox.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/json-diff/json-diff.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/kityminder-core/dist/kityminder.core.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/bower_components/color-picker/dist/color-picker.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/index.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/kityminder.editor.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/ng-layer.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/kityminder.editor.extend.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/itemSituation.fun.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/cache/affair/catalogues/issuesManagement/itemSituation/itemSituation_index.js"></script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/mindExtend.js"></script>

<style type="text/css">
    .btn-group-vertical.note-btn-group{
        margin: 0px 2px;
    }
    .tab-content .km-progress .km-progress-item .km-progress-icon.progress-9{
        background-position: 0 0;
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/images/app/mustSelect.png) no-repeat;
    }
    .tab-content .km-progress .km-progress-item .km-progress-icon.progress-0{
        background-position: 0 0;
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/images/app/close.png) no-repeat;
    }

    .tab-content .km-priority .km-priority-item .km-priority-icon.priority-2{
        background-position: 0 0;
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/images/app/sellectMore.png) no-repeat;
    }
    .tab-content .km-priority .km-priority-item .km-priority-icon.priority-0{
        background-position: 0 0;
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/images/app/close.png) no-repeat;
    }

    /*终止情形*/
    .tab-content .km-priority .km-priority-item .km-terminate-situation-icon {
        /*background:#aa00aa !important;*/
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-terminate-situation.png) repeat-y 0px 100px;
        background-color: transparent;
    }
    .tab-content .km-priority .km-priority-item .km-terminate-situation-icon.terminate-situation-0 {
        background-position: 0 0;
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/images/app/close.png) no-repeat;
    }

    /*情形答案个数*/
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-0{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 20px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 200px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-2{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 180px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-3{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 160px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-4{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 140px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-5{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 120px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-6{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 100px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-7{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 80px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-8{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 60px;
        background-color: transparent;
        width:20px;
    }
    .tab-content .km-priority .km-priority-item .km-situation-answer-num-icon.situation-answer-num-9{
        background: url(${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-situation-answer-num.png) repeat-y 0px 40px;
        background-color: transparent;
        width:20px;
    }

    .tab-content .km-priority .km-priority-item .km-isInformCommit-icon {
        background: url("${pageContext.request.contextPath}/ui-static/agcloud/bsc/mind/resource/js/kityminder/images/icon-isInformCommit.png") repeat-y;
        background-color: transparent;
    }

    .tab-content .km-priority, .tab-content .km-progress{
        border-right: none;
    }
    .modal .modal-content .modal-header .close {
        margin-top: -4px;
    }

</style>
