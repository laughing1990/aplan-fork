/*
*   两张图纸比对
*   1 获取第二张图纸
*   2 图纸二值化
*   3 相减
*   4 取反  加红色
*   5 显示权值相加
*   6 渲染到Map
*/

agDrawingCompare = {};
//图纸对象
agDrawingCompare.base_url = null;
agDrawingCompare.pdf_document = null;
agDrawingCompare.base_canvas = null;
agDrawingCompare.base_ctx = null;

//比对Map
agDrawingCompare.map={};
agDrawingCompare.map.layer =null;
agDrawingCompare.map.bounds = null;
agDrawingCompare.width = 0;
agDrawingCompare.height = 0;
agDrawingCompare.mapControl = null;

//打开另外一张图纸
function openOtherDrawing(){
    var fileInput = $("<input type='file' accept='application/pdf'/>");
    fileInput.change(function (evt) {
        var file = this.files[0];
        var read = new FileReader();
        read.onload = function (evt) {
            var tem = this;
            var tem_canvas =  document.getElementById("tem_canvas");
            agDrawingCompare.base_canvas = tem_canvas;
            readOtherDrawing({data: evt.target.result},tem_canvas);
        };
        read.readAsArrayBuffer(file);
    }).click();
}

//读取比对图纸
function readOtherDrawing(pdf_url,canvas){
    try {
        if(pdf_url === undefined || pdf_url ==="") return;

        PDFJS.workerSrc = workPath;
        PDFJS.getDocument(pdf_url).then(function getPdf(pdf) {
            agDrawingCompare.pdf_document = pdf;
            var page_index = 1;
            pdf.getPage(page_index).then(function getPage(page) {
                var pageType = typeof(page);
                //最大分辨率 5000左右 GPU不支持
                var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
                var factor = fitScale(viewport);
                gDrawingViewer.scale = gDrawingViewer.scale*factor;
                viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);

                agDrawingCompare.base_ctx = canvas.getContext('2d');
                canvas.height = viewport.height;
                agDrawingCompare.height =canvas.height;
                canvas.width = viewport.width;
                agDrawingCompare.width =canvas.width;
                var renderContext = {
                    canvasContext: agDrawingCompare.base_ctx,
                    viewport: viewport
                };
                page.render(renderContext).then(function renderDrawing(){
                    addCompareViewer();
                    var src = canvas.toDataURL('image/jpg');
                    agDrawingCompare.base_src = src;
                    //gDrawingViewer.map_cv_src = cv.imread(canvas);
                    createOtherMap(src);
                    var maps = [map,agDrawingCompare.mapControl];
                    linkMap(maps);
                });
            });
        });
    } catch (err) {
        var temOutput = err.message;
    }
}

//创建比对Map
function createOtherMap(src){
    if(agDrawingCompare.mapControl != null && agDrawingCompare.map.layer!=null) {
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
        addLayer2OtherMap(src);
        return;
    }

    var bounds = [[-2*agDrawingCompare.height, -2*agDrawingCompare.width],
        [agDrawingCompare.height*2, agDrawingCompare.width*2]];
    agDrawingCompare.mapControl = L.map('compare_viewer', {
        crs: L.CRS.Simple,
        maxZoom: 4,
        minZoom: -4,
        maxBounds: bounds,
        zoomControl: false,
        attributionControl: false
    });
    bounds = [[-0.5*agDrawingCompare.height, -0.5*agDrawingCompare.width],
        [0.5*agDrawingCompare.height, 0.5*agDrawingCompare.width]];
    agDrawingCompare.map.layer = L.imageOverlay(src, bounds).addTo(agDrawingCompare.mapControl);

    bounds = [[-1*agDrawingCompare.height, -1*agDrawingCompare.width],
        [agDrawingCompare.height, agDrawingCompare.width]];
    agDrawingCompare.mapControl.fitBounds(bounds);
}

//添加图层到地图
function addLayer2OtherMap(src){
    var bounds = [[-0.5*agDrawingCompare.height, -0.5*agDrawingCompare.width],
        [0.5*agDrawingCompare.height, 0.5*agDrawingCompare.width]];
    agDrawingCompare.map.layer = L.imageOverlay(src, bounds).addTo(agDrawingCompare.mapControl);

    bounds = [[-1*agDrawingCompare.height, -1*agDrawingCompare.width],
        [agDrawingCompare.height, agDrawingCompare.width]];
    agDrawingCompare.mapControl.fitBounds(bounds);
    nineCell.clearNineCellLayers();
    areaMeasure.clear();
    lengthMeasure.clear();
}

//增加比对窗口
function addCompareViewer(){
    $("#drawings_viewer").css({"width":"50%","height":"100%","float":"left"});
    $("#drawings_viewer").width("50%");
    map.invalidateSize(true);
    $("#compare_viewer").css({"width":"50%","height":"100%","float":"right","display":"inline"});
    if(agDrawingCompare.mapControl != null)
        agDrawingCompare.mapControl.invalidateSize(true);
}

//删除比对窗口
function hideCompareViewer(){
    $("#drawings_viewer").css({"width":"100%","height":"100%","float":"left"});
    $("#compare_viewer").css({"width":"50%","height":"100%","float":"right","display":"none"});
    map.invalidateSize(true);
}

//联动两个地图linkMap[map1,map2]
function linkMap(maps){
    //地图联动实现  
    function maplink(e){
        var _this = this;
        maps.map(function (t) {
            if(t==null||_this==null)return;
            t.setView(_this.getCenter(),_this.getZoom())
        })
    }
    maps.map(function (t) {
        t.on({drag:maplink,zoom:maplink})
    });
}

