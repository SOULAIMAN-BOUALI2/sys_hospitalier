package com.example.systemhospitalier.network

import com.example.systemhospitalier.EtapePriseEnCharge
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GeminiServiceTest {

    @Test
    fun `test gemini service instantiation`() {
        val service = GeminiService()
        assertNotNull(service)
    }

    @Test
    fun `test etape prise en charge creation`() {
        val etape = EtapePriseEnCharge(
            idEtape = 1L,
            priseEnChargeId = 10L,
            ordre = 1,
            type = "Diagnostic",
            description = "Examen clinique",
            acteur = "Médecin",
            etat = "A faire"
        )
        
        assertEquals(1, etape.ordre)
        assertEquals("Diagnostic", etape.type)
        assertEquals("A faire", etape.etat)
        assertFalse(etape.estApprouve)
    }

    @Test
    fun `test etape prise en charge default values`() {
        val etape = EtapePriseEnCharge(
            priseEnChargeId = 10L,
            ordre = 2,
            description = "Observation"
        )
        
        assertEquals("A faire", etape.etat)
        assertFalse(etape.estApprouve)
        assertNull(etape.type)
    }
}
