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
agDrawingCompare.map = {};
agDrawingCompare.map.layer = null;
agDrawingCompare.map.bounds = null;
agDrawingCompare.width = 0;
agDrawingCompare.height = 0;
agDrawingCompare.mapControl = null;
agDrawingCompare.rectsGroup = null;

//打开另外一张图纸
function openOtherDrawing() {
    if (map == null) {
        swal("请先加载基础图纸", "没有加载基础图纸。");
        return;
    }
    var fileInput = $("<input type='file' accept='application/pdf'/>");
    fileInput.change(function () {
        $.loading();
        var file = this.files[0];
        toastr.info('正在初始化图纸', '提示信息', {timeOut: 0, extendedTimeOut: 0});
        agDrawingCompare.pageNum = 1;
        // agDrawingCompare.source = {url: URL.createObjectURL(file), originalUrl: file.name};
        // return otherPDFOverlay(agDrawingCompare.source, agDrawingCompare.pageNum);
        agDrawingCompare.fileName = file.name;
        var reader = new FileReader();
        reader.onload =function(e) {

            agDrawingCompare.source =e.target.result;
            otherPDFOverlay(agDrawingCompare.source, agDrawingCompare.pageNum);
        };
        reader.readAsArrayBuffer(file);
    }).click();
}

//读取比对图纸
function otherPDFOverlay(file, idx) {
    var parameters, loadComplete, loadingTask;
    if (agDrawingCompare.pageRendering) return;
    agDrawingCompare.pageRendering = true;
    if (($.isEmptyObject(file) && !('byteLength' in file)) || !file) return;
    $('#loadingBar').removeClass('hidden');
    pdfjsLib.workerSrc = workPath
    loadComplete = false;
    parameters = Object.create(null);
    if (typeof file === 'string') { // URL
        parameters.url = file;
    } else if (file && 'byteLength' in file) { // ArrayBuffer
        parameters.data = file;
    } else if (file.url && file.originalUrl) {
        parameters.url = file.url;
    }
    for (var key in apiParameters) {
        parameters[key] = apiParameters[key];
    }
    loadingTask = pdfjsLib.getDocument(parameters);

    function progress(level) {
        if (loadComplete) {
            return;
        }
        var percent = Math.round(level * 100),
            bar = $('#loadingBar'),
            percent = Math.min(Math.max(percent, 0), 100);
        window.requestAnimationFrame($.proxy(function (_percent) {
            this.width(_percent + '%');
        }, bar, percent));
    }

    //读取文档的进度条
    loadingTask.onProgress = function (_ref8) {
        var loaded = _ref8.loaded,
            total = _ref8.total;
        progress(loaded / total);
    };
    loadingTask.promise.then(function (pdf) {
      //  typeof agDrawingCompare.source.url =='string' && agDrawingCompare.source.originalUrl && window.URL.revokeObjectURL( agDrawingCompare.source.url);
        loadComplete = true;
        window.requestAnimationFrame(function () {
            $('#loadingBar').addClass('hidden').width('0%');
        });
        agDrawingCompare.pdf_document = pdf;
        agDrawingCompare.pageNumPending = idx;

        pdf.getPage(idx).then(function (page) {
            var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation),
                canvas = document.createElement('canvas'),
                cxt = canvas.getContext('2d'),
                bgCanvas,
                bgCxt,
                bounds;
            canvas.width = viewport.width;
            canvas.height = viewport.height;
            bgCanvas = canvas.cloneNode(true);
            bgCxt = bgCanvas.getContext('2d')
            agDrawingCompare.page = page;
            agDrawingCompare.pdfViewer = viewport;
            agDrawingCompare.base_canvas = canvas;
            agDrawingCompare.base_ctx = cxt;
            agDrawingCompare.height = canvas.height;
            agDrawingCompare.width = canvas.width;
            createOtherMap(canvas);
            addCompareViewer();
            bounds = [[-1 * agDrawingCompare.height, -1 * agDrawingCompare.width],
                [agDrawingCompare.height, agDrawingCompare.width]];
            gDrawingViewer.map.fitBounds(bounds);
            agDrawingCompare.mapControl.fitBounds(bounds);
            var renderContext = {
                canvasContext: cxt,
                viewport: viewport
            };
            page.render(renderContext).then(function () {
                // bounds = [[-1 * agDrawingCompare.height, -1 * agDrawingCompare.width],
                //     [agDrawingCompare.height, agDrawingCompare.width]];
                //
                // agDrawingCompare.mapControl.fitBounds(bounds);
                // gDrawingViewer.map.fitBounds(bounds);
                var maps = [map, agDrawingCompare.mapControl];
                linkMap(maps);
                $.loading.close();
                toastr.remove();
                window.requestAnimationFrame(function () {
                    agDrawingCompare.pageRendering = false;
                    toastr.success('图纸渲染完毕', '提示信息', {timeOut: 1500});
                });
                // if (typeof agDrawingCompare.canvasSource === 'undefined') {
                    var _canvasElement = canvas.cloneNode(true);
                    _canvasElement.getContext('2d').putImageData(agDrawingCompare.base_ctx.getImageData(0, 0, agDrawingCompare.width, agDrawingCompare.height), 0, 0);
                    agDrawingCompare.canvasSource = _canvasElement;
                // }
            });
        });
    });
}

