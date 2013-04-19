goog.provide('io.app');

goog.require('goog.dom');

io.app = function() {
  var newDiv = goog.dom.createDom('h1', {'style': 'background-color:#EEE'},
      'Hello world!');
  goog.dom.appendChild(document.body, newDiv);
  window.alert("hhhh");
};

// Ensures the symbol will be visible after compiler renaming.
goog.exportSymbol('io.app', io.app);

