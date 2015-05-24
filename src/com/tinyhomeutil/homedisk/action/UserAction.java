/**
 * www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.tinyhomeutil.homedisk.dao.BaseDao;
import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.i18n.LocaleToolkit;
import com.tinyhomeutil.homedisk.pojo.Album;
import com.tinyhomeutil.homedisk.pojo.Resource;
import com.tinyhomeutil.homedisk.pojo.User;
import com.tinyhomeutil.homedisk.pojo.UserOverView;
import com.tinyhomeutil.homedisk.util.ImageUtil;
import com.tinyhomeutil.homedisk.util.Toolkit;

/**
 * @author jack www.tinyhomeutil.com
 */
public class UserAction implements IUser{

	@Override
	public UserOverView overview(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int id = (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		StringBuilder sb = new StringBuilder();
		sb.append("select * from home_user where id = ").append(id);
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setName(rs.getString("name"));
		u.setLocked(rs.getInt("locked"));
		u.setAdmin(0);
		u.setPrivateVisit(rs.getInt("private_visit"));
		
		UserOverView obj = new UserOverView();
		obj.setUser(u);
		
		rs.close();
		sb = new StringBuilder();
		sb.append("select count(1) as itemnumber from home_resource where userid = ")
		  .append(id).append(" and deleted = 0 ");
		rs = rs.getStatement().executeQuery(sb.toString());
		rs.next();
		obj.setUploadResourceNumber(rs.getInt("itemnumber"));		
		BaseDao.release(rs);
		
		return obj;
	}

	@Override
	public void updatePwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pwd1 = request.getParameter(Constant.PARAM_PWD1);
		String pwd2 = request.getParameter(Constant.PARAM_PWD2);
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		String link = null;
		String nextlink = "pwd.jsp";
		if ( pwd1 == null ) {
			pwd1 = "";
		}
		if ( pwd2 == null ) {
			pwd2 = "";
		}
		if ( pwd1.equals(pwd2) ) {
			StringBuilder sb = new StringBuilder();
			sb.append("update home_user set pwd = '")
			  .append(Toolkit.md5(pwd1)).append("'")
			  .append(" where id = ").append(userId);
			BaseDao.execute(sb.toString());
			link = Toolkit.successLink(nextlink, "noctice_pwd_changed");
		} else {
			link = Toolkit.failLink(nextlink, "pwd_equal_err_noctice");
		}
		response.sendRedirect(link);
		
	}

	private String getAlubmsCondition(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder(); 
		String albumName = request.getParameter(Constant.PARAM_ALBUM_NAME);
		if ( albumName != null && !"".equals(albumName) && !"null".equals(albumName)  ) {
			sb.append(" and name like '%").append(albumName).append("%'");
		}
		String albumParent = request.getParameter(Constant.PARAM_ALBUM_PARENT);
		if ( albumParent != null && !"".equals(albumParent) && !"null".equals(albumParent)  ) {
			if ( !Constant.ALBUM_ALL.equals(albumParent) ) {
				sb.append(" and parent = '").append(albumParent).append("'");
			}
		}
		return sb.toString();
	}
	
	@Override
	public Pages<List<Album>> getAlbums(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		int currentPage = Toolkit.getCurrentPage(request);
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		
		Pages<List<Album>> page = new Pages<List<Album>>();
		List<Album> albums = new ArrayList<Album>();
		Album a = null;
		
		String condition = this.getAlubmsCondition(request);		
		if ( !"".equals(condition) ) {
			page.setCurrentPage(1);
		} else {
			page.setCurrentPage(currentPage);
		}
		
		StringBuilder sb = new StringBuilder(); 
		sb.append("select count(1) as itemnumber from home_album where userid = ").append(userId)
		  .append(this.getAlubmsCondition(request));
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		page.setItems(rs.getInt("itemnumber"));
		rs.close();
		
		sb = new StringBuilder(); 
		sb.append("select * from home_album where userid = ").append(userId)
		  .append(this.getAlubmsCondition(request))
		  .append(" order by id desc limit ").append(page.getStartNumber()).append(",").append(page.getPageSize());
		
		rs = rs.getStatement().executeQuery(sb.toString());
		while( rs.next() ) {
			a = new Album();
			a.setId(rs.getInt("id"));
			a.setName(rs.getString("name"));
			a.setParent(rs.getString("parent"));
			a.setUserId(rs.getInt("userid"));
			a.setUserName(rs.getString("username"));
			a.setImageUrl(rs.getString("imageweburl"));
			a.setImageWebUrl(rs.getString("imagefileurl"));
			a.setPrivateView(rs.getInt("private"));
			albums.add(a);
		}
		page.setList(albums);
		BaseDao.release(rs);
		
		return page;
	}

