//BEGIN SETUP
var baseUrl;
var defaultPicUrl = ctx + '/ui-static/agcloud/framework/ui-themes/common/images/orther/default_view.png';//窗口默认显示图片
var socket;
//分辨率默认选择索引
var defaultIndex = 2;
//主、副摄像头拍照数据（base64）
var priPhotoData = "";
var subPhotoData = "";
//储存图片路径的变量和数组
var imgPath = "";
// var imgPathArray = new Array();

var priDevName;//主摄像头名称
var subDevName;//副摄像头名称
var picModel = 'YUY2';//视频图像模式，有MJPG和YUY2，默认YUY2。切换前先关闭视频传输！  有些设备MJPG不显示图像。坑！！！

//上传参数
var matinstId;
var iteminstId;
var inoutId;

var uploadCallBack = null;//执行上传的函数

// 初始化vue对象
var ltGaoPaiYiDia = new Vue({
  el: '#ltGaoPaiYiDia',
  data: function () {
    return {
      ltPaiZhaoVisible: false, // 是否展示良田高拍
      ltInnerVisible: false, // 照片预览
    }
  },
  mounted: function (){
    baseUrl = "ws://127.0.0.1:12345";
    openSocket();
    this.$nextTick(function() {
      $('#upload_photo_modal .modal-lg').css('max-width', 860);
      $('#bigSubDev').css('display', 'none');                  //隐藏副头窗口
    })
  },
  methods: {
    // 关闭预览窗口
    closedBigImg: function(){
      var bigImg = document.getElementById('bigImgPreView');
      bigImg.src = '';
    },
    //当拍照窗口关闭的时候
    closedUplodPhoto: function () {
      console.log('记得关闭视频...');
      closeDev();
    }
  }
});
//网页初始化函数
$(function () {
    // baseUrl = "ws://127.0.0.1:12345";
    // openSocket();
});
function openSocket() {
    socket = new WebSocket(baseUrl);
    socket.onclose = function () {
        console.error("web channel closed");
    };
    socket.onerror = function (error) {
        console.error("web channel error: " + error);
    };
    socket.onopen = function () {
        new QWebChannel(socket, function (channel) {
            window.dialog = channel.objects.dialog;
            window.onbeforeunload = function () {
                dialog.get_actionType("closeSignal");
            }
            //网页关闭函数
            window.onunload = function () {
                dialog.get_actionType("closeSignal");
            }
            //1.接收设备名
            dialog.send_devName.connect(function (message) {
                //判断摄像头类型（主/副），是否含有delete，如有"delete"代表设备拔出
                if (message.indexOf("primaryDev_") >= 0) priDevName = message.substr(11);
                if (message.indexOf("subDev_") >= 0) subDevName = message.substr(7); //副摄像头
                if (message.indexOf("delete") >= 0) {
                    //摄像头拔出
                    var msg = message.substr(6);
                    if (msg == priDevName) priDevName = '';
                    if (msg == subDevName) subDevName = '';
                }
            });
            //2.接收设备出图模式 //这个方法会被调用多次
            dialog.send_modelList.connect(function (message) {
                if (message.indexOf("primaryDev_") >= 0) {
                    //主摄像头
                    var msg = message.substr(11);
                    console.log('主图像模式：' + msg);
                }
                if (message.indexOf("subDev_") >= 0) {
                    //副摄像头
                    var msg = message.substr(7);
                    console.log('副图像模式：' + msg);
                }
            });
            //3.接收设备分辨率，若为空，则清空现有列表
            dialog.send_resolutionList.connect(function (message) {
                if (message.indexOf("primaryDev_") >= 0) {
                    //主摄像头
                    var msg = message.substr(11);
                    var select = document.getElementById("priResolutionList");
                    if (msg) {
                        select.add(new Option(msg));
                    } else {
                        select.options.length = 0;
                    }
                    //默认第三个分辨率
                    if (select.options.length > 3) {
                        select.selectedIndex = defaultIndex;
                    }
                }
                if (message.indexOf("subDev_") >= 0) {
                    //副摄像头
                    var msg2 = message.substr(7);
                    var select = document.getElementById("subResolutionList");
                    if (msg2) {
                        select.add(new Option(msg2));
                    } else {
                        select.options.length = 0;
                    }
                    //默认第三个分辨率
                    if (select.options.length > 3) {
                        select.selectedIndex = defaultIndex;
                    }
                }
            });
            //4.服务器返回消息
            dialog.sendPrintInfo.connect(function (message) {
                //图片本地保存后返回路径
                //格式：savePhoto_success:C:/Users/Administrator/Desktop/eloamPhoto/2018.06.25.19.32.31.jpg
                if (message.indexOf("savePhoto_success:") >= 0) {
                    imgPath = message.substr(18);
                    imgPath = imgPath + '?date=' + new Date().getTime();
                    console.log('图片路径： ' + imgPath);
                }
            });

            //5.接收图片流用来展示，多个，较小的base64，主头数据
            dialog.send_priImgData.connect(function (message) {
                var element = document.getElementById("bigPriDev");
                element.src = "data:image/jpg;base64," + message;
            });
            //6.接收图片流用来展示，多个，较小的base64，副头数据
            dialog.send_subImgData.connect(function (message) {
                var element = document.getElementById("bigSubDev");
                element.src = "data:image/jpg;base64," + message;
            });

            //主头分辨率列表点击，切换前先关闭视频传输
            document.getElementById("priResolutionList").onchange = function () {
                dialog.get_actionType("closePriVideo");//关闭视频传输
                var select = document.getElementById("priResolutionList");
                dialog.devChanged2("primaryDev_" + priDevName, select.value);//切换分辨率
                console.log('切换主摄像头分辨率');
            };
            //副头分辨率列表点击，切换前先关闭视频传输
            document.getElementById("subResolutionList").onchange = function () {
                dialog.get_actionType("closeSubVideo");
                var select = document.getElementById("subResolutionList");
                dialog.devChanged2("subDev_" + subDevName, select.value);
                console.log('切换副摄像头分辨率');
            };

            //7.拍照成功返回base64图片数据
            dialog.send_imaData3.connect(function (message) {
                // console.log(message);
                if (message.indexOf("primaryDev_") >= 0) {
                    var msg = message.substr(11);
                    var base64Data = "data:image/jpeg;base64," + msg;
                    priPhotoData = base64Data;
                    lt_addImgDiv(base64Data);
                }
                if (message.indexOf("subDev_") >= 0) {
                    var msg = message.substr(7);
                    var base64Data = "data:image/jpeg;base64," + msg;
                    subPhotoData = base64Data;
                    lt_addImgDiv(base64Data);
                }
            });

            //网页加载完成信号，发送到服务器。two表示两个预览窗口
            dialog.html_loaded("two");

            //设置本地默认保存路径D:\UploadFile       //如果文件夹不存在不会自动创建文件夹
            var picDefaultPath = 'set_savePath:D:\\UploadFile';
            dialog.set_configValue(picDefaultPath);
        });
    }
}
//增加图片缩略图
function lt_addImgDiv(base64Data) {
    $('#container').html('');//清空
    var container = document.getElementById('container');
    var newchild = document.createElement("div");
    newchild.setAttribute("style", "float:left");
    newchild.setAttribute("class", 'imgbox');
    newchild.innerHTML = '<img width="105" height="85" src="' + base64Data + '" onclick=lt_seeBigImg(this);>' +
        '<input id="picName" name="realFileName" value="' + new Date().getTime() + '" style="height: 20px;width: 105px"/>';
    container.appendChild(newchild);
}
//缩略图图片点击 预览图片
function lt_seeBigImg(imageItem) {
    var src = imageItem.src;
    var bigImg = document.getElementById('bigImgPreView');
    bigImg.setAttribute("width", 800);
    bigImg.setAttribute("height", 600);
    bigImg.src = src;
    $('#big_img_photo_modal .modal-lg').css('max-width', 860);
    // $('#big_img_photo_modal').modal('show');
    ltGaoPaiYiDia.ltInnerVisible = true
    var picName = $('#picName').val().trim() + '.jpeg';
    $('#big_img_photo_modal_title').html(picName);
}
//***************************************************************************************************************************************************
var workDevFlag = '';//当前打开的摄像头
$(function () {

    // $('#upload_photo_modal .modal-lg').css('max-width', 860);
    // $('#bigSubDev').css('display', 'none');                  //隐藏副头窗口
    //当拍照窗口关闭的时候
    // $('#upload_photo_modal').on('hidden.bs.modal', function () {
    //     console.log('记得关闭视频...');
    //     closeDev();
    // });
    //当预览窗口关闭的时候
    // $('#big_img_photo_modal').on('hidden.bs.modal', function () {
    //     var bigImg = document.getElementById('bigImgPreView');
    //     bigImg.src = '';
    // });
});

