package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.TransactionRequest;
import com.example.demo.service.RewardService;

import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

	@Autowired
	private RewardService rewardService;

	/**
	 * Endpoint to calculate reward points for customers based on a list of
	 * transactions. The points will be aggregated per customer per month.
	 * 
	 * @param transactions List of transactions.
	 * @return Map of months and total points per customer.
	 */
	@PostMapping("/calculate")
	public Map<Month, Map<String, Integer>> calculateRewardPoints(@RequestBody List<TransactionRequest> transactions) {
		Map<Month, Map<String, Integer>> result = rewardService.calculatePointsByMonth(transactions);
		return result;
	}
}
