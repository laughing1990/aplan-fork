<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>个人消息库</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/agcloud/aoa/msg/aoaMsgContent.js"
            type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/aoa/msg/gray/orgTheme.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/aoa/msg/gray/message.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .btn-lg, .btn-group-lg > .btn {
            padding: 1rem 1.0rem;
            font-size: 1.25rem;
            line-height: 1.5;
            border-radius: 0.3rem;
        }

        .m-header-menu {
            display: table;
            float: left;
            width: auto;
            height: 100%;
            margin: 0px 0px 0px 0px;
        }

        .m-portlet .m-portlet__body {
            padding: 0px;
        }

        .btn-brand {
            background-color: rgb(88, 103, 221);
            border-color: rgb(88, 103, 221);
        }
    </style>
</head>
<body>
<div style="height: 100%;overflow: hidden">
    <!--BEGIN:头部-->
    <header class="m-header" id="msg-header">
        <div class="m-grid__item">
            <!--BEGIN: 标题 -->
            <div class="agcloud-m-title">
                <div class="agcloud-title-icon">
                    <img src="${pageContext.request.contextPath}/ui-static/agcloud/framework/ui-scheme/scheme2/admin/images/common/view-message-img.png" width="30" height="30" align="absmiddle">
                    <span>我的消息</span>
                </div>
            </div>
            <!--END: 标题 -->
            <!--BEGIN: 头部按钮 -->
            <div class="operate-btn-box clearfix">
                <div class="operate-btn">
                    <button class="btn btn-info fl" onclick="newPersonMsg();">
                        <i class="la la-plus-circle"></i>
                        <span>新建信息</span>
                    </button>

                    <form id="search_msg_list_form"  class="input-group fl" method="post">
                        <input type="text" id="searchMsgKeyword" class="form-control form-control-warning" placeholder="请输入主题查询">
                        <div class="input-group-append">
                            <button type="button" class="btn btn-primary"  onclick="searchMsg();"><i class="flaticon flaticon-search"></i> 查询</button>
                        </div>
                        <button type="button" class="btn  btn-danger" onclick="clearSearch();" style="margin-left: 10px">清空</button>
                    </form>

                </div>
            </div>
            <!--END: 头部按钮 -->
        </div>
    </header>
    <!--BEGIN: 主体内容部分 -->
    <div class="m-body">
        <div class="m-body-left"  id="westSpace">
            <div class="m-portlet">

                <div class="m-portlet__body" style="" id="west_body">
                    <div class="leftbutton">
                        <button type="button" class="btn btn-default btn-primary" onclick="boxShow('1')" id="inbox"
                                style="width: 100%;margin:px 0px 0px 0px;">
                            收件箱
                        </button>
                    </div>
                    <div class="leftbutton">
                        <button type="button" class="btn btn-default" onclick="boxShow('2')" id="outbox"
                                style="width: 100%;margin:5px 0px 0px 0px;">
                            发件箱
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="m-body-right">
            <div class="m-portlet m-portlet--primary" style="height: 100%;">
                <div class="m-portlet__body" id="center_body" style="height: 100%;">
                    <div id="containerMsgReceive" style="padding: 0px 0px 13px;">
                        <div class="btn-group" id="containerReadStatus">
                            <span class="btn btn-info" readStatus="1" >
                                已读消息
                            </span>

                            <span class="btn btn-outline-info"  readStatus="0" style="border-left: 1px solid #fff">
                                未读消息
                            </span>
                        </div>
                        <div class="stage-color btn-group" id="btnContainerReceive">
                            <span class="btn btn-outline-info" onclick="replyMsg();">答复</span>
                            <span class="btn btn-outline-info" onclick="deleteMsgList();">删除</span>
                            <span class="btn btn-outline-info" onclick="tagMsgReaded();">标为已读</span>
                            <span class="btn btn-outline-info" onclick="tagMsgUnReaded();">标为未读</span>
                        </div>

                    </div>
                    <div id="index_msg_list_tb" class="m_datatable table-sm" style="height:500px;"></div>


                </div>
            </div>
        </div>
    </div>
    <!--END: 主体内容部分 -->
