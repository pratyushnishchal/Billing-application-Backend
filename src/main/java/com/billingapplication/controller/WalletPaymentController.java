package com.billingapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billingapplication.service.PaymentService;


@RestController
public class WalletPaymentController {
	@Autowired
	private PaymentService paySer;
	@PostMapping("/wallet")
    public ResponseEntity<String> payByWallet(
            @RequestParam("customerId") int customerId,
            @RequestParam("invoiceId") int invoiceId) {
        String response = paySer.payByWallet(customerId, invoiceId);
        return ResponseEntity.ok(response);
    }
}
