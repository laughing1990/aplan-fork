var item_extend_info_form_validator;
$(function () {

    initItemExtendInfoValidate();

});

function initItemExtendInfoValidate() {

    item_extend_info_form_validator = $("#item_extend_info_form").validate({
        // 提交表单
        submitHandler: function () {

            $("#uploadProgress").modal("show");
            $('#saveItemGuideExtendBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var formData = new FormData(document.getElementById("item_extend_info_form"));
            $.ajax({
                url: ctx+'/aea/item/guide/saveOrUpdateExtendInfo.do',
                type: 'POST',
                dataType: 'json',
                data: formData,
                async: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success){

                        $("#item_extend_info_id").val(result.content.id);
                        clearGuideExtendFile();

                        if(result.content.zzzResultGuidNum>0){

                            $('#resultSampleDiv').show();
                            $('#resultSampleButton').html(result.content.zzzResultGuidNum + "个附件");
                        }else{

                            $('#resultSampleDiv').hide();
                        }

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            $('#saveItemGuideExtendBtn').show();
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                        },500);
                    } else {

                        setTimeout(function(){

                            $('#saveItemGuideExtendBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#saveItemGuideExtendBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function loadCheckboxText(inputTextId,checkboxName) {

    var arr = [];
    $("input[name='"+checkboxName+"']").each(function(i){
        if($(this).is(':checked') == true){
            arr.push($(this).attr("title"));
        }
    });
    $("#"+inputTextId).val(arr.join(","));
}

function loadCheckbox(value,checkboxName) {

    if(value && value!=''){
        var arr = value.split(",");
        $("input[name='"+checkboxName+"']").each(function(i){

            for ( var j = 0; j <arr.length; j++){
                if($(this).val()==arr[j]){
                    $(this).prop("checked",true);
                }
            }
        });
    }
}