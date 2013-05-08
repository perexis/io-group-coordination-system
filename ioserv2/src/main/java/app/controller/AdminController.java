package app.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.dao.RegisteredLayerDao;
import app.dao.RegisteredUserDao;
import app.dao.RegisteredUserItemDao;
import app.model.db.RegisteredLayer;
import app.model.db.RegisteredUser;
import app.model.db.RegisteredUserItem;
import app.model.form.FormSimpleUser;
import app.model.form.FormString;
import app.model.form.FormUser;
import app.model.form.FormUserItem;
import app.model.local.MapItem;
import app.model.local.User;
import app.model.local.UserItem;

import com.google.common.collect.BiMap;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private RegisteredUserDao registeredUserDao;
	@Autowired
	private RegisteredUserItemDao registeredUserItemDao;
	@Autowired
	private RegisteredLayerDao registeredLayerDao;
	@Autowired
	private MainController mainController;
	
	@RequestMapping(value = "/", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	public String admin(@ModelAttribute("formSimpleUser") FormSimpleUser formSimpleUser, 
			Model model, final RedirectAttributes redirectAttributes) {
		if ((!formSimpleUser.getLogin().equals("admin") && !formSimpleUser.getPassword().equals("admin")) &&
				(!formSimpleUser.getLogin().equals("") && !formSimpleUser.getPassword().equals(""))) {
			redirectAttributes.addFlashAttribute("message", "Wrong login or password");
			return "redirect:/";
		}
		model.addAttribute("formUser", new FormUser());
		model.addAttribute("formString", new FormString());
		model.addAttribute("formUserItem", new FormUserItem());
		return "admin";
	}
	
	@RequestMapping(value = "addUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String addUser(@ModelAttribute("formUser") FormUser formUser) {
		RegisteredUser registeredUser = new RegisteredUser(formUser);
		if (registeredUserDao.save(registeredUser) == null) {
			return "ERROR: User " + registeredUser.getId() + " already exists!";
		}
		mainController.getUsers().add(new User(registeredUser));
		return "User " + registeredUser.getId() + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteUser(@ModelAttribute("formUser") FormUser formUser) {
		User user = new User(formUser);
		if (!mainController.getUsers().contains(user)) {
			return "ERROR: User " + user.getId() + " doesn't exists!";
		}
		registeredUserDao.delete(user.getId());
		mainController.getUsers().remove(user);
		System.out.println(mainController.getSessions().inverse().remove(user));
		return "User " + user.getId() + " successfully deleted!";
	}
	
	@RequestMapping(value = "updateUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String updateUser(@ModelAttribute("formUser") FormUser formUser) {
		RegisteredUser registeredUser = new RegisteredUser(formUser);
		if (registeredUserDao.update(registeredUser) == null) {
			return "ERROR: User " + registeredUser.getId() + " doesn't exists!";
		}
		List<User> users = mainController.getUsers();
		users.get(users.indexOf(new User(registeredUser))).update(registeredUser);
		return "User " + registeredUser.getId() + " successfully updated!";
	}
	
	@RequestMapping(value = "findUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String findUser(@ModelAttribute("formUser") FormUser formUser) {
		User user = new User(formUser);
		List<User> users = mainController.getUsers();
		int index;
		if ((index = users.indexOf(user)) == -1) {
			return "ERROR: User " + user.getId() + " doesn't exists!";
		}
		return users.get(index).toString();
	}
	
	@RequestMapping(value = "listAllUsers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String listAllUsers() {
		List<User> users = mainController.getUsers();
		if (users.isEmpty()) {
			return "There are no Users in the database!";
		}
		StringBuilder res = new StringBuilder();
		for (User u : users) {
			res.append(u).append("<br/>");
		}
		return res.toString();
	}
	
	@RequestMapping(value = "deleteAllUsers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteAllUsers() {
		registeredUserDao.deleteAll();
		mainController.getUsers().clear();
		mainController.getSessions().clear();
		return "All Users have been deleted!";
	}
	
	@RequestMapping(value = "addUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String addUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		RegisteredUserItem registeredUserItem = new RegisteredUserItem(formUserItem);
		if (registeredUserItemDao.save(registeredUserItem) == null) {
			return "ERROR: UserItem " + registeredUserItem.getId() + " already exists!";
		}
		mainController.getUserItems().add(new UserItem(registeredUserItem));
		return "UserItem " + registeredUserItem.getId() + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		UserItem userItem = new UserItem(formUserItem);
		if (!mainController.getUserItems().contains(userItem)) {
			return "ERROR: UserItem " + userItem.getId() + " doesn't exists!";
		}
		registeredUserItemDao.delete(userItem.getId());
		mainController.getUserItems().remove(userItem);
		for (User u : mainController.getSessions().values()) {
			Iterator<UserItem> iter = u.getItems().iterator();
			while(iter.hasNext()) {
				if (iter.next().equals(userItem)) {
					iter.remove();
				}
			}
		}
		return "UserItem " + userItem.getId() + " successfully deleted!";
	}
	
	@RequestMapping(value = "updateUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String updateUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		RegisteredUserItem registeredUserItem = new RegisteredUserItem(formUserItem);
		if (registeredUserItemDao.update(registeredUserItem) == null) {
			return "ERROR: UserItem " + registeredUserItem.getId() + " doesn't exists!";
		}
		List<UserItem> userItems = mainController.getUserItems();
		userItems.get(userItems.indexOf(new UserItem(registeredUserItem))).update(registeredUserItem);
		return "UserItem " + registeredUserItem.getId() + " successfully updated!";
	}
	
	@RequestMapping(value = "findUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String findUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		UserItem userItem = new UserItem(formUserItem);
		List<UserItem> userItems = mainController.getUserItems();
		int index;
		if ((index = userItems.indexOf(userItem)) == -1) {
			return "ERROR: UserItem " + userItem.getId() + " doesn't exists!";
		}
		return userItems.get(index).toString();
	}
	
	@RequestMapping(value = "listAllUserItems", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String listAllUserItems() {
		List<UserItem> userItems = mainController.getUserItems();
		if (userItems.isEmpty()) {
			return "There are no UserItems in the database!";
		}
		StringBuilder res = new StringBuilder();
		for (UserItem u : userItems) {
			res.append(u).append("<br/>");
		}
		return res.toString();
	}
	
	@RequestMapping(value = "deleteAllUserItems", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteAllUserItems() {
		registeredUserItemDao.deleteAll();
		mainController.getUserItems().clear();
		for (User u : mainController.getSessions().values()) {
			u.getItems().clear();
		}
		return "All UserItems have been deleted!";
	}
	
	@RequestMapping(value = "addLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String addLayer(@ModelAttribute("formString") FormString formString) {
		RegisteredLayer registeredLayer = new RegisteredLayer(formString.getValue());
		if (registeredLayerDao.save(registeredLayer) == null) {
			return "ERROR: Layer " + registeredLayer.getId() + " already exists!";
		}
		String layer = registeredLayer.getId();
		mainController.getLayers().put(layer, new ArrayList<MapItem>());
		return "Layer " + layer + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteLayer(@ModelAttribute("formString") FormString formString) {
		String layer = formString.getValue();
		if (!mainController.getLayers().keySet().contains(layer)) {
			return "ERROR: Layer " + layer + " doesn't exists!";
		}
		registeredLayerDao.delete(layer);
		mainController.getLayers().remove(layer);
		return "Layer " + formString.getValue() + " successfully deleted!";
	}
	
	@RequestMapping(value = "findLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String findLayer(@ModelAttribute("formString") FormString formString) {
		String layer = formString.getValue();
		if (!mainController.getLayers().keySet().contains(layer)) {
			return "ERROR: Layer " + layer + " doesn't exists!";
		}
		return layer;
	}
	
	@RequestMapping(value = "listAllLayers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String listAllLayers() {
		Set<String> layers = mainController.getLayers().keySet();
		if (layers.isEmpty()) {
			return "There are no Layers in the database!";
		}
		StringBuilder res = new StringBuilder();
		for (String l : layers) {
			res.append(l).append("<br/>");
		}
		return res.toString();
	}
	
	@RequestMapping(value = "deleteAllLayers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String deleteAllLayers() {
		registeredLayerDao.deleteAll();
		mainController.getLayers().clear();
		return "All Layers have been deleted!";
	}
	
	@RequestMapping(value = "listSessions", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String listSessions() {
		BiMap<Long, User> sessions = mainController.getSessions();
		if (sessions.isEmpty()) {
			return "There are no current sessions!";
		}
		StringBuilder res = new StringBuilder();
		for (Map.Entry<Long, User> entry : sessions.entrySet()) {
			res.append(entry.getKey() + " " + entry.getValue().getId() + "<br/>");
		}
		return res.toString();
	}

}
