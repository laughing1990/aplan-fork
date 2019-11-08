;
void !function () {
    $.loading = function (config) {
        var options = $.extend({
            draggable: false,
            backgroundDismissAnimation: '',
            title: false,
            closeIcon: false,
            type: 'loading',
            content: '<div class="text-center"><img src="' + ctx + '/checkdrawing/css/images/loading-circle.gif"></div>',
            onOpenBefore: function () {
                this.$body.css({
                    'background-color': 'transparent',
                    'box-shadow': 'none'
                })
                this.$jconfirmBg.css('background-color', '#ccc');
            },
        }, config)
        return $.dialog(options)
    }
    $.loading.closeAll = function () {
        jconfirm.instances.filter(function (ins) {
            return ins.type === 'loading'
        }).forEach(function (ins) {
            ins.close();
        })
        ;
    }
    $.loading.close = function () {
        jconfirm.instances.length && jconfirm.instances.slice(-1)[0].close();
    }
}();
$(function () {
    InitGlobalEvent();
})
var apiParameters = {
    cMapPacked: true,
    cMapUrl: "../web/cmaps/",
    disableAutoFetch: false,
    disableCreateObjectURL: false,
    disableFontFace: false,
    disableRange: false,
    disableStream: false,
    isEvalSupported: true,
    maxImageSize: -1,
    pdfBug: false,
    postMessageTransfers: true,
    verbosity: 1
}
//图纸浏览基本变量
var viewerDefault = {
    //施工图对象
    map: {
        //施工图图层
        layer: {}
    },
    bounds: [],
    width: 0,
    height: 0,
//基础图纸
    base_url: '',
    base_src: {},
//当前Pdf对象
    pdf_document: {},
    pageNum: 1,
    pageRendering: !!0,
    pdfViewer: {},
    rotation: 0,
    scale: 0.8,
    paperSize: 5000.0,
    base_canvas: {},
    base_ctx: {},
    pageNumPending: {},
//比对图纸
    compare_url: '',
    compare_src: '',
//图纸canvas对象
    map_canvas_src: {},
//图纸Mat对象
    map_cv_src: {},
//图纸背景颜色
    back_color: {
        white: [0, 0, 0, 0], //白色
        light_blue: [32, 16, 0, 0], //浅蓝
        light_green: [44, 0, 35, 0], //浅绿
        light_red: [0, 37, 70, 0], //浅红
        base_src: {}//修改颜色图纸
    },
//图片基础大小，执行图片运算操作时，将大图分割为1024X1024大小的图片
    unit_size: [1024, 1024],
}
var map = null;
var bounds = null;
var width = 0;
var height = 0;
var opinionParm = {
    width: 80,
    height: 40,
    fontSize: 0.75,
    zoom: -2,
    lastChangeZoom: null
}
if (typeof (gDrawingViewer) === "undefined")
    gDrawingViewer = {};
$.extend(true, gDrawingViewer, viewerDefault);

//初始化全局事件
function InitGlobalEvent() {
    //初始化工具条按钮提示
    $('[data-toggle="tooltip"]').tooltip();
    $('.ag-toolbar-item', '#agToolBar').each(function () {
        $(this).mouseenter(function () {
            $(this).css('opacity', 1)
        }).mouseleave(function () {
            $(this).css('opacity', 0.2)
        });
    });
    // $('[data-toggle="popover"]').popover();

    //初始化工具条事件
    var GlobalMethod = {
        resize: resize,
        lock: Lock,
        unlock: UnLock,
        rotateViewer: rotateViewer,
        initGlass: $.proxy(magnifyingGlass, 'initGlass'),
        initCells: $.proxy(nineCell, 'init'),
        screenShot: $.proxy(screenshotTool, 'init'),
        openCogModel: $.proxy(gDrawingViewer, 'OpenModel'),
        setLengthMeasure: $.proxy(lengthMeasure, 'init'),
        setAreaMeasure: $.proxy(areaMeasure, 'init'),
        addOptionMarker: addOptionMarker1,
        openOpinions: $.proxy(addOptionMarker, 'openOpinions'),
        openFile: openFile,
        openOtherFile: openOtherDrawing,
        compare: $.proxy(agDrawingCompare, 'DiffAdditionTiles'),
        setColor: setPdfColor,
        //addCollection: addCollection
        OpenReviewModel: $.proxy(agReview, 'init'),
        clearMeasure: $.proxy(common, 'clearMeasureShape'),
        openDrawings: $.proxy(agDrawings, 'init')
        //addCollection: addCollection
    }
    $('button', '[data-toggle="toolbar"]').on('click', function () {
        var name = $(this).data('method');
        var options = $(this).data('option');
        if (!name) return;
        if (typeof options === 'undefined') {
            return GlobalMethod[name](this);
        }
        return GlobalMethod[name](options);
    });

    //初始化地图
    addMap('drawings_viewer');
    $.loading();
    PDFOverlay(_pdf_url, 1);
    gDrawingViewer.fileName =_pdf_url;
}


/**
 * 添加地图
 * */
