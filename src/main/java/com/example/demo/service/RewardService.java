package com.example.demo.service;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.exception.InvalidTransactionException;
import com.example.demo.model.RewardPoints;
import com.example.demo.model.TransactionRequest;

/**
 * Service class for calculating reward points based on transactions.
 */
@Service
public class RewardService {

	/**
	 * Calculate reward points based on the transaction amount.
	 * 
	 * @param transaction The transaction data.
	 * @return RewardPoints object containing customer ID and points earned.
	 */
	public RewardPoints calculateRewardPoints(TransactionRequest transaction) {
		// Validate the transaction to ensure the amount spent is valid
		if (transaction.getAmountSpent() < 0) {
			throw new InvalidTransactionException("Transaction amount cannot be negative.");
		}

		double amountSpent = transaction.getAmountSpent();
		int points = 0;

		if (amountSpent > 100) {
			points += (amountSpent - 100) * 2; // 2 points per dollar over $100
			amountSpent = 100;
		}
		if (amountSpent > 50 && amountSpent <= 100) {
			points += (amountSpent - 50); // 1 point per dollar between $50 and $100
		}

		return new RewardPoints(transaction.getCustomerId(), points);
	}

	/**
	 * Calculate reward points for a list of transactions, grouped by month.
	 * 
	 * @param transactions List of all transactions.
	 * @return Map of customer IDs to total reward points earned per month.
	 */
	public Map<Month, Map<String, Integer>> calculatePointsByMonth(List<TransactionRequest> transactions) {
		Map<Month, Map<String, Integer>> pointsByMonth = new HashMap<>();

		for (TransactionRequest transaction : transactions) {
			// Calculate reward points and catch any potential exceptions
			try {
				RewardPoints rewardPoints = calculateRewardPoints(transaction);
				Month month = transaction.getTransactionDate().getMonth();

				pointsByMonth.computeIfAbsent(month, m -> new HashMap<>()).merge(rewardPoints.getCustomerId(),
						rewardPoints.getPoints(), Integer::sum);
			} catch (InvalidTransactionException e) {
				// Log the error (optional), but we handle the exception gracefully
				System.out.println("Skipping invalid transaction: " + e.getMessage());
			}
		}

		return pointsByMonth;
	}
}
