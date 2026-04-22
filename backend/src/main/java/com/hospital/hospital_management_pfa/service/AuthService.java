package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.CreateUtilisateurRequest;
import com.hospital.hospital_management_pfa.dto.LoginRequest;
import com.hospital.hospital_management_pfa.dto.LoginResponse;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import com.hospital.hospital_management_pfa.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email reçu : " + request.getEmail());
        System.out.println("MotDePasse reçu : " + request.getMotDePasse());

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ Utilisateur NON trouvé pour : " + request.getEmail());
                    return new RuntimeException("Utilisateur introuvable");
                });

        System.out.println("✅ Utilisateur trouvé : " + utilisateur.getEmail());
        System.out.println("Hash en base : " + utilisateur.getMotDePasse());
        boolean match = passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse());
        System.out.println("Password match : " + match);

        if (!match) {
            System.out.println("❌ Mot de passe incorrect");
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (utilisateur.getStatutCompte() == null || utilisateur.getStatutCompte().equals("INACTIF")) {
            utilisateur.setStatutCompte("ACTIF");
            utilisateurRepository.save(utilisateur);
        }

        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole());
        System.out.println("✅ Token généré avec succès");
        return new LoginResponse(
                token,
                utilisateur.getRole(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail()
        );
    }

    public void createUtilisateur(CreateUtilisateurRequest request) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(request.getRole());
        utilisateur.setStatutCompte("INACTIF");
        utilisateurRepository.save(utilisateur);
    }
}