	@Override
	public void updateAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int userId = (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		int albumId = -1;
		String userName = "";
		StringBuilder sb = new StringBuilder();
		sb.append("select id,name from home_user where id = ").append(userId);
		ResultSet rs = BaseDao.query(sb.toString());
		if ( rs.next() ) {
			userName = rs.getString("name");
			userName = userName.trim();
		}
		BaseDao.release(rs);
		
		String albumName = null;
		String parent = null;
		boolean isNull = false;
		String nextlink = "album.jsp";
		Iterator<FileItem> itr = items.iterator();
		FileItem item = null;
		FileItem imageItem = null;
		String fileType = null;
		int currentPage = 1;
		while (itr.hasNext()) {
			item = itr.next();
			if (item.isFormField()) {
				if ( Constant.PARAM_ALBUM_NAME.equals(item.getFieldName()) ) {
					albumName = Toolkit.encode8859toUtf8(item.getString());
					isNull = Toolkit.isNull(albumName, nextlink, "user_album_name_null_notice",response);
					if ( isNull ) return;
				} else if ( Constant.PARAM_ALBUM_PARENT.equals(item.getFieldName()) ) {
					parent = item.getString();
					isNull = Toolkit.isNull(parent, nextlink, "user_album_parent_null_notice",response);
					if ( isNull ) return;
				} else if ( Constant.PARAM_ALBUM_ID.equals(item.getFieldName()) ) {
					String id = item.getString();
					isNull = Toolkit.isNull(id, nextlink, "user_album_id_null_notice",response);
					if ( isNull ) return;
					albumId = Integer.valueOf(id);
				} else if ( Constant.PARAM_PAGE.equals(item.getFieldName()) ) {
					isNull = Toolkit.isNull(item.getString(), "", "",response);
					if ( isNull ) {
						currentPage = 1;
					} else {
						currentPage = Integer.valueOf(item.getString());
					}
				} 
			} else {
				if (item.getName() != null && !item.getName().equals("")) {
					if ( Toolkit.isImageFile(item.getName()) ) {
						imageItem = item;
						fileType = Toolkit.getFileType(item.getName());
					}
				} 
			}
		}
		
		String webPath = "";
		String localePath = "";
		if ( imageItem != null ) {
			String imageLocaleUrl = null;
			sb = new StringBuilder();
			sb.append("select imagefileurl from home_album where id = ").append(albumId);
			rs = BaseDao.query(sb.toString());
			if ( rs.next() ) {
				imageLocaleUrl = rs.getString("imagefileurl");
			}
			BaseDao.release(rs);
			if ( imageLocaleUrl != null && !"".equals(imageLocaleUrl) ) {
				new File(imageLocaleUrl).delete();
			}
						
			String appPath = Toolkit.getAppRootPath(request);
			String simpleFileName = Toolkit.getSimpleFileName(fileType);
			localePath = Toolkit.getAbsoluteAlbumImagePath(appPath, userName, parent, simpleFileName);
			String webRootPath = Toolkit.getWebRootPath(request);
			webPath = Toolkit.getRelativeAlbumImagePath(webRootPath, userName, parent, simpleFileName);
			localePath = localePath.replace("\\", "/");
						
			File image = new File(localePath);
			imageItem.write(image);
			
			this.zipImage(image);
		} 
		sb = new StringBuilder();
		sb.append("update home_album set ")
		  .append("name = ").append("'").append(albumName).append("'").append(",")
		  .append("parent = ").append("'").append(parent).append("'");
		if ( imageItem != null ) {
			sb.append(",")
			  .append("imageweburl = ").append("'").append(webPath).append("'").append(",")
			  .append("imagefileurl = ").append("'").append(localePath).append("'");
		}
		sb.append(" where id = ").append(albumId);
		BaseDao.execute(sb.toString());
		String link = Toolkit.successLink(nextlink, "user_album_update_success_notice");
		link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
		response.sendRedirect(link);
		
		if ( imageItem != null ) {
			
		}
	}

