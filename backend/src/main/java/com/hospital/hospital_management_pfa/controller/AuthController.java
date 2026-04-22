package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.dto.CreateUtilisateurRequest;
import com.hospital.hospital_management_pfa.dto.LoginRequest;
import com.hospital.hospital_management_pfa.dto.LoginResponse;
import com.hospital.hospital_management_pfa.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println("=== CONTROLLER LOGIN APPELÉ ===");
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody CreateUtilisateurRequest request) {
        authService.createUtilisateur(request);
        return ResponseEntity.ok("Utilisateur créé avec succès");
    }
}