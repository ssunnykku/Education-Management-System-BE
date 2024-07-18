package com.kosta.ems.student;

public enum ResCode {
	SUCCESS(0), FAIL(-1), NO_SUCH_DATA(-2), DUPLICATE_KEY(-3), NULL_VALUE(-4), NOT_NULL(-5), UNKNOWN(-99);

	private final int value;

	ResCode(int value) {
		this.value=value;
	}

	public int value() {
		return value;
	}
}