	@Override
	public void addAlbum(List<FileItem> items,HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int userId = (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		String userName = this.getUserName(userId);
		
		
		String albumName = null;
		String parent = null;
		boolean isNull = false;
		String nextlink = "album.jsp";
		Iterator<FileItem> itr = items.iterator();
		FileItem item = null;
		FileItem imageItem = null;
		String fileType = null;
		while (itr.hasNext()) {
			item = itr.next();
			if (item.isFormField()) {
				if ( Constant.PARAM_ALBUM_NAME.equals(item.getFieldName()) ) {
					albumName = Toolkit.encode8859toUtf8(item.getString());
					isNull = Toolkit.isNull(userName, nextlink, "user_album_name_null_notice",response);
					if ( isNull ) return;
				} else if ( Constant.PARAM_ALBUM_PARENT.equals(item.getFieldName()) ) {
					parent = item.getString();
					isNull = Toolkit.isNull(userName, nextlink, "user_album_parent_null_notice",response);
					if ( isNull ) return;
				} 
			} else {
				if (item.getName() != null && !item.getName().equals("")) {
					if ( Toolkit.isImageFile(item.getName()) ) {
						imageItem = item;
						fileType = Toolkit.getFileType(item.getName());
					}
				} 
			}
		}
		
		String webPath = "";
		String localePath = "";
		if ( imageItem != null ) {
			String appPath = Toolkit.getAppRootPath(request);
			String simpleFileName = Toolkit.getSimpleFileName(fileType);
			localePath = Toolkit.getAbsoluteAlbumImagePath(appPath, userName, parent, simpleFileName);
			String webRootPath = Toolkit.getWebRootPath(request);
			webPath = Toolkit.getRelativeAlbumImagePath(webRootPath, userName, parent, simpleFileName);
			localePath = localePath.replace("\\", "/");
			
			File image = new File(localePath);
			imageItem.write(image);
			
			this.zipImage(image);
		} 
		
		StringBuilder sb = new StringBuilder();
		sb.append("insert into home_album(name,parent,userid,username,imageweburl,imagefileurl,private)values(")
		  .append("'").append(albumName).append("'").append(",")
		  .append("'").append(parent).append("'").append(",")
		  .append(userId).append(",")
		  .append("'").append(userName).append("'").append(",")
		  .append("'").append(webPath).append("'").append(",")
		  .append("'").append(localePath).append("'").append(",")
		  .append(1)
		  .append(")");
		BaseDao.execute(sb.toString());
		response.sendRedirect(Toolkit.successLink(nextlink, "user_album_add_success_notice"));
		
	}


	@Override
	public void setAlbumViewStatus(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int albumId = Integer.valueOf(request.getParameter(Constant.PARAM_ALBUM_ID));
		int privateState = Integer.valueOf(request.getParameter(Constant.PARAM_ALBUM_PRIVATE));
		int currentPage = Toolkit.getCurrentPage(request);
		String link = null;
		String nextlink = "album.jsp";
		StringBuilder sb = new StringBuilder();
		sb.append("update home_album set private = ").append(privateState)
		  .append(" where id = ").append(albumId);
		
		BaseDao.execute(sb.toString());
		
		sb = new StringBuilder();
		sb.append("update home_resource set private = ").append(privateState)
		  .append(" where albumid = ").append(albumId);
		
		BaseDao.execute(sb.toString());
		
		link = Toolkit.successLink(nextlink, "user_album_view_set_success_notice");
		link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
		response.sendRedirect(link);	
	}
	
	@Override
	public void dellAlbum(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int albumId = Integer.valueOf(request.getParameter(Constant.PARAM_ALBUM_ID));
		int currentPage = Toolkit.getCurrentPage(request);
		String link = null;
		String nextlink = "album.jsp";
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) as itemnumber from home_resource")
		  .append(" where id = ").append(albumId);
		
		ResultSet rs = BaseDao.query(sb.toString());
		if ( rs.next() ) {
			int itemnumber = rs.getInt("itemnumber");
			BaseDao.release(rs);
			if ( itemnumber > 0 ) {
				link = Toolkit.failLink(nextlink, "user_album_delete_success_notice");
				link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
				response.sendRedirect(link);	
				return;
			}	
		}
		
		sb = new StringBuilder();
		sb.append("delete from home_album")
		  .append(" where id = ").append(albumId);
		
		BaseDao.execute(sb.toString());
		link = Toolkit.successLink(nextlink, "user_album_delete_success_notice");
		link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
		
		response.sendRedirect(link);	
	}

