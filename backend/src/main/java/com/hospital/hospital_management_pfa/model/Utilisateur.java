package com.hospital.hospital_management_pfa.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "utilisateur")
@Data
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "statut_compte")
    private String statutCompte;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column
    private String role;
}