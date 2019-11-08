var szDeviceIndex = '0';			//设备的编号；   0：文档摄像头;1：人像摄像头
var mainDevName = '主摄像头';
var secondDevName = '副摄像头';
var workDevName = '';				//当前打开的摄像头
var devInitResult = '0';   		//标识设备初始化是否成功，0 表示成功，1 表示失败
var mainPicData = '';				//主摄像头图片base64数据
var secondPicData = '';			//副摄像头图片数据
var Capture;						//必须得获取object对象
var ResSelect;						//必须取得分辨率标签对象
var count = 1;                      //照片旋转次数
var angle = 0;                      //旋转角度
var imgsToPdf = {};                 //存放转pdf文件的图片数据及参数
var uploadFile_formData;           //返回上传文件的formData对象，包含上传的文件
var photo_Info = {};                //由于IE下FormData取不出来数据。返回图片数据，key：图片名称 ；value：图片base64数据

//上传参数
var matinstIdFz;
var iteminstIdFz;
var inoutIdFz;

var fz_uploadCallBack = null;//执行上传的函数
// 初始化vue对象
var fzGaoPaiYiDia = new Vue({
  el: '#fzGaoPaiYiDia',
  data: function () {
    return {
      fzPaiZhaoVisible: false, // 是否展示方正高拍
      fzInnerVisible: false, // 照片预览
    }
  },
  mounted: function(){

    this.$nextTick(function() {
      //设置pdf的字体
      pdfMake.fonts = {
        Roboto: {
          normal: 'Roboto-Regular.ttf',
          bold: 'Roboto-Medium.ttf',
          italics: 'Roboto-Italic.ttf',
          bolditalics: 'Roboto-Italic.ttf'
        }
      };
      $('#fz_upload_photo_modal .modal-lg').css('max-width', 860);
      initOCX();
      //封装post表单提交图片数据实现预览
      $.extend({
        StandardPost:function(url,args){
          var body = $(document.body),
            form = $("<form method='post'></form>"),
            input;
          form.attr({"action":url});
          form.attr({"target":"_blank"});//新开窗口
          $.each(args,function(key,value){
            input = $("<input type='hidden'>");
            input.attr({"name":key});
            input.val(value);
            form.append(input);
          });
          form.appendTo(document.body);
          form.submit();
          document.body.removeChild(form[0]);
        }
      });
    })

  },
  methods: {
    // 关闭预览窗口
    closedBigImg: function(){
      var bigImg = document.getElementById('fz_bigImgPreView');
      bigImg.src = '';
      $('#dev').css('display','block');
    },
    //当拍照窗口关闭的时候
    closedUplodPhoto: function () {
      if(devInitResult == '0'){
        closeMainDev()
        closeSecondDev();
        imgsToPdf = {};
        // Capture.StopDevice('2');//关闭附加设备（第二人像设备）
      }
      console.log('释放设备...');
    }
  }
});

//打开拍照窗口
function fz_openPhotoWindow(matinstId, iteminstId, inoutId,callback_) {
    //上传参数
    matinstIdFz = matinstId;
    iteminstIdFz = iteminstId;
    inoutIdFz = inoutId;
    fz_uploadCallBack = callback_;
    // $('#fz_upload_photo_modal').modal('show');
    fzGaoPaiYiDia.fzPaiZhaoVisible = true;
    fzGaoPaiYiDia.$nextTick(function(){
        $('#fz_container').html('');//清空
        $('#mainDevBtn').html('打开主摄像头');
        $('#secondDevBtn').html('打开副摄像头');
        setTimeout(openMainDev, 0.3*1000);        //默认打开主摄像头
    });

}
//初始注册事件
$(function () {
    // //设置pdf的字体
    // pdfMake.fonts = {
    //   Roboto: {
    //     normal: 'Roboto-Regular.ttf',
    //     bold: 'Roboto-Medium.ttf',
    //     italics: 'Roboto-Italic.ttf',
    //     bolditalics: 'Roboto-Italic.ttf'
    //   }
    // };
    // $('#fz_upload_photo_modal .modal-lg').css('max-width', 860);
    // //当拍照窗口关闭的时候
    // $('#fz_upload_photo_modal').on('hidden.bs.modal', function () {
    //     if(devInitResult == '0'){
    //         closeMainDev()
    //         closeSecondDev();
    //         imgsToPdf = {};
    //         // Capture.StopDevice('2');//关闭附加设备（第二人像设备）
    //     }
    //     console.log('释放设备...');
    // });
    //当预览窗口关闭的时候
    // $('#fz_big_img_photo_modal').on('hidden.bs.modal', function () {
    //     var bigImg = document.getElementById('fz_bigImgPreView');
    //     bigImg.src = '';
    //     $('#dev').css('display','block');
    // });
    // initOCX();
    // //封装post表单提交图片数据实现预览
    // $.extend({
    //     StandardPost:function(url,args){
    //         var body = $(document.body),
    //             form = $("<form method='post'></form>"),
    //             input;
    //         form.attr({"action":url});
    //         form.attr({"target":"_blank"});//新开窗口
    //         $.each(args,function(key,value){
    //             input = $("<input type='hidden'>");
    //             input.attr({"name":key});
    //             input.val(value);
    //             form.append(input);
    //         });
    //         form.appendTo(document.body);
    //         form.submit();
    //         document.body.removeChild(form[0]);
    //     }
    // });
});