function readOtherDrawing(pdf_url, canvas) {
    try {
        if (!pdf_url) return;

        // var workPath = "http://" + window.location.host + ctx +
        //     '/ui-static/aplanmis/cod/pdf/js/pdf.worker.js';
        pdfjsLib.workerSrc = workPath;
        pdfjsLib.getDocument(pdf_url).then(function getPdf(pdf) {
            agDrawingCompare.pdf_document = pdf;
            var page_index = 1;
            pdf.getPage(page_index).then(function getPage(page) {
                var pageType = typeof (page);
                //最大分辨率 5000左右 GPU不支持
                var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
                var factor = fitScale(viewport);
                gDrawingViewer.scale = gDrawingViewer.scale * factor;
                viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);

                agDrawingCompare.base_ctx = canvas.getContext('2d');
                canvas.height = viewport.height;
                agDrawingCompare.height = canvas.height;
                canvas.width = viewport.width;
                agDrawingCompare.width = canvas.width;
                var renderContext = {
                    canvasContext: agDrawingCompare.base_ctx,
                    viewport: viewport
                };
                $.loading.close();
                page.render(renderContext).then(function renderDrawing() {
                    toastr.remove();
                    window.requestAnimationFrame(function () {
                        toastr.success('图纸渲染完毕', '提示信息', {progressBar: true, timeOut: 1500});
                    });

                    var src = canvas.toDataURL('image/jpg');
                    agDrawingCompare.base_src = src;
                    //gDrawingViewer.map_cv_src = cv.imread(canvas);
                    createOtherMap(src);
                    addCompareViewer();
                    var maps = [map, agDrawingCompare.mapControl];
                    linkMap(maps);
                });
            });
        });
    } catch (err) {
        var temOutput = err.message;
        $.loading.close();
    }
}

//创建比对Map
function createOtherMap(src) {
    if (agDrawingCompare.mapControl != null && agDrawingCompare.map.layer != null) {
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
        addLayer2OtherMap(src);
        return;
    }

    var bounds = [[-2 * agDrawingCompare.height, -2 * agDrawingCompare.width],
        [agDrawingCompare.height * 2, agDrawingCompare.width * 2]];
    agDrawingCompare.mapControl = L.map('compare_viewer', {
        crs: L.CRS.Simple,
        maxZoom: 4,
        minZoom: -4,
        maxBounds: bounds,
        zoomControl: false,
        attributionControl: false,
        doubleClickZoom: false
    });
    bounds = [[-0.5 * agDrawingCompare.height, -0.5 * agDrawingCompare.width],
        [0.5 * agDrawingCompare.height, 0.5 * agDrawingCompare.width]];
    agDrawingCompare.map.layer = L.svgOverlay(src, bounds).addTo(agDrawingCompare.mapControl);

    // bounds = [[-1 * agDrawingCompare.height, -1 * agDrawingCompare.width],
    //     [agDrawingCompare.height, agDrawingCompare.width]];
    //
    // agDrawingCompare.mapControl.fitBounds(bounds);
}

