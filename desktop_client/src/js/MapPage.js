'use strict';
goog.provide('io.map.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('goog.net.jsloader');
goog.require('goog.object');
goog.require('io.api.ApiConnector');
goog.require('io.chat.Page');
goog.require('io.geo');
goog.require('io.log');
goog.require('io.soy.map');
goog.require('io.soy.settings');



/**
 * @constructor
 * @param {!io.main.Page} main
 * @param {?Element} elem
 */
io.map.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
  this.curClickElem = null;
  this.pendingRefresh = false;
  this.showUsers = true;
  this.userMarkers = {};
  this.markers = {};
  this.activeInfo = null;
  this.layers = {};
  this.numLayers = 0;
  this.layerHandlers = {
    'images': this.handleImageMarker,
    'videos': this.handleVideoMarker,
    'notes': this.handleNoteMarker
  };
  this.possibleItems = [];
  var self = this;
  this.main.api.getPossibleUserItems(function(items) {
    self.possibleItems = items;
  });
};


/**
 * Renders the map page inside this.elem element.
 */
io.map.Page.prototype.render = function() {
  var self = this;
  self.main.setMenuLinkActive('mapButton');
  soy.renderElement(self.elem, io.soy.main.map);
  io.map.loadScript(function() {
    self.initMap();
    self.loadLayers(function() {
      self.refreshLayers();
      goog.events.listen(self.main.timer, goog.Timer.TICK, function() {
        self.refreshLayers();
      });
    });
  });
  var chat = new io.chat.Page(this.main, goog.dom.getElement('chat'));
  chat.render();
};

io.map.Page.prototype.loadLayers = function(callback) {
  var self = this;
  this.main.api.getLayers(function(layers) {
    goog.object.forEach(layers, function(layer) {
      self.layers[layer] = true;
    });
    self.numLayers = layers.length;
    callback();
    soy.renderElement(goog.dom.getElement('mapLayers'), io.soy.map.layersMenu,
        {layers: layers});
    goog.array.forEach(layers, function(layer) {
      var ul = goog.dom.getElement('layer_' + layer);
      var toggle = function() {
        if (ul.className == '') {
          ul.className = 'active';
          self.layers[layer] = true;
        } else {
          ul.className = '';
          self.layers[layer] = false;
        }
        self.refreshMapItems();
      };
      toggle();
      goog.events.listen(ul, goog.events.EventType.CLICK, toggle);
    });
    var ulUsers = goog.dom.getElement('layer_users');
    var toggleUsers = function() {
      if (ulUsers.className == '') {
        ulUsers.className = 'active';
        self.showUsers = true;
      } else {
        ulUsers.className = '';
        self.showUsers = false;
      }
      self.refreshUsers();
    };
    toggleUsers();
    goog.events.listen(ulUsers, goog.events.EventType.CLICK, toggleUsers);
  });
};

io.map.Page.prototype.refreshLayers = function() {
  this.refreshUsers();
  this.refreshMapItems();
};

io.map.Page.prototype.refreshUsers = function() {
  var self = this;
  var newMarkers = {};
  var cleanup = function() {
    goog.object.forEach(self.userMarkers, function(obj, key) {
      obj['info'].close();
      obj['marker'].setVisible(false);
    });
    self.userMarkers = newMarkers;
  };
  if (this.showUsers) {
    this.main.api.getUsers(function(users) {
      var numUsers = users.length;
      var finished = function() {
        numUsers--;
        if (numUsers == 0) {
          cleanup();
        }
      };
      goog.array.forEach(users, function(user) {
        self.main.api.getUserState({'user': user['id']}, function(state) {
          self.putUser(user, state, self.userMarkers, newMarkers);
          finished();
        });
      });
    });
  } else {
    cleanup();
  }
};

io.map.Page.prototype.refreshMapItems = function() {
  if (!this.pendingRefresh) {
    var self = this;
    self.pendingRefresh = true;
    var layersLoaded = 0;
    var finished = function() {
      layersLoaded++;
      if (layersLoaded == self.numLayers) {
        self.pendingRefresh = false;
      }
    };
    goog.object.forEach(self.layers, function(active, layer) {
      self.main.api.getMapItems({'layer': layer}, function(items) {
        var newMarkers = {};
        if (active) {
          goog.array.forEach(items, function(item) {
            self.putMapItem(item, newMarkers, self.markers[layer], layer);
          });
        }
        goog.object.forEach(self.markers[layer], function(obj, key) {
          obj['info'].close();
          obj['marker'].setVisible(false);
        });
        self.markers[layer] = newMarkers;
        finished();
      }, finished);
    });
  }
};

