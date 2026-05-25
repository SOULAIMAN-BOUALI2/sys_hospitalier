package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.dto.ConsultationRequestDTO;
import com.hospital.hospital_management_pfa.dto.ConsultationResponseDTO;
import com.hospital.hospital_management_pfa.service.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping("/admin/consultations")
    public ResponseEntity<List<ConsultationResponseDTO>> getAll() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @GetMapping("/consultations/patient/{patientId}")
    public ResponseEntity<List<ConsultationResponseDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getConsultationsByPatient(patientId));
    }

    @GetMapping("/medecin/{medecinId}/consultations")
    public ResponseEntity<List<ConsultationResponseDTO>> getByMedecin(@PathVariable Long medecinId) {
        return ResponseEntity.ok(consultationService.getConsultationsByMedecin(medecinId));
    }

    @PostMapping("/consultations")
    public ResponseEntity<ConsultationResponseDTO> create(@RequestBody ConsultationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.createConsultation(dto));
    }

    @PutMapping("/consultations/{id}")
    public ResponseEntity<ConsultationResponseDTO> update(@PathVariable Long id,
                                                          @RequestBody ConsultationRequestDTO dto) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, dto));
    }

    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}