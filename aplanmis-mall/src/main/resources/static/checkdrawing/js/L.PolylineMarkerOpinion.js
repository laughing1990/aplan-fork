L.PolylineMarkerOpinion = L.FeatureGroup.extend({
    options: {
        patterns: [],
        opinions: "",
        textSize: 12,
        textColor: '#ff0000'
    },
    initialize: function (latlngs, options) {
        L.FeatureGroup.prototype.initialize.call(this);
        L.Util.setOptions(this, options);
        this._map = null;
        this._latlngs = latlngs;
    },
    _createMarker: function (latlng, opinions) {
        var iconAnchor = this._latlngs[0][1] < this._latlngs[2].lng ? [80, 0] : [0, 0];
        var myIcon = L.divIcon({
            className: 'opinion-div-icon',
            iconSize: [80, 40],
            iconAnchor: iconAnchor,
            html: "<div style='width: 80px;height: 40px;'><h2 style='margin:3px 0;font-size:" +
                (this.options.textSize === undefined ? 12 : this.options.textSize) +
                "px;color:'" + (this.options.textColor == '' ? '#ff0000' : this.options.textColor) + "'>意见：" + opinions + "</h2></div>",
        });//0.75rem
        var point = [latlng[0] + 5, latlng[1] + 5];
        return new L.marker(point, {icon: myIcon});
    },
    getlatlngsInfo: function () {
        return this._latlngs;
    },
    onAdd: function (map) {
        this._map = map;
        this.redraw();
    },
    onRemove: function (map) {
        this._map = null;
        L.FeatureGroup.prototype.onRemove.call(this, map);
    },
    redraw: function () {
        if (!this._map) {
            return;
        }
        this.clearLayers();
        this._draw();
    },
    _draw: function () {
        this._polyline = L.polyline(this._latlngs, this.options);
        this._arrow = L.polylineDecorator(this._polyline, this.options);
        this._marker = this._createMarker(this._latlngs[0], this.options.opinions);
        this.addLayer(this._polyline);
        this.addLayer(this._arrow);
        this.addLayer(this._marker);
    },
    setPatterns: function (patterns) {
        this.options.patterns = patterns;
        this._arrow._patterns = this._arrow._initPatterns(this.options.patterns);
        this.redraw();
    },
    setOpinions: function (opinions) {
        this.options.opinions = opinions;
        this._marker = this._createMarker(this._latlngs[0], opinions);
        this.redraw();
    }
});

L.polylineMarkerOpinion = function (latlngs, option) {
    return new L.PolylineMarkerOpinion(latlngs, option);
}