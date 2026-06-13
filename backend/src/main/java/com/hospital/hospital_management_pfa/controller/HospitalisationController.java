package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.dto.HospitalisationRequestDTO;
import com.hospital.hospital_management_pfa.dto.HospitalisationResponseDTO;
import com.hospital.hospital_management_pfa.service.HospitalisationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HospitalisationController {

    private final HospitalisationService hospitalisationService;

    public HospitalisationController(HospitalisationService hospitalisationService) {
        this.hospitalisationService = hospitalisationService;
    }

    @GetMapping("/admin/hospitalisations")
    public ResponseEntity<List<HospitalisationResponseDTO>> getAll() {
        return ResponseEntity.ok(hospitalisationService.getAllHospitalisations());
    }

    @GetMapping("/hospitalisations/patient/{patientId}")
    public ResponseEntity<List<HospitalisationResponseDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(hospitalisationService.getHospitalisationsByPatient(patientId));
    }

    @GetMapping("/medecin/{medecinId}/hospitalisations")
    public ResponseEntity<List<HospitalisationResponseDTO>> getByMedecin(@PathVariable Long medecinId) {
        return ResponseEntity.ok(hospitalisationService.getHospitalisationsByMedecin(medecinId));
    }

    @PostMapping("/hospitalisations")
    public ResponseEntity<HospitalisationResponseDTO> create(@RequestBody HospitalisationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hospitalisationService.createHospitalisation(dto));
    }

    @PutMapping("/hospitalisations/{id}")
    public ResponseEntity<HospitalisationResponseDTO> update(@PathVariable Long id,
                                                             @RequestBody HospitalisationRequestDTO dto) {
        return ResponseEntity.ok(hospitalisationService.updateHospitalisation(id, dto));
    }

    @DeleteMapping("/hospitalisations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hospitalisationService.deleteHospitalisation(id);
        return ResponseEntity.noContent().build();
    }
}