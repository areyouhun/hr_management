package com.bs.service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcTemplate {
	private static final String KEY_DRIVER = "driver";
	private static final String KEY_URL = "url";
	private static final String KEY_USER = "user";
	private static final String KEY_PW = "pw";

	public static Connection getConnection() {
		final Properties driver = new Properties();
		final String path = JdbcTemplate.class.getResource("/driver.properties").getPath();
		Connection conn = null;
		
		try (FileReader fileReader = new FileReader(path)) {
			driver.load(fileReader);
			Class.forName(driver.getProperty(KEY_DRIVER));
			
			conn = DriverManager.getConnection(
					driver.getProperty(KEY_URL), 
					driver.getProperty(KEY_USER),
					driver.getProperty(KEY_PW));
			conn.setAutoCommit(false);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn) {
		try {
			if (conn != null & !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
		try {
			if (stmt != null & !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if (rs != null & !rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void commit(Connection conn) {
		try {
			if (conn != null & !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if (conn != null & !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
