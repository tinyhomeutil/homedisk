/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jack www.tinyhomeutil.com 
 */
public class ActionFacotry {

	private Map<String,Object> beans;
	
	private static ActionFacotry factory;
	static {
		factory = new ActionFacotry();
	}
	private ActionFacotry(){
		beans = new HashMap<String,Object>();
	}
	
	public static ActionFacotry getInstance(){
		return factory;
	}

	public Object getAction(HttpServletRequest request)
	throws Exception {
		return this.getAction(true, request);
	}

	public Object getAction(boolean singleton,HttpServletRequest request)
	throws Exception {
		int model = Integer.valueOf(request.getParameter("m"));
		return this.getAction(singleton, model);
	}
	
	public Object getAction(int model)
	throws Exception {
		return this.getAction(true, model);
	}

	public Object getAction(boolean singleton,int model){
		switch ( model ) {
		case SwitchAction.FORE_INDEX:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_IMAGE_LIST:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_IMAGE_PLAY:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_VIDEO_LIST:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_VIDEO_PLAY:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_AUDIO_LIST:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_AUDIO_PLAY:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_OTHER_LIST:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_QUERY:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_LOGIN:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_GET_IMAGE_FOR_AUDIO_PLAY:
			return this.getForeAction(singleton);
		case SwitchAction.FORE_DOWNLOAD_RESOURCE:
			return this.getForeAction(singleton);	
			
			
		case SwitchAction.BACK_ADMIN_OVERVIEW:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_UPDATE_MANAGE_PWD:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_USER_LIST:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_ADD_USER:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_RESET_USER_PWD:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_DEL_USER:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_GET_USER_INFO:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_DELETED_RESOURCE_LIST:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_CLEAR_ALL_DELETED_RESOURCE:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_RECOVER_RESOURCE:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_DELETED_RESOURCE:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_SET_PRIVATE_VIEW:
			return this.getAdminAction(singleton);
		case SwitchAction.BACK_ADMIN_LOCK_USER:
			return this.getAdminAction(singleton);
			
		case SwitchAction.BACK_USER_OVERVIEW:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_UPDATE_MANAGE_PWD:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_ALBUM_LIST:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_ADD_ALBUM:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_UPDATE_ALBUM:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_DEL_ALBUM:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_VIEW_SET:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_RESOURCE_LIST:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_DEL_RESOURCE:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_UPLOAD_RESOURCE:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_DOWNLOAD_RESOURCE:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_ALBUMS:
			return this.getUserAction(singleton);
		case SwitchAction.BACK_USER_SAVE_RESOURCE:
			return this.getUserAction(singleton);	
		case SwitchAction.GLOBAL_NOTICE:
			return this.getGlobalAction(singleton);
			
		default:;
			return null;
		}
	}
		
	private static final String STR_FORE_ACTION_KEY = "foreaction";	
	private static final String STR_BACK_ADMIN_ACTION_KEY = "adminaction";	
	private static final String STR_BACK_USER_ACTION_KEY = "useraction";	
	private static final String STR_GLOBAL_ACTION_KEY = "globalaction";	

	private IFore getForeAction(boolean singleton){
		Object obj = null;
		if ( singleton ) {
			obj = beans.get(STR_FORE_ACTION_KEY);
			if ( obj == null ) {
				obj = new ForeAction();
				beans.put(STR_FORE_ACTION_KEY, obj);
			}
			return (IFore)obj;
		}
		return new ForeAction();
	}
	
	private IAdmin getAdminAction(boolean singleton){
		Object obj = null;
		if ( singleton ) {
			obj = beans.get(STR_BACK_ADMIN_ACTION_KEY);
			if ( obj == null ) {
				obj = new AdminAction();
				beans.put(STR_BACK_ADMIN_ACTION_KEY, obj);
			}
			return (IAdmin)obj;
		}
		return new AdminAction();
	}
	
	private IUser getUserAction(boolean singleton){
		Object obj = null;
		if ( singleton ) {
			obj = beans.get(STR_BACK_USER_ACTION_KEY);
			if ( obj == null ) {
				obj = new UserAction();
				beans.put(STR_BACK_USER_ACTION_KEY, obj);
			}
			return (IUser)obj;
		}
		return new UserAction();
	}
	
	private IGlobal getGlobalAction(boolean singleton){
		Object obj = null;
		if ( singleton ) {
			obj = beans.get(STR_GLOBAL_ACTION_KEY);
			if ( obj == null ) {
				obj = new GlobalAction();
				beans.put(STR_GLOBAL_ACTION_KEY, obj);
			}
			return (IGlobal)obj;
		}
		return new GlobalAction();
	}
}
