package pl.edu.agh.io.coordinator.utils.net;

import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.LayerDiff;
import pl.edu.agh.io.coordinator.resources.MapItem;
import pl.edu.agh.io.coordinator.resources.Message;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import pl.edu.agh.io.coordinator.resources.UserState;

public interface IJSonProxy {
	//session
	public boolean login(String userName, String password);
	public void logout();
	
	//
	public LayerDiff getDiff(Layer layer);
	public Set<Layer> getLayers();
	public void addToLayer(Layer layer, MapItem data);
	public void removeFromLayer(Layer layer, MapItem data);
	public void updateSelfState(UserState userState);
	public void addItemToUser(User user, UserItem item);
	public void removeItemFromUser(User user, UserItem item);
	public Set<User> getUsers();
	
	
	//groups
	public Group getGroups();
	public void createGroup(String groupName);
	public void addToGroup(Group group, String user);
	public void removeFromGroup(Group group, String user);
	
	//chat
	public void sendMessage(String message);
	public Set<Message> getMessages();
	
	
}
