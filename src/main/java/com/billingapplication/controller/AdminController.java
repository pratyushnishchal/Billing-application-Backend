package com.billingapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.billingapplication.entity.Admin;
import com.billingapplication.entity.Customer;
import com.billingapplication.service.AdminService;
import com.billingapplication.service.CustomerService;
import com.billingapplication.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private AdminService adminSer;
	@Autowired
	private CustomerService cusSer;
	@Autowired
	private UserService userSer;

	@GetMapping("/")
	public ResponseEntity<String> index() {
		return ResponseEntity.ok("Welcome to the Admin API");
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginAdminValidate(@RequestBody Admin adminCredentials,HttpSession session) {
		String email = adminCredentials.getEmail();
		String password = adminCredentials.getPassword();
		Admin admin = adminSer.validateAdmin(email, password);
		if (admin != null) {
			return ResponseEntity.ok().body(adminSer.validateAdmin(email, password));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}

	@GetMapping("/welcome")
	public ResponseEntity<?> welcomePage() {
            return ResponseEntity.ok("Welcome to the Admin Dashboard!");
	}
	@GetMapping("/getAdminDetails/{id}")
	public ResponseEntity<?> getDetails(@PathVariable("id") int id) {
		Admin admin=adminSer.getAdminById(id);
		return ResponseEntity.ok().body(admin);
	}
	@PutMapping("/updateAdmin/{id}")
	public ResponseEntity<?> getUpdateAdmin(@PathVariable("id") int id, @RequestBody Admin ad) {
		Boolean emailExists1 = cusSer.existsEmail(ad.getEmail());
		Boolean emailExists2 = adminSer.existsEmail(ad.getEmail());
		Boolean emailExists3 = userSer.existsEmail(ad.getEmail());
		String email = ad.getEmail();
		if (emailExists1) {
			return new ResponseEntity<>("Email already exists in admin database", HttpStatus.CONFLICT);
		} else if (emailExists3) {
			return new ResponseEntity<>("Email already exists in accountant database", HttpStatus.CONFLICT);
		} else if (email.equals(adminSer.getEmailById(id))) {
			ad.setId(id);
			return ResponseEntity.ok().body(adminSer.updateAdmin(ad));
		} else if (emailExists2) {
			return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
		}

		ad.setId(id);
		return ResponseEntity.ok().body(adminSer.updateAdmin(ad));
	}
}
