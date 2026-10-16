package com.hms.billing_service; 
  
import org.springframework.data.jpa.repository.JpaRepository; 
  
/** 
Hospital Management System — Java Full Stack SOA with PostgreSQL    |    Page 33 
 * Spring Data JPA repository - all SQL is generated automatically. 
 */ 
public interface BillRepository extends JpaRepository<Bill, Long> { 
} 
 