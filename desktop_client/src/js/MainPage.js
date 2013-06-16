/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';
goog.provide('io.main.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.groups.Page');
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
 * @param {?Element} root
 */
io.main.Page = function(login, sid, api, logout, root) {
  this.sid = sid;
  this.login = login;
  this.api = api;
  this.logout = logout;
  this.root = root;
  this.restartTimers();
  this.timer.start();
  this.slowTimer.start();
  var self = this;
  api.setExceptionHandler('InvalidSessionID', function(e) {
    io.log().warning('Session has expired, logging out');
    self.restartTimers();
    self.logout('Session has expired');
  });
};

io.main.Page.prototype.restartTimers = function() {
  if (this.timer != undefined) {
    this.timer.stop();
    this.slowTimer.stop();
  }
  this.timer = new goog.Timer(2000);
  this.slowTimer = new goog.Timer(5000);
  this.timer.start();
  this.slowTimer.start();
};

io.main.Page.prototype.render = function() {
  soy.renderElement(this.root, io.soy.main.pageHeader,
      {login: this.login});
  this.pageDiv = goog.dom.getElement('page');
  (new io.map.Page(this, this.pageDiv)).render();

  var self = this;
  var onLogoutBtn = function() {
    self.restartTimers();
    io.log().info('Logging out');
    self.api.logout(function() {
      self.logout();
    });
  };

  var onMapBtn = function() {
    self.restartTimers();
    (new io.map.Page(self, self.pageDiv)).render();
  };

  var onSettingsBtn = function() {
    self.restartTimers();
    (new io.settings.Page(self, self.pageDiv)).render();
  };

  var onGroupsBtn = function() {
    self.restartTimers();
    (new io.groups.Page(self, self.pageDiv)).render();
  };

  goog.events.listen(goog.dom.getElement('groupsButton'),
      goog.events.EventType.CLICK, onGroupsBtn);
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
  var elems = ['mapButton', 'settingsButton', 'groupsButton'];
  goog.array.map(elems, function(elid) {
    goog.dom.getElement(elid).className = '';
  });
  goog.dom.getElement(id).className = 'active';
};

