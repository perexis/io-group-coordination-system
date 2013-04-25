'use strict';
goog.provide('io');
goog.provide('io.start');

goog.require('goog.dom');
goog.require('io.soy');

io.start = function() {
  soy.renderElement(document.body, io.soy.base);
  io.initMap();
  io.putSampleData();
};

var W = -34.397;
var E = 150.644;

io.putSampleData = function() {
  var location = new google.maps.LatLng(W, E);
  var marker = new google.maps.Marker({
    position: location, map: io.map
  });
  var infowindow = new google.maps.InfoWindow({
    content: 'hiho', size: new google.maps.Size(50, 50)
  });
  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(io.map, marker);
  });
};

io.initMap = function() {
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(-34.397, 150.644),
    zoom: 8,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  io.map = new google.maps.Map(mapDiv, mapOptions);
};

goog.exportSymbol('io', io);
goog.exportSymbol('io.start', io.start);

