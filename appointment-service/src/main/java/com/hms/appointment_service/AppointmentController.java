package com.hms.appointment_service; 
  
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import org.springframework.web.client.HttpClientErrorException; 
import org.springframework.web.client.ResourceAccessException; 
import org.springframework.web.client.RestTemplate; 
  
import java.util.List; 
import java.util.Map; 
import java.util.Optional; 
  
/** 
 * The most important service of the project! 
 * Before booking, it CALLS the Patient Service and the Doctor Service 
 * to make sure the patient and doctor really exist, then saves the 
 * appointment in PostgreSQL (appointment_db). 
 */ 
@RestController 
@CrossOrigin(origins = "*") 

public class AppointmentController { 
  
    // ---------- Addresses of the other services ---------- 
    private static final String PATIENT_SERVICE_URL = "http://localhost:8081"; 
    private static final String DOCTOR_SERVICE_URL  = "http://localhost:8082"; 
  
    // RestTemplate is Spring's tool for calling other services over HTTP 
    private final RestTemplate restTemplate = new RestTemplate(); 
  
    private final AppointmentRepository repository; 
  
    public AppointmentController(AppointmentRepository repository) { 
        this.repository = repository; 
    } 
  
    // ---------- 1. Home route ---------- 
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Appointment Service is running on port 8083 (database: appointment_db)");
    }
    // ---------- 2. Get ALL appointments ---------- 
    @GetMapping("/appointments") 
    public List<Appointment> getAllAppointments() { 
        return repository.findAll(); 
    } 
  
    // ---------- 3. Get ONE appointment by id ---------- 
    @GetMapping("/appointments/{id}") 
    public ResponseEntity<Object> getAppointment(@PathVariable Long id) { 
        Optional<Appointment> appointment = repository.findById(id); 
        if (appointment.isPresent()) { 
            return ResponseEntity.ok(appointment.get()); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Appointment not found")); 
    } 
  
    // ---------- 4. Book a NEW appointment ---------- 
    //  Steps performed: 
    //   (a) Ask Patient Service : "Does this patient exist?" 
    //   (b) Ask Doctor Service  : "Does this doctor exist?" 
    //   (c) If both say YES -> save the appointment in PostgreSQL 
    @PostMapping("/appointments") 
    public ResponseEntity<Object> bookAppointment(@RequestBody Map<String, Object> request) { 
  
        Long patientId = ((Number) request.get("patientId")).longValue(); 
        Long doctorId  = ((Number) request.get("doctorId")).longValue(); 
        String date    = (String) request.get("date"); 
  
        // (a) Verify patient by CALLING the Patient Service 
        Map<String, Object> patient; 
        try { 
            patient = restTemplate.getForObject( 
                    PATIENT_SERVICE_URL + "/patients/" + patientId, Map.class); 
        } catch (HttpClientErrorException.NotFound e) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                    .body(Map.of("error", "Patient with id " + patientId + " not found")); 
        } catch (ResourceAccessException e) { 
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE) 
                    .body(Map.of("error", "Patient Service is not running. Start it first!")); 
        } 

  
        // (b) Verify doctor by CALLING the Doctor Service 
        Map<String, Object> doctor; 
        try { 
            doctor = restTemplate.getForObject( 
                    DOCTOR_SERVICE_URL + "/doctors/" + doctorId, Map.class); 
        } catch (HttpClientErrorException.NotFound e) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                    .body(Map.of("error", "Doctor with id " + doctorId + " not found")); 
        } catch (ResourceAccessException e) { 
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE) 
                    .body(Map.of("error", "Doctor Service is not running. Start it first!")); 
        } 
  
        // (c) Both exist -> create and SAVE the appointment in PostgreSQL 
        Appointment a = new Appointment(); 
        a.setPatientId(patientId); 
        a.setPatientName((String) patient.get("name")); 
        a.setDoctorId(doctorId); 
        a.setDoctorName((String) doctor.get("name")); 
        a.setSpecialization((String) doctor.get("specialization")); 
        a.setFee(((Number) doctor.get("fee")).intValue()); 
        a.setDate(date); 
        a.setStatus("Booked"); 
        Appointment saved = repository.save(a);   // INSERT INTO appointments ... 
  
        return ResponseEntity.status(HttpStatus.CREATED) 
                .body(Map.of("message", "Appointment booked successfully", 
                             "appointment", saved)); 
    } 
  
    // ---------- 5. Cancel an appointment ---------- 
    @DeleteMapping("/appointments/{id}") 
    public ResponseEntity<Object> cancelAppointment(@PathVariable Long id) { 
        if (repository.existsById(id)) { 
            repository.deleteById(id); 
            return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully")); 
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                .body(Map.of("error", "Appointment not found")); 
    } 
} 