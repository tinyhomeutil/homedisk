/**
 * www.tinyhomeutil.com 
 */
package com.tinyhomeutil.homedisk.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author jack www.tinyhomeutil.com
 */
public class BaseDao {

	private static ComboPooledDataSource pooledDataSource = new ComboPooledDataSource(); 
	
	public static Connection getConnection() { 
	    try { 
	        return pooledDataSource.getConnection(); 
	    } catch (SQLException e) { 
	        throw new RuntimeException(e); 
	    } 
	} 
	
	public static void execute(String sql) throws Exception{
		Connection conn = null;
		try {
			conn = BaseDao.getConnection();
			conn.createStatement().execute(sql);
			if ( !conn.getAutoCommit() )
				conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( null != conn ) {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static ResultSet query(String sql) throws Exception{
		return BaseDao.getConnection().createStatement().executeQuery(sql);
	}
	
	public static void release(ResultSet rs) throws Exception{
		if ( rs != null ) {
			rs.close();
			rs.getStatement().getConnection().close();
			rs = null;
		}
	}
	
	public Pages getPages(Pages pages,String tableName) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		String sql = "select count(1) as items from " + tableName;
		try {
			conn = BaseDao.getConnection();
			rs = conn.createStatement().executeQuery(sql);
			while ( rs.next() ) {
				pages.setItems(rs.getInt("items"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( null != rs ) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if ( null != conn ) {
				try {
					conn.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		}
		return pages;
	}
	
	public static void main(String[] args){
		
	}
}
