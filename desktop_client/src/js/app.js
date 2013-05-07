'use strict';
goog.provide('io');
goog.provide('io.start');

goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.logger.init');
goog.require('io.soy.login');
goog.require('io.soy.main');

io.start = function() {
  io.logger.init();
  io.api_ = new io.api.ApiConnector();
  io.initLoginPage_();
  //io.initMap();
  //io.putSampleData();
};


/**
 * @private
 */
io.initLoginPage_ = function() {
  soy.renderElement(document.body, io.soy.login.page);
  var callback = function(e) {
    var login = goog.dom.getElement('loginInput').value;
    io.log().info('Submitted login form, login: ' + login);
    e.preventDefault();
  };
  goog.events.listen(goog.dom.getElement('loginForm'),
      goog.events.EventType.SUBMIT, callback);
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

goog.exportSymbol('io.start', io.start);

