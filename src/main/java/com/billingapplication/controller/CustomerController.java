package com.billingapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.billingapplication.entity.Admin;
import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Subscribe;
import com.billingapplication.repository.CustomerRepository;
import com.billingapplication.service.AdminService;
import com.billingapplication.service.CustomerService;
import com.billingapplication.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class CustomerController {
	@Autowired
	private CustomerService customerSer;
	@Autowired
	CustomerRepository cusRepo;
	@Autowired
	UserService userSer;
	@Autowired
	private AdminService adminSer;
	@Autowired
	private CustomerService cusSer;

	@PostMapping("/api/users/createCustomer")
	public ResponseEntity<String> createAccountant(@RequestBody Customer cus) {
		Boolean emailExists1 = customerSer.existsEmail(cus.getEmail());
		Boolean emailExists2 = userSer.existsEmail(cus.getEmail());
		Boolean emailExists3 = adminSer.existsEmail(cus.getEmail());
		if (emailExists1) {
			return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
		} else if (emailExists2) {
			return new ResponseEntity<>("Email already exists in accountant database", HttpStatus.CONFLICT);
		} else if (emailExists3) {
			return new ResponseEntity<>("Email already exists in admin database", HttpStatus.CONFLICT);
		} else {
			Customer newCus = customerSer.createCustomerAcc(cus);
			if (newCus != null) {
				return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("/api/users/updateCustomer/{id}")
	public ResponseEntity<?> getUpdateCustomer(@PathVariable("id") int id, @RequestBody Customer cus) {
		Boolean emailExists1 = cusSer.existsEmail(cus.getEmail());
		Boolean emailExists2 = adminSer.existsEmail(cus.getEmail());
		Boolean emailExists3 = userSer.existsEmail(cus.getEmail());
		String email = cus.getEmail();
		if (emailExists2) {
			return new ResponseEntity<>("Email already exists in admin database", HttpStatus.CONFLICT);
		} else if (emailExists3) {
			return new ResponseEntity<>("Email already exists in accountant database", HttpStatus.CONFLICT);
		} else if (email.equals(cusSer.getEmailById(id))) {
			cus.setId(id);
			return ResponseEntity.ok().body(cusSer.updateCustomer(cus));
		} else if (emailExists1) {
			return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
		}

		cus.setId(id);
		return ResponseEntity.ok().body(cusSer.updateCustomer(cus));
	}

	@GetMapping("/api/users/displayCus")
	public ResponseEntity<?> getAllCustomer(Customer cus) {
		List<Customer> customer = customerSer.getAllCustomer(cus);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	@DeleteMapping("/api/users/deleteCus/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") int id) {
		customerSer.deleteCustomer(id);
		return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
	}

	@GetMapping("/api/users/search")
	public ResponseEntity<?> searchCustomer(@RequestParam("search") String search) {
		List<Customer> cus = cusSer.searchCustomer(search);
		if (cus.size() == 0) {
			return new ResponseEntity<>("Not Found", HttpStatus.CONFLICT);
		} else {
			return ResponseEntity.ok(cus);
		}
	}

	@GetMapping("/api/users/customerinfo/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") int id) {
		return ResponseEntity.ok().body(cusSer.getCustomerById(id));
	}

	@PostMapping("/customerLogin")
	public ResponseEntity<?> loginCustomerValidate(@RequestBody Customer customerCredentials) {
		String email = customerCredentials.getEmail();
		String password = customerCredentials.getPassword();
		Customer cus = cusSer.validateCustomer(email, password);
		if (cus != null) {
			return ResponseEntity.ok().body(cusSer.validateCustomer(email, password));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}

	@PostMapping("/subscribe")
	public ResponseEntity<?> subscribeEmail(@RequestParam("email") String email, Subscribe sub) {
		Subscribe sendMail=cusSer.subscribeMail(sub);
		return new ResponseEntity<>("Mail Sent", HttpStatus.OK);
	}
	@PostMapping("/{customerId}/add-to-wallet")
    public String addToWallet(@PathVariable("customerId") int customerId, @RequestParam("amount") Double amount) {
        return customerSer.addToWallet(customerId, amount);
    }

}
