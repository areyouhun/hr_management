package com.bs.view;

import java.util.List;

import com.bs.model.dto.Employee;

public class OutputView {
	private static OutputView outputView = new OutputView();
	
	private OutputView() {}
	
	public static OutputView getOutputView() {
		return outputView;
	}
	
	public void printEmployees(List<Employee> employees) {
		System.out.println("========== 조회 결과 ==========");
		if(employees.size() > 0) {
			employees.forEach(employee -> System.out.println(employee));
		}
	}
	
	public void printResult(int result, String successMsg, String failureMsg) {
		System.out.println("======== 시스템 메시지 ========");
		System.out.println((result > 0) ? successMsg : failureMsg);
		System.out.println("===============================");
	}
}