//添加图层到地图
function addLayer2OtherMap(src) {
    var bounds = [[-0.5 * agDrawingCompare.height, -0.5 * agDrawingCompare.width],
        [0.5 * agDrawingCompare.height, 0.5 * agDrawingCompare.width]];
    agDrawingCompare.map.layer = L.svgOverlay(src, bounds).addTo(agDrawingCompare.mapControl);

    bounds = [[-1 * agDrawingCompare.height, -1 * agDrawingCompare.width],
        [agDrawingCompare.height, agDrawingCompare.width]];
    agDrawingCompare.mapControl.fitBounds(bounds);
    common.clearShape();
}

//增加比对窗口
function addCompareViewer() {
    $("#drawings_viewer").css({"width": "50%", "height": "100%", "float": "left", "display": "inline"});
    map.invalidateSize(true);
    $("#compare_viewer").css({"width": "50%", "height": "100%", "float": "right", "display": "inline"});
    $("#compare_viewer").show();
    if (agDrawingCompare.mapControl != null)
        agDrawingCompare.mapControl.invalidateSize(true);
}

//删除比对窗口
function hideCompareViewer() {
    $("#drawings_viewer").css({"width": "100%", "height": "100%", "float": "left"});
    $("#compare_viewer").hide();
    map.invalidateSize(true);
}

//联动两个地图linkMap[map1,map2]
function linkMap(maps) {
    //地图联动实现  
    function maplink(e) {
        var _this = this;
        maps.map(function (t) {
            if (t == null || _this == null) return;
            t.setView(_this.getCenter(), _this.getZoom())
        })
    }

    maps.map(function (t) {
        t.on({drag: maplink, zoom: maplink})
    });

    //显示执行比对按钮
    $("#do_compare").show();
}

//取消关联
function cancelLink() {
    if (null != map && null != agDrawingCompare.mapControl) {
        var maps = [map, agDrawingCompare.mapControl];
        maps.map(function (t) {
            t.off()
        });
        hideCompareViewer();
    }
    $("#do_compare").hide();
}

//设置进度条信息
function updateProgress(value, total) {
    var temp;
    if (total == 0) {
        temp = "100%";
    } else {
        temp = (value / total * 100).toFixed(0) + "%";
    }

    $("#prog").css("width", temp).text(temp);
}

//进度条操作事项：1 显示 2 隐藏 3 更新进度
gDrawingViewer.progressBar = null;

//初始化工具条，显示工具条
function showProgressbar() {
    //
    $('#progressBar').show();
    updateProgress(0, 100);
}

//关闭工具条，隐藏工具条
function hideProgressbar() {
    //
    updateProgress(100, 100)
    $('#progressBar').hide();
}

//工具条是否在运行
function progressBusing() {
    if ($('#progressBar').is(':hidden')) {
        return false;
    } else {
        return true;
    }
}

//设置进度条信息
function updateProgress(value, total) {
    var temp;
    if (total == 0) {
        temp = "100%";
    } else {
        temp = (value / total * 100).toFixed(0) + "%";
    }

    $("#prog").css("width", temp).text(temp);
    return;
}

