package com.bs.common;

import java.util.Arrays;

public enum SearchConditions {
	UP("U", ">="),
	DOWN("D", "<=");
	
	private final String letter;
	private final String sign;
	
	private static final String WRONG_LETTER = "잘못 입력하였습니다.";
	
	SearchConditions(String letter, String sign) {
		this.letter = letter;
		this.sign = sign;
	}
	
	public static SearchConditions getBy(String letter) {
		return Arrays.stream(values())
					.filter(condition -> condition.letter.equals(letter))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(WRONG_LETTER));
	}
	
	public String getSign() {
		return this.sign;
	}
}
