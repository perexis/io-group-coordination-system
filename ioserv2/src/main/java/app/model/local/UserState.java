package app.model.local;

public class UserState {
	
	private Point position;
	private Double speed;
	
	public UserState() {
		this.position = new Point();
	}
	
	public UserState(Point position, Double speed) {
		this.position = position;
		this.speed = speed;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	
	@Override
	public String toString() {
		return String.format("[position=%s, speed=%s]", position, speed);
	}
	
}