//执行比对图纸
agDrawingCompare.doCompare = function () {
    try {
        //获取比对图纸
        var baseDrawing = null, otherDrawing = null;

        var layerImgs = $(".leaflet-image-layer");

        for (var i = 0; i < layerImgs.length; i++) {
            var mapViewer = layerImgs[i].parentElement.parentElement.parentElement;
            if (mapViewer.id == "drawings_viewer")
                baseDrawing = cv.imread(layerImgs[i]);
            else if (mapViewer.id == "compare_viewer")
                otherDrawing = cv.imread(layerImgs[i]);
        }

        //生成灰度图
        cv.cvtColor(baseDrawing, baseDrawing, cv.COLOR_RGBA2GRAY, 0);
        cv.cvtColor(otherDrawing, otherDrawing, cv.COLOR_RGBA2GRAY, 0);

        //执行相减操作
        var dst = new cv.Mat();
        var resultDiff = cv.absdiff(baseDrawing, otherDrawing, dst);
        cv.bitwise_not(dst, dst);

        //切换为彩色模式
        cv.cvtColor(dst, dst, cv.COLOR_GRAY2RGB, 0);
        cv.cvtColor(baseDrawing, baseDrawing, cv.COLOR_GRAY2RGB, 0);
        cv.cvtColor(otherDrawing, otherDrawing, cv.COLOR_GRAY2RGB, 0);
        var typeName = dst.type();
        var temBase = new cv.Mat(dst.rows, dst.cols, typeName, [255, 0, 0, 0]);
        cv.add(dst, temBase, dst);

        //加载基础图
        cv.addWeighted(dst, 0.3, baseDrawing, 0.7, 3, dst);
        var tem_canvas = document.getElementById("tem_compare_canvas");
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

        temBase.delete();
        dst.delete();
    } catch (err) {
        var output = err.message;
    }
}

//执行比对图纸
agDrawingCompare.DiffAddition = function () {
    var baseDrawing = null, otherDrawing = null, tem = null;
    try {
        //获取比对图纸
        var layerImgs = $(".leaflet-image-layer");

        for (var i = 0; i < layerImgs.length; i++) {
            var mapViewer = layerImgs[i].parentElement.parentElement.parentElement;
            if (mapViewer.id == "drawings_viewer") {
                var imgTem = document.createElement("img");
                imgTem.src = layerImgs[i].src;
                baseDrawing = cv.imread(imgTem);
            } else if (mapViewer.id == "compare_viewer") {
                var imgTem = document.createElement("img");
                imgTem.src = layerImgs[i].src;
                otherDrawing = cv.imread(imgTem);
            }
        }

        if (baseDrawing == null || otherDrawing == null) {
            swal("请加载比对图纸", "没有加载基础图纸或比对图纸。");
            $(".l-wrapper").hide();
            return;
        }

        if (baseDrawing.rows != otherDrawing.rows || baseDrawing.rows != otherDrawing.rows) {
            swal("无法比对", "图纸大小不同。");
            $(".l-wrapper").hide();
            return;
        }

        var typeName = baseDrawing.type();
        tem = new cv.Mat(baseDrawing.rows, baseDrawing.cols, typeName, [255, 0, 0, 0]);

        cv.subtract(baseDrawing, tem, tem);
        cv.bitwise_or(otherDrawing, tem, tem);

        var tem_canvas = document.getElementById("tem_compare_canvas");
        cv.imshow(tem_canvas, tem);

        //添加到地图
        var src = tem_canvas.toDataURL('image/jpg');
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
        addLayer2OtherMap(src);

        tem.delete();
        baseDrawing.delete();
        otherDrawing.delete();
        $(".l-wrapper").hide();
    } catch (err) {
        var output = err.message;
        swal("比对出现异常", err.message);
        if (baseDrawing != null)
            baseDrawing.delete();
        if (otherDrawing != null)
            otherDrawing.delete();
        if (tem != null)
            tem.delete();
        baseDrawing = null;
        otherDrawing = null;
        tem = null;
        $(".l-wrapper").hide();
    }
}


