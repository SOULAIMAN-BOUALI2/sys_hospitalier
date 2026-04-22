import { useNavigate } from "react-router-dom";

export default function DashboardMedecin() {
  const navigate = useNavigate();
  const nom = localStorage.getItem("nom");
  const prenom = localStorage.getItem("prenom");

  return (
    <div className="min-h-screen bg-slate-950 text-white p-8">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">🩺 Dashboard Médecin</h1>
          <button onClick={() => { localStorage.clear(); navigate("/login"); }}
            className="bg-red-600 px-4 py-2 rounded-lg hover:bg-red-700">
            Déconnexion
          </button>
        </div>
        <p className="text-slate-400 mb-8">Bienvenue Dr. {prenom} {nom}</p>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-slate-800 rounded-xl p-6">
            <h2 className="text-xl font-semibold mb-2">🗓️ Mes consultations</h2>
            <p className="text-slate-400">Voir mes rendez-vous</p>
          </div>
          <div className="bg-slate-800 rounded-xl p-6">
            <h2 className="text-xl font-semibold mb-2">👥 Mes patients</h2>
            <p className="text-slate-400">Gérer mes patients</p>
          </div>
        </div>
      </div>
    </div>
  );
}