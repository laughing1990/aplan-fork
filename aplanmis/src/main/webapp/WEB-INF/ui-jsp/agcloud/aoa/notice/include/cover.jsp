<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/ui-static/agcloud/aoa/notice/cropper.css">
    <script src="${pageContext.request.contextPath}/ui-static/agcloud/aoa/notice/cropper.js"></script>

<div class="container">
    <label class="label" data-toggle="tooltip" title="修改封面">
        <img class="rounded" id="avatarImageView" src="" alt="avatar" style="width:150px;height:150px;">
        <input type="file" class="sr-only" id="imageFile" name="avatar_file" accept="image/*">
    </label>
    <div class="progress">
        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="0"
             aria-valuemin="0" aria-valuemax="100">0%
        </div>
    </div>
    <div class="alert" role="alert"></div>
    <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalLabel">截取图片</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="img-container">
                        <img id="imageInModal"
                             src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/notice/images/3456749.jpg">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btnCrop">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .label {
        cursor: pointer;
    }
    .progress {
        display: none;
        margin-bottom: 1rem;
    }
    .alert {
        display: none;
    }
    .img-container img {
        max-width: 100%;
    }
    /*added*/
	.container{
        padding-left:0px;
        margin-left:0px;
    }
</style>

<script>
    window.addEventListener('DOMContentLoaded', function () {
        var avatarImageView = document.getElementById('avatarImageView');
        var imageInModal = document.getElementById('imageInModal');
        var imageFile = document.getElementById('imageFile');
        var $progress = $('.progress');
        var $progressBar = $('.progress-bar');
        var $alert = $('.alert');
        var $modal = $('#modal');
        var cropper;

        $('[data-toggle="tooltip"]').tooltip();
        imageFile.addEventListener('change', function (e) {
            var files = e.target.files;
            var done = function (url) {
                imageFile.value = '';
                imageInModal.src = url;
                $alert.hide();
                $modal.modal('show');
            };
            var reader;
            var file;
            var url;

            if (files && files.length > 0) {
                file = files[0];
                if (URL) {
                    done(URL.createObjectURL(file));
                } else if (FileReader) {
                    reader = new FileReader();
                    reader.onload = function (e) {
                        done(reader.result);
                    };
                    reader.readAsDataURL(file);
                }
            }
        });

        $modal.on('shown.bs.modal', function () {
            cropper = new Cropper(imageInModal, {
                //指定图像纵横比
                // aspectRatio: 1,
                viewMode: 3,
                autoCropArea:1
            });
        }).on('hidden.bs.modal', function () {
            cropper.destroy();
            cropper = null;
        });

        document.getElementById('btnCrop').addEventListener('click', function () {
            var initialAvatarURL;
            var canvas;
            $modal.modal('hide');

            if (cropper) {
                canvas = cropper.getCroppedCanvas({
                    width: cropper.cropBoxData.width,
                    height:cropper.cropBoxData.height,
                });

                initialAvatarURL = avatarImageView.src;
                avatarImageView.src = canvas.toDataURL();
                $progress.show();
                $alert.removeClass('alert-success alert-warning');

                var json = [
                    '{"x":' + 0,
                    '"y":' + 0,
                    '"height":' + cropper.cropBoxData.height,
                    '"width":' + cropper.cropBoxData.width,
                    '"rotate":' + 1 + '}'
                ].join();
                canvas.toBlob(function (blob) {
                    var formData = new FormData();
                    // formData.append('avatarImageView', blob);
                    formData.append('avatar_file', blob);
                    formData.append('avatar_data', json);

                    formData.append('tableName', CurrentDataCropper.tableName);
                    formData.append('pkName', CurrentDataCropper.pkName);
                    formData.append('recordId', CurrentDataCropper.recordId);
                    var url =Url_Cropper;
                    $.ajax(url, {
                        method: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false,

                        xhr: function () {
                            var xhr = new XMLHttpRequest();

                            xhr.upload.onprogress = function (e) {
                                var percent = '0';
                                var percentage = '0%';

                                if (e.lengthComputable) {
                                    percent = Math.round((e.loaded / e.total) * 100);
                                    percentage = percent + '%';
                                    $progressBar.width(percentage).attr('aria-valuenow', percent).text(percentage);
                                }
                            };

                            return xhr;
                        },
                        success: function () {
                            // $alert.show().addClass('alert-success').text('上传成功');
                        },
                        error: function () {
                            avatarImageView.src = initialAvatarURL;
                            $alert.show().addClass('alert-warning').text('上传出错');
                        },
                        complete: function () {
                            $progress.hide();
                        },
                    });
                });
            }
        });
    });
</script>

<script>
    var ctx = '${pageContext.request.contextPath}';
    var Url_Cropper= ctx+'/bsc/att/cropper.do';
    var Url_ExistCroppper=ctx+'/bsc/att/listAttLinkAndDetailByTablePKRecordId.do';

    function getUrlViewCroppper(dataCropper) {
        var result=ctx + '/bsc/att/downloadCropImg.do?tableName=' + dataCropper.tableName + '&pkName=' + dataCropper.pkName + '&recordId=' + dataCropper.recordId;
        return result;
    }

    function setCropperImg(dataCropper) {
        var viewImgUrl;
        $.ajax({
            type: "post",
            url: Url_ExistCroppper,
            data: dataCropper,
            success: function (result) {
                if (result && result.length > 0) {
                    viewImgUrl = getUrlViewCroppper(dataCropper) + "&time=" + new Date();
                } else {

                }
                //设置用户头像地址
                $("#avatarImageView").attr('src', viewImgUrl);
            }
        })
    }
    var CurrentDataCropper = {};
    $(document).ready(function () {

    })
</script>
