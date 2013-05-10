package app.model.local;

public class Message {
	private Long sentTime;
	private String id;
	private String text;

	public Message() {
	}
	
	public Message(Long sentTime, String id, String text) {
		this.sentTime = sentTime;
		this.id = id;
		this.text = text;
	}

	public Long getSentTime() {
		return sentTime;
	}

	public void setSentTime(Long sentTime) {
		this.sentTime = sentTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("[sentTime=%s, id=%s, text=%s]",
				sentTime, id, text);
	}
}
