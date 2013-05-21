package pl.edu.agh.io.coordinator.utils.layersmenu;

public interface LayersMenuListener {

	void itemChecked(String item);
	void itemUnchecked(String item);
	void userChecked(String user);
	void userUnchecked(String user);
	void groupChecked(String group);
	void groupUnchecked(String group);
	void layerChecked(String layer);
	void layerUnchecked(String layer);
	
}
