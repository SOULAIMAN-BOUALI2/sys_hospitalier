import { useEffect, useState, useCallback } from "react";
import type { CSSProperties } from "react";
import { patientService } from "../../services/Patientservice";
import PatientModal from "../../components/PatientModal";
import DeleteConfirmModal from "../../components/DeleteConfirmModal";
import type { Patient, PatientFormData } from "../../types/patientTypes";

export default function DashboardMedecin() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [showModal, setShowModal] = useState(false);
  const [editPatient, setEditPatient] = useState<Patient | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Patient | null>(null);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const fetchPatients = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const data = await patientService.getPatients();
      setPatients(data);
    } catch {
      setError("Impossible de charger les patients. Vérifiez la connexion au serveur.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPatients();
  }, [fetchPatients]);

  const filtered = patients.filter((p) => {
    const q = search.toLowerCase();
    return (
      p.nom?.toLowerCase().includes(q) ||
      p.prenom?.toLowerCase().includes(q) ||
      p.numeroDossier?.toLowerCase().includes(q)
    );
  });

  const handleSave = async (formData: PatientFormData) => {
    if (editPatient) {
      await patientService.updatePatient(editPatient.idPatient, formData);
    } else {
      await patientService.createPatient(formData);
    }
    await fetchPatients();
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setDeleteLoading(true);
    try {
      await patientService.deletePatient(deleteTarget.idPatient);
      setDeleteTarget(null);
      await fetchPatients();
    } catch {
      alert("Erreur lors de la suppression.");
    } finally {
      setDeleteLoading(false);
    }
  };

  const nbM = patients.filter((p) => p.sexe === "M").length;
  const nbF = patients.filter((p) => p.sexe === "F").length;
  const nom = localStorage.getItem("nom") ?? "";
  const prenom = localStorage.getItem("prenom") ?? "";

  return (
    <div style={styles.page}>
      {/* En-tête */}
      <div style={styles.header}>
        <div>
          <h1 style={styles.h1}>Dashboard médecin</h1>
          <p style={styles.subtitle}>Dr. {prenom} {nom} — Gestion de vos patients</p>
        </div>
      </div>

      {/* Statistiques */}
      <div style={styles.stats}>
        <div style={styles.statCard}>
          <div style={styles.statLabel}>Total patients</div>
          <div style={styles.statValue}>{patients.length}</div>
        </div>
        <div style={styles.statCard}>
          <div style={styles.statLabel}>Hommes</div>
          <div style={styles.statValue}>{nbM}</div>
        </div>
        <div style={styles.statCard}>
          <div style={styles.statLabel}>Femmes</div>
          <div style={styles.statValue}>{nbF}</div>
        </div>
      </div>

      {/* Barre d'outils */}
      <div style={styles.toolbar}>
        <input
          style={styles.searchInput}
          type="text"
          placeholder="Rechercher par nom, prénom ou n° dossier..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button
          style={styles.addBtn}
          onClick={() => { setEditPatient(null); setShowModal(true); }}
        >
          + Ajouter un patient
        </button>
      </div>

      {/* Erreur */}
      {error && <div style={styles.errorBox}>{error}</div>}

      {/* Tableau */}
      <div style={styles.tableWrapper}>
        {loading ? (
          <div style={styles.center}>Chargement...</div>
        ) : filtered.length === 0 ? (
          <div style={styles.center}>Aucun patient trouvé.</div>
        ) : (
          <table style={styles.table}>
            <thead>
              <tr>
                {["N° dossier", "Nom complet", "Date naissance", "Sexe", "Téléphone", "Groupe sanguin", "Actions"].map((h) => (
                  <th key={h} style={styles.th}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtered.map((p) => (
                <tr key={p.idPatient} style={styles.tr}>
                  <td style={styles.td}>{p.numeroDossier}</td>
                  <td style={styles.td}>{p.nom} {p.prenom}</td>
                  <td style={styles.td}>{p.dateNaissance || "—"}</td>
                  <td style={styles.td}>
                    <span style={p.sexe === "M" ? styles.badgeM : styles.badgeF}>
                      {p.sexe === "M" ? "Homme" : p.sexe === "F" ? "Femme" : "—"}
                    </span>
                  </td>
                  <td style={styles.td}>{p.telephone || "—"}</td>
                  <td style={styles.td}>{p.groupeSanguin || "—"}</td>
                  <td style={styles.td}>
                    <div style={styles.actions}>
                      <button
                        style={styles.editBtn}
                        onClick={() => { setEditPatient(p); setShowModal(true); }}
                      >
                        ✏️ Modifier
                      </button>
                      <button
                        style={styles.delBtnRow}
                        onClick={() => setDeleteTarget(p)}
                      >
                        🗑️
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Modal Ajouter / Modifier */}
      {showModal && (
        <PatientModal
          patient={editPatient}
          medecinId={0}
          onClose={() => setShowModal(false)}
          onSave={handleSave}
        />
      )}

      {/* Modal Suppression */}
      {deleteTarget && (
        <DeleteConfirmModal
          patient={deleteTarget}
          loading={deleteLoading}
          onClose={() => setDeleteTarget(null)}
          onConfirm={handleDelete}
        />
      )}
    </div>
  );
}

const styles: Record<string, CSSProperties> = {
  page: { padding: "2rem", fontFamily: "sans-serif", maxWidth: 1100, margin: "0 auto" },
  header: { display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "1.5rem" },
  h1: { fontSize: 22, fontWeight: 600, color: "#111", margin: 0 },
  subtitle: { fontSize: 14, color: "#666", marginTop: 4 },
  stats: { display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 12, marginBottom: "1.5rem" },
  statCard: { background: "#f5f5f5", borderRadius: 10, padding: "14px 18px" },
  statLabel: { fontSize: 12, color: "#888", marginBottom: 4 },
  statValue: { fontSize: 24, fontWeight: 600, color: "#111" },
  toolbar: { display: "flex", gap: 12, marginBottom: "1rem" },
  searchInput: {
    flex: 1, padding: "9px 14px", border: "1px solid #ddd",
    borderRadius: 8, fontSize: 14, outline: "none",
  },
  addBtn: {
    background: "#185FA5", color: "#fff", border: "none",
    borderRadius: 8, padding: "9px 18px", fontSize: 14, cursor: "pointer", whiteSpace: "nowrap",
  },
  errorBox: { background: "#FCEBEB", color: "#A32D2D", borderRadius: 8, padding: "10px 14px", fontSize: 13, marginBottom: 12 },
  tableWrapper: { overflowX: "auto", border: "1px solid #eee", borderRadius: 10 },
  table: { width: "100%", borderCollapse: "collapse", fontSize: 13 },
  th: {
    textAlign: "left", padding: "10px 12px", background: "#fafafa",
    color: "#888", fontWeight: 500, borderBottom: "1px solid #eee", fontSize: 12,
  },
  tr: { borderBottom: "1px solid #f0f0f0" },
  td: { padding: "10px 12px", color: "#222" },
  badgeM: { background: "#E6F1FB", color: "#0C447C", borderRadius: 99, padding: "2px 10px", fontSize: 11, fontWeight: 500 },
  badgeF: { background: "#FBEAF0", color: "#72243E", borderRadius: 99, padding: "2px 10px", fontSize: 11, fontWeight: 500 },
  actions: { display: "flex", gap: 6, alignItems: "center" },
  editBtn: {
    background: "none", border: "1px solid #ddd", borderRadius: 7,
    padding: "5px 10px", fontSize: 12, cursor: "pointer", color: "#333",
  },
  delBtnRow: {
    background: "none", border: "1px solid #f09595", borderRadius: 7,
    padding: "5px 9px", fontSize: 13, cursor: "pointer", color: "#A32D2D",
  },
  center: { padding: "2rem", textAlign: "center", color: "#888" },
};