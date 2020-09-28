package model;

public class IdentifyYou {
	
	private String type = "identify";
	private String forScreen;
	private String idYou;
	
	public IdentifyYou() {
		
	}
	
	public IdentifyYou(String idYou,String forScreen) {
		this.idYou = idYou;
		this.forScreen = forScreen;
	}

	public String getType() {
		return type;
	}

	public String getForScreen() {
		return forScreen;
	}

	public String getIdYou() {
		return idYou;
	}
	
	
	
	

}
