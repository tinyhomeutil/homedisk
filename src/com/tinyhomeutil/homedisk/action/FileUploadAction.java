/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * @author jack www.tinyhomeutil.com
 *
 */
public class FileUploadAction {

	private static FileUploadAction upload = null;
	static {
		upload = new FileUploadAction();
	}
	private FileUploadAction(){
		
	}
	
	public static FileUploadAction getInstance(){
		return upload;
	}
	
	
	public void albumSwitchAction(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> itr = items.iterator();
		FileItem item = null;
		while (itr.hasNext()) {
			item = itr.next();
			if (item.isFormField()) {
				if ( "m".equals(item.getFieldName()) ) {
					int model = Integer.valueOf(item.getString());
					switch ( model ) {
					case SwitchAction.BACK_USER_ADD_ALBUM:
						this.addAlbum(items,session,request,response,model);
						return;
					case SwitchAction.BACK_USER_UPDATE_ALBUM:
						this.updateAlbum(items,session,request,response,model);
						return;
					default:;
						this.to404(request,response);
						return;
					}
				}
			}
		}		
	}
	
	public void resourceSwitchAction(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> itr = items.iterator();
		FileItem item = null;
		while (itr.hasNext()) {
			item = itr.next();
			if (item.isFormField()) {
				if ( "m".equals(item.getFieldName()) ) {
					int model = Integer.valueOf(item.getString());
					switch ( model ) {
					case SwitchAction.BACK_USER_UPLOAD_RESOURCE:
						this.uploadResource(items,session,request,response,model);
						return;
					default:;
						this.to404(request,response);
						return;
					}
				}
			}
		}		
	}
	
	private void uploadResource(List<FileItem> items,HttpSession session,HttpServletRequest request,HttpServletResponse response,int model) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(model);
		action.uploadResource(items, session, request, response);
	}
	
	private void addAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,HttpServletResponse response,int model) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(model);
		action.addAlbum(items, session, request, response);
	}
	
	private void updateAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,HttpServletResponse response,int model) 
			throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(model);
		action.updateAlbum(items, session, request, response);
	}
	
	private void to404(HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		
	}
	
	public void upload(HttpServletRequest request,
			HttpServletResponse response){
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(Constant.PARAM_ALBUM_IMAGE_MAX_MEM_FILE);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(Constant.PARAM_ALBUM_IMAGE_MAX_FILE_SIZE);
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> itr = items.iterator();
			FileItem item = null;
			while (itr.hasNext()) {
				item = itr.next();
				
				if (item.isFormField()) {
					
				} else {
					this.upload(item);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void upload(FileItem item) throws Exception{
		item.write(new File("D:/"+item.getName()));
	}
}
