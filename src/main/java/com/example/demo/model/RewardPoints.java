package com.example.demo.model;

public class RewardPoints {
	private String customerId;
	private int points;

	public RewardPoints(String customerId, int points) {
		this.customerId = customerId;
		this.points = points;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
