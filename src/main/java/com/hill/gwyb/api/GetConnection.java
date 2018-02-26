package com.hill.gwyb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;


public  class GetConnection {
	
	private Connection conn=null;
	private void getConnection() throws SQLException, ClassNotFoundException {
		
		ResourceBundle resource = ResourceBundle.getBundle("db");
		
		String userName =resource.getString("user");
		String passWord = resource.getString("password");
		String url = resource.getString("jdbcUrl");
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection(url,userName,passWord);
		
	}
	
	public Connection getCon() throws ClassNotFoundException, SQLException{
		if(this.conn==null){
			this.getConnection();
		}
		return this.conn;
	}
	
	public void closeCon(){
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
