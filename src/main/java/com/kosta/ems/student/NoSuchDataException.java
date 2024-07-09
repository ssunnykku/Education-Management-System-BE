package com.kosta.ems.student;

public class NoSuchDataException extends RuntimeException{
	public NoSuchDataException(String message) {
		super(message);
	}
}
