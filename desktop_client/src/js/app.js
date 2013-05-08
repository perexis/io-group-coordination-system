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
  io.api_ = new io.api.ApiConnector();
  io.initLoginPage_();
};


/**
 * @private
 */
io.initLoginPage_ = function() {
  soy.renderElement(document.body, io.soy.login.page);
  var loginElem = goog.dom.getElement('loginInput');
  loginElem.focus();
  var callback = function(e) {
    soy.renderElement(goog.dom.getElement('loginError'), io.soy.main.empty);
    var login = loginElem.value;
    var password = goog.dom.getElement('passwordInput').value;
    io.log().info('Submitted login form, login: ' + login);
    e.preventDefault();

    var onSuccess = function(json) {
      io.log().info('Successfully logged in, sid:' + json);
      var main = new io.main.Page(login, json, io.api_, io.initLoginPage_);
      main.render();
    };

    var onError = function(reason) {
      if (reason == 'CouldNotLogin') {
        reason = 'Wrong login or password';
      }
      soy.renderElement(goog.dom.getElement('loginError'), io.soy.login.error,
          {reason: reason});
    };
    io.api_.login({'id': login, 'password': password}, onSuccess, onError);
  };
  goog.events.listen(goog.dom.getElement('loginForm'),
      goog.events.EventType.SUBMIT, callback);
};

