package com.hospital.hospital_management_pfa.repository;

import com.hospital.hospital_management_pfa.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientIdPatientOrderByDateConsultationDesc(Long patientId);
    List<Consultation> findByMedecinIdOrderByDateConsultationDesc(Long medecinId);
}