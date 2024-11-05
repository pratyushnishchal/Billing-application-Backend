package com.billingapplication.repository;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingapplication.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
	@Query("SELECT SUM(totalAmount) FROM Invoice")
	public Double totalSales();
	@Query("SELECT COUNT(id) FROM Invoice")
	public int noOfInvoices();
	@Query("SELECT AVG(totalAmount) FROM Invoice")
	public double avgOrderValue();
	@Query("SELECT SUM(totalAmount) FROM Invoice WHERE DATE(invoiceDate) = CURDATE()")
	public Double todaysSales();
	@Query("SELECT SUM(totalAmount) FROM Invoice WHERE YEARWEEK(invoiceDate, 1) = YEARWEEK(CURDATE(), 1)")
	public Double salesForWeek();
	@Query("SELECT i.invoiceDate, SUM(i.totalAmount) FROM Invoice i GROUP BY i.invoiceDate")
    List<Object[]> findDailySales();
    @Query("SELECT COUNT(id) FROM Invoice WHERE DATE(invoiceDate) = CURDATE()")
    public int todaysOrderNo();
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate >= :startOfWeek AND i.invoiceDate <= :endOfWeek")
    List<Invoice> findInvoicesForCurrentWeek(@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);
    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId AND i.paymentStatus = 'Unpaid'")
    List<Invoice> findByCustomerId(@Param("customerId") int customerId);
    @Query("SELECT COUNT(id) FROM Invoice i WHERE i.paymentStatus='Paid'")
    public int completedOrder();
    @Query("SELECT SUM(totalAmount) FROM Invoice i WHERE i.paymentStatus='Unpaid'")
    public double pendingAmount();
    @Query("SELECT i FROM Invoice i WHERE i.customer.id=:customerId")
    public List<Invoice> orderHistory(@Param("customerId") int customerId);
}