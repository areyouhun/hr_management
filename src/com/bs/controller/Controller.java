package com.bs.controller;

import java.util.List;
import java.util.Map;

import com.bs.common.SalaryConditions;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;
import com.bs.service.Service;
import com.bs.view.InputView;
import com.bs.view.OutputView;

public class Controller {
	private static Controller controller = new Controller();
	
	private final Service service;
	private final InputView inputView;
	private final OutputView outputView;
	
	private Controller() {
		service = Service.getService();
		inputView = InputView.getInputView();
		outputView = OutputView.getOutputView();
	}
	
	public static Controller getController() {
		return controller;
	}
	
	public void start() {
		while (true) {
			switch (inputView.readMainMenu()) {
			case 1:
				outputView.printEmployees(service.selectAllFromEmployee());
				break;
			case 2:
				submenuForEmployee();
				break;
			case 3:
				inputView.header("======== 신규 사원 등록 ========");
				Employee newEmployee = inputView.readEmployee();
				Map<String, String> titles = inputView.readTitles("부서명: ", "직책명 : ");
				outputView.printResult(service.insertIntoEmployee(newEmployee, titles), "신규 사원 등록 성공", "신규 사원 등록 실패");
				break;
			case 4:
				inputView.header("======== 사원 정보 수정 ========");
				Employee employeeToBeUpdated = inputView.readEmployeeToBeUpdated();
				Map<String, String> titlesToBeRenamed = inputView.readTitles("새로운 부서명: ", "새로운 직책명 : ");
				outputView.printResult(service.updateEmployee(employeeToBeUpdated, titlesToBeRenamed), "사원 정보 수정 성공", "사원 정보 수정 실패");
				break;
			case 5:
				inputView.header("======== 사원 삭제 ========");
				Employee employeeToBeDeleted = inputView.readEmployeeToBeUpdated();
				Map<String, String> titlesToBeRenamed2 = inputView.readTitles("변경된 부서명: ", "변경된 직책명 : ");
				outputView.printResult(service.insertIntoEmployee(employeeToBeDeleted, titlesToBeRenamed2), "사원 정보 수정 성공", "사원 정보 수정 실패");
				break;
			case 6:
				submenuForDept();
				break;
			case 7:
				submenuForJob();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("입력값이 올바르지 않습니다. 다시 입력해주세요.");
			}
		}
	}
	
	public void submenuForEmployee() {
		while (true) {
			switch (inputView.readSubMenuForEmployee()) {
			case 1:
				List<Employee> employeesByDept = service.selectFromEmployeeByDeptTitle(inputView.readDeptTitle("찾고자 하는 사원의 부서명을 입력해주세요 : "));
				outputView.printEmployees(employeesByDept);
				break;
			case 2:
				List<Employee> employeesByJob = service.selectFromEmployeeByJobName(inputView.readJobName("찾고자 하는 사원의 직책명을 입력해주세요 : "));
				outputView.printEmployees(employeesByJob);
				break;
			case 3:
				List<Employee> employeesByName = service.selectFromEmployeeByName(inputView.readName());
				outputView.printEmployees(employeesByName);
				break;
			case 4:
				int salary = inputView.readSalary();
				SalaryConditions searchCondition = getSalaryCondition(inputView.readSearchCondition());
				List<Employee> employeesBySalary = service.selectFromEmployeeBySalary(salary, searchCondition);
				outputView.printEmployees(employeesBySalary);
				break;
			case 0:
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("입력값이 올바르지 않습니다. 다시 입력해주세요.");
			}
		}
	}
	
	public void submenuForDept() {
		while (true) {
			switch (inputView.readSubMenuForDept()) {
			case 1:
				inputView.header("======== 신규 부서 등록 ========");
				Department newDepartment = inputView.readDepartment();
				outputView.printResult(service.insertIntoDepartment(newDepartment), "신규 부서 등록 성공", "신규 부서 등록 실패");
				break;
			case 2:
				inputView.header("======== 부서 정보 수정 ========");
				Department departmentToBeUpdated = inputView.readDepartmentToBeUpdated();
				outputView.printResult(service.updateDepartment(departmentToBeUpdated), "부서 정보 수정 성공", "부서 정보 수정 실패");
				break;
			case 3:
				inputView.header("========== 부서 삭제 ==========");
				String deptTitle = inputView.readDeptTitle("삭제하려는 부서명을 입력해주세요 : ");
				outputView.printResult(service.deleteFromDepartment(deptTitle), "부서 삭제 성공", "부서 삭제 실패");
				break;
			case 0:
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("입력값이 올바르지 않습니다. 다시 입력해주세요.");
			}
		}
	}
	
	public void submenuForJob() {
		while (true) {
			switch (inputView.readSubMenuForJob()) {
			case 1:
				inputView.header("======== 신규 직책 등록 ========");
				Job newJob = inputView.readJob();
				outputView.printResult(service.insertIntoJob(newJob), "신규 직책 등록 성공", "신규 직책 등록 실패");
				break;
			case 2:
				inputView.header("======== 직책 정보 수정 ========");
				Job JobToBeUpdated = inputView.readJobToBeUpdated();
				outputView.printResult(service.updateJob(JobToBeUpdated), "직책 정보 수정 성공", "직책 정보 수정 실패");
				break;
			case 3:
				inputView.header("========== 직책 삭제 ==========");
				String jobName = inputView.readJobName("삭제하려는 직책명을 입력해주세요 : ");
				outputView.printResult(service.deleteFromJob(jobName), "직책 삭제 성공", "직책 삭제 실패");
				break;
			case 0:
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("입력값이 올바르지 않습니다. 다시 입력해주세요.");
			}
		}
	}
	
	private SalaryConditions getSalaryCondition(String letter) {
		try {
			return SalaryConditions.getSalaryConditionBy(letter);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return getSalaryCondition(letter);
		}
	}
}
