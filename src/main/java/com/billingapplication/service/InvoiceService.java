package com.billingapplication.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.billingapplication.entity.Invoice;
import com.billingapplication.entity.Product;


public interface InvoiceService {
	public Invoice createInvoice(List<Integer> productIds, List<Integer> quantities, int customerId);
	public Invoice getInvoiceById(int invoiceId);
	public void deleteInvoice(int InvoiceId);
	public List<Invoice> getAllInvoices(Invoice inv);
	public List<Invoice> getInvoiceByCustomerId(int customerid);
	public Map<String, Integer> getPaymentStatusPieChart();
	public List<Invoice> orderHistory(int customerid);
}