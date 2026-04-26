package com.hospital.hospital_management_pfa.model;

import jakarta.persistence.*;

@Entity
public class Infirmier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_infirmier;

    private String matricule;
    private String service;
    private String shift;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    // getters & setters

    public Long getId_infirmier() {
        return id_infirmier;
    }

    public void setId_infirmier(Long id_infirmier) {
        this.id_infirmier = id_infirmier;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}