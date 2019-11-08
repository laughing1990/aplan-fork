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
    samllRectSVG: L.svg(),
    bigRectInfo: [],
    ratio: 4,
    arrRowOne: [0, 3, 15, 12],
    arrRowTwo: [1, 7, 14, 8],
    arrRowThree: [2, 11, 13, 4],
    arrRowFour: [5, 6, 10, 9],
    init: function (el) {
        nineCell.elem = el;
        if (map == null || map._layers == null) return;
        common.offEvent('');
        if ($(nineCell.elem).hasClass("viewer-toolbar-sel")) {
            nineCell.bigRectInfo = [];
            nineCell.clearNineCellLayers();
            return;
        }
        $(nineCell.elem).addClass("viewer-toolbar-sel");
        var v_h = 0, h_w = 0,
            v_h_z = 0, h_w_z = 0;
        var layer_bounds = gDrawingViewer.map.layer._bounds;
        var leftbottom = layer_bounds._southWest;//左下角
        var righttop = layer_bounds._northEast;//右上角
        //地图中心点 在 图纸中心
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
        v_h_z = Math.round(v_h / nineCell.ratio);
        h_w_z = Math.round(h_w / nineCell.ratio);

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
            } else if (i == 4) {
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
            } else {
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
        var polygon = new L.polygon(latlngs, {
            color: 'red',
            weight: "2",
            className: className,
            renderer: nineCell.samllRectSVG,
        });

        // var centerPoint=[latlngs[3][0],latlngs[3][1]];
        // var myIcon = L.divIcon({
        //     className: 'my-div-icon',
        //     iconSize:[16,16],
        //     iconAnchor: [0, 0],
        //     html:"<p>"+index+"</p>",
        // });
        // var marker=new L.marker(centerPoint, {icon: myIcon});

        polygon.on('click', function (e) {
            nineCell.createBigPolygon(index)
        });
        //$("." + className).text(index);
        // nineCell.layers_small.push(marker);
        nineCell.layers_small.push(polygon);
    },

    createBigPolygon: function (index) {
        var latlngs = nineCell.rectangle_big[index];
        var className = "big_" + index;
        var existIndex = -1;
        var notChangeIndex = nineCell.calcIndexByRotate({"index": index, "rotateAngle": gDrawingViewer.rotation});
        for (var i = 0, len = nineCell.bigRectInfo.length; i < len; i++) {
            if (nineCell.bigRectInfo[i].index == notChangeIndex) {
                existIndex = i;
                break;
            }
        }
        if (existIndex == -1) {
            var temp = [];
            temp.index = notChangeIndex;
            temp.rotateAngle = gDrawingViewer.rotation;
            nineCell.bigRectInfo.push(temp);
        } else nineCell.bigRectInfo.splice(existIndex, 1);

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
    },

    //清除9宫格相关数据
    clearNineCellLayers: function () {
        if ($(nineCell.elem).hasClass("viewer-toolbar-sel") == false) return;
        if (nineCell.elem == '') return;
        $(nineCell.elem).removeClass("viewer-toolbar-sel");
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

    createExistBigRectByRotate: function (arr) {
        for (var i = 0; i < arr.length; i++) {
            var index = nineCell.calcIndexByRotate(arr[i]);

            var latlngs = nineCell.rectangle_big[index];
            var className = "big_" + index;
            var polygon = new L.polygon(latlngs, {color: 'blue', weight: "2", className: className});
            nineCell.layers_big.push(polygon);
            if (nineCell.ninelayerGroup_big)
                nineCell.ninelayerGroup_big.clearLayers();
            nineCell.ninelayerGroup_big = new L.layerGroup(nineCell.layers_big);
            map.addLayer(nineCell.ninelayerGroup_big);
        }
        //先移除小的图层组，再加进来，因为会被大矩形遮挡，导致无法触发点击事件
        nineCell.ninelayerGroup_small.clearLayers();
        nineCell.ninelayerGroup_small = L.layerGroup(nineCell.layers_small);
        map.addLayer(nineCell.ninelayerGroup_small);
    },
    calcIndexByRotate: function (item) {
        var index_add = -1;
        var angle = gDrawingViewer.rotation - item.rotateAngle;
        if (angle == 0 || angle == -360)
            return item.index;
        else if (angle == 90 || angle == -270) {
            index_add = 1;
        } else if (angle == 180 || angle == -180) {
            index_add = 2;
        } else index_add = 3;
        var index_one = nineCell.arrRowOne.indexOf(item.index);
        if (index_one >= 0) {
            return nineCell.arrRowOne[(index_one + index_add) % 4];
        }
        var index_two = nineCell.arrRowTwo.indexOf(item.index);
        if (index_two >= 0) {
            return nineCell.arrRowTwo[(index_two + index_add) % 4];
        }
        var index_three = nineCell.arrRowThree.indexOf(item.index);
        if (index_three >= 0) {
            return nineCell.arrRowThree[(index_three + index_add) % 4];
        }
        var index_four = nineCell.arrRowFour.indexOf(item.index);
        if (index_four >= 0) {
            return nineCell.arrRowFour[(index_four + index_add) % 4];
        }
    },
}
// 面积测量方法
var areaMeasure = {
    points: [],
    color: "red",
    layers: L.layerGroup(),
    polygon: null,
    elem: '',
    init: function (el) {
        areaMeasure.elem = el;
        common.offEvent("areaMeasure");
        if ($(areaMeasure.elem).hasClass("viewer-toolbar-sel")) {
            $(areaMeasure.elem).removeClass("viewer-toolbar-sel");
            if (areaMeasure.points.length > 0) {
                areaMeasure.dblclick(1);
            }
            return;
        } else $(areaMeasure.elem).addClass("viewer-toolbar-sel");
        areaMeasure.points = [];
        areaMeasure.polygon = null;
        map.on('click', areaMeasure.click).on('contextmenu', areaMeasure.dblclick);
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
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
        $(".leaflet-interactive").css({"cursor": "crosshair"});
    },
    dblclick: function (e) { // 双击结束
        areaMeasure.polygon.addTo(areaMeasure.layers);
        //areaMeasure.polygon.on("contextmenu", areaMeasure.destory)
        common.addCloseMarker(areaMeasure.polygon.getBounds().getCenter(), areaMeasure.polygon, areaMeasure.layers);
        map.on('editable:vertex:drag editable:vertex:deleted', areaMeasure.polygon.updateMeasurements, areaMeasure.polygon);
        map.off('click', areaMeasure.click).off('mousemove', areaMeasure.mousemove).off('contextmenu', areaMeasure.dblclick);
        if ($(areaMeasure.elem).hasClass("viewer-toolbar-sel")) {
            $(areaMeasure.elem).removeClass("viewer-toolbar-sel");
            common.setCursorForMeasure("#drawings_viewer", "");
            $(".leaflet-interactive").css({"cursor": ""});
        }
    },
    destory: function (e) {
        //areaMeasure.layers.removeLayer(e.target);
    },
    clear: function () {
        if (areaMeasure.points == null || areaMeasure.points.length == 0) return;
        areaMeasure.points.splice(0, areaMeasure.points.length);
        areaMeasure.layers.clearLayers();
        //map.removeLayer(areaMeasure.polygon);
    }
}
// 距离测量方法
var lengthMeasure = {
    points: [],
    color: "red",
    layers: L.layerGroup(),
    polyline: null,
    elem: '',
    lastPoint: null,
    init: function (el) {
        lengthMeasure.elem = el;
        common.offEvent("lengthMeasure");
        if ($(lengthMeasure.elem).hasClass("viewer-toolbar-sel")) {
            $(lengthMeasure.elem).removeClass("viewer-toolbar-sel");
            if (lengthMeasure.lastPoint != null) {
                lengthMeasure.dblclick(1);
                lengthMeasure.lastPoint = null;
            }
            return;
        } else $(lengthMeasure.elem).addClass("viewer-toolbar-sel");
        lengthMeasure.points = [];
        lengthMeasure.polyline = null;
        map.on('click', lengthMeasure.click).on('contextmenu', lengthMeasure.dblclick);
        lengthMeasure.lastPoint = null;
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
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

        lengthMeasure.polyline.addTo(lengthMeasure.layers);
        lengthMeasure.layers.addTo(map);
        lengthMeasure.lastPoint = lengthMeasure.points.pop();
        $(".leaflet-interactive").css({"cursor": "crosshair"});
    },
    dblclick: function (e) { // 双击结束
        lengthMeasure.polyline.addTo(lengthMeasure.layers);
        // lengthMeasure.polyline.on("contextmenu", lengthMeasure.destory)
        common.addCloseMarker(lengthMeasure.lastPoint, lengthMeasure.polyline, lengthMeasure.layers);
        map.on('editable:vertex:drag editable:vertex:deleted', lengthMeasure.polyline.updateMeasurements, lengthMeasure.polyline);
        map.off('click', lengthMeasure.click).off('mousemove', lengthMeasure.mousemove).off('contextmenu', lengthMeasure.dblclick);
        if ($(lengthMeasure.elem).hasClass("viewer-toolbar-sel")) {
            $(lengthMeasure.elem).removeClass("viewer-toolbar-sel");
            common.setCursorForMeasure("#drawings_viewer", "");
            $(".leaflet-interactive").css({"cursor": ""});
        }
    },
    destory: function (e) {
        //lengthMeasure.layers.removeLayer(e.target);
    },
    clear: function () {
        if (lengthMeasure.points == null || lengthMeasure.points.length == 0) return;
        lengthMeasure.points.splice(0, lengthMeasure.points.length);
        lengthMeasure.layers.clearLayers();
        //map.removeLayer(lengthMeasure.polyline);
    }
}

