package model;

public class NewConnectionMessage {
	private String type = "NC";
	private String idN;
	
	public NewConnectionMessage(String idN) {
		this.idN = idN;
	}
	
	public NewConnectionMessage() {
		
	}

	public String getType() {
		return type;
	}

	public String getIdN() {
		return idN;
	}
	
	

}
