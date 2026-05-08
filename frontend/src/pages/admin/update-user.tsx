import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { ArrowLeft, Save, User, ShieldCheck } from "lucide-react"; // Optional: Icons make it look premium

export default function UpdateUserPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [form, setForm] = useState(location.state || {});

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 font-sans antialiased">
      {/* HEADER / NAVBAR */}
      <nav className="border-b border-slate-800 bg-slate-900/50 backdrop-blur-md sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button 
              onClick={() => navigate(-1)}
              className="p-2 hover:bg-slate-800 rounded-full transition-colors text-slate-400 hover:text-white"
            >
              <ArrowLeft size={20} />
            </button>
            <div>
              <h1 className="text-lg font-semibold tracking-tight">Dashboard Admin</h1>
              <p className="text-xs text-slate-500 uppercase tracking-wider font-medium">Gestion du personnel</p>
            </div>
          </div>
          
          <div className="hidden md:flex flex-col items-end">
            <div className="flex items-center gap-2">
              <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-blue-500/10 text-blue-400 border border-blue-500/20">
                {form.role}
              </span>
              <span className="text-sm font-medium text-slate-300">{form.email}</span>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-2xl mx-auto p-6 md:p-12">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-white">Modifier le profil</h2>
          <p className="text-slate-400 mt-2">Mettez à jour les informations professionnelles et les accès de l'utilisateur.</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 rounded-2xl shadow-xl overflow-hidden">
          <div className="p-8 space-y-6">
            
            {/* SECTION: IDENTITÉ */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium text-slate-400 ml-1">Nom</label>
                <input 
                  name="nom" 
                  value={form.nom} 
                  onChange={handleChange}
                  className="w-full bg-slate-800 border border-slate-700 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 transition-all"
                />
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-slate-400 ml-1">Prénom</label>
                <input 
                  name="prenom" 
                  value={form.prenom} 
                  onChange={handleChange}
                  className="w-full bg-slate-800 border border-slate-700 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 transition-all"
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-slate-400 ml-1">Adresse Email</label>
              <input 
                name="email" 
                type="email"
                value={form.email} 
                onChange={handleChange}
                className="w-full bg-slate-800 border border-slate-700 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 transition-all"
              />
            </div>

            <hr className="border-slate-800 my-2" />

            {/* SECTION: ATTRIBUTS SPÉCIFIQUES */}
            <div className="bg-slate-950/50 p-4 rounded-xl border border-slate-800/50 space-y-4">
              <div className="flex items-center gap-2 mb-2">
                <ShieldCheck size={18} className="text-blue-400" />
                <span className="text-sm font-semibold uppercase tracking-widest text-slate-500">Détails {form.role}</span>
              </div>

              {form.role === "MEDECIN" && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-xs font-bold text-slate-500 uppercase">Matricule</label>
                    <input name="matricule" value={form.matricule} onChange={handleChange} className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-2" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-xs font-bold text-slate-500 uppercase">Spécialité</label>
                    <input name="specialite" value={form.specialite} onChange={handleChange} className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-2" />
                  </div>
                </div>
              )}

              {form.role === "INFIRMIER" && (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <label className="text-xs font-bold text-slate-500 uppercase">Matricule</label>
                    <input name="matricule" value={form.matricule} onChange={handleChange} className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-2" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-xs font-bold text-slate-500 uppercase">Service</label>
                    <input name="service" value={form.service} onChange={handleChange} className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-2" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-xs font-bold text-slate-500 uppercase">Shift</label>
                    <input name="shift" value={form.shift} onChange={handleChange} className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-2" />
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* ACTION BAR */}
          <div className="bg-slate-800/50 px-8 py-4 flex items-center justify-end gap-3">
            <button 
              onClick={() => navigate(-1)}
              className="px-4 py-2 text-sm font-medium text-slate-400 hover:text-white transition-colors"
            >
              Annuler
            </button>
            <button className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-6 py-2 rounded-lg font-semibold transition-all shadow-lg shadow-blue-900/20 active:scale-95">
              <Save size={18} />
              Sauvegarder les modifications
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}