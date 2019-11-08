//图片路径
var markerpng = "http://" + window.location.host + ctx + "/preview/pdf/image/marker.png";

//九宫格
var nineCell = {
    ninelayerGroup_line: null,
    layers_line: [],
    ninelayerGroup_big: null,
    layers_big: [],
    rectangle_big: [],
    ninelayerGroup_small: null,
    layers_small: [],
    rectangle_small: [],
    elem: "",
    init: function () {
        if (map == null || map._layers == null) return;

        if ($(nineCell.elem).hasClass("selected")) {
            nineCell.clearNineCellLayers();
            return;
        }
        $(nineCell.elem).addClass("selected");
        var v_h = 0, h_w = 0,
            v_h_z = 0, h_w_z = 0;
        var ratio = 4;
        var layer_bounds = gDrawingViewer.map.layer._bounds;
        var leftbottom = layer_bounds._southWest;//左下角
        var righttop = layer_bounds._northEast;//右上角
        //目前 垂直是 纬度 lat，水平是经度 lng
        //地图中心点 在 屏幕中心
        var lefttop = {"lat": righttop.lat, "lng": leftbottom.lng};
        var rightbottom = {"lat": leftbottom.lat, "lng": righttop.lng};

        if (lefttop.lat * leftbottom.lat < 0)
            v_h = Math.abs(lefttop.lat) + Math.abs(leftbottom.lat);
        else {
        }

        if (leftbottom.lng * rightbottom.lng < 0)
            h_w = Math.abs(leftbottom.lng) + Math.abs(rightbottom.lng);
        else {
        }
        v_h_z = Math.round(v_h / ratio);
        h_w_z = Math.round(h_w / ratio);

        for (var i = 0; i < 5; i++) {
            var latlngs_h = []; //纵线 点数组
            var latlngs_v = []; //横线 点数组
            if (i == 0) {
                latlngs_h = [
                    [lefttop.lat, lefttop.lng],
                    [lefttop.lat, lefttop.lng + 150],
                    [righttop.lat, righttop.lng]
                ];

                latlngs_v = [
                    [lefttop.lat, lefttop.lng],
                    [lefttop.lat - 150, lefttop.lng],
                    [leftbottom.lat, leftbottom.lng]
                ];
            }
            else if (i == 4) {
                latlngs_h = [
                    [leftbottom.lat, leftbottom.lng],
                    [leftbottom.lat, leftbottom.lng + 150],
                    [rightbottom.lat, rightbottom.lng]
                ];

                latlngs_v = [
                    [righttop.lat, righttop.lng],
                    [righttop.lat - 150, righttop.lng],
                    [rightbottom.lat, rightbottom.lng]
                ];
            }
            else {
                latlngs_h = [
                    [lefttop.lat - v_h_z * i, lefttop.lng],
                    [lefttop.lat - v_h_z * i, lefttop.lng + 150],
                    [lefttop.lat - v_h_z * i, righttop.lng]
                ];

                latlngs_v = [
                    [lefttop.lat, lefttop.lng + h_w_z * i],
                    [lefttop.lat - 150, lefttop.lng + h_w_z * i],
                    [leftbottom.lat, leftbottom.lng + h_w_z * i]
                ];
            }
            nineCell.createLine(latlngs_h);
            nineCell.createLine(latlngs_v);

            if (i == 4) break;
            //最后一行的矩形 要采用 没有计算的坐标值，因为用了除法 会丢失精度
            if (i == 3) {
                nineCell.clacPointArrayByLeftBottomPoint(leftbottom, v_h_z, h_w_z);
                continue;
            }
            nineCell.clacPointArrayByLeftTopPoint(lefttop, v_h_z, i, h_w_z);
        }

        nineCell.ninelayerGroup_line = new L.layerGroup(nineCell.layers_line);
        map.addLayer(nineCell.ninelayerGroup_line);

        for (var s = 0; s < nineCell.rectangle_small.length; s++) {
            nineCell.createSmallPolygon(nineCell.rectangle_small[s], s);
        }
        nineCell.ninelayerGroup_small = new L.layerGroup(nineCell.layers_small);
        map.addLayer(nineCell.ninelayerGroup_small);
        // for (var i = 0; i < 16; i++) {
        //     $(".smallRect_" + i).after('<text x="400" y="200" style="z-index:1000">'+i+'</text>');//.html("<a>" + i + "</a>");
        // }
    },

    createLine: function (latlngs) {
        var polyline = new L.polyline(latlngs, {color: 'red', weight: "2"});
        nineCell.layers_line.push(polyline);
    },

    clacPointArrayByLeftTopPoint: function (lefttop, v_h_z, i, h_w_z) {
        var rec_b1 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z],
            [lefttop.lat - v_h_z * i, lefttop.lng]
        ];
        nineCell.rectangle_big.push(rec_b1);
        var rec_b2 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 2],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z * 2],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z]
        ];
        nineCell.rectangle_big.push(rec_b2);
        var rec_b3 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 2],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 3],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z * 3],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z * 2]
        ];
        nineCell.rectangle_big.push(rec_b3);
        var rec_b4 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 3],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 4],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z * 4],
            [lefttop.lat - v_h_z * i, lefttop.lng + h_w_z * 3]
        ];
        nineCell.rectangle_big.push(rec_b4);

        // var rec_s1 = [
        //     [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z - 10],
        //     [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z],
        //     [lefttop.lat - v_h_z * (i + 1) + 5, lefttop.lng + h_w_z],
        //     [lefttop.lat - v_h_z * (i + 1) + 5, lefttop.lng + h_w_z - 10]
        // ];
        var rec_s1 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 0.9],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 0.9]
        ];
        nineCell.rectangle_small.push(rec_s1);
        var rec_s2 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 1.9],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 2],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 2],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 1.9]
        ];
        nineCell.rectangle_small.push(rec_s2);
        var rec_s3 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 2.9],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 3],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 3],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 2.9]
        ];
        nineCell.rectangle_small.push(rec_s3);
        var rec_s4 = [
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 3.9],
            [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z * 4],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 4],
            [lefttop.lat - v_h_z * (i + 0.95), lefttop.lng + h_w_z * 3.9]
        ];
        nineCell.rectangle_small.push(rec_s4);
    },

    //使用下顶点
    clacPointArrayByLeftBottomPoint: function (leftbottom, v_h_z, h_w_z) {
        var rec_b1 = [
            [leftbottom.lat, leftbottom.lng],
            [leftbottom.lat, leftbottom.lng + h_w_z],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z],
            [leftbottom.lat + v_h_z, leftbottom.lng]
        ];
        nineCell.rectangle_big.push(rec_b1);
        var rec_b2 = [
            [leftbottom.lat, leftbottom.lng + h_w_z],
            [leftbottom.lat, leftbottom.lng + h_w_z * 2],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z * 2],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z]
        ];
        nineCell.rectangle_big.push(rec_b2);
        var rec_b3 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 2],
            [leftbottom.lat, leftbottom.lng + h_w_z * 3],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z * 3],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z * 2]
        ];
        nineCell.rectangle_big.push(rec_b3);
        var rec_b4 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 3],
            [leftbottom.lat, leftbottom.lng + h_w_z * 4],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z * 4],
            [leftbottom.lat + v_h_z, leftbottom.lng + h_w_z * 3]
        ];
        nineCell.rectangle_big.push(rec_b4);

        // var rec_s1 = [
        //     [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z - 10],
        //     [lefttop.lat - v_h_z * (i + 1), lefttop.lng + h_w_z],
        //     [lefttop.lat - v_h_z * (i + 1) + 5, lefttop.lng + h_w_z],
        //     [lefttop.lat - v_h_z * (i + 1) + 5, lefttop.lng + h_w_z - 10]
        // ];
        var rec_s1 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 0.9],
            [leftbottom.lat, leftbottom.lng + h_w_z],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 0.9]
        ];
        nineCell.rectangle_small.push(rec_s1);
        var rec_s2 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 1.9],
            [leftbottom.lat, leftbottom.lng + h_w_z * 2],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 2],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 1.9]
        ];
        nineCell.rectangle_small.push(rec_s2);
        var rec_s3 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 2.9],
            [leftbottom.lat, leftbottom.lng + h_w_z * 3],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 3],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 2.9]
        ];
        nineCell.rectangle_small.push(rec_s3);
        var rec_s4 = [
            [leftbottom.lat, leftbottom.lng + h_w_z * 3.9],
            [leftbottom.lat, leftbottom.lng + h_w_z * 4],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 4],
            [leftbottom.lat + v_h_z * 0.05, leftbottom.lng + h_w_z * 3.9]
        ];
        nineCell.rectangle_small.push(rec_s4);
    },

    //创建下标小矩形
    createSmallPolygon: function (latlngs, index) {
        var className = "smallRect_" + index;
        var polygon = new L.polygon(latlngs, {color: 'red', weight: "2", className: className});

        // var centerPoint=[latlngs[3][0]-5,latlngs[3][1]+10];
        // var myIcon = L.divIcon({
        //     className: 'my-div-icon',
        //     html:index,
        // });
        // //you can set .my-div-icon styles in CSS
        // var marker=new L.marker(centerPoint, {icon: myIcon});

        polygon.on('click', function (e) {
            nineCell.createBigPolygon(index)
        });
        // $("." + className).text(index);
        // layers_small.push(marker);
        nineCell.layers_small.push(polygon);
    },

    createBigPolygon: function (index) {
        var latlngs = nineCell.rectangle_big[index];
        var className = "big_" + index;
        //再次点击清除大矩形
        if (nineCell.ninelayerGroup_big != null) {
            for (var j = 0; j < nineCell.ninelayerGroup_big.getLayers().length; j++) {
                var tempLayer = nineCell.ninelayerGroup_big.getLayers()[j];
                if (tempLayer.options.className == className) {
                    nineCell.ninelayerGroup_big.removeLayer(tempLayer);
                    for (var m = 0; m < nineCell.layers_big.length; m++) {
                        if (nineCell.layers_big[m].options.className == className) {
                            nineCell.layers_big.splice(m, 1);
                            return;
                        }
                    }
                }
            }
        }

        var polygon = new L.polygon(latlngs, {color: 'blue', weight: "2", className: className});
        nineCell.layers_big.push(polygon);
        if (nineCell.ninelayerGroup_big)
            nineCell.ninelayerGroup_big.clearLayers();
        nineCell.ninelayerGroup_big = new L.layerGroup(nineCell.layers_big);
        map.addLayer(nineCell.ninelayerGroup_big);

        //先移除小的图层组，再加进来，因为会被大矩形遮挡，导致无法触发点击事件
        nineCell.ninelayerGroup_small.clearLayers();
        nineCell.ninelayerGroup_small = L.layerGroup(nineCell.layers_small);
        map.addLayer(nineCell.ninelayerGroup_small);
        // for (var i = 0; i < 16; i++) {
        //     $(".smallRect_" + i).after('<text x="400" y="200" style="z-index:1000">'+i+'</text>');//.html("<a>" + i + "</a>");
        // }
    },

    //清除9宫格相关数据
    clearNineCellLayers: function () {
        if ($(nineCell.elem).hasClass("selected") == false) return;
        if (nineCell.elem == '') return;
        $(nineCell.elem).removeClass("selected");
        nineCell.ninelayerGroup_line.clearLayers();
        nineCell.ninelayerGroup_small.clearLayers();
        if (nineCell.ninelayerGroup_big)
            nineCell.ninelayerGroup_big.clearLayers();
        nineCell.layers_line.splice(0, nineCell.layers_line.length);
        nineCell.layers_big.splice(0, nineCell.layers_big.length);
        nineCell.rectangle_big.splice(0, nineCell.rectangle_big.length);
        nineCell.layers_small.splice(0, nineCell.layers_small.length);
        nineCell.rectangle_small.splice(0, nineCell.rectangle_small.length);

    },
}
// 面积测量方法
var areaMeasure = {
    points: [],
    color: "red",
    layers: L.layerGroup(),
    polygon: null,
    elem: '',
    init: function () {
        areaMeasure.points = [];
        areaMeasure.polygon = null;
        map.on('click', areaMeasure.click).on('dblclick', areaMeasure.dblclick);

        $(areaMeasure.elem).addClass("selected");
    },
    close: function () {
        var lab = rectangleMeasure.tips.getLabel();
        var tt = document.createTextNode(rectangleMeasure.tips.getLabel()._content);
        lab._container.innerHTML = "";
        lab._container.appendChild(tt);
        var span = document.createElement("span");
        span.innerHTML = "【关闭】";
        span.style.color = "#00ff40";
        lab._container.appendChild(span);
        L.DomEvent.addListener(span, "click", function () {
            rectangleMeasure.destory();
        });
    },
    click: function (e) {
        map.doubleClickZoom.disable();
        // 添加点信息
        areaMeasure.points.push(e.latlng);
        // 添加面
        map.on('mousemove', areaMeasure.mousemove);
    },
    mousemove: function (e) {
        areaMeasure.points.push(e.latlng);
        if (areaMeasure.polygon)
            map.removeLayer(areaMeasure.polygon);
        areaMeasure.polygon = L.polygon(areaMeasure.points, {showMeasurements: true, color: 'red'});
        //areaMeasure.polygon.addTo(map);
        areaMeasure.polygon.addTo(areaMeasure.layers);
        areaMeasure.layers.addTo(map);
        areaMeasure.points.pop();
    },
    dblclick: function (e) { // 双击结束
        areaMeasure.polygon.addTo(areaMeasure.layers);
        //areaMeasure.polygon.on("contextmenu", areaMeasure.destory)
        common.addCloseMarker(areaMeasure.points[0], areaMeasure.polygon, areaMeasure.layers);
        //areaMeasure.polygon.enableEdit();
        map.on('editable:vertex:drag editable:vertex:deleted', areaMeasure.polygon.updateMeasurements, areaMeasure.polygon);
        map.off('click', areaMeasure.click).off('mousemove', areaMeasure.mousemove).off('dblclick', areaMeasure.dblclick);
        if ($(areaMeasure.elem).hasClass("selected")) {
            $(areaMeasure.elem).removeClass("selected");
        }
    },
    destory: function (e) {
        //areaMeasure.layers.removeLayer(e.target);
    },
    clear: function () {
        if (areaMeasure.points == null || areaMeasure.points.length == 0) return;
        areaMeasure.points.splice(0, areaMeasure.points.length);
        areaMeasure.layers.clearLayers();
        map.removeLayer(areaMeasure.polygon);
    }
}
// 距离测量方法
var lengthMeasure = {
    points: [],
    color: "red",
    layers: L.layerGroup(),
    polyline: null,
    elem: '',
    init: function () {
        lengthMeasure.points = [];
        lengthMeasure.polyline = null;
        map.on('click', lengthMeasure.click).on('dblclick', lengthMeasure.dblclick);

        $(lengthMeasure.elem).addClass("selected");

    },
    close: function () {
        var lab = rectangleMeasure.tips.getLabel();
        var tt = document.createTextNode(rectangleMeasure.tips.getLabel()._content);
        lab._container.innerHTML = "";
        lab._container.appendChild(tt);
        var span = document.createElement("span");
        span.innerHTML = "【关闭】";
        span.style.color = "#00ff40";
        lab._container.appendChild(span);
        L.DomEvent.addListener(span, "click", function () {
            rectangleMeasure.destory();
        });
    },
    click: function (e) {
        map.doubleClickZoom.disable();
        // 添加点信息
        lengthMeasure.points.push(e.latlng);
        // 添加线
        map.on('mousemove', lengthMeasure.mousemove);
    },
    mousemove: function (e) {
        lengthMeasure.points.push(e.latlng);
        if (lengthMeasure.polyline)
            map.removeLayer(lengthMeasure.polyline);
        lengthMeasure.polyline = L.polyline(lengthMeasure.points, {showMeasurements: true, color: 'red'});
        //areaMeasure.polygon.addTo(map);
        lengthMeasure.polyline.addTo(lengthMeasure.layers);
        lengthMeasure.layers.addTo(map);
        lengthMeasure.points.pop();
    },
    dblclick: function (e) { // 双击结束
        lengthMeasure.polyline.addTo(lengthMeasure.layers);
        // lengthMeasure.polyline.on("contextmenu", lengthMeasure.destory)
        common.addCloseMarker(lengthMeasure.points[0], lengthMeasure.polyline, lengthMeasure.layers);
        //areaMeasure.polygon.enableEdit();
        map.on('editable:vertex:drag editable:vertex:deleted', lengthMeasure.polyline.updateMeasurements, lengthMeasure.polyline);
        map.off('click', lengthMeasure.click).off('mousemove', lengthMeasure.mousemove).off('dblclick', lengthMeasure.dblclick);
        if ($(lengthMeasure.elem).hasClass("selected")) {
            $(lengthMeasure.elem).removeClass("selected");
        }
    },
    destory: function (e) {
        // lengthMeasure.layers.removeLayer(e.target);
    },
    clear: function () {
        if (lengthMeasure.points == null || lengthMeasure.points.length == 0) return;
        lengthMeasure.points.splice(0, lengthMeasure.points.length);
        lengthMeasure.layers.clearLayers();
        map.removeLayer(lengthMeasure.polyline);
    }
}

