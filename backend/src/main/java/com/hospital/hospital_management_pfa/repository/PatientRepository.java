package com.hospital.hospital_management_pfa.repository;

import com.hospital.hospital_management_pfa.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {



    // Vérifier si numéro dossier existe déjà
    boolean existsByNumeroDossier(String numeroDossier);
}