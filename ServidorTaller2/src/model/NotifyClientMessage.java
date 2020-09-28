package model;

public class NotifyClientMessage {
	
	private String type = "Notify client";
	private String idNewClient;
	
	public NotifyClientMessage() {
		
	}
	
	public NotifyClientMessage(String idNewClient) {
		this.idNewClient = idNewClient;
	}
	
	public String getIDNewClient() {
		return idNewClient;
	}
	
	public String getType() {
		return type;
	}

}
