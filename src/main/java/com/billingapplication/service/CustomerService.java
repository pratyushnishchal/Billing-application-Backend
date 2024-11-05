package com.billingapplication.service;

import java.util.List;
import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Subscribe;

public interface CustomerService {
	public Customer createCustomerAcc(Customer customer);
	public List<Customer> getAllCustomer(Customer customer);
	public Customer updateCustomer(Customer customer);
	public void deleteCustomer(int id);
	public boolean existsEmail(String email);
	public String getEmailById(int id);
	public List<Customer> searchCustomer(String search);
	public Customer getCustomerById(int id);
	public Customer validateCustomer(String email, String password);
	public Subscribe subscribeMail(Subscribe subscribe);
	public String addToWallet(int customerid,Double amount);
}
