'use strict';
goog.provide('io');
goog.provide('io.start');

goog.require('goog.dom');
goog.require('goog.events');
goog.require('goog.net.jsloader');

io.start = function() {
  var newDiv = goog.dom.createDom('h1', {'id': 'map_canvas'});
  goog.dom.appendChild(document.body, newDiv);

  var initialize = function() {
    var mapOptions = {
      center: new google.maps.LatLng(-34.397, 150.644),
      zoom: 8,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(newDiv, mapOptions);
  };
  initialize();
};

goog.exportSymbol('io.start', io.start);

