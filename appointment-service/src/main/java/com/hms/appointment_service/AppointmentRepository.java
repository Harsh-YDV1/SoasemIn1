package com.hms.appointment_service; 

  
import org.springframework.data.jpa.repository.JpaRepository; 
  
/** 
 * Spring Data JPA repository - all SQL is generated automatically. 
 */ 
public interface AppointmentRepository extends JpaRepository<Appointment, Long> { 
} 
  