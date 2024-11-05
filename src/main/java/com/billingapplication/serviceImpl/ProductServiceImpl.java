package com.billingapplication.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Product;
import com.billingapplication.exception.RecordNotFoundException;
import com.billingapplication.repository.ProductRepository;
import com.billingapplication.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository prodRepo;

	@Override
	public Product addProducts(Product prod) {
		Product newProduct = prodRepo.save(prod);
		return newProduct;
	}

	@Override
	public Product updateProducts(Product prod) {
		Optional<Product> p = prodRepo.findById(prod.getId());
		if (p.isPresent()) {
			Product pro = p.get();
			pro.setProdName(prod.getProdName());
			pro.setPrice(prod.getPrice());
			pro.setProdDescription(prod.getProdDescription());
			pro.setProductCategory(prod.getProductCategory());
			return prodRepo.save(pro);
		} else {
			throw new RecordNotFoundException("Product with id " + prod.getId() + " not found");
		}
	}

	@Override
	public void deleteProduct(int id) {
		Optional<Product> p = prodRepo.findById(id);
		if (p.isPresent()) {
			Product pro = p.get();
			prodRepo.delete(pro);
		} else {
			throw new RecordNotFoundException("Product with id " + id + " not found");
		}

	}

	@Override
	public List<Product> getAllProduct(Product prod) {
		return prodRepo.findAll();
	}

	@Override
	public List<Product> searchProduct(String search) {
		return prodRepo.searchProduct(search);
	}

	@Override
	public Map<String, Integer> getCategoryDistribution() {
		List<Product> products = prodRepo.findAll();
		Map<String, Integer> categoryCount = new HashMap<>();
		for (Product product : products) {
			String categoryName = product.getProductCategory();
			categoryCount.put(categoryName, categoryCount.getOrDefault(categoryName, 0) + 1);
		}
		return categoryCount;
	}

}
