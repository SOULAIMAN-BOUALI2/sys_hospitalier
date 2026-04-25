package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import com.hospital.hospital_management_pfa.service.MedecinService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final UtilisateurRepository repo;
    private final MedecinService medecinService;

    public AdminController(UtilisateurRepository repo, MedecinService medecinService) {
        this.repo = repo;
        this.medecinService = medecinService;

    }

    @GetMapping("/adminDash")
    public List<Utilisateur> getUsers() {
        return repo.findAll();
    }


    @PutMapping("/medecins/{id}")
    public Medecin update(@PathVariable Long id, @RequestBody Medecin m) {
        return medecinService.updateMedecin(id, m);
    }
}