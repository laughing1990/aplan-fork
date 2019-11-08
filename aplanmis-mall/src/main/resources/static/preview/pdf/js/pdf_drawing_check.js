//图纸浏览基本变量
gDrawingViewer = {};
gDrawingViewer.map = null;
gDrawingViewer.bounds = null;
gDrawingViewer.width = 0;
gDrawingViewer.height = 0;

//基础图纸
gDrawingViewer.base_url = "";
gDrawingViewer.base_src = null;

//当前Pdf对象
gDrawingViewer.pdf_document = null;
gDrawingViewer.pageNum = 1;
gDrawingViewer.pageRendering = false;
gDrawingViewer.pdfViewer = null;
gDrawingViewer.rotation = 0;
gDrawingViewer.scale = 1;
gDrawingViewer.base_canvas = null;
gDrawingViewer.base_ctx = null;
gDrawingViewer.pageNumPending = null;

//比对图纸
gDrawingViewer.compare_url = "";
gDrawingViewer.compare_src = "";

//图纸canvas对象
gDrawingViewer.map_canvas_src = null;
//图纸Mat对象
gDrawingViewer.map_cv_src = null;
//施工图对象
gDrawingViewer.map = {};
//施工图图层
gDrawingViewer.map.layer = null;

//图纸背景颜色
gDrawingViewer.back_color = {};
gDrawingViewer.back_color.white = [255, 255, 255];          //白色
gDrawingViewer.back_color.light_blue = [223, 239, 255];     //浅蓝
gDrawingViewer.back_color.light_green = [211, 255, 220];    //浅绿
gDrawingViewer.back_color.light_red = [255, 218, 185];      //浅红

//修改颜色图纸
gDrawingViewer.back_color.base_src = null;
//图片基础大小，执行图片运算操作时，将大图分割为1024X1024大小的图片
gDrawingViewer.unit_size = [1024, 1024];

var map = null;

var bounds = null;
var width = 0;
var height = 0;

//通过pngurl加载地图
function load_drawings() {
    try {
        var tem_canvas = document.getElementById("tem_canvas");
        renderPdf2Canvas(_pdf_url, 1, tem_canvas);
        document.agBaseViewer = gDrawingViewer;
    } catch (e) {
    }
}

//初始化地图
function init_map(src) {
    bounds = [[-2 * gDrawingViewer.height, -2 * gDrawingViewer.width],
        [gDrawingViewer.height*2, gDrawingViewer.width*2]];
    map = L.map('drawings_viewer', {
        crs: L.CRS.Simple,
        maxZoom: 4,
        minZoom: -4,
        maxBounds: bounds,
        zoomControl: false,
        attributionControl: false
    });

    bounds = [[-0.5 * gDrawingViewer.height , -0.5 * gDrawingViewer.width],
        [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]];
    gDrawingViewer.map.layer = L.imageOverlay(src, bounds).addTo(map);

    map.fitBounds(bounds);
    //先注释
    // nineCell.clearNineCellLayers();
    // areaMeasure.clear();
    // lengthMeasure.clear();
    map.on('mousemove', function (e) {
        var html = JSON.stringify(e.containerPoint) + " _ "+map._zoom
            + "<br />" + JSON.stringify(e.latlng);
        $("#info").html(html);
        /* innerHTML 属性设置或返回表格行的开始和结束标签之间的 HTML  */
        // e.point is the x, y coordinates of the mousemove event relative
        // to the top-left corner of the map
    });
    // map.on("zoom", function (e) {
    //
    // })
}

//加载施工图到MapaddImageLayer
function addImageLayer(src) {
    bounds = [[-0.5 * gDrawingViewer.height, -0.5 * gDrawingViewer.width],
        [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]];
    gDrawingViewer.map.layer = L.imageOverlay(src, bounds).addTo(map);

    map.fitBounds(bounds);
    //先注释
    // nineCell.clearNineCellLayers();
    // areaMeasure.clear();
    // lengthMeasure.clear();
}

