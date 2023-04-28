package com.bs.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bs.common.JdbcTemplate;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;

public class DepartmentDao {
	private static final String SQL_PATH = "/sql/board/board_sql.properties";
	private static final String SELECT_ALL_FROM_DEPT = "selectAllFromDept";
	private static final String INSERT_INTO_DEPARTMENT = "insertIntoDepartments";
	private static final String UPDATE_DEPARTMENT = "updateDepartment";
	private static final String DELETE_FROM = "deleteFrom";
	private static final String WHERE = "where";
	private static final String COL = "#COL";
	private static final String TABLE_NAME = "DEPARTMENT";
	private static final String SYNTAX = "#SYNTAX";
	private static final String COL_DEPT_ID = "DEPT_ID";
	private static final String COL_DEPT_TITLE = "DEPT_TITLE";
	private static final String COL_LOCATION_ID = "LOCATION_ID";
	private static final String EQUAL = "=";
	private static final String LIKE = "LIKE";
	
	private final Properties sql;
	
	public DepartmentDao() {
		sql = new Properties();
		loadProperties(sql);
	}
	
	private void loadProperties(Properties properties) {
		final String path = EmployeeDao.class.getResource(SQL_PATH).getPath();
		try (FileReader fileReader = new FileReader(path)) {
			properties.load(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void matchWithEmployee(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = sql.getProperty(SELECT_ALL_FROM_DEPT) + " " + sql.getProperty(WHERE);
		query = query.replace(COL, COL_DEPT_ID);
		query = query.replace(SYNTAX, EQUAL);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, employee.getDeptCode());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				employee.setDepartment(generateDepartment(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
	}
	
	public List<String> findDeptIdByDeptTitle(Connection conn, String deptTitle) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> deptIds = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_DEPT) + " " + sql.getProperty(WHERE);
		query = query.replace(COL, COL_DEPT_TITLE);
		query = query.replace(SYNTAX, LIKE); 
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + deptTitle + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				deptIds.add(rs.getString("DEPT_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return deptIds;
	}
	
	public int insertDepartment(Connection conn, Department department) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(INSERT_INTO_DEPARTMENT);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, department.getDeptId());
			pstmt.setString(2, department.getDeptTitle());
			pstmt.setString(3, department.getLocationId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public int updateDepartment(Connection conn, Department department) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(UPDATE_DEPARTMENT);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, department.getDeptTitle());
			pstmt.setString(2, department.getDeptId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public int deleteDepartment(Connection conn, String deptId) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(DELETE_FROM) + " " + sql.getProperty(WHERE);
		query = query.replace("#TABLE", TABLE_NAME);
		query = query.replace("#COL", COL_DEPT_ID);
		query = query.replace("#SYNTAX", EQUAL);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, deptId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	private Department generateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getString(COL_DEPT_ID),
							rs.getString(COL_DEPT_TITLE),
							rs.getString(COL_LOCATION_ID));
	}
}
