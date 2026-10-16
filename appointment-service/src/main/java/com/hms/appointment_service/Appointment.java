package com.hms.appointment_service;
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 

import jakarta.persistence.Table; 
  
/** 
 * Appointment ENTITY class -> becomes the "appointments" table 
 * in appointment_db. 
 */ 
@Entity 
@Table(name = "appointments") 
public class Appointment { 
  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    private Long patientId; 
    private String patientName; 
    private Long doctorId; 
    private String doctorName; 
    private String specialization; 
    private int fee; 
    private String date; 
    private String status; 
  
    public Appointment() { 
    } 
  
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
  
    public Long getPatientId() { return patientId; } 
    public void setPatientId(Long patientId) { this.patientId = patientId; } 
  
    public String getPatientName() { return patientName; } 
    public void setPatientName(String patientName) { this.patientName = patientName; } 
  
    public Long getDoctorId() { return doctorId; } 
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; } 
  
    public String getDoctorName() { return doctorName; } 
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; } 
  
    public String getSpecialization() { return specialization; } 
    public void setSpecialization(String specialization) { this.specialization = specialization; } 
  
    public int getFee() { return fee; } 
    public void setFee(int fee) { this.fee = fee; } 
  
    public String getDate() { return date; } 
    public void setDate(String date) { this.date = date; } 
  
    public String getStatus() { return status; } 
    public void setStatus(String status) { this.status = status; } 
} 