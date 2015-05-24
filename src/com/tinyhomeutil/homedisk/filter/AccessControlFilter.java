/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.action.Constant;
import com.tinyhomeutil.homedisk.pojo.User;

/**
 * @author jack www.tinyhomeutil.com
 */
public class AccessControlFilter implements Filter {

	private static List<String> foreLinks;

	private static List<String> backUserLinks;
	
	private static List<String> backAdminLinks;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.initForeLinks();
		this.initBackUserLinks();
		this.initBackAdminLinks();
	}
	
	@Override
	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String link = null;
				
		if ( !this.isJSP(httpRequest) ) {
			chain.doFilter(request, response);
			return;
		}
		
		if ( this.isForeURL(httpRequest) ) {
			chain.doFilter(request, response); 
			if ( httpRequest.getServletPath().endsWith("login.jsp") ){
				HttpSession session = httpRequest.getSession();
				if ( session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT) != null &&
						session.getAttribute(Constant.PARAM_SESSION_USER_ID) != null ) {
					User user = (User)session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT);
					if ( user.getAdmin() == 1 ) {
						link = "adminoverview.jsp";
					} else {
						link = "overview.jsp";
					}
					httpResponse.sendRedirect(link);
				}
			}
			return;
		} else {
			link = "login.jsp";
			HttpSession session = httpRequest.getSession();
			if ( session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT) == null ||
					session.getAttribute(Constant.PARAM_SESSION_USER_ID) == null ) {
				session.setAttribute(Constant.PARAM_SESSION_USER_OBJECT, null);
				session.setAttribute(Constant.PARAM_SESSION_USER_ID, null);
				httpResponse.sendRedirect(link);
				return;
			} else {
				User user = (User)session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT);
				if ( this.isBackAdminURL(httpRequest) ) {
					if ( user.getAdmin() == 1 ) {
						chain.doFilter(request, response); 
						return;
					} else {
						if ( this.isBackUserURL(httpRequest) ) {
							chain.doFilter(request, response); 
							return;
						} else {
							link = "overview.jsp";
							httpResponse.sendRedirect(link);
							return;
						}
					}
				} else if ( this.isBackUserURL(httpRequest) ) {
					if ( user.getAdmin() == 0 ) {
						chain.doFilter(request, response); 
						return;
					} else {
						if ( this.isBackAdminURL(httpRequest) ) {
							chain.doFilter(request, response); 
							return;
						} else {
							link = "adminoverview.jsp";
							httpResponse.sendRedirect(link);
							return;
						}
					}
				} else {
					session.setAttribute(Constant.PARAM_SESSION_USER_OBJECT, null);
					session.setAttribute(Constant.PARAM_SESSION_USER_ID, null);
					link = "index.jsp";
					httpResponse.sendRedirect(link);
					return;
				}
				
			}
		}
		
	}
	
	private void initForeLinks(){
		foreLinks = new ArrayList<String>();
		foreLinks.add("index.jsp");
		foreLinks.add("login.jsp");
		foreLinks.add("act.jsp");
		foreLinks.add("act_resource_down.jsp");
		foreLinks.add("audio.jsp");
		foreLinks.add("audioplay.jsp");
		foreLinks.add("images.jsp");
		foreLinks.add("imageview.jsp");
		foreLinks.add("other.jsp");
		foreLinks.add("videos.jsp");
		foreLinks.add("videoview.jsp");
		foreLinks.add("help.jsp");
		foreLinks.add("act_get_image.jsp");
		foreLinks.add("act_fore_resource_down.jsp");
		foreLinks.add("search.jsp");
	}
	
	private void initBackUserLinks(){
		backUserLinks = new ArrayList<String>();
		backUserLinks.add("act_album_up.jsp");
		backUserLinks.add("act_resource_up.jsp");
		backUserLinks.add("act_resource_down.jsp");
		backUserLinks.add("act.jsp");
		backUserLinks.add("album.jsp");
		backUserLinks.add("overview.jsp");
		backUserLinks.add("pwd.jsp");
		backUserLinks.add("resource.jsp");
		backUserLinks.add("uploadresource.jsp");
		backUserLinks.add("logout.jsp");
		backUserLinks.add("act_get_image.jsp");
		backUserLinks.add("help.jsp");
	}
	
	private void initBackAdminLinks(){
		backAdminLinks = new ArrayList<String>();
		backAdminLinks.add("act.jsp");
		backAdminLinks.add("admindelfile.jsp");
		backAdminLinks.add("adminoverview.jsp");
		backAdminLinks.add("adminpwd.jsp");
		backAdminLinks.add("adminusersmgr.jsp");
		backAdminLinks.add("logout.jsp");
	}
	
	private boolean isForeURL(HttpServletRequest request){
		String path = request.getServletPath();
		for ( String s : foreLinks ) {
			if ( path.endsWith(s) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isBackUserURL(HttpServletRequest request){
		String path = request.getServletPath();
		for ( String s : backUserLinks ) {
			if ( path.endsWith(s) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isBackAdminURL(HttpServletRequest request){
		String path = request.getServletPath();
		for ( String s : backAdminLinks ) {
			if ( path.endsWith(s) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isJSP(HttpServletRequest request){
		return request.getServletPath().endsWith(".jsp");
	}
}