function addMap(element) {
    var maxBounds = [
        [-2 * gDrawingViewer.height, -2 * gDrawingViewer.width],
        [gDrawingViewer.height * 2, gDrawingViewer.width * 2]
    ];
    var map = L.map(element, {
        crs: L.CRS.Simple,
        maxZoom: 4,
        minZoom: -4,
        maxBounds: maxBounds,
        zoomControl: false,
        attributionControl: false,
        doubleClickZoom: false
    });
    gDrawingViewer.maxBounds = maxBounds
    gDrawingViewer.map = map;

    map.on('mousemove', function (e) {
        var html = JSON.stringify(e.containerPoint) + " _ " + map._zoom +
            "<br />" + JSON.stringify(e.latlng);
        $("#info").html(html);
    });
    map.on("zoomend", function (e) {
        var els = $(".opinion-div-icon");
        var len = els.length;
        if (len == 0) return;
        var usezoom = 0;
        if (opinionParm.lastChangeZoom == null) {
            usezoom = opinionParm.zoom;
        } else {
            usezoom = opinionParm.lastChangeZoom;
        }
        var changZoom = Math.abs(usezoom - e.target._zoom);
        if (changZoom < 1)
            return;
        opinionParm.lastChangeZoom = e.target._zoom;
        var ratio = e.target._zoom < 0 ? (1 - changZoom / 6 + 0.1) : (1 + changZoom / 6);
        for (var i = 0; i < len; i++) {
            var el = els[i];
            var marginleft = $(el).css("margin-left");
            var leftvalue = parseInt(marginleft);
            $(el).children().css({
                "height": ratio * opinionParm.height + "px",
                "width": ratio * opinionParm.width + "px"
            });
            if (e.target._zoom < -2 || e.target._zoom > 2) {
                $(el).css("display", "none");
            } else $(el).css("display", "");
            // $(el).children().children().css("font-size", ratio * opinionParm.fontSize / 2 + "rem");
            // if (e.target._zoom > 2) {
            //     var trans = $(el).css("transform");
            //     var reg=/matrix.(((-)?([0-9]+.)?\d+([, ]+)?){6})./g;
            //     var arr=reg.exec(trans);
            //     var newarr = arr[1].split(/[, ]+/g);
            //     switch (e.target._zoom){
            //         case 3:
            //             if(leftvalue<0)
            //                 newarr[4]=Number(newarr[4])+20;
            //             else
            //                 newarr[4]=Number(newarr[4])-20;
            //             newarr[5]=Number(newarr[5])+20;
            //             break;
            //         case 4:
            //             if(leftvalue<0)
            //                 newarr[4]=Number(newarr[4])+40;
            //             else
            //                 newarr[4]=Number(newarr[4])-40;
            //             newarr[5]=Number(newarr[5])+60;
            //             break;
            //         // case 5:
            //         //     newarr[4]=Number(newarr[4])/1.2;
            //         //     newarr[5]=Number(newarr[5])/3.5;
            //         //     break;
            //         // case 6:
            //         //     newarr[4]=Number(newarr[4])/1.8;
            //         //     newarr[5]=Number(newarr[5])/4.5;
            //         //     break;
            //     }
            //     var transform="matrix(";
            //     for (var j = 0, leng = newarr.length; j < leng; j++) {
            //             transform += newarr[j];
            //         if(j!=leng-1) transform+=",";
            //     }
            //     transform+=")";
            //     $(el).css("transform", transform);
            // }
        }
    });
    return map;
}

/**
 * 设置PDF背景色
 * */
function setPdfColor() {
    if (!$('[data-method="setColor"]').hasClass('popover-done')) {
        $('[data-method="setColor"]').popover({
            html: true,
            content: '<div class="container" style="width: 50px;"><div class="row" >\n' +
                '  <div class="col color-white" data-color="white"></div>\n' +
                '  <div class="w-100"></div>\n' +
                '  <div class="col color-blue" data-color="light_blue"></div>\n' +
                '  <div class="w-100"></div>\n' +
                '  <div class="col color-green" data-color="light_green"></div>\n' +
                '  <div class="w-100"></div>\n' +
                '  <div class="col color-yellow" data-color="light_red"></div>\n' +
                '</div></div>',
            placement: 'left',
            animation: false,
        }).on('shown.bs.popover', function () {
            var popId = ['#', $(this).attr('aria-describedby')].join('');
            var pop = this;
            $('.col', popId).on('click', function () {
                gDrawingViewer.color = $(this).data('color');
                changeColorFromLayer(gDrawingViewer.color);
                $(pop).popover('hide');
            })
        }).popover('show').addClass('popover-done')
    }
}

/**
 * 清除所有Layer
 * */
function clearLayer(viewer) {
    return viewer.layerGroup.clearLayers();
}

