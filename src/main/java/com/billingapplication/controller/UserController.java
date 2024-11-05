package com.billingapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.billingapplication.entity.User;
import com.billingapplication.repository.UserRepository;
import com.billingapplication.service.AdminService;
import com.billingapplication.service.CustomerService;
import com.billingapplication.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userSer;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CustomerService cusSer;
	@Autowired
	private AdminService adminSer;

	@PostMapping("/api/admin/createAccountant")
	public ResponseEntity<String> createAccountant(@RequestBody User us) {
		Boolean emailExists1 = userSer.existsEmail(us.getEmail());
		Boolean emailExists2 = cusSer.existsEmail(us.getEmail());
		Boolean emailExists3 = adminSer.existsEmail(us.getEmail());
		Long mobile = us.getMobileNumber();
		if (emailExists1) {
			return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
		} else if (emailExists2) {
			return new ResponseEntity<>("Email already exists in customer database", HttpStatus.CONFLICT);
		} else if (emailExists3) {
			return new ResponseEntity<>("Email already exists in admin database", HttpStatus.CONFLICT);
		} else {
			User newUser = userSer.createAccountant(us);
			if (newUser != null) {
				return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("/api/admin/updateAccountant/{id}")
	public ResponseEntity<?> getUpdateAccountant(@PathVariable("id") int id, @RequestBody User us) {
		Boolean emailExists1 = cusSer.existsEmail(us.getEmail());
		Boolean emailExists2 = adminSer.existsEmail(us.getEmail());
		Boolean emailExists3 = userSer.existsEmail(us.getEmail());
		String email = us.getEmail();
		if (emailExists1) {
			return new ResponseEntity<>("Email already exists in customer database", HttpStatus.CONFLICT);
		} else if (emailExists2) {
			return new ResponseEntity<>("Email already exists in admin database", HttpStatus.CONFLICT);
		} else if (email.equals(userSer.getEmailById(id))) {
			us.setId(id);
			return ResponseEntity.ok().body(userSer.updateAccountant(us));
		} else if (emailExists3) {
			return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
		}

		us.setId(id);
		return ResponseEntity.ok().body(userSer.updateAccountant(us));
	}

	@GetMapping("/api/admin/displayAcc")
	public ResponseEntity<?> getAllAccountants(User us) {
		List<User> users = userSer.getAllAccountant(us);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@DeleteMapping("/api/admin/deleteAcc/{id}")
	public ResponseEntity<String> deleteAccountant(@PathVariable("id") int id) {
		userSer.deleteAccountant(id);
		return new ResponseEntity<>("Accountant deleted successfully", HttpStatus.OK);
	}

	@PostMapping("/api/users/accLogin")
	public ResponseEntity<?> loginAccountant(@RequestBody User accountantCredentials) {
		String email = accountantCredentials.getEmail();
		String password = accountantCredentials.getPassword();
		User user = userSer.validateAccountant(email, password);
		if (user != null) {
			return ResponseEntity.ok().body(userSer.validateAccountant(email, password));
		} else {
			return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/api/admin/accountantinfo/{id}")
	public ResponseEntity<User> getAccById(@PathVariable("id") int id) {
		return ResponseEntity.ok().body(userSer.getAccountantById(id));
	}

}
