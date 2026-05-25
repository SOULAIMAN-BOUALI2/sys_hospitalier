import { useState, useEffect } from "react";
import type { CSSProperties } from "react";
import type { Patient } from "../types/patientTypes";
import type { Consultation } from "../types/consultationTypes";
import type { Hospitalisation } from "../types/hospitalisationTypes";
import { consultationService } from "../services/consultationService";
import { hospitalisationService } from "../services/hospitalisationService";

interface Props {
  patient: Patient;
  onClose: () => void;
}

type Tab = "infos" | "consultations" | "hospitalisations";

export default function PatientDetailModal({ patient, onClose }: Props) {
  const [tab, setTab] = useState<Tab>("infos");
  const [consultations, setConsultations] = useState<Consultation[] | null>(null);
  const [hospitalisations, setHospitalisations] = useState<Hospitalisation[] | null>(null);

  // null = pas encore chargé, [] = chargé mais vide
  const loadingC = tab === "consultations" && consultations === null;
  const loadingH = tab === "hospitalisations" && hospitalisations === null;

  useEffect(() => {
    if (tab === "consultations" && consultations === null) {
      consultationService.getConsultationsByPatient(patient.idPatient)
        .then(data => setConsultations(data))
        .catch(() => setConsultations([]));
    }
    if (tab === "hospitalisations" && hospitalisations === null) {
      hospitalisationService.getHospitalisationsByPatient(patient.idPatient)
        .then(data => setHospitalisations(data))
        .catch(() => setHospitalisations([]));
    }
  }, [tab, patient.idPatient, consultations, hospitalisations]);

  const etatColor = (etat: string) => {
    if (etat === "CRITIQUE") return { background: "#FCEBEB", color: "#A32D2D" };
    if (etat === "GRAVE") return { background: "#FEF3C7", color: "#92400E" };
    if (etat === "STABLE") return { background: "#D1FAE5", color: "#065F46" };
    return { background: "#EEF2FF", color: "#3730A3" };
  };

  return (
    <div style={s.overlay}>
      <div style={s.modal}>
        {/* Header */}
        <div style={s.header}>
          <div style={s.headerLeft}>
            <div style={s.avatar}>{patient.prenom?.[0]}{patient.nom?.[0]}</div>
            <div>
              <h2 style={s.title}>{patient.prenom} {patient.nom}</h2>
              <p style={s.subtitle}>Dossier n° {patient.numeroDossier}</p>
            </div>
          </div>
          <button style={s.closeBtn} onClick={onClose}>✕</button>
        </div>

        {/* Tabs */}
        <div style={s.tabs}>
          {(["infos", "consultations", "hospitalisations"] as Tab[]).map(t => (
            <button key={t} style={{ ...s.tab, ...(tab === t ? s.tabActive : {}) }}
              onClick={() => setTab(t)}>
              {t === "infos" ? "📋 Informations" : t === "consultations" ? "🩺 Consultations" : "🏥 Hospitalisations"}
            </button>
          ))}
        </div>

        {/* Body */}
        <div style={s.body}>

          {/* Tab Infos */}
          {tab === "infos" && (
            <div style={s.grid}>
              {[
                ["Date de naissance", patient.dateNaissance || "—"],
                ["Sexe", patient.sexe === "M" ? "Homme" : patient.sexe === "F" ? "Femme" : "—"],
                ["Téléphone", patient.telephone || "—"],
                ["Groupe sanguin", patient.groupeSanguin || "—"],
                ["Adresse", patient.adresse || "—"],
                ["Personne urgence", patient.personneUrgence || "—"],
                ["Tél. urgence", patient.telephoneUrgence || "—"],
              ].map(([label, value]) => (
                <div key={label} style={s.infoCard}>
                  <div style={s.infoLabel}>{label}</div>
                  <div style={s.infoValue}>{value}</div>
                </div>
              ))}
            </div>
          )}

          {/* Tab Consultations */}
          {tab === "consultations" && (
            loadingC ? (
              <div style={s.center}>Chargement...</div>
            ) : (consultations ?? []).length === 0 ? (
              <div style={s.center}>Aucune consultation enregistrée.</div>
            ) : (
              <div style={s.list}>
                {(consultations ?? []).map(c => (
                  <div key={c.idConsultation} style={s.card}>
                    <div style={s.cardHeader}>
                      <span style={s.cardDate}>🗓 {new Date(c.dateConsultation).toLocaleString("fr-FR")}</span>
                      <span style={s.cardDoc}>Dr. {c.medecinNomComplet}</span>
                    </div>
                    <div style={s.cardBody}>
                      <div style={s.cardRow}><span style={s.cardLabel}>Motif</span><span>{c.motif || "—"}</span></div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Diagnostic</span><span>{c.diagnostic || "—"}</span></div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Traitement</span><span>{c.traitement || "—"}</span></div>
                      {c.notes && <div style={s.cardRow}><span style={s.cardLabel}>Notes</span><span>{c.notes}</span></div>}
                    </div>
                  </div>
                ))}
              </div>
            )
          )}

          {/* Tab Hospitalisations */}
          {tab === "hospitalisations" && (
            loadingH ? (
              <div style={s.center}>Chargement...</div>
            ) : (hospitalisations ?? []).length === 0 ? (
              <div style={s.center}>Aucune hospitalisation enregistrée.</div>
            ) : (
              <div style={s.list}>
                {(hospitalisations ?? []).map(h => (
                  <div key={h.idHosp} style={s.card}>
                    <div style={s.cardHeader}>
                      <span style={s.cardDate}>🗓 Admission : {new Date(h.dateAdmission).toLocaleString("fr-FR")}</span>
                      <span style={{ ...s.badge, ...etatColor(h.etatPatient) }}>{h.etatPatient?.replace("_", " ")}</span>
                    </div>
                    <div style={s.cardBody}>
                      <div style={s.cardRow}><span style={s.cardLabel}>Chambre</span><span>{h.chambre || "—"}</span></div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Motif</span><span>{h.motif || "—"}</span></div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Diagnostic initial</span><span>{h.diagnosticInitial || "—"}</span></div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Date de sortie</span>
                        <span>{h.dateSortie ? new Date(h.dateSortie).toLocaleString("fr-FR") : "En cours"}</span>
                      </div>
                      <div style={s.cardRow}><span style={s.cardLabel}>Médecin</span><span>Dr. {h.medecinNomComplet}</span></div>
                    </div>
                  </div>
                ))}
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
}

const s: Record<string, CSSProperties> = {
  overlay: { position: "fixed", inset: 0, background: "rgba(0,0,0,0.5)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000 },
  modal: { background: "#fff", borderRadius: 16, width: "100%", maxWidth: 680, maxHeight: "90vh", display: "flex", flexDirection: "column", boxShadow: "0 24px 80px rgba(0,0,0,0.25)" },
  header: { display: "flex", justifyContent: "space-between", alignItems: "center", padding: "20px 24px", borderBottom: "1px solid #f0f0f0" },
  headerLeft: { display: "flex", alignItems: "center", gap: 14 },
  avatar: { width: 46, height: 46, borderRadius: "50%", background: "#E6F1FB", color: "#185FA5", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: 700, fontSize: 16 },
  title: { fontSize: 17, fontWeight: 700, color: "#111", margin: 0 },
  subtitle: { fontSize: 12, color: "#888", marginTop: 2 },
  closeBtn: { background: "none", border: "none", fontSize: 18, cursor: "pointer", color: "#aaa" },
  tabs: { display: "flex", borderBottom: "1px solid #f0f0f0", padding: "0 24px" },
  tab: { background: "none", border: "none", padding: "12px 16px", fontSize: 13, cursor: "pointer", color: "#888", borderBottom: "2px solid transparent", fontWeight: 500 },
  tabActive: { color: "#185FA5", borderBottom: "2px solid #185FA5" },
  body: { flex: 1, overflowY: "auto", padding: "20px 24px" },
  grid: { display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 },
  infoCard: { background: "#fafafa", borderRadius: 10, padding: "12px 16px", border: "1px solid #f0f0f0" },
  infoLabel: { fontSize: 11, color: "#999", fontWeight: 500, marginBottom: 4, textTransform: "uppercase" as const },
  infoValue: { fontSize: 14, color: "#111", fontWeight: 500 },
  list: { display: "flex", flexDirection: "column", gap: 14 },
  card: { border: "1px solid #eee", borderRadius: 12, overflow: "hidden" },
  cardHeader: { background: "#f9f9f9", padding: "10px 16px", display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid #eee" },
  cardDate: { fontSize: 13, color: "#444", fontWeight: 500 },
  cardDoc: { fontSize: 12, color: "#888" },
  cardBody: { padding: "12px 16px", display: "flex", flexDirection: "column", gap: 8 },
  cardRow: { display: "flex", gap: 8, fontSize: 13 },
  cardLabel: { fontWeight: 600, color: "#555", minWidth: 120 },
  badge: { borderRadius: 99, padding: "2px 10px", fontSize: 11, fontWeight: 600 },
  center: { textAlign: "center", color: "#888", padding: "3rem 0" },
};