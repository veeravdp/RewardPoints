package com.example.demo.model;

import java.time.LocalDate;
import java.time.Month;

public class TransactionRequest {
	private String customerId;
	private double amountSpent;
	private LocalDate transactionDate;

	public TransactionRequest(String customerId, double amountSpent, LocalDate transactionDate) {
		this.customerId = customerId;
		this.amountSpent = amountSpent;
		this.transactionDate = transactionDate;
	}

	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getAmountSpent() {
		return amountSpent;
	}

	public void setAmountSpent(double amountSpent) {
		this.amountSpent = amountSpent;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
}