//执行比对图纸
agDrawingCompare.DiffAdditionTiles = function () {
    var that = this;
    $.loading({
        onOpen: $.proxy(function () {
            //获取比对图纸
            var baseCanvas = gDrawingViewer.base_canvas;
            var otherCanvas = agDrawingCompare.base_canvas;
            var arrayBaseTiles = getTilesFromCanvas(gDrawingViewer.canvasSource) || [];
            var arrayOtherTiles = getTilesFromCanvas(agDrawingCompare.canvasSource) || [];

            if (!arrayBaseTiles.length || !arrayOtherTiles.length) {
                swal("请加载比对图纸", "没有加载基础图纸或比对图纸。");
                $.loading.close();
                return
            }

            if (arrayBaseTiles.length != arrayOtherTiles.length || arrayBaseTiles[0].length != arrayOtherTiles[0].length) {
                swal("无法比对", "图纸大小不同。");
                $.loading.close();
                return
            }
            try {
                var otherImgData = [];
                var baseImgData = [];
                var X = gDrawingViewer.unit_size[0];
                var Y = gDrawingViewer.unit_size[1];
                var otherCtx = otherCanvas.getContext('2d');
                for (var i = 0; i < arrayBaseTiles.length; i++) {
                    for (var j = 0; j < arrayBaseTiles[0].length; j++) {
                        var src = cv.matFromImageData(arrayBaseTiles[i][j]);
                        var otherTile = cv.matFromImageData(arrayOtherTiles[i][j]);
                        var typeName = src.type();
                        var tem = new cv.Mat(src.rows, src.cols, typeName, [255, 0, 0, 0]);

                        cv.subtract(src, tem, tem);
                        cv.bitwise_or(otherTile, tem, tem);

                        var imgData = new ImageData(new Uint8ClampedArray(tem.data), tem.cols, tem.rows);
                        // imgData->temMat
                        otherImgData.push({data: imgData, dx: i * X, dy: j * Y});
                        // otherCtx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
                        tem.delete();
                        src.delete();
                        otherTile.delete();
                    }
                }

                var baseCtx = baseCanvas.getContext('2d');
                for (var i = 0; i < arrayBaseTiles.length; i++) {
                    for (var j = 0; j < arrayBaseTiles[0].length; j++) {
                        var src = cv.matFromImageData(arrayBaseTiles[i][j]);
                        var otherTile = cv.matFromImageData(arrayOtherTiles[i][j]);
                        var typeName = src.type();
                        var tem = new cv.Mat(src.rows, src.cols, typeName, [0, 255, 255, 0]);

                        cv.subtract(otherTile, tem, tem);
                        cv.bitwise_or(src, tem, tem);
                        var imgData = new ImageData(new Uint8ClampedArray(tem.data), tem.cols, tem.rows);
                        // imgData->temMat
                        baseImgData.push({data: imgData, dx: i * X, dy: j * Y});
                        // baseCtx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
                        tem.delete();
                        src.delete();
                        otherTile.delete();
                    }
                }
                window.requestAnimationFrame(function () {
                    otherImgData.forEach(function (item) {
                        otherCtx.putImageData(item.data, item.dx, item.dy);
                    })
                }, 100);
                window.requestAnimationFrame(function () {
                    baseImgData.forEach(function (item) {
                        baseCtx.putImageData(item.data, item.dx, item.dy);
                    })
                }, 100);
                /*        var typeName = baseDrawing.type();
                        tem = new cv.Mat(baseDrawing.rows,baseDrawing.cols,typeName,[255, 0, 0, 0]);

                        cv.subtract(baseDrawing,tem,tem);
                        cv.bitwise_or(otherDrawing,tem,tem);

                        var tem_canvas = document.getElementById("tem_compare_canvas");
                        cv.imshow(tem_canvas, tem);*/
            } catch (err) {
                var output = typeof err == 'object' ? err.message : err;
                $.loading.close();
                swal("比对出现异常", output);
            }
            //添加比对后的地图
            // var srcBase = baseCanvas.toDataURL('image/jpg');
            map.removeLayer(gDrawingViewer.map.layer);
            addImageLayer(baseCanvas);

            //添加到地图
            // var srcOther = otherCanvas.toDataURL('image/jpg');
            agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
            addLayer2OtherMap(otherCanvas);
            agCompareBox.init();
            $('.ag-filename-dsp').empty().append($('<p>').html(gDrawingViewer.fileName), $('<p>').html(agDrawingCompare.fileName));
            window.requestAnimationFrame(function () {
                $.loading.close();
            });
        }, that)
    });
}

