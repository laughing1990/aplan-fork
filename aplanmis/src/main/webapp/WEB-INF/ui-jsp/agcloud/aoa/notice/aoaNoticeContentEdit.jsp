<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;margin: 0; padding: 0;border: 0;overflow: auto;">
<head>
    <title>公告管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%--    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>--%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <%--富文本--%>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ueditor1/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ueditor1/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/ueditor1/lang/zh-cn/zh-cn.js"></script>
    <style>
        .edui-editor-breadcrumb span {
            display: inline;
        }
        /*ztree样式修复*/
        article, aside, dialog, figcaption, figure, footer, header, hgroup, main, nav, section, span {
            display: inline;
        }
        /*BEGIN 头部*/
        #msg-header .m-grid__item{
            background: #fff!important;
        }
        #msg-header .agcloud-m-title{
            border: 0;
            border-radius: 3px;
            height: 42px;
            line-height: 42px;
            color: #555;
            background-color: #fff !important;
            border-bottom: 1px solid #E8E8E8;
        }
        #msg-header .agcloud-title-icon{
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            display: -webkit-flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            margin-left: 15px;
            font-size: 16px;
            font-weight: bold;
        }
        #msg-header .agcloud-title-icon .flaticon-edit{
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            justify-content: center;
            background:#FFB101;
            color: #fff;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            font-size: 19px;
        }
        #msg-header .agcloud-title-icon span{
            margin-left: 10px;
        }
        /*END 头部*/

        .m-portlet{
            border: none;
        }
        .m-input{
            height: 36px;
            line-height: 36px;
        }
        #edui1{
            min-height: 300px;
        }
        #wrapper{
             margin-left: 0!important;
         }
    </style>
</head>
<body style="width: 98%">
<!--BEGIN:头部-->
<header class="m-header" id="msg-header">
    <div class="m-grid__item">
        <!--BEGIN: 标题 -->
        <div class="agcloud-m-title">
            <div class="agcloud-title-icon">
                <i class="flaticon-edit"></i>
                <span>编辑公告</span>
            </div>
        </div>
        <!--END: 标题 -->
    </div>
</header>
<!--END:头部-->

<div class="m-grid__item m-grid__item--fluid m-wrapper" style="padding: 10px">
    <div class="m-content">
        <div class="m-portlet__body">
            <div class="col-lg-12">
                <form id="editAoaNoticeContentForm" class="m-form m-form--label-align-right" method="post">
                    <div class="m-portlet">
                        <div class="m-portlet__head">
                            <!-- 标题工具栏 -->
                            <div class="m-portlet__head-tools" style="text-align:left;">
                                <button type="button" class="btn btn-info" onclick="history.go(-1);">
                                    <i class="la la-mail-reply"></i>
                                    返回公告列表
                                </button>&nbsp;

                            </div>
                        </div>
                        <div class="m-portlet__body">
                            <input type="hidden" id="contentId" name="contentId"/>
                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    标题：<font color="red">*</font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <input type="text" name="contentTitle" id="contentTitle" class="form-control m-input"
                                           placeholder="请输入标题">
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    作者：<font color="red">*</font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <input type="text" name="contentAuthor" id="contentAuthor" class="form-control m-input"
                                           placeholder="请输入作者">
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    封面图片：<font color="red">*</font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <jsp:include page="include/cover.jsp"></jsp:include>
                                    <span class="btn btn-outline-info" style="padding: 12px 20px;"><i class="la la-plus"></i>添加封面</span>
                                </div>
                            </div>
                            <input type="hidden" id="coverAttLinkId" name="coverAttLinkId" value="1">

                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    正文：<font color="red">*</font>
                                </label>
                                <input type="hidden" name="contentText" id="contentText"/>
                                <div class="col-xl-10 col-lg-10">
                                    <script id="editorContentText" type="text/plain" style=""></script>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    附件：<font color="red">*</font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <jsp:include page="../../bsc/att/me/att_simple_upload.jsp"/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    选择分类：<font color="red">*</font>
                                </label>
                                <div class="col-xl-8 col-lg-8">
                                    <select name="categoryId" id="categoryId"
                                            class="form-control m-input m-input--air">
                                        <c:if test="${listAoaNoticeCategory!=null}">
                                            <c:forEach items="${listAoaNoticeCategory}" var="category">
                                                <c:choose>
                                                    <c:when test="${aoaNoticeContent.categoryId!=null &&aoaNoticeContent.categoryId==category.categoryId}">
                                                        <option value="${category.categoryId}"
                                                                selected="selected">${category.categoryName}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${category.categoryId}">${category.categoryName}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                                <div class="col-xl-2 col-lg-2">
                                    <a href="javascript:;" onclick="addAoaNoticeCategory();">
                                        <span class="btn btn-outline-info" style="padding: 12px 20px;"><i class="la la-plus"></i>新增分类</span>
                                    </a>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    发送范围：<font color="red">*</font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <a href="javascript:loadTreeAoaNoticeRange()">
                                        <span class="btn btn-outline-info"><i class="flaticon-search"></i>查看</span>
                                    </a>
                                    <jsp:include page="include/orgRangeTree.jsp"></jsp:include>
                                </div>
                            </div>
                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    是否需要发送消息：<font color="red"></font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <select name="needSendMsg" id="needSendMsg"
                                            class="form-control m-input m-input--air">
                                        <c:choose>
                                            <c:when test="${aoaNoticeContent.needSendMsg!=null &&aoaNoticeContent.needSendMsg==1}">
                                                <option value="1" selected="selected">是</option>
                                                <option value="0">否</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="1">是</option>
                                                <option value="0">否</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                    备注： <font color="red"></font>
                                </label>
                                <div class="col-xl-10 col-lg-10">
            <textarea class="form-control" name="contentMemo" id="contentMemo" value="" rows="5"
                      placeholder="请输入备注说明"></textarea>
                                </div>
                            </div>


                            <div class="form-group m-form__group row">
                                <label class="col-xl-2 col-lg-2 col-form-label">
                                </label>
                                <div class="col-xl-10 col-lg-10">
                                    <button type="button" class="btn btn-info m-btn--pill" onclick="saveAoaNoticeContent();" style="padding: 8px 25px;">发送</button>
                                </div>
                            </div>


                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<style>
    .modal-lg {
        max-width: 900px;
    }
