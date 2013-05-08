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
 * @param {?Element} elem
 */
io.map.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
};


/**
 * Renders the map page inside this.elem element.
 */
io.map.Page.prototype.render = function() {
  this.main.setMenuLinkActive('mapButton');
  soy.renderElement(this.elem, io.soy.main.map);
  this.initMap();
  this.putSampleData();
};

var W = 47.369057;
var E = 8.541671;

io.map.Page.prototype.putSampleData = function() {
  var location = new google.maps.LatLng(W, E);
  var marker = new google.maps.Marker(
      {position: location, map: this.map});
  var infowindow = new google.maps.InfoWindow({
    content: 'hiho', size: new google.maps.Size(50, 50)
  });
  var self = this;
  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(self.map, marker);
  });
};

io.map.Page.prototype.initMap = function() {
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(W, E),
    zoom: 14,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  this.map = new google.maps.Map(mapDiv, mapOptions);
};

