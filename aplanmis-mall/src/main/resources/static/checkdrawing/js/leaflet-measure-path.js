// !(function () {
//     'use strict';
var unitSet = 'm';
L.Marker.Measurement = L[L.Layer ? 'Layer' : 'Class'].extend({
    options: {
        pane: 'markerPane'
    },

    initialize: function (latlng, measurement, title, rotation, options) {
        L.setOptions(this, options);

        this._latlng = latlng;
        this._measurement = measurement;
        this._title = title;
        this._rotation = rotation;
    },

    addTo: function (map) {
        map.addLayer(this);
        return this;
    },

    onAdd: function (map) {
        this._map = map;
        var pane = this.getPane ? this.getPane() : map.getPanes().markerPane;
        var el = this._element = L.DomUtil.create('div', 'leaflet-zoom-animated leaflet-measure-path-measurement', pane);
        var inner = L.DomUtil.create('div', '', el);
        inner.title = this._title;
        inner.innerHTML = this._measurement;
        map.on('zoomanim', this._animateZoom, this);

        this._setPosition();
    },

    onRemove: function (map) {
        map.off('zoomanim', this._animateZoom, this);
        var pane = this.getPane ? this.getPane() : map.getPanes().markerPane;
        pane.removeChild(this._element);
        this._map = null;
    },

    _setPosition: function () {
        L.DomUtil.setPosition(this._element, this._map.latLngToLayerPoint(this._latlng));
        this._element.style.transform += ' rotate(' + this._rotation + 'rad)';
    },

    _animateZoom: function (opt) {
        var pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round();
        L.DomUtil.setPosition(this._element, pos);
        this._element.style.transform += ' rotate(' + this._rotation + 'rad)';
    }
});

L.marker.measurement = function (latLng, measurement, title, rotation, options) {
    return new L.Marker.Measurement(latLng, measurement, title, rotation, options);
};

var formatDistance = function (d) {
    var unit,
        feet;

    if (this._measurementOptions.imperial) {
        feet = d / 0.3048;
        if (feet > 3000) {
            d = d / 1609.344;
            unit = 'mi';
        } else {
            d = feet;
            unit = 'ft';
        }
    } else {
        if (unitSet === 'mm') {
            if (d > 50000) {
                d = d / 1000;
                unit = 'm';
            } else {
                unit = 'mm';
            }
        } else {
            d = d / 1000;
            if (d > 50000) {
                d = d / 1000;
                unit = 'km';
            } else {
                unit = 'm';
            }
        }
    }

    if (d < 100) {
        return d.toFixed(2) + ' ' + unit;
    } else {
        return Math.round(d) + ' ' + unit;
    }
}

var formatArea = function (a) {
    var unit,
        sqfeet;

    if (this._measurementOptions.imperial) {
        if (a > 404.685642) {
            a = a / 4046.85642;
            unit = 'ac';
        } else {
            a = a / 0.09290304;
            unit = 'ft²';
        }
    } else {
        if (unitSet === 'mm') {
            if (a > 100000) {
                a = a / 1000000;
                unit = 'm²';
            } else {
                unit = 'mm²';
            }
        } else {
            a = a / 1000000;
            if (a > 500000) {
                a = a / 1000;
                unit = 'km';
            } else {
                unit = 'm';
            }
        }
    }

    if (a < 100) {
        return a.toFixed(2) + ' ' + unit;
    } else {
        return Math.round(a) + ' ' + unit;
    }
}

