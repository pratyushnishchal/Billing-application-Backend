package com.billingapplication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billingapplication.service.AnalysisService;
import com.billingapplication.service.InvoiceService;
import com.billingapplication.service.ProductService;

@RestController
public class AnalysisController {
	@Autowired
	AnalysisService analysisSer;
	@Autowired
	InvoiceService invSer;
	@Autowired
	ProductService prodSer;
	@GetMapping("/analysis/totalSales")
	public double totalSales() {
		double totalsales=analysisSer.totalSales();
		return totalsales;
	}
	@GetMapping("/analysis/countInvoices")
	public int countSales() {
		int countInv=analysisSer.noOfInvoices();
		return countInv;
	}
	@GetMapping("/analysis/countProducts")
	public int countProducts() {
		int countProducts=analysisSer.noOfProducts();
		return countProducts;
	}
	@GetMapping("/analysis/countCustomer")
	public int countCustomer() {
		int countCustomer=analysisSer.noOfCustomer();
		return countCustomer;
	}
	@GetMapping("/analysis/countAccountant")
	public int countAccountant() {
		int countAcc=analysisSer.noOfAccountant();
		return countAcc;
	}
	@GetMapping("/analysis/avgpricePerProd")
	public double avgPrice () {
		double avgPrice1=analysisSer.avgPricePerProduct();
		return avgPrice1;
	}
	@GetMapping("/analysis/invValue")
	public double inventoryValue () {
		double inveValue=analysisSer.inventoryValue();
		return inveValue;
	}
	@GetMapping("/analysis/avgorderValue")
	public double avgOrder () {
		double avgorder=analysisSer.avgOrderValue();
		return avgorder;
	}
	@GetMapping("/analysis/todaysSale")
	public double todaysTotalSale () {
		double todaySale=analysisSer.todaysTotalSale();
		return todaySale;
	}
	@GetMapping("/analysis/weekSales")
	public double weekTotalSale () {
		double weeksSale=analysisSer.salesForWeek();
		return weeksSale;
	}
	@GetMapping("/analysis/salesByCategory")
	public Map<String, Integer> getSalesByCategory() {
	    return analysisSer.getSalesByCategory();
	}
	@GetMapping("/analysis/dailySales")
    public List<Map<String, Object>> getDailySalesTrend() {
        return analysisSer.getDailySalesTrend();
    }
	@GetMapping("/analysis/todaysOrder")
	public double todaysOrderNumber () {
		double todayorder=analysisSer.todaysTotalOrder();
		return todayorder;
	}
	@GetMapping("/analysis/daily-orders")
    public List<Map<String, Object>> getDailyOrdersData() {
        return analysisSer.getDailyOrdersTrend();
    }
	@GetMapping("/analysis/todayscustomer")
	public int todaysCustomer() {
		int todaycustomer=analysisSer.todayAddedCustomer();
		return todaycustomer;
	}
	@GetMapping("/analysis/weeksCustomer")
	public double weekTotalCus() {
		double weeksCustomer=analysisSer.customerAddedForWeek();
		return weeksCustomer;
	}
	@GetMapping("/analysis/completedOrders")
	public int completeOrders() {
		int co=analysisSer.completedOrders();
		return co;
	}
	@GetMapping("/analysis/pending-amount")
	public double pendingMoney() {
		double p=analysisSer.pendingAmt();
		return p;
	}
	@GetMapping("/chart/paymentStatus-piechart")
	public ResponseEntity<Map<String, Integer>> getPaymentPieChart() {
		Map<String, Integer> payPieChart = invSer.getPaymentStatusPieChart();
        return ResponseEntity.ok(payPieChart);
    }
	@GetMapping("/chart/category-distribution")
	public ResponseEntity<Map<String, Integer>> getCategoryDistribution() {
		Map<String, Integer> categoryDistribution = prodSer.getCategoryDistribution();
        return ResponseEntity.ok(categoryDistribution);
    }
}
