package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.dto.PatientRequestDTO;
import com.hospital.hospital_management_pfa.dto.PatientResponseDTO;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import com.hospital.hospital_management_pfa.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medecin")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    private final PatientService patientService;
    private final MedecinRepository medecinRepository;

    public PatientController(PatientService patientService,
                             MedecinRepository medecinRepository) {
        this.patientService = patientService;
        this.medecinRepository = medecinRepository;
    }

    // GET /api/medecin/me?email=...
    @GetMapping("/me")
    public ResponseEntity<Long> getMedecinIdByEmail(@RequestParam String email) {
        Medecin medecin = medecinRepository.findByUtilisateurEmail(email)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        return ResponseEntity.ok(medecin.getId());
    }

    // GET /api/medecin/patients
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // GET /api/medecin/patients/{id}
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // POST /api/medecin/patients
    @PostMapping("/patients")
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody PatientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.createPatient(dto));
    }

    // PUT /api/medecin/patients/{id}
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequestDTO dto) {
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    // DELETE /api/medecin/patients/{id}
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}