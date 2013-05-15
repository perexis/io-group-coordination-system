'use strict';
goog.provide('io.api');
goog.provide('io.api.ApiConnector');
goog.provide('io.api.Point');

goog.require('goog.debug');
goog.require('goog.events');
goog.require('goog.json');
goog.require('goog.net.XhrIo');
goog.require('io.log');



/**
 * @constructor
 * @param {!string|number} longitude
 * @param {!string|number} latitude
 */
io.api.Point = function(longitude, latitude) {
  this['latitude'] = latitude;
  this['longitude'] = longitude;
};



/**
 * @constructor
 */
io.api.ApiConnector = function() {
  this.url = 'http://io.wojtasskorcz.eu.cloudbees.net/';
  this.exHandlers = {};
  io.log().info('Initialized API pointing to: ' + this.url);
};


/**
 * @param {!string} sid
 */
io.api.ApiConnector.prototype.setSessionId = function(sid) {
  this.sid = sid;
};


/**
 * @param {!string} name - the name of ex to handle.
 * @param {!function(string)} callback - the function which will be called.
 */
io.api.ApiConnector.prototype.setExceptionHandler = function(name, callback) {
  this.exHandlers[name] = callback;
};


/**
 * @param {!{id: string, password: string}} data to be sent.
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.login = function(data, callback,
    opt_errcallback) {
  this.request_('login', data, callback, opt_errcallback, true);
};


/**
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.getLayers = function(callback, opt_errcallback) {
  this.request_('getLayers', {}, callback, opt_errcallback);
};


/**
 * @param {!{point: {io.api.Point}, layer: {string}, data: {string}}} data
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.addItemToLayer = function(data, callback,
    opt_errcallback) {
  this.request_('addItemToLayer', data, callback, opt_errcallback);
};


/**
 * @param {!{layer: {string}}} data
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.getMapItems = function(data, callback,
    opt_errcallback) {
  this.request_('getMapItems', data, callback, opt_errcallback);
};


/**
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.getUsers = function(callback, opt_errcallback) {
  this.request_('getUsers', {}, callback, opt_errcallback);
};


/**
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 */
io.api.ApiConnector.prototype.logout = function(callback, opt_errcallback) {
  this.request_('logout', {}, callback, opt_errcallback);
};


/**
 * @param {!string} resource - resource to be accessed i.e. 'login'.
 * @param {!Object} data - data to be sent.
 * @param {function(string, goog.net.XhrIo=)} callback - on success call.
 * @param {function(string, goog.net.XhrIo=)=} opt_errcallback -
 *   optional on error call.
 * @param {boolean=} opt_notincludesession
 */
io.api.ApiConnector.prototype.request_ = function(resource, data, callback,
    opt_errcallback, opt_notincludesession) {
  var url = this.url + resource;
  var xhr = new goog.net.XhrIo();
  var headers = {'Content-Type': 'application/json'};
  var self = this;

  if (!opt_notincludesession) {
    data['sessionID'] = this.sid;
  }

  var onError = function(e) {
    io.log().warning('Error accessing url: ' + url + '\n' +
        e.target.getResponseText());
    var reason = null;
    if (e.getStatus && e.getStatus() != 0) {
      reason = e.getStatus() + ' ' + e.getStatusText();
    }

    if (opt_errcallback) {
      opt_errcallback(reason, e.target);
    }
  };

  var onException = function(e) {
    io.log().info('Exception accessing url: ' + url + '\n' + e);
    if (self.exHandlers[e]) {
      self.exHandlers[e](e);
    } else if (opt_errcallback) {
      opt_errcallback(e);
    }
  };

  goog.events.listen(xhr, goog.net.EventType.SUCCESS, function(e) {
    try {
      var json = this.getResponseJson();
      //io.log().info('Got response from: ' + url + '\n' +
      //    goog.debug.deepExpose(json));
    } catch (err) {
      io.log().warning('Internal exception: ' + err);
      onError(e);
      return;
    }
    if (json['exception']) {
      onException(json['exception']);
    } else if (callback) {
      callback(json['retval']);
    }
  });

  goog.events.listen(xhr, goog.net.EventType.ERROR, onError);

  xhr.send(url, 'POST', goog.json.serialize(data), headers);
};

