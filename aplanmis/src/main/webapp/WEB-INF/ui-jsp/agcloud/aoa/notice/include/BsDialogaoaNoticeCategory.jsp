<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    /* BEGIN 关闭弹窗按钮样式*/
    .modal-header {

        border-bottom: 2px solid #36a3f7;
    }
    .close{
        position: absolute;
        top: 16px;
        right: 13px;
        font-size:30px!important;
    }
    /* END 关闭弹窗按钮样式*/
</style>
<!-- 新增公告分类信息 -->
<div class="modal fade" id="dialogAoaNoticeCategory" tabindex="-1" role="dialog" aria-labelledby="dialogAoaNoticeCategoryTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="height: 44px;line-height:44px;align-items: center;">
                <h5 class="modal-title" id="dialogAoaNoticeCategoryTitle"></h5>
                <span class="close"  data-dismiss="modal" aria-label="Close"></span>
            </div>
            <div class="modal-body" style="text-align: center;overflow-y: auto;">
                <form id="editAoaNoticeCategoryForm" method="post" novalidate="novalidate">
                    <input type="hidden" id="categoryId" name="categoryId"/>
                    <input type="hidden" id="categorySortNo" name="categorySortNo"/>
                    <%--
					<div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            顶级组织ID：<font color="red">*</font>
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="categoryOrgId" id="categoryOrgId" class="form-control m-input" placeholder="请输入顶级组织ID">
                        </div>
                    </div>
--%>
					<div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            分类名称：<font color="red">*</font>
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="categoryName" id="categoryName" class="form-control m-input" placeholder="请输入分类名称">
                        </div>
                    </div>

                    <%--
					<div class="form-group m-form__group row">
                        <label class="col-xl-3 col-lg-3 col-form-label">
                            排序号：<font color="red">*</font>
                        </label>
                        <div class="col-xl-9 col-lg-9">
                            <input type="text" name="categorySortNo" id="categorySortNo" class="form-control m-input" placeholder="请输入排序号">
                        </div>
                    </div>
--%>


                </form>
            </div>
            <div class="modal-footer" style="padding: 15px">
                <button type="button" class="btn btn-info" style="width: 75px;" onclick="saveAoaNoticeCategory();">
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
    function saveAoaNoticeCategory(){
        $.post(ctx+UrlAdapterPrefix+'/aoa/notice/category/saveAoaNoticeCategory.do', $('#editAoaNoticeCategoryForm').serialize(), function(result){
            if (result.success){
                swal({
                    title: '提示信息',
                    text: "保存成功！",
                    type: 'info',
                    showCancelButton: false,
                    confirmButtonText: '是'
                }).then(function(result) {
                    $('#dialogAoaNoticeCategory').modal('hide');
                });
            } else {
                swal('提示信息', '保存失败！', 'error');
            }
        }, 'json');
    }

    //新增公告分类
    function addAoaNoticeCategory(){
        $("#editAoaNoticeCategoryForm :input").not(":button, :submit, :reset").val("").removeAttr("checked").remove("selected");
        $('#dialogAoaNoticeCategoryTitle').html('新增公告分类');
        $('#dialogAoaNoticeCategory').modal('show');
        $('#editAoaNoticeCategoryForm #categorySortNo').val('1');
    }

    //编辑公告分类
    function editAoaNoticeCategory(id){
        $('#dialogAoaNoticeCategoryTitle').html('编辑公告分类');
        $('#dialogAoaNoticeCategory').modal('show');
        $.post(ctx+UrlAdapterPrefix+'/aoa/notice/category/getAoaNoticeCategory.do', {id:id}, function(data){
        $('#editAoaNoticeCategoryForm #categoryId').val(data.categoryId);
        $('#editAoaNoticeCategoryForm #categoryOrgId').val(data.categoryOrgId);
        $('#editAoaNoticeCategoryForm #categoryName').val(data.categoryName);
        $('#editAoaNoticeCategoryForm #categorySortNo').val(data.categorySortNo);
        $('#editAoaNoticeCategoryForm #creater').val(data.creater);
        $('#editAoaNoticeCategoryForm #createTime').val(data.createTime);
        $('#editAoaNoticeCategoryForm #modifier').val(data.modifier);
        $('#editAoaNoticeCategoryForm #modifyTime').val(data.modifyTime);
        }, 'json');
    }

    //删除公告分类
    function deleteAoaNoticeCategory(id){
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
                    url: ctx+UrlAdapterPrefix+'/aoa/notice/category/deleteAoaNoticeCategoryById.do',
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
        location.href = ctx+UrlAdapterPrefix+'/aoa/notice/category/index.do?'+'keyword='+keyword;
    }
</script>
