/**
 *  www.tinyhomeutil.com
 */
package com.tinyhomeutil.homedisk.action;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.dao.BaseDao;
import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.pojo.Album;
import com.tinyhomeutil.homedisk.pojo.Resource;
import com.tinyhomeutil.homedisk.pojo.User;
import com.tinyhomeutil.homedisk.util.Toolkit;

/**
 * @author jack www.tinyhomeutil.com
 *
 */
public class ForeAction implements IFore {

	@Override
	public void login(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String link = null;
		String nextlink = "login.jsp";
		boolean isNull = false;
		
		String userName = request.getParameter(Constant.PARAM_USER_NAME);
		isNull = Toolkit.isNull(userName, nextlink, "user_login_username_null_notice",response);
		if ( isNull ) return;
		userName = Toolkit.htmlTransfer(userName);
				
		String pwd = request.getParameter(Constant.PARAM_PWD);
		isNull = Toolkit.isNull(pwd, nextlink, "user_login_pwd_null_notice",response);
		if ( isNull ) return;
		pwd = Toolkit.md5(pwd);		

		StringBuilder sb = new StringBuilder();
		sb.append("select id,name,locked,admin,private_visit from home_user where name = ")
		   .append("'").append(userName).append("'").append(" and pwd = ")
		  .append("'").append(pwd).append("'");
		
		ResultSet rs = BaseDao.query(sb.toString());
		User u = null;
		if ( rs.next() ) {
			u = new User();
			u.setId(rs.getInt("id"));
			u.setName(rs.getString("name"));
			u.setLocked(rs.getInt("locked"));
			u.setAdmin(rs.getInt("admin"));
			u.setPrivateVisit(rs.getInt("private_visit"));
			session.setAttribute(Constant.PARAM_SESSION_USER_OBJECT, u);
			session.setAttribute(Constant.PARAM_SESSION_USER_ID, u.getId());
			BaseDao.release(rs);
		} else {
			link = Toolkit.failLink(nextlink, "user_login_fail_notice");
			response.sendRedirect(link);	
			return;
		}
				
		if ( u.getLocked() == 1 ) {
			session.setAttribute(Constant.PARAM_SESSION_USER_OBJECT, null);
			session.setAttribute(Constant.PARAM_SESSION_USER_ID, null);
			link = Toolkit.failLink(nextlink, "user_login_locked_notice");
			response.sendRedirect(link);
			return;
		}
		if ( u.getAdmin() == 0 ) {
			link = "overview.jsp";
		} else {
			link = "adminoverview.jsp";
		}
		response.sendRedirect(link);	
		return;
	}
	
	@Override
	public Map<String, List<Album>> getIndexAlbumList(
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String viewRange = this.getAlbumViewRangeCondition(session);
		
		StringBuilder sb = new StringBuilder();
		sb.append("(select id,name,parent,imageweburl from home_album where ").append(viewRange).append(" and parent = '").append(Constant.ALBUM_IMAGE).append("' order by id desc limit 0,7)")
		  .append("union all")
		  .append("(select id,name,parent,imageweburl from home_album where ").append(viewRange).append(" and parent = '").append(Constant.ALBUM_VIDEO).append("' order by id desc limit 0,7)")
		  .append("union all")
		  .append("(select id,name,parent,imageweburl from home_album where ").append(viewRange).append(" and parent = '").append(Constant.ALBUM_AUDIO).append("' order by id desc limit 0,7)");
		ResultSet rs = BaseDao.query(sb.toString());
		
		Map<String, List<Album>> map = new HashMap<String, List<Album>>();
		List<Album> list = null;
		Album a = null;
		while ( rs.next() ) {
			a = new Album();
			a.setId(rs.getInt("id"));
			a.setName(rs.getString("name"));
			a.setParent(rs.getString("parent"));
			a.setImageWebUrl(rs.getString("imageweburl"));
			if ( map.get(a.getParent()) == null ) {
				list = new ArrayList<Album>();
				map.put(a.getParent(), list);
			} 
			map.get(a.getParent()).add(a);
		}
		BaseDao.release(rs);
		return map;
	}

	@Override
	public Pages<List<Album>> getVideoAlbumList(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageSize = 14;
		return this.getMediaAlbumList(session, request, response, Constant.ALBUM_VIDEO,pageSize);
	}