//打开主摄像头
function openMainDev(){
    catchInitFinishedMessage();
	if(devInitResult == '1'){
        alert('未检测到设备连接！');
        return false;
	}
    $('#fz_container').html('');//清空
    imgsToPdf = {};
    closeSecondDev();
	var val = $('#mainDevBtn').html();
	if (val == '打开主摄像头') {
		// Capture.StopDevice('2');//关闭附加设备（第二人像设备）
		if (Capture.StartDevice('0') == 0){
			workDevName = mainDevName;
            szDeviceIndex = '0';
            angle = 0;
            count = 1;
			$('#mainDevBtn').html('关闭主摄像头');
            $('#fz_dev_Name').html('（设备名：' + workDevName + '）');
			fillInRes();
		}
	} else if (val == '关闭主摄像头') {
        closeMainDev();
	}
}
//关闭主摄像头
function closeMainDev() {
    Capture.StopDevice('0');//关闭主摄像头
    workDevName = '';
    mainPicData = '';
    $('#mainDevBtn').html('打开主摄像头');
    $('#fz_dev_Name').html('（设备名：无）');
}
//打开副摄像头
function openSecondDev() {
    catchInitFinishedMessage();
    if(devInitResult == '1'){
        alert('未检测到设备连接！');
        return false;
    }
    closeMainDev();
    $('#fz_container').html('');//清空
    imgsToPdf = {};
    var val = $('#secondDevBtn').html();
    if (val == '打开副摄像头') {
        // Capture.StopDevice('2');//关闭附加设备（第二人像设备）
        if (Capture.StartDevice('1') == 0){
            workDevName = secondDevName;
            szDeviceIndex = '1';
            angle = 0;
            count = 1;
            $('#secondDevBtn').html('关闭副摄像头');
            $('#fz_dev_Name').html('（设备名：' + workDevName + '）');
            fillInRes();
        }
    } else if (val == '关闭副摄像头') {
        closeSecondDev();
    }
}
//关闭副摄像头
function closeSecondDev() {
    Capture.StopDevice('1');//关闭主摄像头
    workDevName = '';
    secondPicData = '';
    $('#secondDevBtn').html('打开副摄像头');
    $('#fz_dev_Name').html('（设备名：无）');
}
//读取分辨率列表
function fillInRes() {
	var iResVecLenth=ResSelect.options.length;
	for(var i=0;i<iResVecLenth;i++) {
		ResSelect.options.remove(i);	//清除分辨率select选项内容
    }
	var strResolutions = JSON.parse(Capture.GetResolution(szDeviceIndex));
     for (var i = 0; i < strResolutions.Resolution.length; i++) {
         var resopp = new Option(strResolutions.Resolution[i].toString(), i.toString());
         ResSelect.add(resopp);
     }
}
//设置分辨率
function setResIndex(value) {
    var iIndex = parseInt(value);
    var szResSel = ResSelect.options[iIndex].text.split("*");
    var iW = parseInt(szResSel[0]); //宽
    var iH = parseInt(szResSel[1]); //高
	if(Capture.SetResolution(szDeviceIndex, iW.toString(), iH.toString()) == 0) {
		console.log("设置分辨率成功,将重新打开设备");
	} else {
		alert("设置分辨率失败");
	}
}
//拍照
function captureToFile() {
    if (workDevName == ''){
        alert('请打开摄像头');
        return false;
	}
    var base64Data = "data:image/jpeg;base64," + Capture.EncodeBase64(szDeviceIndex); //base64字符串
	if(szDeviceIndex == '0'){
        mainPicData = base64Data;
	}else{
        secondPicData = base64Data;
	}
    fz_addImgDiv(base64Data);
}
//图片缩略图
function fz_addImgDiv(base64Data) {
    var idFlag = new Date().getTime();
    var fz_container = document.getElementById('fz_container');
    var newchild = document.createElement("div");
    newchild.setAttribute("style", "float:left");
    newchild.setAttribute("class", 'imgbox');
    newchild.setAttribute("id", 'imgbox_'+idFlag);
    newchild.innerHTML = '<div class="d-flex align-items-center">' +
        '<label class="m-checkbox m-checkbox--solid m-checkbox--brand">' +
        '<input name="photoCheck" imgId="'+idFlag+'" type="checkbox">' +
        '<span></span></label><img class="poto" id="'+idFlag+'" width="75" height="75" src="' + base64Data + '" onclick=fz_seeBigImg(this,\''+idFlag+'\');>' +
        '</div>' +
        '<input id="fz_picName_'+ idFlag +'" name="realFileName" value="' + idFlag + '" style="margin-left:24px;height: 20px;width: 74px;border: 1px solid #e8e8e8;"/>';
    fz_container.appendChild(newchild);
}
//缩略图图片点击 预览图片
function fz_seeBigImg(imageItem,idFlag) {
    // var src = imageItem.src;
    // var bigImg = document.getElementById('fz_bigImgPreView');
    // bigImg.setAttribute("width", 800);
    // bigImg.setAttribute("height", 600);
    // bigImg.src = src;
    // $('#fz_big_img_photo_modal .modal-lg').css('max-width', 860);
    // $('#fz_big_img_photo_modal').modal('show');
    // var picName = $('#fz_picName_' + idFlag).val().trim() + '.jpeg';
    // $('#fz_big_img_photo_modal_title').html(picName);
    // $('#dev').css('display','none');
    // fzGaoPaiYiDia.fzInnerVisible = true;
    //图片数据传回后台使用插件预览
    var base64data = imageItem.src;
    var picName = $('#fz_picName_' + idFlag).val().trim() + '.jpeg';
    var url =  ctx+'/file/showPicture.do';
    var data = {base64Data:base64data,fileName:picName};
    $.StandardPost(url,data);
}

