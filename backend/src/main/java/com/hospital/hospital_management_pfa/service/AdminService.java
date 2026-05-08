package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.CreateUtilisateurRequest;
import com.hospital.hospital_management_pfa.model.Infirmier;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.InfirmierRepository;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final MedecinRepository medecinRepository;
    private final InfirmierRepository infirmierRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UtilisateurRepository utilisateurRepository,
                        MedecinRepository medecinRepository,
                        InfirmierRepository infirmierRepository,
                        PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.medecinRepository = medecinRepository;
        this.infirmierRepository = infirmierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUtilisateurRequest request) {

        // ✅ créer user
        Utilisateur u = new Utilisateur();
        u.setNom(request.getNom());
        u.setPrenom(request.getPrenom());
        u.setEmail(request.getEmail());
        u.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        u.setRole(request.getRole());
        u.setStatutCompte("ACTIF");

        utilisateurRepository.save(u);

        // ✅ créer selon role
        if (request.getRole().equals("MEDECIN")) {
            Medecin m = new Medecin();
            m.setMatricule(request.getMatricule());
            m.setSpecialite(request.getSpecialite());
            m.setUtilisateur(u);

            medecinRepository.save(m);
        }

        if (request.getRole().equals("INFIRMIER")) {
            Infirmier i = new Infirmier();
            i.setMatricule(request.getMatricule());
            i.setService(request.getService());
            i.setShift(request.getShift());
            i.setUtilisateur(u);

            infirmierRepository.save(i);
        }
    }
    @Transactional
    public void deleteUser(String email) {

        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("-----------------recived to delete --------------"+ email);

        medecinRepository.deleteByUtilisateur(user);
        infirmierRepository.deleteByUtilisateur(user);

        utilisateurRepository.delete(user);
    }
}