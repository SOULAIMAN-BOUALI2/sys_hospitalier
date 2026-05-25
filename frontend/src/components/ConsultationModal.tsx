import { useState, useEffect } from "react";
import type { CSSProperties } from "react";
import type { Consultation, ConsultationFormData } from "../types/consultationTypes";

interface Props {
  consultation: Consultation | null;
  patientId: number;
  medecinId: number;
  patientNom: string;
  onClose: () => void;
  onSave: (data: ConsultationFormData) => Promise<void>;
}

export default function ConsultationModal({ consultation, patientId, medecinId, patientNom, onClose, onSave }: Props) {
  const [form, setForm] = useState<ConsultationFormData>({
    patientId, medecinId,
    dateConsultation: new Date().toISOString().slice(0, 16),
    motif: "", diagnostic: "", traitement: "", notes: "",
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (consultation) {
      setForm({
        patientId: consultation.patientId,
        medecinId: consultation.medecinId,
        dateConsultation: consultation.dateConsultation?.slice(0, 16) ?? "",
        motif: consultation.motif ?? "",
        diagnostic: consultation.diagnostic ?? "",
        traitement: consultation.traitement ?? "",
        notes: consultation.notes ?? "",
      });
    }
  }, [consultation]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async () => {
    if (!form.dateConsultation || !form.motif) {
      setError("La date et le motif sont obligatoires.");
      return;
    }
    setSaving(true); setError("");
    try {
      await onSave(form);
      onClose();
    } catch {
      setError("Erreur lors de l'enregistrement.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div style={s.overlay}>
      <div style={s.modal}>
        <div style={s.header}>
          <div>
            <h2 style={s.title}>{consultation ? "Modifier la consultation" : "Nouvelle consultation"}</h2>
            <p style={s.subtitle}>Patient : <strong>{patientNom}</strong></p>
          </div>
          <button style={s.closeBtn} onClick={onClose}>✕</button>
        </div>

        <div style={s.body}>
          {error && <div style={s.errorBox}>{error}</div>}

          <div style={s.field}>
            <label style={s.label}>Date de consultation *</label>
            <input style={s.input} type="datetime-local" name="dateConsultation"
              value={form.dateConsultation} onChange={handleChange} />
          </div>

          <div style={s.field}>
            <label style={s.label}>Motif *</label>
            <input style={s.input} type="text" name="motif"
              placeholder="Motif de la consultation" value={form.motif} onChange={handleChange} />
          </div>

          <div style={s.field}>
            <label style={s.label}>Diagnostic</label>
            <textarea style={s.textarea} name="diagnostic"
              placeholder="Diagnostic établi..." value={form.diagnostic} onChange={handleChange} />
          </div>

          <div style={s.field}>
            <label style={s.label}>Traitement prescrit</label>
            <textarea style={s.textarea} name="traitement"
              placeholder="Traitement, médicaments..." value={form.traitement} onChange={handleChange} />
          </div>

          <div style={s.field}>
            <label style={s.label}>Notes / Observations</label>
            <textarea style={s.textarea} name="notes"
              placeholder="Notes complémentaires..." value={form.notes} onChange={handleChange} />
          </div>
        </div>

        <div style={s.footer}>
          <button style={s.cancelBtn} onClick={onClose}>Annuler</button>
          <button style={s.saveBtn} onClick={handleSubmit} disabled={saving}>
            {saving ? "Enregistrement..." : consultation ? "Modifier" : "Enregistrer"}
          </button>
        </div>
      </div>
    </div>
  );
}

const s: Record<string, CSSProperties> = {
  overlay: { position: "fixed", inset: 0, background: "rgba(0,0,0,0.45)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000 },
  modal: { background: "#fff", borderRadius: 14, width: "100%", maxWidth: 560, maxHeight: "90vh", display: "flex", flexDirection: "column", boxShadow: "0 20px 60px rgba(0,0,0,0.2)" },
  header: { display: "flex", justifyContent: "space-between", alignItems: "flex-start", padding: "20px 24px 16px", borderBottom: "1px solid #f0f0f0" },
  title: { fontSize: 17, fontWeight: 600, color: "#111", margin: 0 },
  subtitle: { fontSize: 13, color: "#666", marginTop: 3 },
  closeBtn: { background: "none", border: "none", fontSize: 18, cursor: "pointer", color: "#aaa" },
  body: { padding: "20px 24px", overflowY: "auto", flex: 1, display: "flex", flexDirection: "column", gap: 14 },
  footer: { padding: "16px 24px", borderTop: "1px solid #f0f0f0", display: "flex", justifyContent: "flex-end", gap: 10 },
  field: { display: "flex", flexDirection: "column", gap: 5 },
  label: { fontSize: 12, fontWeight: 500, color: "#555" },
  input: { padding: "9px 12px", border: "1px solid #ddd", borderRadius: 8, fontSize: 14, outline: "none" },
  textarea: { padding: "9px 12px", border: "1px solid #ddd", borderRadius: 8, fontSize: 14, outline: "none", minHeight: 80, resize: "vertical" as const, fontFamily: "inherit" },
  errorBox: { background: "#FCEBEB", color: "#A32D2D", borderRadius: 8, padding: "10px 14px", fontSize: 13 },
  cancelBtn: { background: "none", border: "1px solid #ddd", borderRadius: 8, padding: "9px 20px", fontSize: 14, cursor: "pointer", color: "#555" },
  saveBtn: { background: "#185FA5", color: "#fff", border: "none", borderRadius: 8, padding: "9px 24px", fontSize: 14, cursor: "pointer", fontWeight: 600 },
};