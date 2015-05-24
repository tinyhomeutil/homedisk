/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.pojo;

/**
 * @author jack www.tinyhomeutil.com
 */
public class AdminOverView {

	private int userNumber = 0;
	private int resourceNumber = 0;
	private int deletedResourceNumber = 0;
	private boolean privateVisit = true;
	
	public int getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}
	public int getResourceNumber() {
		return resourceNumber;
	}
	public void setResourceNumber(int resourceNumber) {
		this.resourceNumber = resourceNumber;
	}
	public int getDeletedResourceNumber() {
		return deletedResourceNumber;
	}
	public void setDeletedResourceNumber(int deletedResourceNumber) {
		this.deletedResourceNumber = deletedResourceNumber;
	}
	public boolean isPrivateVisit() {
		return privateVisit;
	}
	public void setPrivateVisit(boolean privateVisit) {
		this.privateVisit = privateVisit;
	}	
	
}
