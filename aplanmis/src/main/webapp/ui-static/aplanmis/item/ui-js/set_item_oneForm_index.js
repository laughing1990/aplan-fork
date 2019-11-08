$(function() {

});

// 设置初始化校验
aeditItemOneformValidator = $("#aedit_item_oneform_form").validate({

    // 定义校验规则
    rules: {
        linkName: {
            required: true,
            maxlength: 200
        },
        sortNo: {
            required: true,
            maxlength: 10
        },
        isActive:{
            required: true,
        }
    },
    messages: {
        linkName: {
            required: '<font color="red">此项必填！</font>',
            maxlength: "长度不能超过200个字母!"
        },
        sortNo: {
            required: '<font color="red">此项必填,且为整数！</font>',
            maxlength: "长度不能超过10个字母!"
        },
        isActive:{
            required: '<font color="red">此项必填！</font>',
        }
    },
    // 提交表单
    submitHandler: function (form) {

        $("#uploadProgress").modal("show");
        $('#saveItemOneform').hide();
        $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

        var d = {};
        var t = $('#aedit_item_oneform_form').serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });

        $.ajax({
            url: ctx + '/aea/item/oneform/saveItemOneform.do',
            type: 'POST',
            data: d,
            async: false,
            success: function (result) {
                if (result.success) {

                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal({
                            type: 'success',
                            title: '保存成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        // 隐藏模式框
                        $('#saveItemOneform').show();
                        $('#aedit_item_oneform_modal').modal('hide');
                        // 列表数据重新加载

                    },500);
                } else {

                    setTimeout(function(){
                        $('#saveItemOneform').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                setTimeout(function(){
                    $('#saveItemOneform').show();
                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }
});

function importOneform(){


}