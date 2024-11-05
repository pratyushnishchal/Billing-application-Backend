package com.billingapplication.service;

import com.billingapplication.entity.Admin;

public interface AdminService {
	public Admin validateAdmin(String email, String password);
	public boolean existsEmail(String email);
	public Admin getAdminById(int id);
	public Admin updateAdmin(Admin admin);
	public String getEmailById(int id);
}
