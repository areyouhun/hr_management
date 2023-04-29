package com.bs.view;

import java.util.Map;
import java.util.Scanner;

import com.bs.model.dto.Department;
import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;

public class InputView {
	private static InputView inputView = new InputView();
	
	private Scanner scanner;
	
	private InputView() {
		scanner = new Scanner(System.in);
	}
	
	public static InputView getInputView() {
		return inputView;
	}
	
	public int readMainMenu() {
		System.out.println("====== 사원관리 프로그램 ======");
		System.out.println("1. 전체 사원 조회");
		System.out.println("2. 조건별 사원 조회");
		System.out.println("3. 신규 사원 등록");
		System.out.println("4. 사원 정보 수정");
		System.out.println("5. 사원 삭제");
		System.out.println("6. 부서 관리");
		System.out.println("7. 직책 관리");
		System.out.println("0. 프로그램 종료");
		return Integer.parseInt(readInput("번호 입력 : "));
	}
	
	public int readSubMenuForEmployee() {
		System.out.println("====== 조건별 사원 조회 ======");
		System.out.println("1. 부서로 사원 찾기");
		System.out.println("2. 직책으로 사원 찾기");
		System.out.println("3. 이름으로 사원 찾기");
		System.out.println("4. 급여로 사원 찾기");
		System.out.println("0. 상위 메뉴로 돌아가기");
		return Integer.parseInt(readInput("번호 입력 : "));
	}
	
	public String readDeptTitle() {
		return readInput("찾고자 하는 사원의 부서명을 입력해주세요 : ");
	}
	
	public String readJobName() {
		return readInput("찾고자 하는 사원의 직책명을 입력해주세요 : ");
	}
	
	public String readName() {
		return readInput("찾고자 하는 사원의 이름을 입력해주세요 : ");
	}
	
	public int readSalary() {
		return Integer.parseInt(readInput("급여를 입력해주세요 : "));
	}
	
	public String readSearchCondition() {
		String letter = readInput("입력된 급여 이상의 급여를 받는 사원을 찾고자 한다면 'U', 그렇지 않다면 'D'를 입력해주세요 : ");
		if (letter.matches("^[a-z]$")) {
			letter = letter.toUpperCase();
		}
		return letter;
	}
	
	public void header(String headerMsg) {
		System.out.println(headerMsg);
	}
	
	public Employee readEmployee() {
		String empId = readInput("사원번호 : ");
		
		String empName = readInput("사원명 : ");
		
		String empNo = readInput("주민등록번호 : ");
		
		String email = readInput("이메일 : ");
		
		String phone = readInput("전화번호 ('-' 빼고 입력) : ").replace("-", "");
		
		int salary = Integer.parseInt(readInput("급여 : "));
		
		double bonus = Double.parseDouble(readInput("보너스 : "));
		
		String managerId = readInput("매니저 아이디 (없으면 'X' 입력) : ");
		
		while (!managerId.matches("^2[0-1][0-9]$") && !managerId.matches("^22[0-2]$") && !managerId.matches("^[xX]$")) {
			System.out.print("입력값이 올바르지 않습니다.");
			managerId = readInput("매니저 아이디 (없으면 'X' 입력) : ");
		}
		
		if (managerId.equals("x")) {
			managerId.toUpperCase();
		}
		
		return new Employee.Builder()
							.empId(empId)
							.empName(empName)
							.empNo(empNo)
							.email(email)
							.phone(phone)
							.salary(salary)
							.bonus(bonus)
							.managerId(managerId)
							.build();
	}
	
	public Map<String, String> readTitles(String deptMsg, String jobMsg) {
		System.out.print(deptMsg);
		String deptTitle = scanner.nextLine();
		
		System.out.print(jobMsg);
		String jobName = scanner.nextLine();
		
		return Map.of("DEPT_TITLE", deptTitle, "JOB_NAME", jobName);
	}
	
	public Employee readEmployeeToBeUpdated() {
		String empId = readInput("수정할 사원번호 : ");
		
		int salary = Integer.parseInt(readInput("변경된 급여 : "));
		
		String phone = readInput("변경된 전화번호 ('-' 빼고 입력) : ").replace("-", "");
		
		String email = readInput("변경된 이메일 : ");
		
		return new Employee.Builder()
							.empId(empId)
							.salary(salary)
							.phone(phone)
							.email(email)
							.build();
	}
	
	public String readEmpId() {
		return readInput("삭제하려는 사원의 사원번호를 입력해주세요 : ");
	}
	
	public String readDeptId() {
		return readInput("삭제하려는 부서의 부서코드를 입력해주세요 : ");
	}
	
	public String readJobCode() {
		return readInput("삭제하려는 직책의 직책코드를 입력해주세요 : ");
	}
	
	public int readSubMenuForDept() {
		System.out.println("========== 부서 관리 ==========");
		System.out.println("1. 신규 부서 등록");
		System.out.println("2. 부서 정보 수정");
		System.out.println("3. 부서 삭제");
		System.out.println("0. 상위 메뉴로 돌아가기");
		return Integer.parseInt(readInput("번호 입력 : "));
	}
	
	public int readSubMenuForJob() {
		System.out.println("========== 직책 관리 ==========");
		System.out.println("1. 신규 직책 등록");
		System.out.println("2. 직책 정보 수정");
		System.out.println("3. 직책 삭제");
		System.out.println("0. 상위 메뉴로 돌아가기");
		return Integer.parseInt(readInput("번호 입력 : "));
	}
	
	public Department readDepartment() {
		Department department = new Department();
		department.setDeptId(readInput("부서코드 : "));
		department.setDeptTitle(readInput("부서명 : "));
		department.setLocationId(readInput("지역코드 : "));
		return department;
	}
	
	public Department readDepartmentToBeUpdated() {
		Department department = new Department();
		department.setDeptId(readInput("수정할 부서코드 : "));
		department.setDeptTitle(readInput("변경된 부서명 : "));
		return department;
	}
	
	public Job readJob() {
		Job job = new Job();
		job.setJobCode(readInput("직책코드 : "));
		job.setJobName(readInput("직책명 : "));
		return job;
	}
	
	public Job readJobToBeUpdated() {
		Job job = new Job();
		job.setJobCode(readInput("수정할 직책코드 : "));
		job.setJobName(readInput("변경된 직책명 : "));
		return job;
	}
	
	private String readInput(String msg) {
		System.out.print(msg);
		return scanner.nextLine();
	}
}
