var userAgent = navigator.userAgent; //用于判断浏览器类型
var defaultImg = ctx +"/ui-static/aplanmis/util/images/file-common.png";//默认文件图片
var matbody = $("#item_mat_add");//材料新增编辑面板
var isNewMat = true;//是否新增材料

$(document).ready(function(){
    initFile("file_0");
    initFile("file_1");
    initFile("file_2");
});

var templateDocArr = new Array();//docType='0'
var sampleDocArr = new Array();//docType='1'
var reviewSampleDocArr = new Array();//docType='2'

var imgCount = 0;


/**
 * 上传控件监听
 * @param file
 */
function initFile(file) {

    $("#"+file).change(function() {
        //获取选择图片的对象
        var docObj = $(this)[0];
        //获取图片列
        var picDiv = $("#"+file).parents(".fileupload-opbtns").children(".uploader-list");
        //得到所有的图片文件
        var fileList = docObj.files;
        var same = false;
        if (fileList && fileList.length>0) {
            // for (var i = 0; i < fileList.length; i++) {
            for (var i = fileList.length-1; i >=0; i--) {
                //不允许同名
                if(checkSameFile(getDocType(file),fileList[i].name)){
                    same = true;
                    continue;
                }

                //显示上传按钮
                picDiv.prev().css("display","inline-block");

                imgCount++;

                var index = imgCount;

                var picHtml = ' <div class="file-item thumbnail" title="'+fileList[i].name+'"  onmouseover = "$(this).find(\'.file-panel\').height(30);"  onmouseout = "$(this).find(\'.file-panel\').height(0);" >'
                    + '<div class="info">'+fileList[i].name+'</div>'
                    // + '<img id="img'+i+'" class="noImgThumb"   onerror="this.src=\''+defaultImg+'\'" >'
                    + '<img id="'+file+'_img_new_'+index+'" class="noImgThumb" >'
                    // + '<span class="upload-state-done success"></span>'
                    + '<span class="success"></span>'
                    + '<div class="file-panel" style="height: 0px;">'
                    // + '<span class="cancel" style = "background: url(${pageContext.request.contextPath}/ui-static/aplanmis/util/images/icons.png) no-repeat;background-position: -48px -24px;"   onclick="delImg(this);">删除</span>'
                    + '<span class="cancel"    onclick="delImg(this);">删除</span>'
                    +  '</div>'
                    + '</div>';

                picDiv.append(picHtml);
                // picDiv.prepend(picHtml);

                //获取图片imgi的对象
                var imgObjPreview = document.getElementById(file+"_img_new_" + index);
                if (fileList && fileList[i]) {
                    //图片属性
                    imgObjPreview.style.display = 'block';
                    imgObjPreview.style.width = '110px';
                    imgObjPreview.style.height = '110px';
                    //imgObjPreview.src = docObj.files[0].getAsDataURL();
                    //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要以下方式
                    var uploadData = null;

                    if (userAgent.indexOf('MSIE') == -1) {

                        if(fileList[i].type.indexOf("image") != -1){
                            //IE以外浏览器
                            imgObjPreview.src = URL.createObjectURL(docObj.files[i]); //获取上传图片文件的物理路径;
                            $("#"+file+"_img_new_" + index).attr("rel",imgObjPreview.src);
                            $("#"+file+"_img_new_" + index).imagezoom();
                        }else{
                            imgObjPreview.src = defaultImg;
                        }

                        uploadData = docObj.files[i];


                    } else {

                        if(fileList[i].type.indexOf("image") != -1){
                            //IE以外浏览器
                            if (docObj.value.indexOf(",") != -1) {
                                var srcArr = docObj.value.split(",");
                                imgObjPreview.src = srcArr[i];
                                uploadData = srcArr[i];
                            } else {
                                imgObjPreview.src = docObj.value;
                                uploadData = docObj.value;
                            }
                            $("#"+file+"_img_new_" + index).attr("rel",imgObjPreview.src);
                            $("#"+file+"_img_new_" + index).imagezoom({body:matbody});
                        }else{
                            imgObjPreview.src = defaultImg;
                        }

                    }

                    var data = {};
                    data.name = fileList[i].name;
                    data.type = fileList[i].type;
                    data.data = uploadData;
                    data.docType = getDocType(file);
                    data.id = file+"_img_new_" + index;
                    data.tag = 'new';
                    data.attType = '';
                    pushArr(data.docType,data);
                }
            }


        }else{
            // picDiv.prev().css("display","none");
        }

        clearDocFile($("#"+file));

        if(same){
            alert("选中的文件有同名的");
        }
    });
}

/**
 * 检查是否有重名文件
 * @param docType
 * @param name
 * @returns {boolean}
 */
function checkSameFile(docType,name){
    var arr = getDataArr(docType);
    if(arr!=null && arr.length>0){
        for(var i =0;i<arr.length;i++){
            var v = arr[i];
            if(v.name == name){
                return true;
            }
        }
    }

    return false;
}

