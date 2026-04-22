package com.hospital.hospital_management_pfa.dto;

import lombok.Data;

@Data
public class CreateUtilisateurRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String role; // "ADMIN", "MEDECIN", "INFIRMIER"
}