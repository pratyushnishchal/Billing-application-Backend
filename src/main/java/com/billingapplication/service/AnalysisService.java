package com.billingapplication.service;

import java.util.List;
import java.util.Map;

public interface AnalysisService {
	public 	Double totalSales();
	public int noOfInvoices();
	public int noOfProducts();
	public int noOfCustomer();
	public int noOfAccountant();
	public double avgPricePerProduct();
	public int inventoryValue();
	public double avgOrderValue();
	public Double todaysTotalSale();
	public Double salesForWeek();
	public Map<String, Integer> getSalesByCategory();
	public List<Map<String, Object>> getDailySalesTrend();
	public int todaysTotalOrder();
	public List<Map<String, Object>> getDailyOrdersTrend();
	public int todayAddedCustomer();
	public int customerAddedForWeek();
	public int completedOrders();
	public double pendingAmt();
}
