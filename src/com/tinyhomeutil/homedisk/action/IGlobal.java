/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jack  www.tinyhomeutil.com
 * 
 */
public interface IGlobal {

	public String getNoticeHTML(ResourceBundle messages,
			HttpServletRequest request,String noticeDivId,String noticeWindowId) throws Exception;
	
	
	public String getPagesHTML(ResourceBundle messages,String url,Map<String,String> params,int firstPage,int previousPage,int nextPage,int lastPage,int currentPage) throws Exception;
	
}
