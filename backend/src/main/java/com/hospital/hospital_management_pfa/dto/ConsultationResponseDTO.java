package com.hospital.hospital_management_pfa.dto;

import java.time.LocalDateTime;

public class ConsultationResponseDTO {
    private Long idConsultation;
    private Long patientId;
    private String patientNomComplet;
    private Long medecinId;
    private String medecinNomComplet;
    private LocalDateTime dateConsultation;
    private String motif;
    private String diagnostic;
    private String traitement;
    private String notes;
    private LocalDateTime createdAt;

    public Long getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Long idConsultation) { this.idConsultation = idConsultation; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getPatientNomComplet() { return patientNomComplet; }
    public void setPatientNomComplet(String patientNomComplet) { this.patientNomComplet = patientNomComplet; }
    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }
    public String getMedecinNomComplet() { return medecinNomComplet; }
    public void setMedecinNomComplet(String medecinNomComplet) { this.medecinNomComplet = medecinNomComplet; }
    public LocalDateTime getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(LocalDateTime dateConsultation) { this.dateConsultation = dateConsultation; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }
    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}