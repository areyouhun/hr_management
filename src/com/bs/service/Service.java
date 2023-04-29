package com.bs.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.bs.common.JdbcTemplate;
import com.bs.common.SearchConditions;
import com.bs.model.dao.DepartmentDao;
import com.bs.model.dao.EmployeeDao;
import com.bs.model.dao.JobDao;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;

public class Service {
	private static final String COL_DEPT_CODE = "DEPT_CODE";
	private static final String COL_DEPT_TITLE = "DEPT_TITLE";
	private static final String COL_JOB_CODE = "JOB_CODE";
	private static final String COL_JOB_NAME = "JOB_NAME";
	
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
		matchWithDeptAndJob(conn, employees);
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public List<Employee> selectFromEmployeeByDeptTitle(String deptTitle) {
		return executeQuery(deptTitle, (conn, title) -> 
						employeeDao.selectEmployeesByIdentifiers(conn, departmentDao.findDeptIdBy(conn, title), 
						COL_DEPT_CODE));
	}
	
	public List<Employee> selectFromEmployeeByJobName(String jobName) {
		return executeQuery(jobName, (conn, name) -> 
						employeeDao.selectEmployeesByIdentifiers(conn, jobDao.findJobCodeBy(conn, name), 
						COL_JOB_CODE));
	}
	
	public List<Employee> selectFromEmployeeByName(String name) {
		return executeQuery(name, 
						(conn, $name) -> employeeDao.selectEmployeesByName(conn, name));
	}
	
	public List<Employee> selectFromEmployeeBySalary(int salary, SearchConditions condition) {
		Connection conn = JdbcTemplate.getConnection();
		List<Employee> employees = employeeDao.selectEmployeesBySalary(conn, salary, condition);
		matchWithDeptAndJob(conn, employees);
		JdbcTemplate.close(conn);
		return employees;
	}
	
	public int insertIntoEmployee(Employee employee, Map<String, String> titles) {
		Connection conn = JdbcTemplate.getConnection();
		int result = 0;
		employeeDao.updateSalaryLevel(conn, employee);
		Map<String, String> codes = Map.of(COL_DEPT_CODE, departmentDao.findDeptIdBy(conn, titles.get(COL_DEPT_TITLE)).get(0), 
										COL_JOB_CODE, jobDao.findJobCodeBy(conn, titles.get(COL_JOB_NAME)).get(0));
		result = employeeDao.insertIntoEmployee(conn, employee, codes);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int updateEmployeeBy(Employee employee, Map<String, String> titles) {
		Connection conn = JdbcTemplate.getConnection();
		int result = 0;
		Map<String, String> codes = Map.of(COL_DEPT_CODE, departmentDao.findDeptIdBy(conn, titles.get(COL_DEPT_TITLE)).get(0), 
										COL_JOB_CODE, jobDao.findJobCodeBy(conn, titles.get(COL_JOB_NAME)).get(0));
		
		employeeDao.updateSalaryLevel(conn, employee);
		employeeDao.updateDeptCode(employee, codes.get(COL_DEPT_CODE));
		employeeDao.updateJobCode(employee, codes.get(COL_JOB_CODE));
		result = employeeDao.updateEmployeeBy(conn, employee);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	public int deleteFromEmployeeBy(String empId) {
		return executeUpdate(empId, (conn, id) -> employeeDao.deleteFromEmployeeBy(conn, id));
	}
	
	public int insertIntoDepartment(Department department) {
		return executeUpdate(department, (conn, dept) -> departmentDao.insertIntoDepartment(conn, dept));
	}
	
	public int updateDepartmentBy(Department department) {
		return executeUpdate(department, (conn, dept) -> departmentDao.updateDepartmentBy(conn, dept));
	}
	
	public int deleteFromDepartmentBy(String deptCode) {
		return executeUpdate(deptCode, (conn, code) -> departmentDao.deleteFromDepartmentBy(conn, code));
	}
	
	public int insertIntoJob(Job job) {
		return executeUpdate(job, (conn, $job) -> jobDao.insertIntoJob(conn, $job));
	}
	
	public int updateJobBy(Job job) {
		return executeUpdate(job, (conn, $job) -> jobDao.updateJobBy(conn, $job));
	}
	
	public int deleteFromJobBy(String jobCode) {
		return executeUpdate(jobCode, (conn, code) -> jobDao.deleteFromJobBy(conn, code));
	}
	
	private <T> List<Employee> executeQuery(T t, BiFunction<Connection, T, List<Employee>> dmlQuery) {
		Connection conn = JdbcTemplate.getConnection();
		List<Employee> employees = dmlQuery.apply(conn, t);
		matchWithDeptAndJob(conn, employees);
		JdbcTemplate.close(conn);
		return employees;
	}
	
	private <T> int executeUpdate(T t, BiFunction<Connection, T, Integer> dmlQuery) {
		Connection conn = JdbcTemplate.getConnection();
		int result = dmlQuery.apply(conn, t);
		
		if (result > 0) {
			JdbcTemplate.commit(conn);
		} else {
			JdbcTemplate.rollback(conn);
		}
		JdbcTemplate.close(conn);
		return result;
	}
	
	private void matchWithDeptAndJob(Connection conn, List<Employee> employees) {
		employees.forEach(employee -> {
			departmentDao.matchWithEmployee(conn, employee);
			jobDao.matchWithEmployee(conn, employee);
		});
	}
}
