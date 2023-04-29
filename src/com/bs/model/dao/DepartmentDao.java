package com.bs.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bs.common.JdbcTemplate;
import com.bs.common.PropertiesGenerator;
import com.bs.common.Constants;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;

public class DepartmentDao {
	private static final String INSERT_INTO_DEPARTMENT = "insertIntoDepartment";
	private static final String UPDATE_DEPARTMENT = "updateDepartment";
	private static final String TABLE_DEPARTMENT = "DEPARTMENT";
	private static final String COL_DEPT_ID = "DEPT_ID";
	private static final String COL_DEPT_TITLE = "DEPT_TITLE";
	private static final String COL_LOCATION_ID = "LOCATION_ID";
	
	private final Properties sql;
	
	public DepartmentDao() {
		sql = new Properties();
		PropertiesGenerator.load(sql);
	}
	
	public void matchWithEmployee(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_DEPT_ID, Constants.EQUAL);
		
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
	
	public List<String> findDeptIdBy(Connection conn, String deptTitle) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> deptIds = new ArrayList<>();
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_DEPT_TITLE, Constants.LIKE);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + deptTitle + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				deptIds.add(rs.getString(COL_DEPT_ID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return deptIds;
	}
	
	public int insertIntoDepartment(Connection conn, Department department) {
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
	
	public int updateDepartmentBy(Connection conn, Department department) {
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
	
	public int deleteFromDepartmentBy(Connection conn, String deptId) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(Constants.DELETE_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_DEPT_ID, Constants.EQUAL);
		
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
	
	private String replace(String query, String column, String operator) {
		query = query.replace(Constants.TABLE, TABLE_DEPARTMENT);
		query = query.replace(Constants.COL, column);
		return query.replace(Constants.OPERATOR, operator);
	}
	
	private Department generateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getString(COL_DEPT_ID),
							rs.getString(COL_DEPT_TITLE),
							rs.getString(COL_LOCATION_ID));
	}
}
