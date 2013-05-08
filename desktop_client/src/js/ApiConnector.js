'use strict';
goog.provide('io.api');
goog.provide('io.api.ApiConnector');

goog.require('goog.debug');
goog.require('goog.events');
goog.require('goog.json');
goog.require('goog.net.XhrIo');
goog.require('io.log');



/**
 * @constructor
 */
io.api.ApiConnector = function() {
  this.url = 'http://io.wojtasskorcz.eu.cloudbees.net/';
  io.log().info('Initialized API pointing to: ' + this.url);
};

io.api.ApiConnector.prototype.login = function(data, callback,
    opt_errcallback) {
  this.request_('login', data, callback, opt_errcallback);
};

io.api.ApiConnector.prototype.request_ = function(resource, data, callback,
    opt_errcallback) {
  var url = this.url + resource;

  var xhr = new goog.net.XhrIo();

  var headers = {'Content-Type': 'application/json'};

  var onError = function(e) {
    io.log().warning('Error accessing url: ' + url + '\n' +
        e.target.getResponseText());
    var reason = null;
    if (e.getStatus() != 0) {
      reason = e.getStatus() + ' ' + e.getStatusText();
    }

    if (opt_errcallback) {
      opt_errcallback(reason, e.target);
    }
  };

  var onException = function(e) {
    io.log().info('Exception accessing url: ' + url + '\n' + e);
    if (opt_errcallback) {
      opt_errcallback(e);
    }
  };

  goog.events.listen(xhr, goog.net.EventType.SUCCESS, function(e) {
    try {
      var json = this.getResponseJson();
      io.log().info('Got response from: ' + url + '\n' +
          goog.debug.expose(json));
    } catch (err) {
      io.log().warning('Internal exception: ' + err);
      onError(e);
      return;
    }
    if (json['exception']) {
      onException(json['exception']);
    } else {
      callback(json['retval']);
    }
  });

  goog.events.listen(xhr, goog.net.EventType.ERROR, onError);

  data = goog.json.serialize(data);
  xhr.send(url, 'POST', data, headers);
};