	@Override
	public Pages<List<Album>> getImageAlbumList(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageSize = 14;
		return this.getMediaAlbumList(session, request, response, Constant.ALBUM_IMAGE,pageSize);
	}

	@Override
	public Pages<List<Album>> getAudioAlbumList(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageSize = 14;
		return this.getMediaAlbumList(session, request, response, Constant.ALBUM_AUDIO,pageSize);
	}

	@Override
	public List<Album> getOtherAlbumList(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewRange = this.getAlbumViewRangeCondition(session);
		
		List<Album> albums = new ArrayList<Album>();
		Album a = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select id,name,parent,imageweburl from home_album where ")
		  .append(viewRange).append(" and parent = '").append(Constant.ALBUM_OTHER).append("' order by id desc");
		ResultSet rs = BaseDao.query(sb.toString());
		
		while ( rs.next() ) {
			a = new Album();
			a.setId(rs.getInt("id"));
			a.setName(rs.getString("name"));
			a.setParent(rs.getString("parent"));
			a.setImageWebUrl(rs.getString("imageweburl"));
			albums.add(a);	
		}
		BaseDao.release(rs);
		return albums;
	}
	
	@Override
	public String getRowHtml(List<Album> list, int columnsInRow, int rowNum,
			String albumName, String defaultImageUrl,String viewUrl) throws Exception {		
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\">");
		
		int size;
    	int realSize;
    	int emptySize;
    	int startNum = 0;
    	Album a = null;
    	String imageUrl = null;
    	
        if ( list != null ) {
        	size = list.size();
        } else{
        	size = 0;
        }
        
        if ( rowNum > 1 ) {
        	if ( columnsInRow >= size ) {
        		startNum = 0;
        		realSize = 0;
        	} else {
        		startNum = columnsInRow * ( rowNum - 1 );
        		if ( columnsInRow * rowNum >= size ) {
        			realSize = size - startNum;
        		} else {
        			realSize = columnsInRow * rowNum - startNum;
        		}
        	}
        } else {
        	startNum = 0;
        	realSize = size;
        }
        
		emptySize = columnsInRow - realSize;		
		
    	for ( int i = startNum ; i < realSize ; i++ ) {
    		a = list.get(i);
    		if ( a.getImageWebUrl() == null || "".equals(a.getImageWebUrl()) )
    			imageUrl = defaultImageUrl;
    		else {
    			imageUrl = a.getImageWebUrl();
    		}
    			
    		sb.append("<div class=\"col-sm-6 col-md-101\">")
    		  .append("<div class=\"thumbnail\">")
    		  .append("<a href=\"").append(viewUrl);
    		if ( viewUrl.contains("?") ) {
    			sb.append("&");
    		} else {
    			sb.append("?");
    		}
    		sb.append(Constant.PARAM_ALBUM_ID).append("=").append(a.getId()).append("\" >")
    		  .append("<img src=").append(imageUrl).append(" alt=\"").append(a.getName()).append("\"  height=\"200\">")
    		  .append("</a>")
    		  .append("<a href=\"").append(viewUrl);
    		if ( viewUrl.contains("?") ) {
    			sb.append("&");
    		} else {
    			sb.append("?");
    		}
    		sb.append(Constant.PARAM_ALBUM_ID).append("=").append(a.getId()).append("\" >")
    		  .append("<h5 class=\"text-style-1\">").append(a.getName()).append("</h5>")
    		  .append("</a>")
    		  .append("</div>")
    		  .append("</div>");
    	}
    	for ( int i = 0 ; i < emptySize ; i++ ) {
    		sb.append("<div class=\"col-sm-6 col-md-101\">")
      		  .append("<div class=\"thumbnail\">")
      		  .append("<a href=\"#\">")
      		  .append("<img src=\"").append(defaultImageUrl).append("\">")
      		  .append("</a>")
      		  .append("<h5>&nbsp;</h5>")
      		  .append("</div>")
      		  .append("</div>");
    	}
    	sb.append("</div>");	
        	
