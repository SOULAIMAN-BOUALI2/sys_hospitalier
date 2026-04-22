package com.hospital.hospital_management_pfa.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String motDePasse;
}