//添加意见标记
function addOptionMarker1() {
    if (!$('[data-method="addOptionMarker"]').hasClass('popover-done')) {
        $('[data-method="addOptionMarker"]').popover({
            html: true,
            content: '<ul class="markercontainer fa-ul" >\n' +
                '  <li><i class="fa-li fa fa-map-marker fa-lg" style="font-size: 24px;" data-type="point" title="点标记"></i>&nbsp;</li>\n' +
                '  <li><i class="fa-li iconfont icon-polyline fa-lg" data-type="line" title="线标记"></i>&nbsp;</li>\n' +
                '  <li><i class="fa-li fa fa-circle-thin fa-lg" data-type="circle" title="圆形标记"></i>&nbsp;</li>\n' +
                '  <li><i class="fa-li iconfont icon-ellipse fa-lg" style="font-size:16px" data-type="ellipse" title="椭圆标记"></i>&nbsp;</li>\n' +
                '  <li><i class="fa-li fa fa-square-o fa-lg" data-type="rectangle" title="矩形标记"></i>&nbsp;</li>\n' +
                '  <li><i class="fa-li fa fa-cog fa-lg" data-type="setStyle" title="样式设置"></i>&nbsp;</li>\n' +
                '</ul>',
            placement: 'left',
            animation: false
        }).on('shown.bs.popover', function () {
            var popId = ['#', $(this).attr('aria-describedby')].join('');
            var pop = this;
            $('.fa-li', popId).on('click', function () {
                var type = $(this).data('type');
                switch (type) {
                    case "point":
                        addOptionMarker.init(this);
                        break;
                    case "line":
                        multiFunctionMarker.createTextMarker();
                        break;
                    case "circle":
                        multiFunctionMarker.createCircle();
                        break;
                    case "ellipse":
                        multiFunctionMarker.createEllipse();
                        break;
                    case "rectangle":
                        multiFunctionMarker.createRectangle();
                        break;
                    case "setStyle":
                        multiFunctionMarker.openConfig();
                        break;
                }
                $(pop).popover('hide');
            })
        }).popover('show').addClass('popover-done')
    }
}

//通过pngurl加载地图
function load_drawings() {
    try {
        $.loading();
        return PDFOverlay(_pdf_url, 1);
        //获取项目编号
        var temId = itemid;
        var tem_canvas = document.getElementById("tem_canvas");
        renderPdf2Canvas(_pdf_url, 1, tem_canvas);
        document.agBaseViewer = gDrawingViewer;
    } catch (e) {
        $.loading.close();
    }
}

//初始化地图
function init_map(src) {
    bounds = [
        [-2 * gDrawingViewer.height, -2 * gDrawingViewer.width],
        [gDrawingViewer.height * 2, gDrawingViewer.width * 2]
    ];
    map = L.map('drawings_viewer', {
        crs: L.CRS.Simple,
        maxZoom: 4,
        minZoom: -4,
        maxBounds: bounds,
        zoomControl: false,
        attributionControl: false,
        doubleClickZoom: false
    });

    bounds = [
        [-0.5 * gDrawingViewer.height, -0.5 * gDrawingViewer.width],
        [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]
    ];
    gDrawingViewer.map.layer = L.imageOverlay(src, bounds).addTo(map);

    map.fitBounds(bounds);
    common.clearShape();
    map.on('mousemove', function (e) {
        var html = JSON.stringify(e.containerPoint) + " _ " + map._zoom +
            "<br />" + JSON.stringify(e.latlng);
        $("#info").html(html);
    });
    map.on("zoomend", function (e) {
        var els = $(".opinion-div-icon");
        var len = els.length;
        if (len == 0) return;
        var usezoom = 0;
        if (opinionParm.lastChangeZoom == null) {
            usezoom = opinionParm.zoom;
        } else {
            usezoom = opinionParm.lastChangeZoom;
        }
        var changZoom = Math.abs(usezoom - e.target._zoom);
        if (changZoom < 1)
            return;
        opinionParm.lastChangeZoom = e.target._zoom;
        var ratio = e.target._zoom < 0 ? (1 - changZoom / 6 + 0.1) : (1 + changZoom / 6);
        for (var i = 0; i < len; i++) {
            var el = els[i];
            var marginleft = $(el).css("margin-left");
            var leftvalue = parseInt(marginleft);
            $(el).children().css({
                "height": ratio * opinionParm.height + "px",
                "width": ratio * opinionParm.width + "px"
            });
            if (e.target._zoom < -2 || e.target._zoom > 2) {
                $(el).css("display", "none");
            } else $(el).css("display", "");
            // $(el).children().children().css("font-size", ratio * opinionParm.fontSize / 2 + "rem");
            // if (e.target._zoom > 2) {
            //     var trans = $(el).css("transform");
            //     var reg=/matrix.(((-)?([0-9]+.)?\d+([, ]+)?){6})./g;
            //     var arr=reg.exec(trans);
            //     var newarr = arr[1].split(/[, ]+/g);
            //     switch (e.target._zoom){
            //         case 3:
            //             if(leftvalue<0)
            //                 newarr[4]=Number(newarr[4])+20;
            //             else
            //                 newarr[4]=Number(newarr[4])-20;
            //             newarr[5]=Number(newarr[5])+20;
            //             break;
            //         case 4:
            //             if(leftvalue<0)
            //                 newarr[4]=Number(newarr[4])+40;
            //             else
            //                 newarr[4]=Number(newarr[4])-40;
            //             newarr[5]=Number(newarr[5])+60;
            //             break;
            //         // case 5:
            //         //     newarr[4]=Number(newarr[4])/1.2;
            //         //     newarr[5]=Number(newarr[5])/3.5;
            //         //     break;
            //         // case 6:
            //         //     newarr[4]=Number(newarr[4])/1.8;
            //         //     newarr[5]=Number(newarr[5])/4.5;
            //         //     break;
            //     }
            //     var transform="matrix(";
            //     for (var j = 0, leng = newarr.length; j < leng; j++) {
            //             transform += newarr[j];
            //         if(j!=leng-1) transform+=",";
            //     }
            //     transform+=")";
            //     $(el).css("transform", transform);
            // }
        }
    });
}