//添加多行文本
Function.prototype.getMultilines = function () {
    var lines = new String(this);
    lines = lines.substring(lines.indexOf("/*") + 3, lines.lastIndexOf("*/"));
    return lines;
}

//显示差异窗口，如果未打开两张图比对图纸则退出。
function diffDailog() {
    if (agDrawingCompare.base_src == null) {
        swal("请先加载比对图纸", "未加载比对图纸");
        return;
    }
    var diffDlgString = function () {
        /*
         <div class="form-group m-form__group row">
				<label class="col-form-label">浏览模式</label>
          </div>
          <div class="form-group m-form__group row">
				<div class="col-12">
					<label class="m-radio m-radio--solid m-radio--brand">
                                            <input type="radio" selectcase="1" name="viewSpeed" onchange="gDrawingViewer.params.viewSpeed ('1');">快速
                                            <span></span>
                                        </label>&nbsp;
					<label class="m-radio m-radio--solid m-radio--brand">
                                            <input type="radio" selectcase="2" name="viewSpeed" onchange="gDrawingViewer.params.viewSpeed ('2');" checked="checked">普通
                                            <span></span>
                                        </label>&nbsp;
					<label class="m-radio m-radio--solid m-radio--brand">
                                            <input type="radio" selectcase="3" name="viewSpeed" onchange="gDrawingViewer.params.viewSpeed ('3');">高清
                                            <span></span>
                                        </label>
				</div>
			</div>
			<div class="form-group m-form__group row" id="drawScale" style="">
				<label class="col-4 col-form-label">
                                        比例尺
                </label>
				<div class="col-8">
					<div class="input-group">
						<input type="text" id="drawingScale" name="localCode" class="form-control moreCode" placeholder="比例尺">
					</div>
				</div>
			</div>
			<div class="form-group m-form__group row">
			    <label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label><label class="col-1 col-form-label">
                                        1
                </label>
			</div>
			<div class="form-group m-form__group row" id="drawScale" style="">
						<button class="btn btn-success" id="btnSubmit" type="button" onclick="gDrawingViewer.params.close_model();" style="padding: 5px 8px;">
                        确 认
                    </button>
			<button class="btn btn-info" id="btnCancel" type="button" onclick="gDrawingViewer.params.close_model();" style="padding: 5px 8px;">
                        取 消
            </button>
            </div>
        */
    }
    var temString = diffDlgString.getMultilines();

    if (!$('[data-method="diffDailog"]').hasClass('popover-done')) {
        $('[data-method="diffDailog"]').popover({
            html: true,
            content: diffDlgString.getMultilines(),
            placement: 'left',
            animation: false,
        }).on('shown.bs.popover', function () {
            var popId = ['#', $(this).attr('aria-describedby')].join('');
            var pop = this;
            $('.col', popId).on('click', function () {
                alert("hello world!");
                /*                gDrawingViewer.color = $(this).data('color');
                                changeColorFromLayer(gDrawingViewer.color);
                                $(pop).popover('hide');*/
            })
        }).popover('show').addClass('popover-done')
    }
}

//获取两张图纸不同内容的轮廓
function compareDrawingContours(base, dsp, map, color) {
    var data = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : '';
    var dfd, $promise;
    $promise = $.Deferred();
    if (Array.isArray(data)) {
        $.loading.close();
        agDrawingCompare.drawingContoursToMap.apply(base, [data, dsp, map, color, $promise]);
        return $promise.promise();
    }
    dfd = canvasToBlobAsync(base.base_canvas, dsp.base_canvas);
    $.when.apply($, dfd).done(function (blobBase, blobOther) {
        if (blobBase == null || blobOther == null) {
            swal("没有争取获取比对的两张图纸。", "请确认参加");
            return $.loading.close();
        }

        var url = ctx + "/cod/drawing/drawingContours.do";
        var formData = new FormData();
        formData.append("baseDrawing", blobBase);
        formData.append("otherDrawing", blobOther);
        formData.append("isDenoise", true);
        formData.append("keySize", 3);

        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (result) {
                var res = eval('(' + result + ')');
                $.loading.close();
                agDrawingCompare.drawingContoursToMap.apply(base, [res, dsp, map, color, $promise]);
                //createCompareMarker(result);
            },
            error: function (res) {
                $promise.reject(res);
                $.loading.close();
            }
        });
    });
    return $promise.promise();
}

