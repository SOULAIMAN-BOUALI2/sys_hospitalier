package com.hospital.hospital_management_pfa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospitalisation")
public class Hospitalisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hosp")
    private Long idHosp;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_medecin", nullable = false)
    private Medecin medecin;

    @Column(name = "date_admission")
    private LocalDateTime dateAdmission;

    @Column(name = "date_sortie")
    private LocalDateTime dateSortie;

    private String chambre;
    private String motif;

    @Column(name = "etat_patient")
    private String etatPatient;

    @Column(name = "diagnostic_initial")
    private String diagnosticInitial;

    public Long getIdHosp() { return idHosp; }
    public void setIdHosp(Long idHosp) { this.idHosp = idHosp; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
    public LocalDateTime getDateAdmission() { return dateAdmission; }
    public void setDateAdmission(LocalDateTime dateAdmission) { this.dateAdmission = dateAdmission; }
    public LocalDateTime getDateSortie() { return dateSortie; }
    public void setDateSortie(LocalDateTime dateSortie) { this.dateSortie = dateSortie; }
    public String getChambre() { return chambre; }
    public void setChambre(String chambre) { this.chambre = chambre; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getEtatPatient() { return etatPatient; }
    public void setEtatPatient(String etatPatient) { this.etatPatient = etatPatient; }
    public String getDiagnosticInitial() { return diagnosticInitial; }
    public void setDiagnosticInitial(String diagnosticInitial) { this.diagnosticInitial = diagnosticInitial; }
}