//加载施工图到MapaddImageLayer
function addImageLayer(src) {
    bounds = [
        [-0.5 * gDrawingViewer.height, -0.5 * gDrawingViewer.width],
        [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]
    ];
    if (typeof src === 'object' && 'nodeName' in src && src.nodeName === 'CANVAS') {
        gDrawingViewer.map.layer = L.svgOverlay(src, bounds).addTo(map);
    } else {
        gDrawingViewer.map.layer = L.imageOverlay(src, bounds).addTo(map);
    }
    common.removeSVGLayer();
    map.fitBounds(bounds);
    common.clearShape();
}


//将pdf渲染到canvas
function renderPdf2Canvas(file, page_index, canvas, flag) {

    try {
        if ($.isEmptyObject(file) || !file) return;
        // var workPath = "http://" + window.location.host + ctx +
        //     '/ui-static/aplanmis/cod/pdf/js/pdf.worker.js';
        // var workPath = "http://" + window.location.host + ctx +
        //     '/ui-static/aplanmis/cod/drawing/pdfjs-2.0/build/pdf.worker.js';
        PDFJS.workerSrc = workPath;
        //TODO:验证文件的有效性
        var parameters = Object.create(null);
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
        var downloadComplete = false;
        var loadingTask = PDFJS.getDocument(parameters);
        $('#loadingBar').removeClass('hidden');

        function progress(level) {
            if (downloadComplete) {
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
            downloadComplete = true;
            window.requestAnimationFrame(function () {
                $('#loadingBar').addClass('hidden').width('0%');
            });
            gDrawingViewer.pdf_document = pdf;
            gDrawingViewer.pageNumPending = page_index;

            pdf.getPage(page_index).then(function (page) {


                // var pageType = typeof (page);
                //最大分辨率 5000左右 GPU不支持
                var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation)
                var factor = fitScale(viewport);
                gDrawingViewer.scale = gDrawingViewer.scale * factor;
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
                // /**
                //  * PDF渲染到SVG
                //  * PDF直接输出为SVG，然后SVG整体渲染到Leaflet
                //  * 优点，矢量图，全高清，确定，事件反应出现卡顿，延时在1秒左右
                //  * */
                // page.getOperatorList().then(function (opList) {
                //     var svgGfx = new pdfjsLib.SVGGraphics(page.commonObjs, page.objs);
                //     svgGfx.embedFonts=!0;
                //     svgGfx.getSVG(opList, viewport).then(function (svg) {
                //         console.log('svg', svg,opList);
                //         bounds = [
                //             [-0.5 * gDrawingViewer.height, -0.5 * gDrawingViewer.width],
                //             [0.5 * gDrawingViewer.height, 0.5 * gDrawingViewer.width]
                //         ];
                //         if($.isEmptyObject(map)){
                //             map = L.map('drawings_viewer', {
                //                 crs: L.CRS.Simple,
                //                 maxZoom: 4,
                //                 minZoom: -4,
                //                 maxBounds: bounds,
                //                 zoomControl: false,
                //                 attributionControl: false,
                //             });
                //         }
                //         gDrawingViewer.map.layer = L.svgOverlay(svg, bounds).addTo(map);
                //     });
                // });

                page.render(renderContext).then(function () {//renderDrawing
                    $.loading.close();
                    toastr.remove();
                    window.requestAnimationFrame(function () {
                        gDrawingViewer.pageRendering = false;
                        toastr.success('图纸渲染完毕', '提示信息', {timeOut: 1500});
                    });
                    var src = canvas.toDataURL('image/webp');
                    gDrawingViewer.base_src = src;
                    if (!$.isEmptyObject(gDrawingViewer.map.layer)) {
                        map.removeLayer(gDrawingViewer.map.layer);
                        addImageLayer(src);
                    } else
                        init_map(src);
                    if (!$.isEmptyObject(gDrawingViewer.map_cv_src)) {
                        gDrawingViewer.map_cv_src.delete();
                        gDrawingViewer.map_cv_src = null;
                    }
                    initPageToolbar();
                    addOptionMarker.createMarkerByDrawingId('2019-440115-36-02-800166-001');
                });
            }, function (exception) {
                console.log(exception)
            });
        });
    } catch
        (err) {
        var temOutput = err.message;
        $.loading.close();
    }
}

/**
 * PDF渲染到Canvas
 * 先输出白屏Canvas到Leaflet，接着直接在Canvas渲染。
 * 优点，节省Canvas转图片和图片到Leaflet的等待时间，实现可见的动态渲染。
 * 缺点，清晰度受分辨率限制。
 * */
