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
goog.provide('io.start');

goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.logger.init');
goog.require('io.main.Page');
goog.require('io.soy.login');
goog.require('io.soy.main');


/**
 * @export
 */
io.start = function() {
  io.logger.init();
  io.log().info('Starting execution');
  io.initLoginPage_();
};


/**
 * @private
 * @param {string=} opt_errormessage
 */
io.initLoginPage_ = function(opt_errormessage) {
  var root = goog.dom.getElement('root');
  soy.renderElement(root, io.soy.login.page);
  if (opt_errormessage) {
    soy.renderElement(goog.dom.getElement('loginError'), io.soy.login.appError,
        {reason: opt_errormessage});
  }
  var loginElem = goog.dom.getElement('loginInput');
  loginElem.focus();
  var callback = function(e) {
    var api = new io.api.ApiConnector();
    soy.renderElement(goog.dom.getElement('loginError'), io.soy.main.empty);
    var login = loginElem.value;
    var password = goog.dom.getElement('passwordInput').value;
    io.log().info('Submitted login form, login: ' + login);
    e.preventDefault();

    var onSuccess = function(json) {
      io.log().info('Successfully logged in, sid:' + json);
      api.setSessionId(json);
      var main = new io.main.Page(login, json, api, io.initLoginPage_, root);
      main.render();
    };

    var onError = function(reason) {
      api.disable();
      if (reason == 'CouldNotLogin') {
        reason = 'Wrong login or password';
      }
      soy.renderElement(goog.dom.getElement('loginError'), io.soy.login.error,
          {reason: reason});
    };
    api.login({'id': login, 'password': password}, onSuccess, onError);
  };
  goog.events.listen(goog.dom.getElement('loginForm'),
      goog.events.EventType.SUBMIT, callback);
};

