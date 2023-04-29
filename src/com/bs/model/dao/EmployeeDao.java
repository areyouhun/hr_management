package com.bs.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bs.common.Constants;
import com.bs.common.JdbcTemplate;
import com.bs.common.PropertiesGenerator;
import com.bs.common.SearchConditions;
import com.bs.model.dto.Employee;

public class EmployeeDao {
	private static final String INSERT_INTO_EMPLOYEE = "insertIntoEmployee";
	private static final String UPDATE_EMPLOYEE = "updateEmployee";
	private static final String TABLE_EMPLOYEE = "EMPLOYEE";
	private static final String TABLE_SAL_GRADE = "SAL_GRADE";
	private static final String COL_DEPT_CODE = "DEPT_CODE";
	private static final String COL_JOB_CODE = "JOB_CODE";
	private static final String COL_EMP_NAME = "EMP_NAME";
	private static final String COL_EMP_ID = "EMP_ID";
	private static final String COL_SALARY = "SALARY";
	private static final String COL_SAL_LEVEL = "SAL_LEVEL";
	private static final String COL_MIN_SAL = "MIN_SAL";
	private static final String COL_MAX_SAL = "MAX_SAL";
	
	private final Properties sql;
	
	public EmployeeDao() {
		sql = new Properties();
		PropertiesGenerator.load(sql);
	}

	public List<Employee> selectAllEmployees(Connection conn) {
		final List<Employee> employees = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = sql.getProperty(Constants.SELECT_ALL_FROM);
		query = query.replace(Constants.TABLE, TABLE_EMPLOYEE);
		
		try {
			pstmt = conn.prepareStatement(query);
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
	
	public List<Employee> selectEmployeesByIdentifiers(Connection conn, List<String> identifiers, String column) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		String placeHolders = String.join(",", Collections.nCopies(identifiers.size(), "?"));
		if (placeHolders.isEmpty()) {
			return employees;
		}
		
		query = replace(query, TABLE_EMPLOYEE, column, Constants.IN);
		query = query.replace("?", "(" + placeHolders + ")");
		
		try {
			pstmt = conn.prepareStatement(query);
			int index = 1;
			for (String identifier : identifiers) {
				pstmt.setString(index++, identifier);
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
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, TABLE_EMPLOYEE, COL_EMP_NAME, Constants.LIKE);
		
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
	
	public List<Employee> selectEmployeesBySalary(Connection conn, int salary, SearchConditions condition) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Employee> employees = new ArrayList<>();
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, TABLE_EMPLOYEE, COL_SALARY, condition.getSign());
		
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
	
	public int insertIntoEmployee(Connection conn, Employee employee, Map<String, String> codes) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(INSERT_INTO_EMPLOYEE);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, employee.getEmpId());
			pstmt.setString(2, employee.getEmpName());
			pstmt.setString(3, employee.getEmpNo());
			pstmt.setString(4, employee.getEmail());
			pstmt.setString(5, employee.getPhone());
			pstmt.setString(6, codes.get(COL_DEPT_CODE));
			pstmt.setString(7, codes.get(COL_JOB_CODE));
			pstmt.setString(8, employee.getSalLevel());
			pstmt.setInt(9, employee.getSalary());
			pstmt.setDouble(10, employee.getBonus());
			if (employee.getManagerId().equals("X")) {
				pstmt.setNull(11, Types.NULL);
			} else {
				pstmt.setString(11, employee.getManagerId());
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public int updateEmployeeBy(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(UPDATE_EMPLOYEE);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, employee.getJobCode());
			pstmt.setString(2, employee.getDeptCode());
			pstmt.setInt(3, employee.getSalary());
			pstmt.setString(4, employee.getSalLevel());
			pstmt.setString(5, employee.getPhone());
			pstmt.setString(6, employee.getEmail());
			pstmt.setString(7, employee.getEmpId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public void updateDeptCode(Employee employee, String deptCode) {
		employee.setDeptCode(deptCode);
	}
	
	public void updateJobCode(Employee employee, String jobCode) {
		employee.setJobCode(jobCode);
	}
	
	public void updateSalaryLevel(Connection conn, Employee employee) {
		employee.setSalLevel(findSalaryLevelBy(conn, employee));
	}
	
	private String findSalaryLevelBy(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String level = "";
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, TABLE_SAL_GRADE, 
						String.valueOf(employee.getSalary()), 
						String.format(Constants.BETWEEN, COL_MIN_SAL, COL_MAX_SAL));
//		query = query.replace(" ?", "");
		query = query.replace("?", "");
		
		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				level = rs.getString(COL_SAL_LEVEL);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return level;
	}
	
	public int deleteFromEmployeeBy(Connection conn, String empId) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(Constants.DELETE_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, TABLE_EMPLOYEE, COL_EMP_ID, Constants.EQUAL);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, empId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String replace(String query, String table, 
						String column, String operator) {
		query = query.replace(Constants.TABLE, table);
		query = query.replace(Constants.COL, column);
		return query.replace(Constants.OPERATOR, operator);
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