//将pdf渲染到canvas
function renderPdf2Canvas(pdf_url, page_index, canvas, flag) {
    try {
        if (pdf_url === undefined || pdf_url === "") return;

        PDFJS.workerSrc = workPath;
        PDFJS.getDocument(pdf_url).then(function getPdf(pdf) {
            gDrawingViewer.pdf_document = pdf;
            pdf.getPage(page_index).then(function getPage(page) {
                var pageType = typeof(page);
                //最大分辨率 5000左右 GPU不支持
                var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
                var factor = fitScale(viewport);
                gDrawingViewer.scale = gDrawingViewer.scale*factor;
                viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);

                gDrawingViewer.pdfViewer = viewport;
                gDrawingViewer.base_canvas = canvas;
                gDrawingViewer.base_ctx = canvas.getContext('2d');
                canvas.height = viewport.height;
                gDrawingViewer.height = canvas.height;
                canvas.width = viewport.width;
                gDrawingViewer.width = canvas.width;
                var renderContext = {
                    canvasContext: gDrawingViewer.base_ctx,
                    viewport: viewport
                };
                page.render(renderContext).then(function renderDrawing() {
                    var src = canvas.toDataURL('image/jpg');
                    gDrawingViewer.base_src = src;
                    if (null != gDrawingViewer.map.layer) {
                        map.removeLayer(gDrawingViewer.map.layer);
                        addImageLayer(src);
                    } else
                        init_map(src);
                    if(gDrawingViewer.map_cv_src!==null){
                        gDrawingViewer.map_cv_src.delete();
                        gDrawingViewer.map_cv_src = null;
                    }
                    initPageToolbar();
                    // addOptionMarker.createMarkerByDrawingId('111'); 先注释
                });
            });
        });
    } catch (err) {
        var temOutput = err.message;
    }
}

//根据页码渲染该页内容
function renderPage(num) {
    gDrawingViewer.pageRendering = true;
    // Using promise to fetch the page
    if (null === gDrawingViewer.pdf_document || num < 1) return;
    gDrawingViewer.pdf_document.getPage(num).then(function (page) {
        var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
        var factor = fitScale(viewport);
        gDrawingViewer.scale = gDrawingViewer.scale*factor;
        viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);

        gDrawingViewer.pdfViewer = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
        var canvas = gDrawingViewer.base_canvas;
        gDrawingViewer.base_ctx = canvas.getContext('2d');
        canvas.height = viewport.height;
        gDrawingViewer.height = canvas.height;
        canvas.width = viewport.width;
        gDrawingViewer.width = canvas.width;
        // Render PDF page into canvas context
        var renderContext = {
            canvasContext: gDrawingViewer.base_ctx,
            viewport: viewport
        };
        var renderTask = page.render(renderContext);
        // Wait for rendering to finish
        renderTask.promise.then(function () {
            gDrawingViewer.pageRendering = false;
            var src = canvas.toDataURL('image/jpg');
            gDrawingViewer.base_src = src;
            if (null != gDrawingViewer.map.layer) {
                map.removeLayer(gDrawingViewer.map.layer);
                addImageLayer(src);
            } else
                init_map(src);
            if(null !== gDrawingViewer.map_cv_src){
                gDrawingViewer.map_cv_src.delete();
                gDrawingViewer.map_cv_src = null;
            }
        });
    });

    // Update page counters
    $("#pageNum").text(gDrawingViewer.pageNum);
}

function fitScale(viewport){
    var maxPixel = viewport.height > viewport.width ? viewport.height:viewport.width;
    var factor = parseInt((5500.0 / maxPixel)*100);
    factor = factor/100.0;
    if(factor < 1) factor = 1;
    //var factor = 5500 / maxPixel;
    return factor;
}
//初始化翻页工具条
function initPageToolbar() {
    if (null === gDrawingViewer.pdf_document) return;
    if (gDrawingViewer.pdf_document.numPages == 1) {
        $("#toolbarPageNum").hide();
    }
    else {
        $("#toolbarPageNum").show();
        $("#pageTotal").text(gDrawingViewer.pdf_document.numPages);
        $("#pageNum").text(1);
    }
}

/**
 * If another page rendering in progress, waits until the rendering is
 * finised. Otherwise, executes rendering immediately.
 */
function queueRenderPage(num) {
    if (gDrawingViewer.pageRendering) {
        gDrawingViewer.pageNumPending = num;
    } else {
        renderPage(num);
    }
}

//校验数字
function checkNum(obj) {
    var val = obj.value;
    if(isNaN(val) || val < 1){
        obj.value = '';
        return;
    }
    if(val > gDrawingViewer.pdf_document.numPages){
        obj.value = gDrawingViewer.pdf_document.numPages;
    }
}

//显示下一页
function onNextPage() {
    if (gDrawingViewer.pageNum >= gDrawingViewer.pdf_document.numPages) {
        return;
    }
    gDrawingViewer.pageNum++;
    queueRenderPage(gDrawingViewer.pageNum);
    $("#pageNum").text(gDrawingViewer.pageNum);
}