function PDFOverlay(file, idx) {
    var parameters, loadComplete, loadingTask;
    if (gDrawingViewer.pageRendering) return;
    gDrawingViewer.pageRendering = true;
    if (($.isEmptyObject(file) && !('byteLength' in file)) || !file) return;
    $('#loadingBar').removeClass('hidden');
    pdfjsLib.workerSrc = workPath
    loadComplete = false;
    parameters = Object.create(null);
    if (typeof file === 'string') { // URL
        parameters.url = file;
        gDrawingViewer.source = {url: file, originalUrl: file};
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

        // typeof gDrawingViewer.source.url =='string' && gDrawingViewer.source.originalUrl && window.URL.revokeObjectURL( gDrawingViewer.source.url);
        loadComplete = true;
        window.requestAnimationFrame(function () {
            $('#loadingBar').addClass('hidden').width('0%');
        });
        gDrawingViewer.pdf_document = pdf;
        gDrawingViewer.pageNumPending = idx;

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
            gDrawingViewer.page = page;
            gDrawingViewer.pdfViewer = viewport;
            gDrawingViewer.base_canvas = canvas;
            gDrawingViewer.base_ctx = cxt;
            gDrawingViewer.height = canvas.height;
            gDrawingViewer.width = canvas.width;


            bounds = [
                [-0.5 * canvas.height, -0.5 * canvas.width],
                [0.5 * canvas.height, 0.5 * canvas.width]
            ]
            map = gDrawingViewer.map;
            map.setMaxBounds([
                [-2 * canvas.height, -2 * canvas.width],
                [2 * canvas.height, 2 * canvas.width]
            ]);
            gDrawingViewer.map.layer && map.removeLayer(gDrawingViewer.map.layer)
            var canvasLayer = L.svgOverlay(canvas, bounds).addTo(map);
            gDrawingViewer.map.layer = canvasLayer;
            common.removeSVGLayer();
            map.fitBounds(bounds);
            common.clearShape();
            // gDrawingViewer.map.bgLayer = L.svgOverlay(bgCanvas, bounds).addTo(map);
            // gDrawingViewer.map.bgLayer.setOpacity(0.3)
            var renderContext = {
                canvasContext: cxt,
                viewport: viewport
            };
            page.render(renderContext).then(function () {
                $.loading.close();
                toastr.remove();
                window.requestAnimationFrame(function () {
                    gDrawingViewer.pageRendering = false;
                    toastr.success('图纸渲染完毕', '提示信息', {timeOut: 1500});
                });
                // if (typeof gDrawingViewer.canvasSource === 'undefined') {
                    var _canvasElement = canvas.cloneNode(true);
                    _canvasElement.getContext('2d').putImageData(gDrawingViewer.base_ctx.getImageData(0, 0, gDrawingViewer.width, gDrawingViewer.height), 0, 0);
                    gDrawingViewer.canvasSource = _canvasElement;
                // }
            });
        });
    });
}

function reloadPDFOverlay(page, scale, rotation, map) {
    var loading = $.loading();
    this.scale = scale;
    this.rotation = rotation;
    var viewport = page.getViewport(scale, rotation),
        canvas = document.createElement('canvas'),
        cxt = canvas.getContext('2d'),
        // bgCanvas,
        // bgCxt,
        bounds;
    canvas.width = viewport.width;
    canvas.height = viewport.height;
    // bgCanvas = canvas.cloneNode(true);
    // bgCxt = bgCanvas.getContext('2d')
    this.pdfViewer = viewport;
    this.base_canvas = canvas;
    this.base_ctx = cxt;
    this.height = canvas.height;
    this.width = canvas.width;

    bounds = [
        [-0.5 * canvas.height, -0.5 * canvas.width],
        [0.5 * canvas.height, 0.5 * canvas.width]
    ];

    map.setMaxBounds([
        [-2 * canvas.height, -2 * canvas.width],
        [2 * canvas.height, 2 * canvas.width]
    ]);
    if(this.map.layer){
        map.removeLayer(this.map.layer);
    }
    var canvasLayer = L.svgOverlay(canvas, bounds).addTo(map);
    this.map.layer = canvasLayer;
    common.removeSVGLayer();
    map.fitBounds(bounds);
    common.clearShape();
    // gDrawingViewer.map.bgLayer = L.svgOverlay(bgCanvas, bounds).addTo(map);
    // gDrawingViewer.map.bgLayer.setOpacity(0.3)
    var renderContext = {
        canvasContext: cxt,
        viewport: viewport
    };
    page.render(renderContext).then($.proxy(function (canvas, loading) {
        var that = this;
        loading.close();
        toastr.remove();
        window.requestAnimationFrame(function () {
            that.pageRendering = false;
            toastr.success('图纸渲染完毕', '提示信息', {timeOut: 1500});
        });
        // if (typeof this.canvasSource !== 'undefined') {
            var _canvasElement = canvas.cloneNode(true);
            _canvasElement.getContext('2d').putImageData(that.base_ctx.getImageData(0, 0, canvas.width, canvas.height), 0, 0);
            this.canvasSource = _canvasElement;
        // }
    }, this, canvas, loading));
}