//设置尺寸
function changePriScanSize() {
    var select = document.getElementById("selScanSize");
    dialog.get_actionType(select.value);
}
//打开良田拍照窗口
function lt_openPhotoWindow(matinstId_, iteminstId_, inoutId_,callback_) {
    matinstId = matinstId_;
    iteminstId = iteminstId_;
    inoutId = inoutId_;
    uploadCallBack = callback_;
  // $('#upload_photo_modal').modal('show');
    // $('#upload_photo_modal_title').html('拍照窗口');
    ltGaoPaiYiDia.ltPaiZhaoVisible = true;
    ltGaoPaiYiDia.$nextTick(function(){
      $('#container').html('');//清空
      $('#priDevBtn').html('打开主摄像头');
      $('#subDevBtn').html('打开副摄像头');
      $('#bigPriDev').css('display', 'block');                         //打开主头窗口
      $('#priResolutionList').css('display', 'inline-block');        //显示主头分辨率
      $('#bigSubDev').css('display', 'none');                          //隐藏副头窗口
      $('#subResolutionList').css('display', 'none');                 //隐藏副头分辨率
      var select = document.getElementById("priResolutionList");
      if (select == null) {
        alert('未检测到设备连接！');
        return false;
      }
      if (select.options.length > 3) select.selectedIndex = defaultIndex;//默认第三个分辨率
      // document.getElementById("selScanSize").selectedIndex = 0;       //默认原始尺寸
      // $('#bigPriDev').attr('src',defaultPicUrl);                      //默认图片
      clickPriDev();                                                     //默认打开主摄像头
    });
}
//主摄像头开关按钮点击
function clickPriDev() {
    if (typeof(priDevName) == 'undefined') {
        alert('未检测到设备连接！');
        return false;
    }
    var val = $('#priDevBtn').html();
    if (val == '打开主摄像头') {
        openPriDev();
        $('#priDevBtn').html('关闭主摄像头');
        $('#subDevBtn').html('打开副摄像头');
    } else if (val == '关闭主摄像头') {
        closePriDev();
        $('#priDevBtn').html('打开主摄像头');
    }
}
//副摄像头开关按钮点击
function clickSubDev() {
    if (typeof(subDevName) == 'undefined') {
        alert('未检测到设备连接！');
        return false;
    }
    var val = $('#subDevBtn').html();
    if (val == '打开副摄像头') {
        openSubDev();
        $('#subDevBtn').html('关闭副摄像头');
        $('#priDevBtn').html('打开主摄像头');
    } else if (val == '关闭副摄像头') {
        closeSubDev();
        $('#subDevBtn').html('打开副摄像头');
    }
}
//打开主摄像头
function openPriDev() {
    $('#container').html('');//清空
    closeSubDev();                                                       //关闭副视频
    $('#bigPriDev').css('display', 'block');                         //打开主头窗口
    $('#priResolutionList').css('display', 'inline-block');                //显示主头分辨率

    $('#bigSubDev').css('display', 'none');                          //隐藏副头窗口
    $('#subResolutionList').css('display', 'none');                 //隐藏副头分辨率

    dialog.get_actionType("closePriVideo");
    dialog.devChanged2("primaryDev_" + priDevName, picModel);      //打开主视频
    workDevFlag = priDevName;
    $('#dev_Name').html('（设备名：' + priDevName + '）');
}
//打开副摄像头
function openSubDev() {
    $('#container').html('');//清空
    closePriDev();                                                       //关闭主视频
    $('#bigSubDev').css('display', 'block');                         //打开副头窗口
    $('#subResolutionList').css('display', 'inline-block');        //显示副头分辨率

    $('#bigPriDev').css('display', 'none');                          //隐藏主头窗口
    $('#priResolutionList').css('display', 'none');                 //隐藏主头分辨率

    dialog.get_actionType("closeSubVideo");
    dialog.devChanged2("subDev_" + subDevName, 'MJPG');          //打开副视频
    workDevFlag = subDevName;
    $('#dev_Name').html('（设备名：' + subDevName + '）');
}
//关闭
function closeDev() {
    closePriDev();//关闭主视频
    closeSubDev();//关闭副视频
}
//拍照
function takePicture() {
    if (workDevFlag == '') {
        alert('请打开摄像头');
    } else if (workDevFlag == priDevName) {        //主头拍照
        priDevPhoto();
    } else if (workDevFlag == subDevName) {        //副头拍照
        subDevPhoto();
    }
}
//关闭主视频
function closePriDev() {
    workDevFlag = '';
    priPhotoData = '';
    $('#container').html('');//清空
    dialog.get_actionType("closePriVideo");
    var priPic = document.getElementById("bigPriDev");
    priPic.src = defaultPicUrl;
    // $(priPic).attr('src',defaultPicUrl);
    console.log('关闭主视频完成');
    $('#dev_Name').html('（设备名：无）');
};
//关闭副视频
function closeSubDev() {
    workDevFlag = '';
    subPhotoData = '';
    $('#container').html('');//清空
    dialog.get_actionType("closeSubVideo");
    var subPic = document.getElementById("bigSubDev");
    subPic.src = defaultPicUrl;
    // $(subPic).attr('src',defaultPicUrl);
    console.log('关闭副视频完成');
    $('#dev_Name').html('（设备名：无）');
};

