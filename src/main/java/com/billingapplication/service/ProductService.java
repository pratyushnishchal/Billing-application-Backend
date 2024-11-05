package com.billingapplication.service;

import java.util.List;
import java.util.Map;

import com.billingapplication.entity.Product;

public interface ProductService {
	public Product addProducts(Product prod);
	public Product updateProducts(Product prod);
	public void deleteProduct(int id);
	public List<Product> getAllProduct(Product prod);
	public List<Product> searchProduct(String search);
	public Map<String, Integer> getCategoryDistribution();
}
