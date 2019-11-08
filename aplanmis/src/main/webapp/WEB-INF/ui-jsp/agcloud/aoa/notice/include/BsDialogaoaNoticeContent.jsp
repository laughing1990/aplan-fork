<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 新增公告信息 -->
<div class="modal fade" id="dialogAoaNoticeContent" tabindex="-1" role="dialog" aria-labelledby="dialogAoaNoticeContentTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialogAoaNoticeContentTitle"></h5>
                <span class="close"  data-dismiss="modal" aria-label="Close" ></span>
            </div>
            <div class="modal-body" style="text-align: center;overflow-y: auto;">
                <form id="editAoaNoticeContentForm" method="post" novalidate="novalidate">
                    <input type="hidden" id="contentId" name="contentId"/>
                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>标题:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="contentTitle" id="contentTitle" class="form-control m-input" placeholder="请输入标题">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>作者:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="contentAuthor" id="contentAuthor" class="form-control m-input" placeholder="请输入作者">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>封面图片:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="coverAttLinkId" id="coverAttLinkId" class="form-control m-input" placeholder="请输入封面关联附件ID">
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>正文:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <textarea class="form-control m-input" name="contentText" id="contentText"  value="" rows="5"></textarea>
                        </div>

                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>附件:
                        </label>
                        <div class="col-xl-9 col-lg-9" style="height:200px;">
                            <%--
                            <button type="button" class="btn btn-info" style="width: 105px;" onclick="">
                                点击上传
                            </button>
                            --%>
                                <script>
                                    var _tableName = "bsc_notice_content";//表名 用于查询和上传
                                    var _pkName = "content_id";//字段 用于查询和上传
                                    var recordIds = "6c9afdb7-7e5a-4cc0-9ce2-eb7c675430c8";//业务的ID数组 用于查询
                                    var recordId = "6c9afdb7-7e5a-4cc0-9ce2-eb7c675430c8";//业务ID 用于上传
                                </script>
                                <%--<iframe style="height: 100%;width: 100%" frameborder="0"  id="contents" src="${pageContext.request.contextPath}/me/bsc/att/commonIndex.do?dirId=10001">
                                </iframe>--%>
                        </div>
                    </div>
					<div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>选择分类:
                        </label>
                        <div class="col-xl-9 col-lg-9">
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
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>发送范围:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="noticeRange" id="noticeRange" class="form-control m-input" placeholder="请输入发送范围">
                        </div>
                    </div>
					<div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>是否需要发送消息:
                        </label>
                        <div class="col-xl-9 col-lg-9">
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
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            <font color="red">*</font>备注说明:
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <textarea class="form-control m-input" name="contentMemo" id="contentMemo"  value="" rows="5" placeholder="请输入备注说明"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 15px">
                <button type="button" class="btn btn-info" style="width: 75px;" onclick="saveAoaNoticeContent();">
                    保存
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal" style="width: 75px;">
                    关闭
                </button>
            </div>
        </div>
    </div>
</div>
<script>
    function saveAoaNoticeContent(){
        $.post(ctx+UrlAdapterPrefix+'/aoa/notice/content/saveAoaNoticeContent.do', $('#editAoaNoticeContentForm').serialize(), function(result){
            if (result.success){
                swal({
                    title: '提示信息',
                    text: "保存成功！",
                    type: 'info',
                    showCancelButton: false,
                    confirmButtonText: '是'
                }).then(function(result) {
                    $('#dialogAoaNoticeContent').modal('hide');
                    location.reload();
                });
            } else {
                swal('提示信息', '保存失败！', 'error');
            }
        }, 'json');
    }

    //新增公告
    function addAoaNoticeContent(){
        $("#editAoaNoticeContentForm :input").not(":button, :submit, :reset").val("").removeAttr("checked").remove("selected");
        $('#dialogAoaNoticeContentTitle').html('新增公告');
        $('#dialogAoaNoticeContent').modal('show');
    }

    //编辑公告
    function editAoaNoticeContent(id){
        $('#dialogAoaNoticeContentTitle').html('编辑公告');
        $('#dialogAoaNoticeContent').modal('show');
        $.post(ctx+UrlAdapterPrefix+'/aoa/notice/content/getAoaNoticeContent.do', {id:id}, function(data){
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
        }, 'json');
    }

    //删除公告
    function deleteAoaNoticeContent(id){
        var msg = '你确定要删除吗？';
        swal({
            title: '此操作影响：',
            text: msg,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value){
                $.ajax({
                    url: ctx+UrlAdapterPrefix+'/aoa/notice/content/deleteAoaNoticeContentById.do',
                    type: 'POST',
                    data: {id:id},
                    success: function (result) {
                        if(result.success){
                            location.reload();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }
    function searchFormData(){
        var keyword = $('#containerCondition #keyword').val();
        var categoryId=$('#containerCondition #categoryId').val();

        location.href = ctx+UrlAdapterPrefix+'/aoa/notice/content/index.do?'+'keyword='+keyword+'&categoryId='+categoryId;
    }
</script>
