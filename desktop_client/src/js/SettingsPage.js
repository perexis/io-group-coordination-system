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
goog.provide('io.settings.Page');

goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.soy.settings');



/**
 * @constructor
 * @param {!io.main.Page} main
 * @param {?Element} elem
 */
io.settings.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
};


/**
 * Renders the settings page inside this.elem element.
 */
io.settings.Page.prototype.render = function() {
  this.main.setMenuLinkActive('settingsButton');
  soy.renderElement(this.elem, io.soy.settings.page);
};