//上传图纸到服务器
agDrawingCompare.uploadDrawing = function (base, dsp, map, color) {
    var data = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : '';
    var dfd, $promise;
    $promise = $.Deferred();
    if (Array.isArray(data)) {
        $.loading.close();
        agDrawingCompare.drawingMarker.apply(base, [data, dsp, map, color]);
        setTimeout(function () {
            $promise.resolve(data);
        });
        return $promise.promise();
    }
    if (agDrawingCompare.width % gDrawingViewer.width > 0 ||
        agDrawingCompare.height % gDrawingViewer.height > 0) {
        swal("图纸大小不相同，不进行比对。");
        return $.loading.close();
    }
    dfd = canvasToBlobAsync(base.base_canvas, dsp.base_canvas);
    $.when.apply($, dfd).done(function (blobBase, blobOther) {
        var url = ctx + "cod/drawing/uploadCompareDrawing.do";
        var formData = new FormData();
        formData.append("baseDrawing", blobBase);//测试不一致的内容
        formData.append("otherDrawing", blobOther);
        formData.append("fileNum", 2);
        formData.append("isDenoise", true);
        formData.append("keySize", 3);
        formData.append("maxCorners", 200);
        formData.append("minDistance", 100);
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (result) {
                var res = eval('(' + result + ')');
                $.loading.close();
                $promise.resolve(res);
                agDrawingCompare.drawingMarker.call(base, res, dsp, map, color);

                //createCompareMarker(result);
            },
            error: function (res) {
                $promise.reject(res);
                $.loading.close();
            }
        });
    });
    return $promise.promise();
}
var compareMarkerLayerGroup = L.layerGroup();

function createCompareMarker(result) {
    var icon = L.divIcon({
        iconSize: [16, 16],
        iconAnchor: [10, 15],
        popupAnchor: [-2, -10],
        html: '<div style="position:relative">' +
            '<img src=' + 'http://' + window.location.host + ctx + '/ui-static/aplanmis/cod/drawing/leaflet/images/compareMarker_16.png /></div>',
    });
    var arr = eval('(' + result + ')');
    for (var i = 0; i < arr.length; i++) {
        var lat = arr[i].x - gDrawingViewer.width / 2;
        var lng = arr[i].y - gDrawingViewer.height / 2;
        //旋转90度
        var latlng = [-lng, lat];
        var marker = L.marker(latlng, {icon: icon});
        compareMarkerLayerGroup.addLayer(marker);
    }
    map.addLayer(compareMarkerLayerGroup);
}

function clearCompareMarker() {
    if (compareMarkerLayerGroup != null) {
        compareMarkerLayerGroup.clearLayers();
    }
}

//根据dataUrl获取文件二进制对象
function dataURItoBlob(dataURI) {
    var byteString = atob(dataURI.split(',')[1]);

    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    var bb = new Blob([ab], {"type": mimeString});
    return bb;
}

//根据Canvas获取文件二进制对象
function canvasToBlobAsync() {
    if (!arguments.length) return;
    return $.makeArray(arguments).map(function (canvasElement) {
        var dfd = $.Deferred();
        canvasElement.toBlob(function (blob) {
            dfd.resolve(blob);
        });
        return dfd;
    });
}

