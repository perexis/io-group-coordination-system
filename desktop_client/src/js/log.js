'use strict';
goog.provide('io.log');
goog.provide('io.logger.init');

goog.require('goog.debug');
goog.require('goog.debug.Console');
goog.require('goog.debug.FancyWindow');
goog.require('goog.debug.Logger');

io.logger.init = function() {
  goog.debug.Console.autoInstall();
  goog.debug.Console.instance.setCapturing(true);
  io.logger_ = goog.debug.Logger.getLogger('io');
  goog.exportSymbol('io.logger', io.logger);
};

io.log = function() {
  return io.logger_;
};

goog.exportSymbol('io.log', io.log);
goog.exportSymbol('io.logger.init', io.logger.init);

