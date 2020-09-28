package model;

public class AccessMessage {
	
	private String type="Access";
	private String id;
	private String body;
	private String date;
	
	public AccessMessage() {
		
	}
	
	public AccessMessage(String id, String date,String body) {
		this.id = id;
		this.body = body;
		this.date = date;
	
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getDate() {
		return date;
	}
	
	

}
