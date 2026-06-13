import type { Consultation, ConsultationFormData } from "../types/consultationTypes";

const API_BASE = "http://localhost:8080/api";

const getAuthHeaders = (): Record<string, string> => ({
  "Content-Type": "application/json",
  "Authorization": `Bearer ${localStorage.getItem("token") ?? ""}`,
});

export const consultationService = {
  getAllConsultations: async (): Promise<Consultation[]> => {
    const res = await fetch(`${API_BASE}/admin/consultations`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement consultations");
    return res.json();
  },

  getConsultationsByPatient: async (patientId: number): Promise<Consultation[]> => {
    const res = await fetch(`${API_BASE}/consultations/patient/${patientId}`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement consultations patient");
    return res.json();
  },

  getConsultationsByMedecin: async (medecinId: number): Promise<Consultation[]> => {
    const res = await fetch(`${API_BASE}/medecin/${medecinId}/consultations`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement consultations médecin");
    return res.json();
  },

  createConsultation: async (data: ConsultationFormData): Promise<Consultation> => {
    const res = await fetch(`${API_BASE}/consultations`, {
      method: "POST", headers: getAuthHeaders(), body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur création consultation");
    return res.json();
  },

  updateConsultation: async (id: number, data: ConsultationFormData): Promise<Consultation> => {
    const res = await fetch(`${API_BASE}/consultations/${id}`, {
      method: "PUT", headers: getAuthHeaders(), body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur modification consultation");
    return res.json();
  },

  deleteConsultation: async (id: number): Promise<void> => {
    const res = await fetch(`${API_BASE}/consultations/${id}`, {
      method: "DELETE", headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Erreur suppression consultation");
  },
};