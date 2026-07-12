import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useUsuario } from '../context/UsuarioContext'
import { api } from '../api/client'

export default function Notificaciones() {
  const { usuario } = useUsuario()
  const [notificaciones, setNotificaciones] = useState([])
  const [cargando, setCargando] = useState(true)

  function cargar() {
    return api.listarNotificaciones(usuario).then(setNotificaciones)
  }

  useEffect(() => {
    setCargando(true)
    cargar().finally(() => setCargando(false))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [usuario])

  async function marcarLeida(id) {
    await api.marcarNotificacionLeida(usuario, id)
    await cargar()
  }

  return (
    <div>
      <h1 className="text-xl font-semibold text-slate-900 mb-4">Notificaciones de {usuario}</h1>

      {cargando ? (
        <p className="text-slate-500 text-sm">Cargando...</p>
      ) : notificaciones.length === 0 ? (
        <p className="text-slate-500 text-sm">No tenés notificaciones.</p>
      ) : (
        <ul className="divide-y divide-slate-200 border border-slate-200 rounded-md">
          {notificaciones.map((n) => (
            <li key={n.id} className={`px-4 py-3 flex items-center justify-between ${n.leida ? 'opacity-60' : ''}`}>
              <div>
                <p className="text-slate-900">{n.mensaje}</p>
                <Link to={`/solicitudes/${n.solicitudId}`} className="text-sm text-blue-600 hover:underline">
                  Ver solicitud
                </Link>
              </div>
              {!n.leida && (
                <button
                  onClick={() => marcarLeida(n.id)}
                  className="text-xs font-medium text-slate-600 border border-slate-300 rounded-md px-2 py-1 hover:bg-slate-100"
                >
                  Marcar leída
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}
