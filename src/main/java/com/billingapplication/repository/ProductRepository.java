package com.billingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.billingapplication.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("SELECT p FROM Product p WHERE p.prodName LIKE %:search% OR p.prodDescription LIKE %:search%")
	public List<Product> searchProduct(@Param("search")String search);
	@Query("SELECT COUNT(id) FROM Product")
	public int countProducts();
	@Query("SELECT AVG(price) FROM Product")
	public double avgPricePerProduct();
	@Query("SELECT SUM(price) FROM Product")
	public int inventoryValue();
//	public List<Product> findAll();
	
}
