package com.hms.billing_service; 
  
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
  
/** 
 * BILLING SERVICE | Hospital Management System (SOA Project) 
 * Runs on  : http://localhost:8084 
 * Database : PostgreSQL -> billing_db 
 * Purpose  : Generates a bill for an appointment. It TALKS to the 
 *            Appointment Service over HTTP to get the details. 
 */ 
@SpringBootApplication 
public class BillingServiceApplication { 
  
    public static void main(String[] args) { 
        SpringApplication.run(BillingServiceApplication.class, args); 
    } 
} 
 