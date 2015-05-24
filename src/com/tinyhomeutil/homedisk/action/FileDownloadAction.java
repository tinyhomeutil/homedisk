/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.pojo.Resource;

/**
 * @author jack lee www.tinyhomeutil.com
 */
public class FileDownloadAction {

	private static FileDownloadAction download = null;
	static {
		download = new FileDownloadAction();
	}
	private FileDownloadAction(){
		
	}
	
	public static FileDownloadAction getInstance(){
		return download;
	}
	
	public void downloadResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		IUser action = (IUser)ActionFacotry.getInstance().getAction(SwitchAction.BACK_USER_DOWNLOAD_RESOURCE);
		Resource res = action.getResourceObject(session, request, response);
		if ( res == null ) {
			return;
		} else {
			String path = res.getFileUrl();
			this.downloadLocal(path, response);
		}
	}
	
	public void foreDownloadResource(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_DOWNLOAD_RESOURCE);
		Resource res = action.getResourceObject(session, request, response);
		if ( res == null ) {
			return;
		} else {
			String path = res.getFileUrl();
			this.downloadLocal(path, response);
		}
	}
	
	protected void downloadLocal(String path,HttpServletResponse response) throws FileNotFoundException {
        File file = new File(path);
        InputStream inStream = new FileInputStream(path);
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        byte[] b = new byte[100];
        int len;
        
        try {
        	OutputStream os = response.getOutputStream();
            while ((len = inStream.read(b)) > 0)
            	os.write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
