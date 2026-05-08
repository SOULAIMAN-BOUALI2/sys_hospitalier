package com.hospital.hospital_management_pfa.repository;

import com.hospital.hospital_management_pfa.model.Infirmier;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfirmierRepository extends JpaRepository<Infirmier, Long>  {

    void deleteByUtilisateur(Utilisateur utilisateur);
}
