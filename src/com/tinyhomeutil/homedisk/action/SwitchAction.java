/**
 * www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jack www.tinyhomeutil.com
 *
 */
public class SwitchAction {
	
	private static SwitchAction action;
	static {
		action = new SwitchAction();
	}
	
	private SwitchAction(){
		
	}
	
	public static SwitchAction getInstance(){
		return action;
	}
	
	public void switchAction(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		int model = Integer.valueOf(request.getParameter("m"));
		switch ( model ) {
		case SwitchAction.FORE_QUERY:
			this.indexquery(session,request,response);
			return;
						
		case SwitchAction.FORE_LOGIN:
			this.login(session,request,response);
			return;	
			
		case SwitchAction.BACK_ADMIN_UPDATE_MANAGE_PWD:
			this.updateAdminManagePwd(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_ADD_USER:
			this.addUser(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_RESET_USER_PWD:
			this.resetUserPwd(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_DEL_USER:
			this.delUser(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_GET_USER_INFO:
			this.getUserInfo(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_CLEAR_ALL_DELETED_RESOURCE:
			this.clearAllDeletedResource(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_RECOVER_RESOURCE:
			this.recoverResource(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_DELETED_RESOURCE:
			this.adminDelResource(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_LOCK_USER:
			this.lockUser(session,request,response);
			return;	
			
		case SwitchAction.BACK_USER_UPDATE_MANAGE_PWD:
			this.updateUserManagePwd(session,request,response);
			return;
		case SwitchAction.BACK_USER_ALBUMS:
			this.getAllAlbums(session,request,response);
			return;

			
		case SwitchAction.BACK_USER_ADD_ALBUM:
			this.addAlbum(session,request,response);
			return;
		case SwitchAction.BACK_USER_UPDATE_ALBUM:
			this.updateAlbum(session,request,response);
			return;
		case SwitchAction.BACK_USER_DEL_ALBUM:
			this.delAlbum(session,request,response);
			return;
		case SwitchAction.BACK_USER_VIEW_SET:
			this.setAlbumViewStatus(session,request,response);
			return;
		case SwitchAction.BACK_USER_UPLOAD_RESOURCE:
			this.uploadResource(session,request,response);
			return;
		case SwitchAction.BACK_USER_DOWNLOAD_RESOURCE:
			this.downloadResource(session,request,response);
			return;
		case SwitchAction.BACK_USER_SAVE_RESOURCE:
			this.saveResource(session,request,response);
			return;
			
		case SwitchAction.BACK_USER_DEL_RESOURCE:
			this.userDelResource(session,request,response);
			return;
		case SwitchAction.BACK_ADMIN_SET_PRIVATE_VIEW:
			this.adminSetPrivateView(session,request,response);
			return;
		case SwitchAction.FORE_GET_IMAGE_FOR_AUDIO_PLAY:
			this.getImageForAudioPlay(session,request,response);
			return;
						
		default:;
			this.to404(session,request,response);
			return;
		}
		
	}
	
	private void getImageForAudioPlay(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IFore action = (IFore)ActionFacotry.getInstance().getAction(request);
		action.getImageResourceForAudioPlay(session, request, response);
	}
	
	private void login(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IFore action = (IFore)ActionFacotry.getInstance().getAction(request);
		action.login(session, request, response);
	}
	
	private void saveResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		action.saveResource(session, request, response);
	}
	
	private String getAllAlbums(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		return action.getAllAlbums(session, request, response);
	}
	
	private void uploadResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
	}
	
	private void downloadResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
	}
	
	private void indexquery(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		
	}
	
	private void updateAdminManagePwd(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.updatePwd(session, request, response);
	}
	
	private void addUser(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.createUser(session, request, response);
	}
	
	private void resetUserPwd(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.resetPwd(session, request, response);
	}
	
	private void delUser(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.delUser(session, request, response);
	}
	
	private void getUserInfo(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		
	}
	
	private void clearAllDeletedResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.clearResource(session, request, response);
	}
	
	private void recoverResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.recoverResource(session, request, response);
	}
	
	private void adminDelResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.delResource(session, request, response);
	}
	
	private void updateUserManagePwd(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		action.updatePwd(session, request, response);
	}
		
	private void addAlbum(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
	}
	
	private void updateAlbum(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
	}
	
	private void delAlbum(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		action.dellAlbum(session, request, response);
	}
	
	private void setAlbumViewStatus(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		action.setAlbumViewStatus(session, request, response);
	}
	
	private void userDelResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(request);
		action.delResource(session, request, response);
	}
	
	private void adminSetPrivateView(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.setVisitStatus(session, request, response);
	}
	
	private void lockUser(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		IAdmin action = (IAdmin)ActionFacotry.getInstance().getAction(request);
		action.lockUser(session, request, response);
	}
	
	private void to404(HttpSession session,HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		
	}
		
	public static final int INSERT = 1;

	public static final int UPDATE = 2;

	public static final int DELETE = 3;

	public static final int QUERY = 4;

	public static final int UPLOAD = 5;
	
	public static final int DOWNLOAD = 6;
	
	public static final int FORE_INDEX = 1;
	
	public static final int FORE_IMAGE_LIST = 2;
	
	public static final int FORE_IMAGE_PLAY = 3;
	
	public static final int FORE_VIDEO_LIST = 4;
	
	public static final int FORE_VIDEO_PLAY = 5;
	
	public static final int FORE_AUDIO_LIST = 6;
	
	public static final int FORE_AUDIO_PLAY = 7;
	
	public static final int FORE_OTHER_LIST = 8;
	
	public static final int FORE_QUERY = 9;
	
	public static final int FORE_LOGIN = 10;
	
	public static final int BACK_USER_OVERVIEW = 11;
	
	public static final int BACK_USER_UPDATE_MANAGE_PWD = 12;
	
	public static final int BACK_USER_ALBUM_LIST = 14;
	
	public static final int BACK_USER_ADD_ALBUM = 15;
	
	public static final int BACK_USER_UPDATE_ALBUM = 16;
	
	public static final int BACK_USER_DEL_ALBUM = 17;
	
	public static final int BACK_USER_VIEW_SET = 18;
	
	public static final int BACK_USER_RESOURCE_LIST = 19;
	
	public static final int BACK_USER_DEL_RESOURCE = 20;
	
	public static final int BACK_ADMIN_OVERVIEW = 21;
	
	public static final int BACK_ADMIN_UPDATE_MANAGE_PWD = 22;
	
	public static final int BACK_ADMIN_INDEX_PRIVATE_VIEW = 23;
	
	public static final int BACK_ADMIN_USER_LIST = 24;
	
	public static final int BACK_ADMIN_ADD_USER = 25;
	
	public static final int BACK_ADMIN_RESET_USER_PWD = 26;
	
	public static final int BACK_ADMIN_DEL_USER = 27;
	
	public static final int BACK_ADMIN_GET_USER_INFO = 28;
	
	public static final int BACK_ADMIN_DELETED_RESOURCE_LIST = 29;
	
	public static final int BACK_ADMIN_CLEAR_ALL_DELETED_RESOURCE = 30;
	
	public static final int BACK_ADMIN_DELETED_RESOURCE = 31;
	
	public static final int BACK_ADMIN_RECOVER_RESOURCE = 32;
	
	public static final int BACK_ADMIN_SET_PRIVATE_VIEW = 33;
	
	public static final int BACK_ADMIN_LOCK_USER = 34;
	
	public static final int BACK_USER_UPLOAD_RESOURCE = 35;
	
	public static final int BACK_USER_DOWNLOAD_RESOURCE = 36;
	
	public static final int BACK_USER_ALBUMS = 37;
	
	public static final int BACK_USER_SAVE_RESOURCE = 38;
	
	public static final int FORE_GET_IMAGE_FOR_AUDIO_PLAY = 39;
	
	public static final int FORE_DOWNLOAD_RESOURCE = 40;
	
	public static final int GLOBAL_NOTICE = 41;
}
