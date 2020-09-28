package model;

public class ExitMessage {
	
	private String type = "Exit";
	private String reID ;
	
	public ExitMessage() {
		
	}
	
	public ExitMessage(String reID) {
		this.reID = reID;
	}

	public String getType() {
		return type;
	}

	public String getReID() {
		return reID;
	}
	

}
