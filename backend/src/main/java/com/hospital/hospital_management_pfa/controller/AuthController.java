package com.hospital.hospital_management_pfa.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
            System.out.println("BACK RECEIVED : " + body);

        return Map.of(
                "email", body.get("email"),
                "role", "ADMIN"
        );
    }
}