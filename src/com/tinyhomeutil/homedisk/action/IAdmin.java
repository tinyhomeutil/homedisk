/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.pojo.AdminOverView;
import com.tinyhomeutil.homedisk.pojo.Resource;
import com.tinyhomeutil.homedisk.pojo.User;

/**
 * @author jack www.tinyhomeutil.com
 *
 */
public interface IAdmin {
	
	public AdminOverView overview(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

	public void setVisitStatus(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void updatePwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public Pages<List<User>> getUsers(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void createUser(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void resetPwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void lockUser(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void delUser(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public Pages<List<Resource>> getResources(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void clearResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void recoverResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void delResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

}
