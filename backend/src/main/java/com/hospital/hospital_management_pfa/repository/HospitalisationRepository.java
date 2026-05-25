package com.hospital.hospital_management_pfa.repository;

import com.hospital.hospital_management_pfa.model.Hospitalisation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospitalisationRepository extends JpaRepository<Hospitalisation, Long> {
    List<Hospitalisation> findByPatientIdPatientOrderByDateAdmissionDesc(Long patientId);
    List<Hospitalisation> findByMedecinIdOrderByDateAdmissionDesc(Long medecinId);
}