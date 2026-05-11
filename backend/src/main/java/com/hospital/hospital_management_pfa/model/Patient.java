package com.hospital.hospital_management_pfa.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private Long idPatient;

    private String nom;
    private String prenom;

    @Column(name = "numero_dossier", unique = true, nullable = false)
    private String numeroDossier;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    private String sexe;
    private String adresse;
    private String telephone;

    @Column(name = "groupe_sanguin")
    private String groupeSanguin;

    @Column(name = "personne_urgence")
    private String personneUrgence;

    @Column(name = "telephone_urgence")
    private String telephoneUrgence;

    // ← plus de relation Medecin

    public Long getIdPatient() { return idPatient; }
    public void setIdPatient(Long idPatient) { this.idPatient = idPatient; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNumeroDossier() { return numeroDossier; }
    public void setNumeroDossier(String numeroDossier) { this.numeroDossier = numeroDossier; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }
    public String getPersonneUrgence() { return personneUrgence; }
    public void setPersonneUrgence(String personneUrgence) { this.personneUrgence = personneUrgence; }
    public String getTelephoneUrgence() { return telephoneUrgence; }
    public void setTelephoneUrgence(String telephoneUrgence) { this.telephoneUrgence = telephoneUrgence; }
}