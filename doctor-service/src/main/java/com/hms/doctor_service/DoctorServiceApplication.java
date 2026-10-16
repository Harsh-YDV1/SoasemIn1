package com.hms.doctor_service; 
  
import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.context.annotation.Bean; 
  
/** 
 * DOCTOR SERVICE | Hospital Management System (SOA Project) 
 * Runs on  : http://localhost:8082 
 * Database : PostgreSQL -> doctor_db 
 */ 
@SpringBootApplication 
public class DoctorServiceApplication { 
  
    public static void main(String[] args) { 
        SpringApplication.run(DoctorServiceApplication.class, args); 
    } 
  
    @Bean 
    CommandLineRunner insertSampleData(DoctorRepository repository) { 
        return args -> { 
            if (repository.count() == 0) { 
                repository.save(new Doctor("Dr. Ramesh Rao", "General Medicine", 300)); 
                repository.save(new Doctor("Dr. Priya Menon", "Cardiology", 700)); 
                repository.save(new Doctor("Dr. Suresh Babu", "Orthopedics", 500)); 
                System.out.println(">>> Sample doctors inserted into doctor_db"); 
            } 
        }; 
    } 
} 
  