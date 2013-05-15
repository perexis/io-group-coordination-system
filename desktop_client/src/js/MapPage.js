'use strict';
goog.provide('io.map.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('goog.net.jsloader');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.soy.map');
goog.require('io.soy.settings');



/**
 * @constructor
 * @param {!io.main.Page} main
 * @param {?Element} elem
 */
io.map.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
  this.curClickElem = null;
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
    self.refreshLayers();
  });
};

io.map.Page.prototype.refreshLayers = function() {
  var self = this;
  var api = this.main.api;
  api.getLayers(function(layers) {
    goog.array.map(layers, function(layer) {
      api.getMapItems({'layer': layer}, function(items) {
        goog.array.map(items, function(item) {
          self.putMapItem(item);
        });
      });
    });
  });
};

io.map.Page.prototype.putMapItem = function(item) {
  var self = this;
  var pos = item['position'];
  var loc = new google.maps.LatLng(pos['longitude'], pos['latitude']);
  var marker = new google.maps.Marker({position: loc, map: self.map});
  var infowindow = new google.maps.InfoWindow({
    content: item['data'], size: new google.maps.Size(50, 50)
  });
  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(self.map, marker);
  });
};

io.map.Page.prototype.onMapClick = function(e) {
  var self = this;
  var hidePrevious = function() {
    if (self.curClick != null) {
      self.curClick['info'].close();
      self.curClick['marker'].setVisible(false);
      self.curClick = null;
    }
  };
  hidePrevious();

  var marker = new google.maps.Marker({position: e.latLng, map: this.map});
  var infowindow = new google.maps.InfoWindow({
    content: soy.renderAsFragment(io.soy.map.addElement)
  });
  infowindow.open(this.map, marker);
  this.curClick = {'info': infowindow, 'marker': marker};
  google.maps.event.addListener(infowindow, 'closeclick', function(e) {
    hidePrevious();
  });

  this.map.panTo(e.latLng);
};

io.map.Page.prototype.initMap = function() {
  var W = 47.369057;
  var E = 8.541671;
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(W, E),
    zoom: 2,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  this.map = new google.maps.Map(mapDiv, mapOptions);
  var self = this;
  google.maps.event.addListener(this.map, 'click', function(e) {
    self.onMapClick(e);
  });
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

