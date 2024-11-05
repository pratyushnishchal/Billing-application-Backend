package com.billingapplication.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billingapplication.entity.Invoice;
import com.billingapplication.entity.Product;
import com.billingapplication.service.InvoiceService;

@RestController
public class InvoiceController {
	@Autowired
	InvoiceService invSer;

	@PostMapping("/api/accountant/createInvoice")
	public ResponseEntity<Invoice> createInvoice(@RequestParam("customerId") int customerId, @RequestParam("productIds") List<Integer> productIds, @RequestParam("quantities") List<Integer> quantities) {

		Invoice invoice = invSer.createInvoice(productIds, quantities, customerId);
		return ResponseEntity.ok(invoice); 
	}
	@GetMapping("/api/accountant/viewInvoice/{invoiceId}")
	public ResponseEntity<Invoice> viewInvoiceById(@PathVariable("invoiceId") int invoiceId){
		Invoice inv=invSer.getInvoiceById(invoiceId);
		return new ResponseEntity<Invoice>(inv, HttpStatus.OK);
	}
	@GetMapping("/api/accountant/getAllInvoice")
	public ResponseEntity<?> getAllInv(Invoice invoice){
		List<Invoice> list_inv=invSer.getAllInvoices(invoice);
		return new ResponseEntity<>(list_inv, HttpStatus.OK);
	}
	@DeleteMapping("/api/accountant/deleteInvoice/{invoiceId}")
	public ResponseEntity<String> deleteInvoiceById(@PathVariable("invoiceId") int invoiceId){
		invSer.deleteInvoice(invoiceId);
		return new ResponseEntity<>("Invoice deleted successfully", HttpStatus.OK);
	}
	@GetMapping("/invoice/{customerId}")
	public ResponseEntity<?> getInvoicesByCustomerId(@PathVariable("customerId") int customerId) {
	    List<Invoice> invoices = invSer.getInvoiceByCustomerId(customerId);
	    
	    if (invoices.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "No pending invoices available");
	        return new ResponseEntity<>(response, HttpStatus.OK); 
	    }
	    
	    return new ResponseEntity<>(invoices, HttpStatus.OK); 
	}
	@GetMapping("/orderhistory/{customerid}")
	public ResponseEntity<?> orderHistory(@PathVariable("customerid") int customerid) {
	    List<Invoice> invoices = invSer.orderHistory(customerid);
	    
	    if (invoices.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "No pending invoices available");
	        return new ResponseEntity<>(response, HttpStatus.OK); 
	    }
	    
	    return new ResponseEntity<>(invoices, HttpStatus.OK); 
	}

	
}