//根据页码渲染该页内容
function renderPage(num) {
    gDrawingViewer.pageRendering = true;
    // Using promise to fetch the page
    if (null === gDrawingViewer.pdf_document || num < 1) return;
    gDrawingViewer.pdf_document.getPage(num).then(function (page) {
        gDrawingViewer.pageNumPending = num;
        var viewport = page.getViewport(gDrawingViewer.scale, gDrawingViewer.rotation);
        var factor = fitScale(viewport);
        gDrawingViewer.scale = gDrawingViewer.scale * factor;
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
            if (!$.isEmptyObject(gDrawingViewer.map_cv_src)) {
                gDrawingViewer.map_cv_src.delete();
                gDrawingViewer.map_cv_src = null;
            }
            if (hasClass) {
                nineCell.init(nineCell.elem);
                nineCell.createExistBigRectByRotate(nineCell.bigRectInfo);
                hasClass = false;
            }
            addOptionMarker.createMarkerByAction();
        });
    });

    // Update page counters
    $("#pageNum").text(gDrawingViewer.pageNum);
}

function fitScale(viewport) {
    var maxPixel = viewport.height > viewport.width ? viewport.height : viewport.width;

    if ("undefined" != gDrawingViewer.params) {
        if (gDrawingViewer.params.speed === 1) {
            gDrawingViewer.paperSize = 3000.0;
        } else if (gDrawingViewer.params.speed === 2) {
            gDrawingViewer.paperSize = 5000.0;
        } else if (gDrawingViewer.params.speed === 3) {
            gDrawingViewer.paperSize = 8000.0;
        }
    }
    var factor = parseInt((gDrawingViewer.paperSize / maxPixel) * 100);
    factor = factor / 100.0;
    if (factor < 1) factor = 1;
    if (maxPixel > 8000.0)
        factor = gDrawingViewer.paperSize / maxPixel;
    return factor;
}

//初始化翻页工具条
function initPageToolbar() {
    console.log('初始化翻页工具条。。。')
    if (null === gDrawingViewer.pdf_document) return;
    if (gDrawingViewer.pdf_document.numPages == 1) {
        $("#toolbarPageNum").hide();
    } else {
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
        reloadPDFOverlay.apply(gDrawingViewer, [gDrawingViewer.page, gDrawingViewer.scale, gDrawingViewer.rotation, gDrawingViewer.map]);
        if (typeof agDrawingCompare.page !== 'undefined') {
            reloadPDFOverlay.apply(agDrawingCompare, [agDrawingCompare.page, gDrawingViewer.scale, gDrawingViewer.rotation, agDrawingCompare.mapControl]);
        }
        // renderPage(num);
    }
}

//显示下一页
function onNextPage() {
    if (gDrawingViewer.pageNum >= gDrawingViewer.pdf_document.numPages) {
        return;
    }
    gDrawingViewer.pageNum++;
    queueRenderPage(gDrawingViewer.pageNum);
    addOptionMarker.createMarkerByAction();
    $("#pageNum").text(gDrawingViewer.pageNum);
}

//显示上一页
function onPrevPage() {
    if (gDrawingViewer.pageNum <= 1) {
        return;
    }
    gDrawingViewer.pageNum--;
    queueRenderPage(gDrawingViewer.pageNum);
    addOptionMarker.createMarkerByAction();
    $("#pageNum").text(gDrawingViewer.pageNum);
}

//执行旋转操作
function rotateViewer(degree) {
    if ($.isEmptyObject(map)) {
        return swal("请加载图纸", "没有加载图纸。");
    }
    hasClass = !!nineCell.elem && $(nineCell.elem).hasClass("viewer-toolbar-sel") ? true : false;
    common.clearShape();
    rotatePages(degree);
}

var hasClass = '';

