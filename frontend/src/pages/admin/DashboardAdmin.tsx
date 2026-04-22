import { useNavigate } from "react-router-dom";

export default function DashboardAdmin() {
  const navigate = useNavigate();
  const nom = localStorage.getItem("nom");
  const prenom = localStorage.getItem("prenom");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-slate-950 text-white p-8">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">🏥 Dashboard Admin</h1>
          <button onClick={handleLogout}
            className="bg-red-600 px-4 py-2 rounded-lg hover:bg-red-700">
            Déconnexion
          </button>
        </div>

        <p className="text-slate-400 mb-8">Bienvenue, {prenom} {nom}</p>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">👨‍⚕️ Médecins</h2>
            <p className="text-slate-400">Gérer les médecins</p>
          </div>
          <div className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">👩‍⚕️ Infirmiers</h2>
            <p className="text-slate-400">Gérer les infirmiers</p>
          </div>
          <div className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">➕ Créer un compte</h2>
            <p className="text-slate-400">Ajouter médecin ou infirmier</p>
          </div>
        </div>
      </div>
    </div>
  );
}