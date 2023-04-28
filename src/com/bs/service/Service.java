package com.bs.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.bs.common.JdbcTemplate;
import com.bs.common.SalaryConditions;
import com.bs.model.dao.DepartmentDao;
import com.bs.model.dao.EmployeeDao;
import com.bs.model.dao.JobDao;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;

public class Service {
	private static Service service = new Service();
	
	private final EmployeeDao employeeDao;
	private final DepartmentDao departmentDao;
	private final JobDao jobDao;
	
	private Service() {
		employeeDao = new EmployeeDao();
		departmentDao = new DepartmentDao();
		jobDao = new JobDao();
	}
	
	public static Service getService() {
		return service;
	}
	
	public List<Employee> selectAllFromEmployee() {
		Connection conn = JdbcTemplate.getConnection();
		List<Employee> employees = employeeDao.selectAllEmployees(conn);
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public List<Employee> selectFromEmployeeByDeptTitle(String deptTitle) {
		Connection conn = JdbcTemplate.getConnection();
		List<String> deptCodes = departmentDao.findDeptIdByDeptTitle(conn, deptTitle);
		List<Employee> employees = employeeDao.selectEmployeesBydeptCode(conn, deptCodes);
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public List<Employee> selectFromEmployeeByJobName(String jobName) {
		Connection conn = JdbcTemplate.getConnection();
		List<String> jobCodes = jobDao.findJobCodeByJobName(conn, jobName);
		List<Employee> employees = employeeDao.selectEmployeesByJobCodes(conn, jobCodes);
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public List<Employee> selectFromEmployeeByName(String name) {
		Connection conn = JdbcTemplate.getConnection();
		List<Employee> employees = employeeDao.selectEmployeesByName(conn, name);
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public List<Employee> selectFromEmployeeBySalary(int salary, SalaryConditions condition) {
		Connection conn = JdbcTemplate.getConnection();
		List<Employee> employees = employeeDao.selectEmployeesBySalary(conn, salary, condition);
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public int insertIntoEmployee(Employee employee, Map<String, String> titles) {
		Connection conn = JdbcTemplate.getConnection();
		int result = 0;
		employeeDao.updateSalaryLevel(conn, employee);
		Map<String, String> codes = Map.of("DEPT_CODE", departmentDao.findDeptIdByDeptTitle(conn, titles.get("DEPT_TITLE")).get(0), 
										"JOB_CODE", jobDao.findJobCodeByJobName(conn, titles.get("JOB_NAME")).get(0));
		result = employeeDao.insertEmployee(conn, employee, codes);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int updateEmployee(Employee employee, Map<String, String> titles) {
		Connection conn = JdbcTemplate.getConnection();
		int result = 0;
		Map<String, String> codes = Map.of("DEPT_CODE", departmentDao.findDeptIdByDeptTitle(conn, titles.get("DEPT_TITLE")).get(0), 
										"JOB_CODE", jobDao.findJobCodeByJobName(conn, titles.get("JOB_NAME")).get(0));
		
		employeeDao.updateSalaryLevel(conn, employee);
		employeeDao.updateDeptCode(conn, employee, codes.get("DEPT_CODE"));
		employeeDao.updateJobCode(conn, employee, codes.get("JOB_CODE"));
		result = employeeDao.updateEmployee(conn, employee);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int deleteEmployee(String empId) {
		Connection conn = JdbcTemplate.getConnection();
		int result = employeeDao.deleteEmployee(conn, empId);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int insertIntoDepartment(Department department) {
		Connection conn = JdbcTemplate.getConnection();
		int result = departmentDao.insertDepartment(conn, department);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int updateDepartment(Department department) {
		Connection conn = JdbcTemplate.getConnection();
		int result = departmentDao.updateDepartment(conn, department);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int deleteDepartment(String deptTitle) {
		Connection conn = JdbcTemplate.getConnection();
		List<String> deptCodes = departmentDao.findDeptIdByDeptTitle(conn, deptTitle);
		
		int commitCount = 0;
		for (String deptCode : deptCodes) {
			if (departmentDao.deleteDepartment(conn, deptCode) > 0) {
				commitCount++;
				JdbcTemplate.commit(conn);
			} else {
				JdbcTemplate.rollback(conn);
			}
		}
		JdbcTemplate.close(conn);
		return commitCount;
	}
}
