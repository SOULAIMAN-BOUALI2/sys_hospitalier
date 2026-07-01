package com.example.systemhospitalier

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConsultationTest {

    @Test
    fun `test consultation creation and attributes`() {
        val consultation = Consultation(
            idConsultation = 10L,
            patientId = 1L,
            medecinId = 5L,
            dateConsultation = "2026-06-17",
            motif = "Fièvre",
            diagnostic = "Grippe",
            traitement = "Paracétamol",
            notes = "Repos recommandé"
        )

        assertEquals(10L, consultation.idConsultation)
        assertEquals(1L, consultation.patientId)
        assertEquals(5L, consultation.medecinId)
        assertEquals("2026-06-17", consultation.dateConsultation)
        assertEquals("Fièvre", consultation.motif)
        assertEquals("Grippe", consultation.diagnostic)
        assertEquals("Paracétamol", consultation.traitement)
        assertEquals("Repos recommandé", consultation.notes)
    }

    @Test
    fun `test consultation default notes`() {
        val consultation = Consultation(
            patientId = 1L,
            medecinId = 5L,
            dateConsultation = "2026-06-17",
            motif = "Check-up",
            diagnostic = "Sain",
            traitement = "Aucun"
        )

        assertNull(consultation.notes)
        assertEquals(0L, consultation.idConsultation)
    }
}
