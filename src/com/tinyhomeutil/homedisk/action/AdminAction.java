/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.action;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinyhomeutil.homedisk.dao.BaseDao;
import com.tinyhomeutil.homedisk.dao.Pages;
import com.tinyhomeutil.homedisk.pojo.AdminOverView;
import com.tinyhomeutil.homedisk.pojo.Resource;
import com.tinyhomeutil.homedisk.pojo.User;
import com.tinyhomeutil.homedisk.util.Toolkit;

/**
 * @author jack www.tinyhomeutil.com
 */
public class AdminAction implements IAdmin {	
	
	@Override
	public AdminOverView overview(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) as items from home_user ")
		  .append("union all ")
		  .append("select count(1) as items from home_resource where deleted = 0 ")
		  .append("union all ")
		  .append("select count(1) as items from home_resource where deleted = 1");
		
		String sql = sb.toString();
		ResultSet rs = BaseDao.query(sql);
		AdminOverView obj = new AdminOverView();
		
		rs.next();
		obj.setUserNumber(rs.getInt("items"));
		rs.next();
		obj.setResourceNumber(rs.getInt("items"));
		rs.next();
		obj.setDeletedResourceNumber(rs.getInt("items"));
		rs.close();
		
		sql = "select private_visit from home_user limit 0,1";
		rs = rs.getStatement().executeQuery(sql);
		rs.next();
		obj.setPrivateVisit(rs.getInt(Constant.PARAM_PRIVATE_VISIT)==0?false:true);
		
		BaseDao.release(rs);
		
		return obj;
	}

	@Override
	public void setVisitStatus(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int value = Integer.valueOf(request.getParameter(Constant.PARAM_PRIVATE_VISIT));
		if ( value != 0 ) {
			value = 1;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("update home_user set private_visit = ")
		  .append(value);
		BaseDao.execute(sb.toString());
		response.sendRedirect("adminoverview.jsp");
	}

	@Override
	public void updatePwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pwd1 = request.getParameter(Constant.PARAM_PWD1);
		String pwd2 = request.getParameter(Constant.PARAM_PWD2);
		int userId= (Integer)session.getAttribute(Constant.PARAM_SESSION_USER_ID);
		String link = null;
		String nextlink = "adminpwd.jsp";
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

	@Override
	public Pages<List<User>> getUsers(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pages<List<User>> page = new Pages<List<User>>();
		List<User> users = new ArrayList<User>();
		User u = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from home_user where admin = 0");
		ResultSet rs = BaseDao.query(sb.toString());
		while( rs.next() ) {
			u = new User();
			u.setId(rs.getInt("id"));
			u.setName(rs.getString("name"));
			u.setLocked(rs.getInt("locked"));
			u.setAdmin(0);
			u.setPrivateVisit(rs.getInt("private_visit"));
			users.add(u);
		}
		page.setList(users);
		
		return page;
	}

	@Override
	public void createUser(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String link = null;
		String nextlink = "adminusersmgr.jsp";
		boolean isNull = false;
		
		String userName = request.getParameter(Constant.PARAM_USER_NAME);
		isNull = Toolkit.isNull(userName, nextlink, "admin_user_name_null_err_notice",response);
		if ( isNull ) return;
		userName = Toolkit.htmlTransfer(userName);
		
		String pwd = request.getParameter(Constant.PARAM_PWD);
		isNull = Toolkit.isNull(pwd, nextlink, "admin_user_pwd_null_err_notice",response);
		if ( isNull ) return;
		pwd = Toolkit.md5(pwd);
				
		ResultSet rs = BaseDao.query("select private_visit from home_user limit 0,1");
		rs.next();
		int privateVisit = rs.getInt(Constant.PARAM_PRIVATE_VISIT);		
		BaseDao.release(rs);
		
		StringBuilder sb = new StringBuilder();
		sb.append("insert into home_user(name,pwd,locked,admin,private_visit) values (")
		  .append("'").append(userName).append("'").append(",")
		  .append("'").append(pwd).append("'").append(",")
		  .append(0).append(",")
		  .append(0).append(",")
		  .append(privateVisit)
		  .append(")");
		BaseDao.execute(sb.toString());
		link = Toolkit.successLink(nextlink, "admin_user_add_success_notice");
		response.sendRedirect(link);
	}

	@Override
	public void resetPwd(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int id = Integer.valueOf(request.getParameter(Constant.PARAM_USER_ID));
		String pwd = Toolkit.md5("1");
		StringBuilder sb = new StringBuilder();
		sb.append("update home_user set pwd = ")
		  .append("'").append(pwd).append("'")
		  .append(" where id = ").append(id);
		BaseDao.execute(sb.toString());
		response.sendRedirect(Toolkit.successLink("adminusersmgr.jsp", "admin_user_reset_pwd_success_notice"));
	}

	@Override
	public void lockUser(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int id = Integer.valueOf(request.getParameter(Constant.PARAM_USER_ID));
		int locked = Integer.valueOf(request.getParameter(Constant.PARAM_USER_LOCKED));
		if ( locked != 0 ) {
			locked = 0;
		} else {
			locked = 1;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("update home_user set locked = ").append(locked).append(" where id = ").append(id);
		BaseDao.execute(sb.toString());
		String notice = null;
		if ( locked == 0 ) {
			notice = "admin_user_unlock_success_notice";
		} else {
			notice = "admin_user_lock_success_notice";
		}
		response.sendRedirect(Toolkit.successLink("adminusersmgr.jsp", notice));
	}

	@Override
	public void delUser(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int id = Integer.valueOf(request.getParameter(Constant.PARAM_USER_ID));
		StringBuilder sb = new StringBuilder();
		sb.append("delete from home_user where id = ").append(id);
		BaseDao.execute(sb.toString());
		response.sendRedirect(Toolkit.successLink("adminusersmgr.jsp", "admin_user_del_success_notice"));
		
	}


	@Override
	public Pages<List<Resource>> getResources(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pages<List<Resource>> page = new Pages<List<Resource>>();
		List<Resource> resources = new ArrayList<Resource>();
		Resource r = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("select id,name,username from home_resource where deleted = 1");
		ResultSet rs = BaseDao.query(sb.toString());
		while( rs.next() ) {
			r = new Resource();
			r.setId(rs.getInt("id"));
			r.setName(rs.getString("name"));
			r.setUserName(rs.getString("username"));
			resources.add(r);
		}
		page.setList(resources);
		return page;
	}
	
	@Override
	public void clearResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseDao.execute("delete from home_resource where deleted = 1");
		response.sendRedirect(Toolkit.successLink("admindelfile.jsp", "admin_res_clear_success_notice"));
	}

	@Override
	public void recoverResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int id = Integer.valueOf(request.getParameter(Constant.PARAM_RESOURCE_ID));
		StringBuilder sb = new StringBuilder();
		sb.append("update home_resource set deleted = 0 where id = ").append(id);
		BaseDao.execute(sb.toString());
		response.sendRedirect(Toolkit.successLink("admindelfile.jsp", "admin_res_recover_success_notice"));
	}

	@Override
	public void delResource(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int id = Integer.valueOf(request.getParameter(Constant.PARAM_RESOURCE_ID));
		StringBuilder sb = new StringBuilder();
		sb.append("delete from home_resource where id = ").append(id);
		BaseDao.execute(sb.toString());
		response.sendRedirect(Toolkit.successLink("admindelfile.jsp", "admin_res_del_success_notice"));
		
	}

}
