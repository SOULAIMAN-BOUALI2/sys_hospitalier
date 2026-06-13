package com.hospital.hospital_management_pfa.dto;

import java.time.LocalDateTime;

public class HospitalisationRequestDTO {
    private Long patientId;
    private Long medecinId;
    private LocalDateTime dateAdmission;
    private LocalDateTime dateSortie;
    private String chambre;
    private String motif;
    private String etatPatient;
    private String diagnosticInitial;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }
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