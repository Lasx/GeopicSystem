MapControl = function (options) {
    let me = this;

    me.ops = $.extend({
        width: 100,
        height: 600
    }, options);

    me.user = me.ops.username;

    me._init()
};

MapControl.prototype._init = function () {
    let me = this;

    me.div = $("#" + me.ops.div).css({
        position: "relative",
        width: me.ops.width + "%",
        height: me.ops.height + "px"
    });

    me.map = new AMap.Map(me.ops.div, {
        center: [112.3, 28.9],
        zoom: 4
    })
};

MapControl.prototype.initAsPreview = function () {
    let me = this;
    let photos = Query({}, ["photoPath", "AMapGPS"], me.user);
    me._loadMarkerCluster(photos);
};

MapControl.prototype._loadMarkerCluster = function (photos) {
    let me = this;

    let markers = me._constructMarkerArray(photos);

    me.markerCluster = new AMap.MarkerClusterer(me.map, markers, {
        zoomOnClick: false,
        renderClusterMarker: function (context) {
            me._clusterRenderer(context)
        }
    })
};

MapControl.prototype._clusterRenderer = function (context) {
    let div = $("<div>").addClass("cluster-icon");
    let img1 = context.markers[0].getIcon();
    let img2 = context.markers[1].getIcon();
    $("<img>").appendTo(div).attr({
        src: img1,
        width: "10px",
        height: "10px",
    }).addClass("cluster-left-img");
    $("<img>").appendTo(div).attr({
        src: img2,
        width: "10px",
        height: "10px",
    }).addClass("cluster-right-img");

    context.marker.setAnchor("bottom-center");
    context.marker.setOffset(new AMap.Pixel(0,0));
    context.marker.setContent(div);
};

MapControl.prototype._loadMarkers = function (photos) {
    let me = this;

    let markers = me._constructMarkerArray(photos);
    me.map.add(markers);
    me.map.setFitView();
};

MapControl.prototype._constructMarkerArray = function (photos) {
    let markers = [];
    for(let i = 0, len = photos.length; i < len; i++){
        let photo = photos[i];
        let lngLat = new AMap.LngLat(photo.AMapGPS[1], photo.AMapGPS[0]);
        let marker = new AMap.Marker({
            position: lngLat,
            anchor: "bottom-center",
            offset: new AMap.Pixel(0,0),
            icon: "../../" + photo.photoPath
        });
        markers.push(marker);
    }
    return markers;
};