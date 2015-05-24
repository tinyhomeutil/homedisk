/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.pojo.Album;
import com.tinyhomeutil.homedisk.pojo.Resource;

/**
 * @author jack www.tinyhomeutil.com
 *
 */
public interface IFore {
	
	public void login(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;
	
	public Map<String,List<Album>> getIndexAlbumList(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public Pages<List<Album>> getVideoAlbumList(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public  Pages<List<Album>> getImageAlbumList(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public  Pages<List<Album>> getAudioAlbumList(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public  List<Album> getOtherAlbumList(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public Pages<List<Resource>> getOtherResourceByAlbumId(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public String getRowHtml(List<Album> list,int columnsInRow,int rowNum,
			String albumName,String defaultImageUrl,String viewUrl) throws Exception;
	
	public List<Resource> getMediaResourceByAlbumId(HttpSession session,HttpServletRequest request,
			HttpServletResponse response,String albumParent) throws Exception;

	public Resource getImageResourceForAudioPlay(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public Resource getResourceObject(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) 
	throws Exception;

	public Pages<List<Resource>> getResourceByKeyWords(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public String getLoginHtml(ResourceBundle messages,HttpSession session,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String getIndexTopMenuHTML(ResourceBundle messages,int activeItemNumber) throws Exception;
	
}
