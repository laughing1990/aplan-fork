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
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <script type="text/javascript">
    </script>
    <style>
        #categoryId{
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }
        #keyword{
            -webkit-border-top-left-radius: 4px;
            -moz-border-top-left-radius: 4px;
            border-top-left-radius: 4px;

            -webkit-border-bottom-left-radius: 4px;
            -moz-border-bottom-left-radius: 4px;
            border-bottom-left-radius: 4px;
        }
        .m-table thead tr th span{
            font-weight: bold;
            color: #333333!important;
        }
    </style>
</head>
<%--去横线滚动条--%>
<body style="width:98%;">
<div class="m-form m-form--label-align-right m--margin-top-20 m--margin-bottom-20">
    <div class="row align-items-center">
        <div class="col-xl-7 order-1 m--align-left m--padding-left-20">
            <a href="javascript:addAoaNoticeContentPage();"class="btn m-btn btn-primary">
               <span class="h6 m--margin-bottom-0"><i class="flaticon-paper-plane"></i> 发公告</span>
            </a>
            <div class="m-separator m-separator--dashed d-xl-none"></div>
        </div>
        <div class="col-xl-5 order-2 order-xl-1  m--align-right" id="containerCondition">
            <div class="form-group m-form__group row align-items-center">
                <%--m--margin-left-210--%>
                <div class="col-md-8 " style="margin-left:100px;">
                    <div class="input-group">
                        <div class="input-group-prepend">
                        </div>
                        <select name="categoryId" id="categoryId"
                                class="form-control m-input m-input--air" style="margin:0px 10px 0px 0px; ">
                            <c:if test="${listAoaNoticeCategory!=null}">
                                <option value="">全部</option>
                                <c:forEach items="${listAoaNoticeCategory}" var="category">
                                    <c:choose>
                                        <c:when test="${aoaNoticeContent.categoryId!=null &&aoaNoticeContentCondition.categoryId==category.categoryId}">
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


                        <input id="keyword" type="text" name="keyword" value="${aoaNoticeContentCondition.keyword}" class="form-control"
                               placeholder="输入名称或编码..."/>
                        <button type="button" class="btn btn-primary"
                                style="border-top-left-radius: 0; border-bottom-left-radius: 0;"
                                onclick="searchFormData();">查询
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="m-portlet border-0">
    <div class="m-portlet__body m--padding-top-0">
        <div class="m_datatable m-datatable m-datatable--default m-datatable--loaded" id="">
            <table class="m-datatable__table table m-table" style="display: block;  overflow-x: auto;">
                <thead class="m-datatable__head">
                <tr class="m-datatable__row border-bottom text-left" style="left: 0px;">
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">分类</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 40%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">标题</span>
                    </th>
                    <%--
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">作者</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 20%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">正文</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">是否需要发送消息</span>
                    </th>
                    --%>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">发布人</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">发布时间</span>
                    </th>
                    <%--
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">备注说明</span>
                    </th>
                    --%>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent"
                        style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">操作</span>
                    </th>
                </tr>
                </thead>
                <tbody class="m-datatable__body">
                <c:choose>
                    <c:when test="${listAoaNoticeContent.size()>0}">
                        <c:forEach items="${listAoaNoticeContent}" var="aoaNoticeContent" varStatus="status">
                        <tr data-row="0" class="m-datatable__row  border-bottom" style="left: 0px;">
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class=" text-left ">
                                        ${aoaNoticeContent.categoryName}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-left" style="width: 40%;">
                                    <span class=" text-left ">
                                        ${aoaNoticeContent.contentTitle}
                                    </span>
                            </td>
                            <%--
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeContent.contentAuthor}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-left" style="width: 20%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeContent.contentText}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeContent.needSendMsg}
                                    </span>
                            </td>
                            --%>
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class=" text-left ">
                                        ${aoaNoticeContent.publishUserName}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class=" text-left ">
                                        <fmt:formatDate value="${aoaNoticeContent.publishTime}" pattern="yyyy-MM-dd hh:mm"/>
                                    </span>
                            </td>
                            <%--
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeContent.contentMemo}
                                    </span>
                            </td>
                            --%>
                            <td class="m-datatable__cell text-left" style="width: 10%;">
                                <a href="javascript:void(0);"
                                   onclick="editAoaNoticeContentPage('${aoaNoticeContent.contentId}');"
                                   class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"
                                   title="编辑">
                                    <i class="la la-edit"></i>
                                </a>
                                <a href="javascript:void(0);"
                                   onclick="deleteAoaNoticeContent('${aoaNoticeContent.contentId}');"
                                   class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill"
                                   title="删除">
                                    <i class="la la-trash"></i>
                                </a>
                            </td>
                        </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="m-datatable__row text-center" style="left: 0px;height: 50px;">
                            <th colspan="6"><span class="h6 m--font-titlegrey">暂无数据</span></th>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="include/BsDialogaoaNoticeContent.jsp"></jsp:include>
<style>
    .modal-lg {
        max-width: 900px;
    }
</style>
<script>
    var UrlAdapterPrefix='';
</script>

</body>
<script>
    function addAoaNoticeContentPage() {
        var url='${pageContext.request.contextPath}/aoa/notice/content/editPage.do?isNew=1';
        location.href=url;
//        window.open();
    }

    function editAoaNoticeContentPage(contentId) {
        var url='${pageContext.request.contextPath}/aoa/notice/content/editPage.do?isNew=0';
        url+='&contentId='+contentId;
        location.href=url;
//        window.open();
    }

</script>

</html>