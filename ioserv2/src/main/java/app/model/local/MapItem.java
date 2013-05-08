package app.model.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MapItem {
	
	@JsonIgnore
	private Random random = new Random();
	
	private Long id;
	private Point position;
	private String data;
	
	public MapItem(Point position, String data, Map<String, List<MapItem>> layers) {
		this.position = position;
		this.data = data;
		List<Long> currentItemIds = new ArrayList<>();
		for (List<MapItem> l : layers.values()) {
			for (MapItem i : l) {
				currentItemIds.add(i.getId());
			}
		}
		this.id = initializeId(currentItemIds);
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	private Long initializeId(List<Long> l) {
		Long id = (long) random.nextInt(Integer.MAX_VALUE);
		while (l.contains(id)) {
			id = (long) random.nextInt(Integer.MAX_VALUE);
		}
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MapItem 
				&& this.id.equals(((MapItem) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("[id=%s, position=%s, data=%s]",
				id, position, data);
	}

}
