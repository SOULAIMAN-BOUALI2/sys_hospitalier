package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import org.springframework.stereotype.Service;

@Service
public class MedecinService {

    private final MedecinRepository medecinRepository;

    public MedecinService(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    public Medecin updateMedecin(Long id, Medecin newData) {
        Medecin m = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        m.setMatricule(newData.getMatricule());
        m.setSpecialite(newData.getSpecialite());
        m.setDisponibilite(newData.isDisponibilite());

        return medecinRepository.save(m);
    }
}
