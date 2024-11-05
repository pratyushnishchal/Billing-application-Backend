package com.billingapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billingapplication.entity.Admin;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
	public Admin findByEmail(String email);
	public boolean existsByEmail(String email);
}
