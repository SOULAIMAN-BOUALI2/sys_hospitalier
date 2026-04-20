package com.example.systemhospitalier

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import java.security.MessageDigest

object AuthRepository {

    // SHA-256 hash
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────
    suspend fun login(email: String, password: String): Pair<String, Long>? {
        val hashed = hashPassword(password)

        val result = SupabaseClient.client.postgrest
            .from("utilisateur")
            .select {
                filter {
                    eq("email", email)
                    eq("mot_de_passe", hashed)
                }
            }
            .decodeSingleOrNull<Utilisateur>()

        result ?: return null

        // Vérifie si c'est un médecin
        val medecin = SupabaseClient.client.postgrest
            .from("medecin")
            .select {
                filter { eq("id_user", result.id) }
            }
            .decodeSingleOrNull<Medecin>()

        if (medecin != null) return Pair("medecin", result.id)

        // Vérifie si c'est un infirmier
        val infirmier = SupabaseClient.client.postgrest
            .from("infirmier")
            .select {
                filter { eq("id_user", result.id) }
            }
            .decodeSingleOrNull<Infirmier>()

        if (infirmier != null) return Pair("infirmier", result.id)

        // Sinon admin
        return Pair("admin", result.id)
    }

    // ── SIGNUP MÉDECIN ───────────────────────────────────────────────────────
    suspend fun signupMedecin(
        nom: String,
        prenom: String,
        email: String,
        password: String,
        matricule: String,
        specialite: String
    ): Boolean {
        return try {
            val hashed = hashPassword(password)

            // 1. Insérer dans utilisateur
            val user = SupabaseClient.client.postgrest
                .from("utilisateur")
                .insert(
                    Utilisateur(
                        nom = nom,
                        prenom = prenom,
                        email = email,
                        motDePasse = hashed
                    )
                ) {
                    select()
                }
                .decodeSingle<Utilisateur>()

            // 2. Insérer dans medecin
            SupabaseClient.client.postgrest
                .from("medecin")
                .insert(
                    Medecin(
                        matricule = matricule,
                        specialite = specialite,
                        disponibilite = true,
                        idUser = user.id
                    )
                )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("SUPABASE_ERROR", "Erreur: ${e.message}")
            android.util.Log.e("SUPABASE_ERROR", "Cause: ${e.cause}")
            false
        }
    }

    // ── SIGNUP INFIRMIER ─────────────────────────────────────────────────────
    suspend fun signupInfirmier(
        nom: String,
        prenom: String,
        email: String,
        password: String,
        matricule: String,
        service: String,
        shift: String
    ): Boolean {
        return try {
            val hashed = hashPassword(password)

            // 1. Insérer dans utilisateur
            val user = SupabaseClient.client.postgrest
                .from("utilisateur")
                .insert(
                    Utilisateur(
                        nom = nom,
                        prenom = prenom,
                        email = email,
                        motDePasse = hashed
                    )
                ) {
                    select()
                }
                .decodeSingle<Utilisateur>()

            // 2. Insérer dans infirmier
            SupabaseClient.client.postgrest
                .from("infirmier")
                .insert(
                    Infirmier(
                        matricule = matricule,
                        service = service,
                        shift = shift,
                        idUser = user.id
                    )
                )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("SUPABASE_ERROR", "Erreur: ${e.message}")
            android.util.Log.e("SUPABASE_ERROR", "Cause: ${e.cause}")
            false
        }
    }
}