//逆时针90度旋转地图
function doRotateViewer(counter) {
    try {
        if (map == null) {
            swal("请加载图纸", "没有加载图纸。");
            return;
        }
        hasClass = nineCell.elem != '' && $(nineCell.elem).hasClass("viewer-toolbar-sel") ? true : false;
        document.body.style.cursor = "wait";
        common.clearShape();
        if (!counter)
            rotatePages(-90);
        else
            rotatePages(90);

        addOptionMarker.createMarkerByAction();
        document.body.style.cursor = "default";
        $(".l-wrapper").hide();
    } catch (err) {
        var temOutput = err.message;
        document.body.style.cursor = "default";
        $(".l-wrapper").hide();
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
    if (tem_canvas.width < gDrawingViewer.unit_size[0] &&
        tem_canvas.height < gDrawingViewer.unit_size[1])
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

//切分图片为多个张图
function getTilesFromCanvas(canvas) {
    var ctx = canvas.getContext("2d");
    if (canvas.width < gDrawingViewer.unit_size[0] &&
        canvas.height < gDrawingViewer.unit_size[1])
        return;

    var iWidth = parseInt(canvas.width / gDrawingViewer.unit_size[0]);
    var iHeight = parseInt(canvas.height / gDrawingViewer.unit_size[1]);
    var matArray = new Array();

    for (var i = 0; i < iWidth; i++) {
        matArray[i] = new Array();
        for (var j = 0; j < iHeight; j++) {
            matArray[i][j] = ctx.getImageData(
                (i) * gDrawingViewer.unit_size[0], (j) * gDrawingViewer.unit_size[1],
                gDrawingViewer.unit_size[0], gDrawingViewer.unit_size[1]);
        }
    }
    var modWidth = canvas.width % gDrawingViewer.unit_size[0];
    var modHeight = canvas.height % gDrawingViewer.unit_size[1];
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

//获取分块切图
function getMatTiles() {
    var tem_canvas = document.getElementById("tem_canvas");

    if (null == gDrawingViewer.map_cv_src) {
        gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
    }
    cv.copyTo()
    var rect = new cv.Rect(100, 100, 200, 200);
    var dst = gDrawingViewer.map_cv_src.roi(rect);
}

/**
 * 打开图纸
 * */
function openFile() {
    var fileInput = $("<input type='file' accept='application/pdf'/>");
    fileInput.change(function () {
        $.loading();
        var file = this.files[0];
        //console.log(file)
        toastr.info('正在初始化图纸', '提示信息', {timeOut: 0, extendedTimeOut: 0});
        gDrawingViewer.pageNum = 1;
        // if(!~navigator.userAgent.indexOf('Trident')) {
        //     gDrawingViewer.source = {url: URL.createObjectURL(file), originalUrl: file.name};
        //     return   PDFOverlay(gDrawingViewer.source, gDrawingViewer.pageNum);
        // }
        gDrawingViewer.fileName = file.name;
        var reader = new FileReader();
        reader.onload =function(e) {
            gDrawingViewer.source =e.target.result;
            PDFOverlay(e.target.result, gDrawingViewer.pageNum);
        };
        reader.readAsArrayBuffer(file);
        // cancelLink();
    }).click();
}

//执行分块执行
function tileChangeColor_bak(color) {
    try {
        if (!progressBusing()) showProgressbar();
        else return;

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
        if (map == null) {
            swal("请先加载基础图纸", "没有加载基础图纸。", "info");
            return;
        }
        getMatTiles();

        var tem_canvas = document.getElementById("tem_canvas");

        if (null == gDrawingViewer.map_cv_src) {
            gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
        }
        if (color == 0) {
            map.removeLayer(gDrawingViewer.map.layer);
            addImageLayer(gDrawingViewer.base_src);
            return;
        }

        cv.imshow(tem_canvas, gDrawingViewer.map_cv_src);

        //if (!progressBusing()) showProgressbar(); else return;

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

        var ctx = tem_canvas.getContext("2d");
        //分为多个小块
        var matArray = getDrawingTiles();
        var total = matArray.length * matArray[0].length;
        subChange(ctx, matArray, 0, total, backColor);
    } catch (err) {
        var temOutput = err.message;
        swal("切换颜色出现异常", err.message);
    }
}

function changeColor(color) {
    gDrawingViewer.color = color;
    $(".l-wrapper").show();
    setTimeout(function () {
            changeColorFromLayer(gDrawingViewer.color)
        },
        200
    );
}

//TODO: 图片切片变换颜色
function changeColorFromLayer(color) {
    if ($.isEmptyObject(map)) {
        return swal("请先加载基础图纸", "没有加载基础图纸。", "info");
    }

    if (!color) {
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(gDrawingViewer.base_src);
        return;
    }
    var backColor = gDrawingViewer.back_color[color];
    try {
        //图纸碎片
        // var baseCanvas = document.createElement("canvas");
        var layerImgs = $(".leaflet-image-layer");
        var arrayBaseTiles = null;
        layerImgs.each(function () {
            var mapViewer = $(this).closest('.leaflet-container');
            var imgTem;
            if ($(mapViewer).is('#drawings_viewer')) {
                // var nw = this.naturalWidth,
                //     nh = this.naturalHeight;
                // imgTem = new Image(nw, nh);
                // imgTem.src = gDrawingViewer.base_src; //修改为原始备份
                // baseCanvas.width = gDrawingViewer.width;
                // baseCanvas.height = gDrawingViewer.height;
                // var layerCanvas = convertImageToCanvas(imgTem);
                if (typeof gDrawingViewer.canvasSource === 'undefined') {
                    var canvasElement = document.createElement('canvas');
                    var _ctx = canvasElement.getContext('2d');
                    canvasElement.width = gDrawingViewer.width;
                    canvasElement.height = gDrawingViewer.height;
                    _ctx.putImageData(gDrawingViewer.base_ctx.getImageData(0, 0, gDrawingViewer.width, gDrawingViewer.height), 0, 0);
                    gDrawingViewer.canvasSource = canvasElement;
                }
                arrayBaseTiles = getTilesFromCanvas(gDrawingViewer.canvasSource);
                return;
            }
            // imgTem = document.createElement("img");
            // imgTem.src = this.src;
            //otherDrawing = cv.imread(imgTem);
        })

        if (arrayBaseTiles == null) {
            $(".l-wrapper").hide();
            swal("没有获取到图片对象", "未获取图片");
            return;
        }

        // var baseCtx = baseCanvas.getContext('2d');
        for (var i = 0; i < arrayBaseTiles.length; i++) {
            for (var j = 0; j < arrayBaseTiles[0].length; j++) {
                var src = cv.matFromImageData(arrayBaseTiles[i][j]);
                var typeName = src.type();
                var tem = new cv.Mat(src.rows, src.cols, typeName, backColor);
                cv.absdiff(tem, src, tem);

                /*var src = cv.matFromImageData(tem);*/
                var imgData = new ImageData(new Uint8ClampedArray(tem.data), tem.cols, tem.rows);
                // imgData->temMat
                gDrawingViewer.base_ctx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
                tem.delete();
                src.delete();
            }
        }
        // gDrawingViewer.base_ctx.putImageData(baseCtx.getImageData(0,0,baseCanvas.width,baseCanvas.height),0,0)
        // var src = baseCanvas.toDataURL('image/jpg');
        // map.removeLayer(gDrawingViewer.map.layer);
        // addImageLayer(src);
    } catch (err) {
        swal("修改背景色出现异常", err.message);
    }
}

// 把image 转换为 canvas对象
function convertImageToCanvas(image) {
    // 创建canvas DOM元素，并设置其宽高和图片一样
    var canvas = document.createElement("canvas");
    canvas.width = image.width;
    canvas.height = image.height;
    // 坐标(0,0) 表示从此处开始绘制，相当于偏移。
    canvas.getContext("2d").drawImage(image, 0, 0);
    return canvas;
}

function subChange(ctx, matArray, k, total, backColor) {
    var i = parseInt(k / matArray[0].length);
    var j = k % matArray[0].length;
    var temMat = matArray[i][j];
    {
        var src = cv.matFromImageData(temMat);
        //change_mat_rgb(src, backColor);
        //TODO:执行变换颜色

        var typeName = src.type();
        var tem = new cv.Mat(src.rows, src.cols, typeName, backColor);
        cv.absdiff(tem, src, tem);

        /*var src = cv.matFromImageData(tem);*/
        var imgData = new ImageData(new Uint8ClampedArray(tem.data), tem.cols, tem.rows);
        // imgData->temMat
        ctx.putImageData(imgData, i * gDrawingViewer.unit_size[0], j * gDrawingViewer.unit_size[1]);
        tem.delete();
        src.delete();
        //updateProgress(k + 1, total);
    }
    k++;
    if (k < total)
        setTimeout(function () {
            subChange(ctx, matArray, k, total, backColor)
        }, 10);
    else {
        var tem_canvas = document.getElementById("tem_canvas");
        var src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);

        gDrawingViewer.map_cv_src.delete();
        gDrawingViewer.map_cv_src = null;
        //hideProgressbar();
    }
}

//修改背景颜色
function doChangeColor(mat, color) {
    try {
        tileChangeColor();
        return;
        var tem_canvas = document.getElementById("tem_canvas");
        if (null == gDrawingViewer.map_cv_src) {
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
function changeColor_del(color) {
    if (map == null) {
        swal("请先加载基础图纸", "没有加载基础图纸。", "error");
        return;
    }

    var tem_canvas = document.getElementById("tem_canvas");

    if (null == gDrawingViewer.map_cv_src) {
        gDrawingViewer.map_cv_src = cv.imread(tem_canvas);
    }
    if (color == 0) {
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(gDrawingViewer.base_src);
        return;
    }
    var srcTem = new cv.Mat();
    var typeName = gDrawingViewer.map_cv_src.type();
    var tem = null;
    if (color == 1) {
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [32, 16, 0, 0]); //浅蓝
    } else if (color == 2) {
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [44, 0, 35, 0]); //浅绿
    } else if (color == 3) {
        tem = new cv.Mat(gDrawingViewer.map_cv_src.rows, gDrawingViewer.map_cv_src.cols,
            typeName, [0, 37, 70, 0]); //浅红
    }
    cv.absdiff(tem, gDrawingViewer.map_cv_src, srcTem);
    try {
        cv.imshow(tem_canvas, srcTem);
        tem.delete();
        srcTem.delete();
        var src = tem_canvas.toDataURL('image/jpg');
        map.removeLayer(gDrawingViewer.map.layer);
        addImageLayer(src);
    } catch (err) {
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
    if (map == null) {
        swal("请加载图纸", "没有加载图纸。");
        return;
    }
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
        if (gDrawingViewer.map_cv_src == null) {
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
    if ($.isEmptyObject(map) || $.isEmptyObject(gDrawingViewer.map.layer))
        return swal("请加载图纸", "没有加载图纸。");
    map.fitBounds(map.options.maxBounds);

}

function UnDragging() {
    map.off('mousemove', OnMouseMove)
        .off('mouseup', OnMouseUp)
        .once('mousemove', OnMouseMove);
}

function OnMouseMove() {
    map.once('mouseup', OnMouseUp);
}

function OnMouseUp() {
    toastr.remove();
    toastr.warning('图纸已锁定,无法移动', '操作提示', {timeOut: 1500});
}

//解锁图纸
function UnLock() {
    map.dragging.enable();
    map.off('mousedown', UnDragging);
    toastr.remove();
    toastr.success('解锁图纸成功', '操作提示', {timeOut: 1500});
}

//锁定图纸
function Lock() {
    map.dragging.disable();
    map.on('mousedown', UnDragging);
    toastr.remove();
    toastr.success('锁定图纸成功', '操作提示', {timeOut: 1500});
}