/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinyhomeutil.homedisk.action.Constant;
import com.tinyhomeutil.homedisk.pojo.Resource;

/**
 * @author jack www.tinyhomeutil.com
 */
public class Toolkit {

	public final static String md5(String message){ 
		MessageDigest md; 
	try { 
		md = MessageDigest.getInstance("md5"); 
	} catch (java.security.NoSuchAlgorithmException e) { 
		throw new RuntimeException(e.getMessage()); 
	} 
		md.update(message.getBytes()); 
		return new BigInteger(md.digest()).toString(); 
	} 
	
	public static final String sha(String message){ 
		MessageDigest md; 
	try { 
		md = MessageDigest.getInstance("SHA"); 
	} catch (java.security.NoSuchAlgorithmException e) { 
		throw new RuntimeException(e.getMessage()); 
	} 
		md.update(message.getBytes()); 
		return new BigInteger(md.digest()).toString(); 
	} 
	
	public static String htmlTransfer(String str){
		if ( str != null ) {
			String result =	str.replaceAll("&", "&amp;");
			result = str.replaceAll("<", "&lt;");
			result = str.replaceAll(">", "&gt;");
			result = str.replaceAll("\"", "&quot;");
			result = str.replaceAll(" ", "&nbsp;");
			result = str.replaceAll("©", "&copy;");
			result = str.replaceAll("®", "&reg;");
			result = str.replaceAll("'", "&apos;");
			return result;
		}
		return "";
	}
	
	public static String successLink(String link,String notice) {
		StringBuilder resultLink = new StringBuilder();
		resultLink.append(link)
		  .append("?")
	      .append(Constant.PARAM_RETURN).append("=").append(Constant.PARAM_RETURN_SUCCESS)
	      .append("&")
	      .append(Constant.PARAM_RETURN_SUCCESS).append("=").append(notice);
		return resultLink.toString();
	}
	
	public static String failLink(String link,String notice) {
		StringBuilder resultLink = new StringBuilder();
		resultLink.append(link)
		  .append("?")
	      .append(Constant.PARAM_RETURN).append("=").append(Constant.PARAM_RETURN_FAIL)
	      .append("&")
	      .append(Constant.PARAM_RETURN_FAIL).append("=").append(notice);
		return resultLink.toString();
	}
	
	public static String checkNull(String input,String link,String notice) {
		if ( input == null || "".equals(input) ) {
			return failLink(link, notice);
		}
		return null;
	}
	
	public static boolean isNull(String input,String link,String notice,
			HttpServletResponse response) 
			throws Exception {
		String result = checkNull(input, link, notice);
		if ( result != null ) {
			response.sendRedirect(result);
			return true;
		}
		return false;
	}
	
	public static boolean isImageFile(String fileName){
		if ( fileName != null && !"".equals(fileName) ) {
			if ( fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") 
					|| fileName.endsWith(".bmp") || fileName.endsWith(".gif") 
					|| fileName.endsWith(".png") ){
				return true;
			}
		}
		return false;
	}
	
	public static String getFileType(String fileName){
		if ( fileName != null && !"".equals(fileName) ) {
			int point = fileName.lastIndexOf(".");
			if ( point > -1 ) {
				return fileName.substring(point+1, fileName.length());
			}
		}
		return null;
	}
		
	@SuppressWarnings("deprecation")
	public static String getAppRootPath(HttpServletRequest request) {
		String path = new File(request.getRealPath(request.getRequestURI())).getParent();
		return new File(path).getParent();
		
	}
	
	public static String getSimpleFileName(String fileType){
		long time = System.currentTimeMillis();
		StringBuilder simpleFileName = new StringBuilder();
		simpleFileName.append(time).append(".").append(fileType);
		return simpleFileName.toString();
	}
	
	public static String getRelativeAlbumImageFolderPath(String userName,String parent) {
		StringBuilder relativePath = new StringBuilder();
		relativePath.append("/upload")
                    .append("/").append(userName)
                    .append("/").append(parent)
                    .append("/album/");
		return relativePath.toString();
	}

	public static String getRelativeAlbumImagePath(String webRootPath,String userName,String parent,String simpleFileName) {
		StringBuilder path = new StringBuilder();
		path.append(webRootPath)
		    .append(getRelativeAlbumImageFolderPath(userName,parent))
		    .append("cut_")
		    .append(simpleFileName);
		return path.toString();
	}

	public static String getWebRootPath(HttpServletRequest request){
		return request.getRequestURL().toString().replace(request.getServletPath(), "");
	}

	public static String getAbsoluteAlbumImagePath(String appPath,String userName,String parent,String simpleFileName) {
		StringBuilder path = new StringBuilder();
		path.append(appPath)
		    .append(getRelativeAlbumImageFolderPath(userName,parent));
		File folder = new File(path.toString());
		folder.mkdirs();
		path.append(simpleFileName);
		return path.toString();
	}

	public static int getCurrentPage(HttpServletRequest request) {
		String pageStr = request.getParameter(Constant.PARAM_PAGE);
		int currentPage;
		if ( pageStr == null || "".equals(pageStr) ) {
			currentPage = 1;
		} else {
			currentPage = Integer.valueOf(pageStr);
		}
		return currentPage;
	}

	public static String getAbsoluteResourcePath(String appPath,String userName,String simpleFileName) {
		StringBuilder path = new StringBuilder();
		path.append(appPath)
		    .append(getRelativeResourceFolderPath(userName));
		File folder = new File(path.toString());
		folder.mkdirs();
		path.append(simpleFileName);
		return path.toString();
	}

	public static String getRelativeResourcePath(String webRootPath,String userName,String simpleFileName) {
		StringBuilder path = new StringBuilder();
		path.append(webRootPath)
		    .append(getRelativeResourceFolderPath(userName))
		    .append(simpleFileName);
		return path.toString();
	}