var RADIUS = 6378137;
// ringArea function copied from geojson-area
// (https://github.com/mapbox/geojson-area)
// This function is distributed under a separate license,
// see LICENSE.md.
//原方法
// var ringArea = function ringArea(coords) {
//     var rad = function rad(_) {
//         return _ * Math.PI / 180;
//     };
//     var p1, p2, p3, lowerIndex, middleIndex, upperIndex,
//         area = 0,
//         coordsLength = coords.length;
//
//     if (coordsLength > 2) {
//         for (var i = 0; i < coordsLength-2; i++) {
//             if (i === coordsLength - 2) {// i = N-2
//                 lowerIndex = coordsLength - 2;
//                 middleIndex = coordsLength - 1;
//                 upperIndex = 0;
//             } else if (i === coordsLength - 1) {// i = N-1
//                 lowerIndex = coordsLength - 1;
//                 middleIndex = 0;
//                 upperIndex = 1;
//             } else { // i = 0 to N-3
//                 lowerIndex = i;
//                 middleIndex = i + 1;
//                 upperIndex = i + 2;
//             }
//             p1 = coords[lowerIndex];
//             p2 = coords[middleIndex];
//             p3 = coords[upperIndex];
//             area += ( rad(p3.lng) - rad(p1.lng) ) * Math.sin( rad(p2.lat));
//         }
//          area = area * RADIUS * RADIUS / 2;
//     }
//
//     return Math.abs(area);
// };

//修改方法
var ringArea = function ringArea(coords, map) {
    var rad = function rad(_) {
        return _ * Math.PI / 180;
    };
    var arr = gDrawingViewer.params.scale.split(':');
    // var bs = 600 * Math.pow(2, 0 - map._zoom) * (1 / 213);
    var bs = Math.pow(2, 0 - map._zoom) * arr[1] / arr[0] / 3.33333;
    var p1, p2, p3, lowerIndex, middleIndex, upperIndex, p11, p22, p33,
        area = 0,
        coordsLength = coords.length;

    if (coordsLength > 2) {
        for (var i = 0; i < coordsLength - 2; i++) {
            if (i == 0) {
                lowerIndex = i;
                middleIndex = i + 1;
                upperIndex = i + 2;
            }
            //最后一次
            else if (coordsLength - 2 == i + 1) {
                lowerIndex = i + 1;
                middleIndex = i + 2;
                upperIndex = 0;
            }
            //大于等于5个点的1
            else if (i == 1) {
                lowerIndex = i + 2;
                middleIndex = i + 3;
                upperIndex = i + 4;
            }
            //大于等于6个点的2
            else if (i == 2) {
                lowerIndex = i + 1;
                middleIndex = i + 3;
                upperIndex = 0;
            } else {
                lowerIndex = i + 1;
                middleIndex = i + 2;
                upperIndex = 0;
            }

            p1 = coords[lowerIndex];
            p2 = coords[middleIndex];
            p3 = coords[upperIndex];
            p11 = map.latLngToLayerPoint(p1);
            p22 = map.latLngToLayerPoint(p2);
            p33 = map.latLngToLayerPoint(p3);
            //area += ( rad(p3.lng) - rad(p1.lng) ) * Math.sin( rad(p2.lat));
            var c = bs * p11.distanceTo(p22);
            var b = bs * p11.distanceTo(p33);
            var a = bs * p22.distanceTo(p33);
            var cosa = (b * b + c * c - a * a) / (2 * b * c);
            var sina = Math.sqrt(1 - cosa * cosa);
            //向量判断 三角形的中间点在线段哪边，p22在向量p33p11
            var vertix = (p11.x - p33.x) * (p22.y - p33.y) - (p11.y - p33.y) * (p22.x - p33.x);
            var zf = vertix > 0 ? 1 : -1;
            area += (zf * (c * b * sina) / 2);
        }
    }

    return Math.abs(area);
};
/**
 * Handles the init hook for polylines and circles.
 * Implements the showOnHover functionality if called for.
 */
var addInitHook = function () {
    var showOnHover = this.options.measurementOptions && this.options.measurementOptions.showOnHover;
    if (this.options.showMeasurements && !showOnHover) {
        this.showMeasurements();
    }
    if (this.options.showMeasurements && showOnHover) {
        this.on('mouseover', function () {
            this.showMeasurements();
        });
        this.on('mouseout', function () {
            this.hideMeasurements();
        });
    }
};

var circleArea = function circleArea(d) {
    var rho = d / RADIUS;
    return 2 * Math.PI * RADIUS * RADIUS * (1 - Math.cos(rho));
};

