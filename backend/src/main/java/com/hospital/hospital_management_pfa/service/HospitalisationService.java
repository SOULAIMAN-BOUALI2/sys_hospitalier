package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.HospitalisationRequestDTO;
import com.hospital.hospital_management_pfa.dto.HospitalisationResponseDTO;
import com.hospital.hospital_management_pfa.model.Hospitalisation;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Patient;
import com.hospital.hospital_management_pfa.repository.HospitalisationRepository;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import com.hospital.hospital_management_pfa.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalisationService {

    private final HospitalisationRepository hospitalisationRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;

    public HospitalisationService(HospitalisationRepository hospitalisationRepository,
                                  PatientRepository patientRepository,
                                  MedecinRepository medecinRepository) {
        this.hospitalisationRepository = hospitalisationRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }

    public HospitalisationResponseDTO createHospitalisation(HospitalisationRequestDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        Medecin medecin = medecinRepository.findById(dto.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        Hospitalisation h = new Hospitalisation();
        h.setPatient(patient);
        h.setMedecin(medecin);
        h.setDateAdmission(dto.getDateAdmission());
        h.setDateSortie(dto.getDateSortie());
        h.setChambre(dto.getChambre());
        h.setMotif(dto.getMotif());
        h.setEtatPatient(dto.getEtatPatient());
        h.setDiagnosticInitial(dto.getDiagnosticInitial());

        return toDTO(hospitalisationRepository.save(h));
    }

    public List<HospitalisationResponseDTO> getAllHospitalisations() {
        return hospitalisationRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<HospitalisationResponseDTO> getHospitalisationsByPatient(Long patientId) {
        return hospitalisationRepository
                .findByPatientIdPatientOrderByDateAdmissionDesc(patientId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<HospitalisationResponseDTO> getHospitalisationsByMedecin(Long medecinId) {
        return hospitalisationRepository
                .findByMedecinIdOrderByDateAdmissionDesc(medecinId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public HospitalisationResponseDTO updateHospitalisation(Long id, HospitalisationRequestDTO dto) {
        Hospitalisation h = hospitalisationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospitalisation non trouvée"));
        h.setDateAdmission(dto.getDateAdmission());
        h.setDateSortie(dto.getDateSortie());
        h.setChambre(dto.getChambre());
        h.setMotif(dto.getMotif());
        h.setEtatPatient(dto.getEtatPatient());
        h.setDiagnosticInitial(dto.getDiagnosticInitial());
        return toDTO(hospitalisationRepository.save(h));
    }

    public void deleteHospitalisation(Long id) {
        hospitalisationRepository.deleteById(id);
    }

    private HospitalisationResponseDTO toDTO(Hospitalisation h) {
        HospitalisationResponseDTO dto = new HospitalisationResponseDTO();
        dto.setIdHosp(h.getIdHosp());
        dto.setPatientId(h.getPatient().getIdPatient());
        dto.setPatientNomComplet(h.getPatient().getPrenom() + " " + h.getPatient().getNom());
        dto.setMedecinId(h.getMedecin().getId());
        dto.setMedecinNomComplet(h.getMedecin().getUtilisateur().getPrenom() + " " + h.getMedecin().getUtilisateur().getNom());
        dto.setDateAdmission(h.getDateAdmission());
        dto.setDateSortie(h.getDateSortie());
        dto.setChambre(h.getChambre());
        dto.setMotif(h.getMotif());
        dto.setEtatPatient(h.getEtatPatient());
        dto.setDiagnosticInitial(h.getDiagnosticInitial());
        return dto;
    }
}