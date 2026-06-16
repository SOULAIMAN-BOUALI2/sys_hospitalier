package com.example.systemhospitalier

import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import java.security.MessageDigest
import org.mindrot.jbcrypt.BCrypt

object AuthRepository {

    // SHA-256 hash
    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────
    suspend fun login(email: String, password: String): Pair<String, Long>? {

        val result = SupabaseClient.client.postgrest
            .from("utilisateur")
            .select {
                filter {
                    eq("email", email)
                }
            }
            .decodeSingleOrNull<Utilisateur>()

        result ?: return null

        if (!BCrypt.checkpw(password, result.motDePasse)) {
            return null
        }

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
}