var common = {
    addCloseMarker: function (latlng, shape, layers) {
        if (latlng == null || shape == null || layers == null) return;
        var myIcon = L.divIcon({
            className: 'my-div-icon',
            iconSize: [30, 30],
            iconAnchor: [12, -3],
            html: "<div style='width: 40px;height: 30px;'><h3 style='margin:3px 0;font-size:0.75rem'>[删除]</h3></div>",
        });
        var point = [latlng.lat, latlng.lng - 2];
        var marker = new L.marker(point, {icon: myIcon});
        marker.on("click", function () {
            layers.removeLayer(marker);
            layers.removeLayer(shape);
        });
        marker.addTo(layers);
    },
    setCursorForMeasure: function (el, cur) {
        $(el).css("cursor", cur);
    },
    getNowTime: function (date) {
        // var second = date.getSeconds();
        // var minute = date.getMinutes();
        // var hour = date.getHours();
        //格式化日，如果小于9，前面补0
        var day = ("0" + date.getDate()).slice(-2);
        //格式化月，如果小于9，前面补0
        var month = ("0" + (date.getMonth() + 1)).slice(-2);
        return date.getFullYear() + "-" + (month) + "-" + (day);
        //+ " " + (hour) + ":" + (minute) + ":" + second;
    },
    offEvent: function (operate) {
        map.off('click', areaMeasure.click).off('mousemove', areaMeasure.mousemove).off('contextmenu', areaMeasure.dblclick);
        map.off('click', lengthMeasure.click).off('mousemove', lengthMeasure.mousemove).off('contextmenu', lengthMeasure.dblclick);
        map.off("click", addOptionMarker.click);
        common.setCursorForMeasure("#drawings_viewer", "");
        $(".leaflet-interactive").css({"cursor": ""});
        if (operate != "lengthMeasure") {
            if ($(lengthMeasure.elem).hasClass("viewer-toolbar-sel")) {
                $(lengthMeasure.elem).removeClass("viewer-toolbar-sel");
                if (lengthMeasure.points.length > 0) {
                    lengthMeasure.dblclick(1);
                }
                lengthMeasure.lastPoint = null;
            }
        }
        if (operate != "areaMeasure") {
            if ($(areaMeasure.elem).hasClass("viewer-toolbar-sel")) {
                $(areaMeasure.elem).removeClass("viewer-toolbar-sel");
                if (areaMeasure.points.length > 0) {
                    areaMeasure.dblclick(1);
                }
            }
        }
        if (operate != "addOptionMarker") {
            if ($(addOptionMarker.elem).hasClass("viewer-toolbar-sel")) {
                $(addOptionMarker.elem).removeClass("viewer-toolbar-sel");
                addOptionMarker.isNew = false;
            }
        }
    },
    clearShape: function () {
        nineCell.clearNineCellLayers();
        areaMeasure.clear();
        lengthMeasure.clear();
    },
    clearMeasureShape: function () {
        areaMeasure.clear();
        lengthMeasure.clear();
    },
    removeSVGLayer: function () {
        map.eachLayer(function (layer) {
            // if (!layer._container || ('' + jQuery(layer._container).attr('class').replace(/\s/g, '')) != 'leaflet-layer') {
            if (layer._container && layer._container.children[0].localName === 'g')
                layer.remove();
            // }
        });
    },
    calcTextLength: function (str) {
        var len = 0;
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 127 || str.charCodeAt(i) == 94) {
                len += 2;
            } else {
                len++;
            }
        }
        return len;
    },
    calcLineLength: function (firstPoint, secondPoint) {
        return Math.sqrt((firstPoint.lat - secondPoint.lat) * (firstPoint.lat - secondPoint.lat) + (firstPoint.lng - secondPoint.lng) * (firstPoint.lng - secondPoint.lng));
    }
}

