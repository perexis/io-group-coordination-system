'use strict';
goog.provide('io.chat.Page');

goog.require('goog.array');
goog.require('goog.date.Date');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.object');
goog.require('io.api.ApiConnector');
goog.require('io.log');
goog.require('io.soy.chat');
goog.require('io.soy.main');



/**
 * @constructor
 * @param {!io.main.Page} main
 * @param {?Element} elem
 */
io.chat.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
  this.isFirst = true;
};


/**
 * Renders the map page inside this.elem element.
 */
io.chat.Page.prototype.render = function() {
  var self = this;
  soy.renderElement(self.elem, io.soy.chat.page);
  var chatCallback = function(e) {
    e.preventDefault();
    var ci = goog.dom.getElement('chatInput');
    var msg = ci.value;
    ci.value = '';
    self.main.api.sendMessage({'message': msg}, function() {
      self.addMessage(self.main.login, msg, new goog.date.DateTime());
    });
  };
  goog.events.listen(goog.dom.getElement('chatForm'),
      goog.events.EventType.SUBMIT, chatCallback);

  goog.events.listen(this.main.timer, goog.Timer.TICK, function() {
    self.refreshChat();
  });
};

io.chat.Page.prototype.refreshChat = function() {
  var self = this;
  this.main.api.getMessages(function(msgs) {
    goog.array.forEach(msgs, function(msg) {
      var date = new goog.date.DateTime();
      date.setTime(msg['sentTime']);
      self.addMessage(msg['id'], msg['text'], date);
    });
  });
};


io.chat.Page.prototype.addMessage = function(login, msg, date) {
  var cw = goog.dom.getElement('chatWindow');
  if (this.isFirst) {
    soy.renderElement(cw, io.soy.main.empty);
    this.isFirst = false;
  }
  var elem = soy.renderAsFragment(io.soy.chat.msg,
      {login: login, msg: msg, date: date.toIsoTimeString()});
  goog.dom.appendChild(cw, elem);
  cw.scrollTop = cw.scrollHeight;
};

