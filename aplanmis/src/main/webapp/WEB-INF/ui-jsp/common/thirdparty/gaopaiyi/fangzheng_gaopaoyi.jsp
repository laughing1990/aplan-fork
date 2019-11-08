<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%--InitializationCompleted 是方正高拍仪控件的事件，表示控件加载完成--%>
<script type="text/javascript" FOR="Capture" EVENT="InitializationCompleted" >
    catchInitFinishedMessage();
</script>
<%--引入图片转pdf文件--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/pdfmake.js"></script>
<%--引入中文字体（方正兰亭细黑）文件--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/common/thirdparty/gaopaiyi/vfs_fonts.js"></script>--%>

<script type="text/javascript">
    $(function () {
        //设置pdf的字体
        pdfMake.fonts = {
            Roboto: {
                normal: 'Roboto-Regular.ttf',
                bold: 'Roboto-Medium.ttf',
                italics: 'Roboto-Italic.ttf',
                bolditalics: 'Roboto-Italic.ttf'
            }
            // ,
            // 方正兰亭细黑: {
            //     normal: '方正兰亭细黑GBK.ttf',
            //     bold: '方正兰亭细黑GBK.ttf',
            //     italics: '方正兰亭细黑GBK.ttf',
            //     bolditalics: '方正兰亭细黑GBK.ttf',
            // }
        };
    });
</script>

<style>
    .imgbox:hover{
        cursor:zoom-in;
        cursor:pointer;
    }
    .imgbox{
        margin-top:10px;
    }
    .poto{
        border-radius:4px;
    }
</style>
<%--拍照窗口--%>
<div id="fz_upload_photo_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="fz_upload_photo_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="fz_upload_photo_modal_title">
                    拍照窗口
                    <span id="fz_dev_Name">（设备名：无）</span>
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-6">
                        <button type="button" class="btn m-btn--square btn-info" id="mainDevBtn" onclick="openMainDev();">打开主摄像头</button>
                        <button type="button" class="btn m-btn--square btn-info" id="secondDevBtn" onclick="openSecondDev();">打开副摄像头</button>
                        <%--<button type="button" class="btn m-btn--square btn-info" onclick="closeDev();">关闭</button>--%>
                    </div>
                    <div class="col-6">
                        <button type="button" class="btn m-btn--square btn-info" onclick="captureToFile();">拍照</button>
                        <button type="button" class="btn m-btn--square btn-info" onclick="fz_upload_photo();">上传</button>
                        <button type="button" class="btn m-btn--square btn-info" onclick="rotationDev();">旋转</button>
                        <button type="button" class="btn m-btn--square btn-info" onclick="imageTopdf();">转为pdf</button>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;padding-bottom: 10px">
                    <div class="col-2" align="right">
                        设置分辨率：
                    </div>
                    <div class="col-8" align="left">
                        <select id="Resolution_Select" name="Resolution" style="width:20%;" onchange="setResIndex(Resolution_Select.value)">
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-2">
                        <div id="fz_container" style="height:500px;overflow-x:hidden;overflow-y:auto;"></div>
                    </div>
                    <div class="col-10">
                        <div style="width:99%;height:500px;" align="center" id="dev">
                            <object id="Capture" type="application/ZCaptureVideo" style ="width: 99%;height: 500px;border: 1px gray solid;"></object>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--照片预览窗口--%>
<div id="fz_big_img_photo_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="fz_big_img_photo_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header"  style="padding:1px; height: 25px;">
                <h5 class="modal-title" id="fz_big_img_photo_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: 0px;margin-right: 0px">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding-top: 2px;padding-bottom: 10px;padding-left: 0px;height:650px">
                <img src="" id="fz_bigImgPreView" style="margin-left:30px">
            </div>
        </div>
    </div>
</div>

