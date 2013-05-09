'use strict';
goog.provide('io.map.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('goog.net.jsloader');
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
  var dbg = function(x) {
    io.log().info(goog.debug.deepExpose(x));
  };
  var api = this.main.api;
};


/**
 * Renders the map page inside this.elem element.
 */
io.map.Page.prototype.render = function() {
  var self = this;
  self.main.setMenuLinkActive('mapButton');
  soy.renderElement(self.elem, io.soy.main.map);
  io.map.loadScript(function() {
    self.initMap();
    self.loadLayers();
  });
};

var W = 47.369057;
var E = 8.541671;

io.map.Page.prototype.loadLayers = function() {
  var self = this;
  var api = this.main.api;
  api.getLayers(function(lrs) {
    goog.array.map(lrs, function(l) {
      api.getMapItems({'layer': l}, function(items) {
        goog.array.map(items, function(item) {
          io.log().info('mapuje');
          var pos = item['position'];
          var location = new google.maps.LatLng(W, E);
          //var location =
          //new google.maps.LatLng(p
          //os['longitude'], pos['latitude']);
          io.log().info(goog.debug.deepExpose(location));
          return;
          var marker = new google.maps.Marker(
              {position: location, map: self.map});
          io.log().info(goog.debug.deepExpose(marker));
        });
      });
    });
  });
};

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
    zoom: 1,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  this.map = new google.maps.Map(mapDiv, mapOptions);
};


/** global */
var SCRIPT_LOADED = false;


/**
 * @param {!function()} callback - to be called after script load.
 */
io.map.loadScript = function(callback) {
  if (!SCRIPT_LOADED) {
    io.log().info('Loading maps script');
    SCRIPT_LOADED = true;
    var url = 'https://maps.googleapis.com/maps/api/js?' +
        'key=AIzaSyBEGwOI2vuHOOeMy7WIDoWaRlEu8R3umDw&sensor=false&' +
        'callback=__ioMapsJsonp';
    goog.global['__ioMapsJsonp'] = callback;
    goog.net.jsloader.load(url);
  } else {
    callback();
  }
};

