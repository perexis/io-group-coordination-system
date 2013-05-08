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

