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
