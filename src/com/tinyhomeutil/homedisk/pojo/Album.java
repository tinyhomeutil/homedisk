/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.pojo;

/**
 * @author jack
 */
public class Album {

	private int id;
	private String name;
	private String parent;
	private int userId;
	private String userName;
	private String imageUrl;
	private String imageWebUrl;
	private int privateView = 1;
	
	
	public String getImageWebUrl() {
		return imageWebUrl;
	}
	public void setImageWebUrl(String imageWebUrl) {
		this.imageWebUrl = imageWebUrl;
	}
	public int getPrivateView() {
		return privateView;
	}
	public void setPrivateView(int privateView) {
		this.privateView = privateView;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
		
}
