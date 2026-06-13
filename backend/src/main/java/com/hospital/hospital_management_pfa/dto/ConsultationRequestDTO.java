package com.hospital.hospital_management_pfa.dto;

import java.time.LocalDateTime;

public class ConsultationRequestDTO {
    private Long patientId;
    private Long medecinId;
    private LocalDateTime dateConsultation;
    private String motif;
    private String diagnostic;
    private String traitement;
    private String notes;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }
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
}