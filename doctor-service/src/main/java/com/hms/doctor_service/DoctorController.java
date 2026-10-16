package com.hms.doctor_service; 
  
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
  
import java.util.List; 
import java.util.Map; 
import java.util.Optional; 
  
/** 
 * All the REST endpoints of the Doctor Service. 
 */ 
@RestController 
@CrossOrigin(origins = "*") 
public class DoctorController { 
  
    private final DoctorRepository repository; 
  
    public DoctorController(DoctorRepository repository) { 
        this.repository = repository; 
    } 
  
    // ---------- 1. Home route ---------- 
    @GetMapping("/") 
    public Map<String, String> home() { 
        return Map.of("message", "Doctor Service is running on port 8082 (database: doctor_db)"); 
    } 
  
    // ---------- 2. Get ALL doctors ---------- 
    @GetMapping("/doctors") 
    public List<Doctor> getAllDoctors() { 

        return repository.findAll(); 
    } 
  
    // ---------- 3. Get ONE doctor by id ---------- 
    @GetMapping("/doctors/{id}") 
    public ResponseEntity<Object> getDoctor(@PathVariable Long id) { 
        Optional<Doctor> doctor = repository.findById(id); 
        if (doctor.isPresent()) { 
            return ResponseEntity.ok(doctor.get()); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Doctor not found")); 
    } 
  
    // ---------- 4. Add a NEW doctor ---------- 
    @PostMapping("/doctors") 
    public ResponseEntity<Object> addDoctor(@RequestBody Doctor newDoctor) { 
        newDoctor.setId(null); 
        Doctor saved = repository.save(newDoctor); 
        return ResponseEntity.status(HttpStatus.CREATED) 
                .body(Map.of("message", "Doctor added successfully", 
                             "doctor", saved)); 
    } 
  
    // ---------- 5. Delete a doctor ---------- 
    @DeleteMapping("/doctors/{id}") 
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id) { 
        if (repository.existsById(id)) { 
            repository.deleteById(id); 
            return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully")); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Doctor not found")); 
    } 
} 