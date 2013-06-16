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

package pl.edu.agh.io.coordinator.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserFilter {

	private Set<String> users;
	private Set<String> userItems;
	private Set<String> groups;

	public UserFilter() {
		this.users = Collections.synchronizedSet(new HashSet<String>());
		this.userItems = Collections.synchronizedSet(new HashSet<String>());
		this.groups = Collections.synchronizedSet(new HashSet<String>());
	}

	public void clear() {
		this.users.clear();
		this.userItems.clear();
		this.groups.clear();
	}

	public void clearUsers() {
		this.users.clear();
	}

	public void clearUserItems() {
		this.userItems.clear();
	}

	public void clearGroups() {
		this.groups.clear();
	}

	public void addUser(String user) {
		this.users.add(user);
	}

	public void addUserItem(String userItem) {
		this.userItems.add(userItem);
	}

	public void addGroup(String group) {
		this.groups.add(group);
	}

	public void removeUser(String user) {
		this.users.remove(user);
	}

	public void removeUserItem(String userItem) {
		this.userItems.remove(userItem);
	}

	public void removeGroup(String group) {
		this.groups.remove(group);
	}

	public boolean isEligible(String user, Set<String> userItems, Set<String> groups) {
		if (!this.users.isEmpty()) {
			if (!this.users.contains(user)) {
				return false;
			}
		}
		if (!this.userItems.isEmpty()) {
			boolean hasUserItem = false;
			for (String s : userItems) {
				if (this.userItems.contains(s)) {
					hasUserItem = true;
					break;
				}
			}
			if (!hasUserItem) {
				return false;
			}
		}
		if (!this.groups.isEmpty()) {
			boolean hasGroup = false;
			for (String s : groups) {
				if (this.groups.contains(s)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup) {
				return false;
			}
		}
		return true;
	}

}

