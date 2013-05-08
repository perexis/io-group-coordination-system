package app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.dao.RegisteredLayerDao;
import app.dao.RegisteredUserDao;
import app.dao.RegisteredUserItemDao;
import app.model.db.RegisteredLayer;
import app.model.db.RegisteredUser;
import app.model.db.RegisteredUserItem;
import app.model.form.FormSimpleUser;
import app.model.form.FormString;
import app.model.local.MapItem;
import app.model.local.Point;
import app.model.local.User;
import app.model.local.UserItem;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jayway.restassured.path.json.JsonPath;

@Controller
public class MainController {
	
	private BiMap<Long, User> sessions = HashBiMap.create();
	private Map<String, List<MapItem>> layers = new HashMap<>();
	
	private List<User> users = new ArrayList<User>();
	private List<UserItem> userItems = new ArrayList<UserItem>();
	
	private Random random = new Random();
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired(required=true)
	public MainController(RegisteredUserDao registeredUserDao,
			RegisteredLayerDao registeredLayerDao, RegisteredUserItemDao registeredUserItemDao) {
		List<RegisteredUser> rUsers = registeredUserDao.getAll();
		for (RegisteredUser u : rUsers) {
			users.add(new User(u));
		}
		List<RegisteredUserItem> rUserItems = registeredUserItemDao.getAll();
		for (RegisteredUserItem i : rUserItems) {
			userItems.add(new UserItem(i));
		}
		List<RegisteredLayer> rLayers = registeredLayerDao.getAll();
		for (RegisteredLayer l : rLayers) {
			layers.put(new String(l.getId()), new ArrayList<MapItem>());
		}
	}

	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("formString", new FormString());
		model.addAttribute("formSimpleUser", new FormSimpleUser());
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String login(@RequestBody String json, HttpServletResponse response) throws Exception {
		User jsonUser = mapper.readValue(json, User.class);
		User user = null;
		int index;
		if ((index = users.indexOf(jsonUser)) >= 0) {
			user = users.get(index);
		}
		if (user == null || jsonUser.getPassword() == null ||
				!jsonUser.getPassword().equals(user.getPassword())) {
			return "{\"retval\": null, \"exception\": \"CouldNotLogin\"}";
		}
		Long sessionID = this.initializeSession(user);
		return String.format("{\"retval\": %s, \"exception\": null}", sessionID);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String logout(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		if (!sessions.containsKey(sessionID)) {
			return "{\"exception\": \"InvalidSessionID\"}";
		}
		sessions.remove(sessionID);
		return "{\"exception\": null}";
	}
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String getUsers(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		if (!sessions.containsKey(sessionID)) {
			return "{\"retval\": null, \"exception\": \"InvalidSessionID\"}";
		}
		String res = mapper.writeValueAsString(sessions.values());
		return String.format("{\"retval\": %s, \"exception\": null}", res);
	}
	
	@RequestMapping(value = "/getMapItems", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String getMapItems(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		String layer = JsonPath.with(json).getString("layer");
		if (!sessions.containsKey(sessionID)) {
			return "{\"retval\": null, \"exception\": \"InvalidSessionID\"}";
		}
		if (!layers.containsKey(layer)) {
			return "{\"retval\": null, \"exception\": \"InvalidLayer\"}";
		}
		String res = mapper.writeValueAsString(layers.get(layer));
		return String.format("{\"retval\": %s, \"exception\": null}", res);
	}
	
	@RequestMapping(value = "/getLayers", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String getLayers(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		if (!sessions.containsKey(sessionID)) {
			return "{\"retval\": null, \"exception\": \"InvalidSessionID\"}";
		}
		String res = mapper.writeValueAsString(layers.keySet());
		return String.format("{\"retval\": %s, \"exception\": null}", res);
	}
	
	@RequestMapping(value = "/addItemToLayer", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String addItemToLayer(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		String layer = JsonPath.with(json).getString("layer");
		Point point = JsonPath.with(json).getObject("point", Point.class);
		String data = JsonPath.with(json).getString("data");
		if (!sessions.containsKey(sessionID)) {
			return "{\"retval\": null, \"exception\": \"InvalidSessionID\"}";
		}
		if (!layers.containsKey(layer)) {
			return "{\"retval\": null, \"exception\": \"InvalidLayer\"}";
		}
		MapItem item = new MapItem(point, data, layers);
		layers.get(layer).add(item);
		return String.format("{\"retval\": %s, \"exception\": null}", item.getId());
	}
	
	@RequestMapping(value = "/removeMapItem", method = RequestMethod.POST, 
			consumes="application/json; charset=utf-8", 
			produces="application/json; charset=utf-8")
	@ResponseBody	
	public synchronized String removeMapItem(@RequestBody String json, HttpServletResponse response) throws Exception {
		Long sessionID = JsonPath.with(json).getLong("sessionID");
		Long itemID = JsonPath.with(json).getLong("item");
		if (!sessions.containsKey(sessionID)) {
			return "{\"exception\": \"InvalidSessionID\"}";
		}
		int resIndex = -1;
		for (List<MapItem> l : layers.values()) {
			int index = 0;
			for (MapItem i : l) {
				if (i.getId().equals(itemID)) {
					resIndex = index;
					break;
				}
				++index;
			}
			if (resIndex >= 0) {
				l.remove(resIndex);
				break;
			}
		}
		if (resIndex == -1) {
			return "{\"exception\": \"InvalidMapItem\"}";
		}
		return String.format("{\"exception\": null}");
	}
	
	public BiMap<Long, User> getSessions() {
		return sessions;
	}
	
	public Map<String, List<MapItem>> getLayers() {
		return layers;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public List<UserItem> getUserItems() {
		return userItems;
	}
	
	private Long initializeSession(User user) {
		if (sessions.containsValue(user)) {
			Long currentSession = sessions.inverse().get(user);
			sessions.remove(currentSession);
		}
		Long sessionID = generateUniqueSessionID();
		sessions.put(sessionID, user);
		return sessionID;
	}
	
	private Long generateUniqueSessionID() {
		Long id = (long) random.nextInt(Integer.MAX_VALUE);
		while (sessions.containsKey(id)) {
			id = (long) random.nextInt(Integer.MAX_VALUE);
		}
		return id;
	}

	// @RequestMapping("/create")
	// public String create(HttpServletRequest request, HttpServletResponse
	// response) {
	// userDao.save(new RegisteredUser("student", "student"));
	// return "redirect:/";
	// }
	//
	// @RequestMapping("/clear")
	// public String clear(HttpServletRequest request, HttpServletResponse
	// response) {
	// List<RegisteredUser> users = userDao.getAll();
	// for (RegisteredUser u : users) {
	// userDao.delete(u);
	// }
	// return "redirect:/";
	// }
	//
//	@RequestMapping("/list")
//	@ResponseBody
//	public void list(HttpServletRequest request, HttpServletResponse
//			response) {
//		List<RegisteredUser> users = userDao.getAll();
//		response.setContentType("application/json");
//		JSONArray jsonArray = new JSONArray();
//		for (RegisteredUser u : users) {
//			jsonArray.put(new JSONObject(u));
//		}
//		String json = jsonArray.toString();
//		String json = "{}";
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			out.write(json);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
