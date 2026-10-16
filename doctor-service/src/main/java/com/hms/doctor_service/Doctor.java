package com.hms.doctor_service; 
  
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.Table; 
  
/** 
 * Doctor ENTITY class -> becomes the "doctors" table in doctor_db. 
 */ 
@Entity 
@Table(name = "doctors") 
public class Doctor { 

  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    private String name; 
    private String specialization; 
    private int fee; 
  
    public Doctor() { 
    } 
  
    public Doctor(String name, String specialization, int fee) { 
        this.name = name; 
        this.specialization = specialization; 
        this.fee = fee; 
    } 
  
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
  
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 
  
    public String getSpecialization() { return specialization; } 
    public void setSpecialization(String specialization) { this.specialization = specialization; } 
  
    public int getFee() { return fee; } 
    public void setFee(int fee) { this.fee = fee; } 
} 
  