</div>

<%--新建消息框--%>
<div id="person_msg_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="person_msg_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 11px;height: 45px;">
                <h5 class="modal-title" id="person_msg_modal_title" style="color: #333">
                    <i class="la la-plus-circle"></i> 新建消息
                </h5>
                <span class="close" onclick="$('#person_msg_modal').modal('hide');" >

                </span>
            </div>
            <div class="modal-body" style="padding: 10px;padding-right: 10px">
                <form id="person_msg_form" method="post">

                    <input type="hidden" id="rangeId" name="rangeId" value=""/>
                    <input type="hidden" id="isImportant" name="isImportant" value=""/>
                    <input type="hidden" name="userId" id='userId' value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font
                                style="color:red">*</font>收件人:</label>
                        <div class="col-lg-8">
                            <input type="text" class="form-control m-input" name="userName" id='userName' value="" readonly="readonly" />
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-outline-info"
                                    onclick="showModalOrgPosUserTree();">
                                <i class="flaticon-download"></i>
                                选择
                            </button>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font
                                style="color:red">*</font>主题:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control m-input" name="contentTitle" value="" style="width: 602px"/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font
                                style="color:red">*</font>内容:</label>
                        <div class="col-lg-9">
                            <textarea class="form-control" name="contentText" rows="10" style="width: 602px"></textarea>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-2">
                            <label class="m-checkbox m-checkbox--state-info" style="margin-top: 10px">
                                <input type="checkbox" id="check0">
                                重要
                                <span></span>
                            </label>
                        </div>
                        <div class="col-sm-7">
                            <label class="m-checkbox m-checkbox--state-info" style="margin-top: 10px">
                                <input type="checkbox" id="check1">
                                一般
                                <span></span>
                            </label>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" id="form-group-btn-box" style="text-align: right;">
                        <div class="col-lg-12">
                            <button type="button" class="btn btn-secondary"
                                    onclick="$('#person_msg_modal').modal('hide');">关闭
                            </button>&nbsp;&nbsp;
                            <button type="submit" class="btn btn-info">发送</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%--收件消息框--%>
<div id="view_person_msg_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_person_msg_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding:0px;height: 45px;">
                <h5 class="modal-title" id="view_person_msg_modal_title" style="color: #333;padding: 13px;">
                   <i class="la	la-envelope-o"></i> 收件消息
                </h5>
                <span class="close" onclick="$('#view_person_msg_modal').modal('hide');" >

                </span>
            </div>
            <div class="modal-body" style="padding: 10px;padding-right: 10px">
                <form id="view_person_msg_form" method="post">

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">发件人:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control m-input" name="sendUserName" value="" readonly/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">主题:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control m-input" name="contentTitle" value="" readonly/>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;">内容:</label>
                        <div class="col-lg-9">
                            <textarea class="form-control" name="contentText" rows="10" readonly></textarea>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-2">
                            <label class="m-checkbox m-checkbox--state-info" style="margin-top: 10px">
                                <input type="checkbox" id="view_check0" onclick="return false">
                                重要
                                <span></span>
                            </label>
                        </div>
                        <div class="col-sm-8">
                            <label class="m-checkbox m-checkbox--state-info" style="margin-top: 10px">
                                <input type="checkbox" id="view_check1" onclick="return false">
                                一般
                                <span></span>
                            </label>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="include/modalOrgPosUserTree.jsp"></jsp:include>
<style>
    #index_msg_list_tb{
        width: calc(100% - 0px);
        max-height: calc(100vh - 100px);
        overflow-y: auto;
    }
    .row {
         margin-right: -0px;
         margin-left: -0px;
    }
    .m-datatable__pager-detail{
        display:inline !important;
    }
</style>
</body>
</html>