//显示上一页
function onPrevPage() {
    if (gDrawingViewer.pageNum <= 1) {
        return;
    }
    gDrawingViewer.pageNum--;
    queueRenderPage(gDrawingViewer.pageNum);
    $("#pageNum").text(gDrawingViewer.pageNum);
}

//跳转到指定页面
function goSelectPage() {
    var num = $('#selectPage').val();
    if(gDrawingViewer.pageNum == num){
        return;
    }
    num = parseInt(num);
    gDrawingViewer.pageNum = num;
    queueRenderPage(gDrawingViewer.pageNum);
    $("#pageNum").text(gDrawingViewer.pageNum);
}

//逆时针90度旋转地图
function rotateViewer(counter) {
    try {
        //var src = cv.matFromImageData(gDrawingViewer.base_src);

        document.body.style.cursor = "wait";
        //先注释
        // nineCell.clearNineCellLayers();
        // areaMeasure.clear();
        // lengthMeasure.clear();
        if (!counter)
            rotatePages(-90);
        else
            rotatePages(90);
        document.body.style.cursor = "default";
    } catch (err) {
        var temOutput = err.message;
        document.body.style.cursor = "default";
    }
}

//旋转90度
function rotatePages(delta) {
    if (!gDrawingViewer.pdf_document || gDrawingViewer.pdfViewer == null) {
        return;
    }
    var newRotation = (gDrawingViewer.pdfViewer.rotation + 360 + delta) % 360;
    gDrawingViewer.rotation = newRotation;
    queueRenderPage(gDrawingViewer.pageNum);
}

//逆时针90度旋转地图,内存正常
function transpose1(counter) {
    try {
        var tem_canvas = document.getElementById("tem_canvas");
        var dst = cv.imread(tem_canvas);

        cv.imshow(tem_canvas, dst);
        var src = tem_canvas.toDataURL('image/jpg');
        gDrawingViewer.base_src = src;

        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);
        dst.delete();
    } catch (err) {
        var temOutput = err.message;
        //如果出现异常调用后台执行操作
    }
}

//切分图片为多个张图
function getDrawingTiles() {
    var tem_canvas = document.getElementById("tem_canvas");
    var ctx = tem_canvas.getContext("2d");
    if (tem_canvas.width < gDrawingViewer.unit_size[0]
        && tem_canvas.height < gDrawingViewer.unit_size[1])
        return;

    var iWidth = parseInt(tem_canvas.width / gDrawingViewer.unit_size[0]);
    var iHeight = parseInt(tem_canvas.height / gDrawingViewer.unit_size[1]);
    var matArray = new Array();

    for (var i = 0; i < iWidth; i++) {
        matArray[i] = new Array();
        for (var j = 0; j < iHeight; j++) {
            matArray[i][j] = ctx.getImageData(
                (i) * gDrawingViewer.unit_size[0], (j) * gDrawingViewer.unit_size[1],
                gDrawingViewer.unit_size[0], gDrawingViewer.unit_size[1]);
        }
    }
    var modWidth = tem_canvas.width % gDrawingViewer.unit_size[0];
    var modHeight = tem_canvas.height % gDrawingViewer.unit_size[1];
    if (modWidth > 0) {
        matArray[iWidth] = new Array();
        for (var j = 0; j < iHeight; j++) {
            matArray[iWidth][j] = ctx.getImageData(
                iWidth * gDrawingViewer.unit_size[0], (j) * gDrawingViewer.unit_size[1],
                modWidth, gDrawingViewer.unit_size[1]);
        }
    }
    if (modHeight > 0) {
        for (var i = 0; i < iWidth; i++) {
            matArray[i][iHeight] = ctx.getImageData(
                (i) * gDrawingViewer.unit_size[0], iHeight * gDrawingViewer.unit_size[1],
                gDrawingViewer.unit_size[0], modHeight);
        }
    }
    if (modWidth > 0 && modHeight > 0) {
        matArray[iWidth][iHeight] = ctx.getImageData(
            iWidth * gDrawingViewer.unit_size[0], iHeight * gDrawingViewer.unit_size[1],
            modWidth, modHeight);
    }

    return matArray;
}

//打开本地图纸
function openFile(e) {
    var fileInput = $("<input type='file' accept='application/pdf'/>");
    fileInput.change(function (evt) {
        var file = this.files[0];
        var read = new FileReader();
        read.onload = function (evt) {
            var tem = this;
            var tem_canvas = document.getElementById("tem_canvas");
            gDrawingViewer.base_canvas = tem_canvas;
            gDrawingViewer.pageNum = 1;
            renderPdf2Canvas({data: evt.target.result}, gDrawingViewer.pageNum, tem_canvas);
            cancelLink();
        };
        read.readAsArrayBuffer(file);
    }).click();
}

