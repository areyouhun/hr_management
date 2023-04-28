package com.bs.service;

import java.sql.Connection;
import java.util.List;

import com.bs.common.JdbcTemplate;
import com.bs.common.SalaryConditions;
import com.bs.model.dao.DepartmentDao;
import com.bs.model.dao.EmployeeDao;
import com.bs.model.dao.JobDao;
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
}
