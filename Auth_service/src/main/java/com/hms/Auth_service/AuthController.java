package com.hms.Auth_service;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository repository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public AuthController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Map<String, String> home() {

        return Map.of(
                "message",
                "Auth Service is running on port 8085"
        );
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(
            @RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");
        String role = request.getOrDefault("role", "STAFF");

        if (username == null ||
                password == null ||
                username.isBlank() ||
                password.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error",
                            "username and password are required"
                    ));
        }

        if (repository.existsByUsername(username)) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error",
                            "Username already exists"
                    ));
        }

        User user = new User(
                username,
                encoder.encode(password),
                role
        );

        User saved = repository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "User registered successfully",
                        "username",
                        saved.getUsername(),
                        "role",
                        saved.getRole()
                ));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(
            @RequestBody Map<String, String> request) {

        try {

            String username = request.get("username");
            String password = request.get("password");

            System.out.println("===== LOGIN START =====");
            System.out.println("Username: " + username);

            if (username == null ||
                    password == null ||
                    username.isBlank() ||
                    password.isBlank()) {

                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error",
                                "username and password are required"
                        ));
            }

            System.out.println("Searching user...");

            Optional<User> user =
                    repository.findByUsername(username);

            System.out.println(
                    "User found: " + user.isPresent()
            );

            if (user.isEmpty()) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "error",
                                "Invalid username or password"
                        ));
            }

            System.out.println("Checking password...");

            boolean passwordCorrect =
                    encoder.matches(
                            password,
                            user.get().getPassword()
                    );

            System.out.println(
                    "Password correct: " + passwordCorrect
            );

            if (!passwordCorrect) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "error",
                                "Invalid username or password"
                        ));
            }

            System.out.println("Password correct!");
            System.out.println("Generating JWT...");

            String token =
                    JwtUtil.generateToken(
                            user.get().getUsername(),
                            user.get().getRole()
                    );

            System.out.println("JWT generated successfully!");
            System.out.println("===== LOGIN SUCCESS =====");

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Login successful",
                            "username",
                            user.get().getUsername(),
                            "role",
                            user.get().getRole(),
                            "token",
                            token
                    )
            );

        } catch (Exception e) {

            System.out.println("===== LOGIN ERROR =====");
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error",
                            e.getClass().getSimpleName(),
                            "message",
                            e.getMessage() == null
                                    ? "Unknown error"
                                    : e.getMessage()
                    ));
        }
    }

    @GetMapping("/auth/validate")
    public ResponseEntity<Object> validate(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String header) {

        if (header == null ||
                !header.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "valid",
                            false,
                            "error",
                            "Missing token"
                    ));
        }

        try {

            Claims claims =
                    JwtUtil.validate(
                            header.substring(7)
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "valid",
                            true,
                            "username",
                            claims.getSubject(),
                            "role",
                            claims.get(
                                    "role",
                                    String.class
                            ),
                            "expiresAt",
                            claims.getExpiration()
                                    .toString()
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "valid",
                            false,
                            "error",
                            "Invalid or expired token"
                    ));
        }
    }
}