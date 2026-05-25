import { useState, useEffect } from "react";
import type { CSSProperties } from "react";
import type { Hospitalisation, HospitalisationFormData } from "../types/hospitalisationTypes";

interface Props {
  hospitalisation: Hospitalisation | null;
  patientId: number;
  medecinId: number;
  patientNom: string;
  onClose: () => void;
  onSave: (data: HospitalisationFormData) => Promise<void>;
}

const ETATS = ["STABLE", "CRITIQUE", "GRAVE", "EN_OBSERVATION", "EN_RECUPERATION"];

export default function HospitalisationModal({ hospitalisation, patientId, medecinId, patientNom, onClose, onSave }: Props) {
  const [form, setForm] = useState<HospitalisationFormData>({
    patientId, medecinId,
    dateAdmission: new Date().toISOString().slice(0, 16),
    dateSortie: "", chambre: "", motif: "", etatPatient: "STABLE", diagnosticInitial: "",
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (hospitalisation) {
      setForm({
        patientId: hospitalisation.patientId,
        medecinId: hospitalisation.medecinId,
        dateAdmission: hospitalisation.dateAdmission?.slice(0, 16) ?? "",
        dateSortie: hospitalisation.dateSortie?.slice(0, 16) ?? "",
        chambre: hospitalisation.chambre ?? "",
        motif: hospitalisation.motif ?? "",
        etatPatient: hospitalisation.etatPatient ?? "STABLE",
        diagnosticInitial: hospitalisation.diagnosticInitial ?? "",
      });
    }
  }, [hospitalisation]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async () => {
    if (!form.dateAdmission || !form.motif) {
      setError("La date d'admission et le motif sont obligatoires.");
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
            <h2 style={s.title}>{hospitalisation ? "Modifier l'hospitalisation" : "Nouvelle hospitalisation"}</h2>
            <p style={s.subtitle}>Patient : <strong>{patientNom}</strong></p>
          </div>
          <button style={s.closeBtn} onClick={onClose}>✕</button>
        </div>

        <div style={s.body}>
          {error && <div style={s.errorBox}>{error}</div>}

          <div style={s.row}>
            <div style={s.field}>
              <label style={s.label}>Date d'admission *</label>
              <input style={s.input} type="datetime-local" name="dateAdmission"
                value={form.dateAdmission} onChange={handleChange} />
            </div>
            <div style={s.field}>
              <label style={s.label}>Date de sortie (prévue)</label>
              <input style={s.input} type="datetime-local" name="dateSortie"
                value={form.dateSortie ?? ""} onChange={handleChange} />
            </div>
          </div>

          <div style={s.row}>
            <div style={s.field}>
              <label style={s.label}>Chambre</label>
              <input style={s.input} type="text" name="chambre"
                placeholder="Ex: 204-B" value={form.chambre} onChange={handleChange} />
            </div>
            <div style={s.field}>
              <label style={s.label}>État du patient</label>
              <select style={s.input} name="etatPatient" value={form.etatPatient} onChange={handleChange}>
                {ETATS.map(e => <option key={e} value={e}>{e.replace("_", " ")}</option>)}
              </select>
            </div>
          </div>

          <div style={s.field}>
            <label style={s.label}>Motif *</label>
            <input style={s.input} type="text" name="motif"
              placeholder="Motif d'hospitalisation" value={form.motif} onChange={handleChange} />
          </div>

          <div style={s.field}>
            <label style={s.label}>Diagnostic initial</label>
            <textarea style={s.textarea} name="diagnosticInitial"
              placeholder="Diagnostic à l'admission..." value={form.diagnosticInitial} onChange={handleChange} />
          </div>
        </div>

        <div style={s.footer}>
          <button style={s.cancelBtn} onClick={onClose}>Annuler</button>
          <button style={s.saveBtn} onClick={handleSubmit} disabled={saving}>
            {saving ? "Enregistrement..." : hospitalisation ? "Modifier" : "Enregistrer"}
          </button>
        </div>
      </div>
    </div>
  );
}

const s: Record<string, CSSProperties> = {
  overlay: { position: "fixed", inset: 0, background: "rgba(0,0,0,0.45)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000 },
  modal: { background: "#fff", borderRadius: 14, width: "100%", maxWidth: 600, maxHeight: "90vh", display: "flex", flexDirection: "column", boxShadow: "0 20px 60px rgba(0,0,0,0.2)" },
  header: { display: "flex", justifyContent: "space-between", alignItems: "flex-start", padding: "20px 24px 16px", borderBottom: "1px solid #f0f0f0" },
  title: { fontSize: 17, fontWeight: 600, color: "#111", margin: 0 },
  subtitle: { fontSize: 13, color: "#666", marginTop: 3 },
  closeBtn: { background: "none", border: "none", fontSize: 18, cursor: "pointer", color: "#aaa" },
  body: { padding: "20px 24px", overflowY: "auto", flex: 1, display: "flex", flexDirection: "column", gap: 14 },
  footer: { padding: "16px 24px", borderTop: "1px solid #f0f0f0", display: "flex", justifyContent: "flex-end", gap: 10 },
  row: { display: "flex", gap: 12 },
  field: { display: "flex", flexDirection: "column", gap: 5, flex: 1 },
  label: { fontSize: 12, fontWeight: 500, color: "#555" },
  input: { padding: "9px 12px", border: "1px solid #ddd", borderRadius: 8, fontSize: 14, outline: "none", width: "100%", boxSizing: "border-box" as const },
  textarea: { padding: "9px 12px", border: "1px solid #ddd", borderRadius: 8, fontSize: 14, outline: "none", minHeight: 80, resize: "vertical" as const, fontFamily: "inherit" },
  errorBox: { background: "#FCEBEB", color: "#A32D2D", borderRadius: 8, padding: "10px 14px", fontSize: 13 },
  cancelBtn: { background: "none", border: "1px solid #ddd", borderRadius: 8, padding: "9px 20px", fontSize: 14, cursor: "pointer", color: "#555" },
  saveBtn: { background: "#0E7C5B", color: "#fff", border: "none", borderRadius: 8, padding: "9px 24px", fontSize: 14, cursor: "pointer", fontWeight: 600 },
};