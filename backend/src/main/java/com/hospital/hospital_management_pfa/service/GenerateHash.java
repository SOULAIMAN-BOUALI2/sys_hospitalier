package com.hospital.hospital_management_pfa.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Admin1234!");
        System.out.println("HASH : " + hash);
        String hash1 = encoder.encode("medecin123");
        System.out.println("medecin123 : " + hash1);

        String hash2 = encoder.encode("admin123");
        System.out.println("admin123 : " + hash2);

        String hash3 = encoder.encode("infirmier123");
        System.out.println("infirmier123 : " + hash3);

        // Vérification immédiate
        System.out.println("Vérifié : " + encoder.matches("Admin1234!", hash));
    }
}