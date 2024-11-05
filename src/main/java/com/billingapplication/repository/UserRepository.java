package com.billingapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billingapplication.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public boolean existsByEmail(String email);
	public Optional<User> findById(int id);
	public User findByEmail(String email);
	@Query("SELECT COUNT(id) FROM User")
	public int countAccountant();
}
