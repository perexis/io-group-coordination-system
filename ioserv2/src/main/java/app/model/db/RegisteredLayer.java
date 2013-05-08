package app.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Layers")
public class RegisteredLayer implements Model {
	
	@Id
	@Column
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public RegisteredLayer() {
	}
	
	public RegisteredLayer(String id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RegisteredLayer 
				&& this.id.equals(((RegisteredLayer) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return id;
	}

}