agDrawingCompare.drawingContoursToMap = function (value, dsp, map, color, $promise) {
    var that = this;
    var xy = function (x, y) {
        var lat = x - that.width / 2;
        var lng = y - that.height / 2;
        //旋转90度
        var latlng = [-1 * lng, lat];
        return latlng;
    }
    // var customIcon = L.divIcon({
    //     className: 'leaflet-red-icon',
    //     iconSize: [12, 12]
    // });
    // var customBiggerIcon = L.divIcon({
    //     className: 'leaflet-red-icon',
    //     iconSize: [18, 18]
    // });
    var arr = value;
    var rects = arr.map(function (contour) {
        var rectTem = contour;
        var latlngs = [xy(contour.x, contour.y), xy(contour.x + contour.w, contour.y),
            xy(contour.x + contour.w, contour.y + contour.h), xy(contour.x, contour.y + contour.h)];

        return L.polygon(latlngs, {
            color: color[0] || '#000eff', fillColor: color[1] || '#0000ed',
            weight: 1
        });

        /*        return L.marker(xy(point), {
                    icon: customIcon
                }).on('mouseover', function () {
                    this.setIcon(customBiggerIcon);
                }).on('mouseout', function () {
                    this.setIcon(customIcon);
                })*/
    });
    var rectsGroup = dsp.rectsGroup = L.layerGroup(rects);
    dsp.polygons = rects;
    map.addLayer(rectsGroup);
    $promise.resolve(value);
}

agDrawingCompare.removeContours = function () {
    if (agDrawingCompare.rectsGroup != null)
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.rectsGroup);
    if (gDrawingViewer.rectsGroup != null)
        map.removeLayer(gDrawingViewer.rectsGroup);
}


// 绘制差异点--leaflet
agDrawingCompare.drawingMarker = function (value, dsp, map, color) {
    // var map = this.mapControl;
    // var yx = L.latLng;
    var that = this;
    var xy = function (point) {
        var lat = point.x - that.width / 2;
        var lng = point.y - that.height / 2;
        //旋转90度
        var latlng = [-lng, lat];
        if (L.Util.isArray(point)) {
            // return  map.layerPointToLatLng(map.panBy(point));
            return latlng;
        }
        //console.log(map.layerPointToLatLng(L.point(point.x, point.y)))
        // return map.layerPointToLatLng(L.point(point.x, point.y))
        return latlng;
    }
    var customIcon = L.divIcon({
        className: color || 'leaflet-red-icon',
        iconSize: [12, 12]
    });
    var customBiggerIcon = L.divIcon({
        className: color || 'leaflet-red-icon',
        iconSize: [18, 18]
    });
    var arr = value;
    var markers = arr.map(function (point) {
        return L.marker(xy(point), {
            icon: customIcon
        }).on('mouseover', function () {
            this.setIcon(customBiggerIcon);
        }).on('mouseout', function () {
            this.setIcon(customIcon);
        })
    });
    var markersGroup = dsp.markersGroup = L.layerGroup(markers);
    map.addLayer(markersGroup);
};

function removeDspMarkers() {
    agDrawingCompare.markersGroup && agDrawingCompare.mapControl.removeLayer(agDrawingCompare.markersGroup)
    gDrawingViewer.markersGroup && map.removeLayer(gDrawingViewer.markersGroup)
}

agDrawingCompare.overlyingDrawing = function () {
    try {
        var img = document.createElement("img");
        img.src = agDrawingCompare.base_src;
        var baseDrawing = cv.imread(img);

        var dst = new cv.Mat();
        cv.cvtColor(baseDrawing, baseDrawing, cv.COLOR_RGBA2GRAY, 0);
        // You can try more different parameters
        cv.adaptiveThreshold(baseDrawing, dst, 250, cv.ADAPTIVE_THRESH_GAUSSIAN_C, cv.THRESH_TOZERO, 3, 2);

        var tem_canvas = document.createElement("canvas");
        cv.imshow(tem_canvas, dst);
        var src = tem_canvas.toDataURL('image/jpg');
        agDrawingCompare.mapControl.removeLayer(agDrawingCompare.map.layer);
        addLayer2OtherMap(src);

        baseDrawing.delete();
        dst.delete();
    } catch (err) {
        swal("出现异常", err.messages.toString());
    }
}
// agDrawingCompare.drawingMarker.call(agDrawingCompare,value)