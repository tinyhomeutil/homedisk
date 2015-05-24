/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.tinyhomeutil.homedisk.i18n.LocaleToolkit;

/**
 * @author jack www.tinyhomeutil.com
 *
 */
public class GlobalAction implements IGlobal{

	@Override
	public String getNoticeHTML(ResourceBundle messages,
			HttpServletRequest request, String noticeDivId,
			String noticeWindowId) throws Exception {
		StringBuilder sb = new StringBuilder();
		String result = (String)request.getParameter(Constant.PARAM_RETURN);
		String notice = "";
		if ( result != null ) {
			if ( Constant.PARAM_RETURN_SUCCESS.equals(result) ) {
				notice = LocaleToolkit.getString(messages, request, Constant.PARAM_RETURN_SUCCESS);
			} else {
				notice = LocaleToolkit.getString(messages, request, Constant.PARAM_RETURN_FAIL);
			}
			sb.append("<script>").append("\r\n")
			  .append("$('").append(noticeDivId).append("').html('").append(notice).append("');").append("\r\n")
			  .append("$('").append(noticeWindowId).append("').modal('show');").append("\r\n")
			  .append("</script>");
		}
		return sb.toString();
	}

	private String getPagesUrl(String url,Map<String, String> params, int pageNumber) 
					throws Exception{
		if ( url == null || "".equals(url) || pageNumber < 1 )
			return "''";
		
		StringBuilder sb = new StringBuilder();
		sb.append("'").append(url).append("?");
		if ( params != null ) {
			Set<Entry<String,String>> entries = params.entrySet();
			for ( Entry<String,String> en : entries ) {
				sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
			}
		}
		sb.append(Constant.PARAM_PAGE).append("=").append(pageNumber).append("'");
		
		return sb.toString();
	}
	
	@Override
	public String getPagesHTML(ResourceBundle messages, String url,
			Map<String, String> params, int firstPage,int previousPage,int nextPage,int lastPage,int currentPage)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"btn-group\" role=\"group\" aria-label=\"...\">");
		if ( currentPage > firstPage ) {
			sb.append("<button type=\"button\" class=\"btn btn-default\" onclick=\"turn(")
			  .append(this.getPagesUrl(url, params, firstPage))
			  .append(")\">")
			  .append(messages.getString("first_page"))
			  .append("<span class=\"badge\">")
			  .append(firstPage)
			  .append("</span></button>");
			sb.append("<button type=\"button\" class=\"btn btn-default\" onclick=\"turn(")
			  .append(this.getPagesUrl(url, params, previousPage))
			  .append(")\">")
			  .append(messages.getString("previous_page"))
			  .append("<span class=\"badge\">")
			  .append(previousPage)
			  .append("</span></button>");
		}
		if ( currentPage < lastPage ) {
			sb.append("<button type=\"button\" class=\"btn btn-default\" onclick=\"turn(")
			  .append(this.getPagesUrl(url, params, nextPage))
			  .append(")\">")
			  .append(messages.getString("next_page"))
			  .append("<span class=\"badge\">")
			  .append(nextPage)
			  .append("</span></button>");
			sb.append("<button type=\"button\" class=\"btn btn-default\" onclick=\"turn(")
			  .append(this.getPagesUrl(url, params, lastPage))
			  .append(")\">")
			  .append(messages.getString("last_page"))
			  .append("<span class=\"badge\">")
			  .append(lastPage)
			  .append("</span></button>");
		}
		sb.append("</div>");
		
		return sb.toString();
	}

}
