package com.hospital.hospital_management_pfa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {

    private String nom;
    private String prenom;
    private String email;
    private String role;

    private String matricule;
    private String specialite;

    private String service;
    private String shift;
}
