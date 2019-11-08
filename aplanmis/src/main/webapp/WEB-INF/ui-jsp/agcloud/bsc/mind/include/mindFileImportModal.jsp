<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-left: 0px;
    }
    .modal-body input[type="file"]{
        position: absolute;
        width: 10%;
        opacity: 0;
    }
    .modal-body .custorm-style{
        display: block;
        width: 100%;
        height: 38px;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
    }
    .modal-body .custorm-style .left-button{
        width: 71px;
        font-size: 13px !important;
        height: 22px;
        line-height: 13px;
        float: left;
        border:1px solid #b1b1b1;
        background: linear-gradient(to bottom, #fff, #ccc);
        color: #444;
        margin-top: 0.9%;
        margin-left: 1%;
    }
    .modal-body .custorm-style .right-text{
        width: 80%;
        height: 99%;
        line-height: 2.5em;
        display: block;
        overflow: hidden;
    }
    .modal-content {
        margin-top:30%;
    }
</style>

<!-- 情形文件导入框 -->
<div id="uploadXmindFileModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="uploadXmindFileModalLable" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 700px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;height: 45px;">
                <h5 class="modal-title" id="uploadXmindFileModalLabel">通过Xmind文件导入情形</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top:0px;">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;height: 150px;">
                <form id="xmind" enctype="multipart/form-data" style="padding: 20px;">
                    <input id="uploadFile" type="file" name="file"/>
                    <span class="custorm-style">
                        <button class="left-button">选择文件</button>
                        <span class="right-text" id="rightText1">未选择任何文件</span>
                    </span>
                </form>
                <div style="font-size: large;color: red;padding-left: 2%;">*&nbsp;导入Xmind文件将替换已存在情形</div>
            </div>
            <div class="modal-footer"style="padding: 10px;height: 60px;">
                <button id="uploadXmindFileBtn" type="button" class="btn btn-primary">导入</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var fileBtn = $("input[type=file]");
    fileBtn.on("change", function(){

        var index = $(this).val().lastIndexOf("\\");
        var sFileName = $(this).val().substr((index+1));
        $(this).siblings('.custorm-style').find(".right-text").html(sFileName);
    });

    function uploadXmindFile(busiType,busiId){

        var formData = new FormData($("#xmind")[0]);
        formData.append("busiId", busiId);
        formData.append("busiType", busiType);
        formData.append("stateVerId",currentStateVerId);

        $("#uploadXmindFileModal").modal("hide");
        $("#uploadProgress").modal("show");
        $('#uploadProgressMsg').html("数据导入中, 请勿点击, 耐心等候...");

        $.ajax({
            url: ctx+"/rest/mind/importXmindFile.do",
            type: "POST",
            dataType: "json",
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if(result.success){

                    setTimeout(function(){

                        $("#uploadProgress").modal('hide');
                        swal({
                            title: '提示信息',
                            text: '导入成功',
                            type: 'info',
                            showCancelButton: false,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                        }).then(function(result) {
                            if (result.value) {
                                window.location.reload();
                            }
                        });
                    },500);

                }else{

                    setTimeout(function(){

                        $("#uploadProgress").modal("hide");
                        swal('错误信息', result.message, 'error');
                    },500);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                setTimeout(function(){

                    $("#uploadProgress").modal('hide');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                },500);
            }
        });
    }
</script>