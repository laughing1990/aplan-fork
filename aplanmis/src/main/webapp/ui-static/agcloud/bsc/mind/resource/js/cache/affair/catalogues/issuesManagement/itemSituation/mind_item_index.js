$(function(){
    var itemDetailFormValidator = null;

    initItemDetailForm();

    $("#fbsj").datetimepicker({
        format: 'yyyy-mm-dd',
        language:  'zh',
        autoclose:true,
        minView:2
    });
    $("#sssj").datetimepicker({
        format: 'yyyy-mm-dd',
        language:  'zh',
        autoclose:true,
        minView:2
    });

    function initItemDetailForm() {
        if(itemDetailFormValidator != null)itemDetailFormValidator.resetForm();
        // 设置页面元素表单初始化校验
        itemDetailFormValidator = $("#itemDetailForm").validate({
            // 定义校验规则
            rules: {
                orgName: {
                    required: true,
                },
                bbh: {
                    required: true,
                },
                itemCode: {
                    required: true
                },
                wtbm: {
                    required: true
                },
                itemName: {
                    required: true
                },
                standardItemCode: {
                    required: true
                },
                isNeedState: {
                    required: true
                }
            },
            messages: {
                orgName: {
                    required: "该输入项为必输项！"
                },
                bbh: {
                    required: "该输入项为必输项！"
                },
                itemCode: {
                    required: "该输入项为必输项！"
                },
                wtbm: {
                    required: "该输入项为必输项！"
                },
                itemName: {
                    required: "该输入项为必输项！"
                },
                standardItemCode: {
                    required: "该输入项为必输项！"
                },
                isNeedState: {
                    required: "该输入项为必输项！"
                }
            },
            invalidHandler: function(event, validator) {
                mApp.scrollTo("#itemDetailForm");
            },
            // 提交表单
            submitHandler: function(form){
                var form = new FormData(document.getElementById("itemDetailForm"));
                var saveUrl = ctx+"";
                // 异步保存
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: saveUrl,
                    data: form,
                    dataType: 'json',
                    processData: false,
                    // contentType: "application/x-www-form-urlencoded",
                    contentType: false,
                    success: function (result) {
                        if (result.success) {
                            swal('提示', '保存成功！', 'info');
                        };
                    },
                    error:function(){
                        swal('错误信息', '保存失败,服务器异常！', 'error');
                    }
                });
            }
        });
    }
});


