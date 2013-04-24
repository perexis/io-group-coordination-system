'use strict';
goog.provide('io');
goog.provide('io.start');

goog.require('goog.dom');

io.start = function() {
  var newDiv = goog.dom.createDom('div', {'class': 'map_canvas container'});
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

goog.exportSymbol('io', io);
goog.exportSymbol('io.start', io.start);

