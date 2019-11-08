<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;margin: 0; padding: 0;border: 0;overflow: auto;">
<head>
    <title>公告分类表管理</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-sortable1.jsp" %>
    <script type="text/javascript">
    </script>
</head>
<body style="width: 100%">
<div class="m-form m-form--label-align-right m--margin-top-20 m--margin-bottom-20">
    <div class="row align-items-center">
        <div class="col-xl-7 order-1 m--align-left m--padding-left-20">
            <a href="javascript:addAoaNoticeCategory();"
               class="btn m-btn btn-primary"> <span> <span class="h6 m--margin-bottom-0 ">新建公告分类表</span> </span> </a>
            <div class="m-separator m-separator--dashed d-xl-none"></div>
        </div>
        <div class="col-xl-5 order-2 order-xl-1  m--align-right" id="containerCondition">
            <div class="form-group m-form__group row align-items-center">
                <div class="col-md-8 m--margin-left-210">
                    <div class="input-group">
                        <div class="input-group-prepend">
                        </div>
                        <input id="keyword" type="text" name="keyword" value="" class="form-control"
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
            <table class="m-datatable__table" style="display: block;  overflow-x: auto;">
                <thead class="m-datatable__head">
                <tr class="m-datatable__row border-bottom text-center" style="left: 0px;">
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">顶级组织ID</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">分类名称</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">排序号</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">创建人</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">创建时间</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">修改人</span>
                    </th>
                    <th class="m-datatable__cell text-left m-datatable__cell--sort bg-transparent" style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">修改时间</span>
                    </th>
                    <th class="m-datatable__cell text-center m-datatable__cell--sort bg-transparent"
                        style="width: 10%;">
                        <span class="h6 m--margin-bottom-0 m--font-titlegrey">操作</span>
                    </th>
                </tr>
                </thead>
                <tbody class="m-datatable__body">
                <c:choose>
                    <c:when test="${listAoaNoticeCategory.size()>0}">
                        <c:forEach items="${listAoaNoticeCategory}" var="aoaNoticeCategory" varStatus="status">
                        <tr data-row="0" class="m-datatable__row  border-bottom" style="left: 0px;">
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeCategory.categoryOrgId}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeCategory.categoryName}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeCategory.categorySortNo}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeCategory.creater}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        <fmt:formatDate value="${aoaNoticeCategory.createTime}" pattern="yyyy-MM-dd"/>
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        ${aoaNoticeCategory.modifier}
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                    <span class="m--margin-bottom-15 text-left ">
                                        <fmt:formatDate value="${aoaNoticeCategory.modifyTime}" pattern="yyyy-MM-dd"/>
                                    </span>
                            </td>
                            <td class="m-datatable__cell text-center" style="width: 10%;">
                                <a href="javascript:void(0);"
                                   onclick="editAoaNoticeCategory('${aoaNoticeCategory.categoryId}');"
                                   class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"
                                   title="编辑">
                                    <i class="la la-edit"></i>
                                </a>
                                <a href="javascript:void(0);"
                                   onclick="deleteAoaNoticeCategory('${aoaNoticeCategory.categoryId}');"
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
<jsp:include page="include/BsDialogaoaNoticeCategory.jsp"></jsp:include>
<script>
    var UrlAdapterPrefix='';
</script>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/bpm/admin/sto/ui-js/common.js"
        type="text/javascript"></script>
</body>
</html>