</style>
<script>
    var UrlAdapterPrefix = '';
    var editorContentText=null;
    var  currentAoaNoticeContentId='';
    //是否新增
    var currentPageIsNew='${isNew}';
</script>
<script>
    function saveAoaNoticeContent() {
        var rawContentText=editorContentText.execCommand( "getlocaldata" );
        $('#editAoaNoticeContentForm #contentText').val(rawContentText);
        var data= $('#editAoaNoticeContentForm').serialize();
        data+='&isNew='+currentPageIsNew;
        $.post(ctx + UrlAdapterPrefix + '/aoa/notice/content/saveAoaNoticeContentByIsNew.do',data, function (result) {
            if (result.success) {
                currentPageIsNew='0';
                swal({
                    title: '提示信息',
                    text: "保存成功！",
                    type: 'info',
                    showCancelButton: false,
                    confirmButtonText: '是'
                }).then(function (result) {
                    $('#dialogAoaNoticeContent').modal('hide');
                });
            } else {
                swal('提示信息', '保存失败！', 'error');
            }
        }, 'json');
    }

    //编辑公告
    function editAoaNoticeContent(id) {
        $.post(ctx + UrlAdapterPrefix + '/aoa/notice/content/getAoaNoticeContent.do', {id: id}, function (data) {
            $('#editAoaNoticeContentForm #contentId').val(data.contentId);
            $('#editAoaNoticeContentForm #orgId').val(data.orgId);
            $('#editAoaNoticeContentForm #categoryId').val(data.categoryId);
            $('#editAoaNoticeContentForm #contentTitle').val(data.contentTitle);
            $('#editAoaNoticeContentForm #contentAuthor').val(data.contentAuthor);
            $('#editAoaNoticeContentForm #coverAttLinkId').val(data.coverAttLinkId);
            $('#editAoaNoticeContentForm #contentText').val(data.contentText);
            $('#editAoaNoticeContentForm #contentSortNo').val(data.contentSortNo);
            $('#editAoaNoticeContentForm #needSendMsg').val(data.needSendMsg);
            $('#editAoaNoticeContentForm #publishUserName').val(data.publishUserName);
            $('#editAoaNoticeContentForm #publishTime').val(data.publishTime);
            $('#editAoaNoticeContentForm #contentMemo').val(data.contentMemo);
            $('#editAoaNoticeContentForm #creater').val(data.creater);
            $('#editAoaNoticeContentForm #createTime').val(data.createTime);
            $('#editAoaNoticeContentForm #modifier').val(data.modifier);
            $('#editAoaNoticeContentForm #modifyTime').val(data.modifyTime);

            editorContentText.ready(function(){
                if(data.contentText!=null&&data.contentText!=''){
                    editorContentText.setContent(data.contentText,false);
                }else{
                    editorContentText.setContent("",false);
                }
            });


        }, 'json');
    }

    //初始化上传组件
    function initUpload() {
        var tableName='bsc_notice_content';
        var pkName='content_id';
        var recordIds=currentAoaNoticeContentId;
        var recordId=currentAoaNoticeContentId;

        if(currentAoaNoticeContentId!=''){
            // Uploader.init("test111","testpk",'1','1');
            Uploader.init(tableName,pkName,recordIds,recordId);
        }
    }
    $(document).ready(function () {
        //实例化编辑器
        editorContentText = UE.getEditor('editorContentText',agcloud.config.EditorWriteOption);
        currentAoaNoticeContentId='${aoaNoticeContent.contentId}';
        $('#editAoaNoticeContentForm #contentId').val(currentAoaNoticeContentId);
        initUpload();
        if(currentPageIsNew=='0'){
            editAoaNoticeContent(currentAoaNoticeContentId);
        }

        CurrentDataCropper = {
            'tableName':'bsc_notice_content',
            'pkName': 'cover_att_link_id',
            'recordId': currentAoaNoticeContentId
        }
        setCropperImg(CurrentDataCropper);
    })
</script>
<%--公告分类--%>
<jsp:include page="include/BsDialogaoaNoticeCategory.jsp"></jsp:include>
</body>
</html>