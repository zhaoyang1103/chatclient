package com.example.chartclient.zy_java.dao;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/*
 * JDBC通用的dao基类
 * 1、得到连接
 * 2、释放资源
 * 3、通用的增删改查的方法
 */
public class BaseDao {
	private static String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static String USERNAME = "root";
	private static String PASSWORD = "123456";
	private static String url = "jdbc:mysql://104.37.212.97:3306/daikuan";
	private static String URL = "jdbc:mysql://47.94.218.85/qqdata?useUnicode=true&characterEncoding=utf8";
	static{
		try {
			Class.forName(DRIVER_CLASS);
			System.out.println("连接驱动成功");

		} catch (ClassNotFoundException e) {
			System.out.println("连接驱动失败");
			e.printStackTrace();
		}
	}
	public Connection getConnection(){
		Connection conn=null;
		try {
			conn=DriverManager.getConnection(URL,USERNAME,PASSWORD);
			System.out.println("连接数据库成功");

		} catch (SQLException e) {
			System.out.println("连接数据库失败");

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public void closeAll(Connection conn,Statement stmt,ResultSet rs){
		try {
			if (rs!=null) {
				rs.close();
				rs=null;
			}
			if (stmt!=null) {
				stmt.close();
				stmt=null;
			}
			if (conn!=null) {
				conn.close();
				conn=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeAll(Connection conn,Statement stmt){
		this.closeAll(conn, stmt, null);
	}
	public int executeUpdate(String sql,List<Object> params){
		return execute(sql,params);
	}
	public int executeAdd(String sql,List<Object> params){
		return execute(sql, params);
	}

	private int execute(String sql, List<Object> params) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement stmt=null;
		int result=0;

		try {
			conn=getConnection();
			stmt=conn.prepareStatement(sql);
			//得到参数个数
			ParameterMetaData pmd=stmt.getParameterMetaData();
			//设置参数
			for (int i = 0; i < pmd.getParameterCount(); i++) {
				stmt.setObject(i+1, params.get(i));
			}
			int rows=stmt.executeUpdate();
			result=rows;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll(conn, stmt);
		}

		return result;
	}

	public <T> List<T> find(String sql,List<Object> params,Class<T> clazz){
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		List<T> list=new ArrayList<T>();
		try {
			conn=getConnection();
			stmt=conn.prepareStatement(sql);
			ParameterMetaData pmd=stmt.getParameterMetaData();
			for (int i = 0; i < pmd.getParameterCount(); i++) {
				stmt.setObject(i+1, params.get(i));
			}
			rs=stmt.executeQuery();
			ResultSetMetaData rsmd=rs.getMetaData();
			while(rs.next()){
				T obj=clazz.newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String colName=rsmd.getColumnName(i+1);
					Object value=rs.getObject(colName);
					BeanUtils.setProperty(obj,colName,value);
				}
				list.add(obj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally{
			closeAll(conn, stmt, rs);
		}
		return list;
	}

}
