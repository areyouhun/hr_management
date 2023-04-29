package com.bs.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.bs.common.SearchConditions;
import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;
import com.bs.service.Service;
import com.bs.view.InputView;
import com.bs.view.OutputView;

public class Controller {
	private static final String BACK_TO_MAIN_MENU = "메인메뉴로 돌아갑니다.";
	private static final String INVALID_INPUT = "입력값이 올바르지 않습니다. 다시 입력해주세요.";
	
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
				outputView.printResult(service.updateEmployeeBy(employeeToBeUpdated, titlesToBeRenamed), "사원 정보 수정 성공", "사원 정보 수정 실패");
				break;
			case 5:
				inputView.header("======== 사원 삭제 ========");
				int result = service.deleteFromEmployeeBy(inputView.readEmpId());
				outputView.printResult(result, "사원 삭제 성공", "사원 삭제 실패");
				break;
			case 6:
				submenuForDept();
				break;
			case 7:
				submenuForJob();
				break;
			case 0:
				outputView.printMsg("프로그램을 종료합니다.");
				return;
			default:
				outputView.printMsg(INVALID_INPUT);
			}
			System.out.println();
		}
	}
	
	public void submenuForEmployee() {
		while (true) {
			switch (inputView.readSubMenuForEmployee()) {
			case 1:
				List<Employee> employeesByDept = service.selectFromEmployeeByDeptTitle(inputView.readDeptTitle());
				outputView.printEmployees(employeesByDept);
				break;
			case 2:
				List<Employee> employeesByJob = service.selectFromEmployeeByJobName(inputView.readJobName());
				outputView.printEmployees(employeesByJob);
				break;
			case 3:
				List<Employee> employeesByName = service.selectFromEmployeeByName(inputView.readName());
				outputView.printEmployees(employeesByName);
				break;
			case 4:
				int salary = inputView.readSalary();
				SearchConditions searchCondition = getSalaryCondition(() -> inputView.readSearchCondition());
				List<Employee> employeesBySalary = service.selectFromEmployeeBySalary(salary, searchCondition);
				outputView.printEmployees(employeesBySalary);
				break;
			case 0:
				outputView.printMsg(BACK_TO_MAIN_MENU);
				return;
			default:
				outputView.printMsg(INVALID_INPUT);
			}
			System.out.println();
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
				outputView.printResult(service.updateDepartmentBy(departmentToBeUpdated), "부서 정보 수정 성공", "부서 정보 수정 실패");
				break;
			case 3:
				inputView.header("========== 부서 삭제 ==========");
				String deptId = inputView.readDeptId();
				outputView.printResult(service.deleteFromDepartmentBy(deptId), "부서 삭제 성공", "부서 삭제 실패");
				break;
			case 0:
				outputView.printMsg(BACK_TO_MAIN_MENU);
				return;
			default:
				outputView.printMsg(INVALID_INPUT);
			}
			System.out.println();
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
				outputView.printResult(service.updateJobBy(JobToBeUpdated), "직책 정보 수정 성공", "직책 정보 수정 실패");
				break;
			case 3:
				inputView.header("========== 직책 삭제 ==========");
				String JobCode = inputView.readJobCode();
				outputView.printResult(service.deleteFromJobBy(JobCode), "직책 삭제 성공", "직책 삭제 실패");
				break;
			case 0:
				outputView.printMsg(BACK_TO_MAIN_MENU);
				return;
			default:
				outputView.printMsg(INVALID_INPUT);
			}
			System.out.println();
		}
	}
	
	private SearchConditions getSalaryCondition(Supplier<String> inputReader) {
		try {
			return SearchConditions.getBy(inputReader.get());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return getSalaryCondition(inputReader);
		}
	}
}
