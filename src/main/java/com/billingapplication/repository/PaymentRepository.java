package com.billingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billingapplication.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{

}