var override = function (method, fn, hookAfter) {
    if (!hookAfter) {
        return function () {
            var originalReturnValue = method.apply(this, arguments);
            var args = Array.prototype.slice.call(arguments)
            args.push(originalReturnValue);
            return fn.apply(this, args);
        }
    } else {
        return function () {
            fn.apply(this, arguments);
            return method.apply(this, arguments);
        }
    }
};

L.Polyline.include({
    showMeasurements: function (options) {
        if (!this._map || this._measurementLayer) return this;

        this._measurementOptions = L.extend({
            showOnHover: (options && options.showOnHover) || false,
            minPixelDistance: 30,
            showDistances: true,
            showArea: true,
            lang: {
                totalLength: 'Total length',
                totalArea: 'Total area',
                segmentLength: 'Segment length'
            }
        }, options || {});

        this._measurementLayer = L.layerGroup().addTo(this._map);
        this.updateMeasurements();

        //this._map.on('zoomend', this.updateMeasurements, this);

        return this;
    },

    hideMeasurements: function () {
        if (!this._map) return this;

        this._map.off('zoomend', this.updateMeasurements, this);

        if (!this._measurementLayer) return this;
        this._map.removeLayer(this._measurementLayer);
        this._measurementLayer = null;

        return this;
    },

    onAdd: override(L.Polyline.prototype.onAdd, function (originalReturnValue) {
        var showOnHover = this.options.measurementOptions && this.options.measurementOptions.showOnHover;
        if (this.options.showMeasurements && !showOnHover) {
            this.showMeasurements(this.options.measurementOptions);
        }

        return originalReturnValue;
    }),

    onRemove: override(L.Polyline.prototype.onRemove, function (originalReturnValue) {
        this.hideMeasurements();

        return originalReturnValue;
    }, true),

    setLatLngs: override(L.Polyline.prototype.setLatLngs, function (originalReturnValue) {
        this.updateMeasurements();

        return originalReturnValue;
    }),

    spliceLatLngs: override(L.Polyline.prototype.spliceLatLngs, function (originalReturnValue) {
        this.updateMeasurements();

        return originalReturnValue;
    }),

    formatDistance: formatDistance,
    formatArea: formatArea,

    updateMeasurements: function () {
        if (!this._measurementLayer) return this;

        var latLngs = this.getLatLngs(),
            isPolygon = this instanceof L.Polygon,
            options = this._measurementOptions,
            totalDist = 0,
            formatter,
            ll1,
            ll2,
            p1,
            p2,
            pixelDist,
            dist;

        if (latLngs && latLngs.length && L.Util.isArray(latLngs[0])) {
            // Outer ring is stored as an array in the first element,
            // use that instead.
            latLngs = latLngs[0];
        }

        this._measurementLayer.clearLayers();

        if (this._measurementOptions.showDistances && latLngs.length > 1) {
            formatter = this._measurementOptions.formatDistance || L.bind(this.formatDistance, this);

            for (var i = 1, len = latLngs.length; (isPolygon && i <= len) || i < len; i++) {
                ll1 = latLngs[i - 1];
                ll2 = latLngs[i % len];
                dist = ll1.distanceTo(ll2);
                // totalDist += dist;

                p1 = this._map.latLngToLayerPoint(ll1);
                p2 = this._map.latLngToLayerPoint(ll2);

                pixelDist = p1.distanceTo(p2);
                var arr = gDrawingViewer.params.scale.split(':');
                var distance = 0;
                //var bs = 600 * Math.pow(2, 0 - this._map._zoom) * (1 / 213);
                var bs = Math.pow(2, 0 - this._map._zoom) * arr[1] / arr[0] / 3.33333;
                distance = bs * pixelDist;
                totalDist += bs * pixelDist;
                if (pixelDist >= options.minPixelDistance) {
                    L.marker.measurement(
                        this._map.layerPointToLatLng([(p1.x + p2.x) / 2, (p1.y + p2.y) / 2]),
                        formatter(distance), options.lang.segmentLength, this._getRotation(ll1, ll2), options)
                        .addTo(this._measurementLayer);
                }
            }
            // for (var i = 1, len = latLngs.length; (isPolygon && i <= len) || i < len; i++) {
            //     ll1 = latLngs[i - 1];
            //     ll2 = latLngs[i % len];
            //     dist = ll1.distanceTo(ll2);
            //     // totalDist += dist;
            //
            //     p1 = this._map.latLngToLayerPoint(ll1);
            //     p2 = this._map.latLngToLayerPoint(ll2);
            //
            //     pixelDist = p1.distanceTo(p2);
            //
            //     var distance = 0;
            //     var bs = 0.6 * Math.pow(2, 0 - this._map._zoom) * (1 / 213);
            //
            //     distance = bs * pixelDist;
            //     totalDist += bs * pixelDist;
            //     if (pixelDist >= options.minPixelDistance) {
            //         L.marker.measurement(
            //             this._map.layerPointToLatLng([(p1.x + p2.x) / 2, (p1.y + p2.y) / 2]),
            //             formatter(distance), options.lang.segmentLength, this._getRotation(ll1, ll2), options)
            //             .addTo(this._measurementLayer);
            //     }
            // }

            // Show total length for polylines
            if (!isPolygon) {
                L.marker.measurement(ll2, formatter(totalDist), options.lang.totalLength, 0, options)
                    .addTo(this._measurementLayer);
            }
        }

        if (isPolygon && options.showArea && latLngs.length > 2) {
            formatter = options.formatArea || L.bind(this.formatArea, this);
            var area = ringArea(latLngs, this._map);
            L.marker.measurement(this.getBounds().getCenter(),
                formatter(area), options.lang.totalArea, 0, options)
                .addTo(this._measurementLayer);
        }

        return this;
    },
    _getRotation: function (ll1, ll2) {
        var p1 = this._map.project(ll1),
            p2 = this._map.project(ll2);

        return Math.atan((p2.y - p1.y) / (p2.x - p1.x));
    }
});

