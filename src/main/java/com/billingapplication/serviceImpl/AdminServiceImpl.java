package com.billingapplication.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Admin;
import com.billingapplication.entity.Customer;
import com.billingapplication.exception.RecordNotFoundException;
import com.billingapplication.repository.AdminRepository;
import com.billingapplication.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public Admin validateAdmin(String email, String password) {
		Admin admin=adminRepo.findByEmail(email);
		if(admin!=null && admin.getPassword().equals(password)) {
			return admin;
		}
		return null;
	}
	@Override
	public boolean existsEmail(String email) {
		return adminRepo.existsByEmail(email);
	}
	@Override
	public Admin getAdminById(int id) {
		Optional<Admin> ad=adminRepo.findById(id);
		if(ad.isPresent()) {
			Admin a=ad.get();
			return a;
		}
		else {
			throw new RecordNotFoundException("Admin with id " + id + " not found");
		}
	}
	@Override
	public Admin updateAdmin(Admin admin) {
		Optional<Admin> ad=adminRepo.findById(admin.getId());
		if(ad.isPresent()) {
			Admin a=ad.get();
			a.setName(admin.getName());
			a.setEmail(admin.getEmail());
			a.setPassword(admin.getPassword());
			a.setRole("ROLE_ADMIN");
			return adminRepo.save(a);
		}
		return null;
	}
	@Override
	public String getEmailById(int id) {
		Optional<Admin> c=adminRepo.findById(id);
		if(c.isPresent()) {
			Admin us=c.get();
			return us.getEmail();
		}
		else {
			throw new RecordNotFoundException("Customer with id " + id + " not found");
		}
	}

}

