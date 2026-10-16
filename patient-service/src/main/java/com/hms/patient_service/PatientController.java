package com.hms.patient_service; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
  
import java.util.List; 
import java.util.Map; 
import java.util.Optional; 

  
/** 
 * All the REST endpoints of the Patient Service. 
 * The controller no longer stores data itself - it asks the 
 * repository, and the repository talks to PostgreSQL. 
 */ 
@RestController 
@CrossOrigin(origins = "*")   // allows our HTML frontend to call this service 
public class PatientController { 
  
    private final PatientRepository repository; 
  
    // Spring automatically gives us the repository (dependency injection) 
    public PatientController(PatientRepository repository) { 
        this.repository = repository; 
    } 
  
    // ---------- 1. Home route (to check if service is running) ---------- 
    @GetMapping("/") 
    public Map<String, String> home() { 
        return Map.of("message", "Patient Service is running on port 8081 (database: patient_db)"); 
    } 
  
    // ---------- 2. Get ALL patients (SELECT * FROM patients) ---------- 
    @GetMapping("/patients") 
    public List<Patient> getAllPatients() { 
        return repository.findAll(); 
    } 
  
    // ---------- 3. Get ONE patient by id ---------- 
    @GetMapping("/patients/{id}") 
    public ResponseEntity<Object> getPatient(@PathVariable Long id) { 
        Optional<Patient> patient = repository.findById(id); 
        if (patient.isPresent()) { 
            return ResponseEntity.ok(patient.get()); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Patient not found")); 
    } 
  
    // ---------- 4. Add a NEW patient (INSERT INTO patients ...) ---------- 
    @PostMapping("/patients") 
    public ResponseEntity<Object> addPatient(@RequestBody Patient newPatient) { 
        newPatient.setId(null);                    // let PostgreSQL assign the id 
        Patient saved = repository.save(newPatient); 
        return ResponseEntity.status(HttpStatus.CREATED) 
                .body(Map.of("message", "Patient added successfully", 
                             "patient", saved)); 
    } 
  
    // ---------- 5. Delete a patient (DELETE FROM patients WHERE id=..) ---------- 
    @DeleteMapping("/patients/{id}") 
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) { 
        if (repository.existsById(id)) { 
            repository.deleteById(id); 
            return ResponseEntity.ok(Map.of("message", "Patient deleted successfully")); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Patient not found")); 
    } 
}