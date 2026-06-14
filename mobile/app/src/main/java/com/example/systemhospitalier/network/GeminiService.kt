package com.example.systemhospitalier.network

import com.example.systemhospitalier.EtapePriseEnCharge
import com.example.systemhospitalier.PriseEnCharge
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.serialization.json.Json
import android.util.Log

class GeminiService {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = GeminiConfig.API_KEY
    )

    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    suspend fun generateEtapes(priseEnCharge: PriseEnCharge): List<EtapePriseEnCharge> {
        val prompt = """
            En tant qu'assistant médical expert, génère un protocole de prise en charge détaillé sous forme de liste JSON d'étapes.
            
            Informations de la prise en charge :
            - Symptômes : ${priseEnCharge.symptomes}
            - Constantes : ${priseEnCharge.constantes}
            - Niveau d'urgence : ${priseEnCharge.niveauUrgence}
            - Observations : ${priseEnCharge.observation}
            
            Format de sortie attendu (JSON uniquement, pas de texte avant ou après) :
            [
              {
                "ordre": 1,
                "type": "Diagnostic/Traitement/Surveillance/etc.",
                "description": "Description précise de l'action",
                "acteur": "Médecin/Infirmier/etc.",
                "etat": "A faire"
              },
              ...
            ]
            
            Assure-toi que les étapes sont cohérentes avec le niveau d'urgence.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonText = response.text?.trim()?.removeSurrounding("```json", "```")?.trim() ?: "[]"
            Log.d("GeminiService", "Response JSON: $jsonText")
            
            val tempEtapes = json.decodeFromString<List<EtapePriseEnChargeSerialized>>(jsonText)
            
            tempEtapes.map { 
                EtapePriseEnCharge(
                    priseEnChargeId = priseEnCharge.idPriseEnCharge ?: 0,
                    ordre = it.ordre,
                    type = it.type,
                    description = it.description,
                    acteur = it.acteur,
                    etat = it.etat ?: "A faire"
                )
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Error generating steps: ${e.message}")
            emptyList()
        }
    }
}

@kotlinx.serialization.Serializable
private data class EtapePriseEnChargeSerialized(
    val ordre: Int,
    val type: String? = null,
    val description: String,
    val acteur: String? = null,
    val etat: String? = "A faire"
)
