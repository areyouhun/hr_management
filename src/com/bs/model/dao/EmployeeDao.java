package com.bs.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.bs.common.JdbcTemplate;
import com.bs.common.SalaryConditions;
import com.bs.model.dto.Employee;

public class EmployeeDao {
	private static final String SQL_PATH = "/sql/board/board_sql.properties";
	private static final String SELECT_ALL_FROM_EMPLOYEE = "selectAllFromEmployee";
	private static final String WHERE = "where";
	private static final String COL = "#COL";
	private static final String SYNTAX = "#SYNTAX";
	private static final String COL_DEPT_CODE = "DEPT_CODE";
	private static final String COL_JOB_CODE = "JOB_CODE";
	private static final String COL_EMP_NAME = "EMP_NAME";
	private static final String COL_SALARY = "SALARY";
	private static final String EQUAL = "=";
	private static final String LIKE = "LIKE";
	private static final String IN = "IN";
	
	private final Properties sql;
	
	public EmployeeDao() {
		sql = new Properties();
		loadProperties(sql);
	}
	
	private void loadProperties(Properties prop) {
		final String path = EmployeeDao.class.getResource(SQL_PATH).getPath();
		try (FileReader fileReader = new FileReader(path)) {
			prop.load(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Employee> selectAllEmployees(Connection conn) {
		final List<Employee> employees = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql.getProperty(SELECT_ALL_FROM_EMPLOYEE));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				employees.add(generateEmployee(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return employees;
	}
	
	public List<Employee> selectEmployeesBydeptCode(Connection conn, List<String> deptCodes) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_EMPLOYEE) + " " + sql.getProperty(WHERE);
		query = query.replace(COL, COL_DEPT_CODE);
		query = query.replace(SYNTAX, IN);
		query = query.replace("?", "(");
		String placeHolders = String.join(",", Collections.nCopies(deptCodes.size(), "?"));
		query = query + placeHolders + ")";
		
		try {
			pstmt = conn.prepareStatement(query);
			int index = 1;
			for (String deptCode : deptCodes) {
				pstmt.setString(index++, deptCode);
			}
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				employees.add(generateEmployee(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return employees;
	}
	
	public List<Employee> selectEmployeesByJobCodes(Connection conn, List<String>jobCodes) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_EMPLOYEE) + " " + sql.getProperty(WHERE);
		String placeHolders = String.join(",", Collections.nCopies(jobCodes.size(), "?"));
		query = query.replace("#COL", COL_JOB_CODE);
		query = query.replace("#SYNTAX", IN);
		query = query.replace("?", "(" + placeHolders + ")");
		
		try {
			pstmt = conn.prepareStatement(query);
			int index = 1;
			for (String jobCode : jobCodes) {
				pstmt.setString(index++, jobCode);
			}
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				employees.add(generateEmployee(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return employees;
	}
	
	public List<Employee> selectEmployeesByName(Connection conn, String name) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_EMPLOYEE) + " " + sql.getProperty(WHERE);
		query = query.replace("#COL", COL_EMP_NAME);
		query = query.replace("#SYNTAX", LIKE);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				employees.add(generateEmployee(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return employees;
	}
	
	public List<Employee> selectEmployeesBySalary(Connection conn, int salary, SalaryConditions condition) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_EMPLOYEE) + " " + sql.getProperty(WHERE);
		query = query.replace("#COL", COL_SALARY);
		query = query.replace("#SYNTAX", condition.getSign());
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, salary);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				employees.add(generateEmployee(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		
		return employees;
	} 

	private Employee generateEmployee(ResultSet rs) throws SQLException {
		return new Employee.Builder()
							.empId(rs.getString("EMP_ID"))
							.empName(rs.getString("EMP_NAME"))
							.empNo(rs.getString("EMP_NO"))
							.email(rs.getString("EMAIL"))
							.phone(rs.getString("PHONE"))
							.deptCode(rs.getString("DEPT_CODE"))
							.jobCode(rs.getString("JOB_CODE"))
							.salLevel(rs.getString("SAL_LEVEL"))
							.salary(rs.getInt("SALARY"))
							.bonus(rs.getDouble("BONUS"))
							.managerId(rs.getString("MANAGER_ID"))
							.hireDate(rs.getDate("HIRE_DATE"))
							.entDate(rs.getDate("ENT_DATE"))
							.entYn(rs.getString("ENT_YN").charAt(0))
							.build();
	}
}
