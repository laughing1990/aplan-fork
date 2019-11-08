var vm = new Vue({
    el: '#qualTree',
    data: function () {
        return {
            selectedQuals: [], // 资质列表
            defaultProps: {
                children: 'children',
                label: 'majorName'
            }
        }
    }
});

function showQualificationTree(unitRequireId) {
    $('#qualMajorModal').modal('show');

    $.ajax({
        url: ctx + '/supermarket/purchase/getMajorTreeByUnitRequireId.do',
        type: 'POST',
        data: {'unitRequireId': unitRequireId},
        async: false,
        success: function (data) {
            if (data) {
                vm.selectedQuals = data;
            }

        },
        error: function () {
            swal('错误信息', "获取资质树失败！", 'error');
        }
    });
}

$(function () {
    $("#auditForm").validate({
        // 定义校验规则
        rules: {
            auditOpinion: {
                required: true,
                maxlength: 2000
            }
        },
        messages: {
            auditOpinion: {
                required: '此项必填!',
                maxlength: "长度不能超过2000个字母!"
            }
        },
        // 提交表单
        submitHandler: function (form) {
            $.ajax({
                url: ctx + '/supermarket/purchase/audit.do',
                type: 'POST',
                data: $(form).serialize(),
                async: false,
                success: function (data) {
                    if (data.success) {
                        swal({
                            title: '提示信息',
                            text: '审核成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        setTimeout(function () {
                            location.href = ctx + '/supermarket/purchase/index.do'
                        }, 1000);
                    } else {
                        swal('错误信息', "审核失败！", 'error');
                        // $('#auditModal').modal('hide');
                    }
                },
                error: function () {
                    swal('错误信息', "审核失败！", 'error');
                }
            });
        }
    });
});