	public static String getRelativeResourceFolderPath(String userName) {
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DAY_OF_MONTH);
		
		StringBuilder relativePath = new StringBuilder();
		relativePath.append("/upload")
                    .append("/").append(userName)
                    .append("/").append(year)
                    .append("/").append(month)
                    .append("/").append(day)
                    .append("/");
		return relativePath.toString();
	}
	
	public static boolean isEqual(double x,double y) {
		if ( Math.abs(x-y) < 0.01d ) {
			return true;
		}
		return false;
	}
	
	public static String resourceList2Json(List<Resource> list){
		return Toolkit.resourceList2Json(list,0);
	}
	
	public static String resourceList2Json(List<Resource> list,int start){
		if ( list == null || "".equals(list) ) {
			return "[]";
		}
		if ( start >= list.size() ) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Resource r = null;
		for ( int i = start ; i < list.size() ; i++ ) {
			r = list.get(i);
			sb.append("{")
			  .append("\"").append("id").append("\" : \"").append(r.getId()).append("\"").append(",")
			  .append("\"").append("name").append("\" : \"").append(r.getName()).append("\"").append(",")
			  .append("\"").append("remark").append("\" : \"").append(r.getRemark()).append("\"").append(",")
			  .append("\"").append("albumid").append("\" : \"").append(r.getAlbumId()).append("\"").append(",")
			  .append("\"").append("albumname").append("\" : \"").append(r.getAlbumName()).append("\"").append(",")
			  .append("\"").append("albumparent").append("\" : \"").append(r.getAlbumParent()).append("\"").append(",")
			  .append("\"").append("weburl").append("\" : \"").append(r.getWebUrl()).append("\"").append(",")
			  .append("\"").append("createtime").append("\" : \"").append(r.getCreateTime()).append("\"")			  
			  .append("}");
			if ( i < list.size() -1 ) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String imageResourceList2Json(List<Resource> list,int start){
		if ( list == null || "".equals(list) ) {
			return "[]";
		}
		if ( start >= list.size() ) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Resource r = null;
		for ( int i = start ; i < list.size() ; i++ ) {
			r = list.get(i);
			sb.append("{")
			  .append("\"").append("id").append("\" : \"").append(r.getId()).append("\"").append(",")
			  .append("\"").append("name").append("\" : \"").append(r.getName()).append("\"").append(",")
			  .append("\"").append("remark").append("\" : \"").append(r.getRemark()).append("\"").append(",")
			  .append("\"").append("albumid").append("\" : \"").append(r.getAlbumId()).append("\"").append(",")
			  .append("\"").append("albumname").append("\" : \"").append(r.getAlbumName()).append("\"").append(",")
			  .append("\"").append("albumparent").append("\" : \"").append(r.getAlbumParent()).append("\"").append(",")
			  .append("\"").append("weburl").append("\" : \"").append(r.getWebUrl()).append("\"").append(",")
			  .append("\"").append("smallweburl").append("\" : \"").append(Toolkit.getSmallImageName(r.getWebUrl())).append("\"").append(",")
			  .append("\"").append("createtime").append("\" : \"").append(r.getCreateTime()).append("\"")			  
			  .append("}");
			if ( i < list.size() -1 ) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String resource2Json(Resource r){
		if ( r == null )
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append("{")
		  .append("\"").append("id").append("\" : \"").append(r.getId()).append("\"").append(",")
		  .append("\"").append("name").append("\" : \"").append(r.getName()).append("\"").append(",")
		  .append("\"").append("remark").append("\" : \"").append(r.getRemark()).append("\"").append(",")
		  .append("\"").append("albumid").append("\" : \"").append(r.getAlbumId()).append("\"").append(",")
		  .append("\"").append("albumname").append("\" : \"").append(r.getAlbumName()).append("\"").append(",")
		  .append("\"").append("albumparent").append("\" : \"").append(r.getAlbumParent()).append("\"").append(",")
		  .append("\"").append("weburl").append("\" : \"").append(r.getWebUrl()).append("\"").append(",")
		  .append("\"").append("createtime").append("\" : \"").append(r.getCreateTime()).append("\"")			  
		  .append("}");
		return sb.toString();
	}
	
	public static String nullFilter(String input,String notice){
		if ( input == null || "".equals(input) ) {
			return notice;
		} else {
			return input;
		}
	}
	
	public static Set<String> imageType(){
		Set<String> set = new HashSet<String>();
		set.add("bmp");
		set.add("jpg");
		set.add("wbmp");
		set.add("jpeg");
		set.add("png");
		set.add("gif");
		return set;
	}
	
	public static String getSmallImageName(String fileName){
		if ( fileName != null && !"".equals(fileName) ) {
			int point = fileName.lastIndexOf(".");
			if ( point > -1 ) {
				return fileName.substring(0,point) + "_m" + fileName.substring(point, fileName.length());
			}
		}
		return null;
	}
	
	public static String getDateString(long time){
		String model = "yyyy-MM-dd"; 
		SimpleDateFormat formater = new SimpleDateFormat(model);
		return formater.format(new Date(time));
	}
	
	public static String removeWildcard(String keyword){
		if ( keyword != null && !"".equals(keyword) ) {
			keyword = keyword.replaceAll("%", "");
			keyword = keyword.replaceAll("_", "");
			keyword = keyword.replaceAll("!", "");
			keyword = keyword.replaceAll("^", "");
		}
		return keyword;
	}
	
	public static String encode8859toUtf8(String input) throws UnsupportedEncodingException {
		if ( input == null )
			return null;
		return new String(input.getBytes("ISO-8859-1"),"utf-8");
	}
	
	public static void main(String[] args) {
		
	}
	
	
}