//取消关联
function cancelLink(){
    if(null!=map && null!=agDrawingCompare.mapControl){
        var maps = [map,agDrawingCompare.mapControl];
        maps.map(function (t) {
            t.off()
        });
        hideCompareViewer();
    }
}

//设置进度条信息
function updateProgress(value,total) {
    var temp;
    if(total == 0){
        temp = "100%";
    }else{
        temp = (value / total * 100).toFixed(0) + "%";
    }

    $("#prog").css("width",temp).text(temp);
}

//进度条操作事项：1 显示 2 隐藏 3 更新进度
gDrawingViewer.progressBar = null;

//初始化工具条，显示工具条
function showProgressbar(){
    //
    $('#progressBar').show();
    updateProgress( 0, 100);
}

//关闭工具条，隐藏工具条
function hideProgressbar(){
    //
    updateProgress( 100, 100)
    $('#progressBar').hide();
}

//工具条是否在运行
function progressBusing(){
    if($('#progressBar').is(':hidden')){
        return false;
    }else{
        return true;
    }
}

//设置进度条信息
function updateProgress(value, total) {
    var temp;
    if(total == 0){
        temp = "100%";
    }else{
        temp = (value / total * 100).toFixed(0) + "%";
    }

    $("#prog").css("width",temp).text(temp);return;
}

//执行比对图纸
agDrawingCompare.doCompare = function(){
    try {
        //获取比对图纸
        var baseDrawing = null,otherDrawing = null;

        var layerImgs = $(".leaflet-image-layer");

        for(var i = 0; i < layerImgs.length; i++) {
            var mapViewer = layerImgs[i].parentElement.parentElement.parentElement;
            if(mapViewer.id == "drawings_viewer")
                baseDrawing = cv.imread(layerImgs[i]);
            else if(mapViewer.id == "compare_viewer")
                otherDrawing = cv.imread(layerImgs[i]);
        }

        //生成灰度图
        cv.cvtColor(baseDrawing, baseDrawing, cv.COLOR_RGBA2GRAY, 0);
        cv.cvtColor(otherDrawing, otherDrawing, cv.COLOR_RGBA2GRAY, 0);

        //执行相减操作
        var dst = new cv.Mat();
        var resultDiff  = cv.absdiff(baseDrawing, otherDrawing, dst);
        cv.bitwise_not(dst,dst);

        //切换为彩色模式
        cv.cvtColor(dst, dst, cv.COLOR_GRAY2RGB, 0);
        cv.cvtColor(baseDrawing, baseDrawing, cv.COLOR_GRAY2RGB, 0);
        cv.cvtColor(otherDrawing, otherDrawing, cv.COLOR_GRAY2RGB, 0);
        var typeName = dst.type();
        var temBase = new cv.Mat(dst.rows,dst.cols,typeName,[255,0,0,0]);
        cv.add(dst,temBase,dst);

        //加载基础图
        cv.addWeighted(dst, 0.3, baseDrawing, 0.7, 3, dst);
        var tem_canvas = document.getElementById("tem_canvas");
        cv.imshow(tem_canvas, dst);

        //保存当前显示内容到全局对象
        var src = tem_canvas.toDataURL('image/jpg');
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
        addLayer2OtherMap(src);

        //加载比对图
        cv.addWeighted(dst, 0.3, otherDrawing, 0.7, 3, dst);
        cv.imshow(tem_canvas, dst);

        //保存当前显示内容到全局对象
        src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);

        temBase.delete();dst.delete();
    }catch(err){
        var output = err.message;
    }
}

//上传图纸到服务器
agDrawingCompare.uploadDrawing = function(imgurl){
    //获取比对图纸
    var baseDrawing = null,otherDrawing = null;
    var layerImgs = $(".leaflet-image-layer");

    if(agDrawingViewer == undefined) var agDrawingViewer = document.agBaseViewer;
    if(Math.abs(agDrawingCompare.width - agDrawingViewer.width)>0||
        Math.abs(agDrawingCompare.height - agDrawingViewer.height)>0 ){
        swal("图纸大小不相同，不进行比对。");
    }

    for(var i = 0; i < layerImgs.length; i++) {
        var mapViewer = layerImgs[i].parentElement.parentElement.parentElement;
        if(mapViewer.id == "drawings_viewer")
            baseDrawing = layerImgs[i];
        else if(mapViewer.id == "compare_viewer")
            otherDrawing = layerImgs[i];
    }
    var blobBase = dataURItoBlob(baseDrawing.src);
    var blobOther = dataURItoBlob(otherDrawing.src);

    var url = ctx + "/cod/drawing/uploadCompareDrawing.do";
    var formData = new FormData();
    formData.append("baseDrawing",blobBase);
    formData.append("otherDrawing",blobOther);
    formData.append("fileNum",2);

    console.log(formData);
    $.ajax({
        url: url,
        type: 'POST',
        data:formData,
        contentType:false,
        processData:false,
        success: function (result) {
            var test = "";
            console.log(result);
        },
        error: function (returndata) {
            var test = "";
        }
    });
}

//根据dataUrl获取文件二进制对象
function dataURItoBlob(dataURI)
{
    var byteString = atob(dataURI.split(',')[1]);

    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++)
    {
        ia[i] = byteString.charCodeAt(i);
    }

    var bb = new Blob([ab], { "type": mimeString });
    return bb;
}