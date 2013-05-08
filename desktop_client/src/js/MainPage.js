'use strict';
goog.provide('io.main.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.map.Page');
goog.require('io.settings.Page');
goog.require('io.soy.main');



/**
 * @constructor
 * @param {!string} login
 * @param {!string|number} sid
 * @param {!io.api.ApiConnector} api
 * @param {!function(string=)} logout function to be called after logout.
 */
io.main.Page = function(login, sid, api, logout) {
  this.sid = sid;
  this.login = login;
  this.api = api;
  this.logout = logout;
  api.setExceptionHandler('InvalidSessionID', function(e) {
    io.log().warning('Session has expired, logging out');
    logout('Session has expired');
  });
};

io.main.Page.prototype.render = function() {
  soy.renderElement(document.body, io.soy.main.pageHeader, {login: this.login});
  this.pageDiv = goog.dom.getElement('page');
  (new io.map.Page(this, this.pageDiv)).render();

  var self = this;
  var onLogoutBtn = function() {
    io.log().info('Logging out');
    self.api.logout({'sessionID': self.sid}, function() {
      self.logout();
    });
  };

  var onMapBtn = function() {
    (new io.map.Page(self, self.pageDiv)).render();
  };

  var onSettingsBtn = function() {
    (new io.settings.Page(self, self.pageDiv)).render();
  };

  goog.events.listen(goog.dom.getElement('settingsButton'),
      goog.events.EventType.CLICK, onSettingsBtn);
  goog.events.listen(goog.dom.getElement('mapButton'),
      goog.events.EventType.CLICK, onMapBtn);
  goog.events.listen(goog.dom.getElement('logoButton'),
      goog.events.EventType.CLICK, onMapBtn);
  goog.events.listen(goog.dom.getElement('logoutButton'),
      goog.events.EventType.CLICK, onLogoutBtn);
};


/**
 * @param {!string} id - DOM id.
 */
io.main.Page.prototype.setMenuLinkActive = function(id) {
  var elems = ['mapButton', 'settingsButton'];
  goog.array.map(elems, function(elid) {
    goog.dom.getElement(elid).className = '';
  });
  goog.dom.getElement(id).className = 'active';
};

