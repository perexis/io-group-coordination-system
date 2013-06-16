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

