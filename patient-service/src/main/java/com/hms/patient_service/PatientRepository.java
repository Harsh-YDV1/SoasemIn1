package com.hms.patient_service; 
  
import org.springframework.data.jpa.repository.JpaRepository; 
  

public interface PatientRepository extends JpaRepository<Patient, Long> { 
} 
 