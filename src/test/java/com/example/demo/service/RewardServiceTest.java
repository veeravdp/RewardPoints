package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.exception.InvalidTransactionException;
import com.example.demo.model.RewardPoints;
import com.example.demo.model.TransactionRequest;

class RewardServiceTest {

	private RewardService rewardService;

	@BeforeEach
	public void setUp() {

		rewardService = new RewardService();
	}

	@Test
	public void testCalculateRewardPointsValidTransaction() {
		// Test a valid transaction with a $120 purchase (Expected: 90 points)
		TransactionRequest transaction = new TransactionRequest("customer1", 120, LocalDate.of(2025, 1, 15));
		RewardPoints rewardPoints = rewardService.calculateRewardPoints(transaction);

		assertEquals("customer1", rewardPoints.getCustomerId());
		assertEquals(90, rewardPoints.getPoints(), "Points should be 90 for a $120 transaction");
	}

	@Test
	public void testCalculateRewardPointsWithBoundaryAmount() {
		// Test edge case with $50 purchase (Expected: 0 points)
		TransactionRequest transaction = new TransactionRequest("customer2", 50, LocalDate.of(2025, 1, 15));
		RewardPoints rewardPoints = rewardService.calculateRewardPoints(transaction);

		assertEquals(0, rewardPoints.getPoints(), "Points should be 0 for a $50 purchase");

		// Test edge case with $100 purchase (Expected: 50 points)
		transaction = new TransactionRequest("customer3", 100, LocalDate.of(2025, 1, 15));
		rewardPoints = rewardService.calculateRewardPoints(transaction);

		assertEquals(50, rewardPoints.getPoints(), "Points should be 50 for a $100 purchase");
	}

	@Test
	public void testCalculateRewardPointsWithInvalidNegativeAmount() {
		// Test invalid transaction with negative amount (Expected: exception thrown)
		TransactionRequest transaction = new TransactionRequest("customer4", -50, LocalDate.of(2025, 1, 15));

		assertThrows(InvalidTransactionException.class, () -> {
			rewardService.calculateRewardPoints(transaction);
		}, "Transaction amount cannot be negative.");
	}

	@Test
	public void testCalculatePointsByMonthWithMultipleTransactions() {
		// Create multiple transactions for a single customer across different months
		List<TransactionRequest> transactions = List.of(
				new TransactionRequest("customer1", 120, LocalDate.of(2025, 01, 15)),
				new TransactionRequest("customer1", 75, LocalDate.of(2025, 01, 15)),
				new TransactionRequest("customer2", 150, LocalDate.of(2025, 02, 15)),
				new TransactionRequest("customer2", 200, LocalDate.of(2025, 03, 15)));

		// Calculate reward points by month
		Map<Month, Map<String, Integer>> pointsByMonth = rewardService.calculatePointsByMonth(transactions);

		// Check January: customer1 should have 90 points (from $120) + 25 points (from
		// $75) = 115
		assertTrue(pointsByMonth.containsKey(Month.JANUARY));
		assertEquals(115, pointsByMonth.get(Month.JANUARY).get("customer1"));

		// Check February: customer2 should have 100 points (from $150)
		assertTrue(pointsByMonth.containsKey(Month.FEBRUARY));
		assertEquals(100, pointsByMonth.get(Month.FEBRUARY).get("customer2"));

		// Check March: customer2 should have 400 points (from $200)
		assertTrue(pointsByMonth.containsKey(Month.MARCH));
		assertEquals(400, pointsByMonth.get(Month.MARCH).get("customer2"));

	}

	@Test
	public void testCalculatePointsByMonthWithInvalidTransaction() {
		// Create invalid transaction with negative amount
		List<TransactionRequest> transactions = List
				.of(new TransactionRequest("customer1", -50, LocalDate.of(2025, 01, 15)));

		// Calculate points by month and ensure invalid transaction is handled
		Map<Month, Map<String, Integer>> pointsByMonth = rewardService.calculatePointsByMonth(transactions);

		// Check that the transaction was skipped and no points were added
		assertFalse(pointsByMonth.containsKey(Month.JANUARY));
	}

	@Test
	public void testCalculatePointsForMultipleCustomers() {
		// Create transactions for multiple customers
		List<TransactionRequest> transactions = List.of(
				new TransactionRequest("customer1", 120, LocalDate.of(2025, 01, 15)),
				new TransactionRequest("customer2", 50, LocalDate.of(2025, 01, 15)),
				new TransactionRequest("customer3", 80, LocalDate.of(2025, 02, 15)),
				new TransactionRequest("customer1", 200, LocalDate.of(2025, 02, 15)));

		// Calculate reward points by month
		Map<Month, Map<String, Integer>> pointsByMonth = rewardService.calculatePointsByMonth(transactions);

		// Test customer points for January
		assertEquals(90, pointsByMonth.get(Month.JANUARY).get("customer1"));
		assertEquals(0, pointsByMonth.get(Month.JANUARY).get("customer2"));

		// Test customer points for February
		assertEquals(50, pointsByMonth.get(Month.FEBRUARY).get("customer3"));
		assertEquals(400, pointsByMonth.get(Month.FEBRUARY).get("customer1"));
	}
}