//执行分块执行
function tileChangeColor_bak(color) {
    try {
        if (!progressBusing()) showProgressbar(); else return;

        var backColor = gDrawingViewer.back_color.white;
        if (color === 1)
            backColor = gDrawingViewer.back_color.light_blue;
        else if (color === 2)
            backColor = gDrawingViewer.back_color.light_green;
        else if (color === 3)
            backColor = gDrawingViewer.back_color.light_red;
        else {
            update_mat_layer(gDrawingViewer.map_cv_src);
        }

        var tem_canvas = document.getElementById("tem_canvas");
        var ctx = tem_canvas.getContext("2d");
        //分为多个小块
        var matArray = getDrawingTiles();
        var total = matArray.length * matArray[0].length;
        for (var i = 0; i < matArray.length; i++) {
            for (var j = 0; j < matArray[i].length; j++) {
                var temMat = matArray[i][j];
                {
                    var src = cv.matFromImageData(temMat);
                    change_mat_rgb(src, backColor);
                    var imgData = new ImageData(new Uint8ClampedArray(src.data), temMat.width, temMat.height);
                    ctx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
                    src.delete();
                    updateProgress((i + 1) * (j + 1), total);
                }
            }
        }
        var src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);
        hideProgressbar();
    } catch (err) {
        var temOutput = err.message;
        hideProgressbar();
    }
}

//执行分块执行
function tileChangeColor(color) {
    try {
        if(!progressBusing()) showProgressbar(); else return;

        var backColor = gDrawingViewer.back_color.white;
        if(color ===1)
            backColor = gDrawingViewer.back_color.light_blue;
        else if(color ===2)
            backColor = gDrawingViewer.back_color.light_green;
        else if(color ===3)
            backColor = gDrawingViewer.back_color.light_red;
        else
        {
            update_mat_layer(gDrawingViewer.map_cv_src);
        }

        var tem_canvas = document.getElementById("tem_canvas");
        var ctx = tem_canvas.getContext("2d");
        //分为多个小块
        var matArray = getDrawingTiles();
        var total = matArray.length*matArray[0].length;
        subChange(ctx,matArray,0,total,backColor);
    } catch (err) {
        var temOutput = err.message;
        hideProgressbar();
    }
}

function subChange(ctx,matArray,k,total,backColor){
    var i = parseInt(k/matArray[0].length);
    var j = k % matArray[0].length;
    var temMat = matArray[i][j];
    {
        var src = cv.matFromImageData(temMat);
        change_mat_rgb(src, backColor);
        var imgData = new ImageData(new Uint8ClampedArray(src.data), temMat.width, temMat.height);

        ctx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
        src.delete();
        updateProgress(k, total);
    }
    k++;
    if(k<total)
        setTimeout(function(){subChange(ctx,matArray,k,total,backColor)},10);
    else{
        var tem_canvas = document.getElementById("tem_canvas");
        var src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);
        hideProgressbar();
    }
}


//修改背景颜色
function doChangeColor(mat, color) {
    try {
        tileChangeColor();
        return;
        var tem_canvas = document.getElementById("tem_canvas");
        if(null==gDrawingViewer.map_cv_src){
            gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
        }

        var backColor = gDrawingViewer.back_color.white;
        if (color === 1)
            backColor = gDrawingViewer.back_color.light_blue;
        else if (color === 2)
            backColor = gDrawingViewer.back_color.light_green;
        else if (color === 3)
            backColor = gDrawingViewer.back_color.light_red;
        else {
            update_mat_layer(gDrawingViewer.map_cv_src);
            dst.delete();
        }

        for (var i = 0; i < 4; i++) {
            for (var j = 0; j < 4; j++) {
            }
        }
        update_mat_layer(dst);
        dst.delete();
    } catch (err) {
        var temOutput = err.message;
    }
}

/*//切片修改背景色
function changeColor(color) {
    tileChangeColor(color);
    return;
}*/

