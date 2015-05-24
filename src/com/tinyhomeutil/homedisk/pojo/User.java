/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.pojo;

/**
 * @author jack www.tinyhomeutil.com
 */
public class User {
	
	private int id;
	private String name;
	private String pwd;
	private int locked;
	private int admin;
	private int privateVisit;

	public User(){
		this.pwd = "";
		this.locked = 0;
		this.admin = 0;
		this.privateVisit = 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}

	public int getPrivateVisit() {
		return privateVisit;
	}

	public void setPrivateVisit(int privateVisit) {
		this.privateVisit = privateVisit;
	}
	
}
