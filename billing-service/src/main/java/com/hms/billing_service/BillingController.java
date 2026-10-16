package com.hms.billing_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class BillingController {

    private static final String APPOINTMENT_SERVICE_URL =
            "http://localhost:8083";

    private static final int REGISTRATION_FEE = 100;
    private static final int SERVICE_TAX_PERCENT = 5;

    private final RestTemplate restTemplate = new RestTemplate();
    private final BillRepository repository;

    public BillingController(BillRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message",
                "Billing Service is running on port 8084 (database: billing_db)"
        );
    }

    @GetMapping("/bills")
    public List<Bill> getAllBills() {
        return repository.findAll();
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<Object> getBill(@PathVariable Long id) {

        Optional<Bill> bill = repository.findById(id);

        if (bill.isPresent()) {
            return ResponseEntity.ok(bill.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Bill not found"));
    }

    @PostMapping("/bills")
    public ResponseEntity<Object> generateBill(
            @RequestBody Map<String, Object> request) {

        Long appointmentId =
                ((Number) request.get("appointmentId")).longValue();

        Map<String, Object> appointment;

        try {

            appointment = restTemplate.getForObject(
                    APPOINTMENT_SERVICE_URL +
                            "/appointments/" +
                            appointmentId,
                    Map.class
            );

        } catch (HttpClientErrorException.NotFound e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "error",
                                    "Appointment with id " +
                                            appointmentId +
                                            " not found"
                            )
                    );

        } catch (ResourceAccessException e) {

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(
                            Map.of(
                                    "error",
                                    "Appointment Service is not running. Start it first!"
                            )
                    );
        }

        int doctorFee =
                ((Number) appointment.get("fee")).intValue();

        int subtotal =
                doctorFee + REGISTRATION_FEE;

        double tax =
                subtotal * SERVICE_TAX_PERCENT / 100.0;

        double total =
                subtotal + tax;

        Bill bill = new Bill();

        bill.setAppointmentId(appointmentId);
        bill.setPatientName(
                (String) appointment.get("patientName")
        );
        bill.setDoctorName(
                (String) appointment.get("doctorName")
        );
        bill.setDate(
                (String) appointment.get("date")
        );
        bill.setDoctorFee(doctorFee);
        bill.setRegistrationFee(REGISTRATION_FEE);
        bill.setTax(tax);
        bill.setTotalAmount(total);
        bill.setPaymentStatus("Paid");

        Bill saved = repository.save(bill);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "message",
                                "Bill generated successfully",
                                "bill",
                                saved
                        )
                );
    }
}