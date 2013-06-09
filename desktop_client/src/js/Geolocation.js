goog.provide('io.geo');

goog.require('io.log');


/**
 * @param {!function(Object)} callback - on success callback.
 * @param {function(Object)=} opt_error - on error callback.
 */
io.geo.locate = function(callback, opt_error) {
  var opts =/** @type {GeolocationPositionOptions} */ ({
    timeout: 5000,
    enableHighAccuracy: true
  });
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        callback, opt_error, opts);
  } else {
    io.log().info('No geolocation available');
  }
};

