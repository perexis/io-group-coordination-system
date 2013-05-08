'use strict';
goog.provide('io.main.Page');

goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.soy.main');



/**
 * @constructor
 * @param {!string} login
 * @param {!string|number} sid
 * @param {!io.api.ApiConnector} api
 * @param {!function()} logout on logout callback.
 */
io.main.Page = function(login, sid, api, logout) {
  this.sid = sid;
  this.login = login;
  this.api = api;
  this.logout = logout;
};

io.main.Page.prototype.render = function() {
  soy.renderElement(document.body, io.soy.main.page, {login: this.login});
  io.initMap();

  var self = this;
  var onLogoutBtn = function() {
    io.log().info('Logging out');
    self.api.logout({'sessionID': self.sid}, function() {
      self.logout();
    });
  };

  goog.events.listen(goog.dom.getElement('logoutButton'),
      goog.events.EventType.CLICK, onLogoutBtn);
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