		return sb.toString();
	}

	private Pages<List<Album>> getMediaAlbumList(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,String mediaType,int pageSize)
			throws Exception {
		String viewRange = this.getAlbumViewRangeCondition(session);
		
		int currentPage = Toolkit.getCurrentPage(request);
		
		Pages<List<Album>> page = new Pages<List<Album>>();
		List<Album> albums = new ArrayList<Album>();
		Album a = null;
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		
		StringBuilder sb = new StringBuilder(); 
		sb.append("select count(1) as itemnumber from home_album where ")
		  .append(viewRange).append(" and parent = '").append(mediaType).append("'");
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		page.setItems(rs.getInt("itemnumber"));
		BaseDao.release(rs);
		
		sb = new StringBuilder();
		sb.append("select id,name,parent,imageweburl from home_album where ")
		   .append(viewRange).append(" and parent = '").append(mediaType).append("' order by id desc limit ")
		  .append(page.getStartNumber()).append(",").append(page.getPageSize());
		rs = BaseDao.query(sb.toString());
		
		while ( rs.next() ) {
			a = new Album();
			a.setId(rs.getInt("id"));
			a.setName(rs.getString("name"));
			a.setParent(rs.getString("parent"));
			a.setImageWebUrl(rs.getString("imageweburl"));
			albums.add(a);	
		}
		BaseDao.release(rs);
		page.setList(albums);
		
		return page;
	}
	
	private String getAlbumViewRangeCondition(HttpSession session){
		int userId = -1;
		if ( session.getAttribute(Constant.PARAM_SESSION_USER_ID) != null ) {
			userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		}
		StringBuilder condition = new StringBuilder();
		if ( userId < 0 ) {
			condition.append(" private = 0 ");
		} else {
			condition.append(" ( private = 0 or userid = ").append(userId).append(") ");
		}
		return condition.toString();
	}

	@Override
	public List<Resource> getMediaResourceByAlbumId(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,String albumParent)
			throws Exception {
		List<Resource> list = null;
		
		String viewRange = this.getAlbumViewRangeCondition(session);
		if ( request.getParameter(Constant.PARAM_ALBUM_ID) == null ||
				"".equals(request.getParameter(Constant.PARAM_ALBUM_ID)) ) {
			return list;
		}
		int albumId = Integer.valueOf(request.getParameter(Constant.PARAM_ALBUM_ID));
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from home_resource where ").append(viewRange)
		  .append(" and deleted = 0 and albumparent = '").append(albumParent).append("'")
		  .append(" and albumid = ").append(albumId)
		  .append(" order by id ");
		ResultSet rs = BaseDao.query(sb.toString());
		
		Resource r = null;
		while ( rs.next() ) {
			if ( list == null ) {
				list = new ArrayList<Resource>();
			}
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
			list.add(r);
		}
		BaseDao.release(rs);
		
		return list;
	}

	@Override
	public Resource getImageResourceForAudioPlay(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String imageIdStr = request.getParameter(Constant.IMAGE_ID);
		if ( imageIdStr == null || "".equals(imageIdStr) ) {
			return null;
		}
		int imageId = Integer.valueOf(imageIdStr);
		String viewRange = this.getAlbumViewRangeCondition(session);
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from home_resource where ").append(viewRange)
		  .append(" and deleted = 0 and albumparent = '").append(Constant.ALBUM_IMAGE).append("'")
		  .append(" and id > ").append(imageId)
		  .append(" order by id limit 0,1");
		ResultSet rs = BaseDao.query(sb.toString());
		
		Resource r = null;
		if ( rs.next() ) {
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
		}
		BaseDao.release(rs);
		
		return r;
	}

	@Override
	public Pages<List<Resource>> getOtherResourceByAlbumId(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageSize = 10;
		Pages<List<Resource>> page = new Pages<List<Resource>>();
		List<Resource> resources = new ArrayList<Resource>();
		String viewRange = this.getAlbumViewRangeCondition(session);
		int currentPage = Toolkit.getCurrentPage(request);
		Resource r = null;
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		String albumIdStr = request.getParameter(Constant.PARAM_ALBUM_ID);
		
		StringBuilder sb = new StringBuilder(); 
		sb.append("select count(1) as itemnumber from home_resource where ")
		  .append(viewRange).append(" and albumparent = '").append(Constant.ALBUM_OTHER).append("'");
		if ( albumIdStr != null && !"".equals(albumIdStr) && !"-1".equals(albumIdStr) && !"undefined".equals(albumIdStr)) {
			sb.append(" and albumid = ").append(albumIdStr);
		} 
		
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		page.setItems(rs.getInt("itemnumber"));
		BaseDao.release(rs);
				
		sb = new StringBuilder();
		sb.append("select * from home_resource where ").append(viewRange)
		  .append(" and deleted = 0 and albumparent = '").append(Constant.ALBUM_OTHER).append("'");
		if ( albumIdStr != null && !"".equals(albumIdStr) && !"-1".equals(albumIdStr) && !"undefined".equals(albumIdStr)) {
			sb.append(" and albumid = ").append(albumIdStr);
		} 
		sb.append(" order by id desc limit ").append(page.getStartNumber()).append(",").append(page.getPageSize());
		rs = BaseDao.query(sb.toString());
		
		
		while ( rs.next() ) {
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
			resources.add(r);
		}
		BaseDao.release(rs);
		page.setList(resources);

		return page;
	}

	@Override
	public Resource getResourceObject(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String nextlink;
		String link;
		int currentPage = Toolkit.getCurrentPage(request);
		int resourceId= Integer.valueOf(request.getParameter(Constant.PARAM_RESOURCE_ID));
		String viewRange = this.getAlbumViewRangeCondition(session);
		StringBuilder sb = new StringBuilder(); 
		sb.append("select * from home_resource where ").append(viewRange).append(" and deleted = 0 and id = ").append(resourceId);
		
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
			nextlink = "other.jsp";
			link = Toolkit.failLink(nextlink, "user_resource_download_file_null_notice");
			link = link + "&"+ Constant.PARAM_PAGE +"=" + String.valueOf(currentPage);
			link = link + "&"+ Constant.PARAM_ALBUM_ID +"=" + request.getParameter(Constant.PARAM_ALBUM_ID);
			response.sendRedirect(link);
			return null;
		}
	}

	@Override
	public Pages<List<Resource>> getResourceByKeyWords(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Pages<List<Resource>> page = new Pages<List<Resource>>();
		List<Resource> resources = new ArrayList<Resource>();
		String keyWord = request.getParameter(Constant.PARAM_KEYWORD);
		int pageSize = 10;
		if ( keyWord == null || "".equals(keyWord) ) {
			page.setPageSize(pageSize);
			page.setCurrentPage(1);
			page.setList(resources);
			page.setItems(0);
			return page;
		}
		
		String viewRange = this.getAlbumViewRangeCondition(session);
		int currentPage = Toolkit.getCurrentPage(request);
		Resource r = null;
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		String condition = this.getKeyWordCondition(keyWord);
		
		StringBuilder sb = new StringBuilder(); 
		sb.append("select count(1) as itemnumber from home_resource where ")
		  .append(viewRange).append(" and ").append(condition);
		
		ResultSet rs = BaseDao.query(sb.toString());
		rs.next();
		page.setItems(rs.getInt("itemnumber"));
		BaseDao.release(rs);
				
		sb = new StringBuilder();
		sb.append("select * from home_resource where ").append(viewRange)
		  .append(" and deleted = 0 ").append(" and ").append(condition)
		  .append(" order by id desc limit ").append(page.getStartNumber()).append(",").append(page.getPageSize());
		rs = BaseDao.query(sb.toString());
		
		
		while ( rs.next() ) {
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
			resources.add(r);
		}
		BaseDao.release(rs);
		page.setList(resources);

		return page;
	}
	
	private String getKeyWordCondition(String keyword){
		if ( keyword == null || "".equals(keyword) ) {
			return "";
		}
		keyword = Toolkit.removeWildcard(keyword);
		StringBuilder condition = new StringBuilder();
		condition.append(" ( name like '%").append(keyword).append("%' or ")
		         .append(" remark like '%").append(keyword).append("%' or ")
		         .append(" albumname like '%").append(keyword).append("%' ) ");
		return condition.toString();
	}

	@Override
	public String getLoginHtml(ResourceBundle messages,HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<form class=\"navbar-form navbar-right\" role=\"search\" method=\"post\" action=\"search.jsp\">").append("\r\n")
		  .append("  <div class=\"col-lg-12\">").append("\r\n")
		  .append("    <div class=\"input-group\">").append("\r\n")
		  .append("    <input type=\"text\" name=\"").append(Constant.PARAM_KEYWORD).append("\" class=\"form-control\" placeholder=\"").append(messages.getString("index_query_input")).append("\">").append("\r\n")
		  .append("    <span class=\"input-group-btn\">").append("\r\n")
		  .append("      <button class=\"btn btn-default\" type=\"submit\">").append("<span class=\"glyphicon glyphicon-search\" aria-hidden=\"true\"></span>&nbsp;")
		  .append(messages.getString("index_query_btn")).append("</button>").append("\r\n")
		  .append("    </span>").append("\r\n")
		  .append("    </div><!-- /input-group -->").append("\r\n");
		if ( session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT) != null ) {
		  sb.append("  <button type=\"button\" class=\"btn btn-success\" onclick=\"turn('login.jsp')\">")
		    .append("<span class=\"glyphicon glyphicon-wrench\" aria-hidden=\"true\"></span>&nbsp;")
		    .append(messages.getString("index_goto_back_btn")).append("</button>").append("\r\n")
		    .append("  <button type=\"button\" class=\"btn btn-success\" onclick=\"turn('logout.jsp')\">")
		    .append("<span class=\"glyphicon glyphicon-log-out\" aria-hidden=\"true\"></span>&nbsp;")
		    .append(messages.getString("logout_btn")).append("</button>").append("\r\n");
		} else {
			sb.append("  <button type=\"button\" class=\"btn btn-success\" onclick=\"turn('login.jsp')\">")
			  .append("<span class=\"glyphicon glyphicon-log-in\" aria-hidden=\"true\"></span>&nbsp;")
			  .append(messages.getString("index_login_btn")).append("</button>").append("\r\n");
		}
		sb.append("  <button type=\"button\" class=\"btn btn-primary\" onclick=\"turn('help.jsp')\">")
		  .append("<span class=\"glyphicon glyphicon-question-sign\" aria-hidden=\"true\"></span>&nbsp;")
		  .append(messages.getString("index_help_btn")).append("</button>").append("\r\n");
		sb.append("  </div><!-- /.col-lg-12 -->").append("\r\n")
		  .append("</form>").append("\r\n");
	  		
		return sb.toString();
	}

	public static String[] INDEX_TOP_MENU_URL = new String[]{"index.jsp","images.jsp","videos.jsp","audio.jsp","other.jsp"};
	public static String[] INDEX_TOP_MENU_NAME = new String[]{"top_menu_1","top_menu_2","top_menu_3","top_menu_4","top_menu_5"};
	
	@Override
	public String getIndexTopMenuHTML(ResourceBundle messages,int activeItemNumber) throws Exception {
		activeItemNumber = activeItemNumber - 1;
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"nav navbar-nav\">").append("\r\n");
		for ( int i = 0 ; i < INDEX_TOP_MENU_URL.length ; i++ ) {
			if ( i == activeItemNumber ) {
				sb.append("<li class=\"active\">");
			} else {
				sb.append("<li>");
			}
			sb.append("<a class=\"ft2\" href=\"").append(INDEX_TOP_MENU_URL[i]).append("\">");
			if ( i == 0 ) {
				sb.append("<span class=\"glyphicon glyphicon-home\" aria-hidden=\"true\"></span>&nbsp;");
			} else if ( i == 1 ) {
				sb.append("<span class=\"glyphicon glyphicon-picture\" aria-hidden=\"true\"></span>&nbsp;");
			} else if ( i == 2 ) {
				sb.append("<span class=\"glyphicon glyphicon-film\" aria-hidden=\"true\"></span>&nbsp;");
			} else if ( i == 3 ) {
				sb.append("<span class=\"glyphicon glyphicon-music\" aria-hidden=\"true\"></span>&nbsp;");
			} else if ( i == 4 ) {
				sb.append("<span class=\"glyphicon glyphicon-hdd\" aria-hidden=\"true\"></span>&nbsp;");
			} 
			
			sb.append(messages.getString(INDEX_TOP_MENU_NAME[i]));
			if ( i == activeItemNumber ) {
				sb.append("<span class=\"sr-only\">(current)</span>");
			}
			sb.append("</a>")
			  .append("</li>")
			  .append("\r\n");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
}
