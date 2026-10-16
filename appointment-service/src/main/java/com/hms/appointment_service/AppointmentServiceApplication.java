package com.hms.appointment_service; 
  
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
  
/** 
 * APPOINTMENT SERVICE | Hospital Management System (SOA Project) 
 * Runs on  : http://localhost:8083 
 * Database : PostgreSQL -> appointment_db 
 * Purpose  : Books appointments. It TALKS to the Patient Service and 
 *            Doctor Service over HTTP (this is the heart of SOA!) 
 */ 
@SpringBootApplication 
public class AppointmentServiceApplication { 
  
    public static void main(String[] args) { 
        SpringApplication.run(AppointmentServiceApplication.class, args); 
    } 
} 