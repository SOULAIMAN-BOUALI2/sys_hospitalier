package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.CreateUtilisateurRequest;
import com.hospital.hospital_management_pfa.dto.UserDetailsResponse;
import com.hospital.hospital_management_pfa.model.Medecin;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.InfirmierRepository;
import com.hospital.hospital_management_pfa.repository.MedecinRepository;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private MedecinRepository medecinRepository;

    @Mock
    private InfirmierRepository infirmierRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    public void testCreateUserMedecin() {
        // Arrange
        CreateUtilisateurRequest request = new CreateUtilisateurRequest();
        request.setNom("Curie");
        request.setPrenom("Marie");
        request.setEmail("marie.curie@hospital.com");
        request.setMotDePasse("pass123");
        request.setRole("MEDECIN");
        request.setMatricule("DOC001");
        request.setSpecialite("Radiologie");

        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pass");

        // Act
        adminService.createUser(request);

        // Assert
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
        verify(medecinRepository, times(1)).save(any(Medecin.class));
    }

    @Test
    public void testGetUserSuccess() {
        // Arrange
        String email = "doctor@test.com";
        Utilisateur user = new Utilisateur();
        user.setEmail(email);
        user.setNom("House");
        user.setPrenom("Gregory");
        user.setRole("MEDECIN");

        Medecin medecin = new Medecin();
        medecin.setMatricule("M123");
        medecin.setSpecialite("Diagnostic");

        when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(medecinRepository.findByUtilisateur(user)).thenReturn(medecin);

        // Act
        UserDetailsResponse response = adminService.getUser(email);

        // Assert
        assertNotNull(response);
        assertEquals("House", response.getNom());
        assertEquals("M123", response.getMatricule());
        assertEquals("Diagnostic", response.getSpecialite());
    }
}
