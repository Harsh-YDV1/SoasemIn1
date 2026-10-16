package com.hms.patient_service; 
  
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.Table; 

  
/** 
 * Patient ENTITY class. 
 * @Entity tells Spring/Hibernate: "make a database table from this class". 
 * Table name : patients   |   Columns : id, name, age, gender, disease 
 */ 
@Entity 
@Table(name = "patients") 
public class Patient { 
  
    @Id                                                  // primary key 
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PostgreSQL auto-generates the id 
    private Long id; 
  
    private String name; 
    private int age; 
    private String gender; 
    private String disease; 
  
    // Empty constructor - REQUIRED by JPA and JSON conversion 
    public Patient() { 
    } 
  
    // Constructor without id (the database assigns the id automatically) 
    public Patient(String name, int age, String gender, String disease) { 
        this.name = name; 
        this.age = age; 
        this.gender = gender; 
        this.disease = disease; 
    } 
  
    // Getters and Setters 
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
  
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 
  
    public int getAge() { return age; } 
    public void setAge(int age) { this.age = age; } 
  
    public String getGender() { return gender; } 
    public void setGender(String gender) { this.gender = gender; } 
  
    public String getDisease() { return disease; } 
    public void setDisease(String disease) { this.disease = disease; } 
}