package com.hospital.hospital_management_pfa.service;

import com.hospital.hospital_management_pfa.dto.LoginRequest;
import com.hospital.hospital_management_pfa.dto.LoginResponse;
import com.hospital.hospital_management_pfa.model.Utilisateur;
import com.hospital.hospital_management_pfa.repository.UtilisateurRepository;
import com.hospital.hospital_management_pfa.security.JwtUtil;
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
public class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setMotDePasse("password123");

        Utilisateur user = new Utilisateur();
        user.setEmail("test@example.com");
        user.setMotDePasse("hashed_password");
        user.setRole("ADMIN");
        user.setNom("Nom");
        user.setPrenom("Prenom");
        user.setStatutCompte("ACTIF");

        when(utilisateurRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasse())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole())).thenReturn("fake_token");

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake_token", response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertEquals("test@example.com", response.getEmail());
        verify(utilisateurRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testLoginFailure() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setMotDePasse("wrongpass");

        when(utilisateurRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(request));
        verify(utilisateurRepository, times(1)).findByEmail(request.getEmail());
    }
}
