/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.pojo.Album;
import com.tinyhomeutil.homedisk.pojo.Resource;
import com.tinyhomeutil.homedisk.pojo.UserOverView;

/**
 * @author jack www.tinyhomeutil.com 
 *
 */
public interface IUser {

	public UserOverView overview(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void updatePwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public Pages<List<Album>> getAlbums(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void updateAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void addAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void dellAlbum(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void setAlbumViewStatus(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

	public Pages<List<Resource>> getResources(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

	public void uploadResource(List<FileItem> items,
			HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void downloadResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public void delResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public String getAllAlbums(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

	public void saveResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public Resource getResourceObject(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
}