//摄像旋转、角度选择为（0、90、180、270） 默认角度为0
function rotationDev(){
    if (workDevName == ''){
        alert('请打开摄像头');
        return false;
    }
    angle = 90 * count;
    count++;
    if(angle > 270){
        angle = 0;
        count = 1;
    }
    Capture.SetDeviceAngle(szDeviceIndex,angle);
}

//控件加载完成事件回调函数
function catchInitFinishedMessage() {
    try {
        Capture.ReleaseDevice();			  //释放设备
        //初始化，成功返回 0
        if (Capture.InitDevice() == 0) {
            devInitResult = '0'
        } else {
            devInitResult = '1'
        }
    }
    catch(err){
        //alert("未检测到设备或该浏览器不支持") // 可执行
    }

}
function initOCX(){
    try{
        Capture = document.getElementById("Capture");              //根据js的脚本内容，必须先获取object对象
        ResSelect = document.getElementById("Resolution_Select");//获取分辨率select标签的object对象
    }catch(err){
        alert("未找到ICaptureVideo.ocx控件，请重新安装");
    }
}

//照片转pdf
function imageTopdf() {
    if($('#fz_container').html() == ''){
        alert('请先拍照！');
        return false;
    }
    var imgs = [];
    var removeBoxId = [];
    var checkedImgList = $('#fz_container input:checkbox[name="photoCheck"]:checked');//获取勾选的图片
    if(checkedImgList.length == 0){
        if(confirm('确定将全部照片转为pdf文件吗?')){
            var allImg = $('#fz_container input:checkbox[name="photoCheck"]');
            allImg.each(function (index,ele) {
                var imgId = $(this).attr('imgId');
                if(imgId.indexOf('pdf_pic') == -1){
                    removeBoxId.push('imgbox_'+imgId);
                    var imgData = $('#' + imgId)[0].src;             //获取要转换的图片数据
                    imgs.push({
                        image:imgData,
                        fit: [500, 900],
                        margin:[10,10,10,10]
                    });
                }
            });
        }
    }else{
        if(confirm('确定将选中的照片转为pdf文件吗?')){
            checkedImgList.each(function (index,ele) {
                var imgId = $(this).attr('imgId');
                if(imgId.indexOf('pdf_pic') == -1){
                    removeBoxId.push('imgbox_'+imgId);
                    var imgData = $('#' + imgId)[0].src;
                    imgs.push({
                        image:imgData,
                        fit: [500, 900],
                        margin:[10,10,10,10]
                    });
                }
            });
        }
    }
    if(imgs.length > 0){
        var url = ctx + '/ui-static/aplanmis/gaopaiyi/pdf_1.png';
        var idFlag = new Date().getTime();
        var fz_container = document.getElementById('fz_container');
        var newchild = document.createElement("div");
        newchild.setAttribute("style", "float:left");
        newchild.setAttribute("class", 'imgbox');
        newchild.innerHTML = '<div class="d-flex align-items-center">' +
            '<label class="m-checkbox m-checkbox--solid m-checkbox--brand">' +
            '<input name="photoCheck" imgId="pdf_pic_'+ idFlag +'" type="checkbox">' +
            '<span></span></label><img class="poto" width="75" height="75" src="'+ url +'" onclick="openpdf(\''+ idFlag +'\');">' +
            '</div>' +
            '<input id="fz_pdfName_'+idFlag+'" name="realFileName" value="' + new Date().getTime() + '" style="margin-left:24px;height: 20px;width: 74px;border: 1px solid #e8e8e8;"/>';
        fz_container.appendChild(newchild);
        //删除已转换的图片
        for(var id in removeBoxId){
            $('#'+removeBoxId[id]).remove();
        }
        //暂存图片数据
        imgsToPdf['pdf_pic_'+idFlag] = imgs;
    }
}
//打开pdf
var pdfNameId;
function openpdf(idFlag) {
    pdfNameId = 'fz_pdfName_' + idFlag;
    var copyImgs = objDeepCopy(imgsToPdf['pdf_pic_'+idFlag]);
    var dd = {
        content: [
            // {text:'高拍仪上传文件照片',fontSize: 12,margin:[ 220, 0, 0, 0 ]},
            copyImgs
        ],
        defaultStyle: {
            // font: '方正兰亭细黑'
            font: 'Roboto'
        }
    };
    var pdfDoc = pdfMake.createPdf(dd);
    // pdfDoc.open();  // IE打不开。没有权限...
    pdfDoc.getBlob(openBlob);
}
//打开生成的pdf文件
function openBlob(blob) {
    var fileName = ($('#'+pdfNameId).val() == '')?new Date().getTime():$('#'+pdfNameId).val();
    // for IE
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob, fileName + '.pdf');
    }else {
        var pdfURL = window.URL.createObjectURL(blob);
        window.open(pdfURL,'_blank');
    }
}
//对象数组的深拷贝
function objDeepCopy(source) {
    var sourceCopy = source instanceof Array ? [] : {};
    for (var item in source) {
        sourceCopy[item] = typeof source[item] === 'object' ? objDeepCopy(source[item]) : source[item];
    }
    return sourceCopy;
}
//读取二代身份证信息
function fz_ReadIDCard() {
    try {
        catchInitFinishedMessage();
        var szPhotoFileName = 'C:\\IDCard.jpg';
        var IDCardJSONStr = Capture.ReadIDCard(szPhotoFileName);    //用于保存身份证头像的图片文件名
        //fz_deleteFile(szPhotoFileName)
        var IDCardObj = IDCardJSONStr==''?'':JSON.parse(IDCardJSONStr);
        return IDCardObj;
    }
    catch(err){
        alert("未检测到设备或浏览器不支持该设备")
    }


}
//删除
function fz_deleteFile(fileName) {
    var fso=new ActiveXObject("Scripting.FileSystemObject");
    if(fso.FileExists(fileName)) fso.DeleteFile(fileName);
}

