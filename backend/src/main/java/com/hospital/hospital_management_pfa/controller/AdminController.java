package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.dto.CreateUtilisateurRequest;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import com.hospital.hospital_management_pfa.service.AdminService;
import com.hospital.hospital_management_pfa.service.MedecinService;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final UtilisateurRepository repo;
    private final MedecinService medecinService;
    private final AdminService adminService;

    public AdminController(UtilisateurRepository repo, MedecinService medecinService, AdminService adminService) {
        this.repo = repo;
        this.medecinService = medecinService;
        this.adminService = adminService;

    }

    @GetMapping("/adminDash")
    public List<Utilisateur> getUsers() {
        return repo.findAll();
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody CreateUtilisateurRequest request) {
        adminService.createUser(request);
    }

    @PutMapping("/medecins/{id}")
    public Medecin update(@PathVariable Long id, @RequestBody Medecin m) {
        return medecinService.updateMedecin(id, m);
    }
}