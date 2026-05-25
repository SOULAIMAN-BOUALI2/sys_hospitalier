import type { Hospitalisation, HospitalisationFormData } from "../types/hospitalisationTypes";

const API_BASE = "http://localhost:8080/api";

const getAuthHeaders = (): Record<string, string> => ({
  "Content-Type": "application/json",
  "Authorization": `Bearer ${localStorage.getItem("token") ?? ""}`,
});

export const hospitalisationService = {
  getAllHospitalisations: async (): Promise<Hospitalisation[]> => {
    const res = await fetch(`${API_BASE}/admin/hospitalisations`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement hospitalisations");
    return res.json();
  },

  getHospitalisationsByPatient: async (patientId: number): Promise<Hospitalisation[]> => {
    const res = await fetch(`${API_BASE}/hospitalisations/patient/${patientId}`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement hospitalisations patient");
    return res.json();
  },

  getHospitalisationsByMedecin: async (medecinId: number): Promise<Hospitalisation[]> => {
    const res = await fetch(`${API_BASE}/medecin/${medecinId}/hospitalisations`, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Erreur chargement hospitalisations médecin");
    return res.json();
  },

  createHospitalisation: async (data: HospitalisationFormData): Promise<Hospitalisation> => {
    const res = await fetch(`${API_BASE}/hospitalisations`, {
      method: "POST", headers: getAuthHeaders(), body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur création hospitalisation");
    return res.json();
  },

  updateHospitalisation: async (id: number, data: HospitalisationFormData): Promise<Hospitalisation> => {
    const res = await fetch(`${API_BASE}/hospitalisations/${id}`, {
      method: "PUT", headers: getAuthHeaders(), body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur modification hospitalisation");
    return res.json();
  },

  deleteHospitalisation: async (id: number): Promise<void> => {
    const res = await fetch(`${API_BASE}/hospitalisations/${id}`, {
      method: "DELETE", headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Erreur suppression hospitalisation");
  },
};