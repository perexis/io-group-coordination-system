package app.controller;

import java.util.Collection;
import java.util.HashMap;
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
		if (!formSimpleUser.getLogin().equals("admin") || !formSimpleUser.getPassword().equals("deprofundis")) {
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
	public synchronized String addUser(@ModelAttribute("formUser") FormUser formUser) {
		RegisteredUser registeredUser = new RegisteredUser(formUser);
		if (registeredUserDao.save(registeredUser) == null) {
			return "ERROR: User " + registeredUser.getId() + " already exists!";
		}
		User u = new User(registeredUser);
		mainController.getUsers().put(u.getId(), u);
		return "User " + u.getId() + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String deleteUser(@ModelAttribute("formUser") FormUser formUser) {
		User u = new User(formUser);
		if (!mainController.getUsers().containsKey(u.getId())) {
			return "ERROR: User " + u.getId() + " doesn't exists!";
		}
		registeredUserDao.delete(u.getId());
		mainController.getUsers().remove(u.getId());
		mainController.getSessions().inverse().remove(u);
		return "User " + u.getId() + " successfully deleted!";
	}
	
	@RequestMapping(value = "updateUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String updateUser(@ModelAttribute("formUser") FormUser formUser) {
		RegisteredUser registeredUser = new RegisteredUser(formUser);
		if (registeredUserDao.update(registeredUser) == null) {
			return "ERROR: User " + registeredUser.getId() + " doesn't exists!";
		}
		Map<String, User> users = mainController.getUsers();
		users.get(registeredUser.getId()).update(registeredUser);
		return "User " + registeredUser.getId() + " successfully updated!";
	}
	
	@RequestMapping(value = "findUser", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String findUser(@ModelAttribute("formUser") FormUser formUser) {
		User u = new User(formUser);
		Map<String, User> users = mainController.getUsers();
		if (!users.containsKey(u.getId())) {
			return "ERROR: User " + u.getId() + " doesn't exists!";
		}
		return users.get(u.getId()).toString();
	}
	
	@RequestMapping(value = "listAllUsers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String listAllUsers() {
		Collection<User> users = mainController.getUsers().values();
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
	public synchronized String deleteAllUsers() {
		registeredUserDao.deleteAll();
		mainController.getUsers().clear();
		mainController.getSessions().clear();
		return "All Users have been deleted!";
	}
	
	@RequestMapping(value = "addUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String addUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		RegisteredUserItem registeredUserItem = new RegisteredUserItem(formUserItem);
		if (registeredUserItemDao.save(registeredUserItem) == null) {
			return "ERROR: UserItem " + registeredUserItem.getId() + " already exists!";
		}
		UserItem i = new UserItem(registeredUserItem);
		mainController.getUserItems().put(i.getId(), i);
		return "UserItem " + i.getId() + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String deleteUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		UserItem i = new UserItem(formUserItem);
		if (!mainController.getUserItems().containsKey(i.getId())) {
			return "ERROR: UserItem " + i.getId() + " doesn't exists!";
		}
		registeredUserItemDao.delete(i.getId());
		mainController.getUserItems().remove(i.getId());
		Map<String, User> users = mainController.getUsers();
		for (String userId : mainController.getSessions().values()) {
			users.get(userId).getItems().remove(i.getId());
		}
		return "UserItem " + i.getId() + " successfully deleted!";
	}
	
	@RequestMapping(value = "updateUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String updateUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		RegisteredUserItem registeredUserItem = new RegisteredUserItem(formUserItem);
		if (registeredUserItemDao.update(registeredUserItem) == null) {
			return "ERROR: UserItem " + registeredUserItem.getId() + " doesn't exists!";
		}
		Map<String, UserItem> userItems = mainController.getUserItems();
		userItems.get(registeredUserItem.getId()).update(registeredUserItem);
		return "UserItem " + registeredUserItem.getId() + " successfully updated!";
	}
	
	@RequestMapping(value = "findUserItem", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String findUserItem(@ModelAttribute("formUserItem") FormUserItem formUserItem) {
		UserItem i = new UserItem(formUserItem);
		Map<String, UserItem> items = mainController.getUserItems();
		if (!items.containsKey(i.getId())) {
			return "ERROR: UserItem " + i.getId() + " doesn't exists!";
		}
		return items.get(i.getId()).toString();
	}
	
	@RequestMapping(value = "listAllUserItems", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String listAllUserItems() {
		Collection<UserItem> userItems = mainController.getUserItems().values();
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
	public synchronized String deleteAllUserItems() {
		registeredUserItemDao.deleteAll();
		mainController.getUserItems().clear();
		Map<String, User> users = mainController.getUsers();
		for (String userId : mainController.getSessions().values()) {
			users.get(userId).getItems().clear();
		}
		return "All UserItems have been deleted!";
	}
	
	@RequestMapping(value = "addLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String addLayer(@ModelAttribute("formString") FormString formString) {
		RegisteredLayer registeredLayer = new RegisteredLayer(formString.getValue());
		if (registeredLayerDao.save(registeredLayer) == null) {
			return "ERROR: Layer " + registeredLayer.getId() + " already exists!";
		}
		String layer = registeredLayer.getId();
		mainController.getLayers().put(layer, new HashMap<Long, MapItem>());
		return "Layer " + layer + " successfully registered!";
	}
	
	@RequestMapping(value = "deleteLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String deleteLayer(@ModelAttribute("formString") FormString formString) {
		String layer = formString.getValue();
		if (!mainController.getLayers().containsKey(layer)) {
			return "ERROR: Layer " + layer + " doesn't exists!";
		}
		registeredLayerDao.delete(layer);
		mainController.getLayers().remove(layer);
		return "Layer " + formString.getValue() + " successfully deleted!";
	}
	
	@RequestMapping(value = "findLayer", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String findLayer(@ModelAttribute("formString") FormString formString) {
		String layer = formString.getValue();
		if (!mainController.getLayers().containsKey(layer)) {
			return "ERROR: Layer " + layer + " doesn't exists!";
		}
		return layer;
	}
	
	@RequestMapping(value = "listAllLayers", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String listAllLayers() {
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
	public synchronized String deleteAllLayers() {
		registeredLayerDao.deleteAll();
		mainController.getLayers().clear();
		return "All Layers have been deleted!";
	}
	
	@RequestMapping(value = "listSessions", method = RequestMethod.POST,
			produces = "text/plain; charset=utf-8")
	@ResponseBody
	public synchronized String listSessions() {
		BiMap<Long, String> sessions = mainController.getSessions();
		if (sessions.isEmpty()) {
			return "There are no current sessions!";
		}
		StringBuilder res = new StringBuilder();
		for (Map.Entry<Long, String> entry : sessions.entrySet()) {
			res.append(entry.getKey() + " " + entry.getValue() + "<br/>");
		}
		return res.toString();
	}

}
