export interface Consultation {
  idConsultation: number;
  patientId: number;
  patientNomComplet: string;
  medecinId: number;
  medecinNomComplet: string;
  dateConsultation: string;
  motif: string;
  diagnostic: string;
  traitement: string;
  notes: string;
  createdAt: string;
}

export interface ConsultationFormData {
  patientId: number;
  medecinId: number;
  dateConsultation: string;
  motif: string;
  diagnostic: string;
  traitement: string;
  notes: string;
}