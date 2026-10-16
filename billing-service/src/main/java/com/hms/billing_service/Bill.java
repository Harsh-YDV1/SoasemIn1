package com.hms.billing_service; 
  
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.Table; 
  
/** 
 * Bill ENTITY class -> becomes the "bills" table in billing_db. 
 */ 
@Entity 
@Table(name = "bills") 

public class Bill { 
  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    private Long appointmentId; 
    private String patientName; 
    private String doctorName; 
    private String date; 
    private int doctorFee; 
    private int registrationFee; 
    private double tax; 
    private double totalAmount; 
    private String paymentStatus; 
  
    public Bill() { 
    } 
  
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
  
    public Long getAppointmentId() { return appointmentId; } 
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; } 
  
    public String getPatientName() { return patientName; } 
    public void setPatientName(String patientName) { this.patientName = patientName; } 
  
    public String getDoctorName() { return doctorName; } 
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; } 
  
    public String getDate() { return date; } 
    public void setDate(String date) { this.date = date; } 
  
    public int getDoctorFee() { return doctorFee; } 
    public void setDoctorFee(int doctorFee) { this.doctorFee = doctorFee; } 
  
    public int getRegistrationFee() { return registrationFee; } 
    public void setRegistrationFee(int registrationFee) { this.registrationFee = registrationFee; } 
  
    public double getTax() { return tax; } 
    public void setTax(double tax) { this.tax = tax; } 
  
    public double getTotalAmount() { return totalAmount; } 
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; } 
  
    public String getPaymentStatus() { return paymentStatus; } 
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; } 
} 
 