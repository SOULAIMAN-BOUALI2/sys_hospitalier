package com.example.systemhospitalier

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PriseEnChargeTest {

    @Test
    fun `test prise en charge creation and attributes`() {
        val pec = PriseEnCharge(
            idPriseEnCharge = 50L,
            patientId = 1L,
            medecinId = 5L,
            dateDebut = "2026-06-17",
            type = "Urgence",
            etat = "En cours",
            symptomes = "Douleur thoracique",
            constantes = "TA: 12/8, Pouls: 80",
            observation = "Patient conscient",
            niveauUrgence = "Elevé",
            recommandationsIA = "ECG immédiat"
        )

        assertEquals(50L, pec.idPriseEnCharge)
        assertEquals("Urgence", pec.type)
        assertEquals("En cours", pec.etat)
        assertEquals("Douleur thoracique", pec.symptomes)
        assertEquals("Elevé", pec.niveauUrgence)
    }

    @Test
    fun `test prise en charge default values`() {
        val pec = PriseEnCharge(
            patientId = 1L,
            medecinId = 5L,
            symptomes = "Toux",
            constantes = "Normales",
            observation = "Rien à signaler",
            niveauUrgence = "Faible"
        )

        assertEquals("Standard", pec.type)
        assertEquals("En cours", pec.etat)
        assertNull(pec.idPriseEnCharge)
    }
}