//切换修改背景色
function changeColor(color) {
    var tem_canvas = document.getElementById("tem_canvas");

    if(null==gDrawingViewer.map_cv_src){
        gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
    }
    if(color == 0) {
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(gDrawingViewer.base_src);
        return;
    }
    var srcTem = new cv.Mat();
    var typeName = gDrawingViewer.map_cv_src.type();
    var tem = null;
    if(color == 1){
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [32,16,0,0]);//浅蓝
    }
    else if(color == 2){
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [44,0,35,0]);//浅绿
    }
    else if(color == 3){
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [0,37,70,0]);//浅红
    }
    cv.absdiff(tem, gDrawingViewer.map_cv_src, srcTem);
    try {
        cv.imshow(tem_canvas, srcTem);
        tem.delete();srcTem.delete();
        var src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);
    }
    catch(err){
        alert(err.message);
    }
}

//整体修改背景颜色
function changeColorWhole(color) {
    try {
        var dst = gDrawingViewer.map_cv_src.clone();

        var backColor = gDrawingViewer.back_color.white;
        if (color === 1)
            backColor = gDrawingViewer.back_color.light_blue;
        else if (color === 2)
            backColor = gDrawingViewer.back_color.light_green;
        else if (color === 3)
            backColor = gDrawingViewer.back_color.light_red;
        else {
            update_mat_layer(gDrawingViewer.map_cv_src);
            dst.delete();
        }

        for (var i = 0; i < 4; i++) {
            for (var j = 0; j < 4; j++) {
                change_rgb(dst, backColor, i * dst.cols / 4, (i + 1) * dst.cols / 4,
                    j * dst.rows / 4, (j + 1) * dst.rows / 4);
            }
        }
        update_mat_layer(dst);
        dst.delete();
    } catch (err) {
        var temOutput = err.message;
    }
}

//更新图纸为cvMat
function update_mat_layer(mat) {
    var tem_canvas = document.getElementById("tem_canvas");
    cv.imshow(tem_canvas, mat);
    var src = tem_canvas.toDataURL('image/jpg');
    map.removeLayer(gDrawingViewer.map.layer);
    addImageLayer(src);
}

//更改区域颜色
function change_rgb(dst, backColor, iMin, iMax, jMin, jMax) {
    for (var i = iMin; i < iMax; i++) {
        for (var j = jMin; j < jMax; j++) {
            if (dst.ucharPtr(j, i)[0] > 225 & dst.ucharPtr(j, i)[1] > 225 & dst.ucharPtr(j, i)[2] > 225) {
                dst.ucharPtr(j, i)[0] = backColor[0];
                dst.ucharPtr(j, i)[1] = backColor[1];
                dst.ucharPtr(j, i)[2] = backColor[2];
            }
        }
    }
}

function change_mat_rgb(dst, backColor) {
    for (var i = 0; i < dst.cols; i++) {
        for (var j = 0; j < dst.rows; j++) {
            if (dst.ucharPtr(j, i)[0] > 225 & dst.ucharPtr(j, i)[1] > 225 & dst.ucharPtr(j, i)[2] > 225) {
                dst.ucharPtr(j, i)[0] = backColor[0];
                dst.ucharPtr(j, i)[1] = backColor[1];
                dst.ucharPtr(j, i)[2] = backColor[2];
            }
        }
    }
}

function transpose2(counter) {
    var tem_canvas = document.getElementById("tem_canvas");
    var ctx = tem_canvas.getContext('2d');
    ctx.rotate(Math.PI / 2);
    ctx.fillRect(100, 100, 300, 200);
}

function setData2Map() {
    try {
        var tem = new cv.Mat();

        var tem_canvas = document.getElementById("tem_canvas");
        var dst = cv.imread(tem_canvas);

        var src = dst.data;

        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);

        src.delete();
        tem.delete();
        dst.delete();
    } catch (err) {
        var temOutput = err.message;
    }
}

//逆时针90度旋转地图
function transpose(counter) {
    try {
        var tem = new cv.Mat();

        var tem_canvas = document.getElementById("tem_canvas");
        var dst = cv.imread(tem_canvas);

        cv.transpose(dst, tem);
        if (!counter)
            cv.flip(tem, dst, 0);
        else
            cv.flip(tem, dst, 1);

        cv.imshow(tem_canvas, dst);
        var src = tem_canvas.toDataURL('image/jpg');
        gDrawingViewer.base_src = src;
        if(gDrawingViewer.map_cv_src == null)
        {
            gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
        }
        gDrawingViewer.width = dst.cols;
        gDrawingViewer.height = dst.rows;

        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);

        src.delete();
        tem.delete();
        dst.delete();
    } catch (err) {
        var temOutput = err.message;
    }
}

//设置1:1窗口
function resize() {
/*    bounds = [[-0.5 * gDrawingViewer.height, -0.5 * gDrawingViewer.width],
        [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]];*/
    map.fitBounds(bounds);
    //map.setView([0,0],1);
}
