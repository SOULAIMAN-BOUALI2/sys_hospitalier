package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.ConsultationRequestDTO;
import com.hospital.hospital_management_pfa.dto.ConsultationResponseDTO;
import com.hospital.hospital_management_pfa.model.Consultation;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Patient;
import com.hospital.hospital_management_pfa.repository.ConsultationRepository;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import com.hospital.hospital_management_pfa.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;

    public ConsultationService(ConsultationRepository consultationRepository,
                               PatientRepository patientRepository,
                               MedecinRepository medecinRepository) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }

    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        Medecin medecin = medecinRepository.findById(dto.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        Consultation c = new Consultation();
        c.setPatient(patient);
        c.setMedecin(medecin);
        c.setDateConsultation(dto.getDateConsultation());
        c.setMotif(dto.getMotif());
        c.setDiagnostic(dto.getDiagnostic());
        c.setTraitement(dto.getTraitement());
        c.setNotes(dto.getNotes());

        return toDTO(consultationRepository.save(c));
    }

    public List<ConsultationResponseDTO> getAllConsultations() {
        return consultationRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultationResponseDTO> getConsultationsByPatient(Long patientId) {
        return consultationRepository
                .findByPatientIdPatientOrderByDateConsultationDesc(patientId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultationResponseDTO> getConsultationsByMedecin(Long medecinId) {
        return consultationRepository
                .findByMedecinIdOrderByDateConsultationDesc(medecinId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultationResponseDTO updateConsultation(Long id, ConsultationRequestDTO dto) {
        Consultation c = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation non trouvée"));
        c.setDateConsultation(dto.getDateConsultation());
        c.setMotif(dto.getMotif());
        c.setDiagnostic(dto.getDiagnostic());
        c.setTraitement(dto.getTraitement());
        c.setNotes(dto.getNotes());
        return toDTO(consultationRepository.save(c));
    }

    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }

    private ConsultationResponseDTO toDTO(Consultation c) {
        ConsultationResponseDTO dto = new ConsultationResponseDTO();
        dto.setIdConsultation(c.getIdConsultation());
        dto.setPatientId(c.getPatient().getIdPatient());
        dto.setPatientNomComplet(c.getPatient().getPrenom() + " " + c.getPatient().getNom());
        dto.setMedecinId(c.getMedecin().getId());
        dto.setMedecinNomComplet(c.getMedecin().getUtilisateur().getPrenom() + " " + c.getMedecin().getUtilisateur().getNom());
        dto.setDateConsultation(c.getDateConsultation());
        dto.setMotif(c.getMotif());
        dto.setDiagnostic(c.getDiagnostic());
        dto.setTraitement(c.getTraitement());
        dto.setNotes(c.getNotes());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}