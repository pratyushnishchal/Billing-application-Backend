package com.billingapplication.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Invoice;
import com.billingapplication.entity.Product;
import com.billingapplication.repository.CustomerRepository;
import com.billingapplication.repository.InvoiceRepository;
import com.billingapplication.repository.ProductRepository;
import com.billingapplication.repository.UserRepository;
import com.billingapplication.service.AnalysisService;

@Service
public class AnalysisServiceImpl implements AnalysisService {
	@Autowired
	InvoiceRepository invRepo;
	@Autowired
	ProductRepository prodRepo;
	@Autowired
	CustomerRepository custRepo;
	@Autowired
	UserRepository userRepo;

	@Override
	public Double totalSales() {
		Double total = invRepo.totalSales();
		return total;
	}

	@Override
	public int noOfInvoices() {
		int countInvoice = invRepo.noOfInvoices();
		return countInvoice;
	}

	@Override
	public int noOfProducts() {
		int countProducts = prodRepo.countProducts();
		return countProducts;
	}

	@Override
	public int noOfCustomer() {
		int countCus = custRepo.countCustomer();
		return countCus;
	}

	@Override
	public int noOfAccountant() {
		int countAcc = userRepo.countAccountant();
		return countAcc;
	}

	@Override
	public double avgPricePerProduct() {
		double avgPrice = prodRepo.avgPricePerProduct();
		return avgPrice;
	}

	@Override
	public int inventoryValue() {
		int inventoryVale = prodRepo.inventoryValue();
		return inventoryVale;
	}

	@Override
	public double avgOrderValue() {
		double avgOrder = invRepo.avgOrderValue();
		return avgOrder;
	}

	@Override
	public Double todaysTotalSale() {
		Double todaysSale = invRepo.todaysSales();
		return (todaysSale != null) ? todaysSale : 0.0;
	}

	@Override
	public Double salesForWeek() {
		Double weekSale = invRepo.salesForWeek();
		return (weekSale != null) ? weekSale : 0.0;
	}

	@Override
	public Map<String, Integer> getSalesByCategory() {
		List<Product> products = prodRepo.findAll();
		Map<String, Integer> categorySales = new HashMap<>();

		for (Product product : products) {
			String categoryName = product.getProductCategory();
			categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + 1);
		}

		return categorySales;
	}

	@Override
	public List<Map<String, Object>> getDailySalesTrend() {
		List<Object[]> salesData = invRepo.findDailySales(); // Calls the custom query

		List<Map<String, Object>> dailySalesList = new ArrayList<>();
		for (Object[] record : salesData) {
			Map<String, Object> salesMap = new HashMap<>();
			salesMap.put("date", record[0].toString());
			salesMap.put("sales", record[1]);
			dailySalesList.add(salesMap);
		}

		return dailySalesList;
	}

	@Override
	public int todaysTotalOrder() {
		int todaysNoOrder = invRepo.todaysOrderNo();
		return todaysNoOrder;

	}

	@Override
	public List<Map<String, Object>> getDailyOrdersTrend() {
		List<Map<String, Object>> ordersData = new ArrayList<>();
		LocalDate today = LocalDate.now();

		LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
		LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
		List<Invoice> invoices = invRepo.findInvoicesForCurrentWeek(startOfWeek, endOfWeek);
		Map<DayOfWeek, Integer> dailyOrders = new HashMap<>();
		for (Invoice invoice : invoices) {
			LocalDate date = invoice.getInvoiceDate();
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			dailyOrders.put(dayOfWeek, dailyOrders.getOrDefault(dayOfWeek, 0) + 1);
		}
		for (DayOfWeek day : DayOfWeek.values()) {
			Map<String, Object> dayOrders = new HashMap<>();
			dayOrders.put("day", day.toString().substring(0, 3));
			dayOrders.put("orders", dailyOrders.getOrDefault(day, 0));
			ordersData.add(dayOrders);
		}

		return ordersData;
	}

	@Override
	public int todayAddedCustomer() {
		int totalCus = custRepo.todaysAddCusomer();
		return totalCus;
	}

	@Override
	public int customerAddedForWeek() {
		int weekCus=custRepo.weeklyCustomers();
		return weekCus;
	}

	@Override
	public int completedOrders() {
		int comOrd=invRepo.completedOrder();
		return comOrd;
	}

	@Override
	public double pendingAmt() {
		double pendingAmount=invRepo.pendingAmount();
		return pendingAmount;
	}
}