io.map.Page.prototype.handleImageMarker = function(item, marker, info,
    renderInfo) {
  marker.setTitle('Image');
  if (renderInfo) {
    var dom = goog.dom.getElement('markerContent');
    soy.renderElement(dom, io.soy.map.imageContent, {'url': item['data']});
  }
};

io.map.Page.parseYoutubeUrl = function(url) {
  var reg = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
  var match = url.match(reg);
  if (match && match[2].length == 11) {
    return match[2];
  } else {
    return 'oHg5SJYRHA0';
  }
};

io.map.Page.prototype.handleVideoMarker = function(item, marker, info,
    renderInfo) {
  marker.setTitle('Video');
  if (renderInfo) {
    var url = io.map.Page.parseYoutubeUrl(item['data']);
    var dom = goog.dom.getElement('markerContent');
    soy.renderElement(dom, io.soy.map.videoContent, {'url': url});
  }
};

io.map.Page.prototype.handleNoteMarker = function(item, marker, info,
    renderInfo) {
  marker.setTitle('Note');
  if (renderInfo) {
    var dom = goog.dom.getElement('markerContent');
    soy.renderElement(dom, io.soy.map.noteContent, {'text': item['data']});
  }
};

io.map.Page.prototype.putMapItem = function(item, newMarkers, markers, layer) {
  var self = this;
  var pos = item['position'];
  var loc = new google.maps.LatLng(pos['latitude'], pos['longitude']);
  var id = item['id'];
  if (markers != undefined && markers[id]) {
    newMarkers[id] = markers[id];
    newMarkers[id]['marker'].setPosition(loc);
    delete markers[id];
  } else {
    var marker = new google.maps.Marker({
      position: loc,
      map: self.map,
      animation: google.maps.Animation.DROP
    });
    var infowindow = new google.maps.InfoWindow();
    google.maps.event.addListener(marker, 'click', function() {
      self.hideCurrentMarker();
      infowindow.setContent(soy.renderAsFragment(io.soy.map.markerWindow));
      infowindow.open(self.map, marker);
      self.activeInfo = infowindow;
      self.initInfoWindow(item, marker);
      if (self.layerHandlers[layer]) {
        self.layerHandlers[layer](item, marker, infowindow, true);
      }
    });
    google.maps.event.addListener(infowindow, 'closeclick', function(e) {
      self.hideCurrentMarker();
    });

    newMarkers[id] = {'marker': marker, 'info': infowindow};
    if (self.layerHandlers[layer]) {
      self.layerHandlers[layer](item, marker, infowindow, false);
    }
  }
};

io.map.Page.prototype.initInfoWindow = function(item, marker) {
  var self = this;
  var deleteCallback = function(e) {
    e.preventDefault();
    io.log().info('Deleting item with id: ' + item['id']);
    self.main.api.removeMapItem({'item': item['id']}, function() {
      self.hideCurrentMarker();
      marker.setVisible(false);
    });
  };

  goog.events.listen(goog.dom.getElement('deleteMarkerForm'),
      goog.events.EventType.SUBMIT, deleteCallback);
};

io.map.Page.prototype.putUser = function(user, position,
    markers, newMarkers) {
  var self = this;
  var pos = position['position'];
  var id = user['id'];
  var loc = new google.maps.LatLng(pos['latitude'], pos['longitude']);
  if (markers[id]) {
    newMarkers[id] = markers[id];
    newMarkers[id]['marker'].setPosition(loc);
    delete markers[id];
  } else {
    var marker = new google.maps.Marker({
      position: loc,
      map: self.map,
      animation: google.maps.Animation.DROP
    });

    var infowindow = new google.maps.InfoWindow();
    google.maps.event.addListener(marker, 'click', function() {
      self.main.api.getUserItems({'user': id}, function(userItems) {
        io.log().info(goog.debug.deepExpose(userItems));
        var myItems = {};
        goog.array.forEach(userItems, function(itm) {
          myItems[itm] = true;
        });
        var items = goog.array.map(self.possibleItems, function(item) {
          return [item['id'], myItems[item['id']]];
        });
        infowindow.setContent(soy.renderAsFragment(io.soy.map.userWindow, {
          name: user['name'],
          surname: user['surname'],
          email: user['email'],
          phone: user['phone'],
          id: user['id'],
          avatar: user['avatar'],
          items: items
        }));
        infowindow.open(self.map, marker);
        self.activeInfo = infowindow;
        goog.array.forEach(items, function(item) {
          var input = goog.dom.getElement('item_' + item[0]);
          var toggle = function(e) {
            var data = {'item': item[0], 'user': id};
            if (input.checked) { // user checked
              self.main.api.addItemToUser(data);
            } else {
              self.main.api.removeItemFromUser(data);
            }
          };
          goog.events.listen(input, goog.events.EventType.CLICK, toggle);
        });
      });
      self.hideCurrentMarker();
    });
    google.maps.event.addListener(infowindow, 'closeclick', function(e) {
      self.hideCurrentMarker();
    });

    newMarkers[id] = {'marker': marker, 'info': infowindow};
  }
};

