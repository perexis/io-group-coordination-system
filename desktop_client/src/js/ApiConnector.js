'use strict';
goog.provide('io.api');
goog.provide('io.api.ApiConnector');

goog.require('goog.events');
goog.require('goog.json');
goog.require('goog.net.XhrIo');
goog.require('io.log');



/**
 * @constructor
 */
io.api.ApiConnector = function() {
  this.url = 'http://io.wojtasskorcz.eu.cloudbees.net';
  io.log().info('Api pointing to: ' + this.url);
};

io.api.ApiConnector.prototype.request = function(data, callbacl) {
  goog.net.XhrIo.send(data, function(e) {
    var xhr = e.target;
    var obj = xhr.getResponseJson();
  });
  goog.net.XhrIo.send(data, function(e) {
    var xhr = e.target;
    var obj = xhr.getResponseJson();
  });
};