function pushArr(docType,data){

    getDataArr(docType).push(data);
}

function getDocType(file){

    if(file.indexOf("0") != -1){
        return "0";
    }else if(file.indexOf("1") != -1){
        return "1";
    }else if(file.indexOf("2") != -1){
        return "2";
    }
}

function getDataArr(docType){

    if(docType == '0'){
        return templateDocArr;
    }else if(docType == '1'){
        return sampleDocArr;
    }else if(docType == '2'){
        return reviewSampleDocArr;
    }
}


function delImg(cancelBtn) {

    var _this = $(cancelBtn);

    var msg = '此操作将删除附件,确定要删除吗？';
    swal({
        title: '',
        text: msg,
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {

            var fileupload_opbtns = _this.parents(".fileupload-opbtns");

            //上传控件
            var file = fileupload_opbtns.find(".webuploader-element-invisible");

            var docType = getDocType(file.attr('id'));

            if(removeArrDataById(docType,_this.parents(".thumbnail").find('img').attr('id'))) {

                //删除图片
                _this.parents(".thumbnail").remove();

                clearDocFile(file);

                // if(fileupload_opbtns.find(".uploader-list").children().length==0){
                //     fileupload_opbtns.children(".uploadBtn").css("display","none");
                // }

                var arr = getDataArr(docType);
                if (arr == null || arr.length == 0) {
                    fileupload_opbtns.children(".uploadBtn").css("display", "none");
                } else {
                    var newCount = 0;
                    for (var i = 0; i < arr.length; i++) {
                        var v = arr[i];
                        if (v.tag == 'new') {
                            newCount++;
                        }
                    }

                    if (newCount == 0) {
                        fileupload_opbtns.children(".uploadBtn").css("display", "none");
                    }
                }
            }
        }
    });
}

function removeArrDataById(docType,id){

    var arr = getDataArr(docType);
    if(arr!=null && arr.length>0){
        for(var i =0;i<arr.length;i++){
            var v = arr[i];

            if(v.id == id){

                if(v.attType=='KP'){
                    alert("同步数据不允许删除");
                    return false;
                }


                if(v.tag=='new'){
                    arr.splice(i,1);
                    return true;
                }else{
                    var delSuccess = false;
                    $.ajax({
                        type: 'post',
                        url: ctx + '/aea/item/mat/'+'deleteGlobalMatDoc.do',
                        dataType: 'json',
                        data: {'detailId': v.data, 'type': docType,'matId': $("#matId").val()},
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                delSuccess = true;
                            } else {
                                swal('错误信息','删除失败','error');
                            }
                        }
                    });

                    if(delSuccess) {
                        arr.splice(i, 1);
                    }

                    return delSuccess;

                }
            }
        }
    }
}

function clearDocFile(file) {

    var id = file.attr('id');
    file.after(file.clone().val(""));
    file.remove();

    initFile(id);
}


function upload(docType,uploadBtn) {
    var _upload = $(uploadBtn);
    var arr = getDataArr(docType);
    if(arr!=null && arr.length>0){
        var formData = new FormData();
        formData.append("matId", $("#matId").val());
        formData.append("docType", docType);
        for(var i =0;i<arr.length;i++){
            var v = arr[i];
            if(v.tag=='new') {
                formData.append("file", v.data);
            }
        }

        $.ajax({
            url: ctx + '/test/uploadMatAtt.do',
            method: 'post',
            processData: false,
            contentType: false,
            async: false,
            data: formData,
            success: function (result) {
                if(result.success) {
                    if (result.content.length > 0) {
                        for (var i = 0; i < result.content.length; i++) {
                            var v = result.content[i];
                            var d = $("[title='" + v.name + "']");
                            if(d && d.length>0) {
                                for(var j=0;j<d.length;j++){
                                    var id = $(d.eq(j)).find('img').attr("id");

                                    if(id && id.indexOf("new") != -1){
                                        var startId = id.substring(0,6);
                                        if(getDocType(startId)==v.docType){
                                            changEdit(v.docType,id);
                                            d.find(".success").addClass("upload-state-done");
                                            d.find(".success").click(function () {
                                                // window.location.href = ctx+'/test/download.do?detailId=' + v.doc;
                                                showAttachment(v.detailId,v.attType,v.type);
                                                // showImgFile(v.detailId);
                                            });
                                        }
                                    }
                                }
                            }


                        }
                    }

                    _upload.css("display", "none");
                }
            }
        });
    }
}


function initDocData() {
    clearDocArr();
    getMatAtt();

}


