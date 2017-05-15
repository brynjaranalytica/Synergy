package shared;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String iD;
	private char[] pass;
	private String name;
	private String phone;
	private UserType type;
	
	public User(String iD, String name, String phone, UserType type) {
		this.iD = iD;
		this.pass = null;
		this.name = name;
		this.phone = phone;
		this.type = type;
	}
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}
	public char[] getPass() {
		return pass;
	}
	public void setPass(char[] pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setUserType(UserType type){
		this.type = type;
	}
	public UserType getUserType(){
		return type;
	}
	public User copyOf(){
		User copy = new User(iD, name, phone, type);
		copy.setPass(getPass());
		return copy;
	}
	
}
