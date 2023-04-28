package com.bs.service;

import java.sql.Connection;
import java.util.List;

import com.bs.common.JdbcTemplate;
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
}
