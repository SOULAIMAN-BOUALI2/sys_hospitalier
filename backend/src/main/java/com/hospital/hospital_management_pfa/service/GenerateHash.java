package com.hospital.hospital_management_pfa.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Admin1234!");
        System.out.println("HASH : " + hash);

        // Vérification immédiate
        System.out.println("Vérifié : " + encoder.matches("Admin1234!", hash));
    }
}