package com.hospital.hospital_management_pfa.dto;

import lombok.Data;

@Data
public class CreateUtilisateurRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String role;

    private String matricule;
    private String specialite;
    private String service;
    private String shift;

    // getters + setters
}