//主头拍照
function priDevPhoto() {
    dialog.photoBtnClicked("primaryDev_");      //拍照
    // dialog.get_actionType("savePhotoPriDev");   //保存到本地
}
//副头拍照
function subDevPhoto() {
    dialog.photoBtnClicked("subDev_");
    // dialog.get_actionType("savePhotosubDev");
}
//设备左转（主、副）
// function devMoveLeft() {
//     dialog.get_actionType("rotateLeft");
// };
//设备右转（主、副）
function devMoveRight() {
    if (workDevFlag == ''){
        alert('请打开摄像头');
        return false;
    }
    dialog.get_actionType("rotateRight");
};
//上传照片
function upload_photo() {
    var photoData = '';
    if (workDevFlag == priDevName) {
        photoData = priPhotoData;
        console.log('上传主头数据');
    } else if (workDevFlag == subDevName) {
        photoData = subPhotoData;
        console.log('上传副头数据');
    }
    if (photoData == '') {
        alert('请拍照后上传！');
        return false;
    }
    if(confirm('确定上传该照片吗?')){
        var realFileName = $('#picName').val().trim();
        if(uploadCallBack){
            uploadCallBack(photoData,realFileName);
        }else{
            ajaxUploadBase64File(photoData);
        }
    }
}

//ajax上传
function ajaxUploadBase64File(base64Data) {
    // var tableName = 'your_tableName';           //业务表名称
    // var pkName = 'your_pkName';                  //主键字段名称
    // var recordId = 'your_recordId';             //业务记录ID

    var realFileName = $('#picName').val().trim();           //文件真实名称，可不传
    // var url = ctx + '/gaopaiyi/imageUpload.do';
    var url = ctx + '/bpm/uploadAttachment.do';
    debugger;
    $.ajax({
        url: url,
        type: "post",
        cache: false,
        data: {
            base64Data: base64Data,
            matinstId: matinstId,
            iteminstId: iteminstId,
            inoutId: inoutId,
            realFileName: realFileName,
            GPY: true,//标识高拍仪上传
            isInput: true
        },
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                priPhotoData = '';
                subPhotoData = '';
                alert("上传成功");
            } else {
                alert("上传失败。" + result.message);
            }
        },
        error: function (msg) {
            console.log(msg);
            alert("上传失败" + msg);
        }
    });
};