io.map.Page.prototype.hideCurrentMarker = function() {
  if (this.curClick != null) {
    this.curClick['info'].close();
    this.curClick['marker'].setVisible(false);
    this.curClick = null;
  }
  if (this.activeInfo) {
    this.activeInfo.close();
    this.activeInfo = null;
  }
};

io.map.Page.prototype.onMapClick = function(e) {
  var self = this;
  this.hideCurrentMarker();

  var marker = new google.maps.Marker({position: e.latLng, map: this.map});
  var layers = [];
  goog.object.forEach(self.layers, function(active, layer) {
    goog.array.insert(layers, layer);
  });
  var infowindow = new google.maps.InfoWindow({
    content: soy.renderAsFragment(io.soy.map.addElement, {layers: layers})});
  infowindow.open(this.map, marker);
  this.curClick = {'info': infowindow, 'marker': marker};
  google.maps.event.addListener(infowindow, 'closeclick', function(e) {
    self.hideCurrentMarker();
  });

  var onItemAdded = function(json) {
    io.log().info('Successfully added item');
    self.hideCurrentMarker();
    self.refreshMapItems();
  };

  var onItemAddError = function(err) {
    io.log().warning('Could not add item: ' + err);
  };

  var clickPoint = new io.api.Point(e.latLng.lng(), e.latLng.lat());
  var addCallback = function(e) {
    e.preventDefault();
    var layer = null;
    var radios = goog.dom.getElementsByClass('addRadio');
    goog.array.forEach(radios, function(radio) {
      if (radio.checked) {
        layer = radio.value;
      }
    });
    var data = goog.dom.getElement('addItemData').value;
    io.log().info('Adding item, layer:' + layer + ' elemData:' + data);
    self.main.api.addItemToLayer({'point': clickPoint, 'data': data,
      'layer': layer}, onItemAdded, onItemAddError);
  };
  goog.events.listen(goog.dom.getElement('addItemForm'),
      goog.events.EventType.SUBMIT, addCallback);

  this.map.panTo(e.latLng);
};

io.map.Page.prototype.initMap = function() {
  google.maps['visualRefresh'] = true;
  var W = 50.0681007;
  var E = 19.9125939;
  var mapDiv = goog.dom.getElement('map');
  var mapOptions = {
    center: new google.maps.LatLng(W, E),
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  this.map = new google.maps.Map(mapDiv, mapOptions);
  var self = this;
  google.maps.event.addListener(this.map, 'click', function(e) {
    self.onMapClick(e);
  });
  io.geo.locate(function(position) {
    var point = new google.maps.LatLng(position.coords.latitude,
        position.coords.longitude);
    self.map.setCenter(point);
    self.map.setZoom(18);
    self.main.api.updateSelfState({'newState': {'position':
          {'latitude': point.lat(), 'longitude': point.lng()}, 'speed': 0}},
    function(x) {
      io.log().info('Updated self state');
    });
  });
};


/** global */
var SCRIPT_LOADED = false;


/**
 * @param {!function()} callback - to be called after script load.
 */
io.map.loadScript = function(callback) {
  if (!SCRIPT_LOADED) {
    io.log().info('Loading maps script');
    SCRIPT_LOADED = true;
    var url = 'https://maps.googleapis.com/maps/api/js?' +
        'key=AIzaSyBEGwOI2vuHOOeMy7WIDoWaRlEu8R3umDw&sensor=false&' +
        'callback=__ioMapsJsonp';
    goog.global['__ioMapsJsonp'] = callback;
    goog.net.jsloader.load(url);
  } else {
    callback();
  }
};

