package pl.edu.agh.io.coordinator.utils.layersmenu;

import java.util.Map;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;

public class LayersMenuState {

	public Set<UserItem> items;
	public Set<User> people;
	public Set<Group> groups;
	public Map<String, Boolean> itemsChecks;
	public Map<String, Boolean> peopleChecks;
	public Map<String, Boolean> groupsChecks;
	
	public LayersMenuState() {
		this.items = null;
		this.people = null;
		this.groups = null;
		this.itemsChecks = null;
		this.peopleChecks = null;
		this.groupsChecks = null;
	}
	
}
