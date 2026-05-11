package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.PatientRequestDTO;
import com.hospital.hospital_management_pfa.dto.PatientResponseDTO;
import com.hospital.hospital_management_pfa.model.Patient;
import com.hospital.hospital_management_pfa.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ── Lister tous les patients ───────────────────────────────────────────────
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ── Récupérer un patient par ID ───────────────────────────────────────────
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé : " + id));
        return toResponseDTO(patient);
    }

    // ── Créer un patient ──────────────────────────────────────────────────────
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        if (patientRepository.existsByNumeroDossier(dto.getNumeroDossier())) {
            throw new RuntimeException("Numéro de dossier déjà existant : " + dto.getNumeroDossier());
        }
        Patient patient = new Patient();
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setNumeroDossier(dto.getNumeroDossier());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setSexe(dto.getSexe());
        patient.setAdresse(dto.getAdresse());
        patient.setTelephone(dto.getTelephone());
        patient.setGroupeSanguin(dto.getGroupeSanguin());
        patient.setPersonneUrgence(dto.getPersonneUrgence());
        patient.setTelephoneUrgence(dto.getTelephoneUrgence());
        return toResponseDTO(patientRepository.save(patient));
    }

    // ── Modifier un patient ───────────────────────────────────────────────────
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé : " + id));
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setNumeroDossier(dto.getNumeroDossier());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setSexe(dto.getSexe());
        patient.setAdresse(dto.getAdresse());
        patient.setTelephone(dto.getTelephone());
        patient.setGroupeSanguin(dto.getGroupeSanguin());
        patient.setPersonneUrgence(dto.getPersonneUrgence());
        patient.setTelephoneUrgence(dto.getTelephoneUrgence());
        return toResponseDTO(patientRepository.save(patient));
    }

    // ── Supprimer un patient ──────────────────────────────────────────────────
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient non trouvé : " + id);
        }
        patientRepository.deleteById(id);
    }

    // ── Mapper entité → DTO ───────────────────────────────────────────────────
    private PatientResponseDTO toResponseDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setIdPatient(patient.getIdPatient());
        dto.setNom(patient.getNom());
        dto.setPrenom(patient.getPrenom());
        dto.setNumeroDossier(patient.getNumeroDossier());
        dto.setDateNaissance(patient.getDateNaissance());
        dto.setSexe(patient.getSexe());
        dto.setAdresse(patient.getAdresse());
        dto.setTelephone(patient.getTelephone());
        dto.setGroupeSanguin(patient.getGroupeSanguin());
        dto.setPersonneUrgence(patient.getPersonneUrgence());
        dto.setTelephoneUrgence(patient.getTelephoneUrgence());
        return dto;
    }
}