	private String getResourceCondition(HttpServletRequest request) {
			StringBuilder sb = new StringBuilder(); 
			String resourceName = request.getParameter(Constant.PARAM_RESOURCE_NAME);
			if ( resourceName != null && !"".equals(resourceName) && !"null".equals(resourceName)  ) {
				sb.append(" and name like '%").append(resourceName).append("%' ");
			}
			String albumName = request.getParameter(Constant.PARAM_ALBUM_NAME);
			if ( albumName != null && !"".equals(albumName) && !"null".equals(albumName)  ) {
				sb.append(" and albumname like '%").append(albumName).append("%' ");
			}
			String albumParent = request.getParameter(Constant.PARAM_ALBUM_PARENT);
			if ( albumParent != null && !"".equals(albumParent) && !"null".equals(albumParent)  ) {
				if ( !Constant.ALBUM_ALL.equals(albumParent) ) {
					sb.append(" and albumparent = '").append(albumParent).append("' ");
				}
			}
			return sb.toString();
		}
		
	@Override
	public Pages<List<Resource>> getResources(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int currentPage = Toolkit.getCurrentPage(request);
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		
		Pages<List<Resource>> page = new Pages<List<Resource>>();
		List<Resource> resourceList = new ArrayList<Resource>();
		Resource r = null;
		page.setCurrentPage(currentPage);
				
		StringBuilder sb = new StringBuilder(); 
		sb.append("select count(1) as itemnumber from home_resource where deleted = 0 and userid = ").append(userId)
		  .append(this.getResourceCondition(request));
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		page.setItems(rs.getInt("itemnumber"));
		rs.close();
		
		sb = new StringBuilder(); 
		sb.append("select * from home_resource where deleted = 0 and userid = ").append(userId)
		  .append(this.getResourceCondition(request))
		  .append(" order by id desc limit ").append(page.getStartNumber()).append(",").append(page.getPageSize());
		
		rs = rs.getStatement().executeQuery(sb.toString());
		while( rs.next() ) {
			r = new Resource();
			r.setId(rs.getInt("id"));
			r.setName(rs.getString("name"));
			r.setUserId(rs.getInt("userid"));
			r.setUserName(rs.getString("username"));
			r.setAlbumId(rs.getInt("albumid"));
			r.setAlbumName(rs.getString("albumname"));
			r.setAlbumParent(rs.getString("albumparent"));
			r.setRemark(rs.getString("remark"));
			r.setIsPrivate(rs.getInt("private"));
			r.setWebUrl(rs.getString("weburl"));
			r.setFileUrl(rs.getString("fileurl"));
			resourceList.add(r);
		}
		page.setList(resourceList);
		BaseDao.release(rs);
		
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void uploadResource(List<FileItem> items,HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Resource> resourceList = null;
		Resource r = null;
		FileItem uploadFileItem = null;
		PrintWriter out = response.getWriter();
		Locale locale = request.getLocale();
		ResourceBundle messages = LocaleToolkit.getBundle(locale);
		
		for ( FileItem item : items ) {
			if ( item.isFormField() ) {
				if ( Constant.PARAM_RESOURCE_TIMESTAMP.equals(item.getFieldName()) ) {
					if ( session.getAttribute(Constant.PARAM_RESOURCE_TIMESTAMP) == null ) {
						session.setAttribute(Constant.PARAM_RESOURCE_TIMESTAMP, item.getString());
						if ( session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST) != null ) {
							resourceList = (List<Resource>)session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST);
							for ( Resource res : resourceList ){
								new File(res.getFileUrl()).delete();
							}
							resourceList.clear();
						} else {
							resourceList = new ArrayList<Resource>();
							session.setAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST, resourceList);
						}
					} else {
						String timestamp = (String)session.getAttribute(Constant.PARAM_RESOURCE_TIMESTAMP);
						if ( timestamp.equals(item.getString()) ) {
							resourceList = (List<Resource>)session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST);
							if ( resourceList == null ) {
								out.print(messages.getString("user_response_upload_fail_notice"));
								out.flush();
								out.close();
								return;
							}
						} else {
							session.setAttribute(Constant.PARAM_RESOURCE_TIMESTAMP, item.getString());
							if ( session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST) != null ) {
								resourceList = (List<Resource>)session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST);
								for ( Resource res : resourceList ){
									new File(res.getFileUrl()).delete();
								}
								resourceList.clear();
							} else {
								resourceList = new ArrayList<Resource>();
								session.setAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST, resourceList);
							}
						}
					}
				}
			} else {
				uploadFileItem = item;
			}
		}
		
		if (uploadFileItem != null) {
			String fileName = Toolkit.encode8859toUtf8(uploadFileItem.getName().trim());
			Set<String> imageType = Toolkit.imageType();
			String fileType = Toolkit.getFileType(fileName);						
			int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
			String userName = this.getUserName(userId);
			
			String appPath = Toolkit.getAppRootPath(request);
			String simpleFileName = Toolkit.getSimpleFileName(fileType);
			String localePath = Toolkit.getAbsoluteResourcePath(appPath, userName, simpleFileName);
			String webRootPath = Toolkit.getWebRootPath(request);
			String webPath = Toolkit.getRelativeResourcePath(webRootPath, userName, simpleFileName);
						
			File file = new File(localePath);
			uploadFileItem.write(file);
			
			if ( imageType.contains(fileType.toLowerCase()) ) {
				this.zipImage2(file);
			}
			
			r = new Resource();
			r.setName(uploadFileItem.getName());
			r.setDeleted(0);
			r.setFileUrl(file.getAbsolutePath().replace("\\", "/"));
			r.setWebUrl(webPath);
			r.setUserId(userId);
			r.setUserName(userName);
			r.setCreateTime(System.currentTimeMillis());
			r.setIsPrivate(1);
			
			resourceList.add(r);
		}
		
		
		out.print("1");
		out.flush();
		out.close();
	}

	@Override
	public void downloadResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
	}

	@Override
	public void delResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		int resourceId= Integer.valueOf(request.getParameter(Constant.PARAM_RESOURCE_ID));
		StringBuilder sb = new StringBuilder(); 
		sb.append("update home_resource set deleted = 1 where id = ").append(resourceId)
		  .append(" and userid = ").append(userId);
		BaseDao.execute(sb.toString());
		
		String link = "resource.jsp";
		link = Toolkit.successLink(link, "user_resource_del_success_notice");
		response.sendRedirect(link);	
	}

	@Override
	public String getAllAlbums(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		StringBuilder sb = new StringBuilder(); 
		sb.append("select id,name,parent from home_album where userid = ").append(userId)
		  .append(" order by id desc ");
		Map<String,List<Album>> map = new HashMap<String,List<Album>>();
		List<Album> albums = null;
		Album a = null;
		ResultSet rs = BaseDao.query(sb.toString());
		
		while( rs.next() ) {
			a = new Album();
			a.setId(rs.getInt("id"));
			a.setName(rs.getString("name"));
			a.setParent(rs.getString("parent"));
			if ( map.get(a.getParent()) == null ) {
				albums = new ArrayList<Album>();
				map.put(a.getParent(), albums);
			}
			map.get(a.getParent()).add(a);
		}
		BaseDao.release(rs);
		
		StringBuilder json = new StringBuilder();
		if ( map.isEmpty() ) {
			return "";
		}
		json.append("{");
		Set<Entry<String,List<Album>>> set = map.entrySet();
		int setSize = set.size();
		int i = 0;
		for ( Entry<String,List<Album>> en : set ) {
			json.append("\"").append(en.getKey()).append("\"").append(":");
			if ( en.getValue().isEmpty() ) {
				json.append("\"\"");
			} else {
				albums = en.getValue();
				json.append("[");
				for ( int j= 0 ; j < albums.size() ; j++ ) {
					a = albums.get(j);
					json.append("{")
					    .append("\"").append("id").append("\"").append(":")
					    .append("\"").append(a.getId()).append("\"").append(",")
					    .append("\"").append("name").append("\"").append(":")
					    .append("\"").append(a.getName()).append("\"")
					    .append("}");
					if ( j < albums.size() - 1 ) {
						json.append(",");
					}
				}
				json.append("]");
			}
			if ( i < setSize -1 ) {
				json.append(",");
			}
			i++;
		}
		json.append("}");
		
		return json.toString();
	}

	@Override
	public void saveResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String link = null;
		String nextlink = "uploadresource.jsp";
		@SuppressWarnings("unchecked")
		List<Resource> resourceList = (List<Resource>)session.getAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST);
		if ( resourceList == null || resourceList.isEmpty() ) {
			link = Toolkit.failLink(nextlink, "user_resource_upload_file_null_notice");
			response.sendRedirect(link);	
			return;
		}
		
		int albumId = Integer.valueOf(request.getParameter(Constant.PARAM_ALBUM_ID));
		String remark = request.getParameter(Constant.PARAM_RESOURCE_REMARK);
		if ( remark == null )
			remark = "";
		
		String albumName = null;
		String parent = null;
		int isPrivate = 1;
		StringBuilder sb = new StringBuilder();
		sb.append("select name,parent,private from home_album ")
		  .append(" where id = ").append(albumId);
		ResultSet rs = BaseDao.query(sb.toString());
		if ( rs.next() ) {
			albumName = rs.getString("name");
			parent = rs.getString("parent");
			isPrivate = rs.getInt("private");
			BaseDao.release(rs);
		} else {
			link = Toolkit.failLink(nextlink, "user_resource_upload_album_null_notice");
			response.sendRedirect(link);	
			return;
		}
		
		for ( Resource r : resourceList ) {			
			sb = new StringBuilder();
			sb.append("insert into home_resource(name,remark,fileurl,weburl,deleted,userid,username,albumid,albumname,albumparent,private,createtime)")
			  .append(" values ( ")
			  .append("'").append(r.getName()).append("'").append(",")
			  .append("'").append(remark).append("'").append(",")
			  .append("'").append(r.getFileUrl()).append("'").append(",")
			  .append("'").append(r.getWebUrl()).append("'").append(",")
			  .append(0).append(",")
			  .append("'").append(r.getUserId()).append("'").append(",")
			  .append("'").append(r.getUserName()).append("'").append(",")
			  .append(albumId).append(",")
			  .append("'").append(albumName).append("'").append(",")
			  .append("'").append(parent).append("'").append(",")
			  .append(isPrivate).append(",")
			  .append(r.getCreateTime())
			  .append(")");
			System.out.println(sb.toString());
			BaseDao.execute(sb.toString());
		}
		
		session.setAttribute(Constant.SESSION_UPLOADED_RESOURCE_LIST, null);
		session.setAttribute(Constant.PARAM_RESOURCE_TIMESTAMP, null);
		
		nextlink = "resource.jsp";
		link = Toolkit.successLink(nextlink, "user_resource_upload_save_success_notice");
		response.sendRedirect(link);	
		return;
	}

	private String getUserName(int userId) throws Exception   {
		String userName = "";
		StringBuilder sb = new StringBuilder();
		sb.append("select id,name from home_user where id = ").append(userId);
		ResultSet rs = BaseDao.query(sb.toString());
		if ( rs.next() ) {
			userName = rs.getString("name");
			userName = userName.trim();
		}
		BaseDao.release(rs);
		return userName;
	}

	@Override
	public Resource getResourceObject(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String nextlink;
		String link;
		int currentPage = Toolkit.getCurrentPage(request);
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		int resourceId= Integer.valueOf(request.getParameter(Constant.PARAM_RESOURCE_ID));
		StringBuilder sb = new StringBuilder(); 
		sb.append("select * from home_resource where deleted = 0 and id = ").append(resourceId)
		  .append(" and userid = ").append(userId);
		
		ResultSet rs = BaseDao.query(sb.toString());
		if ( rs.next() ) {
			Resource r = new Resource();
			r.setId(rs.getInt("id"));
			r.setName(rs.getString("name"));
			r.setDeleted(rs.getInt("deleted"));
			r.setFileUrl(rs.getString("fileurl"));
			r.setWebUrl(rs.getString("weburl"));
			r.setUserId(rs.getInt("userid"));
			r.setUserName(rs.getString("username"));
			r.setCreateTime(rs.getLong("createtime"));
			r.setIsPrivate(rs.getInt("private"));
			r.setAlbumId(rs.getInt("albumid"));
			r.setAlbumName(rs.getString("albumname"));
			r.setAlbumParent(rs.getString("albumparent"));
			BaseDao.release(rs);
			return r;
		} else {
			BaseDao.release(rs);
			nextlink = "resource.jsp";
			link = Toolkit.failLink(nextlink, "user_resource_download_file_null_notice");
			link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
			response.sendRedirect(link);
			return null;
		}
	}
	
	private void zipImage(File srcImageFile) throws IOException{
		double targetImageWidth = 128d;
		double targetImageHeight = 105d;
		double targetScale = targetImageWidth / targetImageHeight;
		
		BufferedImage src = ImageIO.read(srcImageFile); 
		double srcImageWidth = src.getWidth(); 
		double srcImageHeight = src.getHeight(); 
        double srcScale = srcImageWidth / srcImageHeight;
		
        String zipImagePath = this.getZipImagePath(srcImageFile);
        String cutImagePath = this.getCutImagePath(srcImageFile);
        
        if ( targetScale > srcScale ) {
        	double tmpImageHeight = srcImageHeight/srcImageWidth * targetImageWidth;
        	
        	ImageUtil.scale2(srcImageFile.getAbsolutePath(), 
        			zipImagePath, 
        			new Double(tmpImageHeight).intValue(), 
        			new Double(targetImageWidth).intValue(), true);
        	
        	double top = (tmpImageHeight - targetImageHeight)/2;
        	
        	ImageUtil.cut(zipImagePath, 
        			cutImagePath, 
        			0,
        			new Double(top).intValue(), 
        			new Double(targetImageWidth).intValue(), 
        			new Double(targetImageHeight).intValue() );
        	
        } else {
        	double tmpImageWidth = srcImageWidth/srcImageHeight * targetImageHeight;
        	        	
        	ImageUtil.scale2(srcImageFile.getAbsolutePath(), 
        			zipImagePath, 
        			new Double(targetImageHeight).intValue(), 
        			new Double(tmpImageWidth).intValue(), true);
        	
        	double left = (tmpImageWidth - targetImageWidth)/2;
        	
        	ImageUtil.cut(zipImagePath, 
        			cutImagePath, 
        			new Double(left).intValue(), 
        			0,
        			new Double(targetImageWidth).intValue(), 
        			new Double(targetImageHeight).intValue() );
        }
		
        srcImageFile.delete();
        new File(zipImagePath).delete();
        
	}
	
	private void zipImage2(File srcImageFile) throws IOException{
		double targetImageWidth = 0d;
		double targetImageHeight = 120d;
		
		BufferedImage src = ImageIO.read(srcImageFile); 
		double srcImageWidth = src.getWidth(); 
		double srcImageHeight = src.getHeight(); 
        double srcScale = srcImageWidth / srcImageHeight;
		
        targetImageWidth = srcScale * targetImageHeight;
        
        String zipImagePath = Toolkit.getSmallImageName(srcImageFile.getAbsolutePath());
               
    	ImageUtil.scale2(srcImageFile.getAbsolutePath(), 
    			zipImagePath, 
    			new Double(targetImageHeight).intValue(), 
    			new Double(targetImageWidth).intValue(), true);
    	
	}
	
	private String getZipImagePath(File srcImageFile){
		return srcImageFile.getParent()+"/zip_"+srcImageFile.getName();
	}
	
	private String getCutImagePath(File srcImageFile){
		return srcImageFile.getParent()+"/cut_"+srcImageFile.getName();
	}
}