//添加意见标记
var addOptionMarker = {
    elem: "",
    markerLayerGroup: new L.layerGroup(),
    drawingId: '',// "2019-440115-36-02-800166-001",
    currentMarker: null,
    currentLatlng: [],
    isNew: false,
    xh: 1,
    size: 32,
    markerType: 0,
    shapeInfo: '',
    opinionTypes: [
        {id: "-1", code: "", text: "请选择..."},
        //需修改完善意见
        {id: "1", code: "违反建设工程强制性条文", text: "违反建设工程强制性条文"},
        {id: "2", code: "违反专业规范、规程和设计深度不足", text: "违反专业规范、规程和设计深度不足"},
        {id: "3", code: "其他方面", text: "其他方面"},
        //可自行修改完善意见
        {id: "4", code: "建设性意见", text: "建设性意见"},
        {id: "5", code: "变更概况", text: "变更概况"}
    ],
    currType: {},
    noProfession: ["勘察", "道路", "桥梁", "园林", "其他"],
    drawingProfession: [
        //图纸专业
        {id: "1", code: "建筑", text: "建筑"},
        //必有专业
        {id: "2", code: "绿建", text: "绿建"},
        {id: "3", code: "消防", text: "消防"},
        {id: "4", code: "人防", text: "人防"}
    ],
    //建筑
    architecturalSelects: [
        {id: "-1", code: "", text: ""},
        {id: "1", code: "设计单位具备相应的资质", text: "设计单位具备相应的资质"},
        {id: "2", code: "消防设计文件的编制符合消防设计文件申报要求情况", text: "消防设计文件的编制符合消防设计文件申报要求情况"},
        {id: "3", code: "建筑工程类别、总平面布局、平面布置和建筑的耐火等级", text: "建筑工程类别、总平面布局、平面布置和建筑的耐火等级"},
        {id: "4", code: "建筑构造", text: "建筑构造"},
        {id: "5", code: "安全疏散和消防电梯", text: "安全疏散和消防电梯"},
        {id: "6", code: "其他方面", text: "其他方面"},
    ],
    //电气
    electricSelects: [
        {id: "-1", code: "", text: ""},
        {id: "1", code: "消防电气", text: "消防电气"},
        {id: "2", code: "建设工程内部装修", text: "建设工程内部装修"},
        {id: "3", code: "消防产品及建筑构件、材料", text: "消防产品及建筑构件、材料"},
        {id: "4", code: "其他方面", text: "其他方面"},
    ],
    //给排水
    plumbingSelects: [
        {id: "-1", code: "", text: ""},
        {id: "1", code: "设计单位具备相应的资质", text: "设计单位具备相应的资质"},
        {id: "2", code: "消防设计文件的编制符合消防设计文件申报要求情况", text: "消防设计文件的编制符合消防设计文件申报要求情况"},
        {id: "3", code: "建筑工程类别、总平面布局、平面布置和建筑的耐火等级", text: "建筑工程类别、总平面布局、平面布置和建筑的耐火等级"},
        {id: "4", code: "建筑构造", text: "建筑构造"},
        {id: "5", code: "安全疏散和消防电梯", text: "安全疏散和消防电梯"},
        {id: "6", code: "其他方面", text: "其他方面"},
    ],
    //结构
    structuralSelects: [
        {id: "-1", code: "", text: ""},
        {id: "1", code: "消防给水", text: "消防给水"},
        {id: "2", code: "自动消防设施和灭火设备", text: "自动消防设施和灭火设备"},
        {id: "3", code: "其他方面", text: "其他方面"},
    ],
    //暖通
    hvacSelects: [
        {id: "-1", code: "", text: ""},
        {id: "1", code: "设计单位具备相应的资质", text: "设计单位具备相应的资质"},
        {id: "2", code: "消防设计文件的编制符合消防设计文件申报要求情况", text: "消防设计文件的编制符合消防设计文件申报要求情况"},
        {id: "3", code: "消防给水", text: "消防给水"},
        {id: "4", code: "热能动力", text: "热能动力"},
        {id: "5", code: "其他方面", text: "其他方面"},
    ],

    init: function (el) {
        addOptionMarker.elem = el;
        if (addOptionMarker.drawingId == '' || addOptionMarker.drawingId == undefined) return;
        if (map == null) return;
        common.offEvent("addOptionMarker");
        if ($(addOptionMarker.elem).hasClass("viewer-toolbar-sel")) {
            $(addOptionMarker.elem).removeClass("viewer-toolbar-sel");
            addOptionMarker.isNew = false;
            return;
        }
        if (addOptionMarker.currentMarker) {
            addOptionMarker.currentMarker.closePopup();
            addOptionMarker.currentMarker = null;
        }
        $(addOptionMarker.elem).addClass("viewer-toolbar-sel");
        common.setCursorForMeasure("#drawings_viewer", "pointer");
        map.on("click", addOptionMarker.click);
        if (map.hasLayer(addOptionMarker.markerLayerGroup) == false)
            map.addLayer(addOptionMarker.markerLayerGroup);
        addOptionMarker.isNew = true;
        addOptionMarker.markerType = 0;
    },
    click: function (e) {
        var latlng = e.latlng;
        addOptionMarker.createMarker(latlng, "", common.getNowTime(new Date()), addOptionMarker.xh, "", "", "", "");
        map.off("click", addOptionMarker.click);
        $(addOptionMarker.elem).removeClass("viewer-toolbar-sel");
        common.setCursorForMeasure("#drawings_viewer", "");
    },
    createMarker: function (latlng, content, date, xh, markerClassName, opinionType, opinionProfession, fireControlSelected) {
        var iconClassName = addOptionMarker.drawingId + '_' + xh;
        var icon = addOptionMarker.createMarkerIconByOpinion(iconClassName, xh, opinionType);
        var marker = L.marker(latlng, {className: markerClassName, icon: icon});
        var html = addOptionMarker.createHtml(content, date, opinionType, opinionProfession, fireControlSelected);
        var popup = L.popup({maxWidth: 620}).setContent(html);
        marker.bindPopup(popup).addTo(addOptionMarker.markerLayerGroup);
        marker.on("popupopen", function () {
            addOptionMarker.currentMarker = marker;
            addOptionMarker.currentLatlng = marker.getLatLng();
            if (addOptionMarker.isNew == false) {
                $('#notSaveBtn').text("关闭");
                $('#title').text("修改意见");
            } else if (addOptionMarker.isNew == true && addOptionMarker.currentMarker.options.className != '') {
                //当点击了 添加标记而又点击了一个已存在的标记，将触发 状态为true导致出问题
                $('#notSaveBtn').text("关闭");
                $('#title').text("修改意见");
                addOptionMarker.isNew = false;
                map.off("click", addOptionMarker.click);
                $(addOptionMarker.elem).removeClass("selected");
                common.setCursorForMeasure("#drawings_viewer", "");
            } else {
                $('#notSaveBtn').text("放弃");
                $('#title').text("添加意见");
            }
            $('[name=opinionProfession]').on("change", function (e) {
                if (e.currentTarget.defaultValue == '消防') {
                    $("#fireControlItems").css("display", "block");
                } else {
                    $("#fireControlItems").css("display", "none");
                }
            });
        });
        //当新插入标记后，点击关闭弹出框，默认不保存标记到数据库
        marker.on("popupclose", function () {
            if (addOptionMarker.isNew)
                addOptionMarker.notSave();
        });
        if (addOptionMarker.isNew && addOptionMarker.currentMarker.options.className == '')
            marker.openPopup();
    },
    checkIsMustModifyOpinion: function (opinionType) {
        var mustModifyOpinion = false;
        if (opinionType != '') {
            for (var i = 1; i < 4; i++) {
                if (addOptionMarker.opinionTypes[i].code == opinionType) {
                    mustModifyOpinion = true;
                    break;
                }
            }
        }
        return mustModifyOpinion;
    },
    createMarkerIconByOpinion: function (iconClassName, xh, opinionType) {
        var markerName = "marker";
        var doubleDigitWidth = xh > 9 ? addOptionMarker.size / 8 : 0;
        var width, height,
            iconAnchorWidth, iconAnchorHeight,
            popupAnchorWidth, popupAnchorHeight,
            numberTop, numberleft,
            color = '#0c26ff';
        if ("" != opinionType) {
            var mustModifyOpinion = addOptionMarker.checkIsMustModifyOpinion(opinionType);
            // markerName = mustModifyOpinion ? "forkMarker_" + addOptionMarker.size : "arrowMarker_" + addOptionMarker.size;
            if (mustModifyOpinion) {
                //forkMarker_32 - 鼠标点击点为中心点
                markerName = "forkMarker_32";
                width = 32;
                height = 32;
                //基于32 div的中心点位置变化
                iconAnchorWidth = 16;//左右
                iconAnchorHeight = 16;//上下
                popupAnchorWidth = 0;
                popupAnchorHeight = -10;//-28;
                numberTop = -16;
                numberleft = 12;
                color = '#0c26ff';
            } else {
                //arrowMarker_32 -鼠标点击点为中心点
                markerName = "arrowMarker_32";
                width = 32;
                height = 32;
                iconAnchorWidth = 0;//左右
                iconAnchorHeight = 20;//上下
                popupAnchorWidth = 16;
                popupAnchorHeight = -16;
                numberTop = -4;
                numberleft = 12;
                color = '#0c26ff';
            }
        } else {
            //标准marker参数，以最下方点为锚点
            width = 48;
            height = 48;
            iconAnchorWidth = 24;
            iconAnchorHeight = 45;
            popupAnchorWidth = -2;
            popupAnchorHeight = -38;
            numberTop = 3;
            numberleft = 18;
            color = '#fff';
        }

        var icon = L.divIcon({
            className: iconClassName,
            iconSize: [width, height],
            iconAnchor: [iconAnchorWidth, iconAnchorHeight],
            popupAnchor: [popupAnchorWidth, popupAnchorHeight],
            // iconSize: [48, 48],
            // iconAnchor: [26, 47],
            // popupAnchor: [-2, -38],
            html: '<div style="position:relative">' +
            '<img src=' + 'http://' + window.location.host + ctx + '/ui-static/aplanmis/cod/drawing/leaflet/images/' + markerName + '.png />' +
            //'<p id="markerxh" style="position:absolute;top:' + numberTop + 'px;left:' + numberleft + 'px;color:' + color + ';font-size:14px">' + xh + '</p>' +
            '</div>'
        });
        return icon;
    },
    setMarkerIcon: function (marker, opinionType, iconClassName) {
        var markerIconName = '';
        if ("" != opinionType) {
            var mustModifyOpinion = addOptionMarker.checkIsMustModifyOpinion(opinionType);
            markerIconName = mustModifyOpinion ? "forkMarker_" + addOptionMarker.size : "arrowMarker_" + addOptionMarker.size;
        }
        var src = 'http://' + window.location.host + ctx + '/ui-static/aplanmis/cod/drawing/leaflet/images/' + markerIconName + '.png';
        var arr = iconClassName.split('_');
        var doubleDigitWidth = arr[1] > 9 ? addOptionMarker.size / 8 : 0;
        var width, height,
            iconAnchorWidth, iconAnchorHeight,
            popupAnchorWidth, popupAnchorHeight,
            numberTop, numberleft;
        if (markerIconName === "forkMarker_32") {
            //forkMarker_32 - 鼠标点击点为中心点
            markerIconName = "forkMarker_32";
            width = 32;
            height = 32;
            //基于32 div的中心点位置变化
            iconAnchorWidth = 16;//左右
            iconAnchorHeight = 16;//上下
            popupAnchorWidth = 0;
            popupAnchorHeight = 10;//-28;
            numberTop = -16;
            numberleft = 12;
            color = '#0c26ff';
        } else {
            //arrowMarker_32 -鼠标点击点为中心点
            markerIconName = "arrowMarker_32";
            width = 32;
            height = 32;
            iconAnchorWidth = 0;//左右
            iconAnchorHeight = 20;//上下
            popupAnchorWidth = 16;
            popupAnchorHeight = -16;
            numberTop = -4;
            numberleft = 12;
            color = '#0c26ff';
        }
        var orderHTML = '';// arr[1] === '' ? '' : '<p id="markerxh" style="position:absolute;top:' + numberTop + 'px;left:' + numberleft + 'px;color:#0c26ff;font-size:14px">' + arr[1] + '</p>';
        var icon = L.divIcon({
            className: iconClassName,
            iconSize: [width, height],
            iconAnchor: [iconAnchorWidth, iconAnchorHeight],
            popupAnchor: [popupAnchorWidth, popupAnchorHeight],
            html: '<div style="position:relative"><img src=' + src + '/>' + orderHTML + '</div>',
        });
        marker.setIcon(icon);
    },
    createFireControlOptions: function (firstProfession, fireControlSelected) {
        var fireControlOptions = '';
        var arr = [];
        var selected = "";
        switch (firstProfession) {
            case '建筑':
                arr = addOptionMarker.architecturalSelects;
                break;
            case '电气':
                arr = addOptionMarker.electricSelects;
                break;
            case '给排水':
                arr = addOptionMarker.plumbingSelects;
                break;
            case '暖通':
                arr = addOptionMarker.hvacSelects;
                break;
            case '结构':
                arr = addOptionMarker.structuralSelects;
                break;
        }
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].code == fireControlSelected)
                selected = " selected='selected'";
            else selected = '';
            fireControlOptions += "<option value='" + arr[i].code + "'" + selected + ">" + arr[i].text + "</option>";
        }
        return fireControlOptions;
    },
    createSelectGroup: function (groupName, array, startIndex, length, opinionTypeSelected) {
        if (array == null || array.length < startIndex + length) return "";
        var options = "<optgroup label='" + groupName + "'>";
        var selected = "";
        for (var i = startIndex; i < startIndex + length; i++) {
            if (array[i].code == opinionTypeSelected)
                selected = " selected='selected'";
            else selected = '';
            options += "<option value='" + array[i].code + "'" + selected + ">" + array[i].text + "</option>";
        }
        options += "</optgroup>";
        return options;
    },
    createHtml: function (content, date, opinionTypeSelected, opinionProfession, fireControlSelected) {
        var options = '';
        options = addOptionMarker.createSelectGroup("需修改完善意见", addOptionMarker.opinionTypes, 1, 3, opinionTypeSelected)
            + addOptionMarker.createSelectGroup("可自行修改完善意见", addOptionMarker.opinionTypes, 4, 2, opinionTypeSelected);
        var profession = '';
        var firstProfession = addOptionMarker.drawingProfession.length > 0 ? firstProfession = addOptionMarker.drawingProfession[0].code : '';
        //根据专业控制专业行是否可见
        var trDisplay = '';
        for (var m = 0; m < addOptionMarker.noProfession.length; m++) {
            if (addOptionMarker.noProfession[m] == firstProfession) {
                trDisplay = "style='display:none';";
                break;
            }
        }

        //添加图纸专业
        for (var i = 0; i < addOptionMarker.drawingProfession.length; i++) {
            if (addOptionMarker.drawingProfession[i].code == opinionProfession) {
                profession += "<label class='m-radio m-radio--solid m-radio--brand' style='padding-left: 24px;margin-top: 8px;'><input type='radio' name='opinionProfession' checked='checked' value='" + addOptionMarker.drawingProfession[i].code + "'/>" + addOptionMarker.drawingProfession[i].text + "<span></span></label>";
            } else if (opinionProfession == '' && i == 0) {
                profession += "<label class='m-radio m-radio--solid m-radio--brand' style='padding-left: 24px;margin-top: 8px;'><input type='radio' name='opinionProfession' checked='checked' value='" + addOptionMarker.drawingProfession[i].code + "'/>" + addOptionMarker.drawingProfession[i].text + "<span></span></label>";
            } else {
                profession += "<label class='m-radio m-radio--solid m-radio--brand' style='padding-left: 24px;margin-top: 8px;'><input type='radio' name='opinionProfession' value='" + addOptionMarker.drawingProfession[i].code + "'/>" + addOptionMarker.drawingProfession[i].text + "<span></span></label>";
            }
        }
        //添加消防下拉框
        var fireControlHtml = '';
        var fireOptions = addOptionMarker.createFireControlOptions(firstProfession, fireControlSelected);
        if (opinionProfession == '消防') {
            fireControlHtml = "<select class='form-control m-select m-me' id='fireControlItems' style='width: 50px;height:30px;margin-top:2px;display: block;border-radius:4px'>" + fireOptions + "</select>"
        } else {
            fireControlHtml = "<select  class='form-control m-select m-me' id='fireControlItems' style='width: 50px;height:30px;margin-top:2px;display: none;border-radius:4px'>" + fireOptions + "</select>"
        }

        var html = "<div><p id='title' style='font-size: 16px;line-height: 34px;margin:0'>修改意见</p><form id='formopinion' style='width:600px'><table class='opinion' style='width:100%'>" +
            "<tr><td style='width: 10%;text-align: right'><span>图号：</span></td>" +
            "<td style='padding:0 0 0 15px'><span id='drawingNum'>" + addOptionMarker.drawingId +
            "</span></td></tr>" +
            "<tr><td style='width: 10%; text-align: right'><span>类型：</span></td>" +
            "<td style='padding:0 0 0 15px'><div class='input-group'><select id='opinionType' class='form-control m-select m-me' style='width: 100%;height:30px' >" +
            options +
            "</select></div></td></tr>" +
            "<tr id='opinionProfessionTR'" + trDisplay + "><td style='width: 10%;text-align: right'><span>专业：</span></td>" +
            "<td class='input-group' style='padding:0 0 0 15px'><div id='profession' style='margin-right: 40px' class='m-checkbox-inline'>" +
            profession +
            "</div>" + fireControlHtml + "</td></tr>" +
            "<tr><td style='width: 10%;text-align: right'><span>日期：</span></td><td style='padding:0 0 0 15px'><span id='datepick'> " + date + "</span></td></tr>" +
            "<tr style='height:150px'><td style='padding:0 0 0 15px' colspan='2'>" +
            "<div class='input-group' style='width: 100%;height:150px;'>" +
            "<textarea class='form-control m-input m-me' id='opinionContent' style='width: 100%;height:150px;resize:none'>" + content +
            "</textarea>" +
            // "<div class='form-control m-input m-me' id='opinionContent' style='width: 100%;height:150px;' contenteditable='true'>" + content +
            // "</div>" +
            "</div></td></tr>" +
            "<tr><td style='width: 100%;' colspan='2'>" +
            // "<button type='button'>加粗</button>" +
            // "<button type='button'>插入特殊符号</button>" +
            // "<button type='button'>插入图片</button>" +
            // "<button type='button'>引用规范</button>" +
            // "<button type='button'>常用意见</button>" +
            // "<button type='button'>收藏</button>" +
            "<button type='button'  onclick='addOptionMarker.saveOpinion()'>保存</button>" +
            "<button  type='button' id='notSaveBtn' onclick='addOptionMarker.notSave()'>放弃</button>" +
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
        addOptionMarker.currType.drawingId = drawingId;
        addOptionMarker.currType.pageNumber = gDrawingViewer.pageNum;
        addOptionMarker.initOpinionsWin();
    },
    createMarkerByAction: function () {
        if (addOptionMarker.drawingId == '' || addOptionMarker.drawingId == undefined) return;
        if (map.hasLayer(addOptionMarker.markerLayerGroup) === false)
            map.addLayer(addOptionMarker.markerLayerGroup);
        addOptionMarker.currType.drawingId = addOptionMarker.drawingId;
        addOptionMarker.currType.pageNumber = gDrawingViewer.pageNum;
        addOptionMarker.initOpinionsWin();
    },
    saveOpinion: function () {
        var opinionContent = $("#opinionContent").val();
        if (opinionContent.trim() === '') {
            swal('提示信息', '请先填写意见！', 'info');
            return;
        }
        if (common.calcTextLength(opinionContent) > 2000) {
            swal('提示信息', '意见内容大于1000字，不能保存！', 'info');
            return;
        }
        var createDate = addOptionMarker.currentMarker.options.className == '' ? common.getNowTime(new Date()) : $("#datepick").text().trim();
        var tempProfession = $('[name=opinionProfession]:checked').val();
        var tempfireControlSelect = tempProfession === "消防" ? $("#fireControlItems").val() : '';
        var data = null;
        if (addOptionMarker.currentMarker.options.className == '') {
            data = {
                id: addOptionMarker.currentMarker.options.className,
                drawingId: $('#drawingNum').text(),
                lat: addOptionMarker.currentLatlng.lat == undefined ? addOptionMarker.currentLatlng[0] : addOptionMarker.currentLatlng.lat,
                lng: addOptionMarker.currentLatlng.lng == undefined ? addOptionMarker.currentLatlng[1] : addOptionMarker.currentLatlng.lng,
                createUser: assignee,
                createDate: createDate,
                opinionContent: opinionContent,
                opinionType: $("#opinionType").val(),
                opinionProfession: tempProfession,
                pageNumber: gDrawingViewer.pageNum,
                rotateAngle: gDrawingViewer.rotation,
                drawingScale: 1,
                //gDrawingViewer.scale,
                firecontrolSelected: tempfireControlSelect,
                markerType: addOptionMarker.markerType,
                shapeInfo: addOptionMarker.getShapeInfoByMarkerType(addOptionMarker.markerType)
            };
        } else {
            data = {
                id: addOptionMarker.currentMarker.options.className,
                opinionContent: opinionContent,
                opinionType: $("#opinionType").val(),
                opinionProfession: tempProfession,
                firecontrolSelected: tempfireControlSelect
            };
        }
        $.ajax({
            url: ctx + "/cod/drawing/marker/info/saveCodDrawingMarkerInfo.do",
            type: "post",
            data: data,
            success: function (e) {
                addOptionMarker.isNew = false;
                if (addOptionMarker.currentMarker.options.className == '') {
                    addOptionMarker.xh = e.content.xh + 1;
                    addOptionMarker.currentMarker.options.className = e.content.id;
                }
                addOptionMarker.currentMarker.setPopupContent(addOptionMarker.createHtml(e.content.opinionContent, createDate, e.content.opinionType, e.content.opinionProfession, e.content.firecontrolSelected));
                switch (addOptionMarker.markerType) {
                    case 0:
                        var iconClassNames = addOptionMarker.currentMarker._icon.classList;
                        var iconClassName = '';
                        for (var i = 0; i < iconClassNames.length; i++) {
                            var fdStart = iconClassNames[i].indexOf("leaflet");
                            if (fdStart < 0) iconClassName = iconClassNames[i];
                        }
                        addOptionMarker.setMarkerIcon(addOptionMarker.currentMarker, e.content.opinionType, iconClassName);
                        break;
                    case 1:
                        addOptionMarker.currentMarker.setOpinions(e.content.opinionContent.slice(0, 9));
                        break;
                    case 2:
                    case 3:
                    case 4:
                        break;
                }
                addOptionMarker.currentMarker.closePopup();
                swal('提示信息', '保存意见成功！', 'info');
            },
            error: function (e) {
                swal('错误信息', '服务器异常，保存意见失败!', 'error');
            },
        });
    },
    notSave: function () {
        if (addOptionMarker.currentMarker == null) return;
        if (addOptionMarker.isNew == false) {
            addOptionMarker.currentMarker.closePopup();
            return;
        }
        switch (addOptionMarker.markerType) {
            case 0:
                addOptionMarker.markerLayerGroup.removeLayer(addOptionMarker.currentMarker);
                break;
            case 1:
                multiFunctionMarker.polylineLayerGroup.removeLayer(multiFunctionMarker.polyline);
                break;
            case 2:
                multiFunctionMarker.circleLayerGroup.removeLayer(multiFunctionMarker.circle);
                break;
            case 3:
                multiFunctionMarker.ellipseLayerGroup.removeLayer(multiFunctionMarker.ellipse);
                break;
            case 4:
                multiFunctionMarker.rectangleLayerGroup.removeLayer(multiFunctionMarker.rectangle);
                break;
        }
        addOptionMarker.isNew = false;
    },
    openOpinions: function () {
        var modal = $('#opinion_modal')
        modal.on('shown.bs.modal', $.proxy(function () {
            this.off('shown.bs.modal');
            this.draggable();
            addOptionMarker.loadOpinionsList(addOptionMarker.currType);
        }, modal)).modal('show');
    },
    initOpinionsWin: function () {
        var opinion = $('#opinion_modal');
        /**
         opinion.draggable().click(function () {
            $(this).draggable({disable: false});
            $(this).css('backgroundColor', 'transparent');
        }).dblclick(function () {
            $(this).draggable({disable: true});
            $(this).css('backgroundColor', '#eeeaf1');
        });
         **/
        var options = '';
        for (var i = 0; i < addOptionMarker.opinionTypes.length; i++) {
            options += '<option value="' +
                addOptionMarker.opinionTypes[i].code + '">' + addOptionMarker.opinionTypes[i].text + '</option>';
        }
        $("#opinionTypeSelect").html(options);
        $("#opinionTypeSelect").on("change", function (f) {
            var type = addOptionMarker.opinionTypes[this.selectedIndex];
            addOptionMarker.currType.opinionType = type.code;
            addOptionMarker.loadOpinionsList(addOptionMarker.currType);
        });
        addOptionMarker.loadOpinionsList(addOptionMarker.currType);
    },
    loadOpinionsList: function (data) {
        return $.ajax({
            url: ctx + '/cod/drawing/marker/info/getListCodDrawingMarkerInfo.do',
            type: 'post',
            data: data,
        }).done(function (arr) {
            addOptionMarker.createAllMarkerByData(arr);
            var content = arr.map(function (obj) {
                return ['<tr><th scope="row">'
                    , obj.xh
                    , '</th>'
                    , '<td><div class="cell">'
                    , obj.opinionContent
                    , '</div></td>'
                    , '<td><div class="cell"><div class="btn-group " role="group"><button type="button" class="btn btn-link " data-pan-to="'
                    , obj.lat + ',' + obj.lng
                    , '">定位</button>'
                    , '<button type="button" class="btn btn-link text-danger" data-row-id="'
                    , obj.id
                    , '">删除</button></div></div></td>'
                    , '</tr>'].join('');
            });
            $('#opinionList')
                .empty()
                .append(content)
                .off('click')
                .on('click', function (evt) {
                    var rowId, row;
                    if (evt.target.nodeName != 'BUTTON') return;
                    rowId = $(evt.target).data('row-id');
                    if (rowId) return addOptionMarker.deleteOpinion(rowId);
                    row = $(evt.target).data('pan-to');
                    map.panTo(row.split(',').map(function (point) {
                        return point * 1
                    }));
                });
        }).fail(function (err) {

        })
    },
    createAllMarkerByData: function (arr) {
        addOptionMarker.markerLayerGroup.clearLayers();
        multiFunctionMarker.clearMultiMarkers();
        $.each(arr, function (i, item) {
            //var latlng = {lat: item.lat, lng: item.lng};
            var content = item.opinionContent;
            var date = common.getNowTime(new Date(item.createDate));
            //var xh = item.xh;
            var id = item.id;
            var opinionType = item.opinionType;
            var opinionProfession = item.opinionProfession;
            var firecontrolSelected = item.firecontrolSelected;
            var markerType = item.markerType;
            var shapeInfo = eval('(' + item.shapeInfo + ')');
            switch (markerType) {
                case 0:
                    shapeInfo = addOptionMarker.calcLatLngByRotate(shapeInfo, (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    addOptionMarker.createMarker(shapeInfo, content, date, i + 1, id, opinionType, opinionProfession, firecontrolSelected);
                    break;
                case 1:
                    var p1 = addOptionMarker.calcLatLngByRotate(shapeInfo.latlngs[0], (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    var p2 = addOptionMarker.calcLatLngByRotate(shapeInfo.latlngs[1], (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    var p3 = addOptionMarker.calcLatLngByRotate(shapeInfo.latlngs[2], (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    var latlngs = "[[" + p1[0] + "," + p1[1] + "],";
                    latlngs += "{lat:" + (p2.lat === undefined ? p2[0] : p2.lat) + ",lng:" + (p2.lng === undefined ? p2[1] : p2.lng) + "},";
                    latlngs += "{lat:" + (p3.lat === undefined ? p3[0] : p3.lat) + ",lng:" + (p3.lng === undefined ? p3[1] : p3.lng) + "}]";
                    latlngs = eval('(' + latlngs + ')');
                    multiFunctionMarker.createPolylineExt(latlngs, shapeInfo.lineHead, shapeInfo.lineColor, shapeInfo.lineWeight, shapeInfo.textSize,
                        content, date, i + 1, id, opinionType, opinionProfession, firecontrolSelected);
                    break;
                case 2:
                    var centerPoint = addOptionMarker.calcLatLngByRotate(shapeInfo.centerPoint, (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    multiFunctionMarker.createCircleExt(centerPoint, shapeInfo.r,
                        shapeInfo.circleColor, shapeInfo.circleFillColor, shapeInfo.circleFillOpacity, shapeInfo.circleWeight,
                        content, date, i + 1, id, opinionType, opinionProfession, firecontrolSelected);
                    break;
                case 3:
                    var ellipseCenterPoint = addOptionMarker.calcLatLngByRotate(shapeInfo.ellipseCenterPoint, (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    multiFunctionMarker.createEllipseExt(ellipseCenterPoint, shapeInfo.semis, shapeInfo.tilt + (gDrawingViewer.rotation - item.rotateAngle) % 360,
                        shapeInfo.ellipseColor, shapeInfo.ellipseFillColor, shapeInfo.ellipseFillOpacity, shapeInfo.ellipseWeight,
                        content, date, i + 1, id, opinionType, opinionProfession, firecontrolSelected);
                    break;
                case 4:
                    var b1 = addOptionMarker.calcLatLngByRotate(shapeInfo.bounds[0], (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    var b2 = addOptionMarker.calcLatLngByRotate(shapeInfo.bounds[1], (gDrawingViewer.rotation - item.rotateAngle) % 360);
                    multiFunctionMarker.createRectangleExt([b1, b2],
                        shapeInfo.rectangleColor, shapeInfo.rectangleFillColor, shapeInfo.rectangleFillOpacity, shapeInfo.rectangleWeight,
                        content, date, i + 1, id, opinionType, opinionProfession, firecontrolSelected);
                    break;
            }
        });
        addOptionMarker.xh = arr == null || arr.length == 0 ? 1 : arr[arr.length - 1].xh + 1;
    },
    deleteOpinion: function (id) {
        // agcloud.ui.metronic.showConfirm("是否删除当前意见？", function () {
        //     $.post(ctx + "/cod/drawing/marker/info/deleteCodDrawingMarkerInfoById.do", {
        //         id: id
        //     }, function (result) {
        //         if (result.success) {
        //             addOptionMarker.loadOpinionsList(addOptionMarker.currType);
        //         } else
        //             agcloud.ui.metronic.showErrorTip(result.message);
        //     });
        // });
        confirmMsg('确认信息','是否删除当前意见？',function () {
            $.post(ctx + "/cod/drawing/marker/info/deleteCodDrawingMarkerInfoById.do", {
                id: id
            }, function (result) {
                if (result.success) {
                    addOptionMarker.loadOpinionsList(addOptionMarker.currType);
                } else
                    agcloud.ui.metronic.showErrorTip(result.message);
            });
        });
    },
    calcLatLngByRotate: function (latlng, rotateAngle) {
        var lat = latlng.lat ? latlng.lat : latlng[0];
        var lng = latlng.lng ? latlng.lng : latlng[1];
        if (rotateAngle == 0 || rotateAngle == -360) {
            return latlng;
        } else if (rotateAngle == 90 || rotateAngle == -270) {
            return [0 - lng, lat];
        } else if (rotateAngle == 180 || rotateAngle == -180) {
            return [0 - lat, 0 - lng];
        } else if (rotateAngle == 270 || rotateAngle == -90) {
            return [lng, 0 - lat];
        }
    },
    getShapeInfoByMarkerType: function (markerType) {
        switch (markerType) {
            case 0:
                var latlng = addOptionMarker.currentMarker.getLatLng();
                return "{lat:" + latlng.lat + ",lng:" + latlng.lng + "}";
            case 1:
                var latlngInfo = multiFunctionMarker.currentMarker.getlatlngsInfo();
                var latlngs = '{latlngs:';
                latlngs += "[[" + latlngInfo[0][0] + "," + latlngInfo[0][1] + "],";
                latlngs += "{lat:" + latlngInfo[1].lat + ",lng:" + latlngInfo[1].lng + "},";
                latlngs += "{lat:" + latlngInfo[2].lat + ",lng:" + latlngInfo[2].lng + "}],";
                latlngs += "lineHead:" + multiFunctionMarker.params.lineHead +
                    ",lineColor:'" + multiFunctionMarker.params.lineColor + "'" +
                    ",lineWeight:" + multiFunctionMarker.params.lineWeight +
                    ",textSize:" + multiFunctionMarker.params.textSize + "}";
                return latlngs;
            case 2:
                var centerPoint = addOptionMarker.currentMarker.getLatLng();
                var r = addOptionMarker.currentMarker.getRadius();
                return "{centerPoint:" + "{lat:" + centerPoint.lat + ",lng:" + centerPoint.lng +
                    "},r:" + r +
                    ",circleColor:'" + multiFunctionMarker.params.circleColor +
                    "',circleFillColor:'" + multiFunctionMarker.params.circleFillColor + "'" +
                    ",circleFillOpacity:" + multiFunctionMarker.params.circleFillOpacity +
                    ",circleWeight:" + multiFunctionMarker.params.circleWeight + "}";
            case 3:
                var ellipseCenterPoint = addOptionMarker.currentMarker.getLatLng();
                var semis = addOptionMarker.currentMarker.getRadius();
                var tilt = addOptionMarker.currentMarker._tiltDeg;
                return "{ellipseCenterPoint:" + "{lat:" + ellipseCenterPoint.lat + ",lng:" + ellipseCenterPoint.lng +
                    "},semis:[" + semis.x + "," + semis.y + "],tilt:" + tilt +
                    ",ellipseColor:'" + multiFunctionMarker.params.ellipseColor +
                    "',ellipseFillColor:'" + multiFunctionMarker.params.ellipseFillColor + "'" +
                    ",ellipseFillOpacity:" + multiFunctionMarker.params.ellipseFillOpacity +
                    ",ellipseWeight:" + multiFunctionMarker.params.ellipseWeight + "}";
            case 4:
                var bounds = addOptionMarker.currentMarker.getBounds();
                return "{bounds:" + "[{lat:" + bounds._northEast.lat + ",lng:" + bounds._northEast.lng +
                    "},{lat:" + bounds._southWest.lat + ",lng:" + bounds._southWest.lng + "}]" +
                    ",rectangleColor:'" + multiFunctionMarker.params.rectangleColor +
                    "',rectangleFillColor:'" + multiFunctionMarker.params.rectangleFillColor + "'" +
                    ",rectangleFillOpacity:" + multiFunctionMarker.params.rectangleFillOpacity +
                    ",rectangleWeight:" + multiFunctionMarker.params.rectangleWeight + "}";
        }
    }
}

var multiFunctionMarker = {
    parentElement: null,
    isAltKey: false,
    params: {
        lineHead: 0,
        lineColor: '#ff0000',
        lineWeight: 3,
        textSize: 12,

        circleColor: '#ff0000',
        circleWeight: 3,
        circleFillColor: '#ff0000',
        circleFillOpacity: 10,

        ellipseColor: '#ff0000',
        ellipseWeight: 3,
        ellipseFillColor: '#ff0000',
        ellipseFillOpacity: 10,

        rectangleColor: '#ff0000',
        rectangleWeight: 3,
        rectangleFillColor: '#ff0000',
        rectangleFillOpacity: 10
    },

    showTool: function (el) {
        multiFunctionMarker.parentElement = el;
        $(el).toggleClass("viewer-toolbar-sel");
        var markerTool = $("#multiMarkerTool");
        if (markerTool.is(":hidden")) {
            var left = $(el).css("left");
            markerTool.css({
                "left": "calc(" + left + 5 + "px)",
                "bottom": "47px"
            });
            markerTool.show();
        } else {
            markerTool.hide();
        }
    },
    hideTool: function () {
        // $("#multiMarkerTool").hide();
        // if (multiFunctionMarker.parentElement === '') return;
        // if ($(multiFunctionMarker.parentElement).hasClass("viewer-toolbar-sel"))
        //     $(multiFunctionMarker.parentElement).removeClass("viewer-toolbar-sel")
    },
    clearMultiMarkers: function () {
        multiFunctionMarker.hideTool();
        multiFunctionMarker.polylineLayerGroup.clearLayers();
        multiFunctionMarker.circleLayerGroup.clearLayers();
        multiFunctionMarker.rectangleLayerGroup.clearLayers();
        multiFunctionMarker.ellipseLayerGroup.clearLayers();
    },
    createPop: function (content, date, opinionType, opinionProfession, fireControlSelected) {
        var html = addOptionMarker.createHtml(content, date, opinionType, opinionProfession, fireControlSelected);
        return L.popup({maxWidth: 620}).setContent(html);
    },
    commonPopupSet: function (marker, markerType, layerGroup, centerPoint, initClick, content, date, opinionType, opinionProfession, fireControlSelected) {
        if (!marker) return;
        if (map.hasLayer(layerGroup) == false)
            map.addLayer(layerGroup);
        marker.bindPopup(multiFunctionMarker.createPop(content, date, opinionType, opinionProfession, fireControlSelected));
        if (layerGroup.hasLayer(marker) == false)
            layerGroup.addLayer(marker);
        marker.on("popupopen", function () {
            addOptionMarker.currentMarker = marker;
            addOptionMarker.currentLatlng = centerPoint;
            addOptionMarker.markerType = markerType;
            if (addOptionMarker.isNew == false) {
                $('#notSaveBtn').text("关闭");
                $('#title').text("修改意见");
            } else if (addOptionMarker.isNew == true && addOptionMarker.currentMarker.options.className != '') {
                //当点击了 添加标记而又点击了一个已存在的标记，将触发 状态为true导致出问题
                $('#notSaveBtn').text("关闭");
                $('#title').text("修改意见");
                addOptionMarker.isNew = false;
                map.off("click", initClick);
                common.setCursorForMeasure("#drawings_viewer", "");
            } else {
                $('#notSaveBtn').text("放弃");
                $('#title').text("添加意见");
            }
            $('[name=opinionProfession]').on("change", function (e) {
                if (e.currentTarget.defaultValue == '消防') {
                    $("#fireControlItems").css("display", "block");
                } else {
                    $("#fireControlItems").css("display", "none");
                }
            });
        });
        //当新插入标记后，点击关闭弹出框，默认不保存标记到数据库
        marker.on("popupclose", function () {
            if (addOptionMarker.isNew)
                addOptionMarker.notSave();
        });
    },
    checkIsLastCompleted: function () {
        if (addOptionMarker.isNew == true) {
            swal('提示信息', '请先绘制完当前标记图形！', 'info');
            return !0;
        }
    },

    polylineStartPoint: null,
    polylineEndPoint: null,
    polyline: null,
    polylineLayerGroup: L.layerGroup(),
    createTextMarker: function () {
        if (multiFunctionMarker.checkIsLastCompleted())
            return;
        addOptionMarker.isNew = true;
        addOptionMarker.markerType = 1;

        multiFunctionMarker.hideTool();
        multiFunctionMarker.polyline = null;
        multiFunctionMarker.polylineStartPoint = null;
        multiFunctionMarker.polylineEndPoint = null;
        if (map.hasLayer(multiFunctionMarker.polylineLayerGroup) === false)
            map.addLayer(multiFunctionMarker.polylineLayerGroup);
        map.on("click", multiFunctionMarker.polylineClick);//.on("dblclick", multiFunctionMarker.polylinedblclick);
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
    },
    polylineClick: function (e) {
        multiFunctionMarker.polylineStartPoint = e.latlng;
        map.on("mousemove", multiFunctionMarker.polylineMouseMove);
        map.on("mousemove", multiFunctionMarker.polylineKeyPress);
        map.off("click", multiFunctionMarker.polylineClick);
        map.on("contextmenu", multiFunctionMarker.polylinedblclick);
    },
    createPatterns: function (lineHead) {
        //var arrowSize=5,circleRadius=10;

        var pattern = lineHead == 0 ? [{
            offset: '100%',
            repeat: 0,
            symbol: L.Symbol.arrowHead({
                pixelSize: 8,
                polygon: true,
                pathOptions: {
                    stroke: true,
                    color: multiFunctionMarker.params.lineColor,
                    fillOpacity: 1
                }
            })
        }] : [{
            offset: '100%',
            repeat: 0,
            symbol: L.Symbol.circleHead({
                circleOptions: {
                    radius: 10,
                    color: multiFunctionMarker.params.lineColor,
                    fillOpacity: 0,
                    stroke: true
                }
            })
        }];
        return pattern;
    },
    polylineMouseMove: function (e) {
        $(".leaflet-interactive").css({"cursor": "crosshair"});
        if (multiFunctionMarker.polyline)
            multiFunctionMarker.polylineLayerGroup.removeLayer(multiFunctionMarker.polyline);

        var middlePoint, firstSegmentLength, secendSegmentLength = 50;
        if (multiFunctionMarker.isAltKey) {
            middlePoint = {lat: e.latlng.lat, lng: multiFunctionMarker.polylineStartPoint.lng};
        } else {
            middlePoint = e.latlng;
        }
        firstSegmentLength = common.calcLineLength(multiFunctionMarker.polylineStartPoint, middlePoint);
        if (firstSegmentLength / 5 > 50) secendSegmentLength = firstSegmentLength / 5;
        if (firstSegmentLength < 50) secendSegmentLength = firstSegmentLength / 3;

        multiFunctionMarker.polylineEndPoint = e.latlng.lng >= multiFunctionMarker.polylineStartPoint.lng
            ? [middlePoint.lat, middlePoint.lng + secendSegmentLength] : [middlePoint.lat, middlePoint.lng - secendSegmentLength];
        multiFunctionMarker.createPolylineExt([multiFunctionMarker.polylineEndPoint, middlePoint, multiFunctionMarker.polylineStartPoint],
            multiFunctionMarker.params.lineHead, multiFunctionMarker.params.lineColor, multiFunctionMarker.params.lineWeight, multiFunctionMarker.params.textSize,
            '', common.getNowTime(new Date()), '', '', '', '', '', true);
    },
    polylinedblclick: function () {
        multiFunctionMarker.commonPopupSet(multiFunctionMarker.polyline, 1, multiFunctionMarker.polylineLayerGroup, multiFunctionMarker.polylineEndPoint, multiFunctionMarker.polylineClick,
            '', common.getNowTime(new Date()), '', '', '');
        multiFunctionMarker.polyline.openPopup();
        map.off("contextmenu", multiFunctionMarker.polylinedblclick).off("mousemove", multiFunctionMarker.polylineMouseMove).off("mousemove", multiFunctionMarker.polylineKeyPress);
        common.setCursorForMeasure("#drawings_viewer", "");
        $(".leaflet-interactive").css({"cursor": ""});
    },
    polylineKeyPress: function (e) {
        if (e.originalEvent.altKey) multiFunctionMarker.isAltKey = true;
        else multiFunctionMarker.isAltKey = false;
    },
    createPolylineExt: function (latlngs, lineHead, lineColor, lineWeight, textSize, content, date, xh, className, opinionType, opinionProfession, fireControlSelected, notbindpop) {
        multiFunctionMarker.polyline = L.polylineMarkerOpinion(latlngs, {
            color: lineColor,
            weight: lineWeight,
            patterns: multiFunctionMarker.createPatterns(lineHead),
            textSize: textSize,
            textColor: lineColor,
            opinions: "",
            className: className
        });
        if (notbindpop == true) {
            multiFunctionMarker.polyline.addTo(multiFunctionMarker.polylineLayerGroup);
        } else {
            multiFunctionMarker.commonPopupSet(multiFunctionMarker.polyline, 1, multiFunctionMarker.polylineLayerGroup, latlngs[2], multiFunctionMarker.polylineClick,
                content, date, opinionType, opinionProfession, fireControlSelected);
            if (content != '') {
                multiFunctionMarker.polyline.setOpinions(content.slice(0, 9));
            }
        }
    },

    centerPoint: null,
    r: null,
    circleLayerGroup: L.layerGroup(),
    circle: null,
    mouseStartPoint: null,
    createCircle: function () {
        if (multiFunctionMarker.checkIsLastCompleted())
            return;

        addOptionMarker.isNew = true;
        addOptionMarker.markerType = 2;

        multiFunctionMarker.hideTool();
        multiFunctionMarker.circle = null;
        multiFunctionMarker.centerPoint = null;
        multiFunctionMarker.r = null;
        if (map.hasLayer(multiFunctionMarker.circleLayerGroup) == false)
            map.addLayer(multiFunctionMarker.circleLayerGroup);
        map.on("click", multiFunctionMarker.circleClick).on("contextmenu", multiFunctionMarker.circledblclick);
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
    },
    circleClick: function (e) {
        multiFunctionMarker.centerPoint = e.latlng;
        map.on("mousemove", multiFunctionMarker.circleMouseMove);
    },
    circleMouseMove: function (e) {
        map.off("click", multiFunctionMarker.circleClick);
        $(".leaflet-interactive").css({"cursor": "crosshair"});
        if (multiFunctionMarker.circle)
            multiFunctionMarker.circleLayerGroup.removeLayer(multiFunctionMarker.circle);
        if (multiFunctionMarker.centerPoint) {
            multiFunctionMarker.r = common.calcLineLength(multiFunctionMarker.centerPoint, e.latlng);
            multiFunctionMarker.createCircleExt(multiFunctionMarker.centerPoint, multiFunctionMarker.r,
                multiFunctionMarker.params.circleColor, multiFunctionMarker.params.circleFillColor, multiFunctionMarker.params.circleFillOpacity, multiFunctionMarker.params.circleWeight,
                '', common.getNowTime(new Date()), '', '', '', '', '', true);
        }
    },
    circledblclick: function (e) {
        multiFunctionMarker.commonPopupSet(multiFunctionMarker.circle, 2, multiFunctionMarker.circleLayerGroup, multiFunctionMarker.centerPoint, multiFunctionMarker.circleClick,
            '', common.getNowTime(new Date()), '', '', '');
        multiFunctionMarker.circle.openPopup();
        //multiFunctionMarker.circle.on("dblclick", multiFunctionMarker.circleDragInit);
        map.off("click", multiFunctionMarker.circleClick).off("mousemove", multiFunctionMarker.circleMouseMove).off("contextmenu", multiFunctionMarker.circledblclick);
        common.setCursorForMeasure("#drawings_viewer", "");
        $(".leaflet-interactive").css({"cursor": ""});
    },
    createCircleExt: function (centerPoint, r, circleColor, circleFillColor, circleFillOpacity, circleWeight, content, date, xh, className, opinionType, opinionProfession, fireControlSelected, notbindpop) {
        multiFunctionMarker.circle = L.circle(centerPoint, {
            radius: r,
            color: circleColor,
            fillColor: circleFillColor,
            fillOpacity: circleFillOpacity / 100,
            weight: circleWeight,
            className: className
        });
        if (notbindpop == true) {
            multiFunctionMarker.circleLayerGroup.addLayer(multiFunctionMarker.circle);
        } else {
            multiFunctionMarker.commonPopupSet(multiFunctionMarker.circle, 2, multiFunctionMarker.circleLayerGroup, centerPoint, multiFunctionMarker.circleClick,
                content, date, opinionType, opinionProfession, fireControlSelected);
        }
    },

    circleDragInit: function (e) {
        multiFunctionMarker.centerPoint = multiFunctionMarker.circle.getLatLng();
        multiFunctionMarker.r = multiFunctionMarker.circle.getRadius();
        multiFunctionMarker.mouseStartPoint = e.latlng;
        map.on("mousemove", multiFunctionMarker.circleDragStart);
    },
    circleDragStart: function (e) {
        var lat = e.latlng.lat - multiFunctionMarker.mouseStartPoint.lat;
        var lng = e.latlng.lng - multiFunctionMarker.mouseStartPoint.lng;
        var newCenter = {
            lat: multiFunctionMarker.centerPoint.lat + lat,
            lng: multiFunctionMarker.centerPoint.lng + lng
        };
        var newCircle = L.circle(newCenter, {
            radius: multiFunctionMarker.r,
            color: multiFunctionMarker.params.circleColor,
            fillColor: multiFunctionMarker.params.circleFillColor,
            fillOpacity: multiFunctionMarker.params.circleFillOpacity / 100,
            weight: multiFunctionMarker.params.circleWeight
        });
        multiFunctionMarker.circleLayerGroup.removeLayer(multiFunctionMarker.circle);
        multiFunctionMarker.circle = newCircle;
        multiFunctionMarker.circle.bindPopup(multiFunctionMarker.createPop());
        multiFunctionMarker.circleLayerGroup.addLayer(multiFunctionMarker.circle);
        map.on("contextmenu", multiFunctionMarker.circleDragEnd);
        multiFunctionMarker.circle.on("dblclick", multiFunctionMarker.circleDragInit);
    },
    circleDragEnd: function (e) {
        map.off("mousemove", multiFunctionMarker.circleDragStart).off("contextmenu", multiFunctionMarker.circleDragEnd);
    },

    ellipseCenterPoint: null,
    semiMajor: null,
    semiMinor: null,
    tilt: null,
    ellipse: null,
    ellipseLayerGroup: L.layerGroup(),
    createEllipse: function () {
        if (multiFunctionMarker.checkIsLastCompleted())
            return;

        addOptionMarker.isNew = true;
        addOptionMarker.markerType = 3;

        multiFunctionMarker.hideTool();
        multiFunctionMarker.ellipse = null;
        multiFunctionMarker.ellipseCenterPoint = null;
        multiFunctionMarker.semiMajor = null;
        multiFunctionMarker.semiMinor = null;
        multiFunctionMarker.tilt = null;
        if (map.hasLayer(multiFunctionMarker.ellipseLayerGroup) == false)
            map.addLayer(multiFunctionMarker.ellipseLayerGroup);
        map.on("click", multiFunctionMarker.ellipseClick).on("contextmenu", multiFunctionMarker.ellipsedblclick);
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
    },
    ellipseClick: function (e) {
        multiFunctionMarker.ellipseCenterPoint = e.latlng;
        map.on("mousemove", multiFunctionMarker.ellipseMouseMove);
    },
    ellipseMouseMove: function (e) {
        map.off("click", multiFunctionMarker.ellipseClick);
        $(".leaflet-interactive").css({"cursor": "crosshair"});
        if (multiFunctionMarker.ellipse)
            multiFunctionMarker.ellipseLayerGroup.removeLayer(multiFunctionMarker.ellipse);
        multiFunctionMarker.semiMajor = Math.abs(e.latlng.lng - multiFunctionMarker.ellipseCenterPoint.lng);
        multiFunctionMarker.semiMinor = Math.abs(e.latlng.lat - multiFunctionMarker.ellipseCenterPoint.lat);
        if (multiFunctionMarker.ellipseCenterPoint && multiFunctionMarker.semiMajor > 0 && multiFunctionMarker.semiMinor > 0) {
            multiFunctionMarker.tilt = 180 / Math.PI * Math.atan(multiFunctionMarker.semiMinor / multiFunctionMarker.semiMajor);
            if (multiFunctionMarker.tilt < 60 && multiFunctionMarker.tilt > 40) {
                multiFunctionMarker.semiMajor = common.calcLineLength(multiFunctionMarker.ellipseCenterPoint, e.latlng);
                multiFunctionMarker.semiMinor = multiFunctionMarker.semiMajor / 2;

                if (e.latlng.lng > multiFunctionMarker.ellipseCenterPoint.lng && e.latlng.lat > multiFunctionMarker.ellipseCenterPoint.lat) {
                    multiFunctionMarker.tilt = 45;
                } else if (e.latlng.lng > multiFunctionMarker.ellipseCenterPoint.lng && e.latlng.lat < multiFunctionMarker.ellipseCenterPoint.lat) {
                    multiFunctionMarker.tilt = -45;
                } else if (e.latlng.lng < multiFunctionMarker.ellipseCenterPoint.lng && e.latlng.lat > multiFunctionMarker.ellipseCenterPoint.lat) {
                    multiFunctionMarker.tilt = 135;
                } else {
                    multiFunctionMarker.tilt = -135;
                }
            } else {
                multiFunctionMarker.tilt = 90;
            }
            //创建椭圆
            multiFunctionMarker.createEllipseExt(multiFunctionMarker.ellipseCenterPoint, [multiFunctionMarker.semiMinor, multiFunctionMarker.semiMajor], multiFunctionMarker.tilt,
                multiFunctionMarker.params.ellipseColor, multiFunctionMarker.params.ellipseFillColor, multiFunctionMarker.params.ellipseFillOpacity, multiFunctionMarker.params.ellipseWeight,
                '', common.getNowTime(new Date()), '', '', '', '', '', true)
        }
    },
    ellipsedblclick: function (e) {
        multiFunctionMarker.commonPopupSet(multiFunctionMarker.ellipse, 3, multiFunctionMarker.ellipseLayerGroup, multiFunctionMarker.ellipseCenterPoint, multiFunctionMarker.ellipseClick,
            '', common.getNowTime(new Date()), '', '', '');
        multiFunctionMarker.ellipse.openPopup();
        map.off("click", multiFunctionMarker.ellipseClick).off("mousemove", multiFunctionMarker.ellipseMouseMove).off("contextmenu", multiFunctionMarker.ellipsedblclick);
        common.setCursorForMeasure("#drawings_viewer", "");
        $(".leaflet-interactive").css({"cursor": ""});
    },
    createEllipseExt: function (ellipseCenterPoint, semis, tilt, ellipseColor, ellipseFillColor, ellipseFillOpacity, ellipseWeight, content, date, xh, className, opinionType, opinionProfession, fireControlSelected, notbindpop) {
        multiFunctionMarker.ellipse = L.ellipse(ellipseCenterPoint, semis, tilt,
            {
                color: ellipseColor,
                fillColor: ellipseFillColor,
                fillOpacity: ellipseFillOpacity / 100,
                weight: ellipseWeight,
                className: className
            });
        if (notbindpop == true) {
            multiFunctionMarker.ellipseLayerGroup.addLayer(multiFunctionMarker.ellipse);
        } else {
            multiFunctionMarker.commonPopupSet(multiFunctionMarker.ellipse, 3, multiFunctionMarker.ellipseLayerGroup, ellipseCenterPoint, multiFunctionMarker.ellipseClick,
                content, date, opinionType, opinionProfession, fireControlSelected);
        }
    },

    firstPoint: null,
    rectangleLayerGroup: L.layerGroup(),
    rectangle: null,
    bounds: null,
    createRectangle: function () {
        if (multiFunctionMarker.checkIsLastCompleted())
            return;

        addOptionMarker.isNew = true;
        addOptionMarker.markerType = 4;

        multiFunctionMarker.hideTool();
        multiFunctionMarker.rectangle = null;
        multiFunctionMarker.firstPoint = null;
        multiFunctionMarker.bounds = null;
        if (map.hasLayer(multiFunctionMarker.rectangleLayerGroup) == false)
            map.addLayer(multiFunctionMarker.rectangleLayerGroup);
        map.on("click", multiFunctionMarker.rectangleClick).on("contextmenu", multiFunctionMarker.rectangledblclick);
        common.setCursorForMeasure("#drawings_viewer", "crosshair");
    },
    rectangleClick: function (e) {
        multiFunctionMarker.firstPoint = e.latlng;
        map.on("mousemove", multiFunctionMarker.rectangleMouseMove);
    },
    rectangleMouseMove: function (e) {
        map.off("click", multiFunctionMarker.rectangleClick);
        $(".leaflet-interactive").css({"cursor": "crosshair"});
        if (multiFunctionMarker.rectangle)
            multiFunctionMarker.rectangleLayerGroup.removeLayer(multiFunctionMarker.rectangle);
        multiFunctionMarker.bounds = [multiFunctionMarker.firstPoint, e.latlng];
        multiFunctionMarker.createRectangleExt(multiFunctionMarker.bounds,
            multiFunctionMarker.params.rectangleColor, multiFunctionMarker.params.rectangleFillColor, multiFunctionMarker.params.rectangleFillOpacity, multiFunctionMarker.params.rectangleWeight,
            '', common.getNowTime(new Date()), '', '', '', '', '', true);
    },
    rectangledblclick: function (e) {
        multiFunctionMarker.commonPopupSet(multiFunctionMarker.rectangle, 4, multiFunctionMarker.rectangleLayerGroup, multiFunctionMarker.rectangle.getBounds().getCenter(), multiFunctionMarker.rectangleClick,
            '', common.getNowTime(new Date()), '', '', '');
        multiFunctionMarker.rectangle.openPopup();
        map.off("click", multiFunctionMarker.rectangleClick).off("mousemove", multiFunctionMarker.rectangleMouseMove).off("contextmenu", multiFunctionMarker.rectangledblclick);
        common.setCursorForMeasure("#drawings_viewer", "");
        $(".leaflet-interactive").css({"cursor": ""});
    },
    createRectangleExt: function (bounds, rectangleColor, rectangleFillColor, rectangleFillOpacity, rectangleWeight, content, date, xh, className, opinionType, opinionProfession, fireControlSelected, notbindpop) {
        multiFunctionMarker.rectangle = L.rectangle(bounds, {
            color: rectangleColor,
            fillColor: rectangleFillColor,
            fillOpacity: rectangleFillOpacity / 100,
            weight: rectangleWeight,
            className: className
        });
        if (notbindpop == true) {
            multiFunctionMarker.rectangleLayerGroup.addLayer(multiFunctionMarker.rectangle);
        } else {
            multiFunctionMarker.commonPopupSet(multiFunctionMarker.rectangle, 4, multiFunctionMarker.rectangleLayerGroup, multiFunctionMarker.rectangle.getBounds().getCenter(), multiFunctionMarker.rectangleClick,
                content, date, opinionType, opinionProfession, fireControlSelected);
        }
    },

    openConfig: function () {
        var modal = $("#setMarkerStyle");
        modal.on('hidden.bs.modal', $.proxy(function () {
            this.off('hidden.bs.modal');
            $('input', this).each(function () {
                $(this).removeClass('is-invalid').removeClass('is-valid');
            });

        }, modal));
        modal.on('shown.bs.modal', $.proxy(function () {
            this.off('shown.bs.modal');
            this.on('click', '.modal-footer>button.btn-opinion-config-primary', multiFunctionMarker.saveSetting);
            this.on('click', '.modal-footer>button.btn-opinion-config-secondary', multiFunctionMarker.closeModel);
            this.on('blur', '.m-weight', function () {
                var minWeight = 0.1,
                    maxWeight = 10;
                if (!this.value) return;
                this.value >= maxWeight && (this.value = maxWeight);
                this.value <= minWeight && (this.value = minWeight);
                //this.value = this.value >>> 0;
            });
            this.on('blur', '.m-fillopactiy', function () {
                var minFillOpactiy = 0,
                    maxFillOpactiy = 1;
                if (!this.value) return;
                this.value >= maxFillOpactiy && (this.value = maxFillOpactiy);
                this.value <= minFillOpactiy && (this.value = minFillOpactiy);
                //this.value = this.value >>> 0;
            });
            var paramArray = $('#opinion_config_form').data('opinionConfig');
            if (!paramArray || !paramArray.length) {
                paramArray = Object.keys(multiFunctionMarker.params).map(function (key) {
                    return {name: key, value: multiFunctionMarker.params[key]}
                })
            }
            paramArray.forEach(function (per) {
                var target = $('input[name="' + per.name + '"]', '#opinion_config_form');
                if (!target.length) return;
                if (target.length > 1) {
                    target.eq(per.value).attr('checked', true);
                } else {
                    target.val(per.value);
                }
            });
            $(".opinion-color").each(function () {
                $(this).minicolors();
            });
        }, modal)).modal("show");
    },
    saveSetting: function (e) {
        e && e.preventDefault();
        var form = $('#opinion_config_form');
        var paramArray = form.serializeArray();

        paramArray.forEach(function (per) {
            multiFunctionMarker.params.hasOwnProperty(per.name) && (multiFunctionMarker.params[per.name] = per.value >>> 0 || per.value);
            $('#' + per.name, '#opinion_config_form').removeClass('is-invalid').removeClass('is-valid');
        });
        console.log(multiFunctionMarker.params);
        form.data('opinionConfig', paramArray);

        multiFunctionMarker.closeModel();
    },
    closeModel: function () {
        $("#setMarkerStyle").modal("hide");
    },
    getSetting: function () {
        return multiFunctionMarker.params;
    }
}

var magnifyingGlass = {
    initGlass: function () {
        if (!map) return toastr.remove(), toastr.warning('请先打开图纸', '操作提示', {timeOut: 1500});
        $("#magnifyingGlassLi").toggleClass("viewer-toolbar-sel");
        map.on("mousemove", magnifyingGlass.modiyfyGlassShape);
        var glassDiv = $("#glassDiv");
        if (glassDiv.is(':hidden')) {
            glassDiv.css({
                "right": "5px",
                "top": "10px"
            });
            $(document).on("mousemove", magnifyingGlass.bodyMousemove);
            map.on("mousemove", magnifyingGlass.mapMousemove);
            map.on("contextmenu", function (e) {
                magnifyingGlass.initGlass();
                map.off("mousemove", magnifyingGlass.modiyfyGlassShape);
            });
            glassDiv.show(function () {
                $(this).parent().addClass('glass-open');
            });
        } else {
            $(document).off("mousemove", magnifyingGlass.bodyMousemove);
            map.off("mousemove", magnifyingGlass.mapMousemove)
            glassDiv.hide(function () {
                $(this).parent().removeClass('glass-open');
            });
            map.off("contextmenu");
        }
    },
    mapMousemove: function (e) {
        var screenPoint = map.mouseEventToContainerPoint(e.originalEvent);
        var baseCanvas = document.getElementById("tem_canvas");
        // var baseCanvas_context = gDrawingViewer.base_ctx; baseCanvas.getContext("2d");
        var baseCanvas_context = gDrawingViewer.base_ctx;
        var glassDiv = $("#glassDiv");
        var width = glassDiv.width();
        var height = glassDiv.height();

        var x = e.latlng.lng + gDrawingViewer.width * 0.5;
        var y = gDrawingViewer.height * 0.5 - e.latlng.lat;

        var imageData = baseCanvas_context.getImageData(x - width / 2, y - height / 4, width, height);

        var glass_canvas = document.getElementById("glass_canvas");
        var glass_context = glass_canvas.getContext("2d");
        glass_context.putImageData(imageData, 0, 0);
    },
    bodyMousemove: function (e) {
        var glassDiv = $("#glassDiv");
        var width = glassDiv.width();
        var height = glassDiv.height();
        var top = e.clientY - height / 2;
        var left = e.clientX + width / 2 - 5;
        glassDiv.css({
            "right": "calc(100% - " + left + "px)",
            "top": top + "px"
        });
    },
    modiyfyGlassShape: function (e) {
        var glassDiv = $("#glassDiv");
        if (e.originalEvent.shiftKey) {
            glassDiv.css({
                "border-radius": "200px"
            });
        } else {
            glassDiv.css({
                "border-radius": ""
            });
        }
    }
}

var screenshotTool = {
    width: 406,
    height: 406,
    screenshotDiv: null,
    screenshotButton: null,
    init: function () {
        this.screenshotDiv = $("#screenshotDiv");
        this.screenshotButton = $("#screenshotButton");
        var top = ($("body").height() - this.height) / 2;
        var left = ($("body").width() - this.width) / 2;
        if (this.screenshotDiv.is(':hidden')) {
            this.screenshotDiv.css({
                "width": this.width + "px",
                "height": this.height + "px",
                "top": top + "px",
                "left": left + "px"
            }).show();
            this.screenshotButton.css({
                "top": (top + this.height - 5) + "px",
                "left": (left + this.width - this.screenshotButton.width() - 12) + "px",
            }).show();
            $('.screenshot-close-btn').off('click').on('click', $.proxy(screenshotTool, 'close'));
            $('.screenshot-done-btn').off('click').on('click', $.proxy(screenshotTool, 'doScreenshot'));
            this.screenshotDiv.resizable({
                handles: "n, e, s, w, ne, se, sw, nw, all",
                containment: "document",
                resize: screenshotTool.divChange
            }).draggable({
                containment: "document",
                drag: screenshotTool.divChange
            });
        } else {
            this.close();
        }
    },
    divChange: function (event, ui) {
        var position = ui.position;
        var top = position.top + screenshotTool.screenshotDiv.height() + 3;
        var left = position.left + screenshotTool.screenshotDiv.width() - screenshotTool.screenshotButton.width() - 8;
        if (top > $("body").height() - screenshotTool.screenshotButton.height() - 10) {
            top = position.top > screenshotTool.screenshotButton.height() + 5 ? position.top - screenshotTool.screenshotButton.height() - 10 : 1;
        }
        screenshotTool.screenshotButton.css({
            "top": top + "px",
            "left": left + "px"
        });
    },
    doScreenshot: function () {
        var top = screenshotTool.screenshotDiv.offset().top;
        var left = screenshotTool.screenshotDiv.offset().left;
        var screenPoint = L.point(left, top);
        var latLng = map.containerPointToLatLng(screenPoint);

        // var baseCanvas = document.getElementById("tem_canvas");
        var baseCanvas = gDrawingViewer.base_canvas;
        var baseCanvas_context = baseCanvas.getContext("2d");
        var width = screenshotTool.screenshotDiv.width();
        var height = screenshotTool.screenshotDiv.height();

        var x = latLng.lng + gDrawingViewer.width * 0.5;
        var y = gDrawingViewer.height * 0.5 - latLng.lat;

        var screenPoint2 = L.point(left + width, top + height);
        var latLng2 = map.containerPointToLatLng(screenPoint2);
        width = Math.abs(latLng2.lng - latLng.lng);
        height = Math.abs(latLng2.lat - latLng.lat);

        var imageData = baseCanvas_context.getImageData(x, y, width, height);
        var screenshot_canvas = document.getElementById("screenshot_canvas");
        screenshot_canvas.width = width;
        screenshot_canvas.height = height;
        var screenshot_context = screenshot_canvas.getContext("2d");
        screenshot_context.putImageData(imageData, 0, 0);
        if (window.navigator.msSaveOrOpenBlob) {
            var imgData = screenshot_canvas.msToBlob();
            var blobObj = new Blob([imgData]);
            window.navigator.msSaveOrOpenBlob(blobObj, '截图.png');
        }else{
            var a = document.createElement('a');
            a.href = screenshot_canvas.toDataURL('image/png', 1).replace('image/png', 'image/octet-stream'); //下载图片
            a.download = '截图.png';
            a.click();
        }


        toastr.success('截图成功', '操作提示', {timeOut: 1500});
        screenshotTool.close();
    },
    close: function () {
        this.screenshotDiv.resizable("destroy").draggable("destroy");
        this.screenshotDiv.hide();
        this.screenshotButton.hide();
    }
}