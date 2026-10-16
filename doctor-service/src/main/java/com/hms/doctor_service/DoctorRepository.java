package com.hms.doctor_service; 
  
import org.springframework.data.jpa.repository.JpaRepository; 
  
/** 
 * Spring Data JPA repository - all SQL is generated automatically. 
 */ 
public interface DoctorRepository extends JpaRepository<Doctor, Long> { 
}