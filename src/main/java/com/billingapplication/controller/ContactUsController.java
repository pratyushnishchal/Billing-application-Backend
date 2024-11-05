package com.billingapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.billingapplication.entity.ContactUs;
import com.billingapplication.service.ContactUsService;

@RestController
public class ContactUsController {
	@Autowired
	ContactUsService conSer;
	@PostMapping("/contactus")
	public ResponseEntity<?> queryCustomer(@RequestBody ContactUs contactUs){
		ContactUs con=conSer.askQuery(contactUs);
		return new ResponseEntity<>("Mail Sent", HttpStatus.OK);
	}
}
