package com.homework.converter.util;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {
	public CustomException(String message) {
        super(message);
    }
	
	public CustomException() {
        super();
    }

}
