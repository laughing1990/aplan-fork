<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__head .m-datatable__row > .m-datatable__cell {
        padding: 5px;
    }

    .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__head .m-datatable__row > .m-datatable__cell, .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__body .m-datatable__row > .m-datatable__cell, .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__foot .m-datatable__row > .m-datatable__cell {
        padding: 5px;
    }

    .m-datatable.m-datatable--default > .m-datatable__pager {
        margin: 5px;
    }

    .m-datatable.m-datatable--default > .m-datatable__pager > .m-datatable__pager-info {
        margin: 5px 20px;
    }

    .m-datatable.m-datatable--default > .m-datatable__pager > .m-datatable__pager-nav {
        float: left;
        margin: 5px 20px;
    }

    .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__head .m-datatable__row > .m-datatable__cell > span, .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__body .m-datatable__row > .m-datatable__cell > span, .m-datatable.m-datatable--default > .m-datatable__table > .m-datatable__foot .m-datatable__row > .m-datatable__cell > span {
        display: inline-table;
        width: 100%;
    }

    .modal-header .close {
        padding: 1rem;
        margin: 0rem 0rem 0rem auto;
    }

</style>

<div id="add_cert_type_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_cert_type_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;padding-left: 10px;height: 45px;">
                <h5 class="modal-title" id="add_cert_type_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_cert_type_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <input type="hidden" name="certTypeId" value=""/>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;类型名称：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="typeName" value=""  placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;类型编号：</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control m-input" name="typeCode" value=""  placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">颁发单位:</label>
                        <div class="col-lg-4 input-group">
                            <input type="hidden" name="issueOrgId" value=""/>
                            <input type="text" class="form-control m-input" name="issueOrgName" placeholder="请点击选择" aria-describedby="select_att_dir" readonly>
                            <div class="input-group-append">
                                <span id="select_att_dir" class="input-group-text" onclick="javascript:selectOpusOmOrgZtree();">
                                    <i class="la la-group"></i>
                                </span>
                            </div>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;">持证者类型:</label>
                        <div class="col-lg-4">
                            <select type="text" class="form-control" name="holderType" value="">
                                <option value="">请选择</option>
                                <c:forEach items="${certHolderTypes}" var="certHolderType">
                                    <option value="${certHolderType.itemCode}">${certHolderType.itemName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限计量:</label>
                        <div class="col-lg-4">
                            <input type="number" class="form-control m-input" name="dueNum" value="1"/>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限单位:</label>
                        <div class="col-lg-4">
                            <select type="text" class="form-control" name="dueUnit" value="">
                                <option value="">请选择</option>
                                <c:forEach items="${dueUnits}" var="dueUnit">
                                    <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;排序:</label>
                        <div class="col-lg-4">
                            <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                        </div>

                        <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>&nbsp;是否有效:</label>
                        <div class="col-lg-4">
                            <select type="text" class="form-control" name="isActive" value="">
                                <option value="">请选择</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="typeMemo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info">保存</button>
                    <button id="closeAddCertTypeBtn" type="button" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">

    var addCertTypeValidator = null;
    $(function(){

        addCertTypeValidator = $('#add_cert_type_form').validate({
            // 定义校验规则
            rules: {
                typeName: {
                    required: true,
                    maxlength: 100
                },
                typeCode:{
                    required: true,
                    maxlength: 50,
                    remote: {
                        url: ctx+'/aea/cert/type/checkUniqueCertTypeCode.do', //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "json",           //接受数据格式
                        data: {   //要传递的数据
                            certTypeId: function(){
                                return $("#add_cert_type_form input[name='certTypeId']").val();
                            },
                            typeCode: function() {
                                return $("#add_cert_type_form input[name='typeCode']").val();
                            }
                        }
                    }
                },
                holderType: {
                    maxlength: 50
                },
                typeMemo: {
                    maxlength: 500
                },
            },
            messages: {
                typeName: {
                    required: '此项必填!',
                    maxlength: "长度不能超过100个字母!"
                },
                typeCode:{
                    required: '此项必填!',
                    maxlength: "长度不能超过50个字母!",
                    remote: "编号已存在！",
                },
                holderType: {
                    maxlength: "长度不能超过50个字母!"
                },
                typeMemo: {
                    maxlength: "长度不能超过500个字母!"
                },
            },
            // 提交表单
            submitHandler: function(form){

                var d = {};
                var t = $('#add_cert_type_form').serializeArray();
                $.each(t, function() {
                    d[this.name] = this.value;
                });
                $.ajax({
                    url: ctx + '/aea/cert/type/saveAeaCertType.do',
                    type: 'POST',
                    data: d,
                    async: false,
                    success: function (result) {
                        if (result.success){
                            $('#add_cert_type_modal').modal('hide');
                            location.reload();
                        }else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', "保存类型信息失败！", 'error');
                    }
                });
            }
        });

        $('#closeAddCertTypeBtn').click(function(){
            $('#add_cert_type_modal').modal('hide');
            addCertTypeValidator.resetForm();
        });

        $('#selectOrgBtn').click(function(){

            var opusOmOrgTree = $.fn.zTree.getZTreeObj("opusOmOrgTree");
            var nodes = opusOmOrgTree.getSelectedNodes();
            if(nodes!=null&&nodes.length>0){
                var selectNode = nodes[0];
                $("#add_cert_type_form input[name='issueOrgId']").val(selectNode.id);
                $("#add_cert_type_form input[name='issueOrgName']").val(selectNode.name);
                closeselectOpusOmOrgZtree();
            }else{
                swal('提示信息', '请选择机构！', 'info');
            }
        });

    });

    // 新增分类
    function addCertType(title){

        $('#add_cert_type_modal').modal('show');
        $('#add_cert_type_modal_title').html(title);
        $('#add_cert_type_form')[0].reset();
        addCertTypeValidator.resetForm();

        $("#add_cert_type_form select[name='dueUnit'] option:eq(1)").prop("selected", 'selected');
        $("#add_cert_type_form select[name='holderType'] option:eq(1)").prop("selected", 'selected');
        $("#add_cert_type_form select[name='isActive'] option:eq(1)").prop("selected", 'selected');

        $.ajax({
            url: ctx + '/aea/cert/type/getMaxSortNo.do',
            type: 'POST',
            data: {},
            async: false,
            success: function (data) {
                $("#add_cert_type_form input[name='sortNo']").val(data);
            },
            error:function(){
                $("#add_cert_type_form input[name='sortNo']").val(1);
            }
        });
    }

    // 编辑分类
    function editCertType(title,certTypeId){

        $('#add_cert_type_modal').modal('show');
        $('#add_cert_type_modal_title').html(title);
        $.ajax({
            url: ctx+'/aea/cert/type/getAeaCertType.do',
            type: 'POST',
            data: {'id': certTypeId},
            success: function (data) {
                if(data){
                    loadFormData(true,'#add_cert_type_form',data);
                }
            },
            error:function(){
                swal('错误信息', '获取证照分类信息出错！', 'error');
            }
        });
    }

    // 删除分类
    function deleteCertType(certTypeId,count){

        if(count>0){
            swal('提示信息', '证照分类下存在证照数据，请先删除证照数据信息！', 'info');
        }else{
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
                        url: ctx+'/aea/cert/type/deleteAeaCertTypeById.do',
                        type: 'POST',
                        data: {'id': certTypeId},
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
    }
</script>