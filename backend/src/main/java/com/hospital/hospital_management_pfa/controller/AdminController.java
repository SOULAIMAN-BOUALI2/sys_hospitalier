package com.hospital.hospital_management_pfa.controller;

import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final UtilisateurRepository repo;

    public AdminController(UtilisateurRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/adminDash")
    public List<Utilisateur> getUsers() {
        return repo.findAll();
    }
}