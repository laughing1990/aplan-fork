var item_basic_info_form_validator;
$(function () {

    initItemBasicInfoValidate();

});

function initItemBasicInfoValidate() {

    item_basic_info_form_validator = $("#item_basic_info_form").validate({
        // 提交表单
        submitHandler: function () {

            $("#uploadProgress").modal("show");
            $('#saveItemGuideBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            // $('#acceptCondition').val(replaceSpecialStr($('#acceptCondition').val()));
            // $('#fdblsxsm').val(replaceSpecialStr($('#fdblsxsm').val()));
            // $('#crbjsxsm').val(replaceSpecialStr($('#crbjsxsm').val()));
            // $('#wsbllc').val(replaceSpecialStr($('#wsbllc').val()));
            // $('#ckbllc').val(replaceSpecialStr($('#ckbllc').val()));

            var formData = new FormData(document.getElementById("item_basic_info_form"));
            $.ajax({
                url: ctx+'/aea/item/guide/saveOrUpdateBasicInfo.do',
                type: 'POST',
                data: formData,
                async: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success){

                        $("#item_basic_info_id").val(result.content.id);
                        clearGuideBasicFile();
                        if(result.content.ckbllctNum>0){
                            $('#ckbllctSampleDiv').show();
                            $('#ckbllctSampleButton').html(result.content.ckbllctNum + "个附件");
                        }else{
                            $('#ckbllctSampleDiv').hide();
                        }
                        if(result.content.wsbllctNum>0){
                            $('#wsbllctSampleDiv').show();
                            $('#wsbllctSampleButton').html(result.content.wsbllctNum + "个附件");
                        }else{
                            $('#wsbllctSampleDiv').hide();
                        }
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            $('#saveItemGuideBtn').show();
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                        },500);
                    } else {

                        setTimeout(function(){

                            $('#saveItemGuideBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#saveItemGuideBtn').show();
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

function replaceSpecialStr(specialStr){

    if(specialStr){
        // specialStr = specialStr.replace(/(\n)/g, "");
        specialStr = specialStr.replace(/(\t)/g, "");
        // specialStr = specialStr.replace(/(\r)/g, "");
        specialStr = specialStr.replace(/<\/?[^>]*>/g, "");
        specialStr = specialStr.replace(/\s*/g, "");
    }
    return specialStr;
}