package com.billingapplication.service;

import java.util.List;

import com.billingapplication.entity.User;

public interface UserService {
	public User createAccountant(User user);
	public boolean existsEmail(String email);
	public User updateAccountant(User user);
	public List<User> getAllAccountant(User user);
	public void deleteAccountant(int id);
	public User validateAccountant(String email,String password);
	public String getEmailById(int id);
	public User getAccountantById(int id);
}
