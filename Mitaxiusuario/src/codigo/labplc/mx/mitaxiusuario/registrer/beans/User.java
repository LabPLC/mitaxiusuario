package codigo.labplc.mx.mitaxiusuario.registrer.beans;

public class User {
	private String name;
	private String email;
	private String teluser;
	private String telemergency;
	private String UUID;
	private String mailenergency;
	private String os = "2";
	
	public String getMailenergency() {
		return mailenergency;
	}
	public void setMailenergency(String mailenergency) {
		this.mailenergency = mailenergency;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTeluser() {
		return teluser;
	}
	public void setTeluser(String teluser) {
		this.teluser = teluser;
	}
	public String getTelemergency() {
		return telemergency;
	}
	public void setTelemergency(String telemergency) {
		this.telemergency = telemergency;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
}