package com.billingapplication.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Product;
import com.billingapplication.entity.User;
import com.billingapplication.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	ProductService prodSer;

	@PostMapping("/api/admin/addProducts")
	public ResponseEntity<String> addProducts(@RequestBody Product prod) {
		Product newprod = prodSer.addProducts(prod);
		if (newprod != null) {
			return new ResponseEntity<>("Product Added successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/api/admin/updateProduct/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product prod, @PathVariable("id") int id) {
		prod.setId(id);
		return ResponseEntity.ok().body(prodSer.updateProducts(prod));
	}

	@DeleteMapping("/api/admin/deleteProduct/{id}")
	public ResponseEntity<String> deleteAccountant(@PathVariable("id") int id) {
		prodSer.deleteProduct(id);
		return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
	}

	@GetMapping("/api/admin/viewallproducts")
	public ResponseEntity<?> getAllAccountants(Product pro) {
		List<Product> prod = prodSer.getAllProduct(pro);
		return new ResponseEntity<>(prod, HttpStatus.OK);
	}

	@GetMapping("/api/admin/search")
	public ResponseEntity<?> searchProduct(@RequestParam("search") String search) {
		List<Product> pro = prodSer.searchProduct(search);
		if (pro.size() == 0) {
			return new ResponseEntity<>("Not Found", HttpStatus.CONFLICT);
		} else {
			return ResponseEntity.ok(pro);
		}
	}
}