L.Polyline.addInitHook(function () {
    addInitHook.call(this);
});

L.Circle.include({
    showMeasurements: function (options) {
        if (!this._map || this._measurementLayer) return this;

        this._measurementOptions = L.extend({
            showOnHover: false,
            showArea: true,
            lang: {
                totalArea: 'Total area',
            }
        }, options || {});

        this._measurementLayer = L.layerGroup().addTo(this._map);
        this.updateMeasurements();

        this._map.on('zoomend', this.updateMeasurements, this);

        return this;
    },

    hideMeasurements: function () {
        if (!this._map) return this;

        this._map.on('zoomend', this.updateMeasurements, this);

        if (!this._measurementLayer) return this;
        this._map.removeLayer(this._measurementLayer);
        this._measurementLayer = null;

        return this;
    },

    onAdd: override(L.Circle.prototype.onAdd, function (originalReturnValue) {
        var showOnHover = this.options.measurementOptions && this.options.measurementOptions.showOnHover;
        if (this.options.showMeasurements && !showOnHover) {
            this.showMeasurements(this.options.measurementOptions);
        }

        return originalReturnValue;
    }),

    onRemove: override(L.Circle.prototype.onRemove, function (originalReturnValue) {
        this.hideMeasurements();

        return originalReturnValue;
    }, true),

    setLatLng: override(L.Circle.prototype.setLatLng, function (originalReturnValue) {
        this.updateMeasurements();

        return originalReturnValue;
    }),

    setRadius: override(L.Circle.prototype.setRadius, function (originalReturnValue) {
        this.updateMeasurements();

        return originalReturnValue;
    }),

    formatArea: formatArea,

    updateMeasurements: function () {
        if (!this._measurementLayer) return;

        var latLng = this.getLatLng(),
            options = this._measurementOptions,
            formatter = options.formatArea || L.bind(this.formatArea, this);

        this._measurementLayer.clearLayers();

        if (options.showArea) {
            formatter = options.formatArea || L.bind(this.formatArea, this);
            var area = circleArea(this.getRadius());
            L.marker.measurement(latLng,
                formatter(area), options.lang.totalArea, 0, options)
                .addTo(this._measurementLayer);
        }
    }
})

L.Circle.addInitHook(function () {
    addInitHook.call(this);
});
// })();
