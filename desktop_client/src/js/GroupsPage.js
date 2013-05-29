'use strict';
goog.provide('io.groups.Page');

goog.require('goog.array');
goog.require('goog.debug');
goog.require('goog.dom');
goog.require('goog.events');
goog.require('io.log');
goog.require('io.soy.groups');



/**
 * @constructor
 * @param {!io.main.Page} main
 * @param {?Element} elem
 */
io.groups.Page = function(main, elem) {
  this.main = main;
  this.elem = elem;
};


/**
 * Renders the settings page inside this.elem element.
 */
io.groups.Page.prototype.render = function() {
  this.main.setMenuLinkActive('groupsButton');
  var self = this;
  self.main.api.getGroups(function(groups) {
    self.main.api.getUsers(function(users) {
      goog.array.forEach(groups, function(group) {
        self.main.api.getGroupUsers({'group': group['id']}, function(gus) {
          var ul = goog.dom.getElement('groups_' + group['id']);
          var gUsers = {};
          goog.array.forEach(gus, function(gu) {
            gUsers[gu] = true;
          });
          soy.renderElement(ul, io.soy.groups.users,
              {users: users, group: group, gUsers: gUsers});
          goog.array.forEach(users, function(user) {
            var check = goog.dom.getElement('groups_' + user['id'] + '_' +
                group['id']);
            var toggle = function(e) {
              var data = {'group': group['id'], 'user': user['id']};
              if (check.checked) {
                self.main.api.addToGroup(data);
              } else {
                self.main.api.removeFromGroup(data);
              }
            };
            goog.events.listen(check, goog.events.EventType.CLICK,
                toggle);

            var del = goog.dom.getElement('deleteGroup_' + group['id']);
            goog.events.listen(del, goog.events.EventType.SUBMIT,
                function(e) {
                  e.preventDefault();
                  self.deleteGroup(group);
                });
          });
        });
      });
    });
    soy.renderElement(self.elem, io.soy.groups.page, {groups: groups});
    goog.events.listen(goog.dom.getElement('createGroup'),
        goog.events.EventType.SUBMIT, function(e) {
          e.preventDefault();
          self.createGroup();
        });
  });
};


io.groups.Page.prototype.deleteGroup = function(group) {
  var self = this;
  this.main.api.removeGroup({'group': group['id']}, function() {
    self.render();
  });
};

io.groups.Page.prototype.createGroup = function() {
  var id = goog.dom.getElement('groupName').value;
  var desc = goog.dom.getElement('createGroupData').value;
  var data = {'group': {'id': id, 'description': desc}};
  var self = this;
  this.main.api.createGroup(data, function() {
    self.render();
  });
};

