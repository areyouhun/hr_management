package com.bs.common;

import java.util.Arrays;

public enum SalaryConditions {
	UP("U", ">="),
	DOWN("D", "<=");
	
	private final String letter;
	private final String sign;
	
	private static final String WRONG_LETTER = "잘못 입력하였습니다. 입력된 급여 이상을 조회하실 경우 'U'를, 이하면 'D'를 입력해주세요.";
	
	SalaryConditions(String letter, String sign) {
		this.letter = letter;
		this.sign = sign;
	}
	
	public static SalaryConditions getSalaryConditionBy(String letter) {
		return Arrays.stream(values())
					.filter(condition -> condition.letter.equals(letter))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(WRONG_LETTER));
	}
	
	public String getSign() {
		return this.sign;
	}
}
