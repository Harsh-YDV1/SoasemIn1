package com.hms.patient_service; 
  
import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.context.annotation.Bean; 
  
/** 
 * PATIENT SERVICE | Hospital Management System (SOA Project) 
 * Runs on  : http://localhost:8081 
 * Database : PostgreSQL -> patient_db 
 * Purpose  : Stores and manages Patient records 
 */ 
@SpringBootApplication 
public class PatientServiceApplication { 
  
    public static void main(String[] args) { 
        SpringApplication.run(PatientServiceApplication.class, args); 
    } 
  
    /** 
     * Runs once at startup: if the table is empty, 
     * insert three sample patients so we can test immediately. 
     */ 
    @Bean 
    CommandLineRunner insertSampleData(PatientRepository repository) { 
        return args -> { 
            if (repository.count() == 0) { 
                repository.save(new Patient("Ravi Kumar", 30, "Male", "Fever")); 
                repository.save(new Patient("Sita Devi", 25, "Female", "Cold")); 
                repository.save(new Patient("Anil Sharma", 45, "Male", "Diabetes")); 
                System.out.println(">>> Sample patients inserted into patient_db"); 
            } 
        }; 
    } 
}