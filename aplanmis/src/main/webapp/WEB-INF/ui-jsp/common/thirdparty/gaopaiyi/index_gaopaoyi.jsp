<%@ page language="java" contentType="text/html;charset=utf-8"%>
<style>
    .imgbox:hover{cursor:zoom-in}
</style>
<%--拍照窗口--%>
<div id="upload_photo_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="upload_photo_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="upload_photo_modal_title">
                    拍照窗口
                    <span id="dev_Name">（设备名：无）</span>
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-8">
                        <button type="button" class="btn m-btn--square btn-info" id="priDevBtn" onclick="clickPriDev();">打开主摄像头</button>
                        <button type="button" class="btn m-btn--square btn-info" id="subDevBtn" onclick="clickSubDev();">打开副摄像头</button>
                        <%--<button type="button" class="btn m-btn--square btn-info" onclick="closeDev();">关闭</button>--%>
                    </div>
                    <div class="col-4">
                        <button type="button" class="btn m-btn--square btn-info" onclick="takePicture();">拍照</button>
                        <button type="button" class="btn m-btn--square btn-info" onclick="upload_photo();">上传</button>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px">
                    <div class="col-4">设置主摄像头尺寸：&nbsp;&nbsp;
                        <select id="selScanSize" style="width: 90px" onchange="changePriScanSize();" >
                            <option selected="selected" value="selScanSizeori">原始尺寸</option>
                            <option value="selScanSizebig">较大尺寸</option>
                            <option value="selScanSizemid">中等尺寸</option>
                            <option value="selScanSizesmall">较小尺寸</option>
                        </select>
                    </div>
                    <div class="col-8" style="display:inline-block">
                        设置分辨率：
                        <select id="priResolutionList" style="width: 120px" ></select>
                        <select id="subResolutionList" style="width: 120px;display: none" ></select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-2">
                        <div id="container"></div>
                    </div>
                    <div class="col-10">
                        <img id="bigPriDev" style="width: 640px;height: 480px"/></img>
                        <img id="bigSubDev" style="width: 640px;height: 480px;"/></img>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--照片预览窗口--%>
<div id="big_img_photo_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="big_img_photo_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header"  style="padding:1px; height: 25px;">
                <h5 class="modal-title" id="big_img_photo_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: 0px;margin-right: 0px">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding-top: 2px;padding-bottom: 10px;padding-left: 0px">
                <img src="" id="bigImgPreView" style="margin-left:30px">
            </div>
        </div>
    </div>
</div>

