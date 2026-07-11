import { NavLink, Outlet } from 'react-router-dom'
import { USUARIOS_DISPONIBLES, useUsuario } from '../context/UsuarioContext'

const linkClase = ({ isActive }) =>
  `px-3 py-2 rounded-md text-sm font-medium ${
    isActive ? 'bg-slate-900 text-white' : 'text-slate-600 hover:bg-slate-100'
  }`

export default function Layout() {
  const { usuario, setUsuario } = useUsuario()

  return (
    <div className="min-h-screen bg-slate-50">
      <header className="bg-white border-b border-slate-200">
        <div className="max-w-4xl mx-auto px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <span className="font-semibold text-slate-900">Flujo de Aprobación</span>
            <nav className="flex gap-1 ml-6">
              <NavLink to="/" end className={linkClase}>Crear</NavLink>
              <NavLink to="/solicitudes" className={linkClase}>Solicitudes</NavLink>
              <NavLink to="/notificaciones" className={linkClase}>Notificaciones</NavLink>
            </nav>
          </div>

          <label className="text-sm text-slate-600 flex items-center gap-2">
            Usuario:
            <select
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              className="border border-slate-300 rounded-md px-2 py-1"
            >
              {USUARIOS_DISPONIBLES.map((u) => (
                <option key={u} value={u}>{u}</option>
              ))}
            </select>
          </label>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-4 py-6">
        <Outlet />
      </main>
    </div>
  )
}
