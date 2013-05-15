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
  this.pendingRefresh = false;
  this.markers = {};
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
    var timer = new goog.Timer(1000);
    timer.start();
    goog.events.listen(timer, goog.Timer.TICK, function() {
      self.refreshLayers();
    });
  });
};

io.map.Page.prototype.refreshLayers = function() {
  if (!this.pendingRefresh) {
    var self = this;
    self.pendingRefresh = true;
    var api = this.main.api;
    api.getLayers(function(layers) {
      var done = 0;
      var finished = function() {
        done++;
        if (done == layers.length) {
          self.pendingRefresh = false;
        }
      };
      goog.array.forEach(layers, function(layer) {
        api.getMapItems({'layer': layer}, function(items) {
          var newMarkers = {};
          goog.array.forEach(items, function(item) {
            self.putMapItem(item, newMarkers, self.markers[layer]);
          });
          for (var key in self.markers[layer]) {
            var obj = self.markers[layer][key];
            obj['marker'].setVisible(false);
            obj['info'].close();
          }
          self.markers[layer] = newMarkers;
          finished();
        }, finished);
      });
    });
  }
};

io.map.Page.prototype.putMapItem = function(item, newMarkers, markers) {
  var self = this;
  var pos = item['position'];
  var loc = new google.maps.LatLng(pos['longitude'], pos['latitude']);
  var id = item['id'];
  if (markers != undefined && markers[id]) {
    newMarkers[id] = markers[id];
    newMarkers[id]['marker'].setPosition(loc);
    delete markers[id];
  } else {
    var marker = new google.maps.Marker({position: loc, map: self.map});
    var infowindow = new google.maps.InfoWindow({
      content: item['data'], size: new google.maps.Size(50, 50)
    });
    google.maps.event.addListener(marker, 'click', function() {
      infowindow.open(self.map, marker);
    });
    newMarkers[id] = {'marker': marker, 'info:': infowindow};
  }
};

io.map.Page.prototype.onMapClick = function(e) {
  var self = this;
  var hideCurrentMarker = function() {
    if (self.curClick != null) {
      self.curClick['info'].close();
      self.curClick['marker'].setVisible(false);
      self.curClick = null;
    }
  };
  hideCurrentMarker();

  var marker = new google.maps.Marker({position: e.latLng, map: this.map});
  var infowindow = new google.maps.InfoWindow({
    content: soy.renderAsFragment(io.soy.map.addElement)
  });
  infowindow.open(this.map, marker);
  this.curClick = {'info': infowindow, 'marker': marker};
  google.maps.event.addListener(infowindow, 'closeclick', function(e) {
    hideCurrentMarker();
  });

  var onItemAdded = function(json) {
    io.log().info('Successfully added item');
    hideCurrentMarker();
    self.refreshLayers();
  };

  var onItemAddError = function(err) {
    io.log().warning('Could not add item: ' + err);
  };

  var clickPoint = new io.api.Point(e.latLng['kb'], e.latLng['lb']);
  var addCallback = function(e) {
    e.preventDefault();
    var layer = goog.dom.getElement('addItemLayer').value;
    var data = goog.dom.getElement('addItemData').value;
    io.log().info('Adding item, layer:' + layer + ' data:' + data);
    self.main.api.addItemToLayer({'point': clickPoint, 'data': data,
      'layer': layer}, onItemAdded, onItemAddError);
  };
  goog.events.listen(goog.dom.getElement('addItemForm'),
      goog.events.EventType.SUBMIT, addCallback);

  this.map.panTo(e.latLng);
};

io.map.Page.prototype.initMap = function() {
  var W = 47.369057;
  var E = 8.541671;
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(W, E),
    zoom: 3,
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