var common = {
    addCloseMarker: function (latlng, shape, layers) {
        if (latlng == null || shape == null || layers == null) return;
        var myIcon = L.divIcon({
            className: 'my-div-icon',
            html: "X",
        });
        var point = [latlng.lat, latlng.lng - 2];
        var marker = new L.marker(point, {icon: myIcon});
        marker.on("click", function () {
            layers.removeLayer(marker);
            layers.removeLayer(shape);
        });
        marker.addTo(layers);
    },
    getNowTime: function (date) {
        //格式化日，如果小于9，前面补0
        var day = ("0" + date.getDate()).slice(-2);
        //格式化月，如果小于9，前面补0
        var month = ("0" + (date.getMonth() + 1)).slice(-2);
        return date.getFullYear() + "-" + (month) + "-" + (day);
    },
}
//添加意见标记
var addOptionMarker = {
    elem: "",
    markerLayerGroup: new L.layerGroup(),
    drawingId: "111",
    currentMarker: null,
    currentLatlng: [],
    isNew: false,
    xh: 1,
    init: function () {
        if (addOptionMarker.elem == '') return;
        if (addOptionMarker.drawingId == '' || addOptionMarker.drawingId == undefined) return;
        if (map == null) return;
        if ($(addOptionMarker.elem).hasClass("selected")) {
            map.off("click", addOptionMarker.click);
            $(addOptionMarker.elem).removeClass("selected");
            addOptionMarker.isNew = false;
            return;
        }
        if (addOptionMarker.currentMarker) {
            addOptionMarker.currentMarker.closePopup();
            addOptionMarker.currentMarker = null;
        }
        $(addOptionMarker.elem).addClass("selected");
        map.on("click", addOptionMarker.click);
        if (map.hasLayer(addOptionMarker.markerLayerGroup) == false)
            map.addLayer(addOptionMarker.markerLayerGroup);
        addOptionMarker.isNew = true;
    },
    click: function (e) {
        var latlng = e.latlng;
        addOptionMarker.createMarker(latlng, "", common.getNowTime(new Date()), addOptionMarker.xh);
        map.off("click", addOptionMarker.click);
        $(addOptionMarker.elem).removeClass("selected");
    },
    createMarker: function (latlng, content, date, xh) {
        //使用图纸唯一id和经纬度值标识标记名称
        var className = addOptionMarker.drawingId + '_' + latlng.lat + '_' + latlng.lng;
        var icon = L.divIcon({
            className: 'myDivIcon',
            iconSize: [60, 60],
            iconAnchor: [33, 59],
            popupAnchor: [-3, -45],
            html: '<div style="position:relative">' +
            '<img src=' + markerpng + '/>' +
            '<p id="markerxh" style="position:absolute;top:8px;left:25px;color:#fff;font-size:14px">' + xh + '</p></div>',
        });
        var marker = L.marker(latlng, {className: className, icon: icon});
        var html = addOptionMarker.createHtml(content);
        var popup = L.popup({maxWidth: 620}).setContent(html);
        marker.bindPopup(popup).addTo(addOptionMarker.markerLayerGroup);
        marker.on("popupopen", function () {
            addOptionMarker.currentMarker = marker;
            addOptionMarker.currentLatlng = marker.getLatLng();
            $("#datepick").val(date);
            // addOptionMarker.initDatepicker("#datepick");
            if (addOptionMarker.isNew == false) {
                $('#notSaveBtn').text("关闭");
            }
            else
                $('#notSaveBtn').text("放弃");
        });
        //当新插入标记后，点击关闭弹出框，默认不保存标记到数据库
        marker.on("popupclose", function () {
            if (addOptionMarker.isNew)
                addOptionMarker.notSave();
        });
        if (addOptionMarker.isNew)
            marker.openPopup();
    },
    createHtml: function (content) {
        var giveupbutton = "";
        if (addOptionMarker.isNew)
            giveupbutton = "<button  type='button' id='notSaveBtn' onclick='addOptionMarker.notSave()'>放弃</button>";
        var html = "<div><h3>修改意见</h3><form id='formopinion' style='width:600px'><table class='opinion'>" +
            "<tr><td style='width: 10%;'><span>图号：</span></td>" +
            "<td><label id='drawingNum'>" + addOptionMarker.drawingId +
            "</label></td></tr>" +
            "<tr><td style='width: 10%;'><span>类型：</span></td>" +
            "<td><select id='class' style='width: 100%;height:30px' ><option>违反专业规范、规程和设计深度不足</option>></select></td></tr>" +
            "<tr><td style='width: 10%;'><span>专业：</span></td>" +
            "<td><div id='profession'><input type='radio' name='profession' checked='checked' value='消防'>消防</input>" +
            "<input type='radio' name='profession' value='人防' style='margin-left:5px'>人防</input></div></td></tr>" +
            "<tr><td style='width: 10%;'><span>日期：</span></td><td><input type='text' id='datepick' readonly='readonly'></td></tr>" +
            "<tr style='height:100px'><td colspan='2'>" +
            "<textarea id='opinionContent' style='width: 100%;height:150px;resize:none'>" + content +
            "</textarea></td></tr>" +
            "<tr><td style='width: 100%;' colspan='2'>" +
            // "<button type='button'>加粗</button>" +
            // "<button type='button'>插入特殊符号</button>" +
            // "<button type='button'>插入图片</button>" +
            // "<button type='button'>引用规范</button>" +
            // "<button type='button'>常用意见</button>" +
            // "<button type='button'>收藏</button>" +
            "<button type='button' onclick='addOptionMarker.saveOpinion()'>保存</button>" + giveupbutton +
            "</td></tr></table></form></div>";
        return html;
    },
    //初始化日期下拉框
    initDatepicker: function (tagId) {
        var datepicker = $(tagId).datepicker({
            todayBtn: 'linked',
            format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh',
            clearBtn: true,
            todayHighlight: true,
            templates: {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        });
    },
    createMarkerByDrawingId: function (drawingId) {
        if (drawingId == '' || drawingId == undefined) return;
        if (map.hasLayer(addOptionMarker.markerLayerGroup) === false)
            map.addLayer(addOptionMarker.markerLayerGroup);
        addOptionMarker.drawingId = drawingId;
        $.ajax({
            url: ctx + '/cod/drawing/getListCodDrawingMarkerInfo.do',
            type: 'post',
            data: {drawingId: drawingId},
            success: function (arr) {
                $.each(arr, function (i, item) {
                    var latlng = {lat: item.lat, lng: item.lng};
                    var content = item.opinionContent;
                    var date = common.getNowTime(new Date(item.createdate));
                    var xh = item.xh;
                    addOptionMarker.createMarker(latlng, content, date, i + 1);
                });
                addOptionMarker.xh = arr.length + 1;
            },
            error: function (e) {

            },
        });
    },
    saveOpinion: function () {
        addOptionMarker.isNew = false;
        var date = new Date();
        var createdate = date.getFullYear() + '-' + date.getMonth() + '-' + date.getDay();
        var data = {
            drawingId: $('#drawingNum').text(),
            lat: addOptionMarker.currentLatlng.lat,
            lng: addOptionMarker.currentLatlng.lng,
            createuser: "",
            createdate: createdate,
            opinionContent: $("#opinionContent").val(),
            opinionType: $("#class").val(),
            opinionProfession: $('[name=profession]:checked').val(),
        };
        $.ajax({
            url: ctx + "/cod/drawing/saveCodDrawingMarkerInfo.do",
            type: "post",
            data: data,
            success: function () {

                $("#opinionContent").html(data.opinionContent);
                addOptionMarker.xh++;
                addOptionMarker.currentMarker.closePopup();
                // agcloud.ui.metronic.showSuccessTip("保存意见成功！");
            },
            error: function () {

                // agcloud.ui.metronic.showErrorTip("保存意见失败！");
            },
        });
    },
    notSave: function () {
        if (addOptionMarker.currentMarker == null) return;
        if (addOptionMarker.isNew == false) return;
        addOptionMarker.markerLayerGroup.removeLayer(addOptionMarker.currentMarker);
        addOptionMarker.isNew = false;
    },
    openOpinions: function () {
        var opinion = $('#opinionTable');
        if (opinion.css('display') == 'block') {
            opinion.css('display', 'none');
            return;
        }
        opinion.css('display', 'block');
        $.ajax({
            url: ctx + '/cod/drawing/getListCodDrawingMarkerInfo.do',
            type: 'post',
            data: {drawingId: addOptionMarker.drawingId},
            success: function (arr) {
                opinion.empty();
                var ol = "<ol>"
                $.each(arr, function (i, item) {
                    var latlng = {lat: item.lat, lng: item.lng};
                    var content = item.opinionContent;
                    var date = common.getNowTime(new Date(item.createdate));
                    var li = '<li>' + content + '</li>';
                    ol += li;
                });
                ol += '</ol>';
                opinion.append(ol);
                opinion.draggable().click(function () {
                    $(this).draggable({disable: false});
                    $(this).css('backgroundColor', 'transparent');
                }).dblclick(function () {
                    $(this).draggable({disable: true});
                    $(this).css('backgroundColor', '#eeeaf1');
                });
            },
            error: function (e) {

            },
        });
    }
}