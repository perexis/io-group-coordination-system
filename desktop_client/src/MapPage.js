'use strict';
goog.provide('io.map.Page');

goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.soy.settings');



/**
 * @constructor
 * @param {!io.main.Page} main
 */
io.map.Page = function(main) {
  this.main = main;
};


/**
 * @param {!Element} elem
 */
io.map.Page.prototype.render = function(elem) {
  soy.renderElement(elem, io.soy.main.map);
  this.initMap();
  this.putSampleData();
};

var W = -34.397;
var E = 150.644;

io.map.Page.prototype.putSampleData = function() {
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

io.map.Page.initMap = function() {
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(-34.397, 150.644),
    zoom: 8,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  io.map = new google.maps.Map(mapDiv, mapOptions);
};

