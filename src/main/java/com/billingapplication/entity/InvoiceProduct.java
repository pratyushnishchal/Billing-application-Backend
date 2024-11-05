package com.billingapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "invoice_products")
public class InvoiceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double productPrice;
    private int quantity;
    private String product_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	@Override
	public String toString() {
		return "InvoiceProduct [id=" + id + ", invoice=" + invoice + ", product=" + product + ", productPrice="
				+ productPrice + ", quantity=" + quantity + ", product_name=" + product_name + "]";
	}
    

    
}