//将base64转换为文件
// function base64toFile(data, filename) {
//     var arr = data.split(','), mime = arr[0].match(/:(.*?);/)[1],
//         bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
//     while(n--){
//         u8arr[n] = bstr.charCodeAt(n);
//     }
//     return new File([u8arr], filename, {type:mime});    //IE不支持File对象
// }
//base64转blob
function dataURItoBlob(base64Data) {
    var byteString;
    if(base64Data.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(base64Data.split(',')[1]);
    // else
    //     byteString = unescape(base64Data.split(',')[1]);
    var mimeString = base64Data.split(',')[0].split(':')[1].split(';')[0];
    var ia = new Uint8Array(byteString.length);
    for(var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ia], {
        type: mimeString
    });
}
//封装上传文件
var fz_pdfName_Id;  //pdf文件名称输入框id
function addPhotoFile(imgId,isPdf) {
    if(isPdf){
        var idFlag = imgId.substring(8);
        fz_pdfName_Id = 'fz_pdfName_' + idFlag;
        var copyImgs = objDeepCopy(imgsToPdf['pdf_pic_'+idFlag]);
        var dd = {
            content: [
                // {text:'高拍仪上传文件照片',fontSize: 12,margin:[ 220, 0, 0, 0 ]},
                copyImgs
            ],
            defaultStyle: {
                // font: '方正兰亭细黑'
                font: 'Roboto'
            }
        };
        var pdfDoc = pdfMake.createPdf(dd);
        pdfDoc.getBlob(function (blob) {
            var fileName = ($('#'+fz_pdfName_Id).val() == '')?new Date().getTime():$('#'+fz_pdfName_Id).val();
            uploadFile_formData.append("files", blob, fileName+'.pdf');
            uploadFile_Count++;
        });

    }else{
        var imgData = $('#' + imgId)[0].src;
        var fileName = $('#fz_picName_'+imgId).val().trim();
        uploadFile_formData.append('files', dataURItoBlob(imgData),fileName + '.jpeg');
        photo_Info[fileName + '.jpeg'] = imgData;
        uploadFile_Count++;
    }
}
//上传照片
var uploadFile_Count;    //上传的文件数量
function fz_upload_photo() {
    if($('#fz_container').html() == ''){
        alert('请拍照后上传！');
        return false;
    }
    uploadFile_formData = new FormData();
    photo_Info = {};
    uploadFile_Count = 0;
    var flag = true;
    var fileCount;
    var checkedImgList = $('#fz_container input:checkbox[name="photoCheck"]:checked');//获取勾选的图片
    if(checkedImgList.length == 0){
        if(confirm('确定上传全部文件吗?')){
            var allImg = $('#fz_container input:checkbox[name="photoCheck"]');
            fileCount = allImg.length;
            allImg.each(function (index,ele) {
                var imgId = $(this).attr('imgId');
                if(imgId.indexOf('pdf_pic') == -1){
                    //图片文件
                    addPhotoFile(imgId,false);
                }else{
                    //转换的pdf文件
                    addPhotoFile(imgId,true);
                }
                flag = false;
            });
        }
    }else{
        if(confirm('确定上传选中的文件吗?')){
            fileCount = checkedImgList.length;
            checkedImgList.each(function (index,ele) {
                var imgId = $(this).attr('imgId');
                if(imgId.indexOf('pdf_pic') == -1){
                    //图片文件
                    addPhotoFile(imgId,false);
                }else{
                    //转换的pdf文件
                    addPhotoFile(imgId,true);
                }
                flag = false;
            });
        }
    }
    if(flag)return false;
    var t = setInterval(function () {   //确定文件已经封装完毕
        if(fileCount == uploadFile_Count){
            clearInterval(t);           //清除定时器
            if(fz_uploadCallBack){
                fz_uploadCallBack(uploadFile_formData,photo_Info);
            }else{
                fz_ajaxUploadBase64File(uploadFile_formData,photo_Info);
            }
        }
    },100);
}
//ajax上传，该上传方法只是参照，实际上传要结合自身业务逻辑。
function fz_ajaxUploadBase64File(formData) {
    // formData.append('');
    var url = ctx + '/aea/business/uploadFile.do';
    $.ajax({
        url: url,
        type: "post",
        cache: false,
        data:formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (result) {
            if (result.success) {
                alert("上传成功");
            } else {
                alert("上传失败。" + result.message);
            }
        },
        error: function (msg) {
            alert("上传失败" + msg);
        }
    });
};
