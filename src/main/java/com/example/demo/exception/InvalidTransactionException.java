package com.example.demo.exception;

public class InvalidTransactionException extends RuntimeException {

	public InvalidTransactionException(String message) {
		super(message);
	}
}