function getMatAtt() {
    var matId = $("#matId").val();

    $.ajax({
        url: ctx + '/test/listMatAttachment.do?matId='+matId,
        processData: false,
        contentType: false,
        success: function (result) {
            if(result){
                if(result.templateDocList!=null && result.templateDocList.length>0){
                    showAtt('0', matId,result.templateDocList);

                }
                if(result.sampleDocList!=null && result.sampleDocList.length>0){
                    showAtt('1',matId,result.sampleDocList);
                }
                if(result.reviewSampleDocList!=null && result.reviewSampleDocList.length>0){
                    showAtt('2',matId,result.reviewSampleDocList);
                }
            }

        }
    });

}

function showAtt(docType,matId,list){

    var file = 'file_'+docType;
    var picDiv = $("#"+file).parents(".fileupload-opbtns").children(".uploader-list");

    for(var i=0;i<list.length;i++){
        imgCount++;

        var index = imgCount;

        var data = list[i];
        var picHtml = ' <div class="file-item thumbnail" title="'+data.attName+'"  onmouseover = "$(this).find(\'.file-panel\').height(30);"  onmouseout = "$(this).find(\'.file-panel\').height(0);" >'
            + '<div class="info">'+data.attName+'</div>'
            // + '<img id="img'+i+'" class="noImgThumb"   onerror="this.src=\''+defaultImg+'\'" >'
            + '<img id="'+file+'_img_edit_'+index+'" class="noImgThumb" >'
            // + '<span class="upload-state-done success"></span>'
            + '<span class="upload-state-done success" onclick="showAttachment(\''+data.detailId+'\',\''+data.attType+'\',\''+data.attFormat+'\');" ></span>'
            + '<div class="file-panel" style="height: 0px;">'
            // + '<span class="cancel" style = "background: url(${pageContext.request.contextPath}/ui-static/aplanmis/util/images/icons.png) no-repeat;background-position: -48px -24px;"   onclick="delImg(this);">删除</span>'
            + '<span class="cancel"    onclick="delImg(this);">删除</span>'
            +  '</div>'
            + '</div>';

        // picDiv.append(picHtml);
        picDiv.prepend(picHtml);

        var imgObjPreview = document.getElementById(file+"_img_edit_" + imgCount);
        imgObjPreview.style.display = 'block';
        imgObjPreview.style.width = '110px';
        imgObjPreview.style.height = '110px';


        if(isImage(data.attFormat)){
            imgObjPreview.src = ctx + '/test/showSmallPic.do?detailId='+data.detailId;
            $("#"+file+"_img_edit_" + index).attr("rel",ctx + '/test/showPic.do?detailId='+data.detailId);
            $("#"+file+"_img_edit_" + index).imagezoom();
        }else{
            imgObjPreview.src = defaultImg;
        }

        var d = {};
        d.name = data.attName;
        d.type = data.attFormat;
        d.data = data.detailId;
        d.docType = docType;
        d.tag = 'edit';
        d.attType = data.attType;
        d.id = file+"_img_edit_" + index;
        pushArr(docType,d);
    }

}

/**
 * 是否图片
 * @param attFormat
 * @returns {boolean}
 */
function isImage(attFormat) {

    if(attFormat!=null && attFormat.trim()!=''){
       if(attFormat.indexOf("image") != -1) {
           return true;
       }else if(attFormat.indexOf("jpg") != -1
           || attFormat.indexOf("png") != -1
           || attFormat.indexOf("jpeg") != -1
           || attFormat.indexOf("jpe") != -1){
           return true;
       }
    }
    return false;

}

/**
 * 下载或展示附件
 * @param detailId
 * @param attType
 * @param type
 */
function showAttachment(detailId,attType,type) {
    // 图片
    if(isImage(type)){
        window.open(ctx + '/aea/item/mat/' + 'showFile.do?detailId='+ detailId,"展示图片");
    // 文件
    }else{
        var url = "";
        if(attType=='KP'){//开普的调用开普接口获取
            url = ctx +'/rest/item/api/kpdownloadFile.do?detailId='+ detailId;
        }else{//下载
            url = ctx + '/aea/item/mat/' + 'downloadGlobalMatDoc.do?detailId='+ detailId;
        }

        window.open(url,"展示图片");
    }
}


function clearAllData() {
    clearDocArr();
    clearDocFile($("#file_0"));
    clearDocFile($("#file_1"));
    clearDocFile($("#file_2"));
    $(".uploadBtn").css("display","none");
    $(".thumbnail").remove();
    imgCount = 0;
}

function clearDocArr(){
    templateDocArr = new Array();
    sampleDocArr = new Array();
    reviewSampleDocArr = new Array();
}

/**
 * 修改新增标志
 * @param docType
 * @param id
 */
function changEdit(docType,id) {
    var arr = getDataArr(docType);
    if(arr!=null && arr.length>0){
        for(var i =0;i<arr.length;i++){
            var v = arr[i];
            if(v.id == id && v.tag == 'new'){
                v.tag  = 'edit';
                break;
            }
        }
    }
}