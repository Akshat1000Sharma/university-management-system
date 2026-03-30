"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Building2, Eye, EyeOff, GraduationCap, Lock, Mail } from "lucide-react";
import { useAuth, validateLogin, DEMO_ACCOUNTS, ROLE_HOME, ROLE_LABELS } from "./lib/auth";

export default function LoginPage() {
  const { login } = useAuth();
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    await new Promise((r) => setTimeout(r, 300));
    const user = validateLogin(email, password);
    if (!user) {
      setError("Invalid email or password.");
      setLoading(false);
      return;
    }
    login(user);
    router.push(ROLE_HOME[user.role]);
  };

  const fillDemo = (idx: number) => {
    setEmail(DEMO_ACCOUNTS[idx].email);
    setPassword(DEMO_ACCOUNTS[idx].password);
    setError("");
  };

  return (
    <div className="min-h-screen flex">
      {/* Left panel */}
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-indigo-600 to-indigo-800 flex-col justify-between p-12 text-white">
        <div className="flex items-center gap-3">
          <Building2 className="w-8 h-8" />
          <span className="text-xl font-bold tracking-tight">HMS</span>
        </div>
        <div>
          <h1 className="text-4xl font-bold leading-tight mb-4">
            Hall Management<br />System
          </h1>
          <p className="text-indigo-200 text-lg">
            Manage student halls, complaints, finances, and staff — all in one place.
          </p>
          <div className="mt-10 grid grid-cols-2 gap-4">
            {[
              { label: "Student Dues", desc: "Track mess charges, room rent & amenities" },
              { label: "Complaints", desc: "Raise and resolve maintenance issues" },
              { label: "Grant Management", desc: "Distribute annual grants across halls" },
              { label: "Staff Salary", desc: "Compute monthly salaries with leave deductions" },
            ].map((f) => (
              <div key={f.label} className="bg-white/10 rounded-xl p-4">
                <p className="font-semibold text-sm">{f.label}</p>
                <p className="text-indigo-200 text-xs mt-1">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
        <p className="text-indigo-300 text-sm">
          University Hall Management System &copy; 2026
        </p>
      </div>

      {/* Right panel */}
      <div className="flex-1 flex flex-col justify-center items-center p-8">
        <div className="w-full max-w-md">
          <div className="flex items-center gap-2 mb-8 lg:hidden">
            <Building2 className="w-6 h-6 text-indigo-600" />
            <span className="text-lg font-bold text-indigo-600">HMS</span>
          </div>

          <h2 className="text-2xl font-bold text-slate-200 mb-1">Sign in to HMS</h2>
          <p className="text-slate-500 mb-8">Enter your credentials to access your dashboard.</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Email address</label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="you@hms.edu"
                  required
                  className="w-full pl-9 pr-4 py-2.5 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Password</label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                <input
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  required
                  className="w-full pl-9 pr-10 py-2.5 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword((v) => !v)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 rounded-lg px-4 py-2.5 text-sm">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:opacity-60 text-white font-semibold py-2.5 rounded-lg transition-colors"
            >
              {loading ? "Signing in…" : "Sign in"}
            </button>
          </form>

          {/* Demo credentials */}
          <div className="mt-8">
            <div className="flex items-center gap-2 mb-3">
              <GraduationCap className="w-4 h-4 text-slate-400" />
              <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">
                Demo Accounts — click to fill
              </p>
            </div>
            <div className="grid grid-cols-2 gap-2">
              {DEMO_ACCOUNTS.map((acc, i) => (
                <button
                  key={acc.email}
                  onClick={() => fillDemo(i)}
                  className="text-left border border-slate-200 rounded-lg p-2.5 hover:border-indigo-300 hover:bg-indigo-50 transition-colors group"
                >
                  <p className="text-xs font-semibold text-slate-700 group-hover:text-indigo-700">
                    {ROLE_LABELS[acc.role]}
                  </p>
                  <p className="text-xs text-slate-400 mt-0.5 truncate">{acc.email}</p>
                  <p className="text-xs text-slate-400 font-mono">{acc.password}</p>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
